package manager;

import com.brunomnsilva.smartgraph.graph.Vertex;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import com.google.gson.JsonParseException;
import java.io.File;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import model.diagram.PackageDiagram;
import model.diagram.arrangement.DiagramArrangementManager;
import model.diagram.arrangement.PackageDiagramArrangementManager;
import model.diagram.arrangement.geometry.DiagramGeometry;
import model.diagram.exportation.CoordinatesUpdater;
import model.diagram.exportation.DiagramExporter;
import model.diagram.exportation.GraphMLPackageDiagramExporter;
import model.diagram.exportation.JavaFXPackageDiagramExporter;
import model.diagram.exportation.PlantUMLPackageDiagramImageExporter;
import model.diagram.exportation.PlantUMLPackageDiagramTextExporter;
import model.diagram.javafx.JavaFXPackageDiagramLoader;
import model.diagram.javafx.JavaFXPackageVisualization;
import model.diagram.javafx.JavaFXVisualization;
import model.diagram.svg.PlantUMLPackageDiagram;
import org.javatuples.Pair;

public class PackageDiagramManager implements DiagramManager {

    private PackageDiagram packageDiagram = new PackageDiagram();
    private DiagramArrangementManager packageDiagramArrangement;
    private Collection<Vertex<String>> vertexCollection;
    private SmartGraphPanel<String, String> graphView;

    @Override
    public Project createSourceProject(Path sourcePackagePath) {
        Project project = new Project(sourcePackagePath);
        project.initialize();
        project.createPackageGraph(packageDiagram);

        return project;
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
    public SmartGraphPanel<String, String> visualizeJavaFXGraph() {
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
    public SmartGraphPanel<String, String> visualizeLoadedJavaFXGraph() {
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
    public SmartGraphPanel<String, String> applyLayout() {
        DiagramGeometry nodesGeometry = packageDiagram.getDiagramGeometry();
        for (Vertex<String> vertex : vertexCollection) {
            if (!nodesGeometry.containsKey(vertex.element())) continue;

            Pair<Double, Double> coordinates = nodesGeometry.getVertexGeometry(vertex.element());
            graphView.setVertexPosition(vertex, coordinates.getValue0(), coordinates.getValue1());
        }

        return graphView;
    }

    @Override
    public SmartGraphPanel<String, String> applySpecificLayout(String choice) {
        DiagramGeometry nodesGeometry = packageDiagramArrangement.applyLayout(choice);
        for (Vertex<String> vertex : vertexCollection) {
            if (!nodesGeometry.containsKey(vertex.element())) continue;

            Pair<Double, Double> coordinates = nodesGeometry.getVertexGeometry(vertex.element());
            graphView.setVertexPosition(vertex, coordinates.getValue0(), coordinates.getValue1());
        }

        return graphView;
    }

    public PackageDiagram getPackageDiagram() {
        return packageDiagram;
    }
}
