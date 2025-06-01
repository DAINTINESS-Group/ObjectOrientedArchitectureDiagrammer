package parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import parser.factory.Parser;
import parser.factory.ParserType;
import parser.factory.ProjectParserFactory;
import parser.tree.LeafNode;
import parser.tree.PackageNode;
import utils.PathTemplate.LatexEditor;

public class ProjectParserTest {

    ParserType parserType = ParserType.JAVAPARSER;

    public static final List<Path> SOURCES_SUB_PACKAGES =
            new ArrayList<>(
                    List.of(
                            LatexEditor.CONTROLLER.path,
                            LatexEditor.MODEL.path,
                            LatexEditor.VIEW.path));

    public static final List<Path> VIEWS_LEAF_NODES =
            new ArrayList<>(
                    List.of(
                            LatexEditor.CHOOSE_TEMPLATE.path,
                            LatexEditor.LATEX_EDITOR_VIEW.path,
                            LatexEditor.MAIN_WINDOW.path,
                            LatexEditor.OPENING_WINDOW.path));

    public static final List<Path> CONTROLLERS_LEAF_NODES =
            new ArrayList<>(List.of(LatexEditor.LATEX_EDITOR_CONTROLLER.path));

    public static final List<Path> STRATEGIES_LEAF_NODES =
            new ArrayList<>(
                    List.of(
                            LatexEditor.STABLE_VERSIONS_STRATEGY.path,
                            LatexEditor.VERSIONS_STRATEGY.path,
                            LatexEditor.VOLATILE_VERSIONS_STRATEGY.path,
                            LatexEditor.VERSIONS_STRATEGY_FACTORY.path));
    public static final List<Path> MODELS_LEAF_NODES =
            new ArrayList<>(
                    List.of(
                            LatexEditor.DOCUMENT.path,
                            LatexEditor.DOCUMENT_MANAGER.path,
                            LatexEditor.VERSIONS_MANAGER.path));

    public static final List<Path> COMMANDS_LEAF_NODES =
            new ArrayList<>(
                    List.of(
                            LatexEditor.ADD_LATEX_COMMAND.path,
                            LatexEditor.COMMAND.path,
                            LatexEditor.COMMAND_FACTORY.path,
                            LatexEditor.CREATE_COMMAND.path,
                            LatexEditor.EDIT_COMMAND.path,
                            LatexEditor.LOAD_COMMAND.path,
                            LatexEditor.SAVE_COMMAND.path,
                            LatexEditor.CHANGE_VERSIONS_STRATEGY_COMMAND.path,
                            LatexEditor.DISABLE_VERSIONS_MANAGER_COMMAND.path,
                            LatexEditor.ENABLE_VERSIONS_MANAGE_COMMAND.path,
                            LatexEditor.ROLLBACK_TO_PREVIOUS_VERSION_COMMAND.path));

    @Test
    void parsingTest() {

        Parser parser = ProjectParserFactory.createProjectParser(parserType);
        Map<Path, PackageNode> packageNodes = parser.parseSourcePackage(LatexEditor.SRC.path);

        PackageNode controllerPackage = packageNodes.get(LatexEditor.CONTROLLER.path);
        List<Path> testingLeafNodes = new ArrayList<>();
        assertEquals(LatexEditor.CONTROLLER.path, controllerPackage.getPath());

        assertEquals(LatexEditor.SRC.path, controllerPackage.getParentNode().getPath());

        assertTrue(controllerPackage.isValid(), "message");
        Map<Path, PackageNode> subNodes = controllerPackage.getSubNodes();
        assertEquals(LatexEditor.COMMANDS.path, subNodes.get(LatexEditor.COMMANDS.path).getPath());

        for (LeafNode l : controllerPackage.getLeafNodes().values()) {
            testingLeafNodes.add(l.path());
            assertEquals(l.parentNode().getPath(), LatexEditor.CONTROLLER.path);
        }
        Collections.sort(testingLeafNodes);
        Collections.sort(CONTROLLERS_LEAF_NODES);

        assertEquals(CONTROLLERS_LEAF_NODES.size(), testingLeafNodes.size());
        assertTrue(CONTROLLERS_LEAF_NODES.containsAll(testingLeafNodes));
        assertTrue(testingLeafNodes.containsAll(CONTROLLERS_LEAF_NODES));

        testingLeafNodes.clear();

        PackageNode commandsPackage = packageNodes.get(LatexEditor.COMMANDS.path);
        assertEquals(LatexEditor.COMMANDS.path, commandsPackage.getPath());
        assertEquals(LatexEditor.CONTROLLER.path, commandsPackage.getParentNode().getPath());

        assertTrue(commandsPackage.isValid());
        subNodes = commandsPackage.getSubNodes();
        assertEquals(0, subNodes.size());
        for (LeafNode l : commandsPackage.getLeafNodes().values()) {
            testingLeafNodes.add(l.path());
            assertEquals(l.parentNode().getPath(), LatexEditor.COMMANDS.path);
        }
        Collections.sort(testingLeafNodes);
        Collections.sort(COMMANDS_LEAF_NODES);

        assertEquals(COMMANDS_LEAF_NODES.size(), testingLeafNodes.size());
        assertTrue(COMMANDS_LEAF_NODES.containsAll(testingLeafNodes));
        assertTrue(testingLeafNodes.containsAll(COMMANDS_LEAF_NODES));

        testingLeafNodes.clear();

        PackageNode modelPackage = packageNodes.get(LatexEditor.MODEL.path);
        assertEquals(LatexEditor.MODEL.path, modelPackage.getPath());

        assertEquals(LatexEditor.SRC.path, modelPackage.getParentNode().getPath());

        assertTrue(modelPackage.isValid());
        subNodes = modelPackage.getSubNodes();
        assertEquals(
                LatexEditor.STRATEGIES.path, subNodes.get(LatexEditor.STRATEGIES.path).getPath());

        for (LeafNode l : modelPackage.getLeafNodes().values()) {
            assertEquals(l.parentNode().getPath(), LatexEditor.MODEL.path);
            testingLeafNodes.add(l.path());
        }
        Collections.sort(testingLeafNodes);
        Collections.sort(MODELS_LEAF_NODES);

        assertEquals(MODELS_LEAF_NODES.size(), testingLeafNodes.size());
        assertTrue(MODELS_LEAF_NODES.containsAll(testingLeafNodes));
        assertTrue(testingLeafNodes.containsAll(MODELS_LEAF_NODES));

        testingLeafNodes.clear();

        PackageNode strategiesPackage = packageNodes.get(LatexEditor.STRATEGIES.path);
        assertEquals(LatexEditor.STRATEGIES.path, strategiesPackage.getPath());
        assertEquals(LatexEditor.MODEL.path, strategiesPackage.getParentNode().getPath());
        assertTrue(strategiesPackage.isValid());
        subNodes = strategiesPackage.getSubNodes();
        assertEquals(0, subNodes.size());
        for (LeafNode l : strategiesPackage.getLeafNodes().values()) {
            assertEquals(l.parentNode().getPath(), LatexEditor.STRATEGIES.path);
            testingLeafNodes.add(l.path());
        }
        Collections.sort(testingLeafNodes);
        Collections.sort(STRATEGIES_LEAF_NODES);

        assertEquals(STRATEGIES_LEAF_NODES.size(), testingLeafNodes.size());
        assertTrue(STRATEGIES_LEAF_NODES.containsAll(testingLeafNodes));
        assertTrue(testingLeafNodes.containsAll(STRATEGIES_LEAF_NODES));

        testingLeafNodes.clear();

        PackageNode viewPackage = packageNodes.get(LatexEditor.VIEW.path);
        assertEquals(LatexEditor.VIEW.path, viewPackage.getPath());
        assertEquals(LatexEditor.SRC.path, viewPackage.getParentNode().getPath());
        assertTrue(viewPackage.isValid());
        subNodes = viewPackage.getSubNodes();
        assertEquals(0, subNodes.size());
        for (LeafNode l : viewPackage.getLeafNodes().values()) {
            assertEquals(l.parentNode().getPath(), LatexEditor.VIEW.path);
            testingLeafNodes.add(l.path());
        }

        Collections.sort(testingLeafNodes);
        Collections.sort(VIEWS_LEAF_NODES);

        assertEquals(VIEWS_LEAF_NODES.size(), testingLeafNodes.size());
        assertTrue(VIEWS_LEAF_NODES.containsAll(testingLeafNodes));
        assertTrue(testingLeafNodes.containsAll(VIEWS_LEAF_NODES));

        testingLeafNodes.clear();

        PackageNode sourcePackage = packageNodes.get(LatexEditor.SRC.path);
        assertEquals(LatexEditor.SRC.path, sourcePackage.getPath());

        assertEquals(Paths.get(""), sourcePackage.getParentNode().getPath());
        assertFalse(sourcePackage.isValid());
        subNodes = sourcePackage.getSubNodes();
        List<Path> testingSubPackages =
                subNodes.values().stream()
                        .map(PackageNode::getPath)
                        .sorted()
                        .collect(Collectors.toCollection(ArrayList::new));

        Collections.sort(SOURCES_SUB_PACKAGES);

        assertEquals(SOURCES_SUB_PACKAGES.size(), testingSubPackages.size());
        assertTrue(SOURCES_SUB_PACKAGES.containsAll(testingSubPackages));
        assertTrue(testingSubPackages.containsAll(SOURCES_SUB_PACKAGES));
        assertEquals(0, sourcePackage.getLeafNodes().size());
    }
}
