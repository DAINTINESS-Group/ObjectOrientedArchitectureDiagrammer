package model.diagram.javafx;

import com.brunomnsilva.smartgraph.graph.Digraph;
import com.brunomnsilva.smartgraph.graph.DigraphEdgeList;
import com.brunomnsilva.smartgraph.graph.Graph;
import com.brunomnsilva.smartgraph.graphview.SmartCircularSortedPlacementStrategy;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import model.diagram.ClassDiagram;
import model.graph.Arc;
import model.graph.ArcType;
import model.graph.SinkVertex;
import model.graph.VertexType;

import java.util.Set;

public class JavaFXClassVisualization implements model.diagram.javafx.JavaFXVisualization {

    private final ClassDiagram classDiagram;
    private SmartGraphPanel<String, String> graphView;

    public JavaFXClassVisualization(ClassDiagram diagram) {
        classDiagram = diagram;
    }

    @Override
    public SmartGraphPanel<String, String> createGraphView() {
        Graph<String, String> graph = createGraph();
        graphView = new SmartGraphPanel<>(graph, new SmartCircularSortedPlacementStrategy());
        setSinkVertexCustomStyle();
        return graphView;
    }

    private Graph<String, String> createGraph() {
        Digraph<String, String> directedGraph = new DigraphEdgeList<>();
        for (SinkVertex sinkVertex: classDiagram.getDiagram().keySet()) {
            directedGraph.insertVertex(sinkVertex.getName());
        }
        insertSinkVertexArcs(directedGraph);
        return directedGraph;
    }

    private void insertSinkVertexArcs(Digraph<String, String> directedGraph){
        for (Set<Arc<SinkVertex>> arcs : classDiagram.getDiagram().values()) {
            for (Arc<SinkVertex> arc: arcs) {
                if (arc.getArcType().equals(ArcType.AGGREGATION)) {
                    directedGraph.insertEdge(arc.getTargetVertex().getName(), arc.getSourceVertex().getName(),
                        arc.getTargetVertex().getName() + "_" + arc.getSourceVertex().getName() + "_" + arc.getArcType().toString().toLowerCase());
                }else {
                    directedGraph.insertEdge(arc.getSourceVertex().getName(), arc.getTargetVertex().getName(),
                        arc.getSourceVertex().getName() + "_" + arc.getTargetVertex().getName() + "_" + arc.getArcType().toString().toLowerCase());
                }
            }
        }
    }

    private void setSinkVertexCustomStyle() {
        for (SinkVertex sinkVertex: classDiagram.getDiagram().keySet()){
            if (sinkVertex.getVertexType().equals(VertexType.INTERFACE)) {
                graphView.getStylableVertex(sinkVertex.getName()).setStyleClass("vertexInterface");
            }else {
                graphView.getStylableVertex(sinkVertex.getName()).setStyleClass("vertexPackage");
            }
        }
    }

}
