package model.diagram.exportation;

import com.google.gson.*;
import model.graph.Arc;
import model.graph.SinkVertex;
import model.graph.Vertex;

import java.lang.reflect.Type;
import java.nio.file.Path;
import java.util.List;

public class VertexSerializer implements JsonSerializer<Vertex> {

    private Vertex vertex;

    @Override
    public JsonElement serialize(Vertex vertex, Type type, JsonSerializationContext jsonSerializationContext) {
        this.vertex = vertex;
        JsonObject jsonObject = new JsonObject();

        String name = vertex.getName();
        Path path = vertex.getPath();
        String vertexType = vertex.getVertexType().toString();

        jsonObject.addProperty("name", name);
        jsonObject.addProperty("path", path.toString());
        jsonObject.addProperty("vertexType", vertexType);

        jsonObject.add("sinkVertices", serializeSinkVertices());
        jsonObject.add("parent", serializeParentVertex());
        jsonObject.add("neighbours", serializeNeighbourVertices());
        jsonObject.add("arcs", serializeArcs());

        return jsonObject;
    }

    private JsonArray serializeSinkVertices() {
        List<SinkVertex> sinkVertices = vertex.getSinkVertices();
        JsonArray sinkVerticesArray = new JsonArray(sinkVertices.size());
        for (SinkVertex sinkVertex: sinkVertices) {
            Gson gson = new GsonBuilder().registerTypeAdapter(SinkVertex.class, new SinkVertexSerializer()).create();
            String json = gson.toJson(sinkVertex);
            sinkVerticesArray.add(json);
        }
        return sinkVerticesArray;
    }

    private JsonObject serializeParentVertex() {
        Vertex parentVertex = vertex.getParentVertex();
        JsonObject parentObject = new JsonObject();
        String name = parentVertex.getName();
        String path = parentVertex.getPath().toString();
        String type = parentVertex.getModifierType().toString();

        parentObject.addProperty("name", name);
        parentObject.addProperty("path", path);
        parentObject.addProperty("vertexType", type);

        return parentObject;
    }

    private JsonArray serializeNeighbourVertices() {
        List<Vertex> neighbourVertices = vertex.getNeighbourVertices();
        JsonArray neighbourVerticesArray = new JsonArray(neighbourVertices.size());
        for (Vertex v: neighbourVertices) {
            JsonObject neighbourVertexObject = new JsonObject();
            String name = v.getName();
            String path = v.getPath().toString();
            String vertexType = v.getVertexType().toString();
            String parentName = v.getParentVertex().getName();

            neighbourVertexObject.addProperty("name", name);
            neighbourVertexObject.addProperty("path", path);
            neighbourVertexObject.addProperty("vertexType", vertexType);
            neighbourVertexObject.addProperty("parentName", parentName);

            neighbourVerticesArray.add(neighbourVertexObject);
        }
        return neighbourVerticesArray;
    }

    private JsonArray serializeArcs() {
        List<Arc<Vertex>> arcs = vertex.getArcs();
        JsonArray arcsArray = new JsonArray(arcs.size());
        for (Arc<Vertex> vertexArc : arcs) {
            JsonObject arcObject = new JsonObject();

            String source = vertexArc.getSourceVertex().getName();
            String target = vertexArc.getTargetVertex().getName();
            String arcType = vertexArc.getArcType().toString();

            arcObject.addProperty("source", source);
            arcObject.addProperty("target", target);
            arcObject.addProperty("arcType", arcType);

            arcsArray.add(arcObject);
        }
        return arcsArray;
    }
}
