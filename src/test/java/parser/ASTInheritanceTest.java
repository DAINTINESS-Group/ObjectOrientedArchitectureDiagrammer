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
		assertEquals("ImplementingClass", ((LeafNode)implementingClassLeaf.getLeafNodeRelationships().get(0).getStartingNode()).getName());
		assertEquals("TestingInterface2", ((LeafNode)implementingClassLeaf.getLeafNodeRelationships().get(0).getEndingNode()).getName());
		assertEquals(RelationshipType.IMPLEMENTATION, implementingClassLeaf.getLeafNodeRelationships().get(0).getRelationshipType());
		
		assertEquals("ImplementingClass", ((LeafNode)implementingClassLeaf.getLeafNodeRelationships().get(1).getStartingNode()).getName());
		assertEquals("ExtensionClass", ((LeafNode)implementingClassLeaf.getLeafNodeRelationships().get(1).getEndingNode()).getName());
		assertEquals(RelationshipType.EXTENSION, implementingClassLeaf.getLeafNodeRelationships().get(1).getRelationshipType());
		
		assertEquals("ImplementingClass", ((LeafNode)implementingClassLeaf.getLeafNodeRelationships().get(2).getStartingNode()).getName());
		assertEquals("TestingInterface", ((LeafNode)implementingClassLeaf.getLeafNodeRelationships().get(2).getEndingNode()).getName());
		assertEquals(RelationshipType.IMPLEMENTATION, implementingClassLeaf.getLeafNodeRelationships().get(2).getRelationshipType());
	}
		
}

