package visual;

import commons.TimeLine;

/**
* <h1>VisualizerFactory</h1>
* Factory class responsible for creating
* different types of visualizer objects
* Parameterized Factory! Can do better; still this is quite simple!
* 
* @version 1.0
* @since   2017-07-23
 * 
 */
public final class VisualizerFactory {
	
	/**
	 * 
	 * @param concreteClassName name of the class that must be created
	 * @param tl timeline needed to be visualized
	 * @return different types of IVisualizer object, depending on the input parameters
	 */
	public IVisualizer createVisualizer(String concreteClassName, TimeLine tl){
		if (concreteClassName.equals("HtmlVisualizer")){
			return new HtmlVisualizer(tl);
		}
		else if (concreteClassName.equals("ConsoleVisualizer")){
			return new ConsoleVisualizer(tl);
		}

		System.out.println("If the code got up to here, you passed a wrong argument to AnalyserFactory");
		return null;
	}
}
