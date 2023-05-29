package model.diagram;

import model.diagram.plantuml.PlantUMLExporter;
import model.tree.node.Node;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PackageDiagram extends Diagram {

    public List<Node> getChosenNodes(List<String> chosenPackagesNames) {
        List<Node> chosenPackages = new ArrayList<>();
        for (String chosenPackage: chosenPackagesNames) {
            if (!isPackageValid(chosenPackage)) {
                continue;
            }
            chosenPackages.add(sourceProject.getPackageNodes().get(Paths.get(chosenPackage)));
        }
        return chosenPackages;
    }

    private boolean isPackageValid(String chosenPackage) {
        return sourceProject.getPackageNodes().get(Paths.get(chosenPackage)).isValid();
    }

    @Override
    public File exportPlantUMLDiagram(PlantUMLExporter plantUMLExporter) {
    	return plantUMLExporter.exportPackageDiagram();
    }

    @Override
    public File exportPlantUMLText(PlantUMLExporter plantUMLExporter) {
    	return plantUMLExporter.exportPackageDiagramText();
    }

    @Override
    protected StringBuilder convertEdgesToGraphML() {
        return graphEdgeCollection.convertPackageEdgesToGraphML();
    }

    @Override
    protected StringBuilder convertNodesToGraphML(Map<Integer, List<Double>> nodesGeometry) {
        return graphNodeCollection.convertPackageNodesToGraphML(nodesGeometry);
    }

}
