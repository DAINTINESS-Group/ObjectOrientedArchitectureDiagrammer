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

import javax.imageio.ImageIO;

import net.sourceforge.plantuml.SourceStringReader;

public class PlantUMLExporter {

	private final String bufferBody;
	private File plantUMLFile;
	
    public PlantUMLExporter(Path savePath, String nodesBuffer, String edgesBuffer) {
		plantUMLFile = savePath.toFile();
    	bufferBody = nodesBuffer + edgesBuffer + "@enduml\n";
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

	public File exportPackageDiagram() {
    	String plantUMLCode = getPackageText();
    	plantUMLCode += bufferBody;
    	plantUMLCode = dotChanger(plantUMLCode);
    	exportDiagram(plantUMLCode);
		return plantUMLFile;
	}

	public File exportPackageDiagramText() {
		String plantUMLCode = getPackageText();
		plantUMLCode += bufferBody;
		plantUMLCode = dotChanger(plantUMLCode);
		textExporter(plantUMLCode);
		return plantUMLFile;
	}
	
	private void textExporter(String plantCode) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(plantUMLFile));
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
				ImageIO.write(convImg, "png", plantUMLFile);
			}
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
    
    private String dotChanger(String plantUMLCode) {
    	String newString = "";
    	String[] lines = plantUMLCode.split("\n");
    	for (String line: lines) {
    		String[] splittedLine = line.split(" ");
    		for (String word: splittedLine) {
    			String newWord = word;
    			if(word.contains(".") && !word.contains("..")) {
    				newWord = word.replace(".", "_");
        			newWord = newWord.replace("-", "_");
    			}
    			newString += newWord + " ";
    		}
    		newString += "\n";
    	}
    	return newString;
    }
    
    private String getPackageText() {
    	return "@startuml\n" +
		        "skinparam package {\n" +
		        "    BackgroundColor lightyellow\n" +
		        "    BorderColor black\n" +
		        "    ArrowColor black\n" +
		        "    Shadowing true\n" +
		        "}\n";
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