package manager.diagram;

import java.util.List;
import java.util.Map;

public interface GraphMLDiagramManager {

    void createDiagram(List<String> chosenPackagesNames);

    void arrangeDiagram();

    void exportDiagramToGraphML(String graphMLSavePath);

    Map<String, Map<String, String>> getGraph();
}
