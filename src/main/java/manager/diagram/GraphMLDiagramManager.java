package manager.diagram;

import java.util.List;

public interface GraphMLDiagramManager {

    void createDiagram(List<String> chosenPackagesNames);

    void arrangeDiagram();

    void exportDiagramToGraphML(String graphMLSavePath);

}