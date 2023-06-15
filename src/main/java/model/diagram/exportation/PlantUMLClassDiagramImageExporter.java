package model.diagram.exportation;

import model.diagram.plantuml.PlantUMLSinkVertex;
import model.diagram.plantuml.PlantUMLSinkVertexArc;
import model.graph.Arc;
import model.graph.SinkVertex;
import net.sourceforge.plantuml.SourceStringReader;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Path;
import java.util.Map;
import java.util.Set;

public class PlantUMLClassDiagramImageExporter implements DiagramExporter {

	private final String bufferBody;

    public PlantUMLClassDiagramImageExporter(Map<SinkVertex, Set<Arc<SinkVertex>>> diagram) {
		PlantUMLSinkVertex plantUMLSinkVertex = new PlantUMLSinkVertex(diagram);
		StringBuilder plantUMLNodeBuffer = plantUMLSinkVertex.convertSinkVertex();
		PlantUMLSinkVertexArc plantUMLEdge = new PlantUMLSinkVertexArc(diagram);
		StringBuilder plantUMLEdgeBuffer = plantUMLEdge.convertSinkVertexArc();
    	bufferBody = plantUMLNodeBuffer.append("\n\n").append(plantUMLEdgeBuffer) + "\n @enduml";
    }

	@Override
	public File exportDiagram(Path exportPath) {
		File plantUMLFile = exportPath.toFile();
		String plantUMLCode = getClassText();
		plantUMLCode += bufferBody;
		exportImage(plantUMLFile, plantUMLCode);
		return plantUMLFile;
	}

	private void exportImage(File plantUMLFile, String plantCode) {
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
		        "}\n\n";
    }
}