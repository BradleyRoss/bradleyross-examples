package bradleyross.j2ee.servlets;
import bradleyross.library.helpers.StringHelpers;
import java.io.Serializable;
import java.io.IOException;
import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.NumberFormat;
/**
 * Superclass for servlets.
 * 
 * @author Bradley Ross
 * @see bradleyross.library.helpers.ServletHelpers
 *
 */
public abstract class Servlet extends HttpServlet 
{
	/**
	 * Inserted for debugging;
	 */
	public Servlet()
	{ ; }
	/**
	 * This class is placed here so that the programmer can subclass either
	 * Servlet.ThisPage or bradleyross.library.servlets.ThisPage.
	 * 
	 * @author Bradley Ross
	 *
	 */
	public class ThisPage extends bradleyross.j2ee.servlets.ThisPage
	{
		/**
		 * Constructor provided to allow subclassing.
		 */
		public ThisPage()
		{ ; }
		/**
		 * Initializes the object with information about the HTTP transaction.
		 * @param request HttpServletRequest object
		 * @param response HttpServletResponse object
		 * @param config ServletConfig object
		 * @throws IOException
		 */
		public ThisPage(HttpServletRequest request, HttpServletResponse response, ServletConfig config)
		throws IOException
		{
			super(request, response, config);
		}
	}
	/**
	 * Added to satisfy Serializable interface.
	 * @see Serializable
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Information on the configuration of the servlet.
	 * 
	 * <p>This is used to pass information from the {@link #init(ServletConfig) } method to the
	 *    other methods in the class.</p>
	 */
	private ServletConfig config = null;
	/**
	 * Called when initializing the object for handling HTTP
	 * transactions.
	 * @param configValue Servlet configuration object
	 * @throws ServletException
	 */
	public void init(ServletConfig configValue) throws ServletException
	{
		config = configValue;
	}
	/**
	 * Obtains servlet configuration information.
	 * @return servlet configuration object
	 */
	public ServletConfig getConfig()
	{
		return config;
	}
	/**
	 * Carries out processing for a HTTP transaction.
	 * 
	 * @see #starter(ThisPage)
	 * @see #processor(ThisPage)
	 * @see #ender(ThisPage)
	 * 
	 * @param requestValue Request object
	 * @param responseValue Response object
	 * @throws IOException
	 */
	public void service (HttpServletRequest requestValue, HttpServletResponse responseValue)
	throws IOException
	{
		ThisPage thisPage = new ThisPage (requestValue, responseValue, getConfig());
		starter(thisPage);
		if (thisPage.getTerminateRequest())
		{ return; }
		processor(thisPage);
		if (thisPage.getTerminateRequest())
		{ return; }
		ender(thisPage);
		if (thisPage.getTerminateRequest())
		{ return; }
		String address = thisPage.getRedirectAddress();
		if (address != null)
		{
			thisPage.getResponse().sendRedirect(thisPage.getResponse().encodeRedirectURL(address));
		}
	}
	/**
	 * Processing before calling processor method.
	 * 
	 * <p>This method can be overridden in subclasses to provide a class
	 *    that carries out the desired operation for a group of classes.</p>
	 * 
	 * @see #processor(ThisPage)
	 * @param thisPage Information on this HTTP transaction
	 * @throws IOException
	 */
	public void starter(ThisPage thisPage) throws IOException
	{ 
		if (thisPage != null)
		{
			thisPage.addMessage("Running Servlet.starter(ThisPage)");
			thisPage.addMessage("This is a dummy stub that doesn't do any work");
		}
	}
	/**
	 * Processing after calling processor method.
	 * 
	 * <p>This method can be overridden in subclasses to provide a class
	 *    that carries out the desired operation for a group of classes.</p>
	 * 
	 * @see #processor(ThisPage)
	 * @param thisPage Information on this HTTP transaction
	 * @throws IOException
	 */
	public void ender(ThisPage thisPage) throws IOException
	{ 
		if (thisPage != null)
		{
			thisPage.addMessage("Running Servlet.ender");
			thisPage.addMessage("This is a dummy stub that doesn't do any work");
		}
	}
	/**
	 * Class specific processing for this class.
	 * 
	 * @param thisPage Information on this HTTP transaction
	 * @throws IOException
	 */
	protected abstract void processor(ThisPage thisPage) throws IOException;
	/**
	 * Construct an SQL date using the year, month, and day
	 * @param yearValue Year (4 digit)
	 * @param monthValue Number of Month (January = 1)
	 * @param dayValue Day of month
	 * @return SQL date
	 */
	protected Date buildDate(String yearValue, String monthValue, String dayValue) 
	{
		int year = -1;
		int month = -1;
		int day = -1;
		Date working = null;
		try
		{
		year = Integer.parseInt(yearValue.trim());
		month = Integer.parseInt(monthValue.trim());
		day = Integer.parseInt(dayValue.trim());
		working = buildDate(year, month, day);
		}
		catch (NumberFormatException e)
		{
			throw new IllegalArgumentException ("buildDate unable to process " + yearValue + ":" + monthValue + ":" + dayValue);
		}
		return working;
	}
	/**
	 * Construct an SQL date using the year, month, and day
	 * @param year Year (4 digit)
	 * @param month Number of month (January = 1)
	 * @param day Day of month
	 * @return SQL date
	 */
	protected Date buildDate(int year, int month, int day) 
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
	 * Obtain date range from HTTP request.
	 * @see Date
	 * @param instance Object containing information for this HTTP request
	 */
	protected void setDateRange(ThisPage instance)
	{
		HttpServletRequest req = instance.getRequest();
		
		   /*
		   * For the constructor for GregorianCalendar, January is
		   * month 0.
		   */
		   Date start = buildDate
		         (req.getParameter("START_YEAR"),
		         req.getParameter("START_MONTH"), 
		         req.getParameter("START_DAY"));
		   Date end = buildDate
		         (req.getParameter("END_YEAR"),
		         req.getParameter("END_MONTH"), 
		         req.getParameter("END_DAY"));
		   instance.setStartDate(start);
		   instance.setEndDate(end);
	}
	/**
	 * Build a date in the java.sql.Date escape format.
	 * @param year Year as an integer
	 * @param month Month as an integer from 1 to 12
	 * @param day Day of month as an integer from 1 to 31
	 * @return Date in escape format
	 * @see Date#toString()
	 * <p>It appears that SQL Server is unable to handle dates before the
	 *    year 1900.</p>
	 * @throws SQLException
	 */
	public static String buildEscapeDate(int year, int month, int day) throws IOException
	{
		if (year < 50)
		{
			year = year + 2000;
		}
		else if (year < 100)
		{
			year = year + 1900;
		}
		if (year < 1900 || year > 3000 || month < 1 || month > 12 || day < 1 || day > 31)
		{
			throw new IOException("Unable to convert year=" + 
					Integer.toString(year) + " month=" +
					Integer.toString(month) + " day=" + Integer.toString(day) + 
					" java.sql.Date escape format");
		}
		NumberFormat format2 =  NumberFormat.getIntegerInstance();
		NumberFormat format4 =  NumberFormat.getIntegerInstance();
		format2.setMinimumIntegerDigits(2);
		format2.setGroupingUsed(false);
		format4.setMinimumIntegerDigits(4);
		format4.setGroupingUsed(false);
		return format4.format(year) + "-" + format2.format(month) + "-" + format2.format(day);
	}
	/**
	 * Build a date in the java.sql.Date escape format.
	 * @param year String containing the year as an integer
	 * @param month String containing the month as an integer from 1 to 12
	 * @param day String containing the day of month as an integer from 1 to 31
	 * @return Date in escape format
	 * @see Date#toString()
	 * @throws IOException
	 */
	public static String buildEscapeDate(String year, String month, String day) throws IOException
	{
		if (year == null)
		{
			throw new IOException("No parameter for year value");
		}
		if (month == null)
		{
			throw new IOException("No parameter for month value");
		}
		if (day == null)
		{
			throw new IOException("No parameter for day value");
		}
		if (year.trim().length() == 0 && month.trim().length() == 0 && day.trim().length() == 0)
		{
			return "0000-00-00";
		}
		int yearValue = -1;
		int monthValue = -1;
		int dayValue = -1;
		try
		{
			yearValue = Integer.parseInt(year.trim());
			monthValue = Integer.parseInt(month.trim());
			dayValue = Integer.parseInt(day.trim());
		}
		catch (Exception e)
		{
			throw new IOException("Unable to convert year=" + 
					year + " month=" +
					month + " day=" + day + 
					" java.sql.Date escape format");
		}
		return buildEscapeDate(yearValue, monthValue, dayValue);
	}
	/**
	 * Generate an SQL escape date.
	 * @param date SQL date
	 * @return SQL escape date
	 */
	public static String buildEscapeDate(java.sql.Date date)
	{
		if (date == null)
		{
			return "0000-00-00";
		}
		else 
		{
			return date.toString();
		}
	}
	/**
	 * Build a date in the java.sql.Date escape format.
	 * @param year Year as an integer
	 * @param month Month as an integer from 1 to 12
	 * @param day Day of month as an integer from 1 to 31
	 * @param defaultDate Date to use if error in processing
	 * @return Date in escape format
	 * @see Date#toString()
	 * <p>It appears that SQL Server is unable to handle dates before the
	 *    year 1900.</p>
	 * @throws SQLException
	 */
	public static String buildEscapeDate(int year, int month, int day, String defaultDate) throws IOException
	{
		if (year < 50)
		{
			year = year + 2000;
		}
		else if (year < 100)
		{
			year = year + 1900;
		}
		if (year < 1900 || year > 3000 || month < 1 || month > 12 || day < 1 || day > 31)
		{
			return defaultDate;
		}
		NumberFormat format2 =  NumberFormat.getIntegerInstance();
		NumberFormat format4 =  NumberFormat.getIntegerInstance();
		format2.setMinimumIntegerDigits(2);
		format2.setGroupingUsed(false);
		format4.setMinimumIntegerDigits(4);
		format4.setGroupingUsed(false);
		return format4.format(year) + "-" + format2.format(month) + "-" + format2.format(day);
	}
	/**
	 * Build a date in the java.sql.Date escape format.
	 * @param year String containing the year as an integer
	 * @param month String containing the month as an integer from 1 to 12
	 * @param day String containing the day of month as an integer from 1 to 31
	 * @param defaultDate Date to use if error in processing
	 * @return Date in escape format
	 * @see Date#toString()
	 * @throws IOException
	 */
	public static String buildEscapeDate(String year, String month, String day, String defaultDate) throws IOException
	{
		if (year == null || month == null || day == null)
		{
			return defaultDate;
		}
		if (year.trim().length() == 0 || month.trim().length() == 0 || day.trim().length() == 0)
		{
			return defaultDate;
		}
		int yearValue = -1;
		int monthValue = -1;
		int dayValue = -1;
		try
		{
			yearValue = Integer.parseInt(year.trim());
			monthValue = Integer.parseInt(month.trim());
			dayValue = Integer.parseInt(day.trim());
		}
		catch (Exception e)
		{
			return defaultDate;
		}
		return buildEscapeDate(yearValue, monthValue, dayValue);
	}
	/**
	 * Break a java.sql.Date escape format date into a set of
	 * string objects for the year, month, and day portions.
	 * @see Date#toString()
	 * @param escapeDate Date to be parsed
	 * @return Array of strings
	 */
	public static String[] parseEscapeDate(String escapeDate)
	{
		String working[] = new String[3];
		if (escapeDate == null)
		{
			working[0] = new String();
			working[1] = new String();
			working[2] = new String();
		}
		else if (escapeDate.equals("0000-00-00"))
		{
			working[0] = " value=\" \" ";
			working[1] = " value=\" \" ";
			working[2] = " value=\" \" ";
		}
		
		else if (escapeDate.trim().length() < 10)
		{
			working[0] = new String();
			working[1] = new String();
			working[2] = new String();
		}
		else
		{
			working[0] = " value=\"" + escapeDate.substring(0, 4) + "\" ";
			working[1] = " value=\"" + escapeDate.substring(5, 7) + "\" ";
			working[2] = " value=\"" + escapeDate.substring(8) + "\" ";
		}	
		return working;
	}

	
	
	/**
	 * Utility method for setting parameters in Prepared Statements.
	 * <p>For CHAR data types.</p>
	 * @param stmt PreparedStatement object
	 * @param location Number of parameter
	 * @param value Value to be loaded in parameter
	 * @throws SQLException
	 */
	protected void loadChar(PreparedStatement stmt, int location, String value) throws SQLException
	{
		if (stmt == null)
		{
			throw new SQLException("PreparedStatement object has null value");
		}
		if (location < 0)
		{
			throw new SQLException("Negative number for parameter number in prepared statement");
		}
		if (value == null)
		{
			stmt.setNull(location, Types.CHAR );
		}
		else if (value.trim().length() == 0)
		{
			stmt.setNull(location, Types.CHAR);
		}
		else
		{
			stmt.setString(location, value);
		}
	}
	/**
	 * Utility method for setting parameters in Prepared Statements.
	 * <p>For VARCHAR data types.</p>
	 * @param stmt PreparedStatement object
	 * @param location Number of parameter
	 * @param value Value to be loaded in parameter
	 * @throws SQLException
	 */
	protected void loadVarchar(PreparedStatement stmt, int location, String value) throws SQLException
	{
		if (value == null)
		{
			stmt.setNull(location, Types.VARCHAR );
		}
		else if (value.trim().length() == 0)
		{
			stmt.setNull(location, Types.VARCHAR);
		}
		else
		{
			stmt.setString(location, value);
		}
	}
	/**
	 * Utility to aid in creating option lists for pull-down
	 * menus.
	 * @param value1 First string to be compared
	 * @param value2 Second string to be compared
	 * @return The word SELECTED if the two strings match, otherwise
	 *         a blank.
	 */
	public static String matchValues(String value1, String value2)
	{
		if (value1 == null || value2 == null)
		{
			return " ";
		}
		else if (value1.equalsIgnoreCase(value2))
		{
			return " selected=\"selected\" ";
		}
		else
		{
			return " ";
		}
	}
	/**
	 * Obtains a value from a result set for use as the value attribute of a
	 * input element of type text.
	 * @param rs Result set
	 * @param name Name of column in result set
	 * @return Text string for value attribute.
	 */
	public static String getTextValue(ResultSet rs, String name)
	{
		try {
			String working = rs.getString(name);
			if (rs.wasNull())
			{	return new String();	}
			else if (working.length() == 0)
			{	return new String();	}
			else 
			{ return "value=\"" + StringHelpers.escapeHTML(working) + "\""; }
		} catch (SQLException e)
		{ return new String();	}
	}
	/**
	 * Obtains a value from a result set for use as the starting value of a
	 * Textarea element.
	 * @see StringHelpers#escapeHTML(String, String)
	 * @param rs Result set
	 * @param name Name of column in result set
	 * @return Text string for Textarea tag.
	 */
	public static String getTextareaValue(ResultSet rs, String name)
	{
		try 
		{
			String working = rs.getString(name);
			if (rs.wasNull())
			{ return new String(); }
			else if (working.trim().length() == 0)
			{  return new String(); }
			else 
			{ return StringHelpers.escapeHTML(working, "TEXTAREA"); }
		} 
		catch (SQLException e)
		{ return new String(); }
	}
	/**
	 * Returns the string for a non-breaking space if a string
	 * column in a table is null or blank, otherwise returns an
	 * escaped string.
	 * @param rs Result set containing value
	 * @param name Name of column
	 * @return Processed string for insertion in web page
	 */
	public static String getString(ResultSet rs, String name)
	{
		try
		{
			String working = rs.getString(name);
			if (rs.wasNull())
			{ return "&nbsp;"; }
			else if (working.trim().length() == 0)
			{ return "&nbsp;"; }
			else
			{ return StringHelpers.escapeHTML(working); }
		}
		catch (SQLException e)
		{
			return new String(); 
		}
	}
	/**
	 * Internal debugging aid for checking parseEscapeDate.
	 * @param value String to be parsed
	 * @see #parseEscapeDate(String)
	 */
	protected static void innerTest(String value)
	{
		String test[] = parseEscapeDate(value);
		System.out.print(value);
		for (int i = 0; i < test.length; i++)
		{
			System.out.print(" " + test[i]);
		}
		System.out.println();
	}
	/**
	 * This was inserted for testing of some of the internal static methods
	 * and is only for debugging purposes.
	 * 
	 * @param args Not used
	 */
	public static void main(String args[])
	{
		String test[] = { "2009-09-09", "0000-00-00"};
		for (int i = 0; i < test.length; i++)
		{ innerTest(test[i]); }
	}
}
