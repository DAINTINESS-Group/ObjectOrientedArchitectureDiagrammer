package model.exportation;

import manager.ClassDiagramManager;
import model.diagram.exportation.DiagramExporter;
import model.diagram.exportation.PlantUMLClassDiagramTextExporter;
import model.diagram.plantuml.PlantUMLClassifierVertex;
import model.diagram.plantuml.PlantUMLClassifierVertexArc;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlantUMLClassDiagramTextExporterTest {

    Path currentDirectory = Path.of(".");

    @Test
    void exportDiagramTest() {
        try {
            ClassDiagramManager classDiagramManager = new ClassDiagramManager();
            classDiagramManager.createSourceProject(Paths.get(currentDirectory.toRealPath() + "\\src\\test\\resources\\LatexEditor\\src"));
            classDiagramManager.convertTreeToDiagram(List.of("StableVersionsStrategy", "VersionsStrategy", "VersionsStrategyFactory", "VolatileVersionsStrategy",
                    "VersionsManager", "Document", "DocumentManager"));

            PlantUMLClassifierVertex plantUMLClassifierVertex = new PlantUMLClassifierVertex(classDiagramManager.getClassDiagram());
            String sinkVertexBuffer = plantUMLClassifierVertex.convertSinkVertex().toString();
            PlantUMLClassifierVertexArc plantUMLEdge = new PlantUMLClassifierVertexArc(classDiagramManager.getClassDiagram());
            String sinkVertexArcBuffer = plantUMLEdge.convertSinkVertexArc().toString();

            DiagramExporter graphMLExporter = new PlantUMLClassDiagramTextExporter(classDiagramManager.getClassDiagram());
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
