package gr.uoi.ooad.model.plantuml;

import static gr.uoi.ooad.utils.ListUtils.assertListsEqual;

import gr.uoi.ooad.manager.PackageDiagramManager;
import gr.uoi.ooad.model.diagram.PackageDiagram;
import gr.uoi.ooad.model.diagram.plantuml.PlantUMLPackageVertex;
import gr.uoi.ooad.utils.PathTemplate.LatexEditor;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

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
