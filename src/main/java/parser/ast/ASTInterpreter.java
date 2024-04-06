package parser.ast;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import model.graph.ArcType;
import model.graph.ClassifierVertex;
import model.graph.PackageVertex;
import model.graph.VertexType;
import parser.Interpreter;
import parser.ast.tree.LeafNode;
import parser.ast.tree.ModifierType;
import parser.ast.tree.NodeType;
import parser.ast.tree.PackageNode;
import parser.ast.tree.Relationship;
import parser.ast.tree.RelationshipType;

public class ASTInterpreter implements Interpreter {

    private final ASTParser parser = new ASTParser();

    private final Map<PackageNode, PackageVertex> packageNodeVertexMap = new HashMap<>();
    private final Map<LeafNode, ClassifierVertex> leafNodeSinkVertexMap = new HashMap<>();
    private Map<PackageNode, Set<Relationship<PackageNode>>> packageNodeRelationships;
    private Map<LeafNode, Set<Relationship<LeafNode>>> leafNodeRelationships;
    private Map<Path, PackageNode> packageNodes;

    @Override
    public void parseProject(Path sourcePackagePath) {
        packageNodes = parser.parsePackage(sourcePackagePath);
        leafNodeRelationships = parser.createRelationships(packageNodes);
        packageNodeRelationships = parser.identifyPackageNodeRelationships(leafNodeRelationships);
    }

    @Override
    public void convertToGraph() {
        packageNodes = PackageNodeCleaner.removeNonPackageNodes(packageNodes);
        populateVertexMaps(packageNodes);
        addVertexArcs(packageNodes);
    }

    public List<ClassifierVertex> getSinkVertices() {
        return leafNodeSinkVertexMap.values().stream().toList();
    }

    public List<PackageVertex> getVertices() {
        return packageNodeVertexMap.values().stream().toList();
    }

    private void populateVertexMaps(Map<Path, PackageNode> packageNodes) {
        for (PackageNode packageNode : packageNodes.values()) {
            PackageVertex vertex =
                    packageNodeVertexMap.computeIfAbsent(
                            packageNode,
                            k ->
                                    new PackageVertex.PackageVertexBuilder()
                                            .withPath(k.getPath())
                                            .withVertexType(
                                                    TypeConverter.convertVertexType(
                                                            k.getNodeType()))
                                            .withName(
                                                    String.join(
                                                            ".",
                                                            k.getParentNode().getNodeName(),
                                                            k.getPath().getFileName().toString()))
                                            .build());

            for (LeafNode leafNode : packageNode.getLeafNodes().values()) {
                ClassifierVertex classifierVertex =
                        leafNodeSinkVertexMap.computeIfAbsent(
                                leafNode, ASTInterpreter::createSinkVertex);
                vertex.addSinkVertex(classifierVertex);
            }
        }

        for (PackageNode packageNode : packageNodes.values()) {
            Path path = Paths.get("");
            packageNodeVertexMap
                    .get(packageNode)
                    .setParentNode(
                            packageNodeVertexMap.getOrDefault(
                                    packageNode.getParentNode(),
                                    new PackageVertex.PackageVertexBuilder()
                                            .withPath(path)
                                            .withName(path.getFileName().toString())
                                            .build()));

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
                new ClassifierVertex.ClassifierVertexBuilder()
                        .withPath(leafNode.path())
                        .withName(leafNode.nodeName())
                        .withVertexType(TypeConverter.convertVertexType(leafNode.nodeType()))
                        .build();

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
                    method.parameters().values());
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

        private static model.graph.ModifierType convertModifierType(ModifierType modifierType) {
            return switch (modifierType) {
                case PRIVATE -> model.graph.ModifierType.PRIVATE;
                case PUBLIC -> model.graph.ModifierType.PUBLIC;
                case PROTECTED -> model.graph.ModifierType.PROTECTED;
                case PACKAGE_PRIVATE -> model.graph.ModifierType.PACKAGE_PRIVATE;
            };
        }
    }
}
