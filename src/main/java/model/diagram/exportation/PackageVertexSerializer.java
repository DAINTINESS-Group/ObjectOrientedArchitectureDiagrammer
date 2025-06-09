package model.diagram.exportation;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.util.List;
import model.graph.Arc;
import model.graph.ClassifierVertex;
import model.graph.PackageVertex;

public class PackageVertexSerializer implements JsonSerializer<PackageVertex> {

    @Override
    public JsonElement serialize(
            PackageVertex packageVertex,
            Type type,
            JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonObject = new JsonObject();
        String name = packageVertex.getName();
        Path path = packageVertex.getPath();
        String vertexType = packageVertex.getVertexType().toString();

        jsonObject.addProperty("name", name);
        jsonObject.addProperty("path", path.toString());
        jsonObject.addProperty("vertexType", vertexType);
        jsonObject.addProperty("coordinate_x", packageVertex.getCoordinate().x());
        jsonObject.addProperty("coordinate_y", packageVertex.getCoordinate().y());
        jsonObject.add("sinkVertices", serializeSinkVertices(packageVertex));
        jsonObject.add("parent", serializeParentVertex(packageVertex));
        jsonObject.add("neighbours", serializeNeighbourVertices(packageVertex));
        jsonObject.add("arcs", serializeArcs(packageVertex));

        return jsonObject;
    }

    private JsonArray serializeSinkVertices(PackageVertex packageVertex) {
        List<ClassifierVertex> sinkVertices = packageVertex.getSinkVertices();
        JsonArray sinkVerticesArray = new JsonArray(sinkVertices.size());
        for (ClassifierVertex classifierVertex : sinkVertices) {
            Gson gson =
                    new GsonBuilder()
                            .registerTypeAdapter(
                                    ClassifierVertex.class, new ClassifierVertexSerializer())
                            .create();
            String json = gson.toJson(classifierVertex);
            sinkVerticesArray.add(json);
        }
        return sinkVerticesArray;
    }

    private JsonObject serializeParentVertex(PackageVertex packageVertex) {
        PackageVertex parentPackageVertex = packageVertex.getParentVertex();
        JsonObject parentObject = new JsonObject();
        String name = parentPackageVertex.getName();
        String path = parentPackageVertex.getPath().toString();
        String type = parentPackageVertex.getModifierType().toString();

        parentObject.addProperty("name", name);
        parentObject.addProperty("path", path);
        parentObject.addProperty("vertexType", type);

        return parentObject;
    }

    private JsonArray serializeNeighbourVertices(PackageVertex packageVertex) {
        List<PackageVertex> neighbourVertices = packageVertex.getNeighbourVertices();
        JsonArray neighbourVerticesArray = new JsonArray(neighbourVertices.size());

        for (PackageVertex v : neighbourVertices) {
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

    private JsonArray serializeArcs(PackageVertex packageVertex) {
        List<Arc<PackageVertex>> arcs = packageVertex.getArcs();
        JsonArray arcsArray = new JsonArray(arcs.size());

        for (Arc<PackageVertex> vertexArc : arcs) {
            JsonObject arcObject = new JsonObject();
            String source = vertexArc.sourceVertex().getName();
            String target = vertexArc.targetVertex().getName();
            String arcType = vertexArc.arcType().toString();

            arcObject.addProperty("source", source);
            arcObject.addProperty("target", target);
            arcObject.addProperty("arcType", arcType);
            arcsArray.add(arcObject);
        }
        return arcsArray;
    }
}
