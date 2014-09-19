package bradleyross.j2ee.servlets;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.text.NumberFormat;
import bradleyross.library.helpers.StringHelpers;
import bradleyross.library.database.DatabaseProperties;
import bradleyross.library.helpers.GenericPrinter;
// import bradleyross.library.helpers.StringHelpers;
/**
 * Start of servlet for testing techniques for uploads.
 * <ul>
 * <li><a href="http://www.faqs.org/rfcs/rfc2616.html" target="_blank">
 *     RFC 2616</a>  See section 19.5.1</li>
 * <li><a href="http://www.faqs.org/rfcs/rfc2388.html" target="_blank">
 *     RFC 2388 - Returning Values From Forms: multipart/form-data</a></li>
 * <li><a href="http://www.faqs.org/rfcs/rfc1867.html" target="_blank">
 *     RFC 1867 - Form-based File Upload in HTML</a></li>
 * </ul>
 * <p>The specifications seem to say that the values on the headers may
 *    be tokens or quoted strings.</p>
 *    
 * <p>It may be necessary to expand this algorithm to handle additional 
 *    transfer encoding types.  The two mentioned on the web are base64 and
 *    quoted-printable.</p>
 * <p>MSDN lists the following types:  7bit, 8bit, binary, base64,
 *    quoted-printable, and X-token.</p>
 * @author Bradley Ross
 *
 */
public class UploadServlet extends HttpServlet
{
	/**
	 * Methods for formatting integers.
	 * 
	 * @author Bradley Ross
	 *
	 */
	public class Formatters
	{
		/**
		 * Format as a two digit number with leading zeroes.
		 */
		protected NumberFormat nf2 = null;
		/**
		 * Return the two digit formatter.
		 * @return Formatter
		 */
		public NumberFormat getNf2()
		{
			return nf2;
		}
		/**
		 * Format as a four digit number with leading zeroes.
		 */
		protected NumberFormat nf4 = null;
		/**
		 * Return the four digit formatter.
		 * @return Formatter
		 */
		public NumberFormat getNf4()
		{
			return nf4;
		}
		/**
		 * Format as a five digit number with leading zeroes.
		 */
		protected NumberFormat nf5 = null;
		/**
		 * Return the five digit formatter.
		 * @return Formatter
		 */
		public NumberFormat getNf5()
		{
			return nf5;
		}
		/**
		 * Format as a six digit number with leading zeroes.
		 */
		protected NumberFormat nf6 = null;
		/**
		 * Return the six digit formatter.
		 * @return Formatter
		 */
		public NumberFormat getNf6()
		{
			return nf6;
		}
		/**
		 * Format as a eight digit number with leading zeroes.
		 */
		protected NumberFormat nf8 = null;
		/**
		 * Return the eight digit formatter.
		 * @return Formatter
		 */
		public NumberFormat getNf8()
		{
			return nf8;
		}
		public Formatters()
		{
			/*
			 * 2 digit formatter
			 */
			nf2 = NumberFormat.getIntegerInstance();
			nf2.setMinimumIntegerDigits(2);
			nf2.setMaximumIntegerDigits(2);
			nf2.setGroupingUsed(false);
			nf2.setParseIntegerOnly(true);
			nf2.setMaximumFractionDigits(0);
			/*
			 * 4 digit formatter
			 */
			nf4 = NumberFormat.getIntegerInstance();
			nf4.setMinimumIntegerDigits(4);
			nf4.setMaximumIntegerDigits(4);
			nf4.setGroupingUsed(false);
			nf4.setParseIntegerOnly(true);
			nf4.setMaximumFractionDigits(0);
			/*
			 * 5 digit formatter
			 */
			nf5 = NumberFormat.getIntegerInstance();
			nf5.setMinimumIntegerDigits(5);
			nf5.setMaximumIntegerDigits(5);
			nf5.setGroupingUsed(false);
			nf5.setParseIntegerOnly(true);
			nf5.setMaximumFractionDigits(0);
			/*
			 * 6 digit formatter
			 */
			nf6 = NumberFormat.getIntegerInstance();
			nf6.setMinimumIntegerDigits(6);
			nf6.setMaximumIntegerDigits(6);	
			nf6.setGroupingUsed(false);
			nf6.setParseIntegerOnly(true);
			nf6.setMaximumFractionDigits(0);
			/*
			 * 8 digit formatter
			 */
			nf8 = NumberFormat.getIntegerInstance();
			nf8.setMinimumIntegerDigits(6);
			nf8.setMaximumIntegerDigits(6);	
			nf8.setGroupingUsed(false);
			nf8.setParseIntegerOnly(true);
			nf8.setMaximumFractionDigits(0);
		}
	}
	/**
	 * Contains information relating to a single HTTP request.
	 * 
	 * <p>Since a single object can be used for multiple 
	 *    simultaneous HTTP requests, information about a
	 *    single request can't be placed in the fields relating
	 *    to the instance.</p>
	 * <p>By placing the contents of all of the data fields in this object, the
	 *    methods can have access to all of the fields without interfering with each other.</p>
	 * <p>Each of the input fields is identified by its name in the HTML tag.</p>
	 *    
	 * @author Bradley Ross
	 *
	 */
	public class ThisPage extends bradleyross.j2ee.servlets.ThisPage
	{
		/**
		 * Contains the various elements from the requesting form.
		 */
		protected Hashtable<String, Contents> items = new Hashtable<String, Contents>();
		/** 
		 * True if final part of request has been
		 * processed.
		 * 
		 * @see #getEndOfPacket()
		 * @see #setEndOfPacket(boolean)
		 */
		protected boolean endOfPacket = false;
		/**
		 * Obtain value of ServletInputStream object.
		 * @return ServletInputStream object
		 */
		/* public ServletInputStream getInputStream()
		{
			return input;
		} */

		/**
		 * Sets value of endOfPacket.
		 * 
		 * @param value Value for endOfPacket
		 * @see #endOfPacket
		 */
		public void setEndOfPacket(boolean value)
		{
			endOfPacket = value;
		}
		/**
		 * Gets value of endOfPacket.
		 * 
		 * @return value of endOfPacket
		 * @see #endOfPacket
		 */
		public boolean getEndOfPacket()
		{
			return endOfPacket;
		}
		/**
		 * Boundary marker for multipart form
		 */
		protected String boundary = null;
		/**
		 * Obtains value of boundary marker
		 * @return Value of boundary marker
		 */
		public String getBoundary()
		{
			return boundary;
		}
		/**
		 * Adds an element to the list associated with the HTTP request.
		 * @param value Element
		 */
		public void addElement(Contents value)
		{
			items.put(value.getName(), value);
		}
		/**
		 * Constructor for object, setting the input and output streams so that the 
		 *    element can be processed.
		 *    @param value1 Request object
		 *    @param value2 Response object
		 *    @param value3 Servlet configuration object
 		 *    
	     */
		public ThisPage (HttpServletRequest value1, HttpServletResponse value2, ServletConfig value3)
		throws IOException
		{
			super(value1, value2, value3);
			context = config.getServletContext();
			boundary = extractBoundary(getRequest());
			input = getRequest().getInputStream();

		}
		/**
		 * Return an enumeration containing the names of the elements.
		 * @return Enumeration containing list of names.
		 */
		public Enumeration<String> getPartNames()
		{
			return items.keys();
		}
		/**
		 * Address to be used for redirecting servlet upon completion.
		 * @see HttpServletResponse#sendRedirect(String)
		 * @see #getRedirectAddress()
		 * @see #setRedirectAddress(String)
		 */
		protected String redirectAddress = null;
		/**
		 * Set the URL for redirecting the servlet upon completion.
		 * @param value URL to be used for redirecting servlet
		 * @see #redirectAddress
		 */
		public void setRedirectAddress(String value)
		{
			redirectAddress = value;
		}
		/**
		 * Get the URL to be used for redirecting the servlet upon completion.
		 * @return URL to be used
		 * @see #redirectAddress
		 */
		public String getRedirectAddress()
		{
			return redirectAddress;
		}
		/**
		 * Retrieve the object associated with an element based on the
		 * element name.
		 * @param key Name of the element in the form
		 * @return Object containing the contents of the element, including
		 *         name, MIME type, and contents
		 */
		public Contents getElement(String key)
		{
			return items.get(key);
		}
		/**
		 * Return the contents of one of the elements as
		 * a String object.
		 * 
		 * @param key Key value for element
		 * @return String object containing contents
		 */
		public String getString(String key)
		{
			Contents element = getElement(key);
			if (element == null)
			{
				return null;
			}
			byte contents[] = element.getContents();
			if (contents == null)
			{
				return null;
			}
			else if (contents.length == 0)
			{
				return null;
			}
			String working = new String(contents);
			return working.trim();
		}
		/**
		 * Return the contents of one of the elements as a byte
		 * array.
		 * 
		 * @param key Key value for the element
		 * @return Byte array containing contents of element
		 */
		public byte[] getByteArray(String key)
		{
			Contents element = getElement(key);
			if (element == null)
			{
				return null;
			}
			byte contents[] = element.getContents();
			if (contents == null)
			{
				return null;
			}
			else if (contents.length == 0)
			{
				return null;
			}
			return contents;
		}
	}
	/**
	 * Contains information on the contents of a single part of a 
	 * multipart form.
	 * 
	 * <p>See <a href="http://www.w3.org/TR/html401/interact/forms.html#h-17.13.4.2" target="_blank">
	 *    http://www.w3.org/TR/html401/interact/forms.html#h-17.13.42</a> for a discussion
	 *    of <i>multipart/form-data</i>.</p>
	 * <p>All lines must end with \r\n.  However, binary data may have \r\n in the middle
	 *    of the material.</p>
	 * 
	 * @author Bradley Ross
	 *
	 */
	public class Contents
	{
		/**
		 * Name of the part.
		 */
		protected String name = null;
		/**
		 * Filename associated with the part.
		 */
		protected String filename = null;
		/**
		 * MIME code for the data in the part.
		 */
		protected String mime = null;
		/**
		 * Content transfer encoding method for the
		 * element of the transaction.
		 * 
		 * <p>According to the documentation, there
		 *    are various encoding methods that can be
		 *    used such as base64 and quoted-printable.
		 *    These have not been seen in the browsers
		 *    I have used, but I may have to allow for
		 *    these encoding methods.</p>
		 */
		protected String transferEncoding = null;
		/**
		 * The byte array containing the data for this
		 * part.
		 */
		protected byte value[] = null;
		/**
		 * Obtain the name of the part.
		 * @return Name of part
		 */
		public String getName()
		{
			return name;
		}
		/**
		 * Obtain the filename associated with the part.
		 * 
		 * <p>If the data was not taken from a file, a null
		 *    value is returned.</p>
		 * @return Filename associated with part
		 */
		public String getFilename()
		{
			return filename;
		}
		/**
		 * Obtain mime type for part.
		 * 
		 * <p>MIME types would only be associated with data taken from
		 *    files.</p>
		 *    
		 * @return MIME code
		 */
		public String getMime()
		{
			return mime;
		}
		/**
		 * Obtain the value for content-transfer-encoding.
		 * 
		 * @return Type of encoding
		 */
		public String getTransferEncoding()
		{
			return transferEncoding;
		}
		/**
		 * <p>Obtains the byte array that was associated with this
		 * part of the form.</p>
		 * @return byte array
		 */
		public byte[] getContents()
		{
			return value;
		}
		/**
		 * Obtains the size of the byte array that was associated with
		 * this part of the form.
		 * @return Size of byte array
		 */
		public int getContentsSize()
		{
			if (value == null)
			{ return 0; }
			return value.length;
		}
		/**
		 * Parses header lines in an individual part of a multipart form.
		 * 
		 * <p>This algorithm works if the values for the name and filename
		 *    are surrounded by double quotes, there are no escaped 
		 *    characters with the values, and there are no spaces between the
		 *    start of the word name or filename and the corresponding closing
		 *    quote.  It also assumes that the name parameter comes before
		 *    the filename parameter.  I'm not sure if these assumptions are valid.</p>
		 * @param lineValue Line to be parsed
		 */
		public void parseLine(String lineValue)
		{
			String line = lineValue;
			if (line.endsWith("\r\n"))
			{
				int length = line.length();
				line = line.substring(0, length - 2);
			}
			if (line.toLowerCase().startsWith("content-disposition:")) 
			{
				int start = -1;
				int end = -1;
				start = line.toLowerCase().indexOf("name=\"");
				if (start >= 0)
				{
					start = start + 6;
					end = line.indexOf("\"", start);
					name = line.substring(start, end);
				}
				start = line.toLowerCase().indexOf("filename=\"") ;
				if (start >= 0)
				{
					start = start + 10;
					end = line.indexOf("\"", start);
					filename = line.substring(start, end);
				}		
			}
			else if (line.toLowerCase().startsWith("content-type:"))
			{
				int loc = line.indexOf(":");
				mime = line.substring(loc + 1).trim();
			}	
			else if (line.toLowerCase().startsWith("content-transfer-encoding:"))
			{
				int loc = line.indexOf(":");
				transferEncoding = line.substring(loc + 1).trim();
			}
		}
		/**
		 * Connect the byte array to the object.
		 * @param input Byte array to be attached to object
		 */
		public void setContents(byte[] input)
		{
			value = input;
		}
		public String toString()
		{
			StringBuffer working = new StringBuffer();
			working.append( "Name: " + name + "; Filename: " + filename + "; MIME: " + mime +
			"\r\n\r\n");
			if (value == null)
			{
				return new String(working);
			}
			else if (value.length == 0)
			{
				return new String(working);
			}
			try 
			{
				String addition = new String(value, "ISO8859_1");
				working.append(addition);
			} 
			catch (UnsupportedEncodingException e) 
			{	
				working.append("Unable to represent byte stream");
			}
			return new String(working);
		}
	}
	/**
	 * Dummy id to satisfy serializable interface
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Used to control amount of debugging output.
	 */
	protected static int debugLevel = 0;
	/**
	 * ServletConfig object for servlet.
	 */
	protected ServletConfig config = null;
	/**
	 * Called when object for handling HTTP request is created.
	 */
	public void init(ServletConfig configIn) throws ServletException
	{
		config= configIn;
	}
	/**
	 * Process the call to the servlet.
	 * 
	 * <p>With regard to reading data from the request, a
	 *    {@link javax.servlet.ServletInputStream} object for binary data
	 *    can be obtained by {@link HttpServletRequest#getInputStream()}
	 *    or a {@link java.io.BufferedReader} can be obtained for character data
	 *    by using
	 *    {@link HttpServletRequest#getReader}.</p>
	 * <p>With regard to writing data to the response a
	 *    {@link javax.servlet.ServletOutputStream} object for binary
	 *    data can be obtained using {@link HttpServletResponse#getOutputStream()}
	 *    while a {@link java.io.PrintWriter} object can be obtained using
	 *    {@link HttpServletResponse#getWriter()}.</p>
	 * <p>The {@link java.io.ByteArrayOutputStream} class can be used as a means of collecting
	 *    the bytes contained in the attached file.</p>
	 * <p>It would be desirable to have tests for enctype and method.</p>
	 *    
	 * @param req Request object
	 * @param res Response object
	 * @throws IOException
	 */
	public void service (HttpServletRequest req,
			HttpServletResponse res)
	throws IOException
	{
		ThisPage thisPage = new ThisPage(req, res, config);
		GenericPrinter output = thisPage.getPrinter();
		if (!req.getMethod().equalsIgnoreCase("post"))
		{
			output.println("<html><head>");
			output.println("<title>Must use POST method</title>");
			output.println("</head><body>");
			output.println("<h1>Must use POST method</h1>");
			output.println("<p>Request used " + req.getMethod() + " method</p>");
			output.println("<p>Must use POST method</p>");
			output.println("</body></html>");
			thisPage.sendContents();
			return;
		}
		else if (req.getHeader("content-type") == null)
		{
			output.println("<html><head>");
			output.println("<title>Missing content-type header</title>");
			output.println("</head><body>");
			output.println("<h1>Missing content-type header</h1>");
			output.println("<p>Must use content-type header " 
					+ "to specify multipart/form-data encoding</p>");
			output.println("</body></html>");
			thisPage.sendContents();
			return;
		}
		else if (!req.getHeader("content-type").toLowerCase().startsWith("multipart/form-data"))
		{
			output.println("<html><head>");
			output.println("<title>Must use multipart/form-data encoding</title>");
			output.println("</head><body>");
			output.println("<h1>Must use multipart/form-data encoding</h1>");
			output.println("<p>content-type is " + req.getHeader("content-type") + " </p>");
			output.println("<p>Must use multipart/form-data encoding</p>");
			output.println("</body></html>");
			thisPage.sendContents();
			return;
		}
		String boundary = extractBoundary(req);
		if (boundary == null)
		{
			thisPage.addMessage("Unable to extract boundary value");
			thisPage.addMessage(req.getHeader("content-type"));
			thisPage.errorMessage();
			return;
		}
		int counter = 0;
		byte buffer[] = new byte[4096];
		byte extract[];
		int bytesRead = -1;
		ServletInputStream input = req.getInputStream();
		bytesRead = input.readLine(buffer, 0, buffer.length);
		if (!new String(buffer, 0, bytesRead, "ISO8859_1").startsWith("--" + boundary))
		{
			thisPage.addMessage("Should be separator " + "--" + boundary + " : "
					+" found " + new String(buffer, 0, bytesRead));
			thisPage.errorMessage();
			return;
		}
		while (true)
		{
			counter++;
			Contents part = new Contents();
			thisPage.addMessage("Starting part " + Integer.toString(counter) + " of form");
			while (true)
			{
				
				bytesRead = input.readLine(buffer, 0, buffer.length);
				if (bytesRead < 0)
				{
					thisPage.addMessage("Unexpected end of packet");
					thisPage.errorMessage();
					return;
				}
				else
				{
					String value = new String(buffer, 0, bytesRead, "ISO8859_1");
					if (value.endsWith("\r\n"))
					{
						value = stripEOL(value);
					}
					if (value.length() == 0)
					{
						extract = readPart(input, thisPage, part.getTransferEncoding());
						if (thisPage.getTerminateRequest())
						{
							return;
						}
						part.setContents(extract);
						thisPage.addElement(part);
						break;
					}
					else
					{
						thisPage.addMessage("service method - Parsing line: " + value);
						part.parseLine(value);
					}
				}
			}
			if (thisPage.getEndOfPacket())
			{
				break;
			}
		}
		if (thisPage.getTerminateRequest())
		{
			return;
		}
		starter(thisPage);
		if (thisPage.getTerminateRequest())
		{
			return;
		}
		processor(thisPage);
		if (thisPage.getTerminateRequest())
		{
			return;
		}
		ender(thisPage);
		if (thisPage.getTerminateRequest())
		{
			return;
		}
		if (thisPage.getRedirectAddress() != null)
		{
			thisPage.getResponse().sendRedirect(thisPage.getResponse().encodeRedirectURL(thisPage.getRedirectAddress()));
		}
	}
	/**
	 * Obtain the byte array for this part of the request.
	 * 
	 * <p>It may be necessary to modify this code to handle additional encoding
	 *    types such as Base64.</p>
	 *    
	 * @param input Object from which data is read
	 * @param thisPage Object containing information for this request
	 * @return Byte array for this part of request
	 * @throws IOException
	 */
	protected byte[] readPart(ServletInputStream input, ThisPage thisPage, String encoding)
	throws IOException
	{
		HttpServletRequest req = thisPage.getRequest();
		String boundary = extractBoundary(req);
		byte buffer[] = new byte[4096];
		int bytesRead = -1;
		ByteArrayOutputStream working = null;
		boolean pendingRN = false;
		working = new ByteArrayOutputStream();
		/*
		 * Read body of part
		 */
		while (true)
		{
			bytesRead = input.readLine(buffer, 0, buffer.length);
			if (bytesRead < 0)
			{
				thisPage.addMessage("readPart method - Section of form not properly ended");
				thisPage.errorMessage();
				return null;
			}
			else if (bytesRead == 0)
			{
				thisPage.addMessage("readPart method - Read yielded 0 characters");
				thisPage.errorMessage();
				return null;
			}
			else if (bytesRead == 1 && buffer[0] == '\n')
			{
				if (pendingRN)
				{
					working.write('\r');
					working.write('\n');
					pendingRN = false;
				}
				working.write(buffer[0]);
			}
			else if (new String(buffer, 0, bytesRead).equals("--" + boundary + "\r\n"))
			{
				if (!pendingRN)
				{
					thisPage.addMessage("readPart method - Boundary reached without preceding end of line");
					thisPage.errorMessage();
					return null;
				}
				if (working.size() == 0)
				{
					return null;
				}
				return working.toByteArray();
			}
			else if (new String(buffer, 0, bytesRead).equals("--" + boundary  + "--\r\n"))
			{
				thisPage.setEndOfPacket(true);
				if (!pendingRN)
				{
					thisPage.addMessage("readPart method - Final boundary reached with preceding end of line");
					thisPage.errorMessage();
					return null;
				}
				if (working.size() == 0)
				{
					return null;
				}
				return working.toByteArray();
			}
			else if (buffer[bytesRead - 2]  == '\r' && buffer[bytesRead - 1] == '\n')
			{
				if (pendingRN)
				{
					working.write('\r');
					working.write('\n');
				}
				if (bytesRead > 2)
				{
					working.write(buffer, 0, bytesRead - 2);
				}
				pendingRN = true;
			}
			else if (buffer[bytesRead - 1] == '\r')
			{
				if (pendingRN)
				{
					working.write('\r');
					working.write('\n');
					pendingRN = false;
				}
				if (bytesRead > 1)
				{
					working.write(buffer,  0, bytesRead - 1);
				}
				int nextChar = input.read();
				if (nextChar == '\n')
				{
					pendingRN = true;
				}
				else
				{
					working.write('\r');
					working.write(nextChar);
				}
			}
			else
			{
				if (pendingRN)
				{
					working.write('\r');
					working.write('\n');
					pendingRN=false;
				}
				working.write(buffer, 0, bytesRead);
			}
		}
	}
	/**
	 * Strips end of line characters from a character string.
	 * 
	 * @param input String to be processed
	 * @return String with EOL characters stripped off
	 */
	protected String stripEOL(String input)
	{
		if (input == null)
		{
			return (String) null;
		}
		String working = input;
		if (working.endsWith("\r\n") || working.endsWith("\n\r"))
		{
			working = working.substring(0, working.length() - 2);
		}
		else if (working.endsWith("\r") || working.endsWith("\n"))
		{
			working = working.substring(0, working.length() - 1);
		}
		return working;
	}
	/**
	 * Extracts the value of the boundary string from the header.
	 * @param req Request object
	 * @return Boundary string
	 */
	public String extractBoundary(HttpServletRequest req)
	{
		String working = null;
		working = req.getHeader("content-type");
		if (working == null)
		{
			return null;
		}
		int loc = working.indexOf("boundary=");
		if ( loc < 0)
		{
			return null;
		}
		else
		{
			working = working.substring(loc + 9);
		}
		loc = working.indexOf(";");
		if (loc == 0)
		{
			return null;
		}
		else if (loc > 0)
		{
			working = working.substring(0, loc);
		}
		return working;
	}
	/**
	 * Utility method for moving the contents of one of the parts into
	 * a prepared statement as a string object.
	 * @param data Data connection object.
	 * @param stmt Prepared statement
	 * @param thisPage Object representing this request
	 * @param position Position of the value in the prepared statement's argument list
	 * @param element Name of the part in the multi-part form
	 * @param convertFlag True means that value should be converted to upper case
	 * @param SQLType Type of SQL column (Types.CHAR or Types.VARCHAR)
	 * @throws SQLException
	 */
	public void loadByteArray(DatabaseProperties data, PreparedStatement stmt, ThisPage thisPage,
			int position, String element, boolean convertFlag, int SQLType) throws SQLException
	{
		byte contents[] = null;
		Contents portion = thisPage.getElement(element);
		if (portion == null)
		{
			stmt.setNull(position, SQLType);
			return;
		}
		contents = thisPage.getElement(element).getContents();
		if (contents == null)
		{
			stmt.setNull(position, SQLType); 
		}
		else if (contents.length == 0)
		{
			stmt.setNull(position, SQLType);
		}
		else 
		{
			String working = new String(contents).trim();
			if (convertFlag)
			{
				working = working.toUpperCase();
			}
			if (working.length() == 0)
			{
				stmt.setNull(position, SQLType);
			}
			else
			{
				stmt.setString(position, working);	
			}
		}
	}
	/**
	 * Utility method for moving the contents of one of the parts into
	 * a prepared statement as a string object.
	 * @param data Data connection object.
	 * @param stmt Prepared statement
	 * @param thisPage Object representing this request
	 * @param position Position of the value in the prepared statement's argument list
	 * @param element Name of the part in the multi-part form
	 * @param convertFlag True means that value should be converted to upper case
	 * @throws SQLException
	 */
	public void loadByteArray(DatabaseProperties data, PreparedStatement stmt, ThisPage thisPage,
			int position, String element, boolean convertFlag) throws SQLException
	{
		loadByteArray(data, stmt, thisPage, position, element, convertFlag, Types.VARCHAR);
	}
	/**
	 * Utility method for moving the contents of one of the parts into
	 * a prepared statement as a string object.
	 * @param data Data connection object.
	 * @param stmt Prepared statement
	 * @param thisPage Object representing this request
	 * @param position Position of the value in the prepared statement's argument list
	 * @param element Name of the part in the multi-part form
	 * @throws SQLException
	 */
	public void loadByteArray(DatabaseProperties data, PreparedStatement stmt, ThisPage thisPage,
			int position, String element) throws SQLException
	{
		loadByteArray(data, stmt, thisPage, position, element, false, Types.VARCHAR);
	}
	/**
	 * Perform common operations on the contents of the
	 * request packet before running the main program.
	 * 
	 * <p>The intent is to override this method in a subclass
	 *    that will then be subclassed to handle the servlets
	 *    for the various web pages.</p>
	 *    
	 * @param thisPage Information on this HTTP request
	 */
	protected void starter(ThisPage thisPage) throws IOException
	{
		;
	}
	/**
	 * Perform common operations on the contents of the request packet
	 * to be carried out after running the main program.
	 * 
	 * @param thisPage Information on this HTTP transaction
	 */
	protected void ender(ThisPage thisPage) throws IOException
	{ 
		;
	}
	/**
	 * This method generates the web page based on the contents
	 * of the request packet.
	 * 
	 * <p>This method will be overwritten to enable various actions
	 *    to take place as the information is uploaded.</p>
	 * <p>For the result of File requests, write the contents to a file.</p>
	 * <p>I am having a problem with the loading of binary files.</p>
	 * @param thisPage Information on this HTTP request
	 */
	protected void processor(ThisPage thisPage) throws IOException
	{
		Enumeration<String> keys = thisPage.getPartNames();
		HttpServletRequest req = thisPage.getRequest();
		ServletConfig config = thisPage.getConfig();
		String targetDirectory = config.getInitParameter("directory");
		GenericPrinter output = thisPage.getPrinter();
		output.println("<html><head>");
		output.println("<title>Dummy Upload Program</title>");
		output.println("</head><body>");
		output.println("<p>It is assumed that the processor method of the ");
		output.println("bradleyross.library.servlets.UploadServlet class will ");
		output.println("be overridden to provide the desired function.  This ");
		output.println("sample version is designed for testing the applications ");
		output.println("and to allow demonstration of the capabilities.</p>");
		output.println("<p>Target directory for upload tests is " + targetDirectory + "</p>");
		output.println("<h2>Headers</h2>");
		output.println("<table border=\"1\">");
		Enumeration<?> list1 = req.getHeaderNames();
		while (list1.hasMoreElements())
		{
			String name = (String) list1.nextElement();
			Enumeration<?> list2 = req.getHeaders(name);
			while (list2.hasMoreElements())
			{
				String value = (String) list2.nextElement();
				output.println("<tr><td>" + name + "</td><td>" + value + "</td></tr>");
			}
		}
		output.println("</table>");
		output.println("<h2>Parts of form</h2>");
		output.println("<p>The following are the parts of the multipart form</p>");		
		output.println("<ul>");
		while (keys.hasMoreElements())
		{
			String name = keys.nextElement();
			String mime = thisPage.getElement(name).getMime();
			String fileName = thisPage.getElement(name).getFilename();
			String encoding = thisPage.getElement(name).getTransferEncoding();
			output.println("<li><p>" + name + "</p>");
			if (fileName == null)
			{
				output.println("<p>Filename not specified</p>");
			}
			else
			{
				output.println("<p>Filename is " + fileName + "</p>");
			}
			if (mime == null)
			{
				output.println("<p>Content type not specified</p>");
				mime = new String();
			}
			else
			{
				output.println("<p>Content-type: " + mime + "</p>");
			}
			if (encoding == null)
			{
				output.println("<p>Transfer encoding not specified</p>");
			}
			else
			{
			output.println("<p>Content-transfer-encoding: " + encoding + "</p>");
			}
			if (mime.toUpperCase().startsWith("TEXT"))
			{
				output.println("<p>" + StringHelpers.escapeHTML(thisPage.getElement(name).toString())
					+ "</p></li>");
			}
			output.println("<p>Size of contents: " + Integer.toString(thisPage.getElement(name).getContentsSize()));
			if (fileName != null && targetDirectory != null)
			{
				try
				{
					boolean validEntry = true;
					File outputFile = null;
					if (targetDirectory.length() == 0 || fileName.length() == 0)
					{
						validEntry = false;
					}
					if (validEntry)
					{
						outputFile = new File (targetDirectory, fileName);
						output.println("<p>Name of file is " + StringHelpers.escapeHTML(outputFile.getCanonicalPath()) + "</p>");
					}
					if (!validEntry)
					{ ; }
					else if (outputFile == null)
					{
						output.println("<p>Unable to open output file</p>");
					}
					else
					{
						FileOutputStream outputStream = new FileOutputStream(outputFile);
						byte transfer[] = thisPage.getElement(name).getContents();
						if (transfer == null)
						{
							output.println("<p>Unable to get file contents</p>");
						}
						else 
						{
							outputStream.write(thisPage.getElement(name).getContents());
						}
						outputStream.close();	
					}
				}
				catch (IOException e)
				{
					output.println("<p>Error while writing file</p>");
					output.println(StringHelpers.escapeHTML("<p>" + e.getClass().getName() + " " +
							e.getMessage() + "</p>"));
				}
			}
			else
			{ ;	}	
		}
		output.println("</ul>");
		output.println("<h2>Messages</h2>");
		output.println("<p>These messages are normally printed only if an error occurs during the processing ");
		output.println("of the HTTP transaction.  They are included here to test the behavior of the servlet.</p><ol>");
		Vector<String> messages = thisPage.getMessageList();
		messages.trimToSize();
		for (int i = 0; i < messages.size(); i++)
		{
			output.println("<li><p>" + StringHelpers.escapeHTML(messages.elementAt(i)) + "</p></li>");
		}
		output.println("</ol>");
		output.println("</body></html>");
		thisPage.sendContents();
	}
}
