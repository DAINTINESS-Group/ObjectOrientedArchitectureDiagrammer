package model.graph;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import org.javatuples.Triplet;
import proguard.classfile.Clazz;
import proguard.classfile.ProgramClass;
import proguard.classfile.ProgramField;
import proguard.classfile.ProgramMethod;
import proguard.classfile.util.ClassUtil;
import proguard.classfile.util.InternalTypeEnumeration;

public class ClassifierVertex {
    private static final VertexCoordinate DEFAULT_COORDINATE = new VertexCoordinate(0, 0);

    private final Path path;
    private final String name;
    private final VertexType vertexType;
    private final List<Arc<ClassifierVertex>> arcs;
    private final List<Method> methods;
    private final List<Field> fields;

    private VertexCoordinate coordinate = DEFAULT_COORDINATE;
    private List<Triplet<String, String, String>> deserializedArcs;

    private ClassifierVertex(Path path, String name, VertexType vertexType) {
        this(path, name, vertexType, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
    }

    private ClassifierVertex(
            Path path,
            String name,
            VertexType vertexType,
            List<Arc<ClassifierVertex>> arcs,
            List<Method> methods,
            List<Field> fields) {
        this.path = path;
        this.name = name;
        this.vertexType = vertexType;
        this.arcs = arcs;
        this.methods = methods;
        this.fields = fields;
    }

    public void setCoordinate(double x, double y) {
        coordinate = new VertexCoordinate(x, y);
    }

    public void addArc(
            ClassifierVertex sourceVertex, ClassifierVertex targetVertex, ArcType arcType) {
        arcs.add(new Arc<>(sourceVertex, targetVertex, arcType));
    }

    public void addArc(Arc<ClassifierVertex> arc) {
        this.arcs.add(arc);
    }

    public void addMethod(
            String name, String returnType, ModifierType modifier, Collection<String> parameters) {
        methods.add(new Method(name, returnType, modifier, parameters));
    }

    public void addField(String name, String type, ModifierType modifier) {
        fields.add(new Field(name, type, modifier));
    }

    public void setDeserializedArcs(List<Triplet<String, String, String>> deserializedArcs) {
        this.deserializedArcs = deserializedArcs;
    }

    public List<Triplet<String, String, String>> getDeserializedArcs() {
        return deserializedArcs;
    }

    public VertexType getVertexType() {
        return vertexType;
    }

    public List<Arc<ClassifierVertex>> getArcs() {
        return arcs;
    }

    public Path getPath() {
        return path;
    }

    public String getName() {
        return name;
    }

    public List<Method> getMethods() {
        return methods;
    }

    public List<Field> getFields() {
        return fields;
    }

    public VertexCoordinate getCoordinate() {
        return coordinate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClassifierVertex that = (ClassifierVertex) o;
        return vertexType == that.vertexType
                && Objects.equals(name, that.name)
                && Objects.equals(methods, that.methods)
                && Objects.equals(fields, that.fields);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, vertexType, methods, fields);
    }

    @Override
    public String toString() {
        return name;
    }

    public record Method(
            String name, String returnType, ModifierType modifier, Collection<String> parameters) {

        public static Method from(ProgramClass programClass, ProgramMethod programMethod) {
            List<String> types = new ArrayList<>();
            InternalTypeEnumeration typeEnumeration =
                    new InternalTypeEnumeration(programMethod.getDescriptor(programClass));
            while (typeEnumeration.hasMoreTypes()) {
                types.add(ClassUtil.internalClassNameFromClassType(typeEnumeration.nextType()));
            }

            return new ClassifierVertex.Method(
                    programMethod.getName(programClass),
                    typeEnumeration.returnType(),
                    ModifierType.from(programMethod.u2accessFlags),
                    types);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Method method = (Method) o;
            return Objects.equals(name, method.name)
                    && Objects.equals(returnType, method.returnType)
                    && modifier == method.modifier
                    && Objects.equals(parameters, method.parameters);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, returnType, modifier, parameters);
        }
    }

    public record Field(String name, String type, ModifierType modifier) {

        public static Field from(ProgramClass programClass, ProgramField programField) {
            InternalTypeEnumeration typeEnumeration =
                    new InternalTypeEnumeration(programField.getDescriptor(programClass));
            String type = ClassUtil.internalClassNameFromClassType(typeEnumeration.nextType());

            return new ClassifierVertex.Field(
                    programField.getName(programClass),
                    type,
                    ModifierType.from(programField.getAccessFlags()));
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Field field = (Field) o;
            return Objects.equals(name, field.name)
                    && Objects.equals(type, field.type)
                    && modifier == field.modifier;
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, type, modifier);
        }
    }

    public static class ClassifierVertexBuilder {
        private String name;
        private VertexType vertexType;
        private Path path;
        private List<Method> methods;
        private List<Field> fields;

        public ClassifierVertexBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public ClassifierVertexBuilder withVertexType(VertexType vertexType) {
            this.vertexType = vertexType;
            return this;
        }

        public ClassifierVertexBuilder withVertexType(Clazz programClass) {
            this.vertexType = VertexType.from(programClass);
            return this;
        }

        public ClassifierVertexBuilder withPath(Path path) {
            this.path = path;
            return this;
        }

        public ClassifierVertexBuilder withMethods(List<Method> methods) {
            this.methods = methods;
            return this;
        }

        public ClassifierVertexBuilder withFields(List<Field> fields) {
            this.fields = fields;
            return this;
        }

        public ClassifierVertex build() {
            return (methods != null && fields != null)
                    ? new ClassifierVertex(
                            path, name, vertexType, new ArrayList<>(), methods, fields)
                    : new ClassifierVertex(path, name, vertexType);
        }
    }
}
