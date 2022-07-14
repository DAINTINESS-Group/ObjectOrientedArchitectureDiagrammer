package controller;

import java.util.List;

public interface DiagramController {

    void createTree(String sourcePackagePath);

    void convertTreeToPackageDiagram(List<String> chosenPackagesNames);

    void convertTreeToClassDiagram(List<String> chosenClassesNames);

    void exportDiagramToGraphML(String graphMLSavePath);
}
