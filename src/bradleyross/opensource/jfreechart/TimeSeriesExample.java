package bradleyross.opensource.jfreechart;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.Minute;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.ChartFrame;
import java.util.Date;
import java.io.File;
import org.jfree.data.time.TimeSeriesCollection;
/**
 * Shows a parameter varying over time with calls giving
 * the time and the value of the parameter.
 * @author Bradley Ross
 * @see org.jfree.data.time.TimeSeries
 * @see org.jfree.data.time.Minute
 * @see org.jfree.chart.ChartFactory
 * @see org.jfree.chart.ChartUtilities
 * @see org.jfree.chart.JFreeChart
 * @see org.jfree.data.time.TimeSeriesCollection
 *
 */
public class TimeSeriesExample {

	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		TimeSeries heat = new TimeSeries("Temperature", Minute.class);
		long now = new java.util.Date().getTime();
		heat.add(new Minute(new Date(now - 10l * 60000l)), 15.0);
		heat.add(new Minute(new Date(now - 9l * 60000l)), 14.5);
		heat.add(new Minute(new Date(now - 8l * 60000l)), 20.0);
		heat.add(new Minute(new Date(now - 7l * 60000l)), 17.0);
		heat.add(new Minute(new Date(now - 6l * 60000l)), 16.5);
		heat.add(new Minute(new Date(now - 5l * 60000l)), 15.0);
		heat.add(new Minute(new Date(now - 4l * 60000l)), 14.0);
		heat.add(new Minute(new Date(now - 3l * 60000l)), 13.0);
		TimeSeriesCollection dataset = new TimeSeriesCollection();
		dataset.addSeries(heat);
		JFreeChart chart = ChartFactory.createTimeSeriesChart
			("Temperature trend", "Time", "Temperature (degrees F)",
					dataset, true, true, false);
		try
		{
			if (args.length > 0)
			{ ChartUtilities.saveChartAsJPEG(new File(args[0]), chart, 500, 300); }
			else
			{
				javax.swing.JMenuBar menuBar = new javax.swing.JMenuBar();
				ChartFrame frame = new ChartFrame("sample", chart);
				frame.setJMenuBar(menuBar);
				frame.pack();
				frame.setVisible(true);
			}
		}
		catch (Exception e)
		{
			System.out.println("Problem occurred creating chart");
		}
	}
}
