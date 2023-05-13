package parsing;

import commons.TimeLine;
/**
* <h1>IParser</h1>
* Interface which provides a contract to the clients
* regarding the parsing of the input files.
*
* @version 1.0
* @since   2017-07-23
*/
public interface IParser {
	/**
	* Reads the given input file and populates a Timeline
	* object with data.
	*
	* @param fileName name of the input file
	* @return Timeline object loaded with the data from the file
	*/
	public abstract TimeLine parse(String fileName);

}