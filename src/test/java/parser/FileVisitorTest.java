package parser;

import org.junit.jupiter.api.Test;
import parser.factory.Parser;
import parser.factory.ParserType;
import parser.factory.ProjectParserFactory;
import parser.tree.LeafNode;
import parser.tree.NodeType;
import parser.tree.PackageNode;
import utils.PathTemplate.LatexEditor;
import utils.PathTemplate.ParserTesting;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static parser.tree.NodeType.CLASS;
import static parser.tree.NodeType.INTERFACE;

public class FileVisitorTest
{

    ParserType parserType = ParserType.JAVAPARSER;


    @Test
    void methodReturnTypesTest()
    {
        Parser parser = ProjectParserFactory.createProjectParser(parserType);
        Map<Path, PackageNode> packages = parser.parseSourcePackage(LatexEditor.SRC.path);

        PackageNode commandPackage = packages.get(LatexEditor.COMMANDS.path);

        LeafNode addLatexCommand = commandPackage.getLeafNodes().get("AddLatexCommand");
        List<String> methodReturnTypesExpected = new ArrayList<>(Arrays.asList("Constructor", "void"));
        List<String> methodReturnTypesActual = addLatexCommand.getMethodReturnTypes();

        Collections.sort(methodReturnTypesActual);
        Collections.sort(methodReturnTypesExpected);

        assertEquals(methodReturnTypesExpected.size(), methodReturnTypesActual.size());
        assertTrue(methodReturnTypesExpected.containsAll(methodReturnTypesActual));
        assertTrue(methodReturnTypesActual.containsAll(methodReturnTypesExpected));
    }


    @Test
    void methodParameterTypesTest()
    {
        Parser parser = ProjectParserFactory.createProjectParser(parserType);

        Map<Path, PackageNode> packages = parser.parseSourcePackage(LatexEditor.SRC.path);
        PackageNode commandPackage = packages.get(LatexEditor.COMMANDS.path);

        LeafNode     addLatexCommand          = commandPackage.getLeafNodes().get("AddLatexCommand");
        List<String> methodParameterTypes     = new ArrayList<>(List.of("VersionsManager"));
        List<String> methodParameterTypesTest = addLatexCommand.getMethodParameterTypes();

        Collections.sort(methodParameterTypesTest);
        Collections.sort(methodParameterTypes);

        assertEquals(methodParameterTypes.size(), methodParameterTypesTest.size());
        assertTrue(methodParameterTypes.containsAll(methodParameterTypesTest));
        assertTrue(methodParameterTypesTest.containsAll(methodParameterTypes));
    }


    @Test
    void fieldTypesTest()
    {
        Parser parser = ProjectParserFactory.createProjectParser(parserType);

        Map<Path, PackageNode> packages = parser.parseSourcePackage(LatexEditor.SRC.path);
        PackageNode commandPackage = packages.get(LatexEditor.COMMANDS.path);
        LeafNode     addLatexCommand = commandPackage.getLeafNodes().get("AddLatexCommand");
        List<String> fieldTypes      = new ArrayList<>(List.of("VersionsManager"));
        List<String> fieldTypesTest = addLatexCommand
            .fields()
            .stream()
            .map(LeafNode.Field::fieldType)
            .sorted()
            .collect(Collectors.toCollection(ArrayList::new));

        Collections.sort(fieldTypes);

        assertEquals(fieldTypes.size(), fieldTypesTest.size());
        assertTrue(fieldTypes.containsAll(fieldTypesTest));
        assertTrue(fieldTypesTest.containsAll(fieldTypes));
    }


    @Test
    void variableTypesTest()
    {
        Parser parser = ProjectParserFactory.createProjectParser(parserType);

        Map<Path, PackageNode> packages = parser.parseSourcePackage(LatexEditor.SRC.path);
        PackageNode commandPackage = packages.get(LatexEditor.CONTROLLER.path);

        LeafNode     latexEditorController = commandPackage.getLeafNodes().get("LatexEditorController");
        List<String> variablesTypes        = new ArrayList<>(List.of("CommandFactory"));
        List<String> variablesTypesTest    = new ArrayList<>(latexEditorController.variables().values());

        Collections.sort(variablesTypesTest);
        Collections.sort(variablesTypes);

        assertEquals(variablesTypes.size(), variablesTypesTest.size());
        assertTrue(variablesTypes.containsAll(variablesTypesTest));
        assertTrue(variablesTypesTest.containsAll(variablesTypes));
    }


    @Test
    void objectCreationTest()
    {
        Parser parser = ProjectParserFactory.createProjectParser(parserType);

        Map<Path, PackageNode> packages = parser.parseSourcePackage(ParserTesting.SRC.path);
        PackageNode sourcePackage = packages.get(ParserTesting.SRC.path);

        LeafNode objectCreationSample = sourcePackage.getLeafNodes().get("ObjectCreationSample");
        List<String> objectsCreatedExpected = new ArrayList<>(List.of("ImplementingClass",
                                                                      "ImplementingClass",
                                                                      "ExtensionClass",
                                                                      "HashMap[String,TestingInterface]"));
        List<String> objectsCreatedActual = objectCreationSample.createdObjects();

        Collections.sort(objectsCreatedActual);
        Collections.sort(objectsCreatedExpected);

        assertEquals(objectsCreatedExpected.size(), objectsCreatedActual.size());
        assertTrue(objectsCreatedExpected.containsAll(objectsCreatedActual));
        assertTrue(objectsCreatedActual.containsAll(objectsCreatedExpected));
    }


    @Test
    void leafNodeTypesTest()
    {
        Parser parser = ProjectParserFactory.createProjectParser(parserType);

        Map<Path, PackageNode> packages = parser.parseSourcePackage(ParserTesting.SRC.path);
        PackageNode inheritancePackage = packages.get(ParserTesting.SRC.path);

        List<LeafNode> classLeafs     = new ArrayList<>();
        classLeafs.add(inheritancePackage.getLeafNodes().get("ImplementingClass"));
        classLeafs.add(inheritancePackage.getLeafNodes().get("ExtensionClass"));

        List<LeafNode> interfaceLeafs = new ArrayList<>();
        interfaceLeafs.add(inheritancePackage.getLeafNodes().get("TestingInterface"));
        interfaceLeafs.add(inheritancePackage.getLeafNodes().get("TestingInterface2"));

        long classCount = classLeafs.stream()
            .filter(it -> it.nodeType().equals(CLASS))
            .count();
        assertEquals(classCount, classLeafs.size());

        long interfaceCount = interfaceLeafs.stream()
            .filter(it -> it.nodeType().equals(INTERFACE))
            .count();
        assertEquals(interfaceCount, interfaceLeafs.size());

        PackageNode sourcePackage = packages.get(ParserTesting.SRC.path);
        LeafNode enumTest = sourcePackage.getLeafNodes().get("EnumSample");
        assertEquals(NodeType.ENUM, enumTest.nodeType());

        LeafNode objectCreationTest = sourcePackage.getLeafNodes().get("ObjectCreationSample");
        assertEquals(CLASS, objectCreationTest.nodeType());
    }

    @Test
    void inheritanceTest()
    {
        Parser parser = ProjectParserFactory.createProjectParser(parserType);

        Map<Path, PackageNode> packages = parser.parseSourcePackage(ParserTesting.SRC.path);
        PackageNode inheritancePackage  = packages.get(ParserTesting.SRC.path);

        LeafNode implementingClass = inheritancePackage.getLeafNodes().get("ImplementingClass");
        assertEquals(2, implementingClass.implementedInterfaces().size());
        assertTrue(implementingClass.implementedInterfaces().contains("TestingInterface"));
        assertTrue(implementingClass.implementedInterfaces().contains("TestingInterface2"));

        assertEquals("ExtensionClass", implementingClass.baseClass());
    }


    @Test
    void innerMembersTest()
    {
        Parser parser = ProjectParserFactory.createProjectParser(parserType);

        Map<Path, PackageNode> packages = parser.parseSourcePackage(ParserTesting.SRC.path);
        PackageNode sourcePackage = packages.get(ParserTesting.SRC.path);

        LeafNode innerClassSample = sourcePackage.getLeafNodes().get("InnerClassSample");

        assertEquals(innerClassSample.innerClasses().size(), 1);
        assertEquals("InnerClass", innerClassSample.innerClasses().get(0).nodeName());

        assertEquals(innerClassSample.records().size(), 1);
        assertEquals("RecordSample", innerClassSample.records().get(0));
    }


    @Test
    void importsTest()
    {
        List<String> expectedImports = List.of("javax.swing.JOptionPane",
                                               "model.strategies.StableVersionsStrategy",
                                               "model.strategies.VersionsStrategy",
                                               "model.strategies.VolatileVersionsStrategy",
                                               "view.LatexEditorView");

        Parser parser = ProjectParserFactory.createProjectParser(parserType);

        Map<Path, PackageNode> packages = parser.parseSourcePackage(LatexEditor.SRC.path);
        PackageNode commandPackage = packages.get(LatexEditor.MODEL.path);
        LeafNode     versionsManager = commandPackage.getLeafNodes().get("VersionsManager");
        List<String> imports         = versionsManager.imports();
        assertEquals(expectedImports, imports);
    }
}
