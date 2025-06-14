package model.plantuml;

import static utils.ListUtils.assertListsEqual;

import java.util.Arrays;
import java.util.List;
import manager.ClassDiagramManager;
import model.diagram.ClassDiagram;
import model.diagram.plantuml.PlantUMLClassifierVertexArc;
import org.junit.jupiter.api.Test;
import utils.PathTemplate.LatexEditor;

public class PlantUMLClassifierVertexArcTest {

    @Test
    void convertSinkVerticesArcTest() {
        String expectedBuffer =
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
        List<String> expectedRelationships = Arrays.asList(expectedBuffer.split("\n"));
        List<String> actualRelationships = Arrays.asList(actualBuffer.split("\n"));

        assertListsEqual(expectedRelationships, actualRelationships);
    }
}
