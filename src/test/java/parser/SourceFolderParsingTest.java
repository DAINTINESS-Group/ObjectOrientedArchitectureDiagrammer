package parser;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import model.LeafNode;
import model.PackageNode;

class SourceFolderParsingTest {

	@Test
	void parsingTest() {
		PackageParser parser = new Parser();
		parser.parseSourcePackage("src\\test\\resources\\LatexEditor\\src");
		List<String> sourcesSubPackages = new ArrayList<>(Arrays.asList(
				"src\\test\\resources\\LatexEditor\\src\\controller",
				"src\\test\\resources\\LatexEditor\\src\\model",
				"src\\test\\resources\\LatexEditor\\src\\view"));
		List<String> viewsLeafNodes = new ArrayList<>(Arrays.asList(
				"src\\test\\resources\\LatexEditor\\src\\view\\ChooseTemplate.java",
				"src\\test\\resources\\LatexEditor\\src\\view\\LatexEditorView.java",
				"src\\test\\resources\\LatexEditor\\src\\view\\MainWindow.java",
				"src\\test\\resources\\LatexEditor\\src\\view\\OpeningWindow.java"
		));
		List<String> controllersLeafNodes = new ArrayList<>(List.of(
				"src\\test\\resources\\LatexEditor\\src\\controller\\LatexEditorController.java"
		));
		List<String> strategiesLeafNodes = new ArrayList<>(Arrays.asList(
				"src\\test\\resources\\LatexEditor\\src\\model\\strategies\\StableVersionsStrategy.java",
				"src\\test\\resources\\LatexEditor\\src\\model\\strategies\\VersionsStrategy.java",
				"src\\test\\resources\\LatexEditor\\src\\model\\strategies\\VolatileVersionsStrategy.java",
				"src\\test\\resources\\LatexEditor\\src\\model\\strategies\\VersionsStrategyFactory.java"
		));
		List<String> modelsLeafNodes = new ArrayList<>(Arrays.asList(
				"src\\test\\resources\\LatexEditor\\src\\model\\Document.java",
				"src\\test\\resources\\LatexEditor\\src\\model\\DocumentManager.java",
				"src\\test\\resources\\LatexEditor\\src\\model\\VersionsManager.java"
		));
		List<String> commandsLeafNodes = new ArrayList<>(Arrays.asList(
				"src\\test\\resources\\LatexEditor\\src\\controller\\commands\\AddLatexCommand.java",
				"src\\test\\resources\\LatexEditor\\src\\controller\\commands\\Command.java",
				"src\\test\\resources\\LatexEditor\\src\\controller\\commands\\CommandFactory.java",
				"src\\test\\resources\\LatexEditor\\src\\controller\\commands\\CreateCommand.java",
				"src\\test\\resources\\LatexEditor\\src\\controller\\commands\\EditCommand.java",
				"src\\test\\resources\\LatexEditor\\src\\controller\\commands\\LoadCommand.java",
				"src\\test\\resources\\LatexEditor\\src\\controller\\commands\\SaveCommand.java",
				"src\\test\\resources\\LatexEditor\\src\\controller\\commands\\ChangeVersionsStrategyCommand.java",
				"src\\test\\resources\\LatexEditor\\src\\controller\\commands\\DisableVersionsManagementCommand.java",
				"src\\test\\resources\\LatexEditor\\src\\controller\\commands\\EnableVersionsManagementCommand.java",
				"src\\test\\resources\\LatexEditor\\src\\controller\\commands\\RollbackToPreviousVersionCommand.java"
		));
		Map<String, PackageNode> packageNodes = parser.getPackageNodes();
		
		PackageNode controllerPackage = packageNodes.get("controller");
		List<String> testingLeafNodes = new ArrayList<>();
		assertEquals("src\\test\\resources\\LatexEditor\\src\\controller", controllerPackage.getNodesPath());
		assertEquals("src\\test\\resources\\LatexEditor\\src", controllerPackage.getParentNode().getNodesPath());
		assertTrue(controllerPackage.isValid(), "message");
		Map<String, PackageNode> subNodes = controllerPackage.getSubNodes();
		assertEquals("src\\test\\resources\\LatexEditor\\src\\controller\\commands", subNodes.get("commands").getNodesPath());
		
		for (LeafNode l: controllerPackage.getLeafNodes().values()) {
			testingLeafNodes.add(l.getPath());
			assertEquals(l.getParentNode().getNodesPath(), "src\\test\\resources\\LatexEditor\\src\\controller");
		}
		Collections.sort(testingLeafNodes);
		Collections.sort(controllersLeafNodes);
		assertTrue(testingLeafNodes.size() == controllersLeafNodes.size() 
				&& controllersLeafNodes.containsAll(testingLeafNodes) 
				&& testingLeafNodes.containsAll(controllersLeafNodes));
		testingLeafNodes.clear();
		
		PackageNode commandsPackage = packageNodes.get("commands");
		assertEquals("src\\test\\resources\\LatexEditor\\src\\controller\\commands", commandsPackage.getNodesPath());
		assertEquals("src\\test\\resources\\LatexEditor\\src\\controller", commandsPackage.getParentNode().getNodesPath());
		assertTrue(commandsPackage.isValid());
		subNodes = commandsPackage.getSubNodes();
		assertEquals(0, subNodes.size());
		for (LeafNode l: commandsPackage.getLeafNodes().values()) {
			testingLeafNodes.add(l.getPath());
			assertEquals(l.getParentNode().getNodesPath(), "src\\test\\resources\\LatexEditor\\src\\controller\\commands");
		}
		Collections.sort(testingLeafNodes);
		Collections.sort(commandsLeafNodes);
		assertTrue(testingLeafNodes.size() == commandsLeafNodes.size() 
				&& commandsLeafNodes.containsAll(testingLeafNodes) 
				&& testingLeafNodes.containsAll(commandsLeafNodes));
		testingLeafNodes.clear();
		
		PackageNode modelPackage = packageNodes.get("model");
		assertEquals("src\\test\\resources\\LatexEditor\\src\\model", modelPackage.getNodesPath());
		assertEquals("src\\test\\resources\\LatexEditor\\src", modelPackage.getParentNode().getNodesPath());
		assertTrue(modelPackage.isValid());
		subNodes = modelPackage.getSubNodes();
		assertEquals("src\\test\\resources\\LatexEditor\\src\\model\\strategies", subNodes.get("strategies").getNodesPath());
		for (LeafNode l: modelPackage.getLeafNodes().values()) {
			assertEquals(l.getParentNode().getNodesPath(), "src\\test\\resources\\LatexEditor\\src\\model");
			testingLeafNodes.add(l.getPath());
		}
		Collections.sort(testingLeafNodes);
		Collections.sort(modelsLeafNodes);
		assertTrue(testingLeafNodes.size() == modelsLeafNodes.size() 
				&& modelsLeafNodes.containsAll(testingLeafNodes) 
				&& testingLeafNodes.containsAll(modelsLeafNodes));
		testingLeafNodes.clear();
		
		PackageNode strategiesPackage = packageNodes.get("strategies");
		assertEquals("src\\test\\resources\\LatexEditor\\src\\model\\strategies", strategiesPackage.getNodesPath());
		assertEquals("src\\test\\resources\\LatexEditor\\src\\model", strategiesPackage.getParentNode().getNodesPath());
		assertTrue(strategiesPackage.isValid());
		subNodes = strategiesPackage.getSubNodes();
		assertEquals(0, subNodes.size());
		for (LeafNode l: strategiesPackage.getLeafNodes().values()) {
			assertEquals(l.getParentNode().getNodesPath(), "src\\test\\resources\\LatexEditor\\src\\model\\strategies");
			testingLeafNodes.add(l.getPath());
		}
		Collections.sort(testingLeafNodes);
		Collections.sort(strategiesLeafNodes);
		assertTrue(testingLeafNodes.size() == strategiesLeafNodes.size() 
				&& strategiesLeafNodes.containsAll(testingLeafNodes) 
				&& testingLeafNodes.containsAll(strategiesLeafNodes));
		testingLeafNodes.clear();
		
		PackageNode viewPackage = packageNodes.get("view");
		assertEquals("src\\test\\resources\\LatexEditor\\src\\view", viewPackage.getNodesPath());
		assertEquals("src\\test\\resources\\LatexEditor\\src", viewPackage.getParentNode().getNodesPath());
		assertTrue(viewPackage.isValid());
		subNodes = viewPackage.getSubNodes();
		assertEquals(0, subNodes.size());
		for (LeafNode l: viewPackage.getLeafNodes().values()) {
			assertEquals(l.getParentNode().getNodesPath(), "src\\test\\resources\\LatexEditor\\src\\view");
			testingLeafNodes.add(l.getPath());
		}
		Collections.sort(testingLeafNodes);
		Collections.sort(viewsLeafNodes);
		assertTrue(testingLeafNodes.size() == viewsLeafNodes.size() 
				&& viewsLeafNodes.containsAll(testingLeafNodes) 
				&& testingLeafNodes.containsAll(viewsLeafNodes));
		testingLeafNodes.clear();
		
		PackageNode sourcePackage = packageNodes.get("src");
		assertEquals("src\\test\\resources\\LatexEditor\\src", sourcePackage.getNodesPath());
		assertEquals("", sourcePackage.getParentNode().getNodesPath());
		assertFalse(sourcePackage.isValid());
		subNodes = sourcePackage.getSubNodes();
		List<String> testingSubPackages = new ArrayList<>();
		for (PackageNode subP: subNodes.values()) {
			testingSubPackages.add(subP.getNodesPath());
		}
		Collections.sort(testingSubPackages);
		Collections.sort(sourcesSubPackages);
		assertTrue(testingSubPackages.size() == sourcesSubPackages.size() 
				&& sourcesSubPackages.containsAll(testingSubPackages) 
				&& testingSubPackages.containsAll(sourcesSubPackages));
		assertEquals(0, sourcePackage.getLeafNodes().size());
	}
}
