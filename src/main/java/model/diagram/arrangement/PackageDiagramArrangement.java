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
import model.diagram.PackageDiagram;
import model.graph.Arc;
import model.graph.PackageVertex;
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

public class PackageDiagramArrangement implements DiagramArrangement {

    private final Map<Integer, Pair<Double, Double>> nodesGeometry;
    private final PackageDiagram packageDiagram;

    public PackageDiagramArrangement(PackageDiagram packageDiagram) {
        this.packageDiagram = packageDiagram;
        nodesGeometry = new HashMap<>();
    }

    @Override
    public Map<Integer, Pair<Double, Double>> arrangeDiagram() {
        Graph<Integer, String> graph = populatePackageGraph();
        AbstractLayout<Integer, String> layout = new SpringLayout<>(graph);
        layout.setSize(new Dimension(1500, 1000));
        
        Graph<String, String> graph2 = populatePackageGraphWithStrings();
        AbstractLayout<String, String> layout2 = new FRLayout2<>(graph2);
        layout2.setSize(new Dimension(900, 800));
        VisualizationViewer<String, String> vv = new VisualizationViewer<String, String>(layout2);
        vv.setPreferredSize(new Dimension(1000, 900));
        vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
        vv.getRenderContext().setEdgeLabelTransformer(edge -> "");
        Function<String, Shape> vertexShapeTransformer = v -> new Rectangle2D.Double(-20, -10, 80, 20); // 3rd parameter is for the x size
        vv.getRenderContext().setVertexShapeTransformer(vertexShapeTransformer::apply);
        vv.getRenderContext().setVertexFillPaintTransformer(v -> {return Color.ORANGE;});
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
        JFrame frame = new JFrame("UML Package Diagram Visualization");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(vv, BorderLayout.CENTER);
        frame.add(layoutChangeButton, BorderLayout.SOUTH);
        frame.pack();
        frame.setVisible(true);
        
        for (Integer i : packageDiagram.getGraphNodes().values()) {
            nodesGeometry.put(i, new Pair<>(layout.getX(i), layout.getY(i)));
        }
        return nodesGeometry;
    }

    private Graph<Integer, String> populatePackageGraph() {
        Graph<Integer, String> graph = new SparseGraph<>();
        for (Integer i : packageDiagram.getGraphNodes().values()) {
            graph.addVertex(i);
        }

        List<Arc<PackageVertex>> arcs = new ArrayList<>();
        for (Set<Arc<PackageVertex>> arcSet: packageDiagram.getDiagram().values()) {
            arcs.addAll(arcSet);
        }

        for (Arc<PackageVertex> arc: arcs) {
            graph.addEdge(packageDiagram.getGraphNodes().get(arc.getSourceVertex()) + " " + packageDiagram.getGraphNodes().get(arc.getTargetVertex()),
                    packageDiagram.getGraphNodes().get(arc.getSourceVertex()), packageDiagram.getGraphNodes().get(arc.getTargetVertex()), EdgeType.DIRECTED);
        }

        return graph;
    }

    private Graph<String, String> populatePackageGraphWithStrings(){
        Graph<String, String> graph = new SparseGraph<>();
        for (PackageVertex vertex: packageDiagram.getGraphNodes().keySet()) {
            graph.addVertex(vertex.getName());
        }

        List<Arc<PackageVertex>> arcs = new ArrayList<>();
        for (Set<Arc<PackageVertex>> arcSet: packageDiagram.getDiagram().values()) {
            arcs.addAll(arcSet);
        }

        for (Arc<PackageVertex> arc: arcs) {
        	graph.addEdge(arc.getSourceVertex().getName() + " " + arc.getTargetVertex().getName(), arc.getSourceVertex().getName(), arc.getTargetVertex().getName(), EdgeType.DIRECTED);
        }

        return graph;
    }
    
}
