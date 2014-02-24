package bradleyross.library.database;
/**
 * Subclass of DatabaseExtras for the
 * MySQL database management program.
 * @author Bradley Ross
 *
 */
public class MySQL extends DatabaseExtras 
{
	public String getDbms()
	{
		return "mysql";
	}
	protected void setup()
	{

		addTerm("CURRENTTIME", "NOW()");
		addTerm("CURRENTDATE", "CURDATE()");
		addTerm("DATE", "DATE");
		addTerm("DATETIME", "DATETIME");
		
		addTerm("DOUBLE", "DOUBLE");
		addTerm("FLOAT", "FLOAT");
		addTerm("LONGBLOB", "LONGBLOB");
		addTerm("LONGCLOB", "LONGCLOB");
		
	}
}
