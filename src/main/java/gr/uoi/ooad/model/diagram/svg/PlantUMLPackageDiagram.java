package gr.uoi.ooad.model.diagram.svg;

import gr.uoi.ooad.model.diagram.PackageDiagram;
import gr.uoi.ooad.model.diagram.plantuml.PlantUMLPackageVertex;
import gr.uoi.ooad.model.diagram.plantuml.PlantUMLPackageVertexArc;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
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
                plantUMLNodeBuffer.append("\n\n").append(plantUMLEdgeBuffer).append("\n @enduml");
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
        String[] lines = plantUMLCode.split("\n");
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
            newString.append("\n");
        }
        return newString.toString();
    }

    private static String getPackageText(int dpi) {
        return "@startuml\n"
                + (dpi > 0 ? "skinparam dpi " + dpi : "")
                + "\n"
                +
                // TODO: Determine the best value for this.
                "skinparam wrapWidth 10"
                + "\n"
                + "skinparam package {\n"
                + "    BackgroundColor lightyellow\n"
                + "    BorderColor black\n"
                + "    ArrowColor black\n"
                + "    Shadowing true\n"
                + "}\n"
                + "\n";
    }
}
