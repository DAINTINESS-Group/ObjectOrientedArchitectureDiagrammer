package parser;

import model.graph.ArcType;
import model.graph.ClassifierVertex;
import model.graph.PackageVertex;
import model.graph.VertexType;
import parser.factory.Parser;
import parser.factory.ParserType;
import parser.factory.ProjectParserFactory;
import parser.tree.Relationship;
import parser.tree.RelationshipType;
import parser.tree.LeafNode;
import parser.tree.ModifierType;
import parser.tree.NodeType;
import parser.tree.PackageNode;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public class Interpreter {

	private static final ParserType PARSER_TYPE = ParserType.JAVAPARSER;
	private Map<Path, PackageNode> packageNodes;
	private final Map<PackageNode, PackageVertex> packageNodeVertexMap;
	private final Map<LeafNode, ClassifierVertex> leafNodeSinkVertexMap;
	private final Map<Path, PackageVertex> vertices;
	private final Map<Path, ClassifierVertex> sinkVertices;

	public Interpreter() {
		vertices = new HashMap<>();
		sinkVertices = new HashMap<>();
		packageNodeVertexMap = new HashMap<>();
		leafNodeSinkVertexMap = new HashMap<>();
	}

	public void parseProject(Path sourcePackagePath) {
		ProjectParserFactory projectParserFactory = new ProjectParserFactory(PARSER_TYPE);
		Parser projectParser = projectParserFactory.createProjectParser();
		packageNodes = projectParser.parseSourcePackage(sourcePackagePath);
	}

	public void convertTreeToGraph() {
		PackageNodeCleaner packageNodeCleaner = new PackageNodeCleaner(packageNodes);
		packageNodes = packageNodeCleaner.removeNonPackageNodes();
		populateVertexMaps();
		addVertexArcs();
		leafNodeSinkVertexMap.values().forEach(sinkVertex -> sinkVertices.put(sinkVertex.getPath(), sinkVertex));
		packageNodeVertexMap.values().forEach(vertex -> vertices.put(vertex.getPath(), vertex));
	}

	private void populateVertexMaps() {
		for (PackageNode packageNode: packageNodes.values()) {
			PackageVertex vertex = packageNodeVertexMap.computeIfAbsent(packageNode, k ->
			new PackageVertex(packageNode.getPackageNodesPath(), EnumMapper.vertexTypeEnumMap.get(packageNode.getType()),
					packageNode.getParentNode().getName())
					);
			for (LeafNode leafNode: packageNode.getLeafNodes().values()) {
				vertex.addSinkVertex(leafNodeSinkVertexMap.computeIfAbsent(leafNode, k -> createSinkVertex(leafNode)));
			}
		}
		for (PackageNode packageNode: packageNodes.values()) {
			packageNodeVertexMap.get(packageNode).setParentNode(
					packageNodeVertexMap.getOrDefault(packageNode.getParentNode(), new PackageVertex(Paths.get(""), VertexType.PACKAGE, ""))
					);
			for (PackageNode subNode: packageNode.getSubNodes().values()) {
				packageNodeVertexMap.get(packageNode).addNeighbourVertex(packageNodeVertexMap.get(subNode));
			}
		}
	}

	private void addVertexArcs() {
		for (PackageNode packageNode: packageNodes.values()) {
			PackageVertex vertex = packageNodeVertexMap.get(packageNode);
			for (Relationship<PackageNode> relationship: packageNode.getPackageNodeRelationships()) {
				vertex.addArc(vertex, packageNodeVertexMap.get(relationship.getEndingNode()), EnumMapper.edgeEnumMap.get(relationship.getRelationshipType()));
			}
			addSinkVertexArcs(packageNode);
		}
	}

	private void addSinkVertexArcs(PackageNode packageNode) {
		for (LeafNode leafNode: packageNode.getLeafNodes().values()) {
			ClassifierVertex classifierVertex = leafNodeSinkVertexMap.get(leafNode);
			for (Relationship<LeafNode> relationship: leafNode.getLeafNodeRelationships()) {
				classifierVertex.addArc(classifierVertex, leafNodeSinkVertexMap.get(relationship.getEndingNode()), EnumMapper.edgeEnumMap.get(relationship.getRelationshipType()));
			}
		}
	}

	private ClassifierVertex createSinkVertex(LeafNode leafNode) {
		ClassifierVertex classifierVertex = new ClassifierVertex(leafNode.getLeafNodesPath(), leafNode.getName(), EnumMapper.vertexTypeEnumMap.get(leafNode.getType()));
		leafNode.getFields().forEach(field ->
		classifierVertex.addField(field.getValue0(), field.getValue1(), EnumMapper.modifierTypeEnumMap.get(field.getValue2()))
				);
		leafNode.getMethods().forEach((method, parameters) ->
		classifierVertex.addMethod(method.getValue0().split("\\$")[0], method.getValue1(), EnumMapper.modifierTypeEnumMap.get(method.getValue2()), parameters)
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

		private static final EnumMap<NodeType, VertexType> vertexTypeEnumMap = new EnumMap<>(Map.of(
				NodeType.CLASS, VertexType.CLASS,
				NodeType.INTERFACE, VertexType.INTERFACE,
				NodeType.ENUM, VertexType.ENUM,
				NodeType.PACKAGE, VertexType.PACKAGE
				));

		private static final EnumMap<RelationshipType, ArcType> edgeEnumMap = new EnumMap<>(Map.of(
				RelationshipType.DEPENDENCY, ArcType.DEPENDENCY,
				RelationshipType.ASSOCIATION, ArcType.ASSOCIATION,
				RelationshipType.AGGREGATION, ArcType.AGGREGATION,
				RelationshipType.IMPLEMENTATION, ArcType.IMPLEMENTATION,
				RelationshipType.EXTENSION, ArcType.EXTENSION
				));

		private static final EnumMap<ModifierType, model.graph.ModifierType> modifierTypeEnumMap = new EnumMap<>(Map.of(
				ModifierType.PRIVATE, model.graph.ModifierType.PRIVATE,
				ModifierType.PUBLIC, model.graph.ModifierType.PUBLIC,
				ModifierType.PROTECTED, model.graph.ModifierType.PROTECTED,
				ModifierType.PACKAGE_PRIVATE, model.graph.ModifierType.PACKAGE_PRIVATE
				));
	}

}
