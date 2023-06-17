package model.diagram.javafx;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import model.graph.ModifierType;
import model.graph.SinkVertex;
import model.graph.VertexType;
import org.javatuples.Triplet;

import java.lang.reflect.Type;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SinkVertexDeserializer implements JsonDeserializer<SinkVertex> {
    private SinkVertex sinkVertex;

    @Override
    public SinkVertex deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        String name = jsonObject.get("name").getAsString();
        String path = jsonObject.get("path").getAsString();
        String vertexType = jsonObject.get("vertexType").getAsString();
        //TODO
        // both here and in the VertexSerializer we need to check if we are loading the correct diagram type,
        // e.g. class && package diagrams respectively
        // otherwise we need to throw an exception
        /*
        if (VertexType.valueOf(vertexType.toUpperCase()).equals(VertexType.PACKAGE)) {
            throw new JsonParseException("Tried to load a package diagram");
        }
        */

        sinkVertex = new SinkVertex(Path.of(path), name, VertexType.valueOf(vertexType));

        deserializeMethods(jsonObject);
        deserializeFields(jsonObject);
        deserializeArcs(jsonObject);

        return sinkVertex;
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
            Map<String, String> methodParameters = gson.fromJson(parameters, new TypeToken<>() {}.getType());

            sinkVertex.addMethod(methodName, returnType, ModifierType.valueOf(modifier), methodParameters);
        }
    }

    private void deserializeFields(JsonObject jsonObject) {
        JsonArray fields = jsonObject.get("fields").getAsJsonArray();
        for (int i = 0; i < fields.size(); i++) {
            JsonObject fieldObject = fields.get(i).getAsJsonObject();

            String fieldName = fieldObject.get("name").getAsString();
            String returnType = fieldObject.get("returnType").getAsString();
            String modifierType = fieldObject.get("modifier").getAsString();

            sinkVertex.addField(fieldName, returnType, ModifierType.valueOf(modifierType));
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
        sinkVertex.setDeserializedArcs(arcs);
    }

}
