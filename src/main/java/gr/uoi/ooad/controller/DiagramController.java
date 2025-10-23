package gr.uoi.ooad.controller;

import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import gr.uoi.ooad.manager.DiagramManager;
import gr.uoi.ooad.manager.DiagramManagerFactory;
import gr.uoi.ooad.manager.SourceProject;
import gr.uoi.smartgraph.graphview.element.UMLEdgeElement;
import gr.uoi.smartgraph.graphview.element.UMLNodeElement;
import java.io.File;
import java.nio.file.Path;
import java.util.List;

public class DiagramController implements Controller {

    private final DiagramManager diagramManager;

    public DiagramController(String diagramType) {
        diagramManager = DiagramManagerFactory.createDiagramManager(diagramType);
    }

    @Override
    public SourceProject createTree(Path sourcePackagePath) {
        return diagramManager.createSourceProject(sourcePackagePath);
    }

    @Override
    public void convertTreeToDiagram(List<String> chosenClassesNames) {
        diagramManager.convertTreeToDiagram(chosenClassesNames);
    }

    @Override
    public void arrangeDiagram() {
        diagramManager.arrangeDiagram();
    }

    @Override
    public SmartGraphPanel<UMLNodeElement, UMLEdgeElement> applyLayout() {
        return diagramManager.applyLayout();
    }

    @Override
    public SmartGraphPanel<UMLNodeElement, UMLEdgeElement> applySpecificLayout(String choice) {
        return diagramManager.applySpecificLayout(choice);
    }

    @Override
    public File exportDiagramToGraphML(Path graphMLSavePath) {
        return diagramManager.exportDiagramToGraphML(graphMLSavePath);
    }

    @Override
    public File exportPlantUMLDiagram(Path plantUMLSavePath) {
        return diagramManager.exportPlantUMLImage(plantUMLSavePath);
    }

    @Override
    public File exportPlantUMLText(Path textSavePath) {
        return diagramManager.exportPlantUMLText(textSavePath);
    }

    @Override
    public SmartGraphPanel<UMLNodeElement, UMLEdgeElement> visualizeJavaFXGraph() {
        return diagramManager.visualizeJavaFXGraph();
    }

    @Override
    public String visualizeSvgGraph(int dpi) {
        return diagramManager.visualizeSvgGraph(dpi);
    }

    @Override
    public SmartGraphPanel<UMLNodeElement, UMLEdgeElement> visualizeLoadedJavaFXGraph() {
        return diagramManager.visualizeLoadedJavaFXGraph();
    }

    @Override
    public File saveDiagram(Path graphSavePath) {
        return diagramManager.saveDiagram(graphSavePath);
    }

    @Override
    public void loadDiagram(Path graphSavePath) {
        diagramManager.loadDiagram(graphSavePath);
    }
}
