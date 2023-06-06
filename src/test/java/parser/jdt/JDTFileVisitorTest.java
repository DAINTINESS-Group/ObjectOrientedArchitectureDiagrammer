package parser.jdt;

import parser.tree.node.LeafNode;
import parser.tree.node.NodeType;
import parser.tree.node.PackageNode;
import org.junit.jupiter.api.Test;
import parser.factory.Parser;
import parser.factory.ParserType;
import parser.factory.ProjectParserFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JDTFileVisitorTest {

	Path currentDirectory = Path.of(".");
	ParserType parserType = ParserType.JDT;

	@Test
	void methodReturnTypesTest() {
		try {
			ProjectParserFactory projectParserFactory = new ProjectParserFactory(parserType);
			Parser parser = projectParserFactory.createProjectParser();

			parser.parseSourcePackage(Paths.get(currentDirectory.toRealPath() + "\\src\\test\\resources\\LatexEditor\\src"));
			Map<Path, PackageNode> packages = parser.getPackageNodes();
			PackageNode commandPackage = packages.get(Paths.get(currentDirectory.toRealPath().normalize().toString(),
					"\\src\\test\\resources\\LatexEditor\\src\\controller\\commands"));
			LeafNode addLatexCommand = commandPackage.getLeafNodes().get("AddLatexCommand");

			List<String> methodReturnTypes = new ArrayList<>(Arrays.asList("Constructor", "void"));
			List<String> methodReturnTypesTest = addLatexCommand.getMethodsReturnTypes();

			Collections.sort(methodReturnTypesTest);
			Collections.sort(methodReturnTypes);
			assertTrue(methodReturnTypesTest.size() == methodReturnTypes.size()
					&& methodReturnTypes.containsAll(methodReturnTypesTest)
					&& methodReturnTypesTest.containsAll(methodReturnTypes));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	void methodParameterTypesTest() {
		try {
			ProjectParserFactory projectParserFactory = new ProjectParserFactory(parserType);
			Parser parser = projectParserFactory.createProjectParser();

			parser.parseSourcePackage(Paths.get(currentDirectory.toRealPath() + "\\src\\test\\resources\\LatexEditor\\src"));
			Map<Path, PackageNode> packages = parser.getPackageNodes();
			PackageNode commandPackage = packages.get(Paths.get(currentDirectory.toRealPath().normalize().toString(),
					"\\src\\test\\resources\\LatexEditor\\src\\controller\\commands"));
			LeafNode addLatexCommand = commandPackage.getLeafNodes().get("AddLatexCommand");

			List<String> methodParameterTypes = new ArrayList<>(List.of("VersionsManager"));
			List<String> methodParameterTypesTest = addLatexCommand.getMethodParameterTypes();

			Collections.sort(methodParameterTypesTest);
			Collections.sort(methodParameterTypes);
			assertTrue(methodParameterTypesTest.size() == methodParameterTypes.size()
					&& methodParameterTypes.containsAll(methodParameterTypesTest)
					&& methodParameterTypesTest.containsAll(methodParameterTypes));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	void fieldTypesTest() {
		try {
			ProjectParserFactory projectParserFactory = new ProjectParserFactory(parserType);
			Parser parser = projectParserFactory.createProjectParser();

			parser.parseSourcePackage(Paths.get(currentDirectory.toRealPath() + "\\src\\test\\resources\\LatexEditor\\src"));
			Map<Path, PackageNode> packages = parser.getPackageNodes();
			PackageNode commandPackage = packages.get(Paths.get(currentDirectory.toRealPath().normalize().toString(),
					"\\src\\test\\resources\\LatexEditor\\src\\controller\\commands"));
			LeafNode addLatexCommand = commandPackage.getLeafNodes().get("AddLatexCommand");

			List<String> fieldTypes = new ArrayList<>(List.of("VersionsManager"));
			List<String> fieldTypesTest = addLatexCommand.getFieldsTypes();

			Collections.sort(fieldTypesTest);
			Collections.sort(fieldTypes);
			assertTrue(fieldTypesTest.size() == fieldTypes.size()
					&& fieldTypes.containsAll(fieldTypesTest)
					&& fieldTypesTest.containsAll(fieldTypes));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	void leafNodeTypesTest() {
		try {
			ProjectParserFactory projectParserFactory = new ProjectParserFactory(parserType);
			Parser parser = projectParserFactory.createProjectParser();

			parser.parseSourcePackage(Paths.get(currentDirectory.toRealPath() + "\\src\\test\\resources\\ParserTesting"));
			Map<Path, PackageNode> packages = parser.getPackageNodes();
			PackageNode inheritancePackage = packages.get(Paths.get(currentDirectory.toRealPath().normalize().toString(), "\\src\\test\\resources\\ParserTesting"));
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

			PackageNode sourcePackage = packages.get(Paths.get(currentDirectory.toRealPath().normalize().toString(), "\\src\\test\\resources\\ParserTesting"));
			LeafNode enumTest = sourcePackage.getLeafNodes().get("EnumTest");
			assertEquals(NodeType.ENUM, enumTest.getType());

			LeafNode objectCreationTest = sourcePackage.getLeafNodes().get("ObjectCreationTest");
			assertEquals(NodeType.CLASS, objectCreationTest.getType());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

