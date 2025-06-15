package parser.classfile;

import static java.util.Objects.requireNonNull;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import model.graph.Arc;
import model.graph.ArcType;
import model.graph.ClassifierVertex;
import model.graph.PackageVertex;
import model.graph.VertexType;
import proguard.classfile.ClassPool;
import proguard.classfile.Clazz;
import proguard.classfile.Member;
import proguard.classfile.ProgramClass;
import proguard.classfile.ProgramField;
import proguard.classfile.ProgramMethod;
import proguard.classfile.util.ClassUtil;
import proguard.classfile.visitor.AllMemberVisitor;
import proguard.classfile.visitor.ClassPoolVisitor;
import proguard.classfile.visitor.ClassVisitor;
import proguard.classfile.visitor.MemberVisitor;

/** This {@link ClassVisitor} creates relationships */
public class ClassFileRelationshipCreator implements ClassPoolVisitor, ClassVisitor {
    private final Map<String, ClassifierVertex> seenClassifierVertices = new HashMap<>();
    private final Map<String, PackageVertex> seenPackageVertices = new HashMap<>();

    private final Collection<ClassifierVertex> classifierVertices;
    private final Collection<PackageVertex> packageVertices;
    private final Map<String, Set<Clazz>> packages;

    public ClassFileRelationshipCreator(
            Collection<ClassifierVertex> classifierVertices,
            Collection<PackageVertex> packageVertices,
            Map<String, Set<Clazz>> packages) {
        this.classifierVertices = classifierVertices;
        this.packageVertices = packageVertices;
        this.packages = packages;
    }

    @Override
    public void visitClassPool(ClassPool classPool) {
        // Filter out classes that haven't been marked.
        classPool.classesAccept(new MyProcessingInfoFilter(this));

        for (String className : seenClassifierVertices.keySet()) {
            String packageName = ClassUtil.internalPackageName(className);
            Collection<Clazz> children = requireNonNull(packages.get(packageName));
            PackageVertex packageVertex = seenPackageVertices.get(packageName);
            if (packageVertex == null) {
                packageVertex =
                        new PackageVertex.PackageVertexBuilder()
                                .withSinkVertices(getOrCreateSinkVertices(children))
                                .withName(ClassUtil.externalClassName(packageName))
                                .build();
                seenPackageVertices.put(packageName, packageVertex);
                packageVertices.add(packageVertex);
            }

            String parentPackageName = ClassUtil.internalPackageName(packageName);
            if (!parentPackageName.isEmpty()) {
                PackageVertex parentVertex = seenPackageVertices.get(parentPackageName);
                // We only create package vertices for packages that contain class files.
                if (parentVertex != null) {
                    packageVertex.addNeighborVertex(parentVertex);
                }
            }

            createPackageVertexRelationships(packageVertex, children);
        }
    }

    // Implementations for ClassVisitor.

    @Override
    public void visitAnyClass(Clazz clazz) {}

    @Override
    public void visitProgramClass(ProgramClass programClass) {
        ClassifierVertex sourceVertex = seenClassifierVertices.get(programClass.getName());
        if (sourceVertex == null) {
            sourceVertex = createClassifierVertex(programClass);
            seenClassifierVertices.put(
                    ClassUtil.internalClassName(sourceVertex.getName()), sourceVertex);
            classifierVertices.add(sourceVertex);
        }
        createClassifierVertexRelationships(programClass, sourceVertex);
    }

    // Helper classes.

    static class ClassifierVertexMemberCreator implements MemberVisitor {
        private final Collection<ClassifierVertex.Method> methods;
        private final Collection<ClassifierVertex.Field> fields;

        public ClassifierVertexMemberCreator(
                Collection<ClassifierVertex.Method> methods,
                Collection<ClassifierVertex.Field> fields) {
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

    // Helper classes.

    /**
     * This {@link ClassVisitor} delegates all visits to the given class visitor, but only if the
     * class that it visits has been marked with {@link
     * ClassFileRelationshipIdentifier.MyProcessingInfo}.
     */
    static class MyProcessingInfoFilter implements ClassVisitor {
        private final ClassVisitor acceptedVisitor;

        public MyProcessingInfoFilter(ClassVisitor acceptedVisitor) {
            this.acceptedVisitor = acceptedVisitor;
        }

        // Implementations for ClassVisitor.

        @Override
        public void visitAnyClass(Clazz clazz) {}

        @Override
        public void visitProgramClass(ProgramClass programClass) {
            Object processingInfo = programClass.getProcessingInfo();
            if (accepted(processingInfo)) {
                programClass.accept(acceptedVisitor);
            }
        }

        // Utility methods.

        private static boolean accepted(Object processingInfo) {
            return processingInfo instanceof ClassFileRelationshipIdentifier.MyProcessingInfo
                    && (((ClassFileRelationshipIdentifier.MyProcessingInfo) processingInfo)
                            .wasMarked());
        }
    }

    // Utility methods.

    private void createClassifierVertexRelationships(
            ProgramClass programClass, ClassifierVertex sourceVertex) {
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
                if (targetVertex == null) {
                    targetVertex = createClassifierVertex((ProgramClass) target);
                    classifierVertices.add(targetVertex);
                    seenClassifierVertices.put(
                            ClassUtil.internalClassName(target.getName()), targetVertex);
                }
                sourceVertex.addArc(new Arc<>(sourceVertex, targetVertex, type));
            }
        }
    }

    private void createPackageVertexRelationships(
            PackageVertex packageVertex, Collection<Clazz> children) {
        for (Clazz clazz : children) {
            ClassFileRelationshipIdentifier.MyProcessingInfo processingInfo =
                    (ClassFileRelationshipIdentifier.MyProcessingInfo) clazz.getProcessingInfo();
            createRelationship(packageVertex, Collections.singleton(processingInfo.superClass));
            createRelationship(packageVertex, processingInfo.dependencies);
            createRelationship(packageVertex, processingInfo.associations);
            createRelationship(packageVertex, processingInfo.aggregations);
            createRelationship(packageVertex, processingInfo.implementations);
        }
    }

    /**
     * Creates a relationship of the given type from the given source vertex to all given targets,
     * but only if the target is not the source itself or a library class.
     */
    private void createRelationship(PackageVertex sourceVertex, Collection<Clazz> targets) {
        for (Clazz target : targets) {
            if (target instanceof ProgramClass) {
                String targetPackageName = ClassUtil.internalPackageName(target.getName());
                PackageVertex targetVertex = seenPackageVertices.get(targetPackageName);
                if (targetVertex == null) {
                    Collection<Clazz> children = requireNonNull(packages.get(targetPackageName));
                    targetVertex =
                            new PackageVertex.PackageVertexBuilder()
                                    .withSinkVertices(getOrCreateSinkVertices(children))
                                    .withName(ClassUtil.externalClassName(targetPackageName))
                                    .withVertexType(VertexType.PACKAGE)
                                    .build();
                    packageVertices.add(targetVertex);
                    seenPackageVertices.put(target.getName(), targetVertex);
                }

                if (sourceVertex != targetVertex) {
                    sourceVertex.addArc(new Arc<>(sourceVertex, targetVertex, ArcType.DEPENDENCY));
                }
            }
        }
    }

    /** @return A list of classifier vertices for the given classes. */
    private Set<ClassifierVertex> getOrCreateSinkVertices(Collection<Clazz> children) {
        Set<ClassifierVertex> ret = new LinkedHashSet<>();

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

    /** Creates a classifier vertex from the given program class. */
    private static ClassifierVertex createClassifierVertex(ProgramClass programClass) {
        // Create the members of the classifier vertex.
        Set<ClassifierVertex.Method> methods = new LinkedHashSet<>();
        Set<ClassifierVertex.Field> fields = new LinkedHashSet<>();
        programClass.accept(
                new AllMemberVisitor(new ClassifierVertexMemberCreator(methods, fields)));

        return new ClassifierVertex.ClassifierVertexBuilder()
                .withName(ClassUtil.externalClassName(programClass.getName()))
                .withVertexType(programClass)
                .withMethods(methods)
                .withFields(fields)
                .build();
    }
}
