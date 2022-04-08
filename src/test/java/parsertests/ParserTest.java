package parsertests;

import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import model.LeafNode;
import model.PackageNode;
import parser.Parser;

class ParserTest {
	private Parser parser = new Parser("src\\test\\resources\\LatexEditor\\src");
	private List<PackageNode> packageNodes = new ArrayList<PackageNode>();
	private List<PackageNode> subNodes = new ArrayList<PackageNode>();
	private List<LeafNode> leafNodes = new ArrayList<LeafNode>();
	private List<String> sourcesSubPackages = new ArrayList<String>();
	private List<String> viewsLeafNodes = new ArrayList<String>();
	private List<String> strategiesLeafNodes = new ArrayList<String>();
	private List<String> modelsLeafNodes = new ArrayList<String>();
	private List<String> commandsLeafNodes = new ArrayList<String>();
	private List<String> controllersLeafNodes = new ArrayList<String>();
	@Test
	void test() {
		sourcesSubPackages = new ArrayList<>(Arrays.asList(
				"src\\test\\resources\\LatexEditor\\src\\controller",
				"src\\test\\resources\\LatexEditor\\src\\model",
				"src\\test\\resources\\LatexEditor\\src\\view"));
		viewsLeafNodes = new ArrayList<>(Arrays.asList(
				"src\\test\\resources\\LatexEditor\\src\\view\\ChooseTemplate.java",
				"src\\test\\resources\\LatexEditor\\src\\view\\LatexEditorView.java",
				"src\\test\\resources\\LatexEditor\\src\\view\\MainWindow.java",
				"src\\test\\resources\\LatexEditor\\src\\view\\OpeningWindow.java"
				));
		controllersLeafNodes = new ArrayList<>(Arrays.asList(
				"src\\test\\resources\\LatexEditor\\src\\controller\\LatexEditorController.java"
				));
		strategiesLeafNodes = new ArrayList<>(Arrays.asList(
				"src\\test\\resources\\LatexEditor\\src\\model\\strategies\\StableVersionsStrategy.java",
				"src\\test\\resources\\LatexEditor\\src\\model\\strategies\\VersionsStrategy.java",
				"src\\test\\resources\\LatexEditor\\src\\model\\strategies\\VolatileVersionsStrategy.java",
				"src\\test\\resources\\LatexEditor\\src\\model\\strategies\\VersionsStrategyFactory.java"
				));
		modelsLeafNodes = new ArrayList<>(Arrays.asList(
				"src\\test\\resources\\LatexEditor\\src\\model\\Document.java",
				"src\\test\\resources\\LatexEditor\\src\\model\\DocumentManager.java",
				"src\\test\\resources\\LatexEditor\\src\\model\\VersionsManager.java"
				));
		commandsLeafNodes = new ArrayList<>(Arrays.asList(
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
		packageNodes = parser.getPackageNodes();
		for (PackageNode p: packageNodes) {
			if (p.getName().equals("controller")) {
				List<String> testingLeafNodes = new ArrayList<String>();
				assertEquals("src\\test\\resources\\LatexEditor\\src\\controller", p.getNodesPath(), "message");
				assertEquals("src\\test\\resources\\LatexEditor\\src", p.getParentNode().getNodesPath(), "message");
				assertEquals(true, p.isValid(), "message");
				subNodes = p.getSubNodes();
				assertEquals("src\\test\\resources\\LatexEditor\\src\\controller\\commands", subNodes.get(0).getNodesPath(), "message");
				leafNodes = p.getLeafNodes();
				for (LeafNode l: leafNodes) {
					testingLeafNodes.add(l.getPath());
					assertEquals(l.getParentNode().getNodesPath(), "src\\test\\resources\\LatexEditor\\src\\controller");
				}
				Collections.sort(testingLeafNodes);
				Collections.sort(commandsLeafNodes);
				assertTrue(testingLeafNodes.size() == controllersLeafNodes.size() 
						&& controllersLeafNodes.containsAll(testingLeafNodes) 
						&& testingLeafNodes.containsAll(controllersLeafNodes));
			}else if (p.getName().equals("commands")) {
				List<String> testingLeafNodes = new ArrayList<String>();
				assertEquals("src\\test\\resources\\LatexEditor\\src\\controller\\commands", p.getNodesPath(), "message");
				assertEquals("src\\test\\resources\\LatexEditor\\src\\controller", p.getParentNode().getNodesPath(), "message");
				assertEquals(true, p.isValid(), "message");
				subNodes = p.getSubNodes();
				assertEquals(0, subNodes.size(), "message");
				leafNodes = p.getLeafNodes();
				for (LeafNode l: leafNodes) {
					testingLeafNodes.add(l.getPath());
					assertEquals(l.getParentNode().getNodesPath(), "src\\test\\resources\\LatexEditor\\src\\controller\\commands");
				}
				Collections.sort(testingLeafNodes);
				Collections.sort(commandsLeafNodes);
				assertTrue(testingLeafNodes.size() == commandsLeafNodes.size() 
						&& commandsLeafNodes.containsAll(testingLeafNodes) 
						&& testingLeafNodes.containsAll(commandsLeafNodes));
			}else if (p.getName().equals("model")) {
				List<String> testingLeafNodes = new ArrayList<String>();
				assertEquals("src\\test\\resources\\LatexEditor\\src\\model", p.getNodesPath(), "message");
				assertEquals("src\\test\\resources\\LatexEditor\\src", p.getParentNode().getNodesPath(), "message");
				assertEquals(true, p.isValid(), "message");
				subNodes = p.getSubNodes();
				assertEquals("src\\test\\resources\\LatexEditor\\src\\model\\strategies", subNodes.get(0).getNodesPath(), "message");
				leafNodes = p.getLeafNodes();
				for (LeafNode l: leafNodes) {
					assertEquals(l.getParentNode().getNodesPath(), "src\\test\\resources\\LatexEditor\\src\\model");
					testingLeafNodes.add(l.getPath());
				}
				Collections.sort(testingLeafNodes);
				Collections.sort(modelsLeafNodes);
				assertTrue(testingLeafNodes.size() == modelsLeafNodes.size() 
						&& modelsLeafNodes.containsAll(testingLeafNodes) 
						&& testingLeafNodes.containsAll(modelsLeafNodes));
			}else if (p.getName().equals("strategies")) {
				List<String> testingLeafNodes = new ArrayList<String>();
				assertEquals("src\\test\\resources\\LatexEditor\\src\\model\\strategies", p.getNodesPath(), "message");
				assertEquals("src\\test\\resources\\LatexEditor\\src\\model", p.getParentNode().getNodesPath(), "message");
				assertEquals(true, p.isValid(), "message");
				subNodes = p.getSubNodes();
				assertEquals(0, subNodes.size(), "message");
				leafNodes = p.getLeafNodes();
				for (LeafNode l: leafNodes) {
					assertEquals(l.getParentNode().getNodesPath(), "src\\test\\resources\\LatexEditor\\src\\model\\strategies");
					testingLeafNodes.add(l.getPath());
				}
				Collections.sort(testingLeafNodes);
				Collections.sort(strategiesLeafNodes);
				assertTrue(testingLeafNodes.size() == strategiesLeafNodes.size() 
						&& strategiesLeafNodes.containsAll(testingLeafNodes) 
						&& testingLeafNodes.containsAll(strategiesLeafNodes));
			}else if (p.getName().equals("view")) {
				List<String> testingLeafNodes = new ArrayList<String>();
				assertEquals("src\\test\\resources\\LatexEditor\\src\\view", p.getNodesPath(), "message");
				assertEquals("src\\test\\resources\\LatexEditor\\src", p.getParentNode().getNodesPath(), "message");
				assertEquals(true, p.isValid(), "message");
				subNodes = p.getSubNodes();
				assertEquals(0, subNodes.size(), "message");
				leafNodes = p.getLeafNodes();
				for (LeafNode l: leafNodes) {
					assertEquals(l.getParentNode().getNodesPath(), "src\\test\\resources\\LatexEditor\\src\\view");
					testingLeafNodes.add(l.getPath());
				}
				Collections.sort(testingLeafNodes);
				Collections.sort(viewsLeafNodes);
				assertTrue(testingLeafNodes.size() == viewsLeafNodes.size() 
						&& viewsLeafNodes.containsAll(testingLeafNodes) 
						&& testingLeafNodes.containsAll(viewsLeafNodes));
			}else if (p.getName().equals("src")) {
				List<String> testingSubPackages = new ArrayList<String>();
				assertEquals("src\\test\\resources\\LatexEditor\\src", p.getNodesPath(), "message");
				assertEquals("", p.getParentNode().getNodesPath(), "message");
				assertEquals(false, p.isValid(), "message");
				subNodes = p.getSubNodes();
				for (PackageNode subP: subNodes) {
					testingSubPackages.add(subP.getNodesPath());
				}
				Collections.sort(testingSubPackages);
				Collections.sort(sourcesSubPackages);
				assertTrue(testingSubPackages.size() == sourcesSubPackages.size() 
						&& sourcesSubPackages.containsAll(testingSubPackages) 
						&& testingSubPackages.containsAll(sourcesSubPackages));
				leafNodes = p.getLeafNodes();
				assertEquals(0, leafNodes.size(), "message");
			}
		}
	}
}
