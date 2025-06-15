package model.plantuml;

import static utils.ListUtils.assertListsEqual;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import manager.PackageDiagramManager;
import model.diagram.PackageDiagram;
import model.diagram.plantuml.PlantUMLPackageVertexArc;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import utils.Compiler;
import utils.PathTemplate;

class PlantUMLPackageVertexArcTest {

    @TempDir private File project;

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
        List<String> expectedRelationship =
                Arrays.asList(EXPECTED_BUFFER_SOURCE_FILE.split(System.lineSeparator()));
        List<String> actualRelationship = Arrays.asList(actualBuffer.split(System.lineSeparator()));

        assertListsEqual(expectedRelationship, actualRelationship);
    }

    @Test
    void convertVerticesArcClassFileTest() {
        project = Compiler.compileSourceProject(PathTemplate.LatexEditor.SRC.path);

        PackageDiagramManager packageDiagramManager = new PackageDiagramManager();
        packageDiagramManager.createSourceProject(project.toPath());
        packageDiagramManager.convertTreeToDiagram(
                List.of("view", "model", "model.strategies", "controller.commands", "controller"));

        PackageDiagram packageDiagram = packageDiagramManager.getPackageDiagram();
        String actualBuffer = PlantUMLPackageVertexArc.convertVertexArcs(packageDiagram).toString();
        List<String> actualRelationship = Arrays.asList(actualBuffer.split(System.lineSeparator()));

        List<String> expectedRelationship =
                Arrays.asList(EXPECTED_BUFFER_CLASS_FILE.split(System.lineSeparator()));

        assertListsEqual(expectedRelationship, actualRelationship);
    }

    public static final String EXPECTED_BUFFER_SOURCE_FILE =
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

    public static final String EXPECTED_BUFFER_CLASS_FILE =
            """
        controller.commands ..> model
        model.strategies ..> model
        view ..> model.strategies
        view ..> controller
        view ..> model""";
}
