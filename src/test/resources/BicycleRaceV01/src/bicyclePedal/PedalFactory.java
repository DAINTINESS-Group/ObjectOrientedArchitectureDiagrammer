package bicyclePedal;
/**
* <h1>PedalFactory</h1>
* Factory class responsible for creating the different
* types of pedals.
*
* @version 1.0
* @since   2017-07-22
*/
public class PedalFactory {
	
	/**
 	 * Creates all the different types of objects which are responsible
 	 * for modeling the different kinds of pedals.
 	 * 
 	 * @return an IPedal object which is responsible
 	 * for the representation of a pedal type.
 	 */
	public IPedal constructPedal(String concreteClassName){
		if (concreteClassName.equals("SimplePedal"))
			return new SimplePedal();
		else if (concreteClassName.equals("ZuperPedal"))
			return new ZuperPedal();
		
		System.out.println("If the code got up to here, you passed a wrong argument to PedalFactory");
		return null;
	}
}
