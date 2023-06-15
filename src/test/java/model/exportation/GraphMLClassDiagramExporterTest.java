package model.exportation;

import manager.ClassDiagramManager;
import manager.SourceProject;
import model.diagram.exportation.DiagramExporter;
import model.diagram.arrangement.ClassDiagramArrangement;
import model.diagram.arrangement.DiagramArrangement;
import model.diagram.exportation.GraphMLClassDiagramExporter;
import model.diagram.graphml.GraphMLSinkVertex;
import model.diagram.graphml.GraphMLSinkVertexArc;
import model.diagram.graphml.GraphMLSyntax;
import model.graph.Arc;
import model.graph.SinkVertex;
import org.javatuples.Pair;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GraphMLClassDiagramExporterTest {

    Path currentDirectory = Path.of(".");

    @Test
    void exportDiagramTest() {
        try {
            ClassDiagramManager classDiagramManager = new ClassDiagramManager();
            List<String> chosenFiles = Arrays.asList("MainWindow", "LatexEditorView", "OpeningWindow");
            SourceProject sourceProject = classDiagramManager.createSourceProject(Paths.get(currentDirectory.toRealPath() + "\\src\\test\\resources\\LatexEditor\\src"));
            classDiagramManager.convertTreeToDiagram(chosenFiles);
            classDiagramManager.arrangeDiagram();
            Map<SinkVertex, Set<Arc<SinkVertex>>> diagram = classDiagramManager.getDiagram();

            Map<SinkVertex, Integer> graphNodes = classDiagramManager.getClassDiagram().getGraphNodes();
            DiagramArrangement classDiagramArrangement = new ClassDiagramArrangement(graphNodes, diagram);
            Map<Integer, Pair<Double, Double>> nodesGeometry = classDiagramArrangement.arrangeDiagram();
            DiagramExporter graphMLExporter = new GraphMLClassDiagramExporter(graphNodes, nodesGeometry, diagram);
            File exportedFile = graphMLExporter.exportDiagram(Paths.get(System.getProperty("user.home") + "\\testingExportedFile.graphML"));
            Stream<String> lines = Files.lines(exportedFile.toPath());
            String actualFileContents = lines.collect(Collectors.joining("\n"));
            lines.close();

            GraphMLSinkVertex graphMLSinkVertex = new GraphMLSinkVertex(graphNodes, nodesGeometry);
            StringBuilder graphMLNodeBuffer = graphMLSinkVertex.convertSinkVertex();
            GraphMLSinkVertexArc graphMLSinkVertexArc = new GraphMLSinkVertexArc(graphNodes, diagram);
            StringBuilder graphMLEdgeBuffer = graphMLSinkVertexArc.convertSinkVertexArc();
            String expectedFileContents = "";
            expectedFileContents += (GraphMLSyntax.getInstance().getGraphMLPrefix());
            expectedFileContents += (graphMLNodeBuffer.toString());
            expectedFileContents += (graphMLEdgeBuffer.toString());
            expectedFileContents += (GraphMLSyntax.getInstance().getGraphMLSuffix());

            assertEquals(expectedFileContents, actualFileContents);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
