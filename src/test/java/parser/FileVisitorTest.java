package parser;

import org.junit.jupiter.api.Test;
import parser.factory.Parser;
import parser.factory.ParserType;
import parser.factory.ProjectParserFactory;
import parser.tree.LeafNode;
import parser.tree.NodeType;
import parser.tree.PackageNode;
import utils.PathConstructor;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FileVisitorTest
{

    ParserType parserType = ParserType.JAVAPARSER;


    @Test
    void methodReturnTypesTest()
    {
        Parser parser = ProjectParserFactory.createProjectParser(parserType);

        Map<Path, PackageNode> packages = parser.parseSourcePackage(Paths.get(String.format("%s%s%s",
                                                                                            PathConstructor.getCurrentPath().toString(),
                                                                                            File.separator,
                                                                                            PathConstructor.constructPath("src",
                                                                                                                          "test",
                                                                                                                          "resources",
                                                                                                                          "LatexEditor",
                                                                                                                          "src"))));
        PackageNode commandPackage = packages.get(Paths.get(PathConstructor.getCurrentPath().normalize().toString(),
                                                            PathConstructor.constructPath("src",
                                                                                          "test",
                                                                                          "resources",
                                                                                          "LatexEditor",
                                                                                          "src",
                                                                                          "controller",
                                                                                          "commands")));

        LeafNode addLatexCommand = commandPackage.getLeafNodes().get("AddLatexCommand");
        List<String> methodReturnTypesExpected = new ArrayList<>(Arrays.asList("Constructor",
                                                                               "void"));
        List<String> methodReturnTypesActual = addLatexCommand.getMethodReturnTypes();

        Collections.sort(methodReturnTypesActual);
        Collections.sort(methodReturnTypesExpected);
        assertTrue(methodReturnTypesActual.size() == methodReturnTypesExpected.size() &&
                   methodReturnTypesExpected.containsAll(methodReturnTypesActual)              &&
                   methodReturnTypesActual.containsAll(methodReturnTypesExpected));
    }


    @Test
    void methodParameterTypesTest()
    {
        Parser parser = ProjectParserFactory.createProjectParser(parserType);

        Map<Path, PackageNode> packages = parser.parseSourcePackage(Paths.get(String.format("%s%s%s",
                                                                                            PathConstructor.getCurrentPath(),
                                                                                            File.separator,
                                                                                            PathConstructor.constructPath("src",
                                                                                                                          "test",
                                                                                                                          "resources",
                                                                                                                          "LatexEditor",
                                                                                                                          "src"))));
        PackageNode commandPackage = packages.get(Paths.get(PathConstructor.getCurrentPath().normalize().toString(),
                                                            PathConstructor.constructPath("src",
                                                                                          "test",
                                                                                          "resources",
                                                                                          "LatexEditor",
                                                                                          "src",
                                                                                          "controller",
                                                                                          "commands")));

        LeafNode     addLatexCommand          = commandPackage.getLeafNodes().get("AddLatexCommand");
        List<String> methodParameterTypes     = new ArrayList<>(List.of("VersionsManager"));
        List<String> methodParameterTypesTest = addLatexCommand.getMethodParameterTypes();

        Collections.sort(methodParameterTypesTest);
        Collections.sort(methodParameterTypes);
        assertTrue(methodParameterTypesTest.size() == methodParameterTypes.size() &&
                   methodParameterTypes.containsAll(methodParameterTypesTest)              &&
                   methodParameterTypesTest.containsAll(methodParameterTypes));
    }


    @Test
    void fieldTypesTest()
    {
        Parser parser = ProjectParserFactory.createProjectParser(parserType);

        Map<Path, PackageNode> packages = parser.parseSourcePackage(Paths.get(String.format("%s%s%s",
                                                                                            PathConstructor.getCurrentPath(),
                                                                                            File.separator,
                                                                                            PathConstructor.constructPath("src",
                                                                                                                          "test",
                                                                                                                          "resources",
                                                                                                                          "LatexEditor",
                                                                                                                          "src"))));
        PackageNode commandPackage = packages.get(Paths.get(PathConstructor.getCurrentPath().normalize().toString(),
                                                            PathConstructor.constructPath("src",
                                                                                          "test",
                                                                                          "resources",
                                                                                          "LatexEditor",
                                                                                          "src",
                                                                                          "controller",
                                                                                          "commands")));
        LeafNode     addLatexCommand = commandPackage.getLeafNodes().get("AddLatexCommand");
        List<String> fieldTypes      = new ArrayList<>(List.of("VersionsManager"));
        List<String> fieldTypesTest = addLatexCommand
            .fields()
            .stream()
            .map(LeafNode.Field::fieldType)
            .sorted()
            .collect(Collectors.toCollection(ArrayList::new));

        Collections.sort(fieldTypes);
        assertTrue(fieldTypesTest.size() == fieldTypes.size() &&
                   fieldTypes.containsAll(fieldTypesTest)              &&
                   fieldTypesTest.containsAll(fieldTypes));
    }


    @Test
    void variableTypesTest()
    {
        Parser parser = ProjectParserFactory.createProjectParser(parserType);

        Map<Path, PackageNode> packages = parser.parseSourcePackage(Paths.get(String.format("%s%s%s",
                                                                                            PathConstructor.getCurrentPath(),
                                                                                            File.separator,
                                                                                            PathConstructor.constructPath("src",
                                                                                                                          "test",
                                                                                                                          "resources",
                                                                                                                          "LatexEditor",
                                                                                                                          "src"))));
        PackageNode commandPackage = packages.get(Paths.get(PathConstructor.getCurrentPath().normalize().toString(),
                                                            "/src/test/resources/LatexEditor/src/controller"));

        LeafNode     latexEditorController = commandPackage.getLeafNodes().get("LatexEditorController");
        List<String> variablesTypes        = new ArrayList<>(List.of("CommandFactory"));
        List<String> variablesTypesTest    = new ArrayList<>(latexEditorController.variables().values());

        Collections.sort(variablesTypesTest);
        Collections.sort(variablesTypes);
        assertTrue(variablesTypesTest.size() == variablesTypes.size() &&
                   variablesTypes.containsAll(variablesTypesTest)              &&
                   variablesTypesTest.containsAll(variablesTypes));
    }


    @Test
    void objectCreationTest()
    {
        Parser parser = ProjectParserFactory.createProjectParser(parserType);

        Map<Path, PackageNode> packages = parser.parseSourcePackage(Paths.get(String.format("%s%s%s",
                                                                                            PathConstructor.getCurrentPath(),
                                                                                            File.separator,
                                                                                            PathConstructor.constructPath("src",
                                                                                                                          "test",
                                                                                                                          "resources",
                                                                                                                          "ParserTesting"))));
        PackageNode sourcePackage = packages.get(Paths.get(PathConstructor.getCurrentPath().normalize().toString(),
                                                           PathConstructor.constructPath("src",
                                                                                         "test",
                                                                                         "resources",
                                                                                         "ParserTesting")));

        LeafNode objectCreationSample = sourcePackage.getLeafNodes().get("ObjectCreationSample");
        List<String> objectsCreatedExpected = new ArrayList<>(List.of("ImplementingClass",
                                                                      "ImplementingClass",
                                                                      "ExtensionClass",
                                                                      "HashMap[String,TestingInterface]"));
        List<String> objectsCreatedActual = objectCreationSample.createdObjects();

        Collections.sort(objectsCreatedActual);
        Collections.sort(objectsCreatedExpected);
        assertTrue(objectsCreatedActual.size() == objectsCreatedExpected.size() &&
                   objectsCreatedExpected.containsAll(objectsCreatedActual)              &&
                   objectsCreatedActual.containsAll(objectsCreatedExpected));
    }


    @Test
    void leafNodeTypesTest()
    {
        Parser parser = ProjectParserFactory.createProjectParser(parserType);

        Map<Path, PackageNode> packages = parser.parseSourcePackage(Paths.get(String.format("%s%s%s",
                                                                                            PathConstructor.getCurrentPath(),
                                                                                            File.separator,
                                                                                            PathConstructor.constructPath("src",
                                                                                                                          "test",
                                                                                                                          "resources",
                                                                                                                          "ParserTesting"))));
        PackageNode inheritancePackage = packages.get(Paths.get(PathConstructor.getCurrentPath().normalize().toString(),
                                                                PathConstructor.constructPath("src",
                                                                                              "test",
                                                                                              "resources",
                                                                                              "ParserTesting")));
        List<LeafNode> classLeafs     = new ArrayList<>();
        List<LeafNode> interfaceLeafs = new ArrayList<>();
        classLeafs.add(inheritancePackage.getLeafNodes().get("ImplementingClass"));
        classLeafs.add(inheritancePackage.getLeafNodes().get("ExtensionClass"));
        interfaceLeafs.add(inheritancePackage.getLeafNodes().get("TestingInterface"));
        interfaceLeafs.add(inheritancePackage.getLeafNodes().get("TestingInterface2"));

        for (LeafNode l : classLeafs)
        {
            assertEquals(NodeType.CLASS, l.nodeType());
        }
        for (LeafNode l : interfaceLeafs)
        {
            assertEquals(NodeType.INTERFACE, l.nodeType());
        }

        PackageNode sourcePackage = packages.get(Paths.get(PathConstructor.getCurrentPath().normalize().toString(),
                                                           PathConstructor.constructPath("src",
                                                                                         "test",
                                                                                         "resources",
                                                                                         "ParserTesting")));

        LeafNode enumTest = sourcePackage.getLeafNodes().get("EnumSample");
        assertEquals(NodeType.ENUM, enumTest.nodeType());

        LeafNode objectCreationTest = sourcePackage.getLeafNodes().get("ObjectCreationSample");
        assertEquals(NodeType.CLASS, objectCreationTest.nodeType());
    }


    @Test
    void innerMembersTest()
    {
        Parser parser = ProjectParserFactory.createProjectParser(parserType);

        Map<Path, PackageNode> packages = parser.parseSourcePackage(Paths.get(String.format("%s%s%s",
                                                                                            PathConstructor.getCurrentPath(),
                                                                                            File.separator,
                                                                                            PathConstructor.constructPath("src",
                                                                                                                          "test",
                                                                                                                          "resources",
                                                                                                                          "ParserTesting"))));
        PackageNode sourcePackage = packages.get(Paths.get(PathConstructor.getCurrentPath().normalize().toString(),
                                                           PathConstructor.constructPath("src",
                                                                                         "test",
                                                                                         "resources",
                                                                                         "ParserTesting")));
        LeafNode innerClassSample = sourcePackage.getLeafNodes().get("InnerClassSample");
        assertEquals(innerClassSample.innerClasses().get(0).nodeName(), "InnerClass");
        assertEquals(innerClassSample.records().get(0), "RecordSample");
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

        Map<Path, PackageNode> packages = parser.parseSourcePackage(Paths.get(String.format("%s%s%s",
                                                                                            PathConstructor.getCurrentPath(),
                                                                                            File.separator,
                                                                                            PathConstructor.constructPath("src",
                                                                                                                          "test",
                                                                                                                          "resources",
                                                                                                                          "LatexEditor",
                                                                                                                          "src"))));
        PackageNode commandPackage = packages.get(Paths.get(PathConstructor.getCurrentPath().normalize().toString(),
                                                            "/src/test/resources/LatexEditor/src/model"));
        LeafNode     versionsManager = commandPackage.getLeafNodes().get("VersionsManager");
        List<String> imports         = versionsManager.imports();
        assertEquals(expectedImports, imports);
    }
}
