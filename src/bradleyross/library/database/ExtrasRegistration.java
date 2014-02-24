package bradleyross.library.database;
import java.util.Hashtable;
import java.util.Enumeration;
import java.util.Collection;
import java.util.Vector;
import java.util.Iterator;
import bradleyross.library.NotSupportedException;
import bradleyross.library.database.DatabaseExtras;
/**
 * Provide for registration of DatabaseExtras
 * subclasses.
 * @see DatabaseExtras
 * @author Bradley Ross
 *
 */
public class ExtrasRegistration 
{
	/**
	 * Contains registration information for a subclass of DatabaseExtras
	 * @author Bradley Ross
	 *
	 */
	public static class Item
	{
		/**
		 * DatabaseExtras object for handling the database specific properties.
		 */
		protected DatabaseExtras object = null;
		/**
		 * Name of the DatabaseExtras subclass that handles the database
		 * specific properties.
		 */
		protected String extrasName = null;
		/**
		 * Character string that identifies the type of database.  
		 * 
		 * <p>Typical values are <code>SQLSERVER</code>, <code>ORACLE</code>,
		 *    and <code>MYSQL</code>.</p>
		 */
		protected String databaseCode = null;
		/**
		 * This object is instantiated when the DatabaseExtras object
		 * calls the register method below.
		 * 
		 * @see #register(DatabaseExtras)
		 * @param extrasObject
		 * @throws ClassNotFoundException
		 * @throws InstantiationException
		 * @throws IllegalAccessException
		 * @throws NotSupportedException
		 */
		public Item(DatabaseExtras extrasObject) 
		throws ClassNotFoundException, InstantiationException, IllegalAccessException, NotSupportedException
		{
			extrasName = extrasObject.getClass().getName();

			object = extrasObject;
			databaseCode = object.getDbms();
		}
		/**
		 * Return the code that identifies the type of database.
		 * @return Code for specific type of database
		 */
		public String getDbms()
		{
			return databaseCode;
		}
		/**
		 * Return the DatabaseExtras object that handles the
		 * specific type of database.
		 * @return DatabaseExtras object
		 */
		public DatabaseExtras getDatabaseExtras()
		{
			return object;
		}
		/**
		 * Return the name of the DatabaseExtras subclass that handles the
		 * specific type of database.
		 * @return Name of subclass
		 */
		public String getClassName()
		{
			return extrasName;
		}
	}
	/**
	 * This table contains the registration information for the various DatabaseExtras objects.
	 */
	protected static Hashtable<String, Item> registrationList = new Hashtable<String, Item>();
	/**
	 * This method is called by the DatabaseExtras objects when they are instantiated.
	 * @see DatabaseExtras
	 * @param object DatabaseExtras object to be registered
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws NotSupportedException
	 */
	public static void register(DatabaseExtras object) 
	throws ClassNotFoundException, InstantiationException, IllegalAccessException, NotSupportedException
	{
		Item item = new ExtrasRegistration.Item(object);
		registrationList.put(item.getDbms(), item);
	}
	/**
	 * Return the entire contents of the registration list.
	 * 
	 * <p>Primarily for diagnostics.
	 * 
	 * @return Collection of registration information
	 */
	public static Collection<Item> getContents()
	{
		return registrationList.values();
	}
	/**
	 * Returns an array containing the codes for the registered databases.
	 * @return Array of code values
	 */
	public static String[] getNames()
	{
		String test[] = new String[1];
		Enumeration<String> keys = registrationList.keys();
		Vector<String> list = new Vector<String>();
		while (keys.hasMoreElements())
		{ list.add(keys.nextElement()); }
		list.trimToSize();
		return list.toArray(test);
	}
	/**
	 * Return the DatabaseExtras object that is appropriate for a specific type
	 * of database.
	 * 
	 * @param code The code identifying the DBMS
	 * @return The appropriate DatabaseExtras object
	 */
	public static DatabaseExtras getDatabaseExtras(String code)
	{
		Item temp = registrationList.get(code);
		return temp.getDatabaseExtras();
	}
	/**
	 * Test driver
	 * @param args Not used in this case
	 */
	public static void main(String[] args) 
	{
		try
		{
			Class.forName("bradleyross.library.database.MySQL").newInstance();
			Class.forName("bradleyross.library.database.SQLServer").newInstance();
			Class.forName("bradleyross.library.database.DB2").newInstance();
			Class.forName("bradleyross.library.database.Postgres").newInstance();
			Class<?> thisclass = Class.forName("bradleyross.library.database.Oracle");
			thisclass.newInstance();
			Collection<Item> list = getContents();
			Iterator<Item> iter = list.iterator();
			while (iter.hasNext())
			{
				System.out.println();
				Item next = (Item) iter.next();
				System.out.println(next.getDbms() + " " + next.getDatabaseExtras().getClass().getName());
				System.out.println(next.getDatabaseExtras().listAllTerms());
			}
		}
		catch (Exception e)
		{
			System.out.println(e.getClass().getName() + " " + e.getMessage());
			e.printStackTrace();
		}
		System.out.println();
		System.out.println("Registered databases");
		String names[] = getNames();
		for (int i = 0; i < names.length; i++)
		{
			String item = names[i];
			
			DatabaseExtras instance = ExtrasRegistration.getDatabaseExtras(item);
			System.out.println(item + " " + instance.getDbms());
		}
	}
}
