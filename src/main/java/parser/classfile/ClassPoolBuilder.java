package parser.classfile;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.stream.Stream;
import javax.lang.model.SourceVersion;
import proguard.classfile.ClassPool;
import proguard.classfile.LibraryClass;
import proguard.classfile.util.ClassInitializer;
import proguard.classfile.util.WarningPrinter;
import proguard.classfile.visitor.ClassNameFilter;
import proguard.classfile.visitor.ClassPoolFiller;
import proguard.classfile.visitor.ClassVisitor;
import proguard.io.ClassFilter;
import proguard.io.ClassPath;
import proguard.io.ClassPathEntry;
import proguard.io.ClassReader;
import proguard.io.DataEntryReader;
import proguard.io.DataEntryReaderFactory;
import proguard.io.DataEntrySource;
import proguard.io.DirectorySource;
import proguard.io.FileDataEntry;
import proguard.io.JarReader;
import proguard.io.NameFilteredDataEntryReader;

public class ClassPoolBuilder {
    private final ClassPath classPath = new ClassPath();
    private final ClassPath libraryClassPath = new ClassPath();
    private boolean initializeReferences = false;
    private String classNameFilter;
    private boolean initializeKotlinMetadata = false;
    private BiFunction<DataEntryReader, ClassVisitor, DataEntryReader> extraDataEntryReader;
    private boolean isAndroid;
    private final ClassPool libraryClassPool = new ClassPool();
    private boolean useDefaultLibraryClasses = false;

    private static final WarningPrinter nullWarningPrinter =
            new WarningPrinter(
                    new PrintWriter(
                            new OutputStream() {
                                @Override
                                public void write(int i) {}
                            }));

    private static final WarningPrinter defaultWarningPrinter =
            new WarningPrinter(new PrintWriter(System.out));

    private WarningPrinter classReaderWarningPrinter = nullWarningPrinter;
    private WarningPrinter missingClassWarningPrinter = nullWarningPrinter;
    private WarningPrinter missingProgramMemberWarningPrinter = nullWarningPrinter;
    private WarningPrinter missingLibraryMemberWarningPrinter = nullWarningPrinter;
    private WarningPrinter dependencyWarningPrinter = nullWarningPrinter;

    public ClassPoolBuilder() {}

    public ClassPoolBuilder withDefaultLibraryClasses() {
        this.useDefaultLibraryClasses = true;
        return this;
    }

    public ClassPoolBuilder addLibraryClasses(ClassPool libraryClassPool) {
        libraryClassPool.classesAccept(new ClassPoolFiller(this.libraryClassPool));
        return this;
    }

    public ClassPoolBuilder addLibraryClass(LibraryClass... libraryClass) {
        Arrays.stream(libraryClass).forEach(libraryClassPool::addClass);
        return this;
    }

    public ClassPoolBuilder addLibraryClasses(String filePath) {
        return addLibraryClasses(new File(filePath));
    }

    public ClassPoolBuilder addLibraryClasses(File file) {
        libraryClassPath.add(new ClassPathEntry(file, false));
        return this;
    }

    public ClassPoolBuilder add(String filePath) {
        return add(new File(filePath));
    }

    public ClassPoolBuilder add(File file) {
        classPath.add(new ClassPathEntry(file, false));
        return this;
    }

    public ClassPoolBuilder withClassNameFilter(String classNameFilter) {
        this.classNameFilter = classNameFilter;
        return this;
    }

    public ClassPoolBuilder initializeReferences() {
        this.initializeReferences = true;
        return this;
    }

    public ClassPoolBuilder initializeKotlinMetadata() {
        this.initializeKotlinMetadata = true;
        return this;
    }

    public ClassPoolBuilder withClassReaderWarningPrinter(WarningPrinter warningPrinter) {
        this.classReaderWarningPrinter = warningPrinter;
        return this;
    }

    public ClassPoolBuilder withMissingClassWarningPrinter(WarningPrinter warningPrinter) {
        this.missingClassWarningPrinter = warningPrinter;
        return this;
    }

    public ClassPoolBuilder withMissingLibraryMemberWarningPrinter(WarningPrinter warningPrinter) {
        this.missingLibraryMemberWarningPrinter = warningPrinter;
        return this;
    }

    public ClassPoolBuilder withDependencyWarningPrinter(WarningPrinter warningPrinter) {
        this.dependencyWarningPrinter = warningPrinter;
        return this;
    }

    public ClassPoolBuilder withMissingProgramMemberWarningPrinter(WarningPrinter warningPrinter) {
        this.missingProgramMemberWarningPrinter = warningPrinter;
        return this;
    }

    public ClassPoolBuilder withDefaultWarningPrinter() {
        this.classReaderWarningPrinter = defaultWarningPrinter;
        this.missingClassWarningPrinter = defaultWarningPrinter;
        this.missingProgramMemberWarningPrinter = defaultWarningPrinter;
        this.missingLibraryMemberWarningPrinter = defaultWarningPrinter;
        this.dependencyWarningPrinter = defaultWarningPrinter;
        return this;
    }

    public ClassPoolBuilder setAndroid(boolean isAndroid) {
        this.isAndroid = isAndroid;
        return this;
    }

    public ClassPoolBuilder withExtraDataEntryReaderFactory(
            BiFunction<DataEntryReader, ClassVisitor, DataEntryReader> extraDataEntryReader) {
        this.extraDataEntryReader = extraDataEntryReader;
        return this;
    }

    public ClassPools build() throws IOException {
        ClassPool libraryClassPool =
                useDefaultLibraryClasses
                        ? createDefaultLibraryClassPool(this.libraryClassPool)
                        : createLibraryClassPool();

        ClassPool programClassPool = new ClassPool();
        ClassVisitor classPoolFiller = new ClassPoolFiller(programClassPool);

        if (classNameFilter != null) {
            classPoolFiller = new ClassNameFilter(classNameFilter, classPoolFiller);
        }

        DataEntryReader classReader =
                new NameFilteredDataEntryReader(
                        "**.class",
                        new ClassReader(
                                false,
                                false,
                                false,
                                false,
                                initializeKotlinMetadata,
                                classReaderWarningPrinter,
                                classPoolFiller));

        if (extraDataEntryReader != null) {
            classReader = extraDataEntryReader.apply(classReader, classPoolFiller);
        }

        for (int index = 0; index < classPath.size(); index++) {
            ClassPathEntry entry = classPath.get(index);
            if (!entry.isOutput()) {
                try {
                    // Create a reader that can unwrap jars, wars, ears, jmods and zips.
                    DataEntryReader reader =
                            new DataEntryReaderFactory(isAndroid)
                                    .createDataEntryReader(entry, classReader);

                    // Create the data entry source.
                    DataEntrySource source = new DirectorySource(entry.getFile());

                    // Pump the data entries into the reader.
                    source.pumpDataEntries(reader);
                } catch (IOException ex) {
                    throw new IOException(
                            "Can't read [" + entry + "] (" + ex.getMessage() + ")", ex);
                }
            }
        }

        if (initializeReferences) {
            ClassInitializer classInitializer =
                    new ClassInitializer(
                            programClassPool,
                            libraryClassPool,
                            missingClassWarningPrinter,
                            missingProgramMemberWarningPrinter,
                            missingLibraryMemberWarningPrinter,
                            dependencyWarningPrinter);
            libraryClassPool.classesAccept(classInitializer);
            programClassPool.classesAccept(classInitializer);
        }

        return new ClassPools(programClassPool, libraryClassPool);
    }

    private ClassPool createLibraryClassPool() throws IOException {
        DataEntryReader classReader =
                new NameFilteredDataEntryReader(
                        "**.class",
                        new ClassReader(
                                true,
                                false,
                                false,
                                false,
                                initializeKotlinMetadata,
                                classReaderWarningPrinter,
                                new ClassPoolFiller(libraryClassPool)));

        for (int index = 0; index < libraryClassPath.size(); index++) {
            ClassPathEntry entry = libraryClassPath.get(index);
            if (!entry.isOutput()) {
                try {
                    // Create a reader that can unwrap jars, wars, ears, jmods and zips.
                    DataEntryReader reader =
                            new DataEntryReaderFactory(isAndroid)
                                    .createDataEntryReader(entry, classReader);

                    // Create the data entry source.
                    DataEntrySource source = new DirectorySource(entry.getFile());

                    // Pump the data entries into the reader.
                    source.pumpDataEntries(reader);
                } catch (IOException ex) {
                    throw new IOException(
                            "Can't read [" + entry + "] (" + ex.getMessage() + ")", ex);
                }
            }
        }
        return libraryClassPool;
    }

    public ClassPool createDefaultLibraryClassPool(ClassPool libraryClassPool) throws IOException {
        File jdkHome = getCurrentJavaHome();
        if (libraryClassPool == null) libraryClassPool = new ClassPool();
        ClassReader classReader =
                new ClassReader(
                        true,
                        false,
                        false,
                        false,
                        initializeKotlinMetadata,
                        classReaderWarningPrinter,
                        new ClassPoolFiller(libraryClassPool));

        if (jdkHome.exists()) {
            Path jmods = jdkHome.toPath().resolve("jmods");
            if (jmods.toFile().exists()) {
                try (Stream<Path> paths = Files.walk(jmods)) {
                    paths.filter(it -> it.getFileName().toString().equals("java.base.jmod"))
                            .forEach(
                                    it -> {
                                        try {
                                            new JarReader(new ClassFilter(classReader))
                                                    .read(new FileDataEntry(it.toFile()));
                                        } catch (IOException e) {
                                            throw new RuntimeException(e);
                                        }
                                    });
                }
            } else {
                Path jre = jdkHome.toPath().resolve("jre");
                Path runtimeJar =
                        jre.toFile().exists()
                                ? jre.resolve("lib").resolve("rt.jar")
                                : jdkHome.toPath().resolve("lib").resolve("rt.jar");

                new JarReader(new ClassFilter(classReader))
                        .read(new FileDataEntry(runtimeJar.toFile()));
            }
        }

        return libraryClassPool;
    }

    private static File getCurrentJavaHome() {
        return isJava9OrLater()
                ? new File(System.getProperty("java.home"))
                : new File(System.getProperty("java.home")).getParentFile();
    }

    private static boolean isJava9OrLater() {
        return SourceVersion.latestSupported().compareTo(SourceVersion.RELEASE_8) > 0;
    }

    public record ClassPools(ClassPool programClassPool, ClassPool libraryClassPool) {}
}
