package bradleyross.library.database;
import java.io.StringWriter;
import java.io.PrintWriter;
import java.util.Hashtable;
import java.util.Enumeration;
import bradleyross.library.database.ExtrasRegistration;

/**
 * Template for database specific methods.
 * 
 * <p>The goal would be to have a separate class for each type of database.
 *    These classes would then be registered with the ExtrasRegistration class
 *    and the DatabaseProperties subclass would then use the appropriate
 *    DatabaseExtras class to obtain information that varies between databases.</p>
 * 
 * @author Bradley Ross
 *
 */
abstract class DatabaseExtras 
{
	/**
	 * Returns database code
	 * @return Identifying code for database type
	 */
	public abstract String getDbms();
	/**
	 * The objects automatically register themselves when instantiated.
	 */
	public DatabaseExtras() 
	{
		try
		{
			ExtrasRegistration.register(this);
			setup();
		}
		catch (Exception e)
		{
			System.out.println(e.getClass().getName() + " " + e.getMessage());
		}
	}
	/**
	 * Sets up database specific objects.
	 */
	protected abstract void setup();
	/**
	 * Array containing valid codes for database specific terms.
	 * <table border="1">

	 * <tr><td><code>CURRENTTIME</code></td><td>Database specific term for obtaining current
	 *     date and time to be used as the value of a column.</td></tr>
	 * <tr><td><code>CURRENTDATE</code></td><td>Database specific term for obtaining current
	 *     date to be used as the value of a column.  (Hour, minutes, seconds and 
	 *     fractional seconds set to zero)</td></tr>
	 * <tr><td><code>DATE</code></td><td>Data type to be used for representing dates
	 *     (Hours, minutes, seconds, and fractional seconds set to zero)</td></tr>
	 * <tr><td><code>DATETIME</code></td><td>Data type to be used for date and time.</td></tr>
	 * <tr><td><code>DOUBLE</code></td><td>Data type to be used for double
	 *     precision floating point values.</tr>
	 * <tr><td><code>FLOAT</code></td><td>Data type to be used for single
	 *     precision floating point values.</td></tr>
	 * <tr><td><code>LONGBLOB</code></td><td>Data type for very long binary objects.</td></tr>
	 * <tr><td><code>LONGCLOB</code></td><td>Data type for very long character objects.</td></tr>
	 * 
	 * 
	 * </table>
	 */
	protected String[] validTerms = { "CURRENTDATE", "CURRENTTIME", 
			"DATE", "DATETIME",
			"DOUBLE", "FLOAT", "LONGBLOB", "LONGCLOB" };
	/**
	 * Contains mapping of database specific terms to their code values.
	 */
	Hashtable<String, String> terms = new Hashtable<String,String>();
	/**
	 * Add a database specific term to the list.
	 * @param key Code for database specific term
	 * @param value Value of database specific term
	 */
	protected void addTerm(String key, String value)
	{
		terms.put(key, value);
	}
	/**
	 * Returns a database specific term for a function.
	 * @param value Identifier for term
	 * @return Database specific term
	 */
	public String getTerm(String value)
	{
		return (String) terms.get(value.trim().toUpperCase());
	}
	public String listAllTerms()
	{
		StringWriter writer = new StringWriter();
		PrintWriter printer = new PrintWriter(writer);
		Enumeration<String> keys = terms.keys();
		printer.println("Listing terms for " + getDbms());
		while (keys.hasMoreElements())
		{
			String keyValue = keys.nextElement();
			String term = terms.get(keyValue);
			printer.println(keyValue+"="+term);
		}
		return writer.toString();
	}
}
