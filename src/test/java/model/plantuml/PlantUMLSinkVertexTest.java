package model.plantuml;

import manager.ClassDiagramManager;
import manager.SourceProject;
import model.diagram.plantuml.PlantUMLSinkVertex;
import model.graph.SinkVertex;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlantUMLSinkVertexTest {

    Path currentDirectory = Path.of(".");

    @Test
    void convertSinkVertexTest() {
        try {
            String expectedBuffer = "class VersionsManager {\n" +
                    "-enabled: boolean\n" +
                    "-strategy: VersionsStrategy\n" +
                    "-latexEditorView: LatexEditorView\n" +
                    "+changeStrategy(): void\n" +
                    "+setPreviousVersion(): Document\n" +
                    "+VersionsManager(LatexEditorView latexEditorView, VersionsStrategy versionsStrategy): Constructor\n" +
                    "+setStrategy(VersionsStrategy strategy): void\n" +
                    "+saveContents(): void\n" +
                    "+enable(): void\n" +
                    "+getType(): String\n" +
                    "+rollback(): void\n" +
                    "+getStrategy(): VersionsStrategy\n" +
                    "+isEnabled(): boolean\n" +
                    "+disable(): void\n" +
                    "+putVersion(Document document): void\n" +
                    "+rollbackToPreviousVersion(): void\n" +
                    "+enableStrategy(): void\n" +
                    "+saveToFile(): void\n" +
                    "+setCurrentVersion(Document document): void\n" +
                    "+loadFromFile(): void\n" +
                    "}\n\n" +
                    "class VolatileVersionsStrategy {\n" +
                    "-history: ArrayList[Document]\n" +
                    "+removeVersion(): void\n" +
                    "+getVersion(): Document\n" +
                    "+VolatileVersionsStrategy(): Constructor\n" +
                    "+setEntireHistory(List[Document] documents): void\n" +
                    "+putVersion(Document document): void\n" +
                    "+getEntireHistory(): List[Document]\n" +
                    "}\n\n" +
                    "interface VersionsStrategy {\n" +
                    "+removeVersion(): void\n" +
                    "+getVersion(): Document\n" +
                    "+setEntireHistory(List[Document] documents): void\n" +
                    "+getEntireHistory(): List[Document]\n" +
                    "+putVersion(Document document): void\n" +
                    "}\n\n" +
                    "class StableVersionsStrategy {\n" +
                    "-versionID: String\n" +
                    "+removeVersion(): void\n" +
                    "+getVersion(): Document\n" +
                    "+setEntireHistory(List[Document] documents): void\n" +
                    "+getEntireHistory(): List[Document]\n" +
                    "+putVersion(Document document): void\n" +
                    "}\n\n" +
                    "class VersionsStrategyFactory {\n" +
                    "-strategies: HashMap[String,VersionsStrategy]\n" +
                    "+createStrategy(String type): VersionsStrategy\n" +
                    "+VersionsStrategyFactory(): Constructor\n" +
                    "}\n\n" +
                    "class Document {\n" +
                    "-author: String\n" +
                    "-date: String\n" +
                    "-copyright: String\n" +
                    "-versionID: String\n" +
                    "-contents: String\n" +
                    "+Document(String date, String copyright, String versionID, String contents, String author): Constructor\n" +
                    "+clone(): Document\n" +
                    "+getContents(): String\n" +
                    "+Document(): Constructor\n" +
                    "+save(String filename): void\n" +
                    "+getVersionID(): String\n" +
                    "+setContents(String contents): void\n" +
                    "+changeVersion(): void\n" +
                    "}\n\n" +
                    "class DocumentManager {\n" +
                    "-templates: HashMap[String,Document]\n" +
                    "+createDocument(String type): Document\n" +
                    "+getContents(String type): String\n" +
                    "+DocumentManager(): Constructor\n" +
                    "}\n\n";

            ClassDiagramManager classDiagramManager = new ClassDiagramManager();
            SourceProject sourceProject = classDiagramManager.createSourceProject(Paths.get(currentDirectory.toRealPath() + "\\src\\test\\resources\\LatexEditor\\src"));
            classDiagramManager.convertTreeToDiagram(List.of("StableVersionsStrategy", "VersionsStrategy", "VersionsStrategyFactory", "VolatileVersionsStrategy",
                    "VersionsManager", "Document", "DocumentManager"));

            Map<SinkVertex, Integer> graphNodes = classDiagramManager.getClassDiagram().getGraphNodes();
            PlantUMLSinkVertex plantUMLSinkVertex = new PlantUMLSinkVertex(graphNodes);
            String actualBuffer = plantUMLSinkVertex.convertSinkVertex().toString();

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
