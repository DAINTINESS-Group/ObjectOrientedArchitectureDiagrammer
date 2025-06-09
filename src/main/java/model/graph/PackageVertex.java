package model.graph;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.javatuples.Triplet;

public class PackageVertex {
    private static final ModifierType VERTEX_MODIFIER_TYPE = ModifierType.PACKAGE_PRIVATE;
    private static final VertexCoordinate DEFAULT_COORDINATE = new VertexCoordinate(0, 0);

    private final List<Arc<PackageVertex>> arcs = new ArrayList<>();
    private final List<PackageVertex> neighbourVertices = new ArrayList<>();

    private final Path path;
    private final VertexType vertexType;
    private final String name;
    private final List<ClassifierVertex> sinkVertices;

    private VertexCoordinate coordinate = DEFAULT_COORDINATE;
    private List<Triplet<String, String, String>> deserializedArcs;
    private PackageVertex parentPackageVertex;

    private PackageVertex(
            Path path, VertexType vertexType, String name, List<ClassifierVertex> sinkVertices) {
        this.path = path;
        this.vertexType = vertexType;
        this.name = name;
        this.sinkVertices = sinkVertices;
    }

    public void setCoordinate(double x, double y) {
        coordinate = new VertexCoordinate(x, y);
    }

    public void addArc(PackageVertex sourceVertex, PackageVertex targetVertex, ArcType arcType) {
        arcs.add(new Arc<>(sourceVertex, targetVertex, arcType));
    }

    public void addArc(Arc<PackageVertex> arc) {
        arcs.add(arc);
    }

    public void addSinkVertex(ClassifierVertex classifierVertex) {
        sinkVertices.add(classifierVertex);
    }

    public void addNeighbourVertex(PackageVertex vertex) {
        neighbourVertices.add(vertex);
    }

    public void setParentNode(PackageVertex parentPackageVertex) {
        this.parentPackageVertex = parentPackageVertex;
    }

    public void setDeserializedArcs(List<Triplet<String, String, String>> deserializedArcs) {
        this.deserializedArcs = deserializedArcs;
    }

    public List<Triplet<String, String, String>> getDeserializedArcs() {
        return deserializedArcs;
    }

    public List<Arc<PackageVertex>> getArcs() {
        return arcs;
    }

    public List<ClassifierVertex> getSinkVertices() {
        return sinkVertices;
    }

    public List<PackageVertex> getNeighbourVertices() {
        return neighbourVertices;
    }

    public Path getPath() {
        return path;
    }

    public String getName() {
        return name;
    }

    public VertexType getVertexType() {
        return vertexType;
    }

    public PackageVertex getParentVertex() {
        return parentPackageVertex;
    }

    public ModifierType getModifierType() {
        return VERTEX_MODIFIER_TYPE;
    }

    public VertexCoordinate getCoordinate() {
        return coordinate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PackageVertex that = (PackageVertex) o;
        return Objects.equals(name, that.name)
                && Objects.equals(sinkVertices, that.sinkVertices)
                && Objects.equals(parentPackageVertex, that.parentPackageVertex);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, sinkVertices, parentPackageVertex);
    }

    @Override
    public String toString() {
        return name;
    }

    public static class PackageVertexBuilder {
        private Path path;
        private VertexType vertexType = VertexType.PACKAGE;
        private String name;
        private List<ClassifierVertex> sinkVertices = new ArrayList<>();

        public PackageVertexBuilder withPath(Path path) {
            this.path = path;
            return this;
        }

        public PackageVertexBuilder withVertexType(VertexType vertexType) {
            this.vertexType = vertexType;
            return this;
        }

        public PackageVertexBuilder withName(String parentName) {
            this.name = parentName;
            return this;
        }

        public PackageVertexBuilder withSinkVertices(List<ClassifierVertex> sinkVertices) {
            this.sinkVertices = sinkVertices;
            return this;
        }

        public PackageVertex build() {
            return new PackageVertex(path, vertexType, name, sinkVertices);
        }
    }
}
