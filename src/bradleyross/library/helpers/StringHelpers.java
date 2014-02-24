package bradleyross.library.helpers;
import java.util.regex.Pattern;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Matcher;
import bradleyross.library.helpers.GenericPrinter;
/**
 * Classes for manipulating strings.
 * @author Bradley Ross
 *
 */
public class StringHelpers 
{
	/**
	 * Pads a string with blanks to the right so that it has
	 * a specified size.
	 * 
	 * @param value String to be padded
	 * @param size Desired size of padded string
	 * @return Padded string
	 */
	public static  String padRight (String value, int size)
	{
		if (value == null)
		{
			StringBuffer local = new StringBuffer();
			for (int i = 0; i < size; i++)
			{
				local.append(" ");
			}
			return new String(local);
		}
		if (value.length() >= size)
		{
			return value;
		}
		StringBuffer working = new StringBuffer(value);
		int padding = size - value.length();
		if (padding <= 0) { return value; }
		for (int i = 0; i < padding; i++)
		{
			working.append(" ");
		}
		return new String(working);
	}
	/**
	 * Pads a string with blanks to the left so that it has
	 * a specified size.
	 * 
	 * @param value String to be padded
	 * @param size Desired size of padded string
	 * @return Padded string
	 */
	public static  String padLeft (String value, int size)
	{
		if (value == null)
		{
			StringBuffer local = new StringBuffer();
			for (int i = 0; i < size; i++)
			{
				local.append(" ");
			}
			return new String(local);
		}
		if (value.length() >= size)
		{
			return value;
		}
		StringBuffer working = new StringBuffer();
		int padding = size - value.length();
		if (padding <= 0) { return value; }
		for (int i = 0; i < padding; i++)
		{
			working.append(" ");
		}
		working.append(value);
		return new String(working);
	}
	/**
	 * Returns true if the string is only composed of numeric
	 * digits.
	 * @param value String to be evaluated
	 * @return True if string only has numeric digits 
	 * as characters.
	 */
	public static boolean isDigitsOnly(String value)
	{
		return Pattern.matches("^[0-9]*$", value.trim());
	}
	/**
	 * Returns true if and only if all of the characters in
	 * the string are alphanumeric.
	 * @param value String to be evaluated
	 * @return True if string has only alphanumeric characters
	 */
	public static boolean isAlphanumericOnly(String value)
	{
		return Pattern.matches("^[0-9a-zA-Z]*$", value.trim());
	}
	public static void printJustified(String value)
	{
		printJustified(System.out, value, 72);
	}
    public static void printJustified(String value, int length)
    {
    	printJustified(System.out, value, length);
    }
    public static void printJustified(PrintStream output, String value)
    {
    	printJustified (output, value, 72);
    }
    public static void printJustified(PrintWriter output, String value)
    {
    	printJustified (output, value, 72);
    }
    public static void printJustified(GenericPrinter output, String value)
    {
    	printJustified(output, value, 72);
    }
	public static void printJustified(PrintStream output, String value, int length)
	{
		GenericPrinter writer = new GenericPrinter(output);
		printJustified(writer, value, length);
	}
	public static void printJustified(PrintWriter output, String value, int length)
	{
		GenericPrinter writer = new GenericPrinter(output);
		printJustified(writer, value, length);
	}
	/**
	 * Send a string to an output device, inserting line breaks at the
	 * first space after a specified number of characters on the line.
	 * @param output Output device
	 * @param value String to be sent to output
	 * @param length Number of characters on a line after which the next space
	 *        is to be replaced by a line feed.
	 */
	protected static void printJustified(GenericPrinter output, String value, int length)
	{
		if (value == null)
		{
			output.println();	
			return;
		}
		String working = value.trim();
		if (working.length() == 0)
		{
			output.println();
			return;
		}
		while (true)
		{
			working = working.trim();
			if (working.length() == 0)
			{
				return;
			}
			int newLocation = working.indexOf(" ", length);
			if (newLocation < 0)
			{
				output.println(working);
				return;
			}
			output.println(working.substring(0, newLocation).trim());
			working = working.substring(newLocation).trim();
		}
	}
	/**
	 * Replace less than signs, greater than signs, and ampersands in a 
	 * string with their HTML representations.
	 * @param value String to be modified
	 * @return Modified string
	 */
	public static String escapeHTML(String value)
	{
		return escapeHTML(value, "NORMAL");
	}
	/**
	 * Carry out the HTML escaping of a string twice.
	 * 
	 * @param value String to be escaped
	 * @return Escaped string
	 */
	public static String doubleEscapeHTML(String value)
	{
		return escapeHTML(escapeHTML(value));
	}
	/**
	 * Carry out the HTML escaping of a string twice.
	 * 
	 * @param value String to be escaped
	 * @param modeValue Mode of escaping
	 * @return Escaped string
	 */
	public static String doubleEscapeHTML(String value, String modeValue)
	{
		return escapeHTML(escapeHTML(value, modeValue));
	}
	/**
	 * Carry out the HTML escaping of a string twice.
	 * 
	 * @param rs Result set containing value
	 * @param parameterName Name of column in result set
	 * @param modeValue Type of processing for escaping
	 * @return Escaped string
	 */
	public static String doubleEscapeHTML(ResultSet rs, String parameterName, String modeValue)
	{
		return escapeHTML(escapeHTML(rs, parameterName, modeValue));
	}
	/**
	 * Replace less than signs, greater than signs, and ampersands in a 
	 * string with their HTML representations.
	 * <p>If mode is TEXTAREA or TEXT, do not replace line breaks.  When
	 *    escaping the contents of a TEXTAREA tag, the new line and carriage return
	 *    characters should remain unchanged so that they are properly
	 *    represented in the form.</p>
	 * @param value String to be modified
	 * @param modeValue Mode of operation
	 * @return Modified string
	 */
	public static String escapeHTML(String value, String modeValue)
	{
		String mode = modeValue;
		if (mode == null)
		{
			modeValue="NORMAL";
		}
		if (value == null)
		{
			return " ";
		}
		else if (value.trim().length() == 0)
		{
			return " ";
		}
		String working = null;
		Pattern pattern = Pattern.compile("\\&");
		Matcher matcher = pattern.matcher(value);
		working =  matcher.replaceAll("&amp;");
		pattern = Pattern.compile("\\\"");
		matcher = pattern.matcher(working);
		working = matcher.replaceAll("&quot;");
		pattern = Pattern.compile("\\<");
		matcher = pattern.matcher(working);
		working = matcher.replaceAll("&lt;");
		pattern = Pattern.compile("\\>");
		matcher = pattern.matcher(working);
		working = matcher.replaceAll("&gt;");
		if (!(mode.equalsIgnoreCase("TEXTAREA") || mode.equalsIgnoreCase("TEXT")))
		{
			pattern = Pattern.compile("\r\n");
			matcher = pattern.matcher(working);
			working = matcher.replaceAll("<br />");
			pattern = Pattern.compile("\r");
			matcher = pattern.matcher(working);
			working = matcher.replaceAll("<br />");
			pattern = Pattern.compile("\n");
			matcher = pattern.matcher(working);
			working = matcher.replaceAll("<br />");
		}
		return working;
	}
	/**
	 * Processes special characters in HTML code so that it will
	 * not be interpreted by browser as commands.
	 * 
	 * <p>Uses @{link {@link #escapeHTML(String, String)}.</p>
	 * @param rs Result set containing value.
	 * @param parameterName Name of column containing value
	 * @param mode Mode of processing to be used
	 * @return Escaped string
	 */
	public static String escapeHTML(ResultSet rs, String parameterName, String mode)
	{
		try
		{
		String working = rs.getString(parameterName);
		if (rs.wasNull())
		{ working = new String(); }
		return escapeHTML(working, mode);
		}
		catch (SQLException e)
		{
			return new String();
		}
	}
	/**
	 * Processes special characters in HTML code so that it will
	 * not be interpreted by browser as commands.
	 * 
	 * <p>Uses @{link {@link #escapeHTML(String)}.</p>
	 * @param rs Result set containing value.
	 * @param parameterName Name of column containing value
	 * @return Escaped string
	 */
	public static String escapeHTML(ResultSet rs, String parameterName)
	{
		return escapeHTML(rs, parameterName, "NORMAL");
	}
	/**
	 * Test driver.
	 * @param args Not used
	 */
	public static void main(String args[])
	{
		System.out.println("Starting test driver at " + (new java.util.Date()).toString());
		System.out.println("***");
		System.out.println("*** Tests for isDigitsOnly");
		String list[] = { "123456", "-2", "a", "a1", "2" };
		for (int i = 0; i < list.length; i++)
		{
			System.out.println(Boolean.toString(isDigitsOnly(list[i])) + " " + list[i]);
		}
		System.out.println("***");
		System.out.println("*** Tests for escapeHTML");
		String list2[] = { "<abc&def>", "&&", "<<<>>>", "\"this\"", "aa\"\"bb", "a'b" };
		for (int i = 0; i < list2.length; i++)
		{
			System.out.println(list2[i] + "   " + escapeHTML(list2[i]));
		}
		System.out.println("***");
		System.out.println("*** Tests for printJustified");
		String working = "abcdefghijklmnopqrstuvwxyz  01234567890123456789 " +
		       "Now is the time for all good men to come to the aid of their country. " +
		       "The quick brown fox jumped over the lazy dog.";
		printJustified(working, 70);
		System.out.println("***");
		System.out.println("*** Tests for isAlphanumericOnly");
		String list3[] = {"alpha23", "a b", "abc", "ABc", "a\"b9", "a&b" };
		for (int i = 0; i < list3.length; i++)
		{
			System.out.println(Boolean.toString(isAlphanumericOnly(list3[i])) + " " + list3[i]);
		}
	}
}
