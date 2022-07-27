package controller;

import manager.diagram.GraphPackageDiagramManager;

import java.util.List;

public class PackageDiagramController extends DiagramController {

    public void convertTreeToDiagram(List<String> chosenPackagesNames) {
        diagramManager = new GraphPackageDiagramManager(packageNodes);
        diagramManager.createDiagram(chosenPackagesNames);
    }
}
