package model.diagram.exportation;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.diagram.ClassDiagram;
import model.graph.ClassifierVertex;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

public class JavaFXClassDiagramExporter implements DiagramExporter {

	private final ClassDiagram classDiagram;

	public JavaFXClassDiagramExporter(ClassDiagram diagram) {
		this.classDiagram = diagram;
	}

	@Override
	public File exportDiagram(Path exportPath) {
		File graphSaveFile = exportPath.toFile();
		try (FileWriter fileWriter = new FileWriter(graphSaveFile)) {
			Gson gson 			   = new GsonBuilder().registerTypeAdapter(ClassifierVertex.class,
																		   new ClassifierVertexSerializer())
																		   .create();
			String json = gson.toJson(this.classDiagram.getDiagram().keySet());
			fileWriter.write(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return graphSaveFile;
	}

}
