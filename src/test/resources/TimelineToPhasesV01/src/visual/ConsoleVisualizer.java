package visual;

import commons.*;

/**
 * <h1>ConsoleVisualizer</h1>
 * Class responsible for visualizing a specific timeline in the console
 *
 * @version 1.0
 * @since 2017-07-23
 */
public final class ConsoleVisualizer extends AbstractRasterBasedVisualizer implements IVisualizer
{


    public ConsoleVisualizer(TimeLine tl)
    {
        super(tl);
        this.produceRaster(tl);
    }


    /**
     * @see visual.IVisualizer#visualize()
     */
    @Override
    public char[][] visualize()
    {

        System.out.println("\n\n--------------------NOW YOU SEE IT---------------");
        /*
         * column by column print x-axis print x-axis legend
         */
        for (int i = 0; i < numRows; i++)
        {
            System.out.println();
            for (int j = 0; j < numCols; j++)
            {
                System.out.printf("%2c", raster[i][j]);
            }

            // outputStream.println("\n</tr>");
        }
        System.out.println();
        return this.raster;
    }// end visualize

}
