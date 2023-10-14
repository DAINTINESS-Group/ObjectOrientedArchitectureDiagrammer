package manager;

import model.diagram.GraphClassDiagramConverter;
import model.diagram.ShadowCleaner;
import model.diagram.arrangement.ClassDiagramArrangementManager;
import model.diagram.arrangement.DiagramArrangementManagerInterface;
import model.diagram.exportation.DiagramExporter;
import model.diagram.exportation.GraphMLClassDiagramExporter;
import model.diagram.exportation.JavaFXClassDiagramExporter;
import model.diagram.graphml.GraphMLClassifierVertex;
import model.diagram.graphml.GraphMLClassifierVertexArc;
import model.graph.Arc;
import model.graph.ClassifierVertex;
import model.graph.PackageVertex;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import parser.Interpreter;
import utils.PathConstructor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ClassDiagramManagerTest {

	@Test
	void createSourceProjectTest() {
		ClassDiagramManager classDiagramManager = new ClassDiagramManager();
		SourceProject sourceProject = classDiagramManager.createSourceProject(Paths.get(PathConstructor.getCurrentPath() + File.separator + PathConstructor.constructPath("src", "test", "resources", "LatexEditor", "src")));
		Map<Path, PackageVertex> vertices = sourceProject.getInterpreter().getVertices();
		Interpreter interpreter = new Interpreter();
		interpreter.parseProject(Paths.get(PathConstructor.getCurrentPath() + File.separator + PathConstructor.constructPath("src", "test", "resources", "LatexEditor", "src")));
		interpreter.convertTreeToGraph();
		ArrayList<PackageVertex> interpreterVertices = new ArrayList<>(interpreter.getVertices().values());

		assertEquals(vertices.size(), interpreterVertices.size());
		for (Map.Entry<Path, PackageVertex> vertexEntry: vertices.entrySet()) {
			PackageVertex optionalPackageVertex = interpreterVertices.stream()
					.filter(vertex ->
					vertex.getName().equals(vertexEntry.getValue().getName()) &&
					vertex.getParentVertex().getName().equals(vertexEntry.getValue().getParentVertex().getName()))
					.findFirst().orElseGet(Assertions::fail);

			assertEquals(vertexEntry.getValue().getNeighbourVertices().size(), optionalPackageVertex.getNeighbourVertices().size());
			for (PackageVertex neighbourPackageVertex : vertexEntry.getValue().getNeighbourVertices()) {
				Optional<PackageVertex> optionalNeighbourVertex = optionalPackageVertex.getNeighbourVertices().stream()
						.filter(neighbour -> neighbour.getName().equals(neighbourPackageVertex.getName()))
						.findAny();
				assertTrue(optionalNeighbourVertex.isPresent());
			}

			assertEquals(vertexEntry.getValue().getSinkVertices().size(), optionalPackageVertex.getSinkVertices().size());
			for (ClassifierVertex classifierVertex : vertexEntry.getValue().getSinkVertices()) {
				Optional<ClassifierVertex> optionalSinkVertex = optionalPackageVertex.getSinkVertices().stream()
						.filter(sinkVertex1 -> sinkVertex1.getName().equals(classifierVertex.getName()))
						.findAny();
				assertTrue(optionalSinkVertex.isPresent());
			}
		}
	}

	@Test
	void populateGraphNodesTest() {
		ClassDiagramManager classDiagramManager = new ClassDiagramManager();
		SourceProject sourceProject = classDiagramManager.createSourceProject(Paths.get(PathConstructor.getCurrentPath() + File.separator + PathConstructor.constructPath("src", "test", "resources", "LatexEditor", "src")));
		classDiagramManager.convertTreeToDiagram(List.of("AddLatexCommand", "ChangeVersionsStrategyCommand", "Command", "CommandFactory",
				"CreateCommand", "DisableVersionsManagementCommand", "EditCommand", "EnableVersionsManagementCommand",
				"LoadCommand", "RollbackToPreviousVersionCommand", "SaveCommand"));

		Map<ClassifierVertex, Integer> graphNodes = classDiagramManager.getClassDiagram().getGraphNodes();

		List<String> l1 = new ArrayList<>();
		List<String> l2 = new ArrayList<>();
		assertEquals(sourceProject.getInterpreter().getVertices().get(
			Paths.get(PathConstructor.getCurrentPath().normalize().toString(), PathConstructor.constructPath("src", "test", "resources", "LatexEditor", "src", "controller", "commands")))
		 	.getSinkVertices().size(), graphNodes.size());

		Iterator<ClassifierVertex> iter1 = sourceProject.getInterpreter().getVertices()
			.get(Paths.get(PathConstructor.getCurrentPath().normalize().toString(), PathConstructor.constructPath("src", "test", "resources", "LatexEditor", "src", "controller", "commands")))
			.getSinkVertices().iterator();
		Iterator<Map.Entry<ClassifierVertex, Integer>> iter2 = graphNodes.entrySet().iterator();
		while(iter1.hasNext() || iter2.hasNext()) {
			ClassifierVertex e1 = iter1.next();
			Map.Entry<ClassifierVertex, Integer> e2 = iter2.next();
			l1.add(e1.getName());
			l2.add(e2.getKey().getName());
		}
		Collections.sort(l1);
		Collections.sort(l2);
		assertTrue(l1.size() == l2.size() && l1.containsAll(l2) && l2.containsAll(l1));
	}

	@Test
	void createDiagramTest() {
		ClassDiagramManager classDiagramManager = new ClassDiagramManager();
		List<String> chosenFiles = Arrays.asList("MainWindow", "LatexEditorView", "OpeningWindow");
		classDiagramManager.createSourceProject(Paths.get(PathConstructor.getCurrentPath() + File.separator + PathConstructor.constructPath("src", "test", "resources", "LatexEditor", "src")));
		classDiagramManager.convertTreeToDiagram(chosenFiles);
		Map<ClassifierVertex, Set<Arc<ClassifierVertex>>> testingCreatedDiagram = classDiagramManager.getClassDiagram().getDiagram();

		Map<ClassifierVertex, Integer> graphNodes = classDiagramManager.getClassDiagram().getGraphNodes();
		GraphClassDiagramConverter graphClassDiagramConverter = new GraphClassDiagramConverter(graphNodes.keySet());
		classDiagramManager.getClassDiagram().setDiagram(graphClassDiagramConverter.convertGraphToClassDiagram());
		ShadowCleaner shadowCleaner = new ShadowCleaner(classDiagramManager.getClassDiagram());
		Map<ClassifierVertex, Set<Arc<ClassifierVertex>>> adjacencyList = shadowCleaner.shadowWeakRelationships();

		assertEquals(adjacencyList, testingCreatedDiagram);
	}

	@Test
	void exportDiagramToGraphMLTest() {
		try {
			ClassDiagramManager classDiagramManager = new ClassDiagramManager();
			List<String> chosenFiles = Arrays.asList("MainWindow", "LatexEditorView", "OpeningWindow");
			classDiagramManager.createSourceProject(Paths.get(PathConstructor.getCurrentPath() + File.separator + PathConstructor.constructPath("src", "test", "resources", "LatexEditor", "src")));
			classDiagramManager.convertTreeToDiagram(chosenFiles);
			classDiagramManager.arrangeDiagram();
			File actualFile = classDiagramManager.exportDiagramToGraphML(Paths.get(PathConstructor.getCurrentPath() + File.separator + PathConstructor.constructPath("src", "test", "resources", "testingExportedFile.graphML")));

			DiagramArrangementManagerInterface classDiagramArrangement = new ClassDiagramArrangementManager(classDiagramManager.getClassDiagram());
			classDiagramManager.getClassDiagram().setGraphMLDiagramGeometry(classDiagramArrangement.arrangeGraphMLDiagram());
			GraphMLClassifierVertex graphMLClassifierVertex = new GraphMLClassifierVertex(classDiagramManager.getClassDiagram());
			graphMLClassifierVertex.convertSinkVertex();
			GraphMLClassifierVertexArc graphMLClassifierVertexArc = new GraphMLClassifierVertexArc(classDiagramManager.getClassDiagram());
			graphMLClassifierVertexArc.convertSinkVertexArc();

			DiagramExporter graphMLExporter = new GraphMLClassDiagramExporter(classDiagramManager.getClassDiagram());
			File expectedFile = graphMLExporter.exportDiagram(Paths.get(PathConstructor.getCurrentPath() + File.separator + PathConstructor.constructPath("src", "test", "resources", "testingExportedFile.graphML")));
			assertTrue(FileUtils.contentEquals(expectedFile, actualFile));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	void saveDiagramTest() {
		try {
			ClassDiagramManager classDiagramManager = new ClassDiagramManager();
			List<String> chosenFiles = Arrays.asList("MainWindow", "LatexEditorView", "OpeningWindow");
			classDiagramManager.createSourceProject(Paths.get(PathConstructor.getCurrentPath() + File.separator + PathConstructor.constructPath("src", "test", "resources", "LatexEditor", "src")));
			classDiagramManager.convertTreeToDiagram(chosenFiles);

			File testingSavedFile = classDiagramManager.saveDiagram(Paths.get(PathConstructor.getCurrentPath() + File.separator + PathConstructor.constructPath("src", "test", "resources", "testingSavedFile.txt")));
			DiagramExporter javaFXExporter = new JavaFXClassDiagramExporter(classDiagramManager.getClassDiagram());
			assertTrue(FileUtils.contentEquals(javaFXExporter.exportDiagram(Paths.get(PathConstructor.getCurrentPath() + File.separator + PathConstructor.constructPath("src", "test", "resources", "testingExportedFile.txt"))), testingSavedFile));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	void loadDiagramTest() {
		ClassDiagramManager classDiagramManager = new ClassDiagramManager();
		List<String> chosenFiles = Arrays.asList("MainWindow", "LatexEditorView", "OpeningWindow");
		classDiagramManager.createSourceProject(Paths.get(PathConstructor.getCurrentPath() + File.separator + PathConstructor.constructPath("src", "test", "resources", "LatexEditor", "src")));
		classDiagramManager.convertTreeToDiagram(chosenFiles);
		Map<ClassifierVertex, Set<Arc<ClassifierVertex>>> createdDiagram = classDiagramManager.getClassDiagram().getDiagram();
		classDiagramManager.saveDiagram(Paths.get(PathConstructor.getCurrentPath() + File.separator + PathConstructor.constructPath("src", "test", "resources", "testingExportedFile.txt")));
		classDiagramManager.loadDiagram(Paths.get(PathConstructor.getCurrentPath() + File.separator + PathConstructor.constructPath("src", "test", "resources", "testingExportedFile.txt")));
		Map<ClassifierVertex, Set<Arc<ClassifierVertex>>> loadedDiagram = classDiagramManager.getClassDiagram().getDiagram();

		for (ClassifierVertex classifierVertex : createdDiagram.keySet()) {
			Optional<ClassifierVertex> optionalSinkVertex = loadedDiagram.keySet().stream().filter(sinkVertex1 ->
			sinkVertex1.getName().equals(classifierVertex.getName())
					).findFirst();
			assertTrue(optionalSinkVertex.isPresent());

			assertEquals(createdDiagram.get(classifierVertex).size(), loadedDiagram.get(optionalSinkVertex.get()).size());
			for (Arc<ClassifierVertex> arc: createdDiagram.get(classifierVertex)) {
				assertTrue(
					loadedDiagram.get(optionalSinkVertex.get()).stream()
					.anyMatch(a ->
						  a.sourceVertex().getName().equals(arc.sourceVertex().getName()) &&
						  a.targetVertex().getName().equals(arc.targetVertex().getName()) &&
						  a.arcType().equals(arc.arcType())
					));
			}
		}
	}
}
