package gr.uoi.smartgraph.graphview.arrow;

import com.brunomnsilva.smartgraph.graphview.SmartArrow;

public class SmartArrowFactory {

    public SmartArrow createInstance() {
        /*
            private void initNodes() {
            for(Vertex<V> vertex : this.listOfVertices()) {
                SmartGraphVertexRectangleNode<V> vertexAnchor = new SmartGraphVertexRectangleNode(vertex, (double)0.0F, (double)0.0F, this.graphProperties.getVertexRadius(), this.graphProperties.getVertexAllowUserMove(), this.graphProperties.getVertexHeight());
                this.vertexNodes.put(vertex, vertexAnchor);
            }

            for(Vertex<V> vertex : this.vertexNodes.keySet()) {
                SmartGraphVertexRectangleNode<V> v = (SmartGraphVertexRectangleNode)this.vertexNodes.get(vertex);
                this.addVertex(v);
            }

            List<Edge<E, V>> edgesToPlace = this.listOfEdges();

            for(Vertex<V> vertex : this.vertexNodes.keySet()) {
                for(Edge<E, V> edge : this.theGraph.incidentEdges(vertex)) {
                    if (edgesToPlace.contains(edge)) {
                        Vertex<V> oppositeVertex = this.theGraph.opposite(vertex, edge);
                        SmartGraphVertexRectangleNode<V> graphVertexIn = (SmartGraphVertexRectangleNode)this.vertexNodes.get(vertex);
                        SmartGraphVertexRectangleNode<V> graphVertexOppositeOut = (SmartGraphVertexRectangleNode)this.vertexNodes.get(oppositeVertex);
                        graphVertexIn.addAdjacentVertex(graphVertexOppositeOut);
                        graphVertexOppositeOut.addAdjacentVertex(graphVertexIn);
                        SmartGraphEdgeBase graphEdge = this.createEdge(edge, graphVertexIn, graphVertexOppositeOut);
                        if (edge.element().toString().split("_")[2].equals("association") || edge.element().toString().split("_")[2].equals("extension") || edge.element().toString().split("_")[2].equals("aggregation")) {
                            graphEdge.setStyleClass("edgeNonDashed");
                        }

                        this.connections.put(edge, new Tuple(vertex, oppositeVertex));
                        this.addEdge(graphEdge, edge);
                        if (this.edgesWithArrows) {
                            SmartArrowBase arrow;
                            if (!edge.element().toString().split("_")[2].equals("implementation") && !edge.element().toString().split("_")[2].equals("extension")) {
                                if (edge.element().toString().split("_")[2].equals("aggregation")) {
                                    arrow = new SmartRhomboidArrow(this.graphProperties.getEdgeArrowSize());
                                    arrow.setStyleClass("arrowClosed");
                                } else {
                                    arrow = new SmartOpenArrow(this.graphProperties.getEdgeArrowSize());
                                }
                            } else {
                                arrow = new SmartClosedArrow(this.graphProperties.getEdgeArrowSize());
                                arrow.setStyleClass("arrowClosed");
                            }

                            graphEdge.attachArrow(arrow);
                            this.getChildren().add(arrow);
                        }

                        edgesToPlace.remove(edge);
                    }
                }
            }

        }

             */
        return null;
    }
}
