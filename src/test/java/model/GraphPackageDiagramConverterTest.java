package model;

import manager.PackageDiagramManager;
import model.diagram.GraphPackageDiagramConverter;
import model.graph.Arc;
import model.graph.PackageVertex;
import org.junit.jupiter.api.Test;
import utils.PathConstructor;
import utils.PathTemplate;
import utils.PathTemplate.LatexEditor;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GraphPackageDiagramConverterTest
{

    @Test
    void convertGraphToPackageDiagramTest()
    {
        PackageDiagramManager packageDiagramManager = new PackageDiagramManager();
        packageDiagramManager.createSourceProject(LatexEditor.SRC.path);
        packageDiagramManager.convertTreeToDiagram(List.of("src.view",
                                                           "src.model",
                                                           "src.model.strategies",
                                                           "src.controller.commands",
                                                           "src.controller"));
        Map<PackageVertex, Set<Arc<PackageVertex>>> diagram = packageDiagramManager.getPackageDiagram().getDiagram();

        List<Arc<PackageVertex>> arcs = new ArrayList<>();
        for (Set<Arc<PackageVertex>> arcSet : diagram.values())
        {
            arcs.addAll(arcSet);
        }

        GraphPackageDiagramConverter                graphPackageDiagramConverter = new GraphPackageDiagramConverter(diagram.keySet());
        Map<PackageVertex, Set<Arc<PackageVertex>>> adjacencyList                = graphPackageDiagramConverter.convertGraphToPackageDiagram();

        Set<Arc<PackageVertex>> actualArcs = new HashSet<>();
        for (Set<Arc<PackageVertex>> value : adjacencyList.values())
        {
            actualArcs.addAll(value);
        }

        assertEquals(arcs.size(), actualArcs.size());
        for (Arc<PackageVertex> vertexArc : actualArcs)
        {
            assertTrue(arcs.contains(vertexArc));
        }
    }
}
