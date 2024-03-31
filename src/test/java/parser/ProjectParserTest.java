package parser;

import org.junit.jupiter.api.Test;
import parser.factory.Parser;
import parser.factory.ParserType;
import parser.factory.ProjectParserFactory;
import parser.tree.LeafNode;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ProjectParserTest
{

    ParserType parserType = ParserType.JAVAPARSER;


    @Test
    void parsingTest()
    {

        Parser parser = ProjectParserFactory.createProjectParser(parserType);
        Map<Path, PackageNode> packageNodes = parser.parseSourcePackage(Paths.get(String.format("%s%s%s",
                                                                                                PathConstructor.getCurrentPath(),
                                                                                                File.separator,
                                                                                                PathConstructor.constructPath("src",
                                                                                                                              "test",
                                                                                                                              "resources",
                                                                                                                              "LatexEditor",
                                                                                                                              "src"))));

        PackageNode controllerPackage = packageNodes.get(Paths.get(PathConstructor.getCurrentPath().normalize().toString(),
                                                                   PathConstructor.constructPath("src",
                                                                                                 "test",
                                                                                                 "resources",
                                                                                                 "LatexEditor",
                                                                                                 "src",
                                                                                                 "controller")));
        List<Path> testingLeafNodes = new ArrayList<>();
        assertEquals(Paths.get(PathConstructor.getCurrentPath().normalize().toString(),
                               PathConstructor.constructPath("src",
                                                             "test",
                                                             "resources",
                                                             "LatexEditor",
                                                             "src",
                                                             "controller")),
                     controllerPackage.getPath());

        assertEquals(Paths.get(PathConstructor.getCurrentPath().normalize().toString(),
                               String.format("%s%s",
                                             File.separator,
                                             PathConstructor.constructPath("src",
                                                                           "test",
                                                                           "resources",
                                                                           "LatexEditor",
                                                                           "src"))),
                     controllerPackage.getParentNode().getPath());

        assertTrue(controllerPackage.isValid(), "message");
        Map<Path, PackageNode> subNodes = controllerPackage.getSubNodes();
        assertEquals(Paths.get(PathConstructor.getCurrentPath().normalize().toString(),
                               PathConstructor.constructPath("src",
                                                             "test",
                                                             "resources",
                                                             "LatexEditor",
                                                             "src",
                                                             "controller",
                                                             "commands")),
                     subNodes.get(Paths.get(PathConstructor.getCurrentPath().normalize().toString(),
                                            PathConstructor.constructPath("src",
                                                                          "test",
                                                                          "resources",
                                                                          "LatexEditor",
                                                                          "src",
                                                                          "controller",
                                                                          "commands"))).getPath());

        for (LeafNode l : controllerPackage.getLeafNodes().values())
        {
            testingLeafNodes.add(l.path());
            assertEquals(l.parentNode().getPath(),
                         Paths.get(PathConstructor.getCurrentPath().normalize().toString(),
                                   PathConstructor.constructPath("src",
                                                                 "test",
                                                                 "resources",
                                                                 "LatexEditor",
                                                                 "src",
                                                                 "controller")));
        }
        Collections.sort(testingLeafNodes);
        Collections.sort(CONTROLLERS_LEAF_NODES);
        assertTrue(testingLeafNodes.size() == CONTROLLERS_LEAF_NODES.size() &&
                   CONTROLLERS_LEAF_NODES.containsAll(testingLeafNodes) &&
                   testingLeafNodes.containsAll(CONTROLLERS_LEAF_NODES));

        testingLeafNodes.clear();

        PackageNode commandsPackage = packageNodes.get(Paths.get(PathConstructor.getCurrentPath().normalize().toString(),
                                                                 PathConstructor.constructPath("src",
                                                                                               "test",
                                                                                               "resources",
                                                                                               "LatexEditor",
                                                                                               "src",
                                                                                               "controller",
                                                                                               "commands")));
        assertEquals(Paths.get(PathConstructor.getCurrentPath().normalize().toString(),
                               PathConstructor.constructPath("src",
                                                             "test",
                                                             "resources",
                                                             "LatexEditor",
                                                             "src",
                                                             "controller",
                                                             "commands")),
                     commandsPackage.getPath());

        assertEquals(Paths.get(PathConstructor.getCurrentPath().normalize().toString(),
                               PathConstructor.constructPath("src",
                                                             "test",
                                                             "resources",
                                                             "LatexEditor",
                                                             "src",
                                                             "controller")),
                     commandsPackage.getParentNode().getPath());

        assertTrue(commandsPackage.isValid());
        subNodes = commandsPackage.getSubNodes();
        assertEquals(0, subNodes.size());
        for (LeafNode l : commandsPackage.getLeafNodes().values())
        {
            testingLeafNodes.add(l.path());
            assertEquals(l.parentNode().getPath(),
                         Paths.get(PathConstructor.getCurrentPath().normalize().toString(),
                                   PathConstructor.constructPath("src",
                                                                 "test",
                                                                 "resources",
                                                                 "LatexEditor",
                                                                 "src",
                                                                 "controller",
                                                                 "commands")));
        }
        Collections.sort(testingLeafNodes);
        Collections.sort(COMMANDS_LEAF_NODES);
        assertTrue(testingLeafNodes.size() == COMMANDS_LEAF_NODES.size() &&
                   COMMANDS_LEAF_NODES.containsAll(testingLeafNodes)              &&
                   testingLeafNodes.containsAll(COMMANDS_LEAF_NODES));

        testingLeafNodes.clear();

        PackageNode modelPackage = packageNodes.get(Paths.get(PathConstructor.getCurrentPath().normalize().toString(),
                                                              PathConstructor.constructPath("src",
                                                                                            "test",
                                                                                            "resources",
                                                                                            "LatexEditor",
                                                                                            "src",
                                                                                            "model")));
        assertEquals(Paths.get(PathConstructor.getCurrentPath().normalize().toString(),
                               PathConstructor.constructPath("src",
                                                             "test",
                                                             "resources",
                                                             "LatexEditor",
                                                             "src",
                                                             "model")),
                     modelPackage.getPath());

        assertEquals(Paths.get(PathConstructor.getCurrentPath().normalize().toString(),
                               String.format("%s%s",
                                             File.separator,
                                             PathConstructor.constructPath("src",
                                                                           "test",
                                                                           "resources",
                                                                           "LatexEditor",
                                                                           "src"))),
                     modelPackage.getParentNode().getPath());

        assertTrue(modelPackage.isValid());
        subNodes = modelPackage.getSubNodes();
        assertEquals(Paths.get(PathConstructor.getCurrentPath().normalize().toString(),
                               PathConstructor.constructPath("src",
                                                             "test",
                                                             "resources",
                                                             "LatexEditor",
                                                             "src",
                                                             "model",
                                                             "strategies")),
                     subNodes.get(Paths.get(PathConstructor.getCurrentPath().normalize().toString(),
                                            PathConstructor.constructPath("src",
                                                                          "test",
                                                                          "resources",
                                                                          "LatexEditor",
                                                                          "src",
                                                                          "model",
                                                                          "strategies"))).getPath());
        for (LeafNode l : modelPackage.getLeafNodes().values())
        {
            assertEquals(l.parentNode().getPath(), Paths.get(PathConstructor.getCurrentPath().normalize().toString(),
                                                             PathConstructor.constructPath("src",
                                                                                           "test",
                                                                                           "resources",
                                                                                           "LatexEditor",
                                                                                           "src",
                                                                                           "model")));
            testingLeafNodes.add(l.path());
        }
        Collections.sort(testingLeafNodes);
        Collections.sort(MODELS_LEAF_NODES);
        assertTrue(testingLeafNodes.size() == MODELS_LEAF_NODES.size() &&
                   MODELS_LEAF_NODES.containsAll(testingLeafNodes)              &&
                   testingLeafNodes.containsAll(MODELS_LEAF_NODES));

        testingLeafNodes.clear();

        PackageNode strategiesPackage = packageNodes.get(Paths.get(PathConstructor.getCurrentPath().normalize().toString(),
                                                                   PathConstructor.constructPath("src",
                                                                                                 "test",
                                                                                                 "resources",
                                                                                                 "LatexEditor",
                                                                                                 "src",
                                                                                                 "model",
                                                                                                 "strategies")));
        assertEquals(Paths.get(PathConstructor.getCurrentPath().normalize().toString(),
                               PathConstructor.constructPath("src",
                                                             "test",
                                                             "resources",
                                                             "LatexEditor",
                                                             "src",
                                                             "model",
                                                             "strategies")),
                     strategiesPackage.getPath());
        assertEquals(Paths.get(PathConstructor.getCurrentPath().normalize().toString(),
                               PathConstructor.constructPath("src",
                                                             "test",
                                                             "resources",
                                                             "LatexEditor",
                                                             "src",
                                                             "model")),
                     strategiesPackage.getParentNode().getPath());
        assertTrue(strategiesPackage.isValid());
        subNodes = strategiesPackage.getSubNodes();
        assertEquals(0, subNodes.size());
        for (LeafNode l : strategiesPackage.getLeafNodes().values())
        {
            assertEquals(l.parentNode().getPath(), Paths.get(PathConstructor.getCurrentPath().normalize().toString(),
                                                             PathConstructor.constructPath("src",
                                                                                           "test",
                                                                                           "resources",
                                                                                           "LatexEditor",
                                                                                           "src",
                                                                                           "model",
                                                                                           "strategies")));
            testingLeafNodes.add(l.path());
        }
        Collections.sort(testingLeafNodes);
        Collections.sort(STRATEGIES_LEAF_NODES);
        assertTrue(testingLeafNodes.size() == STRATEGIES_LEAF_NODES.size() &&
                   STRATEGIES_LEAF_NODES.containsAll(testingLeafNodes)              &&
                   testingLeafNodes.containsAll(STRATEGIES_LEAF_NODES));

        testingLeafNodes.clear();

        PackageNode viewPackage = packageNodes.get(Paths.get(PathConstructor.getCurrentPath().normalize().toString(),
                                                             PathConstructor.constructPath("src",
                                                                                           "test",
                                                                                           "resources",
                                                                                           "LatexEditor",
                                                                                           "src",
                                                                                           "view")));
        assertEquals(Paths.get(PathConstructor.getCurrentPath().normalize().toString(),
                               PathConstructor.constructPath("src",
                                                             "test",
                                                             "resources",
                                                             "LatexEditor",
                                                             "src",
                                                             "view")),
                     viewPackage.getPath());
        assertEquals(Paths.get(PathConstructor.getCurrentPath().normalize().toString(), String.format("%s%s",
                                                                                                      File.separator,
                                                                                                      PathConstructor.constructPath("src",
                                                                                                                                    "test",
                                                                                                                                    "resources",
                                                                                                                                    "LatexEditor",
                                                                                                                                    "src"))),
                     viewPackage.getParentNode().getPath());
        assertTrue(viewPackage.isValid());
        subNodes = viewPackage.getSubNodes();
        assertEquals(0, subNodes.size());
        for (LeafNode l : viewPackage.getLeafNodes().values())
        {
            assertEquals(l.parentNode().getPath(), Paths.get(PathConstructor.getCurrentPath().normalize().toString(),
                                                             PathConstructor.constructPath("src",
                                                                                           "test",
                                                                                           "resources",
                                                                                           "LatexEditor",
                                                                                           "src",
                                                                                           "view")));
            testingLeafNodes.add(l.path());
        }
        Collections.sort(testingLeafNodes);
        Collections.sort(VIEWS_LEAF_NODES);
        assertTrue(testingLeafNodes.size() == VIEWS_LEAF_NODES.size() &&
                   VIEWS_LEAF_NODES.containsAll(testingLeafNodes)              &&
                   testingLeafNodes.containsAll(VIEWS_LEAF_NODES));
        testingLeafNodes.clear();

        PackageNode sourcePackage = packageNodes.get(Paths.get(PathConstructor.getCurrentPath().normalize().toString(),
                                                               String.format("%s%s",
                                                                             File.separator,
                                                                             PathConstructor.constructPath("src",
                                                                                                           "test",
                                                                                                           "resources",
                                                                                                           "LatexEditor",
                                                                                                           "src"))));
        assertEquals(Paths.get(PathConstructor.getCurrentPath().normalize().toString(),
                               String.format("%s%s",
                                             File.separator,
                                             PathConstructor.constructPath("src",
                                                                           "test",
                                                                           "resources",
                                                                           "LatexEditor",
                                                                           "src"))),
                     sourcePackage.getPath());

        assertEquals(Paths.get(""), sourcePackage.getParentNode().getPath());
        assertFalse(sourcePackage.isValid());
        subNodes = sourcePackage.getSubNodes();
        List<Path> testingSubPackages = new ArrayList<>();
        for (PackageNode subP : subNodes.values())
        {
            testingSubPackages.add(subP.getPath());
        }
        Collections.sort(testingSubPackages);
        Collections.sort(SOURCES_SUB_PACKAGES);
        assertTrue(testingSubPackages.size() == SOURCES_SUB_PACKAGES.size() &&
                   SOURCES_SUB_PACKAGES.containsAll(testingSubPackages)              &&
                   testingSubPackages.containsAll(SOURCES_SUB_PACKAGES));
        assertEquals(0, sourcePackage.getLeafNodes().size());
    }


    public static final List<Path> SOURCES_SUB_PACKAGES =
        new ArrayList<>(Arrays.asList(Paths.get(PathConstructor.getCurrentPath().normalize().toString(),
                                                PathConstructor.constructPath("src",
                                                                              "test",
                                                                              "resources",
                                                                              "LatexEditor",
                                                                              "src",
                                                                              "controller")),
                                      Paths.get(PathConstructor.getCurrentPath().normalize().toString(),
                                                PathConstructor.constructPath("src",
                                                                              "test",
                                                                              "resources",
                                                                              "LatexEditor",
                                                                              "src",
                                                                              "model")),
                                                                                        Paths.get(PathConstructor.getCurrentPath().normalize().toString(),
                                                                                                  PathConstructor.constructPath("src",
                                                                                                                                "test",
                                                                                                                                "resources",
                                                                                                                                "LatexEditor",
                                                                                                                                "src",
                                                                                                                                "view"))));

    public static final List<Path> VIEWS_LEAF_NODES =
        new ArrayList<>(Arrays.asList(Paths.get(PathConstructor.getCurrentPath().normalize().toString(),
                                                PathConstructor.constructPath("src",
                                                                              "test",
                                                                              "resources",
                                                                              "LatexEditor",
                                                                              "src",
                                                                              "view",
                                                                              "ChooseTemplate.java")),
                                      Paths.get(PathConstructor.getCurrentPath().normalize().toString(),
                                                PathConstructor.constructPath("src",
                                                                              "test",
                                                                              "resources",
                                                                              "LatexEditor",
                                                                              "src",
                                                                              "view",
                                                                              "LatexEditorView.java")),
                                      Paths.get(PathConstructor.getCurrentPath().normalize().toString(),
                                                PathConstructor.constructPath("src",
                                                                              "test",
                                                                              "resources",
                                                                              "LatexEditor",
                                                                              "src",
                                                                              "view",
                                                                              "MainWindow.java")),
                                      Paths.get(PathConstructor.getCurrentPath().normalize().toString(),
                                                PathConstructor.constructPath("src",
                                                                              "test",
                                                                              "resources",
                                                                              "LatexEditor",
                                                                              "src",
                                                                              "view",
                                                                              "OpeningWindow.java"))));

    public static final List<Path> CONTROLLERS_LEAF_NODES =
        new ArrayList<>(List.of(Paths.get(PathConstructor.getCurrentPath().normalize().toString(),
                                          PathConstructor.constructPath("src",
                                                                        "test",
                                                                        "resources",
                                                                        "LatexEditor",
                                                                        "src",
                                                                        "controller",
                                                                        "LatexEditorController.java"))));

    public static final List<Path> STRATEGIES_LEAF_NODES =
        new ArrayList<>(Arrays.asList(Paths.get(PathConstructor.getCurrentPath().normalize().toString(),
                                                PathConstructor.constructPath("src",
                                                                              "test",
                                                                              "resources",
                                                                              "LatexEditor",
                                                                              "src",
                                                                              "model",
                                                                              "strategies",
                                                                              "StableVersionsStrategy.java")),
                                      Paths.get(PathConstructor.getCurrentPath().normalize().toString(),
                                                PathConstructor.constructPath("src",
                                                                              "test",
                                                                              "resources",
                                                                              "LatexEditor",
                                                                              "src",
                                                                              "model",
                                                                              "strategies",
                                                                              "VersionsStrategy.java")),
                                      Paths.get(PathConstructor.getCurrentPath().normalize().toString(),
                                                PathConstructor.constructPath("src",
                                                                              "test",
                                                                              "resources",
                                                                              "LatexEditor",
                                                                              "src",
                                                                              "model",
                                                                              "strategies",
                                                                              "VolatileVersionsStrategy.java")),
                                      Paths.get(PathConstructor.getCurrentPath().normalize().toString(),
                                                PathConstructor.constructPath("src",
                                                                              "test",
                                                                              "resources",
                                                                              "LatexEditor",
                                                                              "src",
                                                                              "model",
                                                                              "strategies",
                                                                              "VersionsStrategyFactory.java"))));

    public static final List<Path> MODELS_LEAF_NODES =
        new ArrayList<>(Arrays.asList(Paths.get(PathConstructor.getCurrentPath().normalize().toString(),
                                                PathConstructor.constructPath("src",
                                                                              "test",
                                                                              "resources",
                                                                              "LatexEditor",
                                                                              "src",
                                                                              "model",
                                                                              "Document.java")),
                                      Paths.get(PathConstructor.getCurrentPath().normalize().toString(),
                                                PathConstructor.constructPath("src",
                                                                              "test",
                                                                              "resources",
                                                                              "LatexEditor",
                                                                              "src",
                                                                              "model",
                                                                              "DocumentManager.java")),
                                      Paths.get(PathConstructor.getCurrentPath().normalize().toString(),
                                                PathConstructor.constructPath("src",
                                                                              "test",
                                                                              "resources",
                                                                              "LatexEditor",
                                                                              "src",
                                                                              "model",
                                                                              "VersionsManager.java"))));

    public static final List<Path> COMMANDS_LEAF_NODES =
        new ArrayList<>(Arrays.asList(Paths.get(PathConstructor.getCurrentPath().normalize().toString(),
                                                PathConstructor.constructPath("src",
                                                                              "test",
                                                                              "resources",
                                                                              "LatexEditor",
                                                                              "src",
                                                                              "controller",
                                                                              "commands",
                                                                              "AddLatexCommand.java")),
                                      Paths.get(PathConstructor.getCurrentPath().normalize().toString(),
                                                PathConstructor.constructPath("src",
                                                                              "test",
                                                                              "resources",
                                                                              "LatexEditor",
                                                                              "src",
                                                                              "controller",
                                                                              "commands",
                                                                              "Command.java")),
                                      Paths.get(PathConstructor.getCurrentPath().normalize().toString(),
                                                PathConstructor.constructPath("src",
                                                                              "test",
                                                                              "resources",
                                                                              "LatexEditor",
                                                                              "src",
                                                                              "controller",
                                                                              "commands",
                                                                              "CommandFactory.java")),
                                      Paths.get(PathConstructor.getCurrentPath().normalize().toString(),
                                                PathConstructor.constructPath("src",
                                                                              "test",
                                                                              "resources",
                                                                              "LatexEditor",
                                                                              "src",
                                                                              "controller",
                                                                              "commands",
                                                                              "CreateCommand.java")),
                                      Paths.get(PathConstructor.getCurrentPath().normalize().toString(),
                                                PathConstructor.constructPath("src",
                                                                              "test",
                                                                              "resources",
                                                                              "LatexEditor",
                                                                              "src",
                                                                              "controller",
                                                                              "commands",
                                                                              "EditCommand.java")),
                                      Paths.get(PathConstructor.getCurrentPath().normalize().toString(),
                                                PathConstructor.constructPath("src",
                                                                              "test",
                                                                              "resources",
                                                                              "LatexEditor",
                                                                              "src",
                                                                              "controller",
                                                                              "commands",
                                                                              "LoadCommand.java")),
                                      Paths.get(PathConstructor.getCurrentPath().normalize().toString(),
                                                PathConstructor.constructPath("src",
                                                                              "test",
                                                                              "resources",
                                                                              "LatexEditor",
                                                                              "src",
                                                                              "controller",
                                                                              "commands",
                                                                              "SaveCommand.java")),
                                      Paths.get(PathConstructor.getCurrentPath().normalize().toString(),
                                                PathConstructor.constructPath("src",
                                                                              "test",
                                                                              "resources",
                                                                              "LatexEditor",
                                                                              "src",
                                                                              "controller",
                                                                              "commands",
                                                                              "ChangeVersionsStrategyCommand.java")),
                                      Paths.get(PathConstructor.getCurrentPath().normalize().toString(),
                                                PathConstructor.constructPath("src",
                                                                              "test",
                                                                              "resources",
                                                                              "LatexEditor",
                                                                              "src",
                                                                              "controller",
                                                                              "commands",
                                                                              "DisableVersionsManagementCommand.java")),
                                      Paths.get(PathConstructor.getCurrentPath().normalize().toString(),
                                                PathConstructor.constructPath("src",
                                                                              "test",
                                                                              "resources",
                                                                              "LatexEditor",
                                                                              "src",
                                                                              "controller",
                                                                              "commands",
                                                                              "EnableVersionsManagementCommand.java")),
                                      Paths.get(PathConstructor.getCurrentPath().normalize().toString(),
                                                PathConstructor.constructPath("src",
                                                                              "test",
                                                                              "resources",
                                                                              "LatexEditor",
                                                                              "src",
                                                                              "controller",
                                                                              "commands",
                                                                              "RollbackToPreviousVersionCommand.java"))));
}
