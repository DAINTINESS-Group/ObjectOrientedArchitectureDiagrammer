package parser.jdt;

import parser.tree.LeafNode;
import parser.tree.NodeType;

import java.nio.file.Path;

public class JDTLeafNode extends LeafNode {

    private String inheritanceLine[];

    public JDTLeafNode(Path path) {
        super(path);
    }

    /**This method is responsible for setting the nodes line that contains the declaration
     *  of the source file
     * @param inheritanceLine the Java source file's line holding the information regarding its inheritance
     */
    public void setInheritanceLine(String[] inheritanceLine) {
        this.inheritanceLine = inheritanceLine;
    }

    public String[] getInheritanceLine() {
        return inheritanceLine;
    }

    public String getName() {
        return path.normalize().toString().substring(path.normalize().toString().lastIndexOf("\\") + 1, path.normalize().toString().lastIndexOf("."));
    }

    public NodeType getType() {
        if (inheritanceLine[0].equals("enum")) {
            return NodeType.ENUM;
        }else if (inheritanceLine[0].equals("interface")) {
            return NodeType.INTERFACE;
        }else {
            return NodeType.CLASS;
        }
    }
}
