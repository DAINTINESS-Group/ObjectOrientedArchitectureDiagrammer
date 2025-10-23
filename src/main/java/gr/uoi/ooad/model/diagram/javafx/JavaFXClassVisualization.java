package gr.uoi.ooad.model.diagram.javafx;

import com.brunomnsilva.smartgraph.graph.*;
import com.brunomnsilva.smartgraph.graphview.SmartGraphEdge;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import gr.uoi.ooad.model.diagram.ClassDiagram;
import gr.uoi.ooad.model.graph.Arc;
import gr.uoi.ooad.model.graph.ArcType;
import gr.uoi.ooad.model.graph.ClassifierVertex;
import gr.uoi.ooad.model.graph.VertexType;
import gr.uoi.smartgraph.graphview.element.UMLEdgeElement;
import gr.uoi.smartgraph.graphview.element.UMLEdgeElementFactory;
import gr.uoi.smartgraph.graphview.element.UMLNodeElement;
import gr.uoi.smartgraph.graphview.element.UMLNodeElementFactory;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class JavaFXClassVisualization implements JavaFXVisualization {

    private final ClassDiagram classDiagram;
    private SmartGraphPanel<UMLNodeElement, UMLEdgeElement> graphView;
    private Collection<Vertex<UMLNodeElement>> vertexCollection;
    private Map<String, UMLNodeElement> vertexElementsMap = new HashMap<>();

    public JavaFXClassVisualization(ClassDiagram diagram) {
        this.classDiagram = diagram;
    }

    @Override
    public SmartGraphPanel<UMLNodeElement, UMLEdgeElement> createGraphView() {
        Graph<UMLNodeElement, UMLEdgeElement> graph = createGraph();
        vertexCollection = graph.vertices();
        graphView = SmartGraphFactory.createGraphView(graph);
        setSinkVertexCustomStyle();
        setEdgeCustomStyle();
        return graphView;
    }

    private void setEdgeCustomStyle() {

        Collection<SmartGraphEdge<UMLEdgeElement, UMLNodeElement>> smartEdges =
                graphView.getSmartEdges();
        for (SmartGraphEdge<UMLEdgeElement, UMLNodeElement> smartEdge : smartEdges) {
            Edge<UMLEdgeElement, UMLNodeElement> edge = smartEdge.getUnderlyingEdge();
            //            if (edge.element().contains())
            // TODO: Introduce UMLEdgeElement(source, target, type) class
            // TODO: Rename JavaFXUMLNode to UMLNodeElement
            UMLEdgeElement element = edge.element();
        }
    }

    private Graph<UMLNodeElement, UMLEdgeElement> createGraph() {
        Digraph<UMLNodeElement, UMLEdgeElement> directedGraph = new DigraphEdgeList<>();
        for (ClassifierVertex classifierVertex : classDiagram.getDiagram().keySet()) {
            UMLNodeElement jfxNode = UMLNodeElementFactory.createClassifierNode(classifierVertex);
            vertexElementsMap.put(jfxNode.getName(), jfxNode);
            directedGraph.insertVertex(jfxNode);
        }
        insertSinkVertexArcs(directedGraph);

        return directedGraph;
    }

    private UMLNodeElement getJFXNode(ClassifierVertex cVertex) {
        UMLNodeElement node = vertexElementsMap.get(cVertex.getName());
        if (node == null) {
            node = UMLNodeElementFactory.createClassifierNode(cVertex);
        }
        return node;
    }

    private void insertSinkVertexArcs(Digraph<UMLNodeElement, UMLEdgeElement> directedGraph) {
        for (Set<Arc<ClassifierVertex>> arcs : classDiagram.getDiagram().values()) {
            for (Arc<ClassifierVertex> arc : arcs) {
                UMLNodeElement sourceNode = getJFXNode(arc.sourceVertex());
                UMLNodeElement targetNode = getJFXNode(arc.targetVertex());

                if (arc.arcType().equals(ArcType.AGGREGATION)) {
                    directedGraph.insertEdge(
                            sourceNode,
                            targetNode,
                            UMLEdgeElementFactory.createFromClassifierArc(arc));
                } else {
                    directedGraph.insertEdge(
                            sourceNode,
                            targetNode,
                            UMLEdgeElementFactory.createFromClassifierArc(arc));
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

            UMLNodeElement node = getJFXNode(classifierVertex);
            graphView.getStylableVertex(node).setStyleClass(styleClass);
        }
    }

    @Override
    public Collection<Vertex<UMLNodeElement>> getVertexCollection() {
        return vertexCollection;
    }

    @Override
    public SmartGraphPanel<UMLNodeElement, UMLEdgeElement> getLoadedGraph() {
        for (Vertex<UMLNodeElement> vertex : vertexCollection) {
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
