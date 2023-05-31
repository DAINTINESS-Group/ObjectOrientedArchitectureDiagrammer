package model;

import manager.ClassDiagramManager;
import manager.PackageDiagramManager;
import model.diagram.plantuml.PlantUMLLeafEdge;
import model.diagram.plantuml.PlantUMLLeafNode;
import model.diagram.plantuml.PlantUMLPackageEdge;
import model.diagram.plantuml.PlantUMLPackageNode;
import model.graph.Arc;
import model.graph.SinkVertex;
import model.graph.Vertex;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PlantUMLConverterTest {

	Path currentDirectory = Path.of(".");

	@Test
	void checkClassDiagramRelationshipsText() throws IOException{
		List<String> expectedRelationships = new ArrayList<>();
		expectedRelationships.add("StableVersionsStrategy ..|> VersionsStrategy");
		expectedRelationships.add("VersionsStrategyFactory ..> VersionsStrategy");
		expectedRelationships.add("VersionsStrategyFactory ..> StableVersionsStrategy");
		expectedRelationships.add("VersionsStrategyFactory ..> VolatileVersionsStrategy");
		expectedRelationships.add("VersionsStrategyFactory --o VersionsStrategy");
		expectedRelationships.add("VersionsManager --> VersionsStrategy");
		expectedRelationships.add("VersionsManager ..> VersionsStrategy");
		expectedRelationships.add("VolatileVersionsStrategy ..|> VersionsStrategy");
		expectedRelationships.add("VolatileVersionsStrategy --o Document");
		expectedRelationships.add("DocumentManager ..> Document");
		expectedRelationships.add("VolatileVersionsStrategy ..> Document");
		expectedRelationships.add("VersionsManager ..> Document");
		expectedRelationships.add("DocumentManager --o Document");
		expectedRelationships.add("StableVersionsStrategy ..> Document");
		expectedRelationships.add("VersionsStrategy ..> Document");

		ClassDiagramManager classDiagramManager = new ClassDiagramManager();
		SourceProject sourceProject = classDiagramManager.createSourceProject(Paths.get(currentDirectory.toRealPath() + "\\src\\test\\resources\\LatexEditor\\src"));
		classDiagramManager.createDiagram(List.of("StableVersionsStrategy", "VersionsStrategy", "VersionsStrategyFactory", "VolatileVersionsStrategy",
				"VersionsManager", "Document", "DocumentManager"));

		Map<Arc<SinkVertex>, Integer> graphEdges = classDiagramManager.getDiagram().getGraphEdges();

		PlantUMLLeafEdge plantUMLEdge = new PlantUMLLeafEdge(graphEdges);
		plantUMLEdge.convertPlantEdge();
		List<String> actualRelationships = plantUMLEdge.getPlantUMLTester();

		Collections.sort(actualRelationships);
		Collections.sort(expectedRelationships);
		assertEquals(expectedRelationships, actualRelationships);
	}
	
	@Test
	void checkClassDiagramDeclarationsText() throws IOException{
		List<String> expected = new ArrayList<>(List.of(
		"class Document {\n" +
				"-author: String\n" +
				"-date: String\n" +
				"-copyright: String\n" +
				"-versionID: String\n" +
				"-contents: String\n" +
				"+Document(): Constructor\n" +
				"+Document(String date, String copyright, String versionID, String contents, String author): Constructor\n" +
				"+clone(): Document\n" +
				"+save(String filename): void\n" +
				"+setContents(String contents): void\n" +
				"+changeVersion(): void\n" +
				"+getVersionID(): String\n" +
				"+getContents(): String\n" +
				"}\n",
				"class StableVersionsStrategy {\n" +
				"-versionID: String\n" +
				"+removeVersion(): void\n" +
				"+getEntireHistory(): List[Document]\n" +
				"+putVersion(Document document): void\n" +
				"+setEntireHistory(List[Document] documents): void\n" +
				"+getVersion(): Document\n" +
				"}\n",
				"interface VersionsStrategy {\n" +
				"+removeVersion(): void\n" +
				"+getEntireHistory(): List[Document]\n" +
				"+putVersion(Document document): void\n" +
				"+setEntireHistory(List[Document] documents): void\n" +
				"+getVersion(): Document\n" +
				"}\n",
				"class DocumentManager {\n" +
				"-templates: HashMap[String,Document]\n" +
				"+createDocument(String type): Document\n" +
				"+getContents(String type): String\n" +
				"+DocumentManager(): Constructor\n" +
				"}\n",
				"class VersionsStrategyFactory {\n" +
				"-strategies: HashMap[String,VersionsStrategy]\n" +
				"+VersionsStrategyFactory(): Constructor\n" +
				"+createStrategy(String type): VersionsStrategy\n" +
				"}\n",
				"class VolatileVersionsStrategy {\n" +
				"-history: ArrayList[Document]\n" +
				"+removeVersion(): void\n" +
				"+VolatileVersionsStrategy(): Constructor\n" +
				"+getEntireHistory(): List[Document]\n" +
				"+setEntireHistory(List[Document] documents): void\n" +
				"+putVersion(Document document): void\n" +
				"+getVersion(): Document\n" +
				"}\n",
				"class VersionsManager {\n" +
				"-enabled: boolean\n" +
				"-strategy: VersionsStrategy\n" +
				"-latexEditorView: LatexEditorView\n" +
				"+loadFromFile(): void\n" +
				"+enable(): void\n" +
				"+isEnabled(): boolean\n" +
				"+VersionsManager(LatexEditorView latexEditorView, VersionsStrategy versionsStrategy): Constructor\n" +
				"+setPreviousVersion(): Document\n" +
				"+saveContents(): void\n" +
				"+getType(): String\n" +
				"+changeStrategy(): void\n" +
				"+setStrategy(VersionsStrategy strategy): void\n" +
				"+setCurrentVersion(Document document): void\n" +
				"+getStrategy(): VersionsStrategy\n" +
				"+rollback(): void\n" +
				"+rollbackToPreviousVersion(): void\n" +
				"+enableStrategy(): void\n" +
				"+disable(): void\n" +
				"+saveToFile(): void\n" +
				"+putVersion(Document document): void\n" +
				"}\n"
		));

		ClassDiagramManager classDiagramManager = new ClassDiagramManager();
		SourceProject sourceProject = classDiagramManager.createSourceProject(Paths.get(currentDirectory.toRealPath() + "\\src\\test\\resources\\LatexEditor\\src"));
		classDiagramManager.createDiagram(List.of("StableVersionsStrategy", "VersionsStrategy", "VersionsStrategyFactory", "VolatileVersionsStrategy",
				"VersionsManager", "Document", "DocumentManager"));

		Map<SinkVertex, Integer> graphNodes = classDiagramManager.getDiagram().getGraphNodes();
		PlantUMLLeafNode plantUMLLeafNode = new PlantUMLLeafNode(graphNodes);
		plantUMLLeafNode.convertPlantLeafNode();
		List<String> actual = new ArrayList<>(plantUMLLeafNode.getPlantUMLTester().values());


		assertEquals(expected.size(), actual.size());
		Collections.sort(actual);
		Collections.sort(expected);
		Iterator<String> actualIterator = actual.iterator();
		Iterator<String> expectedIterator = expected.iterator();

		while (actualIterator.hasNext() || expectedIterator.hasNext()) {
			List<String> actualList = Arrays.asList(actualIterator.next().split("\n"));
			List<String> expectedList = Arrays.asList(expectedIterator.next().split("\n"));

			assertEquals(actualList.size(), expectedList.size());
			Collections.sort(actualList);
			Collections.sort(expectedList);
			assertTrue(actualList.containsAll(expectedList));
			assertTrue(expectedList.containsAll(actualList));
		}
	}
	
	@Test
	void checkPackageDiagramRelationshipsText() {
		try {
			List<String> actualRelationships;
			List<String> expectedRelationships = new ArrayList<>();
			expectedRelationships.add("src.model ..> src.view");
			expectedRelationships.add("src.model ..> src.model.strategies");
			expectedRelationships.add("src.model.strategies ..> src.model");
			expectedRelationships.add("src.controller ..> src.model");
			expectedRelationships.add("src.controller ..> src.controller.commands");
			expectedRelationships.add("src.controller.commands ..> src.model");
			expectedRelationships.add("src.view ..> src.model");
			expectedRelationships.add("src.view ..> src.controller");
			expectedRelationships.add("src.view ..> src.model.strategies");

			PackageDiagramManager packageDiagramManager = new PackageDiagramManager();
			SourceProject sourceProject = packageDiagramManager.createSourceProject(Paths.get(currentDirectory.toRealPath() + "\\src\\test\\resources\\LatexEditor\\src"));
			packageDiagramManager.createDiagram(List.of(
					currentDirectory.toRealPath() + "\\src\\test\\resources\\LatexEditor\\src",
					currentDirectory.toRealPath() + "\\src\\test\\resources\\LatexEditor\\src\\view",
					currentDirectory.toRealPath() + "\\src\\test\\resources\\LatexEditor\\src\\model",
					currentDirectory.toRealPath() + "\\src\\test\\resources\\LatexEditor\\src\\model\\strategies",
					currentDirectory.toRealPath() + "\\src\\test\\resources\\LatexEditor\\src\\controller\\commands",
					currentDirectory.toRealPath() + "\\src\\test\\resources\\LatexEditor\\src\\controller"));

			Map<Arc<Vertex>, Integer> graphEdges = packageDiagramManager.getDiagram().getGraphEdges();
			PlantUMLPackageEdge plantUMLEdge = new PlantUMLPackageEdge(graphEdges);
			plantUMLEdge.convertPlantEdge();
			actualRelationships = plantUMLEdge.getPlantUMLTester();

			Collections.sort(actualRelationships);
			Collections.sort(expectedRelationships);
			assertEquals(expectedRelationships, actualRelationships);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	void checkPackageDiagramDeclarationsText() {
		try {
			Map<String, String> actualDeclarations;
			Map<String, String> expectedDeclarations = new HashMap<>();
			expectedDeclarations.put("src", "package src {\n" + "}\n");
			expectedDeclarations.put("src.model", "package src.model {\n" + "}\n");
			expectedDeclarations.put("src.model.strategies", "package src.model.strategies {\n" + "}\n");
			expectedDeclarations.put("src.controller", "package src.controller {\n" + "}\n");
			expectedDeclarations.put("src.controller.commands", "package src.controller.commands {\n" + "}\n");
			expectedDeclarations.put("src.view", "package src.view {\n" + "}\n");

			PackageDiagramManager packageDiagramManager = new PackageDiagramManager();
			SourceProject sourceProject = packageDiagramManager.createSourceProject(Paths.get(currentDirectory.toRealPath() + "\\src\\test\\resources\\LatexEditor\\src"));
			packageDiagramManager.createDiagram(List.of(
					currentDirectory.toRealPath() + "\\src\\test\\resources\\LatexEditor\\src",
					currentDirectory.toRealPath() + "\\src\\test\\resources\\LatexEditor\\src\\view",
					currentDirectory.toRealPath() + "\\src\\test\\resources\\LatexEditor\\src\\model",
					currentDirectory.toRealPath() + "\\src\\test\\resources\\LatexEditor\\src\\model\\strategies",
					currentDirectory.toRealPath() + "\\src\\test\\resources\\LatexEditor\\src\\controller\\commands",
					currentDirectory.toRealPath() + "\\src\\test\\resources\\LatexEditor\\src\\controller"));

			Map<Vertex, Integer> graphNodes = packageDiagramManager.getDiagram().getGraphNodes();
			PlantUMLPackageNode plantUMLPackageNode = new PlantUMLPackageNode(graphNodes);
			plantUMLPackageNode.convertPlantPackageNode();
			actualDeclarations = plantUMLPackageNode.getPlantUMLTester();

			assertEquals(expectedDeclarations.size(), actualDeclarations.size());
			for (Map.Entry<String, String> expected: expectedDeclarations.entrySet()) {
				assertTrue(actualDeclarations.containsKey(expected.getKey()));
				assertEquals(expected.getValue(), actualDeclarations.get(expected.getKey()));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
