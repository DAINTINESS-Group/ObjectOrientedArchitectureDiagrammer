package model.graph;

public class Arc<T> {

	private final T sourceVertex;
	private final T targetVertex;
	private final ArcType arcType;

	public Arc(T sourceVertex, T targetVertex, ArcType arcType) {
		this.sourceVertex = sourceVertex;
		this.targetVertex = targetVertex;
		this.arcType = arcType;
	}

	public T getSourceVertex() {
		return sourceVertex;
	}

	public T getTargetVertex() {
		return targetVertex;
	}

	public ArcType getArcType() {
		return arcType;
	}
}
