package model;

import manager.ClassDiagramManager;
import manager.PackageDiagramManager;
import model.diagram.plantuml.PlantUMLLeafNode;
import model.diagram.plantuml.PlantUMLPackageNode;
import model.graph.SinkVertex;
import model.graph.Vertex;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlantUMLNodeTest {

    Path currentDirectory = Path.of(".");

    @Test
    void checkClassDiagramDeclarationsText() throws IOException{
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
                "}\n" +
                "class VolatileVersionsStrategy {\n" +
                "-history: ArrayList[Document]\n" +
                "+removeVersion(): void\n" +
                "+getVersion(): Document\n" +
                "+VolatileVersionsStrategy(): Constructor\n" +
                "+setEntireHistory(List[Document] documents): void\n" +
                "+putVersion(Document document): void\n" +
                "+getEntireHistory(): List[Document]\n" +
                "}\n" +
                "interface VersionsStrategy {\n" +
                "+removeVersion(): void\n" +
                "+getVersion(): Document\n" +
                "+setEntireHistory(List[Document] documents): void\n" +
                "+getEntireHistory(): List[Document]\n" +
                "+putVersion(Document document): void\n" +
                "}\n" +
                "class StableVersionsStrategy {\n" +
                "-versionID: String\n" +
                "+removeVersion(): void\n" +
                "+getVersion(): Document\n" +
                "+setEntireHistory(List[Document] documents): void\n" +
                "+getEntireHistory(): List[Document]\n" +
                "+putVersion(Document document): void\n" +
                "}\n" +
                "class VersionsStrategyFactory {\n" +
                "-strategies: HashMap[String,VersionsStrategy]\n" +
                "+createStrategy(String type): VersionsStrategy\n" +
                "+VersionsStrategyFactory(): Constructor\n" +
                "}\n" +
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
                "}\n" +
                "class DocumentManager {\n" +
                "-templates: HashMap[String,Document]\n" +
                "+createDocument(String type): Document\n" +
                "+getContents(String type): String\n" +
                "+DocumentManager(): Constructor\n" +
                "}\n";

        ClassDiagramManager classDiagramManager = new ClassDiagramManager();
        SourceProject sourceProject = classDiagramManager.createSourceProject(Paths.get(currentDirectory.toRealPath() + "\\src\\test\\resources\\LatexEditor\\src"));
        classDiagramManager.createDiagram(List.of("StableVersionsStrategy", "VersionsStrategy", "VersionsStrategyFactory", "VolatileVersionsStrategy",
                "VersionsManager", "Document", "DocumentManager"));

        Map<SinkVertex, Integer> graphNodes = classDiagramManager.getDiagram().getGraphNodes();
        PlantUMLLeafNode plantUMLLeafNode = new PlantUMLLeafNode(graphNodes);
        String actualBuffer = plantUMLLeafNode.convertPlantLeafNode().toString();

        List<String> expected = Arrays.asList(expectedBuffer.split("\n"));
        List<String> actual = Arrays.asList(actualBuffer.split("\n"));

        Collections.sort(expected);
        Collections.sort(actual);
        assertEquals(expected, actual);
    }

    @Test
    void checkPackageDiagramDeclarationsText() {
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
                    currentDirectory.toRealPath() + "\\src\\test\\resources\\LatexEditor\\src",
                    currentDirectory.toRealPath() + "\\src\\test\\resources\\LatexEditor\\src\\view",
                    currentDirectory.toRealPath() + "\\src\\test\\resources\\LatexEditor\\src\\model",
                    currentDirectory.toRealPath() + "\\src\\test\\resources\\LatexEditor\\src\\model\\strategies",
                    currentDirectory.toRealPath() + "\\src\\test\\resources\\LatexEditor\\src\\controller\\commands",
                    currentDirectory.toRealPath() + "\\src\\test\\resources\\LatexEditor\\src\\controller"));

            Map<Vertex, Integer> graphNodes = packageDiagramManager.getDiagram().getGraphNodes();
            PlantUMLPackageNode plantUMLPackageNode = new PlantUMLPackageNode(graphNodes);
            String actualBuffer = plantUMLPackageNode.convertPlantPackageNode().toString();

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
