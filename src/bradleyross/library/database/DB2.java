package bradleyross.library.database;

import bradleyross.library.database.DatabaseExtras;
/**
 * Subclass of DatabaseExtras for the DB2 database server.
 * @author Bradley Ross
 *
 */
public class DB2 extends DatabaseExtras 
{

	@Override
	public String getDbms() 
	{
		
		return "db2";
	}

	@Override
	protected void setup() 
	{
		;
	}

}
