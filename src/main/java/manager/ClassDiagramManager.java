package manager;

import com.brunomnsilva.smartgraph.graph.Edge;
import com.brunomnsilva.smartgraph.graph.Vertex;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import com.google.gson.JsonParseException;
import model.diagram.ClassDiagram;
import model.diagram.ShadowCleaner;
import model.diagram.arrangement.ClassDiagramArrangement;
import model.diagram.arrangement.DiagramArrangement;
import model.diagram.exportation.*;
import model.diagram.javafx.JavaFXVisualization;
import model.diagram.javafx.JavaFXClassDiagramLoader;
import model.diagram.javafx.JavaFXClassVisualization;
import org.javatuples.Pair;

import java.io.File;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ClassDiagramManager implements DiagramManager {

    private ClassDiagram classDiagram;
    private Collection<Edge<String, String>> edgeCollection;
    private Collection<Vertex<String>> vertexCollection;
    
    public ClassDiagramManager() {
        classDiagram = new ClassDiagram();
    }

    @Override
    public SourceProject createSourceProject(Path sourcePackagePath) {
        SourceProject sourceProject = new SourceProject(classDiagram);
        sourceProject.createGraph(sourcePackagePath);
        sourceProject.setClassDiagramSinkVertices();
        return sourceProject;
    }

    @Override
    public void convertTreeToDiagram(List<String> chosenFilesNames) {
        classDiagram.createNewDiagram(chosenFilesNames);
        ShadowCleaner shadowCleaner = new ShadowCleaner(classDiagram);
        classDiagram.setDiagram(shadowCleaner.shadowWeakRelationships());
    }

    @Override
    public Map<Integer, Pair<Double, Double>> arrangeDiagram(){
        DiagramArrangement classDiagramArrangement = new ClassDiagramArrangement(classDiagram);
        Map<Integer, Pair<Double, Double>> diagramGeometry = classDiagramArrangement.arrangeDiagram();
        classDiagram.setDiagramGeometry(diagramGeometry);
        return diagramGeometry;
    }

    @Override
    public SmartGraphPanel<String, String> visualizeJavaFXGraph() {
        JavaFXVisualization javaFXVisualization = new JavaFXClassVisualization(classDiagram);
        SmartGraphPanel<String, String> graphView= javaFXVisualization.createGraphView();
        edgeCollection = javaFXVisualization.getEdgeCollection();
        vertexCollection = javaFXVisualization.getVertexCollection();
        return graphView;
    }

    @Override
    public File exportDiagramToGraphML(Path graphMLSavePath) {
        DiagramExporter diagramExporter = new GraphMLClassDiagramExporter(classDiagram);
        return diagramExporter.exportDiagram(graphMLSavePath);
    }
    
    @Override
    public Collection<Vertex<String>> getVertexCollection(){
    	return vertexCollection;
    }
    
	@Override
    public Collection<Edge<String, String>> getEdgeCollection(){
    	return edgeCollection;
    }

    @Override
    public File exportPlantUMLImage(Path plantUMLSavePath) {
        DiagramExporter diagramExporter = new PlantUMLClassDiagramImageExporter(classDiagram);
        return diagramExporter.exportDiagram(plantUMLSavePath);
    }

    @Override
    public File exportPlantUMLText(Path textSavePath) {
        DiagramExporter diagramExporter = new PlantUMLClassDiagramTextExporter(classDiagram);
        return diagramExporter.exportDiagram(textSavePath);
    }

    @Override
    public File saveDiagram(Path graphSavePath) {
        DiagramExporter diagramExporter = new JavaFXClassDiagramExporter(classDiagram);
        return diagramExporter.exportDiagram(graphSavePath);
    }

    @Override
    public void loadDiagram(Path graphSavePath) throws JsonParseException {
        classDiagram = new ClassDiagram();
        JavaFXClassDiagramLoader javaFXClassDiagramLoader =  new JavaFXClassDiagramLoader(graphSavePath);
        classDiagram.createDiagram(javaFXClassDiagramLoader.loadDiagram());
        ShadowCleaner shadowCleaner = new ShadowCleaner(classDiagram);
        classDiagram.setDiagram(shadowCleaner.shadowWeakRelationships());
    }

    public ClassDiagram getClassDiagram() {
        return classDiagram;
    }

}
