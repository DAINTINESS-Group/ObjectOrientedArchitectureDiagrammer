package bicycleBody;

import bicycleBreaks.IBrakes;
import bicycleBreaks.BrakeFactory;
import bicyclePedal.IPedal;
import bicyclePedal.PedalFactory;
/**
* <h1>Bicycle</h1>
* Class responsible for handling all the functionality
* that is needed for a bicycle.
*
* @version 1.0
* @since   2017-07-24
*/
public class Bicycle {

	public Bicycle(String pedalsName, String breaksName, String name){
		breakFactory = new BrakeFactory();
		pedalFactory = new PedalFactory();
		breaks = breakFactory.constructBrake(breaksName);
		pedal = pedalFactory.constructPedal(pedalsName);
		velocity = 0.0;
		timeRun = 0.0;
		this.name = name;
	}
	
	public double getVelocity(){
		return velocity;
	}
	
	public double getTimeRun(){
		return timeRun;
	}
	
	public void computeTime(double distance){
		timeRun += distance/velocity;
	}
	
	public String getName(){
		return name;
	}
	
	public void setOriginalVelocity(double originalVelocity){
		velocity = originalVelocity;
	}
	
	/**
 	 * Computes the velocity of the bicycle
 	 * based on the pedaling rate.
 	 * 
 	 * @param rate pedaling rate
 	 * 
 	 * @return the computed velocity
 	 */
	public double setPedaling(double rate){
		velocity += pedal.getSpeed(rate);
		return velocity;
	}
	
	/**
 	 * Computes the velocity of the bicycle
 	 * based on the given braking force.
 	 * 
 	 * @param force braking force
 	 * 
 	 * @return the computed velocity
 	 */
	public double setBraking(double force){
		velocity -= breaks.getSpeedReduction(force);
		if (velocity < 0)
			velocity = 0;
		return velocity;
	}
	
	/**
 	 * Reports the information regarding a bicycle
 	 * (velocity and status)
 	 */
	public void reportDetails(){
		
		System.out.println(getName() + ": velocity is: " + String.format( "%.2f",getVelocity()) + " and time run is: " + String.format( "%.2f",getTimeRun()));
		
		if(reportIfDamageExists() == false)
			System.out.println("Status: OK");
		else
			System.out.println("Status: Broken");
		System.out.println("-------------------");
	}
	
	public boolean reportIfDamageExists(){
		boolean brokenStatus = false;
		if (breaks.reportIfBroken(0.7) == true){
			brokenStatus = true;
			System.out.println("Breaks are broken");
		}
		if (pedal.reportIfBroken() == true){
			brokenStatus = true;
			System.out.println("Pedals are broken");
		}		
		return brokenStatus;
	}
	
	public boolean equals(Bicycle bicycle){
		if(getName().equals(bicycle.getName()))
			return true;
		
		return false;
	}
	
	private double timeRun;
	private String name;
	private double velocity;
	private IBrakes breaks;
	private IPedal pedal;
	private BrakeFactory breakFactory;
	private PedalFactory pedalFactory;
}
