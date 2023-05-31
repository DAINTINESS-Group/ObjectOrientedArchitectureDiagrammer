package model.diagram;

import model.graph.Arc;
import model.graph.SinkVertex;
import model.graph.Vertex;

import java.util.HashMap;
import java.util.Map;

public class CollectionsDiagramConverter {

    public CollectionsDiagramConverter() {
    }

    public Map<String, Map<String, String>> convertClassCollectionsToDiagram(Map<SinkVertex, Integer> graphNodes, Map<Arc<SinkVertex>, Integer> graphEdges) {
        Map<String, Map<String, String>> graph = new HashMap<>();
        for (Map.Entry<SinkVertex, Integer> sinkVertex: graphNodes.entrySet()) {
            Map<String, String> nodeEdges = iterateClassGraphEdges(sinkVertex.getKey(), graphEdges);
            insertClassNode(graph, sinkVertex, nodeEdges);
        }
        return graph;
    }

    public Map<String, Map<String, String>> convertPackageCollectionsToDiagram(Map<Vertex, Integer> graphNodes, Map<Arc<Vertex>, Integer> graphEdges) {
        Map<String, Map<String, String>> graph = new HashMap<>();
        for (Map.Entry<Vertex, Integer> vertex: graphNodes.entrySet()) {
            Map<String, String> nodeEdges = iteratePackageGraphEdges(vertex.getKey(), graphEdges);
            insertPackageNode(graph, vertex, nodeEdges);
        }
        return graph;
    }


    private Map<String, String> iterateClassGraphEdges(SinkVertex sinkVertex, Map<Arc<SinkVertex>, Integer> graphEdges) {
        Map<String, String> nodeEdges = new HashMap<>();
        for (Arc<SinkVertex> arc: graphEdges.keySet()) {
            if (!doesClassEdgeStartFromCurrentNode(sinkVertex, arc)) {
                continue;
            }
            insertClassEdge(nodeEdges, arc);
        }
        return nodeEdges;
    }

    private void insertClassNode(Map<String, Map<String, String>> graph, Map.Entry<SinkVertex, Integer> sinkVertex, Map<String, String> nodeEdges) {
        graph.put(sinkVertex.getKey().getName() + "_" + sinkVertex.getKey().getVertexType().name(), nodeEdges);
    }

    private void insertClassEdge(Map<String, String> nodeEdges, Arc<SinkVertex> arc) {
        nodeEdges.put(arc.getTargetVertex().getName() + "_" + arc.getTargetVertex().getVertexType().name() + "_" + arc.getArcType().toString(),
                arc.getArcType().name());
    }

    private Map<String, String> iteratePackageGraphEdges(Vertex vertex, Map<Arc<Vertex>, Integer> graphEdges) {
        Map<String, String> nodeEdges = new HashMap<>();
        for (Arc<Vertex> arc: graphEdges.keySet()) {
            if (!doesPackageEdgeStartFromCurrentNode(vertex, arc)) {
                continue;
            }
            insertPackageEdge(nodeEdges, arc);
        }
        return nodeEdges;
    }

    private void insertPackageNode(Map<String, Map<String, String>> graph, Map.Entry<Vertex, Integer> vertex, Map<String, String> nodeEdges) {
        graph.put(vertex.getKey().getName() + "_" + vertex.getKey().getVertexType().name(), nodeEdges);
    }

    private void insertPackageEdge(Map<String, String> nodeEdges, Arc<Vertex> arc) {
        nodeEdges.put(arc.getTargetVertex().getName() + "_" + arc.getTargetVertex().getVertexType().name() + "_" + arc.getArcType().toString(),
                arc.getArcType().name());
    }

    private boolean doesClassEdgeStartFromCurrentNode(SinkVertex sinkVertex, Arc<SinkVertex> arc) {
        return arc.getSourceVertex().equals(sinkVertex);
    }

    private boolean doesPackageEdgeStartFromCurrentNode(Vertex vertex, Arc<Vertex> arc) {
        return arc.getSourceVertex().equals(vertex);
    }
}
