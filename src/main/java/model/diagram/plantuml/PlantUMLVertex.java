package model.diagram.plantuml;

import model.graph.Vertex;

import java.util.Map;
import java.util.stream.Collectors;

public class PlantUMLVertex {

    private final Map<Vertex, Integer> graphNodes;

    public PlantUMLVertex(Map<Vertex, Integer> graphNodes) {
        this.graphNodes = graphNodes;
    }

    public StringBuilder convertVertex() {
        return new StringBuilder(
            graphNodes.keySet().stream()
                .map(vertex -> vertex.getVertexType().toString().toLowerCase() + " " + vertex.getName() + " {\n" + "}\n")
                .collect(Collectors.joining("\n"))
        );
	}

}
