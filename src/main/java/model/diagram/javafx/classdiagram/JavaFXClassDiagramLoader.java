package model.diagram.javafx.classdiagram;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.graph.ArcType;
import model.graph.SinkVertex;
import org.javatuples.Triplet;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class JavaFXClassDiagramLoader {

    private final Path graphSavePath;

    public JavaFXClassDiagramLoader(Path graphSavePath) {
        this.graphSavePath = graphSavePath;
    }

    public Set<SinkVertex> loadDiagram(){
        Set<SinkVertex> sinkVertices = new HashSet<>();
        try {
            String json = Files.readString(graphSavePath);
            Gson gson = new GsonBuilder().registerTypeAdapter(SinkVertex.class, new SinkVertexDeserializer()).create();
            SinkVertex[] sinkVerticesArray = gson.fromJson(json, SinkVertex[].class);
            Collections.addAll(sinkVertices, sinkVerticesArray);
            for (SinkVertex sinkVertex: sinkVertices) {
                List<Triplet<String, String, String>> deserializedArcs = sinkVertex.getDeserializedArcs();
                for (Triplet<String, String, String> arc: deserializedArcs) {
                    Optional<SinkVertex> sourceVertex = sinkVertices.stream().filter(sinkVertex1 -> sinkVertex1.getName().equals(arc.getValue0())).findFirst();
                    Optional<SinkVertex> targetVertex = sinkVertices.stream().filter(sinkVertex1 -> sinkVertex1.getName().equals(arc.getValue1())).findFirst();
                    if (sourceVertex.isEmpty() || targetVertex.isEmpty()) {
                        continue;
                    }
                    sinkVertex.addArc(sourceVertex.get(), targetVertex.get(), ArcType.valueOf(arc.getValue2()));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return sinkVertices;
    }

}
