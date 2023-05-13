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
    	System.out.println(plantUMLCode);
    	ByteArrayOutputStream png = new ByteArrayOutputStream();
    	try {
        	SourceStringReader reader = new SourceStringReader(plantUMLCode);
        	reader.outputImage(png).getDescription();
			byte [] data = png.toByteArray();
		    InputStream in = new ByteArrayInputStream(data);
		    BufferedImage convImg = ImageIO.read(in);
		    ImageIO.write(convImg, "png", new File(selectedFile.toString()));
        } catch (IOException e) {
			e.printStackTrace();
		}
	}
}