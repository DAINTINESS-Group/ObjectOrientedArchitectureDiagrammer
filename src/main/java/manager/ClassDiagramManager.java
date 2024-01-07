package manager;

import com.brunomnsilva.smartgraph.graph.Vertex;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import model.diagram.ClassDiagram;
import model.diagram.ShadowCleaner;
import model.diagram.arrangement.ClassDiagramArrangementManager;
import model.diagram.arrangement.DiagramArrangementManagerInterface;
import model.diagram.arrangement.geometry.DiagramGeometry;
import model.diagram.exportation.CoordinatesUpdater;
import model.diagram.exportation.DiagramExporter;
import model.diagram.exportation.GraphMLClassDiagramExporter;
import model.diagram.exportation.JavaFXClassDiagramExporter;
import model.diagram.exportation.PlantUMLClassDiagramImageExporter;
import model.diagram.exportation.PlantUMLClassDiagramTextExporter;
import model.diagram.javafx.JavaFXClassDiagramLoader;
import model.diagram.javafx.JavaFXClassVisualization;
import model.diagram.javafx.JavaFXVisualization;
import org.javatuples.Pair;

import java.io.File;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;

public class ClassDiagramManager implements DiagramManager
{

    private ClassDiagram                       classDiagram;
    private DiagramArrangementManagerInterface classDiagramArrangement;
    private Collection<Vertex<String>>         vertexCollection;
    private SmartGraphPanel<String, String>    graphView;


    public ClassDiagramManager()
    {
        classDiagram = new ClassDiagram();
    }


    @Override
    public SourceProject createSourceProject(Path sourcePackagePath)
    {
        SourceProject sourceProject = new SourceProject();
        sourceProject.createClassGraph(sourcePackagePath, classDiagram);
        return sourceProject;
    }


    @Override
    public void convertTreeToDiagram(List<String> chosenFilesNames)
    {
        classDiagram.createNewDiagram(chosenFilesNames);
        ShadowCleaner shadowCleaner = new ShadowCleaner(classDiagram);
        classDiagram.setDiagram(shadowCleaner.shadowWeakRelationships());
    }


    @Override
    public DiagramGeometry arrangeDiagram()
    {
        classDiagramArrangement         = new ClassDiagramArrangementManager(classDiagram);
        DiagramGeometry diagramGeometry = classDiagramArrangement.arrangeDiagram();
        classDiagram.setDiagramGeometry(diagramGeometry);
        return diagramGeometry;
    }


    @Override
    public SmartGraphPanel<String, String> visualizeJavaFXGraph()
    {
        JavaFXVisualization javaFXVisualization = new JavaFXClassVisualization(classDiagram);
        graphView                               = javaFXVisualization.createGraphView();
        vertexCollection                        = javaFXVisualization.getVertexCollection();
        return graphView;
    }


    @Override
    public SmartGraphPanel<String, String> visualizeLoadedJavaFXGraph()
    {
        JavaFXVisualization javaFXVisualization = new JavaFXClassVisualization(classDiagram);
        javaFXVisualization.createGraphView();

        graphView        = javaFXVisualization.getLoadedGraph();
        vertexCollection = javaFXVisualization.getVertexCollection();
        return graphView;
    }


    @Override
    public File exportDiagramToGraphML(Path graphMLSavePath)
    {
        classDiagram.setGraphMLDiagramGeometry(classDiagramArrangement.arrangeGraphMLDiagram());
        DiagramExporter diagramExporter = new GraphMLClassDiagramExporter(classDiagram);
        return diagramExporter.exportDiagram(graphMLSavePath);
    }


    @Override
    public File exportPlantUMLImage(Path plantUMLSavePath)
    {
        DiagramExporter diagramExporter = new PlantUMLClassDiagramImageExporter(classDiagram);
        return diagramExporter.exportDiagram(plantUMLSavePath);
    }


    @Override
    public File exportPlantUMLText(Path textSavePath)
    {
        DiagramExporter diagramExporter = new PlantUMLClassDiagramTextExporter(classDiagram);
        return diagramExporter.exportDiagram(textSavePath);
    }


    @Override
    public File saveDiagram(Path graphSavePath)
    {
        CoordinatesUpdater coordinatesUpdater = new CoordinatesUpdater(classDiagram);
        coordinatesUpdater.updateClassCoordinates(vertexCollection, graphView);

        DiagramExporter diagramExporter = new JavaFXClassDiagramExporter(classDiagram);
        return diagramExporter.exportDiagram(graphSavePath);
    }


    @Override
    public void loadDiagram(Path graphSavePath)
    {
        JavaFXClassDiagramLoader javaFXClassDiagramLoader = new JavaFXClassDiagramLoader(graphSavePath);

        classDiagram = new ClassDiagram();
        classDiagram.createDiagram(javaFXClassDiagramLoader.loadDiagram());

        ShadowCleaner shadowCleaner = new ShadowCleaner(classDiagram);
        classDiagram.setDiagram(shadowCleaner.shadowWeakRelationships());
    }


    public ClassDiagram getClassDiagram()
    {
        return classDiagram;
    }

    @Override
    public SmartGraphPanel<String, String> applyLayout()
    {
        DiagramGeometry nodesGeometry = classDiagram.getDiagramGeometry();
        for (Vertex<String> vertex : vertexCollection)
        {
            if (!nodesGeometry.containsKey(vertex.element()))
            {
                continue;
            }

            Pair<Double, Double> coordinates = nodesGeometry.getVertexGeometry(vertex.element());
            graphView.setVertexPosition(vertex, coordinates.getValue0(), coordinates.getValue1());
        }

        return graphView;
    }

    @Override
    public SmartGraphPanel<String, String> applySpecificLayout(String choice)
    {
        DiagramGeometry nodesGeometry = classDiagramArrangement.applyNewLayout(choice);
        for (Vertex<String> vertex : vertexCollection)
        {
            if (!nodesGeometry.containsKey(vertex.element()))
            {
                continue;
            }

            Pair<Double, Double> coordinates = nodesGeometry.getVertexGeometry(vertex.element());
            graphView.setVertexPosition(vertex,
                                        coordinates.getValue0(),
                                        coordinates.getValue1());
        }

        return graphView;
    }

}
