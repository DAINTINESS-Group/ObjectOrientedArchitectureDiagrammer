package model.diagram.javafx;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import model.graph.ArcType;
import model.graph.ClassifierVertex;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.javatuples.Triplet;

public class JavaFXClassDiagramLoader {
    private static final Logger logger = LogManager.getLogger(JavaFXClassDiagramLoader.class);

    public static Set<ClassifierVertex> loadDiagram(Path graphSavePath) throws JsonParseException {
        Set<ClassifierVertex> sinkVertices = new HashSet<>();
        try {
            byte[] encodedBytes = Files.readAllBytes(graphSavePath);
            String json = new String(encodedBytes, StandardCharsets.ISO_8859_1);
            Gson gson =
                    new GsonBuilder()
                            .registerTypeAdapter(
                                    ClassifierVertex.class, new ClassifierVertexDeserializer())
                            .create();

            ClassifierVertex[] sinkVerticesArray = gson.fromJson(json, ClassifierVertex[].class);
            Collections.addAll(sinkVertices, sinkVerticesArray);
            deserializeArcs(sinkVertices);
        } catch (IOException e) {
            logger.error("Failed to read bytes from file: {}", graphSavePath.toAbsolutePath());
            throw new RuntimeException(e);
        }
        return sinkVertices;
    }

    private static void deserializeArcs(Set<ClassifierVertex> sinkVertices) {
        for (ClassifierVertex classifierVertex : sinkVertices) {
            List<Triplet<String, String, String>> deserializedArcs =
                    classifierVertex.getDeserializedArcs();
            for (Triplet<String, String, String> arc : deserializedArcs) {
                Optional<ClassifierVertex> sourceVertex =
                        sinkVertices.stream()
                                .filter(it -> it.getName().equals(arc.getValue0()))
                                .findFirst();
                Optional<ClassifierVertex> targetVertex =
                        sinkVertices.stream()
                                .filter(it -> it.getName().equals(arc.getValue1()))
                                .findFirst();
                if (sourceVertex.isEmpty() || targetVertex.isEmpty()) continue;

                classifierVertex.addArc(
                        sourceVertex.get(), targetVertex.get(), ArcType.get(arc.getValue2()));
            }
        }
    }
}
