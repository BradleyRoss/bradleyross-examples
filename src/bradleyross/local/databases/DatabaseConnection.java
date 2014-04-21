/**
 * 
 */
package bradleyross.local.databases;

import java.sql.SQLException;
import java.sql.ResultSet;

import bradleyross.library.database.DatabaseProperties;

/**
 * Information to connecting to databases on my laptop.
 * <ul>
 * <li><p>dummy</p></li>
 * <li><p>sample</p></li>
 * <li><p>test</p></li>
 * <li><p>tomcat</p></li>
 * @author Bradley Ross
 *
 */
public class DatabaseConnection extends DatabaseProperties 
{

	/**
	 * 
	 */
	public DatabaseConnection() 
	{ ;	}
	/**
	 * Constructor specifying database to be used.
	 * @param value Specifies database to be used.
	 */
	public DatabaseConnection(String value) 
	{
		super(value);
	}

	/**
	 * Constructor specifying database and database account to be used.
	 * @param systemName Database to be used.
	 * @param accountName Database account to be used.
	 */
	public DatabaseConnection(String systemName, String accountName) 
	{
		super(systemName, accountName);
	}

	/** 
	 * This method specifies a default database and database account.
	 * @see bradleyross.library.database.DatabaseProperties#setDatabaseInstance()
	 */
	@Override
	public void setDatabaseInstance() 
	{
		setDatabaseInstance("sample", "sample");
	}

	/** 
	 * This method specifies a default database account to be used for each database.
	 * @see bradleyross.library.database.DatabaseProperties#setDatabaseInstance(java.lang.String)
	 */
	@Override
	public void setDatabaseInstance(String system) 
	{
		if (system.equalsIgnoreCase("sample"))
		{
			setDatabaseInstance("sample", "sample");
		}

	}

	/** 
	 * 
	 * @see bradleyross.library.database.DatabaseProperties#setDatabaseInstance(java.lang.String, java.lang.String)
	 */
	@Override
	public void setDatabaseInstance(String system, String user) 
	{
		if (system.equalsIgnoreCase("sample") && user.equalsIgnoreCase("sample"))
		{
			accountName = "sample";
			setPassword("mypass");
			portNumber=1521;
			domainName="localhost";
			handlerClass="com.mysql.jdbc.Driver";
			connectionString="jdbc:mysql://localhost/sample";
		}
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		DatabaseProperties data = new DatabaseConnection();
		try
		{
		data.connect();
		System.out.println("Database connected");
		String sqlCode = "SELECT * FROM test";
		ResultSet rs = data.executeQuery(sqlCode);
		while (rs.next())
		{
			System.out.println(Integer.toString(rs.getInt("counter")) + " " + rs.getString("value"));
		}
		data.close();
		System.out.println("Database closed");
		}
		catch (SQLException e)
		{
			System.out.println(e.getClass().getName() + " " + e.getMessage());
			e.printStackTrace(System.out);
		}
	}
}
