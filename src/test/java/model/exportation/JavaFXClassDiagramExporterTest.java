package model.exportation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static utils.JsonUtils.getVertices;
import static utils.ListUtils.assertListsEqual;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import manager.ClassDiagramManager;
import model.diagram.exportation.DiagramExporter;
import model.diagram.exportation.JavaFXClassDiagramExporter;
import model.diagram.javafx.ClassifierVertexDeserializer;
import model.graph.ClassifierVertex;
import model.graph.VertexType;
import org.javatuples.Triplet;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import utils.PathConstructor;
import utils.PathTemplate.LatexEditor;

public class JavaFXClassDiagramExporterTest {

    @Test
    void exportDiagramTest() {
        try {
            ClassDiagramManager classDiagramManager = new ClassDiagramManager();
            List<String> chosenFiles =
                    Arrays.asList("MainWindow", "LatexEditorView", "OpeningWindow");
            classDiagramManager.createSourceProject(LatexEditor.SRC.path);
            classDiagramManager.convertTreeToDiagram(chosenFiles);
            DiagramExporter javaFXExporter =
                    new JavaFXClassDiagramExporter(classDiagramManager.getClassDiagram());
            File actualFile =
                    javaFXExporter.exportDiagram(
                            Path.of(
                                    String.format(
                                            "%s%s%s",
                                            PathConstructor.getCurrentPath(),
                                            File.separator,
                                            PathConstructor.constructPath(
                                                    "src",
                                                    "test",
                                                    "resources",
                                                    "testingExportedFile.txt"))));

            try (Reader reader =
                    new FileReader(actualFile.getAbsolutePath(), StandardCharsets.UTF_8)) {
                JsonElement jsonElement = JsonParser.parseReader(reader);
                JsonArray asJsonArray = jsonElement.getAsJsonArray();

                List<JsonElement> jsonElements = new ArrayList<>();
                asJsonArray.forEach(jsonElements::add);

                List<ClassifierVertex> actualClassifierVertices =
                        getVertices(
                                jsonElements,
                                ClassifierVertex.class,
                                new ClassifierVertexDeserializer());

                for (ClassifierVertex classifierVertex : actualClassifierVertices) {
                    Path actualPath = classifierVertex.getPath();
                    VertexType actualVertexType = classifierVertex.getVertexType();

                    Set<ClassifierVertex.Method> classifierVertexMethods =
                            classifierVertex.getMethods();
                    List<String> sinkVertexMethodNames =
                            classifierVertexMethods.stream()
                                    .map(ClassifierVertex.Method::name)
                                    .collect(Collectors.toCollection(LinkedList::new));

                    Set<ClassifierVertex.Field> classifierVertexFields =
                            classifierVertex.getFields();
                    List<String> sinkVertexFieldNames =
                            classifierVertexFields.stream()
                                    .map(ClassifierVertex.Field::name)
                                    .collect(Collectors.toCollection(LinkedList::new));

                    List<Triplet<String, String, String>> deserializedArcs =
                            classifierVertex.getDeserializedArcs();
                    List<String> targetArcVertex =
                            deserializedArcs.stream()
                                    .map(Triplet::getValue1)
                                    .collect(Collectors.toCollection(LinkedList::new));

                    switch (classifierVertex.getName()) {
                        case "MainWindow":
                            assertEquals(LatexEditor.MAIN_WINDOW.path, actualPath);
                            assertEquals(actualVertexType, VertexType.CLASS);

                            assertListsEqual(
                                    Arrays.asList(
                                            "MainWindow",
                                            "editContents",
                                            "actionPerformed",
                                            "initialize"),
                                    sinkVertexMethodNames);

                            assertListsEqual(
                                    sinkVertexFieldNames,
                                    Arrays.asList("frame", "editorPane", "latexEditorView"));

                            assertListsEqual(
                                    Arrays.asList(
                                            "LatexEditorView", "LatexEditorView", "ChooseTemplate"),
                                    targetArcVertex);

                            break;
                        case "LatexEditorView":
                            assertEquals(LatexEditor.LATEX_EDITOR_VIEW.path, actualPath);
                            assertEquals(actualVertexType, VertexType.CLASS);

                            assertListsEqual(
                                    Arrays.asList(
                                            "controller",
                                            "currentDocument",
                                            "type",
                                            "text",
                                            "filename",
                                            "strategy",
                                            "versionsManager"),
                                    sinkVertexFieldNames);

                            assertListsEqual(
                                    Arrays.asList(
                                            "getVersionsManager",
                                            "setVersionsManager",
                                            "getStrategy",
                                            "setStrategy",
                                            "getText",
                                            "setText",
                                            "getController",
                                            "setController",
                                            "getCurrentDocument",
                                            "setCurrentDocument",
                                            "getType",
                                            "setType",
                                            "saveContents",
                                            "saveToFile",
                                            "getFilename",
                                            "setFilename",
                                            "loadFromFile"),
                                    sinkVertexMethodNames);

                            assertListsEqual(
                                    Arrays.asList(
                                            "VersionsManager",
                                            "Document",
                                            "VersionsManager",
                                            "LatexEditorController",
                                            "LatexEditorController",
                                            "Document"),
                                    targetArcVertex);

                            break;

                        case "OpeningWindow":
                            assertEquals(LatexEditor.OPENING_WINDOW.path, actualPath);
                            assertEquals(actualVertexType, VertexType.CLASS);

                            assertListsEqual(
                                    Arrays.asList("frame", "latexEditorView"),
                                    sinkVertexFieldNames);

                            assertListsEqual(
                                    Arrays.asList(
                                            "OpeningWindow",
                                            "run",
                                            "main",
                                            "actionPerformed",
                                            "initialize"),
                                    sinkVertexMethodNames);

                            assertListsEqual(
                                    Arrays.asList(
                                            "VersionsStrategy",
                                            "ChooseTemplate",
                                            "VolatileVersionsStrategy",
                                            "VersionsManager",
                                            "LatexEditorController",
                                            "LatexEditorView"),
                                    targetArcVertex);

                            break;
                        default:
                            Assertions.fail("The package vertex is not one of the expected ones");
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
