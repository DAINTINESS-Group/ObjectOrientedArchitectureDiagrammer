package model.plantuml;

import manager.PackageDiagramManager;
import model.diagram.plantuml.PlantUMLPackageVertex;
import org.junit.jupiter.api.Test;
import utils.PathConstructor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlantUMLPackageVertexTest {

	@Test
	void convertVertexTest() {
		String expectedBuffer = "package src.controller {\n" +
				"}\n" +
				"\n" +
				"package src.controller.commands {\n" +
				"}\n" +
				"\n" +
				"package src.model {\n" +
				"}\n" +
				"\n" +
				"package src {\n" +
				"}\n" +
				"\n" +
				"package src.view {\n" +
				"}\n" +
				"\n" +
				"package src.model.strategies {\n" +
				"}\n\n";

		PackageDiagramManager packageDiagramManager = new PackageDiagramManager();
		packageDiagramManager.createSourceProject(Paths.get(PathConstructor.getCurrentPath() + File.separator + PathConstructor.constructPath("src", "test", "resources", "LatexEditor", "src")));
		packageDiagramManager.convertTreeToDiagram(List.of(
				"src",
				"src.view",
				"src.model",
				"src.model.strategies",
				"src.controller.commands",
				"src.controller"
				));

		PlantUMLPackageVertex plantUMLPackageVertex = new PlantUMLPackageVertex(packageDiagramManager.getPackageDiagram());
		String actualBuffer = plantUMLPackageVertex.convertVertex().toString();

		List<String> expected = Arrays.asList(expectedBuffer.split("\n"));
		List<String> actual = Arrays.asList(actualBuffer.split("\n"));

		Collections.sort(expected);
		Collections.sort(actual);
		assertEquals(expected, actual);
	}
}
