package model.plantuml;

import manager.ClassDiagramManager;
import model.diagram.plantuml.PlantUMLClassifierVertex;
import org.junit.jupiter.api.Test;
import utils.PathTemplate.LatexEditor;

import java.util.Arrays;
import java.util.List;

import static utils.ListUtils.assertListsEqual;

public class PlantUMLClassifierVertexTest
{

    @Test
    void convertSinkVerticesTest()
    {

        ClassDiagramManager classDiagramManager = new ClassDiagramManager();
        classDiagramManager.createSourceProject(LatexEditor.SRC.path);
        classDiagramManager.convertTreeToDiagram(List.of("StableVersionsStrategy",
                                                         "VersionsStrategy",
                                                         "VersionsStrategyFactory",
                                                         "VolatileVersionsStrategy",
                                                         "VersionsManager",
                                                         "Document",
                                                         "DocumentManager"));

        String actualBuffer = PlantUMLClassifierVertex.convertSinkVertices(classDiagramManager.getClassDiagram()).toString();

        List<String> expected = Arrays.asList(EXPECTED_BUFFER.split("\n"));
        List<String> actual   = Arrays.asList(actualBuffer.split("\n"));

        assertListsEqual(expected, actual);
    }


    public static final String EXPECTED_BUFFER = """
        class VersionsManager {
        -enabled: boolean
        -strategy: VersionsStrategy
        -latexEditorView: LatexEditorView
        +changeStrategy(): void
        +setPreviousVersion(): Document
        +VersionsManager(LatexEditorView latexEditorView, VersionsStrategy versionsStrategy): Constructor
        +setStrategy(VersionsStrategy strategy): void
        +saveContents(): void
        +enable(): void
        +getType(): String
        +rollback(): void
        +getStrategy(): VersionsStrategy
        +isEnabled(): boolean
        +disable(): void
        +putVersion(Document document): void
        +rollbackToPreviousVersion(): void
        +enableStrategy(): void
        +saveToFile(): void
        +setCurrentVersion(Document document): void
        +loadFromFile(): void
        }
        										 
        class VolatileVersionsStrategy {
        -history: ArrayList[Document]
        +removeVersion(): void
        +getVersion(): Document
        +VolatileVersionsStrategy(): Constructor
        +setEntireHistory(List[Document] documents): void
        +putVersion(Document document): void
        +getEntireHistory(): List[Document]
        }
        										 
        interface VersionsStrategy {
        +removeVersion(): void
        +getVersion(): Document
        +setEntireHistory(List[Document] documents): void
        +getEntireHistory(): List[Document]
        +putVersion(Document document): void
        }
        										 
        class StableVersionsStrategy {
        -versionID: String
        +removeVersion(): void
        +getVersion(): Document
        +setEntireHistory(List[Document] documents): void
        +getEntireHistory(): List[Document]
        +putVersion(Document document): void
        }
        										 
        class VersionsStrategyFactory {
        -strategies: HashMap[String,VersionsStrategy]
        +createStrategy(String type): VersionsStrategy
        +VersionsStrategyFactory(): Constructor
        }
        										 
        class Document {
        -author: String
        -date: String
        -copyright: String
        -versionID: String
        -contents: String
        +Document(String date, String copyright, String versionID, String contents, String author): Constructor
        +clone(): Document
        +getContents(): String
        +Document(): Constructor
        +save(String filename): void
        +getVersionID(): String
        +setContents(String contents): void
        +changeVersion(): void
        }
        										 
        class DocumentManager {
        -templates: HashMap[String,Document]
        +createDocument(String type): Document
        +getContents(String type): String
        +DocumentManager(): Constructor
        }
        										 
        """;
}
