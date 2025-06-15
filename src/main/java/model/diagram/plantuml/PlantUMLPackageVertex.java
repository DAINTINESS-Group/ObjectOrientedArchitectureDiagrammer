package model.diagram.plantuml;

import model.diagram.PackageDiagram;
import model.graph.PackageVertex;

public class PlantUMLPackageVertex {

    public static StringBuilder convertVertices(PackageDiagram packageDiagram) {
        StringBuilder ret = new StringBuilder();
        for (PackageVertex it : packageDiagram.getDiagram().keySet()) {
            ret.append(it.getVertexType())
                    .append(" ")
                    .append(it.getName())
                    .append(" {")
                    .append(System.lineSeparator())
                    .append("}")
                    .append(System.lineSeparator());
        }

        return ret;
    }
}
