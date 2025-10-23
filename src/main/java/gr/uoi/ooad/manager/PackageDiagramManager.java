package gr.uoi.ooad.manager;

import com.brunomnsilva.smartgraph.graph.Vertex;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import com.google.gson.JsonParseException;
import gr.uoi.ooad.model.diagram.PackageDiagram;
import gr.uoi.ooad.model.diagram.arrangement.DiagramArrangementManager;
import gr.uoi.ooad.model.diagram.arrangement.PackageDiagramArrangementManager;
import gr.uoi.ooad.model.diagram.arrangement.geometry.DiagramGeometry;
import gr.uoi.ooad.model.diagram.exportation.CoordinatesUpdater;
import gr.uoi.ooad.model.diagram.exportation.DiagramExporter;
import gr.uoi.ooad.model.diagram.exportation.GraphMLPackageDiagramExporter;
import gr.uoi.ooad.model.diagram.exportation.JavaFXPackageDiagramExporter;
import gr.uoi.ooad.model.diagram.exportation.PlantUMLPackageDiagramImageExporter;
import gr.uoi.ooad.model.diagram.exportation.PlantUMLPackageDiagramTextExporter;
import gr.uoi.ooad.model.diagram.javafx.JavaFXPackageDiagramLoader;
import gr.uoi.ooad.model.diagram.javafx.JavaFXPackageVisualization;
import gr.uoi.ooad.model.diagram.javafx.JavaFXVisualization;
import gr.uoi.ooad.model.diagram.svg.PlantUMLPackageDiagram;
import gr.uoi.smartgraph.graphview.element.UMLEdgeElement;
import gr.uoi.smartgraph.graphview.element.UMLNodeElement;
import java.io.File;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import org.javatuples.Pair;

public class PackageDiagramManager implements DiagramManager {

    private PackageDiagram packageDiagram;
    private DiagramArrangementManager packageDiagramArrangement;
    private Collection<Vertex<UMLNodeElement>> vertexCollection;
    private SmartGraphPanel<UMLNodeElement, UMLEdgeElement> graphView;

    public PackageDiagramManager() {
        packageDiagram = new PackageDiagram();
    }

    @Override
    public SourceProject createSourceProject(Path sourcePackagePath) {
        SourceProject sourceProject = new SourceProject();
        sourceProject.createPackageGraph(sourcePackagePath, packageDiagram);
        return sourceProject;
    }

    @Override
    public void convertTreeToDiagram(List<String> chosenFilesNames) {
        packageDiagram.createNewDiagram(chosenFilesNames);
    }

    @Override
    public DiagramGeometry arrangeDiagram() {
        packageDiagramArrangement = new PackageDiagramArrangementManager(packageDiagram);
        DiagramGeometry diagramGeometry = packageDiagramArrangement.arrangeDiagram();
        packageDiagram.setDiagramGeometry(diagramGeometry);
        return diagramGeometry;
    }

    @Override
    public SmartGraphPanel<UMLNodeElement, UMLEdgeElement> visualizeJavaFXGraph() {
        JavaFXVisualization javaFXPackageVisualization =
                new JavaFXPackageVisualization(packageDiagram);
        graphView = javaFXPackageVisualization.createGraphView();
        vertexCollection = javaFXPackageVisualization.getVertexCollection();
        return graphView;
    }

    @Override
    public String visualizeSvgGraph(int dpi) {
        PlantUMLPackageDiagram plantUMLPackageDiagram = new PlantUMLPackageDiagram(packageDiagram);
        return plantUMLPackageDiagram.toSvg(dpi);
    }

    @Override
    public SmartGraphPanel<UMLNodeElement, UMLEdgeElement> visualizeLoadedJavaFXGraph() {
        JavaFXVisualization javaFXPackageVisualization =
                new JavaFXPackageVisualization(packageDiagram);
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
        DiagramExporter diagramExporter = new PlantUMLPackageDiagramImageExporter(packageDiagram);

        return diagramExporter.exportDiagram(plantUMLSavePath);
    }

    @Override
    public File exportPlantUMLText(Path textSavePath) {
        DiagramExporter diagramExporter = new PlantUMLPackageDiagramTextExporter(packageDiagram);

        return diagramExporter.exportDiagram(textSavePath);
    }

    @Override
    public File saveDiagram(Path graphSavePath) {
        CoordinatesUpdater coordinatesUpdater = new CoordinatesUpdater(packageDiagram);
        coordinatesUpdater.updatePackageCoordinates(vertexCollection, graphView);

        DiagramExporter diagramExporter = new JavaFXPackageDiagramExporter(packageDiagram);

        return diagramExporter.exportDiagram(graphSavePath);
    }

    @Override
    public void loadDiagram(Path graphSavePath) throws JsonParseException {
        packageDiagram = new PackageDiagram();
        packageDiagram.createDiagram(JavaFXPackageDiagramLoader.loadDiagram(graphSavePath));
    }

    @Override
    public SmartGraphPanel<UMLNodeElement, UMLEdgeElement> applyLayout() {
        DiagramGeometry nodesGeometry = packageDiagram.getDiagramGeometry();
        for (Vertex<UMLNodeElement> vertex : vertexCollection) {
            if (!nodesGeometry.containsKey(vertex.element().getName())) continue;

            Pair<Double, Double> coordinates =
                    nodesGeometry.getVertexGeometry(vertex.element().getName());
            graphView.setVertexPosition(vertex, coordinates.getValue0(), coordinates.getValue1());
        }

        return graphView;
    }

    @Override
    public SmartGraphPanel<UMLNodeElement, UMLEdgeElement> applySpecificLayout(String choice) {
        DiagramGeometry nodesGeometry = packageDiagramArrangement.applyLayout(choice);
        for (Vertex<UMLNodeElement> vertex : vertexCollection) {
            if (!nodesGeometry.containsKey(vertex.element().getName())) continue;

            Pair<Double, Double> coordinates =
                    nodesGeometry.getVertexGeometry(vertex.element().getName());
            graphView.setVertexPosition(vertex, coordinates.getValue0(), coordinates.getValue1());
        }

        return graphView;
    }

    public PackageDiagram getPackageDiagram() {
        return packageDiagram;
    }
}
