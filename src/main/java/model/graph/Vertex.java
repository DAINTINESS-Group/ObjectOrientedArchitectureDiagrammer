package model.graph;

import org.javatuples.Triplet;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Vertex {

    private static final ModifierType VERTEX_MODIFIER_TYPE = ModifierType.PACKAGE_PRIVATE;
    private final List<Arc<Vertex>> arcs;
    private final List<SinkVertex> sinkVertices;
    private final List<Vertex> neighbourVertices;
    private final VertexType vertexType;
    private final Path path;
    private final String name;
    private Vertex parentVertex;
    private List<Triplet<String, String, String>> deserializedArcs;
    //TODO maybe add package validity as boolean, same as PackageNode

    public Vertex(Path path, VertexType vertexType, String parentName) {
        this.path = path;
        this.vertexType = vertexType;
        arcs = new ArrayList<>();
        sinkVertices = new ArrayList<>();
        neighbourVertices = new ArrayList<>();
        //TODO maybe change to '\\' instead of '.'
        name = (parentName.equals("")) ? path.getFileName().toString() : parentName + "." + path.getFileName().toString();
    }

    public void addArc(Vertex sourceVertex, Vertex targetVertex, ArcType arcType) {
        arcs.add(new Arc<>(sourceVertex, targetVertex, arcType));
    }

    public void addSinkVertex(SinkVertex sinkVertex) {
        sinkVertices.add(sinkVertex);
    }

    public void addNeighbourVertex(Vertex vertex) {
        neighbourVertices.add(vertex);
    }

    public void setParentNode(Vertex parentVertex) {
        this.parentVertex = parentVertex;
    }

    public void setDeserializedArcs(List<Triplet<String, String, String>> deserializedArcs) {
        this.deserializedArcs = deserializedArcs;
    }

    public List<Triplet<String, String, String>> getDeserializedArcs() {
        return deserializedArcs;
    }

    public List<Arc<Vertex>> getArcs() {
        return arcs;
    }

    public List<SinkVertex> getSinkVertices() {
        return sinkVertices;
    }

    public List<Vertex> getNeighbourVertices() {
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

    public Vertex getParentVertex() {
        return parentVertex;
    }

    public ModifierType getModifierType() { return VERTEX_MODIFIER_TYPE; }
}
