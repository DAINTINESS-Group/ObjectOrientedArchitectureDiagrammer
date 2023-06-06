package model.diagram.javafx.packagediagram;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.graph.ArcType;
import model.graph.Vertex;
import org.javatuples.Triplet;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class JavaFXPackageDiagramLoader {

    private final Path graphSavePath;

    public JavaFXPackageDiagramLoader(Path graphSavePath) {
        this.graphSavePath = graphSavePath;
    }

    public Set<Vertex> loadDiagram(){
        Set<Vertex> vertices = new HashSet<>();
        try {
            String json = Files.readString(graphSavePath);
            Gson gson = new GsonBuilder().registerTypeAdapter(Vertex.class, new VertexDeserializer()).create();
            Vertex[] verticesArray = gson.fromJson(json, Vertex[].class);
            Collections.addAll(vertices, verticesArray);
            for (Vertex vertex: vertices) {
                List<Triplet<String, String, String>> deserializedArcs = vertex.getDeserializedArcs();
                for (Triplet<String, String, String> arc: deserializedArcs) {
                    Optional<Vertex> sourceVertex = vertices.stream().filter(vertex1 -> vertex1.getName().equals(arc.getValue0())).findFirst();
                    Optional<Vertex> targetVertex = vertices.stream().filter(vertex1 -> vertex1.getName().equals(arc.getValue1())).findFirst();
                    if (sourceVertex.isEmpty() || targetVertex.isEmpty()) {
                        continue;
                    }
                    vertex.addArc(sourceVertex.get(), targetVertex.get(), ArcType.valueOf(arc.getValue2()));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return vertices;
    }
}
