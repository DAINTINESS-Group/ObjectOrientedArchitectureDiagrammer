package gr.uoi.ooad.model.diagram.javafx;

import com.brunomnsilva.smartgraph.graph.*;
import com.brunomnsilva.smartgraph.graphview.SmartGraphEdge;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import gr.uoi.ooad.model.diagram.ClassDiagram;
import gr.uoi.ooad.model.graph.Arc;
import gr.uoi.ooad.model.graph.ArcType;
import gr.uoi.ooad.model.graph.ClassifierVertex;
import gr.uoi.ooad.model.graph.VertexType;
import gr.uoi.smartgraph.graphview.element.UMLNodeElement;
import gr.uoi.smartgraph.graphview.element.UMLNodeElementFactory;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class JavaFXClassVisualization implements JavaFXVisualization {

    private final ClassDiagram classDiagram;
    private SmartGraphPanel<UMLNodeElement, String> graphView;
    private Collection<Vertex<UMLNodeElement>> vertexCollection;
    private Map<String, UMLNodeElement> vertexElementsMap = new HashMap<>();

    public JavaFXClassVisualization(ClassDiagram diagram) {
        this.classDiagram = diagram;
    }

    @Override
    public SmartGraphPanel<UMLNodeElement, String> createGraphView() {
        Graph<UMLNodeElement, String> graph = createGraph();
        vertexCollection = graph.vertices();
        graphView = SmartGraphFactory.createGraphView(graph);
        setSinkVertexCustomStyle();
        setEdgeCustomStyle();
        return graphView;
    }

    private void setEdgeCustomStyle() {

        Collection<SmartGraphEdge<String, UMLNodeElement>> smartEdges = graphView.getSmartEdges();
        for (SmartGraphEdge<String, UMLNodeElement> smartEdge : smartEdges) {
            Edge<String, UMLNodeElement> edge = smartEdge.getUnderlyingEdge();
            //            if (edge.element().contains())
            // TODO: Introduce UMLEdgeElement(source, target, type) class
            // TODO: Rename JavaFXUMLNode to UMLNodeElement
            String element = edge.element();
        }
    }

    private Graph<UMLNodeElement, String> createGraph() {
        Digraph<UMLNodeElement, String> directedGraph = new DigraphEdgeList<>();
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

    private void insertSinkVertexArcs(Digraph<UMLNodeElement, String> directedGraph) {
        for (Set<Arc<ClassifierVertex>> arcs : classDiagram.getDiagram().values()) {
            for (Arc<ClassifierVertex> arc : arcs) {
                UMLNodeElement sourceNode = getJFXNode(arc.sourceVertex());
                UMLNodeElement targetNode = getJFXNode(arc.targetVertex());

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

            UMLNodeElement node = getJFXNode(classifierVertex);
            graphView.getStylableVertex(node).setStyleClass(styleClass);
        }
    }

    @Override
    public Collection<Vertex<UMLNodeElement>> getVertexCollection() {
        return vertexCollection;
    }

    @Override
    public SmartGraphPanel<UMLNodeElement, String> getLoadedGraph() {
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
