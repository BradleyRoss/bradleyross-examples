package bradleyross.common;
/**
* General purpose helpers for use in creating servlets
*/
public class servletHelpers
{
/**
* This method is intended to help in showing the elapsed time for
* generating a page.
* <p>By using this method, you avoid the possibility of the
*    value for milliseconds displaying with over a dozen
*    digits due to round-off error.</p>
* @param startTime Date (in milliseconds that was generated using
*        a previous java.util.Date object.
* @return Formatted string containing the elapsed time in
*        milliseconds
* @see java.util.Date
* @see java.text.NumberFormat
*/
public static String formatElapsedTime (long startTime)
   {
   long difference = new java.util.Date().getTime() - startTime;
   java.text.NumberFormat nf = java.text.NumberFormat.getInstance();
   nf.setMaximumFractionDigits(3);
   return nf.format((double) difference / 1000.0d);
   }
/**
* Returns a default value if the examined value is null.
* <p>Trying to run a number of String methods, such as equals or 
*    equalsIgnoreCase, result in exeptions being thrown if the 
*    object being examined has a value of null<p>
* @param value String value to be examined
* @param ifDefault Default value to be used if value is null
* @return String containing either examined or default value
*/
public static String valueWithDefault ( String value, String ifDefault)
   {
   if ( value == null)
      { return ifDefault; }
   else
      {return value; }
   }
/**
* Replace occurences of double quotes with two
* double quote characters.
* @param value String to be processed
* @return Processed string
*/
public static String doubleDoubleQuotes (String value)
   {
   java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("\\\"");
   java.util.regex.Matcher matcher = pattern.matcher(value);
   return matcher.replaceAll("\"\"");
   }
/**
Test driver
*/
public static void main(String args[])
   {
   System.out.println(doubleDoubleQuotes("\"This is  a test\""));
   }
}
