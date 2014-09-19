package bradleyross.j2ee.servlets;
import java.io.*;
import java.sql.Types;
import javax.servlet.*;
import javax.servlet.http.*;
/**
* Serves as a template for writing servlets that
* display the contains of a java.sql.ResultSet object
* as a CSV (comma separated values) page.
* <p>The methods getResultSet and makeConnection are
*    abstract and will need to be overridden before
*    this class can be used.  The user may also wish to
*    override several of the methods for formatting
*    information.</p>
* <p>This is the class that is currently used by
*    com.amtrak.eng.servlets.downloadDataServlet and
*    com.amtrak.eng.tests.testServlet.  After
*    bradleyross.library.servlets.csvWriter is modified
*    to make the variables local to the service method,
*    the classes in com.amtrak.eng should be modified to
*    make use of that version.</p>
* @author Bradley Ross
* @deprecated
*/
public abstract class csvWriter2 extends HttpServlet
   {
   /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
/**
   * File name to be used when creating file.  File will be
   * suffixed with .csv.
   * <p>Changes to this field can be placed in the method
   *    getResultSet.</p>
   * @see #getResultSet(javax.servlet.http.HttpServletRequest)
   */
   protected String outputFilename = "default";
   /**
   * String to be placed in CSV file as the first line in the file.
   * <p>Changes to this field can be placed in the method
   *    getResultSet.</p>
   * @see #getResultSet(javax.servlet.http.HttpServletRequest)
   */
   protected String message = null;
   /**
   * Object containing database connection information.
   * <p>This field is set by the makeConnection method.</p>
   * @see #makeConnection()
   */
   protected bradleyross.library.database.DatabaseProperties data = null;
   /**
   * Object containing the result set.
   * <p>This object is created by the getResultMethod method which
   *    is called by the service method.
   */
   protected java.sql.ResultSet rs = null;
   /**
   * Object containing meta data on result set.
   * <p>This object is created by the service method after the
   *    execution of the getResultSet method and before the
   *    execution of the printHeader method.</p>
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
   * ServletConfig object as passed to the init method.
   */
   protected ServletConfig config = null;
   public void init(ServletConfig configIn) throws ServletException
      { 
      super.init(configIn); 
      config = configIn;
      }
   public void destroy()
      { super.destroy(); }
   /**
   * @param req Object containing request information
   * @param res Object containing response information
   */
   public void service (HttpServletRequest req,
      HttpServletResponse res) throws IOException
      {
      try
         {
         data = makeConnection();
         data.connect();
         output = res.getWriter();
         rs = getResultSet(req);
         res.setContentType("text/csv");
         res.setHeader("Content-Disposition", "attachment;filename=" +
            outputFilename + ".csv");
         rsmd = rs.getMetaData();
         if (message != null)
            { output.print(message + "\r\n"); }
         columnCount = rsmd.getColumnCount();
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
            { printHeader(); }
         while (true)
            {
            printLine();
            moreRecords = rs.next();
            if (!moreRecords) { break; }
            }
         rs.close();
         data.close();
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
   * <p>Changes to fields such as message and outputFilename can go in this
   *    method.</p>
   * @param req Request information
   * @return Result set
   * @see #message
   * @see #outputFilename
   */
   protected abstract java.sql.ResultSet getResultSet(HttpServletRequest req)
         throws java.io.IOException;
   /**
   * Will print header line for CSV file.
   */
   protected void printHeader ()
         throws java.io.IOException
      {
      try
         {
         for (int i = 1; i <= columnCount; i++)
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
   protected void printLine ()
         throws java.io.IOException
      {
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
   protected String formatString(String valueIn)
      { return formatString(valueIn, 1); }
   /**
   * Double up on all double quotes in string and surround the string with
   * double quotes so that it will be in the proper format for the CSV file.
   * @param valueIn String to be processed
   * @param column Column number of item (not used unless method overriden)
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
   protected String formatDouble (double valueIn)
      { return formatDouble (valueIn, 1); }
   /**
   * Formatter for floating point numbers.
   * <p>This method can be overridden to control printing of
   *    the values in the CSV file.</p>
   * @param valueIn Floating point number to be processed
   * @param column Column number
   * @return Formatted string
   */
   protected String formatDouble (double valueIn, int column)
      { return Double.toString(valueIn); }
   /**
   * Alternate calling sequence, omitting the column number.
   * @param valueIn Integer value to be formatted
   * @return Formatted string
   */
   protected String formatInteger (int valueIn)
      { return formatInteger(valueIn, 1); }
   /**
   * Formatter for integer values.
   * <p>This method can be overridden to control printing of
   *    the values in the CSV file.</p>
   * @param valueIn Integer to be formatted
   * @param column Column number
   * @return Formatted string
   */
   protected String formatInteger (int valueIn, int column)
      { return Integer.toString(valueIn); }
   /**
   * Alternate calling sequence, leaving out the column number
   * @param valueIn java.util.Date object containing date and time
   * @return Formatted string
   */
   protected String formatDateTime (java.util.Date valueIn)
      { return formatDateTime(valueIn, 1); }
   /**
   * Formatter for values involving date and time.
   * <p>This method can be overridden to control printing of
   *    the values in the CSV file.</p>
   * @param valueIn java.util.Date object containing date and time
   * @param column Column number
   * @return Formatted string
   */
   protected String formatDateTime (java.util.Date valueIn, int column)
      {
      java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm");
      return format.format(valueIn);
      }
   }