package gr.uoi.ooad.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import gr.uoi.ooad.manager.PackageDiagramManager;
import gr.uoi.ooad.model.diagram.GraphPackageDiagramConverter;
import gr.uoi.ooad.model.graph.Arc;
import gr.uoi.ooad.model.graph.PackageVertex;
import org.junit.jupiter.api.Test;
import gr.uoi.ooad.utils.PathTemplate.LatexEditor;

public class GraphPackageDiagramConverterTest {

    @Test
    void convertGraphToPackageDiagramTest() {
        PackageDiagramManager packageDiagramManager = new PackageDiagramManager();
        packageDiagramManager.createSourceProject(LatexEditor.SRC.path);
        packageDiagramManager.convertTreeToDiagram(
                List.of(
                        "src.view",
                        "src.model",
                        "src.model.strategies",
                        "src.controller.commands",
                        "src.controller"));
        Map<PackageVertex, Set<Arc<PackageVertex>>> diagram =
                packageDiagramManager.getPackageDiagram().getDiagram();

        List<Arc<PackageVertex>> arcs =
                diagram.values().stream()
                        .flatMap(Collection::stream)
                        .collect(Collectors.toCollection(ArrayList::new));

        GraphPackageDiagramConverter graphPackageDiagramConverter =
                new GraphPackageDiagramConverter(diagram.keySet());
        Map<PackageVertex, Set<Arc<PackageVertex>>> adjacencyList =
                graphPackageDiagramConverter.convertGraphToPackageDiagram();

        Set<Arc<PackageVertex>> actualArcs =
                adjacencyList.values().stream()
                        .flatMap(Collection::stream)
                        .collect(Collectors.toCollection(HashSet::new));

        assertEquals(arcs.size(), actualArcs.size());
        assertTrue(arcs.containsAll(actualArcs));
    }
}
