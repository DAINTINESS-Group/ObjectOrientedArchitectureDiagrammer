package parser.javaparser;

import model.tree.edge.Relationship;
import model.tree.edge.RelationshipType;
import model.tree.node.LeafNode;
import model.tree.node.NodeType;
import model.tree.node.PackageNode;
import org.junit.jupiter.api.Test;
import parser.Parser;
import parser.ParserType;
import parser.ProjectParserFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RelationshipIdentifierTest {

    Path currentDirectory = Path.of(".");
    ParserType parserType = ParserType.JAVAPARSER;

    @Test
    void leafNodeRelationshipsTest() throws IOException {
        ProjectParserFactory projectParserFactory = new ProjectParserFactory(parserType);
        Parser parser = projectParserFactory.createProjectParser();

        parser.parseSourcePackage(Paths.get(currentDirectory.toRealPath() + "\\src\\test\\resources\\ParserTesting"));
        Map<Path, PackageNode> packages = parser.getPackageNodes();
        PackageNode sourcePackage = packages.get(Paths.get(currentDirectory.toRealPath().normalize().toString(),
                "\\src\\test\\resources\\ParserTesting"));

        LeafNode objectCreation = sourcePackage.getLeafNodes().get("ObjectCreationTest");
        List<Relationship> nodeRelationships = objectCreation.getNodeRelationships();

        boolean foundObligatoryRelationship = false;
        int relationshipCounter = 0;
        for (Relationship relationship : nodeRelationships) {
            if ((relationship.getStartingNode().getName().equals("ObjectCreationTest")) && (relationship.getEndingNode().getName().equals("ExtensionClass"))) {
                if (relationship.getRelationshipType().equals(RelationshipType.DEPENDENCY)) {
                    foundObligatoryRelationship = true;
                }else {
                    foundObligatoryRelationship = relationship.getRelationshipType().equals(RelationshipType.ASSOCIATION);
                }
            }else if ((relationship.getStartingNode().getName().equals("ObjectCreationTest")) && (relationship.getEndingNode().getName().equals("TestingInterface"))) {
                assertEquals(RelationshipType.DEPENDENCY, relationship.getRelationshipType());
                foundObligatoryRelationship = true;
            }else if ((relationship.getStartingNode().getName().equals("ObjectCreationTest")) && (relationship.getEndingNode().getName().equals("ImplementingClass"))) {
                assertEquals(RelationshipType.DEPENDENCY, relationship.getRelationshipType());
                foundObligatoryRelationship = true;
            }else {
                foundObligatoryRelationship = false;
            }
            relationshipCounter++;
        }
        assertEquals(4, relationshipCounter);
        assertTrue(foundObligatoryRelationship);
    }

    @Test
    void leafNodeInheritanceRelationshipTest() throws IOException {
        ProjectParserFactory projectParserFactory = new ProjectParserFactory(parserType);
        Parser parser = projectParserFactory.createProjectParser();

        parser.parseSourcePackage(Paths.get(currentDirectory.toRealPath() + "\\src\\test\\resources\\ParserTesting"));
        Map<Path, PackageNode> packages = parser.getPackageNodes();
        PackageNode sourcePackage = packages.get(Paths.get(currentDirectory.toRealPath().normalize().toString(),
                "\\src\\test\\resources\\ParserTesting"));

        LeafNode implementingClassLeaf = sourcePackage.getLeafNodes().get("ImplementingClass");
        List<Relationship> nodeRelationships = implementingClassLeaf.getNodeRelationships();

        boolean foundObligatoryRelationship = false;
        int relationshipCounter = 0;
        for (Relationship relationship : nodeRelationships) {
            if ((relationship.getStartingNode().getName().equals("ImplementingClass")) && (relationship.getEndingNode().getName().equals("TestingInterface2"))) {
                assertEquals(RelationshipType.IMPLEMENTATION, relationship.getRelationshipType());
                foundObligatoryRelationship = true;
            } else if ((relationship.getStartingNode().getName().equals("ImplementingClass")) && (relationship.getEndingNode().getName().equals("ExtensionClass"))) {
                assertEquals(RelationshipType.EXTENSION, relationship.getRelationshipType());
                foundObligatoryRelationship = true;
            } else if ((relationship.getStartingNode().getName().equals("ImplementingClass")) && (relationship.getEndingNode().getName().equals("TestingInterface"))) {
                assertEquals(RelationshipType.IMPLEMENTATION, relationship.getRelationshipType());
                foundObligatoryRelationship = true;
            } else {
                foundObligatoryRelationship = false;
            }
            relationshipCounter++;
        }

        assertTrue(foundObligatoryRelationship);
        assertEquals(3, relationshipCounter);
        assertEquals(NodeType.CLASS, implementingClassLeaf.getType());
    }

    @Test
    void packageNodeRelationshipsTest() throws IOException {
        ProjectParserFactory projectParserFactory = new ProjectParserFactory(parserType);
        Parser parser = projectParserFactory.createProjectParser();

        parser.parseSourcePackage(Paths.get(currentDirectory.toRealPath() + "\\src\\test\\resources\\LatexEditor\\src"));
        Map<Path, PackageNode> packages = parser.getPackageNodes();

        PackageNode commands = packages.get(Paths.get(currentDirectory.toRealPath().normalize().toString(), "\\src\\test\\resources\\LatexEditor\\src\\controller\\commands"));
        List<Relationship> packageRelationships = commands.getNodeRelationships();

        boolean foundObligatoryRelationship = false;
        int relationshipCounter = 0;
        for (Relationship relationship : packageRelationships) {
            if ((relationship.getStartingNode().getName().equals("src.controller.commands")) && (relationship.getEndingNode().getName().equals("src.model"))) {
                assertEquals(RelationshipType.DEPENDENCY, relationship.getRelationshipType());
                foundObligatoryRelationship = true;
            } else {
                foundObligatoryRelationship = false;
            }
            relationshipCounter++;
        }

        assertTrue(foundObligatoryRelationship);
        assertEquals(1, relationshipCounter);
        assertEquals(NodeType.PACKAGE, commands.getType());

        PackageNode controller = packages.get(Paths.get(currentDirectory.toRealPath().normalize().toString(), "\\src\\test\\resources\\LatexEditor\\src\\controller"));
        packageRelationships = controller.getNodeRelationships();

        foundObligatoryRelationship = false;
        relationshipCounter = 0;
        for (Relationship relationship : packageRelationships) {
            if ((relationship.getStartingNode().getName().equals("src.controller")) && (relationship.getEndingNode().getName().equals("src.model"))) {
                assertEquals(RelationshipType.DEPENDENCY, relationship.getRelationshipType());
                foundObligatoryRelationship = true;
            } else if ((relationship.getStartingNode().getName().equals("src.controller")) && (relationship.getEndingNode().getName().equals("src.controller.commands"))) {
                assertEquals(RelationshipType.DEPENDENCY, relationship.getRelationshipType());
                foundObligatoryRelationship = true;
            } else {
                foundObligatoryRelationship = false;
            }
            relationshipCounter++;
        }

        assertTrue(foundObligatoryRelationship);
        assertEquals(2, relationshipCounter);
        assertEquals(NodeType.PACKAGE, commands.getType());
    }
}
