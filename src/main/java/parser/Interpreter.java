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
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public class Interpreter {

	private static final ParserType PARSER_TYPE = ParserType.JAVAPARSER;

	private final Map<PackageNode, PackageVertex> packageNodeVertexMap;
	private final Map<LeafNode, ClassifierVertex> leafNodeSinkVertexMap;
	private final Map<Path, PackageVertex> 		  vertices;
	private final Map<Path, ClassifierVertex> 	  sinkVertices;
	private		  Map<Path, PackageNode> 		  packageNodes;

	public Interpreter() {
		this.vertices 			   = new HashMap<>();
		this.sinkVertices 		   = new HashMap<>();
		this.packageNodeVertexMap  = new HashMap<>();
		this.leafNodeSinkVertexMap = new HashMap<>();
	}

	public void parseProject(Path sourcePackagePath) {
		Parser projectParser = ProjectParserFactory.createProjectParser(PARSER_TYPE);
		this.packageNodes = projectParser.parseSourcePackage(sourcePackagePath);
	}

	public void convertTreeToGraph() {
		PackageNodeCleaner packageNodeCleaner = new PackageNodeCleaner(this.packageNodes);
		this.packageNodes 					  = packageNodeCleaner.removeNonPackageNodes();
		populateVertexMaps();
		addVertexArcs();
		this.leafNodeSinkVertexMap.values().forEach(sinkVertex -> sinkVertices.put(sinkVertex.getPath(), sinkVertex));
		this.packageNodeVertexMap.values().forEach(vertex -> vertices.put(vertex.getPath(), vertex));
	}

	private void populateVertexMaps() {
		for (PackageNode packageNode: this.packageNodes.values()) {
			PackageVertex vertex = this.packageNodeVertexMap
				.computeIfAbsent(packageNode, k ->
					new PackageVertex(packageNode.getPackageNodesPath(),
									  EnumMapper.vertexTypeEnumMap.get(packageNode.getType()),
									  packageNode.getParentNode().getName())
				);
			for (LeafNode leafNode: packageNode.getLeafNodes().values()) {
				vertex.addSinkVertex(this.leafNodeSinkVertexMap.computeIfAbsent(leafNode, k -> createSinkVertex(leafNode)));
			}
		}
		for (PackageNode packageNode: this.packageNodes.values()) {
			this.packageNodeVertexMap.get(packageNode)
				.setParentNode(
					this.packageNodeVertexMap.getOrDefault(packageNode.getParentNode(),
														   new PackageVertex(Paths.get(""),
																			 VertexType.PACKAGE,
																			 ""))
				);
			for (PackageNode subNode: packageNode.getSubNodes().values()) {
				this.packageNodeVertexMap.get(packageNode).addNeighbourVertex(this.packageNodeVertexMap.get(subNode));
			}
		}
	}

	private void addVertexArcs() {
		for (PackageNode packageNode: this.packageNodes.values()) {
			PackageVertex vertex = this.packageNodeVertexMap.get(packageNode);
			for (Relationship<PackageNode> relationship: packageNode.getPackageNodeRelationships()) {
				vertex.addArc(vertex, this.packageNodeVertexMap.get(relationship.endingNode()), EnumMapper.edgeEnumMap.get(relationship.relationshipType()));
			}
			addSinkVertexArcs(packageNode);
		}
	}

	private void addSinkVertexArcs(PackageNode packageNode) {
		for (LeafNode leafNode: packageNode.getLeafNodes().values()) {
			ClassifierVertex classifierVertex = this.leafNodeSinkVertexMap.get(leafNode);
			for (Relationship<LeafNode> relationship: leafNode.getLeafNodeRelationships()) {
				classifierVertex.addArc(classifierVertex, this.leafNodeSinkVertexMap.get(relationship.endingNode()), EnumMapper.edgeEnumMap.get(relationship.relationshipType()));
			}
		}
	}

	private ClassifierVertex createSinkVertex(LeafNode leafNode) {
		ClassifierVertex classifierVertex = new ClassifierVertex(leafNode.getLeafNodesPath(), leafNode.getName(), EnumMapper.vertexTypeEnumMap.get(leafNode.getType()));
		leafNode.getFields()
			.forEach(field ->
				classifierVertex.addField(field.getValue0(),
										  field.getValue1(),
										  EnumMapper.modifierTypeEnumMap.get(field.getValue2()))
			);
		leafNode.getMethods()
			.forEach((method, parameters) ->
				classifierVertex.addMethod(method.getValue0().split("\\$")[0],
										   method.getValue1(), EnumMapper.modifierTypeEnumMap.get(method.getValue2()),
										   parameters)
			);
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

	private static class EnumMapper {

		private static final EnumMap<NodeType, VertexType> vertexTypeEnumMap = new EnumMap<>(
			Map.of(
				NodeType.CLASS, VertexType.CLASS,
				NodeType.INTERFACE, VertexType.INTERFACE,
				NodeType.ENUM, VertexType.ENUM,
				NodeType.PACKAGE, VertexType.PACKAGE
			)
		);

		private static final EnumMap<RelationshipType, ArcType> edgeEnumMap = new EnumMap<>(
			Map.of(
				RelationshipType.DEPENDENCY, ArcType.DEPENDENCY,
				RelationshipType.ASSOCIATION, ArcType.ASSOCIATION,
				RelationshipType.AGGREGATION, ArcType.AGGREGATION,
				RelationshipType.IMPLEMENTATION, ArcType.IMPLEMENTATION,
				RelationshipType.EXTENSION, ArcType.EXTENSION
			)
		);

		private static final EnumMap<ModifierType, model.graph.ModifierType> modifierTypeEnumMap = new EnumMap<>(
			Map.of(
				ModifierType.PRIVATE, model.graph.ModifierType.PRIVATE,
				ModifierType.PUBLIC, model.graph.ModifierType.PUBLIC,
				ModifierType.PROTECTED, model.graph.ModifierType.PROTECTED,
				ModifierType.PACKAGE_PRIVATE, model.graph.ModifierType.PACKAGE_PRIVATE
			)
		);
	}

}
