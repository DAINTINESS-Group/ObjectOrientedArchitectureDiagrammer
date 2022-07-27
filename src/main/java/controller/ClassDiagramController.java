package controller;

import manager.diagram.GraphClassDiagramManager;

import java.util.List;

public class ClassDiagramController extends DiagramController {

    public void convertTreeToDiagram(List<String> chosenClassesNames) {
        diagramManager = new GraphClassDiagramManager(packageNodes);
        diagramManager.createDiagram(chosenClassesNames);
    }
}
