package bradleyross.library.debugging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import bradleyross.library.helpers.ExceptionHelper;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.ResultSet;
// import java.sql.PreparedStatement;
import java.sql.SQLException;
// import java.sql.Driver;
import java.sql.DriverManager;
// import java.sql.Connection;
// import java.sql.ResultSet;
// import java.sql.Statement;
// import java.sql.ResultSetMetaData;
import java.util.List;
import java.util.Properties;
import java.util.ArrayList;
/**
 * 
 * @author Bradley Ross
 *
 * @see Connection
 * @see DriverManager
 * @see Statement
 * @see ResultSet
 */
public class SQLExample {
	protected static Logger logger = null;
	protected static ExceptionHelper helper = null;
	protected String accountName = "sample";
	protected String password = "mypass";
	protected int portNumber = 1521;
	protected String domainName = "localhost";
	protected String handlerClass="com.mysql.jdbc.Driver";
	protected String connectionString="jdbc:mysql://localhost/sample" + 
			"?sessionVariables=sql_mode='ANSI'";
	static {
		Properties props = System.getProperties();
		if (!props.containsKey("catalina.home")) {
			System.setProperty("catalina.home", System.getProperty("user.home"));
		}
		logger = LoggerFactory.getLogger(bradleyross.library.debugging.SQLExample.class);
		helper = new ExceptionHelper(logger);
	}
	public void run() {
		Connection connection = null;
		Statement statement = null;
		try
		{
			Class.forName(handlerClass).newInstance();
		}
		catch (InstantiationException e)
		{
			List<String> strings = new ArrayList<String>();
			strings.add("*** Error in establishing instance of handler");
			strings.add("*** Instantiation Exception");
			helper.error(strings, e);
			return;
		}
		catch (IllegalAccessException e )
		{
			List<String> strings = new ArrayList<String>();
			strings.add("*** Error in establishing instance of handler");
			strings.add("*** Illegal Access Exception");
			helper.error(strings, e);
			return;
		}
		catch (ClassNotFoundException e)
		{
			List<String> strings = new ArrayList<String>();
			strings.add("*** Error in establishing instance of handler");
			strings.add("*** Class Not Found Exception");
			helper.error(strings, e);
			return;
		}
		try
		{
			connection = DriverManager.getConnection(connectionString, accountName,
					password); 
			connection.setAutoCommit(true);
		}
		catch (SQLException e)
		{
			List<String> strings = new ArrayList<String>();
			System.out.println("SQL Exception");
			System.out.println(e.getMessage());
			helper.error(strings, e);
			return;
		}
		/*  First statement */
		helper.info("Database is now open");
		helper.info("First statement is SELECT STATE, NAME FROM STATE");
		try {
			statement = connection.createStatement();
			helper.info("Statement created");
			ResultSet rs = statement.executeQuery("SELECT STATE, NAME FROM STATE");
			helper.info("ResultSet created");
			while (rs.next()) {
				System.out.println(rs.getString("STATE") + "  " +
			rs.getString("NAME"));
			}
		} catch (SQLException e) {
			helper.error("Problem with first statement", e);
		} catch (RuntimeException e) {
			helper.error("RuntimeException encountered", e);
			throw e;
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
			} catch (SQLException e) {
				helper.error("Problem closing statement", e);
			}
		}
		/* 
		 * Second statement
		 */
		helper.info("Second statement is SELECT STATE, NAME FROM NOTTHERE");
		try {
			statement = connection.createStatement();
			helper.info("Statement created");
			ResultSet rs = statement.executeQuery("SELECT STATE, NAME FROM NOTTHERE");
			helper.info("ResultSet created for second statement");
			while (rs.next()) {
				System.out.println(rs.getString("STATE") + "  " +
			rs.getString("NAME"));
			}
		} catch (SQLException e) {
			helper.error("Problem with second statement", e);
		} catch (RuntimeException e) {
			helper.error("RuntimeException encountered in second statement", e);
			throw e;
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
			} catch (SQLException e) {
				helper.error("Problem closing second statement", e);
			}
		}
		helper.info("Third statement is SELECT STATE, NAME, NOTTHERE FROM STATE");
		try {
			statement = connection.createStatement();
			helper.info("Statement created");
			ResultSet rs = statement.executeQuery("SELECT STATE, NAME, NOTTHERE FROM STATE");
			helper.info("ResultSet created");
			while (rs.next()) {
				System.out.println(rs.getString("STATE") + "  " +
			rs.getString("NAME"));
			}
		} catch (SQLException e) {
			helper.error("Problem with third statement", e);
		} catch (RuntimeException e) {
			helper.error("RuntimeException encountered in third statement", e);
			throw e;
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
			} catch (SQLException e) {
				helper.error("Problem closing third statement", e);
			}
		}
		try {
			connection.close();
		} catch (SQLException e) {
			helper.error("Error while closing connection", e);
		}
	}
	public static void main(String[] args) {

		SQLExample instance = new SQLExample();
		instance.run();
	}
}
