package gr.uoi.smartgraph.graphview.element;

import com.brunomnsilva.smartgraph.graphview.SmartLabelSource;
import gr.uoi.ooad.model.graph.ArcType;
import java.util.Objects;

public class UMLEdgeElement {

    private String sourceVertexName;
    private String targetVertexName;
    private ArcType arcType;

    public UMLEdgeElement(String sourceVertexName, String targetVertexName, ArcType arcType) {
        this.sourceVertexName = sourceVertexName;
        this.targetVertexName = targetVertexName;
        this.arcType = arcType;
    }

    @Override
    public String toString() {
        return sourceVertexName + "_" + targetVertexName + "_" + arcType;
    }

    @SmartLabelSource
    public String info() {
        return toString();
    }

    public String getSourceVertexName() {
        return sourceVertexName;
    }

    public String getTargetVertexName() {
        return targetVertexName;
    }

    public ArcType getArcType() {
        return arcType;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UMLEdgeElement that = (UMLEdgeElement) o;
        return Objects.equals(sourceVertexName, that.sourceVertexName)
                && Objects.equals(targetVertexName, that.targetVertexName)
                && arcType == that.arcType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(sourceVertexName, targetVertexName, arcType);
    }
}
