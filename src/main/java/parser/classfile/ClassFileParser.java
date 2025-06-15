package parser.classfile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import model.graph.ClassifierVertex;
import model.graph.PackageVertex;
import proguard.classfile.ClassPool;
import proguard.classfile.Clazz;
import proguard.classfile.util.ClassInitializer;
import proguard.classfile.visitor.AllClassVisitor;
import proguard.classfile.visitor.ClassVisitor;
import proguard.classfile.visitor.MultiClassPoolVisitor;
import proguard.io.DataEntryReader;
import proguard.io.DexClassReader;
import proguard.io.NameFilteredDataEntryReader;
import proguard.io.util.IOUtil;

public class ClassFileParser {

    private final Map<String, Set<Clazz>> packages = new HashMap<>();

    public ClassPool programClassPool;
    public ClassPool libraryClassPool;

    /**
     * Parses the given input and initializes the program class pool. Program classes will be marked
     * with the necessary information that will allow us to create a diagrams.
     *
     * @param path Path to the input, can be
     *     <ul>
     *       <li>{@code .class} files
     *       <li>jars
     *       <li>zips
     *       <li>mods
     *       <li>aars
     *       <li>apks
     *       <li>{@code .dex} files
     *     </ul>
     */
    public void parsePackage(Path path) {
        try {
            // Read in the program class pool.
            BiFunction<DataEntryReader, ClassVisitor, DataEntryReader> dexConverter =
                    (dataEntryReader, classPoolFiller) ->
                            new NameFilteredDataEntryReader(
                                    "classes*.dex",
                                    new DexClassReader(true, classPoolFiller),
                                    dataEntryReader);
            programClassPool = IOUtil.read(path.toFile(), false, true, dexConverter);

            // Create a default library class pool by going through disk.
            libraryClassPool = new ClassPool();
            try {
                ClassPoolBuilder classPoolBuilder = new ClassPoolBuilder();
                classPoolBuilder.createDefaultLibraryClassPool(libraryClassPool);
            } catch (IOException ignored) {
            }

            // Now mark the classes of the class pool with the necessary info that
            // is needed in order to create our graph.
            ClassFileRelationshipIdentifier relationshipIdentifier =
                    new ClassFileRelationshipIdentifier(packages);
            programClassPool.accept(
                    new MultiClassPoolVisitor(
                            new AllClassVisitor(
                                    new ClassInitializer(programClassPool, libraryClassPool)),
                            relationshipIdentifier));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Reads the processing info that were previously set to the program classes and fills the given
     * collections.
     */
    public void createRelationships(
            Collection<ClassifierVertex> classifierVertices,
            Collection<PackageVertex> packageVertices) {
        programClassPool.accept(
                new ClassFileRelationshipCreator(classifierVertices, packageVertices, packages));
    }

    /** For testing purposes only. */
    public static void main(String... args) {
        try {
            BiFunction<DataEntryReader, ClassVisitor, DataEntryReader> dexConverter =
                    (dataEntryReader, classPoolFiller) ->
                            new NameFilteredDataEntryReader(
                                    "classes*.dex",
                                    new DexClassReader(true, classPoolFiller),
                                    dataEntryReader);

            ClassPoolBuilder.ClassPools classPools =
                    new ClassPoolBuilder()
                            .add(args[0])
                            .initializeReferences()
                            .initializeKotlinMetadata()
                            .withDefaultWarningPrinter()
                            .withDefaultLibraryClasses()
                            .withExtraDataEntryReaderFactory(dexConverter)
                            .build();
            ClassPool programClassPool = classPools.programClassPool();
            ClassPool libraryClassPool = classPools.libraryClassPool();

            ClassPool classPool = IOUtil.read(new File(args[0]), false, true, dexConverter);
            classPool.accept(
                    new MultiClassPoolVisitor(
                            new AllClassVisitor(new ClassInitializer(classPool, new ClassPool())),
                            new ClassFileRelationshipIdentifier(Collections.emptyMap())));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
