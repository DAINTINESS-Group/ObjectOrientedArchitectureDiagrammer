package model.exportation;

import com.github.romankh3.image.comparison.ImageComparison;
import com.github.romankh3.image.comparison.ImageComparisonUtil;
import com.github.romankh3.image.comparison.model.ImageComparisonResult;
import com.github.romankh3.image.comparison.model.ImageComparisonState;
import manager.ClassDiagramManager;
import manager.SourceProject;
import model.diagram.exportation.DiagramExporter;
import model.diagram.exportation.PlantUMLClassDiagramImageExporter;
import model.diagram.plantuml.PlantUMLSinkVertex;
import model.diagram.plantuml.PlantUMLSinkVertexArc;
import net.sourceforge.plantuml.SourceStringReader;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlantUMLClassDiagramImageExporterTest {

    Path currentDirectory = Path.of(".");

    @Test
    void exportDiagramTest() {
        try {
            ClassDiagramManager classDiagramManager = new ClassDiagramManager();
            SourceProject sourceProject = classDiagramManager.createSourceProject(Paths.get(currentDirectory.toRealPath() + "\\src\\test\\resources\\LatexEditor\\src"));
            classDiagramManager.convertTreeToDiagram(List.of("StableVersionsStrategy", "VersionsStrategy", "VersionsStrategyFactory", "VolatileVersionsStrategy",
                    "VersionsManager", "Document", "DocumentManager"));

            PlantUMLSinkVertex plantUMLSinkVertex = new PlantUMLSinkVertex(classDiagramManager.getClassDiagram());
            String sinkVertexBuffer = plantUMLSinkVertex.convertSinkVertex().toString();
            PlantUMLSinkVertexArc plantUMLEdge = new PlantUMLSinkVertexArc(classDiagramManager.getClassDiagram());
            String sinkVertexArcBuffer = plantUMLEdge.convertSinkVertexArc().toString();

            DiagramExporter graphMLExporter = new PlantUMLClassDiagramImageExporter(classDiagramManager.getClassDiagram());
            graphMLExporter.exportDiagram(Paths.get(System.getProperty("user.home") + "\\testingExportedFile.png"));

            String expected = "@startuml\n" +
                    "skinparam class {\n" +
                    "    BackgroundColor lightyellow\n" +
                    "    BorderColor black\n" +
                    "    ArrowColor black\n" +
                    "}\n";
            expected += sinkVertexBuffer + "\n\n" + sinkVertexArcBuffer + "\n @enduml";
            ByteArrayOutputStream png = new ByteArrayOutputStream();
            SourceStringReader reader = new SourceStringReader(expected);
            reader.outputImage(png).getDescription();
            byte[] data = png.toByteArray();
            InputStream in = new ByteArrayInputStream(data);
            BufferedImage convImg = ImageIO.read(in);
            ImageIO.write(convImg, "png", Path.of(System.getProperty("user.home") + "\\actualExportedFile.png").toFile());

            BufferedImage expectedImage = ImageComparisonUtil.readImageFromResources(Path.of(System.getProperty("user.home") + "\\testingExportedFile.png").toString());
            BufferedImage actualImage = ImageComparisonUtil.readImageFromResources(Path.of(System.getProperty("user.home") + "\\actualExportedFile.png").toString());
            ImageComparisonResult imageComparisonResult = new ImageComparison(expectedImage, actualImage).compareImages();
            assertEquals(ImageComparisonState.MATCH, imageComparisonResult.getImageComparisonState());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
