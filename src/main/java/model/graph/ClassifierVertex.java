package model.graph;

import org.javatuples.Triplet;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ClassifierVertex {

	private final List<Arc<ClassifierVertex>> 		    arcs;
	private final List<Method> 							methods;
	private final List<Field> 							fields;
	private final VertexType 							vertexType;
	private final Path 									path;
	private final String 								name;
	private final VertexCoordinate						coordinate;
	private 	  List<Triplet<String, String, String>> deserializedArcs;

	public ClassifierVertex(Path 	   path,
							String 	   name,
							VertexType vertexType) {
		this.vertexType = vertexType;
		this.path 		= path;
		this.name	    = name;
		arcs 			= new ArrayList<>();
		methods 		= new ArrayList<>();
		fields 			= new ArrayList<>();
		coordinate  	= new VertexCoordinate();
	}


	public void updateCoordinate(double x, double y) {
		coordinate.x = x;
		coordinate.y = y;
	}

	public void addArc(ClassifierVertex sourceVertex, ClassifierVertex targetVertex, ArcType arcType) {
		arcs.add(new Arc<>(sourceVertex, targetVertex, arcType));
	}

	public void addMethod(String name, String returnType, ModifierType modifier, Map<String, String> parameters) {
		methods.add(new Method(name, returnType, modifier, parameters));
	}

	public void addField(String name, String type, ModifierType modifier) {
		fields.add(new Field(name, type, modifier));
	}

	public void setDeserializedArcs(List<Triplet<String, String, String>> deserializedArcs) {
		this.deserializedArcs = deserializedArcs;
	}

	public List<Triplet<String, String, String>> getDeserializedArcs() {
		return deserializedArcs;
	}

	public VertexType getVertexType() {
		return vertexType;
	}

	public List<Arc<ClassifierVertex>> getArcs() {
		return arcs;
	}

	public Path getPath() {
		return path;
	}

	public String getName() {
		return name;
	}

	public List<Method> getMethods() {
		return methods;
	}

	public List<Field> getFields() {
		return fields;
	}

	public VertexCoordinate getCoordinate() {
        return coordinate;
	}


	public record Method(String 			 name,
						 String 			 returnType,
						 ModifierType 		 modifier,
						 Map<String, String> parameters) {}

	public record Field(String 	  	 name,
						String 	  	 type,
						ModifierType modifier) {}

}
