package controller;

import java.util.List;
import java.util.Map;

public interface DiagramController {

    void createTree(String sourcePackagePath);

    void convertTreeToDiagram(List<String> chosenPackagesNames);

    void arrangeDiagram();

    void exportDiagramToGraphML(String graphMLSavePath);

    Map<String, Map<String, String>> getDiagram();
}
