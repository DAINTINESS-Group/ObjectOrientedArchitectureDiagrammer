package parser.tree;

public enum RelationshipType {
	DEPENDENCY,
	AGGREGATION,
	ASSOCIATION,
	EXTENSION,
	IMPLEMENTATION;

	public String toString() {
		return super.toString().toLowerCase();
	}
}
