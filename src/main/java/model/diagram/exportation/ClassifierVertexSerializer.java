package model.diagram.exportation;

import com.google.gson.*;
import model.graph.Arc;
import model.graph.ClassifierVertex;

import java.lang.reflect.Type;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class ClassifierVertexSerializer implements JsonSerializer<ClassifierVertex> {

	private ClassifierVertex classifierVertex;

	@Override
	public JsonElement serialize(ClassifierVertex classifierVertex, Type type, JsonSerializationContext jsonSerializationContext) {
		this.classifierVertex = classifierVertex;
		JsonObject jsonObject = new JsonObject();

		String name = classifierVertex.getName();
		Path path = classifierVertex.getPath();
		String vertexType = classifierVertex.getVertexType().toString();

		jsonObject.addProperty("name", name);
		jsonObject.addProperty("path", path.toString());
		jsonObject.addProperty("vertexType", vertexType);
		jsonObject.addProperty("coordinate_x", classifierVertex.getCoordinates().getValue0());
		jsonObject.addProperty("coordinate_y", classifierVertex.getCoordinates().getValue1()); 
		jsonObject.add("methods", serializeMethods());
		jsonObject.add("fields", serializeFields());
		jsonObject.add("arcs", serializeArcs());

		return jsonObject;
	}

	private JsonArray serializeMethods() {
		Gson gson = new Gson();
		List<ClassifierVertex.Method> methods = classifierVertex.getMethods();
		JsonArray methodsArray = new JsonArray(methods.size());
		for (ClassifierVertex.Method method: methods) {
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
		List<ClassifierVertex.Field> fields = classifierVertex.getFields();
		JsonArray fieldsArray = new JsonArray(fields.size());
		for (ClassifierVertex.Field field: fields) {
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
		List<Arc<ClassifierVertex>> arcs = classifierVertex.getArcs();
		JsonArray arcsArray = new JsonArray(arcs.size());
		for (Arc<ClassifierVertex> sinkVertexArc : arcs) {
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
