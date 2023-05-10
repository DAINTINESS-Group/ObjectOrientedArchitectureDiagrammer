package parsing;

import commons.*;
/**
* <h1>Phase</h1>
* Responsible for handling the information
* about a phase
*
* @version 1.0
* @since   2017-07-23
*/
public final class TestParser implements IParser {

		/**
		* Reads the given input file and populates a Timeline
		* object with data.
		* @see parsing.IParser#parse(java.lang.String)
		*
		* @param fileName name of the input file
		* @return Timeline object loaded with the data from the file
		*/
		@Override
		public TimeLine parse(String fileName){
			System.out.println("\nNot using " + fileName + " for the moment:)");
			TimeLine tl = new TimeLine();
			
			//In this example, I treat X-value as Position value. However, it could be sth else.
			//TODO: reconsider, whether X-value should be String instead. Or sth else. Or sth generic.
			//e.g., it can be a date, so you have for position : 1,2,3, ... and for X-values: jan'13, Feb'13, ...
			ValuePair v = new ValuePair(1, 1, 10); tl.addValue(v);
			v = new ValuePair(2, 2, 10); tl.addValue(v);
			v = new ValuePair(3, 3, 12); tl.addValue(v);
			v = new ValuePair(4, 4, 16); tl.addValue(v);
			v = new ValuePair(5, 5, 15); tl.addValue(v);
			v = new ValuePair(6, 6, 17); tl.addValue(v);
			v = new ValuePair(7, 7, 23); tl.addValue(v);
			v = new ValuePair(7, 7, 14); tl.addValue(v);
			v = new ValuePair(8, 8, 12); tl.addValue(v);
			v = new ValuePair(9, 9, 13); tl.addValue(v);
			v = new ValuePair(10, 10, 11); tl.addValue(v);
			v = new ValuePair(11, 11, 11); tl.addValue(v);
			v = new ValuePair(12, 12, 9); tl.addValue(v);
			
			return tl;
		}
}
