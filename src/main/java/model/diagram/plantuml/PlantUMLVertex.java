package model.diagram.plantuml;

import model.graph.Arc;
import model.graph.Vertex;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class PlantUMLVertex {

    private final Map<Vertex, Set<Arc<Vertex>>> diagram;

    public PlantUMLVertex(Map<Vertex, Set<Arc<Vertex>>> diagram) {
        this.diagram = diagram;
    }

    public StringBuilder convertVertex() {
        return new StringBuilder(
            diagram.keySet().stream()
                .map(vertex -> vertex.getVertexType().toString().toLowerCase() + " " + vertex.getName() + " {\n" + "}\n")
                .collect(Collectors.joining("\n"))
        );
	}

}
