package visual;

/**
 * <h1>IVisualizer</h1>
 * Interface which provides a contract to the clients regarding visualization
 *
 * @version 1.0
 * @since 2017-07-22
 */
public interface IVisualizer
{
    /**
     * Visualizes a specific timeline
     *
     * @return a 2D raster of char, describing the raster to be visualized
     */
    public abstract char[][] visualize();// end visualize
}