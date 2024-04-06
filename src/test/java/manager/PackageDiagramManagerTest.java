package manager;

import model.graph.PackageVertex;
import org.junit.jupiter.api.Test;
import utils.PathTemplate.LatexEditor;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PackageDiagramManagerTest
{

    @Test
    void populateGraphMLPackageNodeTest()
    {
        PackageDiagramManager packageDiagramManager = new PackageDiagramManager();
        SourceProject         sourceProject         = packageDiagramManager.createSourceProject(LatexEditor.SRC.path);

        packageDiagramManager.convertTreeToDiagram(List.of("src.view",
                                                           "src.model",
                                                           "src.model.strategies",
                                                           "src.controller.commands",
                                                           "src.controller"));
        Map<PackageVertex, Integer> graphNodes   = packageDiagramManager.getPackageDiagram().getGraphNodes();
        Map<Path, PackageVertex>    packageNodes = sourceProject.getInterpreter().getVertices();
        packageNodes.remove(LatexEditor.SRC.path);
        assertEquals(packageNodes.size(), graphNodes.size());

        List<String> l1 = packageNodes.values().stream()
            .map(PackageVertex::getName)
            .collect(Collectors.toCollection(ArrayList::new));

        List<String> l2 = graphNodes.keySet().stream()
            .map(PackageVertex::getName)
            .collect(Collectors.toCollection(ArrayList::new));

        Collections.sort(l1);
        Collections.sort(l2);

        assertEquals(l1.size(), l2.size());
        assertTrue(l1.containsAll(l2));
        assertTrue(l2.containsAll(l1));
    }
}
