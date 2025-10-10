package gr.uoi.ooad.model.diagram.exportation;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import javax.imageio.ImageIO;
import gr.uoi.ooad.model.diagram.PackageDiagram;
import gr.uoi.ooad.model.diagram.plantuml.PlantUMLPackageVertex;
import gr.uoi.ooad.model.diagram.plantuml.PlantUMLPackageVertexArc;
import net.sourceforge.plantuml.SourceStringReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PlantUMLPackageDiagramImageExporter implements DiagramExporter {
    private static final Logger logger =
            LogManager.getLogger(PlantUMLPackageDiagramImageExporter.class);

    private final StringBuilder bufferBody;

    public PlantUMLPackageDiagramImageExporter(PackageDiagram packageDiagram) {
        StringBuilder plantUMLNodeBuffer = PlantUMLPackageVertex.convertVertices(packageDiagram);
        StringBuilder plantUMLEdgeBuffer =
                PlantUMLPackageVertexArc.convertVertexArcs(packageDiagram);
        bufferBody =
                plantUMLNodeBuffer.append("\n\n").append(plantUMLEdgeBuffer).append("\n @enduml");
    }

    @Override
    public File exportDiagram(Path exportPath) {
        File plantUMLFile = exportPath.toFile();
        String plantUMLCode = getPackageText() + bufferBody;
        exportImage(plantUMLFile, replaceDots(plantUMLCode));
        return plantUMLFile;
    }

    private static void exportImage(File plantUMLFile, String plantCode) {
        try (ByteArrayOutputStream png = new ByteArrayOutputStream()) {
            SourceStringReader reader = new SourceStringReader(plantCode);
            reader.outputImage(png).getDescription();

            byte[] data = png.toByteArray();
            InputStream in = new ByteArrayInputStream(data);
            BufferedImage convImg = ImageIO.read(in);
            int width = convImg.getWidth();
            int wrapWidth = 150;

            if (width == 4096) {
                try (ByteArrayOutputStream newPng = new ByteArrayOutputStream()) {
                    plantCode = wrapWidthChanger(plantCode, wrapWidth);
                    reader = new SourceStringReader(plantCode);
                    reader.outputImage(newPng).getDescription();
                    data = newPng.toByteArray();
                    in = new ByteArrayInputStream(data);
                    convImg = ImageIO.read(in);
                } catch (IOException e) {
                    logger.error("Failed to read from input stream");
                    throw new RuntimeException(e);
                }
            }
            ImageIO.write(convImg, "png", plantUMLFile);
        } catch (IOException e) {
            logger.error("Failed to write image to file: {}", plantUMLFile.getAbsolutePath());
            throw new RuntimeException(e);
        }
    }

    private static String wrapWidthChanger(String plantCode, int wrapWidth) {
        String updatedString;
        int indexOfNewLine = plantCode.indexOf("\n");
        String firstPart = plantCode.substring(0, indexOfNewLine + 1);
        String secondPart = plantCode.substring(indexOfNewLine + 1);
        updatedString = firstPart + "skinparam wrapWidth " + wrapWidth + "\n" + secondPart;
        // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ //
        // KEEP REDUCING THE WRAPWIDTH PARAMETER IN ORDER TO FIT PROPERLY THE IMAGE //
        // DOESNT WORK PROPERLY, COMMENTED JUST TO KEEP THE IDEA.					//
        // POP UP MESSAGE CAN BE ADDED TO INFORM THE USER THAT THE IMAGE HE			//
        // REQUESTED IS OVER 4096x4096 SO WE REDUCE THE WRAPWIDTH TO REDUCE			//
        // EXTRACTED IMAGE'S WIDTH. NOW THE USER CAN SEE MORE CLASSES.				//
        //    	} else {															//
        //    		String[] lines = plantCode.split("\n");							//
        //    		lines[1] = "skinparam wrapWidth " + wrapWidth;					//
        //    		updatedString = String.join("\n", lines);						//
        //    	}																	//
        // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ //
        return updatedString;
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

    private static String getPackageText() {
        return """
            @startuml
            skinparam package {
                BackgroundColor lightyellow
                BorderColor black
                ArrowColor black
                Shadowing true
            }

            """;
    }
}
