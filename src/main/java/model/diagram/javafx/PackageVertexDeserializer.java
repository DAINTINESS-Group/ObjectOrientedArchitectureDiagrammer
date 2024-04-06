package model.diagram.javafx;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import model.graph.ClassifierVertex;
import model.graph.PackageVertex;
import model.graph.VertexType;
import org.javatuples.Triplet;

public class PackageVertexDeserializer implements JsonDeserializer<PackageVertex> {

    private PackageVertex packageVertex;

    @Override
    public PackageVertex deserialize(
            JsonElement jsonElement,
            Type type,
            JsonDeserializationContext jsonDeserializationContext)
            throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        String path = jsonObject.get("path").getAsString();
        String vertexType = jsonObject.get("vertexType").getAsString();

        if (!VertexType.get(vertexType).equals(VertexType.PACKAGE)) {
            throw new JsonParseException("Wrong diagram type");
        }

        JsonObject parent = jsonObject.get("parent").getAsJsonObject();
        String parentName = parent.get("name").getAsString();
        Path path1 = Path.of(path);
        packageVertex =
                new PackageVertex.PackageVertexBuilder()
                        .withPath(path1)
                        .withVertexType(VertexType.get(vertexType))
                        .withName(
                                parentName.isEmpty()
                                        ? path1.getFileName().toString()
                                        : String.join(
                                                ".", parentName, path1.getFileName().toString()))
                        .build();

        if (jsonObject.has("coordinate_x") && jsonObject.has("coordinate_x")) {
            double coordinateX = jsonObject.get("coordinate_x").getAsDouble();
            double coordinateY = jsonObject.get("coordinate_y").getAsDouble();
            packageVertex.setCoordinate(coordinateX, coordinateY);
        }
        deserializeSinkVertices(jsonObject);
        deserializeNeighbourVertices(jsonObject);
        deserializeArcs(jsonObject);

        return packageVertex;
    }

    private void deserializeSinkVertices(JsonObject jsonObject) {
        JsonArray sinkVertices = jsonObject.get("sinkVertices").getAsJsonArray();
        for (int i = 0; i < sinkVertices.size(); i++) {
            String json = sinkVertices.get(i).getAsString();
            Gson gson =
                    new GsonBuilder()
                            .registerTypeAdapter(
                                    ClassifierVertex.class, new ClassifierVertexDeserializer())
                            .create();
            ClassifierVertex classifierVertex = gson.fromJson(json, ClassifierVertex.class);
            packageVertex.addSinkVertex(classifierVertex);
        }
    }

    private void deserializeNeighbourVertices(JsonObject jsonObject) {
        JsonArray neighbourVertices = jsonObject.get("neighbours").getAsJsonArray();
        for (int i = 0; i < neighbourVertices.size(); i++) {
            JsonObject vertexObject = neighbourVertices.get(i).getAsJsonObject();
            String path = vertexObject.get("path").getAsString();
            String vertexType = vertexObject.get("vertexType").getAsString();
            String parentName = vertexObject.get("parentName").getAsString();
            Path path1 = Path.of(path);
            packageVertex.addNeighbourVertex(
                    new PackageVertex.PackageVertexBuilder()
                            .withPath(path1)
                            .withVertexType(VertexType.get(vertexType))
                            .withName(
                                    parentName.isEmpty()
                                            ? path1.getFileName().toString()
                                            : String.join(
                                                    ".",
                                                    parentName,
                                                    path1.getFileName().toString()))
                            .build());
        }
    }

    private void deserializeArcs(JsonObject jsonObject) {
        JsonArray arcsArray = jsonObject.get("arcs").getAsJsonArray();
        List<Triplet<String, String, String>> arcs = new ArrayList<>();
        for (int i = 0; i < arcsArray.size(); i++) {
            JsonObject arcObject = arcsArray.get(i).getAsJsonObject();
            String sourceVertex = arcObject.get("source").getAsString();
            String targetVertex = arcObject.get("target").getAsString();
            String arcType = arcObject.get("arcType").getAsString();
            arcs.add(new Triplet<>(sourceVertex, targetVertex, arcType));
        }
        packageVertex.setDeserializedArcs(arcs);
    }
}
