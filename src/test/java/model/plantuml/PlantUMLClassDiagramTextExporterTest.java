package model.plantuml;

import manager.ClassDiagramManager;
import manager.SourceProject;
import model.diagram.DiagramExporter;
import model.diagram.plantuml.PlantUMLClassDiagramTextExporter;
import model.diagram.plantuml.PlantUMLSinkVertex;
import model.diagram.plantuml.PlantUMLSinkVertexArc;
import model.graph.Arc;
import model.graph.SinkVertex;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlantUMLClassDiagramTextExporterTest {

    Path currentDirectory = Path.of(".");

    @Test
    void exportDiagramTest() {
        try {
            ClassDiagramManager classDiagramManager = new ClassDiagramManager();
            SourceProject sourceProject = classDiagramManager.createSourceProject(Paths.get(currentDirectory.toRealPath() + "\\src\\test\\resources\\LatexEditor\\src"));
            classDiagramManager.createDiagram(List.of("StableVersionsStrategy", "VersionsStrategy", "VersionsStrategyFactory", "VolatileVersionsStrategy",
                    "VersionsManager", "Document", "DocumentManager"));

            Map<SinkVertex, Integer> graphNodes = classDiagramManager.getDiagram().getGraphNodes();
            PlantUMLSinkVertex plantUMLSinkVertex = new PlantUMLSinkVertex(graphNodes);
            String sinkVertexBuffer = plantUMLSinkVertex.convertSinkVertex().toString();
            Map<Arc<SinkVertex>, Integer> graphEdges = classDiagramManager.getDiagram().getGraphEdges();
            PlantUMLSinkVertexArc plantUMLEdge = new PlantUMLSinkVertexArc(graphEdges);
            String sinkVertexArcBuffer = plantUMLEdge.convertSinkVertexArc().toString();

            DiagramExporter graphMLExporter = new PlantUMLClassDiagramTextExporter(graphNodes, graphEdges);
            File exportedFile = graphMLExporter.exportDiagram(Paths.get(System.getProperty("user.home") + "\\testingExportedFile.txt"));
            Stream<String> lines = Files.lines(exportedFile.toPath());
            String actualFileContents = lines.collect(Collectors.joining("\n"));
            lines.close();

            String expectedFileContents = "@startuml\n" +
                    "skinparam class {\n" +
                    "    BackgroundColor lightyellow\n" +
                    "    BorderColor black\n" +
                    "    ArrowColor black\n" +
                    "}\n";
            expectedFileContents += sinkVertexBuffer + sinkVertexArcBuffer + "@enduml";

            assertEquals(expectedFileContents, actualFileContents);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
