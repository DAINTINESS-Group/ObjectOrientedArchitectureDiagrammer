package analysis;

import java.util.ArrayList;
import java.util.List;

import commons.*;

/**
 * <h1>Phase</h1>
 * Responsible for handling the information about a phase
 *
 * @version 1.0
 * @since 2017-07-23
 */
public class Phase
{
    private int             startPos;
    private int             endPos;
    private List<ValuePair> values;
    private String          trend;
    private double          endY;

    /*
     * TODO Add: startX, startY, endX, steepness ((Ye-Ys)/(Xe-Xs)), duration(endPos-startPos), ...
     * TODO Add: a method that computes stats for the phases
     */
	
	/* Dangerous! Can cause only trouble. For the moment: start a phase with a concrete value, else, don't start at all!
	public Phase(){
		startPos = -1; endPos = -1;
		values = new ArrayList<ValuePair>();
		trend = "NoTrend";
	}
	*/


    /*
     * TODO: check what you get as input for the first value pair of the phase
     */
    public Phase(ValuePair v, String aTrend)
    {
        startPos = v.getPos(); endPos = v.getPos();
        values = new ArrayList<ValuePair>();
        values.add(v);
        trend = aTrend;
        endY = v.getY();
    }


    public Boolean checkIfAppendable(ValuePair v)
    {
        Boolean valueFits = false;

        //if there is just one valuePair inside
        if (trend.equals("NoTrend"))
        {
            valueFits = true;
            if (v.getY() == endY)
                trend = "Flat";
            else if (v.getY() > endY)
                trend = "Up";
            else if (v.getY() < endY)
                trend = "Down";
        }

        if (trend.equals("Flat"))
        {
            if (v.getY() == endY)
                valueFits = true;
            else
                valueFits = false;
        }

        //ATTN: we treat equals as parts of the same phase, that's why "<=" and not "<"
        if (trend.equals("Down"))
        {
            if (v.getY() <= endY)
                valueFits = true;
            else
                valueFits = false;
        }

        if (trend.equals("Up"))
        {
            if (v.getY() >= endY)
                valueFits = true;
            else
                valueFits = false;
        }

        return valueFits;
    }


    /**
     * PRE-CONDITION: use ONLY if checkIfAppendable() == false
     *
     * @param v the next ValuePair to be checked, on whether a change of Trend happens
     * @return a String reporting on the trend is returned (Flat / Up / Down)
     */
    public String decideNewTrend(ValuePair v)
    {
        String newTrend = new String();

        if (trend.equals("Up"))
        {        //No other possibility. If trend==equal, then the only notAppendable is a strictly lower value
            newTrend = "Down";
        }
        if (trend.equals("Down"))
        {
            newTrend = "Up";
        }
        if (trend.equals("Flat"))
        {
            if (endY > v.getY())
                newTrend = "Down";
            if (endY < v.getY())
                newTrend = "Up";
        }
        return newTrend;
    }


    public void appendValuePair(ValuePair v)
    {
        if (values == null)
        {
            System.out.println("Phase::appendValuePair() error: Must first construct the list of values for the phase!");
            System.exit(-1);
        }
        values.add(v);
        endPos = v.getPos();
        endY = v.getY();
    }


    public String consoleVerticalReport()
    {
        String result = "------------ New Phase: " + trend + " ---------------\n";
//		System.out.println("------------ New Phase: " + trend + " ---------------");

        for (ValuePair v : values)
        {
            result += v.getPos() + "\t" + v.getX() + "\t" + v.getY() + "\n";
            //		System.out.println(v.getPos() + "\t" + v.getX()+ "\t"+v.getY());
        }
        return result;
    }//end consloeVerticalReport


    public String toString()
    {
        return "From " + startPos + " to " + this.endPos + " with " + this.trend + "\n";

    }
}
