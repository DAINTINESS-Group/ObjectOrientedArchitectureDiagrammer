package model.diagram.exportation;

import model.diagram.PackageDiagram;
import model.diagram.plantuml.PlantUMLPackageVertex;
import model.diagram.plantuml.PlantUMLPackageVertexArc;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

public class PlantUMLPackageDiagramTextExporter implements DiagramExporter
{

    private static final Logger logger = LogManager.getLogger(PlantUMLPackageDiagramTextExporter.class);

    private final StringBuilder bufferBody;


    public PlantUMLPackageDiagramTextExporter(PackageDiagram diagram)
    {
        StringBuilder plantUMLNodeBuffer = PlantUMLPackageVertex.convertVertices(diagram);
        StringBuilder plantUMLEdgeBuffer = PlantUMLPackageVertexArc.convertVertexArcs(diagram);
        bufferBody = plantUMLNodeBuffer
            .append("\n\n")
            .append(plantUMLEdgeBuffer)
            .append("\n @enduml");
    }


    @Override
    public File exportDiagram(Path exportPath)
    {
        File   plantUMLFile = exportPath.toFile();
        String plantUMLCode = getPackageText() + bufferBody;
        writeFile(plantUMLFile, dotChanger(plantUMLCode));
        return plantUMLFile;
    }


    private static void writeFile(File plantUMLFile, String plantCode)
    {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(plantUMLFile)))
        {
            writer.write(plantCode);
        }
        catch (IOException e)
        {
            logger.error("Failed to write file {}", plantUMLFile.getName());
            throw new RuntimeException(e);
        }
    }


    private static String dotChanger(String plantUMLCode)
    {
        StringBuilder newString = new StringBuilder();
        String[]      lines     = plantUMLCode.split("\n");
        for (String line : lines)
        {
            String[] words = line.split(" ");
            for (String word : words)
            {
                String newWord = word;
                if (word.contains(".") && !word.contains(".."))
                {
                    newWord = word.replace(".", "_");
                    newWord = newWord.replace("-", "_");
                }
                newString.append(newWord).append(" ");
            }
            newString.append("\n");
        }
        return newString.toString();
    }


    private String getPackageText()
    {
        return """
            @startuml
            skinparam package {
                BackgroundColor lightyellow
                BorderColor black
                ArrowColor black
                Shadowing true
            }

            """;
    }
}
