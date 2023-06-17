package model.diagram.javafx;

import com.google.gson.*;
import model.graph.SinkVertex;
import model.graph.Vertex;
import model.graph.VertexType;
import org.javatuples.Triplet;

import java.lang.reflect.Type;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class VertexDeserializer implements JsonDeserializer<Vertex> {

    private Vertex vertex;

    @Override
    public Vertex deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        String path = jsonObject.get("path").getAsString();
        String vertexType = jsonObject.get("vertexType").getAsString();

        JsonObject parent = jsonObject.get("parent").getAsJsonObject();
        String parentName = parent.get("name").getAsString();

        vertex = new Vertex(Path.of(path), VertexType.valueOf(vertexType), parentName);

        deserializeSinkVertices(jsonObject);
        deserializeNeighbourVertices(jsonObject);
        deserializeArcs(jsonObject);

        return vertex;
    }

    private void deserializeSinkVertices(JsonObject jsonObject) {
        JsonArray sinkVertices = jsonObject.get("sinkVertices").getAsJsonArray();
        for (int i = 0; i < sinkVertices.size(); i++) {
            String json = sinkVertices.get(i).getAsString();

            Gson gson = new GsonBuilder().registerTypeAdapter(SinkVertex.class, new SinkVertexDeserializer()).create();
            SinkVertex sinkVertex = gson.fromJson(json, SinkVertex.class);
            vertex.addSinkVertex(sinkVertex);
        }
    }

    private void deserializeNeighbourVertices(JsonObject jsonObject) {
        JsonArray neighbourVertices = jsonObject.get("neighbours").getAsJsonArray();
        for (int i = 0; i < neighbourVertices.size(); i++) {
            JsonObject vertexObject = neighbourVertices.get(i).getAsJsonObject();
            String path = vertexObject.get("path").getAsString();
            String vertexType = vertexObject.get("vertexType").getAsString();
            String parentName = vertexObject.get("parentName").getAsString();

            vertex.addNeighbourVertex(new Vertex(Path.of(path), VertexType.valueOf(vertexType), parentName));
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
        vertex.setDeserializedArcs(arcs);
    }

}
