package controller;

import manager.diagram.ClassDiagramManager;

import java.util.List;
import java.util.Map;

public class ClassDiagramController extends DiagramController {

    public ClassDiagramController() {
        diagramManager = new ClassDiagramManager();
    }

    public Map<String, Map<String, String>> loadDiagram(String graphSavePath) {
        return diagramManager.loadDiagram(graphSavePath);
    }

    public Map<String, Map<String, String>> convertTreeToDiagram(List<String> chosenClassesNames) {
        return diagramManager.createDiagram(chosenClassesNames);
    }
}
