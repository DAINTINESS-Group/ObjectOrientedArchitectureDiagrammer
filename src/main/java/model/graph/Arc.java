package model.graph;

public record Arc<T>(T       sourceVertex,
                     T       targetVertex,
                     ArcType arcType) {}