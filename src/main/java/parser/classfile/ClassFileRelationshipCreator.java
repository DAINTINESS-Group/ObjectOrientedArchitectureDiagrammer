package parser.classfile;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.graph.Arc;
import model.graph.ArcType;
import model.graph.ClassifierVertex;
import model.graph.PackageVertex;
import model.graph.VertexType;
import proguard.classfile.Clazz;
import proguard.classfile.Member;
import proguard.classfile.ProgramClass;
import proguard.classfile.ProgramField;
import proguard.classfile.ProgramMethod;
import proguard.classfile.util.ClassUtil;
import proguard.classfile.visitor.AllMemberVisitor;
import proguard.classfile.visitor.ClassVisitor;
import proguard.classfile.visitor.MemberVisitor;

public class ClassFileRelationshipCreator implements ClassVisitor {
    private final Map<String, ClassifierVertex> seenClassifierVertices = new HashMap<>();
    private final Map<String, PackageVertex> seenPackageVertices = new HashMap<>();

    private final List<ClassifierVertex> classifierVertices;
    private final List<PackageVertex> packageVertices;
    private final Map<String, List<Clazz>> packages;

    public ClassFileRelationshipCreator(
            List<ClassifierVertex> classifierVertices,
            List<PackageVertex> packageVertices,
            Map<String, List<Clazz>> packages) {
        this.classifierVertices = classifierVertices;
        this.packageVertices = packageVertices;
        this.packages = packages;
    }

    // Implementations for ClassVisitor.

    @Override
    public void visitAnyClass(Clazz clazz) {}

    @Override
    public void visitProgramClass(ProgramClass programClass) {
        ClassifierVertex sourceVertex = seenClassifierVertices.get(programClass.getName());
        if (sourceVertex == null) {
            sourceVertex = createClassifierVertex(programClass);
            seenClassifierVertices.put(sourceVertex.getName(), sourceVertex);
            classifierVertices.add(sourceVertex);
        }

        createClassifierVertexRelationships(programClass, sourceVertex);

        String packageName = ClassUtil.internalPackageName(programClass.getName());
        List<Clazz> children = requireNonNull(packages.get(packageName));
        PackageVertex packageVertex = seenPackageVertices.get(packageName);
        if (packageVertex == null) {
            packageVertex =
                    new PackageVertex.PackageVertexBuilder()
                            .withSinkVertices(getOrCreateSinkVertices(children))
                            .withName(packageName)
                            .build();
            seenPackageVertices.put(packageName, packageVertex);
            packageVertices.add(packageVertex);
        }

        createPackageVertexRelationships(programClass, children, packageVertex);
    }

    // Helper classes.

    static class ClassifierVertexMemberCreator implements MemberVisitor {
        private final List<ClassifierVertex.Method> methods;
        private final List<ClassifierVertex.Field> fields;

        public ClassifierVertexMemberCreator(
                List<ClassifierVertex.Method> methods, List<ClassifierVertex.Field> fields) {
            this.methods = methods;
            this.fields = fields;
        }

        @Override
        public void visitAnyMember(Clazz clazz, Member member) {}

        @Override
        public void visitProgramField(ProgramClass programClass, ProgramField programField) {
            fields.add(ClassifierVertex.Field.from(programClass, programField));
        }

        @Override
        public void visitProgramMethod(ProgramClass programClass, ProgramMethod programMethod) {
            methods.add(ClassifierVertex.Method.from(programClass, programMethod));
        }
    }

    // Utility methods.

    private void createClassifierVertexRelationships(ProgramClass programClass, ClassifierVertex sourceVertex)
    {
        ClassFileRelationshipIdentifier.MyProcessingInfo processingInfo =
            (ClassFileRelationshipIdentifier.MyProcessingInfo) programClass.getProcessingInfo();

        createRelationship(
            programClass,
            sourceVertex,
            Collections.singleton(processingInfo.superClass),
            ArcType.EXTENSION);
        createRelationship(
            programClass, sourceVertex, processingInfo.dependencies, ArcType.DEPENDENCY);
        createRelationship(
            programClass, sourceVertex, processingInfo.associations, ArcType.ASSOCIATION);
        createRelationship(
            programClass, sourceVertex, processingInfo.aggregations, ArcType.AGGREGATION);
        createRelationship(
            programClass, sourceVertex, processingInfo.implementations, ArcType.IMPLEMENTATION);
    }


    /**
     * Creates a relationship of the given type from the given source vertex to all given targets,
     * but only if the target is not the source itself or a library class.
     */
    private void createRelationship(
            ProgramClass source,
            ClassifierVertex sourceVertex,
            Collection<Clazz> targets,
            ArcType type) {
        for (Clazz target : targets) {
            if (target instanceof ProgramClass && target != source) {
                ClassifierVertex targetVertex = seenClassifierVertices.get(target.getName());
                // Have we seen this vertex before?
                if (targetVertex == null) {
                    targetVertex = createClassifierVertex((ProgramClass) target);

                    classifierVertices.add(targetVertex);
                    seenClassifierVertices.put(target.getName(), targetVertex);
                }
                sourceVertex.addArc(new Arc<>(sourceVertex, targetVertex, type));
            }
        }
    }


    private void createPackageVertexRelationships(ProgramClass programClass, List<Clazz> children, PackageVertex packageVertex)
    {
        for (Clazz clazz : children) {
            ClassFileRelationshipIdentifier.MyProcessingInfo processingInfo =
                (ClassFileRelationshipIdentifier.MyProcessingInfo) clazz.getProcessingInfo();

            createRelationship(
                programClass,
                packageVertex,
                Collections.singleton(processingInfo.superClass),
                ArcType.EXTENSION);
            createRelationship(
                programClass, packageVertex, processingInfo.dependencies, ArcType.DEPENDENCY);
            createRelationship(
                programClass, packageVertex, processingInfo.associations, ArcType.ASSOCIATION);
            createRelationship(
                programClass, packageVertex, processingInfo.aggregations, ArcType.AGGREGATION);
            createRelationship(
                programClass,
                packageVertex,
                processingInfo.implementations,
                ArcType.IMPLEMENTATION);
        }
    }

    /**
     * Creates a relationship of the given type from the given source vertex to all given targets,
     * but only if the target is not the source itself or a library class.
     */
    private void createRelationship(
            ProgramClass source,
            PackageVertex sourceVertex,
            Collection<Clazz> targets,
            ArcType type) {
        for (Clazz target : targets) {
            if (target instanceof ProgramClass && target != source) {
                PackageVertex targetVertex = seenPackageVertices.get(target.getName());
                // Have we seen this vertex before?
                if (targetVertex == null) {
                    targetVertex =
                            new PackageVertex.PackageVertexBuilder()
                                    .withName(target.getName())
                                    .withVertexType(VertexType.PACKAGE)
                                    .build();

                    packageVertices.add(targetVertex);
                    seenPackageVertices.put(target.getName(), targetVertex);
                }
                sourceVertex.addArc(new Arc<>(sourceVertex, targetVertex, type));
            }
        }
    }

    /** @return A list of classifier vertices for the given classes. */
    private List<ClassifierVertex> getOrCreateSinkVertices(List<Clazz> children) {
        List<ClassifierVertex> ret = new ArrayList<>();

        for (Clazz child : children) {
            ClassifierVertex vertex = seenClassifierVertices.get(child.getName());
            if (vertex == null) {
                vertex = createClassifierVertex((ProgramClass) child);
                classifierVertices.add(vertex);
                seenClassifierVertices.put(child.getName(), vertex);
            }
            ret.add(vertex);
        }

        return ret;
    }

    private static ClassifierVertex createClassifierVertex(ProgramClass programClass) {
        // Create the members of the classifier vertex.
        List<ClassifierVertex.Method> methods = new ArrayList<>();
        List<ClassifierVertex.Field> fields = new ArrayList<>();
        programClass.accept(
                new AllMemberVisitor(new ClassifierVertexMemberCreator(methods, fields)));

        return new ClassifierVertex.ClassifierVertexBuilder()
                .withName(programClass.getName())
                .withVertexType(programClass)
                .withMethods(methods)
                .withFields(fields)
                .build();
    }
}
