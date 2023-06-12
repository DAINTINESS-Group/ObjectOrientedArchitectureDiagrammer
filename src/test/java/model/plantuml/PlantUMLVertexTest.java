package model.plantuml;

import manager.PackageDiagramManager;
import manager.SourceProject;
import model.diagram.plantuml.PlantUMLVertex;
import model.graph.Vertex;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlantUMLVertexTest {

    Path currentDirectory = Path.of(".");

    @Test
    void convertVertexTest() {
        try {
            String expectedBuffer = "package src.controller {\n" +
                "}\n" +
                "\n" +
                "package src.controller.commands {\n" +
                "}\n" +
                "\n" +
                "package src.model {\n" +
                "}\n" +
                "\n" +
                "package src {\n" +
                "}\n" +
                "\n" +
                "package src.view {\n" +
                "}\n" +
                "\n" +
                "package src.model.strategies {\n" +
                "}\n\n";

            PackageDiagramManager packageDiagramManager = new PackageDiagramManager();
            SourceProject sourceProject = packageDiagramManager.createSourceProject(Paths.get(currentDirectory.toRealPath() + "\\src\\test\\resources\\LatexEditor\\src"));
            packageDiagramManager.createDiagram(List.of(
                "src",
                "src.view",
                "src.model",
                "src.model.strategies",
                "src.controller.commands",
                "src.controller"
            ));

            Map<Vertex, Integer> graphNodes = packageDiagramManager.getDiagram().getGraphNodes();
            PlantUMLVertex plantUMLVertex = new PlantUMLVertex(graphNodes);
            String actualBuffer = plantUMLVertex.convertVertex().toString();

            List<String> expected = Arrays.asList(expectedBuffer.split("\n"));
            List<String> actual = Arrays.asList(actualBuffer.split("\n"));

            Collections.sort(expected);
            Collections.sort(actual);
            assertEquals(expected, actual);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
