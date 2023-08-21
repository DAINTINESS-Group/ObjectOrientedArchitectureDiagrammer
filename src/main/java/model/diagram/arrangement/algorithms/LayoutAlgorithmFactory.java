package model.diagram.arrangement.algorithms;

public class LayoutAlgorithmFactory {

	public LayoutAlgorithm createLayoutAlgorithm(String algorithmType) {
		if (LayoutAlgorithmType.valueOf(algorithmType.toUpperCase()).equals(LayoutAlgorithmType.SUGIYAMA)) {
            return new Sugiyama();
        }else if (LayoutAlgorithmType.valueOf(algorithmType.toUpperCase()).equals(LayoutAlgorithmType.FRUCHTERMAN_REINGOLD)) {
            return new FruchtermanReingold();
        }else if(LayoutAlgorithmType.valueOf(algorithmType.toUpperCase()).equals(LayoutAlgorithmType.ADVANCED_FRUCHTERMAN_REINGOLD)){
            return new AdvancedFruchtermanReingold();
        }else if(LayoutAlgorithmType.valueOf(algorithmType.toUpperCase()).equals(LayoutAlgorithmType.SPRING)) {
            return new Spring();
        }else if(LayoutAlgorithmType.valueOf(algorithmType.toUpperCase()).equals(LayoutAlgorithmType.ADVANCED_SPRING)) {
            return new AdvancedSpring();
        }else if(LayoutAlgorithmType.valueOf(algorithmType.toUpperCase()).equals(LayoutAlgorithmType.KAMADA_KAWAI)){
        	return new KamadaKawai();
		}else {
            throw new RuntimeException();
        }
	}
	
}
