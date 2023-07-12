package model.diagram.arrangement.algorithms;

import java.util.HashMap;
import java.util.Map;

import org.javatuples.Pair;

import de.odysseus.ithaka.digraph.SimpleDigraph;
import de.odysseus.ithaka.digraph.SimpleDigraphAdapter;
import de.odysseus.ithaka.digraph.layout.DigraphLayout;
import de.odysseus.ithaka.digraph.layout.DigraphLayoutDimension;
import de.odysseus.ithaka.digraph.layout.DigraphLayoutDimensionProvider;
import de.odysseus.ithaka.digraph.layout.DigraphLayoutNode;
import de.odysseus.ithaka.digraph.layout.DigrpahLayoutBuilder;
import de.odysseus.ithaka.digraph.layout.sugiyama.SugiyamaBuilder;
import edu.uci.ics.jung.graph.Graph;

public class Sugiyama implements LayoutAlgorithm{
	
	private Graph<String, String> graph;
	private Map<String, Integer> verticesMap;
	private SimpleDigraph<Integer> digraph;

	
	public Sugiyama(Graph<String, String> graph) {
		this.graph = graph;
		verticesMap = new HashMap<>();
	}
	
	@Override
	public Map<String, Pair<Double, Double>> arrangeDiagram() {
		Map<String, Pair<Double, Double>> nodesGeometry = new HashMap<>();
		digraph = new SimpleDigraphAdapter<Integer>();
		fillVertexMap();
		fillNeighboursMap();
		DigraphLayoutDimensionProvider<Integer> dimensionProvider = new DigraphLayoutDimensionProvider<Integer>() {
			@Override
			public DigraphLayoutDimension getDimension(Integer node) {
				return new DigraphLayoutDimension(20, 20);
			}
		};
		DigrpahLayoutBuilder<Integer,Boolean> builder = new SugiyamaBuilder<Integer,Boolean>(125, 50);
		DigraphLayout<Integer,Boolean> layout = builder.build(digraph, dimensionProvider);
		for (DigraphLayoutNode<Integer> vertex : layout.getLayoutGraph().vertices()) {
			for (Map.Entry<String, Integer> entryVertex : verticesMap.entrySet()) {
	            if (entryVertex.getValue() == vertex.getVertex()) {
	            	double x = vertex.getPoint().x;
	            	double y = vertex.getPoint().y;
	            	if (vertex.getPoint().x < 25) {
	            		x = 25;
	            	}
	            	if (vertex.getPoint().y < 25) {
	            		y = 25;
	            	}
	            	nodesGeometry.put(entryVertex.getKey(), new Pair<>(x, y));
	                break; // Found the matching vertex, exit the loop
	            }
	        }
		}
        return nodesGeometry;
	}
	
	private void fillNeighboursMap() {
		for (String edge : graph.getEdges()){
        	String[] vertices = edge.split(" ");
        	digraph.add(verticesMap.get(vertices[0]), verticesMap.get(vertices[1]));
		}
	}
	
	private void fillVertexMap() {
		int counter = 1;
		for (String vertex : graph.getVertices()) {
			verticesMap.put(vertex, counter);
			counter += 1;
		}
	}

}
