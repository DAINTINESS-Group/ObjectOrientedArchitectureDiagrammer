package parsing;

import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.io.FileInputStream;

import commons.*;

/**
 * <h1>SimpleTextParser</h1>
 * Responsible for handling the reading the data from a text file
 *
 * @version 1.0
 * @since 2017-07-23
 */
public final class SimpleTextParser implements IParser
{

    /**
     * Reads the given input file and populates a Timeline object with data.
     *
     * @param fileName name of the input file
     * @return Timeline object loaded with the data from the file
     */
    @Override
    public TimeLine parse(String fileName)
    {
        Scanner  inputStream = null;
        TimeLine tl          = new TimeLine();

        //Opening files for read and write, checking exception
        try
        {
            inputStream = new Scanner(new FileInputStream(fileName));

        }
        catch (FileNotFoundException e)
        {
            System.out.println("Problem opening files.");
            System.exit(0);
        }

        int count = 0;
        while (inputStream.hasNextLine())
        {
            String line = inputStream.nextLine();
            count++;

            StringTokenizer tokenizer = new StringTokenizer(line);
            if (tokenizer.countTokens() != 3)
            {
                System.out.println("Wrong Input format. I expect <pos xValue yValue>.");
                System.exit(0);
            }
            String s   = tokenizer.nextToken();
            int    pos = Integer.parseInt(s);
            s = tokenizer.nextToken();
            double xValue = Double.parseDouble(s);
            s = tokenizer.nextToken();
            double yValue = Double.parseDouble(s);


            ValuePair v = new ValuePair(pos, xValue, yValue);
            tl.addValue(v);
        }

        System.out.println(count + " lines parsed");
        inputStream.close();


        return tl;
    }

}
