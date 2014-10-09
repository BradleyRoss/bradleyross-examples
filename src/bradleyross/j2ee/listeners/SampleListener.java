/**
 * 
 */
package bradleyross.j2ee.listeners;
import org.apache.log4j.Logger;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletContext;
import javax.servlet.annotation.WebListener;

/**
 * Demonstration listener.  
 * 
 * <p>When the listener is linked to a context, it places a message on the log
 *    when the context is started and when it is stopped.</p>
 * 
 * @author Bradley Ross
 * 
 * 
 * @see ServletContextEvent
 * @see ServletContext
 * @see WebListener
 *
 */
@WebListener
public class SampleListener implements ServletContextListener {
	protected Logger logger = null;
	/* (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
	 */
	/**
	 * Method to be executed when context is destroyed.
	 * 
	 * <p>In this sample, it places a message in the log4j log file.</p>
	 * @param arg0 event object describing destruction of context
	 */
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		logger = Logger.getLogger(this.getClass());	
		ServletContext context = arg0.getServletContext();
		String path = context.getContextPath();
		logger.info("Destroying context at " + path);
	}
	/* (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
	 */
	/**
	 * Method to be executed when context is initialized.
	 * 
	 * <p>This method places a message in the log4j log file.</p>
	 * 
	 * @param arg0 context event describing initialization of context
	 */
	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		logger = Logger.getLogger(this.getClass());	
		ServletContext context = arg0.getServletContext();
		String path = context.getContextPath();
		logger.info("Starting context at " + path);
	}
}
