package parser.tree;

public enum RelationshipType
{
    DEPENDENCY,
    AGGREGATION,
    ASSOCIATION,
    EXTENSION,
    IMPLEMENTATION;


    @Override
    public String toString()
    {
        return super.toString().toLowerCase();
    }
}
