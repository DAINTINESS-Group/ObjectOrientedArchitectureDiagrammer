package model.diagram.arrangement;

import edu.uci.ics.jung.algorithms.layout.AbstractLayout;
import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.FRLayout2;
import edu.uci.ics.jung.algorithms.layout.KKLayout;
import edu.uci.ics.jung.algorithms.layout.SpringLayout;
import edu.uci.ics.jung.algorithms.layout.SpringLayout2;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;

import model.diagram.ClassDiagram;
import model.graph.Arc;
import model.graph.ClassifierVertex;
import org.javatuples.Pair;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.util.*;
import java.util.List;
import java.util.function.Function;

import javax.swing.JButton;
import javax.swing.JFrame;

public class ClassDiagramArrangement implements DiagramArrangement{
    private final Map<Integer, Pair<Double, Double>> nodesGeometry;
    private final ClassDiagram classDiagram;
    private Map<String, String> vertexTypes;
    private Map<String, String> edgeTypes;


    public ClassDiagramArrangement(ClassDiagram classDiagram) {
        this.classDiagram = classDiagram;
        nodesGeometry = new HashMap<>();
        vertexTypes = new HashMap<>();
        edgeTypes = new HashMap<>();
    }

    @Override
    public Map<Integer, Pair<Double, Double>> arrangeDiagram() {
        Graph<Integer, String> graph = createGraph();
        AbstractLayout<Integer, String> layout = new SpringLayout<>(graph);
        layout.setSize(new Dimension(1500, 1000));
        
        Graph<String, String> graph2 = createGraphWithStrings();
        AbstractLayout<String, String> layout2 = new FRLayout2<>(graph2);
        layout2.setSize(new Dimension(900, 800));
        VisualizationViewer<String, String> vv = new VisualizationViewer<String, String>(layout2);
        vv.setPreferredSize(new Dimension(1000, 900));
        vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
        vv.getRenderContext().setEdgeLabelTransformer(edge -> "");
        Function<String, Shape> vertexShapeTransformer = v -> new Rectangle2D.Double(-20, -10, 80, 20); // 3rd parameter is for the x size
        vv.getRenderContext().setVertexShapeTransformer(vertexShapeTransformer::apply);
        vv.getRenderContext().setVertexFillPaintTransformer(v -> {
            if (vertexTypes.get(v) == "INTERFACE") {
                return Color.CYAN;
            } else {
                return Color.ORANGE;
            }
        });
        vv.getRenderContext().setEdgeStrokeTransformer(e -> {
            if (edgeTypes.get(e) == "DEPENDENCY" || edgeTypes.get(e) == "IMPLEMENTATION" ) {
                return new BasicStroke(1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] {2}, 0);
          //  } else if (edgeTypes.get(e) == "EXTENSION" ){
          //  	return new BasicStroke(1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] {2}, 0);
          //      return new BasicStroke();
          //  } else if (edgeTypes.get(e) == "AGGREGATION"){
          //      return new BasicStroke(1f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_ROUND, 0, new float[] {2}, 0);
            } else {
            	return new BasicStroke();
            }
        });
        
        vv.getRenderer().getVertexLabelRenderer().setPosition(Position.CNTR);
        DefaultModalGraphMouse<String, Integer> gm = new DefaultModalGraphMouse<>();
        gm.setMode(ModalGraphMouse.Mode.PICKING);
        vv.setGraphMouse(gm);
        JButton layoutChangeButton = new JButton("Change Layout");
        layoutChangeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	AbstractLayout<String, String> frLayout = new KKLayout<>(graph2);
                vv.setGraphLayout(frLayout);
                vv.repaint();
            }
        });
        JFrame frame = new JFrame("UML Class Diagram Visualization");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(vv, BorderLayout.CENTER);
        frame.add(layoutChangeButton, BorderLayout.SOUTH);
        frame.pack();
        frame.setVisible(true);

        
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

        List<Arc<ClassifierVertex>> arcs = new ArrayList<>();
        for (Set<Arc<ClassifierVertex>> arcSet: classDiagram.getDiagram().values()) {
            arcs.addAll(arcSet);
        }

        for (Arc<ClassifierVertex> arc: arcs) {
            graph.addEdge(classDiagram.getGraphNodes().get(arc.getSourceVertex()) + " " + classDiagram.getGraphNodes().get(arc.getTargetVertex()),
                    classDiagram.getGraphNodes().get(arc.getSourceVertex()), classDiagram.getGraphNodes().get(arc.getTargetVertex()), EdgeType.DIRECTED);
        }

        return graph;
    }
    
    private Graph<String, String> createGraphWithStrings(){
        Graph<String, String> graph = new SparseGraph<>();
        for (ClassifierVertex vertex: classDiagram.getGraphNodes().keySet()) {
            graph.addVertex(vertex.getName());
            vertexTypes.put(vertex.getName(), vertex.getVertexType().toString());
        }

        List<Arc<ClassifierVertex>> arcs = new ArrayList<>();
        for (Set<Arc<ClassifierVertex>> arcSet: classDiagram.getDiagram().values()) {
            arcs.addAll(arcSet);
        }

        for (Arc<ClassifierVertex> arc: arcs) {
        	graph.addEdge(arc.getSourceVertex().getName() + " " + arc.getTargetVertex().getName(), arc.getSourceVertex().getName(), arc.getTargetVertex().getName(), EdgeType.DIRECTED);
        	edgeTypes.put(arc.getSourceVertex().getName() + " " + arc.getTargetVertex().getName(), arc.getArcType().toString());
        }

        return graph;
    }
    
}