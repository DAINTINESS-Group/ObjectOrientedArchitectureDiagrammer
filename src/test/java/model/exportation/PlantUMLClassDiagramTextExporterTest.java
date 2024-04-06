package model.exportation;

import manager.ClassDiagramManager;
import model.diagram.exportation.DiagramExporter;
import model.diagram.exportation.PlantUMLClassDiagramTextExporter;
import model.diagram.plantuml.PlantUMLClassifierVertex;
import model.diagram.plantuml.PlantUMLClassifierVertexArc;
import org.junit.jupiter.api.Test;
import utils.PathConstructor;
import utils.PathTemplate.LatexEditor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlantUMLClassDiagramTextExporterTest
{

    @Test
    void exportDiagramTest()
    {
        try
        {
            ClassDiagramManager classDiagramManager = new ClassDiagramManager();
            classDiagramManager.createSourceProject(LatexEditor.SRC.path);
            classDiagramManager.convertTreeToDiagram(List.of("StableVersionsStrategy",
                                                             "VersionsStrategy",
                                                             "VersionsStrategyFactory",
                                                             "VolatileVersionsStrategy",
                                                             "VersionsManager",
                                                             "Document",
                                                             "DocumentManager"));

            String sinkVertexBuffer    = PlantUMLClassifierVertex.convertSinkVertices(classDiagramManager.getClassDiagram()).toString();
            String sinkVertexArcBuffer = PlantUMLClassifierVertexArc.convertSinkVertexArcs(classDiagramManager.getClassDiagram()).toString();
            DiagramExporter             graphMLExporter          = new PlantUMLClassDiagramTextExporter(classDiagramManager.getClassDiagram());
            File exportedFile = graphMLExporter.exportDiagram(Paths.get(String.format("%s%s%s",
                                                                                      PathConstructor.getCurrentPath(),
                                                                                      File.separator,
                                                                                      PathConstructor.constructPath("src",
                                                                                                                    "test",
                                                                                                                    "resources",
                                                                                                                    "testingExportedFile.txt"))));
            Stream<String> lines              = Files.lines(exportedFile.toPath());
            String         actualFileContents = lines.collect(Collectors.joining("\n"));
            lines.close();

            String expectedFileContents = """
                @startuml
                skinparam class {
                    BackgroundColor lightyellow
                    BorderColor black
                    ArrowColor black
                }

                """;

            expectedFileContents += sinkVertexBuffer +
                                    "\n\n" +
                                    sinkVertexArcBuffer +
                                    "\n @enduml";
            assertEquals(expectedFileContents, actualFileContents);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

}
