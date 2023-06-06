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
        for (Vertex node : graphNodes.keySet()) {
            String convertedNode = node.getVertexType().toString().toLowerCase() + " " + node.getName() + " {\n" + "}\n";
            plantUMLBuffer.append(convertedNode).append("\n");
        }
        return plantUMLBuffer;
	}

}
