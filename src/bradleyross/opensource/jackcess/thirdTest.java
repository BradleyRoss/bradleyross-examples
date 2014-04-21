package bradleyross.opensource.jackcess;
// import java.sql.Types;
import com.healthmarketscience.jackcess.*;
import bradleyross.opensource.jackcess.helpers;
/** 
* Reads an MDB file using the Microsoft Access 2000 format
* and displays information concerning its layout and
* contents.
* @author Bradley Ross
*/
public class thirdTest 
{
	static void processTable(Table table) throws Exception
	{
		Cursor cursor;
		// java.util.Iterator<java.util.Map<String,Object>> iterator = null;
		java.util.Map<String,Object> row;
		String indexedColumnName = null;
		/*
		** List columns
		*/
		System.out.println("There are " + Integer.toString(table.getRowCount()) + " rows");
		System.out.println("List names of columns");
		System.out.println("There are " + Integer.toString(table.getColumnCount()) + " columns");
		java.util.List<Column> columnList = table.getColumns();
		for (int i = 0; i < table.getColumnCount(); i++)
		{
			Column column = columnList.get(i);
			System.out.print(
			Short.toString( column.getColumnNumber()) + " " 
			+ columnList.get(i).getName() + " " +
			helpers.decodeDataType(column.getType()) + " ");
			if (i%3 ==2) 
				{ System.out.println(""); }
			else
				{ System.out.print("          "); }
		}
		java.util.List<Index> indexList = table.getIndexes();
		System.out.println("");
		System.out.println("There are " + Integer.toString(indexList.size()) + " indexes");
		for (int i = 0; i < indexList.size(); i++)
		{
			Index index = indexList.get(i);
			System.out.println(Integer.toString(i) + " " + index.getName() +
				" " + Boolean.toString(index.isForeignKey()) + ":" +
				Boolean.toString(index.isPrimaryKey()) + ":" +
				Boolean.toString(index.isUnique()));
			java.util.List<Index.ColumnDescriptor> indexColumns = index.getColumns();
			System.out.print("Columns:  ");
			for (int i2 = 0; i2 < indexColumns.size(); i2++)
			{
				Column indexItem = indexColumns.get(i).getColumn();
				indexedColumnName = indexItem.getName();
				System.out.print(indexedColumnName + "(" + Integer.toString(indexItem.getSQLType()) +")");
			}
			System.out.println(":");
			cursor = Cursor.createIndexCursor(table, index);
			// iterator = cursor.iterator();
			/*
			** The documentation needs a mapping of SQL Types and the classes
			** of the objects that are stored in the Map object.
			*/
			cursor.beforeFirst();
			row = cursor.getNextRow();
			System.out.println("First row");
			System.out.println(row.get(indexedColumnName).getClass().getName());

			printRow(row, table);
            cursor.afterLast();
            System.out.println("Last row");
            row = cursor.getPreviousRow();

			printRow(row, table);
		}
	}
	protected static void printRow(java.util.Map<String,Object> row, Table table)
	{
		try
		{
			int columnCount = table.getColumnCount();
			java.util.List<Column> columnList = table.getColumns();
			System.out.println(Integer.toString(columnCount) + " columns");
			for (int i = 0; i < columnCount; i ++)
			{
				String columnName = columnList.get(i).getName();
				short columnNumber = columnList.get(i).getColumnNumber();
				System.out.println(Short.toString(columnNumber) + " " + columnName + " " +
					helpers.printObject(row.get(columnName)));
			}
		}
		catch (Exception e)
		{
		System.out.println("Exception: " + e.getClass().getName() + " " +
			e.getMessage());
		e.printStackTrace();
		}
		
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		String fileName = "test.mdb";
		if (args.length >0) { fileName = args[0]; }
		try
		{
			System.out.println("Starting thirdTest");
			Database db = Database.open(new java.io.File(fileName));
            String tableNameList[] = db.getTableNames().toArray(new String[1] );
            for (int i = 0; i < tableNameList.length; i++)
            {
            	System.out.println("*****  *****");
            	System.out.println("*****  *****");
            	System.out.println("Processing table " + tableNameList[i]);
            	processTable(db.getTable(tableNameList[i]));
            }
			db.flush();
			db.close();
			System.out.println("Ending thirdTest");
		}
		catch (Exception e)
		{
			System.out.println("Exception " + e.getClass().getName() +
					" " + e.getMessage());
		}
	}
}