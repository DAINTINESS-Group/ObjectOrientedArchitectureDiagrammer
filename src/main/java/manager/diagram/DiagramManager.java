package manager.diagram;

import edu.uci.ics.jung.algorithms.layout.AbstractLayout;
import edu.uci.ics.jung.algorithms.layout.SpringLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import model.PackageNode;

import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.io.File;
import java.util.List;

public class DiagramManager {

    private final GraphMLNode graphMLNode;
    private final GraphMLEdge graphMLEdge;
    private FileWriter graphMLWriter;
    private final Map<String, PackageNode> packages;
    private final Map<Integer, List<Double>> nodesGeometry;
    private StringBuilder graphMLBuffer;

    public DiagramManager (Map<String, PackageNode> packageNodes) {
        this.packages = packageNodes;
        graphMLNode = new GraphMLNode();
        graphMLEdge = new GraphMLEdge();
        nodesGeometry = new HashMap<>();
        try {
            createGraphMLFile();
            parsePackages();
            arrangeDiagram();
            generateGraphMLGraph();
            closeGraphMLFile();
        }catch (IOException e){
            e.printStackTrace();
        }
        printFile();
    }

    private void createGraphMLFile() throws IOException {
        new File("all_packages.graphml");
        graphMLWriter = new FileWriter("all_packages.graphml");
        graphMLBuffer = new StringBuilder();
        graphMLBuffer.append(GraphMLSyntax.getInstance().getGraphMLPrefix());
    }

    private void closeGraphMLFile() throws IOException {
        graphMLBuffer.append(GraphMLSyntax.getInstance().getGraphMLSuffix());
        graphMLWriter.write(graphMLBuffer.toString());
        graphMLWriter.close();
    }

    private void parsePackages() {

        for (PackageNode p: packages.values()) {
            if (!p.isValid()) {
                continue;
            }
            graphMLNode.populateGraphMLNodes(p);
        }
        for (PackageNode p: packages.values()) {
            if (!p.isValid()) {
                continue;
            }
            graphMLEdge.populateGraphMLEdges(p, graphMLNode.getGraphMLNodes());
        }
    }

    private void arrangeDiagram(){
        Graph<Integer, String> graph = new SparseGraph<>();
        for (Integer i: graphMLNode.getGraphMLNodes().values()) {
            graph.addVertex(i);
        }
        for (Map.Entry<Integer, Integer> entry: graphMLEdge.getGraphMLEdges().entrySet()) {
            graph.addEdge(entry.getKey() + " " + entry.getValue(), entry.getKey(), entry.getValue(), EdgeType.DIRECTED);
        }
        AbstractLayout<Integer, String> layout = new SpringLayout(graph);
        layout.setSize(new Dimension(1500,1000));
        populateNodesGeometry(layout);
    }

    private void populateNodesGeometry(AbstractLayout<Integer, String> layout) {
        for (Integer i: graphMLNode.getGraphMLNodes().values()){
            nodesGeometry.put(i, Arrays.asList(layout.getX(i), layout.getY(i)));
        }
    }

    private void generateGraphMLGraph(){
        graphMLNode.parseGraphMLNodes(nodesGeometry);
        graphMLBuffer.append(graphMLNode.getGraphMLBuffer());
        graphMLBuffer.append(graphMLEdge.getGraphMLBuffer());
    }

    private void printFile() {
        System.out.println(graphMLBuffer);
    }

}