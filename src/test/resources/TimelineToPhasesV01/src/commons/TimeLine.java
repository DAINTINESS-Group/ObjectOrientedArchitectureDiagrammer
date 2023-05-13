package commons;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
/**
* <h1>TimeLine</h1>
* Class responsible for handling the data
* regarding a single timeline.
*
* @version 1.0
* @since   2017-07-23
*/
public class TimeLine {
	List<ValuePair> values;
	
	public TimeLine(){
		values = new ArrayList<ValuePair>();
		
	}
	
	public List<ValuePair> getValues(){return values;}
	
	public int addValue(ValuePair v){
		if(Objects.nonNull(v)) {
			this.values.add(v);
			return this.values.size();
		}
		return -1;
	}
	
	/**
	 * Prints a report for all the values in the 
	 * specific timeline.
	 */
	public void consoleVerticalReport(){
		System.out.println("--- Time series data---");
		for (ValuePair v: values){
			System.out.println(v.getPos() + "\t" + v.getX()+ "\t"+v.getY());
		}
		System.out.println();
	}//end consloeVerticalReport
}

