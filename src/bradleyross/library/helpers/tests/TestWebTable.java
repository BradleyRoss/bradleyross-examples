package bradleyross.library.helpers.tests;
import bradleyross.library.helpers.WebTable;
import bradleyross.library.helpers.GenericPrinter;
public class TestWebTable extends WebTable
{
	public static void main (String args[])
	{
		TestWebTable instance = new TestWebTable();
		GenericPrinter out = new GenericPrinter(System.out);
		instance.setOutput(out);
		instance.startTable();
		
		instance.setColumnCount(4);
		instance.formatHeaderRow("First", "Second", "Third", "Fourth");
		instance.finishTable();
	}


}
