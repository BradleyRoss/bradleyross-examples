package bradleyross.j2ee.servlets;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.sql.ResultSet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import bradleyross.library.helpers.SpreadsheetHelpers;
import bradleyross.local.databases.DatabaseConnection;
import bradleyross.library.database.DatabaseProperties;
/**
 * Test servlet for bradleyross.library.helpers.SpreadsheetHelpers.
 * 
 * <p>It makes use of a database named <i>sample</i> that is located
 *    on my laptop computer.</p>
 * @author Bradley Ross
 * @see bradleyross.library.helpers.SpreadsheetHelpers
 *
 */
public class TestSpreadsheetHelpers extends HttpServlet
{
	/**
	 * Inserted in order to satisfy serializable interface
	 */
	private static final long serialVersionUID = 1L;
	public void init(ServletConfig config) throws ServletException
	{
		super.init(config);
	}
	public void destroy()
	{
		super.destroy();
	}
	/**
	 * Creates the web page.
	 * <p>The methods setHeader and setContentType for the HttpServletResponse
	 *    object are used to set the properties of the file being downloaded.</p>
	 * @see HttpServletRequest
	 * @see HttpServletResponse
	 */
	public void service (HttpServletRequest req, HttpServletResponse res) throws IOException
	{
		String mimeCode = "application/vnd.ms-excel";
		res.setHeader("Content-Disposition", "attachment;filename=" +
				"spreadsheet");
		PrintWriter out = res.getWriter();
		res.setContentType(mimeCode);
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
			String sqlCode = "SELECT * FROM " + data.prefixTableName("STATES") +
			" ORDER BY NAME ";
			ResultSet rs = data.executeQuery(sqlCode);
			out.println(helper.startWorkbook());
			out.println(helper.newWorksheet("first", rs));
			out.println(helper.endWorkbook());
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
			out.println(e.getClass().getName() + " " + e.getMessage());
		}
	}
}
