package reporting;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Objects;

import model.Dish;
import model.Employee;
import model.WeeklyStats;
/**
* <h1>ReportGeneratorForHTML</h1>
* Responsible for saving the restaurant statistics 
* in HTML format
*
* Observe that the class has package-private visibility:
* clients should only use the public Interface for work-to-be-done
* and the factory for object creation
*
* The class is final: cannot be subclassed; if need, create a new v. of the class
* and exploit the factory for its generation
* 
* @version 1.0
* @since   2017-07-22
*/
final class ReportGeneratorForHTML implements IReportGenerator {

	/**
 	 * Creates and saves the data in HTML format
 	 * 
 	 */
	private int rasterToHTML(String fileName, String title, String[][] raster, int numRows, int numCols) {
		String header = new String("");
		header.concat("<!doctype html>");
		header.concat("\n");
		header.concat("<html>");
		header.concat("\n");
		header.concat("<head>");
		header.concat("\n");
		header.concat("<meta http-equiv=\"Content-Type\" content\"text/html; charset=windows-1253\">");
		header.concat("\n");
		header.concat(title);
		header.concat("\n");
		header.concat("</head>");
		header.concat("\n");
		header.concat("<body>");
		header.concat("\n");
		// Opening file to write, checking exception
		try {
			PrintWriter outputStream = new PrintWriter(new FileOutputStream(fileName)); // APPEND would be .... (new FileOutputStream(outputFileName, true));
			outputStream.println(header);

			outputStream.println("<table>");

			for(int i =0; i < numRows; i++){
				outputStream.println("<tr>");
				for(int j =0; j < numCols; j++){
					outputStream.print("<td>"+raster[i][j]+"</td>");
				}
				outputStream.println("\n</tr>");
			}
			outputStream.println("</table>");
			outputStream.println("</body>\n</html>");
			outputStream.close();
		} catch (FileNotFoundException e) {
			System.err.println("Problem opening file " + fileName);
			//System.exit(0);
			return -1;
		}
		return 0;
	}// end visualize
	
	
	/**
 	 * Saves the statistics for the employees
 	 * 
 	 * @param personnel: list of employees
 	 * @return 0 if all goes well, a negative number if sth goes wrong
 	 */
	@Override
	public int reportOnEmps(List<Employee> personnel) {
		if(Objects.isNull(personnel)) {
			System.err.println("ReportGeneratorForHTML: null object in report for Emps");
			return -1;
		}
		DecimalFormat df = new DecimalFormat("###.##");
		//df.setDecimalSeparatorAlwaysShown(true);
		DecimalFormatSymbols dfs=new DecimalFormatSymbols();			
		dfs.setDecimalSeparator('.');	//otherwise, the String.format conversion makes "," in GR environments; I want "." for decimals
		df.setDecimalFormatSymbols(dfs);		
	
		int numCols = 4;
		int numRows = WeeklyStats.getNumEmployees() + 1;
				
		String [][]raster = new String[numRows][numCols];
		raster[0][0] = "Name"; raster[0][1] = "Stars"; raster[0][2] = "#Orders"; raster[0][3] = "Salary";
		int i = 1;
		for(Employee e: personnel){
			raster[i][0] = e.getName(); 
			raster[i][1] = df.format(e.getEvaluation()); 
					//String.format( "%.2f",e.getEvaluation());
					//Double.toString(e.getEvaluation()); 
			raster[i][2] = Integer.toString(e.getNumberOfOrders()); 
			raster[i][3] = df.format(e.getSalary());  
					//Double.toString(e.getSalary());
			i++;			
		}
		return  rasterToHTML("./src/main/resources/output/Emps.html", "Weekly Stats Emps", raster, numRows, numCols);
		
	}
	
	/**
 	 * Saves the statistics for the dishes
 	 * 
 	 * @param menu: list of dishes
 	 * @return 0 if all goes well, a negative number if sth goes wrong
 	 */
	@Override
	public int reportOnDishes(List<Dish> menu) {
		if(Objects.isNull(menu)) {
			System.err.println("ReportGeneratorForHTML: null object in report for Dishes");
			return -1;
		}
		
		int numCols = 4;
		int numRows = WeeklyStats.getNumDishes() + 1;
		
		String [][]raster = new String[numRows][numCols];
		raster[0][0] = "Name"; raster[0][1] = "Gain"; raster[0][2] = "AvgTime"; raster[0][3] = "Stars";
		int i = 1;
		for(Dish d: menu){
			raster[i][0] = d.getDishName();
			raster[i][1] = Double.toString(d.getGain());
			raster[i][2] = Double.toString(d.getAvgTimeToMake());
			raster[i][3] = Double.toString(d.getEvaluation());
			i++;
		}
		return rasterToHTML("./src/main/resources/output/Dishes.html", "Weekly Stats Menu", raster, numRows, numCols);
	}

}
