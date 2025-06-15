package model.javafx;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import manager.ClassDiagramManager;
import model.diagram.GraphClassDiagramConverter;
import model.diagram.exportation.DiagramExporter;
import model.diagram.exportation.JavaFXClassDiagramExporter;
import model.diagram.javafx.JavaFXClassDiagramLoader;
import model.graph.Arc;
import model.graph.ClassifierVertex;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import utils.PathConstructor;
import utils.PathTemplate.LatexEditor;

public class JavaFXClassDiagramLoaderTest {

    @Test
    @Disabled
    void loadDiagramTest() {
        ClassDiagramManager classDiagramManager = new ClassDiagramManager();
        List<String> chosenFiles = List.of("MainWindow", "LatexEditorView", "OpeningWindow");

        classDiagramManager.createSourceProject(LatexEditor.SRC.path);
        classDiagramManager.convertTreeToDiagram(chosenFiles);
        Map<ClassifierVertex, Integer> graphNodes =
                classDiagramManager.getClassDiagram().getGraphNodes();

        GraphClassDiagramConverter graphClassDiagramConverter =
                new GraphClassDiagramConverter(graphNodes.keySet());
        Map<ClassifierVertex, Set<Arc<ClassifierVertex>>> createdDiagram =
                graphClassDiagramConverter.convertGraphToClassDiagram();

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

        Set<ClassifierVertex> loadedDiagram =
                JavaFXClassDiagramLoader.loadDiagram(actualFile.toPath());
        assertEquals(createdDiagram.size(), loadedDiagram.size());
        for (ClassifierVertex classifierVertex : createdDiagram.keySet()) {
            ClassifierVertex vertex =
                    loadedDiagram.stream()
                            .filter(getClassifierVertexPredicate(classifierVertex))
                            .findFirst()
                            .orElseGet(Assertions::fail);

            Set<Arc<ClassifierVertex>> arcs = vertex.getArcs();
            assertEquals(createdDiagram.get(classifierVertex).size(), arcs.size());
            for (Arc<ClassifierVertex> arc : createdDiagram.get(classifierVertex)) {
                assertTrue(arcs.stream().anyMatch(getArcPredicate(arc)));
            }

            Set<ClassifierVertex.Method> methods = vertex.getMethods();
            assertEquals(classifierVertex.getMethods().size(), methods.size());
            for (ClassifierVertex.Method method : classifierVertex.getMethods()) {
                assertTrue(methods.stream().anyMatch(getMethodPredicate(method)));
            }

            Set<ClassifierVertex.Field> fields = vertex.getFields();
            assertEquals(classifierVertex.getFields().size(), fields.size());
            for (ClassifierVertex.Field field : classifierVertex.getFields()) {
                assertTrue(fields.stream().anyMatch(getFieldPredicate(field)));
            }
        }
    }

    @NotNull
    private static Predicate<ClassifierVertex.Field> getFieldPredicate(
            ClassifierVertex.Field field) {
        return it ->
                it.name().equals(field.name())
                        && it.type().equals(field.type())
                        && it.modifier().equals(field.modifier());
    }

    @NotNull
    private static Predicate<ClassifierVertex.Method> getMethodPredicate(
            ClassifierVertex.Method method) {
        return it ->
                it.name().equals(method.name())
                        && it.returnType().equals(method.returnType())
                        && it.modifier().equals(method.modifier());
    }

    @NotNull
    private static Predicate<ClassifierVertex> getClassifierVertexPredicate(
            ClassifierVertex classifierVertex) {
        return it ->
                it.getName().equals(classifierVertex.getName())
                        && it.getVertexType().equals(classifierVertex.getVertexType());
    }

    @NotNull
    private static Predicate<Arc<ClassifierVertex>> getArcPredicate(Arc<ClassifierVertex> arc) {
        return it ->
                it.sourceVertex().getName().equals(arc.sourceVertex().getName())
                        && it.targetVertex().getName().equals(arc.targetVertex().getName())
                        && it.arcType().equals(arc.arcType());
    }
}
