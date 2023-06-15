package model.plantuml;

import manager.PackageDiagramManager;
import manager.SourceProject;
import model.diagram.plantuml.PlantUMLVertexArc;
import model.graph.Arc;
import model.graph.Vertex;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PlantUMLVertexArcTest {

	Path currentDirectory = Path.of(".");
	@Test
	void convertVertexArcTest() {
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
			packageDiagramManager.convertTreeToDiagram(List.of(
				"src.view",
				"src.model",
				"src.model.strategies",
				"src.controller.commands",
				"src.controller"
			));

			Map<Vertex, Set<Arc<Vertex>>> diagram = packageDiagramManager.getDiagram();
			PlantUMLVertexArc plantUMLEdge = new PlantUMLVertexArc(diagram);
			String actualBuffer = plantUMLEdge.convertVertexArc().toString();

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
