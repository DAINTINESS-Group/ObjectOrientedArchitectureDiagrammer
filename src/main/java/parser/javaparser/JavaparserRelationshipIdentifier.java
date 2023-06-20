package parser.javaparser;

import parser.tree.PackageNode;
import parser.tree.RelationshipIdentifier;
import parser.tree.RelationshipType;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class JavaparserRelationshipIdentifier extends RelationshipIdentifier {

    public JavaparserRelationshipIdentifier(Map<Path, PackageNode> packageNodes) {
        super(packageNodes);
    }

    @Override
    protected void checkRelationship(int i, int j) {
        List<String> imports = ((JavaparserLeafNode) allLeafNodes.get(i)).getImports();
        Optional<String> optional = imports.stream().filter(imprt ->
            (allLeafNodes.get(j).getParentNode().getName() + "." + allLeafNodes.get(j).getName()).endsWith(imprt) ||
            (allLeafNodes.get(j).getParentNode().getName() + "." + "*") .endsWith(imprt))
        .findFirst();
        if (optional.isEmpty() && !isSubNode(i, j)) {
            return;
        }

        if (isDependency(i, j)) {
            createRelationship(i, j, RelationshipType.DEPENDENCY);
        }
        if (isAggregation(i, j)) {
            createRelationship(i, j, RelationshipType.AGGREGATION);
        }else if (isAssociation(i, j)) {
            createRelationship(i, j, RelationshipType.ASSOCIATION);
        }

        if (isExtension(i, j)) {
            createRelationship(i, j, RelationshipType.EXTENSION);
        }
        if (isImplementation(i, j)) {
            createRelationship(i, j, RelationshipType.IMPLEMENTATION);
        }
    }

    private boolean isSubNode(int i, int j) {
        PackageNode node = allLeafNodes.get(j).getParentNode();
        while (true) {
            if (node.equals(allLeafNodes.get(i).getParentNode())) {
                return true;
            }

            if (node.getPackageNodesPath().toString().equals("")) {
                return false;
            }
            node = node.getParentNode();
        }
    }

    @Override
    protected boolean isDependency(int i, int j) {
        return doesRelationshipExist(allLeafNodes.get(i).getMethodParameterTypes(), allLeafNodes.get(j).getName()) ||
                doesRelationshipExist(allLeafNodes.get(i).getMethodsReturnTypes(), allLeafNodes.get(j).getName()) ||
                doesRelationshipExist(getLeafNode(i).getVariablesTypes(), allLeafNodes.get(j).getName()) ||
                doesRelationshipExist(getLeafNode(i).getCreatedObjects(), allLeafNodes.get(j).getName());
    }

    @Override
    protected boolean isExtension(int i, int j) {
        if (getLeafNode(i).getBaseClass().isEmpty()) {
            return false;
        }
        return getLeafNode(i).getBaseClass().equals(allLeafNodes.get(j).getName());
    }

    @Override
    protected boolean isImplementation(int i, int j) {
        if (getLeafNode(i).getImplementedInterfaces().size() == 0) {
            return false;
        }

        for (String implementedInterface: getLeafNode(i).getImplementedInterfaces()) {
            if (implementedInterface.equals(allLeafNodes.get(j).getName())) {
                return true;
            }
        }
        return false;
    }

    private JavaparserLeafNode getLeafNode(int i) {
        return (JavaparserLeafNode) allLeafNodes.get(i);
    }
}
