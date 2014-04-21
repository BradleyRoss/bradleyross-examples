package bradleyross.demonstrations;
import java.io.File;
import java.util.Vector;
/**
* Reads a file containing an XML document
* and searches for strings having a given
* sequence of tags.
* <p>The first parameter is the name of the
*    file from which the information is to be
*    obtained.</p>
* <p>The second parameter is the tag list to be
*    checked for.  In this case parsing of the tags
*    is case sensitive.</p>
* <p>Example:</p>
* <p><code>java bradleyross.demonstrations.parseFile test.txt "&lt;html&gt;&lt;body&gt;"</code></p>
* <p>This will give the contents of <code>body</code> tags contained
*    within <code>html</code> tags.</p>
* <p>This application requires the appropriate XML jars, such as
*    <code>xml-apis.jar</code> and <code>xercesImpl.jar</code>, to be in the
*    CLASSPATH.</p>
* @see bradleyross.common.XmlParser
* @author Bradley Ross
*/
public class parseFile
   {
   public static void main(String args[])
      {
      Vector<String> v;
      File inputFile = new File(args[0]);
      bradleyross.common.XmlParser parser = 
          new bradleyross.common.XmlParser();
      v = parser.parseString(inputFile, args[1]);
      String itemList[] = new String[v.size()];
      itemList = v.toArray(itemList);
      for (int i = 0; i < itemList.length; i++)
         { System.out.println(itemList[i]); }
      }
   }
