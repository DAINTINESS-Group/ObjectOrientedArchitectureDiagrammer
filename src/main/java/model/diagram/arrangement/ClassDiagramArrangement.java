package model.diagram.arrangement;

import edu.uci.ics.jung.algorithms.layout.AbstractLayout;
import edu.uci.ics.jung.algorithms.layout.SpringLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import model.diagram.ClassDiagram;
import model.graph.Arc;
import model.graph.SinkVertex;
import org.javatuples.Pair;

import java.awt.*;
import java.util.*;
import java.util.List;

public class ClassDiagramArrangement implements DiagramArrangement{
    private final Map<Integer, Pair<Double, Double>> nodesGeometry;
    private final ClassDiagram classDiagram;


    public ClassDiagramArrangement(ClassDiagram classDiagram) {
        this.classDiagram = classDiagram;
        nodesGeometry = new HashMap<>();
    }

    @Override
    public Map<Integer, Pair<Double, Double>> arrangeDiagram() {
        Graph<Integer, String> graph = createGraph();
        AbstractLayout<Integer, String> layout = new SpringLayout<>(graph);
        layout.setSize(new Dimension(1500, 1000));
        for (Integer i : classDiagram.getGraphNodes().values()) {
            nodesGeometry.put(i, new Pair<>(layout.getX(i), layout.getY(i)));
        }
        return nodesGeometry;
    }

    private Graph<Integer, String> createGraph(){
        Graph<Integer, String> graph = new SparseGraph<>();
        for (Integer nodeId: classDiagram.getGraphNodes().values()) {
            graph.addVertex(nodeId);
        }

        List<Arc<SinkVertex>> arcs = new ArrayList<>();
        for (Set<Arc<SinkVertex>> arcSet: classDiagram.getDiagram().values()) {
            arcs.addAll(arcSet);
        }

        for (Arc<SinkVertex> arc: arcs) {
            graph.addEdge(classDiagram.getGraphNodes().get(arc.getSourceVertex()) + " " + classDiagram.getGraphNodes().get(arc.getTargetVertex()),
                    classDiagram.getGraphNodes().get(arc.getSourceVertex()), classDiagram.getGraphNodes().get(arc.getTargetVertex()), EdgeType.DIRECTED);
        }

        return graph;
    }

}