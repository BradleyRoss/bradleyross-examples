package bradleyross.library;
/**
 * Indicates that a method is not supported for the
 * existing environment.
 *
 * @author Bradley Ross
 *
 */
public class NotSupportedException extends Exception 
{
	/**
	 * Included to satisfy Serializable interface
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Creates an exception where the message and cause are
	 * set to null.
	 */
	public NotSupportedException() 
	{ ; }
	/**
	 *  Creates an exception where the message is specified but the
	 *  cause is set to null.
	 *
	 *  @param message Message describing exception
	 */
	public NotSupportedException(String message) 
	{
		super(message);
	}
	/**
	 *  Creates an exception where the cause is specified but the
	 *  message is set to null.
	 *
	 *  @param cause Cause of exception
	 */
	public NotSupportedException(Throwable cause) 
	{
		super(cause);
	}
	/**
	 * Creates an exception where both the message and cause
	 * are specified.
	 *
	 * @param message Message describing exception
	 * @param cause Cause of exception
	 */
	public NotSupportedException(String message, Throwable cause) 
	{
		super(message, cause);
	}
}
