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
import bradleyross.opensource.jfreechart.helpers.DataGenerator;
import bradleyross.opensource.jfreechart.helpers.TickUnitCollection;
import bradleyross.library.helpers.DateHelpers;
/**
 * Shows a parameter varying over time.
 * <p>This plot shows a sine wave over a period of
 *    a few days.</p>
 * @author Bradley Ross
 * @see org.jfree.data.time.TimeSeries
 * @see org.jfree.data.time.Minute
 * @see org.jfree.chart.ChartFactory
 * @see org.jfree.chart.ChartUtilities
 * @see org.jfree.chart.JFreeChart
 * @see org.jfree.chart.ChartFrame
 * @see org.jfree.data.time.TimeSeriesCollection
 *
 */
public class TimeSeriesExample2 {

	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		TimeSeries heat = new TimeSeries("Track 1", Minute.class);
		TimeSeries heat2 = new TimeSeries("Track 2", Minute.class);
		long now = new java.util.Date().getTime();
		for (long i = 0; i < 960; i++)
		{ 
			heat.add(new Minute(new Date(now + i * 6l * 60000l)), 
				10.0d + 2.0d*Math.sin(0.1d*i*Math.PI)); 
			heat2.add(new Minute(new Date(now + 30000l + i * 6l * 60000l)), 
					14.0d + 3.0d*Math.sin(0.1d*i*Math.PI)); 			
		}
		TimeSeriesCollection dataset = new TimeSeriesCollection();
		dataset.addSeries(heat);
		dataset.addSeries(heat2);
		dataset.addSeries(DataGenerator.generateSine
				("Test", 3600l*4000l, 3600l*24000l*4l, 60000l, 2.0d, 3.0d,
						DateHelpers.startOfHour(new Date())));
		
		JFreeChart chart = ChartFactory.createTimeSeriesChart
			("Temperature trend", null, "Temperature (degrees F)",
					dataset, true, true, false);
		TickUnitCollection ticks = new TickUnitCollection();
		ticks.setMNTickMarks(chart, DateHelpers.startOfHour(new Date()),
				new Date(DateHelpers.startOfHour(new Date()).getTime() + 3600l*24000l*4l));
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
			System.out.println(e.getClass().getName());
			System.out.println(e.getMessage());
		}
	}
}
