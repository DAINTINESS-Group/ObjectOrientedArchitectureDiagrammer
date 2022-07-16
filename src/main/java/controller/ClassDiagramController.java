package controller;

import manager.diagram.ClassDiagramManager;

import java.util.List;

public class ClassDiagramController extends Controller{

    public void convertTreeToDiagram(List<String> chosenClassesNames) {
        diagramManager = new ClassDiagramManager(packageNodes);
        diagramManager.createDiagram(chosenClassesNames);
    }
}
