package visual;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.FileOutputStream;


import commons.*;

/**
* <h1>HtmlVisualizer</h1>
* Class responsible for creating a HTML
* page which visualizes a specific timeline
* 
* @version 1.0
* @since   2017-07-23
 * 
 */
public final class HtmlVisualizer extends AbstractRasterBasedVisualizer implements IVisualizer{

	private String header;

	
	public HtmlVisualizer(TimeLine tl) {
		super(tl);
		header = "<!doctype html>" + 
				"\n" + 
				"<html>" + 
				"\n" + 
				"<head>" + 
				"\n" + 
				"<meta http-equiv=\"Content-Type\" content\"text/html; charset=windows-1253\">" + 
				"\n" + 
				"<title>MyNiceTimeline</title>" + 
				"\n" + 
				"</head>" + 
				"\n" + 
				"<body>" + 
				"\n";
	
		this.produceRaster(tl);

	}


	/**
	 * 
	 * @see visual.IVisualizer#visualize()
	 */
	@Override
	public char [][] visualize() {
		String fileName = new String("showMeTheTL.html");

		// Opening file to write, checking exception
		try {
			PrintWriter outputStream = new PrintWriter(new FileOutputStream(
					fileName)); // APPEND would be .... (new
								// FileOutputStream(outputFileName, true));
			outputStream.println(header);

			outputStream.println("<table>");
			/* column by column print
			 * x-axis print x-axis legend
			 */
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
			System.out.println("Problem opening files.");
			System.exit(0);
		}
		return this.raster;
	}// end visualize

}
