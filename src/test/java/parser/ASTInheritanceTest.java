package parser;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

import model.RelationshipType;
import org.junit.jupiter.api.Test;
import model.PackageNode;
import model.LeafNode;

class ASTInheritanceTest {
	@Test
	void test() {
		PackageParser parser = new Parser();
		parser.parseSourcePackage("src\\test\\resources\\InheritanceTesting\\src");
		Map<String, PackageNode> packages = parser.getPackageNodes();
		PackageNode sourcePackage = packages.get("src");
		LeafNode implementingClassLeaf = sourcePackage.getLeafNodes().get("ImplementingClass");
		assertEquals("ImplementingClass", implementingClassLeaf.getLeafNodeRelationships().get(0).getStartingLeafNode().getName());
		assertEquals("TestingInterface2", implementingClassLeaf.getLeafNodeRelationships().get(0).getEndingLeafNode().getName());
		assertEquals(RelationshipType.IMPLEMENTATION, implementingClassLeaf.getLeafNodeRelationships().get(0).getRelationshipType());
		
		assertEquals("ImplementingClass", implementingClassLeaf.getLeafNodeRelationships().get(1).getStartingLeafNode().getName());
		assertEquals("ExtensionClass", implementingClassLeaf.getLeafNodeRelationships().get(1).getEndingLeafNode().getName());
		assertEquals(RelationshipType.EXTENSION, implementingClassLeaf.getLeafNodeRelationships().get(1).getRelationshipType());
		
		assertEquals("ImplementingClass", implementingClassLeaf.getLeafNodeRelationships().get(2).getStartingLeafNode().getName());
		assertEquals("TestingInterface", implementingClassLeaf.getLeafNodeRelationships().get(2).getEndingLeafNode().getName());
		assertEquals(RelationshipType.IMPLEMENTATION, implementingClassLeaf.getLeafNodeRelationships().get(2).getRelationshipType());
	}
		
}

