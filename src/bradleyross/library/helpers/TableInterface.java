package bradleyross.library.helpers;
import java.sql.ResultSet;
import java.sql.SQLException;
import bradleyross.library.helpers.GenericPrinter;
/**
 * 
 * @author Bradley Ross
 *
 */
interface TableInterface 
{
	/**
	 * Indicates normal formatting.
	 */
	public final static int NORMAL = 0;
	/**
	 * Text rotated 90 degrees counter-clockwise.
	 */
	public final static int CCW = 1;
	/**
	 * Text rotated 90 degrees clockwise.
	 */
	public final static int CW = 2;
	public void setOutput(GenericPrinter output);
	public void setDebugLevel(int value);
	public int getDebugLevel();
	public void setColumnCount(int value);
	public int getColumnCount();
	public void startTable();
	public void finishTable();
	public void formatHeaderRow(Object... values );
	public void formatHeaderRow(ResultSet rs) throws SQLException;
	public void formatDataRow(Object... values);
	public void formatDataRow(ResultSet rs) throws SQLException;
	public void formatCell(Object value, int format);
}
