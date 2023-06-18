package manager;

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
import java.util.List;
import java.util.Map;

public class ClassDiagramManager implements DiagramManager {

    private ClassDiagram classDiagram;

    public ClassDiagramManager() {
        classDiagram = new ClassDiagram();
    }

    @Override
    public SourceProject createSourceProject(Path sourcePackagePath) {
        SourceProject sourceProject = new SourceProject();
        sourceProject.createGraph(sourcePackagePath);
        classDiagram.setSinkVertices(sourceProject.getSinkVertices());
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
        return javaFXVisualization.createGraphView();
    }

    @Override
    public File exportDiagramToGraphML(Path graphMLSavePath) {
        DiagramExporter diagramExporter = new GraphMLClassDiagramExporter(classDiagram);
        return diagramExporter.exportDiagram(graphMLSavePath);
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
