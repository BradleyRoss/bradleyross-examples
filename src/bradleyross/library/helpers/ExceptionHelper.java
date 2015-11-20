package bradleyross.library.helpers;
import java.io.StringWriter;
import java.io.PrintWriter;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.StringReader;
import java.util.Properties;
/*
 * SQLException and SOAPFault are in Java SE library.
 * SOAPFaultException is in Java EE library.
 */
import java.sql.SQLException;
import javax.xml.ws.soap.SOAPFaultException;
import javax.xml.soap.SOAPFault;
import bradleyross.library.helpers.ExceptionProcessor;
/*
 * Although this exception is mentioned in the Java EE 6 documentation,
 * I am unable to find it.  According to 
 * http://stackoverflow.com/questions/17008188/the-import-javax-xml-rpc-encoding-cannot-be-resolved
 * this requires the jar files xerces.jar, jaxrpc.jar, and axis.jar.
 */
// import javax.xml.rpc.soap.SOAPFaultException;
/**
 * An attempt to write a generic exception logger that will
 * provide both the location of the exception and the location
 * of the log request.
 * 
 * @author Bradley Ross
 *
 */
public class ExceptionHelper {
	/**
	 * An implementation of {@link ExceptionProcessor} can be used to customize
	 * the log messages for various types of exceptions.
	 * 
	 * <p>It should be noted that the information returned by 
	 *    {@link SOAPFaultException} is very complicated.  This is intended solely
	 *    as an example.</p>
	 * 
	 * @author Bradley Boss
	 *
	 */
	public class DummyExceptionProcessor implements ExceptionProcessor {
		public List<String> getInformation(Exception e) {
			List<String> notes = new ArrayList<String>();
			if (e == null) {
				return notes;
			}
			if (SQLException.class.isAssignableFrom(e.getClass())) {
				SQLException e2 = (SQLException) e;
				notes.add("Exception is subclass of SQLException");
				notes.add("Vendor specific error code is " + Integer.toString(e2.getErrorCode()));
				String sqlState = e2.getSQLState();
				if (sqlState != null && sqlState.trim().length() > 0) {
					notes.add("SQL State is " + sqlState);
				}
			} else if (SOAPFaultException.class.isAssignableFrom(e.getClass())) {
				SOAPFaultException e2 = (SOAPFaultException) e;
				notes.add("Exception is subclass of SOAPFaultException");
				SOAPFault fault = e2.getFault();
				if (fault == null) {
					notes.add("SOAPFault object is null");
				} else {
					notes.add("SOAPFault object is not null");
				}
			}
			return notes;
		}
	}
	public static final int FATAL = 1;
	public static final int ERROR = 2;
	public static final int WARN  = 3;
	public static final int INFO  = 4;
	public static final int DEBUG = 5;
	public static final int TRACE = 6;
	protected org.apache.log4j.Logger apacheLogger = null;
	protected org.slf4j.Logger slf4jLogger = null;
	public ExceptionHelper(org.apache.log4j.Logger logger) {
		apacheLogger = logger;
	}
	public ExceptionHelper(org.slf4j.Logger logger) {
		slf4jLogger = logger;
	}
	/**
	 * This object can be used to provide extra processing of
	 * various exception subclasses.
	 */
	protected ExceptionProcessor extra = new DummyExceptionProcessor();
	/**
	 * Setter for {@link #extra}
	 * @param value object to be used for extra processing
	 */
	public void setExceptionProcessor(ExceptionProcessor value) {
		extra = value;
	}
	protected void logException(int level, List<String> value, Exception e) {
		StringWriter writer = new StringWriter();
		PrintWriter out = new PrintWriter(writer);
		List<String> strings = new ArrayList<String>(value);
		if (e != null && SQLException.class.isAssignableFrom(e.getClass())) {
			strings.add("Subclass of SQLException");
		}
		strings.addAll(extra.getInformation(e));
		Iterator<String> iter = strings.iterator();
		while (iter.hasNext()) {
			out.println(iter.next());
		}
		out.println("     Location where log statement called");
		StackTraceElement[] stack = Thread.currentThread().getStackTrace();
		if (stack != null && stack.length > 0) {
			for (int i = 0; i < stack.length; i++) {
				out.println("        " + stack[i].toString());
			}
		}
		if (apacheLogger != null) {
			apacheLog(level, writer.toString(), e);
		} else if (slf4jLogger != null) {
			slf4jLog(level, writer.toString(), e);
		}

	}

	public void logException(int level, String string, Exception e) {
		List<String> list = new ArrayList<String>();
		StringReader reader = new StringReader(string);
		LineNumberReader in = new LineNumberReader(reader);
		try {
			while (true) {
				String line = in.readLine();
				if (line == null) { break; }
				list.add(line.trim());
			}
		} catch (IOException e2) {
			list.add("Error generating log entry");
		}

		logException(level, list, e);
	}
	protected void apacheLog(int level, String message, Exception e) {
		switch (level) {
		case FATAL :
			if (e == null) {
				apacheLogger.fatal(message);
			} else {
				apacheLogger.fatal(message, e);
			}
			break;
		case ERROR :
			if (e == null) {
				apacheLogger.error(message);
			} else {
				apacheLogger.error(message, e);
			}
			break;			
		case WARN :
			if (e == null) {
				apacheLogger.warn(message);
			} else {
				apacheLogger.warn(message, e);
			}
			break;			

		case INFO :
			if (e == null) {
				apacheLogger.info(message);
			} else {
				apacheLogger.info(message, e);
			}
			break;
		case DEBUG :
			if (e == null) {
				apacheLogger.debug(message);
			} else {
				apacheLogger.debug(message, e);
			}
			break;			
		case TRACE :
			if (e == null) {
				apacheLogger.trace(message);
			} else {
				apacheLogger.trace(message, e);
			}
			break;	
		}
	}
	protected void slf4jLog(int level, String message, Exception e) {
		switch (level) {
		case FATAL :
			if (e == null) {
				slf4jLogger.error(message);
			} else {
				slf4jLogger.error(message, e);
			}
			break;
		case ERROR :
			if (e == null) {
				slf4jLogger.error(message);
			} else {
				slf4jLogger.error(message, e);
			}
			break;			
		case WARN :
			if (e == null) {
				slf4jLogger.warn(message);
			} else {
				slf4jLogger.warn(message, e);
			}
			break;			

		case INFO :
			if (e == null) {
				slf4jLogger.info(message);
			} else {
				slf4jLogger.info(message, e);
			}
			break;
		case DEBUG :
			if (e == null) {
				slf4jLogger.debug(message);
			} else {
				slf4jLogger.debug(message, e);
			}
			break;			
		case TRACE :
			if (e == null) {
				slf4jLogger.trace(message);
			} else {
				slf4jLogger.trace(message, e);
			}
			break;	
		}
	}
	
	/**
	 * Helper method for fatal messages.
	 * @param string messages
	 * @param e exception
	 */
	public void fatal(String string, Exception e) {
		logException(FATAL, string, e);
	}
	/**
	 * Helper method for fatal messages.
	 * @param string message
	 */
	public void fatal(String string) {
		logException(FATAL, string, (Exception) null);
	}
	/**
	 * Helper method for fatal messages.
	 * @param strings messages
	 * @param e exception
	 */
	public void fatal(List<String> strings, Exception e) {
		logException(FATAL, strings, e);
	}
	/**
	 * Helper method for fatal messages.
	 * @param strings messages
	 */
	public void fatal(List<String> strings) {
		logException(FATAL, strings, (Exception) null);
	}
	/**
	 * Helper method for fatal messages.
	 * @param strings messages
	 * @param e exception
	 */
	public void fatal(String[] strings, Exception e) {
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < strings.length; i++) {
			list.add(strings[i]);
		}
		logException(FATAL, list, e);
	}	
	/**
	 * Helper method for fatal messages.
	 * @param strings messages
	 */
	public void fatal(String[] strings) {
		fatal(strings, (Exception) null);
	}
	/*
	 * Helper methods for error messages.
	 */
	/**
	 * Helper method for error messages.
	 * @param string message
	 * @param e exception
	 */
	public void error(String string, Exception e) {
		logException(ERROR, string, e);
	}
	/**
	 * Helper method for error messages.
	 * @param string message
	 */
	public void error(String string) {
		logException(ERROR, string, (Exception) null);
	}
	/**
	 * Helper method for error messages.
	 * @param strings messages
	 * @param e exception
	 */
	public void error(List<String> strings, Exception e) {
		logException(ERROR, strings, e);
	}
	/**
	 * Helper method for error messages.
	 * @param strings messages
	 */
	public void error(List<String> strings) {
		logException(ERROR, strings, (Exception) null);
	}
	/**
	 * Helper method for error messages.
	 * @param strings messages
	 * @param e exception
	 */
	public void error(String[] strings, Exception e) {
		List<String> list = new ArrayList<String>();
	    
		for (int i = 0; i < strings.length; i++) {
			list.add(strings[i]);
		}
		logException(ERROR, list, e);
	}
	/**
	 * Helper method for error messages.
	 * @param strings messages
	 */
	public void error (String[] strings) {
		error(strings, (Exception) null);
	}
	/**
	 * Helper method for warn messages.
	 * @param string messages
	 * @param e exception
	 */
	public void warn(String string, Exception e) {
		logException(WARN, string, e);
	}
	/**
	 * Helper method for warn messages.
	 * @param string message
	 */
	public void warn(String string) {
		logException(WARN, string, (Exception) null);
	}
	/**
	 * Helper method for warn messages.
	 * @param strings messages
	 * @param e exception
	 */
	public void warn(List<String> strings, Exception e) {
		logException(WARN, strings, e);
	}
	/**
	 * Helper method for warn messages.
	 * @param strings messages
	 */
	public void warn(List<String> strings) {
		logException(WARN, strings, (Exception) null);
	}
	/**
	 * Helper method for warn messages.
	 * @param strings messages
	 * @param e exception
	 */
	public void warn(String[] strings, Exception e) {
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < strings.length; i++) {
			list.add(strings[i]);
		}
		logException(WARN, list, e);
	}	
	/**
	 * Helper method for warn messages.
	 * @param strings messages
	 */
	public void warn(String[] strings) {
		warn(strings, (Exception) null);
	}
	/**
	 * Helper method for info messages.
	 * @param string message
	 * @param e exception
	 */
	public void info(String string, Exception e) {
		logException(INFO, string, e);
	}
	/**
	 * Helper method for info messages.
	 * @param string message
	 */
	public void info(String string) {
		logException(INFO, string, (Exception) null);
	}
	/**
	 * Helper method for info messages.
	 * @param strings messages
	 * @param e exception
	 */
	public void info(List<String> strings, Exception e) {
		logException(INFO, strings, e);
	}
	/**
	 * Helper method for info messages.
	 * @param strings messages
	 */
	public void info(List<String> strings) {
		logException(INFO, strings, (Exception) null);
	}
	/**
	 * Helper method for info messages.
	 * @param strings messages
	 * @param e exception
	 */
	public void info(String[] strings, Exception e) {
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < strings.length; i++) {
			list.add(strings[i]);
		}
		logException(INFO, list, e);
	}
	/**
	 * Helper method for info messages.
	 * @param strings messages
	 */
	public void info (String[] strings) {
		info(strings, (Exception) null);
	}
	/**
	 * Helper method for debug messages.
	 * @param string messages
	 * @param e exception
	 */
	public void debug(String string, Exception e) {
		logException(DEBUG, string, e);
	}
	/**
	 * Helper method for debug messages.
	 * @param string message
	 */
	public void debug(String string) {
		logException(DEBUG, string, (Exception) null);
	}
	/**
	 * Helper method for debug messages.
	 * @param strings messages
	 * @param e exception
	 */
	public void debug(List<String> strings, Exception e) {
		logException(DEBUG, strings, e);
	}
	/**
	 * Helper method for debug messages.
	 * @param strings messages
	 */
	public void debug(List<String> strings) {
		logException(DEBUG, strings, (Exception) null);
	}
	/**
	 * Helper method for debug messages.
	 * @param strings messages
	 * @param e exception
	 */
	public void debug(String[] strings, Exception e) {
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < strings.length; i++) {
			list.add(strings[i]);
		}
		logException(DEBUG, list, e);
	}	
	/**
	 * Helper method for debug messages.
	 * @param strings messages
	 */
	public void debug(String[] strings) {
		debug(strings, (Exception) null);
	}
	/**
	 * Helper method for trace messages.
	 * @param string message
	 * @param e exception
	 */
	public void trace(String string, Exception e) {
		logException(TRACE, string, e);
	}
	/**
	 * Helper method for trace messages.
	 * @param string message
	 */
	public void trace(String string) {
		logException(TRACE, string, (Exception) null);
	}
	/**
	 * Helper method for trace messages.
	 * @param strings messages
	 * @param e exception
	 */
	public void trace(List<String> strings, Exception e) {
		logException(TRACE, strings, e);
	}
	/**
	 * Helper method for trace messages.
	 * @param strings messages
	 */
	public void trace(List<String> strings) {
		logException(TRACE, strings, (Exception) null);
	}
	/**
	 * Helper method for trace messages.
	 * @param strings messages
	 * @param e exception
	 */
	public void trace(String[] strings, Exception e) {
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < strings.length; i++) {
			list.add(strings[i]);
		}
		logException(TRACE, list, e);
	}
	/**
	 * Helper method for trace messages.
	 * @param strings messages
	 */
	public void trace (String[] strings) {
		trace(strings, (Exception) null);
	}
	/**
	 * Demonstrates many of the features of the
	 * {@link ExceptionHelper} class.
	 * 
	 * @author Bradley Ross
	 *
	 */
	protected class Tester implements Runnable {
		protected ExceptionHelper helper = null;
		public Tester(ExceptionHelper value) {
			helper = value;
		}
		public void run() {
			try {
				level1();
			} catch (Exception e) {
				helper.info("Trapping in method run", e);
			}
		}
		protected void level1() throws IOException {
			try {
				level2();
			} catch (Exception e) {
				helper.info("Trapping in method level1", e);
				throw e;
			}
		}
		protected void level2() throws IOException {
			try {
				level3();
			} catch (Exception e) {
				helper.info("Trap and throw in level 2 - level info", e);
				helper.warn("Trap and throw in level 2 - level warn", e);
				throw e;
			}
		}
		protected void level3() throws IOException {
			IOException  e = new IOException("Triggering IOException");
			throw e;
		}
	}
	/**
	 * Test driver.
	 * 
	 * <p>Log4j or Slf4j must be set up separately so that this code will work.</p>
	 * 
	 * @param params - not used
	 */
	public static void main(String[] params) {
		/*
		 * If the catalina.home Java system variable is not set,  
		 * catalina.home will be set
		 * to the home directory.
		 */
		Properties props = System.getProperties();
		if (!props.containsKey("catalina.home")) {
			System.setProperty("catalina.home", System.getProperty("user.home"));
		}
		org.slf4j.Logger logger2 = org.slf4j.LoggerFactory.getLogger(ExceptionHelper.class);
		org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger("test3");
		ExceptionHelper helper = new ExceptionHelper(logger);
		Runnable run = helper.new Tester(helper);
		logger.info("Starting test with log4j");
		run.run();
		ExceptionHelper helper2 = new ExceptionHelper(logger2);
		helper2.info("Starting test with org.slf4j");
		Runnable run2 = helper2.new Tester(helper2);
		run2.run();
	}
}
