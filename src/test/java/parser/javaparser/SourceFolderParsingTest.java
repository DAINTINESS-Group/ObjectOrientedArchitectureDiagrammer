package parser.javaparser;

import model.tree.node.LeafNode;
import model.tree.node.PackageNode;
import org.junit.jupiter.api.Test;
import parser.Parser;
import parser.ParserType;
import parser.ProjectParserFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class SourceFolderParsingTest {

    Path currentDirectory = Path.of(".");
    ParserType parserType = ParserType.JAVAPARSER;

    @Test
    void parsingTest() throws IOException {
        ProjectParserFactory projectParserFactory = new ProjectParserFactory(parserType);
        Parser parser = projectParserFactory.createProjectParser();

        parser.parseSourcePackage(Paths.get(currentDirectory.toRealPath() + "\\src\\test\\resources\\LatexEditor\\src"));
        List<Path> sourcesSubPackages = new ArrayList<>(Arrays.asList(
                Paths.get(currentDirectory.toRealPath().normalize().toString(), "\\src\\test\\resources\\LatexEditor\\src\\controller"),
                Paths.get(currentDirectory.toRealPath().normalize().toString(), "\\src\\test\\resources\\LatexEditor\\src\\model"),
                Paths.get(currentDirectory.toRealPath().normalize().toString(), "\\src\\test\\resources\\LatexEditor\\src\\view")));
        List<Path> viewsLeafNodes = new ArrayList<>(Arrays.asList(
                Paths.get(currentDirectory.toRealPath().normalize().toString(), "\\src\\test\\resources\\LatexEditor\\src\\view\\ChooseTemplate.java"),
                Paths.get(currentDirectory.toRealPath().normalize().toString(), "\\src\\test\\resources\\LatexEditor\\src\\view\\LatexEditorView.java"),
                Paths.get(currentDirectory.toRealPath().normalize().toString(), "\\src\\test\\resources\\LatexEditor\\src\\view\\MainWindow.java"),
                Paths.get(currentDirectory.toRealPath().normalize().toString(), "\\src\\test\\resources\\LatexEditor\\src\\view\\OpeningWindow.java")
        ));
        List<Path> controllersLeafNodes = new ArrayList<>(List.of(
                Paths.get(currentDirectory.toRealPath().normalize().toString(), "\\src\\test\\resources\\LatexEditor\\src\\controller\\LatexEditorController.java")
        ));
        List<Path> strategiesLeafNodes = new ArrayList<>(Arrays.asList(
                Paths.get(currentDirectory.toRealPath().normalize().toString(), "\\src\\test\\resources\\LatexEditor\\src\\model\\strategies\\StableVersionsStrategy.java"),
                Paths.get(currentDirectory.toRealPath().normalize().toString(), "\\src\\test\\resources\\LatexEditor\\src\\model\\strategies\\VersionsStrategy.java"),
                Paths.get(currentDirectory.toRealPath().normalize().toString(), "\\src\\test\\resources\\LatexEditor\\src\\model\\strategies\\VolatileVersionsStrategy.java"),
                Paths.get(currentDirectory.toRealPath().normalize().toString(), "\\src\\test\\resources\\LatexEditor\\src\\model\\strategies\\VersionsStrategyFactory.java")
        ));
        List<Path> modelsLeafNodes = new ArrayList<>(Arrays.asList(
                Paths.get(currentDirectory.toRealPath().normalize().toString(), "\\src\\test\\resources\\LatexEditor\\src\\model\\Document.java"),
                Paths.get(currentDirectory.toRealPath().normalize().toString(), "\\src\\test\\resources\\LatexEditor\\src\\model\\DocumentManager.java"),
                Paths.get(currentDirectory.toRealPath().normalize().toString(), "\\src\\test\\resources\\LatexEditor\\src\\model\\VersionsManager.java")
        ));
        List<Path> commandsLeafNodes = new ArrayList<>(Arrays.asList(
                Paths.get(currentDirectory.toRealPath().normalize().toString(), "\\src\\test\\resources\\LatexEditor\\src\\controller\\commands\\AddLatexCommand.java"),
                Paths.get(currentDirectory.toRealPath().normalize().toString(), "\\src\\test\\resources\\LatexEditor\\src\\controller\\commands\\Command.java"),
                Paths.get(currentDirectory.toRealPath().normalize().toString(), "\\src\\test\\resources\\LatexEditor\\src\\controller\\commands\\CommandFactory.java"),
                Paths.get(currentDirectory.toRealPath().normalize().toString(), "\\src\\test\\resources\\LatexEditor\\src\\controller\\commands\\CreateCommand.java"),
                Paths.get(currentDirectory.toRealPath().normalize().toString(), "\\src\\test\\resources\\LatexEditor\\src\\controller\\commands\\EditCommand.java"),
                Paths.get(currentDirectory.toRealPath().normalize().toString(), "\\src\\test\\resources\\LatexEditor\\src\\controller\\commands\\LoadCommand.java"),
                Paths.get(currentDirectory.toRealPath().normalize().toString(), "\\src\\test\\resources\\LatexEditor\\src\\controller\\commands\\SaveCommand.java"),
                Paths.get(currentDirectory.toRealPath().normalize().toString(), "\\src\\test\\resources\\LatexEditor\\src\\controller\\commands\\ChangeVersionsStrategyCommand.java"),
                Paths.get(currentDirectory.toRealPath().normalize().toString(), "\\src\\test\\resources\\LatexEditor\\src\\controller\\commands\\DisableVersionsManagementCommand.java"),
                Paths.get(currentDirectory.toRealPath().normalize().toString(), "\\src\\test\\resources\\LatexEditor\\src\\controller\\commands\\EnableVersionsManagementCommand.java"),
                Paths.get(currentDirectory.toRealPath().normalize().toString(), "\\src\\test\\resources\\LatexEditor\\src\\controller\\commands\\RollbackToPreviousVersionCommand.java")
        ));
        Map<Path, PackageNode> packageNodes = parser.getPackageNodes();

        PackageNode controllerPackage = packageNodes.get(Paths.get(currentDirectory.toRealPath().normalize().toString(), "src\\test\\resources\\LatexEditor\\src\\controller"));
        List<Path> testingLeafNodes = new ArrayList<>();
        assertEquals(Paths.get(currentDirectory.toRealPath().normalize().toString(),
                "\\src\\test\\resources\\LatexEditor\\src\\controller"), controllerPackage.getNodesPath());
        assertEquals(Paths.get(currentDirectory.toRealPath().normalize().toString(),
                "\\src\\test\\resources\\LatexEditor\\src"), controllerPackage.getParentNode().getNodesPath());
        assertTrue(controllerPackage.isValid(), "message");
        Map<Path, PackageNode> subNodes = controllerPackage.getSubNodes();
        assertEquals(Paths.get(currentDirectory.toRealPath().normalize().toString(),
                        "\\src\\test\\resources\\LatexEditor\\src\\controller\\commands"),
                subNodes.get(Paths.get(currentDirectory.toRealPath().normalize().toString(),
                        "\\src\\test\\resources\\LatexEditor\\src\\controller\\commands")).getNodesPath());

        for (LeafNode l : controllerPackage.getLeafNodes().values()) {
            testingLeafNodes.add(l.getNodesPath());
            assertEquals(l.getParentNode().getNodesPath(), Paths.get(currentDirectory.toRealPath().normalize().toString(),
                    "\\src\\test\\resources\\LatexEditor\\src\\controller"));
        }
        Collections.sort(testingLeafNodes);
        Collections.sort(controllersLeafNodes);
        assertTrue(testingLeafNodes.size() == controllersLeafNodes.size()
                && controllersLeafNodes.containsAll(testingLeafNodes)
                && testingLeafNodes.containsAll(controllersLeafNodes));
        testingLeafNodes.clear();

        PackageNode commandsPackage = packageNodes.get(Paths.get(currentDirectory.toRealPath().normalize().toString(),
                "\\src\\test\\resources\\LatexEditor\\src\\controller\\commands"));
        assertEquals(Paths.get(currentDirectory.toRealPath().normalize().toString(),
                "\\src\\test\\resources\\LatexEditor\\src\\controller\\commands"), commandsPackage.getNodesPath());
        assertEquals(Paths.get(currentDirectory.toRealPath().normalize().toString(),
                "\\src\\test\\resources\\LatexEditor\\src\\controller"), commandsPackage.getParentNode().getNodesPath());
        assertTrue(commandsPackage.isValid());
        subNodes = commandsPackage.getSubNodes();
        assertEquals(0, subNodes.size());
        for (LeafNode l : commandsPackage.getLeafNodes().values()) {
            testingLeafNodes.add(l.getNodesPath());
            assertEquals(l.getParentNode().getNodesPath(), Paths.get(currentDirectory.toRealPath().normalize().toString(),
                    "\\src\\test\\resources\\LatexEditor\\src\\controller\\commands"));
        }
        Collections.sort(testingLeafNodes);
        Collections.sort(commandsLeafNodes);
        assertTrue(testingLeafNodes.size() == commandsLeafNodes.size()
                && commandsLeafNodes.containsAll(testingLeafNodes)
                && testingLeafNodes.containsAll(commandsLeafNodes));
        testingLeafNodes.clear();

        PackageNode modelPackage = packageNodes.get(Paths.get(currentDirectory.toRealPath().normalize().toString(),
                "\\src\\test\\resources\\LatexEditor\\src\\model"));
        assertEquals(Paths.get(currentDirectory.toRealPath().normalize().toString(),
                "\\src\\test\\resources\\LatexEditor\\src\\model"), modelPackage.getNodesPath());
        assertEquals(Paths.get(currentDirectory.toRealPath().normalize().toString(),
                "\\src\\test\\resources\\LatexEditor\\src"), modelPackage.getParentNode().getNodesPath());
        assertTrue(modelPackage.isValid());
        subNodes = modelPackage.getSubNodes();
        assertEquals(Paths.get(currentDirectory.toRealPath().normalize().toString(),
                        "\\src\\test\\resources\\LatexEditor\\src\\model\\strategies"),
                subNodes.get(Paths.get(currentDirectory.toRealPath().normalize().toString(),
                        "\\src\\test\\resources\\LatexEditor\\src\\model\\strategies")).getNodesPath());
        for (LeafNode l : modelPackage.getLeafNodes().values()) {
            assertEquals(l.getParentNode().getNodesPath(), Paths.get(currentDirectory.toRealPath().normalize().toString(),
                    "\\src\\test\\resources\\LatexEditor\\src\\model"));
            testingLeafNodes.add(l.getNodesPath());
        }
        Collections.sort(testingLeafNodes);
        Collections.sort(modelsLeafNodes);
        assertTrue(testingLeafNodes.size() == modelsLeafNodes.size()
                && modelsLeafNodes.containsAll(testingLeafNodes)
                && testingLeafNodes.containsAll(modelsLeafNodes));
        testingLeafNodes.clear();

        PackageNode strategiesPackage = packageNodes.get(Paths.get(currentDirectory.toRealPath().normalize().toString(),
                "\\src\\test\\resources\\LatexEditor\\src\\model\\strategies"));
        assertEquals(Paths.get(currentDirectory.toRealPath().normalize().toString(),
                "\\src\\test\\resources\\LatexEditor\\src\\model\\strategies"), strategiesPackage.getNodesPath());
        assertEquals(Paths.get(currentDirectory.toRealPath().normalize().toString(),
                "\\src\\test\\resources\\LatexEditor\\src\\model"), strategiesPackage.getParentNode().getNodesPath());
        assertTrue(strategiesPackage.isValid());
        subNodes = strategiesPackage.getSubNodes();
        assertEquals(0, subNodes.size());
        for (LeafNode l : strategiesPackage.getLeafNodes().values()) {
            assertEquals(l.getParentNode().getNodesPath(), Paths.get(currentDirectory.toRealPath().normalize().toString(),
                    "\\src\\test\\resources\\LatexEditor\\src\\model\\strategies"));
            testingLeafNodes.add(l.getNodesPath());
        }
        Collections.sort(testingLeafNodes);
        Collections.sort(strategiesLeafNodes);
        assertTrue(testingLeafNodes.size() == strategiesLeafNodes.size()
                && strategiesLeafNodes.containsAll(testingLeafNodes)
                && testingLeafNodes.containsAll(strategiesLeafNodes));
        testingLeafNodes.clear();

        PackageNode viewPackage = packageNodes.get(Paths.get(currentDirectory.toRealPath().normalize().toString(),
                "\\src\\test\\resources\\LatexEditor\\src\\view"));
        assertEquals(Paths.get(currentDirectory.toRealPath().normalize().toString(),
                "\\src\\test\\resources\\LatexEditor\\src\\view"), viewPackage.getNodesPath());
        assertEquals(Paths.get(currentDirectory.toRealPath().normalize().toString(),
                "\\src\\test\\resources\\LatexEditor\\src"), viewPackage.getParentNode().getNodesPath());
        assertTrue(viewPackage.isValid());
        subNodes = viewPackage.getSubNodes();
        assertEquals(0, subNodes.size());
        for (LeafNode l : viewPackage.getLeafNodes().values()) {
            assertEquals(l.getParentNode().getNodesPath(), Paths.get(currentDirectory.toRealPath().normalize().toString(),
                    "\\src\\test\\resources\\LatexEditor\\src\\view"));
            testingLeafNodes.add(l.getNodesPath());
        }
        Collections.sort(testingLeafNodes);
        Collections.sort(viewsLeafNodes);
        assertTrue(testingLeafNodes.size() == viewsLeafNodes.size()
                && viewsLeafNodes.containsAll(testingLeafNodes)
                && testingLeafNodes.containsAll(viewsLeafNodes));
        testingLeafNodes.clear();

        PackageNode sourcePackage = packageNodes.get(Paths.get(currentDirectory.toRealPath().normalize().toString(),
                "\\src\\test\\resources\\LatexEditor\\src"));
        assertEquals(Paths.get(currentDirectory.toRealPath().normalize().toString(),
                "\\src\\test\\resources\\LatexEditor\\src"), sourcePackage.getNodesPath());
        assertEquals(Paths.get(""), sourcePackage.getParentNode().getNodesPath());
        assertFalse(sourcePackage.isValid());
        subNodes = sourcePackage.getSubNodes();
        List<Path> testingSubPackages = new ArrayList<>();
        for (PackageNode subP : subNodes.values()) {
            testingSubPackages.add(subP.getNodesPath());
        }
        Collections.sort(testingSubPackages);
        Collections.sort(sourcesSubPackages);
        assertTrue(testingSubPackages.size() == sourcesSubPackages.size()
                && sourcesSubPackages.containsAll(testingSubPackages)
                && testingSubPackages.containsAll(sourcesSubPackages));
        assertEquals(0, sourcePackage.getLeafNodes().size());
    }
}
