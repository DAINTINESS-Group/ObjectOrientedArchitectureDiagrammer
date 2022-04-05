package parsertests;

import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
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
	@Test
	void test() {
		packageNodes = parser.getPackageNodes();
		for (PackageNode p: packageNodes) {
			if (p.getName().equals("controller")) {
				assertEquals("C:\\Users\\user\\Java-workspace\\UMLDiagramTool\\src\\test\\resources\\src\\controller", p.getNodesPath(), "message");
				assertEquals("C:\\Users\\user\\Java-workspace\\UMLDiagramTool\\src\\test\\resources\\src", p.getParentNode().getNodesPath(), "message");
				assertEquals(true, p.isValid(), "message");
				subNodes = p.getSubNodes();
				assertEquals("C:\\Users\\user\\Java-workspace\\UMLDiagramTool\\src\\test\\resources\\src\\controller\\commands", subNodes.get(0).getNodesPath(), "message");
				leafNodes = p.getLeafNodes();
				assertEquals("C:\\Users\\user\\Java-workspace\\UMLDiagramTool\\src\\test\\resources\\src\\controller\\LatexEditorController.java", leafNodes.get(0).getPath(), "message");
				assertEquals("LatexEditorController.java", leafNodes.get(0).getName(), "message");
			}else if (p.getName().equals("commands")) {
				assertEquals("C:\\Users\\user\\Java-workspace\\UMLDiagramTool\\src\\test\\resources\\src\\controller\\commands", p.getNodesPath(), "message");
				assertEquals("C:\\Users\\user\\Java-workspace\\UMLDiagramTool\\src\\test\\resources\\src\\controller", p.getParentNode().getNodesPath(), "message");
				assertEquals(true, p.isValid(), "message");
				subNodes = p.getSubNodes();
				assertEquals(0, subNodes.size(), "message");
				leafNodes = p.getLeafNodes();
				for (LeafNode l: leafNodes) {
					if (l.getPath().equals("C:\\Users\\user\\Java-workspace\\UMLDiagramTool\\src\\test\\resources\\src\\controller\\AddLatexCommand.java")) {
						assertEquals("AddLatexCommand.java", leafNodes.get(0).getName(), "message");
					}
				}
			}else if (p.getName().equals("model")) {
				assertEquals("C:\\Users\\user\\Java-workspace\\UMLDiagramTool\\src\\test\\resources\\src\\model", p.getNodesPath(), "message");
				assertEquals("C:\\Users\\user\\Java-workspace\\UMLDiagramTool\\src\\test\\resources\\src", p.getParentNode().getNodesPath(), "message");
				assertEquals(true, p.isValid(), "message");
				subNodes = p.getSubNodes();
				assertEquals("C:\\Users\\user\\Java-workspace\\UMLDiagramTool\\src\\test\\resources\\src\\model\\strategies", subNodes.get(0).getNodesPath(), "message");
				leafNodes = p.getLeafNodes();
				for (LeafNode l: leafNodes) {
					if (l.getPath().equals("C:\\Users\\user\\Java-workspace\\UMLDiagramTool\\src\\test\\resources\\src\\controller\\Document.java")) {
						assertEquals("Document.java", leafNodes.get(0).getName(), "message");
					}else if (l.getPath().equals("C:\\Users\\user\\Java-workspace\\UMLDiagramTool\\src\\test\\resources\\src\\controller\\DocumentManager.java")) {
						assertEquals("DocumentManager.java", leafNodes.get(0).getName(), "message");
					}else if (l.getPath().equals("C:\\Users\\user\\Java-workspace\\UMLDiagramTool\\src\\test\\resources\\src\\controller\\VersionsManager.java")) {
						assertEquals("VersionsManager.java", leafNodes.get(0).getName(), "message");
					}
				}
			}else if (p.getName().equals("strategies")) {
				assertEquals("C:\\Users\\user\\Java-workspace\\UMLDiagramTool\\src\\test\\resources\\src\\model\\strategies", p.getNodesPath(), "message");
				assertEquals("C:\\Users\\user\\Java-workspace\\UMLDiagramTool\\src\\test\\resources\\src\\model", p.getParentNode().getNodesPath(), "message");
				assertEquals(true, p.isValid(), "message");
				subNodes = p.getSubNodes();
				assertEquals(0, subNodes.size(), "message");
				leafNodes = p.getLeafNodes();
				for (LeafNode l: leafNodes) {
					if (l.getPath().equals("C:\\Users\\user\\Java-workspace\\UMLDiagramTool\\src\\test\\resources\\src\\controller\\StableVersionsStrategy.java")) {
						assertEquals("StableVersionsStrategy.java", leafNodes.get(0).getName(), "message");
					}else if (l.getPath().equals("C:\\Users\\user\\Java-workspace\\UMLDiagramTool\\src\\test\\resources\\src\\controller\\VersionsStrategy.java")) {
						assertEquals("VersionsStrategy.java", leafNodes.get(0).getName(), "message");
					}else if (l.getPath().equals("C:\\Users\\user\\Java-workspace\\UMLDiagramTool\\src\\test\\resources\\src\\controller\\VersionsStrategyFactory.java")) {
						assertEquals("VersionsStrategyFactory.java", leafNodes.get(0).getName(), "message");
					}else if (l.getPath().equals("C:\\Users\\user\\Java-workspace\\UMLDiagramTool\\src\\test\\resources\\src\\controller\\VolatileVersionsStrategy.java")) {
						assertEquals("VolatileVersionsStrategy.java", leafNodes.get(0).getName(), "message");
					}
				}
			}else if (p.getName().equals("view")) {
				assertEquals("C:\\Users\\user\\Java-workspace\\UMLDiagramTool\\src\\test\\resources\\src\\view", p.getNodesPath(), "message");
				assertEquals("C:\\Users\\user\\Java-workspace\\UMLDiagramTool\\src\\test\\resources\\src", p.getParentNode().getNodesPath(), "message");
				assertEquals(true, p.isValid(), "message");
				subNodes = p.getSubNodes();
				assertEquals(0, subNodes.size(), "message");
				leafNodes = p.getLeafNodes();
				for (LeafNode l: leafNodes) {
					if (l.getPath().equals("C:\\Users\\user\\Java-workspace\\UMLDiagramTool\\src\\test\\resources\\src\\controller\\ChooseTemplate.java")) {
						assertEquals("ChooseTemplate.java", leafNodes.get(0).getName(), "message");
					}else if (l.getPath().equals("C:\\Users\\user\\Java-workspace\\UMLDiagramTool\\src\\test\\resources\\src\\controller\\LatexEditorView.java")) {
						assertEquals("LatexEditorView.java", leafNodes.get(0).getName(), "message");
					}else if (l.getPath().equals("C:\\Users\\user\\Java-workspace\\UMLDiagramTool\\src\\test\\resources\\src\\controller\\MainWindow.java")) {
						assertEquals("MainWindow.java", leafNodes.get(0).getName(), "message");
					}else if (l.getPath().equals("C:\\Users\\user\\Java-workspace\\UMLDiagramTool\\src\\test\\resources\\src\\controller\\OpeningWindow.java")) {
						assertEquals("OpeningWindow.java", leafNodes.get(0).getName(), "message");
					}
				}
			}
		}
	}

}
