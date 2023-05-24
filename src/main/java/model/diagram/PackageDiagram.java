package model.diagram;

import model.diagram.graphml.GraphMLPackageEdge;
import model.diagram.graphml.GraphMLPackageNode;
import model.diagram.plantuml.PlantUMLEdge;
import model.diagram.plantuml.PlantUMLExporter;
import model.diagram.plantuml.PlantUMLPackageNode;
import model.tree.node.Node;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class PackageDiagram extends Diagram {

    public List<Node> getChosenNodes(List<String> chosenPackagesNames) {
        List<Node> chosenPackages = new ArrayList<>();
        for (String chosenPackage: chosenPackagesNames) {
            if (isPackageValid(chosenPackage)) {
                chosenPackages.add(sourceProject.getPackageNodes().get(Paths.get(chosenPackage)));
            }
        }
        return chosenPackages;
    }

    public void createCollections() {
        graphNodeCollection = new GraphMLPackageNode();
        graphEdgeCollection = new GraphMLPackageEdge();
        graphNodePlantCollection = new PlantUMLPackageNode();
        graphEdgePlantCollection = new PlantUMLEdge();
    }

    private boolean isPackageValid(String chosenPackage) {
        return sourceProject.getPackageNodes().get(Paths.get(chosenPackage)).isValid();
    }
    
    public void exportPlantUMLDiagram(Path graphSavePath) {
    	graphNodePlantCollection.convertPackageNodesToPlantUML();
    	graphEdgePlantCollection.convertEdgesToPlantUML();
    	PlantUMLExporter plantUMLExporter = new PlantUMLExporter(graphSavePath, graphNodePlantCollection.getPlantUMLBuffer(), graphEdgePlantCollection.getPlantUMLBuffer());
    	plantUMLExporter.exportPackageDiagram();
    }
    
    public void exportPlantUMLText(Path textSavePath) {
    	graphNodePlantCollection.convertPackageNodesToPlantUML();
    	graphEdgePlantCollection.convertEdgesToPlantUML();
    	PlantUMLExporter plantUMLExporter = new PlantUMLExporter(textSavePath, graphNodePlantCollection.getPlantUMLBuffer(), graphEdgePlantCollection.getPlantUMLBuffer());
    	plantUMLExporter.exportPackageDiagramText();
    }

}
