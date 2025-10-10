package gr.uoi.ooad.model.exportation;

import static gr.uoi.ooad.utils.JsonUtils.getVertices;
import static gr.uoi.ooad.utils.ListUtils.assertListsEqual;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import gr.uoi.ooad.manager.PackageDiagramManager;
import gr.uoi.ooad.model.diagram.exportation.DiagramExporter;
import gr.uoi.ooad.model.diagram.exportation.JavaFXPackageDiagramExporter;
import gr.uoi.ooad.model.diagram.javafx.PackageVertexDeserializer;
import gr.uoi.ooad.model.graph.ClassifierVertex;
import gr.uoi.ooad.model.graph.PackageVertex;
import gr.uoi.ooad.model.graph.VertexType;
import gr.uoi.ooad.utils.PathConstructor;
import gr.uoi.ooad.utils.PathTemplate.LatexEditor;
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
import java.util.stream.Collectors;
import org.javatuples.Triplet;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class JavaFXPackageDiagramExporterTest {

    @Test
    void exportDiagramTest() {
        try {
            PackageDiagramManager packageDiagramManager = new PackageDiagramManager();
            packageDiagramManager.createSourceProject(LatexEditor.SRC.path);
            packageDiagramManager.convertTreeToDiagram(Arrays.asList("src.view", "src.model"));

            DiagramExporter javaFXExporter =
                    new JavaFXPackageDiagramExporter(packageDiagramManager.getPackageDiagram());
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

                List<PackageVertex> actualPackageVertices =
                        getVertices(
                                jsonElements, PackageVertex.class, new PackageVertexDeserializer());

                for (PackageVertex packageVertex : actualPackageVertices) {
                    Path actualPath = packageVertex.getPath();
                    VertexType actualVertexType = packageVertex.getVertexType();
                    PackageVertex actualParent = packageVertex.getParentVertex();

                    List<ClassifierVertex> packageVertexSinkVertices =
                            packageVertex.getSinkVertices();
                    List<String> sinkVerticesNames =
                            packageVertexSinkVertices.stream()
                                    .map(ClassifierVertex::getName)
                                    .collect(Collectors.toCollection(LinkedList::new));

                    List<PackageVertex> neighbourVertices = packageVertex.getNeighbourVertices();
                    List<String> packageVertexNames =
                            neighbourVertices.stream()
                                    .map(PackageVertex::getName)
                                    .collect(Collectors.toCollection(LinkedList::new));

                    List<Triplet<String, String, String>> deserializedArcs =
                            packageVertex.getDeserializedArcs();
                    List<String> targetArcVertex =
                            deserializedArcs.stream()
                                    .map(Triplet::getValue1)
                                    .collect(Collectors.toCollection(LinkedList::new));

                    switch (packageVertex.getName()) {
                        case "src.model":
                            assertEquals(LatexEditor.MODEL.path, actualPath);
                            assertEquals(actualVertexType, VertexType.PACKAGE);
                            assertNull(actualParent);

                            assertListsEqual(
                                    sinkVerticesNames,
                                    Arrays.asList(
                                            "VersionsManager", "DocumentManager", "Document"));

                            assertThat(packageVertexNames, is(List.of("src.model.strategies")));

                            assertListsEqual(
                                    targetArcVertex,
                                    Arrays.asList("src.view", "src.model.strategies"));

                            break;
                        case "src.view":
                            assertEquals(LatexEditor.VIEW.path, actualPath);
                            assertEquals(actualVertexType, VertexType.PACKAGE);
                            assertNull(actualParent);

                            assertEquals(neighbourVertices.size(), 0);

                            assertListsEqual(
                                    sinkVerticesNames,
                                    Arrays.asList(
                                            "LatexEditorView",
                                            "MainWindow",
                                            "ChooseTemplate",
                                            "OpeningWindow"));

                            assertListsEqual(
                                    targetArcVertex,
                                    Arrays.asList(
                                            "src.controller", "src.model", "src.model.strategies"));

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
