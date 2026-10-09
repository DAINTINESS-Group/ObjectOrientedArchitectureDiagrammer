package gr.uoi.ooad.model.plantuml;

import static gr.uoi.ooad.utils.ListUtils.assertListsEqual;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import gr.uoi.ooad.manager.PackageDiagramManager;
import gr.uoi.ooad.model.diagram.PackageDiagram;
import gr.uoi.ooad.model.diagram.plantuml.PlantUMLPackageVertex;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import gr.uoi.ooad.utils.Compiler;
import gr.uoi.ooad.utils.PathTemplate.LatexEditor;

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
                Arrays.asList(actualBuffer.split("}" + System.lineSeparator()))
                        .stream()
                        .map(line -> line.replace("\r", ""))
                        .collect(Collectors.toList());

        List<String> expectedRelationships =
                Arrays.asList(EXPECTED_BUFFER_SOURCE_FILE.split("}\n"));

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
                Arrays.asList(actualBuffer.split("}" + System.lineSeparator()))
                        .stream()
                        .map(line -> line.replace("\r", ""))
                        .collect(Collectors.toList());

        List<String> expectedRelationships =
                Arrays.asList(EXPECTED_BUFFER_CLASS_FILE.split("}\n"));

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
