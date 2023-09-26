package manager;

import com.brunomnsilva.smartgraph.graph.Vertex;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import com.google.gson.JsonParseException;
import model.diagram.PackageDiagram;
import model.diagram.arrangement.DiagramArrangementManagerInterface;
import model.diagram.arrangement.PackageDiagramArrangementManager;
import model.diagram.arrangement.geometry.DiagramGeometry;
import model.diagram.exportation.*;
import model.diagram.javafx.JavaFXVisualization;
import model.diagram.javafx.JavaFXPackageDiagramLoader;
import model.diagram.javafx.JavaFXPackageVisualization;
import org.javatuples.Pair;

import java.io.File;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;

public class PackageDiagramManager implements DiagramManager {

	private PackageDiagram packageDiagram;
	private DiagramArrangementManagerInterface packageDiagramArrangement;
	private Collection<Vertex<String>> vertexCollection;
	private SmartGraphPanel<String, String> graphView;

	public PackageDiagramManager() {
		packageDiagram = new PackageDiagram();
	}

	@Override
	public SourceProject createSourceProject(Path sourcePackagePath) {
		SourceProject sourceProject = new SourceProject(packageDiagram);
		sourceProject.createGraph(sourcePackagePath);
		sourceProject.setPackageDiagramVertices();
		return sourceProject;
	}

	@Override
	public void convertTreeToDiagram(List<String> chosenFilesNames) {
		packageDiagram.createNewDiagram(chosenFilesNames);
	}

	@Override
	public void arrangeDiagram(){
		packageDiagramArrangement = new PackageDiagramArrangementManager(packageDiagram);
		DiagramGeometry diagramGeometry = packageDiagramArrangement.arrangeDiagram();
		packageDiagram.setDiagramGeometry(diagramGeometry);
	}

	@Override
	public SmartGraphPanel<String, String> visualizeJavaFXGraph() {
		JavaFXVisualization javaFXPackageVisualization = new JavaFXPackageVisualization(packageDiagram);
		graphView = javaFXPackageVisualization.createGraphView();
		vertexCollection = javaFXPackageVisualization.getVertexCollection();
		return graphView;
	}

	@Override
	public SmartGraphPanel<String, String> visualizeLoadedJavaFXGraph() {
		JavaFXVisualization javaFXPackageVisualization = new JavaFXPackageVisualization(packageDiagram);
		javaFXPackageVisualization.createGraphView();
		graphView = javaFXPackageVisualization.getLoadedGraph();
		vertexCollection = javaFXPackageVisualization.getVertexCollection();
		return graphView;
	}

	@Override
	public File exportDiagramToGraphML(Path graphMLSavePath) {
		packageDiagram.setGraphMLDiagramGeometry(packageDiagramArrangement.arrangeGraphMLDiagram());
		DiagramExporter diagramExporter = new GraphMLPackageDiagramExporter(packageDiagram);
		return diagramExporter.exportDiagram(graphMLSavePath);
	}

	@Override
	public File exportPlantUMLImage(Path plantUMLSavePath) {
		DiagramExporter diagramExporter =  new PlantUMLPackageDiagramImageExporter(packageDiagram);
		return diagramExporter.exportDiagram(plantUMLSavePath);
	}

	@Override
	public File exportPlantUMLText(Path textSavePath) {
		DiagramExporter diagramExporter =  new PlantUMLPackageDiagramTextExporter(packageDiagram);
		return diagramExporter.exportDiagram(textSavePath);
	}

	@Override
	public File saveDiagram(Path graphSavePath) {
		CoordinatesUpdater coordinatesUpdater = new CoordinatesUpdater(packageDiagram);
		coordinatesUpdater.updatePackageCoordinates(vertexCollection, graphView);
		DiagramExporter diagramExporter =  new JavaFXPackageDiagramExporter(packageDiagram);
		return diagramExporter.exportDiagram(graphSavePath);
	}

	@Override
	public void loadDiagram(Path graphSavePath) throws JsonParseException {
		packageDiagram = new PackageDiagram();
		JavaFXPackageDiagramLoader javaFXPackageDiagramLoader = new JavaFXPackageDiagramLoader(graphSavePath);
		packageDiagram.createDiagram(javaFXPackageDiagramLoader.loadDiagram());
	}

	public PackageDiagram getPackageDiagram() {
		return packageDiagram;
	}

	public SmartGraphPanel<String, String> applyLayout() {
		DiagramGeometry nodesGeometry = packageDiagram.getDiagramGeometry();
		for(Vertex<String> vertex : vertexCollection) {
			if(nodesGeometry.containsKey(vertex.element())) {
				Pair<Double, Double> coordinates = nodesGeometry.getVertexGeometry(vertex.element());
				graphView.setVertexPosition(vertex,  coordinates.getValue0(), coordinates.getValue1());
			}else {
				System.out.println(vertex.element());
			}
		}
		return graphView;
	}

	public SmartGraphPanel<String, String> applySpecificLayout(String choice){
		DiagramGeometry nodesGeometry = packageDiagramArrangement.applyNewLayout(choice);
		for(Vertex<String> vertex : vertexCollection) {
			if(nodesGeometry.containsKey(vertex.element())) {
				Pair<Double, Double> coordinates = nodesGeometry.getVertexGeometry(vertex.element());
				graphView.setVertexPosition(vertex,  coordinates.getValue0(), coordinates.getValue1());
			}
		}
		return graphView;
	}

}
