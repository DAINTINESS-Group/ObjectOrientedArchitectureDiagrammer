package model.exportation;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import manager.ClassDiagramManager;
import model.diagram.exportation.DiagramExporter;
import model.diagram.exportation.JavaFXClassDiagramExporter;
import org.junit.jupiter.api.Test;
import utils.PathConstructor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JavaFXClassDiagramExporterTest {

	@Test
	void exportDiagramTest() {
		try {
			ClassDiagramManager classDiagramManager = new ClassDiagramManager();
			List<String> chosenFiles = Arrays.asList("MainWindow", "LatexEditorView", "OpeningWindow");
			classDiagramManager.createSourceProject(Paths.get(PathConstructor.getCurrentPath() + File.separator + PathConstructor.constructPath("src", "test", "resources", "LatexEditor", "src")));
			classDiagramManager.convertTreeToDiagram(chosenFiles);
			DiagramExporter javaFXExporter = new JavaFXClassDiagramExporter(classDiagramManager.getClassDiagram());
			File actualFile = javaFXExporter.exportDiagram(Path.of(PathConstructor.getCurrentPath() + File.separator + PathConstructor.constructPath("src", "test", "resources", "testingExportedFile.txt")));

			String expectedJsonString =
				"[{" +
				"\"name\":\"MainWindow\",\"path\":\"C:\\\\Users\\\\user\\\\IntelliJProjects\\\\ObjectOrientedArchitectureDiagrammerDaintiness\\\\src\\\\test\\\\resources\\\\LatexEditor\\\\src\\\\view\\\\MainWindow.java\"," +
				"\"vertexType\":\"CLASS\",\"coordinate_x\":0.0,\"coordinate_y\":0.0,\"methods\":[{\"name\":\"actionPerformed\",\"returnType\":\"void\",\"modifier\":\"PUBLIC\",\"parameters\":\"{\\\"e\\\":\\\"ActionEvent\\\"}\"}," +
				"{\"name\":\"actionPerformed\",\"returnType\":\"void\",\"modifier\":\"PUBLIC\",\"parameters\":\"{\\\"e\\\":\\\"ActionEvent\\\"}\"}," +
				"{\"name\":\"actionPerformed\",\"returnType\":\"void\",\"modifier\":\"PUBLIC\",\"parameters\":\"{\\\"arg0\\\":\\\"ActionEvent\\\"}\"}," +
				"{\"name\":\"actionPerformed\",\"returnType\":\"void\",\"modifier\":\"PUBLIC\",\"parameters\":\"{\\\"e\\\":\\\"ActionEvent\\\"}\"}," +
				"{\"name\":\"actionPerformed\",\"returnType\":\"void\",\"modifier\":\"PUBLIC\",\"parameters\":\"{\\\"arg0\\\":\\\"ActionEvent\\\"}\"}," +
				"{\"name\":\"actionPerformed\",\"returnType\":\"void\",\"modifier\":\"PUBLIC\",\"parameters\":\"{\\\"e\\\":\\\"ActionEvent\\\"}\"}," +
				"{\"name\":\"actionPerformed\",\"returnType\":\"void\",\"modifier\":\"PUBLIC\",\"parameters\":\"{\\\"e\\\":\\\"ActionEvent\\\"}\"}," +
				"{\"name\":\"actionPerformed\",\"returnType\":\"void\",\"modifier\":\"PUBLIC\",\"parameters\":\"{\\\"e\\\":\\\"ActionEvent\\\"}\"}," +
				"{\"name\":\"actionPerformed\",\"returnType\":\"void\",\"modifier\":\"PUBLIC\",\"parameters\":\"{\\\"e\\\":\\\"ActionEvent\\\"}\"}," +
				"{\"name\":\"actionPerformed\",\"returnType\":\"void\",\"modifier\":\"PUBLIC\",\"parameters\":\"{\\\"arg0\\\":\\\"ActionEvent\\\"}\"}," +
				"{\"name\":\"actionPerformed\",\"returnType\":\"void\",\"modifier\":\"PUBLIC\",\"parameters\":\"{\\\"e\\\":\\\"ActionEvent\\\"}\"}," +
				"{\"name\":\"actionPerformed\",\"returnType\":\"void\",\"modifier\":\"PUBLIC\",\"parameters\":\"{\\\"arg0\\\":\\\"ActionEvent\\\"}\"}," +
				"{\"name\":\"editContents\",\"returnType\":\"void\",\"modifier\":\"PUBLIC\",\"parameters\":\"{\\\"type\\\":\\\"String\\\"}\"}," +
				"{\"name\":\"actionPerformed\",\"returnType\":\"void\",\"modifier\":\"PUBLIC\",\"parameters\":\"{\\\"e\\\":\\\"ActionEvent\\\"}\"}," +
				"{\"name\":\"actionPerformed\",\"returnType\":\"void\",\"modifier\":\"PUBLIC\",\"parameters\":\"{\\\"e\\\":\\\"ActionEvent\\\"}\"}," +
				"{\"name\":\"actionPerformed\",\"returnType\":\"void\",\"modifier\":\"PUBLIC\",\"parameters\":\"{\\\"e\\\":\\\"ActionEvent\\\"}\"}," +
				"{\"name\":\"actionPerformed\",\"returnType\":\"void\",\"modifier\":\"PUBLIC\",\"parameters\":\"{\\\"e\\\":\\\"ActionEvent\\\"}\"}," +
				"{\"name\":\"initialize\",\"returnType\":\"void\",\"modifier\":\"PRIVATE\",\"parameters\":\"{}\"}," +
				"{\"name\":\"MainWindow\",\"returnType\":\"Constructor\",\"modifier\":\"PUBLIC\",\"parameters\":\"{\\\"latexEditorView\\\":\\\"LatexEditorView\\\"}\"}]," +
				"\"fields\":[{\"name\":\"frame\",\"returnType\":\"JFrame\",\"modifier\":\"PRIVATE\"}," +
				"{\"name\":\"editorPane\",\"returnType\":\"JEditorPane\",\"modifier\":\"PRIVATE\"}," +
				"{\"name\":\"latexEditorView\",\"returnType\":\"LatexEditorView\",\"modifier\":\"PRIVATE\"}],\"arcs\":[{\"source\":\"MainWindow\",\"target\":\"LatexEditorView\",\"arcType\":\"DEPENDENCY\"}," +
				"{\"source\":\"MainWindow\",\"target\":\"LatexEditorView\",\"arcType\":\"ASSOCIATION\"},{\"source\":\"MainWindow\",\"target\":\"ChooseTemplate\",\"arcType\":\"DEPENDENCY\"}]}," +
				"{\"name\":\"OpeningWindow\",\"path\":\"C:\\\\Users\\\\user\\\\IntelliJProjects\\\\ObjectOrientedArchitectureDiagrammerDaintiness\\\\src\\\\test\\\\resources\\\\LatexEditor\\\\src\\\\view\\\\OpeningWindow.java\"," +
				"\"vertexType\":\"CLASS\",\"coordinate_x\":0.0,\"coordinate_y\":0.0,\"methods\":[{\"name\":\"OpeningWindow\",\"returnType\":\"Constructor\",\"modifier\":\"PUBLIC\",\"parameters\":\"{}\"}," +
				"{\"name\":\"actionPerformed\",\"returnType\":\"void\",\"modifier\":\"PUBLIC\",\"parameters\":\"{\\\"e\\\":\\\"ActionEvent\\\"}\"}," +
				"{\"name\":\"main\",\"returnType\":\"void\",\"modifier\":\"PUBLIC\",\"parameters\":\"{\\\"args\\\":\\\"String[]\\\"}\"}," +
				"{\"name\":\"actionPerformed\",\"returnType\":\"void\",\"modifier\":\"PUBLIC\",\"parameters\":\"{\\\"e\\\":\\\"ActionEvent\\\"}\"},{\"name\":\"run\",\"returnType\":\"void\",\"modifier\":\"PUBLIC\",\"parameters\":\"{}\"}," +
				"{\"name\":\"actionPerformed\",\"returnType\":\"void\",\"modifier\":\"PUBLIC\",\"parameters\":\"{\\\"e\\\":\\\"ActionEvent\\\"}\"}," +
				"{\"name\":\"initialize\",\"returnType\":\"void\",\"modifier\":\"PRIVATE\",\"parameters\":\"{}\"}],\"fields\":[{\"name\":\"frame\",\"returnType\":\"JFrame\",\"modifier\":\"PRIVATE\"}," +
				"{\"name\":\"latexEditorView\",\"returnType\":\"LatexEditorView\",\"modifier\":\"PRIVATE\"}],\"arcs\":[{\"source\":\"OpeningWindow\",\"target\":\"LatexEditorController\",\"arcType\":\"DEPENDENCY\"}," +
				"{\"source\":\"OpeningWindow\",\"target\":\"VersionsManager\",\"arcType\":\"DEPENDENCY\"},{\"source\":\"OpeningWindow\",\"target\":\"LatexEditorView\",\"arcType\":\"ASSOCIATION\"}," +
				"{\"source\":\"OpeningWindow\",\"target\":\"ChooseTemplate\",\"arcType\":\"DEPENDENCY\"},{\"source\":\"OpeningWindow\",\"target\":\"VersionsStrategy\",\"arcType\":\"DEPENDENCY\"}," +
				"{\"source\":\"OpeningWindow\",\"target\":\"VolatileVersionsStrategy\",\"arcType\":\"DEPENDENCY\"}]}," +
				"{\"name\":\"LatexEditorView\",\"path\":\"C:\\\\Users\\\\user\\\\IntelliJProjects\\\\ObjectOrientedArchitectureDiagrammerDaintiness\\\\src\\\\test\\\\resources\\\\LatexEditor\\\\src\\\\view\\\\LatexEditorView.java\"," +
				"\"vertexType\":\"CLASS\",\"coordinate_x\":0.0,\"coordinate_y\":0.0,\"methods\":[{\"name\":\"getStrategy\",\"returnType\":\"String\",\"modifier\":\"PUBLIC\",\"parameters\":\"{}\"}," +
				"{\"name\":\"getController\",\"returnType\":\"LatexEditorController\",\"modifier\":\"PUBLIC\",\"parameters\":\"{}\"}," +
				"{\"name\":\"setVersionsManager\",\"returnType\":\"void\",\"modifier\":\"PUBLIC\",\"parameters\":\"{\\\"versionsManager\\\":\\\"VersionsManager\\\"}\"}," +
				"{\"name\":\"getType\",\"returnType\":\"String\",\"modifier\":\"PUBLIC\",\"parameters\":\"{}\"},{\"name\":\"setController\"," +
				"\"returnType\":\"void\",\"modifier\":\"PUBLIC\",\"parameters\":\"{\\\"controller\\\":\\\"LatexEditorController\\\"}\"},{\"name\":\"getFilename\",\"returnType\":\"String\"," +
				"\"modifier\":\"PUBLIC\",\"parameters\":\"{}\"},{\"name\":\"setType\",\"returnType\":\"void\",\"modifier\":\"PUBLIC\",\"parameters\":\"{\\\"type\\\":\\\"String\\\"}\"}," +
				"{\"name\":\"loadFromFile\",\"returnType\":\"void\",\"modifier\":\"PUBLIC\",\"parameters\":\"{}\"}," +
				"{\"name\":\"getCurrentDocument\",\"returnType\":\"Document\",\"modifier\":\"PUBLIC\",\"parameters\":\"{}\"}," +
				"{\"name\":\"getText\",\"returnType\":\"String\",\"modifier\":\"PUBLIC\",\"parameters\":\"{}\"}," +
				"{\"name\":\"setCurrentDocument\",\"returnType\":\"void\",\"modifier\":\"PUBLIC\",\"parameters\":\"{\\\"currentDocument\\\":\\\"Document\\\"}\"}," +
				"{\"name\":\"getVersionsManager\",\"returnType\":\"VersionsManager\",\"modifier\":\"PUBLIC\",\"parameters\":\"{}\"}," +
				"{\"name\":\"setText\",\"returnType\":\"void\",\"modifier\":\"PUBLIC\",\"parameters\":\"{\\\"text\\\":\\\"String\\\"}\"}," +
				"{\"name\":\"saveContents\",\"returnType\":\"void\",\"modifier\":\"PUBLIC\",\"parameters\":\"{}\"}," +
				"{\"name\":\"setFilename\",\"returnType\":\"void\",\"modifier\":\"PUBLIC\",\"parameters\":\"{\\\"filename\\\":\\\"String\\\"}\"}," +
				"{\"name\":\"saveToFile\",\"returnType\":\"void\",\"modifier\":\"PUBLIC\",\"parameters\":\"{}\"}," +
				"{\"name\":\"setStrategy\",\"returnType\":\"void\",\"modifier\":\"PUBLIC\",\"parameters\":\"{\\\"strategy\\\":\\\"String\\\"}\"}]," +
				"\"fields\":[{\"name\":\"controller\",\"returnType\":\"LatexEditorController\",\"modifier\":\"PRIVATE\"}," +
				"{\"name\":\"currentDocument\",\"returnType\":\"Document\",\"modifier\":\"PRIVATE\"}," +
				"{\"name\":\"type\",\"returnType\":\"String\",\"modifier\":\"PRIVATE\"},{\"name\":\"text\",\"returnType\":\"String\",\"modifier\":\"PRIVATE\"}," +
				"{\"name\":\"filename\",\"returnType\":\"String\",\"modifier\":\"PRIVATE\"},{\"name\":\"strategy\",\"returnType\":\"String\",\"modifier\":\"PRIVATE\"},{\"name\":\"versionsManager\"," +
				"\"returnType\":\"VersionsManager\",\"modifier\":\"PRIVATE\"}],\"arcs\":[{\"source\":\"LatexEditorView\",\"target\":\"LatexEditorController\",\"arcType\":\"DEPENDENCY\"}," +
				"{\"source\":\"LatexEditorView\",\"target\":\"LatexEditorController\",\"arcType\":\"ASSOCIATION\"},{\"source\":\"LatexEditorView\",\"target\":\"VersionsManager\",\"" +
				"arcType\":\"DEPENDENCY\"},{\"source\":\"LatexEditorView\",\"target\":\"VersionsManager\",\"arcType\":\"ASSOCIATION\"},{\"source\":\"LatexEditorView\",\"target\":\"Document\"," +
				"\"arcType\":\"DEPENDENCY\"},{\"source\":\"LatexEditorView\",\"target\":\"Document\",\"arcType\":\"ASSOCIATION\"}]}]";
			JsonArray expectedJsonArray = JsonParser.parseString(expectedJsonString).getAsJsonArray();
			JsonArray actualJsonArray = JsonParser.parseString(Files.readAllLines(actualFile.toPath()).get(0)).getAsJsonArray();
			assertEquals(expectedJsonArray.size(), actualJsonArray.size());
			for (JsonElement element: expectedJsonArray) {
				for (JsonElement actualElement: actualJsonArray) {
					if (!actualElement.getAsJsonObject().get("name").equals(element.getAsJsonObject().get("name"))) {
						continue;
					}
					assertEquals(element.getAsJsonObject().size(), actualElement.getAsJsonObject().size());

					JsonArray expectedMethods = JsonParser.parseString(element.getAsJsonObject().get("methods").toString()).getAsJsonArray();
					JsonArray actualMethods = JsonParser.parseString(actualElement.getAsJsonObject().get("methods").toString()).getAsJsonArray();
					Set<JsonElement> expectedJsonElementSet = CompareArray.setOfElements(expectedMethods);
					Set<JsonElement> actualJsonElementSet = CompareArray.setOfElements(actualMethods);
					assertEquals(expectedJsonElementSet, actualJsonElementSet);

					JsonArray expectedFields = JsonParser.parseString(element.getAsJsonObject().get("fields").toString()).getAsJsonArray();
					JsonArray actualFields = JsonParser.parseString(actualElement.getAsJsonObject().get("fields").toString()).getAsJsonArray();
					expectedJsonElementSet = CompareArray.setOfElements(expectedFields);
					actualJsonElementSet = CompareArray.setOfElements(actualFields);
					assertEquals(expectedJsonElementSet, actualJsonElementSet);

					JsonArray expectedArcs = JsonParser.parseString(element.getAsJsonObject().get("arcs").toString()).getAsJsonArray();
					JsonArray actualArcs = JsonParser.parseString(actualElement.getAsJsonObject().get("arcs").toString()).getAsJsonArray();
					expectedJsonElementSet = CompareArray.setOfElements(expectedArcs);
					actualJsonElementSet = CompareArray.setOfElements(actualArcs);
					assertEquals(expectedJsonElementSet, actualJsonElementSet);
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static class CompareArray {
		public static Set<JsonElement> setOfElements(JsonArray arr) {
			Set<JsonElement> set = new HashSet<>();
			for (JsonElement j : arr) {
				set.add(j);
			}
			return set;
		}
	}
}
