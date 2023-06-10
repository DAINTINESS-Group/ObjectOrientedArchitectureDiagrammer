package model.plantuml;

import manager.ClassDiagramManager;
import manager.SourceProject;
import model.diagram.DiagramExporter;
import model.diagram.plantuml.PlantUMLClassDiagramImageExporter;
import model.diagram.plantuml.PlantUMLSinkVertex;
import model.diagram.plantuml.PlantUMLSinkVertexArc;
import model.graph.Arc;
import model.graph.SinkVertex;
import net.sourceforge.plantuml.SourceStringReader;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlantUMLClassDiagramImageExporterTest {

    Path currentDirectory = Path.of(".");

    @Test
    void exportDiagramTest() {
        try {
            ClassDiagramManager classDiagramManager = new ClassDiagramManager();
            SourceProject sourceProject = classDiagramManager.createSourceProject(Paths.get(currentDirectory.toRealPath() + "\\src\\test\\resources\\LatexEditor\\src"));
            classDiagramManager.createDiagram(List.of("StableVersionsStrategy", "VersionsStrategy", "VersionsStrategyFactory", "VolatileVersionsStrategy",
                    "VersionsManager", "Document", "DocumentManager"));

            Map<SinkVertex, Integer> graphNodes = classDiagramManager.getDiagram().getGraphNodes();
            PlantUMLSinkVertex plantUMLSinkVertex = new PlantUMLSinkVertex(graphNodes);
            String sinkVertexBuffer = plantUMLSinkVertex.convertSinkVertex().toString();
            Map<Arc<SinkVertex>, Integer> graphEdges = classDiagramManager.getDiagram().getGraphEdges();
            PlantUMLSinkVertexArc plantUMLEdge = new PlantUMLSinkVertexArc(graphEdges);
            String sinkVertexArcBuffer = plantUMLEdge.convertSinkVertexArc().toString();

            DiagramExporter graphMLExporter = new PlantUMLClassDiagramImageExporter(graphNodes, graphEdges);
            graphMLExporter.exportDiagram(Paths.get(System.getProperty("user.home") + "\\testingExportedFile.png"));
            BufferedImage originalImage = ImageIO.read( Path.of(System.getProperty("user.home") + "\\testingExportedFile.png").toFile());
            byte[] byteArray = ((DataBufferByte) originalImage.getData().getDataBuffer()).getData();

            String expected = "@startuml\n" +
                    "skinparam class {\n" +
                    "    BackgroundColor lightyellow\n" +
                    "    BorderColor black\n" +
                    "    ArrowColor black\n" +
                    "}\n";

            expected += sinkVertexBuffer + sinkVertexArcBuffer + "@enduml\n";
            ByteArrayOutputStream png = new ByteArrayOutputStream();
            SourceStringReader reader = new SourceStringReader(expected);
            reader.outputImage(png).getDescription();
            byte [] data = png.toByteArray();

            assertEquals(data, byteArray);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
