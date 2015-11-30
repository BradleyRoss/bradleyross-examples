package bradleyross.library.helpers;
import java.io.StringWriter;
import java.io.PrintWriter;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.StringReader;
// import java.io.PrintWriter;
import java.io.PrintStream;
import java.util.Properties;
/*
 * SQLException and SOAPFault are in Java SE library.
 * SOAPFaultException is in Java EE library.
 */
import java.sql.SQLException;
import javax.xml.ws.soap.SOAPFaultException;
import javax.xml.soap.SOAPFault;
import bradleyross.library.helpers.ExceptionProcessor;
// import java.util.regex.Matcher;
//  import java.util.regex.Pattern;
// import java.util.regex.PatternSyntaxException;
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
 * <p>The following is a sample log using this class.</p>
 * <p><pre>
 * 2015-11-29 19:21:03,278 INFO  [SQLExample] -       Database is now open
 *    Location where log statement called
 *            java.lang.Thread.getStackTrace(Thread.java:1589)
 *            bradleyross.library.helpers.ExceptionHelper.logException(ExceptionHelper.java:123)
 *            bradleyross.library.helpers.ExceptionHelper.logException(ExceptionHelper.java:168)
 *            bradleyross.library.helpers.ExceptionHelper.info(ExceptionHelper.java:379)
 *            bradleyross.library.debugging.SQLExample.run(SQLExample.java:92)
 *            bradleyross.library.debugging.SQLExample.main(SQLExample.java:177)
 *
 * 2015-11-29 19:21:03,279 INFO  [SQLExample] -       First statement is SELECT STATE, NAME FROM STATE
 *    Location where log statement called
 *            java.lang.Thread.getStackTrace(Thread.java:1589)
 *            bradleyross.library.helpers.ExceptionHelper.logException(ExceptionHelper.java:123)
 *            bradleyross.library.helpers.ExceptionHelper.logException(ExceptionHelper.java:168)
 *            bradleyross.library.helpers.ExceptionHelper.info(ExceptionHelper.java:379)
 *            bradleyross.library.debugging.SQLExample.run(SQLExample.java:93)
 *            bradleyross.library.debugging.SQLExample.main(SQLExample.java:177)
 *
 * 2015-11-29 19:21:03,280 INFO  [SQLExample] -       Statement created
 *    Location where log statement called
 *            java.lang.Thread.getStackTrace(Thread.java:1589)
 *            bradleyross.library.helpers.ExceptionHelper.logException(ExceptionHelper.java:123)
 *            bradleyross.library.helpers.ExceptionHelper.logException(ExceptionHelper.java:168)
 *            bradleyross.library.helpers.ExceptionHelper.info(ExceptionHelper.java:379)
 *            bradleyross.library.debugging.SQLExample.run(SQLExample.java:96)
 *            bradleyross.library.debugging.SQLExample.main(SQLExample.java:177)
 *
 * 2015-11-29 19:21:03,288 INFO  [SQLExample] -       ResultSet created
 *    Location where log statement called
 *            java.lang.Thread.getStackTrace(Thread.java:1589)
 *            bradleyross.library.helpers.ExceptionHelper.logException(ExceptionHelper.java:123)
 *            bradleyross.library.helpers.ExceptionHelper.logException(ExceptionHelper.java:168)
 *            bradleyross.library.helpers.ExceptionHelper.info(ExceptionHelper.java:379)
 *            bradleyross.library.debugging.SQLExample.run(SQLExample.java:98)
 *            bradleyross.library.debugging.SQLExample.main(SQLExample.java:177)
 * 
 * 2015-11-29 19:21:03,291 INFO  [SQLExample] -       Second statement is SELECT STATE, NAME FROM NOTTHERE
 *    Location where log statement called
 *            java.lang.Thread.getStackTrace(Thread.java:1589)
 *            bradleyross.library.helpers.ExceptionHelper.logException(ExceptionHelper.java:123)
 *            bradleyross.library.helpers.ExceptionHelper.logException(ExceptionHelper.java:168)
 *            bradleyross.library.helpers.ExceptionHelper.info(ExceptionHelper.java:379)
 *            bradleyross.library.debugging.SQLExample.run(SQLExample.java:120)
 *            bradleyross.library.debugging.SQLExample.main(SQLExample.java:177)
 * 
 * 2015-11-29 19:21:03,292 INFO  [SQLExample] -       Statement created
 *    Location where log statement called
 *            java.lang.Thread.getStackTrace(Thread.java:1589)
 *            bradleyross.library.helpers.ExceptionHelper.logException(ExceptionHelper.java:123)
 *            bradleyross.library.helpers.ExceptionHelper.logException(ExceptionHelper.java:168)
 *            bradleyross.library.helpers.ExceptionHelper.info(ExceptionHelper.java:379)
 *            bradleyross.library.debugging.SQLExample.run(SQLExample.java:123)
 *            bradleyross.library.debugging.SQLExample.main(SQLExample.java:177)
 * 
 * 2015-11-29 19:21:03,302 ERROR [SQLExample] -       Problem with second statement
 *    Subclass of SQLException
 *    Location of exception
 *       com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException: Table 'sample.notthere' doesn't exist
 *       	at sun.reflect.NativeConstructorAccessorImpl.newInstance0(Native Method)
 *       	at sun.reflect.NativeConstructorAccessorImpl.newInstance(NativeConstructorAccessorImpl.java:57)
 *       	at sun.reflect.DelegatingConstructorAccessorImpl.newInstance(DelegatingConstructorAccessorImpl.java:45)
 *       	at java.lang.reflect.Constructor.newInstance(Constructor.java:526)
 *       	at com.mysql.jdbc.Util.handleNewInstance(Util.java:408)
 *       	at com.mysql.jdbc.Util.getInstance(Util.java:383)
 *       	at com.mysql.jdbc.SQLError.createSQLException(SQLError.java:1062)
 *       	at com.mysql.jdbc.MysqlIO.checkErrorPacket(MysqlIO.java:4208)
 *       	at com.mysql.jdbc.MysqlIO.checkErrorPacket(MysqlIO.java:4140)
 *       	at com.mysql.jdbc.MysqlIO.sendCommand(MysqlIO.java:2597)
 *       	at com.mysql.jdbc.MysqlIO.sqlQueryDirect(MysqlIO.java:2758)
 *       	at com.mysql.jdbc.ConnectionImpl.execSQL(ConnectionImpl.java:2820)
 *       	at com.mysql.jdbc.ConnectionImpl.execSQL(ConnectionImpl.java:2769)
 *       	at com.mysql.jdbc.StatementImpl.executeQuery(StatementImpl.java:1569)
 *       	at bradleyross.library.debugging.SQLExample.run(SQLExample.java:124)
 *       	at bradleyross.library.debugging.SQLExample.main(SQLExample.java:177)
 *    Exception is subclass of SQLException
 *    Vendor specific error code is 1146
 *    SQL State is 42S02
 *    Location where log statement called
 *            java.lang.Thread.getStackTrace(Thread.java:1589)
 *            bradleyross.library.helpers.ExceptionHelper.logException(ExceptionHelper.java:123)
 *            bradleyross.library.helpers.ExceptionHelper.logException(ExceptionHelper.java:168)
 *            bradleyross.library.helpers.ExceptionHelper.error(ExceptionHelper.java:273)
 *            bradleyross.library.debugging.SQLExample.run(SQLExample.java:131)
 *            bradleyross.library.debugging.SQLExample.main(SQLExample.java:177)
 * 
 * 2015-11-29 19:21:03,303 INFO  [SQLExample] -       Third statement is SELECT STATE, NAME, NOTTHERE FROM STATE
 *    Location where log statement called
 *            java.lang.Thread.getStackTrace(Thread.java:1589)
 *            bradleyross.library.helpers.ExceptionHelper.logException(ExceptionHelper.java:123)
 *            bradleyross.library.helpers.ExceptionHelper.logException(ExceptionHelper.java:168)
 *            bradleyross.library.helpers.ExceptionHelper.info(ExceptionHelper.java:379)
 *            bradleyross.library.debugging.SQLExample.run(SQLExample.java:144)
 *            bradleyross.library.debugging.SQLExample.main(SQLExample.java:177)
 * 
 * 2015-11-29 19:21:03,303 INFO  [SQLExample] -       Statement created
 *    Location where log statement called
 *            java.lang.Thread.getStackTrace(Thread.java:1589)
 *            bradleyross.library.helpers.ExceptionHelper.logException(ExceptionHelper.java:123)
 *            bradleyross.library.helpers.ExceptionHelper.logException(ExceptionHelper.java:168)
 *            bradleyross.library.helpers.ExceptionHelper.info(ExceptionHelper.java:379)
 *            bradleyross.library.debugging.SQLExample.run(SQLExample.java:147)
 *            bradleyross.library.debugging.SQLExample.main(SQLExample.java:177)
 * 
 * 2015-11-29 19:21:03,304 ERROR [SQLExample] -       Problem with third statement
 *    Subclass of SQLException
 *    Location of exception
 *       com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException: Unknown column 'NOTTHERE' in 'field list'
 *       	at sun.reflect.NativeConstructorAccessorImpl.newInstance0(Native Method)
 *       	at sun.reflect.NativeConstructorAccessorImpl.newInstance(NativeConstructorAccessorImpl.java:57)
 *       	at sun.reflect.DelegatingConstructorAccessorImpl.newInstance(DelegatingConstructorAccessorImpl.java:45)
 *       	at java.lang.reflect.Constructor.newInstance(Constructor.java:526)
 *       	at com.mysql.jdbc.Util.handleNewInstance(Util.java:408)
 *       	at com.mysql.jdbc.Util.getInstance(Util.java:383)
 *       	at com.mysql.jdbc.SQLError.createSQLException(SQLError.java:1062)
 *       	at com.mysql.jdbc.MysqlIO.checkErrorPacket(MysqlIO.java:4208)
 *       	at com.mysql.jdbc.MysqlIO.checkErrorPacket(MysqlIO.java:4140)
 *       	at com.mysql.jdbc.MysqlIO.sendCommand(MysqlIO.java:2597)
 *       	at com.mysql.jdbc.MysqlIO.sqlQueryDirect(MysqlIO.java:2758)
 *       	at com.mysql.jdbc.ConnectionImpl.execSQL(ConnectionImpl.java:2820)
 *       	at com.mysql.jdbc.ConnectionImpl.execSQL(ConnectionImpl.java:2769)
 *       	at com.mysql.jdbc.StatementImpl.executeQuery(StatementImpl.java:1569)
 *       	at bradleyross.library.debugging.SQLExample.run(SQLExample.java:148)
 *       	at bradleyross.library.debugging.SQLExample.main(SQLExample.java:177)
 *    Exception is subclass of SQLException
 *    Vendor specific error code is 1054
 *    SQL State is 42S22
 *    Location where log statement called
 *            java.lang.Thread.getStackTrace(Thread.java:1589)
 *            bradleyross.library.helpers.ExceptionHelper.logException(ExceptionHelper.java:123)
 *            bradleyross.library.helpers.ExceptionHelper.logException(ExceptionHelper.java:168)
 *            bradleyross.library.helpers.ExceptionHelper.error(ExceptionHelper.java:273)
 *            bradleyross.library.debugging.SQLExample.run(SQLExample.java:155)
 *            bradleyross.library.debugging.SQLExample.main(SQLExample.java:177)
 * 
 * 2015-11-29 19:22:29,874 INFO  [test3] - Starting test with log4j
 * 2015-11-29 19:22:29,876 INFO  [test3] -       Trap and throw in level 2 - level info
 *    Location of exception
 *       java.io.IOException: Triggering IOException
 *       	at bradleyross.library.helpers.ExceptionHelper$Tester.level3(ExceptionHelper.java:598)
 *       	at bradleyross.library.helpers.ExceptionHelper$Tester.level2(ExceptionHelper.java:590)
 *       	at bradleyross.library.helpers.ExceptionHelper$Tester.level1(ExceptionHelper.java:582)
 *       	at bradleyross.library.helpers.ExceptionHelper$Tester.run(ExceptionHelper.java:575)
 *       	at bradleyross.library.helpers.ExceptionHelper.main(ExceptionHelper.java:641)
 *    Location where log statement called
 *            java.lang.Thread.getStackTrace(Thread.java:1589)
 *            bradleyross.library.helpers.ExceptionHelper.logException(ExceptionHelper.java:123)
 *            bradleyross.library.helpers.ExceptionHelper.logException(ExceptionHelper.java:168)
 *            bradleyross.library.helpers.ExceptionHelper.info(ExceptionHelper.java:372)
 *            bradleyross.library.helpers.ExceptionHelper$Tester.level2(ExceptionHelper.java:592)
 *            bradleyross.library.helpers.ExceptionHelper$Tester.level1(ExceptionHelper.java:582)
 *            bradleyross.library.helpers.ExceptionHelper$Tester.run(ExceptionHelper.java:575)
 *            bradleyross.library.helpers.ExceptionHelper.main(ExceptionHelper.java:641)
 * 
 * 2015-11-29 19:22:29,876 WARN  [test3] -       Trap and throw in level 2 - level warn
 *    Location of exception
 *       java.io.IOException: Triggering IOException
 *       	at bradleyross.library.helpers.ExceptionHelper$Tester.level3(ExceptionHelper.java:598)
 *       	at bradleyross.library.helpers.ExceptionHelper$Tester.level2(ExceptionHelper.java:590)
 *       	at bradleyross.library.helpers.ExceptionHelper$Tester.level1(ExceptionHelper.java:582)
 *       	at bradleyross.library.helpers.ExceptionHelper$Tester.run(ExceptionHelper.java:575)
 *       	at bradleyross.library.helpers.ExceptionHelper.main(ExceptionHelper.java:641)
 *    Location where log statement called
 *            java.lang.Thread.getStackTrace(Thread.java:1589)
 *            bradleyross.library.helpers.ExceptionHelper.logException(ExceptionHelper.java:123)
 *            bradleyross.library.helpers.ExceptionHelper.logException(ExceptionHelper.java:168)
 *            bradleyross.library.helpers.ExceptionHelper.warn(ExceptionHelper.java:323)
 *            bradleyross.library.helpers.ExceptionHelper$Tester.level2(ExceptionHelper.java:593)
 *            bradleyross.library.helpers.ExceptionHelper$Tester.level1(ExceptionHelper.java:582)
 *            bradleyross.library.helpers.ExceptionHelper$Tester.run(ExceptionHelper.java:575)
 *            bradleyross.library.helpers.ExceptionHelper.main(ExceptionHelper.java:641)
 * 
 * 2015-11-29 19:22:29,877 INFO  [test3] -       Trapping in method level1
 *    Location of exception
 *       java.io.IOException: Triggering IOException
 *       	at bradleyross.library.helpers.ExceptionHelper$Tester.level3(ExceptionHelper.java:598)
 *       	at bradleyross.library.helpers.ExceptionHelper$Tester.level2(ExceptionHelper.java:590)
 *       	at bradleyross.library.helpers.ExceptionHelper$Tester.level1(ExceptionHelper.java:582)
 *       	at bradleyross.library.helpers.ExceptionHelper$Tester.run(ExceptionHelper.java:575)
 *       	at bradleyross.library.helpers.ExceptionHelper.main(ExceptionHelper.java:641)
 *    Location where log statement called
 *            java.lang.Thread.getStackTrace(Thread.java:1589)
 *            bradleyross.library.helpers.ExceptionHelper.logException(ExceptionHelper.java:123)
 *            bradleyross.library.helpers.ExceptionHelper.logException(ExceptionHelper.java:168)
 *            bradleyross.library.helpers.ExceptionHelper.info(ExceptionHelper.java:372)
 *            bradleyross.library.helpers.ExceptionHelper$Tester.level1(ExceptionHelper.java:584)
 *            bradleyross.library.helpers.ExceptionHelper$Tester.run(ExceptionHelper.java:575)
 *            bradleyross.library.helpers.ExceptionHelper.main(ExceptionHelper.java:641)
 * 
 * 2015-11-29 19:22:29,878 INFO  [test3] -       Trapping in method run
 *    Location of exception
 *       java.io.IOException: Triggering IOException
 *       	at bradleyross.library.helpers.ExceptionHelper$Tester.level3(ExceptionHelper.java:598)
 *       	at bradleyross.library.helpers.ExceptionHelper$Tester.level2(ExceptionHelper.java:590)
 *       	at bradleyross.library.helpers.ExceptionHelper$Tester.level1(ExceptionHelper.java:582)
 *       	at bradleyross.library.helpers.ExceptionHelper$Tester.run(ExceptionHelper.java:575)
 *       	at bradleyross.library.helpers.ExceptionHelper.main(ExceptionHelper.java:641)
 *    Location where log statement called
 *            java.lang.Thread.getStackTrace(Thread.java:1589)
 *            bradleyross.library.helpers.ExceptionHelper.logException(ExceptionHelper.java:123)
 *            bradleyross.library.helpers.ExceptionHelper.logException(ExceptionHelper.java:168)
 *            bradleyross.library.helpers.ExceptionHelper.info(ExceptionHelper.java:372)
 *            bradleyross.library.helpers.ExceptionHelper$Tester.run(ExceptionHelper.java:577)
 *            bradleyross.library.helpers.ExceptionHelper.main(ExceptionHelper.java:641)
 * 
 * 2015-11-29 19:22:29,878 INFO  [ExceptionHelper] -       Starting test with org.slf4j
 *    Location where log statement called
 *            java.lang.Thread.getStackTrace(Thread.java:1589)
 *            bradleyross.library.helpers.ExceptionHelper.logException(ExceptionHelper.java:123)
 *            bradleyross.library.helpers.ExceptionHelper.logException(ExceptionHelper.java:168)
 *            bradleyross.library.helpers.ExceptionHelper.info(ExceptionHelper.java:379)
 *            bradleyross.library.helpers.ExceptionHelper.main(ExceptionHelper.java:643)
 * 
 * 2015-11-29 19:22:29,879 INFO  [ExceptionHelper] -       Trap and throw in level 2 - level info
 *    Location of exception
 *       java.io.IOException: Triggering IOException
 *       	at bradleyross.library.helpers.ExceptionHelper$Tester.level3(ExceptionHelper.java:598)
 *       	at bradleyross.library.helpers.ExceptionHelper$Tester.level2(ExceptionHelper.java:590)
 *       	at bradleyross.library.helpers.ExceptionHelper$Tester.level1(ExceptionHelper.java:582)
 *       	at bradleyross.library.helpers.ExceptionHelper$Tester.run(ExceptionHelper.java:575)
 *       	at bradleyross.library.helpers.ExceptionHelper.main(ExceptionHelper.java:645)
 *    Location where log statement called
 *            java.lang.Thread.getStackTrace(Thread.java:1589)
 *            bradleyross.library.helpers.ExceptionHelper.logException(ExceptionHelper.java:123)
 *            bradleyross.library.helpers.ExceptionHelper.logException(ExceptionHelper.java:168)
 *            bradleyross.library.helpers.ExceptionHelper.info(ExceptionHelper.java:372)
 *            bradleyross.library.helpers.ExceptionHelper$Tester.level2(ExceptionHelper.java:592)
 *            bradleyross.library.helpers.ExceptionHelper$Tester.level1(ExceptionHelper.java:582)
 *            bradleyross.library.helpers.ExceptionHelper$Tester.run(ExceptionHelper.java:575)
 *            bradleyross.library.helpers.ExceptionHelper.main(ExceptionHelper.java:645)
 * 
 * 2015-11-29 19:22:29,879 WARN  [ExceptionHelper] -       Trap and throw in level 2 - level warn
 *    Location of exception
 *       java.io.IOException: Triggering IOException
 *       	at bradleyross.library.helpers.ExceptionHelper$Tester.level3(ExceptionHelper.java:598)
 *       	at bradleyross.library.helpers.ExceptionHelper$Tester.level2(ExceptionHelper.java:590)
 *       	at bradleyross.library.helpers.ExceptionHelper$Tester.level1(ExceptionHelper.java:582)
 *       	at bradleyross.library.helpers.ExceptionHelper$Tester.run(ExceptionHelper.java:575)
 *       	at bradleyross.library.helpers.ExceptionHelper.main(ExceptionHelper.java:645)
 *    Location where log statement called
 *            java.lang.Thread.getStackTrace(Thread.java:1589)
 *            bradleyross.library.helpers.ExceptionHelper.logException(ExceptionHelper.java:123)
 *            bradleyross.library.helpers.ExceptionHelper.logException(ExceptionHelper.java:168)
 *            bradleyross.library.helpers.ExceptionHelper.warn(ExceptionHelper.java:323)
 *            bradleyross.library.helpers.ExceptionHelper$Tester.level2(ExceptionHelper.java:593)
 *            bradleyross.library.helpers.ExceptionHelper$Tester.level1(ExceptionHelper.java:582)
 *            bradleyross.library.helpers.ExceptionHelper$Tester.run(ExceptionHelper.java:575)
 *            bradleyross.library.helpers.ExceptionHelper.main(ExceptionHelper.java:645)
 * 
 * 2015-11-29 19:22:29,880 INFO  [ExceptionHelper] -       Trapping in method level1
 *    Location of exception
 *       java.io.IOException: Triggering IOException
 *       	at bradleyross.library.helpers.ExceptionHelper$Tester.level3(ExceptionHelper.java:598)
 *       	at bradleyross.library.helpers.ExceptionHelper$Tester.level2(ExceptionHelper.java:590)
 *       	at bradleyross.library.helpers.ExceptionHelper$Tester.level1(ExceptionHelper.java:582)
 *       	at bradleyross.library.helpers.ExceptionHelper$Tester.run(ExceptionHelper.java:575)
 *       	at bradleyross.library.helpers.ExceptionHelper.main(ExceptionHelper.java:645)
 *    Location where log statement called
 *            java.lang.Thread.getStackTrace(Thread.java:1589)
 *            bradleyross.library.helpers.ExceptionHelper.logException(ExceptionHelper.java:123)
 *            bradleyross.library.helpers.ExceptionHelper.logException(ExceptionHelper.java:168)
 *            bradleyross.library.helpers.ExceptionHelper.info(ExceptionHelper.java:372)
 *            bradleyross.library.helpers.ExceptionHelper$Tester.level1(ExceptionHelper.java:584)
 *            bradleyross.library.helpers.ExceptionHelper$Tester.run(ExceptionHelper.java:575)
 *            bradleyross.library.helpers.ExceptionHelper.main(ExceptionHelper.java:645)
 * 
 * 2015-11-29 19:22:29,881 INFO  [ExceptionHelper] -       Trapping in method run
 *    Location of exception
 *       java.io.IOException: Triggering IOException
 *       	at bradleyross.library.helpers.ExceptionHelper$Tester.level3(ExceptionHelper.java:598)
 *       	at bradleyross.library.helpers.ExceptionHelper$Tester.level2(ExceptionHelper.java:590)
 *       	at bradleyross.library.helpers.ExceptionHelper$Tester.level1(ExceptionHelper.java:582)
 *       	at bradleyross.library.helpers.ExceptionHelper$Tester.run(ExceptionHelper.java:575)
 *       	at bradleyross.library.helpers.ExceptionHelper.main(ExceptionHelper.java:645)
 *    Location where log statement called
 *            java.lang.Thread.getStackTrace(Thread.java:1589)
 *            bradleyross.library.helpers.ExceptionHelper.logException(ExceptionHelper.java:123)
 *            bradleyross.library.helpers.ExceptionHelper.logException(ExceptionHelper.java:168)
 *            bradleyross.library.helpers.ExceptionHelper.info(ExceptionHelper.java:372)
 *            bradleyross.library.helpers.ExceptionHelper$Tester.run(ExceptionHelper.java:577)
 *            bradleyross.library.helpers.ExceptionHelper.main(ExceptionHelper.java:645)
 * </pre></p>
 * @author Bradley Ross
 * @see bradleyross.library.helpers.ExceptionProcessor
 * @see bradleyross.library.debugging.SQLExample
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
		try {
			List<String> strings = new ArrayList<String>(splitLines(value));
			if (e != null && SQLException.class.isAssignableFrom(e.getClass())) {
				strings.add("Subclass of SQLException");
			}
			if (e != null) {
				strings.add("Location of exception");
				StringWriter writer2 = new StringWriter();
				PrintWriter out2 = new PrintWriter(writer2);
				e.printStackTrace(out2);
				strings.addAll(splitLines(writer2.toString()));
			}
			strings.addAll(extra.getInformation(e));
			strings.add("Location where log statement called");
			StackTraceElement[] stack = Thread.currentThread().getStackTrace();
			if (stack != null && stack.length > 0) {
				for (int i = 0; i < stack.length; i++) {
					strings.addAll(splitLines("     " + stack[i].toString()));
				}
			}
			Iterator<String> iter = strings.iterator();
			while (iter.hasNext()) {
				out.println("   " + iter.next());
			}
		} catch (IOException eio) {
			if (apacheLogger != null){
				apacheLog(ERROR, "IOException while writing to log", eio);
			}
			if (slf4jLogger != null) {
				slf4jLog(ERROR, "IOException while writing to log", eio);
			}
			return;
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
				list.add(line);
			}
		} catch (IOException eio) {
			if (apacheLogger != null){
				apacheLog(ERROR, "IOException while writing to log", eio);
			}
			if (slf4jLogger != null) {
				slf4jLog(ERROR, "IOException while writing to log", eio);
			}
			return;
		}
		logException(level, list, e);
	}
	protected void apacheLog(int level, String message, Exception e) {
		switch (level) {
		case FATAL :
			apacheLogger.fatal(message);
			break;
		case ERROR :
			apacheLogger.error(message);
			break;			
		case WARN :
			apacheLogger.warn(message);
			break;			
		case INFO :
			apacheLogger.info(message);
			break;
		case DEBUG :
			apacheLogger.debug(message);
			break;			
		case TRACE :
			apacheLogger.trace(message);
			break;	
		}
	}
	protected void slf4jLog(int level, String message, Exception e) {
		switch (level) {
		case FATAL :
			slf4jLogger.error(message);
			break;
		case ERROR :
			slf4jLogger.error(message);
			break;			
		case WARN :
			slf4jLogger.warn(message);
			break;			
		case INFO :
			slf4jLogger.info(message);
			break;
		case DEBUG :
			slf4jLogger.debug(message);
			break;			
		case TRACE :
			slf4jLogger.trace(message);
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
	protected List<String> splitLines(List<String> start) throws IOException {
		return splitLines(start, 3);
	}
	public List<String> splitLines(List<String> start, int indent) throws IOException {
		StringBuffer temp = new StringBuffer();
		int size = indent;
		if (size < 0) {
			size = 3;
		}
		for (int i = 0; i < size; i++) {
			temp.append(" ");
		}
		String prefix = temp.toString();
		List<String> result = new ArrayList<String>();
		if (start == null) { return result; }
		Iterator<String> iter = start.iterator();
		while (iter.hasNext()) {
			String preSplit = iter.next();
			LineNumberReader reader = new LineNumberReader(new StringReader(preSplit));
			while (true) {
				String line = reader.readLine();
				if (line == null) { break; }
				result.add(prefix + line);
			}
		}
		return result;
	}
	protected List<String> splitLines(String start) throws IOException {
		List<String> result = new ArrayList<String>();
		result.add(start);
		return splitLines(result);
	}
	/**
	 * Internal debugging aid for printing list of strings.
	 * @param list list to be printed
	 * @param out destination for printing
	 */
	protected void printList(List<String> list, PrintStream out) {
		if (list == null ) { return; }
		if (out == null) { return; }
		Iterator<String> iter = list.iterator();
		while (iter.hasNext()) {
			out.println("line: " + iter.next());
		}
	}
	protected void printList(List<String> list) {
		printList(list, System.out);
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
		/*
		 * Test of splitting input lines using line feeds and carriage returns.
		 */
		try {
			System.out.println("Testing line splitters");
			StringBuilder test1 = new StringBuilder("abc\r\n");
			test1.append("def\n");
			test1.append("ghi\n");			
			List<String> test2 = helper.splitLines(test1.toString());
			helper.printList(test2);
		} catch (IOException e) {
			helper.error("IOException Error in splitLines", e);
		}
		/*
		 * Testing with log4j
		 */
		Runnable run = helper.new Tester(helper);
		logger.info("Starting test with log4j");
		run.run();
		ExceptionHelper helper2 = new ExceptionHelper(logger2);
		helper2.info("Starting test with org.slf4j");
		Runnable run2 = helper2.new Tester(helper2);
		run2.run();
	}
}
