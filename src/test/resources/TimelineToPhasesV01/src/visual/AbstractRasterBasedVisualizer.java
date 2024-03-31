package visual;

import java.util.List;

import commons.TimeLine;
import commons.ValuePair;

public class AbstractRasterBasedVisualizer
{

    private double          rowInterval = 2;
    private double          minY        = 0;
    private double          maxY        = 25;
    private List<ValuePair> values;

    protected char     raster[][];
    protected int      numCols = 0;
    protected int      numRows = 0;
    protected TimeLine workingTimeline;


    public AbstractRasterBasedVisualizer(TimeLine tl)
    {
        this.workingTimeline = tl;
    }


    protected char[][] produceRaster(TimeLine tl)
    {
        //TODO: automatically compute mixY, maxY and rowInterval
        numCols = tl.getValues().size() + 2;
        numRows = (int)((maxY - minY) / rowInterval) + 3;
        raster = new char[numRows][numCols];
        //DEBUG: System.out.println(numRows + " " + numCols);
        values = tl.getValues();
        for (int i = 0; i < numRows; i++)
        {
            for (int j = 0; j < numCols; j++)
            {raster[i][j] = ' ';}
        }

        for (ValuePair v : values)
        {
            int col = v.getPos() + 1;
            int row = (int)(((int)(maxY - v.getY())) / rowInterval);
            if ((row < 0) || (row >= numRows))
            {
                System.out.println("kapoia saxlamara egine" + col + " " + row); System.exit(-10);
            }
            raster[row][col] = '@';
        }
        for (int i = 0; i < numRows; i++)
        {raster[i][1] = '|';}
        for (int i = 0; i < numCols; i++)
        {raster[numRows - 2][i] = '~';}

        if (numRows > 5)
        {
            raster[0][0] = 'v';
            raster[1][0] = 'a';
            raster[2][0] = 'l';
            raster[3][0] = 'u';
            raster[4][0] = 'e';
        }
        if (numCols > 4)
        {
            raster[numRows - 1][numCols - 4] = 'T';
            raster[numRows - 1][numCols - 3] = 'i';
            raster[numRows - 1][numCols - 2] = 'm';
            raster[numRows - 1][numCols - 1] = 'e';
        }
        return raster;
    }

}//end class