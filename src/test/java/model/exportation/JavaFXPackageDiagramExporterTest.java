package model.exportation;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import manager.PackageDiagramManager;
import model.diagram.exportation.DiagramExporter;
import model.diagram.exportation.JavaFXPackageDiagramExporter;
import model.diagram.javafx.ClassifierVertexDeserializer;
import model.graph.Arc;
import model.graph.ClassifierVertex;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import utils.PathConstructor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JavaFXPackageDiagramExporterTest {

	private static String expectedJsonString;
	private static String expectedString2;
	private static String relativePath;
	private static String os;

	// TODO Decouple this
	@Test
	void exportDiagramTest() {
		try {
			PackageDiagramManager packageDiagramManager = new PackageDiagramManager();
			packageDiagramManager.createSourceProject(Paths.get(PathConstructor.getCurrentPath() + File.separator + PathConstructor.constructPath("src", "test", "resources", "LatexEditor", "src")));
			packageDiagramManager.convertTreeToDiagram(getPackages());

			DiagramExporter javaFXExporter = new JavaFXPackageDiagramExporter(packageDiagramManager.getPackageDiagram());
			File actualFile = javaFXExporter.exportDiagram(Path.of(PathConstructor.getCurrentPath() + File.separator + PathConstructor.constructPath("src", "test", "resources", "testingExportedFile.txt")));

			JsonArray expectedJsonArray = getJsonArray();
			JsonArray actualJsonArray = JsonParser.parseString(Files.readAllLines(actualFile.toPath()).get(0)).getAsJsonArray();
			assertEquals(expectedJsonArray.size(), actualJsonArray.size());
			for (JsonElement element: expectedJsonArray) {
				for (JsonElement actualElement: actualJsonArray) {
					if (!actualElement.getAsJsonObject().get("name").equals(element.getAsJsonObject().get("name"))) {
						continue;
					}
					assertEquals(element.getAsJsonObject().size(), actualElement.getAsJsonObject().size());

					JsonArray expectedArcs = JsonParser.parseString(element.getAsJsonObject().get("arcs").toString()).getAsJsonArray();
					JsonArray actualArcs = JsonParser.parseString(actualElement.getAsJsonObject().get("arcs").toString()).getAsJsonArray();
					Set<JsonElement> expectedJsonElementSet = CompareArray.setOfElements(expectedArcs);
					Set<JsonElement> actualJsonElementSet = CompareArray.setOfElements(actualArcs);
					assertEquals(expectedJsonElementSet, actualJsonElementSet);

					JsonArray expJson = JsonParser.parseString(element.getAsJsonObject().get("sinkVertices").toString()).getAsJsonArray();
					JsonArray actJson = JsonParser.parseString(actualElement.getAsJsonObject().get("sinkVertices").toString()).getAsJsonArray();
					List<ClassifierVertex> expSinkVertices = new ArrayList<>();
					List<ClassifierVertex> actSinkVertices = new ArrayList<>();
					for (JsonElement jsonElement: expJson) {
						Gson gson = new GsonBuilder().registerTypeAdapter(ClassifierVertex.class, new ClassifierVertexDeserializer()).create();
						expSinkVertices.add(gson.fromJson(jsonElement.getAsString(), ClassifierVertex.class));
					}
					for (JsonElement jsonElement: actJson) {
						Gson gson = new GsonBuilder().registerTypeAdapter(ClassifierVertex.class, new ClassifierVertexDeserializer()).create();
						actSinkVertices.add(gson.fromJson(jsonElement.getAsString(), ClassifierVertex.class));
					}
					for (ClassifierVertex classifierVertex : expSinkVertices) {
						Optional<ClassifierVertex> optionalSinkVertex = actSinkVertices.stream().filter(sinkVertex1 ->
							sinkVertex1.getName().equals(classifierVertex.getName()) &&
							sinkVertex1.getVertexType().equals(classifierVertex.getVertexType()) &&
							sinkVertex1.getArcs().size() == classifierVertex.getArcs().size() &&
							sinkVertex1.getMethods().size() == classifierVertex.getMethods().size() &&
							sinkVertex1.getFields().size() == classifierVertex.getFields().size())
						.findFirst();
						assertTrue(optionalSinkVertex.isPresent());

						List<ClassifierVertex.Field> fields = optionalSinkVertex.get().getFields();
						for (ClassifierVertex.Field field: classifierVertex.getFields()) {
							assertTrue(fields.stream()
								.anyMatch(field1 ->
								   field1.name().equals(field.name()) &&
								   field1.type().equals(field.type()) &&
								   field1.modifier().equals(field.modifier())
								)
							);
						}

						List<ClassifierVertex.Method> methods = optionalSinkVertex.get().getMethods();
						for (ClassifierVertex.Method method: classifierVertex.getMethods()) {
							assertTrue(methods.stream()
							    .anyMatch(method1 ->
								     method1.name().equals(method.name()) &&
								     method1.returnType().equals(method.returnType()) &&
								     method1.parameters().equals(method.parameters())
								)
							);
						}

						List<Arc<ClassifierVertex>> arcs = optionalSinkVertex.get().getArcs();
						for (Arc<ClassifierVertex> arc: classifierVertex.getArcs()) {
							assertTrue(arcs.stream()
						    	.anyMatch(sinkVertexArc ->
								 	sinkVertexArc.sourceVertex().getName().equals(arc.sourceVertex().getName()) &&
								 	sinkVertexArc.targetVertex().getName().equals(arc.targetVertex().getName()) &&
								 	sinkVertexArc.arcType().equals(arc.arcType())
								)
							);
						}
					}

					JsonArray expectedNeighbours = JsonParser.parseString(element.getAsJsonObject().get("neighbours").toString()).getAsJsonArray();
					JsonArray actualNeighbours = JsonParser.parseString(actualElement.getAsJsonObject().get("neighbours").toString()).getAsJsonArray();
					expectedJsonElementSet = CompareArray.setOfElements(expectedNeighbours);
					actualJsonElementSet = CompareArray.setOfElements2(actualNeighbours);
					assertEquals(expectedJsonElementSet, actualJsonElementSet);

					JsonObject expectedParent = JsonParser.parseString(element.getAsJsonObject().get("parent").toString()).getAsJsonObject();
					JsonObject actualParent = JsonParser.parseString(actualElement.getAsJsonObject().get("parent").toString()).getAsJsonObject();
					assertEquals(expectedParent.get("name"), actualParent.get("name"));
					assertEquals(expectedParent.get("vertexType"), actualParent.get("vertexType"));
					assertEquals(expectedParent.get("path").toString(), actualParent.get("path").toString().replaceAll("\\\\", "\\\\\\\\"));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@BeforeAll
	public static void setUp(){
		relativePath = Paths.get(PathConstructor.getCurrentPath() + File.separator + PathConstructor.constructPath("src", "test", "resources", "LatexEditor", "src")).toString();

		os = System.getProperty("os.name");

		expectedJsonString = "[{\"name\":\"src.view\",\"path\":\""
							 + relativePath + "\\\\view\",\"vertexType\":\"PACKAGE\",\"coordinate_x\":362.0,\"coordinate_y\":25.0,\"sinkVertices\":[\"{\\\"name\\\":\\\"LatexEditorView\\\",\\\"path\\\":\\\""
							 + relativePath + "\\\\\\\\view\\\\\\\\LatexEditorView.java\\\",\\\"vertexType\\\":\\\"CLASS\\\",\\\"methods\\\":[{\\\"name\\\":\\\"getStrategy\\\",\\\"returnType\\\":\\\"String\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{}\\\"},{\\\"name\\\":\\\"getController\\\",\\\"returnType\\\":\\\"LatexEditorController\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{}\\\"},{\\\"name\\\":\\\"getFilename\\\",\\\"returnType\\\":\\\"String\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{}\\\"},{\\\"name\\\":\\\"setStrategy\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{\\\\\\\"strategy\\\\\\\":\\\\\\\"String\\\\\\\"}\\\"},{\\\"name\\\":\\\"getVersionsManager\\\",\\\"returnType\\\":\\\"VersionsManager\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{}\\\"},{\\\"name\\\":\\\"setController\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{\\\\\\\"controller\\\\\\\":\\\\\\\"LatexEditorController\\\\\\\"}\\\"},{\\\"name\\\":\\\"getType\\\",\\\"returnType\\\":\\\"String\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{}\\\"},{\\\"name\\\":\\\"setType\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{\\\\\\\"type\\\\\\\":\\\\\\\"String\\\\\\\"}\\\"},{\\\"name\\\":\\\"setCurrentDocument\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{\\\\\\\"currentDocument\\\\\\\":\\\\\\\"Document\\\\\\\"}\\\"},{\\\"name\\\":\\\"setFilename\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{\\\\\\\"filename\\\\\\\":\\\\\\\"String\\\\\\\"}\\\"},{\\\"name\\\":\\\"getText\\\",\\\"returnType\\\":\\\"String\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{}\\\"},{\\\"name\\\":\\\"setVersionsManager\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{\\\\\\\"versionsManager\\\\\\\":\\\\\\\"VersionsManager\\\\\\\"}\\\"},{\\\"name\\\":\\\"setText\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{\\\\\\\"text\\\\\\\":\\\\\\\"String\\\\\\\"}\\\"},{\\\"name\\\":\\\"getCurrentDocument\\\",\\\"returnType\\\":\\\"Document\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{}\\\"},{\\\"name\\\":\\\"saveToFile\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{}\\\"},{\\\"name\\\":\\\"loadFromFile\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{}\\\"},{\\\"name\\\":\\\"saveContents\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{}\\\"}],\\\"fields\\\":[{\\\"name\\\":\\\"controller\\\",\\\"returnType\\\":\\\"LatexEditorController\\\",\\\"modifier\\\":\\\"PRIVATE\\\"},{\\\"name\\\":\\\"currentDocument\\\",\\\"returnType\\\":\\\"Document\\\",\\\"modifier\\\":\\\"PRIVATE\\\"},{\\\"name\\\":\\\"type\\\",\\\"returnType\\\":\\\"String\\\",\\\"modifier\\\":\\\"PRIVATE\\\"},{\\\"name\\\":\\\"text\\\",\\\"returnType\\\":\\\"String\\\",\\\"modifier\\\":\\\"PRIVATE\\\"},{\\\"name\\\":\\\"filename\\\",\\\"returnType\\\":\\\"String\\\",\\\"modifier\\\":\\\"PRIVATE\\\"},{\\\"name\\\":\\\"strategy\\\",\\\"returnType\\\":\\\"String\\\",\\\"modifier\\\":\\\"PRIVATE\\\"},{\\\"name\\\":\\\"versionsManager\\\",\\\"returnType\\\":\\\"VersionsManager\\\",\\\"modifier\\\":\\\"PRIVATE\\\"}],\\\"arcs\\\":[{\\\"source\\\":\\\"LatexEditorView\\\",\\\"target\\\":\\\"LatexEditorController\\\",\\\"arcType\\\":\\\"DEPENDENCY\\\"},{\\\"source\\\":\\\"LatexEditorView\\\",\\\"target\\\":\\\"LatexEditorController\\\",\\\"arcType\\\":\\\"ASSOCIATION\\\"},{\\\"source\\\":\\\"LatexEditorView\\\",\\\"target\\\":\\\"VersionsManager\\\",\\\"arcType\\\":\\\"DEPENDENCY\\\"},{\\\"source\\\":\\\"LatexEditorView\\\",\\\"target\\\":\\\"VersionsManager\\\",\\\"arcType\\\":\\\"ASSOCIATION\\\"},{\\\"source\\\":\\\"LatexEditorView\\\",\\\"target\\\":\\\"Document\\\",\\\"arcType\\\":\\\"DEPENDENCY\\\"},{\\\"source\\\":\\\"LatexEditorView\\\",\\\"target\\\":\\\"Document\\\",\\\"arcType\\\":\\\"ASSOCIATION\\\"}]}\",\"{\\\"name\\\":\\\"MainWindow\\\",\\\"path\\\":\\\""
							 + relativePath + "\\\\\\\\view\\\\\\\\MainWindow.java\\\",\\\"vertexType\\\":\\\"CLASS\\\",\\\"methods\\\":[{\\\"name\\\":\\\"actionPerformed\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{\\\\\\\"e\\\\\\\":\\\\\\\"ActionEvent\\\\\\\"}\\\"},{\\\"name\\\":\\\"actionPerformed\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{\\\\\\\"e\\\\\\\":\\\\\\\"ActionEvent\\\\\\\"}\\\"},{\\\"name\\\":\\\"actionPerformed\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{\\\\\\\"e\\\\\\\":\\\\\\\"ActionEvent\\\\\\\"}\\\"},{\\\"name\\\":\\\"actionPerformed\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{\\\\\\\"e\\\\\\\":\\\\\\\"ActionEvent\\\\\\\"}\\\"},{\\\"name\\\":\\\"actionPerformed\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{\\\\\\\"arg0\\\\\\\":\\\\\\\"ActionEvent\\\\\\\"}\\\"},{\\\"name\\\":\\\"actionPerformed\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{\\\\\\\"e\\\\\\\":\\\\\\\"ActionEvent\\\\\\\"}\\\"},{\\\"name\\\":\\\"actionPerformed\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{\\\\\\\"arg0\\\\\\\":\\\\\\\"ActionEvent\\\\\\\"}\\\"},{\\\"name\\\":\\\"actionPerformed\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{\\\\\\\"e\\\\\\\":\\\\\\\"ActionEvent\\\\\\\"}\\\"},{\\\"name\\\":\\\"actionPerformed\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{\\\\\\\"arg0\\\\\\\":\\\\\\\"ActionEvent\\\\\\\"}\\\"},{\\\"name\\\":\\\"actionPerformed\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{\\\\\\\"e\\\\\\\":\\\\\\\"ActionEvent\\\\\\\"}\\\"},{\\\"name\\\":\\\"actionPerformed\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{\\\\\\\"e\\\\\\\":\\\\\\\"ActionEvent\\\\\\\"}\\\"},{\\\"name\\\":\\\"editContents\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{\\\\\\\"type\\\\\\\":\\\\\\\"String\\\\\\\"}\\\"},{\\\"name\\\":\\\"actionPerformed\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{\\\\\\\"e\\\\\\\":\\\\\\\"ActionEvent\\\\\\\"}\\\"},{\\\"name\\\":\\\"actionPerformed\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{\\\\\\\"e\\\\\\\":\\\\\\\"ActionEvent\\\\\\\"}\\\"},{\\\"name\\\":\\\"actionPerformed\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{\\\\\\\"e\\\\\\\":\\\\\\\"ActionEvent\\\\\\\"}\\\"},{\\\"name\\\":\\\"actionPerformed\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{\\\\\\\"arg0\\\\\\\":\\\\\\\"ActionEvent\\\\\\\"}\\\"},{\\\"name\\\":\\\"actionPerformed\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{\\\\\\\"e\\\\\\\":\\\\\\\"ActionEvent\\\\\\\"}\\\"},{\\\"name\\\":\\\"MainWindow\\\",\\\"returnType\\\":\\\"Constructor\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{\\\\\\\"latexEditorView\\\\\\\":\\\\\\\"LatexEditorView\\\\\\\"}\\\"},{\\\"name\\\":\\\"initialize\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"PRIVATE\\\",\\\"parameters\\\":\\\"{}\\\"}],\\\"fields\\\":[{\\\"name\\\":\\\"frame\\\",\\\"returnType\\\":\\\"JFrame\\\",\\\"modifier\\\":\\\"PRIVATE\\\"},{\\\"name\\\":\\\"editorPane\\\",\\\"returnType\\\":\\\"JEditorPane\\\",\\\"modifier\\\":\\\"PRIVATE\\\"},{\\\"name\\\":\\\"latexEditorView\\\",\\\"returnType\\\":\\\"LatexEditorView\\\",\\\"modifier\\\":\\\"PRIVATE\\\"}],\\\"arcs\\\":[{\\\"source\\\":\\\"MainWindow\\\",\\\"target\\\":\\\"LatexEditorView\\\",\\\"arcType\\\":\\\"DEPENDENCY\\\"},{\\\"source\\\":\\\"MainWindow\\\",\\\"target\\\":\\\"LatexEditorView\\\",\\\"arcType\\\":\\\"ASSOCIATION\\\"},{\\\"source\\\":\\\"MainWindow\\\",\\\"target\\\":\\\"ChooseTemplate\\\",\\\"arcType\\\":\\\"DEPENDENCY\\\"}]}\",\"{\\\"name\\\":\\\"ChooseTemplate\\\",\\\"path\\\":\\\""
							 + relativePath + "\\\\\\\\view\\\\\\\\ChooseTemplate.java\\\",\\\"vertexType\\\":\\\"CLASS\\\",\\\"methods\\\":[{\\\"name\\\":\\\"actionPerformed\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{\\\\\\\"e\\\\\\\":\\\\\\\"ActionEvent\\\\\\\"}\\\"},{\\\"name\\\":\\\"initialize\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"PRIVATE\\\",\\\"parameters\\\":\\\"{}\\\"},{\\\"name\\\":\\\"actionPerformed\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{\\\\\\\"e\\\\\\\":\\\\\\\"ActionEvent\\\\\\\"}\\\"},{\\\"name\\\":\\\"actionPerformed\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{\\\\\\\"arg0\\\\\\\":\\\\\\\"ActionEvent\\\\\\\"}\\\"},{\\\"name\\\":\\\"diselectRadioButtons\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"PRIVATE\\\",\\\"parameters\\\":\\\"{\\\\\\\"radioButton3\\\\\\\":\\\\\\\"JRadioButton\\\\\\\",\\\\\\\"radioButton4\\\\\\\":\\\\\\\"JRadioButton\\\\\\\",\\\\\\\"radioButton1\\\\\\\":\\\\\\\"JRadioButton\\\\\\\",\\\\\\\"radioButton2\\\\\\\":\\\\\\\"JRadioButton\\\\\\\"}\\\"},{\\\"name\\\":\\\"actionPerformed\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{\\\\\\\"e\\\\\\\":\\\\\\\"ActionEvent\\\\\\\"}\\\"},{\\\"name\\\":\\\"actionPerformed\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{\\\\\\\"e\\\\\\\":\\\\\\\"ActionEvent\\\\\\\"}\\\"},{\\\"name\\\":\\\"ChooseTemplate\\\",\\\"returnType\\\":\\\"Constructor\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{\\\\\\\"previous\\\\\\\":\\\\\\\"String\\\\\\\",\\\\\\\"latexEditorView\\\\\\\":\\\\\\\"LatexEditorView\\\\\\\"}\\\"},{\\\"name\\\":\\\"actionPerformed\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{\\\\\\\"e\\\\\\\":\\\\\\\"ActionEvent\\\\\\\"}\\\"}],\\\"fields\\\":[{\\\"name\\\":\\\"frame\\\",\\\"returnType\\\":\\\"JFrame\\\",\\\"modifier\\\":\\\"PRIVATE\\\"},{\\\"name\\\":\\\"latexEditorView\\\",\\\"returnType\\\":\\\"LatexEditorView\\\",\\\"modifier\\\":\\\"PRIVATE\\\"},{\\\"name\\\":\\\"previous\\\",\\\"returnType\\\":\\\"String\\\",\\\"modifier\\\":\\\"PRIVATE\\\"}],\\\"arcs\\\":[{\\\"source\\\":\\\"ChooseTemplate\\\",\\\"target\\\":\\\"LatexEditorView\\\",\\\"arcType\\\":\\\"DEPENDENCY\\\"},{\\\"source\\\":\\\"ChooseTemplate\\\",\\\"target\\\":\\\"LatexEditorView\\\",\\\"arcType\\\":\\\"ASSOCIATION\\\"},{\\\"source\\\":\\\"ChooseTemplate\\\",\\\"target\\\":\\\"MainWindow\\\",\\\"arcType\\\":\\\"DEPENDENCY\\\"},{\\\"source\\\":\\\"ChooseTemplate\\\",\\\"target\\\":\\\"OpeningWindow\\\",\\\"arcType\\\":\\\"DEPENDENCY\\\"}]}\",\"{\\\"name\\\":\\\"OpeningWindow\\\",\\\"path\\\":\\\""
							 + relativePath + "\\\\\\\\view\\\\\\\\OpeningWindow.java\\\",\\\"vertexType\\\":\\\"CLASS\\\",\\\"methods\\\":[{\\\"name\\\":\\\"main\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{\\\\\\\"args\\\\\\\":\\\\\\\"String[]\\\\\\\"}\\\"},{\\\"name\\\":\\\"actionPerformed\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{\\\\\\\"e\\\\\\\":\\\\\\\"ActionEvent\\\\\\\"}\\\"},{\\\"name\\\":\\\"initialize\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"PRIVATE\\\",\\\"parameters\\\":\\\"{}\\\"},{\\\"name\\\":\\\"OpeningWindow\\\",\\\"returnType\\\":\\\"Constructor\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{}\\\"},{\\\"name\\\":\\\"actionPerformed\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{\\\\\\\"e\\\\\\\":\\\\\\\"ActionEvent\\\\\\\"}\\\"},{\\\"name\\\":\\\"run\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{}\\\"},{\\\"name\\\":\\\"actionPerformed\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{\\\\\\\"e\\\\\\\":\\\\\\\"ActionEvent\\\\\\\"}\\\"}],\\\"fields\\\":[{\\\"name\\\":\\\"frame\\\",\\\"returnType\\\":\\\"JFrame\\\",\\\"modifier\\\":\\\"PRIVATE\\\"},{\\\"name\\\":\\\"latexEditorView\\\",\\\"returnType\\\":\\\"LatexEditorView\\\",\\\"modifier\\\":\\\"PRIVATE\\\"}],\\\"arcs\\\":[{\\\"source\\\":\\\"OpeningWindow\\\",\\\"target\\\":\\\"LatexEditorController\\\",\\\"arcType\\\":\\\"DEPENDENCY\\\"},{\\\"source\\\":\\\"OpeningWindow\\\",\\\"target\\\":\\\"VersionsManager\\\",\\\"arcType\\\":\\\"DEPENDENCY\\\"},{\\\"source\\\":\\\"OpeningWindow\\\",\\\"target\\\":\\\"LatexEditorView\\\",\\\"arcType\\\":\\\"ASSOCIATION\\\"},{\\\"source\\\":\\\"OpeningWindow\\\",\\\"target\\\":\\\"ChooseTemplate\\\",\\\"arcType\\\":\\\"DEPENDENCY\\\"},{\\\"source\\\":\\\"OpeningWindow\\\",\\\"target\\\":\\\"VersionsStrategy\\\",\\\"arcType\\\":\\\"DEPENDENCY\\\"},{\\\"source\\\":\\\"OpeningWindow\\\",\\\"target\\\":\\\"VolatileVersionsStrategy\\\",\\\"arcType\\\":\\\"DEPENDENCY\\\"}]}\"],\"parent\":{\"name\":\"src\",\"path\":\""
							 + relativePath + "\",\"vertexType\":\"PACKAGE_PRIVATE\"},\"neighbours\":[],\"arcs\":[{\"source\":\"src.view\",\"target\":\"src.controller\",\"arcType\":\"DEPENDENCY\"},{\"source\":\"src.view\",\"target\":\"src.model\",\"arcType\":\"DEPENDENCY\"},{\"source\":\"src.view\",\"target\":\"src.model.strategies\",\"arcType\":\"DEPENDENCY\"}]},{\"name\":\"src.controller\",\"path\":\""
							 + relativePath + "\\\\controller\",\"vertexType\":\"PACKAGE\",\"coordinate_x\":72.0,\"coordinate_y\":70.0,\"sinkVertices\":[\"{\\\"name\\\":\\\"LatexEditorController\\\",\\\"path\\\":\\\""
							 + relativePath + "\\\\\\\\controller\\\\\\\\LatexEditorController.java\\\",\\\"vertexType\\\":\\\"CLASS\\\",\\\"methods\\\":[{\\\"name\\\":\\\"LatexEditorController\\\",\\\"returnType\\\":\\\"Constructor\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{\\\\\\\"versionsManager\\\\\\\":\\\\\\\"VersionsManager\\\\\\\"}\\\"},{\\\"name\\\":\\\"enact\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{\\\\\\\"command\\\\\\\":\\\\\\\"String\\\\\\\"}\\\"}],\\\"fields\\\":[{\\\"name\\\":\\\"commands\\\",\\\"returnType\\\":\\\"HashMap[String,Command]\\\",\\\"modifier\\\":\\\"PRIVATE\\\"}],\\\"arcs\\\":[{\\\"source\\\":\\\"LatexEditorController\\\",\\\"target\\\":\\\"VersionsManager\\\",\\\"arcType\\\":\\\"DEPENDENCY\\\"},{\\\"source\\\":\\\"LatexEditorController\\\",\\\"target\\\":\\\"Command\\\",\\\"arcType\\\":\\\"AGGREGATION\\\"},{\\\"source\\\":\\\"LatexEditorController\\\",\\\"target\\\":\\\"CommandFactory\\\",\\\"arcType\\\":\\\"DEPENDENCY\\\"}]}\"],\"parent\":{\"name\":\"src\",\"path\":\""
							 + relativePath + "\",\"vertexType\":\"PACKAGE_PRIVATE\"},\"neighbours\":[{\"name\":\"src.controller.commands\",\"path\":\"\\\""
							 + relativePath + "\\\\\\\\controller\\\\\\\\commands\",\"vertexType\":\"PACKAGE\",\"parentName\":\"src.controller\"}],\"arcs\":[{\"source\":\"src.controller\",\"target\":\"src.model\",\"arcType\":\"DEPENDENCY\"},{\"source\":\"src.controller\",\"target\":\"src.controller.commands\",\"arcType\":\"DEPENDENCY\"}]},{\"name\":\"src.controller.commands\",\"path\":\""
							 + relativePath + "\\\\controller\\\\commands\",\"vertexType\":\"PACKAGE\",\"coordinate_x\":25.0,\"coordinate_y\":140.0,\"sinkVertices\":[\"{\\\"name\\\":\\\"CreateCommand\\\",\\\"path\\\":\\\""
							 + relativePath + "\\\\\\\\controller\\\\\\\\commands\\\\\\\\CreateCommand.java\\\",\\\"vertexType\\\":\\\"CLASS\\\",\\\"methods\\\":[{\\\"name\\\":\\\"CreateCommand\\\",\\\"returnType\\\":\\\"Constructor\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{\\\\\\\"documentManager\\\\\\\":\\\\\\\"DocumentManager\\\\\\\",\\\\\\\"versionsManager\\\\\\\":\\\\\\\"VersionsManager\\\\\\\"}\\\"},{\\\"name\\\":\\\"execute\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{}\\\"}],\\\"fields\\\":[{\\\"name\\\":\\\"documentManager\\\",\\\"returnType\\\":\\\"DocumentManager\\\",\\\"modifier\\\":\\\"PRIVATE\\\"},{\\\"name\\\":\\\"versionsManager\\\",\\\"returnType\\\":\\\"VersionsManager\\\",\\\"modifier\\\":\\\"PRIVATE\\\"}],\\\"arcs\\\":[{\\\"source\\\":\\\"CreateCommand\\\",\\\"target\\\":\\\"VersionsManager\\\",\\\"arcType\\\":\\\"DEPENDENCY\\\"},{\\\"source\\\":\\\"CreateCommand\\\",\\\"target\\\":\\\"VersionsManager\\\",\\\"arcType\\\":\\\"ASSOCIATION\\\"},{\\\"source\\\":\\\"CreateCommand\\\",\\\"target\\\":\\\"DocumentManager\\\",\\\"arcType\\\":\\\"DEPENDENCY\\\"},{\\\"source\\\":\\\"CreateCommand\\\",\\\"target\\\":\\\"DocumentManager\\\",\\\"arcType\\\":\\\"ASSOCIATION\\\"},{\\\"source\\\":\\\"CreateCommand\\\",\\\"target\\\":\\\"Command\\\",\\\"arcType\\\":\\\"IMPLEMENTATION\\\"}]}\",\"{\\\"name\\\":\\\"AddLatexCommand\\\",\\\"path\\\":\\\""
							 + relativePath + "\\\\\\\\controller\\\\\\\\commands\\\\\\\\AddLatexCommand.java\\\",\\\"vertexType\\\":\\\"CLASS\\\",\\\"methods\\\":[{\\\"name\\\":\\\"AddLatexCommand\\\",\\\"returnType\\\":\\\"Constructor\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{\\\\\\\"versionsManager\\\\\\\":\\\\\\\"VersionsManager\\\\\\\"}\\\"},{\\\"name\\\":\\\"execute\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{}\\\"}],\\\"fields\\\":[{\\\"name\\\":\\\"versionsManager\\\",\\\"returnType\\\":\\\"VersionsManager\\\",\\\"modifier\\\":\\\"PRIVATE\\\"}],\\\"arcs\\\":[{\\\"source\\\":\\\"AddLatexCommand\\\",\\\"target\\\":\\\"VersionsManager\\\",\\\"arcType\\\":\\\"DEPENDENCY\\\"},{\\\"source\\\":\\\"AddLatexCommand\\\",\\\"target\\\":\\\"VersionsManager\\\",\\\"arcType\\\":\\\"ASSOCIATION\\\"},{\\\"source\\\":\\\"AddLatexCommand\\\",\\\"target\\\":\\\"Command\\\",\\\"arcType\\\":\\\"IMPLEMENTATION\\\"}]}\",\"{\\\"name\\\":\\\"SaveCommand\\\",\\\"path\\\":\\\""
							 + relativePath + "\\\\\\\\controller\\\\\\\\commands\\\\\\\\SaveCommand.java\\\",\\\"vertexType\\\":\\\"CLASS\\\",\\\"methods\\\":[{\\\"name\\\":\\\"execute\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{}\\\"},{\\\"name\\\":\\\"SaveCommand\\\",\\\"returnType\\\":\\\"Constructor\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{\\\\\\\"versionsManager\\\\\\\":\\\\\\\"VersionsManager\\\\\\\"}\\\"}],\\\"fields\\\":[{\\\"name\\\":\\\"versionsManager\\\",\\\"returnType\\\":\\\"VersionsManager\\\",\\\"modifier\\\":\\\"PRIVATE\\\"}],\\\"arcs\\\":[{\\\"source\\\":\\\"SaveCommand\\\",\\\"target\\\":\\\"VersionsManager\\\",\\\"arcType\\\":\\\"DEPENDENCY\\\"},{\\\"source\\\":\\\"SaveCommand\\\",\\\"target\\\":\\\"VersionsManager\\\",\\\"arcType\\\":\\\"ASSOCIATION\\\"},{\\\"source\\\":\\\"SaveCommand\\\",\\\"target\\\":\\\"Command\\\",\\\"arcType\\\":\\\"IMPLEMENTATION\\\"}]}\",\"{\\\"name\\\":\\\"ChangeVersionsStrategyCommand\\\",\\\"path\\\":\\\""
							 + relativePath + "\\\\\\\\controller\\\\\\\\commands\\\\\\\\ChangeVersionsStrategyCommand.java\\\",\\\"vertexType\\\":\\\"CLASS\\\",\\\"methods\\\":[{\\\"name\\\":\\\"ChangeVersionsStrategyCommand\\\",\\\"returnType\\\":\\\"Constructor\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{\\\\\\\"versionsManager\\\\\\\":\\\\\\\"VersionsManager\\\\\\\"}\\\"},{\\\"name\\\":\\\"execute\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{}\\\"}],\\\"fields\\\":[{\\\"name\\\":\\\"versionsManager\\\",\\\"returnType\\\":\\\"VersionsManager\\\",\\\"modifier\\\":\\\"PRIVATE\\\"}],\\\"arcs\\\":[{\\\"source\\\":\\\"ChangeVersionsStrategyCommand\\\",\\\"target\\\":\\\"VersionsManager\\\",\\\"arcType\\\":\\\"DEPENDENCY\\\"},{\\\"source\\\":\\\"ChangeVersionsStrategyCommand\\\",\\\"target\\\":\\\"VersionsManager\\\",\\\"arcType\\\":\\\"ASSOCIATION\\\"},{\\\"source\\\":\\\"ChangeVersionsStrategyCommand\\\",\\\"target\\\":\\\"Command\\\",\\\"arcType\\\":\\\"IMPLEMENTATION\\\"}]}\",\"{\\\"name\\\":\\\"Command\\\",\\\"path\\\":\\\""
							 + relativePath + "\\\\\\\\controller\\\\\\\\commands\\\\\\\\Command.java\\\",\\\"vertexType\\\":\\\"INTERFACE\\\",\\\"methods\\\":[{\\\"name\\\":\\\"execute\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{}\\\"}],\\\"fields\\\":[],\\\"arcs\\\":[]}\",\"{\\\"name\\\":\\\"DisableVersionsManagementCommand\\\",\\\"path\\\":\\\""
							 + relativePath + "\\\\\\\\controller\\\\\\\\commands\\\\\\\\DisableVersionsManagementCommand.java\\\",\\\"vertexType\\\":\\\"CLASS\\\",\\\"methods\\\":[{\\\"name\\\":\\\"DisableVersionsManagementCommand\\\",\\\"returnType\\\":\\\"Constructor\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{\\\\\\\"versionsManager\\\\\\\":\\\\\\\"VersionsManager\\\\\\\"}\\\"},{\\\"name\\\":\\\"execute\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{}\\\"}],\\\"fields\\\":[{\\\"name\\\":\\\"versionsManager\\\",\\\"returnType\\\":\\\"VersionsManager\\\",\\\"modifier\\\":\\\"PRIVATE\\\"}],\\\"arcs\\\":[{\\\"source\\\":\\\"DisableVersionsManagementCommand\\\",\\\"target\\\":\\\"VersionsManager\\\",\\\"arcType\\\":\\\"DEPENDENCY\\\"},{\\\"source\\\":\\\"DisableVersionsManagementCommand\\\",\\\"target\\\":\\\"VersionsManager\\\",\\\"arcType\\\":\\\"ASSOCIATION\\\"},{\\\"source\\\":\\\"DisableVersionsManagementCommand\\\",\\\"target\\\":\\\"Command\\\",\\\"arcType\\\":\\\"IMPLEMENTATION\\\"}]}\",\"{\\\"name\\\":\\\"LoadCommand\\\",\\\"path\\\":\\\""
							 + relativePath + "\\\\\\\\controller\\\\\\\\commands\\\\\\\\LoadCommand.java\\\",\\\"vertexType\\\":\\\"CLASS\\\",\\\"methods\\\":[{\\\"name\\\":\\\"execute\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{}\\\"},{\\\"name\\\":\\\"LoadCommand\\\",\\\"returnType\\\":\\\"Constructor\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{\\\\\\\"versionsManager\\\\\\\":\\\\\\\"VersionsManager\\\\\\\"}\\\"},{\\\"name\\\":\\\"getVersionsManager\\\",\\\"returnType\\\":\\\"VersionsManager\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{}\\\"},{\\\"name\\\":\\\"setVersionsManager\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{\\\\\\\"versionsManager\\\\\\\":\\\\\\\"VersionsManager\\\\\\\"}\\\"}],\\\"fields\\\":[{\\\"name\\\":\\\"versionsManager\\\",\\\"returnType\\\":\\\"VersionsManager\\\",\\\"modifier\\\":\\\"PRIVATE\\\"}],\\\"arcs\\\":[{\\\"source\\\":\\\"LoadCommand\\\",\\\"target\\\":\\\"VersionsManager\\\",\\\"arcType\\\":\\\"DEPENDENCY\\\"},{\\\"source\\\":\\\"LoadCommand\\\",\\\"target\\\":\\\"VersionsManager\\\",\\\"arcType\\\":\\\"ASSOCIATION\\\"},{\\\"source\\\":\\\"LoadCommand\\\",\\\"target\\\":\\\"Command\\\",\\\"arcType\\\":\\\"IMPLEMENTATION\\\"}]}\",\"{\\\"name\\\":\\\"CommandFactory\\\",\\\"path\\\":\\\""
							 + relativePath + "\\\\\\\\controller\\\\\\\\commands\\\\\\\\CommandFactory.java\\\",\\\"vertexType\\\":\\\"CLASS\\\",\\\"methods\\\":[{\\\"name\\\":\\\"CommandFactory\\\",\\\"returnType\\\":\\\"Constructor\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{\\\\\\\"versionsManager\\\\\\\":\\\\\\\"VersionsManager\\\\\\\"}\\\"},{\\\"name\\\":\\\"createCommand\\\",\\\"returnType\\\":\\\"Command\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{\\\\\\\"type\\\\\\\":\\\\\\\"String\\\\\\\"}\\\"}],\\\"fields\\\":[{\\\"name\\\":\\\"documentManager\\\",\\\"returnType\\\":\\\"DocumentManager\\\",\\\"modifier\\\":\\\"PRIVATE\\\"},{\\\"name\\\":\\\"versionsManager\\\",\\\"returnType\\\":\\\"VersionsManager\\\",\\\"modifier\\\":\\\"PRIVATE\\\"}],\\\"arcs\\\":[{\\\"source\\\":\\\"CommandFactory\\\",\\\"target\\\":\\\"VersionsManager\\\",\\\"arcType\\\":\\\"DEPENDENCY\\\"},{\\\"source\\\":\\\"CommandFactory\\\",\\\"target\\\":\\\"VersionsManager\\\",\\\"arcType\\\":\\\"ASSOCIATION\\\"},{\\\"source\\\":\\\"CommandFactory\\\",\\\"target\\\":\\\"DocumentManager\\\",\\\"arcType\\\":\\\"ASSOCIATION\\\"},{\\\"source\\\":\\\"CommandFactory\\\",\\\"target\\\":\\\"CreateCommand\\\",\\\"arcType\\\":\\\"DEPENDENCY\\\"},{\\\"source\\\":\\\"CommandFactory\\\",\\\"target\\\":\\\"AddLatexCommand\\\",\\\"arcType\\\":\\\"DEPENDENCY\\\"},{\\\"source\\\":\\\"CommandFactory\\\",\\\"target\\\":\\\"SaveCommand\\\",\\\"arcType\\\":\\\"DEPENDENCY\\\"},{\\\"source\\\":\\\"CommandFactory\\\",\\\"target\\\":\\\"ChangeVersionsStrategyCommand\\\",\\\"arcType\\\":\\\"DEPENDENCY\\\"},{\\\"source\\\":\\\"CommandFactory\\\",\\\"target\\\":\\\"Command\\\",\\\"arcType\\\":\\\"DEPENDENCY\\\"},{\\\"source\\\":\\\"CommandFactory\\\",\\\"target\\\":\\\"DisableVersionsManagementCommand\\\",\\\"arcType\\\":\\\"DEPENDENCY\\\"},{\\\"source\\\":\\\"CommandFactory\\\",\\\"target\\\":\\\"LoadCommand\\\",\\\"arcType\\\":\\\"DEPENDENCY\\\"},{\\\"source\\\":\\\"CommandFactory\\\",\\\"target\\\":\\\"EnableVersionsManagementCommand\\\",\\\"arcType\\\":\\\"DEPENDENCY\\\"},{\\\"source\\\":\\\"CommandFactory\\\",\\\"target\\\":\\\"EditCommand\\\",\\\"arcType\\\":\\\"DEPENDENCY\\\"},{\\\"source\\\":\\\"CommandFactory\\\",\\\"target\\\":\\\"RollbackToPreviousVersionCommand\\\",\\\"arcType\\\":\\\"DEPENDENCY\\\"}]}\",\"{\\\"name\\\":\\\"EnableVersionsManagementCommand\\\",\\\"path\\\":\\\""
							 + relativePath + "\\\\\\\\controller\\\\\\\\commands\\\\\\\\EnableVersionsManagementCommand.java\\\",\\\"vertexType\\\":\\\"CLASS\\\",\\\"methods\\\":[{\\\"name\\\":\\\"EnableVersionsManagementCommand\\\",\\\"returnType\\\":\\\"Constructor\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{\\\\\\\"versionsManager\\\\\\\":\\\\\\\"VersionsManager\\\\\\\"}\\\"},{\\\"name\\\":\\\"execute\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{}\\\"}],\\\"fields\\\":[{\\\"name\\\":\\\"versionsManager\\\",\\\"returnType\\\":\\\"VersionsManager\\\",\\\"modifier\\\":\\\"PRIVATE\\\"}],\\\"arcs\\\":[{\\\"source\\\":\\\"EnableVersionsManagementCommand\\\",\\\"target\\\":\\\"VersionsManager\\\",\\\"arcType\\\":\\\"DEPENDENCY\\\"},{\\\"source\\\":\\\"EnableVersionsManagementCommand\\\",\\\"target\\\":\\\"VersionsManager\\\",\\\"arcType\\\":\\\"ASSOCIATION\\\"},{\\\"source\\\":\\\"EnableVersionsManagementCommand\\\",\\\"target\\\":\\\"Command\\\",\\\"arcType\\\":\\\"IMPLEMENTATION\\\"}]}\",\"{\\\"name\\\":\\\"EditCommand\\\",\\\"path\\\":\\\""
							 + relativePath + "\\\\\\\\controller\\\\\\\\commands\\\\\\\\EditCommand.java\\\",\\\"vertexType\\\":\\\"CLASS\\\",\\\"methods\\\":[{\\\"name\\\":\\\"execute\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{}\\\"},{\\\"name\\\":\\\"EditCommand\\\",\\\"returnType\\\":\\\"Constructor\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{\\\\\\\"versionsManager\\\\\\\":\\\\\\\"VersionsManager\\\\\\\"}\\\"}],\\\"fields\\\":[{\\\"name\\\":\\\"versionsManager\\\",\\\"returnType\\\":\\\"VersionsManager\\\",\\\"modifier\\\":\\\"PRIVATE\\\"}],\\\"arcs\\\":[{\\\"source\\\":\\\"EditCommand\\\",\\\"target\\\":\\\"VersionsManager\\\",\\\"arcType\\\":\\\"DEPENDENCY\\\"},{\\\"source\\\":\\\"EditCommand\\\",\\\"target\\\":\\\"VersionsManager\\\",\\\"arcType\\\":\\\"ASSOCIATION\\\"},{\\\"source\\\":\\\"EditCommand\\\",\\\"target\\\":\\\"Command\\\",\\\"arcType\\\":\\\"IMPLEMENTATION\\\"}]}\",\"{\\\"name\\\":\\\"RollbackToPreviousVersionCommand\\\",\\\"path\\\":\\\""
							 + relativePath + "\\\\\\\\controller\\\\\\\\commands\\\\\\\\RollbackToPreviousVersionCommand.java\\\",\\\"vertexType\\\":\\\"CLASS\\\",\\\"methods\\\":[{\\\"name\\\":\\\"RollbackToPreviousVersionCommand\\\",\\\"returnType\\\":\\\"Constructor\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{\\\\\\\"versionsManager\\\\\\\":\\\\\\\"VersionsManager\\\\\\\"}\\\"},{\\\"name\\\":\\\"execute\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{}\\\"}],\\\"fields\\\":[{\\\"name\\\":\\\"versionsManager\\\",\\\"returnType\\\":\\\"VersionsManager\\\",\\\"modifier\\\":\\\"PRIVATE\\\"}],\\\"arcs\\\":[{\\\"source\\\":\\\"RollbackToPreviousVersionCommand\\\",\\\"target\\\":\\\"VersionsManager\\\",\\\"arcType\\\":\\\"DEPENDENCY\\\"},{\\\"source\\\":\\\"RollbackToPreviousVersionCommand\\\",\\\"target\\\":\\\"VersionsManager\\\",\\\"arcType\\\":\\\"ASSOCIATION\\\"},{\\\"source\\\":\\\"RollbackToPreviousVersionCommand\\\",\\\"target\\\":\\\"Command\\\",\\\"arcType\\\":\\\"IMPLEMENTATION\\\"}]}\"],\"parent\":{\"name\":\"src.controller\",\"path\":\""
							 + relativePath + "\\\\\\\\controller\",\"vertexType\":\"PACKAGE_PRIVATE\"},\"neighbours\":[],\"arcs\":[{\"source\":\"src.controller.commands\",\"target\":\"src.model\",\"arcType\":\"DEPENDENCY\"}]},{\"name\":\"src.model\",\"path\":\""
							 + relativePath + "\\\\model\",\"vertexType\":\"PACKAGE\",\"coordinate_x\":217.0,\"coordinate_y\":210.0,\"sinkVertices\":[\"{\\\"name\\\":\\\"VersionsManager\\\",\\\"path\\\":\\\""
							 + relativePath + "\\\\\\\\model\\\\\\\\VersionsManager.java\\\",\\\"vertexType\\\":\\\"CLASS\\\",\\\"methods\\\":[{\\\"name\\\":\\\"setStrategy\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{\\\\\\\"strategy\\\\\\\":\\\\\\\"VersionsStrategy\\\\\\\"}\\\"},{\\\"name\\\":\\\"setCurrentVersion\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{\\\\\\\"document\\\\\\\":\\\\\\\"Document\\\\\\\"}\\\"},{\\\"name\\\":\\\"changeStrategy\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{}\\\"},{\\\"name\\\":\\\"getType\\\",\\\"returnType\\\":\\\"String\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{}\\\"},{\\\"name\\\":\\\"rollback\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{}\\\"},{\\\"name\\\":\\\"isEnabled\\\",\\\"returnType\\\":\\\"boolean\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{}\\\"},{\\\"name\\\":\\\"setPreviousVersion\\\",\\\"returnType\\\":\\\"Document\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{}\\\"},{\\\"name\\\":\\\"getStrategy\\\",\\\"returnType\\\":\\\"VersionsStrategy\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{}\\\"},{\\\"name\\\":\\\"saveContents\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{}\\\"},{\\\"name\\\":\\\"enable\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{}\\\"},{\\\"name\\\":\\\"loadFromFile\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{}\\\"},{\\\"name\\\":\\\"VersionsManager\\\",\\\"returnType\\\":\\\"Constructor\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{\\\\\\\"latexEditorView\\\\\\\":\\\\\\\"LatexEditorView\\\\\\\",\\\\\\\"versionsStrategy\\\\\\\":\\\\\\\"VersionsStrategy\\\\\\\"}\\\"},{\\\"name\\\":\\\"enableStrategy\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{}\\\"},{\\\"name\\\":\\\"disable\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{}\\\"},{\\\"name\\\":\\\"putVersion\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{\\\\\\\"document\\\\\\\":\\\\\\\"Document\\\\\\\"}\\\"},{\\\"name\\\":\\\"rollbackToPreviousVersion\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{}\\\"},{\\\"name\\\":\\\"saveToFile\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{}\\\"}],\\\"fields\\\":[{\\\"name\\\":\\\"enabled\\\",\\\"returnType\\\":\\\"boolean\\\",\\\"modifier\\\":\\\"PRIVATE\\\"},{\\\"name\\\":\\\"strategy\\\",\\\"returnType\\\":\\\"VersionsStrategy\\\",\\\"modifier\\\":\\\"PRIVATE\\\"},{\\\"name\\\":\\\"latexEditorView\\\",\\\"returnType\\\":\\\"LatexEditorView\\\",\\\"modifier\\\":\\\"PRIVATE\\\"}],\\\"arcs\\\":[{\\\"source\\\":\\\"VersionsManager\\\",\\\"target\\\":\\\"Document\\\",\\\"arcType\\\":\\\"DEPENDENCY\\\"},{\\\"source\\\":\\\"VersionsManager\\\",\\\"target\\\":\\\"LatexEditorView\\\",\\\"arcType\\\":\\\"DEPENDENCY\\\"},{\\\"source\\\":\\\"VersionsManager\\\",\\\"target\\\":\\\"LatexEditorView\\\",\\\"arcType\\\":\\\"ASSOCIATION\\\"},{\\\"source\\\":\\\"VersionsManager\\\",\\\"target\\\":\\\"VersionsStrategy\\\",\\\"arcType\\\":\\\"DEPENDENCY\\\"},{\\\"source\\\":\\\"VersionsManager\\\",\\\"target\\\":\\\"VersionsStrategy\\\",\\\"arcType\\\":\\\"ASSOCIATION\\\"}]}\",\"{\\\"name\\\":\\\"DocumentManager\\\",\\\"path\\\":\\\""
							 + relativePath + "\\\\\\\\model\\\\\\\\DocumentManager.java\\\",\\\"vertexType\\\":\\\"CLASS\\\",\\\"methods\\\":[{\\\"name\\\":\\\"createDocument\\\",\\\"returnType\\\":\\\"Document\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{\\\\\\\"type\\\\\\\":\\\\\\\"String\\\\\\\"}\\\"},{\\\"name\\\":\\\"DocumentManager\\\",\\\"returnType\\\":\\\"Constructor\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{}\\\"},{\\\"name\\\":\\\"getContents\\\",\\\"returnType\\\":\\\"String\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{\\\\\\\"type\\\\\\\":\\\\\\\"String\\\\\\\"}\\\"}],\\\"fields\\\":[{\\\"name\\\":\\\"templates\\\",\\\"returnType\\\":\\\"HashMap[String,Document]\\\",\\\"modifier\\\":\\\"PRIVATE\\\"}],\\\"arcs\\\":[{\\\"source\\\":\\\"DocumentManager\\\",\\\"target\\\":\\\"Document\\\",\\\"arcType\\\":\\\"DEPENDENCY\\\"},{\\\"source\\\":\\\"DocumentManager\\\",\\\"target\\\":\\\"Document\\\",\\\"arcType\\\":\\\"AGGREGATION\\\"}]}\",\"{\\\"name\\\":\\\"Document\\\",\\\"path\\\":\\\""
							 + relativePath + "\\\\\\\\model\\\\\\\\Document.java\\\",\\\"vertexType\\\":\\\"CLASS\\\",\\\"methods\\\":[{\\\"name\\\":\\\"clone\\\",\\\"returnType\\\":\\\"Document\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{}\\\"},{\\\"name\\\":\\\"changeVersion\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{}\\\"},{\\\"name\\\":\\\"save\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{\\\\\\\"filename\\\\\\\":\\\\\\\"String\\\\\\\"}\\\"},{\\\"name\\\":\\\"setContents\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{\\\\\\\"contents\\\\\\\":\\\\\\\"String\\\\\\\"}\\\"},{\\\"name\\\":\\\"getContents\\\",\\\"returnType\\\":\\\"String\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{}\\\"},{\\\"name\\\":\\\"getVersionID\\\",\\\"returnType\\\":\\\"String\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{}\\\"},{\\\"name\\\":\\\"Document\\\",\\\"returnType\\\":\\\"Constructor\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{\\\\\\\"date\\\\\\\":\\\\\\\"String\\\\\\\",\\\\\\\"copyright\\\\\\\":\\\\\\\"String\\\\\\\",\\\\\\\"versionID\\\\\\\":\\\\\\\"String\\\\\\\",\\\\\\\"contents\\\\\\\":\\\\\\\"String\\\\\\\",\\\\\\\"author\\\\\\\":\\\\\\\"String\\\\\\\"}\\\"},{\\\"name\\\":\\\"Document\\\",\\\"returnType\\\":\\\"Constructor\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{}\\\"}],\\\"fields\\\":[{\\\"name\\\":\\\"author\\\",\\\"returnType\\\":\\\"String\\\",\\\"modifier\\\":\\\"PRIVATE\\\"},{\\\"name\\\":\\\"date\\\",\\\"returnType\\\":\\\"String\\\",\\\"modifier\\\":\\\"PRIVATE\\\"},{\\\"name\\\":\\\"copyright\\\",\\\"returnType\\\":\\\"String\\\",\\\"modifier\\\":\\\"PRIVATE\\\"},{\\\"name\\\":\\\"versionID\\\",\\\"returnType\\\":\\\"String\\\",\\\"modifier\\\":\\\"PRIVATE\\\"},{\\\"name\\\":\\\"contents\\\",\\\"returnType\\\":\\\"String\\\",\\\"modifier\\\":\\\"PRIVATE\\\"}],\\\"arcs\\\":[]}\"],\"parent\":{\"name\":\"src\",\"path\":\""
							 + relativePath + "\",\"vertexType\":\"PACKAGE_PRIVATE\"},\"neighbours\":[{\"name\":\"src.model.strategies\",\"path\":\"\\\""
							 + relativePath + "\\\\\\\\model\\\\\\\\strategies\",\"vertexType\":\"PACKAGE\",\"parentName\":\"src.model\"}],\"arcs\":[{\"source\":\"src.model\",\"target\":\"src.view\",\"arcType\":\"DEPENDENCY\"},{\"source\":\"src.model\",\"target\":\"src.model.strategies\",\"arcType\":\"DEPENDENCY\"}]},{\"name\":\"src.model.strategies\",\"path\":\""
							 + relativePath + "\\\\model\\\\strategies\",\"vertexType\":\"PACKAGE\",\"coordinate_x\":362.0,\"coordinate_y\":280.0,\"sinkVertices\":[\"{\\\"name\\\":\\\"VersionsStrategy\\\",\\\"path\\\":\\\""
							 + relativePath + "\\\\\\\\model\\\\\\\\strategies\\\\\\\\VersionsStrategy.java\\\",\\\"vertexType\\\":\\\"INTERFACE\\\",\\\"methods\\\":[{\\\"name\\\":\\\"removeVersion\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{}\\\"},{\\\"name\\\":\\\"setEntireHistory\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{\\\\\\\"documents\\\\\\\":\\\\\\\"List[Document]\\\\\\\"}\\\"},{\\\"name\\\":\\\"putVersion\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{\\\\\\\"document\\\\\\\":\\\\\\\"Document\\\\\\\"}\\\"},{\\\"name\\\":\\\"getEntireHistory\\\",\\\"returnType\\\":\\\"List[Document]\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{}\\\"},{\\\"name\\\":\\\"getVersion\\\",\\\"returnType\\\":\\\"Document\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{}\\\"}],\\\"fields\\\":[],\\\"arcs\\\":[{\\\"source\\\":\\\"VersionsStrategy\\\",\\\"target\\\":\\\"Document\\\",\\\"arcType\\\":\\\"DEPENDENCY\\\"}]}\",\"{\\\"name\\\":\\\"VolatileVersionsStrategy\\\",\\\"path\\\":\\\""
							 + relativePath + "\\\\\\\\model\\\\\\\\strategies\\\\\\\\VolatileVersionsStrategy.java\\\",\\\"vertexType\\\":\\\"CLASS\\\",\\\"methods\\\":[{\\\"name\\\":\\\"VolatileVersionsStrategy\\\",\\\"returnType\\\":\\\"Constructor\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{}\\\"},{\\\"name\\\":\\\"setEntireHistory\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{\\\\\\\"documents\\\\\\\":\\\\\\\"List[Document]\\\\\\\"}\\\"},{\\\"name\\\":\\\"removeVersion\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{}\\\"},{\\\"name\\\":\\\"putVersion\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{\\\\\\\"document\\\\\\\":\\\\\\\"Document\\\\\\\"}\\\"},{\\\"name\\\":\\\"getEntireHistory\\\",\\\"returnType\\\":\\\"List[Document]\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{}\\\"},{\\\"name\\\":\\\"getVersion\\\",\\\"returnType\\\":\\\"Document\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{}\\\"}],\\\"fields\\\":[{\\\"name\\\":\\\"history\\\",\\\"returnType\\\":\\\"ArrayList[Document]\\\",\\\"modifier\\\":\\\"PRIVATE\\\"}],\\\"arcs\\\":[{\\\"source\\\":\\\"VolatileVersionsStrategy\\\",\\\"target\\\":\\\"Document\\\",\\\"arcType\\\":\\\"DEPENDENCY\\\"},{\\\"source\\\":\\\"VolatileVersionsStrategy\\\",\\\"target\\\":\\\"Document\\\",\\\"arcType\\\":\\\"AGGREGATION\\\"},{\\\"source\\\":\\\"VolatileVersionsStrategy\\\",\\\"target\\\":\\\"VersionsStrategy\\\",\\\"arcType\\\":\\\"IMPLEMENTATION\\\"}]}\",\"{\\\"name\\\":\\\"VersionsStrategyFactory\\\",\\\"path\\\":\\\""
							 + relativePath + "\\\\\\\\model\\\\\\\\strategies\\\\\\\\VersionsStrategyFactory.java\\\",\\\"vertexType\\\":\\\"CLASS\\\",\\\"methods\\\":[{\\\"name\\\":\\\"VersionsStrategyFactory\\\",\\\"returnType\\\":\\\"Constructor\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{}\\\"},{\\\"name\\\":\\\"createStrategy\\\",\\\"returnType\\\":\\\"VersionsStrategy\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{\\\\\\\"type\\\\\\\":\\\\\\\"String\\\\\\\"}\\\"}],\\\"fields\\\":[{\\\"name\\\":\\\"strategies\\\",\\\"returnType\\\":\\\"HashMap[String,VersionsStrategy]\\\",\\\"modifier\\\":\\\"PRIVATE\\\"}],\\\"arcs\\\":[{\\\"source\\\":\\\"VersionsStrategyFactory\\\",\\\"target\\\":\\\"VersionsStrategy\\\",\\\"arcType\\\":\\\"DEPENDENCY\\\"},{\\\"source\\\":\\\"VersionsStrategyFactory\\\",\\\"target\\\":\\\"VersionsStrategy\\\",\\\"arcType\\\":\\\"AGGREGATION\\\"},{\\\"source\\\":\\\"VersionsStrategyFactory\\\",\\\"target\\\":\\\"VolatileVersionsStrategy\\\",\\\"arcType\\\":\\\"DEPENDENCY\\\"},{\\\"source\\\":\\\"VersionsStrategyFactory\\\",\\\"target\\\":\\\"StableVersionsStrategy\\\",\\\"arcType\\\":\\\"DEPENDENCY\\\"}]}\",\"{\\\"name\\\":\\\"StableVersionsStrategy\\\",\\\"path\\\":\\\""
							 + relativePath + "\\\\\\\\model\\\\\\\\strategies\\\\\\\\StableVersionsStrategy.java\\\",\\\"vertexType\\\":\\\"CLASS\\\",\\\"methods\\\":[{\\\"name\\\":\\\"removeVersion\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{}\\\"},{\\\"name\\\":\\\"setEntireHistory\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{\\\\\\\"documents\\\\\\\":\\\\\\\"List[Document]\\\\\\\"}\\\"},{\\\"name\\\":\\\"putVersion\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{\\\\\\\"document\\\\\\\":\\\\\\\"Document\\\\\\\"}\\\"},{\\\"name\\\":\\\"getEntireHistory\\\",\\\"returnType\\\":\\\"List[Document]\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{}\\\"},{\\\"name\\\":\\\"getVersion\\\",\\\"returnType\\\":\\\"Document\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{}\\\"}],\\\"fields\\\":[{\\\"name\\\":\\\"versionID\\\",\\\"returnType\\\":\\\"String\\\",\\\"modifier\\\":\\\"PRIVATE\\\"}],\\\"arcs\\\":[{\\\"source\\\":\\\"StableVersionsStrategy\\\",\\\"target\\\":\\\"Document\\\",\\\"arcType\\\":\\\"DEPENDENCY\\\"},{\\\"source\\\":\\\"StableVersionsStrategy\\\",\\\"target\\\":\\\"VersionsStrategy\\\",\\\"arcType\\\":\\\"IMPLEMENTATION\\\"}]}\"],\"parent\":{\"name\":\"src.model\",\"path\":\""
							 + relativePath + "\\\\\\\\model\",\"vertexType\":\"PACKAGE_PRIVATE\"},\"neighbours\":[],\"arcs\":[{\"source\":\"src.model.strategies\",\"target\":\"src.model\",\"arcType\":\"DEPENDENCY\"}]}]";

		expectedString2 = "[{\"name\":\"src.model\",\"path\":\"" +
						  relativePath + "model\",\"vertexType\":\"PACKAGE\",\"coordinate_x\":0.0,\"coordinate_y\":0.0,\"sinkVertices\":[\"{\\\"name\\\":\\\"VersionsManager\\\",\\\"path\\\":\\\"" +
						  relativePath + "/model/VersionsManager.java\\\",\\\"vertexType\\\":\\\"CLASS\\\",\\\"coordinate_x\\\":0.0,\\\"coordinate_y\\\":0.0," +
						  "\\\"methods\\\":[{\\\"name\\\":\\\"loadFromFile\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"PUBLIC\\\"," +
						  "\\\"parameters\\\":\\\"{}\\\"},{\\\"name\\\":\\\"enable\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"PUBLIC\\\"," +
						  "\\\"parameters\\\":\\\"{}\\\"},{\\\"name\\\":\\\"rollbackToPreviousVersion\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"PUBLIC\\\"," +
						  "\\\"parameters\\\":\\\"{}\\\"},{\\\"name\\\":\\\"disable\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"PUBLIC\\\"," +
						  "\\\"parameters\\\":\\\"{}\\\"},{\\\"name\\\":\\\"saveToFile\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"PUBLIC\\\"," +
						  "\\\"parameters\\\":\\\"{}\\\"},{\\\"name\\\":\\\"putVersion\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"PUBLIC\\\"," +
						  "\\\"parameters\\\":\\\"{\\\\\\\"document\\\\\\\":\\\\\\\"Document\\\\\\\"}\\\"},{\\\"name\\\":\\\"enableStrategy\\\"," +
						  "\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{}\\\"},{\\\"name\\\":\\\"changeStrategy\\\"," +
						  "\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{}\\\"},{\\\"name\\\":\\\"setStrategy\\\"," +
						  "\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{\\\\\\\"strategy\\\\\\\":\\\\\\\"VersionsStrategy\\\\\\\"}\\\"}," +
						  "{\\\"name\\\":\\\"setCurrentVersion\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"PUBLIC\\\"," +
						  "\\\"parameters\\\":\\\"{\\\\\\\"document\\\\\\\":\\\\\\\"Document\\\\\\\"}\\\"},{\\\"name\\\":\\\"getStrategy\\\"," +
						  "\\\"returnType\\\":\\\"VersionsStrategy\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{}\\\"}," +
						  "{\\\"name\\\":\\\"rollback\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{}\\\"}," +
						  "{\\\"name\\\":\\\"isEnabled\\\",\\\"returnType\\\":\\\"boolean\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{}\\\"}," +
						  "{\\\"name\\\":\\\"VersionsManager\\\",\\\"returnType\\\":\\\"Constructor\\\",\\\"modifier\\\":\\\"PUBLIC\\\"," +
						  "\\\"parameters\\\":\\\"{\\\\\\\"latexEditorView\\\\\\\":\\\\\\\"LatexEditorView\\\\\\\",\\\\\\\"versionsStrategy\\\\\\\":\\\\\\\"VersionsStrategy\\\\\\\"}\\\"}," +
						  "{\\\"name\\\":\\\"setPreviousVersion\\\",\\\"returnType\\\":\\\"Document\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{}\\\"}" +
						  ",{\\\"name\\\":\\\"getType\\\",\\\"returnType\\\":\\\"String\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{}\\\"}," +
						  "{\\\"name\\\":\\\"saveContents\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{}\\\"}]," +
						  "\\\"fields\\\":[{\\\"name\\\":\\\"enabled\\\",\\\"returnType\\\":\\\"boolean\\\",\\\"modifier\\\":\\\"PRIVATE\\\"}," +
						  "{\\\"name\\\":\\\"strategy\\\",\\\"returnType\\\":\\\"VersionsStrategy\\\",\\\"modifier\\\":\\\"PRIVATE\\\"}," +
						  "{\\\"name\\\":\\\"latexEditorView\\\",\\\"returnType\\\":\\\"LatexEditorView\\\",\\\"modifier\\\":\\\"PRIVATE\\\"}]," +
						  "\\\"arcs\\\":[{\\\"source\\\":\\\"VersionsManager\\\",\\\"target\\\":\\\"LatexEditorView\\\",\\\"arcType\\\":\\\"DEPENDENCY\\\"}," +
						  "{\\\"source\\\":\\\"VersionsManager\\\",\\\"target\\\":\\\"LatexEditorView\\\",\\\"arcType\\\":\\\"ASSOCIATION\\\"}," +
						  "{\\\"source\\\":\\\"VersionsManager\\\",\\\"target\\\":\\\"VersionsStrategy\\\",\\\"arcType\\\":\\\"DEPENDENCY\\\"}," +
						  "{\\\"source\\\":\\\"VersionsManager\\\",\\\"target\\\":\\\"VersionsStrategy\\\",\\\"arcType\\\":\\\"ASSOCIATION\\\"}," +
						  "{\\\"source\\\":\\\"VersionsManager\\\",\\\"target\\\":\\\"Document\\\",\\\"arcType\\\":\\\"DEPENDENCY\\\"}]}\"," +
						  "\"{\\\"name\\\":\\\"DocumentManager\\\",\\\"path\\\":\\\"" +
						  relativePath + "/model/DocumentManager.java\\\",\\\"vertexType\\\":\\\"CLASS\\\",\\\"coordinate_x\\\":0.0,\\\"coordinate_y\\\":0.0," +
						  "\\\"methods\\\":[{\\\"name\\\":\\\"createDocument\\\",\\\"returnType\\\":\\\"Document\\\",\\\"modifier\\\":\\\"PUBLIC\\\"," +
						  "\\\"parameters\\\":\\\"{\\\\\\\"type\\\\\\\":\\\\\\\"String\\\\\\\"}\\\"},{\\\"name\\\":\\\"getContents\\\",\\\"returnType\\\":\\\"String\\\"," +
						  "\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{\\\\\\\"type\\\\\\\":\\\\\\\"String\\\\\\\"}\\\"},{\\\"name\\\":\\\"DocumentManager\\\"," +
						  "\\\"returnType\\\":\\\"Constructor\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{}\\\"}],\\\"fields\\\":[{\\\"name\\\":\\\"templates\\\"," +
						  "\\\"returnType\\\":\\\"HashMap[String,Document]\\\",\\\"modifier\\\":\\\"PRIVATE\\\"}],\\\"arcs\\\":[{\\\"source\\\":\\\"DocumentManager\\\"," +
						  "\\\"target\\\":\\\"Document\\\",\\\"arcType\\\":\\\"DEPENDENCY\\\"},{\\\"source\\\":\\\"DocumentManager\\\",\\\"target\\\":\\\"Document\\\"," +
						  "\\\"arcType\\\":\\\"AGGREGATION\\\"}]}\",\"{\\\"name\\\":\\\"Document\\\",\\\"path\\\":\\\"" +
						  relativePath + "/model/Document.java\\\",\\\"vertexType\\\":\\\"CLASS\\\",\\\"coordinate_x\\\":0.0,\\\"coordinate_y\\\":0.0," +
						  "\\\"methods\\\":[{\\\"name\\\":\\\"Document\\\",\\\"returnType\\\":\\\"Constructor\\\",\\\"modifier\\\":\\\"PUBLIC\\\"," +
						  "\\\"parameters\\\":\\\"{}\\\"},{\\\"name\\\":\\\"Document\\\",\\\"returnType\\\":\\\"Constructor\\\",\\\"modifier\\\":\\\"PUBLIC\\\"," +
						  "\\\"parameters\\\":\\\"{\\\\\\\"date\\\\\\\":\\\\\\\"String\\\\\\\",\\\\\\\"copyright\\\\\\\":\\\\\\\"String\\\\\\\",\\\\\\\"versionID\\\\\\\":\\\\\\\"String\\\\\\\"," +
						  "\\\\\\\"contents\\\\\\\":\\\\\\\"String\\\\\\\",\\\\\\\"author\\\\\\\":\\\\\\\"String\\\\\\\"}\\\"}," +
						  "{\\\"name\\\":\\\"clone\\\",\\\"returnType\\\":\\\"Document\\\",\\\"modifier\\\":\\\"PUBLIC\\\"," +
						  "\\\"parameters\\\":\\\"{}\\\"},{\\\"name\\\":\\\"save\\\",\\\"returnType\\\":\\\"void\\\"," +
						  "\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{\\\\\\\"filename\\\\\\\":\\\\\\\"String\\\\\\\"}\\\"}," +
						  "{\\\"name\\\":\\\"setContents\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"PUBLIC\\\"," +
						  "\\\"parameters\\\":\\\"{\\\\\\\"contents\\\\\\\":\\\\\\\"String\\\\\\\"}\\\"},{\\\"name\\\":\\\"changeVersion\\\"," +
						  "\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{}\\\"},{\\\"name\\\":\\\"getVersionID\\\"," +
						  "\\\"returnType\\\":\\\"String\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{}\\\"},{\\\"name\\\":\\\"getContents\\\"," +
						  "\\\"returnType\\\":\\\"String\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{}\\\"}],\\\"fields\\\":[{\\\"name\\\":\\\"author\\\"," +
						  "\\\"returnType\\\":\\\"String\\\",\\\"modifier\\\":\\\"PRIVATE\\\"},{\\\"name\\\":\\\"date\\\",\\\"returnType\\\":\\\"String\\\",\\\"modifier\\\":\\\"PRIVATE\\\"}," +
						  "{\\\"name\\\":\\\"copyright\\\",\\\"returnType\\\":\\\"String\\\",\\\"modifier\\\":\\\"PRIVATE\\\"},{\\\"name\\\":\\\"versionID\\\"," +
						  "\\\"returnType\\\":\\\"String\\\",\\\"modifier\\\":\\\"PRIVATE\\\"},{\\\"name\\\":\\\"contents\\\",\\\"returnType\\\":\\\"String\\\"," +
						  "\\\"modifier\\\":\\\"PRIVATE\\\"}],\\\"arcs\\\":[]}\"],\"parent\":{\"name\":\"src\",\"path\":\"" +
						  relativePath + "\",\"vertexType\":\"PACKAGE_PRIVATE\"},\"neighbours\":[{\"name\":\"src.model.strategies\",\"path\":\"" +
						  relativePath + "/model/strategies\",\"vertexType\":\"PACKAGE\",\"parentName\":\"src.model\"}],\"arcs\":[{\"source\":\"src.model\"," +
						  "\"target\":\"src.view\",\"arcType\":\"DEPENDENCY\"},{\"source\":\"src.model\",\"target\":\"src.model.strategies\",\"arcType\":\"DEPENDENCY\"}]},{\"name\":\"src.view\",\"path\":\"" +
						  relativePath + "/view\",\"vertexType\":\"PACKAGE\",\"coordinate_x\":0.0,\"coordinate_y\":0.0,\"sinkVertices\":[\"{\\\"name\\\":\\\"LatexEditorView\\\",\\\"path\\\":\\\"" +
						  relativePath + "/view/LatexEditorView.java\\\",\\\"vertexType\\\":\\\"CLASS\\\",\\\"coordinate_x\\\":0.0,\\\"coordinate_y\\\":0.0,\\\"methods\\\":[{\\\"name\\\":\\\"getText\\\"," +
						  "\\\"returnType\\\":\\\"String\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{}\\\"},{\\\"name\\\":\\\"setType\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"PUBLIC\\\"," +
						  "\\\"parameters\\\":\\\"{\\\\\\\"type\\\\\\\":\\\\\\\"String\\\\\\\"}\\\"},{\\\"name\\\":\\\"getVersionsManager\\\",\\\"returnType\\\":\\\"VersionsManager\\\",\\\"modifier\\\":\\\"PUBLIC\\\"," +
						  "\\\"parameters\\\":\\\"{}\\\"},{\\\"name\\\":\\\"getCurrentDocument\\\",\\\"returnType\\\":\\\"Document\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{}\\\"},{\\\"name\\\":\\\"saveContents\\\"," +
						  "\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{}\\\"},{\\\"name\\\":\\\"setCurrentDocument\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"PUBLIC\\\"," +
						  "\\\"parameters\\\":\\\"{\\\\\\\"currentDocument\\\\\\\":\\\\\\\"Document\\\\\\\"}\\\"},{\\\"name\\\":\\\"setText\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"PUBLIC\\\"," +
						  "\\\"parameters\\\":\\\"{\\\\\\\"text\\\\\\\":\\\\\\\"String\\\\\\\"}\\\"},{\\\"name\\\":\\\"saveToFile\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"PUBLIC\\\"," +
						  "\\\"parameters\\\":\\\"{}\\\"},{\\\"name\\\":\\\"setFilename\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"PUBLIC\\\"," +
						  "\\\"parameters\\\":\\\"{\\\\\\\"filename\\\\\\\":\\\\\\\"String\\\\\\\"}\\\"},{\\\"name\\\":\\\"loadFromFile\\\",\\\"returnType\\\":\\\"void\\\"," +
						  "\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{}\\\"},{\\\"name\\\":\\\"setStrategy\\\",\\\"returnType\\\":\\\"void\\\"," +
						  "\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{\\\\\\\"strategy\\\\\\\":\\\\\\\"String\\\\\\\"}\\\"},{\\\"name\\\":\\\"getStrategy\\\"," +
						  "\\\"returnType\\\":\\\"String\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{}\\\"},{\\\"name\\\":\\\"getFilename\\\"," +
						  "\\\"returnType\\\":\\\"String\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{}\\\"},{\\\"name\\\":\\\"setVersionsManager\\\"," +
						  "\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{\\\\\\\"versionsManager\\\\\\\":\\\\\\\"VersionsManager\\\\\\\"}\\\"}," +
						  "{\\\"name\\\":\\\"setController\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{\\\\\\\"controller\\\\\\\":\\\\\\\"LatexEditorController\\\\\\\"}\\\"}," +
						  "{\\\"name\\\":\\\"getType\\\",\\\"returnType\\\":\\\"String\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{}\\\"},{\\\"name\\\":\\\"getController\\\",\\\"returnType\\\":\\\"LatexEditorController\\\"," +
						  "\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{}\\\"}],\\\"fields\\\":[{\\\"name\\\":\\\"controller\\\",\\\"returnType\\\":\\\"LatexEditorController\\\"," +
						  "\\\"modifier\\\":\\\"PRIVATE\\\"},{\\\"name\\\":\\\"currentDocument\\\",\\\"returnType\\\":\\\"Document\\\",\\\"modifier\\\":\\\"PRIVATE\\\"}," +
						  "{\\\"name\\\":\\\"type\\\",\\\"returnType\\\":\\\"String\\\",\\\"modifier\\\":\\\"PRIVATE\\\"},{\\\"name\\\":\\\"text\\\",\\\"returnType\\\":\\\"String\\\",\\\"modifier\\\":\\\"PRIVATE\\\"}," +
						  "{\\\"name\\\":\\\"filename\\\",\\\"returnType\\\":\\\"String\\\",\\\"modifier\\\":\\\"PRIVATE\\\"},{\\\"name\\\":\\\"strategy\\\",\\\"returnType\\\":\\\"String\\\"," +
						  "\\\"modifier\\\":\\\"PRIVATE\\\"},{\\\"name\\\":\\\"versionsManager\\\",\\\"returnType\\\":\\\"VersionsManager\\\"," +
						  "\\\"modifier\\\":\\\"PRIVATE\\\"}],\\\"arcs\\\":[{\\\"source\\\":\\\"LatexEditorView\\\",\\\"target\\\":\\\"VersionsManager\\\"," +
						  "\\\"arcType\\\":\\\"DEPENDENCY\\\"},{\\\"source\\\":\\\"LatexEditorView\\\",\\\"target\\\":\\\"VersionsManager\\\",\\\"arcType\\\":\\\"ASSOCIATION\\\"}," +
						  "{\\\"source\\\":\\\"LatexEditorView\\\",\\\"target\\\":\\\"Document\\\",\\\"arcType\\\":\\\"DEPENDENCY\\\"},{\\\"source\\\":\\\"LatexEditorView\\\",\\\"target\\\":\\\"Document\\\"," +
						  "\\\"arcType\\\":\\\"ASSOCIATION\\\"},{\\\"source\\\":\\\"LatexEditorView\\\",\\\"target\\\":\\\"LatexEditorController\\\",\\\"arcType\\\":\\\"DEPENDENCY\\\"},{\\\"source\\\":\\\"LatexEditorView\\\"," +
						  "\\\"target\\\":\\\"LatexEditorController\\\",\\\"arcType\\\":\\\"ASSOCIATION\\\"}]}\",\"{\\\"name\\\":\\\"MainWindow\\\",\\\"path\\\":\\\"" +
						  relativePath + "/view/MainWindow.java\\\",\\\"vertexType\\\":\\\"CLASS\\\",\\\"coordinate_x\\\":0.0,\\\"coordinate_y\\\":0.0," +
						  "\\\"methods\\\":[{\\\"name\\\":\\\"MainWindow\\\",\\\"returnType\\\":\\\"Constructor\\\",\\\"modifier\\\":\\\"PUBLIC\\\"," +
						  "\\\"parameters\\\":\\\"{\\\\\\\"latexEditorView\\\\\\\":\\\\\\\"LatexEditorView\\\\\\\"}\\\"},{\\\"name\\\":\\\"actionPerformed\\\"," +
						  "\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{\\\\\\\"e\\\\\\\":\\\\\\\"ActionEvent\\\\\\\"}\\\"}," +
						  "{\\\"name\\\":\\\"actionPerformed\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{\\\\\\\"e\\\\\\\":\\\\\\\"ActionEvent\\\\\\\"}\\\"}," +
						  "{\\\"name\\\":\\\"actionPerformed\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{\\\\\\\"e\\\\\\\":\\\\\\\"ActionEvent\\\\\\\"}\\\"}," +
						  "{\\\"name\\\":\\\"actionPerformed\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{\\\\\\\"e\\\\\\\":\\\\\\\"ActionEvent\\\\\\\"}\\\"}," +
						  "{\\\"name\\\":\\\"initialize\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"PRIVATE\\\",\\\"parameters\\\":\\\"{}\\\"},{\\\"name\\\":\\\"actionPerformed\\\"," +
						  "\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{\\\\\\\"arg0\\\\\\\":\\\\\\\"ActionEvent\\\\\\\"}\\\"},{\\\"name\\\":\\\"editContents\\\"," +
						  "\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{\\\\\\\"type\\\\\\\":\\\\\\\"String\\\\\\\"}\\\"},{\\\"name\\\":\\\"actionPerformed\\\"," +
						  "\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{\\\\\\\"e\\\\\\\":\\\\\\\"ActionEvent\\\\\\\"}\\\"},{\\\"name\\\":\\\"actionPerformed\\\"," +
						  "\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{\\\\\\\"e\\\\\\\":\\\\\\\"ActionEvent\\\\\\\"}\\\"},{\\\"name\\\":\\\"actionPerformed\\\"," +
						  "\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{\\\\\\\"arg0\\\\\\\":\\\\\\\"ActionEvent\\\\\\\"}\\\"},{\\\"name\\\":\\\"actionPerformed\\\"," +
						  "\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{\\\\\\\"e\\\\\\\":\\\\\\\"ActionEvent\\\\\\\"}\\\"},{\\\"name\\\":\\\"actionPerformed\\\"," +
						  "\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{\\\\\\\"e\\\\\\\":\\\\\\\"ActionEvent\\\\\\\"}\\\"},{\\\"name\\\":\\\"actionPerformed\\\"," +
						  "\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{\\\\\\\"e\\\\\\\":\\\\\\\"ActionEvent\\\\\\\"}\\\"},{\\\"name\\\":\\\"actionPerformed\\\"," +
						  "\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{\\\\\\\"e\\\\\\\":\\\\\\\"ActionEvent\\\\\\\"}\\\"},{\\\"name\\\":\\\"actionPerformed\\\"," +
						  "\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{\\\\\\\"arg0\\\\\\\":\\\\\\\"ActionEvent\\\\\\\"}\\\"},{\\\"name\\\":\\\"actionPerformed\\\"," +
						  "\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{\\\\\\\"e\\\\\\\":\\\\\\\"ActionEvent\\\\\\\"}\\\"},{\\\"name\\\":\\\"actionPerformed\\\"," +
						  "\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{\\\\\\\"arg0\\\\\\\":\\\\\\\"ActionEvent\\\\\\\"}\\\"},{\\\"name\\\":\\\"actionPerformed\\\"," +
						  "\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{\\\\\\\"e\\\\\\\":\\\\\\\"ActionEvent\\\\\\\"}\\\"}],\\\"fields\\\":[{\\\"name\\\":\\\"frame\\\"," +
						  "\\\"returnType\\\":\\\"JFrame\\\",\\\"modifier\\\":\\\"PRIVATE\\\"},{\\\"name\\\":\\\"editorPane\\\",\\\"returnType\\\":\\\"JEditorPane\\\",\\\"modifier\\\":\\\"PRIVATE\\\"}," +
						  "{\\\"name\\\":\\\"latexEditorView\\\",\\\"returnType\\\":\\\"LatexEditorView\\\",\\\"modifier\\\":\\\"PRIVATE\\\"}],\\\"arcs\\\":[{\\\"source\\\":\\\"MainWindow\\\",\\\"target\\\":\\\"LatexEditorView\\\"," +
						  "\\\"arcType\\\":\\\"DEPENDENCY\\\"},{\\\"source\\\":\\\"MainWindow\\\",\\\"target\\\":\\\"LatexEditorView\\\",\\\"arcType\\\":\\\"ASSOCIATION\\\"},{\\\"source\\\":\\\"MainWindow\\\",\\\"target\\\":\\\"ChooseTemplate\\\"," +
						  "\\\"arcType\\\":\\\"DEPENDENCY\\\"}]}\",\"{\\\"name\\\":\\\"ChooseTemplate\\\",\\\"path\\\":\\\"" +
						  relativePath + "/view/ChooseTemplate.java\\\",\\\"vertexType\\\":\\\"CLASS\\\",\\\"coordinate_x\\\":0.0,\\\"coordinate_y\\\":0.0,\\\"methods\\\":[{\\\"name\\\":\\\"actionPerformed\\\"," +
						  "\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{\\\\\\\"e\\\\\\\":\\\\\\\"ActionEvent\\\\\\\"}\\\"},{\\\"name\\\":\\\"actionPerformed\\\"," +
						  "\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{\\\\\\\"e\\\\\\\":\\\\\\\"ActionEvent\\\\\\\"}\\\"},{\\\"name\\\":\\\"actionPerformed\\\"," +
						  "\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{\\\\\\\"e\\\\\\\":\\\\\\\"ActionEvent\\\\\\\"}\\\"},{\\\"name\\\":\\\"initialize\\\"," +
						  "\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"PRIVATE\\\",\\\"parameters\\\":\\\"{}\\\"},{\\\"name\\\":\\\"ChooseTemplate\\\",\\\"returnType\\\":\\\"Constructor\\\"," +
						  "\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{\\\\\\\"previous\\\\\\\":\\\\\\\"String\\\\\\\",\\\\\\\"latexEditorView\\\\\\\":\\\\\\\"LatexEditorView\\\\\\\"}\\\"}," +
						  "{\\\"name\\\":\\\"actionPerformed\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{\\\\\\\"e\\\\\\\":\\\\\\\"ActionEvent\\\\\\\"}\\\"}," +
						  "{\\\"name\\\":\\\"actionPerformed\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{\\\\\\\"arg0\\\\\\\":\\\\\\\"ActionEvent\\\\\\\"}\\\"},{\\\"name\\\":\\\"diselectRadioButtons\\\"," +
						  "\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"PRIVATE\\\",\\\"parameters\\\":\\\"{\\\\\\\"radioButton3\\\\\\\":\\\\\\\"JRadioButton\\\\\\\",\\\\\\\"radioButton4\\\\\\\":\\\\\\\"JRadioButton\\\\\\\"," +
						  "\\\\\\\"radioButton1\\\\\\\":\\\\\\\"JRadioButton\\\\\\\",\\\\\\\"radioButton2\\\\\\\":\\\\\\\"JRadioButton\\\\\\\"}\\\"}," +
						  "{\\\"name\\\":\\\"actionPerformed\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"PUBLIC\\\"," +
						  "\\\"parameters\\\":\\\"{\\\\\\\"e\\\\\\\":\\\\\\\"ActionEvent\\\\\\\"}\\\"}],\\\"fields\\\":[{\\\"name\\\":\\\"frame\\\"," +
						  "\\\"returnType\\\":\\\"JFrame\\\",\\\"modifier\\\":\\\"PRIVATE\\\"},{\\\"name\\\":\\\"latexEditorView\\\"," +
						  "\\\"returnType\\\":\\\"LatexEditorView\\\",\\\"modifier\\\":\\\"PRIVATE\\\"},{\\\"name\\\":\\\"previous\\\"," +
						  "\\\"returnType\\\":\\\"String\\\",\\\"modifier\\\":\\\"PRIVATE\\\"}],\\\"arcs\\\":[{\\\"source\\\":\\\"ChooseTemplate\\\"," +
						  "\\\"target\\\":\\\"LatexEditorView\\\",\\\"arcType\\\":\\\"DEPENDENCY\\\"},{\\\"source\\\":\\\"ChooseTemplate\\\",\\\"target\\\":\\\"LatexEditorView\\\"," +
						  "\\\"arcType\\\":\\\"ASSOCIATION\\\"},{\\\"source\\\":\\\"ChooseTemplate\\\",\\\"target\\\":\\\"MainWindow\\\",\\\"arcType\\\":\\\"DEPENDENCY\\\"}," +
						  "{\\\"source\\\":\\\"ChooseTemplate\\\",\\\"target\\\":\\\"OpeningWindow\\\",\\\"arcType\\\":\\\"DEPENDENCY\\\"}]}\",\"{\\\"name\\\":\\\"OpeningWindow\\\"," +
						  "\\\"path\\\":\\\"/home/dimitrios/IdeaProjects/GitHub/ObjectOrientedArchitectureDiagrammer/src/test/resources/LatexEditor/src/view/OpeningWindow.java\\\"," +
						  "\\\"vertexType\\\":\\\"CLASS\\\",\\\"coordinate_x\\\":0.0,\\\"coordinate_y\\\":0.0,\\\"methods\\\":[{\\\"name\\\":\\\"actionPerformed\\\"," +
						  "\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{\\\\\\\"e\\\\\\\":\\\\\\\"ActionEvent\\\\\\\"}\\\"}," +
						  "{\\\"name\\\":\\\"OpeningWindow\\\",\\\"returnType\\\":\\\"Constructor\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{}\\\"}," +
						  "{\\\"name\\\":\\\"actionPerformed\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{\\\\\\\"e\\\\\\\":\\\\\\\"ActionEvent\\\\\\\"}\\\"}," +
						  "{\\\"name\\\":\\\"initialize\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"PRIVATE\\\",\\\"parameters\\\":\\\"{}\\\"}," +
						  "{\\\"name\\\":\\\"run\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{}\\\"},{\\\"name\\\":\\\"main\\\",\\\"returnType\\\":\\\"void\\\"," +
						  "\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{\\\\\\\"args\\\\\\\":\\\\\\\"String[]\\\\\\\"}\\\"},{\\\"name\\\":\\\"actionPerformed\\\",\\\"returnType\\\":\\\"void\\\"," +
						  "\\\"modifier\\\":\\\"PUBLIC\\\",\\\"parameters\\\":\\\"{\\\\\\\"e\\\\\\\":\\\\\\\"ActionEvent\\\\\\\"}\\\"}],\\\"fields\\\":[{\\\"name\\\":\\\"frame\\\",\\\"returnType\\\":\\\"JFrame\\\"," +
						  "\\\"modifier\\\":\\\"PRIVATE\\\"},{\\\"name\\\":\\\"latexEditorView\\\",\\\"returnType\\\":\\\"LatexEditorView\\\",\\\"modifier\\\":\\\"PRIVATE\\\"}],\\\"arcs\\\":[{\\\"source\\\":\\\"OpeningWindow\\\"," +
						  "\\\"target\\\":\\\"LatexEditorView\\\",\\\"arcType\\\":\\\"ASSOCIATION\\\"},{\\\"source\\\":\\\"OpeningWindow\\\",\\\"target\\\":\\\"ChooseTemplate\\\",\\\"arcType\\\":\\\"DEPENDENCY\\\"},{\\\"source\\\":\\\"OpeningWindow\\\"," +
						  "\\\"target\\\":\\\"VersionsStrategy\\\",\\\"arcType\\\":\\\"DEPENDENCY\\\"},{\\\"source\\\":\\\"OpeningWindow\\\",\\\"target\\\":\\\"VolatileVersionsStrategy\\\",\\\"arcType\\\":\\\"DEPENDENCY\\\"},{\\\"source\\\":\\\"OpeningWindow\\\"," +
						  "\\\"target\\\":\\\"VersionsManager\\\",\\\"arcType\\\":\\\"DEPENDENCY\\\"},{\\\"source\\\":\\\"OpeningWindow\\\",\\\"target\\\":\\\"LatexEditorController\\\",\\\"arcType\\\":\\\"DEPENDENCY\\\"}]}\"],\"parent\":{\"name\":\"src\",\"path\":\"" +
						  relativePath + "\",\"vertexType\":\"PACKAGE_PRIVATE\"},\"neighbours\":[],\"arcs\":[{\"source\":\"src.view\"," +
						  "\"target\":\"src.model\",\"arcType\":\"DEPENDENCY\"},{\"source\":\"src.view\",\"target\":\"src.controller\"," +
						  "\"arcType\":\"DEPENDENCY\"},{\"source\":\"src.view\",\"target\":\"src.model.strategies\",\"arcType\":\"DEPENDENCY\"}]}]";
	}

	private JsonArray getJsonArray() {
		if (os.equals("Linux")) {
			return JsonParser.parseString(expectedString2).getAsJsonArray();
		}else {
			return JsonParser.parseString(expectedJsonString).getAsJsonArray();
		}
	}


	private List<String> getPackages() {
		if (os.equals("Linux")){
			return List.of("src.view", "src.model");
		}else {
			return
				List.of(
					"src.view",
					"src.model",
					"src.model.strategies",
					"src.controller.commands",
					"src.controller"
				);
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

		public static Set<JsonElement> setOfElements2(JsonArray arr) {
			Set<JsonElement> set = new HashSet<>();
			for (JsonElement j : arr) {
				String path = j.getAsJsonObject().get("path").toString();
				if (!os.equals("Linux") && !path.isEmpty()) {
					j.getAsJsonObject().addProperty("path", path.substring(0, path.length()-1));
				}
				set.add(j);
			}
			return set;
		}


	}
}