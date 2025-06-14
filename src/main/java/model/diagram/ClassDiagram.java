package model.diagram;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import model.diagram.arrangement.geometry.DiagramGeometry;
import model.graph.Arc;
import model.graph.ClassifierVertex;
import org.javatuples.Pair;

public class ClassDiagram {

    private final Map<ClassifierVertex, Integer> graphNodes;
    private Map<ClassifierVertex, Set<Arc<ClassifierVertex>>> diagram;
    private Map<Path, ClassifierVertex> sinkVertices;
    private Map<Integer, Pair<Double, Double>> diagramGeometryGraphML;
    private DiagramGeometry diagramGeometry;

    public ClassDiagram() {
        this.graphNodes = new HashMap<>();
    }

    public void createNewDiagram(List<String> chosenFilesNames) {
        createGraphNodes(chosenFilesNames);
        createDiagram(graphNodes.keySet());
    }

    public void createDiagram(Set<ClassifierVertex> sinkVertices) {
        GraphClassDiagramConverter classDiagramConverter =
                new GraphClassDiagramConverter(sinkVertices);
        diagram = classDiagramConverter.convertGraphToClassDiagram();
    }

    private void createGraphNodes(List<String> chosenFileNames) {
        int nodeId = 0;
        List<ClassifierVertex> chosenNodes = getChosenNodes(chosenFileNames);
        for (ClassifierVertex classifierVertex : chosenNodes) {
            graphNodes.put(classifierVertex, nodeId++);
        }
    }

    private List<ClassifierVertex> getChosenNodes(List<String> chosenClassesNames) {
        List<ClassifierVertex> chosenClasses = new ArrayList<>();
        for (String chosenClass : chosenClassesNames) {
            Optional<ClassifierVertex> optionalSinkVertex =
                    sinkVertices.values().stream()
                            .filter(it -> it.getName().equals(chosenClass))
                            .findFirst();

            optionalSinkVertex.ifPresent(chosenClasses::add);
        }

        return chosenClasses;
    }

    public void setSinkVertices(Map<Path, ClassifierVertex> sinkVertices) {
        this.sinkVertices = sinkVertices;
    }

    public void setDiagram(Map<ClassifierVertex, Set<Arc<ClassifierVertex>>> diagram) {
        this.diagram = diagram;
    }

    public void setGraphMLDiagramGeometry(
            Map<Integer, Pair<Double, Double>> diagramGeometryGraphML) {
        this.diagramGeometryGraphML = diagramGeometryGraphML;
    }

    public void setDiagramGeometry(DiagramGeometry diagramGeometry) {
        this.diagramGeometry = diagramGeometry;
    }

    public Map<ClassifierVertex, Set<Arc<ClassifierVertex>>> getDiagram() {
        return diagram;
    }

    public Map<ClassifierVertex, Integer> getGraphNodes() {
        return graphNodes;
    }

    public Map<Integer, Pair<Double, Double>> getGraphMLDiagramGeometry() {
        return diagramGeometryGraphML;
    }

    public DiagramGeometry getDiagramGeometry() {
        return diagramGeometry;
    }
}
