package load;

import java.util.Objects;

/**
* <h1>FullDataLoaderFactory</h1>
* Factory class responsible for creating different types
* of loaders.
* 
* The class is final: cannot be subclassed; if need, create a new v. of the class
* and exploit the factory for its generation
* 
* @version 1.0
* @since   2017-07-22
*/
public final class FullDataLoaderFactory {
	
	/**
 	 * Creates different types of loaders based on the input parameter
 	 * 
 	 * @param className: type of loader
 	 * @param empPath a String with the path of the file with employees
 	 * @param dishPath a String with the path of the file with dishes
 	 * @param orderPath a String with the path of the file with orders
 	 * @return an IFullDataLoader loader to load all three files
 	 */
	public IFullDataLoader createFullDataLoader(String className, String empPath, String dishPath, String orderPath){
		if (Objects.isNull(empPath) || empPath.length() == 0) {
			System.err.println("FullDataLoaderFactory: invalid path for employees file");
			return null;
		}
		if (Objects.isNull(dishPath) || dishPath.length() == 0) {
			System.err.println("FullDataLoaderFactory: invalid path for dishes file");
			return null;
		}
		if (Objects.isNull(orderPath) || orderPath.length() == 0) {
			System.err.println("FullDataLoaderFactory: invalid path for orderItems file");
			return null;
		}
		
		
		if(className.equals("FullDataLoader"))
			return new FullDataLoader(empPath, dishPath, orderPath);
		
		System.err.println("FullDataLoaderFactory: you passed a wrong className argument to the FullDataLoader Factory");
		return null;
	}

}
