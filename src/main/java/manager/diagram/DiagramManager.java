package manager.diagram;

import model.diagram.GraphEdge;
import model.diagram.GraphEdgePair;
import model.diagram.GraphNode;

import java.io.File;
import java.util.List;
import java.util.Map;

public interface DiagramManager {

    /* This method converts the tree created by the Parser to a diagram, based on the files(classes or packages) selected
     * by the designer. The type of the diagram depends on the type of files the designer has chosen and the controller
     * is responsible for creating the corresponding GraphDiagramManager that implements the createDiagram method */
    GraphEdgePair<GraphNode, GraphEdge> createDiagram(List<String> chosenFilesNames);

    /* This method arranges the created diagram's node geometry by creating a Jung Graph and then applying the SpringLayout
    algorithm, implemented by the Jung library */
    Map<Integer, List<Double>> arrangeDiagram();

    /* This method exports the created diagram to a file, to the path selected by the designer, in GraphMLFormat, by
     * converting the nodes and edges to GraphML syntax */
    File exportDiagramToGraphML(String graphMLSavePath);

    /* This method returns the graph that corresponds to the created diagram, so that the view can visualize the graph
     * based on its nodes, edges and different edge types */
    Map<String, Map<String, String>> getGraph();
}
