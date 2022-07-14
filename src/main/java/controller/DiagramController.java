package controller;

import java.util.List;

public interface DiagramController {

    void createTree(String sourcePackagePath);

    void convertTreeToPackageDiagram(List<String> chosenPackagesNames, String graphMLSavePath);

    void convertTreeToClassDiagram(List<String> chosenClassesNames, String graphMLSavePath);
}
