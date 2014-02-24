package bradleyross.library.helpers;
import java.text.NumberFormat;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Types;
import java.util.Enumeration;
import javax.servlet.http.HttpServletRequest;
import bradleyross.library.database.DatabaseProperties;
import bradleyross.library.helpers.StringHelpers;
/**
 * General purpose helpers for use in creating servlets.
 * @author - Bradley Ross
 */
public class ServletHelpers
{
	protected DatabaseProperties data = null;
	public ServletHelpers(DatabaseProperties dataValue)
	{
		data = dataValue;
	}
	/**
	 * This method is intended to help in showing the elapsed time for
	 * generating a page.
	 * <p>By using this method, you avoid the possibility of the
	 *    value for milliseconds displaying with over a dozen
	 *    digits due to round-off error.</p>
	 * @param startTime Date (in milliseconds that was generated using
	 *        a previous java.util.Date object.
	 * @return Formatted string containing the elapsed time in
	 *        milliseconds
	 * @see java.util.Date
	 * @see java.text.NumberFormat
	 */
	public static String formatElapsedTime (long startTime)
	{
		long difference = new java.util.Date().getTime() - startTime;
		java.text.NumberFormat nf = java.text.NumberFormat.getInstance();
		nf.setMaximumFractionDigits(3);
		return nf.format((double) difference / 1000.0d);
	}
	/**
	 * Returns a default value if the examined value is null.
	 * <p>Trying to run a number of String methods, such as equals or 
	 *    equalsIgnoreCase, result in exeptions being thrown if the 
	 *    object being examined has a value of null<p>
	 * @param value String value to be examined
	 * @param ifDefault Default value to be used if value is null
	 * @return String containing either examined or default value
	 */
	public static String valueWithDefault ( String value, String ifDefault)
	{
		if ( value == null)
		{ return ifDefault; }
		else
		{return value; }
	}
	/**
	 * Replace occurrences of double quotes with two
	 * double quote characters.
	 * @param value String to be processed
	 * @return Processed string
	 */
	public static String doubleDoubleQuotes (String value)
	{
		java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("\\\"");
		java.util.regex.Matcher matcher = pattern.matcher(value);
		return matcher.replaceAll("\"\"");
	}
	/**
	 * 
	 * Replace occurrences of double quotes with a back slash followed by
	 * a double quote.
	 * @param value String to be processed
	 * @return Processed string
	 */
	public static String escapeDoubleQuotes (String value)
	{
		Pattern pattern = Pattern.compile("\\\"");
		Matcher matcher = pattern.matcher(value);
		return matcher.replaceAll("\\\\\"");
	}
	/**
	 * Return a character value from a result set as a String object.
	 * @param rs Result set to be used
	 * @param columnName Name of column to be read
	 * @param defaultValue Default value if column in result set has a value of null
	 * @return Character data
	 * @throws SQLException
	 */
	public static String getStringValue(ResultSet rs, String columnName, String defaultValue)
	throws SQLException
	{
		String working = rs.getString(columnName);
		if (rs.wasNull())
		{
			working = defaultValue;
		}
		
		return working;
	}
	/**
	 * Return a character value from a result set as a String object.
	 * @param rs Result set to be read
	 * @param columnName Name of column to be read
	 * @param defaultValue Default value to be used in value in database is null
	 * @return Character data
	 * @throws SQLException
	 */
	public static String getStringValueEscaped(ResultSet rs, String columnName, String defaultValue)
	throws SQLException
	{
		String working = getStringValue(rs, columnName, defaultValue);
		return StringHelpers.escapeHTML(working);
	}
	/**
	 * Return the contents of a character column in a result set as a String object.
	 * @param rs Result set to be read
	 * @param columnName Name of column to be read
	 * @return Character data from database
	 * @throws SQLException
	 */
	public static String getStringValue(ResultSet rs, String columnName) throws SQLException
	{
		return getStringValue (rs, columnName, (String) null);
	}
	/**
	 * Return the contents of a character column in a result set as a String object.
	 * 
	 * <p>This method escapes the character strings using the HTML
	 *    character entity literals before returning the data to the calling program.</p>
	 * @param rs Result set to be read
	 * @param columnName Name of column to be read
	 * @return Character data from database
	 * @throws SQLException
	 */
	public static String getStringValueEscaped (ResultSet rs, String columnName) throws SQLException
	{
		return getStringValueEscaped (rs, columnName, (String) null);
	}
	/**
	 * Return a floating point value from the result set.
	 * @param rs ResultSet object containing information
	 * @param columnName Name of column in result set
	 * @param defaultValue Value to be returned if value of column is null
	 * @return Value from result set
	 * @throws SQLException
	 */
	public static float getFloatValue(ResultSet rs, String columnName, float defaultValue)
	throws SQLException
	{
		float working = rs.getFloat(columnName);
		if (rs.wasNull())
		{
			working = defaultValue;
		}
		return working;
	}
	/**
	 * Return a floating point value from a result set, returning zero if the value
	 * in the result set was null.
	 * @param rs Result set from which data is to be taken
	 * @param columnName Name of column in result set
	 * @return Floating point value from result set
	 * @throws SQLException
	 */
	public static float getFloatValue (ResultSet rs, String columnName) throws SQLException
	{
		return getFloatValue (rs, columnName, 0.0f );
	}
	/**
	 * Set a CHAR column in a prepared statement.
	 * 
	 * @param stmt Prepared statement
	 * @param position Position in prepared statement
	 * @param value Value to be used for parameter
	 * @throws SQLException
	 */
	public static void setChar(PreparedStatement stmt, int position, String value) throws SQLException
	{
		setChar(stmt, position, value, false);
	}
	/**
	 * Set a CHAR column in a prepared statement.
	 * 
	 * @param stmt Prepared statement
	 * @param position Position in prepared statement
	 * @param value Value to be used for parameter
	 * @param toUpperCase True means strings are converted to upper case
	 * @throws SQLException
	 */
	public static void setChar(PreparedStatement stmt, int position, String value, boolean toUpperCase) throws SQLException
	{
		if (value == null)
		{
			stmt.setNull(position, Types.CHAR);
		}
		else if (value.trim().length() == 0)
		{
			stmt.setNull(position, Types.CHAR);
		}
		else
		{
			if (toUpperCase)
			{
				stmt.setString(position, value.toUpperCase());
			}
			else
			{
				stmt.setString(position, value);
			}
		}	
	}
	/**
	 * Set a CHAR column in a prepared statement.
	 * 
	 * @param stmt Prepared statement
	 * @param position Position in prepared statement
	 * @param rs Result set containing value to be used for parameter
	 * @param name Name of column in result set
	 * @throws SQLException
	 */
	public static void setChar (PreparedStatement stmt, int position, ResultSet rs, String name) throws SQLException
	{
		setChar(stmt, position, rs, name, false);
	}
	/**
	 * Set a CHAR column in a prepared statement.
	 * 
	 * @param stmt Prepared statement
	 * @param position Position in prepared statement
	 * @param rs Result set containing value to be used for parameter
	 * @param name Name of column in result set
	 * @param toUpperCase True means strings are converted to upper case
	 * @throws SQLException
	 */
	public static void setChar (PreparedStatement stmt, int position, ResultSet rs, String name, boolean toUpperCase) throws SQLException
	{
		String working = rs.getString(name);
		if (rs.wasNull())
		{
			stmt.setNull(position, Types.CHAR);
		}
		else if (working == null)
		{
			stmt.setNull(position, Types.CHAR);
		}
		else if (working.trim().length() == 0)
		{
			stmt.setNull(position, Types.CHAR);
		}
		else
		{
			if (toUpperCase)
			{
				working = working.toUpperCase();
			}
			stmt.setString(position, working);
		}
	}
	/**
	 * Set a VARCHAR column in a prepared statement.
	 * 
	 * @param stmt Prepared statement
	 * @param position Position in prepared statement
	 * @param value Value to be used for parameter
	 * @throws SQLException
	 */
	public static void setVarchar(PreparedStatement stmt, int position, String value) throws SQLException
	{
		setVarchar(stmt, position, value, false);
	}
	/**
	 * Set a VARCHAR column in a prepared statement.
	 * 
	 * @param stmt Prepared statement
	 * @param position Position in prepared statement
	 * @param value Value to be used for parameter
	 * @param toUpperCase True means that strings are converted to upper case
	 * @throws SQLException
	 */
	public static void setVarchar(PreparedStatement stmt, int position, String value, boolean toUpperCase) throws SQLException
	{
		if (value == null)
		{
			stmt.setNull(position, Types.VARCHAR);
		}
		else if (value.trim().length() == 0)
		{
			stmt.setNull(position, Types.VARCHAR);
		}
		else
		{
			if (toUpperCase)
			{
				stmt.setString(position, value.toUpperCase());
			}
			else
			{
				stmt.setString(position, value);
			}
		}	
	}
	/**
	 * Set a VARCHAR column in a prepared statement.
	 * 
	 * @param stmt Prepared statement
	 * @param position Position in prepared statement
	 * @param rs Result set containing value to be used for parameter
	 * @param name Name of column in result set
	 * @throws SQLException
	 */
	public static void setVarchar (PreparedStatement stmt, int position, ResultSet rs, String name) throws SQLException
	{
		setVarchar (stmt, position, rs, name, false);
	}
	/**
	 * Set a VARCHAR column in a prepared statement.
	 * 
	 * @param stmt Prepared statement
	 * @param position Position in prepared statement
	 * @param rs Result set containing value to be used for parameter
	 * @param name Name of column in result set
	 * @param toUpperCase True means that strings are to be converted to upper case
	 * @throws SQLException
	 */
	public static void setVarchar (PreparedStatement stmt, int position, ResultSet rs, String name, boolean toUpperCase) throws SQLException
	{
		String working = rs.getString(name);
		if (rs.wasNull())
		{
			stmt.setNull(position, Types.VARCHAR);
		}
		else if (working == null)
		{
			stmt.setNull(position, Types.VARCHAR);
		}
		else if (working.trim().length() == 0)
		{
			stmt.setNull(position, Types.VARCHAR);
		}
		else
		{
			if (toUpperCase)
			{
				working = working.toUpperCase();
			}
			stmt.setString(position, working);
		}
	}	
	/**
	 * Set a FLOAT column in a prepared statement.
	 * 
	 * @param stmt Prepared statement
	 * @param position Position in prepared statement
	 * @param value Value to be used for parameter
	 * @throws SQLException
	 */
	public static void setFloat(PreparedStatement stmt, int position, Float value) throws SQLException
	{
		if (value == null)
		{
			stmt.setNull(position, Types.FLOAT);
		}
		else
		{
			stmt.setFloat(position, value);
		}	
	}
	/**
	 * Set a FLOAT column in a prepared statement.
	 * 
	 * @param stmt Prepared statement
	 * @param position Position in prepared statement
	 * @param rs Result set containing value to be used for parameter
	 * @param name Name of column in result set
	 * @throws SQLException
	 */
	public static void setFloat (PreparedStatement stmt, int position, ResultSet rs, String name) throws SQLException
	{
		float working = rs.getFloat(name);
		if (rs.wasNull())
		{
			stmt.setNull(position, Types.FLOAT);
		}
		else
		{
			stmt.setFloat(position, working);
		}
	}		
	/**
	 * Set a INTEGER column in a prepared statement.
	 * 
	 * @param stmt Prepared statement
	 * @param position Position in prepared statement
	 * @param value Value to be used for parameter
	 * @throws SQLException
	 */
	public static void setInteger(PreparedStatement stmt, int position, Integer value) throws SQLException
	{
		if (value == null)
		{
			stmt.setNull(position, Types.INTEGER);
		}
		else
		{
			stmt.setInt(position, value);
		}	
	}
	/**
	 * Set a INTEGER column in a prepared statement.
	 * 
	 * @param stmt Prepared statement
	 * @param position Position in prepared statement
	 * @param rs Result set containing value to be used for parameter
	 * @param name Name of column in result set
	 * @throws SQLException
	 */
	public static void setInteger (PreparedStatement stmt, int position, ResultSet rs, Integer name) throws SQLException
	{
		int working = rs.getInt(name);
		if (rs.wasNull())
		{
			stmt.setNull(position, Types.INTEGER);
		}
		else
		{
			stmt.setInt(position, working);
		}
	}		
 	/**
	 * Construct an SQL date using the year, month, and day
	 * @param yearValue Year (4 digit)
	 * @param monthValue Number of Month (January = 1)
	 * @param dayValue Day of month
	 * @return SQL date
	 */
	public static Date buildDate(String yearValue, String monthValue, String dayValue) 
	{
		int year = -1;
		int month = -1;
		int day = -1;
		try
		{
		year = Integer.parseInt(yearValue);
		month = Integer.parseInt(monthValue);
		day = Integer.parseInt(dayValue);
		}
		catch (NumberFormatException e)
		{
			throw new IllegalArgumentException ("buildDate unable to process " + yearValue + ":" + monthValue + ":" + dayValue);
		}
		return buildDate(year, month, day);
	}
	/**
	 * Construct an SQL date using the year, month, and day
	 * @param year Year (4 digit)
	 * @param month Number of month (January = 1)
	 * @param day Day of month
	 * @return SQL date
	 */
	public static Date buildDate(int year, int month, int day) 
	{
		NumberFormat format2 =  NumberFormat.getIntegerInstance();
		NumberFormat format4 =  NumberFormat.getIntegerInstance();
		format2.setMinimumIntegerDigits(2);
		format2.setGroupingUsed(false);
		format4.setMinimumIntegerDigits(4);
		format4.setGroupingUsed(false);
		return Date.valueOf(format4.format(year) + "-" + format2.format(month) + "-" + format2.format(day));
	}
	/**
	 * Place information on the parameters in the body of the servlet.
	 * 
	 * <p>This is intended for debugging purposes.</p>
	 * 
	 * @param request Request object
	 * @return Text to be included in HTML document
	 */
	public static String showParameters(HttpServletRequest request)
	{
		Enumeration<?> parameterNames = null;
		String values[] = null;
		parameterNames = request.getParameterNames();
		StringBuffer working = new StringBuffer("<ul>");
		while (parameterNames.hasMoreElements())
		{
			String parameter = (String) parameterNames.nextElement();
			working.append("<li>" + StringHelpers.escapeHTML(parameter) + ": ");
			values = request.getParameterValues(parameter);
			for (int i = 0; i < values.length; i++)
			{
				working.append(StringHelpers.escapeHTML(values[i]) + " ");
			}
			working.append("</li>");
		}
		working.append("</ul>");
		return new String(working);
	}
	/**
	 * Test driver.
	 * @param args Not used in this program
	 */
	public static void main(String args[])
	{
		System.out.println(doubleDoubleQuotes("\"This is  a test\""));
		System.out.println(escapeDoubleQuotes("aaa\"\"bbb \""));
		System.out.println(doubleDoubleQuotes("aaa\"bbb"));
	}
}
