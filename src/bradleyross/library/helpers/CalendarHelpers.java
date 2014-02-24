package bradleyross.library.helpers;
import java.util.Date;
import java.util.TimeZone;
import java.util.Calendar;
import java.util.GregorianCalendar;
/**
 * Classes for manipulating calendar dates.
 * @author Bradley Ross
 *
 */
public class CalendarHelpers 
{
	protected Calendar refCalendar;
	/**
	 * Controls amount of intermediate output to aid
	 * in software development and diagnostices.
	 * <p>A more positive value indicates that more 
	 *    output is to be generated.  A more negative 
	 *    value indicates that less output is to be
	 *    generated.</p>
	 */
	protected int debugLevel = 0;
	/**
	 * Getter for debugLevel
	 * @see #debugLevel
	 * @return Value of debugLevel
	 */
	public int getDebugLevel()
	{
		return debugLevel;
	}
	/**
	 * Setter for debugLevel
	 * @see #debugLevel
	 * @param value Value for debugLevel
	 */
	public void setDebugLevel(int value)
	{
		debugLevel = value;
	}
	public CalendarHelpers()
	{
		refCalendar = new GregorianCalendar();
	}
	public CalendarHelpers(TimeZone value)
	{
		refCalendar = new GregorianCalendar(value);
	}
	public CalendarHelpers(Calendar value)
	{
		refCalendar = (Calendar) value.clone();
	}
	/**
	 * Returns number of milliseconds since start of day.
	 * @param value Date for which calculation is to be run
	 * @return Number of milliseconds
	 */
	public long getTimeOfDay(Date value)
	{
		long working;
		Calendar local = (Calendar) refCalendar.clone();
		local.setTime(value);
		working = (long) local.get(Calendar.MILLISECOND) +
		(long) (local.get(Calendar.SECOND)*1000) +
		(long) (local.get(Calendar.MINUTE)*60*1000) +
		(long) (local.get(Calendar.HOUR_OF_DAY)*60*1000*60);
		return working;
	}
	public Date getStartOfDay(Date value)
	{
		Calendar local = (Calendar) refCalendar.clone();
		local.setTime(value);
		local.set(Calendar.MILLISECOND, 0);
		local.set(Calendar.SECOND, 0);
		local.set(Calendar.MINUTE, 0);
		local.set(Calendar.HOUR_OF_DAY, 0);
		return local.getTime();
	}
	public Date getNextDay(Date value)
	{
		Calendar local = (Calendar) refCalendar.clone();
		local.setTime(getStartOfDay(value));
		local.add(Calendar.DAY_OF_MONTH, 1);
		return local.getTime();
	}
	/**
	 * This was to get around a bug in some data feeds.
	 * <p>The problem was that the source program was incorrectly assigning
	 *    am and pm for some times.  It appears that this problem has now been corrected.</p>
	 * @param value  Date as provided by program
	 * @return Corrected date
	 * @deprecated
	 */
	public Date specialFix(Date value)
	{
		Calendar local = (Calendar) refCalendar.clone();
		local.setTime(value);
		int hour = local.get(Calendar.HOUR);
		if (hour == 0)
		{
			local.set(Calendar.HOUR, 12);
		}
		else if (hour == 12)
		{
			local.set(Calendar.HOUR, 0);
		}
		return local.getTime();
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		System.out.println("Starting program at " + new Date().toString());

	}

}
