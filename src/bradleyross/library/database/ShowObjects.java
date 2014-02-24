package bradleyross.library.database;
import bradleyross.library.database.DatabaseProperties;
import bradleyross.library.helpers.StringHelpers;
import java.sql.ResultSet;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.io.File;
import java.io.IOException;
import java.io.FileWriter;
/**
 * Quick and dirty program to make files out of procedures and views
 * in the database.
 * <p>This only works for SQL Server.</p>
 * <p>It was called by com.amtrak.eng.vtr.research.VTRListProcedures</p>
 * @author Bradley Ross
 *
 */
public class ShowObjects 
{
	DatabaseProperties data = null;
	DatabaseMetaData schema = null;
	File rootDirectory1 = null;
	File rootDirectory2 = null;
	public ShowObjects(DatabaseProperties value) throws SQLException
	{
		data = value;
		schema = data.getConnection().getMetaData();
		rootDirectory1 = 
			new File("/Users/bradleyross/AmtrakTest/Procedures");
		rootDirectory2 = 
			new File("/Users/bradleyross/AmtrakTest/Views");
	}
	public void listProcedures()
	{
		ResultSet rs = null;
		ResultSet rs2 = null;
		
		String sqlQuery = null;
		int counter = 0;
		
		try
		{
			rs = schema.getProcedures((String) null, (String) null, (String) null);
			while (rs.next())
			{
				
				counter++;
				
				String schemaName = rs.getString("PROCEDURE_SCHEM");
				String working = rs.getString("PROCEDURE_NAME");
				String procedureName = working.substring(0, working.indexOf(";"));
				/*
				 * This gets the number after the semicolon.  It appears that
				 * version numbers are not supported in SQL Server
				 */
				String value = working.substring(working.indexOf(";") + 1);
				if (schemaName == null) { continue; }
				if (schemaName.equalsIgnoreCase("sys")) { continue; }
				if (schemaName.equalsIgnoreCase("INFORMATION_SCHEMA")) { continue; }
				System.out.println(working);
				System.out.println(StringHelpers.padLeft(Integer.toString(counter), 5) + " " +
						StringHelpers.padLeft(value, 5) + " " +
						StringHelpers.padRight(schemaName, 8) + " " +
						StringHelpers.padRight(procedureName, 30) + " " + 
						" " + rs.getString("PROCEDURE_TYPE"));
				
			
					sqlQuery = "SELECT A.NAME AS PROCEDURE_NAME, A.TYPE_DESC AS TYPE, " +
					" B.NAME AS SCHEMA_NAME, C.DEFINITION AS SOURCE " +
					" FROM VTR.SYS.PROCEDURES A, VTR.SYS.SCHEMAS B, VTR.SYS.SQL_MODULES C " +
					" WHERE A.SCHEMA_ID=B.SCHEMA_ID AND A.OBJECT_ID=C.OBJECT_ID AND " +
					" A.NAME=\'" + procedureName + "\'";
					System.out.println("     Trying " + sqlQuery);
					rs2 = data.executeQuery(sqlQuery);
					int check = 0;
					while (rs2.next())
					{
						check++;
						if (check >1)
						{
							System.out.println("Too many rows");
						}
						File output = new File (rootDirectory1,
								rs2.getString("SCHEMA_NAME") + "_" +
								rs2.getString("PROCEDURE_NAME") + ".txt");
						FileWriter writer = new FileWriter(output);
						String text = rs2.getString("SOURCE");
						System.out.println(rs.getString("PROCEDURE_NAME") + " " 
								+ rs2.getString("SCHEMA_NAME") );
						writer.write(text);
						writer.close();
					}
				
				sqlQuery = null;

			}
			
		}
		catch (SQLException e)
		{
			System.out.println(e.getClass().getName() + " " + e.getMessage());
			if (sqlQuery != null)
			{
				System.out.println(sqlQuery);
			}
		}
		catch (IOException e)
		{
			System.out.println(e.getClass().getName() + " " + e.getMessage());
		}
	}
	public void listViews()
	{
		ResultSet rs = null;
		ResultSet rs2 = null;
		String permissions[] = {"VIEW"};
		int counter = 0;
		String sqlQuery = null;
		try
		{
			rs = schema.getTables((String) null, (String) null, (String) null,permissions);
			while (rs.next())
			{
				
				counter++;
				
				String schemaName = rs.getString("TABLE_SCHEM");
				String tableName = rs.getString("TABLE_NAME");
				
				if (schemaName == null) { continue; }
				if (schemaName.equalsIgnoreCase("sys")) { continue; }
				if (schemaName.equalsIgnoreCase("INFORMATION_SCHEMA")) { continue; }

				System.out.println(StringHelpers.padLeft(Integer.toString(counter), 5) + " " +
						StringHelpers.padRight(schemaName, 8) + " " +
						StringHelpers.padRight(tableName, 30) + " " + 
						" " + rs.getString("TABLE_TYPE"));
				
			
					sqlQuery = "SELECT A.NAME AS TABLE_NAME, A.TYPE_DESC AS TYPE, " +
					" B.NAME AS SCHEMA_NAME, C.DEFINITION AS SOURCE " +
					" FROM VTR.SYS.VIEWS A, VTR.SYS.SCHEMAS B, VTR.SYS.SQL_MODULES C " +
					" WHERE A.SCHEMA_ID=B.SCHEMA_ID AND A.OBJECT_ID=C.OBJECT_ID AND " +
					" A.NAME=\'" + tableName + "\'";
					System.out.println("     Trying " + sqlQuery);
					rs2 = data.executeQuery(sqlQuery);
					int check = 0;
					while (rs2.next())
					{
						check++;
						if (check >1)
						{
							System.out.println("Too many rows");
						}
						File output = new File (rootDirectory2,
								rs2.getString("SCHEMA_NAME") + "_" +
								rs2.getString("TABLE_NAME") + ".txt");
						FileWriter writer = new FileWriter(output);
						String text = rs2.getString("SOURCE");
						System.out.println(rs.getString("TABLE_NAME") + " " 
								+ rs2.getString("SCHEMA_NAME") );
						writer.write(text);
						writer.close();
					}
				
				sqlQuery = null;

			}
			
		}
		catch (SQLException e)
		{
			System.out.println(e.getClass().getName() + " " + e.getMessage());
			if (sqlQuery != null)
			{
				System.out.println(sqlQuery);
			}
		}
		catch (IOException e)
		{
			System.out.println(e.getClass().getName() + " " + e.getMessage());
		}
	}

}
