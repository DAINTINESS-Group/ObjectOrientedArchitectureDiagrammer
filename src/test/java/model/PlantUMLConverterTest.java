package model;

import org.junit.jupiter.api.Test;

import model.diagram.GraphEdgeCollection;
import model.diagram.GraphNodeCollection;
import model.diagram.graphml.GraphMLLeafEdge;
import model.diagram.graphml.GraphMLLeafNode;
import model.diagram.graphml.GraphMLPackageEdge;
import model.diagram.graphml.GraphMLPackageNode;
import parser.Parser;
import parser.ProjectParser;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class PlantUMLConverterTest {

	Path currentDirectory = Path.of(".");
	@Test
	void checkClassDiagramRelationshipsText() throws IOException{
		List<String> actualRelationships; 
		List<String> expectedRelationships = new ArrayList<>();
		expectedRelationships.add("StableVersionsStrategy ..|> VersionsStrategy");
		expectedRelationships.add("VersionsStrategyFactory ..> VersionsStrategy");
		expectedRelationships.add("VersionsStrategyFactory --o VersionsStrategy");
		expectedRelationships.add("VersionsManager --> VersionsStrategy");
		expectedRelationships.add("VersionsManager ..> VersionsStrategy");
		expectedRelationships.add("VolatileVersionsStrategy ..|> VersionsStrategy");
		expectedRelationships.add("VolatileVersionsStrategy --o Document");
		expectedRelationships.add("DocumentManager ..> Document");
		expectedRelationships.add("VolatileVersionsStrategy ..> Document");
		expectedRelationships.add("VersionsManager ..> Document");
		expectedRelationships.add("DocumentManager --o Document");
		expectedRelationships.add("StableVersionsStrategy ..> Document");
		expectedRelationships.add("VersionsStrategy ..> Document");
		GraphNodeCollection graphNodeCollection = new GraphMLLeafNode();
		GraphEdgeCollection graphEdgeCollection = new GraphMLLeafEdge();
        Parser parser = new ProjectParser();
        parser.parseSourcePackage(Paths.get(currentDirectory.toRealPath() + "\\src\\test\\resources\\LatexEditor\\src"));
        graphNodeCollection.populateGraphNodes(new ArrayList<>(parser.getPackageNodes().get(Paths.get(currentDirectory.toRealPath().normalize().toString(),
                "\\src\\test\\resources\\LatexEditor\\src\\model")).getLeafNodes().values()));
        graphNodeCollection.populateGraphNodes(new ArrayList<>(parser.getPackageNodes().get(Paths.get(currentDirectory.toRealPath().normalize().toString(),
                "\\src\\test\\resources\\LatexEditor\\src\\model\\strategies")).getLeafNodes().values()));
        graphEdgeCollection.setGraphNodes(graphNodeCollection.getGraphNodes());
        graphEdgeCollection.populateGraphEdges(new ArrayList<>(parser.getPackageNodes().get(Paths.get(currentDirectory.toRealPath().normalize().toString(),
                "\\src\\test\\resources\\LatexEditor\\src\\model")).getLeafNodes().values()));
        graphEdgeCollection.populateGraphEdges(new ArrayList<>(parser.getPackageNodes().get(Paths.get(currentDirectory.toRealPath().normalize().toString(),
                "\\src\\test\\resources\\LatexEditor\\src\\model\\strategies")).getLeafNodes().values()));
        actualRelationships = graphEdgeCollection.convertEdgesToPlantUML();
        Collections.sort(actualRelationships);
        Collections.sort(expectedRelationships);
        assertEquals(actualRelationships, expectedRelationships);
	}
	
	@Test
	void checkClassDiagramDeclarationsText() throws IOException{
		Map<String, String> actualDeclarations; 
		Map<String, String> expectedDeclarations = new HashMap<>();
		expectedDeclarations.put("Document", "class Document {\n"
				+ "-date: String\n"
				+ "-copyright: String\n"
				+ "-versionID: String\n"
				+ "-contents: String\n"
				+ "-author: String\n"
				+ "+changeVersion(): void\n"
				+ "+getVersionID(): String\n"
				+ "+save(String filename): void\n"
				+ "+clone(): Document\n"
				+ "+getContents(): String\n"
				+ "+Document(): Constructor\n"
				+ "+setContents(String contents): void\n"
				+ "}\n");
		expectedDeclarations.put("StableVersionsStrategy", "class StableVersionsStrategy {\n"
				+ "-versionID: String\n"
				+ "+getVersion(): Document\n"
				+ "+getEntireHistory(): List[Document]\n"
				+ "+removeVersion(): void\n"
				+ "+setEntireHistory(List<Document> documents): void\n"
				+ "+putVersion(Document document): void\n"
				+ "}\n");
		expectedDeclarations.put("VersionsStrategy", "interface VersionsStrategy {\n"
				+ "+getVersion(): Document\n"
				+ "+getEntireHistory(): List[Document]\n"
				+ "+removeVersion(): void\n"
				+ "+setEntireHistory(List<Document> documents): void\n"
				+ "+putVersion(Document document): void\n"
				+ "}\n");
		expectedDeclarations.put("VersionsStrategyFactory", "class VersionsStrategyFactory {\n"
				+ "-strategies: HashMap[String,VersionsStrategy]\n"
				+ "+createStrategy(String type): VersionsStrategy\n"
				+ "+VersionsStrategyFactory(): Constructor\n"
				+ "}\n");
		expectedDeclarations.put("DocumentManager", "class DocumentManager {\n"
				+ "-templates: HashMap[String,Document]\n"
				+ "+DocumentManager(): Constructor\n"
				+ "+getContents(String type): String\n"
				+ "+createDocument(String type): Document\n"
				+ "}\n");
		expectedDeclarations.put("VolatileVersionsStrategy", "class VolatileVersionsStrategy {\n"
				+ "-history: ArrayList[Document]\n"
				+ "+getVersion(): Document\n"
				+ "+getEntireHistory(): List[Document]\n"
				+ "+removeVersion(): void\n"
				+ "+setEntireHistory(List<Document> documents): void\n"
				+ "+VolatileVersionsStrategy(): Constructor\n"
				+ "+putVersion(Document document): void\n"
				+ "}\n");
		expectedDeclarations.put("VersionsManager", "class VersionsManager {\n"
				+ "-latexEditorView: LatexEditorView\n"
				+ "-strategy: VersionsStrategy\n"
				+ "-enabled: boolean\n"
				+ "+VersionsManager(VersionsStrategy versionsStrategy, LatexEditorView latexEditorView): Constructor\n"
				+ "+rollback(): void\n"
				+ "+getStrategy(): VersionsStrategy\n"
				+ "+loadFromFile(): void\n"
				+ "+changeStrategy(): void\n"
				+ "+rollbackToPreviousVersion(): void\n"
				+ "+saveContents(): void\n"
				+ "+setPreviousVersion(): Document\n"
				+ "+saveToFile(): void\n"
				+ "+getType(): String\n"
				+ "+enable(): void\n"
				+ "+disable(): void\n"
				+ "+isEnabled(): boolean\n"
				+ "+setStrategy(VersionsStrategy strategy): void\n"
				+ "+putVersion(Document document): void\n"
				+ "+enableStrategy(): void\n"
				+ "+setCurrentVersion(Document document): void\n"
				+ "}\n");
		
		GraphNodeCollection graphNodeCollection = new GraphMLLeafNode();
        Parser parser = new ProjectParser();
        parser.parseSourcePackage(Paths.get(currentDirectory.toRealPath() + "\\src\\test\\resources\\LatexEditor\\src"));
        graphNodeCollection.populateGraphNodes(new ArrayList<>(parser.getPackageNodes().get(Paths.get(currentDirectory.toRealPath().normalize().toString(),
                "\\src\\test\\resources\\LatexEditor\\src\\model")).getLeafNodes().values()));
        graphNodeCollection.populateGraphNodes(new ArrayList<>(parser.getPackageNodes().get(Paths.get(currentDirectory.toRealPath().normalize().toString(),
                "\\src\\test\\resources\\LatexEditor\\src\\model\\strategies")).getLeafNodes().values()));
        actualDeclarations = graphNodeCollection.convertNodesToPlantUML();
        assertEquals(actualDeclarations , expectedDeclarations);
	}
	
	@Test
	void checkPackageDiagramRelationshipsText() throws IOException{
		List<String> actualRelationships; 
		List<String> expectedRelationships = new ArrayList<>();
		expectedRelationships.add("src.model ..> src.view");
		expectedRelationships.add("src.model ..> src.model.strategies");
		expectedRelationships.add("src.model.strategies ..> src.model");
		expectedRelationships.add("src.controller ..> src.model");
		expectedRelationships.add("src.controller ..> src.controller.commands");
		expectedRelationships.add("src.controller.commands ..> src.model");
		expectedRelationships.add("src.view ..> src.model");
		expectedRelationships.add("src.view ..> src.controller");
		GraphNodeCollection graphNodeCollection = new GraphMLPackageNode();
		GraphEdgeCollection graphEdgeCollection = new GraphMLPackageEdge();
        Parser parser = new ProjectParser();
        parser.parseSourcePackage(Paths.get(currentDirectory.toRealPath() + "\\src\\test\\resources\\LatexEditor\\src"));
        graphNodeCollection.populateGraphNodes(new ArrayList<>(parser.getPackageNodes().values()));
        graphEdgeCollection.setGraphNodes(graphNodeCollection.getGraphNodes());
        graphEdgeCollection.populateGraphEdges(new ArrayList<>(parser.getPackageNodes().values()));
        actualRelationships = graphEdgeCollection.convertEdgesToPlantUML();
        Collections.sort(actualRelationships);
        Collections.sort(expectedRelationships);
        assertEquals(actualRelationships, expectedRelationships);   
	}
	
	@Test
	void checkPackageDiagramDeclarationsText() throws IOException{
		Map<String, String> actualDeclarations; 
		Map<String, String> expectedDeclarations = new HashMap<>();
		expectedDeclarations.put("src", "package src {\n"
				+ "}\n");
		expectedDeclarations.put("src.model", "package src.model {\n"
				+ "}\n");
		expectedDeclarations.put("src.model.strategies", "package src.model.strategies {\n"
				+ "}\n");
		expectedDeclarations.put("src.controller", "package src.controller {\n"
				+ "}\n");
		expectedDeclarations.put("src.controller.commands", "package src.controller.commands {\n"
				+ "}\n");
		expectedDeclarations.put("src.view", "package src.view {\n"
				+ "}\n");
		GraphNodeCollection graphNodeCollection = new GraphMLPackageNode();
        Parser parser = new ProjectParser();
        parser.parseSourcePackage(Paths.get(currentDirectory.toRealPath() + "\\src\\test\\resources\\LatexEditor\\src"));
        graphNodeCollection.populateGraphNodes(new ArrayList<>(parser.getPackageNodes().values()));
        actualDeclarations = graphNodeCollection.convertNodesToPlantUML();
        assertEquals(actualDeclarations , expectedDeclarations);  
	}
}
