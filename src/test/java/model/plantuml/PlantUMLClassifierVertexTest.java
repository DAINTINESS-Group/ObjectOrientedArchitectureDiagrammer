package model.plantuml;

import static utils.ListUtils.assertListsEqual;

import java.util.Arrays;
import java.util.List;
import manager.ClassDiagramManager;
import model.diagram.plantuml.PlantUMLClassifierVertex;
import org.junit.jupiter.api.Test;
import utils.PathTemplate.LatexEditor;

public class PlantUMLClassifierVertexTest {

    @Test
    void convertSinkVerticesTest() {

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

        String actualBuffer =
                PlantUMLClassifierVertex.convertSinkVertices(classDiagramManager.getClassDiagram())
                        .toString();

        List<String> expected = Arrays.asList(EXPECTED_BUFFER.split(System.lineSeparator()));
        List<String> actual = Arrays.asList(actualBuffer.split(System.lineSeparator()));

        assertListsEqual(expected, actual);
    }

    public static final String EXPECTED_BUFFER =
            """
        class VersionsManager {
        -enabled: boolean
        -strategy: VersionsStrategy
        -latexEditorView: LatexEditorView
        +changeStrategy(): void
        +setPreviousVersion(): Document
        +VersionsManager(LatexEditorView, VersionsStrategy): Constructor
        +setStrategy(VersionsStrategy): void
        +saveContents(): void
        +enable(): void
        +getType(): String
        +rollback(): void
        +getStrategy(): VersionsStrategy
        +isEnabled(): boolean
        +disable(): void
        +putVersion(Document): void
        +rollbackToPreviousVersion(): void
        +enableStrategy(): void
        +saveToFile(): void
        +setCurrentVersion(Document): void
        +loadFromFile(): void
        }

        class VolatileVersionsStrategy {
        -history: ArrayList[Document]
        +removeVersion(): void
        +getVersion(): Document
        +VolatileVersionsStrategy(): Constructor
        +setEntireHistory(List[Document]): void
        +putVersion(Document): void
        +getEntireHistory(): List[Document]
        }

        interface VersionsStrategy {
        +removeVersion(): void
        +getVersion(): Document
        +setEntireHistory(List[Document]): void
        +getEntireHistory(): List[Document]
        +putVersion(Document): void
        }

        class StableVersionsStrategy {
        -versionID: String
        +removeVersion(): void
        +getVersion(): Document
        +setEntireHistory(List[Document]): void
        +getEntireHistory(): List[Document]
        +putVersion(Document): void
        }

        class VersionsStrategyFactory {
        -strategies: HashMap[String,VersionsStrategy]
        +createStrategy(String): VersionsStrategy
        +VersionsStrategyFactory(): Constructor
        }

        class Document {
        -author: String
        -date: String
        -copyright: String
        -versionID: String
        -contents: String
        +Document(String, String, String, String, String): Constructor
        +clone(): Document
        +getContents(): String
        +Document(): Constructor
        +save(String): void
        +getVersionID(): String
        +setContents(String): void
        +changeVersion(): void
        }

        class DocumentManager {
        -templates: HashMap[String,Document]
        +createDocument(String): Document
        +getContents(String): String
        +DocumentManager(): Constructor
        }

        """;
}
