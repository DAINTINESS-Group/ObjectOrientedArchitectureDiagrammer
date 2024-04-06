package model.diagram.exportation;

import model.diagram.ClassDiagram;
import model.diagram.plantuml.PlantUMLClassifierVertex;
import model.diagram.plantuml.PlantUMLClassifierVertexArc;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

public class PlantUMLClassDiagramTextExporter implements DiagramExporter
{
    private static final Logger logger = LogManager.getLogger(PlantUMLClassDiagramTextExporter.class);

    private final StringBuilder bufferBody;


    public PlantUMLClassDiagramTextExporter(ClassDiagram classDiagram)
    {
        PlantUMLClassifierVertex    plantUMLClassifierVertex = new PlantUMLClassifierVertex();
        StringBuilder               plantUMLNodeBuffer       = plantUMLClassifierVertex.convertSinkVertex(classDiagram);
        PlantUMLClassifierVertexArc plantUMLEdge             = new PlantUMLClassifierVertexArc();
        StringBuilder               plantUMLEdgeBuffer       = plantUMLEdge.convertSinkVertexArc(classDiagram);
        bufferBody = plantUMLNodeBuffer
            .append("\n\n")
            .append(plantUMLEdgeBuffer)
            .append("\n @enduml");
    }


    @Override
    public File exportDiagram(Path exportPath)
    {
        File   plantUMLFile = exportPath.toFile();
        String plantUMLCode = getClassText() + bufferBody;
        writeFile(plantUMLFile, plantUMLCode);
        return plantUMLFile;
    }


    private void writeFile(File plantUMLFile, String plantCode)
    {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(plantUMLFile)))
        {
            writer.write(plantCode);
        }
        catch (IOException e)
        {
            logger.error("Failed to write to file: {}", plantUMLFile.getAbsolutePath());
            throw new RuntimeException(e);
        }
    }


    private String getClassText()
    {
        return """
            @startuml
            skinparam class {
                BackgroundColor lightyellow
                BorderColor black
                ArrowColor black
            }

            """;
    }
}
