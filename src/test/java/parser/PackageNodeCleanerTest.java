package parser;

import org.junit.jupiter.api.Test;
import parser.tree.PackageNode;
import utils.PathConstructor;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PackageNodeCleanerTest {

	@Test
	void removeNonPackageNodesTest() {
		Interpreter interpreter = new Interpreter();
		interpreter.parseProject(Paths.get(PathConstructor.getCurrentPath() + File.separator + PathConstructor.constructPath("src", "test", "resources", "BookstoreAdvancedV01")));
		Map<Path, PackageNode> packageNodes = interpreter.getPackageNodes();

		PackageNodeCleaner packageNodeCleaner = new PackageNodeCleaner(packageNodes);
		Map<Path, PackageNode> validPackageNodes = packageNodeCleaner.removeNonPackageNodes();

		assertEquals(packageNodes.size(), validPackageNodes.size() + 5);

		// Valid Package Nodes
		assertTrue(validPackageNodes.containsKey(Paths.get(PathConstructor.getCurrentPath() + File.separator + PathConstructor.constructPath("src", "test", "resources", "BookstoreAdvancedV01", "src"))));
		assertTrue(validPackageNodes.containsKey(Paths.get(PathConstructor.getCurrentPath() + File.separator + PathConstructor.constructPath("src", "test", "resources", "BookstoreAdvancedV01", "src", "bookstore"))));
		assertTrue(validPackageNodes.containsKey(Paths.get(PathConstructor.getCurrentPath() + File.separator + PathConstructor.constructPath("src", "test", "resources", "BookstoreAdvancedV01", "src", "gui"))));
		assertTrue(validPackageNodes.containsKey(Paths.get(PathConstructor.getCurrentPath() + File.separator + PathConstructor.constructPath("src", "test", "resources", "BookstoreAdvancedV01"))));

		// Non Valid Package Nodes
		assertFalse(validPackageNodes.containsKey(Paths.get(PathConstructor.getCurrentPath() + File.separator + PathConstructor.constructPath("src", "test", "resources", "BookstoreAdvancedV01", "src", ".settings"))));
		assertFalse(validPackageNodes.containsKey(Paths.get(PathConstructor.getCurrentPath() + File.separator + PathConstructor.constructPath("src", "test", "resources", "BookstoreAdvancedV01", "src", "bin"))));
		assertFalse(validPackageNodes.containsKey(Paths.get(PathConstructor.getCurrentPath() + File.separator + PathConstructor.constructPath("src", "test", "resources", "BookstoreAdvancedV01", "src", "bin", "bookstore"))));
		assertFalse(validPackageNodes.containsKey(Paths.get(PathConstructor.getCurrentPath() + File.separator + PathConstructor.constructPath("src", "test", "resources", "BookstoreAdvancedV01", "src", "bin", "gui"))));
		assertFalse(validPackageNodes.containsKey(Paths.get(PathConstructor.getCurrentPath() + File.separator + PathConstructor.constructPath("src", "test", "resources", "BookstoreAdvancedV01", "src", "lib"))));

		PackageNode sourcePackage = validPackageNodes.get(Paths.get(PathConstructor.getCurrentPath() + File.separator + PathConstructor.constructPath("src", "test", "resources", "BookstoreAdvancedV01")));
		Map<Path, PackageNode> sourcePackageSubNodes = sourcePackage.getSubNodes();
		assertEquals(sourcePackageSubNodes.size(), 1);
		assertTrue(sourcePackageSubNodes.containsKey(Paths.get(PathConstructor.getCurrentPath() + File.separator + PathConstructor.constructPath("src", "test", "resources", "BookstoreAdvancedV01", "src"))));
	}
}
