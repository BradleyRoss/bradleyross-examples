package bradleyross.opensource.xerces;
import java.util.Vector;
/**
 * This is the demonstration driver for the class {@link XmlParser}.
 * 
 * <p>It runs a dummy string through the parser and lists the tags
 *    and how they nest.</p>
 * 
 * @author Bradley Ross
 *
 */
public class RunXmlParser {
	private static String firstTest = "<html><head><title>Test</title></head>" +
       "<body></body></html>";
	public static void main(String[] args) {
		Vector<String> list = new Vector<String>();
	XmlParser parser = new XmlParser();
	list = parser.listTags(firstTest);
	System.out.println(firstTest);
	for (int i = 0; i < list.size(); i++) {
		System.out.println(list.get(i));
	    }

	}

}
