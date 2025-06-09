package model.plantuml;

import static utils.ListUtils.assertListsEqual;

import java.util.Arrays;
import java.util.List;
import manager.PackageDiagramManager;
import model.diagram.PackageDiagram;
import model.diagram.plantuml.PlantUMLPackageVertexArc;
import org.junit.jupiter.api.Test;
import utils.PathTemplate;

class PlantUMLPackageVertexArcTest {

    private static final String EXPECTED_BUFFER =
            """
        src.model ..> src.view
        src.controller.commands ..> src.model
        src.model ..> src.model.strategies
        src.view ..> src.model
        src.model.strategies ..> src.model
        src.controller ..> src.model
        src.view ..> src.controller
        src.controller ..> src.controller.commands
        src.view ..> src.model.strategies
        """;

    @Test
    void convertVerticesArcTest() {
        PackageDiagramManager packageDiagramManager = new PackageDiagramManager();
        packageDiagramManager.createSourceProject(PathTemplate.LatexEditor.SRC.path);
        packageDiagramManager.convertTreeToDiagram(
                List.of(
                        "src.view",
                        "src.model",
                        "src.model.strategies",
                        "src.controller.commands",
                        "src.controller"));

        PackageDiagram packageDiagram = packageDiagramManager.getPackageDiagram();
        String actualBuffer = PlantUMLPackageVertexArc.convertVertexArcs(packageDiagram).toString();
        List<String> expectedRelationship = Arrays.asList(EXPECTED_BUFFER.split("\n"));
        List<String> actualRelationship = Arrays.asList(actualBuffer.split("\n"));

        assertListsEqual(expectedRelationship, actualRelationship);
    }
}
