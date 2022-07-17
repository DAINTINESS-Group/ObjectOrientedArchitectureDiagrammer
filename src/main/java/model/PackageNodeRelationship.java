package model;

public class PackageNodeRelationship {

    private final PackageNode startingPackageNode;
    private final PackageNode endingPackageNode;
    private final RelationshipType relationshipType;

    public PackageNodeRelationship(PackageNode startingPackageNode, PackageNode endingPackageNode, RelationshipType relationshipType) {
        this.startingPackageNode = startingPackageNode;
        this.endingPackageNode = endingPackageNode;
        this.relationshipType = relationshipType;
    }

    public PackageNode getStartingPackageNode() {
        return startingPackageNode;
    }

    public PackageNode getEndingPackageNode() {
        return endingPackageNode;
    }

    public RelationshipType getRelationshipType() {
        return relationshipType;
    }

}
