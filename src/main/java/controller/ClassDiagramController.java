package controller;

import manager.diagram.GraphClassDiagramManager;

import java.util.List;
import java.util.Map;

public class ClassDiagramController extends DiagramController {

    public Map<String, Map<String, String>> convertTreeToDiagram(List<String> chosenClassesNames) {
        diagramManager = new GraphClassDiagramManager(sourceProject);
        return diagramManager.createDiagram(chosenClassesNames);
    }
}
