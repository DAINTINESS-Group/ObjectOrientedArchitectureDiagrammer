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
import java.util.Set;
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
            classDiagramManager.convertTreeToDiagram(List.of("StableVersionsStrategy", "VersionsStrategy", "VersionsStrategyFactory", "VolatileVersionsStrategy",
                    "VersionsManager", "Document", "DocumentManager"));

            Map<SinkVertex, Set<Arc<SinkVertex>>> diagram = classDiagramManager.getDiagram();
            Map<SinkVertex, Integer> graphNodes = classDiagramManager.getClassDiagram().getGraphNodes();
            PlantUMLSinkVertex plantUMLSinkVertex = new PlantUMLSinkVertex(diagram);
            String sinkVertexBuffer = plantUMLSinkVertex.convertSinkVertex().toString();
            PlantUMLSinkVertexArc plantUMLEdge = new PlantUMLSinkVertexArc(diagram);
            String sinkVertexArcBuffer = plantUMLEdge.convertSinkVertexArc().toString();

            DiagramExporter graphMLExporter = new PlantUMLClassDiagramTextExporter(diagram);
            File exportedFile = graphMLExporter.exportDiagram(Paths.get(System.getProperty("user.home") + "\\testingExportedFile.txt"));
            Stream<String> lines = Files.lines(exportedFile.toPath());
            String actualFileContents = lines.collect(Collectors.joining("\n"));
            lines.close();

            String expectedFileContents = "@startuml\n" +
                    "skinparam class {\n" +
                    "    BackgroundColor lightyellow\n" +
                    "    BorderColor black\n" +
                    "    ArrowColor black\n" +
                    "}\n\n";
            expectedFileContents += sinkVertexBuffer + "\n\n" + sinkVertexArcBuffer + "\n @enduml";

            assertEquals(expectedFileContents, actualFileContents);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
