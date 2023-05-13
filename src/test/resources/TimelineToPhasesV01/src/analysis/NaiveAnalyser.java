package analysis;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import commons.*;
/**
* <h1>NaiveAnalyser</h1>
* Responsible for (a) the production of phases from a timeline
* and (b) the reporting to the console
*
* @version 1.0
* @since   2017-07-23
*/
public final class NaiveAnalyser implements IAnalyser {
	private List<Phase> phases;
	private List<String> phaseDescriptions;	
	public NaiveAnalyser(){
		phases = new ArrayList<Phase>(); 
		phaseDescriptions = new ArrayList<String>();
	}
	
	/** Produces the phases from a given timeline
	* 
	* @param tl timeline object
	* @return list of phases
	* @see analysis.IAnalyser#producePhasesFromTimeLine(commons.TimeLine)
	* 
	* 	//TODO (NOT YET)	ArrayList<String> resultText = new ArrayList<String>();
	*   //	ArrayList<Phase> phases = new ArrayList<Phase>();
	*/
	@Override
	public List<Phase> producePhasesFromTimeLine(TimeLine tl) { 
		List<ValuePair> values = tl.getValues();
		if (values == null) {
			System.out.println("NaiveAnalyser::producePhasesFromTimeLine errir: What to analyse in an empty value list? Oeo?");
			System.exit(-1);
		}

		Iterator<ValuePair> i = values.iterator();
		ValuePair v = i.next();
		Phase nextPhase = new Phase(v, "NoTrend");
		phases.add(nextPhase);
		while (i.hasNext()) {
			ValuePair nextV = i.next();
			Phase currentPhase = phases.get(phases.size() - 1); // get the last
																// phase
			if (currentPhase == null) {
				System.out.println("null Phase? Oeo?");
				System.exit(-1);
			}
			if (currentPhase.checkIfAppendable(nextV) == true) {
				currentPhase.appendValuePair(nextV);
			} else {
				/*
				 * DONE: isolated the small spikes as different phases. 
				 * Have fixed the following problem:
			------------ New Phase: Up ---------------
			3	3.0	12.0
			4	4.0	16.0
			------------ New Phase: Up ---------------
			5	5.0	15.0
			6	6.0	17.0
				 * The series falls from 16 to 15. However, what happened is that when 15 came, the "Up" phase rejected it and it became a new phase with "No Trend" value;then 17 came and the trend is "Up"
				 * IMHO, 15 should have been a new phase with trend "Down" and 17 a new phase with trend "Up" :)
				 * Interestingly, if instead of 17 we had a 13, then it would have continued with a nice "Down"
				 * Q: How to fix this? 
				 * A: added a decision method decideNewTrend() @ �hase that decides what the new trend should be.
				 */

				String newTrend = currentPhase.decideNewTrend(nextV);
				Phase newPhase = new Phase(nextV, newTrend);
				phases.add(newPhase);
			}
		}// end while next value
		return phases;
	} //end producePhasesFrom TimeLine
	

	/**
	 * Reports the phases to the console
	 *
	 * @see analysis.IAnalyser#reportToConsole()
	 */
	@Override
	public List<String> reportToConsole(){
		for (Phase p: phases){
			String s = p.consoleVerticalReport();
			phaseDescriptions.add(s);
			System.out.println(s);
		}
		
		return phaseDescriptions;
	}//end reportToConsole
}//end class
