package model.diagram.exportation;

import model.diagram.ClassDiagram;
import model.diagram.plantuml.PlantUMLClassifierVertex;
import model.diagram.plantuml.PlantUMLClassifierVertexArc;
import net.sourceforge.plantuml.SourceStringReader;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

public class PlantUMLClassDiagramImageExporter implements DiagramExporter {

	private final StringBuilder bufferBody;

	public PlantUMLClassDiagramImageExporter(ClassDiagram diagram) {
		PlantUMLClassifierVertex 	plantUMLClassifierVertex = new PlantUMLClassifierVertex(diagram);
		StringBuilder 			 	plantUMLNodeBuffer		 = plantUMLClassifierVertex.convertSinkVertex();
		PlantUMLClassifierVertexArc plantUMLEdge 		  	 = new PlantUMLClassifierVertexArc(diagram);
		StringBuilder 				plantUMLEdgeBuffer		 = plantUMLEdge.convertSinkVertexArc();
		bufferBody				 							 = plantUMLNodeBuffer
															       .append("\n\n")
															       .append(plantUMLEdgeBuffer)
															       .append("\n @enduml");
	}

	@Override
	public File exportDiagram(Path exportPath) {
		File   plantUMLFile = exportPath.toFile();
		String plantUMLCode = getClassText();
		plantUMLCode 		+= bufferBody;
		exportImage(plantUMLFile, plantUMLCode);
		return plantUMLFile;
	}

	private void exportImage(File plantUMLFile, String plantCode) {
		try (ByteArrayOutputStream png = new ByteArrayOutputStream()) {
			SourceStringReader reader  = new SourceStringReader(plantCode);
			reader.outputImage(png).getDescription();

			byte[] 		  data 	    = png.toByteArray();
			InputStream   in 	    = new ByteArrayInputStream(data);
			BufferedImage convImg   = ImageIO.read(in);
			int 		  width     = convImg.getWidth();
			int 		  wrapWidth = 150;
			if (width == 4096) {
				try (ByteArrayOutputStream newPng = new ByteArrayOutputStream())
				{
					plantCode = wrapWidthChanger(plantCode, wrapWidth);
					reader 	  = new SourceStringReader(plantCode);
					reader.outputImage(newPng).getDescription();
					data 	  = newPng.toByteArray();
					in 		  = new ByteArrayInputStream(data);
					convImg   = ImageIO.read(in);
				}
			}
			ImageIO.write(convImg, "png", plantUMLFile);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	private String wrapWidthChanger(String plantCode, int wrapWidth){
		String updatedString;
		//if (counter == 0) {
		int    indexOfNewLine = plantCode.indexOf("\n");
		String firstPart   	  = plantCode.substring(0, indexOfNewLine + 1);
		String secondPart  	  = plantCode.substring(indexOfNewLine + 1);
		updatedString 	   	  = firstPart + "skinparam wrapWidth " + wrapWidth + "\n" + secondPart;
		// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ //
		// KEEP REDUCING THE WRAPWIDTH PARAMETER IN ORDER TO FIT PROPERLY THE IMAGE //
		// DOESNT WORK PROPERLY, COMMENTED JUST TO KEEP THE IDEA.					//
		// POP UP MESSAGE CAN BE ADDED TO INFORM THE USER THAT THE IMAGE HE			//
		// REQUESTED IS OVER 4096x4096 SO WE REDUCE THE WRAPWIDTH TO REDUCE			//
		// EXTRACTED IMAGE'S WIDTH. NOW THE USER CAN SEE MORE CLASSES.				//
		//    	}else {																//
		//    		String[] lines = plantCode.split("\n");							//
		//    		lines[1] = "skinparam wrapWidth " + wrapWidth;					//
		//    		updatedString = String.join("\n", lines);						//
		//    	}																	//
		// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ //
		return updatedString;
	}

	private String getClassText() {
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