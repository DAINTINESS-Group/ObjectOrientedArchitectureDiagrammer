package model.graph;

import java.util.Objects;

public record Arc<T>(T sourceVertex, T targetVertex, ArcType arcType) {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Arc<?> arc = (Arc<?>) o;
        return Objects.equals(sourceVertex, arc.sourceVertex)
                && Objects.equals(targetVertex, arc.targetVertex)
                && arcType == arc.arcType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(sourceVertex, targetVertex, arcType);
    }
}
