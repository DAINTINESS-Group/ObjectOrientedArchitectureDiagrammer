package model.diagram.plantuml;

import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Map;

import javax.imageio.ImageIO;

import model.graph.Arc;
import model.graph.SinkVertex;
import net.sourceforge.plantuml.SourceStringReader;

public class PlantUMLClassExporter {

	private final String bufferBody;
	private final File plantUMLFile;
	
    public PlantUMLClassExporter(Path savePath, Map<SinkVertex, Integer> graphNodes, Map<Arc<SinkVertex>, Integer> graphEdges) {
		PlantUMLSinkVertex plantUMLSinkVertex = new PlantUMLSinkVertex(graphNodes);
		StringBuilder plantUMLNodeBuffer = plantUMLSinkVertex.convertPlantLeafNode();
		PlantUMLSinkVertexArc plantUMLEdge = new PlantUMLSinkVertexArc(graphEdges);
		StringBuilder plantUMLEdgeBuffer = plantUMLEdge.convertPlantEdge();

		plantUMLFile = savePath.toFile();
    	bufferBody = plantUMLNodeBuffer.append(plantUMLEdgeBuffer)  + "@enduml\n";
    }

	public File exportClassDiagram() {
    	String plantUMLCode = getClassText();
    	plantUMLCode += bufferBody;
    	exportDiagram(plantUMLCode);
		return plantUMLFile;
	}

	public File exportClassDiagramText() {
		String plantUMLCode = getClassText();
		plantUMLCode += bufferBody;
		textExporter(plantUMLCode);
		return plantUMLFile;
	}

	private void textExporter(String plantCode) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(plantUMLFile))) {
            writer.write(plantCode);
        } catch (IOException e) {
        	e.printStackTrace();
        }
	}
	
	private void exportDiagram(String plantCode) {
    	try {
			ByteArrayOutputStream png = new ByteArrayOutputStream();
			SourceStringReader reader = new SourceStringReader(plantCode);
        	reader.outputImage(png).getDescription();
			byte [] data = png.toByteArray();
		    InputStream in = new ByteArrayInputStream(data);
		    BufferedImage convImg = ImageIO.read(in);
		    int width = convImg.getWidth();
            int wrapWidth = 150;
            //int stringChangerCounter = 0; 
            if (width == 4096) {
            	png = new ByteArrayOutputStream();
            	plantCode = wrapWidthChanger(plantCode, wrapWidth);
            	reader = new SourceStringReader(plantCode);
            	reader.outputImage(png).getDescription();
    			data = png.toByteArray();
    		    in = new ByteArrayInputStream(data);
    		    convImg = ImageIO.read(in);
    		    width = convImg.getWidth();
                //stringChangerCounter ++;
			}
			ImageIO.write(convImg, "png", plantUMLFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	   
    private String wrapWidthChanger(String plantCode, int wrapWidth){
    	String updatedString;
    	//if (counter == 0) {
    	int indexOfNewLine = plantCode.indexOf("\n");
        String firstPart = plantCode.substring(0, indexOfNewLine + 1);
       	String secondPart = plantCode.substring(indexOfNewLine + 1);
       	updatedString = firstPart + "skinparam wrapWidth " + wrapWidth + "\n" + secondPart;
        // !! COMMENTED CODE HERE - KEEP REDUCING THE WRAPWIDTH PARAMETER IN ORDER TO FIT PROPERLY THE IMAGE
        // DOESNT WORK PROPERLY, COMMENTED JUST TO KEEP THE IDEA. 
        // POP UP MESSAGE CAN BE ADDED TO INFORM THE USER THAT THE IMAGE HE REQUESTED IS OVER 4096x4096
        // SO WE REDUCE THE WRAPWIDTH TO REDUCE EXTRACTED IMAGE'S WIDTH. NOW THE USER CAN SEE MORE CLASSES.
//    	}else {
//    		String[] lines = plantCode.split("\n");
//    		lines[1] = "skinparam wrapWidth " + wrapWidth;
//    		updatedString = String.join("\n", lines);
//    	}
    	return updatedString;
    }
    
    private String getClassText() {
    	return "@startuml\n" +
		        "skinparam class {\n" +
		        "    BackgroundColor lightyellow\n" +
		        "    BorderColor black\n" +
		        "    ArrowColor black\n" +
		        "}\n";
    }
}