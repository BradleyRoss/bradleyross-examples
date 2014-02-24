package bradleyross.library.helpers;
/**
 * Common procedures for use with HTML document.
 * @author bradleyross
 *
 */
public class HtmlHelpers {
	/**
	 * Replace the characters ampersand, less than, and greater than with
	 * literal strings so that they are not interpreted as HTML control characters.
	 * <p>This process is known as escaping the control characters.</p>
	 * @param value original string
	 * @return escaped string
	 */
	public static String escape(String value) {
		if (value == null) { return null; }
		if (value.length() == 0) { return value; }
		return value.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;");
	}
	/**
	 * Reverse the escaping of the control characters to get the original string.
	 * @param value escaped string
	 * @return original string
	 */
	public static String unescape(String value) {
		if (value == null) { return value; }
		if (value.length() == 0) { return value; }
		return value.replace("&gt;", ">").replaceAll("&lt;", "<").replaceAll("&amp;", "&");
	}
	/**
	 * Test driver for process of escaping strings;
	 * @param value string to be converted
	 */
	protected static void test(String value) {
		if (value == null) {
			System.out.println("Argument has null value");
		}
		if (value.length() == 0) {
			System.out.println("Argument has zero length");
		}
		System.out.println("*****  *****  *****");
		System.out.println(value);
		System.out.println(escape(value));
		System.out.println(unescape(escape(value)));
		if (value.equals(unescape(escape(value)))) {
			System.out.println("Success");
		} else {
			System.out.println("*****  *****  *****  *****  *****");
			System.out.println("*****  Failure");
			System.out.println("*****  *****  *****  *****  *****");
		}
		System.out.println("*****  *****  *****");
	}
	/**
	 * Main driver for test cases.
	 * @param args not used
	 */
	public static void main(String args[]) {
		String[] tests = { "<> &&", "&amp;&gt;&lt;" };
		for (int i = 0; i < tests.length; i++) {
			test(tests[i]);
		}
	}
}
