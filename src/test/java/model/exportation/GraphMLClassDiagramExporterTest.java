package model.exportation;

import manager.ClassDiagramManager;
import model.diagram.arrangement.ClassDiagramArrangementManager;
import model.diagram.arrangement.DiagramArrangementManager;
import model.diagram.exportation.DiagramExporter;
import model.diagram.exportation.GraphMLClassDiagramExporter;
import model.diagram.graphml.GraphMLClassifierVertex;
import model.diagram.graphml.GraphMLClassifierVertexArc;
import model.diagram.graphml.GraphMLSyntax;
import org.javatuples.Pair;
import org.junit.jupiter.api.Test;
import utils.PathConstructor;
import utils.PathTemplate.LatexEditor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GraphMLClassDiagramExporterTest
{

    @Test
    void exportDiagramTest()
    {
        try
        {
            ClassDiagramManager classDiagramManager = new ClassDiagramManager();
            List<String> chosenFiles = List.of("MainWindow",
                                                     "LatexEditorView",
                                                     "OpeningWindow");

            classDiagramManager.createSourceProject(LatexEditor.SRC.path);
            classDiagramManager.convertTreeToDiagram(chosenFiles);
            classDiagramManager.arrangeDiagram();

            DiagramArrangementManager          classDiagramArrangement = new ClassDiagramArrangementManager(classDiagramManager.getClassDiagram());
            Map<Integer, Pair<Double, Double>> nodesGeometry           = classDiagramArrangement.arrangeGraphMLDiagram();
            classDiagramManager.getClassDiagram().setGraphMLDiagramGeometry(nodesGeometry);

            DiagramExporter graphMLExporter = new GraphMLClassDiagramExporter(classDiagramManager.getClassDiagram());
            File exportedFile = graphMLExporter.exportDiagram(Paths.get(String.format("%s%s%s",
                                                                                      PathConstructor.getCurrentPath(),
                                                                                      File.separator,
                                                                                      PathConstructor.constructPath("src",
                                                                                                                    "test",
                                                                                                                    "resources",
                                                                                                                    "testingExportedFile.txt"))));
            Stream<String> lines              = Files.lines(exportedFile.toPath());
            String         actualFileContents = lines.collect(Collectors.joining("\n")) + "\n";
            lines.close();

            String expectedFileContents = getExpectedFileContents(classDiagramManager);
            assertEquals(expectedFileContents, actualFileContents);

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    private static String getExpectedFileContents(ClassDiagramManager classDiagramManager)
    {
        GraphMLClassifierVertex    graphMLClassifierVertex    = new GraphMLClassifierVertex();
        StringBuilder              graphMLNodeBuffer          = graphMLClassifierVertex.convertSinkVertex(classDiagramManager.getClassDiagram());
        GraphMLClassifierVertexArc graphMLClassifierVertexArc = new GraphMLClassifierVertexArc();
        StringBuilder              graphMLEdgeBuffer          = graphMLClassifierVertexArc.convertSinkVertexArc(classDiagramManager.getClassDiagram());
        return String.format("%s%s%s%s",
                             GraphMLSyntax.getInstance().getGraphMLPrefix(),
                             graphMLNodeBuffer.toString(),
                             graphMLEdgeBuffer.toString(),
                             GraphMLSyntax.getInstance().getGraphMLSuffix());
    }
}
