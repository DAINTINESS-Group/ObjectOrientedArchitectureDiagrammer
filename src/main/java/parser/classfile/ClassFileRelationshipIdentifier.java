package parser.classfile;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import proguard.classfile.ClassPool;
import proguard.classfile.Clazz;
import proguard.classfile.Member;
import proguard.classfile.Method;
import proguard.classfile.ProgramClass;
import proguard.classfile.ProgramField;
import proguard.classfile.ProgramMethod;
import proguard.classfile.attribute.Attribute;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.attribute.LocalVariableTableAttribute;
import proguard.classfile.attribute.LocalVariableTypeTableAttribute;
import proguard.classfile.attribute.visitor.AttributeVisitor;
import proguard.classfile.attribute.visitor.LocalVariableInfoVisitor;
import proguard.classfile.attribute.visitor.LocalVariableTypeInfoVisitor;
import proguard.classfile.attribute.visitor.MultiAttributeVisitor;
import proguard.classfile.instruction.ConstantInstruction;
import proguard.classfile.instruction.Instruction;
import proguard.classfile.instruction.visitor.InstructionVisitor;
import proguard.classfile.util.ClassUtil;
import proguard.classfile.visitor.AllMemberVisitor;
import proguard.classfile.visitor.ClassCleaner;
import proguard.classfile.visitor.ClassCollector;
import proguard.classfile.visitor.ClassPoolVisitor;
import proguard.classfile.visitor.ClassVisitor;
import proguard.classfile.visitor.MemberVisitor;
import proguard.classfile.visitor.MultiClassVisitor;
import proguard.classfile.visitor.ProcessingInfoSetter;
import proguard.classfile.visitor.ProgramMemberFilter;
import proguard.classfile.visitor.ReferencedClassVisitor;
import proguard.classfile.visitor.SignatureAttributeReferencedClassVisitor;

public class ClassFileRelationshipIdentifier
        implements ClassPoolVisitor,
                ClassVisitor,
                MemberVisitor,
                AttributeVisitor,
                InstructionVisitor {
    public final Map<String, List<Clazz>> packages;

    private MyProcessingInfo processingInfo;
    private ClassCollector dependenciesCollector;

    public ClassFileRelationshipIdentifier(Map<String, List<Clazz>> packages) {
        this.packages = packages;
    }

    // Implementations for ClassPoolVisitor.

    @Override
    public void visitClassPool(ClassPool classPool) {
        classPool.classesAccept(new MultiClassVisitor(new ClassCleaner(), this));
    }

    // Implementations for ClassVisitor.

    @Override
    public void visitAnyClass(Clazz clazz) {}

    @Override
    public void visitProgramClass(ProgramClass programClass) {
        // Remember the package name.
        packages.computeIfAbsent(
                        ClassUtil.internalPackageName(programClass.getName()),
                        k -> new ArrayList<>())
                .add(programClass);

        // Initialize and remember the processing info of the class that we are visiting.
        programClass.accept(new ProcessingInfoSetter(new MyProcessingInfo()));
        processingInfo = (MyProcessingInfo) programClass.getProcessingInfo();
        dependenciesCollector = new ClassCollector(processingInfo.dependencies);

        // The super class might not be initialized if the referenced class
        // is neither the program class pool nor the library class pool.
        processingInfo.superClass = programClass.getSuperClass();
        programClass.interfaceConstantsAccept(
                new ReferencedClassVisitor(new ClassCollector(processingInfo.implementations)));

        // Identify the rest of the relationships, namely dependencies, associations and
        // aggregations.
        programClass.accept(new AllMemberVisitor(new ProgramMemberFilter(this)));
    }

    // Implementations for MemberVisitor.

    @Override
    public void visitAnyMember(Clazz clazz, Member member) {}

    @Override
    public void visitProgramMethod(ProgramClass programClass, ProgramMethod programMethod) {
        // Collect all dependencies. First check the parameters and the return type of the method.
        programMethod.referencedClassesAccept(dependenciesCollector);
        // Then, we need to check its local variables in the local variable table attribute, if
        // present,
        // and then go over the instructions of the program method that we are visiting.
        programMethod.attributesAccept(programClass, this);
    }

    @Override
    public void visitProgramField(ProgramClass programClass, ProgramField programField) {
        // First, check the referenced class to identify an association relationship.
        Clazz referencedClass = programField.referencedClass;
        if (referencedClass != null) {
            processingInfo.associations.add(referencedClass);
        }

        // Then, we check for an aggregation; this is a special type of association.
        // The signature attribute is an optional attribute but if present,
        // we will collect the classes referenced in the field signature.
        programField.attributesAccept(
                programClass,
                new SignatureAttributeReferencedClassVisitor(
                        new ClassCollector(processingInfo.aggregations)));
    }

    // Implementations for AttributeVisitor.

    @Override
    public void visitAnyAttribute(Clazz clazz, Attribute attribute) {}

    @Override
    public void visitCodeAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute) {
        ReferencedClassVisitor referencedClassVisitor =
                new ReferencedClassVisitor(dependenciesCollector);
        codeAttribute.attributesAccept(
                clazz,
                method,
                new MultiAttributeVisitor(
                        new AllVariableInfoVisitor(referencedClassVisitor),
                        new AllVariableTypeInfoVisitor(referencedClassVisitor)));

        // The local variable table and type attributes are optional attributes so we
        // will also go over the instructions of the code attribute to check for
        // the initialization of variables.
        codeAttribute.instructionsAccept(clazz, method, this);
    }

    // Implementations for InstructionVisitor.

    @Override
    public void visitAnyInstruction(
            Clazz clazz,
            Method method,
            CodeAttribute codeAttribute,
            int offset,
            Instruction instruction) {}

    @Override
    public void visitConstantInstruction(
            Clazz clazz,
            Method method,
            CodeAttribute codeAttribute,
            int offset,
            ConstantInstruction constantInstruction) {
        if (constantInstruction.opcode == Instruction.OP_NEW) {
            clazz.constantPoolEntryAccept(
                    constantInstruction.constantIndex,
                    new ReferencedClassVisitor(dependenciesCollector));
        }
    }

    // Helper classes.

    /**
     * This {@link AttributeVisitor} lets a given {@link LocalVariableInfoVisitor} visit all local
     * variables of the {@link LocalVariableTableAttribute} that it visits.
     */
    static class AllVariableInfoVisitor implements AttributeVisitor {

        private final LocalVariableInfoVisitor localVariableInfoVisitor;

        public AllVariableInfoVisitor(LocalVariableInfoVisitor localVariableInfoVisitor) {
            this.localVariableInfoVisitor = localVariableInfoVisitor;
        }

        // Implementations for AttributeVisitor.

        @Override
        public void visitAnyAttribute(Clazz clazz, Attribute attribute) {}

        @Override
        public void visitLocalVariableTableAttribute(
                Clazz clazz,
                Method method,
                CodeAttribute codeAttribute,
                LocalVariableTableAttribute localVariableTableAttribute) {
            localVariableTableAttribute.localVariablesAccept(
                    clazz, method, codeAttribute, localVariableInfoVisitor);
        }
    }

    /**
     * This {@link AttributeVisitor} lets a given {@link LocalVariableTypeInfoVisitor} visit all
     * local variables of the {@link LocalVariableTypeTableAttribute} that it visits.
     */
    static class AllVariableTypeInfoVisitor implements AttributeVisitor {

        private final LocalVariableTypeInfoVisitor localVariableTypeInfoVisitor;

        public AllVariableTypeInfoVisitor(
                LocalVariableTypeInfoVisitor localVariableTypeInfoVisitor) {
            this.localVariableTypeInfoVisitor = localVariableTypeInfoVisitor;
        }

        // Implementations for AttributeVisitor.

        @Override
        public void visitAnyAttribute(Clazz clazz, Attribute attribute) {}

        @Override
        public void visitLocalVariableTypeTableAttribute(
                Clazz clazz,
                Method method,
                CodeAttribute codeAttribute,
                LocalVariableTypeTableAttribute localVariableTypeTableAttribute) {
            localVariableTypeTableAttribute.localVariablesAccept(
                    clazz, method, codeAttribute, localVariableTypeInfoVisitor);
        }
    }

    static class MyProcessingInfo {
        Set<Clazz> dependencies = new HashSet<>();
        Set<Clazz> associations = new HashSet<>();
        Set<Clazz> aggregations = new HashSet<>();
        Set<Clazz> implementations = new HashSet<>();
        Clazz superClass;

        boolean wasMarked() {
            return !dependencies.isEmpty()
                    || !associations.isEmpty()
                    || !aggregations.isEmpty()
                    || !implementations.isEmpty()
                    || superClass != null;
        }
    }
}
