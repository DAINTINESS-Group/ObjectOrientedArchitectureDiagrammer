package parser.javaparser;

import parser.tree.node.LeafNode;
import parser.tree.node.NodeType;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JavaparserLeafNode extends LeafNode {

    private final List<String> implementedInterfaces;
    private final Map<String, String> variables;
    private final List<String> createdObjects;
    private String nodeName;
    private String baseClass;
    private NodeType nodeType;

    public JavaparserLeafNode(Path path) {
        super(path);
        implementedInterfaces = new ArrayList<>();
        variables = new HashMap<>();
        createdObjects = new ArrayList<>();
        baseClass = "";
        nodeName = "";
    }

    public void addVariable(String variableName, String variableType) {
        variables.put(variableName, variableType);
    }

    public void setNodeType(NodeType nodeType) {
        this.nodeType = nodeType;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public void setBaseClass(String baseClass) {
        this.baseClass = baseClass;
    }

    public void addImplementedInterface(String interfaceName) {
        implementedInterfaces.add(interfaceName);
    }

    public void addCreatedObject(String createdObject) {
        createdObjects.add(createdObject);
    }

    public List<String> getVariablesTypes() {
        return new ArrayList<>(variables.values());
    }

    public String getBaseClass() {
        return baseClass;
    }

    public List<String> getCreatedObjects() {
        return createdObjects;
    }

    public List<String> getImplementedInterfaces() {
        return implementedInterfaces;
    }

    @Override
    public String getName() {
        return nodeName;
    }

    @Override
    public NodeType getType() {
        return nodeType;
    }

}