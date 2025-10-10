package gr.uoi.ooad.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.util.Map;

import org.junit.jupiter.api.Test;
import gr.uoi.ooad.parser.tree.PackageNode;
import gr.uoi.ooad.utils.PathTemplate.BookstoreAdvanced;

public class PackageNodeCleanerTest {

    public static final int NON_VALID_NODES = 5;

    @Test
    void removeNonPackageNodesTest() {
        Interpreter interpreter = new Interpreter();
        interpreter.parseProject(BookstoreAdvanced.SRC_ROOT.path);

        Map<Path, PackageNode> packageNodes = interpreter.getPackageNodes();
        Map<Path, PackageNode> validPackageNodes =
                PackageNodeCleaner.removeNonPackageNodes(packageNodes);

        assertEquals(packageNodes.size(), validPackageNodes.size() + NON_VALID_NODES);

        // Valid Package Nodes.
        assertTrue(validPackageNodes.containsKey(BookstoreAdvanced.SRC.path));
        assertTrue(validPackageNodes.containsKey(BookstoreAdvanced.BOOKSTORE.path));
        assertTrue(validPackageNodes.containsKey(BookstoreAdvanced.GUI.path));
        assertTrue(validPackageNodes.containsKey(BookstoreAdvanced.SRC_ROOT.path));

        // Non Valid Package Nodes.
        assertFalse(validPackageNodes.containsKey(BookstoreAdvanced.DOT_SETTINGS.path));
        assertFalse(validPackageNodes.containsKey(BookstoreAdvanced.BIN.path));
        assertFalse(validPackageNodes.containsKey(BookstoreAdvanced.BIN_BOOKSTORE.path));
        assertFalse(validPackageNodes.containsKey(BookstoreAdvanced.BIN_GUI.path));
        assertFalse(validPackageNodes.containsKey(BookstoreAdvanced.LIB.path));

        PackageNode sourcePackage = validPackageNodes.get(BookstoreAdvanced.SRC_ROOT.path);
        Map<Path, PackageNode> sourcePackageSubNodes = sourcePackage.getSubNodes();
        assertEquals(sourcePackageSubNodes.size(), 1);
        assertTrue(sourcePackageSubNodes.containsKey(BookstoreAdvanced.SRC.path));
    }
}
