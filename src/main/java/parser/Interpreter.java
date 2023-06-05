package parser;

import javafx.util.Pair;
import model.graph.ArcType;
import model.graph.SinkVertex;
import model.graph.Vertex;
import model.graph.VertexType;
import parser.tree.edge.Relationship;
import parser.tree.edge.RelationshipType;
import parser.tree.node.LeafNode;
import parser.tree.node.ModifierType;
import parser.tree.node.NodeType;
import parser.tree.node.PackageNode;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public class Interpreter {

    private static final ParserType PARSER_TYPE = ParserType.JAVAPARSER;
    private Map<Path, PackageNode> packageNodes;
    private final Map<PackageNode, Vertex> vertices;
    private final Map<LeafNode, SinkVertex> sinkVertices;

    public Interpreter() {
        vertices = new HashMap<>();
        sinkVertices = new HashMap<>();
    }

    public void parseProject(Path sourcePackagePath) {
        ProjectParserFactory projectParserFactory = new ProjectParserFactory(PARSER_TYPE);
        Parser projectParser = projectParserFactory.createProjectParser();
        projectParser.parseSourcePackage(sourcePackagePath);
        packageNodes = projectParser.getPackageNodes();
    }

    public Pair<ArrayList<Vertex>, ArrayList<SinkVertex>> convertTreeToGraph() {
        populateVertexMaps();
        addVertexArcs();
        return new Pair<>(new ArrayList<>(vertices.values()), new ArrayList<>(sinkVertices.values()));
    }

    private void populateVertexMaps() {
        for (PackageNode packageNode: packageNodes.values()) {
            Vertex vertex = vertices.computeIfAbsent(packageNode, k ->
                new Vertex(packageNode.getNodesPath(), EnumMapper.vertexTypeEnumMap.get(packageNode.getType()), packageNode.getParentNode().getName())
            );
            for (LeafNode leafNode: packageNode.getLeafNodes().values()) {
                vertex.addSinkVertex(sinkVertices.computeIfAbsent(leafNode, k -> createSinkVertex(leafNode)));
            }
        }
        for (PackageNode packageNode: packageNodes.values()) {
            vertices.get(packageNode).setParentNode(
                    vertices.getOrDefault(packageNode.getParentNode(), new Vertex(Paths.get(""), VertexType.PACKAGE, ""))
            );
            for (PackageNode subNode: packageNode.getSubNodes().values()) {
                vertices.get(packageNode).addNeighbourVertex(vertices.get(subNode));
            }
        }
    }

    private void addVertexArcs() {
        for (PackageNode packageNode: packageNodes.values()) {
            Vertex vertex = vertices.get(packageNode);
            for (Relationship relationship: packageNode.getNodeRelationships()) {
                vertex.addArc(vertex, vertices.get((PackageNode) relationship.getEndingNode()),
                        EnumMapper.edgeEnumMap.get(relationship.getRelationshipType()));
            }
            addSinkVertexArcs(packageNode);
        }
    }

    private void addSinkVertexArcs(PackageNode packageNode) {
        for (LeafNode leafNode: packageNode.getLeafNodes().values()) {
            SinkVertex sinkVertex = sinkVertices.get(leafNode);
            for (Relationship relationship: leafNode.getNodeRelationships()) {
                sinkVertex.addArc(sinkVertex, sinkVertices.get((LeafNode) relationship.getEndingNode()),
                        EnumMapper.edgeEnumMap.get(relationship.getRelationshipType()));
            }
        }
    }

    private SinkVertex createSinkVertex(LeafNode leafNode) {
        SinkVertex sinkVertex = new SinkVertex(leafNode.getNodesPath(), leafNode.getName(), EnumMapper.vertexTypeEnumMap.get(leafNode.getType()));
        leafNode.getFields().forEach(field ->
            sinkVertex.addField(field.getValue0(), field.getValue1(), EnumMapper.modifierTypeEnumMap.get(field.getValue2()))
        );
        leafNode.getMethods().forEach((method, parameters) ->
            sinkVertex.addMethod(method.getValue0().split("\\$")[0], method.getValue1(), EnumMapper.modifierTypeEnumMap.get(method.getValue2()), parameters)
        );
        return sinkVertex;
    }

    public Map<Path, PackageNode> getPackageNodes() {
        return packageNodes;
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
