package model.graph;

import org.javatuples.Pair;
import org.javatuples.Triplet;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class PackageVertex {

	private static final ModifierType VERTEX_MODIFIER_TYPE = ModifierType.PACKAGE_PRIVATE;
	private final List<Arc<PackageVertex>> arcs;
	private final List<ClassifierVertex> sinkVertices;
	private final List<PackageVertex> neighbourVertices;
	private final VertexType vertexType;
	private final Path path;
	private final String name;
	private PackageVertex parentPackageVertex;
	private List<Triplet<String, String, String>> deserializedArcs;
	private double x;
	private double y;

	public PackageVertex(Path path, VertexType vertexType, String parentName) {
		this.path = path;
		this.vertexType = vertexType;
		arcs = new ArrayList<>();
		sinkVertices = new ArrayList<>();
		neighbourVertices = new ArrayList<>();
		name = (parentName.equals("")) ? path.getFileName().toString() : parentName + "." + path.getFileName().toString();
	}

	public void addArc(PackageVertex sourceVertex, PackageVertex targetVertex, ArcType arcType) {
		arcs.add(new Arc<>(sourceVertex, targetVertex, arcType));
	}

	public void addSinkVertex(ClassifierVertex classifierVertex) {
		sinkVertices.add(classifierVertex);
	}

	public void addNeighbourVertex(PackageVertex vertex) {
		neighbourVertices.add(vertex);
	}

	public void setParentNode(PackageVertex parentPackageVertex) {
		this.parentPackageVertex = parentPackageVertex;
	}

	public void setDeserializedArcs(List<Triplet<String, String, String>> deserializedArcs) {
		this.deserializedArcs = deserializedArcs;
	}

	public List<Triplet<String, String, String>> getDeserializedArcs() {
		return deserializedArcs;
	}

	public List<Arc<PackageVertex>> getArcs() {
		return arcs;
	}

	public List<ClassifierVertex> getSinkVertices() {
		return sinkVertices;
	}

	public List<PackageVertex> getNeighbourVertices() {
		return neighbourVertices;
	}

	public Path getPath() {
		return path;
	}

	public String getName() {
		return name;
	}

	public VertexType getVertexType() {
		return vertexType;
	}

	public PackageVertex getParentVertex() {
		return parentPackageVertex;
	}

	public ModifierType getModifierType() { return VERTEX_MODIFIER_TYPE; }

	public void setCoordinates(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public Pair<Double, Double> getCoordinates(){
		Pair<Double, Double> coordinates = new Pair<>(x, y);
		return coordinates;
	}
}
