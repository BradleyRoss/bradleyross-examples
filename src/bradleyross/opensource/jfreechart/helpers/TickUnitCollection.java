package bradleyross.opensource.jfreechart.helpers;
import org.jfree.chart.axis.TickUnits;
import org.jfree.chart.axis.DateTickUnit;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.DateTickMarkPosition;
import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.text.SimpleDateFormat;
import java.text.NumberFormat;

/**
 * Create a new collection of tick units.
 * 
 * <p>This class contains methods that help to control the appearance
 *    of the axis and the associated tick marks.</p>
 * @author Bradley Ross
 * @see TickUnits
 * @see DateTickUnit
 *
 */
public class TickUnitCollection 
{
	/** 
	 * Controls amount of diagnostic messages 
	 * @see #getDebugLevel()
	 * @see #setDebugLevel(int)
	 * */
	protected int outerDebugLevel = 0;
	/**
	 * Getter for outerDebugLevel
	 * @see #outerDebugLevel
	 * @return Value of outerDebugLevel
	 */
	public int getDebugLevel()
	{ return outerDebugLevel; }
	/** 
	 * Setter for outerDebugLevel.
	 * 
	 * @see #outerDebugLevel
	 * @param value Value for outerDebugLevel
	 */
	public void setDebugLevel(int value)
	{ outerDebugLevel = value; }
	/**
	 * Constructor allowing extra debugging aids.
	 * 
	 * @param value Value for outerDebugLevel
	 */
	public TickUnitCollection(int value)
	{
		outerDebugLevel = value;
		if (value > 0)
		{
			System.out.println("Running constructor for TickUnitCollection");
		}
	}
	/**
	 * Default constructor.
	 */
	public TickUnitCollection()
	{ outerDebugLevel = 0; }
	/**
	 * java.text.DateFormat class required to satisfy the
	 * formatting requirements of the JFreeChart package.
	 * 
	 * @author Bradley Ross
	 *
	 */
	protected class MNFormat extends java.text.DateFormat
	{
		protected int debugLevel = 0;
		public int getDebugLevel()
		{ return debugLevel; }
		public void setDebugLevel(int value)
		{ debugLevel = value; }
		/**
		 * Default entry to satisfy serializable interface.
		 */
		private static final long serialVersionUID = 1L;
		MNFormat()
		{ if (outerDebugLevel > 0) { setDebugLevel(outerDebugLevel); }}
		/**
		 * Returns  a StringBuffer containing N or M to represent
		 * noon and midnight in the time axis of the chart.  For other times
		 * of day, it returns a blank character.
		 * <p>This method may not return null although it may
		 *    return an empty StringBuffer.</p>
		 * <p>The values of 11, 13, 23, and 1 were added to the tests
		 *    for the value of the hour field to handle the case where
		 *    Daylight Savings Time begins or ends in the reporting
		 *    period.  In this case, the hour corresponding to the tick
		 *    marks is shifted by an hour after the change.</p>
		 * @param date Date to be used in selecting the character
		 * @param toAppendTo
		 * @param fieldPosition
		 * @return Character N or M to indicate noon or midnight or a
		 *         blank character to indicate other times.
		 */
		public StringBuffer format(java.util.Date date, StringBuffer toAppendTo,
				java.text.FieldPosition fieldPosition)
		{
			if (debugLevel > 1)
			{ 
				java.text.SimpleDateFormat format =
					new java.text.SimpleDateFormat("dd-MMM-yyyy HH:mm:ss:SSS");
				System.out.println("MNFormat.format - Time for tick " +
						format.format(date)); 
			}
			GregorianCalendar calendar = new GregorianCalendar();
			calendar.setTime(date);
			int hour = calendar.get(Calendar.HOUR_OF_DAY);
			int minute = calendar.get(Calendar.MINUTE);
			if ( minute >= 30)
			{
				calendar.set(Calendar.MINUTE, 0);
				calendar.add(Calendar.HOUR_OF_DAY, 1);
			}
			else
			{
				calendar.set(Calendar.MINUTE, 0);
			}
			hour = calendar.get(Calendar.HOUR_OF_DAY);
			if (hour == 23 | hour == 24 || hour == 00 | hour == 01)
			{ 
				toAppendTo.append("M");
				return new StringBuffer("M"); }
			else if (hour == 11 | hour == 12 | hour == 13)
			{ 
				toAppendTo.append("N");
				return new StringBuffer("N"); }
			else
			{ return new StringBuffer(); }
		}
		/**
		 * Not used in this class.
		 * <p>This would normally be used to convert the character 
		 *    representation into a java.util.Date object.  However,
		 *    that is not possible given the nature of this class.  A
		 *    dummy method that returns null is used to satisfy the 
		 *    interface and superclass requirements.  Actually
		 *    calling this method will cause an exception to be 
		 *    raised.</p>
		 */
		public java.util.Date parse(String source, java.text.ParsePosition pos)
		{
			return null;
		}
	}
	/**
	 * Set up the time tick marks and text items for the charts so that the time
	 * axis has letters indicating noon and midnight as well as the day of week,
	 * day of month and month.
     * <p>This version didn't work.  The version specifying start and end
     *    times worked.</p>
	 * @param chart JFreeChart object to be modified.
	 */
	public void setMNTickMarks (JFreeChart chart)
	{
		XYPlot plot = null;
		try
		{
			plot = chart.getXYPlot();
			DateAxis newAxis = new DateAxis();
			DateAxis oldAxis = (DateAxis) plot.getDomainAxis();
			plot.setDomainAxis(1, newAxis);
			oldAxis.setStandardTickUnits(getNewUnits());
			oldAxis.setDateFormatOverride(new MNFormat());
			newAxis.setAxisLinePaint(chart.getBackgroundPaint());
			/*
			 * The default for setTickMarkPosition is to place the
			 * tick mark at the end of the interval
			 */
			oldAxis.setTickMarkPosition(DateTickMarkPosition.START);
			newAxis.setTickMarkPosition(DateTickMarkPosition.MIDDLE);
			newAxis.setAutoTickUnitSelection(false);
			plot.setDomainAxisLocation(1, AxisLocation.BOTTOM_OR_RIGHT);
			DateAxis bottomAxis = new DateAxis();
			plot.setDomainAxis(2, bottomAxis);
			newAxis.setDateFormatOverride(new SimpleDateFormat("dd-MMM"));
			bottomAxis.setDateFormatOverride(new SimpleDateFormat("EEE"));
			bottomAxis.setStandardTickUnits(getDailyUnits());
			newAxis.setStandardTickUnits(getDailyUnits());
			bottomAxis.setAxisLinePaint(chart.getBackgroundPaint());
			bottomAxis.setTickMarkPosition(DateTickMarkPosition.MIDDLE);
			bottomAxis.setAutoTickUnitSelection(false);
			plot.setDomainAxisLocation(2, AxisLocation.BOTTOM_OR_RIGHT);
		}
		catch (Exception e)
		{
		}
	}	
	
	
	/**
	 * Set up the time tick marks and text items for the charts so that the time
	 * axis has letters indicating noon and midnight as well as the day of week,
	 * day of month and month.
	 * <p>It is necessary to specify the starting and ending time so that the
	 * 	  three time based axis can be lined up correctly.  For the three axis
	 *    (month/day, day of week, noon/midnight), the following actions
	 *    are carried out.</p>
	 * <p>When I didn't set the upper/lower bounds of each domain axis and turn off
	 *    autoranging, only the first row appeared.</p>
	 * <ul>
	 * <li><p>setAutoRange(false) is used to turn off automatic adjustment of 
	 *        the axis range and the setLowerBound and setUpperBound methods
	 *        are used to set the time range.</p></li>
	 * <li><p>The color of the axis line is set to the background color so that
	 *        it becomes invisible.</p></li>
	 * <li><p>The setDateFormatOverride, setStandardTickUnits, 
	 *        setAutoTickUnitSelection, and
	 *        setTickMarkPosition methods are used to set the appearance
	 *        of the axis.</p></li>
	 * </ul>
	 * <p>There appears to be a problem with the JFreeChart software.  Unless I moved
	 *    the start to before the start of the first interval, the first interval
	 *    was reduced in length by an hour.  Moving the start time created an
	 *    initial interval that was two milliseconds in length.</p>
	 * @param chart JFreeChart object to be modified.
	 * @param startTimeIn Start of time domain range
	 * @param endTime End of time domain range
	 */
	public void setMNTickMarks (JFreeChart chart, Date startTimeIn, Date endTime)
	{
		XYPlot plot = null;
		try
		{
			/*
			 * I had to move the start of the reporting period before the start of the
			 * first tick.  Otherwise, it reduced the size of the first interval
			 * by one hour.
			 * 
			 * This appears to be a bug.
			 */
			java.util.Date startTime = new java.util.Date(startTimeIn.getTime() - 2l);
			if (outerDebugLevel > 0)
			{
				java.text.SimpleDateFormat format =
					new java.text.SimpleDateFormat ("dd-MMM-yyyy HH:mm:ss:SSS");
				System.out.println("Constructor for setMNTickMarks " +
						format.format(startTime) + "    " +
						format.format(endTime));
			}
			plot = chart.getXYPlot();
			DateAxis newAxis = new DateAxis();
			DateAxis oldAxis = (DateAxis) plot.getDomainAxis();
			newAxis.setAutoRange(false);
			oldAxis.setAutoRange(false);
			newAxis.setLowerBound(startTime.getTime());
			oldAxis.setLowerBound(startTime.getTime());
			newAxis.setUpperBound(endTime.getTime());
			oldAxis.setUpperBound(endTime.getTime());
			plot.setDomainAxis(1, newAxis);
			oldAxis.setStandardTickUnits(getHourlyUnits(6));
			oldAxis.setDateFormatOverride(new MNFormat());
			newAxis.setAxisLinePaint(chart.getBackgroundPaint());
			newAxis.setTickMarkPosition(DateTickMarkPosition.MIDDLE);
			newAxis.setAutoTickUnitSelection(false);
			plot.setDomainAxisLocation(1, AxisLocation.BOTTOM_OR_RIGHT);
			DateAxis bottomAxis = new DateAxis();
			bottomAxis.setAutoRange(false);
			bottomAxis.setLowerBound(startTime.getTime());
			bottomAxis.setUpperBound(endTime.getTime());
			plot.setDomainAxis(2, bottomAxis);
			newAxis.setDateFormatOverride(new SimpleDateFormat("dd-MMM"));
			bottomAxis.setDateFormatOverride(new SimpleDateFormat("EEE"));
			bottomAxis.setStandardTickUnits(getDailyUnits());
			newAxis.setStandardTickUnits(getDailyUnits());
			bottomAxis.setAxisLinePaint(chart.getBackgroundPaint());
			bottomAxis.setTickMarkPosition(DateTickMarkPosition.MIDDLE);
			bottomAxis.setAutoTickUnitSelection(false);
			plot.setDomainAxisLocation(2, AxisLocation.BOTTOM_OR_RIGHT);
		}
		catch (Exception e)
		{
		}
	}
	/**
	 * Creates tick marks at twelve hour intervals.
	 * <p>org.jfree.chart.axis.TickUnits implements the
	 *    org.jfree.chart.axis.TickUnitSource interface.</p>
	 * <p>org.jfree.chart.axis.DateTickUnit is a subclass
	 *    of the abstract class
	 *    org.jfree.chart.axis.TickUnit.</p>
	 * <p>According to section 9.4.8 of the manual, you use the
	 *    setStandardTickUnits method of the axis object
	 *    to specify the tick mark collection to be used.<br>
	 *    DateAxis.setStandardTickUnits(TickUnitSource)</p>
	 *    
	 * @return Object defining set of tick marks.
	 * @see org.jfree.chart.axis.TickUnitSource
	 * @see org.jfree.chart.axis.TickUnits
	 * @see org.jfree.chart.axis.TickUnit
	 * @see org.jfree.chart.axis.DateTickUnit
	 */
	public TickUnits getNewUnits()
	{
		TickUnits newUnits = null;
		newUnits = new TickUnits();
		newUnits.add(new DateTickUnit(DateTickUnit.HOUR, 12));
		return newUnits;
	}
	/**
	 * Set up tick marks at multiple hour intervals.  
	 * 
	 * <p>Recommended intervals are
	 * 2, 3, 4, 6, and 12 hours.  Other intervals may have a strange appearance.</p>
	 * @param hours Number of hours per tick mark
	 * @return Set of tick units
	 */
	public TickUnits getHourlyUnits(int hours)
	{
		TickUnits newUnits = new TickUnits();
		newUnits.add(new DateTickUnit(DateTickUnit.HOUR, hours));
		return newUnits;
	}
	/**
	 * Creates tick marks at 24 hour (daily) intervals.
	 * <p>The constructor getDailyUnits() behaves the same
	 *    as getHourlyUnits(24).</p>
	 * @return Object defining tick marks
	 */
	public TickUnits getDailyUnits()
	{
		TickUnits dailyUnits = new TickUnits();
		dailyUnits.add(new DateTickUnit(DateTickUnit.HOUR, 24));
		return dailyUnits;
	}
	/**
	 * Specify the distance between tick marks.
	 * <p>This allows the tick marks on a plot with
	 *    multiple range axis to be lined up to
	 *    provide a cleaner appearance.</p>
	 * @param size Distance between tick marks
	 * @return Set of tick units
	 */
	public TickUnits getNumberUnits(double size)
	{
		TickUnits valueUnits = new TickUnits();
		valueUnits.add(new NumberTickUnit(size));
		return valueUnits;
	}
	/**
	 * Specify the distance between tick marks and the format
	 * to be used in presenting the values for the tick marks.
	 * <p>This allows the tick marks on a plot with
	 *    multiple range axis to be lined up to
	 *    provide a cleaner appearance.</p>
	 * @param size Distance between tick marks
	 * @param formatter Formatter to be used for values of tick marks
	 * @return Set of tick units
	 */
	public TickUnits getNumberUnits(double size, NumberFormat formatter)
	{
		TickUnits valueUnits = new TickUnits();
		valueUnits.add(new NumberTickUnit(size, formatter));
		return valueUnits;
	}		
}
