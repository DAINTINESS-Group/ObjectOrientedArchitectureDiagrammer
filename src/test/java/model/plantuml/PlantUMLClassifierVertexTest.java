package model.plantuml;

import static utils.ListUtils.assertListsEqual;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import manager.ClassDiagramManager;
import model.diagram.plantuml.PlantUMLClassifierVertex;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import utils.Compiler;
import utils.PathTemplate.LatexEditor;

public class PlantUMLClassifierVertexTest {

    @TempDir private File project;

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
        List<String> expected =
                Arrays.asList(EXPECTED_BUFFER_SOURCE_FILE.split(System.lineSeparator()));
        List<String> actual = Arrays.asList(actualBuffer.split(System.lineSeparator()));

        assertListsEqual(expected, actual);
    }

    @Test
    public void convertSinkVerticesClassFileTest() {
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
                PlantUMLClassifierVertex.convertSinkVertices(classDiagramManager.getClassDiagram())
                        .toString();
        List<String> actualRelationships =
                Arrays.asList(actualBuffer.split("}" + System.lineSeparator()));
        List<String> expectedRelationships =
                Arrays.asList(EXPECTED_BUFFER_CLASS_FILE.split("}" + System.lineSeparator()));

        assertListsEqual(expectedRelationships, actualRelationships);
    }

    public static final String EXPECTED_BUFFER_SOURCE_FILE =
            """
        class DocumentManager {
        -templates: HashMap[String,Document]
        +DocumentManager(): Constructor
        +createDocument(String): Document
        +getContents(String): String
        }
        class VolatileVersionsStrategy {
        -history: ArrayList[Document]
        +VolatileVersionsStrategy(): Constructor
        +getVersion(): Document
        +putVersion(Document): void
        +removeVersion(): void
        +setEntireHistory(List[Document]): void
        +getEntireHistory(): List[Document]
        }
        class Document {
        -date: String
        -copyright: String
        -contents: String
        -versionID: String
        -author: String
        +Document(): Constructor
        +Document(String, String, String, String, String): Constructor
        +getContents(): String
        +getVersionID(): String
        +setContents(String): void
        +clone(): Document
        +changeVersion(): void
        +save(String): void
        }
        class StableVersionsStrategy {
        -versionID: String
        +putVersion(Document): void
        +getEntireHistory(): List[Document]
        +removeVersion(): void
        +getVersion(): Document
        +setEntireHistory(List[Document]): void
        }
        class VersionsManager {
        -latexEditorView: LatexEditorView
        -strategy: VersionsStrategy
        -enabled: boolean
        +VersionsManager(LatexEditorView, VersionsStrategy): Constructor
        +putVersion(Document): void
        +saveToFile(): void
        +loadFromFile(): void
        +enableStrategy(): void
        +changeStrategy(): void
        +isEnabled(): boolean
        +getType(): String
        +getStrategy(): VersionsStrategy
        +setCurrentVersion(Document): void
        +rollbackToPreviousVersion(): void
        +rollback(): void
        +disable(): void
        +setStrategy(VersionsStrategy): void
        +setPreviousVersion(): Document
        +enable(): void
        +saveContents(): void
        }
        interface VersionsStrategy {
        +getVersion(): Document
        +setEntireHistory(List[Document]): void
        +putVersion(Document): void
        +removeVersion(): void
        +getEntireHistory(): List[Document]
        }
        class VersionsStrategyFactory {
        -strategies: HashMap[String,VersionsStrategy]
        +VersionsStrategyFactory(): Constructor
        +createStrategy(String): VersionsStrategy
        }
        """;
    public static final String EXPECTED_BUFFER_CLASS_FILE =
            """
        class model.strategies.VolatileVersionsStrategy {
        -history: java.util.ArrayList
        +<init>(): void
        +putVersion(model.Document): void
        +getVersion(): model.Document
        +setEntireHistory(java.util.List): void
        +getEntireHistory(): java.util.List
        +removeVersion(): void
        }
        class model.DocumentManager {
        -templates: java.util.HashMap
        +<init>(): void
        +createDocument(java.lang.String): model.Document
        +getContents(java.lang.String): java.lang.String
        }
        interface model.strategies.VersionsStrategy {
        +putVersion(model.Document): void
        +getVersion(): model.Document
        +setEntireHistory(java.util.List): void
        +getEntireHistory(): java.util.List
        +removeVersion(): void
        }
        class model.strategies.StableVersionsStrategy {
        -versionID: java.lang.String
        +<init>(): void
        +putVersion(model.Document): void
        +getVersion(): model.Document
        +setEntireHistory(java.util.List): void
        +getEntireHistory(): java.util.List
        +removeVersion(): void
        }
        class model.VersionsManager {
        -enabled: boolean
        -strategy: model.strategies.VersionsStrategy
        -latexEditorView: view.LatexEditorView
        +<init>(model.strategies.VersionsStrategy, view.LatexEditorView): void
        +isEnabled(): boolean
        +enable(): void
        +disable(): void
        +setStrategy(model.strategies.VersionsStrategy): void
        +setCurrentVersion(model.Document): void
        +setPreviousVersion(): model.Document
        +rollbackToPreviousVersion(): void
        +getType(): java.lang.String
        +saveContents(): void
        +saveToFile(): void
        +loadFromFile(): void
        +enableStrategy(): void
        +changeStrategy(): void
        +putVersion(model.Document): void
        +rollback(): void
        +getStrategy(): model.strategies.VersionsStrategy
        }
        class model.strategies.VersionsStrategyFactory {
        -strategies: java.util.HashMap
        +<init>(): void
        +createStrategy(java.lang.String): model.strategies.VersionsStrategy
        }
        class model.Document {
        -author: java.lang.String
        -date: java.lang.String
        -copyright: java.lang.String
        -versionID: java.lang.String
        -contents: java.lang.String
        +<init>(): void
        +<init>(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String): void
        +getContents(): java.lang.String
        +setContents(java.lang.String): void
        +save(java.lang.String): void
        +clone(): model.Document
        +changeVersion(): void
        +getVersionID(): java.lang.String
        +clone(): java.lang.Object
        }
        """;
}
