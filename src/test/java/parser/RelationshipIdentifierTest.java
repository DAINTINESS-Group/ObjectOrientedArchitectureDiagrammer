package parser;

import model.tree.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RelationshipIdentifierTest {

    Path currentDirectory = Path.of(".");

    @Test
    void leafNodeRelationshipsTest() throws IOException {
        Parser parser = new ProjectParser();
        parser.parseSourcePackage(Paths.get(currentDirectory.toRealPath() + "\\src\\test\\resources\\LatexEditor\\src"));
        Map<Path, PackageNode> packages = parser.getPackageNodes();
        PackageNode commandPackage = packages.get(Paths.get(currentDirectory.toRealPath().normalize().toString(),
                "\\src\\test\\resources\\LatexEditor\\src\\controller\\commands"));

        LeafNode addLatexCommand = commandPackage.getLeafNodes().get("AddLatexCommand");
        List<Relationship> nodeRelationships = addLatexCommand.getNodeRelationships();

        boolean foundObligatoryRelationship = false;
        int relationshipCounter = 0;
        for (Relationship relationship : nodeRelationships) {
            if ((relationship.getStartingNode().getNodeName().equals("AddLatexCommand")) && (relationship.getEndingNode().getNodeName().equals("VersionsManager"))) {
                if (relationship.getRelationshipType().equals(RelationshipType.DEPENDENCY)) {
                    foundObligatoryRelationship = true;
                }else {
                    foundObligatoryRelationship = relationship.getRelationshipType().equals(RelationshipType.ASSOCIATION);
                }
            }else if ((relationship.getStartingNode().getNodeName().equals("AddLatexCommand")) && (relationship.getEndingNode().getNodeName().equals("Command"))) {
                assertEquals(RelationshipType.IMPLEMENTATION, relationship.getRelationshipType());
                foundObligatoryRelationship = true;
            }else {
                foundObligatoryRelationship = false;
            }
            relationshipCounter++;
        }
        assertEquals(3, relationshipCounter);
        assertTrue(foundObligatoryRelationship);
        assertEquals(NodeType.CLASS, addLatexCommand.getType());

        LeafNode commandFactory = commandPackage.getLeafNodes().get("CommandFactory");
        nodeRelationships = commandFactory.getNodeRelationships();

        boolean foundObligatoryRelationships_CommandFactoryToVersionsManager = false;
        boolean foundObligatoryRelationships_CommandFactoryToCommand = false;
        relationshipCounter = 0;
        for(Relationship relationship: nodeRelationships) {
            if((relationship.getStartingNode().getNodeName().equals("CommandFactory")) && (relationship.getEndingNode().getNodeName().equals("VersionsManager"))) {
                if (relationship.getRelationshipType().equals(RelationshipType.DEPENDENCY) || (relationship.getRelationshipType().equals(RelationshipType.ASSOCIATION))) {
                    foundObligatoryRelationships_CommandFactoryToVersionsManager = true;
                }
            }else if ((relationship.getStartingNode().getNodeName().equals("CommandFactory")) && (relationship.getEndingNode().getNodeName().equals("Command"))) {
                if(relationship.getRelationshipType().equals(RelationshipType.DEPENDENCY))
                    foundObligatoryRelationships_CommandFactoryToCommand = true;
            }
            relationshipCounter++;
        }
        assertTrue(foundObligatoryRelationships_CommandFactoryToVersionsManager);
        assertTrue(foundObligatoryRelationships_CommandFactoryToCommand);
        assertEquals(4, relationshipCounter);
        assertEquals(NodeType.CLASS, commandFactory.getType());

        parser = new ProjectParser();
        parser.parseSourcePackage(Paths.get(currentDirectory.toRealPath() + "\\src\\test\\resources\\ParserTesting"));
        packages = parser.getPackageNodes();
        PackageNode sourcePackage = packages.get(Paths.get(currentDirectory.toRealPath().normalize().toString(),
                "\\src\\test\\resources\\ParserTesting"));

        LeafNode objectCreation = sourcePackage.getLeafNodes().get("ObjectCreationTest");
        nodeRelationships = objectCreation.getNodeRelationships();

        foundObligatoryRelationship = false;
        relationshipCounter = 0;
        for (Relationship relationship : nodeRelationships) {
            if ((relationship.getStartingNode().getNodeName().equals("ObjectCreationTest")) && (relationship.getEndingNode().getNodeName().equals("ExtensionClass"))) {
                if (relationship.getRelationshipType().equals(RelationshipType.DEPENDENCY)) {
                    foundObligatoryRelationship = true;
                }else {
                    foundObligatoryRelationship = relationship.getRelationshipType().equals(RelationshipType.ASSOCIATION);
                }
            }else if ((relationship.getStartingNode().getNodeName().equals("ObjectCreationTest")) && (relationship.getEndingNode().getNodeName().equals("TestingInterface"))) {
                assertEquals(RelationshipType.DEPENDENCY, relationship.getRelationshipType());
                foundObligatoryRelationship = true;
            }else if ((relationship.getStartingNode().getNodeName().equals("ObjectCreationTest")) && (relationship.getEndingNode().getNodeName().equals("ImplementingClass"))) {
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
        Parser parser = new ProjectParser();
        parser.parseSourcePackage(Paths.get(currentDirectory.toRealPath() + "\\src\\test\\resources\\ParserTesting\\InheritanceTesting"));
        Map<Path, PackageNode> packages = parser.getPackageNodes();
        PackageNode sourcePackage = packages.get(Paths.get(currentDirectory.toRealPath().normalize().toString(),
                "\\src\\test\\resources\\ParserTesting\\InheritanceTesting"));

        LeafNode implementingClassLeaf = sourcePackage.getLeafNodes().get("ImplementingClass");
        List<Relationship> nodeRelationships = implementingClassLeaf.getNodeRelationships();

        boolean foundObligatoryRelationship = false;
        int relationshipCounter = 0;
        for(Relationship relationship: nodeRelationships) {
            if((relationship.getStartingNode().getNodeName().equals("ImplementingClass")) && (relationship.getEndingNode().getNodeName().equals("TestingInterface2"))) {
                assertEquals(RelationshipType.IMPLEMENTATION, relationship.getRelationshipType());
                foundObligatoryRelationship = true;
            }else if ((relationship.getStartingNode().getNodeName().equals("ImplementingClass")) && (relationship.getEndingNode().getNodeName().equals("ExtensionClass"))) {
                assertEquals(RelationshipType.EXTENSION, relationship.getRelationshipType());
                foundObligatoryRelationship = true;
            }else if ((relationship.getStartingNode().getNodeName().equals("ImplementingClass")) && (relationship.getEndingNode().getNodeName().equals("TestingInterface"))) {
                assertEquals(RelationshipType.IMPLEMENTATION, relationship.getRelationshipType());
                foundObligatoryRelationship = true;
            }else {
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
        Parser parser = new ProjectParser();
        parser.parseSourcePackage(Paths.get(currentDirectory.toRealPath() + "\\src\\test\\resources\\LatexEditor\\src"));
        Map<Path, PackageNode> packages = parser.getPackageNodes();

        PackageNode commands = packages.get(Paths.get(currentDirectory.toRealPath().normalize().toString(), "\\src\\test\\resources\\LatexEditor\\src\\controller\\commands"));
        List<Relationship> packageRelationships = commands.getNodeRelationships();

        boolean foundObligatoryRelationship = false;
        int relationshipCounter = 0;
        for(Relationship relationship: packageRelationships) {
            if((relationship.getStartingNode().getNodeName().equals("src.controller.commands")) && (relationship.getEndingNode().getNodeName().equals("src.model"))) {
                assertEquals(RelationshipType.DEPENDENCY, relationship.getRelationshipType());
                foundObligatoryRelationship = true;
            }else {
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
        for(Relationship relationship: packageRelationships) {
            if((relationship.getStartingNode().getNodeName().equals("src.controller")) && (relationship.getEndingNode().getNodeName().equals("src.model"))) {
                assertEquals(RelationshipType.DEPENDENCY, relationship.getRelationshipType());
                foundObligatoryRelationship = true;
            }else if((relationship.getStartingNode().getNodeName().equals("src.controller")) && (relationship.getEndingNode().getNodeName().equals("src.controller.commands"))) {
                assertEquals(RelationshipType.DEPENDENCY, relationship.getRelationshipType());
                foundObligatoryRelationship = true;
            }else {
                foundObligatoryRelationship = false;
            }
            relationshipCounter++;
        }

        assertTrue(foundObligatoryRelationship);
        assertEquals(2, relationshipCounter);
        assertEquals(NodeType.PACKAGE, commands.getType());
    }
}
