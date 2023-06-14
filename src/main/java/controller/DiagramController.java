package controller;

import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import manager.DiagramManager;
import manager.DiagramManagerFactory;
import manager.SourceProject;
import org.javatuples.Pair;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class DiagramController implements Controller {

	private final DiagramManager diagramManager;

	public DiagramController(String diagramType) {
		DiagramManagerFactory diagramManagerFactory = new DiagramManagerFactory();
		diagramManager = diagramManagerFactory.createDiagramManager(diagramType);
	}

	public SourceProject createTree(Path sourcePackagePath) {
		return diagramManager.createSourceProject(sourcePackagePath);
	}

	public void convertTreeToDiagram(List<String> chosenClassesNames) {
		diagramManager.convertTreeToDiagram(chosenClassesNames);
	}

	public Map<Integer, Pair<Double, Double>> arrangeDiagram(){
		return diagramManager.arrangeDiagram();
	}

	public File exportDiagramToGraphML(Path graphMLSavePath) {
		return diagramManager.exportDiagramToGraphML(graphMLSavePath);
	}

	public File exportPlantUMLDiagram(Path plantUMLSavePath) {
		return diagramManager.exportPlantUMLDiagram(plantUMLSavePath);
	}

	public File exportPlantUMLText(Path textSavePath) {
		return diagramManager.exportPlantUMLText(textSavePath);
	}

	public SmartGraphPanel<String, String> visualizeJavaFXGraph() {
		return diagramManager.visualizeJavaFXGraph();
	}

	public File saveDiagram(Path graphSavePath) {
		return diagramManager.saveDiagram(graphSavePath);
	}

	public void loadDiagram(Path graphSavePath) {
		diagramManager.loadDiagram(graphSavePath);
	}

}
