package parser.javaparser;

import org.junit.jupiter.api.Test;
import parser.factory.Parser;
import parser.factory.ParserType;
import parser.factory.ProjectParserFactory;
import parser.tree.LeafNode;
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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JavaparserProjectParserTest {

	ParserType parserType = ParserType.JAVAPARSER;

	@Test
	void parsingTest() {
		List<Path> sourcesSubPackages = new ArrayList<>(
			Arrays.asList(
				Paths.get(PathConstructor.getCurrentPath().normalize().toString(), PathConstructor.constructPath("src", "test", "resources", "LatexEditor", "src", "controller")),
				Paths.get(PathConstructor.getCurrentPath().normalize().toString(), PathConstructor.constructPath("src", "test", "resources", "LatexEditor", "src", "model")),
				Paths.get(PathConstructor.getCurrentPath().normalize().toString(), PathConstructor.constructPath("src", "test", "resources", "LatexEditor", "src", "view"))
			));
		List<Path> viewsLeafNodes = new ArrayList<>(
			Arrays.asList(
				Paths.get(PathConstructor.getCurrentPath().normalize().toString(), PathConstructor.constructPath("src", "test", "resources", "LatexEditor", "src", "view", "ChooseTemplate.java")),
				Paths.get(PathConstructor.getCurrentPath().normalize().toString(), PathConstructor.constructPath("src", "test", "resources", "LatexEditor", "src", "view", "LatexEditorView.java")),
				Paths.get(PathConstructor.getCurrentPath().normalize().toString(), PathConstructor.constructPath("src", "test", "resources", "LatexEditor", "src", "view", "MainWindow.java")),
				Paths.get(PathConstructor.getCurrentPath().normalize().toString(), PathConstructor.constructPath("src", "test", "resources", "LatexEditor", "src", "view", "OpeningWindow.java"))
			));
		List<Path> controllersLeafNodes = new ArrayList<>(
			List.of(
				Paths.get(PathConstructor.getCurrentPath().normalize().toString(), "/src/test/resources/LatexEditor/src/controller/LatexEditorController.java")
			));
		List<Path> strategiesLeafNodes = new ArrayList<>(
			Arrays.asList(
				Paths.get(PathConstructor.getCurrentPath().normalize().toString(), PathConstructor.constructPath("src", "test", "resources", "LatexEditor", "src", "model", "strategies", "StableVersionsStrategy.java")),
				Paths.get(PathConstructor.getCurrentPath().normalize().toString(), PathConstructor.constructPath("src", "test", "resources", "LatexEditor", "src", "model", "strategies", "VersionsStrategy.java")),
				Paths.get(PathConstructor.getCurrentPath().normalize().toString(), PathConstructor.constructPath("src", "test", "resources", "LatexEditor", "src", "model", "strategies", "VolatileVersionsStrategy.java")),
				Paths.get(PathConstructor.getCurrentPath().normalize().toString(), PathConstructor.constructPath("src", "test", "resources", "LatexEditor", "src", "model", "strategies", "VersionsStrategyFactory.java"))
			));
		List<Path> modelsLeafNodes = new ArrayList<>(
			Arrays.asList(
				Paths.get(PathConstructor.getCurrentPath().normalize().toString(), PathConstructor.constructPath("src", "test", "resources", "LatexEditor", "src", "model", "Document.java")),
				Paths.get(PathConstructor.getCurrentPath().normalize().toString(), PathConstructor.constructPath("src", "test", "resources", "LatexEditor", "src", "model", "DocumentManager.java")),
				Paths.get(PathConstructor.getCurrentPath().normalize().toString(), PathConstructor.constructPath("src", "test", "resources", "LatexEditor", "src", "model", "VersionsManager.java"))
			));
		List<Path> commandsLeafNodes = new ArrayList<>(
			Arrays.asList(
				Paths.get(PathConstructor.getCurrentPath().normalize().toString(), PathConstructor.constructPath("src", "test", "resources", "LatexEditor", "src", "controller", "commands", "AddLatexCommand.java")),
				Paths.get(PathConstructor.getCurrentPath().normalize().toString(), PathConstructor.constructPath("src", "test", "resources", "LatexEditor", "src", "controller", "commands", "Command.java")),
				Paths.get(PathConstructor.getCurrentPath().normalize().toString(), PathConstructor.constructPath("src", "test", "resources", "LatexEditor", "src", "controller", "commands", "CommandFactory.java")),
				Paths.get(PathConstructor.getCurrentPath().normalize().toString(), PathConstructor.constructPath("src", "test", "resources", "LatexEditor", "src", "controller", "commands", "CreateCommand.java")),
				Paths.get(PathConstructor.getCurrentPath().normalize().toString(), PathConstructor.constructPath("src", "test", "resources", "LatexEditor", "src", "controller", "commands", "EditCommand.java")),
				Paths.get(PathConstructor.getCurrentPath().normalize().toString(), PathConstructor.constructPath("src", "test", "resources", "LatexEditor", "src", "controller", "commands", "LoadCommand.java")),
				Paths.get(PathConstructor.getCurrentPath().normalize().toString(), PathConstructor.constructPath("src", "test", "resources", "LatexEditor", "src", "controller", "commands", "SaveCommand.java")),
				Paths.get(PathConstructor.getCurrentPath().normalize().toString(), PathConstructor.constructPath("src", "test", "resources", "LatexEditor", "src", "controller", "commands", "ChangeVersionsStrategyCommand.java")),
				Paths.get(PathConstructor.getCurrentPath().normalize().toString(), PathConstructor.constructPath("src", "test", "resources", "LatexEditor", "src", "controller", "commands", "DisableVersionsManagementCommand.java")),
				Paths.get(PathConstructor.getCurrentPath().normalize().toString(), PathConstructor.constructPath("src", "test", "resources", "LatexEditor", "src", "controller", "commands", "EnableVersionsManagementCommand.java")),
				Paths.get(PathConstructor.getCurrentPath().normalize().toString(), PathConstructor.constructPath("src", "test", "resources", "LatexEditor", "src", "controller", "commands", "RollbackToPreviousVersionCommand.java"))
			));

		ProjectParserFactory projectParserFactory = new ProjectParserFactory(parserType);
		Parser parser = projectParserFactory.createProjectParser();
		Map<Path, PackageNode> packageNodes = parser.parseSourcePackage(Paths.get(PathConstructor.getCurrentPath() + File.separator + PathConstructor.constructPath("src", "test", "resources", "LatexEditor", "src")));

		PackageNode controllerPackage = packageNodes.get(Paths.get(PathConstructor.getCurrentPath().normalize().toString(), PathConstructor.constructPath("src", "test", "resources", "LatexEditor", "src", "controller")));
		List<Path> testingLeafNodes = new ArrayList<>();
		assertEquals(Paths.get(PathConstructor.getCurrentPath().normalize().toString(),
				PathConstructor.constructPath("src", "test", "resources", "LatexEditor", "src", "controller")), controllerPackage.getPackageNodesPath());
		assertEquals(Paths.get(PathConstructor.getCurrentPath().normalize().toString(),
				File.separator + PathConstructor.constructPath("src", "test", "resources", "LatexEditor", "src")), controllerPackage.getParentNode().getPackageNodesPath());
		assertTrue(controllerPackage.isValid(), "message");
		Map<Path, PackageNode> subNodes = controllerPackage.getSubNodes();
		assertEquals(Paths.get(PathConstructor.getCurrentPath().normalize().toString(),
				PathConstructor.constructPath("src", "test", "resources", "LatexEditor", "src", "controller", "commands")),
				subNodes.get(Paths.get(PathConstructor.getCurrentPath().normalize().toString(),
						PathConstructor.constructPath("src", "test", "resources", "LatexEditor", "src", "controller", "commands"))).getPackageNodesPath());

		for (LeafNode l : controllerPackage.getLeafNodes().values()) {
			testingLeafNodes.add(l.getLeafNodesPath());
			assertEquals(l.getParentNode().getPackageNodesPath(), Paths.get(PathConstructor.getCurrentPath().normalize().toString(),
					PathConstructor.constructPath("src", "test", "resources", "LatexEditor", "src", "controller")));
		}
		Collections.sort(testingLeafNodes);
		Collections.sort(controllersLeafNodes);
		assertTrue(testingLeafNodes.size() == controllersLeafNodes.size()
				&& controllersLeafNodes.containsAll(testingLeafNodes)
				&& testingLeafNodes.containsAll(controllersLeafNodes));
		testingLeafNodes.clear();

		PackageNode commandsPackage = packageNodes.get(Paths.get(PathConstructor.getCurrentPath().normalize().toString(),
				PathConstructor.constructPath("src", "test", "resources", "LatexEditor", "src", "controller", "commands")));
		assertEquals(Paths.get(PathConstructor.getCurrentPath().normalize().toString(),
				PathConstructor.constructPath("src", "test", "resources", "LatexEditor", "src", "controller", "commands")), commandsPackage.getPackageNodesPath());
		assertEquals(Paths.get(PathConstructor.getCurrentPath().normalize().toString(),
				PathConstructor.constructPath("src", "test", "resources", "LatexEditor", "src", "controller")), commandsPackage.getParentNode().getPackageNodesPath());
		assertTrue(commandsPackage.isValid());
		subNodes = commandsPackage.getSubNodes();
		assertEquals(0, subNodes.size());
		for (LeafNode l : commandsPackage.getLeafNodes().values()) {
			testingLeafNodes.add(l.getLeafNodesPath());
			assertEquals(l.getParentNode().getPackageNodesPath(), Paths.get(PathConstructor.getCurrentPath().normalize().toString(),
					PathConstructor.constructPath("src", "test", "resources", "LatexEditor", "src", "controller", "commands")));
		}
		Collections.sort(testingLeafNodes);
		Collections.sort(commandsLeafNodes);
		assertTrue(testingLeafNodes.size() == commandsLeafNodes.size()
				&& commandsLeafNodes.containsAll(testingLeafNodes)
				&& testingLeafNodes.containsAll(commandsLeafNodes));
		testingLeafNodes.clear();

		PackageNode modelPackage = packageNodes.get(Paths.get(PathConstructor.getCurrentPath().normalize().toString(),
				PathConstructor.constructPath("src", "test", "resources", "LatexEditor", "src", "model")));
		assertEquals(Paths.get(PathConstructor.getCurrentPath().normalize().toString(),
				PathConstructor.constructPath("src", "test", "resources", "LatexEditor", "src", "model")), modelPackage.getPackageNodesPath());
		assertEquals(Paths.get(PathConstructor.getCurrentPath().normalize().toString(),
				File.separator + PathConstructor.constructPath("src", "test", "resources", "LatexEditor", "src")), modelPackage.getParentNode().getPackageNodesPath());
		assertTrue(modelPackage.isValid());
		subNodes = modelPackage.getSubNodes();
		assertEquals(Paths.get(PathConstructor.getCurrentPath().normalize().toString(),
				PathConstructor.constructPath("src", "test", "resources", "LatexEditor", "src", "model", "strategies")),
				subNodes.get(Paths.get(PathConstructor.getCurrentPath().normalize().toString(),
						PathConstructor.constructPath("src", "test", "resources", "LatexEditor", "src", "model", "strategies"))).getPackageNodesPath());
		for (LeafNode l : modelPackage.getLeafNodes().values()) {
			assertEquals(l.getParentNode().getPackageNodesPath(), Paths.get(PathConstructor.getCurrentPath().normalize().toString(),
					PathConstructor.constructPath("src", "test", "resources", "LatexEditor", "src", "model")));
			testingLeafNodes.add(l.getLeafNodesPath());
		}
		Collections.sort(testingLeafNodes);
		Collections.sort(modelsLeafNodes);
		assertTrue(testingLeafNodes.size() == modelsLeafNodes.size()
				&& modelsLeafNodes.containsAll(testingLeafNodes)
				&& testingLeafNodes.containsAll(modelsLeafNodes));
		testingLeafNodes.clear();

		PackageNode strategiesPackage = packageNodes.get(Paths.get(PathConstructor.getCurrentPath().normalize().toString(),
				PathConstructor.constructPath("src", "test", "resources", "LatexEditor", "src", "model", "strategies")));
		assertEquals(Paths.get(PathConstructor.getCurrentPath().normalize().toString(),
				PathConstructor.constructPath("src", "test", "resources", "LatexEditor", "src", "model", "strategies")), strategiesPackage.getPackageNodesPath());
		assertEquals(Paths.get(PathConstructor.getCurrentPath().normalize().toString(),
				PathConstructor.constructPath("src", "test", "resources", "LatexEditor", "src", "model")), strategiesPackage.getParentNode().getPackageNodesPath());
		assertTrue(strategiesPackage.isValid());
		subNodes = strategiesPackage.getSubNodes();
		assertEquals(0, subNodes.size());
		for (LeafNode l : strategiesPackage.getLeafNodes().values()) {
			assertEquals(l.getParentNode().getPackageNodesPath(), Paths.get(PathConstructor.getCurrentPath().normalize().toString(),
					PathConstructor.constructPath("src", "test", "resources", "LatexEditor", "src", "model", "strategies")));
			testingLeafNodes.add(l.getLeafNodesPath());
		}
		Collections.sort(testingLeafNodes);
		Collections.sort(strategiesLeafNodes);
		assertTrue(testingLeafNodes.size() == strategiesLeafNodes.size()
				&& strategiesLeafNodes.containsAll(testingLeafNodes)
				&& testingLeafNodes.containsAll(strategiesLeafNodes));
		testingLeafNodes.clear();

		PackageNode viewPackage = packageNodes.get(Paths.get(PathConstructor.getCurrentPath().normalize().toString(),
				PathConstructor.constructPath("src", "test", "resources", "LatexEditor", "src", "view")));
		assertEquals(Paths.get(PathConstructor.getCurrentPath().normalize().toString(),
				PathConstructor.constructPath("src", "test", "resources", "LatexEditor", "src", "view")), viewPackage.getPackageNodesPath());
		assertEquals(Paths.get(PathConstructor.getCurrentPath().normalize().toString(),
				File.separator + PathConstructor.constructPath("src", "test", "resources", "LatexEditor", "src")), viewPackage.getParentNode().getPackageNodesPath());
		assertTrue(viewPackage.isValid());
		subNodes = viewPackage.getSubNodes();
		assertEquals(0, subNodes.size());
		for (LeafNode l : viewPackage.getLeafNodes().values()) {
			assertEquals(l.getParentNode().getPackageNodesPath(), Paths.get(PathConstructor.getCurrentPath().normalize().toString(),
					PathConstructor.constructPath("src", "test", "resources", "LatexEditor", "src", "view")));
			testingLeafNodes.add(l.getLeafNodesPath());
		}
		Collections.sort(testingLeafNodes);
		Collections.sort(viewsLeafNodes);
		assertTrue(testingLeafNodes.size() == viewsLeafNodes.size()
				&& viewsLeafNodes.containsAll(testingLeafNodes)
				&& testingLeafNodes.containsAll(viewsLeafNodes));
		testingLeafNodes.clear();

		PackageNode sourcePackage = packageNodes.get(Paths.get(PathConstructor.getCurrentPath().normalize().toString(),
				File.separator + PathConstructor.constructPath("src", "test", "resources", "LatexEditor", "src")));
		assertEquals(Paths.get(PathConstructor.getCurrentPath().normalize().toString(),
				File.separator + PathConstructor.constructPath("src", "test", "resources", "LatexEditor", "src")), sourcePackage.getPackageNodesPath());
		assertEquals(Paths.get(""), sourcePackage.getParentNode().getPackageNodesPath());
		assertFalse(sourcePackage.isValid());
		subNodes = sourcePackage.getSubNodes();
		List<Path> testingSubPackages = new ArrayList<>();
		for (PackageNode subP : subNodes.values()) {
			testingSubPackages.add(subP.getPackageNodesPath());
		}
		Collections.sort(testingSubPackages);
		Collections.sort(sourcesSubPackages);
		assertTrue(
			testingSubPackages.size() == sourcesSubPackages.size() &&
			sourcesSubPackages.containsAll(testingSubPackages) &&
			testingSubPackages.containsAll(sourcesSubPackages));
		assertEquals(0, sourcePackage.getLeafNodes().size());
	}

}
