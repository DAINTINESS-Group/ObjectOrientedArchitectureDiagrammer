package manager;

import javafx.util.Pair;
import model.graph.SinkVertex;
import model.graph.Vertex;
import parser.Interpreter;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SourceProject {

    private final Map<Path, Vertex> vertices;
    private final Map<Path, SinkVertex> sinkVertices;

    public SourceProject() {
        vertices = new HashMap<>();
        sinkVertices = new HashMap<>();
    }

    public void createGraph(Path sourcePackagePath) {
        Interpreter interpreter = new Interpreter();
        interpreter.parseProject(sourcePackagePath);
        Pair<ArrayList<Vertex>, ArrayList<SinkVertex>> vertices = interpreter.convertTreeToGraph();
        vertices.getKey().forEach(this::addVertex);
        vertices.getValue().forEach(this::addSinkVertex);
    }

    private void addVertex(Vertex vertex) {
        vertices.put(vertex.getPath(), vertex);
    }

    private void addSinkVertex(SinkVertex sinkVertex) {
        sinkVertices.put(sinkVertex.getPath(), sinkVertex);
    }

    public Map<Path, Vertex> getVertices() {
        return vertices;
    }

    public Map<Path, SinkVertex> getSinkVertices() {
        return sinkVertices;
    }

}