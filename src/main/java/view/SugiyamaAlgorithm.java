package view;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.brunomnsilva.smartgraph.graph.Edge;
import com.brunomnsilva.smartgraph.graph.Vertex;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;

import de.odysseus.ithaka.digraph.SimpleDigraph;
import de.odysseus.ithaka.digraph.SimpleDigraphAdapter;
import de.odysseus.ithaka.digraph.layout.DigraphLayout;
import de.odysseus.ithaka.digraph.layout.DigraphLayoutDimension;
import de.odysseus.ithaka.digraph.layout.DigraphLayoutDimensionProvider;
import de.odysseus.ithaka.digraph.layout.DigraphLayoutNode;
import de.odysseus.ithaka.digraph.layout.DigrpahLayoutBuilder;
import de.odysseus.ithaka.digraph.layout.sugiyama.SugiyamaBuilder;

public class SugiyamaAlgorithm {

	private SmartGraphPanel<String, String> graphView;
	private Collection<Edge<String, String>> edgeCollection;
	private Collection<Vertex<String>> vertexCollection;
	private Map<Vertex<String>, Integer> vertexIntMap = new HashMap<>();
	private SimpleDigraph<Integer> digraph;
	private static final int WIDTH = 914;
    private static final int HEIGHT = 637;

	public SugiyamaAlgorithm(SmartGraphPanel<String, String> graphView, Collection<Edge<String, String>> edgeCollection, Collection<Vertex<String>> vertexCollection) {
		this.graphView = graphView;
		this.edgeCollection = edgeCollection;
		this.vertexCollection = vertexCollection;
	}
	
	public SmartGraphPanel<String, String> getUpdatedGraph(){
		digraph = new SimpleDigraphAdapter<Integer>();
		fillVertexMap();
		fillNeighboursMap();
		DigraphLayoutDimensionProvider<Integer> dimensionProvider = new DigraphLayoutDimensionProvider<Integer>() {
			@Override
			public DigraphLayoutDimension getDimension(Integer node) {
				return new DigraphLayoutDimension(20, 20);
			}
		};
		DigrpahLayoutBuilder<Integer,Boolean> builder = new SugiyamaBuilder<Integer,Boolean>(75, 75);
		DigraphLayout<Integer,Boolean> layout = builder.build(digraph, dimensionProvider);
		
		for (DigraphLayoutNode<Integer> vertex : layout.getLayoutGraph().vertices()) {
			System.out.println(vertex.getVertex() + " --> " + vertex.getPoint());
			for (Map.Entry<Vertex<String>, Integer> entry : vertexIntMap.entrySet()) {
	            if (entry.getValue() == vertex.getVertex()) {
	            	graphView.setVertexPosition(entry.getKey(),  vertex.getPoint().x, vertex.getPoint().y);
	                break; // Found the matching vertex, exit the loop
	            }
	        }
		}
        return graphView;
    }
	
	private void fillNeighboursMap() {
		for (Edge<String, String> i : edgeCollection){
        	Vertex<String>[] connectedNodes = i.vertices();
        	digraph.add(vertexIntMap.get(connectedNodes[0]), vertexIntMap.get(connectedNodes[1]));
		}
	}
	
	private void fillVertexMap() {
		int counter = 1;
		for (Vertex<String> vertex : vertexCollection) {
			vertexIntMap.put(vertex, counter);
			counter += 1;
		}
	}

}