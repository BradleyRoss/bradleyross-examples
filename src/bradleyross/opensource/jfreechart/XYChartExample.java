package bradleyross.opensource.jfreechart;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.ChartFrame;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.chart.plot.PlotOrientation;
import java.io.File;
/**
 * Example of generating XY chart.
 * <p>This example was taken from
 *    <a href="http://www.screaming-penguin.com/node/4005" target="_blank">
 *    http://www.screaming-penguin.com/node/4005</a>.  I added some
 *    additional comments and completed the code sample.  (Primarily adding package
 *    and import statements.)</p>
 * <p>When the program is run from within Eclipse, there doesn't seem to be
 *    any way to shut it down withut shutting down eclipse.  When it is started
 *    from the command line, it is resizable and has its own menu bar which
 *    allows it to be closed.  The problem may be a combination of Swing and
 *    SWT graphics.</p>
 * @author Bradley Ross
 * @see ChartFactory
 * @see ChartFrame
 * @see ChartUtilities
 * @see JFreeChart
 * @see PlotOrientation
 * @see XYSeries
 * @see XYSeriesCollection
 *
 */
public class XYChartExample {

	/**
	 * Creates and displays the chart.
	 * @param args If a parameter is specified, it is used as the file name to be
	 *    generated.  If there are parameters in the array args, the chart is
	 *    displayed in a window.
	 */
	public static void main(String[] args) {
		XYSeries series = new XYSeries("XYGraph");
		series.add(1, 1);
		series.add(1, 2);
		series.add(2, 1);
		series.add(3, 9);
		series.add(4, 10);
		XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(series);
		JFreeChart chart = ChartFactory.createXYLineChart("XY Chart",
				"x-axis",
				"y-axis",
				dataset,
				PlotOrientation.VERTICAL,
				true,
				true,
				false);
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
