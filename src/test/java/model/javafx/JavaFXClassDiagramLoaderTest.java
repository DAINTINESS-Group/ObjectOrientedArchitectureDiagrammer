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

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
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
        List<String> chosenFiles                = Arrays.asList("MainWindow",
                                                                "LatexEditorView",
                                                                "OpeningWindow");

        classDiagramManager.createSourceProject(Paths.get(String.format("%s%s%s".formatted(PathConstructor.getCurrentPath(),
                                                                                           File.separator,
                                                                                           PathConstructor.constructPath("src",
                                                                                                                         "test",
                                                                                                                         "resources",
                                                                                                                         "LatexEditor",
                                                                                                                         "src")))));
        classDiagramManager.convertTreeToDiagram(chosenFiles);
        Map<ClassifierVertex, Integer> graphNodes = classDiagramManager.getClassDiagram().getGraphNodes();

        GraphClassDiagramConverter                        graphClassDiagramConverter = new GraphClassDiagramConverter(graphNodes.keySet());
        Map<ClassifierVertex, Set<Arc<ClassifierVertex>>> createdDiagram             = graphClassDiagramConverter.convertGraphToClassDiagram();

        DiagramExporter javaFXExporter = new JavaFXClassDiagramExporter(classDiagramManager.getClassDiagram());
        File            actualFile     = javaFXExporter.exportDiagram(Path.of(PathConstructor.getCurrentPath() + File.separator + PathConstructor.constructPath("src", "test", "resources", "testingExportedFile.txt")));

        JavaFXClassDiagramLoader javaFXClassDiagramLoader = new JavaFXClassDiagramLoader(actualFile.toPath());
        Set<ClassifierVertex>    loadedDiagram            = javaFXClassDiagramLoader.loadDiagram();
        assertEquals(createdDiagram.size(), loadedDiagram.size());
        for (ClassifierVertex classifierVertex : createdDiagram.keySet())
        {
            Optional<ClassifierVertex> optionalSinkVertex = loadedDiagram
                .stream()
                .filter(sinkVertex1 ->
                            sinkVertex1.getName().equals(classifierVertex.getName()) &&
                            sinkVertex1.getVertexType().equals(classifierVertex.getVertexType()))
                .findFirst();
            assertTrue(optionalSinkVertex.isPresent());

            List<Arc<ClassifierVertex>> arcs = optionalSinkVertex.get().getArcs();
            assertEquals(createdDiagram.get(classifierVertex).size(), arcs.size());
            for (Arc<ClassifierVertex> arc : createdDiagram.get(classifierVertex))
            {
                assertTrue(arcs
                               .stream()
                               .anyMatch(sinkVertexArc ->
                                             sinkVertexArc.sourceVertex().getName().equals(arc.sourceVertex().getName()) &&
                                             sinkVertexArc.targetVertex().getName().equals(arc.targetVertex().getName()) &&
                                             sinkVertexArc.arcType().equals(arc.arcType())));
            }

            List<ClassifierVertex.Method> methods = optionalSinkVertex.get().getMethods();
            assertEquals(classifierVertex.getMethods().size(), methods.size());
            for (ClassifierVertex.Method method : classifierVertex.getMethods())
            {
                assertTrue(methods
                               .stream()
                               .anyMatch(method1 ->
                                             method1.name().equals(method.name()) &&
                                             method1.returnType().equals(method.returnType()) &&
                                             method1.modifier().equals(method.modifier())));
            }

            List<ClassifierVertex.Field> fields = optionalSinkVertex.get().getFields();
            assertEquals(classifierVertex.getFields().size(), fields.size());
            for (ClassifierVertex.Field field : classifierVertex.getFields())
            {
                assertTrue(fields
                               .stream()
                               .anyMatch(field1 ->
                                             field1.name().equals(field.name()) &&
                                             field1.type().equals(field.type()) &&
                                             field1.modifier().equals(field.modifier())));
            }
        }
    }
}
