package bradleyross.library.database;
import java.util.Hashtable;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Types;
import java.sql.DatabaseMetaData;
import java.io.StringWriter;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Collection;
import bradleyross.library.database.DatabaseProperties;
/**
 * Superclass for handling interactions with a database table.
 * @author Bradley Ross
 *
 */
public class TableHelper 
{
	/**
	 * Encapsulates information on a column in the table.
	 * @author Bradley Ross
	 *
	 */
	class ColumnStatus
	{
		/**
		 * Name of column.
		 */
		protected String name = null;
		/**
		 * Name of the java.sql or java.lang class that is
		 * used to represent the information in this column.
		 */
		protected String dataclassName = null;
		/**
		 * True indicates that value of column has been changed.
		 */
		protected boolean isChanged = false;
		/**
		 * Data type as defined by java.sql.Types.
		 * @see Types
		 */
		protected int type = -1;
		/**
		 * Code from java.sql.Types that is associated with this
		 * column.
		 */
		protected Class<?> objectClass = null;
		/**
		 * True indicates that the value contains a null;
		 */
		boolean isNull = false;
		/**
		 * Value of the column.
		 */
		Object value = null;
		public ColumnStatus(String nameValue, int typeValue)
		{
			name = nameValue;
			type = typeValue;
			isChanged = false;
		}
		public String getName()
		{
			return name;
		}
		public int getType()
		{
			return type;
		}
		public void setName(String value)
		{
			name = value;
		}
		public void setType(int value)
		{
			type = value;
		}
	}
	/**
	 * List of column names and the data types as defined in java.sql.Types.
	 */
	protected Hashtable<String, ColumnStatus> columns = new Hashtable<String, ColumnStatus>();
	/**
	 * Object containing information for connecting to database.
	 */
	protected DatabaseProperties data = null;
	public void setDatabase(DatabaseProperties dataValue)
	{
		data = dataValue;
	}
	public DatabaseProperties getDatabase()
	{
		return data;
	}
	public TableHelper(DatabaseProperties data, String catalog, String schema, String table)
	throws SQLException
	{
		DatabaseMetaData meta = data.getConnection().getMetaData();
		ResultSet rs = meta.getColumns(catalog, schema, table, null);
		String columnName = null;
		int dataType = -100000;
		while (rs.next())
		{
			columnName = rs.getString("COLUMN_NAME");
			dataType = rs.getInt("DATA_TYPE");
			if (rs.wasNull())
			{
				dataType = -100000;
			}
			ColumnStatus properties = new ColumnStatus(columnName, dataType);
			columns.put(columnName, properties);
		}
	}
	/**
	 * Display contents of internal tables for diagnostic purposes.
	 * @return Information
	 */
	public String displayEntries()
	{
		StringWriter writer = new StringWriter();
		PrintWriter printer = new PrintWriter(writer);
		Collection<ColumnStatus> collection = columns.values();
		Iterator<ColumnStatus> iter = collection.iterator();
		while (iter.hasNext())
		{
			ColumnStatus item = iter.next();
			printer.println(item.getName() + " " + Integer.toString(item.getType()) + " " +
					DatabaseProperties.decodeSqlType(item.getType()));
		}
		return writer.toString();
	}
	
}
