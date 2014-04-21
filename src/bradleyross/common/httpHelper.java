package bradleyross.common;
import java.io.*;
import java.net.*;
/**
 * Provide a set of methods that will help in processing
 * the contents of web pages.
 * <p>Some methods need to be added for escaping and unescaping
 *    character strings.  For text that appears in web pages, this
 *    would include the handling of ampersands, less than, and greater
 *    than symbols</p>
 * <p>It may also be necessary to include something for the handling of material
 *    inside quoted strings.  (both single and double quotes)</p>
 * 
 * @author Bradley Ross
 */
public class httpHelper
{
	/** 
	 * Controls amount of diagnostic output.
	 * 
	 * @see #getDebugLevel()
	 * @see #setDebugLevel(int)
	 */
	protected int debugLevel = 0;
	/**
	 * Getter for debugLevel.
	 * 
	 * @return Value of debugLevel
	 * 
	 * @see #debugLevel
	 */
	public int getDebugLevel()
	{ return debugLevel; }
	/**
	 * Setter for debugLevel.
	 * @param value Value for debugLevel
	 * @see #debugLevel
	 */
	public void setDebugLevel(int value)
	{ debugLevel = value; }
	/**
	 * Copy the contents of a web page into a String object
	 * <p>It may be advantageous to have these routines able to
	 *    time out in the same way as the methods in
	 *    {@link bradleyross.library.helpers.FileHelpers}.
	 *    The first thing to do would be to use the set timeout
	 *    methods for Socket.</p>
	 * @param host Domain name or IP address of web server (Domain name only. 
	 * e.g. www.cnn.com)
	 * @param port Port number for web server
	 * @param fileName Directory and name for web page (No leading slash.
	 *   e.g. index.html)
	 * @see java.net.Socket
	 */
	public String readHttpPage(String host, int port, String fileName)
	throws java.io.IOException
	{
		String newLine;
		Socket sock;
		long contentLength = 0;
		long charactersRead = 0;
		StringBuffer body = new StringBuffer();
		char buffer[] = new char[1024];
		try 
		{
			sock = new Socket(host, port);
			sock.setSoTimeout(10000);
			PrintWriter output = new PrintWriter(sock.getOutputStream());
			BufferedReader input = 
				new BufferedReader(new InputStreamReader(sock.getInputStream()));
			String location = "/" + fileName;
			output.print("GET " + location + " HTTP/1.1 \r\n");
			output.print("HOST: " + host);
			output.print("\r\n");
			output.print("\r\n");
			output.flush();
			/*
			 * Read headers
			 *
			 * The headers have a header name, a colon character,
			 * and then the value for that header.  The only
			 * header affecting the treatment of the page is
			 * content-length.
			 */
			while ((newLine = input.readLine()) != null)
			{
				if (newLine.trim().length() == 0) {break; }
				if ( newLine.indexOf(":") > 0)
				{
					String type = newLine.substring(0, newLine.indexOf(":")).trim();
					String value = newLine.substring(newLine.indexOf(":") + 1).trim();
					if (type.equalsIgnoreCase("content-length"))
					{
						try
						{ contentLength = Long.parseLong(value); }
						catch (Exception e) { }
					}
				}
			}
			/* 
			 * Read body of transaction response
			 */
			if (contentLength <= (long) 0)
			{
				while ((newLine = input.readLine()) != null)
				{ body = body.append(newLine + "\r\n"); }
			}
			else
			{
				StringBuffer buildUp = new StringBuffer();
				int packetLength;
				charactersRead = (long) 0;
				while ((packetLength = input.read(buffer, 0, 1024)) >= 0)
				{
					charactersRead += (long) packetLength;
					buildUp.append(new String(buffer, 0, packetLength));
					if (charactersRead >= contentLength) {break;}
				}
				body = buildUp;
			}
			sock.close();
			if (debugLevel > 0)
			{System.out.print(" " + Long.toString(charactersRead) + 
					" characters read "); }
		} /*  End of try block */
		catch (IOException e)
		{
		    System.out.println("Exception encountered in readHttpPage");
		    System.out.println(e.getClass().getName() + " : " +
		            e.getMessage());
			e.printStackTrace(System.out);
			throw new java.io.IOException("Unable to read HTTP page");
		}
		return new String(body);
	} /* End of method readHttpPage */
} /* End of class httpHelper */

