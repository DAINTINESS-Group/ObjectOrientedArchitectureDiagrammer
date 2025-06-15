package model.diagram.svg;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import model.diagram.ClassDiagram;
import model.diagram.plantuml.PlantUMLClassifierVertex;
import model.diagram.plantuml.PlantUMLClassifierVertexArc;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;

public class PlantUMLClassDiagram {

    private final StringBuilder bufferBody;

    public PlantUMLClassDiagram(ClassDiagram diagram) {
        StringBuilder plantUMLNodeBuffer = PlantUMLClassifierVertex.convertSinkVertices(diagram);
        StringBuilder plantUMLEdgeBuffer =
                PlantUMLClassifierVertexArc.convertSinkVertexArcs(diagram);
        bufferBody =
                plantUMLNodeBuffer
                        .append(System.lineSeparator())
                        .append(System.lineSeparator())
                        .append(plantUMLEdgeBuffer)
                        .append(System.lineSeparator())
                        .append(" @enduml");
    }

    public String toSvg(int dpi) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            String plantUMLCode = getClassText(dpi) + bufferBody;
            SourceStringReader reader = new SourceStringReader(plantUMLCode);
            reader.outputImage(byteArrayOutputStream, new FileFormatOption(FileFormat.SVG))
                    .getDescription();
            return byteArrayOutputStream.toString(StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String getClassText(int dpi) {
        return "@startuml"
                + System.lineSeparator()
                + (dpi > 0 ? "skinparam dpi " + dpi : "")
                + System.lineSeparator()
                +
                // TODO: Determine the best value for this.
                "skinparam wrapWidth 10"
                + System.lineSeparator()
                + "skinparam class {"
                + System.lineSeparator()
                + "    BackgroundColor lightyellow"
                + System.lineSeparator()
                + "    BorderColor black"
                + System.lineSeparator()
                + "    ArrowColor black"
                + System.lineSeparator()
                + "}"
                + System.lineSeparator()
                + System.lineSeparator();
    }
}
