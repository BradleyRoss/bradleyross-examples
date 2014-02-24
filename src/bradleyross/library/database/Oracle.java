package bradleyross.library.database;
/**
 * Subclass of DatabaseExtras for the Oracle database server.
 * 
 * @author Bradley Ross
 *
 */
public class Oracle extends DatabaseExtras 
{
	protected String databaseCode = "oracle";
	public String getDbms()
	{
		return "oracle";
	}
	protected void setup()
	{
		addTerm("CURRENTTIME", "SYSDATE");
		addTerm("CURRENTDATE", "TRUNC(SYSDATE)");
		addTerm("DATE", "DATE");
		addTerm("DATETIME", "TIMESTAMP");
		addTerm("LONGBLOB", "BLOB");
		addTerm("LONGCLOB", "CLOB");
	}
}
