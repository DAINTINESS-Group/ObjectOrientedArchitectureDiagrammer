package parser.classfile;

import static proguard.classfile.AccessConstants.PUBLIC;
import static proguard.classfile.ClassConstants.METHOD_NAME_INIT;
import static proguard.classfile.ClassConstants.METHOD_TYPE_INIT;
import static proguard.classfile.ClassConstants.NAME_JAVA_LANG_OBJECT;
import static proguard.classfile.ClassConstants.NAME_JAVA_UTIL_LIST;
import static proguard.classfile.VersionConstants.CLASS_VERSION_1_6;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import proguard.classfile.ClassPool;
import proguard.classfile.Clazz;
import proguard.classfile.ProgramClass;
import proguard.classfile.editor.ClassBuilder;
import proguard.classfile.util.ClassInitializer;
import proguard.classfile.util.ClassUtil;
import proguard.classfile.visitor.AllClassVisitor;
import proguard.classfile.visitor.MultiClassPoolVisitor;
import utils.ListUtils;

public class ClassFileParserTest {

    @Test
    public void dependenciesTest() {
        ProgramClass programClass =
                new ClassBuilder(CLASS_VERSION_1_6, PUBLIC, "Dependent", NAME_JAVA_LANG_OBJECT)
                        .addMethod(
                                PUBLIC,
                                "foo",
                                "(LDependentUpon;)LDependentUpon2;",
                                10,
                                ____ ->
                                        ____.new_("DependentUpon3")
                                                .dup()
                                                .invokespecial(
                                                        "DependentUpon3",
                                                        METHOD_NAME_INIT,
                                                        METHOD_TYPE_INIT)
                                                .astore_1()
                                                .return_())
                        .getProgramClass();

        ProgramClass programClass1 =
                new ClassBuilder(CLASS_VERSION_1_6, PUBLIC, "DependentUpon", NAME_JAVA_LANG_OBJECT)
                        .getProgramClass();
        ProgramClass programClass2 =
                new ClassBuilder(CLASS_VERSION_1_6, PUBLIC, "DependentUpon2", NAME_JAVA_LANG_OBJECT)
                        .getProgramClass();
        ProgramClass programClass3 =
                new ClassBuilder(CLASS_VERSION_1_6, PUBLIC, "DependentUpon3", NAME_JAVA_LANG_OBJECT)
                        .addMethod(PUBLIC, METHOD_NAME_INIT, METHOD_TYPE_INIT)
                        .getProgramClass();
        ClassPools classPools =
                ClassPoolBuilder.from(programClass, programClass1, programClass2, programClass3);

        classPools.programClassPool.accept(new ClassFileRelationshipIdentifier(new HashMap<>()));

        Clazz clazz = classPools.programClassPool.getClass("Dependent");
        ClassFileRelationshipIdentifier.MyProcessingInfo processingInfo =
                (ClassFileRelationshipIdentifier.MyProcessingInfo) clazz.getProcessingInfo();
        ListUtils.assertListsEqual(
                new ArrayList<>(processingInfo.dependencies),
                Arrays.asList(programClass1, programClass2, programClass3));
    }

    @Test
    public void associationTest() {
        ProgramClass programClass =
                new ClassBuilder(CLASS_VERSION_1_6, PUBLIC, "Dependent", NAME_JAVA_LANG_OBJECT)
                        .addField(PUBLIC, "foo", "LDependentUpon;")
                        .addField(PUBLIC, "bar", "Ljava/util/List<LDependentUpon2;>;")
                        .getProgramClass();

        ProgramClass programClass1 =
                new ClassBuilder(CLASS_VERSION_1_6, PUBLIC, "DependentUpon", NAME_JAVA_LANG_OBJECT)
                        .getProgramClass();

        ProgramClass programClass2 =
                new ClassBuilder(CLASS_VERSION_1_6, PUBLIC, "DependentUpon2", NAME_JAVA_LANG_OBJECT)
                        .getProgramClass();

        ClassPools classPools = ClassPoolBuilder.from(programClass, programClass1, programClass2);

        classPools.programClassPool.accept(new ClassFileRelationshipIdentifier(new HashMap<>()));

        Clazz clazz = classPools.programClassPool.getClass("Dependent");
        ClassFileRelationshipIdentifier.MyProcessingInfo processingInfo =
                (ClassFileRelationshipIdentifier.MyProcessingInfo) clazz.getProcessingInfo();
        ListUtils.assertListsEqual(
                new ArrayList<>(processingInfo.associations),
                Arrays.asList(
                        classPools.libraryClassPool.getClass(
                                ClassUtil.internalClassName(NAME_JAVA_UTIL_LIST)),
                        programClass1));
    }

    @Test
    public void inheritanceTest() {
        ProgramClass programClass =
                new ClassBuilder(CLASS_VERSION_1_6, PUBLIC, "Dependent", "Parent")
                        .addInterface("LDependentUpon;")
                        .getProgramClass();

        ProgramClass programClass1 =
                new ClassBuilder(CLASS_VERSION_1_6, PUBLIC, "Parent", NAME_JAVA_LANG_OBJECT)
                        .getProgramClass();

        ProgramClass programClass2 =
                new ClassBuilder(CLASS_VERSION_1_6, PUBLIC, "DependentUpon", NAME_JAVA_LANG_OBJECT)
                        .getProgramClass();

        ClassPools classPools = ClassPoolBuilder.from(programClass, programClass1, programClass2);

        classPools.programClassPool.accept(new ClassFileRelationshipIdentifier(new HashMap<>()));

        Clazz clazz = classPools.programClassPool.getClass("Dependent");
        ClassFileRelationshipIdentifier.MyProcessingInfo processingInfo =
                (ClassFileRelationshipIdentifier.MyProcessingInfo) clazz.getProcessingInfo();

        Assertions.assertEquals(programClass1, processingInfo.superClass);
        ListUtils.assertListsEqual(
                Collections.singletonList(programClass2), processingInfo.implementations);
    }

    @Test
    public void myTest() {
        ProgramClass programClass =
                new ClassBuilder(CLASS_VERSION_1_6, PUBLIC, "a", NAME_JAVA_LANG_OBJECT)
                        .getProgramClass();
        ProgramClass programClass1 =
                new ClassBuilder(CLASS_VERSION_1_6, PUBLIC, "a/a", NAME_JAVA_LANG_OBJECT)
                        .getProgramClass();
        ProgramClass programClass2 =
                new ClassBuilder(CLASS_VERSION_1_6, PUBLIC, "a/b", NAME_JAVA_LANG_OBJECT)
                        .getProgramClass();
        ClassPools classPools = ClassPoolBuilder.from(programClass, programClass1, programClass2);

        HashMap<String, List<Clazz>> packages = new HashMap<>();
        classPools.programClassPool.accept(
                new MultiClassPoolVisitor(
                        new ClassFileRelationshipIdentifier(packages),
                        new AllClassVisitor(
                                new ClassFileRelationshipCreator(
                                        new ArrayList<>(), new ArrayList<>(), packages))));
    }

    static class ClassPoolBuilder {
        static ClassPools from(Clazz... clazz) {
            parser.classfile.ClassPoolBuilder classPoolBuilder =
                    new parser.classfile.ClassPoolBuilder();
            ClassPool libraryClassPool = new ClassPool();
            try {
                classPoolBuilder.createDefaultLibraryClassPool(libraryClassPool);
            } catch (IOException ignored) {
            }

            ClassPool programClassPool = new ClassPool(clazz);
            programClassPool.classesAccept(
                    new ClassInitializer(programClassPool, libraryClassPool));

            return new ClassPools(programClassPool, libraryClassPool);
        }
    }

    record ClassPools(ClassPool programClassPool, ClassPool libraryClassPool) {}
}
