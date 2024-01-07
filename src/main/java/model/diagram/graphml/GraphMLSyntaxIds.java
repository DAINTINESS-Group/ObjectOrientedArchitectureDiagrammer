package model.diagram.graphml;

public enum GraphMLSyntaxIds
{
    NODE_ID             (0),
    NODE_COLOR          (1),
    NODE_NAME           (2),
    NODE_FIELDS         (3),
    NODE_METHODS        (4),
    NODE_X_COORDINATE   (5),
    NODE_Y_COORDINATE   (6),
    EDGE_ID             (0),
    EDGE_SOURCE         (1),
    EDGE_TARGET         (2),
    EDGE_TYPE           (3),
    EDGES_SOURCE_TYPE   (4),
    EDGES_TARGET_TYPE   (5),
    PACKAGE_X_COORDINATE(2),
    PACKAGE_Y_COORDINATE(3),
    PACKAGE_NAME        (1);

    private final int id;


    GraphMLSyntaxIds(int id)
    {
        this.id = id;
    }


    public int getId()
    {
        return id;
    }
}
