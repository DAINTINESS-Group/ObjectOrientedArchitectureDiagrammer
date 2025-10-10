package gr.uoi.ooad.model.plantuml;

import static gr.uoi.ooad.utils.ListUtils.assertListsEqual;

import java.util.Arrays;
import java.util.List;
import gr.uoi.ooad.manager.ClassDiagramManager;
import gr.uoi.ooad.model.diagram.ClassDiagram;
import gr.uoi.ooad.model.diagram.plantuml.PlantUMLClassifierVertexArc;
import org.junit.jupiter.api.Test;
import gr.uoi.ooad.utils.PathTemplate.LatexEditor;

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
