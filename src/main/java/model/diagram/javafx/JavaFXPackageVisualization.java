package model.diagram.javafx;

import com.brunomnsilva.smartgraph.graph.Digraph;
import com.brunomnsilva.smartgraph.graph.DigraphEdgeList;
import com.brunomnsilva.smartgraph.graph.Graph;
import com.brunomnsilva.smartgraph.graph.Vertex;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import java.util.Collection;
import java.util.Set;
import model.diagram.PackageDiagram;
import model.graph.Arc;
import model.graph.ArcType;
import model.graph.PackageVertex;
import model.graph.VertexType;

public class JavaFXPackageVisualization implements JavaFXVisualization {

    private final PackageDiagram packageDiagram;
    private SmartGraphPanel<String, String> graphView;
    private Collection<Vertex<String>> vertexCollection;

    public JavaFXPackageVisualization(PackageDiagram diagram) {
        this.packageDiagram = diagram;
    }

    @Override
    public SmartGraphPanel<String, String> createGraphView() {
        Graph<String, String> graph = createGraph();
        vertexCollection = graph.vertices();
        graphView = SmartGraphFactory.createGraphView(graph);
        setVertexCustomStyle();

        return graphView;
    }

    @Override
    public Collection<Vertex<String>> getVertexCollection() {
        return vertexCollection;
    }

    private Graph<String, String> createGraph() {
        Digraph<String, String> directedGraph = new DigraphEdgeList<>();
        for (PackageVertex vertex : packageDiagram.getDiagram().keySet()) {
            if (vertex.getSinkVertices().isEmpty()) continue;

            directedGraph.insertVertex(vertex.getName());
        }
        insertVertexArcs(directedGraph);

        return directedGraph;
    }

    private void insertVertexArcs(Digraph<String, String> directedGraph) {
        for (Set<Arc<PackageVertex>> arcs : packageDiagram.getDiagram().values()) {
            for (Arc<PackageVertex> arc : arcs) {
                if (arc.arcType().equals(ArcType.AGGREGATION)) {
                    directedGraph.insertEdge(
                            arc.targetVertex().getName(),
                            arc.sourceVertex().getName(),
                            arc.targetVertex().getName()
                                    + "_"
                                    + arc.sourceVertex().getName()
                                    + "_"
                                    + arc.arcType());
                } else {
                    directedGraph.insertEdge(
                            arc.sourceVertex().getName(),
                            arc.targetVertex().getName(),
                            arc.sourceVertex().getName()
                                    + "_"
                                    + arc.targetVertex().getName()
                                    + "_"
                                    + arc.arcType());
                }
            }
        }
    }

    private void setVertexCustomStyle() {
        for (PackageVertex vertex : packageDiagram.getDiagram().keySet()) {
            if (vertex.getVertexType().equals(VertexType.INTERFACE)) {
                graphView.getStylableVertex(vertex.getName()).setStyleClass("vertexInterface");
            } else {
                if (vertex.getSinkVertices().isEmpty()) continue;

                graphView.getStylableVertex(vertex.getName()).setStyleClass("vertexPackage");
            }
        }
    }

    @Override
    public SmartGraphPanel<String, String> getLoadedGraph() {
        for (Vertex<String> vertex : vertexCollection) {
            for (PackageVertex packageVertex : packageDiagram.getDiagram().keySet()) {
                if (!packageVertex.getName().equals(vertex.element())) continue;

                graphView.setVertexPosition(
                        vertex,
                        packageVertex.getCoordinate().x(),
                        packageVertex.getCoordinate().y());
                break;
            }
        }

        return graphView;
    }
}
