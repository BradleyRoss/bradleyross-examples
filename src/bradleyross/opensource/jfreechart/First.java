package bradleyross.opensource.jfreechart;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
/** Example of a pie chart from the installation notes.
 * <p>This example was given in the installation notes
 *    that accompanied the files in the JFreeChart distribution.</p>
 * 
 * @author Bradley Ross
 *
 */
public class First {

	/**
	 * Create and display the chart.
	 * @param args - Parameters are not used by this program.
	 * @see ChartFactory
	 */
	public static void main(String[] args) {
		DefaultPieDataset data = new DefaultPieDataset();
		data.setValue("Category 1", 43.2);
		data.setValue("Category 2", 27.9);
		data.setValue("Category 3", 79.5);
		JFreeChart chart = ChartFactory.createPieChart(
				"Sample Pie Chart",
				data,
				true,
				true,
				false
	);
		ChartFrame frame = new ChartFrame("First", chart);
		frame.pack();
		frame.setVisible(true);
	}
}
