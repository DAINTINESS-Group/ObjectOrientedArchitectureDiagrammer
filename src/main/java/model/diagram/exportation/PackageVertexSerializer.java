package model.diagram.exportation;

import com.google.gson.*;
import model.graph.Arc;
import model.graph.ClassifierVertex;
import model.graph.PackageVertex;

import java.lang.reflect.Type;
import java.nio.file.Path;
import java.util.List;

public class PackageVertexSerializer implements JsonSerializer<PackageVertex> {

	private PackageVertex packageVertex;

	@Override
	public JsonElement serialize(PackageVertex vertex, Type type, JsonSerializationContext jsonSerializationContext) {
		this.packageVertex = vertex;
		JsonObject jsonObject = new JsonObject();

		String name = vertex.getName();
		Path path = vertex.getPath();
		String vertexType = vertex.getVertexType().toString();

		jsonObject.addProperty("name", name);
		jsonObject.addProperty("path", path.toString());
		jsonObject.addProperty("vertexType", vertexType);
		jsonObject.addProperty("coordinate_x", packageVertex.getCoordinates().getValue0());
		jsonObject.addProperty("coordinate_y", packageVertex.getCoordinates().getValue1());   
		jsonObject.add("sinkVertices", serializeSinkVertices());
		jsonObject.add("parent", serializeParentVertex());
		jsonObject.add("neighbours", serializeNeighbourVertices());
		jsonObject.add("arcs", serializeArcs());

		return jsonObject;
	}

	private JsonArray serializeSinkVertices() {
		List<ClassifierVertex> sinkVertices = packageVertex.getSinkVertices();
		JsonArray sinkVerticesArray = new JsonArray(sinkVertices.size());
		for (ClassifierVertex classifierVertex : sinkVertices) {
			Gson gson = new GsonBuilder().registerTypeAdapter(ClassifierVertex.class, new ClassifierVertexSerializer()).create();
			String json = gson.toJson(classifierVertex);
			sinkVerticesArray.add(json);
		}
		return sinkVerticesArray;
	}

	private JsonObject serializeParentVertex() {
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

	private JsonArray serializeNeighbourVertices() {
		List<PackageVertex> neighbourVertices = packageVertex.getNeighbourVertices();
		JsonArray neighbourVerticesArray = new JsonArray(neighbourVertices.size());
		for (PackageVertex v: neighbourVertices) {
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
		List<Arc<PackageVertex>> arcs = packageVertex.getArcs();
		JsonArray arcsArray = new JsonArray(arcs.size());
		for (Arc<PackageVertex> vertexArc : arcs) {
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
