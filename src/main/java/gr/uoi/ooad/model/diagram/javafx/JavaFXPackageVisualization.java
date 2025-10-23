package gr.uoi.ooad.model.diagram.javafx;

import com.brunomnsilva.smartgraph.graph.Digraph;
import com.brunomnsilva.smartgraph.graph.DigraphEdgeList;
import com.brunomnsilva.smartgraph.graph.Graph;
import com.brunomnsilva.smartgraph.graph.Vertex;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import gr.uoi.ooad.model.diagram.PackageDiagram;
import gr.uoi.ooad.model.graph.Arc;
import gr.uoi.ooad.model.graph.ArcType;
import gr.uoi.ooad.model.graph.PackageVertex;
import gr.uoi.ooad.model.graph.VertexType;
import gr.uoi.smartgraph.graphview.element.PackageNodeElement;
import gr.uoi.smartgraph.graphview.element.UMLNodeElement;
import java.util.Collection;
import java.util.Set;

public class JavaFXPackageVisualization implements JavaFXVisualization {

    private final PackageDiagram packageDiagram;
    private SmartGraphPanel<UMLNodeElement, String> graphView;
    private Collection<Vertex<UMLNodeElement>> vertexCollection;

    public JavaFXPackageVisualization(PackageDiagram diagram) {
        this.packageDiagram = diagram;
    }

    @Override
    public SmartGraphPanel<UMLNodeElement, String> createGraphView() {
        Graph<UMLNodeElement, String> graph = createGraph();
        vertexCollection = graph.vertices();
        graphView = SmartGraphFactory.createGraphView(graph);
        setVertexCustomStyle();

        return graphView;
    }

    @Override
    public Collection<Vertex<UMLNodeElement>> getVertexCollection() {
        return vertexCollection;
    }

    private Graph<UMLNodeElement, String> createGraph() {
        Digraph<UMLNodeElement, String> directedGraph = new DigraphEdgeList<>();
        for (PackageVertex vertex : packageDiagram.getDiagram().keySet()) {
            if (vertex.getSinkVertices().isEmpty()) continue;

            directedGraph.insertVertex(new PackageNodeElement(vertex.getName()));
        }
        insertVertexArcs(directedGraph);

        return directedGraph;
    }

    private void insertVertexArcs(Digraph<UMLNodeElement, String> directedGraph) {
        for (Set<Arc<PackageVertex>> arcs : packageDiagram.getDiagram().values()) {
            for (Arc<PackageVertex> arc : arcs) {
                if (arc.arcType().equals(ArcType.AGGREGATION)) {
                    directedGraph.insertEdge(
                            new PackageNodeElement(arc.targetVertex().getName()),
                            new PackageNodeElement(arc.sourceVertex().getName()),
                            arc.targetVertex().getName()
                                    + "_"
                                    + arc.sourceVertex().getName()
                                    + "_"
                                    + arc.arcType());
                } else {
                    directedGraph.insertEdge(
                            new PackageNodeElement(arc.sourceVertex().getName()),
                            new PackageNodeElement(arc.targetVertex().getName()),
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
                graphView
                        .getStylableVertex(new PackageNodeElement(vertex.getName()))
                        .setStyleClass("vertexInterface");
            } else {
                if (vertex.getSinkVertices().isEmpty()) continue;

                graphView
                        .getStylableVertex(new PackageNodeElement(vertex.getName()))
                        .setStyleClass("vertexPackage");
            }
        }
    }

    @Override
    public SmartGraphPanel<UMLNodeElement, String> getLoadedGraph() {
        for (Vertex<UMLNodeElement> vertex : vertexCollection) {
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
