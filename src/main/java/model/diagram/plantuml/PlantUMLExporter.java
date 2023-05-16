package model.diagram.plantuml;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

import javax.imageio.ImageIO;

import net.sourceforge.plantuml.SourceStringReader;

public class PlantUMLExporter {
	
    public PlantUMLExporter() {
    }
    
    public void exportDiagram(Path selectedFile, String nodesBuffer, String edgesBuffer, boolean packageDiagram) {
    	String plantUMLCode;
    	if (packageDiagram) {
    		plantUMLCode ="@startuml\n";
    	}
    	else{
    		plantUMLCode ="@startuml\n" +
    		        "skinparam class {\n" +
    		        "    BackgroundColor lightyellow\n" +
    		        "    BorderColor black\n" +
    		        "    ArrowColor black\n" +
    		        "}\n";
    	}
    	plantUMLCode += nodesBuffer;
    	plantUMLCode += edgesBuffer;
    	plantUMLCode += "@enduml\n";

    	ByteArrayOutputStream png = new ByteArrayOutputStream();
    	try {
    		SourceStringReader reader = new SourceStringReader(plantUMLCode);
        	reader.outputImage(png).getDescription();
			byte [] data = png.toByteArray();
		    InputStream in = new ByteArrayInputStream(data);
		    BufferedImage convImg = ImageIO.read(in);
		    int width = convImg.getWidth();
            int wrapWidth = 150;
            //int stringChangerCounter = 0; 
            if (width == 4096) {
            	png = new ByteArrayOutputStream();
            	plantUMLCode = stringChanger(plantUMLCode, wrapWidth);
            	reader = new SourceStringReader(plantUMLCode);
            	reader.outputImage(png).getDescription();
    			data = png.toByteArray();
    		    in = new ByteArrayInputStream(data);
    		    convImg = ImageIO.read(in);
    		    width = convImg.getWidth();
                //stringChangerCounter ++;
            }
		    ImageIO.write(convImg, "png", new File(selectedFile.toString()));
        } catch (IOException e) {
			e.printStackTrace();
		}
	}
    
    private String stringChanger(String plantCode, int wrapWidth){
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
}