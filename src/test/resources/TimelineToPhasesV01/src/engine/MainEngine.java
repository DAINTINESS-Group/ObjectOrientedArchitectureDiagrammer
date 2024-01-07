package engine;

import commons.*;
import parsing.IParser;
import parsing.ParserFactory;
import analysis.IAnalyser;

import java.util.List;

import analysis.AnalyserFactory;
import visual.IVisualizer;
import visual.VisualizerFactory;

/**
 * <h1>ValuePair</h1>
 * Helper Class which in fact is data structure that holds all the information for a point in a time series
 *
 * @version 1.0
 * @since 2017-07-22
 */
public final class MainEngine implements IEngine
{
    private IParser     parser;
    private IAnalyser   analyser;
    private IVisualizer visualizer;

    //TODO: think of the extension, e.g., single timeseries -> many timeSeries?
    private TimeLine workingTimeLine;


    public MainEngine()
    {
        AnalyserFactory analyserF = new AnalyserFactory();
        ParserFactory   parserF   = new ParserFactory();

        parser = parserF.createParser("SimpleTextParser");
        analyser = analyserF.createAnalyser("NaiveAnalyser");

        workingTimeLine = new TimeLine();

        //cannot create visualizer here, because timeLine is null for the moment;
        //moreover, we want to have > 1 of visualizations
    }


    public IParser getParser()         {return parser;}


    public IAnalyser getAnalyzer()     {return analyser;}


    public IVisualizer getVisualizer() {return visualizer;}


    /**
     * Populates a timeline objects by reading the data from a given input file
     *
     * @param fileName file name of the input file
     */
    @Override
    public int setTimeLine(String fileName)
    {
        workingTimeLine = parser.parse(fileName);
        if (workingTimeLine == null)
        {
            System.out.println("Sth went wrong in setting the TimeLine");
            return -1;
        }
        return workingTimeLine.getValues().size();
    }//end setTimeLine


    /**
     * Produces the phases from the timeline
     *
     * @return returns an int with the number of phases of the working timeline, 0 if sth goes wrong.
     */
    @Override
    public int producePhases()
    {
        int numPhases = 0;
        numPhases = analyser.producePhasesFromTimeLine(workingTimeLine).size();
        return numPhases;
    }


    /**
     * Reports the phases to the console
     */
    @Override
    public List<String> reportPhases()
    {
        return analyser.reportToConsole();
    }


    /**
     * Prints a report for all the values in the specific timeline.
     */
    @Override
    public void consoleVerticalReport()
    {
        workingTimeLine.consoleVerticalReport();
    }


    /**
     * Initializes the type of visualizer that is needed.
     *
     * @param className type of visualizer
     */
    @Override
    public void setVisualizer(String className)
    {
        VisualizerFactory visualF = new VisualizerFactory();
        visualizer = visualF.createVisualizer(className, workingTimeLine);
    }


    /**
     * Visualizes the results in the console or in a HTML file format, depending on the type of visualizer that is set up.
     */
    @Override
    public char[][] visualize()
    {
        return visualizer.visualize();
    }

    //public TimeLine getTimeLine(){return workingTimeLine;}

}//end MainEngine
