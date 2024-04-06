package parser;

import model.graph.Arc;
import model.graph.ClassifierVertex;
import model.graph.PackageVertex;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import parser.tree.LeafNode;
import parser.tree.PackageNode;
import parser.tree.Relationship;
import utils.PathTemplate.LatexEditor;

import java.nio.file.Path;
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
        interpreter.parseProject(LatexEditor.SRC.path);
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
                               .anyMatch(it -> it.getPath().toString().equals(subNode.getKey().toString()) &&
                                               it.getName().equals(subNode.getValue().getNodeName())       &&
                                               it.getVertexType().toString().equals(subNode.getValue().getNodeType().toString())));
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
                               .anyMatch(it -> it.arcType().toString().equals(relationship.relationshipType().toString()) &&
                                               it.sourceVertex().getPath().equals(relationship.startingNode().getPath())  &&
                                               it.targetVertex().getPath().equals(relationship.endingNode().getPath())));
            }

            Map<String, LeafNode>  leafNodes    = packageNode.getLeafNodes();
            List<ClassifierVertex> sinkVertices = vertex.getSinkVertices();
            assertEquals(leafNodes.size(), sinkVertices.size());
            for (Map.Entry<String, LeafNode> leafNodeEntry : leafNodes.entrySet())
            {
                ClassifierVertex classifierVertex = sinkVertices
                    .stream()
                    .filter(it ->
                                it.getName().equals(leafNodeEntry.getKey()))
                    .findAny()
                    .orElseGet(Assertions::fail);

                List<LeafNode.Method>         leafMethods   = leafNodeEntry.getValue().methods();
                List<ClassifierVertex.Method> vertexMethods = classifierVertex.getMethods();
                for (LeafNode.Method leafMethod : leafMethods)
                {
                    assertTrue(vertexMethods
                                   .stream()
                                   .anyMatch(it -> it.name().equals(leafMethod.methodName())                &&
                                                   it.parameters().size() == leafMethod.parameters().size() &&
                                                   it.parameters().equals(leafMethod.parameters())          &&
                                                   it.returnType().equals(leafMethod.returnType())          &&
                                                   it.modifier().toString().equals(leafMethod.modifierType().toString())));
                }

                List<LeafNode.Field>         leafFields   = leafNodeEntry.getValue().fields();
                List<ClassifierVertex.Field> vertexFields = classifierVertex.getFields();
                for (LeafNode.Field leafField : leafFields)
                {
                    assertTrue(vertexFields
                                   .stream()
                                   .anyMatch(it -> it.name().equals(leafField.fieldNames()) &&
                                                   it.type().equals(leafField.fieldType())  &&
                                                   it.modifier().toString().equals(leafField.modifierType().toString())));
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
                                   .anyMatch(it -> it.arcType().toString().equals(relationship.relationshipType().toString()) &&
                                                   it.sourceVertex().getPath().equals(relationship.startingNode().path())     &&
                                                   it.targetVertex().getPath().equals(relationship.endingNode().path())));
                }
            }
        }
    }

}
