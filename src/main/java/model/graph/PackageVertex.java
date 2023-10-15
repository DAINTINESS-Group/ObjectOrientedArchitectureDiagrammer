package model.graph;

import org.javatuples.Pair;
import org.javatuples.Triplet;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class PackageVertex {

	private static final ModifierType 						   VERTEX_MODIFIER_TYPE = ModifierType.PACKAGE_PRIVATE;

	private 	   final List<Arc<PackageVertex>> 			   arcs;
	private 	   final List<ClassifierVertex> 			   sinkVertices;
	private 	   final List<PackageVertex> 				   neighbourVertices;
	private 	   final VertexType 						   vertexType;
	private 	   final Path 								   path;
	private 	   final String 							   name;
	private 	  		 List<Triplet<String, String, String>> deserializedArcs;
	private 	  		 PackageVertex 						   parentPackageVertex;
	private 	  		 double 							   x;
	private 	  		 double 							   y;

	public PackageVertex(Path 		path,
						 VertexType vertexType,
						 String 	parentName) {
		this.path 		  	   = path;
		this.vertexType   	   = vertexType;
		this.arcs 			   = new ArrayList<>();
		this.sinkVertices 	   = new ArrayList<>();
		this.neighbourVertices = new ArrayList<>();
		this.name 			   = (parentName.isEmpty()) ? path.getFileName().toString() : parentName + "." + path.getFileName().toString();
	}

	public void addArc(PackageVertex sourceVertex, PackageVertex targetVertex, ArcType arcType) {
		this.arcs.add(new Arc<>(sourceVertex, targetVertex, arcType));
	}

	public void addSinkVertex(ClassifierVertex classifierVertex) {
		this.sinkVertices.add(classifierVertex);
	}

	public void addNeighbourVertex(PackageVertex vertex) {
		this.neighbourVertices.add(vertex);
	}

	public void setParentNode(PackageVertex parentPackageVertex) {
		this.parentPackageVertex = parentPackageVertex;
	}

	public void setDeserializedArcs(List<Triplet<String, String, String>> deserializedArcs) {
		this.deserializedArcs = deserializedArcs;
	}

	public List<Triplet<String, String, String>> getDeserializedArcs() {
		return this.deserializedArcs;
	}

	public List<Arc<PackageVertex>> getArcs() {
		return this.arcs;
	}

	public List<ClassifierVertex> getSinkVertices() {
		return this.sinkVertices;
	}

	public List<PackageVertex> getNeighbourVertices() {
		return this.neighbourVertices;
	}

	public Path getPath() {
		return this.path;
	}

	public String getName() {
		return this.name;
	}

	public VertexType getVertexType() {
		return this.vertexType;
	}

	public PackageVertex getParentVertex() {
		return this.parentPackageVertex;
	}

	public ModifierType getModifierType() { return VERTEX_MODIFIER_TYPE; }

	public void setCoordinates(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public Pair<Double, Double> getCoordinates() {
        return new Pair<>(x, y);
	}
}
