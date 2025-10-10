package gr.uoi.ooad.parser;

import gr.uoi.ooad.model.graph.ArcType;
import gr.uoi.ooad.model.graph.ClassifierVertex;
import gr.uoi.ooad.model.graph.PackageVertex;
import gr.uoi.ooad.model.graph.VertexType;
import gr.uoi.ooad.parser.factory.Parser;
import gr.uoi.ooad.parser.factory.ParserType;
import gr.uoi.ooad.parser.factory.ProjectParserFactory;
import gr.uoi.ooad.parser.tree.LeafNode;
import gr.uoi.ooad.parser.tree.ModifierType;
import gr.uoi.ooad.parser.tree.NodeType;
import gr.uoi.ooad.parser.tree.PackageNode;
import gr.uoi.ooad.parser.tree.Relationship;
import gr.uoi.ooad.parser.tree.RelationshipType;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Interpreter {

    private static final ParserType PARSER_TYPE = ParserType.JAVAPARSER;

    private final Map<PackageNode, PackageVertex> packageNodeVertexMap;
    private final Map<LeafNode, ClassifierVertex> leafNodeSinkVertexMap;
    private Map<PackageNode, Set<Relationship<PackageNode>>> packageNodeRelationships;
    private Map<LeafNode, Set<Relationship<LeafNode>>> leafNodeRelationships;
    private Map<Path, PackageNode> packageNodes;

    public Interpreter() {
        packageNodeVertexMap = new HashMap<>();
        leafNodeSinkVertexMap = new HashMap<>();
    }

    public void parseProject(Path sourcePackagePath) {
        Parser projectParser = ProjectParserFactory.createProjectParser(PARSER_TYPE);
        packageNodes = projectParser.parseSourcePackage(sourcePackagePath);
        leafNodeRelationships = projectParser.createRelationships(packageNodes);
        packageNodeRelationships =
                projectParser.identifyPackageNodeRelationships(leafNodeRelationships);
    }

    public void convertTreeToGraph() {
        packageNodes = PackageNodeCleaner.removeNonPackageNodes(packageNodes);
        populateVertexMaps(packageNodes);
        addVertexArcs(packageNodes);
    }

    public Map<Path, ClassifierVertex> getSinkVertices() {
        return leafNodeSinkVertexMap.values().stream()
                .collect(Collectors.toMap(ClassifierVertex::getPath, it -> it));
    }

    public Map<Path, PackageVertex> getVertices() {
        return packageNodeVertexMap.values().stream()
                .collect(Collectors.toMap(PackageVertex::getPath, it -> it));
    }

    private void populateVertexMaps(Map<Path, PackageNode> packageNodes) {
        for (PackageNode packageNode : packageNodes.values()) {
            PackageVertex vertex =
                    packageNodeVertexMap.computeIfAbsent(
                            packageNode,
                            k ->
                                    new PackageVertex(
                                            k.getPath(),
                                            TypeConverter.convertVertexType(k.getNodeType()),
                                            k.getParentNode().getNodeName()));

            for (LeafNode leafNode : packageNode.getLeafNodes().values()) {
                ClassifierVertex classifierVertex =
                        leafNodeSinkVertexMap.computeIfAbsent(
                                leafNode, Interpreter::createSinkVertex);
                vertex.addSinkVertex(classifierVertex);
            }
        }

        for (PackageNode packageNode : packageNodes.values()) {
            packageNodeVertexMap
                    .get(packageNode)
                    .setParentNode(
                            packageNodeVertexMap.getOrDefault(
                                    packageNode.getParentNode(),
                                    new PackageVertex(Paths.get(""), VertexType.PACKAGE, "")));

            for (PackageNode subNode : packageNode.getSubNodes().values()) {
                packageNodeVertexMap
                        .get(packageNode)
                        .addNeighbourVertex(packageNodeVertexMap.get(subNode));
            }
        }
    }

    private void addVertexArcs(Map<Path, PackageNode> packageNodes) {
        for (PackageNode packageNode : packageNodes.values()) {
            if (!packageNodeRelationships.containsKey(packageNode)) continue;

            PackageVertex vertex = packageNodeVertexMap.get(packageNode);
            for (Relationship<PackageNode> relationship :
                    packageNodeRelationships.get(packageNode)) {
                vertex.addArc(
                        vertex,
                        packageNodeVertexMap.get(relationship.endingNode()),
                        TypeConverter.convertRelationshipType(relationship.relationshipType()));
            }
            addSinkVertexArcs(packageNode);
        }
    }

    private void addSinkVertexArcs(PackageNode packageNode) {
        for (LeafNode leafNode : packageNode.getLeafNodes().values()) {
            if (!leafNodeRelationships.containsKey(leafNode)) continue;

            ClassifierVertex classifierVertex = leafNodeSinkVertexMap.get(leafNode);
            for (Relationship<LeafNode> relationship : leafNodeRelationships.get(leafNode)) {
                classifierVertex.addArc(
                        classifierVertex,
                        leafNodeSinkVertexMap.get(relationship.endingNode()),
                        TypeConverter.convertRelationshipType(relationship.relationshipType()));
            }
        }
    }

    private static ClassifierVertex createSinkVertex(LeafNode leafNode) {
        ClassifierVertex classifierVertex =
                new ClassifierVertex(
                        leafNode.path(),
                        leafNode.nodeName(),
                        TypeConverter.convertVertexType(leafNode.nodeType()));

        for (LeafNode.Field field : leafNode.fields()) {
            classifierVertex.addField(
                    field.name(),
                    field.fieldType(),
                    TypeConverter.convertModifierType(field.modifierType()));
        }

        for (LeafNode.Method method : leafNode.methods()) {
            classifierVertex.addMethod(
                    method.name(),
                    method.returnType(),
                    TypeConverter.convertModifierType(method.modifierType()),
                    method.parameters());
        }

        return classifierVertex;
    }

    public Map<Path, PackageNode> getPackageNodes() {
        return packageNodes;
    }

    public Map<PackageNode, Set<Relationship<PackageNode>>> getPackageNodeRelationships() {
        return packageNodeRelationships;
    }

    public Map<LeafNode, Set<Relationship<LeafNode>>> getLeafNodeRelationships() {
        return leafNodeRelationships;
    }

    /**
     * Utility class to convert {@link NodeType}s, {@link RelationshipType}s and {@link
     * ModifierType}s.
     */
    private static class TypeConverter {

        private static VertexType convertVertexType(NodeType nodeType) {
            return switch (nodeType) {
                case CLASS -> VertexType.CLASS;
                case INTERFACE -> VertexType.INTERFACE;
                case ENUM -> VertexType.ENUM;
                case PACKAGE -> VertexType.PACKAGE;
            };
        }

        private static ArcType convertRelationshipType(RelationshipType relationshipType) {
            return switch (relationshipType) {
                case DEPENDENCY -> ArcType.DEPENDENCY;
                case ASSOCIATION -> ArcType.ASSOCIATION;
                case AGGREGATION -> ArcType.AGGREGATION;
                case IMPLEMENTATION -> ArcType.IMPLEMENTATION;
                case EXTENSION -> ArcType.EXTENSION;
            };
        }

        private static gr.uoi.ooad.model.graph.ModifierType convertModifierType(
                ModifierType modifierType) {
            return switch (modifierType) {
                case PRIVATE -> gr.uoi.ooad.model.graph.ModifierType.PRIVATE;
                case PUBLIC -> gr.uoi.ooad.model.graph.ModifierType.PUBLIC;
                case PROTECTED -> gr.uoi.ooad.model.graph.ModifierType.PROTECTED;
                case PACKAGE_PRIVATE -> gr.uoi.ooad.model.graph.ModifierType.PACKAGE_PRIVATE;
            };
        }
    }
}
