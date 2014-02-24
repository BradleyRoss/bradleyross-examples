package bradleyross.library.database;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.ResultSetMetaData;
import java.io.PrintWriter;
import java.io.PrintStream;
import java.sql.Types;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import bradleyross.library.NotSupportedException;
import bradleyross.library.database.DatabaseExtras;
import bradleyross.library.helpers.GenericPrinter;
/**
 * Supports usage of databases via JDBC (Java DataBase Connection)
 * protocol.
 *<p>This is an abstract class and can't be used directly in the
 *   creation of objects.  This means that subclasses must be
 *   created.</p>
 * <p>When creating subclasses, many fields will have to be set, 
 *   including
 *   <code><a href="#handlerClass">handlerClass</a></code>,
 *   <code><a href="#accountName">accountName</a></code>,
 *   <code><a href="#connectionString">connectionString</a></code>, and 
 *   <code><a href="#password">password</a></code>.</p>
 *<p>By including the objects for the JDBC connection and statement, it
 *   is possible to encapsulate a large amount of the code for
 *   the database connectivity.</p>
 *<p>Please note that the methods in this class do not include one
 *   for obtaining the password used in connecting to the database.
 *   If required, this method can be added in the subclass.  
 *   Eliminating this method adds to the security of the system.
 *   By encapsulating the password in a field in this class for
 *   use by the connect method, there is no need for other classes
 *   to have access to the information.</p>
 *  <p>SQL Server notes</p>
 *  <ul>
 *  <li><p>Wants the string <code>dbo.</code> in front of table names.</p></li>
 *  <li><p>Specify the default database as part of the user configuration rather
 *         than including it in the connection string.</p></li>
 *  </ul>
 *  <p>Oracle notes</p>
 *  <ul>
 *  <li><p>Will not accept semicolons at the end of SQL statements.</p></li>
 *  </ul>
 *  <p>MySQL notes</p>
 * @author Bradley Ross
 */
public abstract class DatabaseProperties 
{

	/**
	 * Constructor using a default system and account name.
	 */
	public DatabaseProperties()
	{ 
		invalidParameters = false;
		clearTableNamePrefixes(); 
		setDatabaseInstance();
	}
	/**
	 * Constructor specifying identifier for system and using
	 * a default account name.
	 * @param value Identifier code for system
	 */
	public DatabaseProperties(String value)
	{ 
		invalidParameters = false;
		clearTableNamePrefixes(); 
		setDatabaseInstance(value);
	}
	/**
	 * Constructor specifying system and account name.
	 * @param systemName  Identifier code for system
	 * @param accountName Account name to be used
	 */
	public DatabaseProperties (String systemName, String accountName)
	{
		invalidParameters = false;
		clearTableNamePrefixes();
		setDatabaseInstance(systemName, accountName);
	}
	/**
	 * Internal program for setting up connection using default
	 * database and account.
	 * @see #setDatabaseInstance(String, String)
	 */
	public abstract void setDatabaseInstance();
	/**
	 * Internal program for setting up connection using specified
	 * database with a default account for the database.
	 * @param system Identifying code for database
	 */
	public abstract void setDatabaseInstance(String system);
	/**
	 * Internal program for setting up connection with a specified
	 * database and account name.
	 * @param system Identifier code for database
	 * @param user Account name to be used
	 */
	public abstract void setDatabaseInstance(String system, String user);
	/**
	 * Amount of diagnostic messages.
	 *<p>A value of zero indicates that no diagnostic messages
	 *   should be displayed.  The higher the value, the more
	 *   diagnostic messages will be generated.</p>
	 */
	protected int debugLevel = 0;
	/**
	 * Used to represent associated DatabaseExtras object.
	 */
	protected DatabaseExtras extras = null;
	/**
	 * Set value of extras.
	 * @param value Object to be used for DatabaseExtras object.
	 * @see #extras
	 */
	public void setDatabaseExtras(DatabaseExtras value)
	{
		extras = value;
	}
	/**
	 * Get value of extras.
	 * @return DatabaseExtras object being used.
	 * @throws NotSupportedException
	 * @see #extras
	 */
	public DatabaseExtras getDatabaseExtras() throws NotSupportedException
	{
		if (extras == null)
		{
			throw new NotSupportedException("Database Extras is not supported");
		}
		else
		{
			return extras;
		}
	}
	/**
	 * Set of prefixes for table names
	 */
	protected String tableNamePrefixes[] = new String[10];
	/** Get the value of the default table name prefix (item 0)
	 * @return Default table name prefix
	 */
	public String getTableNamePrefixes()
	{ return tableNamePrefixes[0]; }
	/** 
	 * Get the value of a table name prefix
	 * @param i Location of table name prefix in list
	 * @return Value of table name prefix
	 */
	public String getTableNamePrefixes(int i)
	{ return tableNamePrefixes[i]; }
	/** 
	 * True indicates that autoCommit is set for this datbase connection when
	 * the connection is opened.
	 */
	protected boolean autoCommit = true;
	/**
	 * Optional field containing a human readable description of the
	 * database.  This string will have to be escaped before it is safe
	 * for use in HTML code and SQL queries.
	 */
	protected String systemDescription = null;
	/**
	 * Getter for systemDescription
	 * @return Description of system
	 * @see #systemDescription
	 */
	public String getSystemDescription()
	{
		return systemDescription;
	}
	/**
	 * This is the name of the system identifier to identify
	 * the database in database systems that can support multiple
	 * databases.
	 */
	protected String systemName = null;
	/**
	 * Name of the handler class for the database.
	 * <p><table border>
	 * <tr><td>Product</td><td>Handler Class</td><td>Default<br />Port</td></tr>
	 * <tr><td>MySQL</td><td><code>com.mysql.jdbc.Driver</code></td><td>3306</td></tr>
	 * <tr><td>Oracle</td><td><code>oracle.jdbc.driver.OracleDriver</code></td><td>1521</td></tr>
	 * <tr><td>SQL Server</td><td><code>com.microsoft.sqlserver.jdbc.SQLServerDriver</code></td><td>1433</td></tr>
	 * </table></p>
	 * <p>It may be necessary to use the name of the handlerClass in
	 *    generating the SQL statements, since the syntax for different
	 *    databases such as MySQL, Oracle, DB2, Postgres, etc. differ for
	 *    many of the built in functions.</p>
	 * <p>Although there is no <i>get</i> method for this field, one can
	 *    be added when creating subclasses.</p>
	 */
	protected String handlerClass = null;
	/** 
	 * Port number of the database connection.
	 * <p>The default port number is shown in the discussion of the
	 *    <a href="#handlerClass">handler class names</a>.  If a port
	 *    number other than the default is used, it is included as
	 *    part of the <a href="#domainName">domain name</a> and
	 *    <a href="#connectionString">connection string</a>.</p>
	 */
	protected int portNumber = 0;
	/**
	 * The connection string to be used to connect to the database using
	 * JDBC (Java DataBase Connectivity)
	 * <p>The connection string is composed as a series of tokens
	 *    separated by colons.  The first token is always
	 *    <code>jdbc</code> while the second is the name
	 *    of the database manager product.  The remaining
	 *    tokens are dependent on the DBMS package used.</p>
	 * <p>The following are examples of connection strings.</p>
	 * <table border>
	 * <tr><td>MySQL</td>
	 *     <td><code>jdbc:mysql://server.foo.com/alpha</code></td>
	 *     <td>alpha is default database</td>
	 *     </tr>
	 * <tr><td>Oracle</td>
	 *     <td><code>jdbc:oracle:thin:@//server.foo.com/weather<br>
	 *			    jdbc:oracle:oci:</code></td>
	 *     <td>Connects to weather service<br>
	 *          The options thin and oci refer to the two types of JDBC connections.  The
	 *          OCI method requires the installation of additional pieces of software
	 *          and uses SQL*Net to connect to the database.</td>
	 *     </tr>
	 * <tr><td>SQL Server</td>
	 *     <td><code>jdbc:sqlserver://server.foo.com</code></td>
	 *     <td>SQL Server allows you to specify a default database as part of the
	 *         user configuration.</td>
	 *     </tr>
	 * </table>
	 */
	protected String connectionString = null;
	/**
	 * Domain name for the system containing the database.
	 * <p>The domain name is included as part of the
	 *    <a href="#connectionString">connection string</a>.</p>
	 */
	protected String domainName = null;
	/**
	 * The account name to be used on the database server.
	 */
	protected String accountName = null;
	/** 
	 * The password to be used for accessing the database server.
	 * <p>It should be noted that there is no <i>get</i> method for
	 *    the field.  This improves security for the package.  If necessary,
	 *    a <i>get</i> method can be added in a subclass.</p>
	 */
	private String password = null;
	/**
	 * When creating MySQL tables, this field can be
	 * used by the getEngineClause to add the ENGINE
	 * statement to CREATE TABLE statements.
	 * @see #getEngineClause()
	 */
	protected String defaultEngine = null;
	/**
	 * Default catalog for methods allowing the use
	 * of a default value.
	 * @see #getDefaultCatalog()
	 * @see #setDefaultCatalog(String)
	 */
	protected String defaultCatalog = null;
	/**
	 * Get the value of defaultCatalog.
	 * @return Value of defaultCatalog
	 * @see #defaultCatalog
	 */
	public String getDefaultCatalog()
	{
		return defaultCatalog;
	}
	/**
	 * Sets value of defaultCatalog.
	 * @param value Value to be used for defaultCatalog
	 * @see #defaultCatalog
	 */
	public void setDefaultCatalog(String value)
	{
		defaultCatalog = value;
	}
	/**
	 * Default schema for methods allowing the use
	 * of a default value.
	 * @see #getDefaultSchema()
	 * @see #setDefaultSchema(String)
	 */
	protected String defaultSchema = null;
	/**
	 * Gets value of defaultSchema
	 * @return Value of defaultSchema
	 * @see #defaultSchema
	 */
	public String getDefaultSchema()
	{
		return defaultSchema;
	}
	/**
	 * Sets value of defaultSchema.
	 * @param value Value to be used for defaultSchema
	 * @see #defaultSchema
	 */
	public void setDefaultSchema(String value)
	{
		defaultSchema = value;
	}
	/**
	 * The object representing the connection to the database.
	 * @see #getConnection()
	 */
	protected java.sql.Connection connection = null;
	/**
	 * Flag indicating whether database has been connected.
	 */
	protected boolean isConnected = false;
	/**
	 * Set the amount of diagnostic messages.
	 * @param level Amount of diagnostic messages.
	 */
	public void setDebugLevel(int level)
	{ debugLevel = level; }
	/**
	 * Gets the amount of diagnostic messages to be printed.
	 *
	 * @return Level of diagnostic messages to be printed.
	 */
	public int getDebugLevel()
	{ return debugLevel; }
	/**
	 * If the parameters supplied for instantiating the object are
	 * invalid, this field should be set to true.
	 * 
	 * @see #setInvalidParameters(boolean)
	 * @see #hasInvalidParameters()
	 */
	protected boolean invalidParameters = false;
	/**
	 * Return the system identifier used for the database connection.
	 * @return System identifier
	 */
	public String getSystem()
	{ return systemName; }
	/**
	 * Get name of database management system
	 * <p>The database name is extracted from the 
	 *    <a href="#connectionString">connection string</a>
	 *    as the second token of the string.</p>
	 */
	public String getDbms()
	{
		String working = connectionString.substring(connectionString.indexOf(":")+1);
		working = working.substring(0, working.indexOf(":"));
		return working.toLowerCase();
	}
	/**
	 * Return the account name used for the database connection.
	 * @return Account name for database.
	 */
	public String getAccountName()
	{ return accountName; }
	/**
	 * Create optional ENGINE clause
	 * @see #defaultEngine
	 * @return ENGINE clause for CREATE TABLE SQL statement
	 */
	public String getEngineClause()
	{
		if (defaultEngine == null)
		{
			return " ";
		}
		else if (getDbms().equalsIgnoreCase("mysql"))
		{
			return " ENGINE " + defaultEngine + " ";
		}
		else return " ";
	}
	/**
	 * Open the database connection.
	 */
	public void connect() throws java.sql.SQLException
	{
		if (invalidParameters)
		{
			throw new SQLException("Parameters for system and account name are not valid : " +
					showAttributes());
		}
		try
		{
			Class.forName(handlerClass).newInstance();
		}
		catch (InstantiationException e)
		{
			System.out.println("*** Error in establishing instance of handler");
			System.out.println("*** Instantiation Exception");
			System.out.println(e.getMessage());
			e.printStackTrace(System.out);
		}
		catch (IllegalAccessException e )
		{
			System.out.println("*** Error in establishing instance of handler");
			System.out.println("*** Illegal Access Exception");
			System.out.println(e.getMessage());
			e.printStackTrace(System.out);
		}
		catch (ClassNotFoundException e)
		{
			System.out.println("*** Error in establishing instance of handler");
			System.out.println("*** Class Not Found Exception");
			System.out.println(e.getMessage());
			e.printStackTrace(System.out);
		}
		try
		{
			connection = DriverManager.getConnection(connectionString, accountName,
					password); 
			connection.setAutoCommit(autoCommit);
		}
		catch (SQLException e)
		{
			System.out.println("SQL Exception");
			System.out.println(e.getMessage());
			e.printStackTrace(System.out);
			throw e;
		}
		isConnected = true;
		if (debugLevel > 1)
		{
			System.out.println("Database connected");
		}
	}
	/**
	 * Close the database connection.
	 */
	public void close() throws SQLException
	{
		try
		{
			connection.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace(System.out);
			throw e;
		}
	}
	/**
	 * Obtain the connection object for the database connection.
	 * @return connection
	 */
	public java.sql.Connection getConnection()
	{ return connection;}
	/**
	 * Execute SQL statement without returning results.
	 * <p>This method is usually used to make updates to the
	 *    database.</p>
	 * @param sqlCode SQL code to be executed.
	 */
	public int executeUpdate(String sqlCode) throws SQLException
	{
		java.sql.Statement stmt = null;
		int recordCount = -1;
		try
		{
			if (debugLevel > 0)
			{ System.out.println("Starting executeUpdate"); }
			if (connection == (java.sql.Connection) null)
			{ connect(); }
			stmt = connection.createStatement();
			recordCount  = stmt.executeUpdate(sqlCode);
			if (debugLevel > 0)
			{ System.out.println("Statement executed: " +
					Integer.toString(recordCount) + 
			" records updated"); }
			stmt.close();
		}
		catch (SQLException e)
		{
			if (debugLevel > 0 )
			{
				System.out.println("SQLException in executeUpdate");
				System.out.println(e.getMessage());
				e.printStackTrace(System.out);
			}
			throw e;
		}
		catch (Exception ex)
		{
			System.out.println("Unknown Exception in executeUpdate");
			ex.printStackTrace(System.out);
			throw new SQLException(ex.getClass().getName() + " : " + ex.getMessage() + 
					"Unknown Exception in executeUpdate");
		}
		return recordCount;
	}
	/**
	 * This class reads the first field on the first record from
	 * an SQL Query.
	 * <p>Since only the first field is read, the statement is
	 *    closed after reading the field.  This eliminates the possibility
	 *    of leaving a statement open if it returns multiple rows.</p>
	 * @param sqlCode SQL code to be executed.
	 * @return String object continuing result of SQL query
	 */
	public String executeSimpleQuery(String sqlCode) 
	throws SQLException
	{
		java.sql.Statement stmt = null;
		java.sql.ResultSet rs = null;
		java.lang.String result = null;
		try
		{
			if (debugLevel > 0)
			{ System.out.println("Starting executeSimpleQuery"); }
			if (connection == (java.sql.Connection) null)
			{ connect(); }
			stmt = connection.createStatement();
			rs = stmt.executeQuery(sqlCode);
			if (debugLevel > 0)
			{ System.out.println("Statement executed"); }
			/*
			 * The first method is not supported for forward only
			 *    result sets.  It looks like forward only result
			 *    sets are the default for SQL Server.
			 */
			rs.next();
			result = rs.getString(1);
			stmt.close();
		}
		catch (SQLException e)
		{
			if (debugLevel > 0)
			{
				System.out.println("SQLException in executeSimpleQuery");
				System.out.println(e.getMessage());
				e.printStackTrace(System.out);
			}
			throw e;
		}
		catch (Exception ex)
		{
			System.out.println("Unknown Exception in executeSimpleQuery");
			ex.printStackTrace(System.out);
		}
		return result;
	}
	/**
	 * This class executes an SQL query and returns a
	 * ResultSet.
	 * @param sqlCode SQL code to be executed.
	 * @return ResultSet
	 */
	public java.sql.ResultSet executeQuery(String sqlCode) 
	throws java.sql.SQLException
	{
		java.sql.Statement stmt = null;
		java.sql.ResultSet rs = null;
		if (!isConnected)
		{
			System.out.println(" Error in executeQuery - database is not connected");
			throw new SQLException("Database not connected");
		}
		try
		{
			if (debugLevel > 0)
			{ System.out.println("Starting executeQuery for " + sqlCode); }
			if (connection == (java.sql.Connection) null)
			{ 
				if (debugLevel > 1)
				{
					System.out.println("Creating connection");
				}
				connect(); 
			}
			stmt = connection.createStatement();
			if (debugLevel > 1)
			{
				System.out.println("Statement created");
			}
			rs = stmt.executeQuery(sqlCode);
			if (debugLevel > 0)
			{ System.out.println("Statement executed"); }
		}
		catch (SQLException e)
		{
			System.out.println("SQLException in executeQuery");
			System.out.println(e.getMessage());
			e.printStackTrace(System.out);
			throw e;
		}
		catch (Exception ex)
		{
			System.out.println("Unknown Exception in executeQuery");
			System.out.println(ex.getClass().getName() + " " + ex.getMessage());
			ex.printStackTrace(System.out);
		}
		return rs;
	}
	/**
	 * Execute a commit statement for the SQL connection.
	 */
	public void commit()
	{
		try
		{ connection.commit(); }
		catch (SQLException e)
		{
			System.out.println("SQLException during commit");
			e.printStackTrace(System.out);
		}
		catch (Exception e)
		{
			System.out.println("Unknown exception during commit");
			e.printStackTrace(System.out);
		}
	}
	/**
	 * Execute a rollback statement for the SQL connection.
	 */
	public void rollback()
	{
		try
		{ connection.rollback(); }
		catch (SQLException e)
		{
			System.out.println("SQLException during rollback");
			e.printStackTrace(System.out);
		}
		catch (Exception e)
		{
			System.out.println("Unknown exception during rollback");
			e.printStackTrace(System.out);
		}
	}
	/** 
	 * Set autoCommit flag
	 * @param value Value to be used for setting autoCommit flag
	 */
	public void setAutoCommit(boolean value)
	{
		try
		{ connection.setAutoCommit(value); }
		catch (SQLException e)
		{
			System.out.println("SQLException while setting autoCommit to " +
					Boolean.toString(value));
			e.printStackTrace(System.out);
		}
	}
	/**
	 * Return attributes of the connection properties
	 *
	 * @return String containing connection information
	 */
	public java.lang.String showAttributes()
	{
		return ("systemName:" + systemName +
				"\naccountName:" + accountName +
				"\ndomainName:" + domainName +
				"\nhandlerClass:" + handlerClass);
	}
	/**
	 * Setter for accountName.
	 * 
	 * <p>Must be used before database connection is made.</p>
	 * @param value Value for accountName
	 *
	 */
	
	public void setAccountName(String value)
	{
		if (isConnected) { return; }
		accountName = value;
		setDatabaseInstance(systemName, accountName);
	}
	/**
	 * Specify the connection string to be used.
	 * 
	 * <p>Do not specify the account name or password as
	 *    part of the connection string.</p>
	 *    
	 * @param value Connection string to be used
	 * @see #connectionString
	 */
	public void setConnectionString(String value)
	{
		if (isConnected) { return; }
		connectionString = value;
	}
	/**
	 * Setter for domainName
	 * <p>Does not include port number</p>
	 * @see #domainName
	 * @param value Value to be used for domain name
	 */
	public void setDomainName(String value)
	{
		if (isConnected) { return; }
		domainName = value;
	}
	/**
	 * Set the class name to be used for the handler class.
	 * @param value Name of handler class
	 * @see #handlerClass
	 */
	public void setHandlerClass(String value)
	{
		if (isConnected) { return; }
		handlerClass = value;
	}
	/** 
	 * Sets the password to be used for the connection
	 * when it is desired to avoid placing the value in
	 * the program.
	 *
	 * @param value Value to be used for the password
	 */
	public void setPassword(String value)
	{ 
		if (isConnected) { return; }
		password = value; 
	}
	/**
	 * Setter for port number
	 * @param value port number to be used for connection
	 * @see #portNumber
	 */
	public void setPortNumber(int value) {
		portNumber = value;
	}
	/**
	 * Setter for systemName.
	 * 
	 * @see #systemName
	 * @param value Value to be used for system name
	 * 
	 */	
	public void setSystemName(String value)
	{
		if (isConnected) { return; }
		systemName = value;
	}	
	/**
	 * Clears the list of table name prefix values.
	 */
	public void clearTableNamePrefixes()
	{
		for (int i=0; i<tableNamePrefixes.length; i++)
		{ tableNamePrefixes[i] = null; }
	}
	/**
	 * Prefixes a table name with a value that was set as part of the
	 * database connection.
	 *
	 * @param type Location of the desired prefix in the tableNamePrefixes list
	 * @param value Table name to be prefixed
	 * @return Prefixed table name
	 */
	public java.lang.String prefixTableName(int type, String value)
	{
		if (type < 0 || type > 9)
		{ return " " + value + " "; }
		if (tableNamePrefixes[type] == null)
		{ return " " + value + " "; }
		if (tableNamePrefixes[type].length() <= 0)
		{ return value; }
		return (" " + new String(tableNamePrefixes[type] + value) + " ");
	}
	/**
	 * Prefixes a table name with a value that was set as part of the database
	 * connection.
	 *
	 * <p>This version of the method uses the first entry in the tableNamePrefixes 
	 *    array.  (index=0)</p>
	 *
	 * @param value Table name to be prefixed
	 * @return Prefixed table name
	 */
	public java.lang.String prefixTableName(java.lang.String value)
	{ return prefixTableName(0, value); }
	/**
	 * Generate a prepared statement for this database.
	 * @param query SQL Query to be used in statement
	 * @return PreparedStatement object
	 * @throws java.sql.SQLException
	 */
	public java.sql.PreparedStatement prepareStatement(String query)
	throws java.sql.SQLException
	{ 
		if (connection == null)
		{
			throw new SQLException("The value of the connection field is null");
		}
		return connection.prepareStatement(query);
	}
	/**
	 * Returns the parameter as a DDL comment
	 * if the database is of a type that allows comments in
	 * DDL statements.
	 * 
	 * <p>SQL Server does not allow comments in DDL statements.</p>
	 * 
	 * @param text Text to be used in comment
	 * @return Formatted material for comment in CREATE TABLE statement
	 */
	public String comment(String text)
	{
		if (text == null) { return " "; }
		if (!getDbms().equalsIgnoreCase("sqlserver"))
		{
			Pattern pattern = Pattern.compile("\'");
			Matcher matcher = pattern.matcher(text);
			String temp = matcher.replaceAll("\'\'");
			return (" COMMENT \'" + temp + "\' "); 
		}
		else
		{
			return (" ");
		}
	}
	/**
	 * Return the data type used for large BLOBS
	 * (Binary Large Objects).
	 * @return Name of data type.
	 */
	public String getLongblobTerm()
	{
		if (getDbms().equalsIgnoreCase("sqlserver"))
		{
			return " VARBINARY(MAX) ";
		}
		else if (getDbms().equalsIgnoreCase("mysql"))
		{
			return " LONGBLOB ";
		}
		else 
		{
			return " LONGBLOB ";
		}
	}
	/**
	 * Return the expression used in SQL statements to
	 * obtain the current time.
	 * @return SQL clause.
	 */
	public String getNowTerm()
	{
		if (getDbms().equalsIgnoreCase("sqlserver"))
		{
			return " GETDATE() ";
		}
		else if (getDbms().equalsIgnoreCase("mysql"))
		{
			return " NOW() ";
		}
		else if (getDbms().equalsIgnoreCase("oracle"))
		{
			return " SYSDATE ";
		}
		else 
		{
			return " NOW() ";
		}
	}
	/**
	 * Return the expression used in SQL statements to
	 * represent a data type for holding a date.
	 * 
	 * <p>SQL Server 2000 and 2005 do not have a DATE data type,
	 *    and SMALLDATETIME should be used instead.</p>
	 *    
	 * @return SQL clause.
	 */
	public String getDateTerm()
	{
		if (getDbms().equalsIgnoreCase("sqlserver"))
		{
			return " SMALLDATETIME ";
		}
		else if (getDbms().equalsIgnoreCase("mysql"))
		{
			return " DATE ";
		}
		else if (getDbms().equalsIgnoreCase("oracle"))
		{
			return " DATE ";
		}
		else 
		{
			return " DATE ";
		}
	}
	/**
	 * Return the expression used in SQL statements to
	 * represent a data type for holding a date and time.
	 * 
	 * <p>It appears that Oracle refers to this data type
	 *    as TIMESTAMP.</p>
	 *    
	 * @return SQL clause.
	 */
	public String getDatetimeTerm()
	{
		if (getDbms().equalsIgnoreCase("sqlserver"))
		{
			return " DATETIME ";
		}
		else if (getDbms().equalsIgnoreCase("mysql"))
		{
			return " DATETIME ";
		}
		else if (getDbms().equalsIgnoreCase("oracle"))
		{
			return " TIMESTAMP ";
		}
		else 
		{
			return " DATETIME ";
		}
	}
	/**
	 * Decode the values of constants in java.sql.TYPES to 
	 *    String values.
	 * @param value  Value of constant
	 * @return Corresponding string describing constant
	 * @see Types
	 */
	public static String decodeSqlType(int value)
	{
		if (value == Types.ARRAY)
		{
			return "ARRAY";
		}
		else if (value == Types.BIGINT)
		{
			return "BIGINT";
		}
		else if (value == Types.BINARY)
		{
			return "BINARY";
		}
		else if (value == Types.BIT)
		{
			return "BIT";
		}
		else if (value == Types.BLOB)
		{
			return "BLOB";
		}
		else if (value == Types.BOOLEAN)
		{
			return "BOOLEAN";
		}
		else if (value == Types.CHAR)
		{
			return "CHAR";
		}
		else if (value == Types.CLOB)
		{
			return "CLOB";
		}
		else if (value == Types.DOUBLE)
		{
			return "DOUBLE";
		}
		else if (value == Types.FLOAT)
		{
			return "FLOAT";
		}
		else if (value == Types.INTEGER)
		{
			return "INTEGER";
		}
		else if (value == Types.JAVA_OBJECT)
		{
			return "JAVA_OBJECT";
		}
		else if (value == Types.LONGVARBINARY)
		{
			return "LONGVARBINARY";
		}
		else if (value == Types.LONGVARCHAR)
		{
			return "LONGVARCHAR";
		}
		else if (value == Types.REAL)
		{
			return "REAL";
		}
		else if (value == Types.SMALLINT)
		{
			return "SMALLINT";
		}
		else if (value == Types.TINYINT)
		{
			return "TINYINT";
		}
		else if (value == Types.VARCHAR)
		{
			return "VARCHAR";
		}
		else if (value == Types.VARBINARY)
		{
			return "VARBINARY";
		}
		else if (value == Types.DATE)
		{
			return "DATE";
		}
		else if (value == Types.TIME)
		{
			return "TIME";
		}
		else if (value == Types.TIMESTAMP)
		{
			return "TIMESTAMP";
		}
		else
		{
			return "Unrecognized value";
		}
	}
	
	/**
	 * Set value of invalidParameters to indicate that constructor is not valid.
	 * @param value Value to be used for notSupported
	 */
	protected void setInvalidParameters(boolean value)
	{
		invalidParameters = value;
	}
	/**
	 * Determine whether parameters for constructor were valid.
	 * @return True if combination of system and account is not supported
	 */
	public boolean hasInvalidParameters()
	{
		return invalidParameters;
	}
	/**
	 * Dump contents of database table to PrintWriter object.
	 * 
	 * @param table Name of table to be dumped
	 * @param output Destination PrintWriter
	 */
	public void dumpTable(String table, PrintWriter output)
	{
		dumpTable(table, table, output, (PrintStream) null);
	}
	/**
	 * Dump contents of database to PrintStream object
	 * @param table  Name of table to be dumped
	 * @param output Destination PrintStream
	 */
	public void dumpTable(String table, PrintStream output)
	{
		dumpTable(table, table, (PrintWriter) null, output);
	}
	/**
	 * Dumps the contents of a table as a series of SQL statements.
	 * <p>An OutputStreamWriter can be created from an OutputStream using the
	 *    constructor <code>OutputStreamWriter(OutputStream out)</code>.</p>
	 * <p>The objects <code>System.out</code> and <code>System.err</code> are
	 *    PrintStream objects.  A PrintStream object can be created from an
	 *    OutputStream object using the constructor
	 *    <code>PrintStream(OutputStream out)</code>.</p>
	 * <p>A PrintStream object can be created from a File object using the
	 *    constructor
	 *    <code>PrintStream (File out)</code>.</p>
	 * 
	 * @param sourceTableName Table to be dumped
	 * @param targetTableName Table name to be used in SELECT statement
	 * @param outputWriter PrintWriter object to which information is to be passed
	 * @param outputStream PrintStream object to which information is to be passed
	 */
	protected void dumpTable(String sourceTableName, String targetTableName, PrintWriter outputWriter,
			PrintStream outputStream)
	{
		System.out.println("Starting table dumper for " + sourceTableName);
		setAutoCommit(false);
		String tableName = prefixTableName(sourceTableName);
		String sqlQuery = "SELECT * FROM " + tableName ;
		if (debugLevel > 1)
		{
			System.out.println("Query is " + sqlQuery);
		}
		Statement stmt = null;
		ResultSet rs = null;
		try
		{		
			boolean test;
			stmt = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
			/*
			 * This is apparently a fix for large result sets specific to MySQL.
			 * There was also a comment to try 
			 * ((com.mysql.jdbc.Statement) stmt).enableStreamingResults();
			 */
			if (getDbms().equalsIgnoreCase("mysql"))
			{
				if (debugLevel > 1)
				{
					System.out.println("Special case for MySQL large result sets");
				}
				stmt.setFetchSize(Integer.MIN_VALUE);
			}
			else if (getDbms().equalsIgnoreCase("sqlserver"))
				stmt.setFetchSize(20);
			if (debugLevel > 1)
			{
				int sample = stmt.getResultSetType();
				if (sample == ResultSet.TYPE_FORWARD_ONLY)
				{
					System.out.println("Result set is TYPE_FORWARD_ONLY");
				}
				else if (sample == ResultSet.TYPE_SCROLL_INSENSITIVE)
				{
					System.out.println("Result set is TYPE_SCROLL_INSENSITIVE");
				}
				else if (sample == ResultSet.TYPE_SCROLL_SENSITIVE)
				{
					System.out.println("Result set is TYPE_SCROLL_SENSITIVE");
				}
				else
				{
					System.out.println("Result set is of unknown type");
				}
			}
			test = stmt.execute(sqlQuery);
			if (!test)
			{
				System.out.println("Problem in executing " + sqlQuery);
				return;
			}
			rs = stmt.getResultSet();
			if (rs == null)
			{
				System.out.println("Result set was null");
			}
			else
			{
				System.out.println("Result set generated");
			}
		}
		catch (java.sql.SQLException e)
		{
			System.out.println("SQL Exception encountered: " + e.getMessage());
			e.printStackTrace(System.out);
		}
		catch (Exception e)
		{
			System.out.println("Unexpected exception in table dumper");
			System.out.println(e.getClass().getName() + " " + e.getMessage());
			e.printStackTrace(System.out);
		}
	}
	public void dumpTable(String sourceTableName, String targetTableName, 
			GenericPrinter output)
	{
		System.out.println("dumpTable: Starting table dumper for " + sourceTableName);
		setAutoCommit(false);
		String tableName = prefixTableName(sourceTableName);
		String sqlQuery = "SELECT * FROM " + tableName ;
		if (debugLevel > 1)
		{
			System.out.println("dumpTable: Query is " + sqlQuery);
		}
		Statement stmt = null;
		ResultSet rs = null;
		try
		{		
			boolean test;
			stmt = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
			/*
			 * This is apparently a fix for large result sets specific to MySQL.
			 * There was also a comment to try 
			 * ((com.mysql.jdbc.Statement) stmt).enableStreamingResults();
			 */
			if (getDbms().equalsIgnoreCase("mysql"))
			{
				if (debugLevel > 1)
				{
					System.out.println("Special case for MySQL large result sets");
				}
				stmt.setFetchSize(Integer.MIN_VALUE);
			}
			else if (getDbms().equalsIgnoreCase("sqlserver"))
				stmt.setFetchSize(20);
			if (debugLevel > 1)
			{
				int sample = stmt.getResultSetType();
				if (sample == ResultSet.TYPE_FORWARD_ONLY)
				{
					System.out.println("Result set is TYPE_FORWARD_ONLY");
				}
				else if (sample == ResultSet.TYPE_SCROLL_INSENSITIVE)
				{
					System.out.println("Result set is TYPE_SCROLL_INSENSITIVE");
				}
				else if (sample == ResultSet.TYPE_SCROLL_SENSITIVE)
				{
					System.out.println("Result set is TYPE_SCROLL_SENSITIVE");
				}
				else
				{
					System.out.println("Result set is of unknown type");
				}
			}
			test = stmt.execute(sqlQuery);
			if (!test)
			{
				System.out.println("Problem in executing " + sqlQuery);
				return;
			}
			rs = stmt.getResultSet();
			if (rs == null)
			{
				System.out.println("dumpTable: Result set was null");
			}
			else
			{
				System.out.println("dumpTable: Result set generated");
				dumpResultSet(rs, targetTableName, output);
			}
		}
		catch (java.sql.SQLException e)
		{
			System.out.println("SQL Exception encountered: " + e.getMessage());
			e.printStackTrace(System.out);
		}
		catch (Exception e)
		{
			System.out.println("Unexpected exception in table dumper");
			System.out.println(e.getClass().getName() + " " + e.getMessage());
			e.printStackTrace(System.out);
		}
	}	
	
	/**
	 * Dumps a result set to a PrintWriter object
	 * @param rs Result Set
	 * @param targetTableName Table name to be used in INSERT statement
	 * @param outputWriter Destination PrintWriter object
	 */
	public void dumpResultSet(ResultSet rs, String targetTableName, PrintWriter outputWriter)
	{
		GenericPrinter out = new GenericPrinter(outputWriter);
		dumpResultSet(rs, targetTableName, out);
	}
	/**
	 * Dumps a result set to a PrintStream object
	 * @param rs Result set
	 * @param targetTableName Table name to be used in INSERT statement
	 * @param outputStream Destination PrintStream object
	 */
	public void dumpResultSet(ResultSet rs, String targetTableName, PrintStream outputStream)
	{
		GenericPrinter out = new GenericPrinter(outputStream);
		dumpResultSet(rs, targetTableName,  out);
	}
	/**
	 * Dumps a result set as a set of INSERT SQL statements.
	 * 
	 * @param rs Result set to be dumped
	 * @param targetTableName Table name to be used in INSERT statements
	 * @param output GenericPrinter object to which information is passed
	 *
	 */
	public void dumpResultSet(ResultSet rs, String targetTableName, GenericPrinter output)
	{
		ResultSetMetaData meta = null;
		int columnCount = 0;
		String columnNames[];
		int columnTypes[];
		int rowCounter = 0;
		try
		{
			rowCounter = 0;
			meta = rs.getMetaData();
			columnCount = meta.getColumnCount();
			if (debugLevel > 0)
			{
				System.out.println("Query executed, " + Integer.toString(columnCount) + " columns"); 
			}
			columnNames = new String[columnCount];
			columnTypes = new int[columnCount];
			for (int i = 0; i < columnCount; i++)
			{
				columnNames[i] = meta.getColumnName(i+1);
				columnTypes[i] = meta.getColumnType(i+1);
			}
			/* Get result set */
			while (rs.next())
			{
				boolean nullValue[] = new boolean[columnCount];
				String columnValue[] = new String[columnCount];
				for (int i = 0; i < columnCount; i++)
				{
					nullValue[i] = false;
					columnValue[i] = null;
				}
				rowCounter++;
				if (rowCounter % 500 == 49) 
				{ 
					output.flush();
				}	
				StringBuffer working;
				/* Read values for SQL statement */
				for (int i = 0; i < columnCount; i++)
				{
					
					int temp = columnTypes[i];
					if (temp == Types.TIMESTAMP)
					{
						java.sql.Timestamp date = rs.getTimestamp(i+1);
						if (rs.wasNull())
						{
							nullValue[i] = true;
							columnValue[i] = "dummy";
						}
						else
						{
							columnValue[i] ="\'" + date.toString() + "\'"; 
						}
					}
					else if (temp == Types.VARCHAR || temp == Types.LONGVARCHAR ||
							temp == Types.CHAR)
					{
						String value = rs.getString(i+1);
						if (rs.wasNull())
						{
							nullValue[i] = true;
							
						}
						else
						{
							
							columnValue[i] = "\'" + value + "\'";
						}
					}
					else if (temp == Types.FLOAT || temp == Types.DOUBLE || temp == Types.DECIMAL ||
							temp == Types.NUMERIC || temp == Types.REAL)
					{
						float value = 0.0f;
						value = rs.getFloat(i+1);
						if (rs.wasNull())
						{
							nullValue[i] = true;
							
						}
						else
						{
							
							columnValue[i] = Float.toString(value);
						}
					}
					else if (temp == Types.TINYINT || temp == Types.SMALLINT ||
							temp == Types.INTEGER)
					{
						int value = rs.getInt(i+1);
						if (rs.wasNull()) 
						{
							nullValue[i] = true;
							
						}
						else
						{
							
							columnValue[i] = Integer.toString(value);
						}
					}
				}
				/*
				 * Write SQL statement
				 */
				working = new StringBuffer();
				boolean firstItemProcessed = false;
				working.append("INSERT INTO " + targetTableName + "(");
				for (int i = 0; i < columnCount; i++)
				{
					if (nullValue[i])
					{
						continue;
					}
					if (firstItemProcessed)
					{
						working.append(", " + columnNames[i]);
					}
					else
					{
						working.append(columnNames[i]);
						firstItemProcessed = true;
					}
				}
				working.append(") VALUES(");
				firstItemProcessed = false;
				for (int i = 0; i < columnCount; i++)
				{
					if (nullValue[i])
					{
						continue;
					}
					if (firstItemProcessed)
					{
						working.append(", " + columnValue[i]);
					}
					else
					{
						working.append(columnValue[i]);
						firstItemProcessed = true;
					}
				}
				working.append(");");
				output.println(new String(working));
			}
			if (debugLevel > 0)
			{
				System.out.println(Integer.toString(rowCounter) + " rows were processed");
			}
			rs.close();		
		}
		catch (java.sql.SQLException e)
		{
			System.out.println("SQL Exception encountered: ");
			System.out.println(e.getClass().getName()+ e.getMessage());
			e.printStackTrace(System.out);
		}
		catch (Exception e)
		{
			System.out.println("Unexpected exception in table dumper");
			System.out.println(e.getClass().getName() + " " + e.getMessage());
			e.printStackTrace(System.out);
		}
	}
	/**
	 * Insert rows in a table where there is only one column in
	 * the primary key.
	 * 
	 * <p>The primary key should be the first column in the list.</p>
	 * 
	 * @param tableName Name of the table
	 * @param columnNames Array containing column names in the database
	 *        table for the columns to be populated with data.
	 * @param value Values to be inserted in the columns
	 * @throws SQLException
	 * @deprecated
	 */
	public void insertRow (String tableName,
		String columnNames[], Object... value) throws SQLException	
			{
				insertRowWithKeys (tableName, columnNames, 1, value);
			}
	/**
	 * Insert rows in a table where there is more than one column in
	 * the primary key.
	 * 
	 * @param tableName Name of the table
	 * @param columnNames Array containing column names in the database
	 *        table.
	 * @param columnCount Number of columns in primary key.  These are the first
	 *        columns in the list.
	 * @param value Values to be inserted in the columns
	 * @throws SQLException
	 */
	public void insertRowWithKeys (String tableName,
			String columnNames[], int columnCount, Object... value) throws SQLException
	{
		if (debugLevel > 1 || columnNames.length != value.length)
		{
			System.out.println("Starting insertRow");

			for (int i = 0; i < columnNames.length; i++)
			{
				System.out.print(columnNames[i] + " ");
			}
			System.out.println();
			for (int i = 0; i < value.length; i ++)
			{
				System.out.print(value[i] + " ");
			}
			System.out.println();
		}
		StringBuffer queryBuffer = new StringBuffer();
		queryBuffer.append("SELECT COUNT(*) AS COUNTER FROM " + 
				prefixTableName(tableName) +
			" WHERE ");
		
		for (int i = 0; i < columnCount; i++)
		{
			if (i > 0) {	queryBuffer.append(" AND "); }
				queryBuffer.append(columnNames[i] + "=? ");
			
		}
		PreparedStatement stmt = prepareStatement(new String(queryBuffer));
		if (debugLevel > 0)
		{
			System.out.println(new String(queryBuffer));
			System.out.print("     ");
			for (int i = 0; i < columnCount; i++)
			{
				if (i > 0) System.out.print(" ");
				System.out.print(value[i].toString());
			}
			System.out.println();
		}
		for (int i = 0; i < columnCount; i++)
		{
			stmt.setObject(i + 1, value[i]);
		}
		ResultSet rs = stmt.executeQuery();
		queryBuffer = new StringBuffer();
		rs.next();
		int rowCount = rs.getInt("COUNTER");
		if (rowCount == 0)
		{
			boolean firstItem = true;
			if (debugLevel > 1)
			{
				System.out.println("Inserting record");
			}
			queryBuffer.append("INSERT INTO " + prefixTableName(tableName) + " ( ");
			for (int i = 0; i < columnNames.length; i++)
			{
				if (value[i] == null) { continue; }
				if (!firstItem) { queryBuffer.append(", "); }
				queryBuffer.append(columnNames[i]);
				firstItem = false;
			}
			firstItem = true;
			queryBuffer.append(" ) VALUES ( ");
			for (int i = 0; i < columnNames.length; i++)
			{
				if (value[i] == null) { continue; }
				if (!firstItem) { queryBuffer.append(", "); }
				if (Number.class.isAssignableFrom(value[i].getClass()))
				{
					queryBuffer.append(value[i].toString());
				}
				else if (String.class.isAssignableFrom(value[i].getClass()))
				{
					queryBuffer.append("\'" + value[i].toString() + "\'");
				}
				firstItem = false;
			}
			queryBuffer.append(" )");
		}
		else
		{
			boolean firstItem = true;
			if (debugLevel > 1)
			{
				System.out.println("Updating row");
			}
			if (columnNames.length == 1)
			{
				return;
			}
			if (value[0] == null)
			{
				throw new SQLException("Primary key value may not be null");
			}
			queryBuffer.append("UPDATE " + prefixTableName(tableName) + " SET ");
			for (int i = columnCount; i < columnNames.length; i++)
			{
				if (value[i] == null) { continue; }
				if (!firstItem) 
				{ 
					queryBuffer.append(", "); 
				}
				queryBuffer.append(" " + columnNames[i] + "=");
				if (Number.class.isAssignableFrom(value[i].getClass()))
				{
					queryBuffer.append(value[i].toString());
				}
				else if (String.class.isAssignableFrom(value[i].getClass()))
				{
					queryBuffer.append("\'" + value[i].toString() + "\'");
				}
				firstItem = false;
			}
			queryBuffer.append(" WHERE ");
			firstItem = true;
			for (int i = 0; i < columnCount; i++)
			{
				if (!firstItem)
				{
					queryBuffer.append(" AND ");
				}
				queryBuffer.append(columnNames[i] + "=");
				if (Number.class.isAssignableFrom(value[i].getClass()))
				{
					queryBuffer.append(value[i].toString());
				}
				else if (String.class.isAssignableFrom(value[i].getClass()))
				{
					queryBuffer.append("\'" + value[i].toString() + "\'");
				}
				firstItem = false;
			}
		}
		if (debugLevel > 0)
		{
			System.out.println(new String(queryBuffer));
		}
		executeUpdate(new String(queryBuffer));
	}	
}
