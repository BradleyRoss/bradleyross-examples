package bradleyross.demonstrations;
import java.io.File;
import java.util.Vector;
/**
* Reads a file containing an XML document
* and lists the tags in the document.
* <p>The first parameter is the name of the
*    file from which the information is to be
*    obtained.</p>
*    
* <p>Example:</p>
* <p><code>java bradleyross.demonstrations.getTags test.txt </code></p>
* <p>This will list the structure of the tags in the file
*    <code>test.txt</code>.</p>
* <p>This application requires the appropriate XML jars, such as
*    <code>xml-apis.jar</code> and <code>xercesImpl.jar</code>, to be in the
*    CLASSPATH.</p>
*    @see bradleyross.common.XmlParser
* @author Bradley Ross
*/
public class getTags
   {
   public static void main(String args[])
      {
      Vector<String> v;
      File inputFile = new File(args[0]);
      bradleyross.common.XmlParser parser = 
          new bradleyross.common.XmlParser();
      parser.setDebugLevel(2);
      v = parser.listTags(inputFile);
      String itemList[] = new String[v.size()];
      itemList = v.toArray(itemList);
      for (int i = 0; i < itemList.length; i++)
         { System.out.println(itemList[i]); }
      }
   }
