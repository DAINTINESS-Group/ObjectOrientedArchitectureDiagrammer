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
import utils.PathTemplate.LatexEditor;

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

public class JavaFXPackageDiagramExporterTest
{

    private static String expectedJsonString;
    private static String expectedString2;
    private static String relativePath;
    private static String os;


    // TODO Decouple this
    @Test
    void exportDiagramTest()
    {
        try
        {
            PackageDiagramManager packageDiagramManager = new PackageDiagramManager();
            packageDiagramManager.createSourceProject(LatexEditor.SRC.path);
            packageDiagramManager.convertTreeToDiagram(getPackages());

            DiagramExporter javaFXExporter = new JavaFXPackageDiagramExporter(packageDiagramManager.getPackageDiagram());
            File actualFile = javaFXExporter.exportDiagram(Path.of(String.format("%s%s%s",
                                                                                 PathConstructor.getCurrentPath(),
                                                                                 File.separator,
                                                                                 PathConstructor.constructPath("src",
                                                                                                               "test",
                                                                                                               "resources",
                                                                                                               "testingExportedFile.txt"))));

            JsonArray expectedJsonArray = getJsonArray();
            JsonArray actualJsonArray   = JsonParser.parseString(Files.readAllLines(actualFile.toPath()).get(0)).getAsJsonArray();
            assertEquals(expectedJsonArray.size(), actualJsonArray.size());
            for (JsonElement element : expectedJsonArray)
            {
                for (JsonElement actualElement : actualJsonArray)
                {
                    if (!actualElement.getAsJsonObject().get("name").equals(element.getAsJsonObject().get("name")))
                    {
                        continue;
                    }
                    assertEquals(element.getAsJsonObject().size(), actualElement.getAsJsonObject().size());

                    JsonArray        expectedArcs           = JsonParser.parseString(element.getAsJsonObject().get("arcs").toString()).getAsJsonArray();
                    JsonArray        actualArcs             = JsonParser.parseString(actualElement.getAsJsonObject().get("arcs").toString()).getAsJsonArray();
                    Set<JsonElement> expectedJsonElementSet = CompareArray.setOfElements(expectedArcs);
                    Set<JsonElement> actualJsonElementSet   = CompareArray.setOfElements(actualArcs);
                    assertEquals(expectedJsonElementSet, actualJsonElementSet);

                    JsonArray              expJson         = JsonParser.parseString(element.getAsJsonObject().get("sinkVertices").toString()).getAsJsonArray();
                    JsonArray              actJson         = JsonParser.parseString(actualElement.getAsJsonObject().get("sinkVertices").toString()).getAsJsonArray();
                    List<ClassifierVertex> expSinkVertices = new ArrayList<>();
                    List<ClassifierVertex> actSinkVertices = new ArrayList<>();
                    for (JsonElement jsonElement : expJson)
                    {
                        Gson gson = new GsonBuilder().registerTypeAdapter(ClassifierVertex.class, new ClassifierVertexDeserializer()).create();
                        expSinkVertices.add(gson.fromJson(jsonElement.getAsString(), ClassifierVertex.class));
                    }
                    for (JsonElement jsonElement : actJson)
                    {
                        Gson gson = new GsonBuilder().registerTypeAdapter(ClassifierVertex.class, new ClassifierVertexDeserializer()).create();
                        actSinkVertices.add(gson.fromJson(jsonElement.getAsString(), ClassifierVertex.class));
                    }
                    for (ClassifierVertex classifierVertex : expSinkVertices)
                    {
                        Optional<ClassifierVertex> optionalSinkVertex = actSinkVertices.stream()
                            .filter(it -> it.getName().equals(classifierVertex.getName())                &&
                                          it.getVertexType().equals(classifierVertex.getVertexType())    &&
                                          it.getArcs().size() == classifierVertex.getArcs().size()       &&
                                          it.getMethods().size() == classifierVertex.getMethods().size() &&
                                          it.getFields().size() == classifierVertex.getFields().size())
                            .findFirst();
                        assertTrue(optionalSinkVertex.isPresent());

                        List<ClassifierVertex.Field> fields = optionalSinkVertex.get().getFields();
                        for (ClassifierVertex.Field field : classifierVertex.getFields())
                        {
                            assertTrue(fields.stream()
                                           .anyMatch(it -> it.name().equals(field.name()) &&
                                                           it.type().equals(field.type()) &&
                                                           it.modifier().equals(field.modifier())));
                        }

                        List<ClassifierVertex.Method> methods = optionalSinkVertex.get().getMethods();
                        for (ClassifierVertex.Method method : classifierVertex.getMethods())
                        {
                            assertTrue(methods.stream()
                                           .anyMatch(it -> it.name().equals(method.name())             &&
                                                           it.returnType().equals(method.returnType()) &&
                                                           it.parameters().equals(method.parameters())));
                        }

                        List<Arc<ClassifierVertex>> arcs = optionalSinkVertex.get().getArcs();
                        for (Arc<ClassifierVertex> arc : classifierVertex.getArcs())
                        {
                            assertTrue(arcs.stream()
                                           .anyMatch(sinkVertexArc ->
                                                         sinkVertexArc.sourceVertex().getName().equals(arc.sourceVertex().getName()) &&
                                                         sinkVertexArc.targetVertex().getName().equals(arc.targetVertex().getName()) &&
                                                         sinkVertexArc.arcType().equals(arc.arcType())));
                        }
                    }

                    JsonArray expectedNeighbours = JsonParser.parseString(element.getAsJsonObject().get("neighbours").toString()).getAsJsonArray();
                    JsonArray actualNeighbours   = JsonParser.parseString(actualElement.getAsJsonObject().get("neighbours").toString()).getAsJsonArray();
                    expectedJsonElementSet = CompareArray.setOfElements(expectedNeighbours);
                    actualJsonElementSet = CompareArray.setOfElements2(actualNeighbours);
                    assertEquals(expectedJsonElementSet, actualJsonElementSet);

                    JsonObject expectedParent = JsonParser.parseString(element.getAsJsonObject().get("parent").toString()).getAsJsonObject();
                    JsonObject actualParent   = JsonParser.parseString(actualElement.getAsJsonObject().get("parent").toString()).getAsJsonObject();
                    assertEquals(expectedParent.get("name"), actualParent.get("name"));
                    assertEquals(expectedParent.get("vertexType"), actualParent.get("vertexType"));
                    assertEquals(expectedParent.get("path").toString(), actualParent.get("path").toString().replaceAll("\\\\", "\\\\\\\\"));
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    @BeforeAll
    public static void setUp()
    {
        relativePath = Paths.get(PathConstructor.getCurrentPath() + File.separator + PathConstructor.constructPath("src", "test", "resources", "LatexEditor", "src")).toString();

        os = System.getProperty("os.name");

        expectedJsonString = "[{\"name\":\"src.view\",\"path\":\""
                             +
                             relativePath +
                             "\\\\view\",\"vertexType\":\"package\",\"coordinate_x\":362.0,\"coordinate_y\":25.0,\"sinkVertices\":[\"{\\\"name\\\":\\\"LatexEditorView\\\",\\\"path\\\":\\\""
                             +
                             relativePath +
                             "\\\\\\\\view\\\\\\\\LatexEditorView.java\\\",\\\"vertexType\\\":\\\"class\\\",\\\"methods\\\":[{\\\"name\\\":\\\"getStrategy\\\",\\\"returnType\\\":\\\"String\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{}\\\"},{\\\"name\\\":\\\"getController\\\",\\\"returnType\\\":\\\"LatexEditorController\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{}\\\"},{\\\"name\\\":\\\"getFilename\\\",\\\"returnType\\\":\\\"String\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{}\\\"},{\\\"name\\\":\\\"setStrategy\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{\\\\\\\"strategy\\\\\\\":\\\\\\\"String\\\\\\\"}\\\"},{\\\"name\\\":\\\"getVersionsManager\\\",\\\"returnType\\\":\\\"VersionsManager\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{}\\\"},{\\\"name\\\":\\\"setController\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{\\\\\\\"controller\\\\\\\":\\\\\\\"LatexEditorController\\\\\\\"}\\\"},{\\\"name\\\":\\\"getType\\\",\\\"returnType\\\":\\\"String\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{}\\\"},{\\\"name\\\":\\\"setType\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{\\\\\\\"type\\\\\\\":\\\\\\\"String\\\\\\\"}\\\"},{\\\"name\\\":\\\"setCurrentDocument\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{\\\\\\\"currentDocument\\\\\\\":\\\\\\\"Document\\\\\\\"}\\\"},{\\\"name\\\":\\\"setFilename\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{\\\\\\\"filename\\\\\\\":\\\\\\\"String\\\\\\\"}\\\"},{\\\"name\\\":\\\"getText\\\",\\\"returnType\\\":\\\"String\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{}\\\"},{\\\"name\\\":\\\"setVersionsManager\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{\\\\\\\"versionsManager\\\\\\\":\\\\\\\"VersionsManager\\\\\\\"}\\\"},{\\\"name\\\":\\\"setText\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{\\\\\\\"text\\\\\\\":\\\\\\\"String\\\\\\\"}\\\"},{\\\"name\\\":\\\"getCurrentDocument\\\",\\\"returnType\\\":\\\"Document\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{}\\\"},{\\\"name\\\":\\\"saveToFile\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{}\\\"},{\\\"name\\\":\\\"loadFromFile\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{}\\\"},{\\\"name\\\":\\\"saveContents\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{}\\\"}],\\\"fields\\\":[{\\\"name\\\":\\\"controller\\\",\\\"returnType\\\":\\\"LatexEditorController\\\",\\\"modifier\\\":\\\"private\\\"},{\\\"name\\\":\\\"currentDocument\\\",\\\"returnType\\\":\\\"Document\\\",\\\"modifier\\\":\\\"private\\\"},{\\\"name\\\":\\\"type\\\",\\\"returnType\\\":\\\"String\\\",\\\"modifier\\\":\\\"private\\\"},{\\\"name\\\":\\\"text\\\",\\\"returnType\\\":\\\"String\\\",\\\"modifier\\\":\\\"private\\\"},{\\\"name\\\":\\\"filename\\\",\\\"returnType\\\":\\\"String\\\",\\\"modifier\\\":\\\"private\\\"},{\\\"name\\\":\\\"strategy\\\",\\\"returnType\\\":\\\"String\\\",\\\"modifier\\\":\\\"private\\\"},{\\\"name\\\":\\\"versionsManager\\\",\\\"returnType\\\":\\\"VersionsManager\\\",\\\"modifier\\\":\\\"private\\\"}],\\\"arcs\\\":[{\\\"source\\\":\\\"LatexEditorView\\\",\\\"target\\\":\\\"LatexEditorController\\\",\\\"arcType\\\":\\\"dependency\\\"},{\\\"source\\\":\\\"LatexEditorView\\\",\\\"target\\\":\\\"LatexEditorController\\\",\\\"arcType\\\":\\\"association\\\"},{\\\"source\\\":\\\"LatexEditorView\\\",\\\"target\\\":\\\"VersionsManager\\\",\\\"arcType\\\":\\\"dependency\\\"},{\\\"source\\\":\\\"LatexEditorView\\\",\\\"target\\\":\\\"VersionsManager\\\",\\\"arcType\\\":\\\"association\\\"},{\\\"source\\\":\\\"LatexEditorView\\\",\\\"target\\\":\\\"Document\\\",\\\"arcType\\\":\\\"dependency\\\"},{\\\"source\\\":\\\"LatexEditorView\\\",\\\"target\\\":\\\"Document\\\",\\\"arcType\\\":\\\"association\\\"}]}\",\"{\\\"name\\\":\\\"MainWindow\\\",\\\"path\\\":\\\""
                             +
                             relativePath +
                             "\\\\\\\\view\\\\\\\\MainWindow.java\\\",\\\"vertexType\\\":\\\"class\\\",\\\"methods\\\":[{\\\"name\\\":\\\"actionPerformed\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{\\\\\\\"e\\\\\\\":\\\\\\\"ActionEvent\\\\\\\"}\\\"},{\\\"name\\\":\\\"actionPerformed\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{\\\\\\\"e\\\\\\\":\\\\\\\"ActionEvent\\\\\\\"}\\\"},{\\\"name\\\":\\\"actionPerformed\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{\\\\\\\"e\\\\\\\":\\\\\\\"ActionEvent\\\\\\\"}\\\"},{\\\"name\\\":\\\"actionPerformed\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{\\\\\\\"e\\\\\\\":\\\\\\\"ActionEvent\\\\\\\"}\\\"},{\\\"name\\\":\\\"actionPerformed\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{\\\\\\\"arg0\\\\\\\":\\\\\\\"ActionEvent\\\\\\\"}\\\"},{\\\"name\\\":\\\"actionPerformed\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{\\\\\\\"e\\\\\\\":\\\\\\\"ActionEvent\\\\\\\"}\\\"},{\\\"name\\\":\\\"actionPerformed\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{\\\\\\\"arg0\\\\\\\":\\\\\\\"ActionEvent\\\\\\\"}\\\"},{\\\"name\\\":\\\"actionPerformed\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{\\\\\\\"e\\\\\\\":\\\\\\\"ActionEvent\\\\\\\"}\\\"},{\\\"name\\\":\\\"actionPerformed\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{\\\\\\\"arg0\\\\\\\":\\\\\\\"ActionEvent\\\\\\\"}\\\"},{\\\"name\\\":\\\"actionPerformed\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{\\\\\\\"e\\\\\\\":\\\\\\\"ActionEvent\\\\\\\"}\\\"},{\\\"name\\\":\\\"actionPerformed\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{\\\\\\\"e\\\\\\\":\\\\\\\"ActionEvent\\\\\\\"}\\\"},{\\\"name\\\":\\\"editContents\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{\\\\\\\"type\\\\\\\":\\\\\\\"String\\\\\\\"}\\\"},{\\\"name\\\":\\\"actionPerformed\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{\\\\\\\"e\\\\\\\":\\\\\\\"ActionEvent\\\\\\\"}\\\"},{\\\"name\\\":\\\"actionPerformed\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{\\\\\\\"e\\\\\\\":\\\\\\\"ActionEvent\\\\\\\"}\\\"},{\\\"name\\\":\\\"actionPerformed\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{\\\\\\\"e\\\\\\\":\\\\\\\"ActionEvent\\\\\\\"}\\\"},{\\\"name\\\":\\\"actionPerformed\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{\\\\\\\"arg0\\\\\\\":\\\\\\\"ActionEvent\\\\\\\"}\\\"},{\\\"name\\\":\\\"actionPerformed\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{\\\\\\\"e\\\\\\\":\\\\\\\"ActionEvent\\\\\\\"}\\\"},{\\\"name\\\":\\\"MainWindow\\\",\\\"returnType\\\":\\\"Constructor\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{\\\\\\\"latexEditorView\\\\\\\":\\\\\\\"LatexEditorView\\\\\\\"}\\\"},{\\\"name\\\":\\\"initialize\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"private\\\",\\\"parameters\\\":\\\"{}\\\"}],\\\"fields\\\":[{\\\"name\\\":\\\"frame\\\",\\\"returnType\\\":\\\"JFrame\\\",\\\"modifier\\\":\\\"private\\\"},{\\\"name\\\":\\\"editorPane\\\",\\\"returnType\\\":\\\"JEditorPane\\\",\\\"modifier\\\":\\\"private\\\"},{\\\"name\\\":\\\"latexEditorView\\\",\\\"returnType\\\":\\\"LatexEditorView\\\",\\\"modifier\\\":\\\"private\\\"}],\\\"arcs\\\":[{\\\"source\\\":\\\"MainWindow\\\",\\\"target\\\":\\\"LatexEditorView\\\",\\\"arcType\\\":\\\"dependency\\\"},{\\\"source\\\":\\\"MainWindow\\\",\\\"target\\\":\\\"LatexEditorView\\\",\\\"arcType\\\":\\\"association\\\"},{\\\"source\\\":\\\"MainWindow\\\",\\\"target\\\":\\\"ChooseTemplate\\\",\\\"arcType\\\":\\\"dependency\\\"}]}\",\"{\\\"name\\\":\\\"ChooseTemplate\\\",\\\"path\\\":\\\""
                             +
                             relativePath +
                             "\\\\\\\\view\\\\\\\\ChooseTemplate.java\\\",\\\"vertexType\\\":\\\"class\\\",\\\"methods\\\":[{\\\"name\\\":\\\"actionPerformed\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{\\\\\\\"e\\\\\\\":\\\\\\\"ActionEvent\\\\\\\"}\\\"},{\\\"name\\\":\\\"initialize\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"private\\\",\\\"parameters\\\":\\\"{}\\\"},{\\\"name\\\":\\\"actionPerformed\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{\\\\\\\"e\\\\\\\":\\\\\\\"ActionEvent\\\\\\\"}\\\"},{\\\"name\\\":\\\"actionPerformed\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{\\\\\\\"arg0\\\\\\\":\\\\\\\"ActionEvent\\\\\\\"}\\\"},{\\\"name\\\":\\\"diselectRadioButtons\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"private\\\",\\\"parameters\\\":\\\"{\\\\\\\"radioButton3\\\\\\\":\\\\\\\"JRadioButton\\\\\\\",\\\\\\\"radioButton4\\\\\\\":\\\\\\\"JRadioButton\\\\\\\",\\\\\\\"radioButton1\\\\\\\":\\\\\\\"JRadioButton\\\\\\\",\\\\\\\"radioButton2\\\\\\\":\\\\\\\"JRadioButton\\\\\\\"}\\\"},{\\\"name\\\":\\\"actionPerformed\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{\\\\\\\"e\\\\\\\":\\\\\\\"ActionEvent\\\\\\\"}\\\"},{\\\"name\\\":\\\"actionPerformed\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{\\\\\\\"e\\\\\\\":\\\\\\\"ActionEvent\\\\\\\"}\\\"},{\\\"name\\\":\\\"ChooseTemplate\\\",\\\"returnType\\\":\\\"Constructor\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{\\\\\\\"previous\\\\\\\":\\\\\\\"String\\\\\\\",\\\\\\\"latexEditorView\\\\\\\":\\\\\\\"LatexEditorView\\\\\\\"}\\\"},{\\\"name\\\":\\\"actionPerformed\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{\\\\\\\"e\\\\\\\":\\\\\\\"ActionEvent\\\\\\\"}\\\"}],\\\"fields\\\":[{\\\"name\\\":\\\"frame\\\",\\\"returnType\\\":\\\"JFrame\\\",\\\"modifier\\\":\\\"private\\\"},{\\\"name\\\":\\\"latexEditorView\\\",\\\"returnType\\\":\\\"LatexEditorView\\\",\\\"modifier\\\":\\\"private\\\"},{\\\"name\\\":\\\"previous\\\",\\\"returnType\\\":\\\"String\\\",\\\"modifier\\\":\\\"private\\\"}],\\\"arcs\\\":[{\\\"source\\\":\\\"ChooseTemplate\\\",\\\"target\\\":\\\"LatexEditorView\\\",\\\"arcType\\\":\\\"dependency\\\"},{\\\"source\\\":\\\"ChooseTemplate\\\",\\\"target\\\":\\\"LatexEditorView\\\",\\\"arcType\\\":\\\"association\\\"},{\\\"source\\\":\\\"ChooseTemplate\\\",\\\"target\\\":\\\"MainWindow\\\",\\\"arcType\\\":\\\"dependency\\\"},{\\\"source\\\":\\\"ChooseTemplate\\\",\\\"target\\\":\\\"OpeningWindow\\\",\\\"arcType\\\":\\\"dependency\\\"}]}\",\"{\\\"name\\\":\\\"OpeningWindow\\\",\\\"path\\\":\\\""
                             +
                             relativePath +
                             "\\\\\\\\view\\\\\\\\OpeningWindow.java\\\",\\\"vertexType\\\":\\\"class\\\",\\\"methods\\\":[{\\\"name\\\":\\\"main\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{\\\\\\\"args\\\\\\\":\\\\\\\"String[]\\\\\\\"}\\\"},{\\\"name\\\":\\\"actionPerformed\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{\\\\\\\"e\\\\\\\":\\\\\\\"ActionEvent\\\\\\\"}\\\"},{\\\"name\\\":\\\"initialize\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"private\\\",\\\"parameters\\\":\\\"{}\\\"},{\\\"name\\\":\\\"OpeningWindow\\\",\\\"returnType\\\":\\\"Constructor\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{}\\\"},{\\\"name\\\":\\\"actionPerformed\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{\\\\\\\"e\\\\\\\":\\\\\\\"ActionEvent\\\\\\\"}\\\"},{\\\"name\\\":\\\"run\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{}\\\"},{\\\"name\\\":\\\"actionPerformed\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{\\\\\\\"e\\\\\\\":\\\\\\\"ActionEvent\\\\\\\"}\\\"}],\\\"fields\\\":[{\\\"name\\\":\\\"frame\\\",\\\"returnType\\\":\\\"JFrame\\\",\\\"modifier\\\":\\\"private\\\"},{\\\"name\\\":\\\"latexEditorView\\\",\\\"returnType\\\":\\\"LatexEditorView\\\",\\\"modifier\\\":\\\"private\\\"}],\\\"arcs\\\":[{\\\"source\\\":\\\"OpeningWindow\\\",\\\"target\\\":\\\"LatexEditorController\\\",\\\"arcType\\\":\\\"dependency\\\"},{\\\"source\\\":\\\"OpeningWindow\\\",\\\"target\\\":\\\"VersionsManager\\\",\\\"arcType\\\":\\\"dependency\\\"},{\\\"source\\\":\\\"OpeningWindow\\\",\\\"target\\\":\\\"LatexEditorView\\\",\\\"arcType\\\":\\\"association\\\"},{\\\"source\\\":\\\"OpeningWindow\\\",\\\"target\\\":\\\"ChooseTemplate\\\",\\\"arcType\\\":\\\"dependency\\\"},{\\\"source\\\":\\\"OpeningWindow\\\",\\\"target\\\":\\\"VersionsStrategy\\\",\\\"arcType\\\":\\\"dependency\\\"},{\\\"source\\\":\\\"OpeningWindow\\\",\\\"target\\\":\\\"VolatileVersionsStrategy\\\",\\\"arcType\\\":\\\"dependency\\\"}]}\"],\"parent\":{\"name\":\"src\",\"path\":\""
                             +
                             relativePath +
                             "\",\"vertexType\":\"package_private\"},\"neighbours\":[],\"arcs\":[{\"source\":\"src.view\",\"target\":\"src.controller\",\"arcType\":\"dependency\"},{\"source\":\"src.view\",\"target\":\"src.model\",\"arcType\":\"dependency\"},{\"source\":\"src.view\",\"target\":\"src.model.strategies\",\"arcType\":\"dependency\"}]},{\"name\":\"src.controller\",\"path\":\""
                             +
                             relativePath +
                             "\\\\controller\",\"vertexType\":\"package\",\"coordinate_x\":72.0,\"coordinate_y\":70.0,\"sinkVertices\":[\"{\\\"name\\\":\\\"LatexEditorController\\\",\\\"path\\\":\\\""
                             +
                             relativePath +
                             "\\\\\\\\controller\\\\\\\\LatexEditorController.java\\\",\\\"vertexType\\\":\\\"class\\\",\\\"methods\\\":[{\\\"name\\\":\\\"LatexEditorController\\\",\\\"returnType\\\":\\\"Constructor\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{\\\\\\\"versionsManager\\\\\\\":\\\\\\\"VersionsManager\\\\\\\"}\\\"},{\\\"name\\\":\\\"enact\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{\\\\\\\"command\\\\\\\":\\\\\\\"String\\\\\\\"}\\\"}],\\\"fields\\\":[{\\\"name\\\":\\\"commands\\\",\\\"returnType\\\":\\\"HashMap[String,Command]\\\",\\\"modifier\\\":\\\"private\\\"}],\\\"arcs\\\":[{\\\"source\\\":\\\"LatexEditorController\\\",\\\"target\\\":\\\"VersionsManager\\\",\\\"arcType\\\":\\\"dependency\\\"},{\\\"source\\\":\\\"LatexEditorController\\\",\\\"target\\\":\\\"Command\\\",\\\"arcType\\\":\\\"aggregation\\\"},{\\\"source\\\":\\\"LatexEditorController\\\",\\\"target\\\":\\\"CommandFactory\\\",\\\"arcType\\\":\\\"dependency\\\"}]}\"],\"parent\":{\"name\":\"src\",\"path\":\""
                             +
                             relativePath +
                             "\",\"vertexType\":\"package_private\"},\"neighbours\":[{\"name\":\"src.controller.commands\",\"path\":\"\\\""
                             +
                             relativePath +
                             "\\\\\\\\controller\\\\\\\\commands\",\"vertexType\":\"package\",\"parentName\":\"src.controller\"}],\"arcs\":[{\"source\":\"src.controller\",\"target\":\"src.model\",\"arcType\":\"dependency\"},{\"source\":\"src.controller\",\"target\":\"src.controller.commands\",\"arcType\":\"dependency\"}]},{\"name\":\"src.controller.commands\",\"path\":\""
                             +
                             relativePath +
                             "\\\\controller\\\\commands\",\"vertexType\":\"package\",\"coordinate_x\":25.0,\"coordinate_y\":140.0,\"sinkVertices\":[\"{\\\"name\\\":\\\"CreateCommand\\\",\\\"path\\\":\\\""
                             +
                             relativePath +
                             "\\\\\\\\controller\\\\\\\\commands\\\\\\\\CreateCommand.java\\\",\\\"vertexType\\\":\\\"class\\\",\\\"methods\\\":[{\\\"name\\\":\\\"CreateCommand\\\",\\\"returnType\\\":\\\"Constructor\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{\\\\\\\"documentManager\\\\\\\":\\\\\\\"DocumentManager\\\\\\\",\\\\\\\"versionsManager\\\\\\\":\\\\\\\"VersionsManager\\\\\\\"}\\\"},{\\\"name\\\":\\\"execute\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{}\\\"}],\\\"fields\\\":[{\\\"name\\\":\\\"documentManager\\\",\\\"returnType\\\":\\\"DocumentManager\\\",\\\"modifier\\\":\\\"private\\\"},{\\\"name\\\":\\\"versionsManager\\\",\\\"returnType\\\":\\\"VersionsManager\\\",\\\"modifier\\\":\\\"private\\\"}],\\\"arcs\\\":[{\\\"source\\\":\\\"CreateCommand\\\",\\\"target\\\":\\\"VersionsManager\\\",\\\"arcType\\\":\\\"dependency\\\"},{\\\"source\\\":\\\"CreateCommand\\\",\\\"target\\\":\\\"VersionsManager\\\",\\\"arcType\\\":\\\"association\\\"},{\\\"source\\\":\\\"CreateCommand\\\",\\\"target\\\":\\\"DocumentManager\\\",\\\"arcType\\\":\\\"dependency\\\"},{\\\"source\\\":\\\"CreateCommand\\\",\\\"target\\\":\\\"DocumentManager\\\",\\\"arcType\\\":\\\"association\\\"},{\\\"source\\\":\\\"CreateCommand\\\",\\\"target\\\":\\\"Command\\\",\\\"arcType\\\":\\\"implementation\\\"}]}\",\"{\\\"name\\\":\\\"AddLatexCommand\\\",\\\"path\\\":\\\""
                             +
                             relativePath +
                             "\\\\\\\\controller\\\\\\\\commands\\\\\\\\AddLatexCommand.java\\\",\\\"vertexType\\\":\\\"class\\\",\\\"methods\\\":[{\\\"name\\\":\\\"AddLatexCommand\\\",\\\"returnType\\\":\\\"Constructor\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{\\\\\\\"versionsManager\\\\\\\":\\\\\\\"VersionsManager\\\\\\\"}\\\"},{\\\"name\\\":\\\"execute\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{}\\\"}],\\\"fields\\\":[{\\\"name\\\":\\\"versionsManager\\\",\\\"returnType\\\":\\\"VersionsManager\\\",\\\"modifier\\\":\\\"private\\\"}],\\\"arcs\\\":[{\\\"source\\\":\\\"AddLatexCommand\\\",\\\"target\\\":\\\"VersionsManager\\\",\\\"arcType\\\":\\\"dependency\\\"},{\\\"source\\\":\\\"AddLatexCommand\\\",\\\"target\\\":\\\"VersionsManager\\\",\\\"arcType\\\":\\\"association\\\"},{\\\"source\\\":\\\"AddLatexCommand\\\",\\\"target\\\":\\\"Command\\\",\\\"arcType\\\":\\\"implementation\\\"}]}\",\"{\\\"name\\\":\\\"SaveCommand\\\",\\\"path\\\":\\\""
                             +
                             relativePath +
                             "\\\\\\\\controller\\\\\\\\commands\\\\\\\\SaveCommand.java\\\",\\\"vertexType\\\":\\\"class\\\",\\\"methods\\\":[{\\\"name\\\":\\\"execute\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{}\\\"},{\\\"name\\\":\\\"SaveCommand\\\",\\\"returnType\\\":\\\"Constructor\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{\\\\\\\"versionsManager\\\\\\\":\\\\\\\"VersionsManager\\\\\\\"}\\\"}],\\\"fields\\\":[{\\\"name\\\":\\\"versionsManager\\\",\\\"returnType\\\":\\\"VersionsManager\\\",\\\"modifier\\\":\\\"private\\\"}],\\\"arcs\\\":[{\\\"source\\\":\\\"SaveCommand\\\",\\\"target\\\":\\\"VersionsManager\\\",\\\"arcType\\\":\\\"dependency\\\"},{\\\"source\\\":\\\"SaveCommand\\\",\\\"target\\\":\\\"VersionsManager\\\",\\\"arcType\\\":\\\"association\\\"},{\\\"source\\\":\\\"SaveCommand\\\",\\\"target\\\":\\\"Command\\\",\\\"arcType\\\":\\\"implementation\\\"}]}\",\"{\\\"name\\\":\\\"ChangeVersionsStrategyCommand\\\",\\\"path\\\":\\\""
                             +
                             relativePath +
                             "\\\\\\\\controller\\\\\\\\commands\\\\\\\\ChangeVersionsStrategyCommand.java\\\",\\\"vertexType\\\":\\\"class\\\",\\\"methods\\\":[{\\\"name\\\":\\\"ChangeVersionsStrategyCommand\\\",\\\"returnType\\\":\\\"Constructor\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{\\\\\\\"versionsManager\\\\\\\":\\\\\\\"VersionsManager\\\\\\\"}\\\"},{\\\"name\\\":\\\"execute\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{}\\\"}],\\\"fields\\\":[{\\\"name\\\":\\\"versionsManager\\\",\\\"returnType\\\":\\\"VersionsManager\\\",\\\"modifier\\\":\\\"private\\\"}],\\\"arcs\\\":[{\\\"source\\\":\\\"ChangeVersionsStrategyCommand\\\",\\\"target\\\":\\\"VersionsManager\\\",\\\"arcType\\\":\\\"dependency\\\"},{\\\"source\\\":\\\"ChangeVersionsStrategyCommand\\\",\\\"target\\\":\\\"VersionsManager\\\",\\\"arcType\\\":\\\"association\\\"},{\\\"source\\\":\\\"ChangeVersionsStrategyCommand\\\",\\\"target\\\":\\\"Command\\\",\\\"arcType\\\":\\\"implementation\\\"}]}\",\"{\\\"name\\\":\\\"Command\\\",\\\"path\\\":\\\""
                             +
                             relativePath +
                             "\\\\\\\\controller\\\\\\\\commands\\\\\\\\Command.java\\\",\\\"vertexType\\\":\\\"interface\\\",\\\"methods\\\":[{\\\"name\\\":\\\"execute\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{}\\\"}],\\\"fields\\\":[],\\\"arcs\\\":[]}\",\"{\\\"name\\\":\\\"DisableVersionsManagementCommand\\\",\\\"path\\\":\\\""
                             +
                             relativePath +
                             "\\\\\\\\controller\\\\\\\\commands\\\\\\\\DisableVersionsManagementCommand.java\\\",\\\"vertexType\\\":\\\"class\\\",\\\"methods\\\":[{\\\"name\\\":\\\"DisableVersionsManagementCommand\\\",\\\"returnType\\\":\\\"Constructor\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{\\\\\\\"versionsManager\\\\\\\":\\\\\\\"VersionsManager\\\\\\\"}\\\"},{\\\"name\\\":\\\"execute\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{}\\\"}],\\\"fields\\\":[{\\\"name\\\":\\\"versionsManager\\\",\\\"returnType\\\":\\\"VersionsManager\\\",\\\"modifier\\\":\\\"private\\\"}],\\\"arcs\\\":[{\\\"source\\\":\\\"DisableVersionsManagementCommand\\\",\\\"target\\\":\\\"VersionsManager\\\",\\\"arcType\\\":\\\"dependency\\\"},{\\\"source\\\":\\\"DisableVersionsManagementCommand\\\",\\\"target\\\":\\\"VersionsManager\\\",\\\"arcType\\\":\\\"association\\\"},{\\\"source\\\":\\\"DisableVersionsManagementCommand\\\",\\\"target\\\":\\\"Command\\\",\\\"arcType\\\":\\\"implementation\\\"}]}\",\"{\\\"name\\\":\\\"LoadCommand\\\",\\\"path\\\":\\\""
                             +
                             relativePath +
                             "\\\\\\\\controller\\\\\\\\commands\\\\\\\\LoadCommand.java\\\",\\\"vertexType\\\":\\\"class\\\",\\\"methods\\\":[{\\\"name\\\":\\\"execute\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{}\\\"},{\\\"name\\\":\\\"LoadCommand\\\",\\\"returnType\\\":\\\"Constructor\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{\\\\\\\"versionsManager\\\\\\\":\\\\\\\"VersionsManager\\\\\\\"}\\\"},{\\\"name\\\":\\\"getVersionsManager\\\",\\\"returnType\\\":\\\"VersionsManager\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{}\\\"},{\\\"name\\\":\\\"setVersionsManager\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{\\\\\\\"versionsManager\\\\\\\":\\\\\\\"VersionsManager\\\\\\\"}\\\"}],\\\"fields\\\":[{\\\"name\\\":\\\"versionsManager\\\",\\\"returnType\\\":\\\"VersionsManager\\\",\\\"modifier\\\":\\\"private\\\"}],\\\"arcs\\\":[{\\\"source\\\":\\\"LoadCommand\\\",\\\"target\\\":\\\"VersionsManager\\\",\\\"arcType\\\":\\\"dependency\\\"},{\\\"source\\\":\\\"LoadCommand\\\",\\\"target\\\":\\\"VersionsManager\\\",\\\"arcType\\\":\\\"association\\\"},{\\\"source\\\":\\\"LoadCommand\\\",\\\"target\\\":\\\"Command\\\",\\\"arcType\\\":\\\"implementation\\\"}]}\",\"{\\\"name\\\":\\\"CommandFactory\\\",\\\"path\\\":\\\""
                             +
                             relativePath +
                             "\\\\\\\\controller\\\\\\\\commands\\\\\\\\CommandFactory.java\\\",\\\"vertexType\\\":\\\"class\\\",\\\"methods\\\":[{\\\"name\\\":\\\"CommandFactory\\\",\\\"returnType\\\":\\\"Constructor\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{\\\\\\\"versionsManager\\\\\\\":\\\\\\\"VersionsManager\\\\\\\"}\\\"},{\\\"name\\\":\\\"createCommand\\\",\\\"returnType\\\":\\\"Command\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{\\\\\\\"type\\\\\\\":\\\\\\\"String\\\\\\\"}\\\"}],\\\"fields\\\":[{\\\"name\\\":\\\"documentManager\\\",\\\"returnType\\\":\\\"DocumentManager\\\",\\\"modifier\\\":\\\"private\\\"},{\\\"name\\\":\\\"versionsManager\\\",\\\"returnType\\\":\\\"VersionsManager\\\",\\\"modifier\\\":\\\"private\\\"}],\\\"arcs\\\":[{\\\"source\\\":\\\"CommandFactory\\\",\\\"target\\\":\\\"VersionsManager\\\",\\\"arcType\\\":\\\"dependency\\\"},{\\\"source\\\":\\\"CommandFactory\\\",\\\"target\\\":\\\"VersionsManager\\\",\\\"arcType\\\":\\\"association\\\"},{\\\"source\\\":\\\"CommandFactory\\\",\\\"target\\\":\\\"DocumentManager\\\",\\\"arcType\\\":\\\"association\\\"},{\\\"source\\\":\\\"CommandFactory\\\",\\\"target\\\":\\\"CreateCommand\\\",\\\"arcType\\\":\\\"dependency\\\"},{\\\"source\\\":\\\"CommandFactory\\\",\\\"target\\\":\\\"AddLatexCommand\\\",\\\"arcType\\\":\\\"dependency\\\"},{\\\"source\\\":\\\"CommandFactory\\\",\\\"target\\\":\\\"SaveCommand\\\",\\\"arcType\\\":\\\"dependency\\\"},{\\\"source\\\":\\\"CommandFactory\\\",\\\"target\\\":\\\"ChangeVersionsStrategyCommand\\\",\\\"arcType\\\":\\\"dependency\\\"},{\\\"source\\\":\\\"CommandFactory\\\",\\\"target\\\":\\\"Command\\\",\\\"arcType\\\":\\\"dependency\\\"},{\\\"source\\\":\\\"CommandFactory\\\",\\\"target\\\":\\\"DisableVersionsManagementCommand\\\",\\\"arcType\\\":\\\"dependency\\\"},{\\\"source\\\":\\\"CommandFactory\\\",\\\"target\\\":\\\"LoadCommand\\\",\\\"arcType\\\":\\\"dependency\\\"},{\\\"source\\\":\\\"CommandFactory\\\",\\\"target\\\":\\\"EnableVersionsManagementCommand\\\",\\\"arcType\\\":\\\"dependency\\\"},{\\\"source\\\":\\\"CommandFactory\\\",\\\"target\\\":\\\"EditCommand\\\",\\\"arcType\\\":\\\"dependency\\\"},{\\\"source\\\":\\\"CommandFactory\\\",\\\"target\\\":\\\"RollbackToPreviousVersionCommand\\\",\\\"arcType\\\":\\\"dependency\\\"}]}\",\"{\\\"name\\\":\\\"EnableVersionsManagementCommand\\\",\\\"path\\\":\\\""
                             +
                             relativePath +
                             "\\\\\\\\controller\\\\\\\\commands\\\\\\\\EnableVersionsManagementCommand.java\\\",\\\"vertexType\\\":\\\"class\\\",\\\"methods\\\":[{\\\"name\\\":\\\"EnableVersionsManagementCommand\\\",\\\"returnType\\\":\\\"Constructor\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{\\\\\\\"versionsManager\\\\\\\":\\\\\\\"VersionsManager\\\\\\\"}\\\"},{\\\"name\\\":\\\"execute\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{}\\\"}],\\\"fields\\\":[{\\\"name\\\":\\\"versionsManager\\\",\\\"returnType\\\":\\\"VersionsManager\\\",\\\"modifier\\\":\\\"private\\\"}],\\\"arcs\\\":[{\\\"source\\\":\\\"EnableVersionsManagementCommand\\\",\\\"target\\\":\\\"VersionsManager\\\",\\\"arcType\\\":\\\"dependency\\\"},{\\\"source\\\":\\\"EnableVersionsManagementCommand\\\",\\\"target\\\":\\\"VersionsManager\\\",\\\"arcType\\\":\\\"association\\\"},{\\\"source\\\":\\\"EnableVersionsManagementCommand\\\",\\\"target\\\":\\\"Command\\\",\\\"arcType\\\":\\\"implementation\\\"}]}\",\"{\\\"name\\\":\\\"EditCommand\\\",\\\"path\\\":\\\""
                             +
                             relativePath +
                             "\\\\\\\\controller\\\\\\\\commands\\\\\\\\EditCommand.java\\\",\\\"vertexType\\\":\\\"class\\\",\\\"methods\\\":[{\\\"name\\\":\\\"execute\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{}\\\"},{\\\"name\\\":\\\"EditCommand\\\",\\\"returnType\\\":\\\"Constructor\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{\\\\\\\"versionsManager\\\\\\\":\\\\\\\"VersionsManager\\\\\\\"}\\\"}],\\\"fields\\\":[{\\\"name\\\":\\\"versionsManager\\\",\\\"returnType\\\":\\\"VersionsManager\\\",\\\"modifier\\\":\\\"private\\\"}],\\\"arcs\\\":[{\\\"source\\\":\\\"EditCommand\\\",\\\"target\\\":\\\"VersionsManager\\\",\\\"arcType\\\":\\\"dependency\\\"},{\\\"source\\\":\\\"EditCommand\\\",\\\"target\\\":\\\"VersionsManager\\\",\\\"arcType\\\":\\\"association\\\"},{\\\"source\\\":\\\"EditCommand\\\",\\\"target\\\":\\\"Command\\\",\\\"arcType\\\":\\\"implementation\\\"}]}\",\"{\\\"name\\\":\\\"RollbackToPreviousVersionCommand\\\",\\\"path\\\":\\\""
                             +
                             relativePath +
                             "\\\\\\\\controller\\\\\\\\commands\\\\\\\\RollbackToPreviousVersionCommand.java\\\",\\\"vertexType\\\":\\\"class\\\",\\\"methods\\\":[{\\\"name\\\":\\\"RollbackToPreviousVersionCommand\\\",\\\"returnType\\\":\\\"Constructor\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{\\\\\\\"versionsManager\\\\\\\":\\\\\\\"VersionsManager\\\\\\\"}\\\"},{\\\"name\\\":\\\"execute\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{}\\\"}],\\\"fields\\\":[{\\\"name\\\":\\\"versionsManager\\\",\\\"returnType\\\":\\\"VersionsManager\\\",\\\"modifier\\\":\\\"private\\\"}],\\\"arcs\\\":[{\\\"source\\\":\\\"RollbackToPreviousVersionCommand\\\",\\\"target\\\":\\\"VersionsManager\\\",\\\"arcType\\\":\\\"dependency\\\"},{\\\"source\\\":\\\"RollbackToPreviousVersionCommand\\\",\\\"target\\\":\\\"VersionsManager\\\",\\\"arcType\\\":\\\"association\\\"},{\\\"source\\\":\\\"RollbackToPreviousVersionCommand\\\",\\\"target\\\":\\\"Command\\\",\\\"arcType\\\":\\\"implementation\\\"}]}\"],\"parent\":{\"name\":\"src.controller\",\"path\":\""
                             +
                             relativePath +
                             "\\\\\\\\controller\",\"vertexType\":\"package_private\"},\"neighbours\":[],\"arcs\":[{\"source\":\"src.controller.commands\",\"target\":\"src.model\",\"arcType\":\"dependency\"}]},{\"name\":\"src.model\",\"path\":\""
                             +
                             relativePath +
                             "\\\\model\",\"vertexType\":\"package\",\"coordinate_x\":217.0,\"coordinate_y\":210.0,\"sinkVertices\":[\"{\\\"name\\\":\\\"VersionsManager\\\",\\\"path\\\":\\\""
                             +
                             relativePath +
                             "\\\\\\\\model\\\\\\\\VersionsManager.java\\\",\\\"vertexType\\\":\\\"class\\\",\\\"methods\\\":[{\\\"name\\\":\\\"setStrategy\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{\\\\\\\"strategy\\\\\\\":\\\\\\\"VersionsStrategy\\\\\\\"}\\\"},{\\\"name\\\":\\\"setCurrentVersion\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{\\\\\\\"document\\\\\\\":\\\\\\\"Document\\\\\\\"}\\\"},{\\\"name\\\":\\\"changeStrategy\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{}\\\"},{\\\"name\\\":\\\"getType\\\",\\\"returnType\\\":\\\"String\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{}\\\"},{\\\"name\\\":\\\"rollback\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{}\\\"},{\\\"name\\\":\\\"isEnabled\\\",\\\"returnType\\\":\\\"boolean\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{}\\\"},{\\\"name\\\":\\\"setPreviousVersion\\\",\\\"returnType\\\":\\\"Document\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{}\\\"},{\\\"name\\\":\\\"getStrategy\\\",\\\"returnType\\\":\\\"VersionsStrategy\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{}\\\"},{\\\"name\\\":\\\"saveContents\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{}\\\"},{\\\"name\\\":\\\"enable\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{}\\\"},{\\\"name\\\":\\\"loadFromFile\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{}\\\"},{\\\"name\\\":\\\"VersionsManager\\\",\\\"returnType\\\":\\\"Constructor\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{\\\\\\\"latexEditorView\\\\\\\":\\\\\\\"LatexEditorView\\\\\\\",\\\\\\\"versionsStrategy\\\\\\\":\\\\\\\"VersionsStrategy\\\\\\\"}\\\"},{\\\"name\\\":\\\"enableStrategy\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{}\\\"},{\\\"name\\\":\\\"disable\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{}\\\"},{\\\"name\\\":\\\"putVersion\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{\\\\\\\"document\\\\\\\":\\\\\\\"Document\\\\\\\"}\\\"},{\\\"name\\\":\\\"rollbackToPreviousVersion\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{}\\\"},{\\\"name\\\":\\\"saveToFile\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{}\\\"}],\\\"fields\\\":[{\\\"name\\\":\\\"enabled\\\",\\\"returnType\\\":\\\"boolean\\\",\\\"modifier\\\":\\\"private\\\"},{\\\"name\\\":\\\"strategy\\\",\\\"returnType\\\":\\\"VersionsStrategy\\\",\\\"modifier\\\":\\\"private\\\"},{\\\"name\\\":\\\"latexEditorView\\\",\\\"returnType\\\":\\\"LatexEditorView\\\",\\\"modifier\\\":\\\"private\\\"}],\\\"arcs\\\":[{\\\"source\\\":\\\"VersionsManager\\\",\\\"target\\\":\\\"Document\\\",\\\"arcType\\\":\\\"dependency\\\"},{\\\"source\\\":\\\"VersionsManager\\\",\\\"target\\\":\\\"LatexEditorView\\\",\\\"arcType\\\":\\\"dependency\\\"},{\\\"source\\\":\\\"VersionsManager\\\",\\\"target\\\":\\\"LatexEditorView\\\",\\\"arcType\\\":\\\"association\\\"},{\\\"source\\\":\\\"VersionsManager\\\",\\\"target\\\":\\\"VersionsStrategy\\\",\\\"arcType\\\":\\\"dependency\\\"},{\\\"source\\\":\\\"VersionsManager\\\",\\\"target\\\":\\\"VersionsStrategy\\\",\\\"arcType\\\":\\\"association\\\"}]}\",\"{\\\"name\\\":\\\"DocumentManager\\\",\\\"path\\\":\\\""
                             +
                             relativePath +
                             "\\\\\\\\model\\\\\\\\DocumentManager.java\\\",\\\"vertexType\\\":\\\"class\\\",\\\"methods\\\":[{\\\"name\\\":\\\"createDocument\\\",\\\"returnType\\\":\\\"Document\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{\\\\\\\"type\\\\\\\":\\\\\\\"String\\\\\\\"}\\\"},{\\\"name\\\":\\\"DocumentManager\\\",\\\"returnType\\\":\\\"Constructor\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{}\\\"},{\\\"name\\\":\\\"getContents\\\",\\\"returnType\\\":\\\"String\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{\\\\\\\"type\\\\\\\":\\\\\\\"String\\\\\\\"}\\\"}],\\\"fields\\\":[{\\\"name\\\":\\\"templates\\\",\\\"returnType\\\":\\\"HashMap[String,Document]\\\",\\\"modifier\\\":\\\"private\\\"}],\\\"arcs\\\":[{\\\"source\\\":\\\"DocumentManager\\\",\\\"target\\\":\\\"Document\\\",\\\"arcType\\\":\\\"dependency\\\"},{\\\"source\\\":\\\"DocumentManager\\\",\\\"target\\\":\\\"Document\\\",\\\"arcType\\\":\\\"aggregation\\\"}]}\",\"{\\\"name\\\":\\\"Document\\\",\\\"path\\\":\\\""
                             +
                             relativePath +
                             "\\\\\\\\model\\\\\\\\Document.java\\\",\\\"vertexType\\\":\\\"class\\\",\\\"methods\\\":[{\\\"name\\\":\\\"clone\\\",\\\"returnType\\\":\\\"Document\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{}\\\"},{\\\"name\\\":\\\"changeVersion\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{}\\\"},{\\\"name\\\":\\\"save\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{\\\\\\\"filename\\\\\\\":\\\\\\\"String\\\\\\\"}\\\"},{\\\"name\\\":\\\"setContents\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{\\\\\\\"contents\\\\\\\":\\\\\\\"String\\\\\\\"}\\\"},{\\\"name\\\":\\\"getContents\\\",\\\"returnType\\\":\\\"String\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{}\\\"},{\\\"name\\\":\\\"getVersionID\\\",\\\"returnType\\\":\\\"String\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{}\\\"},{\\\"name\\\":\\\"Document\\\",\\\"returnType\\\":\\\"Constructor\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{\\\\\\\"date\\\\\\\":\\\\\\\"String\\\\\\\",\\\\\\\"copyright\\\\\\\":\\\\\\\"String\\\\\\\",\\\\\\\"versionID\\\\\\\":\\\\\\\"String\\\\\\\",\\\\\\\"contents\\\\\\\":\\\\\\\"String\\\\\\\",\\\\\\\"author\\\\\\\":\\\\\\\"String\\\\\\\"}\\\"},{\\\"name\\\":\\\"Document\\\",\\\"returnType\\\":\\\"Constructor\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{}\\\"}],\\\"fields\\\":[{\\\"name\\\":\\\"author\\\",\\\"returnType\\\":\\\"String\\\",\\\"modifier\\\":\\\"private\\\"},{\\\"name\\\":\\\"date\\\",\\\"returnType\\\":\\\"String\\\",\\\"modifier\\\":\\\"private\\\"},{\\\"name\\\":\\\"copyright\\\",\\\"returnType\\\":\\\"String\\\",\\\"modifier\\\":\\\"private\\\"},{\\\"name\\\":\\\"versionID\\\",\\\"returnType\\\":\\\"String\\\",\\\"modifier\\\":\\\"private\\\"},{\\\"name\\\":\\\"contents\\\",\\\"returnType\\\":\\\"String\\\",\\\"modifier\\\":\\\"private\\\"}],\\\"arcs\\\":[]}\"],\"parent\":{\"name\":\"src\",\"path\":\""
                             +
                             relativePath +
                             "\",\"vertexType\":\"package_private\"},\"neighbours\":[{\"name\":\"src.model.strategies\",\"path\":\"\\\""
                             +
                             relativePath +
                             "\\\\\\\\model\\\\\\\\strategies\",\"vertexType\":\"package\",\"parentName\":\"src.model\"}],\"arcs\":[{\"source\":\"src.model\",\"target\":\"src.view\",\"arcType\":\"dependency\"},{\"source\":\"src.model\",\"target\":\"src.model.strategies\",\"arcType\":\"dependency\"}]},{\"name\":\"src.model.strategies\",\"path\":\""
                             +
                             relativePath +
                             "\\\\model\\\\strategies\",\"vertexType\":\"package\",\"coordinate_x\":362.0,\"coordinate_y\":280.0,\"sinkVertices\":[\"{\\\"name\\\":\\\"VersionsStrategy\\\",\\\"path\\\":\\\""
                             +
                             relativePath +
                             "\\\\\\\\model\\\\\\\\strategies\\\\\\\\VersionsStrategy.java\\\",\\\"vertexType\\\":\\\"interface\\\",\\\"methods\\\":[{\\\"name\\\":\\\"removeVersion\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{}\\\"},{\\\"name\\\":\\\"setEntireHistory\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{\\\\\\\"documents\\\\\\\":\\\\\\\"List[Document]\\\\\\\"}\\\"},{\\\"name\\\":\\\"putVersion\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{\\\\\\\"document\\\\\\\":\\\\\\\"Document\\\\\\\"}\\\"},{\\\"name\\\":\\\"getEntireHistory\\\",\\\"returnType\\\":\\\"List[Document]\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{}\\\"},{\\\"name\\\":\\\"getVersion\\\",\\\"returnType\\\":\\\"Document\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{}\\\"}],\\\"fields\\\":[],\\\"arcs\\\":[{\\\"source\\\":\\\"VersionsStrategy\\\",\\\"target\\\":\\\"Document\\\",\\\"arcType\\\":\\\"dependency\\\"}]}\",\"{\\\"name\\\":\\\"VolatileVersionsStrategy\\\",\\\"path\\\":\\\""
                             +
                             relativePath +
                             "\\\\\\\\model\\\\\\\\strategies\\\\\\\\VolatileVersionsStrategy.java\\\",\\\"vertexType\\\":\\\"class\\\",\\\"methods\\\":[{\\\"name\\\":\\\"VolatileVersionsStrategy\\\",\\\"returnType\\\":\\\"Constructor\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{}\\\"},{\\\"name\\\":\\\"setEntireHistory\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{\\\\\\\"documents\\\\\\\":\\\\\\\"List[Document]\\\\\\\"}\\\"},{\\\"name\\\":\\\"removeVersion\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{}\\\"},{\\\"name\\\":\\\"putVersion\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{\\\\\\\"document\\\\\\\":\\\\\\\"Document\\\\\\\"}\\\"},{\\\"name\\\":\\\"getEntireHistory\\\",\\\"returnType\\\":\\\"List[Document]\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{}\\\"},{\\\"name\\\":\\\"getVersion\\\",\\\"returnType\\\":\\\"Document\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{}\\\"}],\\\"fields\\\":[{\\\"name\\\":\\\"history\\\",\\\"returnType\\\":\\\"ArrayList[Document]\\\",\\\"modifier\\\":\\\"private\\\"}],\\\"arcs\\\":[{\\\"source\\\":\\\"VolatileVersionsStrategy\\\",\\\"target\\\":\\\"Document\\\",\\\"arcType\\\":\\\"dependency\\\"},{\\\"source\\\":\\\"VolatileVersionsStrategy\\\",\\\"target\\\":\\\"Document\\\",\\\"arcType\\\":\\\"aggregation\\\"},{\\\"source\\\":\\\"VolatileVersionsStrategy\\\",\\\"target\\\":\\\"VersionsStrategy\\\",\\\"arcType\\\":\\\"implementation\\\"}]}\",\"{\\\"name\\\":\\\"VersionsStrategyFactory\\\",\\\"path\\\":\\\""
                             +
                             relativePath +
                             "\\\\\\\\model\\\\\\\\strategies\\\\\\\\VersionsStrategyFactory.java\\\",\\\"vertexType\\\":\\\"class\\\",\\\"methods\\\":[{\\\"name\\\":\\\"VersionsStrategyFactory\\\",\\\"returnType\\\":\\\"Constructor\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{}\\\"},{\\\"name\\\":\\\"createStrategy\\\",\\\"returnType\\\":\\\"VersionsStrategy\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{\\\\\\\"type\\\\\\\":\\\\\\\"String\\\\\\\"}\\\"}],\\\"fields\\\":[{\\\"name\\\":\\\"strategies\\\",\\\"returnType\\\":\\\"HashMap[String,VersionsStrategy]\\\",\\\"modifier\\\":\\\"private\\\"}],\\\"arcs\\\":[{\\\"source\\\":\\\"VersionsStrategyFactory\\\",\\\"target\\\":\\\"VersionsStrategy\\\",\\\"arcType\\\":\\\"dependency\\\"},{\\\"source\\\":\\\"VersionsStrategyFactory\\\",\\\"target\\\":\\\"VersionsStrategy\\\",\\\"arcType\\\":\\\"aggregation\\\"},{\\\"source\\\":\\\"VersionsStrategyFactory\\\",\\\"target\\\":\\\"VolatileVersionsStrategy\\\",\\\"arcType\\\":\\\"dependency\\\"},{\\\"source\\\":\\\"VersionsStrategyFactory\\\",\\\"target\\\":\\\"StableVersionsStrategy\\\",\\\"arcType\\\":\\\"dependency\\\"}]}\",\"{\\\"name\\\":\\\"StableVersionsStrategy\\\",\\\"path\\\":\\\""
                             +
                             relativePath +
                             "\\\\\\\\model\\\\\\\\strategies\\\\\\\\StableVersionsStrategy.java\\\",\\\"vertexType\\\":\\\"class\\\",\\\"methods\\\":[{\\\"name\\\":\\\"removeVersion\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{}\\\"},{\\\"name\\\":\\\"setEntireHistory\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{\\\\\\\"documents\\\\\\\":\\\\\\\"List[Document]\\\\\\\"}\\\"},{\\\"name\\\":\\\"putVersion\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{\\\\\\\"document\\\\\\\":\\\\\\\"Document\\\\\\\"}\\\"},{\\\"name\\\":\\\"getEntireHistory\\\",\\\"returnType\\\":\\\"List[Document]\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{}\\\"},{\\\"name\\\":\\\"getVersion\\\",\\\"returnType\\\":\\\"Document\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{}\\\"}],\\\"fields\\\":[{\\\"name\\\":\\\"versionID\\\",\\\"returnType\\\":\\\"String\\\",\\\"modifier\\\":\\\"private\\\"}],\\\"arcs\\\":[{\\\"source\\\":\\\"StableVersionsStrategy\\\",\\\"target\\\":\\\"Document\\\",\\\"arcType\\\":\\\"dependency\\\"},{\\\"source\\\":\\\"StableVersionsStrategy\\\",\\\"target\\\":\\\"VersionsStrategy\\\",\\\"arcType\\\":\\\"implementation\\\"}]}\"],\"parent\":{\"name\":\"src.model\",\"path\":\""
                             +
                             relativePath +
                             "\\\\\\\\model\",\"vertexType\":\"package_private\"},\"neighbours\":[],\"arcs\":[{\"source\":\"src.model.strategies\",\"target\":\"src.model\",\"arcType\":\"dependency\"}]}]";

        expectedString2 = "[{\"name\":\"src.model\",\"path\":\"" +
                          relativePath + "model\",\"vertexType\":\"package\",\"coordinate_x\":0.0,\"coordinate_y\":0.0,\"sinkVertices\":[\"{\\\"name\\\":\\\"VersionsManager\\\",\\\"path\\\":\\\"" +
                          relativePath + "/model/VersionsManager.java\\\",\\\"vertexType\\\":\\\"class\\\",\\\"coordinate_x\\\":0.0,\\\"coordinate_y\\\":0.0," +
                          "\\\"methods\\\":[{\\\"name\\\":\\\"loadFromFile\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"public\\\"," +
                          "\\\"parameters\\\":\\\"{}\\\"},{\\\"name\\\":\\\"enable\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"public\\\"," +
                          "\\\"parameters\\\":\\\"{}\\\"},{\\\"name\\\":\\\"rollbackToPreviousVersion\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"public\\\"," +
                          "\\\"parameters\\\":\\\"{}\\\"},{\\\"name\\\":\\\"disable\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"public\\\"," +
                          "\\\"parameters\\\":\\\"{}\\\"},{\\\"name\\\":\\\"saveToFile\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"public\\\"," +
                          "\\\"parameters\\\":\\\"{}\\\"},{\\\"name\\\":\\\"putVersion\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"public\\\"," +
                          "\\\"parameters\\\":\\\"{\\\\\\\"document\\\\\\\":\\\\\\\"Document\\\\\\\"}\\\"},{\\\"name\\\":\\\"enableStrategy\\\"," +
                          "\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{}\\\"},{\\\"name\\\":\\\"changeStrategy\\\"," +
                          "\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{}\\\"},{\\\"name\\\":\\\"setStrategy\\\"," +
                          "\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{\\\\\\\"strategy\\\\\\\":\\\\\\\"VersionsStrategy\\\\\\\"}\\\"}," +
                          "{\\\"name\\\":\\\"setCurrentVersion\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"public\\\"," +
                          "\\\"parameters\\\":\\\"{\\\\\\\"document\\\\\\\":\\\\\\\"Document\\\\\\\"}\\\"},{\\\"name\\\":\\\"getStrategy\\\"," +
                          "\\\"returnType\\\":\\\"VersionsStrategy\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{}\\\"}," +
                          "{\\\"name\\\":\\\"rollback\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{}\\\"}," +
                          "{\\\"name\\\":\\\"isEnabled\\\",\\\"returnType\\\":\\\"boolean\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{}\\\"}," +
                          "{\\\"name\\\":\\\"VersionsManager\\\",\\\"returnType\\\":\\\"Constructor\\\",\\\"modifier\\\":\\\"public\\\"," +
                          "\\\"parameters\\\":\\\"{\\\\\\\"latexEditorView\\\\\\\":\\\\\\\"LatexEditorView\\\\\\\",\\\\\\\"versionsStrategy\\\\\\\":\\\\\\\"VersionsStrategy\\\\\\\"}\\\"}," +
                          "{\\\"name\\\":\\\"setPreviousVersion\\\",\\\"returnType\\\":\\\"Document\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{}\\\"}" +
                          ",{\\\"name\\\":\\\"getType\\\",\\\"returnType\\\":\\\"String\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{}\\\"}," +
                          "{\\\"name\\\":\\\"saveContents\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{}\\\"}]," +
                          "\\\"fields\\\":[{\\\"name\\\":\\\"enabled\\\",\\\"returnType\\\":\\\"boolean\\\",\\\"modifier\\\":\\\"private\\\"}," +
                          "{\\\"name\\\":\\\"strategy\\\",\\\"returnType\\\":\\\"VersionsStrategy\\\",\\\"modifier\\\":\\\"private\\\"}," +
                          "{\\\"name\\\":\\\"latexEditorView\\\",\\\"returnType\\\":\\\"LatexEditorView\\\",\\\"modifier\\\":\\\"private\\\"}]," +
                          "\\\"arcs\\\":[{\\\"source\\\":\\\"VersionsManager\\\",\\\"target\\\":\\\"LatexEditorView\\\",\\\"arcType\\\":\\\"dependency\\\"}," +
                          "{\\\"source\\\":\\\"VersionsManager\\\",\\\"target\\\":\\\"LatexEditorView\\\",\\\"arcType\\\":\\\"association\\\"}," +
                          "{\\\"source\\\":\\\"VersionsManager\\\",\\\"target\\\":\\\"VersionsStrategy\\\",\\\"arcType\\\":\\\"dependency\\\"}," +
                          "{\\\"source\\\":\\\"VersionsManager\\\",\\\"target\\\":\\\"VersionsStrategy\\\",\\\"arcType\\\":\\\"association\\\"}," +
                          "{\\\"source\\\":\\\"VersionsManager\\\",\\\"target\\\":\\\"Document\\\",\\\"arcType\\\":\\\"dependency\\\"}]}\"," +
                          "\"{\\\"name\\\":\\\"DocumentManager\\\",\\\"path\\\":\\\"" +
                          relativePath + "/model/DocumentManager.java\\\",\\\"vertexType\\\":\\\"class\\\",\\\"coordinate_x\\\":0.0,\\\"coordinate_y\\\":0.0," +
                          "\\\"methods\\\":[{\\\"name\\\":\\\"createDocument\\\",\\\"returnType\\\":\\\"Document\\\",\\\"modifier\\\":\\\"public\\\"," +
                          "\\\"parameters\\\":\\\"{\\\\\\\"type\\\\\\\":\\\\\\\"String\\\\\\\"}\\\"},{\\\"name\\\":\\\"getContents\\\",\\\"returnType\\\":\\\"String\\\"," +
                          "\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{\\\\\\\"type\\\\\\\":\\\\\\\"String\\\\\\\"}\\\"},{\\\"name\\\":\\\"DocumentManager\\\"," +
                          "\\\"returnType\\\":\\\"Constructor\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{}\\\"}],\\\"fields\\\":[{\\\"name\\\":\\\"templates\\\"," +
                          "\\\"returnType\\\":\\\"HashMap[String,Document]\\\",\\\"modifier\\\":\\\"private\\\"}],\\\"arcs\\\":[{\\\"source\\\":\\\"DocumentManager\\\"," +
                          "\\\"target\\\":\\\"Document\\\",\\\"arcType\\\":\\\"dependency\\\"},{\\\"source\\\":\\\"DocumentManager\\\",\\\"target\\\":\\\"Document\\\"," +
                          "\\\"arcType\\\":\\\"aggregation\\\"}]}\",\"{\\\"name\\\":\\\"Document\\\",\\\"path\\\":\\\"" +
                          relativePath + "/model/Document.java\\\",\\\"vertexType\\\":\\\"class\\\",\\\"coordinate_x\\\":0.0,\\\"coordinate_y\\\":0.0," +
                          "\\\"methods\\\":[{\\\"name\\\":\\\"Document\\\",\\\"returnType\\\":\\\"Constructor\\\",\\\"modifier\\\":\\\"public\\\"," +
                          "\\\"parameters\\\":\\\"{}\\\"},{\\\"name\\\":\\\"Document\\\",\\\"returnType\\\":\\\"Constructor\\\",\\\"modifier\\\":\\\"public\\\"," +
                          "\\\"parameters\\\":\\\"{\\\\\\\"date\\\\\\\":\\\\\\\"String\\\\\\\",\\\\\\\"copyright\\\\\\\":\\\\\\\"String\\\\\\\",\\\\\\\"versionID\\\\\\\":\\\\\\\"String\\\\\\\"," +
                          "\\\\\\\"contents\\\\\\\":\\\\\\\"String\\\\\\\",\\\\\\\"author\\\\\\\":\\\\\\\"String\\\\\\\"}\\\"}," +
                          "{\\\"name\\\":\\\"clone\\\",\\\"returnType\\\":\\\"Document\\\",\\\"modifier\\\":\\\"public\\\"," +
                          "\\\"parameters\\\":\\\"{}\\\"},{\\\"name\\\":\\\"save\\\",\\\"returnType\\\":\\\"void\\\"," +
                          "\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{\\\\\\\"filename\\\\\\\":\\\\\\\"String\\\\\\\"}\\\"}," +
                          "{\\\"name\\\":\\\"setContents\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"public\\\"," +
                          "\\\"parameters\\\":\\\"{\\\\\\\"contents\\\\\\\":\\\\\\\"String\\\\\\\"}\\\"},{\\\"name\\\":\\\"changeVersion\\\"," +
                          "\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{}\\\"},{\\\"name\\\":\\\"getVersionID\\\"," +
                          "\\\"returnType\\\":\\\"String\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{}\\\"},{\\\"name\\\":\\\"getContents\\\"," +
                          "\\\"returnType\\\":\\\"String\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{}\\\"}],\\\"fields\\\":[{\\\"name\\\":\\\"author\\\"," +
                          "\\\"returnType\\\":\\\"String\\\",\\\"modifier\\\":\\\"private\\\"},{\\\"name\\\":\\\"date\\\",\\\"returnType\\\":\\\"String\\\",\\\"modifier\\\":\\\"private\\\"}," +
                          "{\\\"name\\\":\\\"copyright\\\",\\\"returnType\\\":\\\"String\\\",\\\"modifier\\\":\\\"private\\\"},{\\\"name\\\":\\\"versionID\\\"," +
                          "\\\"returnType\\\":\\\"String\\\",\\\"modifier\\\":\\\"private\\\"},{\\\"name\\\":\\\"contents\\\",\\\"returnType\\\":\\\"String\\\"," +
                          "\\\"modifier\\\":\\\"private\\\"}],\\\"arcs\\\":[]}\"],\"parent\":{\"name\":\"src\",\"path\":\"" +
                          relativePath + "\",\"vertexType\":\"package_private\"},\"neighbours\":[{\"name\":\"src.model.strategies\",\"path\":\"" +
                          relativePath + "/model/strategies\",\"vertexType\":\"package\",\"parentName\":\"src.model\"}],\"arcs\":[{\"source\":\"src.model\"," +
                          "\"target\":\"src.view\",\"arcType\":\"dependency\"},{\"source\":\"src.model\",\"target\":\"src.model.strategies\",\"arcType\":\"dependency\"}]},{\"name\":\"src.view\",\"path\":\"" +
                          relativePath + "/view\",\"vertexType\":\"package\",\"coordinate_x\":0.0,\"coordinate_y\":0.0,\"sinkVertices\":[\"{\\\"name\\\":\\\"LatexEditorView\\\",\\\"path\\\":\\\"" +
                          relativePath + "/view/LatexEditorView.java\\\",\\\"vertexType\\\":\\\"class\\\",\\\"coordinate_x\\\":0.0,\\\"coordinate_y\\\":0.0,\\\"methods\\\":[{\\\"name\\\":\\\"getText\\\"," +
                          "\\\"returnType\\\":\\\"String\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{}\\\"},{\\\"name\\\":\\\"setType\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"public\\\"," +
                          "\\\"parameters\\\":\\\"{\\\\\\\"type\\\\\\\":\\\\\\\"String\\\\\\\"}\\\"},{\\\"name\\\":\\\"getVersionsManager\\\",\\\"returnType\\\":\\\"VersionsManager\\\",\\\"modifier\\\":\\\"public\\\"," +
                          "\\\"parameters\\\":\\\"{}\\\"},{\\\"name\\\":\\\"getCurrentDocument\\\",\\\"returnType\\\":\\\"Document\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{}\\\"},{\\\"name\\\":\\\"saveContents\\\"," +
                          "\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{}\\\"},{\\\"name\\\":\\\"setCurrentDocument\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"public\\\"," +
                          "\\\"parameters\\\":\\\"{\\\\\\\"currentDocument\\\\\\\":\\\\\\\"Document\\\\\\\"}\\\"},{\\\"name\\\":\\\"setText\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"public\\\"," +
                          "\\\"parameters\\\":\\\"{\\\\\\\"text\\\\\\\":\\\\\\\"String\\\\\\\"}\\\"},{\\\"name\\\":\\\"saveToFile\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"public\\\"," +
                          "\\\"parameters\\\":\\\"{}\\\"},{\\\"name\\\":\\\"setFilename\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"public\\\"," +
                          "\\\"parameters\\\":\\\"{\\\\\\\"filename\\\\\\\":\\\\\\\"String\\\\\\\"}\\\"},{\\\"name\\\":\\\"loadFromFile\\\",\\\"returnType\\\":\\\"void\\\"," +
                          "\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{}\\\"},{\\\"name\\\":\\\"setStrategy\\\",\\\"returnType\\\":\\\"void\\\"," +
                          "\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{\\\\\\\"strategy\\\\\\\":\\\\\\\"String\\\\\\\"}\\\"},{\\\"name\\\":\\\"getStrategy\\\"," +
                          "\\\"returnType\\\":\\\"String\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{}\\\"},{\\\"name\\\":\\\"getFilename\\\"," +
                          "\\\"returnType\\\":\\\"String\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{}\\\"},{\\\"name\\\":\\\"setVersionsManager\\\"," +
                          "\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{\\\\\\\"versionsManager\\\\\\\":\\\\\\\"VersionsManager\\\\\\\"}\\\"}," +
                          "{\\\"name\\\":\\\"setController\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{\\\\\\\"controller\\\\\\\":\\\\\\\"LatexEditorController\\\\\\\"}\\\"}," +
                          "{\\\"name\\\":\\\"getType\\\",\\\"returnType\\\":\\\"String\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{}\\\"},{\\\"name\\\":\\\"getController\\\",\\\"returnType\\\":\\\"LatexEditorController\\\"," +
                          "\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{}\\\"}],\\\"fields\\\":[{\\\"name\\\":\\\"controller\\\",\\\"returnType\\\":\\\"LatexEditorController\\\"," +
                          "\\\"modifier\\\":\\\"private\\\"},{\\\"name\\\":\\\"currentDocument\\\",\\\"returnType\\\":\\\"Document\\\",\\\"modifier\\\":\\\"private\\\"}," +
                          "{\\\"name\\\":\\\"type\\\",\\\"returnType\\\":\\\"String\\\",\\\"modifier\\\":\\\"private\\\"},{\\\"name\\\":\\\"text\\\",\\\"returnType\\\":\\\"String\\\",\\\"modifier\\\":\\\"private\\\"}," +
                          "{\\\"name\\\":\\\"filename\\\",\\\"returnType\\\":\\\"String\\\",\\\"modifier\\\":\\\"private\\\"},{\\\"name\\\":\\\"strategy\\\",\\\"returnType\\\":\\\"String\\\"," +
                          "\\\"modifier\\\":\\\"private\\\"},{\\\"name\\\":\\\"versionsManager\\\",\\\"returnType\\\":\\\"VersionsManager\\\"," +
                          "\\\"modifier\\\":\\\"private\\\"}],\\\"arcs\\\":[{\\\"source\\\":\\\"LatexEditorView\\\",\\\"target\\\":\\\"VersionsManager\\\"," +
                          "\\\"arcType\\\":\\\"dependency\\\"},{\\\"source\\\":\\\"LatexEditorView\\\",\\\"target\\\":\\\"VersionsManager\\\",\\\"arcType\\\":\\\"association\\\"}," +
                          "{\\\"source\\\":\\\"LatexEditorView\\\",\\\"target\\\":\\\"Document\\\",\\\"arcType\\\":\\\"dependency\\\"},{\\\"source\\\":\\\"LatexEditorView\\\",\\\"target\\\":\\\"Document\\\"," +
                          "\\\"arcType\\\":\\\"association\\\"},{\\\"source\\\":\\\"LatexEditorView\\\",\\\"target\\\":\\\"LatexEditorController\\\",\\\"arcType\\\":\\\"dependency\\\"},{\\\"source\\\":\\\"LatexEditorView\\\"," +
                          "\\\"target\\\":\\\"LatexEditorController\\\",\\\"arcType\\\":\\\"association\\\"}]}\",\"{\\\"name\\\":\\\"MainWindow\\\",\\\"path\\\":\\\"" +
                          relativePath + "/view/MainWindow.java\\\",\\\"vertexType\\\":\\\"class\\\",\\\"coordinate_x\\\":0.0,\\\"coordinate_y\\\":0.0," +
                          "\\\"methods\\\":[{\\\"name\\\":\\\"MainWindow\\\",\\\"returnType\\\":\\\"Constructor\\\",\\\"modifier\\\":\\\"public\\\"," +
                          "\\\"parameters\\\":\\\"{\\\\\\\"latexEditorView\\\\\\\":\\\\\\\"LatexEditorView\\\\\\\"}\\\"},{\\\"name\\\":\\\"actionPerformed\\\"," +
                          "\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{\\\\\\\"e\\\\\\\":\\\\\\\"ActionEvent\\\\\\\"}\\\"}," +
                          "{\\\"name\\\":\\\"actionPerformed\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{\\\\\\\"e\\\\\\\":\\\\\\\"ActionEvent\\\\\\\"}\\\"}," +
                          "{\\\"name\\\":\\\"actionPerformed\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{\\\\\\\"e\\\\\\\":\\\\\\\"ActionEvent\\\\\\\"}\\\"}," +
                          "{\\\"name\\\":\\\"actionPerformed\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{\\\\\\\"e\\\\\\\":\\\\\\\"ActionEvent\\\\\\\"}\\\"}," +
                          "{\\\"name\\\":\\\"initialize\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"private\\\",\\\"parameters\\\":\\\"{}\\\"},{\\\"name\\\":\\\"actionPerformed\\\"," +
                          "\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{\\\\\\\"arg0\\\\\\\":\\\\\\\"ActionEvent\\\\\\\"}\\\"},{\\\"name\\\":\\\"editContents\\\"," +
                          "\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{\\\\\\\"type\\\\\\\":\\\\\\\"String\\\\\\\"}\\\"},{\\\"name\\\":\\\"actionPerformed\\\"," +
                          "\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{\\\\\\\"e\\\\\\\":\\\\\\\"ActionEvent\\\\\\\"}\\\"},{\\\"name\\\":\\\"actionPerformed\\\"," +
                          "\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{\\\\\\\"e\\\\\\\":\\\\\\\"ActionEvent\\\\\\\"}\\\"},{\\\"name\\\":\\\"actionPerformed\\\"," +
                          "\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{\\\\\\\"arg0\\\\\\\":\\\\\\\"ActionEvent\\\\\\\"}\\\"},{\\\"name\\\":\\\"actionPerformed\\\"," +
                          "\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{\\\\\\\"e\\\\\\\":\\\\\\\"ActionEvent\\\\\\\"}\\\"},{\\\"name\\\":\\\"actionPerformed\\\"," +
                          "\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{\\\\\\\"e\\\\\\\":\\\\\\\"ActionEvent\\\\\\\"}\\\"},{\\\"name\\\":\\\"actionPerformed\\\"," +
                          "\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{\\\\\\\"e\\\\\\\":\\\\\\\"ActionEvent\\\\\\\"}\\\"},{\\\"name\\\":\\\"actionPerformed\\\"," +
                          "\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{\\\\\\\"e\\\\\\\":\\\\\\\"ActionEvent\\\\\\\"}\\\"},{\\\"name\\\":\\\"actionPerformed\\\"," +
                          "\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{\\\\\\\"arg0\\\\\\\":\\\\\\\"ActionEvent\\\\\\\"}\\\"},{\\\"name\\\":\\\"actionPerformed\\\"," +
                          "\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{\\\\\\\"e\\\\\\\":\\\\\\\"ActionEvent\\\\\\\"}\\\"},{\\\"name\\\":\\\"actionPerformed\\\"," +
                          "\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{\\\\\\\"arg0\\\\\\\":\\\\\\\"ActionEvent\\\\\\\"}\\\"},{\\\"name\\\":\\\"actionPerformed\\\"," +
                          "\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{\\\\\\\"e\\\\\\\":\\\\\\\"ActionEvent\\\\\\\"}\\\"}],\\\"fields\\\":[{\\\"name\\\":\\\"frame\\\"," +
                          "\\\"returnType\\\":\\\"JFrame\\\",\\\"modifier\\\":\\\"private\\\"},{\\\"name\\\":\\\"editorPane\\\",\\\"returnType\\\":\\\"JEditorPane\\\",\\\"modifier\\\":\\\"private\\\"}," +
                          "{\\\"name\\\":\\\"latexEditorView\\\",\\\"returnType\\\":\\\"LatexEditorView\\\",\\\"modifier\\\":\\\"private\\\"}],\\\"arcs\\\":[{\\\"source\\\":\\\"MainWindow\\\",\\\"target\\\":\\\"LatexEditorView\\\"," +
                          "\\\"arcType\\\":\\\"dependency\\\"},{\\\"source\\\":\\\"MainWindow\\\",\\\"target\\\":\\\"LatexEditorView\\\",\\\"arcType\\\":\\\"association\\\"},{\\\"source\\\":\\\"MainWindow\\\",\\\"target\\\":\\\"ChooseTemplate\\\"," +
                          "\\\"arcType\\\":\\\"dependency\\\"}]}\",\"{\\\"name\\\":\\\"ChooseTemplate\\\",\\\"path\\\":\\\"" +
                          relativePath + "/view/ChooseTemplate.java\\\",\\\"vertexType\\\":\\\"class\\\",\\\"coordinate_x\\\":0.0,\\\"coordinate_y\\\":0.0,\\\"methods\\\":[{\\\"name\\\":\\\"actionPerformed\\\"," +
                          "\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{\\\\\\\"e\\\\\\\":\\\\\\\"ActionEvent\\\\\\\"}\\\"},{\\\"name\\\":\\\"actionPerformed\\\"," +
                          "\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{\\\\\\\"e\\\\\\\":\\\\\\\"ActionEvent\\\\\\\"}\\\"},{\\\"name\\\":\\\"actionPerformed\\\"," +
                          "\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{\\\\\\\"e\\\\\\\":\\\\\\\"ActionEvent\\\\\\\"}\\\"},{\\\"name\\\":\\\"initialize\\\"," +
                          "\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"private\\\",\\\"parameters\\\":\\\"{}\\\"},{\\\"name\\\":\\\"ChooseTemplate\\\",\\\"returnType\\\":\\\"Constructor\\\"," +
                          "\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{\\\\\\\"previous\\\\\\\":\\\\\\\"String\\\\\\\",\\\\\\\"latexEditorView\\\\\\\":\\\\\\\"LatexEditorView\\\\\\\"}\\\"}," +
                          "{\\\"name\\\":\\\"actionPerformed\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{\\\\\\\"e\\\\\\\":\\\\\\\"ActionEvent\\\\\\\"}\\\"}," +
                          "{\\\"name\\\":\\\"actionPerformed\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{\\\\\\\"arg0\\\\\\\":\\\\\\\"ActionEvent\\\\\\\"}\\\"},{\\\"name\\\":\\\"diselectRadioButtons\\\"," +
                          "\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"private\\\",\\\"parameters\\\":\\\"{\\\\\\\"radioButton3\\\\\\\":\\\\\\\"JRadioButton\\\\\\\",\\\\\\\"radioButton4\\\\\\\":\\\\\\\"JRadioButton\\\\\\\"," +
                          "\\\\\\\"radioButton1\\\\\\\":\\\\\\\"JRadioButton\\\\\\\",\\\\\\\"radioButton2\\\\\\\":\\\\\\\"JRadioButton\\\\\\\"}\\\"}," +
                          "{\\\"name\\\":\\\"actionPerformed\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"public\\\"," +
                          "\\\"parameters\\\":\\\"{\\\\\\\"e\\\\\\\":\\\\\\\"ActionEvent\\\\\\\"}\\\"}],\\\"fields\\\":[{\\\"name\\\":\\\"frame\\\"," +
                          "\\\"returnType\\\":\\\"JFrame\\\",\\\"modifier\\\":\\\"private\\\"},{\\\"name\\\":\\\"latexEditorView\\\"," +
                          "\\\"returnType\\\":\\\"LatexEditorView\\\",\\\"modifier\\\":\\\"private\\\"},{\\\"name\\\":\\\"previous\\\"," +
                          "\\\"returnType\\\":\\\"String\\\",\\\"modifier\\\":\\\"private\\\"}],\\\"arcs\\\":[{\\\"source\\\":\\\"ChooseTemplate\\\"," +
                          "\\\"target\\\":\\\"LatexEditorView\\\",\\\"arcType\\\":\\\"dependency\\\"},{\\\"source\\\":\\\"ChooseTemplate\\\",\\\"target\\\":\\\"LatexEditorView\\\"," +
                          "\\\"arcType\\\":\\\"association\\\"},{\\\"source\\\":\\\"ChooseTemplate\\\",\\\"target\\\":\\\"MainWindow\\\",\\\"arcType\\\":\\\"dependency\\\"}," +
                          "{\\\"source\\\":\\\"ChooseTemplate\\\",\\\"target\\\":\\\"OpeningWindow\\\",\\\"arcType\\\":\\\"dependency\\\"}]}\",\"{\\\"name\\\":\\\"OpeningWindow\\\"," +
                          "\\\"path\\\":\\\"/home/dimitrios/IdeaProjects/GitHub/ObjectOrientedArchitectureDiagrammer/src/test/resources/LatexEditor/src/view/OpeningWindow.java\\\"," +
                          "\\\"vertexType\\\":\\\"class\\\",\\\"coordinate_x\\\":0.0,\\\"coordinate_y\\\":0.0,\\\"methods\\\":[{\\\"name\\\":\\\"actionPerformed\\\"," +
                          "\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{\\\\\\\"e\\\\\\\":\\\\\\\"ActionEvent\\\\\\\"}\\\"}," +
                          "{\\\"name\\\":\\\"OpeningWindow\\\",\\\"returnType\\\":\\\"Constructor\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{}\\\"}," +
                          "{\\\"name\\\":\\\"actionPerformed\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{\\\\\\\"e\\\\\\\":\\\\\\\"ActionEvent\\\\\\\"}\\\"}," +
                          "{\\\"name\\\":\\\"initialize\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"private\\\",\\\"parameters\\\":\\\"{}\\\"}," +
                          "{\\\"name\\\":\\\"run\\\",\\\"returnType\\\":\\\"void\\\",\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{}\\\"},{\\\"name\\\":\\\"main\\\",\\\"returnType\\\":\\\"void\\\"," +
                          "\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{\\\\\\\"args\\\\\\\":\\\\\\\"String[]\\\\\\\"}\\\"},{\\\"name\\\":\\\"actionPerformed\\\",\\\"returnType\\\":\\\"void\\\"," +
                          "\\\"modifier\\\":\\\"public\\\",\\\"parameters\\\":\\\"{\\\\\\\"e\\\\\\\":\\\\\\\"ActionEvent\\\\\\\"}\\\"}],\\\"fields\\\":[{\\\"name\\\":\\\"frame\\\",\\\"returnType\\\":\\\"JFrame\\\"," +
                          "\\\"modifier\\\":\\\"private\\\"},{\\\"name\\\":\\\"latexEditorView\\\",\\\"returnType\\\":\\\"LatexEditorView\\\",\\\"modifier\\\":\\\"private\\\"}],\\\"arcs\\\":[{\\\"source\\\":\\\"OpeningWindow\\\"," +
                          "\\\"target\\\":\\\"LatexEditorView\\\",\\\"arcType\\\":\\\"association\\\"},{\\\"source\\\":\\\"OpeningWindow\\\",\\\"target\\\":\\\"ChooseTemplate\\\",\\\"arcType\\\":\\\"dependency\\\"},{\\\"source\\\":\\\"OpeningWindow\\\"," +
                          "\\\"target\\\":\\\"VersionsStrategy\\\",\\\"arcType\\\":\\\"dependency\\\"},{\\\"source\\\":\\\"OpeningWindow\\\",\\\"target\\\":\\\"VolatileVersionsStrategy\\\",\\\"arcType\\\":\\\"dependency\\\"},{\\\"source\\\":\\\"OpeningWindow\\\"," +
                          "\\\"target\\\":\\\"VersionsManager\\\",\\\"arcType\\\":\\\"dependency\\\"},{\\\"source\\\":\\\"OpeningWindow\\\",\\\"target\\\":\\\"LatexEditorController\\\",\\\"arcType\\\":\\\"dependency\\\"}]}\"],\"parent\":{\"name\":\"src\",\"path\":\"" +
                          relativePath + "\",\"vertexType\":\"package_private\"},\"neighbours\":[],\"arcs\":[{\"source\":\"src.view\"," +
                          "\"target\":\"src.model\",\"arcType\":\"dependency\"},{\"source\":\"src.view\",\"target\":\"src.controller\"," +
                          "\"arcType\":\"dependency\"},{\"source\":\"src.view\",\"target\":\"src.model.strategies\",\"arcType\":\"dependency\"}]}]";
    }


    private JsonArray getJsonArray()
    {
        if (os.equals("Linux"))
        {
            return JsonParser.parseString(expectedString2).getAsJsonArray();
        }
        else
        {
            return JsonParser.parseString(expectedJsonString).getAsJsonArray();
        }
    }


    private List<String> getPackages()
    {
        if (os.equals("Linux"))
        {
            return List.of("src.view", "src.model");
        }
        else
        {
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


    public static class CompareArray
    {
        public static Set<JsonElement> setOfElements(JsonArray arr)
        {
            Set<JsonElement> set = new HashSet<>();
            for (JsonElement j : arr)
            {
                set.add(j);
            }
            return set;
        }


        public static Set<JsonElement> setOfElements2(JsonArray arr)
        {
            Set<JsonElement> set = new HashSet<>();
            for (JsonElement j : arr)
            {
                String path = j.getAsJsonObject().get("path").toString();
                if (!os.equals("Linux") && !path.isEmpty())
                {
                    j.getAsJsonObject().addProperty("path", path.substring(0, path.length() - 1));
                }
                set.add(j);
            }
            return set;
        }
    }
}
