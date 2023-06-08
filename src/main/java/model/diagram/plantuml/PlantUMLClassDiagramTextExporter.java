package model.diagram.plantuml;

import model.diagram.DiagramExporter;
import model.graph.Arc;
import model.graph.SinkVertex;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

public class PlantUMLClassDiagramTextExporter implements DiagramExporter {

    private final String bufferBody;

    public PlantUMLClassDiagramTextExporter(Map<SinkVertex, Integer> graphNodes, Map<Arc<SinkVertex>, Integer> graphEdges) {
        PlantUMLSinkVertex plantUMLSinkVertex = new PlantUMLSinkVertex(graphNodes);
        StringBuilder plantUMLNodeBuffer = plantUMLSinkVertex.convertSinkVertex();
        PlantUMLSinkVertexArc plantUMLEdge = new PlantUMLSinkVertexArc(graphEdges);
        StringBuilder plantUMLEdgeBuffer = plantUMLEdge.convertSinkVertexArc();
        bufferBody = plantUMLNodeBuffer.append(plantUMLEdgeBuffer)  + "@enduml\n";
    }

    @Override
    public File exportDiagram(Path exportPath) {
        File plantUMLFile = exportPath.toFile();
        String plantUMLCode = getClassText();
        plantUMLCode += bufferBody;
        textExporter(plantUMLFile, plantUMLCode);
        return plantUMLFile;
    }

    private void textExporter(File plantUMLFile, String plantCode) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(plantUMLFile))) {
            writer.write(plantCode);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getClassText() {
        return
            "@startuml\n" +
            "skinparam class {\n" +
            "    BackgroundColor lightyellow\n" +
            "    BorderColor black\n" +
            "    ArrowColor black\n" +
            "}\n";
    }
}
