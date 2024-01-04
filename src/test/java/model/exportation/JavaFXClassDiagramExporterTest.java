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
			List<String> 		chosenFiles 		= Arrays.asList("MainWindow",
																   "LatexEditorView",
																   "OpeningWindow");
			classDiagramManager.createSourceProject(Paths.get(String.format("%s%s%s",
																			PathConstructor.getCurrentPath(),
																			File.separator,
																			PathConstructor.constructPath("src",
																										  "test",
																										  "resources",
																										  "LatexEditor",
																										  "src"))));
			classDiagramManager.convertTreeToDiagram(chosenFiles);
			DiagramExporter javaFXExporter = new JavaFXClassDiagramExporter(classDiagramManager.getClassDiagram());
			File 			actualFile 	   = javaFXExporter.exportDiagram(Path.of(String.format("%s%s%s",
																								  PathConstructor.getCurrentPath(),
																								  File.separator,
																								  PathConstructor.constructPath("src",
																																"test",
																																"resources",
																																"testingExportedFile.txt"))));

			String    expectedJsonString = getExpectedJsonString();
			JsonArray expectedJsonArray  = JsonParser.parseString(expectedJsonString).getAsJsonArray();
			JsonArray actualJsonArray    = JsonParser.parseString(Files.readAllLines(actualFile.toPath()).get(0)).getAsJsonArray();
			assertEquals(expectedJsonArray.size(), actualJsonArray.size());
			for (JsonElement element: expectedJsonArray) {
				for (JsonElement actualElement: actualJsonArray) {
					if (!actualElement.getAsJsonObject().get("name").equals(element.getAsJsonObject().get("name"))) {
						continue;
					}
					assertEquals(element.getAsJsonObject().size(), actualElement.getAsJsonObject().size());

					JsonArray 		 expectedMethods = JsonParser.parseString(element.getAsJsonObject().get("methods").toString()).getAsJsonArray();
					JsonArray 		 actualMethods = JsonParser.parseString(actualElement.getAsJsonObject().get("methods").toString()).getAsJsonArray();
					Set<JsonElement> expectedJsonElementSet = CompareArray.setOfElements(expectedMethods);
					Set<JsonElement> actualJsonElementSet = CompareArray.setOfElements(actualMethods);
					assertEquals(expectedJsonElementSet, actualJsonElementSet);

					JsonArray expectedFields = JsonParser.parseString(element.getAsJsonObject().get("fields").toString()).getAsJsonArray();
					JsonArray actualFields   = JsonParser.parseString(actualElement.getAsJsonObject().get("fields").toString()).getAsJsonArray();
					expectedJsonElementSet   = CompareArray.setOfElements(expectedFields);
					actualJsonElementSet     = CompareArray.setOfElements(actualFields);
					assertEquals(expectedJsonElementSet, actualJsonElementSet);

					JsonArray expectedArcs = JsonParser.parseString(element.getAsJsonObject().get("arcs").toString()).getAsJsonArray();
					JsonArray actualArcs   = JsonParser.parseString(actualElement.getAsJsonObject().get("arcs").toString()).getAsJsonArray();
					expectedJsonElementSet = CompareArray.setOfElements(expectedArcs);
					actualJsonElementSet   = CompareArray.setOfElements(actualArcs);
					assertEquals(expectedJsonElementSet, actualJsonElementSet);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	private static String getExpectedJsonString()
	{
		return "[{" +
			   "\"name\":\"MainWindow\",\"path\":\"C:\\\\Users\\\\user\\\\IntelliJProjects\\\\ObjectOrientedArchitectureDiagrammerDaintiness\\\\src\\\\test\\\\resources\\\\LatexEditor\\\\src\\\\view\\\\MainWindow.java\"," +
			   "\"vertexType\":\"class\",\"coordinate_x\":0.0,\"coordinate_y\":0.0,\"methods\":[{\"name\":\"actionPerformed\",\"returnType\":\"void\",\"modifier\":\"public\",\"parameters\":\"{\\\"e\\\":\\\"ActionEvent\\\"}\"}," +
			   "{\"name\":\"actionPerformed\",\"returnType\":\"void\",\"modifier\":\"public\",\"parameters\":\"{\\\"e\\\":\\\"ActionEvent\\\"}\"}," +
			   "{\"name\":\"actionPerformed\",\"returnType\":\"void\",\"modifier\":\"public\",\"parameters\":\"{\\\"arg0\\\":\\\"ActionEvent\\\"}\"}," +
			   "{\"name\":\"actionPerformed\",\"returnType\":\"void\",\"modifier\":\"public\",\"parameters\":\"{\\\"e\\\":\\\"ActionEvent\\\"}\"}," +
			   "{\"name\":\"actionPerformed\",\"returnType\":\"void\",\"modifier\":\"public\",\"parameters\":\"{\\\"arg0\\\":\\\"ActionEvent\\\"}\"}," +
			   "{\"name\":\"actionPerformed\",\"returnType\":\"void\",\"modifier\":\"public\",\"parameters\":\"{\\\"e\\\":\\\"ActionEvent\\\"}\"}," +
			   "{\"name\":\"actionPerformed\",\"returnType\":\"void\",\"modifier\":\"public\",\"parameters\":\"{\\\"e\\\":\\\"ActionEvent\\\"}\"}," +
			   "{\"name\":\"actionPerformed\",\"returnType\":\"void\",\"modifier\":\"public\",\"parameters\":\"{\\\"e\\\":\\\"ActionEvent\\\"}\"}," +
			   "{\"name\":\"actionPerformed\",\"returnType\":\"void\",\"modifier\":\"public\",\"parameters\":\"{\\\"e\\\":\\\"ActionEvent\\\"}\"}," +
			   "{\"name\":\"actionPerformed\",\"returnType\":\"void\",\"modifier\":\"public\",\"parameters\":\"{\\\"arg0\\\":\\\"ActionEvent\\\"}\"}," +
			   "{\"name\":\"actionPerformed\",\"returnType\":\"void\",\"modifier\":\"public\",\"parameters\":\"{\\\"e\\\":\\\"ActionEvent\\\"}\"}," +
			   "{\"name\":\"actionPerformed\",\"returnType\":\"void\",\"modifier\":\"public\",\"parameters\":\"{\\\"arg0\\\":\\\"ActionEvent\\\"}\"}," +
			   "{\"name\":\"editContents\",\"returnType\":\"void\",\"modifier\":\"public\",\"parameters\":\"{\\\"type\\\":\\\"String\\\"}\"}," +
			   "{\"name\":\"actionPerformed\",\"returnType\":\"void\",\"modifier\":\"public\",\"parameters\":\"{\\\"e\\\":\\\"ActionEvent\\\"}\"}," +
			   "{\"name\":\"actionPerformed\",\"returnType\":\"void\",\"modifier\":\"public\",\"parameters\":\"{\\\"e\\\":\\\"ActionEvent\\\"}\"}," +
			   "{\"name\":\"actionPerformed\",\"returnType\":\"void\",\"modifier\":\"public\",\"parameters\":\"{\\\"e\\\":\\\"ActionEvent\\\"}\"}," +
			   "{\"name\":\"actionPerformed\",\"returnType\":\"void\",\"modifier\":\"public\",\"parameters\":\"{\\\"e\\\":\\\"ActionEvent\\\"}\"}," +
			   "{\"name\":\"initialize\",\"returnType\":\"void\",\"modifier\":\"private\",\"parameters\":\"{}\"}," +
			   "{\"name\":\"MainWindow\",\"returnType\":\"Constructor\",\"modifier\":\"public\",\"parameters\":\"{\\\"latexEditorView\\\":\\\"LatexEditorView\\\"}\"}]," +
			   "\"fields\":[{\"name\":\"frame\",\"returnType\":\"JFrame\",\"modifier\":\"private\"}," +
			   "{\"name\":\"editorPane\",\"returnType\":\"JEditorPane\",\"modifier\":\"private\"}," +
			   "{\"name\":\"latexEditorView\",\"returnType\":\"LatexEditorView\",\"modifier\":\"private\"}],\"arcs\":[{\"source\":\"MainWindow\",\"target\":\"LatexEditorView\",\"arcType\":\"dependency\"}," +
			   "{\"source\":\"MainWindow\",\"target\":\"LatexEditorView\",\"arcType\":\"association\"},{\"source\":\"MainWindow\",\"target\":\"ChooseTemplate\",\"arcType\":\"dependency\"}]}," +
			   "{\"name\":\"OpeningWindow\",\"path\":\"C:\\\\Users\\\\user\\\\IntelliJProjects\\\\ObjectOrientedArchitectureDiagrammerDaintiness\\\\src\\\\test\\\\resources\\\\LatexEditor\\\\src\\\\view\\\\OpeningWindow.java\"," +
			   "\"vertexType\":\"class\",\"coordinate_x\":0.0,\"coordinate_y\":0.0,\"methods\":[{\"name\":\"OpeningWindow\",\"returnType\":\"Constructor\",\"modifier\":\"public\",\"parameters\":\"{}\"}," +
			   "{\"name\":\"actionPerformed\",\"returnType\":\"void\",\"modifier\":\"public\",\"parameters\":\"{\\\"e\\\":\\\"ActionEvent\\\"}\"}," +
			   "{\"name\":\"main\",\"returnType\":\"void\",\"modifier\":\"public\",\"parameters\":\"{\\\"args\\\":\\\"String[]\\\"}\"}," +
			   "{\"name\":\"actionPerformed\",\"returnType\":\"void\",\"modifier\":\"public\",\"parameters\":\"{\\\"e\\\":\\\"ActionEvent\\\"}\"},{\"name\":\"run\",\"returnType\":\"void\",\"modifier\":\"public\",\"parameters\":\"{}\"}," +
			   "{\"name\":\"actionPerformed\",\"returnType\":\"void\",\"modifier\":\"public\",\"parameters\":\"{\\\"e\\\":\\\"ActionEvent\\\"}\"}," +
			   "{\"name\":\"initialize\",\"returnType\":\"void\",\"modifier\":\"private\",\"parameters\":\"{}\"}],\"fields\":[{\"name\":\"frame\",\"returnType\":\"JFrame\",\"modifier\":\"private\"}," +
			   "{\"name\":\"latexEditorView\",\"returnType\":\"LatexEditorView\",\"modifier\":\"private\"}],\"arcs\":[{\"source\":\"OpeningWindow\",\"target\":\"LatexEditorController\",\"arcType\":\"dependency\"}," +
			   "{\"source\":\"OpeningWindow\",\"target\":\"VersionsManager\",\"arcType\":\"dependency\"},{\"source\":\"OpeningWindow\",\"target\":\"LatexEditorView\",\"arcType\":\"association\"}," +
			   "{\"source\":\"OpeningWindow\",\"target\":\"ChooseTemplate\",\"arcType\":\"dependency\"},{\"source\":\"OpeningWindow\",\"target\":\"VersionsStrategy\",\"arcType\":\"dependency\"}," +
			   "{\"source\":\"OpeningWindow\",\"target\":\"VolatileVersionsStrategy\",\"arcType\":\"dependency\"}]}," +
			   "{\"name\":\"LatexEditorView\",\"path\":\"C:\\\\Users\\\\user\\\\IntelliJProjects\\\\ObjectOrientedArchitectureDiagrammerDaintiness\\\\src\\\\test\\\\resources\\\\LatexEditor\\\\src\\\\view\\\\LatexEditorView.java\"," +
			   "\"vertexType\":\"class\",\"coordinate_x\":0.0,\"coordinate_y\":0.0,\"methods\":[{\"name\":\"getStrategy\",\"returnType\":\"String\",\"modifier\":\"public\",\"parameters\":\"{}\"}," +
			   "{\"name\":\"getController\",\"returnType\":\"LatexEditorController\",\"modifier\":\"public\",\"parameters\":\"{}\"}," +
			   "{\"name\":\"setVersionsManager\",\"returnType\":\"void\",\"modifier\":\"public\",\"parameters\":\"{\\\"versionsManager\\\":\\\"VersionsManager\\\"}\"}," +
			   "{\"name\":\"getType\",\"returnType\":\"String\",\"modifier\":\"public\",\"parameters\":\"{}\"},{\"name\":\"setController\"," +
			   "\"returnType\":\"void\",\"modifier\":\"public\",\"parameters\":\"{\\\"controller\\\":\\\"LatexEditorController\\\"}\"},{\"name\":\"getFilename\",\"returnType\":\"String\"," +
			   "\"modifier\":\"public\",\"parameters\":\"{}\"},{\"name\":\"setType\",\"returnType\":\"void\",\"modifier\":\"public\",\"parameters\":\"{\\\"type\\\":\\\"String\\\"}\"}," +
			   "{\"name\":\"loadFromFile\",\"returnType\":\"void\",\"modifier\":\"public\",\"parameters\":\"{}\"}," +
			   "{\"name\":\"getCurrentDocument\",\"returnType\":\"Document\",\"modifier\":\"public\",\"parameters\":\"{}\"}," +
			   "{\"name\":\"getText\",\"returnType\":\"String\",\"modifier\":\"public\",\"parameters\":\"{}\"}," +
			   "{\"name\":\"setCurrentDocument\",\"returnType\":\"void\",\"modifier\":\"public\",\"parameters\":\"{\\\"currentDocument\\\":\\\"Document\\\"}\"}," +
			   "{\"name\":\"getVersionsManager\",\"returnType\":\"VersionsManager\",\"modifier\":\"public\",\"parameters\":\"{}\"}," +
			   "{\"name\":\"setText\",\"returnType\":\"void\",\"modifier\":\"public\",\"parameters\":\"{\\\"text\\\":\\\"String\\\"}\"}," +
			   "{\"name\":\"saveContents\",\"returnType\":\"void\",\"modifier\":\"public\",\"parameters\":\"{}\"}," +
			   "{\"name\":\"setFilename\",\"returnType\":\"void\",\"modifier\":\"public\",\"parameters\":\"{\\\"filename\\\":\\\"String\\\"}\"}," +
			   "{\"name\":\"saveToFile\",\"returnType\":\"void\",\"modifier\":\"public\",\"parameters\":\"{}\"}," +
			   "{\"name\":\"setStrategy\",\"returnType\":\"void\",\"modifier\":\"public\",\"parameters\":\"{\\\"strategy\\\":\\\"String\\\"}\"}]," +
			   "\"fields\":[{\"name\":\"controller\",\"returnType\":\"LatexEditorController\",\"modifier\":\"private\"}," +
			   "{\"name\":\"currentDocument\",\"returnType\":\"Document\",\"modifier\":\"private\"}," +
			   "{\"name\":\"type\",\"returnType\":\"String\",\"modifier\":\"private\"},{\"name\":\"text\",\"returnType\":\"String\",\"modifier\":\"private\"}," +
			   "{\"name\":\"filename\",\"returnType\":\"String\",\"modifier\":\"private\"},{\"name\":\"strategy\",\"returnType\":\"String\",\"modifier\":\"private\"},{\"name\":\"versionsManager\"," +
			   "\"returnType\":\"VersionsManager\",\"modifier\":\"private\"}],\"arcs\":[{\"source\":\"LatexEditorView\",\"target\":\"LatexEditorController\",\"arcType\":\"dependency\"}," +
			   "{\"source\":\"LatexEditorView\",\"target\":\"LatexEditorController\",\"arcType\":\"association\"},{\"source\":\"LatexEditorView\",\"target\":\"VersionsManager\",\"" +
			   "arcType\":\"dependency\"},{\"source\":\"LatexEditorView\",\"target\":\"VersionsManager\",\"arcType\":\"association\"},{\"source\":\"LatexEditorView\",\"target\":\"Document\"," +
			   "\"arcType\":\"dependency\"},{\"source\":\"LatexEditorView\",\"target\":\"Document\",\"arcType\":\"association\"}]}]";
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
