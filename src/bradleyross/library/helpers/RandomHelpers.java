package bradleyross.library.helpers;
import java.util.Random;
/**
 * A set of methods to provide assistance in generating random number patterns.
 * 
 * @author Bradley Ross
 *
 */
public class RandomHelpers 
{
	/**
	 * Since all of the methods are static, the constructor
	 * should never be used.
	 */
	protected RandomHelpers() { ; }
	/**
	 * Controls amount of diagnostic listings.
	 * <p>Larger values result in more diagnostic
	 *    information being printed.</p>
	 * @see #setDebugLevel(int)
	 * @see #getDebugLevel()
	 */
	protected static int debugLevel = 0;
	/**
	 * Setter for debugLevel
	 * @param value Number to be used for debugLevel
	 * @see #setDebugLevel(int)
	 * @see #debugLevel
	 */
	public void setDebugLevel(int value)
	{ debugLevel = value; }
	/**
	 * Getter for debugLevel
	 * @return Value of debugLevel
	 * @see #debugLevel
	 */
	public int getDebugLevel()
	{ return debugLevel; }
	/** 
	 * Create an array with the integers from 0 to
	 * value-1 in a random order.
	 * 
	 * @param value Size of array to be produced
	 * @return Randomized array
	 */
	public static int[] createRandomOrder (int value)
	{
		int working[] = new int[value];
		boolean selected[] = new boolean[value];
		int remaining = value;
		for (int i = 0; i < value; i++)
		{ 
			selected[i] = false; 
			working[i] = -1;
		}
		Random generator = new Random();
		for (int i = 0; i < value - 1; i++)
		{
			int pos = generator.nextInt(remaining);
			for (int j = 0; j < value; j++)
			{
				if (selected[j]) {continue; }
				if (pos == 0)
				{ 
					working[i] = j;
					selected[j] = true;
					break;	
				}
				else 
				{
					pos--;
				}
			}
			if (debugLevel > 0)
			{
				System.out.println("Value selected is " +
						Integer.toString(working[i]));	
			}
			remaining--;
		}
		for (int k = 0; k < value; k++)
		{
			if (!selected[k])
			{ 
				working[value - 1] = k; 
				break;
			}
		}
		return working;
	}
	/**
	 * Test driver
	 * @param args Not used
	 */
	public static void main(String args[])
	{
		for (int j = 0; j< 6; j++)
		{
			int test[] = createRandomOrder(36);
			System.out.print("List: ");

			for (int i = 0; i < test.length; i++)
			{
				System.out.print(Integer.toString(test[i]) +
				" ");
			}
			System.out.println();
		}
	}
}