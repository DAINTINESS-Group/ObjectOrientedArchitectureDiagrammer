package model.graph;

public enum VertexType {
	CLASS("class"),
	INTERFACE("interface"),
	ENUM("enum"),
	PACKAGE("package");

	private final String type;

	VertexType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}
}
