package model.graph;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SinkVertex {

    private final List<Method> methods;
    private final List<Field> fields;
    private final List<Arc<SinkVertex>> arcs;
    private final VertexType vertexType;
    private final Path path;
    private final String name;

    public SinkVertex(Path path, String name, VertexType vertexType) {
        this.vertexType = vertexType;
        this.path = path;
        this.name = name;
        arcs = new ArrayList<>();
        methods = new ArrayList<>();
        fields = new ArrayList<>();
    }

    public void addArc(SinkVertex sourceVertex, SinkVertex targetVertex, ArcType arcType) {
        arcs.add(new Arc<>(sourceVertex, targetVertex, arcType));
    }

    public void addMethod(String name, String returnType, ModifierType modifier, Map<String, String> parameters) {
        methods.add(new Method(name, returnType, modifier, parameters));
    }

    public void addField(String name, String type, ModifierType modifier) {
        fields.add(new Field(name, type, modifier));
    }

    public VertexType getVertexType() {
        return vertexType;
    }

    public List<Arc<SinkVertex>> getArcs() {
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

    public class Method {

        private final Map<String, String> parameters;
        private final ModifierType modifierType;
        private final String name;
        private final String returnType;

        public Method(String name, String returnType, ModifierType modifier, Map<String, String> parameters) {
            this.name = name;
            this.returnType = returnType;
            modifierType = modifier;
            this.parameters = parameters;
        }

        public String getName() {
            return name;
        }

        public ModifierType getModifierType() {
            return modifierType;
        }

        public Map<String, String> getParameters() {
            return parameters;
        }

        public String getReturnType() {
            return returnType;
        }
    }

    public class Field {

        private final String name;
        private final String type;
        private final ModifierType modifier;

        public Field(String name, String type, ModifierType modifier) {
            this.name = name;
            this.type = type;
            this.modifier = modifier;
        }

        public String getName() {
            return name;
        }

        public String getType() {
            return type;
        }

        public ModifierType getModifier() {
            return modifier;
        }
    }
}
