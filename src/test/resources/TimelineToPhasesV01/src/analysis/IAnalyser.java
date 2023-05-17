package analysis;

import java.util.List;

import commons.TimeLine;
/**
* <h1>IAnalyser</h1>
* Interface which provides a contract to the clients
* regarding (a) the production of phases from a timeline
* and (b) the reporting to the console
*
* @version 1.0
* @since   2017-07-22
*/
public interface IAnalyser {
	
	/**
	* 
	* Produces the phases from a given timeline
	* 
	* @param tl timeline object
	* @return list of phases
	*/
	public abstract List<Phase> producePhasesFromTimeLine(TimeLine tl); //end producePhasesFrom TimeLine

	/**
	 * Reports the phases to the console
	 * 
	 * @return an ArraList of Strings, one String per phase
	 */
	public abstract List<String> reportToConsole();//end debugConsole

}