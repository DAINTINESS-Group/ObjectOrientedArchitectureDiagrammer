package parser;

import org.junit.jupiter.api.Test;
import parser.tree.PackageNode;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class PackageNodeCleanerTest {

    Path currentDirectory = Path.of(".");

    @Test
    void removeNonPackageNodesTest() {
        try {
            Interpreter interpreter = new Interpreter();
            interpreter.parseProject(Paths.get(currentDirectory.toRealPath() + "\\src\\test\\resources\\BookstoreAdvancedV01"));
            Map<Path, PackageNode> packageNodes = interpreter.getPackageNodes();

            PackageNodeCleaner packageNodeCleaner = new PackageNodeCleaner(packageNodes);
            Map<Path, PackageNode> validPackageNodes = packageNodeCleaner.removeNonPackageNodes();

            assertEquals(packageNodes.size(), validPackageNodes.size() + 7);

            assertTrue(validPackageNodes.containsKey(Paths.get(currentDirectory.toRealPath() + "\\src\\test\\resources\\BookstoreAdvancedV01\\src")));
            assertTrue(validPackageNodes.containsKey(Paths.get(currentDirectory.toRealPath() + "\\src\\test\\resources\\BookstoreAdvancedV01\\src\\bookstore")));
            assertTrue(validPackageNodes.containsKey(Paths.get(currentDirectory.toRealPath() + "\\src\\test\\resources\\BookstoreAdvancedV01\\src\\gui")));
            assertTrue(validPackageNodes.containsKey(Paths.get(currentDirectory.toRealPath() + "\\src\\test\\resources\\BookstoreAdvancedV01\\src\\testPackage")));
            assertTrue(validPackageNodes.containsKey(Paths.get(currentDirectory.toRealPath() + "\\src\\test\\resources\\BookstoreAdvancedV01")));

            assertFalse(validPackageNodes.containsKey(Paths.get(currentDirectory.toRealPath() + "\\src\\test\\resources\\BookstoreAdvancedV01\\src\\.settings")));
            assertFalse(validPackageNodes.containsKey(Paths.get(currentDirectory.toRealPath() + "\\src\\test\\resources\\BookstoreAdvancedV01\\src\\bin")));
            assertFalse(validPackageNodes.containsKey(Paths.get(currentDirectory.toRealPath() + "\\src\\test\\resources\\BookstoreAdvancedV01\\src\\bin\\bookstore")));
            assertFalse(validPackageNodes.containsKey(Paths.get(currentDirectory.toRealPath() + "\\src\\test\\resources\\BookstoreAdvancedV01\\src\\bin\\gui")));
            assertFalse(validPackageNodes.containsKey(Paths.get(currentDirectory.toRealPath() + "\\src\\test\\resources\\BookstoreAdvancedV01\\src\\bin\\testPackage")));
            assertFalse(validPackageNodes.containsKey(Paths.get(currentDirectory.toRealPath() + "\\src\\test\\resources\\BookstoreAdvancedV01\\src\\lib")));
            assertFalse(validPackageNodes.containsKey(Paths.get(currentDirectory.toRealPath() + "\\src\\test\\resources\\BookstoreAdvancedV01\\src\\test")));

            PackageNode sourcePackage = validPackageNodes.get(Paths.get(currentDirectory.toRealPath() + "\\src\\test\\resources\\BookstoreAdvancedV01"));
            Map<Path, PackageNode> sourcePackageSubNodes = sourcePackage.getSubNodes();
            assertEquals(sourcePackageSubNodes.size(), 1);
            assertTrue(sourcePackageSubNodes.containsKey(Paths.get(currentDirectory.toRealPath() + "\\src\\test\\resources\\BookstoreAdvancedV01\\src")));

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
