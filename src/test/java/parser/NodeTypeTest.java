package parser;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import model.LeafNodeType;
import org.junit.jupiter.api.Test;
import model.LeafNode;
import model.PackageNode;

class NodeTypeTest {
	@Test
	void test() {
		PackageParser parser = new Parser();
		parser.parseSourcePackage("src\\test\\resources\\InheritanceTesting\\src");
		Map<String, PackageNode> packages = parser.getPackageNodes();
		PackageNode sourcePackage = packages.get("src");
		List<LeafNode> classLeafs = new ArrayList<>();
		List<LeafNode> interfaceLeafs = new ArrayList<>();
		classLeafs.add(sourcePackage.getLeafNodes().get("ImplementingClass"));
		classLeafs.add(sourcePackage.getLeafNodes().get("ExtensionClass"));
		interfaceLeafs.add(sourcePackage.getLeafNodes().get("TestingInterface"));
		interfaceLeafs.add(sourcePackage.getLeafNodes().get("TestingInterface2"));
		for (LeafNode l: classLeafs) {
			assertEquals(LeafNodeType.CLASS, l.getType());
		}
		for (LeafNode l: interfaceLeafs) {
			assertEquals(LeafNodeType.INTERFACE, l.getType());
		}
	}

}
