package model.diagram.exportation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import model.diagram.ClassDiagram;
import model.diagram.plantuml.PlantUMLClassifierVertex;
import model.diagram.plantuml.PlantUMLClassifierVertexArc;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PlantUMLClassDiagramTextExporter implements DiagramExporter {
    private static final Logger logger =
            LogManager.getLogger(PlantUMLClassDiagramTextExporter.class);

    private final StringBuilder bufferBody;

    public PlantUMLClassDiagramTextExporter(ClassDiagram classDiagram) {
        StringBuilder plantUMLNodeBuffer =
                PlantUMLClassifierVertex.convertSinkVertices(classDiagram);
        StringBuilder plantUMLEdgeBuffer =
                PlantUMLClassifierVertexArc.convertSinkVertexArcs(classDiagram);
        bufferBody =
                plantUMLNodeBuffer
                        .append(System.lineSeparator())
                        .append(System.lineSeparator())
                        .append(plantUMLEdgeBuffer)
                        .append(System.lineSeparator())
                        .append(" @enduml");
    }

    @Override
    public File exportDiagram(Path exportPath) {
        File plantUMLFile = exportPath.toFile();
        String plantUMLCode = getClassText() + bufferBody;
        writeFile(plantUMLFile, plantUMLCode);
        return plantUMLFile;
    }

    private static void writeFile(File plantUMLFile, String plantCode) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(plantUMLFile))) {
            writer.write(plantCode);
        } catch (IOException e) {
            logger.error("Failed to write to file: {}", plantUMLFile.getAbsolutePath());
            throw new RuntimeException(e);
        }
    }

    private static String getClassText() {
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
