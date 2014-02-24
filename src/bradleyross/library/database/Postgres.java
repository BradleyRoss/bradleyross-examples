package bradleyross.library.database;
/**
 * Subclass of DatabaseExtras for the Postgres database server.
 * @author Bradley Ross
 *
 */
public class Postgres extends DatabaseExtras 
{

	@Override
	public String getDbms() 
	{
		
		return "postgres";
	}

	@Override
	protected void setup() 
	{
		;

	}

}
