package commons;
/**
* <h1>ValuePair</h1>
* Helper Class which in fact is data
* structure that holds all the information
* for a point in a time series
*
* @version 1.0
* @since   2017-07-22
*/
public class ValuePair {
	private int pos = -1;
	private double xValue = -1.0;
	private double yValue = -1.0;
	
	public ValuePair(int aPos, double aX, double aY){
		pos = aPos; xValue = aX; yValue = aY;
	}
	public int getPos(){return pos;}
	public double getX(){return xValue;}
	public double getY(){return yValue;}
}
