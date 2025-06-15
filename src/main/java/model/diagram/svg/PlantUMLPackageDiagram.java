package model.diagram.svg;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import model.diagram.PackageDiagram;
import model.diagram.plantuml.PlantUMLPackageVertex;
import model.diagram.plantuml.PlantUMLPackageVertexArc;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PlantUMLPackageDiagram {

    private static final Logger logger = LogManager.getLogger(PlantUMLPackageDiagram.class);

    private final StringBuilder bufferBody;

    public PlantUMLPackageDiagram(PackageDiagram packageDiagram) {
        StringBuilder plantUMLNodeBuffer = PlantUMLPackageVertex.convertVertices(packageDiagram);
        StringBuilder plantUMLEdgeBuffer =
                PlantUMLPackageVertexArc.convertVertexArcs(packageDiagram);

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
            String plantUMLCode = getPackageText(dpi) + bufferBody;
            SourceStringReader reader = new SourceStringReader(replaceDots(plantUMLCode));
            reader.outputImage(byteArrayOutputStream, new FileFormatOption(FileFormat.SVG))
                    .getDescription();

            return byteArrayOutputStream.toString(StandardCharsets.UTF_8);
        } catch (IOException e) {
            logger.error("Failed to create svg image.");
            throw new RuntimeException(e);
        }
    }

    private static String replaceDots(String plantUMLCode) {
        StringBuilder newString = new StringBuilder();
        String[] lines = plantUMLCode.split(System.lineSeparator());
        for (String line : lines) {
            String[] words = line.split(" ");
            for (String word : words) {
                String newWord = word;
                if (word.contains(".") && !word.contains("..")) {
                    newWord = word.replace(".", "_");
                    newWord = newWord.replace("-", "_");
                }
                newString.append(newWord).append(" ");
            }
            newString.append(System.lineSeparator());
        }
        return newString.toString();
    }

    private static String getPackageText(int dpi) {
        return "@startuml"
                + System.lineSeparator()
                + (dpi > 0 ? "skinparam dpi " + dpi : "")
                + System.lineSeparator()
                +
                // TODO: Determine the best value for this.
                "skinparam wrapWidth 10"
                + System.lineSeparator()
                + "skinparam package {"
                + System.lineSeparator()
                + "    BackgroundColor lightyellow"
                + System.lineSeparator()
                + "    BorderColor black"
                + System.lineSeparator()
                + "    ArrowColor black"
                + System.lineSeparator()
                + "    Shadowing true"
                + System.lineSeparator()
                + "}"
                + System.lineSeparator()
                + System.lineSeparator();
    }
}
