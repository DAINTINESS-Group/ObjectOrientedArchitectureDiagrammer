package model.diagram.exportation;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import javax.imageio.ImageIO;
import model.diagram.ClassDiagram;
import model.diagram.plantuml.PlantUMLClassifierVertex;
import model.diagram.plantuml.PlantUMLClassifierVertexArc;
import net.sourceforge.plantuml.SourceStringReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PlantUMLClassDiagramImageExporter implements DiagramExporter {
    private static final Logger logger =
            LogManager.getLogger(PlantUMLPackageDiagramImageExporter.class);

    private final StringBuilder bufferBody;

    public PlantUMLClassDiagramImageExporter(ClassDiagram diagram) {
        StringBuilder plantUMLNodeBuffer = PlantUMLClassifierVertex.convertSinkVertices(diagram);
        StringBuilder plantUMLEdgeBuffer =
                PlantUMLClassifierVertexArc.convertSinkVertexArcs(diagram);
        bufferBody =
                plantUMLNodeBuffer.append("\n\n").append(plantUMLEdgeBuffer).append("\n @enduml");
    }

    @Override
    public File exportDiagram(Path exportPath) {
        File plantUMLFile = exportPath.toFile();
        String plantUMLCode = getClassText();
        plantUMLCode += bufferBody;
        exportImage(plantUMLFile, plantUMLCode);
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
        // if (counter == 0) {
        int indexOfNewLine = plantCode.indexOf("\n");
        String firstPart = plantCode.substring(0, indexOfNewLine + 1);
        String secondPart = plantCode.substring(indexOfNewLine + 1);
        updatedString =
                String.format("%sskinparam wrapWidth %d\n%s", firstPart, wrapWidth, secondPart);
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
