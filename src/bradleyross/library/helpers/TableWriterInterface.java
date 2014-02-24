/**
 * 
 */
package bradleyross.library.helpers;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
/**
 * Methods required to generate the table representation 
 * described using TableDataInterface.
 * 
 * @author Bradley Ross
 *
 */
public interface TableWriterInterface 
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
	/**
	 * Text is composed of fixed width characters.
	 */
	public final static int MONOSPACE = 16;
	public final static int SANSSERIF = 32;
	public final static int SERIF = 48;
	public void startSheet();
	public void endSheet();
	public void newRow();
	public void setDateFormat(SimpleDateFormat format);
	public void setNumberFormat(NumberFormat format);
	public void setIntegerFormat(NumberFormat format);
}
