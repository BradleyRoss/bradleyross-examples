package bradleyross.j2ee.servlets;
import bradleyross.library.database.DatabaseProperties;
import bradleyross.library.helpers.GenericPrinter;
import bradleyross.library.helpers.StringHelpers;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.DatabaseMetaData;
import java.sql.Types;
import java.sql.Driver;
import java.util.Vector;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * Servlet for showing database schema information using
 *  {@link DatabaseMetaData}.
 * 
 * <p>This was an earlier attempt to do an autodocumenter for
 *    database schemas.  There was a later version that also listed indices
 *    and foreign keys and used better formatting, and I'm trying to locate it.</p>
 * 
 * <p>It should be noted that SQL Server does not have a BOOLEAN
 *    type but uses an integer value instead for result sets
 *    containing boolean data.  A value of 0 indicates false
 *    while a value of 1 indicates true.<p>
 * 
 * <p>The servlet uses the init parameters class, system, and account.</p>
 * <ul>
 * <li>class is the name of the class that is a subclass of 
 *     {@link DatabaseProperties} that serves to connect the databases.</li>
 * <li>system is the identifier for the database to be used.</li>
 * <li>account is the name of the account to be used.</li>
 * </ul>
 * 
 * @author Bradley Ross
 * @see DatabaseMetaData#getCrossReference(String, String, String, String, String, String)
 * @see DatabaseMetaData#getIndexInfo(String, String, String, boolean, boolean)
 * @see DatabaseMetaData#getTables(String, String, String, String[])
 * @see DatabaseMetaData#getImportedKeys(String, String, String)
 * @see DatabaseMetaData#getExportedKeys(String, String, String)
 */
@SuppressWarnings("serial")
public class ShowSchema extends HttpServlet 
{
	/**
	 * True if servlet was correctly instantiated.
	 */
	protected boolean isValid = true;
	/**
	 * Name of class for handling database.
	 */
	protected String databaseClassName = null;
	/**
	 * Class for handling database.
	 */
	protected Class<?> databaseClass = null;
	/**
	 * Name of system for database connection.
	 */
	protected String databaseSystem = null;
	/**
	 * Description of database.
	 */
	protected String databaseDescription = null;
	/**
	 * Name of account to be used for database connection.
	 */
	protected String databaseAccount = null;
	/**
	 * List of warning or error messages generated by this application.
	 */
	protected Vector<String> errorMessages = new Vector<String>();
	/**
	 * List of informative messages generated by this application.
	 */
	protected Vector<String> infoMessages = new Vector<String>();
	/**
	 * Generates an error message for the application.
	 * @param title Text to appear in title
	 * @param message Text to appear in body of message
	 * @return HTML page
	 */
	public String buildErrorMessage(String title, String message)
	{
		StringWriter writer = new StringWriter();
		GenericPrinter output = new GenericPrinter(writer);
		output.println("<html><head>");
		output.println("<title>" + StringHelpers.escapeHTML(title) + "</title>");
		output.println("</head><body");
		output.println("<h1>" + StringHelpers.escapeHTML(title) + "</h1>");
		output.println("<p>" + StringHelpers.escapeHTML(message) + "</p>");
		if (errorMessages.size() > 0)
		{
			output.println("<h2>Possible Problems</h2>");
			output.println("<ul>");
			for (int i = 0; i < errorMessages.size(); i++)
			{
				output.println("<li>" + StringHelpers.escapeHTML(errorMessages.elementAt(i)) 
						+ "</li>");
			}
			output.println("</ul");
		}
		if (infoMessages.size() > 0)
		{
			output.println("<h2>Messages</h2>");
			output.println("<ul>");
			for (int i = 0; i < infoMessages.size(); i++)
			{
				output.println("<li>" + StringHelpers.escapeHTML(infoMessages.elementAt(i)) 
						+ "</li>");
			}
			output.println("</ul>");
		}
		output.println("</body></html>");
		String result = writer.toString();
		output.close();
		return result;
	}

	/**
	 * Determines whether an item in the database is to be included in the
	 * reports.
	 * 
	 * <p>When dealing with SQLServer, dbo.dtproperties was showing up on the list
	 *    of tables even though it was a system table.  The other system tables
	 *    weren't appearing.</p>
	 * 
	 * @param data Object describing data connection
	 * @param catalog Name of catalog for item in database
	 * @param schema Name of schema for item in database
	 * @param item Name of item in database
	 * @return True if item is to be included in report
	 */
	protected boolean isIncluded(DatabaseProperties data, String catalog, String schema, String item)
	{
		String database = data.getDbms();
		if (database == null)
		{
			return true;
		}
		else if (database.equalsIgnoreCase("sqlserver"))
		{
			try
			{
				String schemaTest = null;
				if (schema == null)
				{
					schemaTest = new String();
				}
				else
				{
					schemaTest = schema;
				}
				if (schemaTest.equalsIgnoreCase("dbo") && item.equalsIgnoreCase("dtproperties"))
				{
					return false;
				}
				else if (schemaTest.equalsIgnoreCase("sys"))
				{
					return false;
				}
				else if (schemaTest.equalsIgnoreCase("INFORMATION_SCHEMA"))
				{
					return false;
				}
				else
				{
					return true;
				}
			}
			catch (Exception e)
			{
				return true;
			}
		}
		else
		{
			return true;
		}
	}
	/**
	 * Default constructor.
	 * 
	 * <p>The default constructor is used by the Tomcat application
	 *    server when setting up the servlet.  The default 
	 *    constructor must be explicitly defined since there
	 *    is an explicit constructor for the class.</p>
	 */
	public ShowSchema()
	{ ; }
	/**
	 * Constructor to allow the methods to be used by a stand-alone
	 * application.
	 * @param className Name of class for defining database connections.
	 * @param system Database system for which connection is to be made.
	 * @param account Account name to be used in connecting to the
	 *        database.
	 */
	public ShowSchema(String className, String system, String account)
	{
		isValid = true;
		databaseClassName = className;
		databaseSystem = system;
		databaseAccount = account;
		try
		{
			databaseClass = Class.forName(className);
		}
		catch (ClassNotFoundException e)
		{
			errorMessages.add("Unable to find class " + className);
			isValid = false;
			return;
		}
		if (!DatabaseProperties.class.isAssignableFrom(databaseClass))
		{
			errorMessages.add("Class " + className + " is not subclass of DatabaseProperties");
			isValid = false;
			return;
		}
		infoMessages.add("Initialization with ShowSchema complete: " +
				className + ", " + system + ", " + account);
	}
	/**
	 * Obtain a connection to the database based on the initialization
	 * parameters for the servlet.
	 * 
	 * @see #databaseAccount
	 * @see #databaseSystem
	 * @see #databaseClassName
	 * @return Database connection information
	 */
	protected DatabaseProperties getData()
	{
		DatabaseProperties data = null;
		if (!isValid)
		{
			return null;
		}
		try 
		{
			data = (DatabaseProperties) databaseClass.newInstance();
		} 
		catch (InstantiationException e) 
		{
			errorMessages.add("Error while instantiating DatabaseProperties object");
			errorMessages.add(e.getClass().getName() + " " + e.getMessage());
			isValid = false;
		} 
		catch (IllegalAccessException e) 
		{
			errorMessages.add("Error while instantiating DatabaseProperties object");
			errorMessages.add(e.getClass().getName() + " " + e.getMessage());
			isValid = false;
		}
		data.setDatabaseInstance(databaseSystem, databaseAccount);
		try 
		{
			data.connect();
		} 
		catch (SQLException e) 
		{
			errorMessages.add("Error while opening database connection");
			errorMessages.add(data.showAttributes());
			errorMessages.add(e.getClass().getName() + " " + e.getMessage());
			isValid = false;
		}
		if (!isValid)
		{
			return null;
		}
		infoMessages.add("Database connection has been opened");
		return data;
	}
	/**
	 * Initializes object for use with servlets.
	 * <p>The following servlet configuration items are used.</p>
	 * <ul>
	 * <li>class - Subclass of DatabaseProperties that is used to connect with the
	 *             database.</li>
	 * <li>system</li>
	 * <li>account</li>
	 * </ul>
	 * @param configIn Servlet configuration object
	 * 
	 */
	public void init(ServletConfig configIn) throws ServletException
	{ 
		isValid = true;
		try
		{
			databaseClassName = configIn.getInitParameter("class");
			databaseSystem = configIn.getInitParameter("system");
			databaseAccount = configIn.getInitParameter("account");
			databaseClass =  Class.forName(databaseClassName);
		}
		catch (ClassNotFoundException e)
		{
			isValid = false;
			return;
		}
		if (!DatabaseProperties.class.isAssignableFrom(databaseClass))
		{
			errorMessages.add("Class " + databaseClassName + " is not subclass of DatabaseProperties");
			isValid = false;
			return;
		}
	}
	/** 
	 * Place error message on web page if exception is encountered.
	 * 
	 * @param e Exception
	 * @param output Output device for servlet writer
	 */
	protected void ProblemFound (Exception e, GenericPrinter output, String message)
	{
		output.println("<html><head>");
		output.println("<title>Error encountered in processing database</title>");
		output.println("</head><body>");
		output.println("<h1>Problem encountered in connecting to database</h1>");
		output.println("<p>" + StringHelpers.escapeHTML(message) + "</p>");
		output.println("<p>System name is " + databaseSystem + "</p>");
		output.println("<p>Account name is " + databaseAccount + "</p>");
		output.println("<p>" + StringHelpers.escapeHTML(e.getClass().getName()) + " " + 
				StringHelpers.escapeHTML(e.getMessage()) + "</p>");
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		output.println("<p>" + StringHelpers.escapeHTML(sw.toString()) + "</p>");
		output.println("</body></html>");
	}	
	/**
	 * This is the method that is executed each time a request is made for the
	 * web page.
	 * 
	 * @param req Request information
	 * @param res Response information
	 * @throws IOException
	 */
	public void service (HttpServletRequest req,
			HttpServletResponse res) throws IOException
			{
		// String rootName = req.getContextPath() + req.getServletPath();
		DatabaseProperties data = null;
		PrintWriter writer = null;
		GenericPrinter generic = null;
		String pathInfo = null;
		res.setContentType("text/html");
		writer = res.getWriter();
		generic = new GenericPrinter(writer);
		pathInfo = req.getPathInfo();

		try
		{
			data = (DatabaseProperties) databaseClass.newInstance();
			data.setDatabaseInstance(databaseSystem, databaseAccount);	
			data.connect();
			databaseDescription = data.getSystemDescription();
			if (databaseDescription == null)
			{
				databaseDescription = new String();
			}
		}
		catch (InstantiationException e)
		{
			ProblemFound(e, generic, "Problem setting up database connection: " +
					"InstantiationException");
			return;
		}
		catch (IllegalAccessException e)
		{
			ProblemFound(e, generic, "Problem setting up database connection: " +
					"IllegalAccessException");
			return;
		}
		catch (SQLException e)
		{
			ProblemFound(e, generic, "Problem setting up database connection: " +
					"SQLException");
			return;
		}
		/*
		 * Now that the page is open, carry out the appropriate action for
		 * writing the web page.
		 */
		if (pathInfo == null || pathInfo.length() == 0 || pathInfo.equals("/") ||
				pathInfo.equalsIgnoreCase("/main.do"))
		{
			// generic.print(buildRootPage(data, rootName, debugLevel));
			processRootPage(data, generic, req, res);
		}
		else if (pathInfo.equalsIgnoreCase("/listtables") ||
				pathInfo.equalsIgnoreCase("/listtables.do"))
		{
			processListTables(data, generic, req, res);
		}
		else if (pathInfo.equalsIgnoreCase("/listviews") ||
				pathInfo.equalsIgnoreCase("/listviews.do"))
		{
			processListViews(data, generic, req, res);
		}
		else if (pathInfo.equalsIgnoreCase("/listprocedures") ||
				pathInfo.equalsIgnoreCase("/listprocedures.do"))
		{
			processListProcedures(data, generic, req, res);
		}
		else if (pathInfo.equalsIgnoreCase("/databaseinformation")||
				pathInfo.equalsIgnoreCase("/databaseinformation.do"))
		{
			processDatabaseInformation(data, generic, req, res);
		}
		else if (pathInfo.equalsIgnoreCase("/listudts") ||
				pathInfo.equalsIgnoreCase("/listudts.do"))
		{
			processUDTInformation(data, generic, req, res);
		}
		else if (pathInfo.equalsIgnoreCase("/describetable") ||
				pathInfo.equalsIgnoreCase("/describetable.do"))
		{
			processDescribeTable(data, generic, req, res);
		}
		else
		{
			//			generic.print(buildRootPage(data, rootName, debugLevel));
			processRootPage(data,generic, req, res);			
		}				
		/*
		 * Close the database now that the page is complete.
		 */
		try
		{
			data.close();
		}
		catch (SQLException e)
		{
			writer.println("<p>Problem closing database connection</p>");
			writer.println("<p>" + 
					StringHelpers.escapeHTML(e.getClass().getName() + " " + e.getMessage()) +
					"</p>");
		}
		writer.println("</body></html>");
			}
	/**
	 * Generate the root page for schema information for the database.
	 * @param data Object describing database connection
	 * @param output Object for writing web page
	 * @param req HTTP request information
	 * @param res HTTP response information
	 * @throws IOException
	 */
	protected void processRootPage(DatabaseProperties data, GenericPrinter output,
			HttpServletRequest req, HttpServletResponse res) throws IOException
			{
		String rootName = req.getContextPath() + req.getServletPath();
		output.println("<html><head>");
		output.println("<title>Database Information</title>");
		output.println("</head><body>");
		output.println("<p>The root name is" + rootName + "</p>");
		output.println("<h1>Database Schema</h1>");
		output.println("<ul>");
		output.println("<li><a href=\"" + rootName + "/DatabaseInformation\">Database Information</a></li>");
		output.println("<li><a href=\"" + rootName + "/ListTables\">List Tables</a></li>");
		output.println("<li><a href=\"" + rootName + "/ListViews\">List Views</a></li>");
		output.println("<li><a href=\"" + rootName + "/ListProcedures\">List Procedures</a></li>");
		output.println("<li><a href=\"" + rootName + "/ListUDTs\">List User Defined Types (UDT)</a></li>");
		output.println("</ul>");
		output.println("<p>DBMS is " + data.getDbms());
		output.println("<p>System is " + data.getSystem() + "</p>");
		output.println("<p>" + StringHelpers.escapeHTML(data.showAttributes()) + "</p>");
		output.println("The URL's for this process start with " +
				req.getContextPath() + req.getServletPath());
			}
	/**
	 * Generate the list of tables in the database.
	 * @param data Object describing database connection
	 * @param output Object for writing web page
	 * @param req HTTP request information
	 * @param res HTTP response information
	 * @throws IOException
	 */
	protected void processListTables(DatabaseProperties data, GenericPrinter output,
			HttpServletRequest req, HttpServletResponse res) throws IOException
			{
		int counter = 0;
		DatabaseMetaData meta = null;
		ResultSet rs = null;
		String list[] = { "TABLE" };

		String rootName = req.getContextPath() + req.getServletPath();

		output.println("<html><head>");
		output.println("<title>" + rootName + "/ListTables</title>");
		output.println("</head><body>");
		output.println("<h1>List of Tables</h1>");
		if (databaseDescription.length() > 0)
		{
			output.println("<h2>" + databaseDescription + "</h2>");
		}
		try
		{
			meta = data.getConnection().getMetaData();
			rs = meta.getTables(null, null, null, list);
		}
		catch (SQLException e)
		{
			output.println("<p>Unable to get list of tables</p>");
			output.println(StringHelpers.escapeHTML(e.getClass().getName() + " " +
					e.getMessage()));
			output.println("</body></html>");
			return;
		}
		try
		{
			output.println("<table border><tr>");
			output.println("<td><b>Catalog</b></td><td><b>Schema</b></td><td><b>Table</b></td>");
			output.println("<td><b>Remarks</b></td></tr>");

			while (rs.next())
			{
				String catalog = rs.getString("TABLE_CAT");
				if (rs.wasNull()) {
					catalog = new String();
				}
				
				String schema = rs.getString("TABLE_SCHEM");
				if (rs.wasNull()) {
					schema = new String();
				}
				String item = rs.getString("TABLE_NAME");
				if (!isIncluded(data, catalog, schema, item))
				{
					continue;
				}
				counter++;
				output.println("<tr>");
				output.println("<td>" + catalog + "</td><td>" + schema +
						"</td><td>" + 
						"<a href=\"" + rootName + "/DescribeTable?CATALOG=" + catalog +
						"&SCHEMA=" + schema + "&ITEM=" + item +
						"\">" + item + "</a></td><td>" +
						rs.getString("REMARKS") + "</td>");
				output.println("</tr>");
			}
			output.println("</table>");
		}
		catch (SQLException e)
		{
			output.println("<p>A problem was encountered while creating the list of tables</p>");
		}
		output.println("<p>There are " + Integer.toString(counter) + " tables</p>");
		output.println("<hr />");
		output.println("<p><a href=\"" + rootName + "\">Return to root page</a></p>");
			}
	/**
	 * Generate the list of views in the database.
	 * @param data Object describing database connection
	 * @param output Object for writing web page
	 * @param req HTTP request information
	 * @param res HTTP response information
	 * @throws IOException
	 */
	protected void processListViews(DatabaseProperties data, GenericPrinter output,
			HttpServletRequest req, HttpServletResponse res) throws IOException
			{
		if (!isValid)
		{

		}
		String list[] = { "VIEW" };
		String catalog = null;
		String schema = null;
		String item = null;
		ResultSet rs = null;
		DatabaseMetaData meta = null;
		int counter = 0;
		String rootName = req.getContextPath() + req.getServletPath();
		output.println("<html><head>");
		output.println("<title>" + rootName + "/ListViews</title>");
		output.println("</head><body>");
		output.println("<h1>List of Views</h1>");
		try
		{
			meta = data.getConnection().getMetaData();
			rs = meta.getTables(null, null, null, list);
			output.println("<table border><tr><td></td><td><b>Catalog</b></td>" +
					"<td><b>Schema</b></td><td><b>View Name</b></td>" +
					"<td><b>Remarks</b></td></tr>");
			while (rs.next())
			{
				catalog = rs.getString("TABLE_CAT");
				schema = rs.getString("TABLE_SCHEM");
				item = rs.getString("TABLE_NAME");
				if (!isIncluded(data, catalog, schema, item))
				{
					continue;
				}
				counter++;
				output.println("<tr><td>" + Integer.toString(counter) + "</td><td>" +
						catalog + "</td><td>" + schema + "</td><td>" +
						item + "</td><td>" + rs.getString("REMARKS") + "</td></tr>");
			}
			output.println("</table>");
			output.println("<p>There are " + Integer.toString(counter) + " views</p>");
		}
		catch (SQLException e)
		{
			output.println("<p>Problem generating list of views</p>");
		}

		output.println("<p><a href=\"" + rootName + "\">Return to root page</a></p>");
			}
	/**
	 * Generate the list of procedures in the database.
	 * 
	 * @param data Object containing database connection information
	 * @param output Object for writing web page
	 * @param req HTTP request information
	 * @param res HTTP response information
	 * @throws IOException
	 */	
	protected void processListProcedures(DatabaseProperties data, GenericPrinter output,
			HttpServletRequest req, HttpServletResponse res) throws IOException
			{
		ResultSet rs = null;
		DatabaseMetaData meta = null;
		int counter = 0;
		String rootName = req.getContextPath() + req.getServletPath();
		output.println("<html><head>");
		output.println("<title>" + rootName + "/ListProcedures</title>");
		output.println("</head><body>");
		output.println("<h1>List of Procedures</h1>");
		try
		{
			meta = data.getConnection().getMetaData();
			rs = meta.getProcedures(null, null, null);
			counter = showResultSet(output, rs );
		}
		catch (SQLException e)
		{
			output.println("<p>Unable to get list of procedures</p>");
		}
		output.println("<p>There are " + Integer.toString(counter) + " procedures</p>");
		output.println("<p><a href=\"" + rootName + "\">Return to root page</a></p>");
			}
	/**
	 * Generate the list of user defined types for the database system.
	 * 
	 * @param data Database connection information
	 * @param output GenericPrinter object to which material is sent
	 * @param req HTTP request object
	 * @param res HTTP response object
	 * @throws IOException
	 */
	protected void processUDTInformation(DatabaseProperties data, GenericPrinter output,
			HttpServletRequest req, HttpServletResponse res) throws IOException
			{
		String rootName = req.getContextPath() + req.getServletPath();
		output.println("<html><head");
		output.println("<title>User Defined Types</title>");
		output.println("</head><body>");
		output.println("<h1>User Defined Types</h1>");
		output.println("<hr />");
		output.println("<p><a href=\"" +rootName + "\">Return to root page</a></p>");
			}
	/**
	 * Generate a page containing information about the database system.
	 * 
	 * @param data Object containing database connection information
	 * @param output Object for writing web page
	 * @param req HTTP request information
	 * @param res HTTP response information
	 * @throws IOException
	 */	
	protected void processDatabaseInformation(DatabaseProperties data, GenericPrinter output,
			HttpServletRequest req, HttpServletResponse res) throws IOException
			{
		boolean hasMetaData = true;
		DatabaseMetaData meta = null;
		Driver driver = data.getDriver();
		ResultSet rs = null;
		String rootName = req.getContextPath() + req.getServletPath();
		try
		{
			meta = data.getConnection().getMetaData();
		}
		catch (SQLException e)
		{
			hasMetaData = false;
		}
		output.println("<html><head>");
		output.println("<title>" + rootName + "/DatabaseInformation</title>");
		output.println("</head><body>");
		if (!hasMetaData)
		{
			output.println("<p>Unable to obtain data about database</p>");
		}
		else
		{
			boolean transactions = true;
			output.println("<p><a href=\"#catalogs\">Catalogs</a> " +
					"<a href=\"#schemas\">Schemas</a> " +
					"<a href=\"#tables\">Tables</a> " +
					"<a href=\"#procedures\">Procedures</a> " +
					"</p>");
			try
			{
				String name = meta.getDatabaseProductName();
				String version = Integer.toString(meta.getDatabaseMajorVersion()) +
						"." + Integer.toString(meta.getDatabaseMinorVersion());
				transactions = meta.supportsTransactions();
				output.println("<h2><a name=\"database\">Database Information</a></h2>");
				output.println("<p>Major version: " + Integer.toString(driver.getMajorVersion()) + "</p>");
				output.println("<p>Minor version: " + Integer.toString(driver.getMinorVersion()) + "</p>");
				output.println("<p>Using database " + name +
						"  Version " + version + "</p>");
				output.println("<p>Supports transactions: " + Boolean.toString(transactions) + "</p>");
			}
			catch (SQLException e)
			{
				output.println("<p>Unable to get database name and version</p>");
			}
			output.println("<h2><a name=\"catalogs\">Catalog Information</a></h2>");
			try
			{
				boolean catalogsInDataManipulation =
						meta.supportsCatalogsInDataManipulation();
				boolean catalogAtStart = meta.isCatalogAtStart();
				String catalogTerm = meta.getCatalogTerm();
				String catalogSeparator = meta.getCatalogSeparator();
				if (catalogTerm == null)
				{
					output.println("<p>No term for catalogss</p>");
				}
				else if (catalogTerm.length() == 0)
				{
					output.println("<p>No term for catalogs</p>");
				}
				else
				{
					output.println("<p>Term for catalogs is " + catalogTerm + "</p>");
				}
				output.println("<p>Supports catalogs in data manipulation: " +
						Boolean.toString(catalogsInDataManipulation) + "</p>");
				output.println("<p>Supports catalog name at start of identifier: " +
						Boolean.toString(catalogAtStart));
				if (catalogAtStart)
				{
					output.println("<p>Separator for catalog name is <code>" +
							catalogSeparator + "</code></p>");
				}
			}
			catch (SQLException e)
			{
				output.println("<p>Unable to get information about catalogs</p>");
			}
			/*
			 * Print information on schemas.
			 */
			output.println("<h2><a name=\"schemas\">Schema Information</a></h2>");
			try
			{
				boolean schemasInDataManipulation =
						meta.supportsSchemasInDataManipulation();
				String schemaTerm = meta.getSchemaTerm();
				if (schemaTerm == null)
				{
					output.println("<p>No term for schemas</p>");
				}
				else if (schemaTerm.length() == 0)
				{
					output.println("<p>No term for schemas</p>");
				}
				else
				{
					output.println("<p>Term for schemas is " + schemaTerm + "</p>");
				}
				output.println("<p>Supports schemas in data manipulation: " +
						Boolean.toString(schemasInDataManipulation) + "</p>");
			}
			catch (SQLException e)
			{
				output.println("<p>Unable to get information about schemas</p>");
			}
			/*
			 * Print information on tables
			 */
			output.println("<h2><a name=\"tables\">Tables</a></h2>");
			try
			{
				int counter = 0;
				rs = meta.getTableTypes();
				output.println("<p>The table types are </p>");
				counter = showResultSet(output, rs);
				output.println("<p>There are " + Integer.toString(counter) + " table types</p>");
			}
			catch(SQLException e)
			{
				output.println("<p>Unable to get information on table types</p>");
			}
			/*
			 * Print information on procedures.
			 */
			output.println("<h2><a name=\"procedures\">Procedures</a></h2>");
			try
			{
				String value = meta.getProcedureTerm();
				output.println("<p>Term for procedure is " + value + "</p>");
			}
			catch (SQLException e)
			{
				output.println("<p>Unable to get information on procedures</p>");
			}
		}
		output.println("<hr />");
		output.println("<p><a href=\"" + rootName + "\">Return to root page</p>");
			}
	/**
	 * Generate a web page describing the layout of a table.
	 * 
	 * @param data Database connection data object
	 * @param output GenericPrinter object to which information is sent
	 * @param req HTTP request object
	 * @param res HTTP response object
	 * @throws IOException
	 */
	protected void processDescribeTable(DatabaseProperties data, GenericPrinter output, 
			HttpServletRequest req, HttpServletResponse res) throws IOException
			{
		String catalog = null;
		String schema = null;
		String item = null;
		int counter = -1;
		String rootName = req.getContextPath() + req.getServletPath();
		DatabaseMetaData meta = null;
		ResultSet rs = null;
		try
		{
			catalog = req.getParameter("CATALOG");
			if (catalog.length()==0) {
				catalog=null;
			}

			schema = req.getParameter("SCHEMA");
						if (schema.length()==0) {
				schema = null;
			}
			item = req.getParameter("ITEM");
			output.println("<html><head>");
			output.println("<title>Layout of Table " + item + "</title>");
			output.println("</head><body>");
			output.println("<h1>Layout of Table " + item + "</h1>");
			if (catalog != null) {
				output.println("<p>Catalog: " + catalog + "</p>");
			}
			if (schema != null) {
				output.println("<p>Schema: " + schema + "</p>");
			}
			output.println("<p>Running processDescribeTable</p>");
			meta = data.getConnection().getMetaData();
			output.println("<p>Result of getColumns</p>");
			rs = meta.getColumns(catalog, schema, item, (String) null);
			output.println("<p>Information on columns</p>");
			counter = showResultSet(output, rs);
			output.println("<p>There are " + Integer.toString(counter) + " columns</p>");
			rs = null;
			output.println("<hr />");
			output.println("<p>Result of getIndexInfo</p>");
			rs = meta.getIndexInfo(catalog, schema, item, false, false);
			counter = showResultSet(output, rs);
			output.println("<p>There are " + Integer.toString(counter) + " indexes</p>");
			rs = null;
			output.println("<hr />");
			output.println("<p>Result of getImportedKeys</p>");
			rs = meta.getImportedKeys(catalog, schema, item);
			counter = showResultSet(output, rs);
			output.println("<p>There are " + Integer.toString(counter) + " imported keys</p>");
			rs = null;
			output.println("<hr />");
			output.println("<p>Result of getExportedKeys</p>");
			rs = meta.getExportedKeys(catalog, schema, item);
			counter = showResultSet(output, rs);
			output.println("<p>There are " + Integer.toString(counter) + " exported keys</p>");
			rs = null;
			output.println("<hr />");
			output.println("<p><a href=\"" + rootName + "\">Return to root page</a></p>");	
		}
		catch (SQLException e)
		{
			System.out.println("<p>Unable to get information</p>");
			System.out.println("<hr />");
			System.out.println("<p><a href=\"" + rootName + "\">Return to root page</a></p>");
		}
			}

	/**
	 * Displays a result set as an HTML table.
	 * @param output Object for printing to HTML page
	 * @param rs Result set to be processed
	 * @return number of rows
	 */
	protected int showResultSet(GenericPrinter output, ResultSet rs)
	{
		int counter = 0;
		ResultSetMetaData rsmeta = null;
		try
		{
			rsmeta = rs.getMetaData();
		}
		catch (SQLException e)
		{
			output.println("<p>Unable to get metadata</p>");
			output.println(StringHelpers.escapeHTML(e.getClass().getName() + " " +
					e.getMessage()));
			output.println("</body></html>");
			return -1;
		}
		try
		{
			int columnCount = rsmeta.getColumnCount();
			int columnType[] = new int[columnCount];
			output.println("<table border><tr><td>&nbsp;</td>");
			for (int i = 1; i <= columnCount; i++)
			{
				columnType[i-1] = rsmeta.getColumnType(i);
				output.println("<td><b><code>" + 
						StringHelpers.escapeHTML(rsmeta.getColumnName(i)) + "</code></b></td>");
			}
			output.println("</tr>");
			while (rs.next())
			{
				counter++;
				output.println("<tr><td align=\"right\">" + Integer.toString(counter) +
						"</td>");
				for (int i = 1; i <= columnCount; i++)
				{
					int localType = columnType[i-1];
					if (localType == Types.BOOLEAN)
					{
						output.println("<td>" + Boolean.toString(rs.getBoolean(i)) + "</td>");
					}
					else if (localType == Types.VARCHAR || localType == Types.CHAR)
					{
						output.println("<td><code>" + StringHelpers.escapeHTML(rs.getString(i)) + "</code></td>");
					}
					else
					{
						output.println("<td align=\"right\"><code>" + 
								StringHelpers.escapeHTML(rs.getString(i)) + "</code></td>");
					}
				}
				output.println("</tr>");
			}
			output.println("</table>");
		}
		catch (SQLException e)
		{
			output.println("<p>Problems generating list</p>");
			return -1;
		}
		return counter;
	}
}
