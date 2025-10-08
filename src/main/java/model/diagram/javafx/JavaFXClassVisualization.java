package model.diagram.javafx;

import com.brunomnsilva.smartgraph.graph.Digraph;
import com.brunomnsilva.smartgraph.graph.DigraphEdgeList;
import com.brunomnsilva.smartgraph.graph.Graph;
import com.brunomnsilva.smartgraph.graph.Vertex;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import java.util.Collection;
import java.util.Set;
import model.diagram.ClassDiagram;
import model.graph.Arc;
import model.graph.ArcType;
import model.graph.ClassifierVertex;
import model.graph.VertexType;

public class JavaFXClassVisualization implements JavaFXVisualization {

    private final ClassDiagram classDiagram;
    private SmartGraphPanel<JavaFXUMLNode, String> graphView;
    private Collection<Vertex<JavaFXUMLNode>> vertexCollection;

    public JavaFXClassVisualization(ClassDiagram diagram) {
        this.classDiagram = diagram;
    }

    @Override
    public SmartGraphPanel<JavaFXUMLNode, String> createGraphView() {
        Graph<JavaFXUMLNode, String> graph = createGraph();
        vertexCollection = graph.vertices();
        graphView = SmartGraphFactory.createGraphView(graph);
        setSinkVertexCustomStyle();
        return graphView;
    }

    private Graph<JavaFXUMLNode, String> createGraph() {
        Digraph<JavaFXUMLNode, String> directedGraph = new DigraphEdgeList<>();
        for (ClassifierVertex classifierVertex : classDiagram.getDiagram().keySet()) {
            directedGraph.insertVertex(new JavaFXClassNode(classifierVertex.getName()));
        }
        insertSinkVertexArcs(directedGraph);

        return directedGraph;
    }

    private void insertSinkVertexArcs(Digraph<JavaFXUMLNode, String> directedGraph) {
        for (Set<Arc<ClassifierVertex>> arcs : classDiagram.getDiagram().values()) {
            for (Arc<ClassifierVertex> arc : arcs) {
                if (arc.arcType().equals(ArcType.AGGREGATION)) {
                    directedGraph.insertEdge(
                            new JavaFXClassNode(arc.targetVertex().getName()),
                            new JavaFXClassNode(arc.sourceVertex().getName()),
                            arc.targetVertex().getName()
                                    + "_"
                                    + arc.sourceVertex().getName()
                                    + "_"
                                    + arc.arcType());
                } else {
                    directedGraph.insertEdge(
                            new JavaFXClassNode(arc.sourceVertex().getName()),
                            new JavaFXClassNode(arc.targetVertex().getName()),
                            arc.sourceVertex().getName()
                                    + "_"
                                    + arc.targetVertex().getName()
                                    + "_"
                                    + arc.arcType());
                }
            }
        }
    }

    private void setSinkVertexCustomStyle() {
        for (ClassifierVertex classifierVertex : classDiagram.getDiagram().keySet()) {
            String styleClass =
                    classifierVertex.getVertexType().equals(VertexType.INTERFACE)
                            ? "vertexInterface"
                            : "vertexPackage";

            graphView
                    .getStylableVertex(new JavaFXClassNode(classifierVertex.getName()))
                    .setStyleClass(styleClass);
        }
    }

    @Override
    public Collection<Vertex<JavaFXUMLNode>> getVertexCollection() {
        return vertexCollection;
    }

    @Override
    public SmartGraphPanel<JavaFXUMLNode, String> getLoadedGraph() {
        for (Vertex<JavaFXUMLNode> vertex : vertexCollection) {
            for (ClassifierVertex classifierVertex : classDiagram.getDiagram().keySet()) {
                if (!classifierVertex.getName().equals(vertex.element())) continue;

                graphView.setVertexPosition(
                        vertex,
                        classifierVertex.getCoordinate().x(),
                        classifierVertex.getCoordinate().y());
                break;
            }
        }

        return graphView;
    }
}
