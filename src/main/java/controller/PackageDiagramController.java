package controller;

import manager.diagram.GraphPackageDiagramManager;

import java.util.List;
import java.util.Map;

public class PackageDiagramController extends DiagramController {

    public Map<String, Map<String, String>> convertTreeToDiagram(List<String> chosenPackagesNames) {
        diagramManager = new GraphPackageDiagramManager(sourceProject);
        return diagramManager.createDiagram(chosenPackagesNames);
    }
}
