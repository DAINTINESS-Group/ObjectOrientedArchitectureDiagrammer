package model.tree;

import java.nio.file.Path;
import java.util.Map;

public class JDTRelationshipIdentifier extends RelationshipIdentifier{

    public static final int DECLARATION_LINE_STANDARD_SIZE = 2;
    public static final int INHERITANCE_TYPE = 2;
    public static final int SUPERCLASS_NAME = 3;
    public static final int MULTIPLE_IMPLEMENTATIONS = 5;
    public static final int INHERITANCE_TYPE_WITH_MULTIPLE_IMPLEMENTATIONS = 4;

    public JDTRelationshipIdentifier(Map<Path, PackageNode> packageNodes) {
        super(packageNodes);
    }

    @Override
    protected void checkRelationship(int i, int j) {
        if (isDependency(i, j)) {
            createRelationship(i, j, RelationshipType.DEPENDENCY);
        }
        if (isAggregation(i, j)) {
            createRelationship(i, j, RelationshipType.AGGREGATION);
        }else if (isAssociation(i, j)) {
            createRelationship(i, j, RelationshipType.ASSOCIATION);
        }
        if (isInheritance(i)) {
            if (isExtension(i, j)) {
                createRelationship(i, j, RelationshipType.EXTENSION);
            }
            if (isImplementation(i, j)) {
                createRelationship(i, j, RelationshipType.IMPLEMENTATION);
            }
        }
    }

    @Override
    protected boolean isDependency(int i, int j) {
        return doesRelationshipExist(allLeafNodes.get(i).getMethodParameterTypes(), allLeafNodes.get(j).getName()) ||
                doesRelationshipExist(allLeafNodes.get(i).getMethodsReturnTypes(), allLeafNodes.get(j).getName());
    }

    @Override
    protected boolean isExtension(int i, int j) {
        if ( getNodesInheritanceLine(i)[INHERITANCE_TYPE].equals("extends") ) {
            for (int k = 0; k < allLeafNodes.size(); k++) {
                if (getNodesInheritanceLine(i)[SUPERCLASS_NAME].equals(allLeafNodes.get(j).getName())) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    protected boolean isImplementation(int i, int j) {
        if (getNodesInheritanceLine(i)[INHERITANCE_TYPE].equals("implements") ) {
            for (int l = 3; l < getNodesInheritanceLine(i).length; l++) {
                if (getNodesInheritanceLine(i)[l].equals(allLeafNodes.get(j).getName())) {
                    return true;
                }
            }
        }else if (getNodesInheritanceLine(i).length > MULTIPLE_IMPLEMENTATIONS &&
                getNodesInheritanceLine(i)[INHERITANCE_TYPE_WITH_MULTIPLE_IMPLEMENTATIONS].equals("implements")) {
            for (int l = 5; l < getNodesInheritanceLine(i).length; l++) {
                if (getNodesInheritanceLine(i)[l].equals(allLeafNodes.get(j).getName())) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isInheritance(int i) {
        return getNodesInheritanceLine(i).length > DECLARATION_LINE_STANDARD_SIZE;
    }

    private String[] getNodesInheritanceLine(int i) {
        return getLeafNode(i).getInheritanceLine();
    }

    private JDTLeafNode getLeafNode(int i) {
        return (JDTLeafNode) allLeafNodes.get(i);
    }
}
