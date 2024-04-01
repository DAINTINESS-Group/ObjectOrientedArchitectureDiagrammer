package model.diagram.svg;

import model.diagram.PackageDiagram;
import model.diagram.plantuml.PlantUMLPackageVertex;
import model.diagram.plantuml.PlantUMLPackageVertexArc;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class PlantUMLPackageDiagram
{

    private final StringBuilder bufferBody;

    public PlantUMLPackageDiagram(PackageDiagram packageDiagram)
    {
        PlantUMLPackageVertex plantUMLPackageVertex = new PlantUMLPackageVertex(packageDiagram);
        StringBuilder         plantUMLNodeBuffer    = plantUMLPackageVertex.convertVertex();
        PlantUMLPackageVertexArc plantUMLPackageVertexArc = new PlantUMLPackageVertexArc(packageDiagram);
        StringBuilder plantUMLEdgeBuffer = plantUMLPackageVertexArc.convertVertexArc();

        bufferBody = plantUMLNodeBuffer
            .append("\n\n")
            .append(plantUMLEdgeBuffer)
            .append("\n @enduml");
    }


    public String toSvg(int dpi)
    {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream())
        {
            String             plantUMLCode = getPackageText(dpi) + bufferBody;
            SourceStringReader reader       = new SourceStringReader(replaceDots(plantUMLCode));
            reader.outputImage(byteArrayOutputStream, new FileFormatOption(FileFormat.SVG)).getDescription();
            return byteArrayOutputStream.toString(StandardCharsets.UTF_8);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }


    private static String replaceDots(String plantUMLCode)
    {
        StringBuilder newString = new StringBuilder();
        String[]      lines     = plantUMLCode.split("\n");
        for (String line : lines)
        {
            String[] split = line.split(" ");
            for (String word : split)
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


    private static String getPackageText(int dpi)
    {
        return "@startuml\n" +
               (dpi > 0 ? "skinparam dpi " + dpi : "") + "\n" +
               // TODO: Determine the best value for this.
               "skinparam wrapWidth 10" + "\n" +
               "skinparam package {\n" +
               "    BackgroundColor lightyellow\n" +
               "    BorderColor black\n" +
               "    ArrowColor black\n" +
               "    Shadowing true\n" +
               "}\n" +
               "\n";
    }
}
