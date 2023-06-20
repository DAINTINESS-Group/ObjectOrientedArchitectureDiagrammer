package model.diagram.javafx;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import model.graph.ArcType;
import model.graph.ClassifierVertex;
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

    public Set<ClassifierVertex> loadDiagram() throws JsonParseException {
        Set<ClassifierVertex> sinkVertices = new HashSet<>();
        try {
            String json = Files.readString(graphSavePath);
            Gson gson = new GsonBuilder().registerTypeAdapter(ClassifierVertex.class, new ClassifierVertexDeserializer()).create();
            ClassifierVertex[] sinkVerticesArray = gson.fromJson(json, ClassifierVertex[].class);
            Collections.addAll(sinkVertices, sinkVerticesArray);
            deserializeArcs(sinkVertices);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sinkVertices;
    }

    private void deserializeArcs(Set<ClassifierVertex> sinkVertices) {
        for (ClassifierVertex classifierVertex : sinkVertices) {
            List<Triplet<String, String, String>> deserializedArcs = classifierVertex.getDeserializedArcs();
            for (Triplet<String, String, String> arc: deserializedArcs) {
                Optional<ClassifierVertex> sourceVertex = sinkVertices.stream()
                    .filter(sinkVertex1 -> sinkVertex1.getName().equals(arc.getValue0()))
                    .findFirst();
                Optional<ClassifierVertex> targetVertex = sinkVertices.stream()
                    .filter(sinkVertex1 -> sinkVertex1.getName().equals(arc.getValue1()))
                    .findFirst();
                if (sourceVertex.isEmpty() || targetVertex.isEmpty()) {
                    continue;
                }
                classifierVertex.addArc(sourceVertex.get(), targetVertex.get(), ArcType.valueOf(arc.getValue2()));
            }
        }
    }

}
