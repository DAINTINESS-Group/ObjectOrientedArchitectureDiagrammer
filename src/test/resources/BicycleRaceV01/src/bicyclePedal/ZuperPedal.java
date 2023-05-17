package bicyclePedal;
/**
* <h1>DuperBreaks</h1>
* Class responsible for the representation of
* a type of pedals (DuperBreaks).
*
* @version 1.0
* @since   2017-07-22
*/
public class ZuperPedal implements IPedal {
	
	/**
 	 * Computes the speed by a factor based on the input
 	 * parameter.
 	 * 
 	 * @param pedalingRate the rate of the pedaling
 	 * 
 	 * @return the speed which is achieved
 	 */
	@Override
	public double getSpeed(double pedalingRate) {
		return 45 * pedalingRate + 650;
	}

	/**
 	 * 
 	 * @return always false
 	 */
	@Override
	public boolean reportIfBroken() {
		// TODO Auto-generated method stub
		return false;
	}

}
