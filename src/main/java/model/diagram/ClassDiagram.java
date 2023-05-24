package model.diagram;

import model.diagram.graphml.GraphMLLeafEdge;
import model.diagram.graphml.GraphMLLeafNode;
import model.diagram.plantuml.PlantUMLEdge;
import model.diagram.plantuml.PlantUMLExporter;
import model.diagram.plantuml.PlantUMLLeafNode;
import model.tree.node.Node;
import model.tree.node.PackageNode;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ClassDiagram extends Diagram {

    public List<Node> getChosenNodes(List<String> chosenClassesNames) {
        List<Node> chosenClasses = new ArrayList<>();
        for (String chosenClass: chosenClassesNames) {
            for (PackageNode p: sourceProject.getPackageNodes().values()){
                if (p.getLeafNodes().containsKey(chosenClass)) {
                    chosenClasses.add(p.getLeafNodes().get(chosenClass));
                    break;
                }
            }
        }
        return chosenClasses;
    }

    public void createCollections() {
        graphNodeCollection = new GraphMLLeafNode();
        graphEdgeCollection = new GraphMLLeafEdge();
        graphNodePlantCollection = new PlantUMLLeafNode();
        graphEdgePlantCollection = new PlantUMLEdge();
    }
    
    public void exportPlantUMLDiagram(Path graphSavePath) {
    	graphNodePlantCollection.convertClassNodesToPlantUML();
    	graphEdgePlantCollection.convertEdgesToPlantUML();
    	PlantUMLExporter plantUMLExporter = new PlantUMLExporter(graphSavePath, graphNodePlantCollection.getPlantUMLBuffer(), graphEdgePlantCollection.getPlantUMLBuffer());
    	plantUMLExporter.exportClassDiagram();
    }
    
    public void exportPlantUMLText(Path textSavePath) {
    	graphNodePlantCollection.convertClassNodesToPlantUML();
    	graphEdgePlantCollection.convertEdgesToPlantUML();
    	PlantUMLExporter plantUMLExporter = new PlantUMLExporter(textSavePath, graphNodePlantCollection.getPlantUMLBuffer(), graphEdgePlantCollection.getPlantUMLBuffer());
    	plantUMLExporter.exportClassDiagramText();
    }

}
