package model.plantuml;

import static utils.ListUtils.assertListsEqual;

import java.util.Arrays;
import java.util.List;
import manager.PackageDiagramManager;
import model.diagram.PackageDiagram;
import model.diagram.plantuml.PlantUMLPackageVertex;
import org.junit.jupiter.api.Test;
import utils.PathTemplate.LatexEditor;

public class PlantUMLPackageVertexTest {

    @Test
    void convertVerticesTest() {

        PackageDiagramManager packageDiagramManager = new PackageDiagramManager();
        packageDiagramManager.createSourceProject(LatexEditor.SRC.path);
        packageDiagramManager.convertTreeToDiagram(
                List.of(
                        "src",
                        "src.view",
                        "src.model",
                        "src.model.strategies",
                        "src.controller.commands",
                        "src.controller"));

        PackageDiagram packageDiagram = packageDiagramManager.getPackageDiagram();
        String actualBuffer = PlantUMLPackageVertex.convertVertices(packageDiagram).toString();

        List<String> expected = Arrays.asList(EXPECTED_BUFFER.split("\n"));
        List<String> actual = Arrays.asList(actualBuffer.split("\n"));

        assertListsEqual(expected, actual);
    }

    public static final String EXPECTED_BUFFER =
            """
        package src.controller {
        }

        package src.controller.commands {
        }

        package src.model {
        }

        package src {
        }

        package src.view {
        }

        package src.model.strategies {
        }

        """;
}
