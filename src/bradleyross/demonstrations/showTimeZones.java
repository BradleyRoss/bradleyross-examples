package bradleyross.demonstrations;
/**
* Listing of time zones in Java
* @author Bradley Ross
*/
public class showTimeZones
{
public static void main(String args[])
   {
   int maxLength = 0;
   String list[] = null;
   list = java.util.TimeZone.getAvailableIDs(-5*3600*1000);
   System.out.println("*****  *****  ***** " + Integer.toString(list.length) + " entries");
   System.out.println("GMT-5 time zones");
   for (int i=0; i < list.length; i++)
      { 
	  System.out.println(list[i]);
	  }
   list = java.util.TimeZone.getAvailableIDs();
   System.out.println("*****  *****  ***** " +
        Integer.toString(list.length) + " entries");
   System.out.println("All time zones");
   for (int i = 0; i < list.length; i++)
      {
	  System.out.println(list[i]);
      if (list[i].length() > maxLength)
	     { maxLength = list[i].length(); }
      }
   System.out.println("Maximum length is " + Integer.toString(maxLength));
   }
}
   