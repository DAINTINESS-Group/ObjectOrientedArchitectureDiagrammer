package model.plantuml;

import static utils.ListUtils.assertListsEqual;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import manager.ClassDiagramManager;
import model.diagram.ClassDiagram;
import model.diagram.plantuml.PlantUMLClassifierVertexArc;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import utils.Compiler;
import utils.PathTemplate.LatexEditor;

public class PlantUMLClassifierVertexArcTest {

    @TempDir private File project;

    @Test
    void convertSinkVerticesArcTest() {

        ClassDiagramManager classDiagramManager = new ClassDiagramManager();
        classDiagramManager.createSourceProject(LatexEditor.SRC.path);
        classDiagramManager.convertTreeToDiagram(
                List.of(
                        "StableVersionsStrategy",
                        "VersionsStrategy",
                        "VersionsStrategyFactory",
                        "VolatileVersionsStrategy",
                        "VersionsManager",
                        "Document",
                        "DocumentManager"));

        ClassDiagram classDiagram = classDiagramManager.getClassDiagram();
        String actualBuffer =
                PlantUMLClassifierVertexArc.convertSinkVertexArcs(classDiagram).toString();
        List<String> actualRelationships =
                Arrays.asList(actualBuffer.split(System.lineSeparator()));

        List<String> expectedRelationships =
                Arrays.asList(EXPECTED_BUFFER_SOURCE_FILE.split(System.lineSeparator()));

        assertListsEqual(expectedRelationships, actualRelationships);
    }

    @Test
    void convertSinkVerticesArcClassFileTest() {
        project = Compiler.compileSourceProject(LatexEditor.SRC.path);

        ClassDiagramManager classDiagramManager = new ClassDiagramManager();
        classDiagramManager.createSourceProject(project.toPath());
        classDiagramManager.convertTreeToDiagram(
                List.of(
                        "model.strategies.StableVersionsStrategy",
                        "model.strategies.VersionsStrategy",
                        "model.strategies.VersionsStrategyFactory",
                        "model.strategies.VolatileVersionsStrategy",
                        "model.VersionsManager",
                        "model.Document",
                        "model.DocumentManager"));

        String actualBuffer =
                PlantUMLClassifierVertexArc.convertSinkVertexArcs(
                                classDiagramManager.getClassDiagram())
                        .toString();
        List<String> actualRelationships =
                Arrays.asList(actualBuffer.split(System.lineSeparator()));

        List<String> expectedRelationships =
                Arrays.asList(EXPECTED_BUFFER_CLASS_FILE.split(System.lineSeparator()));

        assertListsEqual(expectedRelationships, actualRelationships);
    }

    public static final String EXPECTED_BUFFER_SOURCE_FILE =
            """
        VersionsStrategy ..> Document
        DocumentManager o-- Document
        VersionsStrategyFactory ..> VolatileVersionsStrategy
        StableVersionsStrategy ..|> VersionsStrategy
        VersionsStrategyFactory o-- VersionsStrategy
        StableVersionsStrategy ..> Document
        VersionsManager --> VersionsStrategy
        VolatileVersionsStrategy o-- Document
        VersionsManager ..> Document
        VersionsStrategyFactory ..> StableVersionsStrategy
        VolatileVersionsStrategy ..|> VersionsStrategy
        """;
    public static final String EXPECTED_BUFFER_CLASS_FILE =
            """
        model.strategies.StableVersionsStrategy ..|> model.strategies.VersionsStrategy
        model.strategies.StableVersionsStrategy ..> model.Document
        model.strategies.VersionsStrategy ..> model.Document
        model.DocumentManager o-- model.Document
        model.strategies.VersionsStrategyFactory ..> model.strategies.StableVersionsStrategy
        model.strategies.VersionsStrategyFactory o-- model.strategies.VersionsStrategy
        model.strategies.VersionsStrategyFactory ..> model.strategies.VolatileVersionsStrategy
        model.VersionsManager ..> model.Document
        model.VersionsManager ..> model.strategies.VolatileVersionsStrategy
        model.VersionsManager --> model.strategies.VersionsStrategy
        model.VersionsManager ..> model.strategies.StableVersionsStrategy
        model.strategies.VolatileVersionsStrategy o-- model.Document
        model.strategies.VolatileVersionsStrategy ..|> model.strategies.VersionsStrategy""";
}
