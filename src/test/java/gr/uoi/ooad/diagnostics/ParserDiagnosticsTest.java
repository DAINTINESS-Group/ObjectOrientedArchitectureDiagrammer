package gr.uoi.ooad.diagnostics;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import gr.uoi.ooad.parser.ast.ASTInterpreter;
import gr.uoi.ooad.utils.PathTemplate;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;

public class ParserDiagnosticsTest {

    @Test
    void arcsBecomeFoundLinesTest() {
        ASTInterpreter interpreter = new ASTInterpreter();
        interpreter.parseProject(PathTemplate.ParserTesting.SRC.path);
        interpreter.convertToGraph(Collections.emptyList(), Collections.emptyList());

        List<String> lines = ParserDiagnostics.foundLines(interpreter.getSinkVertices());

        assertTrue(
                lines.contains("FOUND extension ImplementingClass -> ExtensionClass"),
                "No FOUND line for the extension of ImplementingClass");

        assertTrue(
                lines.contains("FOUND nesting InnerClassSample -> InnerClassSample::InnerClass"),
                "No FOUND line for the nesting of InnerClassSample");
    }

    @Test
    void parentOutsideTheProjectIsExternalTest() {
        ASTInterpreter interpreter = new ASTInterpreter();
        interpreter.parseProject(PathTemplate.BookstoreAdvanced.SRC.path);
        interpreter.convertToGraph(Collections.emptyList(), Collections.emptyList());

        List<String> lines = ParserDiagnostics.unresolvedLines(interpreter);

        assertEquals(List.of("EXTERNAL extension Gui -> Application"), lines);
    }

    @Test
    void parentInsideTheProjectWithoutArcIsMissedTest() {
        ASTInterpreter interpreter = new ASTInterpreter();
        interpreter.parseProject(PathTemplate.InnerInheritanceSamples.SRC.path);
        interpreter.convertToGraph(Collections.emptyList(), Collections.emptyList());

        List<String> lines = ParserDiagnostics.unresolvedLines(interpreter);

        assertEquals(List.of("MISSED extension Outer::Derived -> Outer::Base"), lines);
    }

    @Test
    void onlyTheLinesOfTheChosenClassesAreKeptTest() {
        ASTInterpreter interpreter = new ASTInterpreter();
        interpreter.parseProject(PathTemplate.BookstoreAdvanced.SRC.path);
        interpreter.convertToGraph(Collections.emptyList(), Collections.emptyList());

        List<String> lines =
                ParserDiagnostics.chosenLines(
                        ParserDiagnostics.allLines(interpreter), List.of("Book", "Item", "Gui"));

        assertEquals(
                List.of("FOUND extension Book -> Item", "EXTERNAL extension Gui -> Application"),
                lines);
    }
}
