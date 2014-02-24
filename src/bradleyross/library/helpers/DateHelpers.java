package bradleyross.library.helpers;
import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;
/**
 * Utility functions for processing dates and times.
 * @author Bradley Ross
 *
 */
public class DateHelpers 
{
/**
 * Since all of the methods are static, the constructor should never
 * be used.
 */
protected DateHelpers()
{ ; }
/**
 * Return the start of a month containing a specified time
 * @param value Specified time
 * @return Start of month
 * 
 */
public static Date startOfMonth(Date value)
{
	Calendar working = new GregorianCalendar();
	working.setTime(value);
	working.set(Calendar.DAY_OF_MONTH, 1);
	working.set(Calendar.HOUR_OF_DAY, 0);
	working.set(Calendar.MINUTE, 0);
	working.set(Calendar.SECOND, 0);
	working.set(Calendar.MILLISECOND, 0);
	return working.getTime();
}	
/**
 * Return the start of a day containing a specified time
 * @param value Specified time
 * @return Start of day
 */
public static Date startOfDay(Date value)
{
	Calendar working = new GregorianCalendar();
	working.setTime(value);
	working.set(Calendar.HOUR_OF_DAY, 0);
	working.set(Calendar.MINUTE, 0);
	working.set(Calendar.SECOND, 0);
	working.set(Calendar.MILLISECOND, 0);
	return working.getTime();
}
/**
 * Return the end of a day containing a specified time
 * @param value Specified time
 * @return Start of day
 */
public static Date endOfDay(Date value)
{
	Calendar working = new GregorianCalendar();
	working.setTime(value);
	working.set(Calendar.HOUR_OF_DAY, 23);
	working.set(Calendar.MINUTE, 59);
	working.set(Calendar.SECOND, 59);
	working.set(Calendar.MILLISECOND, 999);
	return working.getTime();
}
/**
 * Return the start of an hour containing a specified time
 * @param value Specified time
 * @return Start of hour
 */
public static Date startOfHour(Date value)
{
	Calendar working = new GregorianCalendar();
	working.setTime(value);
	working.set(Calendar.MINUTE, 0);
	working.set(Calendar.SECOND, 0);
	working.set(Calendar.MILLISECOND, 0);
	return working.getTime();
}
/**
 * Return the end of an hour containing a specified time
 * @param value Specified time
 * @return End of hour
 */
public static Date endOfHour(Date value)
{
	Calendar working = new GregorianCalendar();
	working.setTime(value);
	working.set(Calendar.MINUTE, 59);
	working.set(Calendar.SECOND, 59);
	working.set(Calendar.MILLISECOND, 999);
	return working.getTime();
}

/**
 * Return the start of a minute containing a specified time
 * @param value Specified time
 * @return Start of minute
 */
public static Date startOfMinute(Date value)
{
	Calendar working = new GregorianCalendar();
	working.setTime(value);
	working.set(Calendar.SECOND, 0);
	working.set(Calendar.MILLISECOND, 0);
	return working.getTime();
}

/**
 * Return the end of a minute containing a specified time
 * @param value Specified time
 * @return End of minute
 */
public static Date endOfMinute(Date value)
{
	Calendar working = new GregorianCalendar();
	working.setTime(value);
	working.set(Calendar.SECOND, 59);
	working.set(Calendar.MILLISECOND, 999);
	return working.getTime();
}

/**
 * Test driver for class.
 * @param args Not used
 */
public static void main (String args[])
{
	System.out.println("Test driver for bradleyross.library.helpers.DateHelpers");
	Date now = new Date();
	java.text.SimpleDateFormat formatter =
		new java.text.SimpleDateFormat("EEE dd-MMM-yyyy HH:mm:ss.SSS");
	System.out.println("Current time is " + formatter.format(now));
	System.out.println("Start of month is " + formatter.format(startOfMonth(now)));
	System.out.println("Start of day is " + formatter.format(startOfDay(now)));
	System.out.println("End of day is " + formatter.format(endOfDay(now)));
	System.out.println("Start of hour is " + formatter.format(startOfHour(now)));
	System.out.println("End of hour is " + formatter.format(endOfHour(now)));
	System.out.println("Start of minute is " + formatter.format(startOfMinute(now)));
	System.out.println("End of minute is " + formatter.format(endOfMinute(now)));
}
}
