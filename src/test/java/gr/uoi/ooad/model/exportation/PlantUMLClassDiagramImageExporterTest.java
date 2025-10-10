package gr.uoi.ooad.model.exportation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.github.romankh3.image.comparison.ImageComparison;
import com.github.romankh3.image.comparison.ImageComparisonUtil;
import com.github.romankh3.image.comparison.model.ImageComparisonResult;
import com.github.romankh3.image.comparison.model.ImageComparisonState;
import gr.uoi.ooad.manager.ClassDiagramManager;
import gr.uoi.ooad.model.diagram.exportation.DiagramExporter;
import gr.uoi.ooad.model.diagram.exportation.PlantUMLClassDiagramImageExporter;
import gr.uoi.ooad.model.diagram.plantuml.PlantUMLClassifierVertex;
import gr.uoi.ooad.model.diagram.plantuml.PlantUMLClassifierVertexArc;
import gr.uoi.ooad.utils.PathConstructor;
import gr.uoi.ooad.utils.PathTemplate.LatexEditor;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import javax.imageio.ImageIO;
import net.sourceforge.plantuml.SourceStringReader;
import org.junit.jupiter.api.Test;

public class PlantUMLClassDiagramImageExporterTest {

    @Test
    void exportDiagramTest() {
        try {
            ClassDiagramManager classDiagramManager = new ClassDiagramManager();
            classDiagramManager.createSourceProject(LatexEditor.SRC.path);
            classDiagramManager.convertTreeToDiagram(
                    List.of(
                            "StableVersionsStrategy",
                            "VersionsStrategy",
                            "VersionsStrategyFactory",
                            "VolatileVersionsStrategy",
                            "VersionsManager",
                            "Document",
                            "DocumentManager"));

            String sinkVertexBuffer =
                    PlantUMLClassifierVertex.convertSinkVertices(
                                    classDiagramManager.getClassDiagram())
                            .toString();
            String sinkVertexArcBuffer =
                    PlantUMLClassifierVertexArc.convertSinkVertexArcs(
                                    classDiagramManager.getClassDiagram())
                            .toString();

            DiagramExporter plantUMLExporter =
                    new PlantUMLClassDiagramImageExporter(classDiagramManager.getClassDiagram());
            plantUMLExporter.exportDiagram(
                    Paths.get(
                            String.format(
                                    "%s%s%s",
                                    PathConstructor.getCurrentPath(),
                                    File.separator,
                                    PathConstructor.constructPath(
                                            "src",
                                            "test",
                                            "resources",
                                            "testingExportedFile.png"))));

            InputStream in = getExpectedInputStream(sinkVertexBuffer, sinkVertexArcBuffer);

            BufferedImage convImg = ImageIO.read(in);
            ImageIO.write(
                    convImg,
                    "png",
                    Path.of(
                                    String.format(
                                            "%s%s%s",
                                            PathConstructor.getCurrentPath(),
                                            File.separator,
                                            PathConstructor.constructPath(
                                                    "src",
                                                    "test",
                                                    "resources",
                                                    "actualExportedFile.png")))
                            .toFile());

            BufferedImage expectedImage =
                    ImageComparisonUtil.readImageFromResources(
                            Path.of(
                                            String.format(
                                                    "%s%s%s",
                                                    PathConstructor.getCurrentPath(),
                                                    File.separator,
                                                    PathConstructor.constructPath(
                                                            "src",
                                                            "test",
                                                            "resources",
                                                            "testingExportedFile.png")))
                                    .toString());
            BufferedImage actualImage =
                    ImageComparisonUtil.readImageFromResources(
                            Path.of(
                                            String.format(
                                                    "%s%s%s",
                                                    PathConstructor.getCurrentPath(),
                                                    File.separator,
                                                    PathConstructor.constructPath(
                                                            "src",
                                                            "test",
                                                            "resources",
                                                            "actualExportedFile.png")))
                                    .toString());
            ImageComparisonResult imageComparisonResult =
                    new ImageComparison(expectedImage, actualImage).compareImages();
            assertEquals(
                    ImageComparisonState.MATCH, imageComparisonResult.getImageComparisonState());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static InputStream getExpectedInputStream(
            String sinkVertexBuffer, String sinkVertexArcBuffer) {
        byte[] data;
        String expected =
                "@startuml\n"
                        + "skinparam class {\n"
                        + "    BackgroundColor lightyellow\n"
                        + "    BorderColor black\n"
                        + "    ArrowColor black\n"
                        + "}\n"
                        + sinkVertexBuffer
                        + "\n\n"
                        + sinkVertexArcBuffer
                        + "\n"
                        + "@enduml";
        try (ByteArrayOutputStream png = new ByteArrayOutputStream()) {
            SourceStringReader reader = new SourceStringReader(expected);
            reader.outputImage(png).getDescription();
            data = png.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        assertNotNull(data);
        return new ByteArrayInputStream(data);
    }
}
