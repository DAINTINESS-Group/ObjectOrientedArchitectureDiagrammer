package controller;
/**
* <h1>MainEngineFactory</h1>
* Class responsible for creating the main engine
*
* Extremely simple, intentionally:should be extended with modifiers
* in the case that a new variant of MainEngine is added. 
*
* The class is final: cannot be subclassed; if need, create a new v. of the class
* and exploit the factory for its generation
*
* @version 1.0
* @since   2017-07-22
*/
public final class MainEngineFactory {
	
	/**
 	 * Creates a MainEngine object
 	 * 
 	 * @return MainEngine object
 	 */
	public IMainEngine createMainEngine(){
		return new MainEngine();
	}
}
