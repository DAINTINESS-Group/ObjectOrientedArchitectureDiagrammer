package model.diagram.exportation;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import model.graph.Arc;
import model.graph.ClassifierVertex;

public class ClassifierVertexSerializer implements JsonSerializer<ClassifierVertex> {

    @Override
    public JsonElement serialize(
            ClassifierVertex classifierVertex,
            Type type,
            JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonObject = new JsonObject();
        String name = classifierVertex.getName();
        Path path = classifierVertex.getPath();
        String vertexType = classifierVertex.getVertexType().toString();
        jsonObject.addProperty("name", name);
        jsonObject.addProperty("path", path.toString());
        jsonObject.addProperty("vertexType", vertexType);
        jsonObject.addProperty("coordinate_x", classifierVertex.getCoordinate().x());
        jsonObject.addProperty("coordinate_y", classifierVertex.getCoordinate().y());
        jsonObject.add("methods", serializeMethods(classifierVertex));
        jsonObject.add("fields", serializeFields(classifierVertex));
        jsonObject.add("arcs", serializeArcs(classifierVertex));

        return jsonObject;
    }

    private JsonArray serializeMethods(ClassifierVertex classifierVertex) {
        Gson gson = new Gson();
        List<ClassifierVertex.Method> methods = classifierVertex.getMethods();
        JsonArray methodsArray = new JsonArray(methods.size());
        for (ClassifierVertex.Method method : methods) {
            JsonObject methodObject = new JsonObject();
            String methodName = method.name();
            String returnType = method.returnType();
            String modifier = method.modifier().toString();
            Map<String, String> parameters = method.parameters();
            String json = gson.toJson(parameters);
            methodObject.addProperty("name", methodName);
            methodObject.addProperty("returnType", returnType);
            methodObject.addProperty("modifier", modifier);
            methodObject.addProperty("parameters", json);

            methodsArray.add(methodObject);
        }
        return methodsArray;
    }

    private JsonArray serializeFields(ClassifierVertex classifierVertex) {
        List<ClassifierVertex.Field> fields = classifierVertex.getFields();
        JsonArray fieldsArray = new JsonArray(fields.size());

        for (ClassifierVertex.Field field : fields) {
            JsonObject fieldObject = new JsonObject();
            String fieldName = field.name();
            String returnType = field.type();
            String modifier = field.modifier().toString();
            fieldObject.addProperty("name", fieldName);
            fieldObject.addProperty("returnType", returnType);
            fieldObject.addProperty("modifier", modifier);
            fieldsArray.add(fieldObject);
        }
        return fieldsArray;
    }

    private JsonArray serializeArcs(ClassifierVertex classifierVertex) {
        List<Arc<ClassifierVertex>> arcs = classifierVertex.getArcs();
        JsonArray arcsArray = new JsonArray(arcs.size());

        for (Arc<ClassifierVertex> sinkVertexArc : arcs) {
            JsonObject arcObject = new JsonObject();
            String source = sinkVertexArc.sourceVertex().getName();
            String target = sinkVertexArc.targetVertex().getName();
            String arcType = sinkVertexArc.arcType().toString();
            arcObject.addProperty("source", source);
            arcObject.addProperty("target", target);
            arcObject.addProperty("arcType", arcType);
            arcsArray.add(arcObject);
        }
        return arcsArray;
    }
}
