package model.plantuml;

import manager.PackageDiagramManager;
import model.diagram.plantuml.PlantUMLPackageVertexArc;
import org.junit.jupiter.api.Test;
import utils.PathConstructor;

import java.io.File;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PlantUMLPackageVertexArcTest
{

    private static final String EXPECTED_BUFFER = """
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
    void convertVertexArcTest()
    {
        //
        // Given
        //
        PackageDiagramManager packageDiagramManager = new PackageDiagramManager();
        packageDiagramManager.createSourceProject(Paths.get(String.format("%s%s%s",
                                                                          PathConstructor.getCurrentPath(),
                                                                          File.separator,
                                                                          PathConstructor.constructPath("src",
                                                                                                        "test",
                                                                                                        "resources",
                                                                                                        "LatexEditor",
                                                                                                        "src"))));

        //
        // When
        //
        packageDiagramManager.convertTreeToDiagram(List.of("src.view",
                                                           "src.model",
                                                           "src.model.strategies",
                                                           "src.controller.commands",
                                                           "src.controller"));
        PlantUMLPackageVertexArc plantUMLEdge = new PlantUMLPackageVertexArc(packageDiagramManager.getPackageDiagram());
        String                   actualBuffer = plantUMLEdge.convertVertexArc().toString();

        //
        // Then
        //
        List<String> expectedRelationship = Arrays.asList(EXPECTED_BUFFER.split("\n"));
        List<String> actualRelationship   = Arrays.asList(actualBuffer.split("\n"));
        Collections.sort(expectedRelationship);
        Collections.sort(actualRelationship);
        assertEquals(expectedRelationship, actualRelationship);
    }

}
