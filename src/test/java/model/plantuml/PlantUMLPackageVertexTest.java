package model.plantuml;

import static utils.ListUtils.assertListsEqual;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import manager.PackageDiagramManager;
import model.diagram.PackageDiagram;
import model.diagram.plantuml.PlantUMLPackageVertex;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import utils.Compiler;
import utils.PathTemplate.LatexEditor;

public class PlantUMLPackageVertexTest {

    @TempDir private File project;

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
        List<String> actualRelationships =
                Arrays.asList(actualBuffer.split("}" + System.lineSeparator()));

        List<String> expectedRelationships =
                Arrays.asList(EXPECTED_BUFFER_SOURCE_FILE.split("}" + System.lineSeparator()));

        assertListsEqual(expectedRelationships, actualRelationships);
    }

    @Test
    void convertVerticesClassFileTest() {
        project = Compiler.compileSourceProject(LatexEditor.SRC.path);

        PackageDiagramManager packageDiagramManager = new PackageDiagramManager();
        packageDiagramManager.createSourceProject(project.toPath());
        packageDiagramManager.convertTreeToDiagram(
                List.of("view", "model", "model.strategies", "controller.commands", "controller"));

        PackageDiagram packageDiagram = packageDiagramManager.getPackageDiagram();
        String actualBuffer = PlantUMLPackageVertex.convertVertices(packageDiagram).toString();
        List<String> actualRelationships =
                Arrays.asList(actualBuffer.split("}" + System.lineSeparator()));

        List<String> expectedRelationships =
                Arrays.asList(EXPECTED_BUFFER_CLASS_FILE.split("}" + System.lineSeparator()));

        assertListsEqual(expectedRelationships, actualRelationships);
    }

    public static final String EXPECTED_BUFFER_SOURCE_FILE =
            """
        package src.model {
        }
        package src {
        }
        package src.controller {
        }
        package src.controller.commands {
        }
        package src.model.strategies {
        }
        package src.view {
        }
        """;

    public static final String EXPECTED_BUFFER_CLASS_FILE =
            """
        package controller.commands {
        }
        package model.strategies {
        }
        package model {
        }
        package controller {
        }
        package view {
        }
        """;
}
