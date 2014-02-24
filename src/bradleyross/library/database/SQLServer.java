package bradleyross.library.database;
/**
 * Subclass of DatabaseExtras for the SQLServer database
 * manager by Microsoft.
 * 
 * <p>The following are the date and time functions for SQL Server.</p>
 * <ul>
 * <li><b>DateAdd</b> Adds to a date (days, weeks, and so on)</li>
 * <li><b>DateDiff</b> Calculates the difference between two dates</li>
 * <li><b>DateName</b> Returns a string representation of date parts</li>
 * <li><b>DatePart</b> Returns parts of a date (day of week, month, year, and so on)</li>
 * <li><b>Day</b> Returns the day of month of a year</li>
 * <li><b>GetDate</b> Returns the current date and time</li>
 * <li><b>Month</b> Returns the month portion of a date</li>
 * <li><b>Year</b> Returns the year portion of a date</li>
 * </ul>
 * @author Bradley Ross
 *
 */
public class SQLServer extends DatabaseExtras 
{
	public String getDbms()
	{
		return "sqlserver";
	}
	protected void setup()
	{

		addTerm("CURRENTTIME", "GETDATE()");

		addTerm("DATE", "SMALLDATETIME");
		addTerm("DATETIME", "DATETIME");
		
		addTerm("DOUBLE", "DOUBLE");
		
		addTerm("FLOAT", "FLOAT");
		
		addTerm("LONGBLOB", "VARBINARY(MAX)");
		addTerm("LONGCLOB", "VARCHAR(MAX)");
		
		
	}
}
