package bradleyross.library.helpers.tests;
import bradleyross.library.helpers.FileHelpers;
/**
 * Test driver for bradleyross.library.helpers.FileHelpers.copyFile(String,String).
 * @author Bradley Ross
 * @see bradleyross.library.helpers.FileHelpers#copyFile(String,String)
 */
public class TestCopyFile {

	/**
	 * @param args The first argument is the name of the original file, while the 
	 *        second parameter is the name of the copy to be created.
	 */
	public static void main(String[] args) 
	{
		if (args.length < 2)
		{
			System.out.println("Error in bradleyross.library.helpers.tests.TestCopyFile");
			System.out.println("Not enough arguments");
		}
		try
		{
		FileHelpers.copyFile(args[0], args[1]);
		}
		catch (Exception e)
		{
			System.out.println(e.getClass().getName() + " " + e.getMessage());
		}
	}
}
