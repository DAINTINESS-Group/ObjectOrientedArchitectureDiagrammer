package model.diagram.plantuml;

import model.graph.Vertex;

import java.util.Map;

public class PlantUMLPackageNode {

    private final Map<Vertex, Integer> graphNodes;

    public PlantUMLPackageNode(Map<Vertex, Integer> graphNodes) {
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
