package controller;

import java.util.List;

public interface DiagramController {

    void createTree(String sourcePackagePath);

    void convertTreeToDiagram(List<String> chosenPackagesNames);

    void exportDiagramToGraphML(String graphMLSavePath);
}
