package model.plantuml;

import manager.ClassDiagramManager;
import model.diagram.plantuml.PlantUMLClassifierVertexArc;
import org.junit.jupiter.api.Test;
import utils.PathConstructor;
import utils.PathTemplate;
import utils.PathTemplate.LatexEditor;

import java.io.File;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlantUMLClassifierVertexArcTest
{

    @Test
    void convertSinkVertexArcTest()
    {
        String expectedBuffer = """
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
        classDiagramManager.convertTreeToDiagram(List.of("StableVersionsStrategy",
                                                         "VersionsStrategy",
                                                         "VersionsStrategyFactory",
                                                         "VolatileVersionsStrategy",
                                                         "VersionsManager",
                                                         "Document",
                                                         "DocumentManager"));

        PlantUMLClassifierVertexArc plantUMLEdge = new PlantUMLClassifierVertexArc();
        String                      actualBuffer = plantUMLEdge.convertSinkVertexArc(classDiagramManager.getClassDiagram()).toString();

        List<String> expectedRelationships = Arrays.asList(expectedBuffer.split("\n"));
        List<String> actualRelationships   = Arrays.asList(actualBuffer.split("\n"));
        Collections.sort(expectedRelationships);
        Collections.sort(actualRelationships);
        assertEquals(expectedRelationships, actualRelationships);
    }
}
