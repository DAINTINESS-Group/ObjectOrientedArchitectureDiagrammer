package model.javafx;

import manager.ClassDiagramManager;
import model.diagram.GraphClassDiagramConverter;
import model.diagram.exportation.DiagramExporter;
import model.diagram.exportation.JavaFXClassDiagramExporter;
import model.diagram.javafx.JavaFXClassDiagramLoader;
import model.graph.Arc;
import model.graph.ClassifierVertex;
import org.junit.jupiter.api.Test;
import utils.PathConstructor;
import utils.PathTemplate.LatexEditor;

import java.io.File;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JavaFXClassDiagramLoaderTest
{

    @Test
    void loadDiagramTest()
    {
        ClassDiagramManager classDiagramManager = new ClassDiagramManager();
        List<String> chosenFiles                = List.of("MainWindow",
                                                          "LatexEditorView",
                                                          "OpeningWindow");

        classDiagramManager.createSourceProject(LatexEditor.SRC.path);
        classDiagramManager.convertTreeToDiagram(chosenFiles);
        Map<ClassifierVertex, Integer> graphNodes = classDiagramManager.getClassDiagram().getGraphNodes();

        GraphClassDiagramConverter                        graphClassDiagramConverter = new GraphClassDiagramConverter(graphNodes.keySet());
        Map<ClassifierVertex, Set<Arc<ClassifierVertex>>> createdDiagram             = graphClassDiagramConverter.convertGraphToClassDiagram();

        DiagramExporter javaFXExporter = new JavaFXClassDiagramExporter(classDiagramManager.getClassDiagram());
        File            actualFile     = javaFXExporter.exportDiagram(Path.of(String.format("%s%s%s",
                                                                                            PathConstructor.getCurrentPath(),
                                                                                            File.separator,
                                                                                            PathConstructor.constructPath("src",
                                                                                                                          "test",
                                                                                                                          "resources",
                                                                                                                          "testingExportedFile.txt"))));

        Set<ClassifierVertex> loadedDiagram = JavaFXClassDiagramLoader.loadDiagram(actualFile.toPath());
        assertEquals(createdDiagram.size(), loadedDiagram.size());
        for (ClassifierVertex classifierVertex : createdDiagram.keySet())
        {
            Optional<ClassifierVertex> optionalSinkVertex = loadedDiagram.stream()
                .filter(it -> it.getName().equals(classifierVertex.getName()) &&
                              it.getVertexType().equals(classifierVertex.getVertexType()))
                .findFirst();
            assertTrue(optionalSinkVertex.isPresent());

            List<Arc<ClassifierVertex>> arcs = optionalSinkVertex.get().getArcs();
            assertEquals(createdDiagram.get(classifierVertex).size(), arcs.size());
            for (Arc<ClassifierVertex> arc : createdDiagram.get(classifierVertex))
            {
                assertTrue(arcs.stream()
                               .anyMatch(it -> it.sourceVertex().getName().equals(arc.sourceVertex().getName()) &&
                                               it.targetVertex().getName().equals(arc.targetVertex().getName()) &&
                                               it.arcType().equals(arc.arcType())));
            }

            List<ClassifierVertex.Method> methods = optionalSinkVertex.get().getMethods();
            assertEquals(classifierVertex.getMethods().size(), methods.size());
            for (ClassifierVertex.Method method : classifierVertex.getMethods())
            {
                assertTrue(methods.stream()
                               .anyMatch(it -> it.name().equals(method.name())             &&
                                               it.returnType().equals(method.returnType()) &&
                                               it.modifier().equals(method.modifier())));
            }

            List<ClassifierVertex.Field> fields = optionalSinkVertex.get().getFields();
            assertEquals(classifierVertex.getFields().size(), fields.size());
            for (ClassifierVertex.Field field : classifierVertex.getFields())
            {
                assertTrue(fields.stream()
                               .anyMatch(it -> it.name().equals(field.name()) &&
                                               it.type().equals(field.type()) &&
                                               it.modifier().equals(field.modifier())));
            }
        }
    }
}
