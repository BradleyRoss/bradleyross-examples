package bradleyross.demonstrations;
/** 
* Provides a quick demonstration of writing to a file.
* <p>If the java class is called with parameters, the first
*    parameter is used as the name of the file to be 
*    written to.</p>
* <p>This program also provides an example of how to print
*    time stamps.</p>
* @author Bradley Ross
*/
public class WriteToFile
{
public static void main (String args[])
   {
   java.io.FileWriter outputFile;
   java.io.BufferedWriter writer;
   java.io.PrintWriter output;
   String fileName = "test.txt";
   if (args.length > 0)
      { fileName = args[0]; }
   String currentTime = (new java.util.Date()).toString();
   try
      {
      outputFile = new java.io.FileWriter(fileName);
      writer = new java.io.BufferedWriter(outputFile);
      output = new java.io.PrintWriter(writer);
      output.println("Writing to file ".concat(fileName));
      output.println("The time is ".concat(currentTime));
      output.println("This is a test");
      output.close();
      }
   catch (java.io.IOException e)
      {
      System.out.println("IOException encountered");
      }
   }
}
