package manager;

import com.brunomnsilva.smartgraph.graph.Edge;
import com.brunomnsilva.smartgraph.graph.Vertex;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import com.google.gson.JsonParseException;
import model.diagram.PackageDiagram;
import model.diagram.arrangement.DiagramArrangement;
import model.diagram.arrangement.PackageDiagramArrangement;
import model.diagram.exportation.*;
import model.diagram.javafx.JavaFXVisualization;
import model.diagram.javafx.JavaFXPackageDiagramLoader;
import model.diagram.javafx.JavaFXPackageVisualization;
import org.javatuples.Pair;

import java.io.File;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class PackageDiagramManager implements DiagramManager {

    private PackageDiagram packageDiagram;
    private Collection<Edge<String, String>> edgeCollection;
    private Collection<Vertex<String>> vertexCollection;

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
    public Map<Integer, Pair<Double, Double>> arrangeDiagram(){
        DiagramArrangement diagramArrangement = new PackageDiagramArrangement(packageDiagram);
        Map<Integer, Pair<Double, Double>> diagramGeometry = diagramArrangement.arrangeDiagram();
        packageDiagram.setDiagramGeometry(diagramGeometry);
        return diagramGeometry;
    }

    @Override
    public SmartGraphPanel<String, String> visualizeJavaFXGraph() {
        JavaFXVisualization javaFXPackageVisualization = new JavaFXPackageVisualization(packageDiagram);
        SmartGraphPanel<String, String> graphView = javaFXPackageVisualization.createGraphView();
        edgeCollection = javaFXPackageVisualization.getEdgeCollection();
        vertexCollection = javaFXPackageVisualization.getVertexCollection();
        return graphView;
    }
    
    @Override
    public Collection<com.brunomnsilva.smartgraph.graph.Vertex<String>> getVertexCollection(){
    	return vertexCollection;
    }
    
	@Override
    public Collection<Edge<String, String>> getEdgeCollection(){
    	return edgeCollection;
    }

    @Override
    public File exportDiagramToGraphML(Path graphMLSavePath) {
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
}
