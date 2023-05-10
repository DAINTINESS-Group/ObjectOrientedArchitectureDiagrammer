package engine;

import java.util.List;

/**
* <h1>IEngine</h1>
* Interface which provides the basic use cases
* of the system: (a) timeline population, (b) phase
* production, (d) reports and visualizations.
*
* @version 1.0
* @since   2017-07-22
*/
public interface IEngine {

	/**
	 * Populates a timeline objects by reading the 
	 * data from a given input file
	 * 
	 * @param filename file name of the input file
	 * @return an int with the size of the produced timeline or -1 if sth went wrong
	 */
	public abstract int setTimeLine(String filename);
	
	/**
	 * Produces the phases from the timeline
	 * @return an int with the number of pases
	 */
	public abstract int producePhases();
	
	/**
	 * Reports the phases to the console
	 * 
	 * @return an ArrayList of String describing the phases with one String per Phase
	 */
	public abstract List<String> reportPhases();
	
	/**
	 * Prints a report for all the values in the 
	 * specific timeline.
	 */
	public abstract void consoleVerticalReport();
	
	/**
	 * Initializes the type of visualizer that is needed.
	 * 
	 * @param visualizerType type of visualizer 
	 */
	public abstract void setVisualizer(String visualizerType);
	
	/**
	 * Visualizes the results in the console or in a HTML
	 * file format, depending on the type of visualizer that
	 * is set up.
	 * @return a 2d matrix of chars with the raster to be depicted
	 */
	public abstract char[][] visualize();
}
