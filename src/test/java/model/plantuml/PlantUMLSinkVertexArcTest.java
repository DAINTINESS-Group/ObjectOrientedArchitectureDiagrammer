package model.plantuml;

import manager.ClassDiagramManager;
import manager.SourceProject;
import model.diagram.plantuml.PlantUMLSinkVertexArc;
import model.graph.Arc;
import model.graph.SinkVertex;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlantUMLSinkVertexArcTest {

    Path currentDirectory = Path.of(".");

    @Test
    void convertSinkVertexArcTest() {
        try {
            String expectedBuffer = "VersionsStrategy ..> Document\n" +
                    "DocumentManager --o Document\n" +
                    "VersionsStrategyFactory ..> VolatileVersionsStrategy\n" +
                    "StableVersionsStrategy ..|> VersionsStrategy\n" +
                    "VersionsStrategyFactory --o VersionsStrategy\n" +
                    "StableVersionsStrategy ..> Document\n" +
                    "VersionsManager --> VersionsStrategy\n" +
                    "VolatileVersionsStrategy --o Document\n" +
                    "VersionsManager ..> Document\n" +
                    "VersionsStrategyFactory ..> StableVersionsStrategy\n" +
                    "VolatileVersionsStrategy ..|> VersionsStrategy\n";

            ClassDiagramManager classDiagramManager = new ClassDiagramManager();
            SourceProject sourceProject = classDiagramManager.createSourceProject(Paths.get(currentDirectory.toRealPath() + "\\src\\test\\resources\\LatexEditor\\src"));
            classDiagramManager.convertTreeToDiagram(List.of("StableVersionsStrategy", "VersionsStrategy", "VersionsStrategyFactory", "VolatileVersionsStrategy",
                    "VersionsManager", "Document", "DocumentManager"));

            Map<Arc<SinkVertex>, Integer> graphEdges = classDiagramManager.getClassDiagram().getGraphEdges();
            Map<SinkVertex, Set<Arc<SinkVertex>>> diagram = classDiagramManager.getDiagram();
            PlantUMLSinkVertexArc plantUMLEdge = new PlantUMLSinkVertexArc(diagram);
            String actualBuffer = plantUMLEdge.convertSinkVertexArc().toString();

            List<String> actualRelationships = Arrays.asList(expectedBuffer.split("\n"));
            List<String> expectedRelationships = Arrays.asList(actualBuffer.split("\n"));
            Collections.sort(actualRelationships);
            Collections.sort(expectedRelationships);
            assertEquals(expectedRelationships, actualRelationships);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
