package model;

import manager.ClassDiagramManager;
import manager.PackageDiagramManager;
import model.diagram.plantuml.PlantUMLLeafEdge;
import model.diagram.plantuml.PlantUMLPackageEdge;
import model.graph.Arc;
import model.graph.SinkVertex;
import model.graph.Vertex;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PlantUMLEdgeTest {

	Path currentDirectory = Path.of(".");

	@Test
	void checkClassDiagramRelationshipsText() {
		try {
			String expectedBuffer = "VersionsStrategy ..> Document\n" +
				"DocumentManager --o Document\n" +
				"VolatileVersionsStrategy ..> Document\n" +
				"VersionsStrategyFactory ..> VolatileVersionsStrategy\n" +
				"StableVersionsStrategy ..|> VersionsStrategy\n" +
				"DocumentManager ..> Document\n" +
				"VersionsStrategyFactory --o VersionsStrategy\n" +
				"VersionsStrategyFactory ..> VersionsStrategy\n" +
				"StableVersionsStrategy ..> Document\n" +
				"VersionsManager --> VersionsStrategy\n" +
				"VolatileVersionsStrategy --o Document\n" +
				"VersionsManager ..> Document\n" +
				"VersionsManager ..> VersionsStrategy\n" +
				"VersionsStrategyFactory ..> StableVersionsStrategy\n" +
				"VolatileVersionsStrategy ..|> VersionsStrategy\n";

			ClassDiagramManager classDiagramManager = new ClassDiagramManager();
			SourceProject sourceProject = classDiagramManager.createSourceProject(Paths.get(currentDirectory.toRealPath() + "\\src\\test\\resources\\LatexEditor\\src"));
			classDiagramManager.createDiagram(List.of("StableVersionsStrategy", "VersionsStrategy", "VersionsStrategyFactory", "VolatileVersionsStrategy",
					"VersionsManager", "Document", "DocumentManager"));

			Map<Arc<SinkVertex>, Integer> graphEdges = classDiagramManager.getDiagram().getGraphEdges();

			PlantUMLLeafEdge plantUMLEdge = new PlantUMLLeafEdge(graphEdges);
			String actualBuffer = plantUMLEdge.convertPlantEdge().toString();

			List<String> actualRelationships = Arrays.asList(expectedBuffer.split("\n"));
			List<String> expectedRelationships = Arrays.asList(actualBuffer.split("\n"));
			Collections.sort(actualRelationships);
			Collections.sort(expectedRelationships);
			assertEquals(expectedRelationships, actualRelationships);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	void checkPackageDiagramRelationshipsText() {
		try {
			String expectedBuffer = "src.model ..> src.view\n" +
					"src.controller.commands ..> src.model\n" +
					"src.model ..> src.model.strategies\n" +
					"src.view ..> src.model\n" +
					"src.model.strategies ..> src.model\n" +
					"src.controller ..> src.model\n" +
					"src.view ..> src.controller\n" +
					"src.controller ..> src.controller.commands\n" +
					"src.view ..> src.model.strategies\n";

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
			String actualBuffer = plantUMLEdge.convertPlantEdge().toString();

			List<String> expectedRelationship = Arrays.asList(expectedBuffer.split("\n"));
			List<String> actualRelationship = Arrays.asList(actualBuffer.split("\n"));
			Collections.sort(expectedRelationship);
			Collections.sort(actualRelationship);

			assertEquals(expectedRelationship, actualRelationship);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
