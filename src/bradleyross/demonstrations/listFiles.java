package bradleyross.demonstrations;
import java.io.*;
/**
* Lists files in current directory
*/
public class listFiles
{
public static void main (String args[])
   {
   try
      {
      File root = new File(".");
      String list[] = root.list();
      for (int i = 0; i < list.length; i++)
         { System.out.println(list[i]); }
      }
   catch (Exception e)
      {
      e.printStackTrace(System.out);
      }
   }
}
