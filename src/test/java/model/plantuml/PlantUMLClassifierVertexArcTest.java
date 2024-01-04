package model.plantuml;

import manager.ClassDiagramManager;
import model.diagram.plantuml.PlantUMLClassifierVertexArc;
import org.junit.jupiter.api.Test;
import utils.PathConstructor;

import java.io.File;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlantUMLClassifierVertexArcTest {

	@Test
	void convertSinkVertexArcTest() {
		String expectedBuffer = "VersionsStrategy ..> Document\n" +
				"DocumentManager o-- Document\n" +
				"VersionsStrategyFactory ..> VolatileVersionsStrategy\n" +
				"StableVersionsStrategy ..|> VersionsStrategy\n" +
				"VersionsStrategyFactory o-- VersionsStrategy\n" +
				"StableVersionsStrategy ..> Document\n" +
				"VersionsManager --> VersionsStrategy\n" +
				"VolatileVersionsStrategy o-- Document\n" +
				"VersionsManager ..> Document\n" +
				"VersionsStrategyFactory ..> StableVersionsStrategy\n" +
				"VolatileVersionsStrategy ..|> VersionsStrategy\n";

		ClassDiagramManager classDiagramManager = new ClassDiagramManager();
		classDiagramManager.createSourceProject(Paths.get(PathConstructor.getCurrentPath() + File.separator + PathConstructor.constructPath("src", "test", "resources", "LatexEditor", "src")));
		classDiagramManager.convertTreeToDiagram(List.of("StableVersionsStrategy", "VersionsStrategy", "VersionsStrategyFactory", "VolatileVersionsStrategy",
				"VersionsManager", "Document", "DocumentManager"));

		PlantUMLClassifierVertexArc plantUMLEdge = new PlantUMLClassifierVertexArc(classDiagramManager.getClassDiagram());
		String 						actualBuffer = plantUMLEdge.convertSinkVertexArc().toString();

		List<String> expectedRelationships = Arrays.asList(expectedBuffer.split("\n"));
		List<String> actualRelationships   = Arrays.asList(actualBuffer.split("\n"));
		Collections.sort(expectedRelationships);
		Collections.sort(actualRelationships);
		assertEquals(expectedRelationships, actualRelationships);
	}
}
