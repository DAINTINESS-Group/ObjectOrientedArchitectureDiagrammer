package gr.uoi.ooad.model.diagram.javafx;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import gr.uoi.ooad.model.graph.ClassifierVertex;
import gr.uoi.ooad.model.graph.ModifierType;
import gr.uoi.ooad.model.graph.VertexType;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import org.javatuples.Triplet;

public class ClassifierVertexDeserializer implements JsonDeserializer<ClassifierVertex> {
    private ClassifierVertex classifierVertex;

    @Override
    public ClassifierVertex deserialize(
            JsonElement jsonElement,
            Type type,
            JsonDeserializationContext jsonDeserializationContext)
            throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        String name = jsonObject.get("name").getAsString();
        String path = jsonObject.get("path").getAsString();
        String vertexType = jsonObject.get("vertexType").getAsString();
        if (VertexType.get(vertexType).equals(VertexType.PACKAGE)) {
            throw new JsonParseException("Wrong diagram type");
        }
        classifierVertex = new ClassifierVertex(Path.of(path), name, VertexType.get(vertexType));
        if (jsonObject.has("coordinate_x") && jsonObject.has("coordinate_x")) {
            double coordinateX = jsonObject.get("coordinate_x").getAsDouble();
            double coordinateY = jsonObject.get("coordinate_y").getAsDouble();
            classifierVertex.setCoordinate(coordinateX, coordinateY);
        }
        deserializeMethods(jsonObject);
        deserializeFields(jsonObject);
        deserializeArcs(jsonObject);

        return classifierVertex;
    }

    private void deserializeMethods(JsonObject jsonObject) {
        JsonArray methods = jsonObject.get("methods").getAsJsonArray();
        Gson gson = new Gson();
        for (int i = 0; i < methods.size(); i++) {
            JsonObject method = methods.get(i).getAsJsonObject();
            String methodName = method.get("name").getAsString();
            String returnType = method.get("returnType").getAsString();
            String modifier = method.get("modifier").getAsString();
            String parameters = method.get("parameters").getAsString();
            classifierVertex.addMethod(
                    methodName,
                    returnType,
                    ModifierType.get(modifier),
                    gson.fromJson(parameters, new TypeToken<>() {}.getType()));
        }
    }

    private void deserializeFields(JsonObject jsonObject) {
        JsonArray fields = jsonObject.get("fields").getAsJsonArray();
        for (int i = 0; i < fields.size(); i++) {
            JsonObject fieldObject = fields.get(i).getAsJsonObject();
            String fieldName = fieldObject.get("name").getAsString();
            String returnType = fieldObject.get("returnType").getAsString();
            String modifierType = fieldObject.get("modifier").getAsString();
            classifierVertex.addField(fieldName, returnType, ModifierType.get(modifierType));
        }
    }

    private void deserializeArcs(JsonObject jsonObject) {
        List<Triplet<String, String, String>> arcs = new ArrayList<>();
        JsonArray arcsArray = jsonObject.get("arcs").getAsJsonArray();
        for (int i = 0; i < arcsArray.size(); i++) {
            JsonObject arcObject = arcsArray.get(i).getAsJsonObject();
            String sourceVertex = arcObject.get("source").getAsString();
            String targetVertex = arcObject.get("target").getAsString();
            String arcType = arcObject.get("arcType").getAsString();
            arcs.add(new Triplet<>(sourceVertex, targetVertex, arcType));
        }
        classifierVertex.setDeserializedArcs(arcs);
    }
}
