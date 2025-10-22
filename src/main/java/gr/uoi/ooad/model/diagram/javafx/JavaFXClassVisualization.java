package gr.uoi.ooad.model.diagram.javafx;

import com.brunomnsilva.smartgraph.graph.*;
import com.brunomnsilva.smartgraph.graphview.SmartGraphEdge;
import com.brunomnsilva.smartgraph.graphview.SmartGraphEdgeNode;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import gr.uoi.ooad.model.diagram.ClassDiagram;
import gr.uoi.ooad.model.graph.Arc;
import gr.uoi.ooad.model.graph.ArcType;
import gr.uoi.ooad.model.graph.ClassifierVertex;
import gr.uoi.ooad.model.graph.VertexType;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class JavaFXClassVisualization implements JavaFXVisualization {

    private final ClassDiagram classDiagram;
    private SmartGraphPanel<JavaFXUMLNode, String> graphView;
    private Collection<Vertex<JavaFXUMLNode>> vertexCollection;
    private Map<String, JavaFXUMLNode> vertexElementsMap = new HashMap<>();

    public JavaFXClassVisualization(ClassDiagram diagram) {
        this.classDiagram = diagram;
    }

    @Override
    public SmartGraphPanel<JavaFXUMLNode, String> createGraphView() {
        Graph<JavaFXUMLNode, String> graph = createGraph();
        vertexCollection = graph.vertices();
        graphView = SmartGraphFactory.createGraphView(graph);
        setSinkVertexCustomStyle();
        setEdgeCustomStyle();
        return graphView;
    }

    private void setEdgeCustomStyle() {

        Collection<SmartGraphEdge<String, JavaFXUMLNode>> smartEdges = graphView.getSmartEdges();
        for(SmartGraphEdge<String, JavaFXUMLNode> smartEdge: smartEdges){
            Edge<String, JavaFXUMLNode> edge = smartEdge.getUnderlyingEdge();
//            if (edge.element().contains())
            // TODO: Introduce UMLEdgeElement(source, target, type) class
            // TODO: Rename JavaFXUMLNode to UMLNodeElement
            String element = edge.element();

        }

    }

    private Graph<JavaFXUMLNode, String> createGraph() {
        Digraph<JavaFXUMLNode, String> directedGraph = new DigraphEdgeList<>();
        for (ClassifierVertex classifierVertex : classDiagram.getDiagram().keySet()) {
            JavaFXUMLNode jfxNode = JavaFXUMLNodeFactory.createClassifierNode(classifierVertex);
            vertexElementsMap.put(jfxNode.getName(), jfxNode);
            directedGraph.insertVertex(jfxNode);
        }
        insertSinkVertexArcs(directedGraph);

        return directedGraph;
    }

    private JavaFXUMLNode getJFXNode(ClassifierVertex cVertex) {
        JavaFXUMLNode node = vertexElementsMap.get(cVertex.getName());
        if (node == null) {
            node = JavaFXUMLNodeFactory.createClassifierNode(cVertex);
        }
        return node;
    }

    private void insertSinkVertexArcs(Digraph<JavaFXUMLNode, String> directedGraph) {
        for (Set<Arc<ClassifierVertex>> arcs : classDiagram.getDiagram().values()) {
            for (Arc<ClassifierVertex> arc : arcs) {
                JavaFXUMLNode sourceNode = getJFXNode(arc.sourceVertex());
                JavaFXUMLNode targetNode = getJFXNode(arc.targetVertex());

                if (arc.arcType().equals(ArcType.AGGREGATION)) {
                    directedGraph.insertEdge(
                            sourceNode,
                            targetNode,
                            arc.targetVertex().getName()
                                    + "_"
                                    + arc.sourceVertex().getName()
                                    + "_"
                                    + arc.arcType());
                } else {
                    directedGraph.insertEdge(
                            sourceNode,
                            targetNode,
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
                            : "vertexClass";

            JavaFXUMLNode node = getJFXNode(classifierVertex);
            graphView.getStylableVertex(node).setStyleClass(styleClass);
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
