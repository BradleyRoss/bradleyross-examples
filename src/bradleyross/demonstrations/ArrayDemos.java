package bradleyross.demonstrations;
/**
 * Some demonstrations of the results of passing objects in
 *    the parameter lists of methods.
 * @author Bradley Ross
 *
 */
public class ArrayDemos implements Runnable {
	int[] a = new int[]{0, 1, 2, 3, 4, 5};
	public void run() {
		int[] array = a.clone();
		System.out.println("Value of element 2 before call to changeElement is " + Integer.toString(array[2]));
		changeElement(array, 2, 6);
		System.out.println("Value of element 2 after call to changeElement is " + Integer.toString(array[2]));
		int a = 24;
		System.out.println("Value before call to changeValue is " + Integer.toString(a));
		changeValue(a, 10);
		System.out.println("Value after call to changeValue is " + Integer.toString(a));
	}
	private void changeValue(int a, int newValue) {
		System.out.println("changeValue: starting value is " + Integer.toString(a));
		a = newValue;
		System.out.println("changeValue: final value is " + Integer.toString(a));
	}
	private void changeElement(int[] array, int position, int newValue) {
		array[position] = newValue;
		System.out.println("Within method changeElement position " + Integer.toString(position) + 
				" changed to " + Integer.toString(newValue));
	}
	public static void main(String[] args) {
		System.out.println("Starting program");
		ArrayDemos instance = new ArrayDemos();
		instance.run();
	}
}
