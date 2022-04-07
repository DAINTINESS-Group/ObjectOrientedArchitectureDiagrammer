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
	private Parser parser = new Parser("C:\\Users\\user\\Java-workspace\\UMLDiagramTool\\src\\test\\resources\\src");
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
				"C:\\Users\\user\\Java-workspace\\UMLDiagramTool\\src\\test\\resources\\src\\controller",
				"C:\\Users\\user\\Java-workspace\\UMLDiagramTool\\src\\test\\resources\\src\\model",
				"C:\\Users\\user\\Java-workspace\\UMLDiagramTool\\src\\test\\resources\\src\\view"));
		viewsLeafNodes = new ArrayList<>(Arrays.asList(
				"C:\\Users\\user\\Java-workspace\\UMLDiagramTool\\src\\test\\resources\\src\\view\\ChooseTemplate.java",
				"C:\\Users\\user\\Java-workspace\\UMLDiagramTool\\src\\test\\resources\\src\\view\\LatexEditorView.java",
				"C:\\Users\\user\\Java-workspace\\UMLDiagramTool\\src\\test\\resources\\src\\view\\MainWindow.java",
				"C:\\Users\\user\\Java-workspace\\UMLDiagramTool\\src\\test\\resources\\src\\view\\OpeningWindow.java"
				));
		controllersLeafNodes = new ArrayList<>(Arrays.asList(
				"C:\\Users\\user\\Java-workspace\\UMLDiagramTool\\src\\test\\resources\\src\\controller\\LatexEditorController.java"
				));
		strategiesLeafNodes = new ArrayList<>(Arrays.asList(
				"C:\\Users\\user\\Java-workspace\\UMLDiagramTool\\src\\test\\resources\\src\\model\\strategies\\StableVersionsStrategy.java",
				"C:\\Users\\user\\Java-workspace\\UMLDiagramTool\\src\\test\\resources\\src\\model\\strategies\\VersionsStrategy.java",
				"C:\\Users\\user\\Java-workspace\\UMLDiagramTool\\src\\test\\resources\\src\\model\\strategies\\VolatileVersionsStrategy.java",
				"C:\\Users\\user\\Java-workspace\\UMLDiagramTool\\src\\test\\resources\\src\\model\\strategies\\VersionsStrategyFactory.java"
				));
		modelsLeafNodes = new ArrayList<>(Arrays.asList(
				"C:\\Users\\user\\Java-workspace\\UMLDiagramTool\\src\\test\\resources\\src\\model\\Document.java",
				"C:\\Users\\user\\Java-workspace\\UMLDiagramTool\\src\\test\\resources\\src\\model\\DocumentManager.java",
				"C:\\Users\\user\\Java-workspace\\UMLDiagramTool\\src\\test\\resources\\src\\model\\VersionsManager.java"
				));
		commandsLeafNodes = new ArrayList<>(Arrays.asList(
				"C:\\Users\\user\\Java-workspace\\UMLDiagramTool\\src\\test\\resources\\src\\controller\\commands\\AddLatexCommand.java",
				"C:\\Users\\user\\Java-workspace\\UMLDiagramTool\\src\\test\\resources\\src\\controller\\commands\\Command.java",
				"C:\\Users\\user\\Java-workspace\\UMLDiagramTool\\src\\test\\resources\\src\\controller\\commands\\CommandFactory.java",
				"C:\\Users\\user\\Java-workspace\\UMLDiagramTool\\src\\test\\resources\\src\\controller\\commands\\CreateCommand.java",
				"C:\\Users\\user\\Java-workspace\\UMLDiagramTool\\src\\test\\resources\\src\\controller\\commands\\EditCommand.java",
				"C:\\Users\\user\\Java-workspace\\UMLDiagramTool\\src\\test\\resources\\src\\controller\\commands\\LoadCommand.java",
				"C:\\Users\\user\\Java-workspace\\UMLDiagramTool\\src\\test\\resources\\src\\controller\\commands\\SaveCommand.java",
				"C:\\Users\\user\\Java-workspace\\UMLDiagramTool\\src\\test\\resources\\src\\controller\\commands\\ChangeVersionsStrategyCommand.java",
				"C:\\Users\\user\\Java-workspace\\UMLDiagramTool\\src\\test\\resources\\src\\controller\\commands\\DisableVersionsManagementCommand.java",
				"C:\\Users\\user\\Java-workspace\\UMLDiagramTool\\src\\test\\resources\\src\\controller\\commands\\EnableVersionsManagementCommand.java",
				"C:\\Users\\user\\Java-workspace\\UMLDiagramTool\\src\\test\\resources\\src\\controller\\commands\\RollbackToPreviousVersionCommand.java"
				));
		packageNodes = parser.getPackageNodes();
		for (PackageNode p: packageNodes) {
			if (p.getName().equals("controller")) {
				List<String> testingLeafNodes = new ArrayList<String>();
				assertEquals("C:\\Users\\user\\Java-workspace\\UMLDiagramTool\\src\\test\\resources\\src\\controller", p.getNodesPath(), "message");
				assertEquals("C:\\Users\\user\\Java-workspace\\UMLDiagramTool\\src\\test\\resources\\src", p.getParentNode().getNodesPath(), "message");
				assertEquals(true, p.isValid(), "message");
				subNodes = p.getSubNodes();
				assertEquals("C:\\Users\\user\\Java-workspace\\UMLDiagramTool\\src\\test\\resources\\src\\controller\\commands", subNodes.get(0).getNodesPath(), "message");
				leafNodes = p.getLeafNodes();
				for (LeafNode l: leafNodes) {
					testingLeafNodes.add(l.getPath());
					assertEquals(l.getParentNode().getNodesPath(), "C:\\Users\\user\\Java-workspace\\UMLDiagramTool\\src\\test\\resources\\src\\controller");
				}
				Collections.sort(testingLeafNodes);
				Collections.sort(commandsLeafNodes);
				assertTrue(testingLeafNodes.size() == controllersLeafNodes.size() 
						&& controllersLeafNodes.containsAll(testingLeafNodes) 
						&& testingLeafNodes.containsAll(controllersLeafNodes));
			}else if (p.getName().equals("commands")) {
				List<String> testingLeafNodes = new ArrayList<String>();
				assertEquals("C:\\Users\\user\\Java-workspace\\UMLDiagramTool\\src\\test\\resources\\src\\controller\\commands", p.getNodesPath(), "message");
				assertEquals("C:\\Users\\user\\Java-workspace\\UMLDiagramTool\\src\\test\\resources\\src\\controller", p.getParentNode().getNodesPath(), "message");
				assertEquals(true, p.isValid(), "message");
				subNodes = p.getSubNodes();
				assertEquals(0, subNodes.size(), "message");
				leafNodes = p.getLeafNodes();
				for (LeafNode l: leafNodes) {
					testingLeafNodes.add(l.getPath());
					assertEquals(l.getParentNode().getNodesPath(), "C:\\Users\\user\\Java-workspace\\UMLDiagramTool\\src\\test\\resources\\src\\controller\\commands");
				}
				Collections.sort(testingLeafNodes);
				Collections.sort(commandsLeafNodes);
				assertTrue(testingLeafNodes.size() == commandsLeafNodes.size() 
						&& commandsLeafNodes.containsAll(testingLeafNodes) 
						&& testingLeafNodes.containsAll(commandsLeafNodes));
			}else if (p.getName().equals("model")) {
				List<String> testingLeafNodes = new ArrayList<String>();
				assertEquals("C:\\Users\\user\\Java-workspace\\UMLDiagramTool\\src\\test\\resources\\src\\model", p.getNodesPath(), "message");
				assertEquals("C:\\Users\\user\\Java-workspace\\UMLDiagramTool\\src\\test\\resources\\src", p.getParentNode().getNodesPath(), "message");
				assertEquals(true, p.isValid(), "message");
				subNodes = p.getSubNodes();
				assertEquals("C:\\Users\\user\\Java-workspace\\UMLDiagramTool\\src\\test\\resources\\src\\model\\strategies", subNodes.get(0).getNodesPath(), "message");
				leafNodes = p.getLeafNodes();
				for (LeafNode l: leafNodes) {
					assertEquals(l.getParentNode().getNodesPath(), "C:\\Users\\user\\Java-workspace\\UMLDiagramTool\\src\\test\\resources\\src\\model");
					testingLeafNodes.add(l.getPath());
				}
				Collections.sort(testingLeafNodes);
				Collections.sort(modelsLeafNodes);
				assertTrue(testingLeafNodes.size() == modelsLeafNodes.size() 
						&& modelsLeafNodes.containsAll(testingLeafNodes) 
						&& testingLeafNodes.containsAll(modelsLeafNodes));
			}else if (p.getName().equals("strategies")) {
				List<String> testingLeafNodes = new ArrayList<String>();
				assertEquals("C:\\Users\\user\\Java-workspace\\UMLDiagramTool\\src\\test\\resources\\src\\model\\strategies", p.getNodesPath(), "message");
				assertEquals("C:\\Users\\user\\Java-workspace\\UMLDiagramTool\\src\\test\\resources\\src\\model", p.getParentNode().getNodesPath(), "message");
				assertEquals(true, p.isValid(), "message");
				subNodes = p.getSubNodes();
				assertEquals(0, subNodes.size(), "message");
				leafNodes = p.getLeafNodes();
				for (LeafNode l: leafNodes) {
					assertEquals(l.getParentNode().getNodesPath(), "C:\\Users\\user\\Java-workspace\\UMLDiagramTool\\src\\test\\resources\\src\\model\\strategies");
					testingLeafNodes.add(l.getPath());
				}
				Collections.sort(testingLeafNodes);
				Collections.sort(strategiesLeafNodes);
				assertTrue(testingLeafNodes.size() == strategiesLeafNodes.size() 
						&& strategiesLeafNodes.containsAll(testingLeafNodes) 
						&& testingLeafNodes.containsAll(strategiesLeafNodes));
			}else if (p.getName().equals("view")) {
				List<String> testingLeafNodes = new ArrayList<String>();
				assertEquals("C:\\Users\\user\\Java-workspace\\UMLDiagramTool\\src\\test\\resources\\src\\view", p.getNodesPath(), "message");
				assertEquals("C:\\Users\\user\\Java-workspace\\UMLDiagramTool\\src\\test\\resources\\src", p.getParentNode().getNodesPath(), "message");
				assertEquals(true, p.isValid(), "message");
				subNodes = p.getSubNodes();
				assertEquals(0, subNodes.size(), "message");
				leafNodes = p.getLeafNodes();
				for (LeafNode l: leafNodes) {
					assertEquals(l.getParentNode().getNodesPath(), "C:\\Users\\user\\Java-workspace\\UMLDiagramTool\\src\\test\\resources\\src\\view");
					testingLeafNodes.add(l.getPath());
				}
				Collections.sort(testingLeafNodes);
				Collections.sort(viewsLeafNodes);
				assertTrue(testingLeafNodes.size() == viewsLeafNodes.size() 
						&& viewsLeafNodes.containsAll(testingLeafNodes) 
						&& testingLeafNodes.containsAll(viewsLeafNodes));
			}else if (p.getName().equals("src")) {
				List<String> testingSubPackages = new ArrayList<String>();
				assertEquals("C:\\Users\\user\\Java-workspace\\UMLDiagramTool\\src\\test\\resources\\src", p.getNodesPath(), "message");
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
