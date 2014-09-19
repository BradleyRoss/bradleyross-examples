package bradleyross.j2ee.servlets;
import java.io.*;
import java.sql.SQLException;
import java.sql.Types;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import javax.servlet.*;
import javax.servlet.http.*;
import bradleyross.library.database.DatabaseProperties;
/**
 * Serves as a template for writing servlets that
 * display the contains of a java.sql.ResultSet object
 * as a CSV (comma separated values) page.
 * 
 * <p>The methods getResultSet and makeConnection are
 *    abstract and will need to be overridden before
 *    this class can be used.  The user may also wish to
 *    override several of the methods for formatting
 *    information.</p>
 * <p>This code needs to be changed since many of the fields that belong
 *    to the instance must be moved within the service method since there
 *    can be multiple copies of the service method running at the same time.</p>
 * @see #getResultSet(HttpServletRequest, DatabaseProperties)
 * @author Bradley Ross
 */
public abstract class CsvWriter extends HttpServlet
{
	/**
	 * Provided for compliance with Serializable interface.
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * ServletConfig object as passed to the init method.
	 * <p>Must be moved to inner class</p>
	 */
	protected ServletConfig config = null;
	/**
	 * Called by the servlet container when the servlet object is first placed
	 * in service.
	 */
	public void init(ServletConfig configIn) throws ServletException
	{ 
		super.init(configIn); 
		config = configIn;
	}
	/**
	 * Called by the servlet container when the servlet object is being taken out
	 * of service.
	 */
	public void destroy()
	{ super.destroy(); }
	/**
	 * Encapsulates methods and properties for a given instance of the
	 * service method.
	 * 
	 * <p>Methods and properties in this class will normally not be
	 *    overridden.</p>
	 * @author Bradley Ross
	 *
	 */
	protected class Instance extends Object
	{
		/**
		 * Object containing HTTP request.
		 */
		protected HttpServletRequest request = null;
		/**
		 * Object used for preparing HTTP response.
		 */
		protected HttpServletResponse response = null;
		public Instance (HttpServletRequest req, HttpServletResponse res)
		{
			request = req;
			response = res;
			try 
			{
				output = res.getWriter();
			}
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
		/**
		 * File name to be used when creating file.  File will be
		 * suffixed with .csv.
		 * <p>Changes to this field can be made using the setValues method of the
		 *    enclosing class.
		 *    </p>
		 * @see #setOutputFilename(String)
		 * @see #getOutputFilename()
		 * @see CsvWriter#setValues(Instance)
		 * 
		 */
		protected String outputFilename = "default";
		/**
		 * String to be placed in CSV file as the first line in the file.
		 * <p>Changes to this field can be made by using the setValues
		 *    method of the enclosing class.</p>
		 * @see CsvWriter#setValues(Instance)
		 * 
		 */
		protected String message = null;
		/**
		 * Object containing database connection information.
		 * <p>This field is set by the makeConnection method.</p>
		 * @see CsvWriter#makeConnection()
		 */
		protected bradleyross.library.database.DatabaseProperties data = null;
		/**
		 * Object containing the result set.
		 * <p>This object is created by the getResultSet method which
		 *    is called by the service method.</p>
		 * @see CsvWriter#getResultSet(HttpServletRequest, DatabaseProperties)
		 */
		protected java.sql.ResultSet rs = null;
		/**
		 * Object containing meta data on result set.
		 * <p>This object is created by the service method after the
		 *    execution of the getResultSet method and before the
		 *    execution of the printHeader method.</p>
		 *    <p>Must be moved to inner class</p>
		 */
		protected java.sql.ResultSetMetaData rsmd = null;  
		/**
		 * Object representing the output to the HTTP response.
		 */
		protected java.io.PrintWriter output = null;
		/**
		 * Number of columns in result set
		 */
		protected int columnCount;
		/**
		 * Getter for rs.
		 * @return ResultSet object
		 * @see #rs
		 */
		public ResultSet getResultSet()
		{
			return rs;
		}
		/**
		 * Setter for rs.
		 * <p>This would not normally be called from outside classes.</p>
		 * @param value Value to be used for field
		 * @see #rs
		 */
		public void setResultSet(ResultSet value)
		{
			try 
			{
				rs = value;
				rsmd = rs.getMetaData();
				columnCount = rsmd.getColumnCount();
			} 
			catch (SQLException e) 
			{

				e.printStackTrace();
			}
		}
		/**
		 * Getter for rsmd.
		 * 
		 * @return ResultSetMetaData object
		 * @see #rsmd
		 */
		public ResultSetMetaData getResultSetMetaData()
		{
			return rsmd;
		}
		/**
		 * Setter for rsmd.
		 * 
		 * <p>This would not normally be called by external classes.</p>
		 * 
		 * @param value ResultSetMetaData object
		 * @see #rsmd
		 */
		public void setResultSetMetaData(ResultSetMetaData value)
		{
			rsmd = value;
		}
		public DatabaseProperties getDatabaseProperties()
		{
			return data;
		}
		public void setDatabaseProperties (DatabaseProperties value)
		{
			data = value;
		}
		public String getOutputFilename()
		{ 
			return outputFilename;
		}
		public void setOutputFilename(String value)
		{
			outputFilename = value;
		}
		/**
		 * Obtains number of columns in result set
		 * @return Number of columns
		 * @see #columnCount
		 */
		public int getColumnCount()
		{
			return columnCount;
		}
		/**
		 * Sets number of columns in result set.
		 * <p>This method would not normally be used.</p>
		 * @param value Value to be used
		 * @see #columnCount
		 */
		public void setColumnCount(int value)
		{
			columnCount = value;
		}
		/**
		 * Getter for output.
		 * @return PrintWriter object.
		 * @see #output
		 */
		public PrintWriter getPrintWriter()
		{
			return output;
		}
		/**
		 * Getter for message.
		 * 
		 * @return Message text
		 * @see #message
		 */
		public String getMessage()
		{ 
			return message; 
		}
		/**
		 * Getter for request.
		 * 
		 * @return HttpServletRequest object
		 * @see #request
		 */
		public HttpServletRequest getRequest()
		{
			return request;
		}
		/**
		 * Getter for response.
		 * @return HttpServletResponse object
		 * @see #response
		 */
		public HttpServletResponse getResponse()
		{
			return response;
		}
	}
	/**
	 * Called by servlet container when an HTTP transaction requests a response
	 * from the server.
	 * 
	 * @param req Object containing request information
	 * @param res Object containing response information
	 */
	public void service (HttpServletRequest req,
			HttpServletResponse res) throws IOException
			{
		Instance instance = new Instance(req, res);
		PrintWriter output = instance.getPrintWriter();
		try
		{
			instance.setDatabaseProperties(makeConnection());
			instance.getDatabaseProperties().connect();
			ResultSet rs = getResultSet(req, instance.getDatabaseProperties());
			instance.setResultSet(rs);
			setValues(instance);
			res.setContentType("text/csv");
			res.setHeader("Content-Disposition", "attachment;filename=" +
					instance.getOutputFilename() + ".csv");
			if (instance.getMessage() != null)
			{ output.print(instance.getMessage() + "\r\n"); }
			if (rs == null)
			{ 
				output.print("No result set provided\r\n"); 
				return;
			}
			boolean moreRecords = rs.next();
			if (!moreRecords)
			{
				output.print("Result set is empty\r\n");
				return;
			}
			else 
			{ printHeader(instance); }
			while (true)
			{
				printLine(instance);
				moreRecords = rs.next();
				if (!moreRecords) { break; }
			}
			instance.getResultSet().close();
			instance.getDatabaseProperties().close();
		}
		catch (java.io.IOException e)
		{ throw new java.io.IOException("Error in processing file: " +
				e.getClass().getName() + " : " + e.getMessage()); }
		catch (java.sql.SQLException e)
		{ 
			System.out.println("SQL error in processing CSV file");
			throw new java.io.IOException("SQL error: " + e.getClass().getName() + 
					" : " + e.getMessage());
		}
			}
	/**
	 * Obtain the database connection information.
	 */
	protected abstract bradleyross.library.database.DatabaseProperties
	makeConnection() throws IOException;
	/**
	 * Will be overridden with the method for obtaining the SQL
	 * result set.
	 * <p>This should be kept separate from the getResultSet method within the
	 *    Instance class. The {@link Instance#getResultSet() } method returns the
	 *    ResultSet object stored in the Instance object.  This method actually generates
	 *    the ResultSet object.  The service method then stores the result of this method 
	 *    in the Instance object.</p>
	 * @param req Request information
	 * @return Result set
	 * @see Instance#message
	 * @see Instance#outputFilename
	 */
	protected abstract java.sql.ResultSet getResultSet(HttpServletRequest req, DatabaseProperties data)
	throws java.io.IOException;
	/**
	 * Sets values in Instance object.
	 * <p>This must be overridden in a subclass in order to set parameters.</p>
	 * @param instance Instance object being affected
	 * @throws java.io.IOException
	 */
	protected void setValues(Instance instance) throws java.io.IOException
	{ ; }
	/**
	 * Will print header line for CSV file.
	 */
	protected void printHeader (Instance instance)
	throws java.io.IOException
	{
		ResultSetMetaData rsmd = instance.getResultSetMetaData();
		PrintWriter output = instance.getPrintWriter();
		try
		{
			for (int i = 1; i <= rsmd.getColumnCount(); i++)
			{
				if (i != 1) { output.print(","); }
				output.print(rsmd.getColumnName(i));
			}
			output.print("\r\n");
		}
		catch (Exception e)
		{ throw new java.io.IOException("Error detected: " + e.getClass().getName() +
				" : " + e.getMessage()); }
	}
	/**
	 * Will print one line of CSV file.
	 * <p>This method will be changed at a later time to allow the
	 *    specification of overriding methods for formatting timestamps,
	 *    integers, floating point numbers, character strings, etc.</p>
	 */
	protected void printLine (Instance instance)
	throws java.io.IOException
	{
		int columnCount = 0;
		ResultSet rs = instance.getResultSet();
		ResultSetMetaData rsmd = instance.getResultSetMetaData();
		PrintWriter output = instance.getPrintWriter();
		try 
		{
			columnCount = rsmd.getColumnCount();
		} 
		catch (SQLException e1) 
		{
			e1.printStackTrace();
		}
		for (int i = 1; i <= columnCount; i++)
		{
			try
			{
				if (i != 1) { output.print(","); } 
				int columnType = rsmd.getColumnType(i);
				if (columnType == Types.VARCHAR || columnType == Types.CHAR ||
						columnType == Types.LONGVARCHAR)
				{
					String working = rs.getString(i);
					if (rs.wasNull()) { working = new String(); }
					output.print(formatString(working, i)); 
				}
				else if (columnType == Types.FLOAT || columnType == Types.REAL ||
						columnType == Types.DOUBLE)
				{
					double working = rs.getDouble(i);
					if (rs.wasNull()) 
					{ output.print(new String()); } 
					else
					{ output.print(formatDouble(working)); }
				}
				else if (columnType == Types.TINYINT || columnType == Types.SMALLINT ||
						columnType == Types.INTEGER || columnType == Types.BIGINT)
				{
					int working = rs.getInt(i);
					if (rs.wasNull())
					{ output.print(new String()); }
					else
					{ output.print(formatInteger(working)); }
				}
				else if (columnType == Types.TIMESTAMP)
				{ 
					java.sql.Timestamp working = rs.getTimestamp(i);
					if (rs.wasNull())
					{ output.print(new String()); }
					else
					{
						java.util.Date date = new java.util.Date(working.getTime());
						output.print(formatDateTime(date)); 
					}
				}
				else
					/*
					 ** Haven't decided how to handle Types.DECIMAL and
					 ** TYPES.GENERIC.  However, this may be academic as I don't
					 ** think that these will occur in the databases
					 ** that I will be using.
					 */
					 {
					String working = rs.getString(i);
					if (rs.wasNull())
					{ output.print(new String()); }
					else
					{ output.print(working); }
					 }
			}
			catch (Exception e)
			{ throw new java.io.IOException("Error detected in column " +
					Integer.toString(i) + ":" +
					e.getClass().getName() + " : " +
					e.getMessage()); }
		}
		try
		{ output.print("\r\n"); }
		catch (Exception e)
		{ throw new java.io.IOException("Error detected: " +
				e.getClass().getName() + " : " +
				e.getMessage()); }
	}
	/**
	 * Method of calling formatString when it doesn't
	 * depend on the column in the result set.
	 * @param valueIn String to be processed
	 * @return Processed string
	 */
	protected final String formatString(String valueIn)
	{ return formatString(valueIn, -1); }
	/**
	 * Double up on all double quotes in string and surround the string with
	 * double quotes so that it will be in the proper format for the CSV file.
	 * @param valueIn String to be processed
	 * @param column Column number of item - This will allow different formatting to be used
	 *        for each column.  This parameter is not used unless the method is
	 *        overridden.
	 * @return Processed string
	 */
	protected String formatString(String valueIn, int column)
	{
		String working = null;
		if (valueIn == null)
		{ working = " "; }
		else if (valueIn.length() == 0)
		{ working = " "; }
		else
		{
			java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("\\\"");
			java.util.regex.Matcher matcher = pattern.matcher(valueIn);
			working = matcher.replaceAll("\"\"");
		}
		if (working == null) { working = " "; }
		return "\"" + working + "\"";
	}
	/**
	 * Alternate calling sequence, leaving out the column number.
	 * @param valueIn Floating point number to be formatted
	 * @return Formatted string
	 */
	protected final String formatDouble (double valueIn)
	{ return formatDouble (valueIn, -1); }
	/**
	 * Formatter for floating point numbers.
	 * <p>This method can be overridden to control printing of
	 *    the values in the CSV file.</p>
	 * @param valueIn Floating point number to be processed
	 * @param column Column number - This will allow different formatting to be used
	 *        for each column.  This parameter is not used unless the method
	 *        is overridden.
	 * @return Formatted string
	 */
	protected String formatDouble (double valueIn, int column)
	{ return Double.toString(valueIn); }
	/**
	 * Alternate calling sequence, omitting the column number.
	 * @param valueIn Integer value to be formatted
	 * @return Formatted string
	 */
	protected final String formatInteger (int valueIn)
	{ return formatInteger(valueIn, -1); }
	/**
	 * Formatter for integer values.
	 * <p>This method can be overridden to control printing of
	 *    the values in the CSV file.</p>
	 * @param valueIn Integer to be formatted
	 * @param column Column number - This will allow different formatting to be used
	 *        for each column.  This parameter is not used unless the method is 
	 *        overridden.
	 * @return Formatted string
	 */
	protected String formatInteger (int valueIn, int column)
	{ return Integer.toString(valueIn); }
	/**
	 * Alternate calling sequence, leaving out the column number
	 * @param valueIn java.util.Date object containing date and time
	 * @return Formatted string
	 */
	protected final String formatDateTime (java.util.Date valueIn)
	{ return formatDateTime(valueIn, 1); }
	/**
	 * Formatter for values involving date and time.
	 * <p>This method can be overridden to control printing of
	 *    the values in the CSV file.</p>
	 * @param valueIn java.util.Date object containing date and time
	 * @param column Column number - This will allow different formatting to be used
	 *        for each column.  This parameter is not used unless the method is
	 *        overridden.
	 * @return Formatted string
	 */
	protected String formatDateTime (java.util.Date valueIn, int column)
	{
		java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm");
		return format.format(valueIn);
	}
}