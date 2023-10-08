package parser.javaparser;

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

public class JavaparserFileVisitorTest {

	ParserType parserType = ParserType.JAVAPARSER;

	@Test
	void methodReturnTypesTest() {
		ProjectParserFactory projectParserFactory = new ProjectParserFactory(parserType);
		Parser parser = projectParserFactory.createProjectParser();

		Map<Path, PackageNode> packages = parser.parseSourcePackage(Paths.get(PathConstructor.getCurrentPath() + File.separator + PathConstructor.constructPath("src", "test", "resources", "LatexEditor", "src")));
		PackageNode commandPackage = packages.get(Paths.get(PathConstructor.getCurrentPath().normalize().toString(), PathConstructor.constructPath("src", "test", "resources", "LatexEditor", "src", "controller", "commands")));
		LeafNode addLatexCommand = commandPackage.getLeafNodes().get("AddLatexCommand");

		List<String> methodReturnTypes = new ArrayList<>(Arrays.asList("Constructor", "void"));
		List<String> methodReturnTypesTest = addLatexCommand.getMethodsReturnTypes();

		Collections.sort(methodReturnTypesTest);
		Collections.sort(methodReturnTypes);
		assertTrue(
			methodReturnTypesTest.size() == methodReturnTypes.size() &&
		   methodReturnTypes.containsAll(methodReturnTypesTest) &&
		   methodReturnTypesTest.containsAll(methodReturnTypes));
	}

	@Test
	void methodParameterTypesTest() {
		ProjectParserFactory projectParserFactory = new ProjectParserFactory(parserType);
		Parser parser = projectParserFactory.createProjectParser();

		Map<Path, PackageNode> packages = parser.parseSourcePackage(Paths.get(PathConstructor.getCurrentPath() + File.separator + PathConstructor.constructPath("src", "test", "resources", "LatexEditor", "src")));
		PackageNode commandPackage = packages.get(Paths.get(PathConstructor.getCurrentPath().normalize().toString(), PathConstructor.constructPath("src", "test", "resources", "LatexEditor", "src", "controller", "commands")));
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

	@Test
	void fieldTypesTest() {
		ProjectParserFactory projectParserFactory = new ProjectParserFactory(parserType);
		Parser parser = projectParserFactory.createProjectParser();

		Map<Path, PackageNode> packages = parser.parseSourcePackage(Paths.get(PathConstructor.getCurrentPath() + File.separator + PathConstructor.constructPath("src", "test", "resources", "LatexEditor", "src")));
		PackageNode commandPackage = packages.get(Paths.get(PathConstructor.getCurrentPath().normalize().toString(), PathConstructor.constructPath("src", "test", "resources", "LatexEditor", "src", "controller", "commands")));
		LeafNode addLatexCommand = commandPackage.getLeafNodes().get("AddLatexCommand");

		List<String> fieldTypes = new ArrayList<>(List.of("VersionsManager"));
		List<String> fieldTypesTest = addLatexCommand.getFieldsTypes();

		Collections.sort(fieldTypesTest);
		Collections.sort(fieldTypes);
		assertTrue(fieldTypesTest.size() == fieldTypes.size()
				&& fieldTypes.containsAll(fieldTypesTest)
				&& fieldTypesTest.containsAll(fieldTypes));
	}

	@Test
	void variableTypesTest() {
		ProjectParserFactory projectParserFactory = new ProjectParserFactory(parserType);
		Parser parser = projectParserFactory.createProjectParser();

		Map<Path, PackageNode> packages = parser.parseSourcePackage(Paths.get(PathConstructor.getCurrentPath() + File.separator + PathConstructor.constructPath("src", "test", "resources", "LatexEditor", "src")));
		PackageNode commandPackage = packages.get(Paths.get(PathConstructor.getCurrentPath().normalize().toString(),
				"/src/test/resources/LatexEditor/src/controller"));
		LeafNode latexEditorController = commandPackage.getLeafNodes().get("LatexEditorController");

		List<String> variablesTypes = new ArrayList<>(List.of("CommandFactory"));
		List<String> variablesTypesTest = ((JavaparserLeafNode) latexEditorController).getVariablesTypes();

		Collections.sort(variablesTypesTest);
		Collections.sort(variablesTypes);
		assertTrue(
			variablesTypesTest.size() == variablesTypes.size() &&
			 variablesTypes.containsAll(variablesTypesTest) &&
			 variablesTypesTest.containsAll(variablesTypes));
	}

	@Test
	void objectCreationTest() {
		ProjectParserFactory projectParserFactory = new ProjectParserFactory(parserType);
		Parser parser = projectParserFactory.createProjectParser();

		Map<Path, PackageNode> packages = parser.parseSourcePackage(Paths.get(PathConstructor.getCurrentPath() + File.separator + PathConstructor.constructPath("src", "test", "resources", "ParserTesting")));
		PackageNode sourcePackage = packages.get(Paths.get(PathConstructor.getCurrentPath().normalize().toString(), PathConstructor.constructPath("src", "test", "resources", "ParserTesting")));
		LeafNode objectCreationTest = sourcePackage.getLeafNodes().get("ObjectCreationTest");

		List<String> objectsCreated = new ArrayList<>(List.of("ImplementingClass", "ImplementingClass", "ExtensionClass", "HashMap[String,TestingInterface]"));
		List<String> objectsCreatedTest = ((JavaparserLeafNode) objectCreationTest).getCreatedObjects();

		Collections.sort(objectsCreatedTest);
		Collections.sort(objectsCreated);
		assertTrue(
		objectsCreatedTest.size() == objectsCreated.size()
			&& objectsCreated.containsAll(objectsCreatedTest)
			&& objectsCreatedTest.containsAll(objectsCreated));
	}

	@Test
	void leafNodeTypesTest() {
		ProjectParserFactory projectParserFactory = new ProjectParserFactory(parserType);
		Parser parser = projectParserFactory.createProjectParser();

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
