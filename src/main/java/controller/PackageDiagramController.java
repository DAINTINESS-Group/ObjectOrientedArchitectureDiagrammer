package controller;

import manager.diagram.PackageDiagramManager;

import java.util.List;

public class PackageDiagramController extends Controller{

    public void convertTreeToDiagram(List<String> chosenPackagesNames) {
        diagramManager = new PackageDiagramManager(packageNodes);
        diagramManager.createDiagram(chosenPackagesNames);
    }
}
