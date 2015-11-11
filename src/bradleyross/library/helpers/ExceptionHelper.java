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
/**
 * An attempt to write a generic exception logger that will
 * provide both the location of the exception and the location
 * of the log request.
 * @author Bradley Ross
 *
 */
public class ExceptionHelper {
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
	public void logException(int level, List<String> strings, Exception e) {
		StringWriter writer = new StringWriter();
		PrintWriter out = new PrintWriter(writer);
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
	public void logException(int level, String string) {
		logException(level, string, (Exception) null);
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
	protected class Tester implements Runnable {
		public void run() {
			try {
				level1();
			} catch (Exception e) {
				logException(INFO, "Trapping in method run", e);
			}
		}
		protected void level1() throws IOException {
			try {
				level2();
			} catch (Exception e) {
				logException(INFO, "Trapping in method level1", e);
				throw e;
			}
		}
		protected void level2() throws IOException {
			try {
				level3();
			} catch (Exception e) {
				logException(INFO, "Trapping in level2", e);
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
		Runnable run = helper.new Tester();
		logger.error("Starting test with log4j");
		run.run();
		ExceptionHelper helper2 = new ExceptionHelper(logger2);
		logger2.error("Starting test with org.slf4j");
		Runnable run2 = helper2.new Tester();
		run2.run();
		
	}
}
