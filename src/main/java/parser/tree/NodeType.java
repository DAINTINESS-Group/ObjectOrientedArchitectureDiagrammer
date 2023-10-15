package parser.tree;

public enum NodeType {
	CLASS,
	INTERFACE,
	ENUM,
	PACKAGE;

	public String toString() {
		return super.toString().toLowerCase();
	}
}
