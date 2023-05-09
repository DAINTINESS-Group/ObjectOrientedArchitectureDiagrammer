package parser;

import model.tree.*;

import java.io.File;
import java.nio.file.Path;
import java.util.Map;

public class ProjectParserHelper {

    private final ParserType parserType;

    public ProjectParserHelper(ParserType parserType) {
        this.parserType = parserType;
    }

    public LeafNode createLeafNode(Path path) {
        if (parserType.equals(ParserType.JDT)) {
            return new JDTLeafNode(path);
        }else {
            return new JavaparserLeafNode(path);
        }
    }

    public void createRelationshipIdentifier(Map<Path, PackageNode> packageNodes) {
        if (parserType.equals(ParserType.JDT)) {
            new JDTRelationshipIdentifier(packageNodes);
        }else {
            new JavaparserRelationshipIdentifier(packageNodes);
        }
    }

    public void createFileVisitor(File file, LeafNode leafNode) {
        if (parserType.equals(ParserType.JDT)) {
            new JDTFileVisitor(file, (JDTLeafNode) leafNode);
        }else {
            new JavaparserFileVisitor(file, (JavaparserLeafNode) leafNode);
        }
    }
}
