package parser;

import model.graph.ArcType;
import model.graph.ClassifierVertex;
import model.graph.PackageVertex;
import model.graph.VertexType;
import parser.factory.Parser;
import parser.factory.ParserType;
import parser.factory.ProjectParserFactory;
import parser.tree.LeafNode;
import parser.tree.ModifierType;
import parser.tree.NodeType;
import parser.tree.PackageNode;
import parser.tree.Relationship;
import parser.tree.RelationshipType;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Interpreter {

	private static final ParserType PARSER_TYPE = ParserType.JAVAPARSER;

	private final Map<PackageNode, PackageVertex>                  packageNodeVertexMap;
	private final Map<LeafNode,    ClassifierVertex>               leafNodeSinkVertexMap;
	private final Map<Path, 	   PackageVertex>                  vertices;
	private final Map<Path, 	   ClassifierVertex>               sinkVertices;
	private       Map<PackageNode, Set<Relationship<PackageNode>>> packageNodeRelationships;
	private       Map<LeafNode,    Set<Relationship<LeafNode>>>    leafNodeRelationships;
	private		  Map<Path, 	   PackageNode> 	 			   packageNodes;

	public Interpreter() {
		vertices 			  = new HashMap<>();
		sinkVertices 		  = new HashMap<>();
		packageNodeVertexMap  = new HashMap<>();
		leafNodeSinkVertexMap = new HashMap<>();
	}

	public void parseProject(Path sourcePackagePath) {
		Parser projectParser     = ProjectParserFactory.createProjectParser(PARSER_TYPE);
		packageNodes 		     = projectParser.parseSourcePackage(sourcePackagePath);
		leafNodeRelationships    = projectParser.createRelationships(packageNodes);
		packageNodeRelationships = projectParser.identifyPackageNodeRelationships(leafNodeRelationships);
	}

	public void convertTreeToGraph() {
		packageNodes = PackageNodeCleaner.removeNonPackageNodes(packageNodes);
		populateVertexMaps();
		addVertexArcs();
		leafNodeSinkVertexMap.values()
			.forEach(sinkVertex -> sinkVertices.put(sinkVertex.getPath(), sinkVertex));
		packageNodeVertexMap.values()
			.forEach(vertex -> vertices.put(vertex.getPath(), vertex));
	}

	private void populateVertexMaps() {
		for (PackageNode packageNode: packageNodes.values()) {
			PackageVertex vertex = packageNodeVertexMap
				.computeIfAbsent(packageNode, k ->
					new PackageVertex(packageNode.getPath(),
									  TypeConverter.convertVertexType(packageNode.getNodeType()),
									  packageNode.getParentNode().getNodeName()));

			for (LeafNode leafNode: packageNode.getLeafNodes().values()) {
				vertex.addSinkVertex(leafNodeSinkVertexMap.computeIfAbsent(leafNode, k -> createSinkVertex(leafNode)));
			}
		}
		for (PackageNode packageNode: packageNodes.values()) {
			packageNodeVertexMap.get(packageNode)
				.setParentNode(packageNodeVertexMap.getOrDefault(packageNode.getParentNode(),
													  			 new PackageVertex(Paths.get(""),
																 				   VertexType.PACKAGE,
																 				   "")));

			for (PackageNode subNode: packageNode.getSubNodes().values()) {
				packageNodeVertexMap.get(packageNode).addNeighbourVertex(packageNodeVertexMap.get(subNode));
			}
		}
	}

	private void addVertexArcs() {
		for (PackageNode packageNode: packageNodes.values()) {
			PackageVertex vertex = packageNodeVertexMap.get(packageNode);
			if (packageNodeRelationships.containsKey(packageNode)) {
				for (Relationship<PackageNode> relationship : packageNodeRelationships.get(packageNode)) {
					vertex.addArc(vertex,
								  packageNodeVertexMap.get(relationship.endingNode()),
								  TypeConverter.convertRelationshipType(relationship.relationshipType()));
				}
				addSinkVertexArcs(packageNode);
			}
		}
	}

	private void addSinkVertexArcs(PackageNode packageNode) {
		for (LeafNode leafNode: packageNode.getLeafNodes().values()) {
			ClassifierVertex classifierVertex = leafNodeSinkVertexMap.get(leafNode);
			if (leafNodeRelationships.containsKey(leafNode)) {
				for (Relationship<LeafNode> relationship : leafNodeRelationships.get(leafNode)) {
					classifierVertex.addArc(classifierVertex,
											leafNodeSinkVertexMap.get(relationship.endingNode()),
											TypeConverter.convertRelationshipType(relationship.relationshipType()));
				}
			}
		}
	}

	private ClassifierVertex createSinkVertex(LeafNode leafNode) {
		ClassifierVertex classifierVertex = new ClassifierVertex(leafNode.path(),
																 leafNode.nodeName(),
																 TypeConverter.convertVertexType(leafNode.nodeType()));
		leafNode.fields()
			.forEach(field ->
				classifierVertex.addField(field.fieldNames(),
										  field.fieldType(),
										  TypeConverter.convertModifierType(field.modifierType())));
		leafNode.methods()
			.forEach((method) ->
				classifierVertex.addMethod(method.methodName(),
										   method.returnType(),
										   TypeConverter.convertModifierType(method.modifierType()),
										   method.parameters()));
		return classifierVertex;
	}

	public Map<Path, PackageNode> getPackageNodes() {
		return packageNodes;
	}

	public Map<Path, PackageVertex> getVertices() {
		return vertices;
	}

	public Map<Path, ClassifierVertex> getSinkVertices() {
		return sinkVertices;
	}

	public Map<PackageNode, Set<Relationship<PackageNode>>> getPackageNodeRelationships() {
		return packageNodeRelationships;
	}

	public Map<LeafNode, Set<Relationship<LeafNode>>> getLeafNodeRelationships() {
		return leafNodeRelationships;
	}

	private static class TypeConverter {

		private static VertexType convertVertexType(NodeType nodeType) {
			return switch (nodeType) {
				case CLASS 	   -> VertexType.CLASS;
				case INTERFACE -> VertexType.INTERFACE;
				case ENUM 	   -> VertexType.ENUM;
				case PACKAGE   -> VertexType.PACKAGE;
			};
		}

		private static ArcType convertRelationshipType(RelationshipType relationshipType) {
			return switch (relationshipType) {
				case DEPENDENCY  	-> ArcType.DEPENDENCY;
				case ASSOCIATION 	-> ArcType.ASSOCIATION;
				case AGGREGATION 	-> ArcType.AGGREGATION;
				case IMPLEMENTATION -> ArcType.IMPLEMENTATION;
				case EXTENSION 		-> ArcType.EXTENSION;
			};
		}

		private static model.graph.ModifierType convertModifierType(ModifierType modifierType) {
			return switch (modifierType) {
				case PRIVATE 		 -> model.graph.ModifierType.PRIVATE;
				case PUBLIC 		 -> model.graph.ModifierType.PUBLIC;
				case PROTECTED  	 -> model.graph.ModifierType.PROTECTED;
				case PACKAGE_PRIVATE -> model.graph.ModifierType.PACKAGE_PRIVATE;
			};
		}
	}

}
