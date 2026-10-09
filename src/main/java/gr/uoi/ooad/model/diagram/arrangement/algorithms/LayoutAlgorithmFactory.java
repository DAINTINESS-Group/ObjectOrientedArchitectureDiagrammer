package gr.uoi.ooad.model.diagram.arrangement.algorithms;

public class LayoutAlgorithmFactory {

    public static LayoutAlgorithm createLayoutAlgorithm(LayoutAlgorithmType algorithmType) {
        return switch (algorithmType) {
            case SUGIYAMA -> new Sugiyama();
            case FRUCHTERMAN_REINGOLD -> new FruchtermanReingold();
            case ADVANCED_FRUCHTERMAN_REINGOLD -> new AdvancedFruchtermanReingold();
            case SPRING -> new Spring();
            case ADVANCED_SPRING -> new AdvancedSpring();
            case KAMADA_KAWAI -> new KamadaKawai();
        };
    }
}
