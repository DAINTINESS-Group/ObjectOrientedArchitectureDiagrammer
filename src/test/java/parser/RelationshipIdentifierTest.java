package parser;

import org.junit.jupiter.api.Test;
import parser.factory.Parser;
import parser.factory.ParserType;
import parser.factory.ProjectParserFactory;
import parser.tree.LeafNode;
import parser.tree.NodeType;
import parser.tree.PackageNode;
import parser.tree.Relationship;
import parser.tree.RelationshipType;
import utils.PathConstructor;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RelationshipIdentifierTest
{

	ParserType parserType = ParserType.JAVAPARSER;

	@Test
	void getFieldAndMethodTypesTest() {
		Parser parser = ProjectParserFactory.createProjectParser(parserType);

		Map<Path, PackageNode> packages = parser.parseSourcePackage(Paths.get(String.format("%s%s%s",
																							PathConstructor.getCurrentPath(),
																							File.separator,
																							PathConstructor.constructPath("src",
																														  "test",
																														  "resources",
																														  "LatexEditor",
																														  "src"))));
		List<String> methodReturnTypes = new ArrayList<>(Arrays.asList("Constructor", "void"));
		List<String> fieldTypes = new ArrayList<>(List.of("VersionsManager"));
		List<String> methodParameterTypes = new ArrayList<>(List.of("VersionsManager"));

		PackageNode commandPackage = packages.get(Paths.get(PathConstructor.getCurrentPath().normalize().toString(), PathConstructor.constructPath("src", "test", "resources", "LatexEditor", "src", "controller", "commands")));
		LeafNode addLatexCommand = commandPackage.getLeafNodes().get("AddLatexCommand");

		List<String> methodReturnTypesTest;
		List<String> fieldTypesTest;
		List<String> methodParameterTypesTest;
		methodParameterTypesTest = addLatexCommand.getMethodParameterTypes();
		fieldTypesTest = addLatexCommand.fields().stream().map(LeafNode.Field::fieldType).collect(Collectors.toCollection(ArrayList::new));
		methodReturnTypesTest = addLatexCommand.getMethodReturnTypes();
		Collections.sort(methodReturnTypesTest);
		Collections.sort(methodReturnTypes);
		assertTrue(methodReturnTypesTest.size() == methodReturnTypes.size() &&
				   methodReturnTypes.containsAll(methodReturnTypesTest) 			 &&
				   methodReturnTypesTest.containsAll(methodReturnTypes));
		Collections.sort(fieldTypesTest);
		Collections.sort(fieldTypes);
		assertTrue(fieldTypesTest.size() == fieldTypes.size() &&
				   fieldTypes.containsAll(fieldTypesTest) 			   &&
				   fieldTypesTest.containsAll(fieldTypes));
		Collections.sort(methodParameterTypesTest);
		Collections.sort(methodParameterTypes);
		assertTrue(methodParameterTypesTest.size() == methodParameterTypes.size() &&
				   methodParameterTypes.containsAll(methodParameterTypesTest) 			   &&
				   methodParameterTypesTest.containsAll(methodParameterTypes));
	}

	@Test
	void leafNodeRelationshipsTest() {
		Parser parser = ProjectParserFactory.createProjectParser(parserType);
		Map<Path, PackageNode> packages = parser.parseSourcePackage(Paths.get(String.format("%s%s%s",
																							PathConstructor.getCurrentPath(),
																							File.separator,
																							PathConstructor.constructPath("src",
																														  "test",
																														  "resources",
																														  "LatexEditor",
																														  "src"))));
		Map<LeafNode, Set<Relationship<LeafNode>>> leafNodeRelationships = parser.createRelationships(packages);
		PackageNode commandPackage = packages.get(Paths.get(PathConstructor.getCurrentPath().normalize().toString(),
															PathConstructor.constructPath("src",
																						  "test",
																						  "resources",
																						  "LatexEditor",
																						  "src",
																						  "controller",
																						  "commands")));

		LeafNode addLatexCommand = commandPackage.getLeafNodes().get("AddLatexCommand");
		Set<Relationship<LeafNode>> nodeRelationships = leafNodeRelationships.get(addLatexCommand);

		boolean foundObligatoryRelationship = false;
		int relationshipCounter = 0;
		for (Relationship<LeafNode> relationship : nodeRelationships) {
			if ((relationship.startingNode().nodeName().equals("AddLatexCommand")) && (relationship.endingNode().nodeName().equals("VersionsManager"))) {
				if (relationship.relationshipType().equals(RelationshipType.DEPENDENCY)) {
					foundObligatoryRelationship = true;
				} else {
					foundObligatoryRelationship = relationship.relationshipType().equals(RelationshipType.ASSOCIATION);
				}
			} else if ((relationship.startingNode().nodeName().equals("AddLatexCommand")) && (relationship.endingNode().nodeName().equals("Command"))) {
				assertEquals(RelationshipType.IMPLEMENTATION, relationship.relationshipType());
				foundObligatoryRelationship = true;
			} else {
				foundObligatoryRelationship = false;
			}
			relationshipCounter++;
		}
		assertEquals(3, relationshipCounter);
		assertTrue(foundObligatoryRelationship);
		assertEquals(NodeType.CLASS, addLatexCommand.nodeType());

		LeafNode commandFactory = commandPackage.getLeafNodes().get("CommandFactory");
		nodeRelationships = leafNodeRelationships.get(commandFactory);

		boolean foundObligatoryRelationships_CommandFactoryToVersionsManager = false;
		boolean foundObligatoryRelationships_CommandFactoryToCommand = false;
		boolean foundObligatoryRelationships_CommandFactoryToAddLatexCommand = false;
		relationshipCounter = 0;
		for (Relationship<LeafNode> relationship : nodeRelationships) {
			if ((relationship.startingNode().nodeName().equals("CommandFactory")) &&
				(relationship.endingNode().nodeName().equals("VersionsManager"))) {
				if (relationship.relationshipType().equals(RelationshipType.DEPENDENCY)) {
					foundObligatoryRelationships_CommandFactoryToVersionsManager = true;
				} else {
					foundObligatoryRelationships_CommandFactoryToVersionsManager = relationship.relationshipType().equals(RelationshipType.ASSOCIATION);
				}
			} else if ((relationship.startingNode().nodeName().equals("CommandFactory")) &&
					   (relationship.endingNode().nodeName().equals("Command"))) {
				if (relationship.relationshipType().equals(RelationshipType.DEPENDENCY))
					foundObligatoryRelationships_CommandFactoryToCommand = true;
			} else if ((relationship.startingNode().nodeName().equals("CommandFactory")) &&
					   (relationship.endingNode().nodeName().equals("AddLatexCommand"))) {
				if (relationship.relationshipType().equals(RelationshipType.DEPENDENCY))
					foundObligatoryRelationships_CommandFactoryToAddLatexCommand = true;
			}
			relationshipCounter++;
		}
		assertTrue(foundObligatoryRelationships_CommandFactoryToVersionsManager);
		assertTrue(foundObligatoryRelationships_CommandFactoryToCommand);
		assertTrue(foundObligatoryRelationships_CommandFactoryToAddLatexCommand);

		assertEquals(13, relationshipCounter);
		assertEquals(NodeType.CLASS, commandFactory.nodeType());

		parser = ProjectParserFactory.createProjectParser(parserType);

		packages = parser.parseSourcePackage(Paths.get(String.format("%s%s%s",
																	 PathConstructor.getCurrentPath(),
																	 File.separator,
																	 PathConstructor.constructPath("src",
																								   "test",
																								   "resources",
																								   "ParserTesting"))));
		leafNodeRelationships = parser.createRelationships(packages);

		PackageNode sourcePackage = packages.get(Paths.get(PathConstructor.getCurrentPath().normalize().toString(),
														   PathConstructor.constructPath("src",
																						 "test",
																						 "resources",
																						 "ParserTesting")));

		LeafNode objectCreation = sourcePackage.getLeafNodes().get("ObjectCreationSample");
		nodeRelationships = leafNodeRelationships.get(objectCreation);

		foundObligatoryRelationship = false;
		relationshipCounter = 0;
		for (Relationship<LeafNode> relationship : nodeRelationships) {
			if ((relationship.startingNode().nodeName().equals("ObjectCreationSample")) &&
				(relationship.endingNode().nodeName().equals("ExtensionClass"))) {
				if (relationship.relationshipType().equals(RelationshipType.DEPENDENCY)) {
					foundObligatoryRelationship = true;
				} else {
					foundObligatoryRelationship = relationship.relationshipType().equals(RelationshipType.ASSOCIATION);
				}
			} else if ((relationship.startingNode().nodeName().equals("ObjectCreationSample")) &&
					   (relationship.endingNode().nodeName().equals("TestingInterface"))) {
				assertEquals(RelationshipType.DEPENDENCY, relationship.relationshipType());
				foundObligatoryRelationship = true;
			} else if ((relationship.startingNode().nodeName().equals("ObjectCreationSample")) &&
					   (relationship.endingNode().nodeName().equals("ImplementingClass"))) {
				assertEquals(RelationshipType.DEPENDENCY, relationship.relationshipType());
				foundObligatoryRelationship = true;
			} else {
				foundObligatoryRelationship = false;
			}
			relationshipCounter++;
		}
		assertEquals(4, relationshipCounter);
		assertTrue(foundObligatoryRelationship);
	}

	@Test
	void leafNodeInheritanceRelationshipTest() {
		Parser parser = ProjectParserFactory.createProjectParser(parserType);

		Map<Path, PackageNode> packages = parser.parseSourcePackage(Paths.get(String.format("%s%s%s",
																							PathConstructor.getCurrentPath(),
																							File.separator,
																							PathConstructor.constructPath("src",
																														  "test",
																														  "resources",
																														  "ParserTesting"))));
		Map<LeafNode, Set<Relationship<LeafNode>>> leafNodeRelationships = parser.createRelationships(packages);
		PackageNode sourcePackage = packages.get(Paths.get(PathConstructor.getCurrentPath().normalize().toString(),
														   PathConstructor.constructPath("src",
																						 "test",
																						 "resources",
																						 "ParserTesting")));

		LeafNode implementingClassLeaf = sourcePackage.getLeafNodes().get("ImplementingClass");
		Set<Relationship<LeafNode>> nodeRelationships = leafNodeRelationships.get(implementingClassLeaf);

		boolean foundObligatoryRelationship = false;
		int relationshipCounter = 0;
		for (Relationship<LeafNode> relationship : nodeRelationships) {
			if ((relationship.startingNode().nodeName().equals("ImplementingClass")) &&
				(relationship.endingNode().nodeName().equals("TestingInterface2"))) {
				assertEquals(RelationshipType.IMPLEMENTATION, relationship.relationshipType());
				foundObligatoryRelationship = true;
			} else if ((relationship.startingNode().nodeName().equals("ImplementingClass")) &&
					   (relationship.endingNode().nodeName().equals("ExtensionClass"))) {
				assertEquals(RelationshipType.EXTENSION, relationship.relationshipType());
				foundObligatoryRelationship = true;
			} else if ((relationship.startingNode().nodeName().equals("ImplementingClass")) &&
					   (relationship.endingNode().nodeName().equals("TestingInterface"))) {
				assertEquals(RelationshipType.IMPLEMENTATION, relationship.relationshipType());
				foundObligatoryRelationship = true;
			} else {
				foundObligatoryRelationship = false;
			}
			relationshipCounter++;
		}

		assertTrue(foundObligatoryRelationship);
		assertEquals(3, relationshipCounter);
		assertEquals(NodeType.CLASS, implementingClassLeaf.nodeType());
	}

	@Test
	void packageNodeRelationshipsTest() {
		Parser parser = ProjectParserFactory.createProjectParser(parserType);

		Map<Path, PackageNode> packages = parser.parseSourcePackage(Paths.get(String.format("%s%s%s",
																							PathConstructor.getCurrentPath(),
																							File.separator,
																							PathConstructor.constructPath("src",
																														  "test",
																														  "resources",
																														  "LatexEditor",
																														  "src"))));
		Map<LeafNode, 	 Set<Relationship<LeafNode>>>    leafNodeRelationships    = parser.createRelationships(packages);
		Map<PackageNode, Set<Relationship<PackageNode>>> packageNodeRelationships = parser.identifyPackageNodeRelationships(leafNodeRelationships);
		PackageNode commands = packages.get(Paths.get(PathConstructor.getCurrentPath().normalize().toString(),
													  PathConstructor.constructPath("src",
																					"test",
																					"resources",
																					"LatexEditor",
																					"src",
																					"controller",
																					"commands")));
		Set<Relationship<PackageNode>> packageRelationships = packageNodeRelationships.get(commands);
		boolean foundObligatoryRelationship = false;
		int relationshipCounter = 0;
		for (Relationship<PackageNode> relationship : packageRelationships) {
			if ((relationship.startingNode().getNodeName().equals("src.controller.commands")) &&
				(relationship.endingNode().getNodeName().equals("src.model"))) {
				assertEquals(RelationshipType.DEPENDENCY, relationship.relationshipType());
				foundObligatoryRelationship = true;
			} else {
				foundObligatoryRelationship = false;
			}
			relationshipCounter++;
		}

		assertTrue(foundObligatoryRelationship);
		assertEquals(1, relationshipCounter);
		assertEquals(NodeType.PACKAGE, commands.getNodeType());

		PackageNode controller = packages.get(Paths.get(PathConstructor.getCurrentPath().normalize().toString(),
														PathConstructor.constructPath("src",
																					  "test",
																					  "resources",
																					  "LatexEditor",
																					  "src",
																					  "controller")));
		packageRelationships = packageNodeRelationships.get(controller);

		foundObligatoryRelationship = false;
		relationshipCounter = 0;
		for (Relationship<PackageNode> relationship : packageRelationships) {
			if ((relationship.startingNode().getNodeName().equals("src.controller")) &&
				(relationship.endingNode().getNodeName().equals("src.model"))) {
				assertEquals(RelationshipType.DEPENDENCY, relationship.relationshipType());
				foundObligatoryRelationship = true;
			} else if ((relationship.startingNode().getNodeName().equals("src.controller")) &&
					   (relationship.endingNode().getNodeName().equals("src.controller.commands"))) {
				assertEquals(RelationshipType.DEPENDENCY, relationship.relationshipType());
				foundObligatoryRelationship = true;
			} else {
				foundObligatoryRelationship = false;
			}
			relationshipCounter++;
		}

		assertTrue(foundObligatoryRelationship);
		assertEquals(2, relationshipCounter);
		assertEquals(NodeType.PACKAGE, commands.getNodeType());
	}

	@Test
	void leafNodeTypesTest() {
		Parser parser = ProjectParserFactory.createProjectParser(parserType);

		Map<Path, PackageNode> packages = parser.parseSourcePackage(Paths.get(String.format("%s%s%s",
																							PathConstructor.getCurrentPath(),
																							File.separator,
																							PathConstructor.constructPath("src",
																														  "test",
																														  "resources",
																														  "ParserTesting"))));
		Map<LeafNode, Set<Relationship<LeafNode>>> leafNodeRelationships = parser.createRelationships(packages);
		PackageNode sourcePackage = packages.get(Paths.get(PathConstructor.getCurrentPath().normalize().toString(),
														   PathConstructor.constructPath("src",
																						 "test",
																						 "resources",
																						 "ParserTesting")));
		List<LeafNode> classLeaves     = new ArrayList<>();
		List<LeafNode> interfaceLeaves = new ArrayList<>();
		classLeaves.add(sourcePackage.getLeafNodes().get("ImplementingClass"));
		classLeaves.add(sourcePackage.getLeafNodes().get("ExtensionClass"));
		interfaceLeaves.add(sourcePackage.getLeafNodes().get("TestingInterface"));
		interfaceLeaves.add(sourcePackage.getLeafNodes().get("TestingInterface2"));

		for (LeafNode l : classLeaves) {
			assertEquals(NodeType.CLASS, l.nodeType());
		}
		for (LeafNode l : interfaceLeaves) {
			assertEquals(NodeType.INTERFACE, l.nodeType());
		}
	}

	@Test
	void innerClassRelationshipTest() {
		Parser parser = ProjectParserFactory.createProjectParser(parserType);

		Map<Path, PackageNode> packages = parser.parseSourcePackage(Paths.get(String.format("%s%s%s",
																							PathConstructor.getCurrentPath(),
																							File.separator,
																							PathConstructor.constructPath("src",
																														  "test",
																														  "resources",
																														  "ParserTesting"))));
		Map<LeafNode, Set<Relationship<LeafNode>>> leafNodeRelationships = parser.createRelationships(packages);
		PackageNode sourcePackage = packages.get(Paths.get(PathConstructor.getCurrentPath().normalize().toString(),
														   PathConstructor.constructPath("src",
																						 "test",
																						 "resources",
																						 "ParserTesting")));

		LeafNode outerClass = sourcePackage.getLeafNodes().get("InnerClassSample");
		Set<Relationship<LeafNode>> outerClassRelationships = leafNodeRelationships.get(outerClass);
		assertEquals(4, outerClassRelationships.size());

		int obligatoryRelationshipsCounter = 0;

		for (Relationship<LeafNode> relationship: outerClassRelationships) {
			if (relationship.endingNode().equals(sourcePackage.getLeafNodes().get("ObjectCreationSample")) &&
				relationship.relationshipType().equals(RelationshipType.ASSOCIATION)) {
				obligatoryRelationshipsCounter++;
			} else if (relationship.endingNode().equals(sourcePackage.getLeafNodes().get("ExtensionClass")) &&
					   relationship.relationshipType().equals(RelationshipType.EXTENSION)) {
				obligatoryRelationshipsCounter++;
			} else if (relationship.endingNode().equals(sourcePackage.getLeafNodes().get("ObjectCreationSample")) &&
					   relationship.relationshipType().equals(RelationshipType.EXTENSION)) {
				obligatoryRelationshipsCounter++;
			}
			else if (relationship.endingNode().equals(sourcePackage.getLeafNodes().get("TestingInterface")) &&
					 relationship.relationshipType().equals(RelationshipType.IMPLEMENTATION)) {
				obligatoryRelationshipsCounter++;
			}
		}

		assertEquals(4, obligatoryRelationshipsCounter);
	}
}
