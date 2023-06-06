package model.javafx;

import manager.ClassDiagramManager;
import model.diagram.DiagramExporter;
import model.diagram.javafx.classdiagram.JavaFXClassDiagramExporter;
import model.diagram.javafx.classdiagram.JavaFXClassDiagramLoader;
import model.graph.Arc;
import model.graph.SinkVertex;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JavaFXClassDiagramLoaderTest {

    Path currentDirectory = Path.of(".");

    @Test
    void loadDiagramTest() {
        try {
            ClassDiagramManager classDiagramManager = new ClassDiagramManager();
            List<String> chosenFiles = Arrays.asList("MainWindow", "LatexEditorView", "OpeningWindow");
            classDiagramManager.createSourceProject(Paths.get(currentDirectory.toRealPath() + "\\src\\test\\resources\\LatexEditor\\src"));
            classDiagramManager.createDiagram(chosenFiles);
            Map<SinkVertex, Set<Arc<SinkVertex>>> createdDiagram = classDiagramManager.getCreatedDiagram();

            DiagramExporter javaFXExporter = new JavaFXClassDiagramExporter(createdDiagram);
            File actualFile = javaFXExporter.exportDiagram(Path.of(System.getProperty("user.home") + "\\testingExportedFile.txt"));

            JavaFXClassDiagramLoader javaFXClassDiagramLoader = new JavaFXClassDiagramLoader(actualFile.toPath());
            Set<SinkVertex> loadedDiagram = javaFXClassDiagramLoader.loadDiagram();

            assertEquals(createdDiagram.size(), loadedDiagram.size());
            for (SinkVertex sinkVertex: createdDiagram.keySet()) {
                Optional<SinkVertex> optionalSinkVertex = loadedDiagram.stream()
                    .filter(
                        sinkVertex1 -> sinkVertex1.getName().equals(sinkVertex.getName()) &&
                        sinkVertex1.getVertexType().equals(sinkVertex.getVertexType()) &&
                        sinkVertex1.getPath().equals(sinkVertex.getPath())
                    ).findFirst();
                assertTrue(optionalSinkVertex.isPresent());

                List<Arc<SinkVertex>> arcs = optionalSinkVertex.get().getArcs();
                assertEquals(createdDiagram.get(sinkVertex).size(), arcs.size());
                for (Arc<SinkVertex> arc: createdDiagram.get(sinkVertex)) {
                    arcs.stream().filter(sinkVertexArc ->
                        sinkVertexArc.getSourceVertex().getName().equals(arc.getSourceVertex().getName()) &&
                        sinkVertexArc.getTargetVertex().getName().equals(arc.getTargetVertex().getName()) &&
                        sinkVertexArc.getArcType().equals(arc.getArcType()))
                    .findFirst().orElseGet(Assertions::fail);
                }

                List<SinkVertex.Method> methods = optionalSinkVertex.get().getMethods();
                assertEquals(sinkVertex.getMethods().size(), methods.size());
                for (SinkVertex.Method method: sinkVertex.getMethods()) {
                    methods.stream().filter(method1 ->
                        method1.getName().equals(method.getName()) &&
                        method1.getReturnType().equals(method.getReturnType()) &&
                        method1.getModifierType().equals(method.getModifierType()))
                    .findFirst().orElseGet(Assertions::fail);
                }

                List<SinkVertex.Field> fields = optionalSinkVertex.get().getFields();
                assertEquals(sinkVertex.getFields().size(), fields.size());
                for (SinkVertex.Field field: sinkVertex.getFields()) {
                    fields.stream().filter(field1 ->
                        field1.getName().equals(field.getName()) &&
                        field1.getType().equals(field.getType()) &&
                        field1.getModifier().equals(field.getModifier()))
                    .findFirst().orElseGet(Assertions::fail);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
