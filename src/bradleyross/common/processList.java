package bradleyross.common;
/**
 * Create and access a list of name/value pairs.
 * <p>This class will process a file where each line contains
 *    a name/value pair with the items separated by a colon.  Spaces 
 *    and colons may not occur within the names.</p>
 * @author Bradley Ross
 *
 */
public class processList 
{
/** 
 *     If false (default condition), 
 *     names of pairs are processed in a case
 *     insensitive manner and all names are converted
 *     to lower case.
 *     @see #isCaseSensitive()
 *     @see #setCaseSensitive boolean;
 */
protected boolean caseSensitive = false;
/**
 * Getter for caseSensitive.
 * @return Value of caseSensitive
 * @see #caseSensitive
 */
public boolean isCaseSensitive()
	{ return caseSensitive; }
/**
 * Setter for caseSensitive.
 * @param valueIn Value to be used for caseSensitive
 * @see #caseSensitive
 */
public void setCaseSensitive(boolean valueIn)
	{ caseSensitive = valueIn; }
/**
 * Controls amount of diagnostic output.
 * 
 * Higher files produce more diagnostic output.
 * @see #getDebugLevel()
 * @see #setDebugLevel(int)
 */
protected int debugLevel = 5;
/**
 * Getter for debugLevel.
 * @return Value of debugLevel
 * @see #debugLevel
 */
public int getDebugLevel()
	{ return debugLevel; }
/**
 * Setter for debug level.
 * @param levelIn Value to be used for debugLevel
 * @see #debugLevel
 */
public void setDebugLevel(int levelIn)
	{ debugLevel = levelIn; }
/** Internal class for name/value pairs.
 * 
 * @author Bradley Ross
 *
 */
protected static class namePair
	{
	protected String name;
	protected String value;
	public namePair(String nameIn, String valueIn)
		{ name = nameIn; value=valueIn; }
	/**
	 * Getter for name.
	 * @return Name of data pair
	 * @see #name
	 */
	protected String getName()
		{ return name; }
	/**
	 * Getter for value.
	 * @return Value for pair
	 * @see #value
	 */
	protected String getValue()
		{ return value; }
	}
protected  java.util.Vector<namePair> internalList;
public processList()
{
	internalList = new java.util.Vector<namePair>();
}
/**
 * Parse a text file to create the name/value pairs.
 * @param input Object containing input file
 */
public void processInput(java.io.LineNumberReader input)
	{
	String lineContents = null;
	try
		{
		while (true)
			{
			String working = null;
			String name = null;
			String value = null;
			lineContents = input.readLine();
			if (lineContents == null) { break; }
			/* Process line */
			working = lineContents.trim();
			int pos = working.indexOf(":");
			if (pos <= 0) { continue; }
			if (pos == working.length() - 1) { continue; }
			name = working.substring(0, pos).trim();
			if (!caseSensitive) { name = name.toLowerCase(); }
			value = working.substring(pos+1).trim();
			internalList.add(new namePair(name, value));
			}
		}
	catch (java.io.IOException e)
		{
		System.out.println("Error in processing input: " + 
				e.getClass().getName() + " : " + e.getMessage());
		e.printStackTrace();
		}
	}
/**
 * Diagnostic method for printing contents of list.
 */
public void listPairs()
	{
	System.out.println("Listing contents of internal list");
	for (int i = 0; i < internalList.size(); i++)
		{
		namePair working = internalList.get(i);
		System.out.println(working.getName() + " : " + working.getValue());
		}
	}
public String getValue(String name)
	{
	String working = null;
	String newValue = null;
	if (isCaseSensitive())
	{ working = name; }
	else
	{ working = name.toLowerCase(); }
	for (int i = 0; i < internalList.size(); i++)
		{ 
		namePair extract = internalList.get(i);
		if (extract.getName().equals(working))
			{ newValue = extract.getValue(); }
		}
	return newValue;
	}
public String getIntValue(String name)
	{
	String value = getValue(name);
	int loc = value.indexOf(".");
	if (loc >= 0)
		{ return value.substring(0, loc); }
	else
		{ return value; }
	}
/**
 * Test driver
 * @param args Argument 1 is name of file
 */
public static void main (String args[])
	{
	System.out.println("Starting test driver");
	String fileName = "a.txt";
	if (args.length > 0) fileName = args[0];
	processList processor = new processList();
	try
		{
		java.io.Reader inputFile = new java.io.FileReader(fileName);
		java.io.LineNumberReader reader = new java.io.LineNumberReader(inputFile);
		processor.processInput(reader);
		processor.listPairs();
		System.out.println("*****");
		System.out.println("Going for individual values");
		System.out.println("dewpoint " + processor.getValue("dewpoint"));
		System.out.println("sunrisetime " + processor.getValue("sunrisetime"));
		System.out.println("stationname " + processor.getValue("stationname"));
		}
	catch (java.io.IOException e)
		{
		System.out.println("Error in main: " + e.getClass().getName() + " : " + e.getMessage());
		e.printStackTrace();
		}
	}
}