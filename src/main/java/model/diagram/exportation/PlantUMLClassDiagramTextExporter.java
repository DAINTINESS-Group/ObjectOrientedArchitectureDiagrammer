package model.diagram.exportation;

import model.diagram.ClassDiagram;
import model.diagram.plantuml.PlantUMLClassifierVertex;
import model.diagram.plantuml.PlantUMLClassifierVertexArc;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

public class PlantUMLClassDiagramTextExporter implements DiagramExporter
{

    private final StringBuilder bufferBody;


    public PlantUMLClassDiagramTextExporter(ClassDiagram diagram)
    {
        PlantUMLClassifierVertex    plantUMLClassifierVertex = new PlantUMLClassifierVertex(diagram);
        StringBuilder               plantUMLNodeBuffer       = plantUMLClassifierVertex.convertSinkVertex();
        PlantUMLClassifierVertexArc plantUMLEdge             = new PlantUMLClassifierVertexArc(diagram);
        StringBuilder               plantUMLEdgeBuffer       = plantUMLEdge.convertSinkVertexArc();
        bufferBody = plantUMLNodeBuffer
            .append("\n\n")
            .append(plantUMLEdgeBuffer)
            .append("\n @enduml");
    }


    @Override
    public File exportDiagram(Path exportPath)
    {
        File   plantUMLFile = exportPath.toFile();
        String plantUMLCode = getClassText();
        plantUMLCode += bufferBody;
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
            e.printStackTrace();
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
