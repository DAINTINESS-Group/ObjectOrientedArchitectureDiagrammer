package application;

import java.util.Scanner;

import engine.EngineFactory;
import engine.IEngine;


public final class ClientApplication {
	public static void main(String[] args){
		EngineFactory engineFactory = new EngineFactory();
		IEngine engine = engineFactory.createMainEngine();

		
		Scanner reader = new Scanner(System.in);
		System.out.println("Please provide the name of the input file:");
		String filename = reader.next();
		engine.setTimeLine(filename);	
		engine.consoleVerticalReport();
		
		while(true){
			System.out.println("1. Set visualizer, 2. ProducePhases, 3. Visualize time series, 4. Report phases, 5. Exit");
			int answer = reader.nextInt();
			if(answer == 1){
				System.out.println("Choose: 1. Html Visualizer, 2. Console visualizer:");
				int visAnswer = reader.nextInt();
				if(visAnswer == 1){
					engine.setVisualizer("HtmlVisualizer");
				}
				else if(visAnswer == 2){
					engine.setVisualizer("ConsoleVisualizer");
				}
			}
			else if(answer == 2){
				int numPhases = 0;
				numPhases = engine.producePhases();
				System.out.println(numPhases + " phases were produced...");
			}
			else if(answer == 3){
				engine.visualize();
			}
			else if(answer == 4){
				engine.reportPhases();
			}
			else{
				System.out.println("Exiting now...");
				break;
			}
		}
		
		reader.close();
	}
}
