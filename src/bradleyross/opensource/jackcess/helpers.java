package bradleyross.opensource.jackcess;
import com.healthmarketscience.jackcess.DataType;
/**
 * Static methods that are designed to aid in the use
 * of the Jackcess software to read MDB files.
 * @author Bradley Ross
 *
 */
public class helpers 
{
	/**
	 * There are no constructors for this method since
	 * all of the methods are static.
	 */
	protected helpers()
	{}
	/**
	 * Returns a string object representing the value of 
	 * a value belonging to the 
	 * {@link com.healthmarketscience.jackcess.DataType}
	 * class.
	 * @param valueIn Type of database column
	 * @return String indicating type of value
	 */
	public static String decodeDataType(DataType valueIn)
	{
		String working = null;
		if (valueIn == DataType.BINARY) { working="BINRY"; }
		else if (valueIn == DataType.BOOLEAN) { working="BOOLEAN"; }
		else if (valueIn == DataType.BYTE) { working="BYTE"; }
		else if (valueIn == DataType.DOUBLE) { working="DOUBLE"; }
		else if (valueIn == DataType.FLOAT) { working="FLOAT"; }
		else if (valueIn == DataType.INT) { working="INT"; }
		else if (valueIn == DataType.LONG) { working="LONG"; }
		else if (valueIn == DataType.MEMO) { working="MEMO"; }
		else if (valueIn == DataType.MONEY) { working="MONEY"; }
		else if (valueIn == DataType.NUMERIC) { working="NUMERIC"; }
		else if (valueIn == DataType.SHORT_DATE_TIME)
			{ working = "SHORT_DATE_TIME"; }
		else if (valueIn == DataType.TEXT) { working="TEXT"; }
		else if (valueIn == DataType.UNKNOWN_0D)
			{ working = "UNKNOWN_OD"; }
		else {working="UNKNOWN"; }
		return working;
	}
	/**
	 * Prints an object that may belong to one of several
	 * classes.
	 * <p>The first step is to test if the object is
	 *    equal to null, since you can't determine the
	 *    class of a null object.  If the object is
	 *    equal to null, the string **null value**
	 *    is returned.</p>
	 *    @param o Object to be printed
	 *    @return String containing value of object
	 *    @see java.lang.Integer
	 *    @see java.lang.Short
	 *    @see java.lang.Long
	 *    @see java.lang.Float
	 *    @see java.lang.Double
	 *    @see java.lang.Boolean
	 */
	public static String printObject (Object o)
	{
		if (o == null) { return "**null value**"; }
		String working = "**bad value**";
		String className = o.getClass().getName();
		try
		{
			if (className.equalsIgnoreCase("java.lang.String"))
				{ working = (String) o; }
			else if (className.equalsIgnoreCase("java.lang.Boolean"))
				{ working = ((Boolean) o).toString(); }
			else if (className.equalsIgnoreCase("java.lang.Double"))
				{ working = ((Double) o).toString(); }
			else if (className.equalsIgnoreCase("java.lang.Float"))
				{ working = ((Float) o).toString(); }
			else if (className.equalsIgnoreCase("java.lang.Long"))
				{ working = ((Long) o).toString(); }
			else if (className.equalsIgnoreCase("java.lang.Integer"))
				{ working = ((Integer) o).toString(); }
			else if (className.equalsIgnoreCase("java.lang.Short"))
				{ working = ((Short) o).toString(); }
			else
				{ working = className + " has no rules"; }
		}
		catch (Exception e)
		{
			System.out.println("Unable to print value " + className);
		}
		return working;
	}
}
