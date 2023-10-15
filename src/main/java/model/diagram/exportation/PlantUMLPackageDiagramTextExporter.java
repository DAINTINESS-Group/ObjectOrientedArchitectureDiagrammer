package model.diagram.exportation;

import model.diagram.PackageDiagram;
import model.diagram.plantuml.PlantUMLPackageVertex;
import model.diagram.plantuml.PlantUMLPackageVertexArc;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

public class PlantUMLPackageDiagramTextExporter implements DiagramExporter {

	private final StringBuilder bufferBody;

	public PlantUMLPackageDiagramTextExporter(PackageDiagram diagram) {
		PlantUMLPackageVertex plantUMLPackageVertex = new PlantUMLPackageVertex(diagram);
		StringBuilder plantUMLNodeBuffer 			= plantUMLPackageVertex.convertVertex();
		PlantUMLPackageVertexArc plantUMLEdge 		= new PlantUMLPackageVertexArc(diagram);
		StringBuilder plantUMLEdgeBuffer 			= plantUMLEdge.convertVertexArc();
		this.bufferBody 							= plantUMLNodeBuffer.append("\n\n")
																		.append(plantUMLEdgeBuffer)
																		.append("\n @enduml");
	}

	@Override
	public File exportDiagram(Path exportPath) {
		File plantUMLFile   = exportPath.toFile();
		String plantUMLCode = getPackageText();
		plantUMLCode 		+= this.bufferBody;
		plantUMLCode 		= dotChanger(plantUMLCode);
		writeFile(plantUMLFile, plantUMLCode);
		return plantUMLFile;
	}

	private void writeFile(File plantUMLFile, String plantCode) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(plantUMLFile))) {
			writer.write(plantCode);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String dotChanger(String plantUMLCode) {
		StringBuilder newString = new StringBuilder();
		String[] lines 			= plantUMLCode.split("\n");
		for (String line: lines) {
			String[] splittedLine = line.split(" ");
			for (String word: splittedLine) {
				String newWord = word;
				if(word.contains(".") && !word.contains("..")) {
					newWord = word.replace(".", "_");
					newWord = newWord.replace("-", "_");
				}
				newString.append(newWord).append(" ");
			}
			newString.append("\n");
		}
		return newString.toString();
	}

	private String getPackageText() {
		return
			"""
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
