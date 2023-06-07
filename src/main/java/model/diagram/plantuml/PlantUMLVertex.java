package model.diagram.plantuml;

import model.graph.Vertex;

import java.util.Map;

public class PlantUMLVertex {

    private final Map<Vertex, Integer> graphNodes;

    public PlantUMLVertex(Map<Vertex, Integer> graphNodes) {
        this.graphNodes = graphNodes;
    }

    public StringBuilder convertPlantPackageNode() {
        StringBuilder plantUMLBuffer = new StringBuilder();
        for (Vertex vertex : graphNodes.keySet()) {
            String convertedNode = vertex.getVertexType().toString().toLowerCase() + " " + vertex.getName() + " {\n" + "}\n";
            plantUMLBuffer.append(convertedNode).append("\n");
        }
        return plantUMLBuffer;
	}

}
