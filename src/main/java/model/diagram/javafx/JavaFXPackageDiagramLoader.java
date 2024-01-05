package model.diagram.javafx;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import model.graph.ArcType;
import model.graph.PackageVertex;
import org.javatuples.Triplet;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class JavaFXPackageDiagramLoader {

	private final Path graphSavePath;

	public JavaFXPackageDiagramLoader(Path graphSavePath) {
		this.graphSavePath = graphSavePath;
	}

	public Set<PackageVertex> loadDiagram() throws JsonParseException {
		Set<PackageVertex> vertices 	 = new HashSet<>();
		try {
			byte[] encodedBytes = Files.readAllBytes(graphSavePath);
			String json 		= new String(encodedBytes, StandardCharsets.ISO_8859_1);
			Gson   gson 		= new GsonBuilder().registerTypeAdapter(PackageVertex.class,
																	   new PackageVertexDeserializer())
																	   .create();
			PackageVertex[] verticesArray = gson.fromJson(json, PackageVertex[].class);
			Collections.addAll(vertices, verticesArray);
			deserializeArcs(vertices);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return vertices;
	}

	private void deserializeArcs(Set<PackageVertex> vertices) {
		for (PackageVertex vertex: vertices) {
			List<Triplet<String, String, String>> deserializedArcs = vertex.getDeserializedArcs();
			for (Triplet<String, String, String> arc: deserializedArcs) {
				Optional<PackageVertex> sourceVertex = vertices
					.stream()
					.filter(vertex1 -> vertex1.getName().equals(arc.getValue0()))
					.findFirst();
				Optional<PackageVertex> targetVertex = vertices
					.stream()
					.filter(vertex1 -> vertex1.getName().equals(arc.getValue1()))
					.findFirst();
				if (sourceVertex.isEmpty() || targetVertex.isEmpty()) {
					continue;
				}
				vertex.addArc(sourceVertex.get(), targetVertex.get(), ArcType.get(arc.getValue2()));
			}
		}
	}
}
