package parser.jdt;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import parser.factory.Parser;
import parser.factory.ParserType;
import parser.factory.ProjectParserFactory;
import parser.tree.LeafNode;
import parser.tree.NodeType;
import parser.tree.PackageNode;
import utils.PathConstructor;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Disabled
class JDTFileVisitorTest {

	ParserType parserType = ParserType.JDT;

	@Deprecated
	@Test
	void methodReturnTypesTest() {
		Parser parser = ProjectParserFactory.createProjectParser(parserType);

		Map<Path, PackageNode> packages = parser.parseSourcePackage(Paths.get(PathConstructor.getCurrentPath() + File.separator + PathConstructor.constructPath("src", "test", "resources", "LatexEditor", "src")));
		PackageNode commandPackage = packages.get(Paths.get(PathConstructor.getCurrentPath().normalize().toString(),
				PathConstructor.constructPath("src", "test", "resources", "LatexEditor", "src", "controller", "commands")));
		LeafNode addLatexCommand = commandPackage.getLeafNodes().get("AddLatexCommand");

		List<String> methodReturnTypes = new ArrayList<>(Arrays.asList("Constructor", "void"));
		List<String> methodReturnTypesTest = addLatexCommand.getMethodsReturnTypes();

		Collections.sort(methodReturnTypesTest);
		Collections.sort(methodReturnTypes);
		assertTrue(methodReturnTypesTest.size() == methodReturnTypes.size()
				&& methodReturnTypes.containsAll(methodReturnTypesTest)
				&& methodReturnTypesTest.containsAll(methodReturnTypes));
	}

	@Deprecated
	@Test
	void methodParameterTypesTest() {
		Parser parser = ProjectParserFactory.createProjectParser(parserType);

		Map<Path, PackageNode> packages = parser.parseSourcePackage(Paths.get(PathConstructor.getCurrentPath() + PathConstructor.constructPath("src", "test", "resources", "LatexEditor", "src")));
		PackageNode commandPackage = packages.get(Paths.get(PathConstructor.getCurrentPath().normalize().toString(),
				PathConstructor.constructPath("src", "test", "resources", "LatexEditor", "src", "controller", "commands")));
		LeafNode addLatexCommand = commandPackage.getLeafNodes().get("AddLatexCommand");

		List<String> methodParameterTypes = new ArrayList<>(List.of("VersionsManager"));
		List<String> methodParameterTypesTest = addLatexCommand.getMethodParameterTypes();

		Collections.sort(methodParameterTypesTest);
		Collections.sort(methodParameterTypes);
		assertTrue(
		methodParameterTypesTest.size() == methodParameterTypes.size() &&
			methodParameterTypes.containsAll(methodParameterTypesTest) &&
			methodParameterTypesTest.containsAll(methodParameterTypes));
	}

	@Deprecated
	@Test
	void fieldTypesTest() {
		Parser parser = ProjectParserFactory.createProjectParser(parserType);

		Map<Path, PackageNode> packages = parser.parseSourcePackage(Paths.get(PathConstructor.getCurrentPath() + File.separator + PathConstructor.constructPath("src", "test", "resources", "LatexEditor", "src")));
		PackageNode commandPackage = packages.get(Paths.get(PathConstructor.getCurrentPath().normalize().toString(),
				PathConstructor.constructPath("src", "test", "resources", "LatexEditor", "src", "controller", "commands")));
		LeafNode addLatexCommand = commandPackage.getLeafNodes().get("AddLatexCommand");

		List<String> fieldTypes = new ArrayList<>(List.of("VersionsManager"));
		List<String> fieldTypesTest = addLatexCommand.getFieldsTypes();

		Collections.sort(fieldTypesTest);
		Collections.sort(fieldTypes);
		assertTrue(
	fieldTypesTest.size() == fieldTypes.size() &&
			fieldTypes.containsAll(fieldTypesTest) &&
			fieldTypesTest.containsAll(fieldTypes));
	}

	@Deprecated
	@Test
	void leafNodeTypesTest() {
		Parser parser = ProjectParserFactory.createProjectParser(parserType);

		Map<Path, PackageNode> packages = parser.parseSourcePackage(Paths.get(PathConstructor.getCurrentPath() + File.separator + PathConstructor.constructPath("src", "test", "resources", "ParserTesting")));
		PackageNode inheritancePackage = packages.get(Paths.get(PathConstructor.getCurrentPath().normalize().toString(), PathConstructor.constructPath("src", "test", "resources", "ParserTesting")));
		List<LeafNode> classLeafs = new ArrayList<>();
		List<LeafNode> interfaceLeafs = new ArrayList<>();
		classLeafs.add(inheritancePackage.getLeafNodes().get("ImplementingClass"));
		classLeafs.add(inheritancePackage.getLeafNodes().get("ExtensionClass"));
		interfaceLeafs.add(inheritancePackage.getLeafNodes().get("TestingInterface"));
		interfaceLeafs.add(inheritancePackage.getLeafNodes().get("TestingInterface2"));

		for (LeafNode l : classLeafs) {
			assertEquals(NodeType.CLASS, l.getType());
		}
		for (LeafNode l : interfaceLeafs) {
			assertEquals(NodeType.INTERFACE, l.getType());
		}

		PackageNode sourcePackage = packages.get(Paths.get(PathConstructor.getCurrentPath().normalize().toString(), PathConstructor.constructPath("src", "test", "resources", "ParserTesting")));
		LeafNode enumTest = sourcePackage.getLeafNodes().get("EnumTest");
		assertEquals(NodeType.ENUM, enumTest.getType());

		LeafNode objectCreationTest = sourcePackage.getLeafNodes().get("ObjectCreationTest");
		assertEquals(NodeType.CLASS, objectCreationTest.getType());
	}
}

