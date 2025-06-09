package parser.ast;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import model.graph.Arc;
import model.graph.ClassifierVertex;
import model.graph.PackageVertex;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import parser.ast.tree.LeafNode;
import parser.ast.tree.PackageNode;
import parser.ast.tree.Relationship;
import utils.PathTemplate.LatexEditor;

public class ASTInterpreterTest {

    @Test
    public void convertTreeToGraphTest() {
        ASTInterpreter interpreter = new ASTInterpreter();
        interpreter.parseProject(LatexEditor.SRC.path);
        interpreter.convertToGraph();
        Map<Path, PackageNode> packageNodes = interpreter.getPackageNodes();
        List<PackageVertex> vertices = interpreter.getVertices();
        assertEquals(packageNodes.size(), vertices.size());

        for (Map.Entry<Path, PackageNode> packageNodeEntry : packageNodes.entrySet()) {
            PackageNode packageNode = packageNodeEntry.getValue();
            PackageVertex vertex =
                    vertices.stream()
                            .filter(it -> it.getPath().equals(packageNodeEntry.getKey()))
                            .findFirst()
                            .orElseGet(Assertions::fail);
            assertEquals(packageNode.getNodeType().toString(), vertex.getVertexType().toString());
            assertEquals(packageNode.getNodeName(), vertex.getName());

            Map<Path, PackageNode> subNodes = packageNode.getSubNodes();
            Set<PackageVertex> neighbours = vertex.getNeighborVertices();
            assertEquals(subNodes.size(), neighbours.size());
            for (Map.Entry<Path, PackageNode> subNode : subNodes.entrySet()) {
                assertTrue(neighbours.stream().anyMatch(getPackageVertexPredicate(subNode)));
            }

            Map<PackageNode, Set<Relationship<PackageNode>>> packageNodeRelationships =
                    interpreter.getPackageNodeRelationships();

            if (!packageNodeRelationships.containsKey(packageNode)) {
                continue;
            }

            Set<Relationship<PackageNode>> relationships =
                    packageNodeRelationships.get(packageNode);
            Set<Arc<PackageVertex>> arcs = vertex.getArcs();
            assertEquals(relationships.size(), arcs.size());
            for (Relationship<PackageNode> relationship : relationships) {
                assertTrue(arcs.stream().anyMatch(getArcPredicate(relationship)));
            }

            Map<String, LeafNode> leafNodes = packageNode.getLeafNodes();
            Set<ClassifierVertex> sinkVertices = vertex.getSinkVertices();
            assertEquals(leafNodes.size(), sinkVertices.size());
            for (Map.Entry<String, LeafNode> leafNodeEntry : leafNodes.entrySet()) {
                ClassifierVertex classifierVertex =
                        sinkVertices.stream()
                                .filter(it -> it.getName().equals(leafNodeEntry.getKey()))
                                .findAny()
                                .orElseGet(Assertions::fail);

                List<LeafNode.Method> leafMethods = leafNodeEntry.getValue().methods();
                List<ClassifierVertex.Method> vertexMethods = classifierVertex.getMethods();
                for (LeafNode.Method leafMethod : leafMethods) {
                    assertTrue(vertexMethods.stream().anyMatch(getMethodPredicate(leafMethod)));
                }

                List<LeafNode.Field> leafFields = leafNodeEntry.getValue().fields();
                List<ClassifierVertex.Field> vertexFields = classifierVertex.getFields();
                for (LeafNode.Field leafField : leafFields) {
                    assertTrue(vertexFields.stream().anyMatch(getFieldPredicate(leafField)));
                }

                Map<LeafNode, Set<Relationship<LeafNode>>> leafNodeRelationships =
                        interpreter.getLeafNodeRelationships();

                if (!leafNodeRelationships.containsKey(leafNodeEntry.getValue())) {
                    continue;
                }

                Set<Relationship<LeafNode>> subNodeRelationships =
                        leafNodeRelationships.get(leafNodeEntry.getValue());
                Set<Arc<ClassifierVertex>> sinkVertexArcs = classifierVertex.getArcs();
                assertEquals(subNodeRelationships.size(), sinkVertexArcs.size());
                for (Relationship<LeafNode> relationship : subNodeRelationships) {
                    assertTrue(sinkVertexArcs.stream().anyMatch(getPredicate(relationship)));
                }
            }
        }
    }

    @NotNull
    private static Predicate<PackageVertex> getPackageVertexPredicate(
            Map.Entry<Path, PackageNode> subNode) {
        return it ->
                it.getPath().toString().equals(subNode.getKey().toString())
                        && it.getName().equals(subNode.getValue().getNodeName())
                        && it.getVertexType()
                                .toString()
                                .equals(subNode.getValue().getNodeType().toString());
    }

    @NotNull
    private static Predicate<Arc<ClassifierVertex>> getPredicate(
            Relationship<LeafNode> relationship) {
        return it ->
                it.arcType().toString().equals(relationship.relationshipType().toString())
                        && it.sourceVertex().getPath().equals(relationship.startingNode().path())
                        && it.targetVertex().getPath().equals(relationship.endingNode().path());
    }

    @NotNull
    private static Predicate<ClassifierVertex.Field> getFieldPredicate(LeafNode.Field leafField) {
        return it ->
                it.name().equals(leafField.name())
                        && it.type().equals(leafField.fieldType())
                        && it.modifier().toString().equals(leafField.modifierType().toString());
    }

    @NotNull
    private static Predicate<Arc<PackageVertex>> getArcPredicate(
            Relationship<PackageNode> relationship) {
        return it ->
                it.arcType().toString().equals(relationship.relationshipType().toString())
                        && it.sourceVertex().getPath().equals(relationship.startingNode().getPath())
                        && it.targetVertex().getPath().equals(relationship.endingNode().getPath());
    }

    @NotNull
    private static Predicate<ClassifierVertex.Method> getMethodPredicate(
            LeafNode.Method leafMethod) {
        return it ->
                it.name().equals(leafMethod.name())
                        && it.parameters().size() == leafMethod.parameters().size()
                        && it.parameters().containsAll(leafMethod.parameters().values())
                        && leafMethod.parameters().values().containsAll(it.parameters())
                        && it.returnType().equals(leafMethod.returnType())
                        && it.modifier().toString().equals(leafMethod.modifierType().toString());
    }
}
