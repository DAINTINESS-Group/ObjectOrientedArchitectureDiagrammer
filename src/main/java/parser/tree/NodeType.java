package parser.tree;

public enum NodeType {
    CLASS,
    INTERFACE,
    ENUM,
    PACKAGE;

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }
}
