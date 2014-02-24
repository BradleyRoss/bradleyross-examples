package bradleyross.library.database;
import bradleyross.library.database.DatabaseProperties;
import bradleyross.library.helpers.GenericPrinter;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.io.PrintStream;
import java.io.PrintWriter;
/**
 * Displays information about database.
 * @see DatabaseMetaData
 * @author Bradley Ross
 *
 */
public class ShowProperties 
{
	/**
	 * Object containing information on the database.
	 */
	protected DatabaseMetaData meta = null;
	/**
	 * True indicates that the class was able to
	 * successfully obtain a DatabaseMetaData object. 
	 */
	protected boolean valid = false;
	/**
	 * Constructor using java.sql.Connection object.
	 * @param conn Connection object
	 * @throws SQLException
	 */
	public ShowProperties(Connection conn) throws SQLException
	{
		try
		{
			meta = conn.getMetaData();
		}
		catch (SQLException e)
		{
			throw e;
		}
		valid = true;
	}
	/**
	 * Constructor using my database class.
	 * @param data Object containing database connectivity information.
	 * @throws SQLException
	 */
	public ShowProperties(DatabaseProperties data) throws SQLException
	{
		Connection conn;
		try
		{
			conn = data.getConnection();
			meta = conn.getMetaData();
		}
		catch (SQLException e)
		{
			throw e;
		}
		valid = true;
	}
	/** 
	 * Class returning database meta data object.
	 * @return Database meta data object
	 */
	public DatabaseMetaData getMetaData()
	{
		return meta;
	}
	/**
	 * Sends information on the database to the object.
	 * @param value Object for printing information,
	 * @throws SQLException
	 */
	public void printInfo(PrintStream value) throws SQLException
	{
		GenericPrinter printer = new GenericPrinter(value);
		printInfo(printer);
	}
	/**
	 * Sends information on the database to the object.
	 * @param value Object for printing information.
	 * @throws SQLException
	 */
	public void printInfo(PrintWriter value) throws SQLException
	{
		GenericPrinter printer = new GenericPrinter(value);
		printInfo(printer);
	}
	/**
	 * Sends information on the database to the object.
	 * @param printer GenericPrinter object to be used for output
	 * @throws SQLException
	 */
	public void printInfo(GenericPrinter printer) throws SQLException
	{
		printer.println("Database is " + meta.getDatabaseProductName() + " " 
				+ Integer.toString(meta.getDatabaseMajorVersion()) + "."
				+ Integer.toString(meta.getDatabaseMinorVersion()));
		printer.println("Handler is " + meta.getDriverName());
		printer.println("The term catalog corresponds to " + meta.getCatalogTerm());
		printer.println("The term schema corresponds to  " + meta.getSchemaTerm());
		printer.println("Time/Date functions are " + meta.getTimeDateFunctions());
		printer.println("Supports catalogs in data manipulation: " 
				+ Boolean.toString(meta.supportsCatalogsInDataManipulation()));
		printer.println("Supports schemas in data manipulation: "
				+ Boolean.toString(meta.supportsSchemasInDataManipulation()));
		printer.println("The following characters can be used in unquoted identifier names: " 
				+ meta.getExtraNameCharacters());
	}
	/**
	 * Sends information on the database to System.out.
	 * @see System#out
	 * @throws SQLException
	 */
	public void printInfo() throws SQLException
	{
		printInfo(System.out);
	}

}
