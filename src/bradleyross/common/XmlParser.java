package bradleyross.common;
import org.xml.sax.*;
import org.xml.sax.helpers.*;
import java.util.*;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
/** The purpose of this class is to provide
* a set of tools for parsing XML documents.
* <p>When you create a class for parsing XML files, it is necessary
*    to create a subclass of org.xml.sax.ContentHandler
*    for a content handler object.  The
*    XML parser calls methods in this class when various events
*    occur while reading the XML files.  Events would be such things
*    as the start or end of a tagged element.</p>
* <p>In order to use this class, this class it is necessary
* to have xercesImpl.jar and xml-apis.jar
* from the Apache Xerces project in the
* CLASSPATH.</p>
* <p>There are two sets of source code for this
*    class, one for versions previous to Java 5 and the other
*    for Java 5 and later.</p>
* <p>When constructing code using Vector classes, it is necessary to
*    code differently for Java 5 and later versions because 
*    version 5 introduced the idea of parameterizing Vector objects.</p>
* <p><ul>
* <li><p>For Java 5 and later, you will see the syntax
*     <code>Vector&lt;String&gt;</code>.  This means
*     objects making up the Vector are all of type String.</p></li>
* <li><p>For versions before Java 5, the syntax is simply
*     <code>Vector</code>.  This is because there is no method in
*     the earlier versions of restricting Vectors and other list
*     to a single class.</p></li>
* </ul></p>
* <p>The following is an example of a file to be processed by this object.</p>
* <p>&lt;html&gt;&lt;head&gt;&lt;/head&gt;<br />
* &lt;body&gt;aaaa&lt;/body&gt;&lt;/html&gt;</code></p>
* @see bradleyross.demonstrations.getTags
* @see bradleyross.demonstrations.parseFile
* @see org.xml.sax.ContentHandler
*
* @author Bradley Ross
*/
public class XmlParser 
{
/** Indicates mode of parsing operation. */
private int mode;
/** Getter for mode */
public int getMode()
	{ return mode; }
/** Option for listing tags in document. */
public static final int LISTTAGS = 1;
/** Option for searching for strings with specified tags. */
public static final int SEARCH = 2;
/** Vector containing strings or lists of tags in document */
private Vector<String> items;
/** Amount of diagnostic listing to be generated. */
private int debugLevel = 0;
/**
* This class provides the means of responding to
* the Xerces parser from the Apache Xerces
* parser.
*/
protected class MyHandler implements org.xml.sax.ContentHandler
   {
   /**
   * Indicates type of search to be carried out.
   * <p>Value of LISTTAGS used for the following methods where
   *    the goal is to get a list of the tags and their structure
   *    in the document.</p>
   * <p><ul>
   * <li><p>public Vector parseString (Vector start,
   *        File document, String search)</p></li>
   * <li><p>public Vector listTags (Vector start, String document)</p></li>
   * <li><p>public Vector listTags (File document, String search)</p></li>
   * <li><p>public Vector listTags (Vector start, File document,
   *        String search)</p></li>
   * </ul></p>
   * <p>Value of SEARCH used for the following methods where the
   *    goal is to get a list of segments of the documents having
   *    the specified tag structure.</p>
   * <p><ul>
   * <li><p>public Vector parseString (Vector&lt;String&gt; start, 
   *        String document, String 
   *        search )</p></li>
   * <li><p>public Vector parseString (String document, String
   *        search)</p></li>
   * <li><p>public Vector parseString (File document, String search)</p></li>
   * <li><p>public Vector parseString (Vector start,
   *        File document, String search)</p></li>
   * </ul></p>
   */
   int mode;
   int depth;
   String searchString;
   int activeDepth;
   boolean activeSection = false;
   StringBuffer  activeString = null;
   boolean testValue;
   String tags[] = new String[40];
   /**
   * Constructor defining actions taken during parsing.
   *
   * @param type This is an integer value defining the type of 
   * parsing operation to be carried out.
   * @param criteria This String contains the criteria used
   * for carrying out the parsing operation.
   */
   public MyHandler(int type, String criteria)
      {
      mode = type;
      searchString = criteria;
      }
   private void printText (String methodName)
      {
      if (debugLevel > 0)
         {
         System.out.println("*** Depth: "
            .concat(Integer.toString(depth))
            .concat(" Running ").concat(methodName));
         }
      }
   private void printText (String methodName, String itemName)
      {
      if (debugLevel > 0)
         {
         System.out.println("*** Depth: "
            .concat(Integer.toString(depth))
            .concat(" Running ").concat(methodName));
         System.out.println("Item: ".concat(itemName));
         }
      }
   private void printAttributes(Attributes atts)
      {
      for (int i=0; i < atts.getLength(); i++)
         {
         System.out.println(atts.getLocalName(i).concat(" :: ")
             .concat(atts.getValue(i)));
         }
      }
   public void setDocumentLocator(Locator locator)
      { printText ("setDocumentLocation");  }
   /**
   * Called by the parser when the start of the document
   * is encountered.
   * <p>Initializes fields used in parsing document.</p>
   */
   public void startDocument() throws SAXException
      {
      depth = -1 ;
      activeSection = false;
      printText("startDocument");
      if ((debugLevel > 0) && (mode == SEARCH))
         { System.out.println("Search string: ".concat(searchString)); }
      }
   /**
   * Called by the parser when the end of the document
   * is encountered.
   */
   public void endDocument() throws SAXException
      {
      printText("endDocument");
      }
   /**
   * Called when prefix mapping is started.
   * <p>No action is taken for this parser action.</p>
   */
   public void startPrefixMapping (String prefix, String uri)
          throws SAXException
      {
      printText ("startPrefixMapping");
      }
   /**
   * Called when prefix mapping is ended.
   * <p>No action is taken for this parser action.</p>
   */
   public void endPrefixMapping(String prefix) throws SAXException
      {
      printText ("endPrefixMapping");
      }
   /**
   * Called when a start tag is encountered.
   * <p>Together with the actions taken in response to 
   *    the endElement method, this represents the heart
   *    of the parsing operation.</p>
   */
   public void startElement(String namespaceURI, String localName,
          String qualifiedName, Attributes atts) throws SAXException
      {
      StringBuffer tagList = new StringBuffer();
      depth = depth + 1;
      printText ("startElement", localName);
      if (debugLevel > 0)
         {
         if (atts.getLength() > 0)
            { printAttributes(atts); }
         }
      tags[depth] = localName;
      tagList = new StringBuffer();
      for (int i = 0; i <= depth; i++)
         { tagList.append("<".concat(tags[i]).concat(">")); }
      if (debugLevel > 0)
         { System.out.println(tagList); }
      if (mode == SEARCH)
         {
         if (activeSection)
            {
            activeString.append("<".concat(localName).concat(">"));
            }
         if ((new String(tagList)).equals(searchString))
            {
            if (debugLevel > 0)
               {
               System.out.println("Match found");
               }
            activeSection = true;
            activeDepth = depth;
            activeString = new StringBuffer();
            }
         } 
      else if (mode == LISTTAGS)
         { 
         items.add(new String(tagList)); 
         if (debugLevel > 0)
            { System.out.println(tagList); }
         }
      }
   /**
   * Called when an end tag is encountered.
   */
   public void endElement(String namespaceURI, String localName,
           String qualifiedName) throws SAXException
      {
      printText ("endElement", localName);
      depth = depth - 1;
      if ((mode == SEARCH) && (activeSection))
         {
         if (depth < activeDepth)
            {
            items.add(new String(activeString));
            activeString = new StringBuffer();
            activeSection = false;
            }
         else
            {
            activeString.append("</".concat(localName).concat(">"));
            }
         }
      }
   /**
   * This method is called when text is encountered between start
   * and end tags.
   * <p>Multiple calls of this method may be executed to handle the
   *    text between the tags.</p>
   */
   public void characters(char[] text, int start, int length)
           throws SAXException
      {
      String data = new String(text, start, length) 
                  .replaceAll("&", "&amp;")
                  .replaceAll("\\\'", "&apos;")
                  .replaceAll("\\\"", "&quot;")
                  .replaceAll("<", "&lt;")
                  .replaceAll(">", "&gt;");
      if (mode == SEARCH)
         {
         if (activeSection)
            {
            activeString.append(data);
            }
          }
      printText ("characters", data);
      }
   /**
   * Called when ignorable whitespace is encountered.
   * <p>Ignorable whitespace is ignored and no action is
   *    taken.</p>
   */
   public void ignorableWhitespace (char[] text, int start, int length)
           throws SAXException
      {
      printText ("ignorableWhitespace");
      }
   public void processingInstruction(String target, String data)
           throws SAXException
      {
      printText ("processingInstruction");
      }
   public void skippedEntity(String name) throws SAXException
      {
      printText("skippedEntity");
      }
   }
/**
* Determine amount of diagnostic output.
* @param level Amount of diagnostic material to be printed.  0
* is default and results in no diagnostic messages.  Higher values
* produce more diagnostic messages.
*/
public void setDebugLevel (int level)
   { debugLevel = level; }
/** 
This method parses an XML document for strings
* @param start Initial Vector of String objects
* @param document This string contains the document to be parsed
* @param search This string indicates the set of tags to be searched
* for.  If the value is 
* <code>&lt;Envelope&gt;&lt;Body&gt;&lt;FetchHandle&gt;</code>,
* the program will return the contents of all
* <code>FetchHandle</code> tags which are within
* <code>Body</code> tags which are within
* <code>Envelope</code> tags.
* @return Vector of String objects that contains all of the 
* objects from the initial list plus the items found in
* document.
*/
public Vector<String> parseString (Vector<String> start, String document,
        String search)
   {
   return internalParse(start, 
          new InputSource(new StringReader(document)), 
          search, SEARCH);
   }
/** 
This method parses an XML document for strings
* @param document This string contains the document to be parsed
* @param search This string indicates the set of tags to be searched
* for.  If the value is 
* <code>&lt;Envelope&gt;&lt;Body&gt;&lt;FetchHandle&gt;</code>,
* the program will return the contents of all
* <code>FetchHandle</code> tags which are within
* <code>Body</code> tags which are within
* <code>Envelope</code> tags.
* @return Vector of String objects that contains all of the 
* objects found in
* document.
*/
public Vector<String> parseString (String document, String search)
   {
   if (debugLevel > 0)
      {
      System.out.println("*** Starting parseString");
      System.out.println("Search string is ".concat(search));
      System.out.println(document);
      }
   return internalParse (new Vector<String>(), 
          new InputSource(new StringReader(document)), 
          search, SEARCH);
   }
/** 
This method parses an XML document for strings
* @param document This string contains the File object representing
* the file to be parsed.
* @param search This string indicates the set of tags to be searched
* for.  If the value is 
* <code>&lt;Envelope&gt;&lt;Body&gt;&lt;FetchHandle&gt;</code>,
* the program will return the contents of all
* <code>FetchHandle</code> tags which are within
* <code>Body</code> tags which are within
* <code>Envelope</code> tags.
* @return Vector of String objects that contains all of the 
* objects found in
* document.
*/
public Vector<String> parseString (File document, String search)
   {
   InputSource source;
   if (debugLevel > 0)
      {
      System.out.println("*** Starting parseString");
      System.out.println("Search string is ".concat(search));
      System.out.println(document);
      }
   try
      {
      source = new InputSource(new FileInputStream(document));
      }
   catch (FileNotFoundException e) 
      {
      System.out.println("Unable to open file");
      e.printStackTrace();
      return null;
      }
   return internalParse (new Vector<String>(), 
          source, 
          search, SEARCH);
   }
/** 
This method parses an XML document for strings
* @param start Vector containing the String objects at the
* start executing the method
* @param document This string is the File object to be parsed
* @param search This string indicates the set of tags to be searched
* for.  If the value is 
* <code>&lt;Envelope&gt;&lt;Body&gt;&lt;FetchHandle&gt;</code>,
* the program will return the contents of all
* <code>FetchHandle</code> tags which are within
* <code>Body</code> tags which are within
* <code>Envelope</code> tags.
* @return Vector of String objects that contains all of the 
* objects found in
* document.
*/
public Vector<String> parseString (Vector<String> start, 
       File document, String search)
   {
   InputSource source;
   if (debugLevel > 0)
      {
      System.out.println("*** Starting parseString");
      System.out.println("Search string is ".concat(search));
      System.out.println(document);
      }
   try
      {
      source = new InputSource(new FileInputStream(document));
      }
   catch (FileNotFoundException e) 
      {
      System.out.println("Unable to open file");
      e.printStackTrace();
      return null;
      }
   return internalParse (start, 
          source, 
          search, SEARCH);
   }
/**
* List tags contained in an XML document.
*
* <p>This method returns a Vector containing String objects.</p>
* <p>Each String object contains a sequence of tags found
* in the document.</p>
* @param start Initial vector of String objects to which
* items are to be appended.
* @param document Document to be parsed.
* @return Vector of String objects containing list of tag
* combinations
*/
public Vector<String> listTags (Vector<String> start, String document)
   {
   return internalParse(start, 
          new InputSource(new StringReader(document)), 
          (String) null, LISTTAGS);
   }
/**
* List strings contained in document.
*
* This method returns a Vector containing String objects.
* Each String object contains a sequence of tags found
* in the document.
* @param document Document to be parsed.
* @return Vector of String objects containing list of tag
* combinations.
*/
public Vector<String> listTags (String document)
   { 
   return internalParse(new Vector<String>(), 
        new InputSource(new StringReader(document)), (String) null,
                  LISTTAGS);
   }
/** 
This method lists tags contained in an XML document.
* @param document This string contains the File object representing
* the file to be parsed.
* @return Vector of String objects that contains a listing
* of the tags in the document
*/
public Vector<String> listTags (File document)
   {
   InputSource source;
   if (debugLevel > 0)
      {
      System.out.println("*** Starting parseString");
      System.out.println(document);
      }
   try
      {
      source = new InputSource(new FileInputStream(document));
      }
   catch (FileNotFoundException e) 
      {
      System.out.println("Unable to open file");
      e.printStackTrace();
      return null;
      }
   return internalParse (new Vector<String>(), 
          source,
          LISTTAGS);
   }
/** 
* This method lists tags contained in an XML document.
* @param start Vector containing the String objects at the
* start executing the method
* @param document Object containing the file to be parsed
* @return Vector of String objects that contains all of the 
* tags found in the
* document.
*/
public Vector<String> listTags (Vector<String> start, 
       File document)
   {
   InputSource source;
   if (debugLevel > 0)
      {
      System.out.println("*** Starting listTags");
      System.out.println(document);
      }
   try
      {
      source = new InputSource(new FileInputStream(document));
      }
   catch (FileNotFoundException e) 
      {
      System.out.println("Unable to open file");
      e.printStackTrace();
      return null;
      }
   return internalParse (start, 
          source, 
          LISTTAGS);
   }
private Vector<String> internalParse(Vector<String> start,
               InputSource document,
                int mode)
   {
   String search = (String) null;
   return internalParse (start, document, search, mode);
   }
private Vector<String> internalParse(Vector<String> start,
               InputSource document,
               String search, int mode)
   {
   items = new Vector<String>(start);
   XMLReader parser = null;
   try 
      {
      parser = XMLReaderFactory.createXMLReader
         ("org.apache.xerces.parsers.SAXParser");
      }
   catch (SAXException e)
      {
      System.out.println ("SAXException error when creating XMLReader");
      return null;
      }
   parser.setContentHandler(new MyHandler(mode, search));
   try
      {
      /*
      ** The argument for parse method must be of type 
      ** InputSource
      */
      parser.parse(document);
      }
   catch (SAXParseException e)
      {
      System.out.println ("SAXParseException");
      }
   catch (SAXException e)
      {
      System.out.println ("SAXException while parsing");
      }
   catch (IOException e)
      {
      System.out.println ("IOException while parsing");
      }
   return items;
   }
}
