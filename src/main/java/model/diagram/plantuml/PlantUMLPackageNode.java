package model.diagram.plantuml;

import model.graph.Vertex;

import java.util.HashMap;
import java.util.Map;

public class PlantUMLPackageNode {

    private final Map<Vertex, Integer> graphNodes;
    private final Map<String, String> plantUMLTester;

    public PlantUMLPackageNode(Map<Vertex, Integer> graphNodes) {
        this.graphNodes = graphNodes;
        plantUMLTester = new HashMap<>();
    }

    public StringBuilder convertPlantPackageNode() {
        StringBuilder plantUMLBuffer = new StringBuilder();
        for (Vertex node : graphNodes.keySet()) {
            String convertedNode = node.getVertexType().toString().toLowerCase() + " " + node.getName() + " {\n" + "}\n";
            plantUMLBuffer.append(convertedNode).append("\n");
            plantUMLTester.put(node.getName(), convertedNode);
        }
        return plantUMLBuffer;
	}

    public Map<String, String> getPlantUMLTester() {
        return plantUMLTester;
    }
}
