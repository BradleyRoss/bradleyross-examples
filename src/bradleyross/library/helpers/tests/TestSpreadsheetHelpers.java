package bradleyross.library.helpers.tests;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.sql.ResultSet;
import bradleyross.library.helpers.SpreadsheetHelpers;
import bradleyross.local.databases.DatabaseConnection;
import bradleyross.library.database.DatabaseProperties;
/**
 * Test driver for bradleyross.library.helpers.SpreadsheetHelpers.
 * @author Bradley Ross
 * @see bradleyross.library.helpers.SpreadsheetHelpers
 *
 */
public class TestSpreadsheetHelpers 
{
	/**
	 * Main driver
	 * @param args First argument is output file destination
	 */
	public static void main(String[] args) 
	{
		File outputFile = null;
		PrintWriter out = null;
		if (args.length == 0)
		{
			outputFile = new File ("spreadsheet.xml");
		}
		else
		{
			outputFile = new File(args[0]);
		}
		
		System.out.println("Starting program");
		DatabaseProperties data = new DatabaseConnection("sample");
		System.out.println(data.showAttributes());
		SpreadsheetHelpers  helper = new SpreadsheetHelpers();
		try
		{
			data.connect();
		}
		catch (SQLException e)
		{
			System.out.println(e.getClass().getName() + e.getMessage());
		}
		try
		{
			String sqlCode = "SELECT * FROM " + data.prefixTableName("STATES");
			ResultSet rs = data.executeQuery(sqlCode);
			out = new PrintWriter(outputFile);
			out.println(helper.startWorkbook());
			out.println(helper.newWorksheet("first", rs));
			out.println(helper.endWorkbook());
			if (out.checkError())
			{
				System.out.println("Error occurred while writing file");
			}
			else
			{
				out.close();
			}
		}
		catch (FileNotFoundException e)
		{
			System.out.println(e.getClass().getName() + " " + e.getMessage());
		}
		catch (SQLException e)
		{
			System.out.println(e.getClass().getName() + " " + e.getMessage());
		}
		try
		{
			data.close();
		}
		catch (SQLException e)
		{
			System.out.println(e.getClass().getName() + " " + e.getMessage());
		}
	}
}
