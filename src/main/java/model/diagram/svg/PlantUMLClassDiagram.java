package model.diagram.svg;

import model.diagram.ClassDiagram;
import model.diagram.plantuml.PlantUMLClassifierVertex;
import model.diagram.plantuml.PlantUMLClassifierVertexArc;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class PlantUMLClassDiagram
{

    private final StringBuilder bufferBody;


    public PlantUMLClassDiagram(ClassDiagram diagram)
    {
        PlantUMLClassifierVertex    plantUMLClassifierVertex = new PlantUMLClassifierVertex();
        StringBuilder               plantUMLNodeBuffer       = plantUMLClassifierVertex.convertSinkVertex(diagram);
        PlantUMLClassifierVertexArc plantUMLEdge             = new PlantUMLClassifierVertexArc();
        StringBuilder               plantUMLEdgeBuffer       = plantUMLEdge.convertSinkVertexArc(diagram);
        bufferBody = plantUMLNodeBuffer
            .append("\n\n")
            .append(plantUMLEdgeBuffer)
            .append("\n @enduml");
    }

    public String toSvg(int dpi)
    {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream())
        {
            String plantUMLCode       = getClassText(dpi) + bufferBody;
            SourceStringReader reader = new SourceStringReader(plantUMLCode);
            reader.outputImage(byteArrayOutputStream, new FileFormatOption(FileFormat.SVG)).getDescription();
            return byteArrayOutputStream.toString(StandardCharsets.UTF_8);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }


    private static String getClassText(int dpi)
    {
        return "@startuml\n" +
               (dpi > 0 ? "skinparam dpi " + dpi : "") + "\n" +
               // TODO: Determine the best value for this.
               "skinparam wrapWidth 10" + "\n" +
               "skinparam class {\n" +
               "    BackgroundColor lightyellow\n" +
               "    BorderColor black\n" +
               "    ArrowColor black\n" +
               "}\n" +
               "\n";
    }
}
