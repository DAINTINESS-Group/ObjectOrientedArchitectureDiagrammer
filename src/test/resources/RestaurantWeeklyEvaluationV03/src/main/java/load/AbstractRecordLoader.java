package load;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;


/**
 * <h1>AbstractRecordLoader</h1>
 * Class responsible for reading the data from an input file.
 * <p>
 * Observe the package-private visibility: the class is visible to the other classes of its package (tests of the package included) but not visible outside the package. Only the public interface and its factory are visible to others.
 *
 * @version 1.0
 * @since 2017-07-22
 */
abstract class AbstractRecordLoader<E>
{
    /**
     * An abstract method (to be implemented at concrete classes) that converts a string array to a concrete object depending on what kind of objects are being instantiated by the loader
     *
     * @param tokens        A string array coming from a file
     * @param objCollection a generic List of objects to be populated with the new-born object
     * @return 0 if all proceeds well, negative int otherwise
     */
    public abstract int constructObjectFromRow(String[] tokens, List<E> objCollection);


    /**
     * Reads the data from the given file and stores them in an ArrayList
     *
     * @param fileName:      name of the input file
     * @param delimeter:     file delimiter
     * @param hasHeaderLine: specifies whether the file has a header
     * @param numFields:     number of columns in the input file
     * @param objCollection: empty list which will be loaded with the data from the input file
     * @return the number of rows that are processed
     */
    public int load(String fileName, String delimeter, boolean hasHeaderLine, int numFields, List<E> objCollection)
    {
        if (numFields < 1)
        {
            System.out.println("AbstractRecordLoader: Wrong number of fields, less than 1!");
            System.exit(0);
        }
        if (delimeter == null)
        {
            System.out.println("AbstractRecordLoader: null delimiter, exiting!");
            System.exit(0);
        }
        if (objCollection == null)
        {
            System.out.println("AbstractRecordLoader: null object collection, exiting!");
            System.exit(0);
        }
        //Opening files for read and write, checking exception
        Scanner inputStream = null;
        try
        {
            inputStream = new Scanner(new FileInputStream(fileName));

        }
        catch (FileNotFoundException e)
        {
            System.out.println("Problem opening file: " + fileName);
            System.exit(0);
        }

        int count = 0;

        //process the title of the csv
        if (hasHeaderLine)
        {
            @SuppressWarnings("unused")
            String titleLine = inputStream.nextLine();
            count++;
        }
        String line = "";
        //process the actual rows one by one
        while (inputStream.hasNextLine())
        {
            line = inputStream.nextLine();
            count++;

            StringTokenizer tokenizer = new StringTokenizer(line, delimeter);
            if (tokenizer.countTokens() != numFields)
            {
                System.out.println("Wrong Input format in file " + fileName + ". I found " + tokenizer.countTokens() + " values, but I expect " + numFields + " values per row!");
                //				System.exit(0);
            }

            String[] tokens = new String[numFields];
            for (int i = 0; i < numFields; i++)
            {
                tokens[i] = tokenizer.nextToken();
            }

            //ToDo: here add the method that takes the token and forms the object and puts it in the resultList
            int objConstructionErrorCode;
            objConstructionErrorCode = constructObjectFromRow(tokens, objCollection);
            if (objConstructionErrorCode != 0)
            {
                System.out.println("ObjParsingError. I found a problem at line " + count + " of file " + fileName);
                System.exit(0);
            }
        }
        inputStream.close();
        System.out.println("Processed " + count + " rows and loaded " + objCollection.size() + " objects.");
        return count;
    }

}
