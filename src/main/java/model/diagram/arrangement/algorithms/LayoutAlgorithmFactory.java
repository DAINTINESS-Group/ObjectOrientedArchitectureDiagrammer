package model.diagram.arrangement.algorithms;

public class LayoutAlgorithmFactory {

	public LayoutAlgorithm createLayoutAlgorithm(LayoutAlgorithmType algorithmType) {
		if (algorithmType == LayoutAlgorithmType.SUGIYAMA) {
			return new Sugiyama();
		}else if (algorithmType == LayoutAlgorithmType.FRUCHTERMAN_REINGOLD) {
			return new FruchtermanReingold();
		}else if(algorithmType == LayoutAlgorithmType.ADVANCED_FRUCHTERMAN_REINGOLD){
			return new AdvancedFruchtermanReingold();
		}else if(algorithmType == LayoutAlgorithmType.SPRING) {
			return new Spring();
		}else if(algorithmType == LayoutAlgorithmType.ADVANCED_SPRING) {
			return new AdvancedSpring();
		}else if(algorithmType == LayoutAlgorithmType.KAMADA_KAWAI){
			return new KamadaKawai();
		}else {
			throw new RuntimeException();
		}
	}

}
