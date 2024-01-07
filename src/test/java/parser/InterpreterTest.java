package parser;

import model.graph.Arc;
import model.graph.ClassifierVertex;
import model.graph.PackageVertex;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import parser.tree.LeafNode;
import parser.tree.PackageNode;
import parser.tree.Relationship;
import utils.PathConstructor;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class InterpreterTest
{

    @Test
    public void convertTreeToGraphTest()
    {
        Interpreter interpreter = new Interpreter();
        interpreter.parseProject(Paths.get(String.format("%s%s%s",
                                                         PathConstructor.getCurrentPath(),
                                                         File.separator,
                                                         PathConstructor.constructPath("src",
                                                                                       "test",
                                                                                       "resources",
                                                                                       "LatexEditor",
                                                                                       "src"))));
        interpreter.convertTreeToGraph();
        Map<Path, PackageNode>   packageNodes = interpreter.getPackageNodes();
        Map<Path, PackageVertex> vertices     = interpreter.getVertices();
        assertEquals(packageNodes.size(), vertices.size());

        for (Map.Entry<Path, PackageNode> packageNodeEntry : packageNodes.entrySet())
        {
            assertTrue(vertices.containsKey(packageNodeEntry.getKey()));

            PackageNode   packageNode = packageNodeEntry.getValue();
            PackageVertex vertex      = vertices.get(packageNodeEntry.getKey());
            assertEquals(packageNode.getNodeType().toString(), vertex.getVertexType().toString());
            assertEquals(packageNode.getNodeName(), vertex.getName());

            Map<Path, PackageNode> subNodes   = packageNode.getSubNodes();
            List<PackageVertex>    neighbours = vertex.getNeighbourVertices();
            assertEquals(subNodes.size(), neighbours.size());
            for (Map.Entry<Path, PackageNode> subNode : subNodes.entrySet())
            {
                assertTrue(neighbours
                               .stream()
                               .anyMatch(neighbour1 ->
                                             neighbour1.getPath().toString().equals(subNode.getKey().toString()) &&
                                             neighbour1.getName().equals(subNode.getValue().getNodeName()) &&
                                             neighbour1.getVertexType().toString().equals(subNode.getValue().getNodeType().toString())));
            }

            Map<PackageNode, Set<Relationship<PackageNode>>> packageNodeRelationships = interpreter.getPackageNodeRelationships();

            if (!packageNodeRelationships.containsKey(packageNode))
            {
                continue;
            }

            Set<Relationship<PackageNode>> relationships = packageNodeRelationships.get(packageNode);
            List<Arc<PackageVertex>>       arcs          = vertex.getArcs();
            assertEquals(relationships.size(), arcs.size());
            for (Relationship<PackageNode> relationship : relationships)
            {
                assertTrue(arcs
                               .stream()
                               .anyMatch(a ->
                                             a.arcType().toString().equals(relationship.relationshipType().toString()) &&
                                             a.sourceVertex().getPath().equals(relationship.startingNode().getPath()) &&
                                             a.targetVertex().getPath().equals(relationship.endingNode().getPath())));
            }

            Map<String, LeafNode>  leafNodes    = packageNode.getLeafNodes();
            List<ClassifierVertex> sinkVertices = vertex.getSinkVertices();
            assertEquals(leafNodes.size(), sinkVertices.size());
            for (Map.Entry<String, LeafNode> leafNodeEntry : leafNodes.entrySet())
            {
                ClassifierVertex classifierVertex = sinkVertices
                    .stream()
                    .filter(sinkVertex1 ->
                                sinkVertex1.getName().equals(leafNodeEntry.getKey())).findAny().orElseGet(Assertions::fail);

                List<LeafNode.Method>         leafMethods   = leafNodeEntry.getValue().methods();
                List<ClassifierVertex.Method> vertexMethods = classifierVertex.getMethods();
                for (LeafNode.Method leafMethod : leafMethods)
                {
                    assertTrue(vertexMethods
                                   .stream()
                                   .anyMatch(m ->
                                                 m.name().equals(leafMethod.methodName()) &&
                                                 m.parameters().size() == leafMethod.parameters().size() &&
                                                 m.parameters().equals(leafMethod.parameters()) &&
                                                 m.returnType().equals(leafMethod.returnType()) &&
                                                 m.modifier().toString().equals(leafMethod.modifierType().toString())));
                }

                List<LeafNode.Field>         leafFields   = leafNodeEntry.getValue().fields();
                List<ClassifierVertex.Field> vertexFields = classifierVertex.getFields();
                for (LeafNode.Field leafField : leafFields)
                {
                    assertTrue(vertexFields
                                   .stream()
                                   .anyMatch(f ->
                                                 f.name().equals(leafField.fieldNames()) &&
                                                 f.type().equals(leafField.fieldType()) &&
                                                 f.modifier().toString().equals(leafField.modifierType().toString())));
                }

                Map<LeafNode, Set<Relationship<LeafNode>>> leafNodeRelationships = interpreter.getLeafNodeRelationships();

                if (!leafNodeRelationships.containsKey(leafNodeEntry.getValue()))
                {
                    continue;
                }

                Set<Relationship<LeafNode>> subNodeRelationships = leafNodeRelationships.get(leafNodeEntry.getValue());
                List<Arc<ClassifierVertex>> sinkVertexArcs       = classifierVertex.getArcs();
                assertEquals(subNodeRelationships.size(), sinkVertexArcs.size());
                for (Relationship<LeafNode> relationship : subNodeRelationships)
                {
                    assertTrue(sinkVertexArcs
                                   .stream()
                                   .anyMatch(a ->
                                                 a.arcType().toString().equals(relationship.relationshipType().toString()) &&
                                                 a.sourceVertex().getPath().equals(relationship.startingNode().path()) &&
                                                 a.targetVertex().getPath().equals(relationship.endingNode().path())));
                }
            }
        }
    }

}
