package controller;

import manager.diagram.PackageDiagramManager;

import java.util.List;
import java.util.Map;

public class PackageDiagramController extends DiagramController {

    public PackageDiagramController() {
        diagramManager = new PackageDiagramManager();
    }

    public Map<String, Map<String, String>> loadDiagram(String graphSavePath) {
        return diagramManager.loadDiagram(graphSavePath);
    }

    public Map<String, Map<String, String>> convertTreeToDiagram(List<String> chosenPackagesNames) {
        return diagramManager.createDiagram(chosenPackagesNames);
    }
}
