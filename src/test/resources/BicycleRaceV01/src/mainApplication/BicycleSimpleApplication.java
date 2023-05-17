package mainApplication;

import java.util.Scanner;

import race.IRace;
import race.RaceTypeFactory;

public class BicycleSimpleApplication {
	private Scanner reader;
	
	public BicycleSimpleApplication(){
		reader = new Scanner(System.in); 
	}
	
	public Scanner getReader(){
		return reader;
	}
	
	public int chooseType(String message){
		int answer = 0;
		while( answer > 2 || answer <= 0){
			System.out.println(message);
			answer = reader.nextInt();
			if(answer > 2 || answer <= 0)
				System.out.println("Wrong answer! Try again...");
		}
		return answer;
	}
	
	
	public int printMenu(){
		int answerOperation = 0;
		while( answerOperation > 3 || answerOperation <= 0){
			System.out.println("Choose(1-3)\n 1. Add vehicle\n 2. Race\n 3. Exit");
			answerOperation = reader.nextInt();
			if(answerOperation > 3 || answerOperation <= 0)
				System.out.println("Wrong answer! Try again...");
		}
		
		return answerOperation;
	}
	
	public void closeReader(){
		reader.close();
	}
	
	public static void main(String[] args) {
		BicycleSimpleApplication bTester = new BicycleSimpleApplication();
		RaceTypeFactory raceFactory = new RaceTypeFactory();
		IRace r = raceFactory.createRaceWithStages();
		while(true){
			int operation = bTester.printMenu();
			if(operation == 1){
				System.out.println("Choose a name for your bicycle:");
				String name = bTester.getReader().next();
				System.out.println(name);
				
				int answerBrake = bTester.chooseType("Choose brake type (1/2)\n"
						+ "1. Nice brakes\n2. Duper brakes");
				int answerPedal = bTester.chooseType("Choose pedal type (1/2)\n"
						+ "1. Simple pedal\n2. Zuper pedal");
				
				System.out.println("Select the initial velocity for your bicycle:");
				double velocity = bTester.getReader().nextDouble();
					
				if(answerBrake == 1 && answerPedal == 1){
					r.setupVehicle("SimplePedal", "NiceBrakes", name, velocity);
				}
				else if(answerBrake == 1 && answerPedal == 2){
					r.setupVehicle("ZuperPedal", "NiceBrakes", name, velocity);
				}
				else if(answerBrake == 2 && answerPedal == 1){
					r.setupVehicle("SimplePedal", "DuperBrakes", name, velocity);
				}
				else{//answerBrake == 2 && answerPedal == 2
					r.setupVehicle("ZuperPedal", "DuperBrakes", name, velocity);
				}
			}
			else if(operation == 2){
				r.runRace();
			}
			else{
				break;
			}	
		}
		
		//r.setupVehicles();
		bTester.closeReader();
	}
	
}
