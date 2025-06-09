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
import javafx.util.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import proguard.classfile.ClassPool;
import proguard.classfile.Clazz;
import proguard.classfile.ProgramClass;
import proguard.classfile.editor.ClassBuilder;
import proguard.classfile.util.ClassInitializer;
import proguard.classfile.util.ClassUtil;
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
        Pair<ClassPool, ClassPool> classPool =
                ClassPoolBuilder.from(programClass, programClass1, programClass2, programClass3);

        classPool.getKey().accept(new ClassFileRelationshipIdentifier(new HashMap<>()));

        Clazz clazz = classPool.getKey().getClass("Dependent");
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

        Pair<ClassPool, ClassPool> classPool =
                ClassPoolBuilder.from(programClass, programClass1, programClass2);

        classPool.getKey().accept(new ClassFileRelationshipIdentifier(new HashMap<>()));

        Clazz clazz = classPool.getKey().getClass("Dependent");
        ClassFileRelationshipIdentifier.MyProcessingInfo processingInfo =
                (ClassFileRelationshipIdentifier.MyProcessingInfo) clazz.getProcessingInfo();
        ListUtils.assertListsEqual(
                new ArrayList<>(processingInfo.associations),
                Arrays.asList(
                        classPool
                                .getValue()
                                .getClass(ClassUtil.internalClassName(NAME_JAVA_UTIL_LIST)),
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

        Pair<ClassPool, ClassPool> classPool =
                ClassPoolBuilder.from(programClass, programClass1, programClass2);

        classPool.getKey().accept(new ClassFileRelationshipIdentifier(new HashMap<>()));

        Clazz clazz = classPool.getKey().getClass("Dependent");
        ClassFileRelationshipIdentifier.MyProcessingInfo processingInfo =
                (ClassFileRelationshipIdentifier.MyProcessingInfo) clazz.getProcessingInfo();

        Assertions.assertEquals(programClass1, processingInfo.superClass);
        ListUtils.assertListsEqual(
                Collections.singletonList(programClass2), processingInfo.implementations);
    }

    static class ClassPoolBuilder {
        static Pair<ClassPool, ClassPool> from(Clazz... clazz) {
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

            return new Pair<>(programClassPool, libraryClassPool);
        }
    }
}
