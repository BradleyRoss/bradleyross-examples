package bradleyross.library.helpers;
import java.util.List;
/**
 * Interface for class providing extra information on
 * exceptions.
 * 
 * @author Bradley Ross
 *
 */
public interface ExceptionProcessor {
	/**
	 * Generates a {@link List} of String objects based on
	 * the information in the exception.
	 * 
	 * @param e  exception
	 * @return message for incorporation in log
	 */
	public List<String> getInformation(Exception e);
}
