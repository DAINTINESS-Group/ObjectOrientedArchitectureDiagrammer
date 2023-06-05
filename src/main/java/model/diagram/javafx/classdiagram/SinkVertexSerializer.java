package model.diagram.javafx.classdiagram;

import com.google.gson.*;
import model.graph.Arc;
import model.graph.SinkVertex;

import java.lang.reflect.Type;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class SinkVertexSerializer implements JsonSerializer<SinkVertex> {

    private SinkVertex sinkVertex;

    @Override
    public JsonElement serialize(SinkVertex sinkVertex, Type type, JsonSerializationContext jsonSerializationContext) {
        this.sinkVertex = sinkVertex;
        JsonObject jsonObject = new JsonObject();

        String name = sinkVertex.getName();
        Path path = sinkVertex.getPath();
        String vertexType = sinkVertex.getVertexType().toString();

        jsonObject.addProperty("name", name);
        jsonObject.addProperty("path", path.toString());
        jsonObject.addProperty("vertexType", vertexType);

        jsonObject.add("methods", serializeMethods());
        jsonObject.add("fields", serializeFields());
        jsonObject.add("arcs", serializeArcs());

        return jsonObject;
    }

    private JsonArray serializeMethods() {
        Gson gson = new Gson();
        List<SinkVertex.Method> methods = sinkVertex.getMethods();
        JsonArray methodsArray = new JsonArray(methods.size());
        for (SinkVertex.Method method: methods) {
            JsonObject methodObject = new JsonObject();
            String methodName = method.getName();
            String returnType = method.getReturnType();
            String modifier = method.getModifierType().toString();
            Map<String, String> parameters = method.getParameters();
            String json = gson.toJson(parameters);

            methodObject.addProperty("name", methodName);
            methodObject.addProperty("returnType", returnType);
            methodObject.addProperty("modifier", modifier);
            methodObject.addProperty("parameters", json);

            methodsArray.add(methodObject);
        }
        return methodsArray;
    }

    private JsonArray serializeFields() {
        List<SinkVertex.Field> fields = sinkVertex.getFields();
        JsonArray fieldsArray = new JsonArray(fields.size());
        for (SinkVertex.Field field: fields) {
            JsonObject fieldObject = new JsonObject();

            String fieldName = field.getName();
            String returnType = field.getType();
            String modifier = field.getModifier().toString();

            fieldObject.addProperty("name", fieldName);
            fieldObject.addProperty("returnType", returnType);
            fieldObject.addProperty("modifier", modifier);

            fieldsArray.add(fieldObject);
        }
        return fieldsArray;
    }

    private JsonArray serializeArcs() {
        List<Arc<SinkVertex>> arcs = sinkVertex.getArcs();
        JsonArray arcsArray = new JsonArray(arcs.size());
        for (Arc<SinkVertex> sinkVertexArc : arcs) {
            JsonObject arcObject = new JsonObject();

            String source = sinkVertexArc.getSourceVertex().getName();
            String target = sinkVertexArc.getTargetVertex().getName();
            String arcType = sinkVertexArc.getArcType().toString();

            arcObject.addProperty("source", source);
            arcObject.addProperty("target", target);
            arcObject.addProperty("arcType", arcType);

            arcsArray.add(arcObject);
        }
        return arcsArray;
    }

}
