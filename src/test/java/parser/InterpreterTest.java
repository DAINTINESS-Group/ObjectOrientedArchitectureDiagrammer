package parser;

import model.graph.Arc;
import model.graph.ClassifierVertex;
import model.graph.PackageVertex;
import parser.tree.Relationship;
import parser.tree.LeafNode;
import parser.tree.ModifierType;
import parser.tree.PackageNode;
import org.javatuples.Triplet;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class InterpreterTest {

	Path currentDirectory = Path.of(".");

	@Test
	public void convertTreeToGraphTest() {
		try {
			Interpreter interpreter = new Interpreter();
			interpreter.parseProject(Paths.get(currentDirectory.toRealPath() + "\\src\\test\\resources\\LatexEditor\\src"));
			interpreter.convertTreeToGraph();
			Map<Path, PackageNode> packageNodes = interpreter.getPackageNodes();
			Map<Path, PackageVertex> vertices = interpreter.getVertices();
			assertEquals(packageNodes.size(), vertices.size());

			for (Map.Entry<Path, PackageNode> packageNodeEntry: packageNodes.entrySet()) {
				assertTrue(vertices.containsKey(packageNodeEntry.getKey()));

				PackageNode packageNode = packageNodeEntry.getValue();
				PackageVertex vertex = vertices.get(packageNodeEntry.getKey());
				assertEquals(packageNode.getType().toString(), vertex.getVertexType().toString());
				assertEquals(packageNode.getName(), vertex.getName());

				Map<Path, PackageNode> subNodes = packageNode.getSubNodes();
				List<PackageVertex> neighbours = vertex.getNeighbourVertices();
				assertEquals(subNodes.size(), neighbours.size());
				for (Map.Entry<Path, PackageNode> subNode: subNodes.entrySet()) {
					Optional<PackageVertex> neighbour = neighbours.stream()
							.filter(neighbour1 ->
							neighbour1.getPath().toString().equals(subNode.getKey().toString()) &&
							neighbour1.getName().equals(subNode.getValue().getName()) &&
							neighbour1.getVertexType().toString().equals(subNode.getValue().getType().toString()))
							.findAny();
					assertTrue(neighbour.isPresent());
				}

				List<Relationship<PackageNode>> relationships = packageNode.getPackageNodeRelationships();
				List<Arc<PackageVertex>> arcs = vertex.getArcs();
				assertEquals(relationships.size(), arcs.size());
				for (Relationship<PackageNode> relationship: relationships) {
					Optional<Arc<PackageVertex>> arc = arcs.stream().filter(a ->
					a.getArcType().toString().equals(relationship.getRelationshipType().toString()) &&
					a.getSourceVertex().getPath().equals(relationship.getStartingNode().getPackageNodesPath()) &&
					a.getTargetVertex().getPath().equals(relationship.getEndingNode().getPackageNodesPath()))
							.findAny();
					assertTrue(arc.isPresent());
				}

				Map<String, LeafNode> leafNodes = packageNode.getLeafNodes();
				List<ClassifierVertex> sinkVertices = vertex.getSinkVertices();
				assertEquals(leafNodes.size(), sinkVertices.size());
				for (Map.Entry<String, LeafNode> leafNodeEntry: leafNodes.entrySet()) {
					ClassifierVertex classifierVertex = sinkVertices.stream().filter(sinkVertex1 ->
					sinkVertex1.getName().equals(leafNodeEntry.getKey())).findAny().orElseGet(Assertions::fail);

					Map<Triplet<String, String, ModifierType>, Map<String, String>> leafMethods = leafNodeEntry.getValue().getMethods();
					List<ClassifierVertex.Method> vertexMethods = classifierVertex.getMethods();
					for (Map.Entry<Triplet<String, String, ModifierType>, Map<String, String>> leafMethod: leafMethods.entrySet()) {
						Optional<ClassifierVertex.Method> vertexMethod = vertexMethods.stream().filter(m ->
						m.getName().equals(leafMethod.getKey().getValue0().split("\\$")[0]) &&
						m.getParameters().size() == leafMethod.getValue().size() &&
						m.getParameters().equals( leafMethod.getValue() ) &&
						m.getReturnType().equals(leafMethod.getKey().getValue1()) &&
						m.getModifierType().toString().equals(leafMethod.getKey().getValue2().toString()))
								.findAny();
						assertTrue(vertexMethod.isPresent());
					}

					List<Triplet<String, String, ModifierType>> leafFields = leafNodeEntry.getValue().getFields();
					List<ClassifierVertex.Field> vertexFields = classifierVertex.getFields();
					for (Triplet<String, String, ModifierType> leafField: leafFields) {
						Optional<ClassifierVertex.Field> vertexField = vertexFields.stream().filter(f ->
						f.getName().equals(leafField.getValue0()) &&
						f.getType().equals(leafField.getValue1()) &&
						f.getModifier().toString().equals(leafField.getValue2().toString()))
								.findAny();
						assertTrue(vertexField.isPresent());
					}

					List<Relationship<LeafNode>> subNodeRelationships = leafNodeEntry.getValue().getLeafNodeRelationships();
					List<Arc<ClassifierVertex>> sinkVertexArcs = classifierVertex.getArcs();
					assertEquals(subNodeRelationships.size(), sinkVertexArcs.size());
					for (Relationship<LeafNode> relationship: subNodeRelationships) {
						Optional<Arc<ClassifierVertex>> arc = sinkVertexArcs.stream().filter(a ->
						a.getArcType().toString().equals(relationship.getRelationshipType().toString()) &&
						a.getSourceVertex().getPath().equals(relationship.getStartingNode().getLeafNodesPath()) &&
						a.getTargetVertex().getPath().equals(relationship.getEndingNode().getLeafNodesPath()))
								.findAny();
						assertTrue(arc.isPresent());
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
