package bradleyross.library.helpers;
import java.net.URL;
import java.net.URLConnection;
import java.io.File;
import java.io.FileReader;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.List;
import java.io.FileOutputStream;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import java.awt.Image;
import java.awt.Graphics;
/**
 * Methods to read and write files designed to be
 * used within other programs.
 * <p>Note: {@link java.net.SocketTimeoutException} is a subclass of
 *    {@link java.io.IOException}.</p>
 * <p>Note: Using the {@link URLConnection#setReadTimeout(int) } and
 *    {@link URLConnection#setConnectTimeout(int) } methods should
 *    prevent the methods in this class from hanging.</p>
 * <p>It looks like the best approach is to rerun the process if an
 *    error occurs.  Start with small timeout values and increase them
 *    on each iteration.</p>
 * @author Bradley Ross
 */
public class FileHelpers
{
	/**
	 * Since all of the methods and fields are static, the constructor
	 * should never be used.
	 */
	protected FileHelpers() { ; }
	/**
	 * Number of milliseconds to wait for a read or
	 * socket operation.
	 * @see #getTimeout()
	 * @see #setTimeout(int)
	 * @see URLConnection#setReadTimeout(int)
	 * @see URLConnection#setConnectTimeout(int)
	 * @deprecated
	 */
	protected static int timeout = 10000;
	/**
	 * Length of time allowed for connect operations.
	 * 
	 * @see URLConnection#setConnectTimeout(int)
	 * @see #setConnectTimeout(int)
	 * @see #getConnectTimeout()
	 */
	protected static int connectTimeout = 500;
	/**
	 * Length of time allowed for read operations.
	 * 
	 * @see URLConnection#setReadTimeout(int)
	 * @see #setReadTimeout(int)
	 * @see #getReadTimeout()
	 */
	protected static int readTimeout = 500;
	/**
	 * Timeout for entire transfer in milliseconds.
	 * @see #getCombinedTime()
	 * @see #setConnectTimeout(int)
	 */
	protected static long combinedTime = 80000;
	/**
	 * Getter for connectTimeout.
	 * 
	 * @see #connectTimeout
	 * @return Timeout value for connect operations
	 */
	public static int getConnectTimeout()
	{ return connectTimeout; }
	/**
	 * Setter for connectTimeout.
	 * 
	 * @see #connectTimeout
	 * @param value Value to be used for timeout for socket operations.
	 */
	public static void setConnectTimeout(int value)
	{ connectTimeout = value; }
	/**
	 * Getter for readTimeout.
	 * 
	 * @see #readTimeout
	 * @return Timeout value for socket operations
	 */
	public static int getReadTimeout()
	{ return readTimeout; }
	/**
	 * Setter for readTimeout.
	 * 
	 * @see #readTimeout
	 * @param value Value to be used for timeout for read operations
	 */
	public static void setReadTimeout(int value)
	{ readTimeout = value; }
	/** 
	 * Getter for combinedTime
	 * @return Milliseconds allowed for entire file transfer
	 * @see #combinedTime
	 */
	public static long getCombinedTime()
	{ return combinedTime; }
	/**
	 * Setter for combinedTimeout
	 * @param value Number of milliseconds to be allowed for the entire file transfer
	 * @see #combinedTime
	 */
	public static void setCombinedTime(long value)
	{ combinedTime = value; }
	/** 
	 * Setter for timeout.
	 * 
	 * @param value Milliseconds to wait for read or socket operation
	 * @see #timeout
	 * @deprecated
	 */
	public void setTimeout(int value)
	{ timeout = value; }
	/**
	 * Getter for timeout
	 * 
	 * @return Milliseconds to wait for read or socket operation
	 * @see #timeout
	 * @deprecated
	 */
	public int getTimeout()
	{ return timeout; }
	/** 
	 * Determines quantity of diagnostic output to be generated. 
	 * @see #getDebugLevel()
	 * @see #setDebugLevel(int)
	 */
	protected static int debugLevel = 0;
	/**
	 * Getter for debugLevel
	 * @see #debugLevel
	 * @return Value of debugLevel
	 */
	public static int getDebugLevel()
	{ return debugLevel; }
	/**
	 * Setter for debugLevel
	 * @see #debugLevel
	 * @param value to be used for debugLevel
	 */
	public static void setDebugLevel(int value)
	{ debugLevel = value; }
	/**
	 * Calculates 2 to the power i as a helper in determining timings
	 * for some of the operations.
	 * @param i Power to which 2 is raised	
	 * @return  2 to the power i
	 */
	protected static int power(int i)
	{
		if (i <= 0) { return 1; }
		else if (i == 1) { return 2; }
		else if (i == 2) { return 4; }
		else if (i > 32) { return Integer.MAX_VALUE; }
		else
		{
			int working = 2;
			for (int i2 = 1; i2 < i; i2++)
			{ working = working * 2; }
			return working;
		}
	}
	/**
	 * Reads the contents of a text file into a String object
	 * given the name of the file.
	 * @param name  File name
	 * @return Contents of file
	 */
	public static String readTextFile(String name)
	throws java.io.IOException
	{
		File inputFile = new File(name);
		if (!inputFile.exists())
		{
			return (String) null;
		}
		FileReader reader = null;
		StringBuffer working = new StringBuffer();
		char[] buffer = new char[4096];
		try
		{
			reader = new FileReader(inputFile);
			while (true)
			{
				int length = reader.read(buffer, 0, buffer.length);
				if (length < 0) { break; }
				working.append(buffer, 0, length);
			}
			reader.close();
		}
		catch (java.io.IOException e)
		{
			if (Thread.currentThread().isInterrupted()) 
			{ 
				System.out.println("*****Interrupted");
				Thread.yield(); 
				return (String) null; 
			}
			System.out.println("Exception encountered " + e.getClass().getName());
			System.out.println(e.getMessage());
			e.printStackTrace(System.out);
			/*
			 * Throwing the exception to the calling program added
			 * 06-November-2008.
			 */
			throw e;
		}
		return new String(working);
	}
	/**
	 * Read the contents referenced by a URL into a String
	 * object.
	 * <p>The java.net.URL object required as a parameter is
	 *    created by a call of the type
	 *    <code>java.net.URL url =
	 *    new java.net.URL(urlString)</code> where urlString
	 *    is a String object containing the URL identifier.</p>
	 * <p>This should only be used for text files.  Results are
	 *    unpredictable and probably undesirable when working
	 *    with binary files.</p>
	 *
	 * <ul>
	 * <li><p>ftp:  <code>ftp://userid:password@host:port/file</code></p>
	 *     </li>
	 * <li><p>file:  <code>file:// (file name starting with / indicating root)</code></p>
	 *     </li>
	 * </ul>
	 * @param address URL of desired contents
	 * @return String
	 */
	public static String readTextFile(java.net.URL address)
	throws java.io.IOException
	{
		StringBuffer working = new StringBuffer();
		long startTime = System.currentTimeMillis();
		long currentTime = System.currentTimeMillis();
		long endTime = System.currentTimeMillis() + combinedTime;
		int totalLength = 0;
		boolean success = false;
		int tries = 0;
		for (int i = 0; i < 4; i++)
		{
			tries++;
			try
			{
				URLConnection connection = null;
				connection = address.openConnection();
				connection.setConnectTimeout(connectTimeout * power(i));
				connection.setReadTimeout(readTimeout * power(i));
				connection.connect();
				java.io.FilterInputStream contents = 
					(java.io.FilterInputStream) connection.getContent();
				if (debugLevel > 0)
				{
					System.out.println("Class is " + contents.getClass().getName());
					System.out.println("Superclass is " +
							contents.getClass().getSuperclass().getName());
				}
				byte[] line = new byte[500];
				java.io.BufferedInputStream buffer = new 
				java.io.BufferedInputStream(contents);
				int charsRead;
				/*  When obtaining the contents, the class of contents is
				 *   sun.net.www.content.text.PlainTextInputStream
				 *   whose superclass is
				 *   java.io.FilterInputStream
				 */
				while (true)
				{
					charsRead = buffer.read(line, 0, 490);
					if (charsRead < 0) { break; }
					// System.out.println("** " + Integer.toString(charsRead) + " **");
					working.append(new String(line, 0, charsRead));
					totalLength = totalLength + charsRead;
					currentTime = System.currentTimeMillis();
					if (currentTime > endTime)
					{ 
						throw new java.io.IOException("Time limit of " +
								Long.toString(combinedTime) + " milliseconds exceeded" +
						" readTextFile(URL)"); 
					}
				}
				if (currentTime - startTime > 30000l)
				{ 
					System.out.println(Long.toString(currentTime - startTime) + " milliseconds for " +
							Integer.toString(totalLength) + " bytes"); 
				}
				if (debugLevel > 0)
				{ 
					System.out.print(" " + Integer.toString(totalLength) +
					" bytes "); 
				}
				success = true;
			}
			catch (java.io.IOException e)
			{
				if (Thread.currentThread().isInterrupted()) 
				{ 
					System.out.println("***** Interrupted");
					Thread.yield(); 
					return (String) null; 
				}
				System.out.println("Exception encountered in readTextFile " +
						"(Attempt " + Integer.toString(tries) + ") ");
				System.out.println("   " + e.getClass().getName() + " " + e.getMessage());
				if (tries > 4) { throw e; }
			}
			if (success) {break; }
		}
		if (success && tries > 1)
		{ System.out.println("Success after " + Integer.toString(tries) + " tries"); }
		return new String(working);
	}
	/**
	 * Write the contents of a byte array to a URL as a binary data stream.
	 * <p>It appears that you can write to protocol ftp but not to protocol
	 *    file.</p>
	 * @param address URL of destination
	 * @param data Byte array to be sent
	 * @throws java.io.IOException
	 * @throws java.net.SocketTimeoutException
	 * @see java.net.URLConnection
	 */
	public static void sendBytes (java.net.URL address, byte[] data) 
	throws java.io.IOException
	{
		int tries = 0;
		boolean success = false;
		for (int i = 0; i < 4; i++)
		{
			URLConnection connection = null;
			java.io.OutputStream output = null;
			try
			{
				connection = address.openConnection();
				connection.setDoInput(false);
				connection.setDoOutput(true);
				connection.setReadTimeout(readTimeout * power(i));
				connection.setConnectTimeout(connectTimeout * power(i));
				showProperties(connection);
				connection.connect();
				output = connection.getOutputStream();
				output.write(data);
				output.close();
				success = true;
			}
			catch (java.io.IOException e)
			{
				if (Thread.currentThread().isInterrupted()) 
				{ 
					System.out.println("***** Interrupted");
					Thread.yield(); 
					return; 
				}
				System.out.println("Error in sendBytes: " + e.getClass().getName() + " " +
						e.getMessage());
			}
			tries++;
			if (success) { break; }
		}
		if (!success)
		{
			System.out.println("Failure after " + Integer.toString(tries) + " tries");
			throw new IOException("Failure after " + Integer.toString(tries) + " tries");
		}
		if (success && tries > 1)
		{ 
			System.out.println("Success after " + Integer.toString(tries) + " tries"); 
		}
	}
	/**
	 * Write the contents of a file to a URL, passing the data as
	 * a byte stream (binary data).
	 * @param address URL of destination
	 * @param data File object containing data to be sent
	 * @throws java.io.IOException
	 */
	public static void sendBytes (java.net.URL address, java.io.File data) 
	throws java.io.IOException
	{
		int tries = 0;
		boolean success = false;
		for (int i = 0; i < 4; i++)
		{
			long startTime = System.currentTimeMillis();
			long currentTime = startTime;
			long endTime = startTime + combinedTime;
			URLConnection connection = null;
			java.io.FileInputStream input = null;
			java.io.OutputStream output = null;
			byte[] buffer = new byte[32768];
			int byteCounter = 0;
			int readCounter = 0;
			try
			{
				connection = address.openConnection();
				connection.setDoInput(false);
				connection.setDoOutput(true);
				/*
				 * I changed the multiplier from (i+1) to
				 * power(i)
				 */
				connection.setReadTimeout(readTimeout * power(i));
				connection.setConnectTimeout(connectTimeout * power(i));
				showProperties(connection);
				connection.connect();
				output = connection.getOutputStream();
				input = new java.io.FileInputStream(data);
				while (true)
				{
					int length = input.read(buffer);
					if (length < 0) { break; }
					output.write(buffer, 0, length);
					byteCounter = byteCounter + length;
					readCounter++;
					// Thread.yield();
					if (System.currentTimeMillis() > endTime)
					{ 
						throw new java.io.IOException("Time limit of " +
								Long.toString(combinedTime) + 
						" milliseconds exceeded in sendBytes(URL, File)"); 
					}
				}
				currentTime = System.currentTimeMillis();
				if (currentTime - startTime > 30000l)
				{
					System.out.println("sendBytes(URL, File) required " + Long.toString(currentTime - startTime) +
							" milliseconds to process " + data.getCanonicalPath() + " : " +
							Integer.toString(byteCounter) + " bytes in " + Integer.toString(readCounter) +
					" reads");
					String name = address.getFile();
					/*
					 * It is necessary to remove the portion of the name following the semicolon since the
					 * URL methods include the type designation (;type=i or ;type=a) with the name portion of the
					 * URL.
					 */
					if (name.indexOf(";") > 0)
					{
						name = name.substring(0, name.indexOf(";"));
					}
					System.out.println("Guessed MIME type for " + name +
							" is " + URLConnection.guessContentTypeFromName(name));
				}
				input.close();
				output.close();
				success = true;
			}
			catch (java.io.IOException e)
			{
				if (Thread.interrupted()) { Thread.yield(); return; }
				System.out.println("Error in sendBytes: " + e.getClass().getName() + " " +
						e.getMessage());
			}
			tries++;
			if (success) { break; }
		}
		if (!success)
		{ 
			System.out.println("Failure after " + Integer.toString(tries) + " tries");
			throw new IOException("Failure after " + Integer.toString(tries) + " tries");
		}
		if (success && tries > 1)
		{ 
			System.out.println("Success after " + Integer.toString(tries) + " tries");
		}
	}
	/**
	 * Send the contents of an InputStream to a File.
	 * @param output File object for output.
	 * @param input InputStream for input.
	 */
	public static void sendBytes (File output, InputStream input)
	{
		byte buffer[] = new byte[65536];
		int charsRead = 0;
		int totalChars = 0;
		try
		{
			FileOutputStream sender = new FileOutputStream(output);
			while (true)
			{
				charsRead = input.read(buffer);

				if (charsRead < 0)
				{
					break;
				}
				totalChars = totalChars + charsRead;
				sender.write (buffer, 0, charsRead);
				System.out.println(Integer.toString(charsRead) + " " + Integer.toString(totalChars));
			}
			sender.close();
		}
		catch (FileNotFoundException e)
		{
			System.out.println(e.getClass().getName() + " " + e.getMessage());
		}
		catch (IOException e)
		{
			System.out.println(e.getClass().getName() + " " + e.getMessage());
		}
	}
	/**
	 * Read the contents of a URL into a file, passing the
	 * data as  byte (binary) stream.
	 * 
	 * <p>In case of failure, the algorithm retries with
	 *    increased values of connectTimeout and
	 *    readTimeout.</p>
	 * <p>This modification may eventually be added to the
	 *    other methods.</p>
	 * @param url Location of data to be read
	 * @param file File where data is to be written
	 * @see URLConnection#setReadTimeout(int)
	 * @see URLConnection#setConnectTimeout(int)
	 * @throws java.io.IOException
	 */
	public static void getBytes(URL url, File file)
	throws java.io.IOException
	{
		boolean success = false;
		int tries = 0;
		int totalLength = 0;
		URLConnection connection = null;
		java.io.FileOutputStream output = null;
		java.io.InputStream input = null;
		byte[] buffer = new byte[32768];
		long startTime = System.currentTimeMillis();
		long endTime = startTime + combinedTime;
		long currentTime = System.currentTimeMillis();
		for (int i = 0; i < 4; i++) 
		{
			if (output != null)
			{
				output.close();
				output = null;
			}
			if (input != null)
			{
				input = null; 
			}
			if (connection != null)
			{
				connection = null;
			}
			output = null;
			input = null;
			try {
				connection = url.openConnection();
				connection.setDoInput(true);
				connection.setDoOutput(false);
				connection.setReadTimeout(readTimeout * power(i));
				connection.setConnectTimeout(connectTimeout * power(i));
				showProperties(connection);
				connection.connect();
				input = connection.getInputStream();
				while (true) 
				{
					/* 
					 * The read statement blocks until there
					 * are characters available to be read.
					 * 
					 * I thought that using 
					 * URLConnection.setReadTimeout
					 * would result in an exception if the
					 * time out was exceeded but it doesn't seem
					 * to be happening.
					 */
					int length = input.read(buffer);
					if (length < 0) 
					{
						break;
					}
					if (output == null) {
						output = new java.io.FileOutputStream(file);
					}
					output.write(buffer, 0, length);
					totalLength = totalLength + length;
					Thread.yield();
					if (debugLevel > 0) 
					{
						System.out.print("*");
					}
					currentTime = System.currentTimeMillis();
					if (currentTime > endTime) {
						throw new java.io.IOException("Time limit of "
								+ Long.toString(combinedTime)
								+ " milliseconds exceeded "
								+ "in getBytes(URL,File)");
					}
				} /*  End of while loop */

				input.close();
				if (output != null) 
				{
					output.close();	
				}
				success = true;
			} 
			catch (java.io.IOException e) 
			{
				if (Thread.currentThread().isInterrupted()) 
				{
					System.out.println("***** Interrupted");
					Thread.yield();
					return;
				}
				if (i == 3 || debugLevel > 0)
				{
					System.out.println("Error in getBytes: "
							+ e.getClass().getName() + " " + e.getMessage());
					System.out.println("     Number of bytes moved: "
							+ Integer.toString(totalLength));
				}
				if (i == 3)
				{
					throw e;
				}
			}
			tries = i + 1;
			if (success) { break; }
		}
		if (!success)
		{
			System.out.println("Failure after " + Integer.toString(tries) + " tries"); 
		}
		else if (success && tries > 1 && debugLevel > 0)
		{
			System.out.println("Success after " + Integer.toString(tries) + " tries");
		}
		if (debugLevel > 0)
		{ System.out.print("      " + Integer.toString(totalLength) + " bytes "); }
		if (currentTime - startTime > 30000)
		{
			System.out.println(Long.toString(currentTime - startTime) +
					" milliseconds for " + Integer.toString(totalLength) +
			" bytes");
		}
	}
	/**
	 * Read the contents of a  a file and then sends
	 * the data to a OutputStream object.
	 * 
	 * @param input File containing data to be written
	 * @param output OutputStream object where data is to be written
	 * @throws java.io.IOException
	 * @see File
	 */
	public static void getBytes(File input, OutputStream output)
	throws java.io.IOException
	{
		FileInputStream reader = new FileInputStream(input);
		int totalLength = 0;
		byte[] buffer = new byte[4096];
		long startTime = System.currentTimeMillis();
		long currentTime = startTime;
		long endTime = startTime + combinedTime;
		if (debugLevel > 1)
		{
			System.out.println("getBytes(File,OutputStream: " + input.getName() + " exists: " +
					Boolean.toString(input.exists()));
			System.out.println("getBytes(File,OutputStream): ");
		}
		try
		{
			while (true)
			{
				/* 
				 * The read statement blocks until there
				 * are characters available to be read.
				 * 
				 * I thought that using 
				 * URLConnection.setReadTimeout
				 * would result in an exception if the
				 * time out was exceeded but it doesn't seem
				 * to be happening.
				 */
				int length = reader.read(buffer);
				if (debugLevel > 1)
				{
					System.out.println(Integer.toString(length) + " bytes read");
				}
				if (length < 0) { break; }

				output.write(buffer, 0, length);
				totalLength = totalLength + length;
				Thread.yield();
				if (debugLevel > 0) { System.out.print("*"); }
				currentTime = System.currentTimeMillis();
				if (currentTime > endTime)
				{ 
					reader.close();
					throw new java.io.IOException("Time limit of " +
							Long.toString(combinedTime) + 
					" milliseconds exceeded in getBytes(File, OutputStream)"); 
				}
			}
			if (debugLevel > 0)
			{ System.out.println("Total of " + Integer.toString(totalLength) + " bytes processed "); }
			if (currentTime - startTime > 30000)
			{
				System.out.println(Long.toString(currentTime - startTime) + " milliseconds for " +
						Integer.toString(totalLength) + " bytes");
			}
		}
		catch (java.io.IOException e)
		{
			if (Thread.currentThread().isInterrupted()) 
			{ 
				System.out.println("***** Interrupted");
				Thread.yield(); 
				return; 
			}
			System.out.println("Error in getBytes: " + e.getClass().getName() + " " +
					e.getMessage());
			System.out.println("Number of bytes moved: " +
					Integer.toString(totalLength));
			throw e;
		}
		reader.close();
	}	
	/**
	 * Read the contents of a  a URL and then sends
	 * the data to a OutputStream object.
	 * 
	 * @param url URL of item to be copied
	 * @param output OutputStream object where data is to be written
	 * @throws java.io.IOException
	 * 
	 */
	public static void getBytes(URL url, OutputStream output)
	throws IOException
	{
		boolean success = false;
		int tries = 0;
		int totalLength = 0;
		URLConnection connection = null;
		java.io.InputStream input = null;
		byte[] buffer = new byte[32768];
		long startTime = System.currentTimeMillis();
		long endTime = startTime + combinedTime;
		long currentTime = System.currentTimeMillis();
		for (int i = 0; i < 4; i++) 
		{
			if (input != null)
			{
				input = null; 
			}
			if (connection != null)
			{
				connection = null;
			}
			input = null;
			try {
				connection = url.openConnection();
				connection.setDoInput(true);
				connection.setDoOutput(false);
				connection.setReadTimeout(readTimeout * power(i));
				connection.setConnectTimeout(connectTimeout * power(i));
				showProperties(connection);
				connection.connect();
				input = connection.getInputStream();

				while (true) {
					/* 
					 * The read statement blocks until there
					 * are characters available to be read.
					 * 
					 * I thought that using 
					 * URLConnection.setReadTimeout
					 * would result in an exception if the
					 * time out was exceeded but it doesn't seem
					 * to be happening.
					 */
					int length = input.read(buffer);
					if (length < 0) {
						break;
					}

					output.write(buffer, 0, length);
					totalLength = totalLength + length;
					Thread.yield();
					if (debugLevel > 0) {
						System.out.print("*");
					}
					currentTime = System.currentTimeMillis();
					if (currentTime > endTime) {
						throw new java.io.IOException("Time limit of "
								+ Long.toString(combinedTime)
								+ " milliseconds exceeded "
								+ "in getBytes(URL,File)");
					}
				} /*  End of while loop */

				input.close();
				if (output != null) 
				{
					output.close();	
				}
				success = true;
			} 
			catch (java.io.IOException e) 
			{
				if (Thread.currentThread().isInterrupted()) {
					System.out.println("***** Interrupted");
					Thread.yield();
					return;
				}
				System.out.println("Error in getBytes: "
						+ e.getClass().getName() + " " + e.getMessage());
				System.out.println("     Number of bytes moved: "
						+ Integer.toString(totalLength));
				if (i == 3)
				{
					throw e;
				}
			}
			tries = i + 1;
			if (success) { break; }
		}
		if (!success)
		{
			System.out.println("Failure after " + Integer.toString(tries) + " tries"); 
		}
		else if (success && tries > 1)
		{
			System.out.println("Success after " + Integer.toString(tries) + " tries");
		}
		if (debugLevel > 0)
		{ System.out.print("      " + Integer.toString(totalLength) + " bytes "); }
		if (currentTime - startTime > 30000)
		{
			System.out.println(Long.toString(currentTime - startTime) +
					" milliseconds for " + Integer.toString(totalLength) +
			" bytes");
		}
	}
	/**
	 * Copy a file.
	 * 
	 *    
	 * @param input File object for file to be copied
	 * @param output File object for copy of file to be created
	 * @throws IOException
	 * @see FileOutputStream
	 */
	public static void copyFile (File input, File output)
	throws IOException
	{
		boolean test;
		if (!input.exists())
		{
			throw new IOException("copyFile: input file does not exist");
		}
		if (!input.canRead())
		{
			throw new IOException("copyFile: unable to read input file");
		}
		if (!output.getParentFile().exists())
		{
			System.out.println("Creating directory " + output.getParent());
			fixDirs(output);
		}
		if (!output.exists())
		{
			test = output.createNewFile();
			if (!test)
			{
				System.out.println("Error creating new file");
				throw new IOException("Unable to create new file");
			}
		}
		if (!output.canWrite())
		{
			throw new IOException("copyFile: unable to write output file");
		}
		if (debugLevel > 1)
		{
			System.out.println("copyFile(File,File): Starting copyFile");
			System.out.println("*** " + input.getName() + " exists: " + Boolean.toString(input.exists()));
			System.out.println("***     " + input.getCanonicalPath());
			System.out.println("***     length:    " + Long.toString(input.length()));
			System.out.println("***     canRead(): " + Boolean.toString(input.canRead()));
			System.out.println("*** " + output.getName() + " exists: " + Boolean.toString(output.exists()));
		}
		FileOutputStream outputStream = new FileOutputStream(output);
		getBytes(input, outputStream);
		outputStream.close();
	}
	/**
	 * Copy a file.
	 * 
	 * @param inputFileName Name of file to be copied
	 * @param outputFileName Name of copy of file to be created
	 * @throws IOException
	 * @see  bradleyross.library.helpers.tests.TestCopyFile
	 */
	public static void copyFile (String inputFileName, String outputFileName)
	throws IOException
	{
		File input = new File(inputFileName);
		File output = new File(outputFileName);
		copyFile(input, output);
	}
	/**
	 * Checks to see if directories for a file exist, and then
	 * creates directories as appropriate.
	 * @param newFile File object to be created
	 * @throws java.io.IOException
	 * @see File
	 */
	public static void fixDirs(File newFile)
	throws java.io.IOException
	{
		try
		{
			File directory = newFile.getParentFile();
			if (directory == null) { return; }
			if (directory.exists()) 
			{ return; }
			else
			{ 
				directory.mkdirs();
				return;
			}
		}
		catch (Exception e)
		{
			System.out.println("Error in fixDirs: unable to create directories " +
					"for " + newFile.toString());
			System.out.println(e.getClass().getName() + " " +
					e.getMessage());
			e.printStackTrace(System.out);
			throw new java.io.IOException("Unable to create directories for " +
					newFile.toString() + "  " +
					e.getClass().getName() + " " + e.getMessage());
		}
	}	

	/**
	 * Checks to see if directories for a file exist, and then
	 * creates directories as appropriate.
	 * @param name Name of file or directory to be created
	 * @throws java.io.IOException
	 * @see File
	 */
	public static void fixDirs(String name)
	throws IOException
	{
		try
		{
			File newFile = new File(name);
			File directory = newFile.getParentFile();
			if (directory == null) { return; }
			if (directory.exists()) 
			{ return; }
			else
			{ 
				directory.mkdirs();
				return;
			}
		}
		catch (Exception e)
		{
			System.out.println("Error in fixDirs: unable to create directories " +
					"for " + name);
			System.out.println(e.getClass().getName() + " " +
					e.getMessage());
			e.printStackTrace(System.out);
			throw new java.io.IOException("Unable to create directories for " +
					e.getClass().getName() + " " + e.getMessage());
		}
	}
	/**
	 * Send properties for connection to System.out
	 * <p>This is intended for diagnostic purposes.
	 * @param connection Connection object
	 */
	public static void showProperties(URLConnection connection)
	{
		if (debugLevel <= 0) { return; }
		Map<String,List<String>> properties = connection.getRequestProperties();
		System.out.println("There are " + Integer.toString(properties.size()) + " properties");
		Object keySet[] = properties.keySet().toArray();
		for (int i = 0; i < keySet.length; i++)
		{
			System.out.println((String) keySet[i]);
		}
	}
	/**
	 * This is a test driver to check out the copyFile method.
	 * 
	 * @param args Names of original file and copy to be created
	 */
	public static void main (String[] args)
	{
		String test = "1, 2, alpha, beta, gamma , ,,,";
		String tokens[] = lineSplitterCommas(test);
		System.out.println("There are " + Integer.toString(tokens.length) + " items");
		for (int i = 0; i < tokens.length; i++)
		{
			System.out.print("*" + tokens[i] + "  ");
		}
		System.out.println();
		/**
		if (args.length < 2)
		{
			System.out.println("Not enough arguments");
		}
		try 
		{
			System.out.println("Copying " + args[0] + " to " + args[1]);
			copyFile(args[0], args[1]);
		} 
		catch (IOException e) 
		{
			System.out.println("Error in running driver");
			System.out.println(e.getClass().getName() + " " + 
					e.getMessage());
			e.printStackTrace();
		}
		 */
	}
	/**
	 * Split a line into tokens using spaces as the field separators.
	 *
	 * <p>This method can be used as an example of processing regular
	 *    expressions in Java.</p>
	 * @param input Line to be split into tokens
	 * @return Array of strings containing the tokens.
	 */
	public static String[] lineSplitterSpaces (String input)
	{
		java.util.regex.Pattern pattern = java.util.regex.Pattern.compile ("\\s+");
		java.util.regex.Matcher matcher = pattern.matcher(input.trim());
		String result = matcher.replaceAll(" ");
		return result.split(" ");
	}	
	/**
	 * Split a line into tokens using commas as the field separators.
	 *
	 * <p>The space is suffixed to the input parameter because the system
	 *    doesn't seem to recognize a comma as the last character in the
	 *    string as signifying a new token.</p>
	 * @param input Line to be split into tokens
	 * @return Array of strings containing the tokens.
	 */
	public static String[] lineSplitterCommas (String input)
	{
		String tokens[] = (input + " ").split(",");
		for (int i = 0; i < tokens.length; i ++)
		{
			tokens[i] = tokens[i].trim();
		}
		return  tokens;
	}
	/**
	 * Create a file containing a thumbnail image from another file.
	 * @param inputFile Name of file to be read
	 * @param outputFile Name of file to be generated
	 * @param sizeX Width of generated image in pixels
	 * @param sizeY Height of generated image in pixels
	 * @see BufferedImage
	 * @see javax.imageio.ImageIO
	 * @see java.awt.image.RenderedImage
	 */
	public static void thumbnail(String inputFile, String outputFile, int sizeX, int sizeY) throws IOException {
		thumbnail(new File(inputFile), new File(outputFile), sizeX, sizeY);
	}
	/**
	 * Create a file containing a thumbnail image from another file.
	 * @param input  file to be read
	 * @param output  file to be generated
	 * @param sizeX Width of generated image in pixels
	 * @param sizeY Height of generated image in pixels
	 * @see BufferedImage
	 * @see javax.imageio.ImageIO
	 * @see java.awt.image.RenderedImage
	 */
	public static void thumbnail(File input, File output, int sizeX, int sizeY) throws IOException
	{
		if (debugLevel > 2)
		{
			System.out.println("Arguments: " + input.getCanonicalPath() + ", " + output.getCanonicalPath() + ", " +
					Integer.toString(sizeX) + ", " + Integer.toString(sizeY));
		}
		Integer targetX = 0;
		Integer targetY = 0;
		if (sizeX <= 0 && sizeY <= 0)
		{
			throw new IOException("Either target width or target height must be positive");
		}
		else if (sizeX == 0 || sizeY == 0)
		{
			throw new IOException("Zero not acceptable for target width or target height");
		}
		String inputFileName = input.getCanonicalPath();
		BufferedImage image = null;
		BufferedImage thumb = null;
		Image intermediate = null;
		String format = null;
		String suffix = null;
		FileOutputStream outputStream = null;
		String outputName = output.getName();
		suffix = outputName.substring(outputName.lastIndexOf(".") + 1);
		if (suffix == null)
		{
			throw new IOException("No suffix found for output file " +
					outputName + " (null value)");
		}
		else if (suffix.equalsIgnoreCase("jpg") || suffix.equalsIgnoreCase("jpeg"))
		{ format = "jpeg"; }
		else if (suffix.equalsIgnoreCase("png"))
		{ format = "png"; }
		else if (suffix.equalsIgnoreCase("gif"))
		{ format = "gif"; }
		else
		{ throw new IOException("Unable to process suffix " + suffix + 
				" for file " + outputName); }
		try 
		{
			image = javax.imageio.ImageIO.read(new FileInputStream(input));
		} 
		catch (FileNotFoundException e) 
		{
			throw new IOException("Unable to find file " + inputFileName + " " +
					e.getMessage());
		} 
		catch (IOException e) 
		{
			throw new IOException("Unexpected error while reading file " + inputFileName + " " +
					e.getMessage());
		}
		if (sizeX <= 0 || sizeY <= 0)
		{
			if (debugLevel > 2)
			{
				System.out.println("Must adjust one parameter");
			}
			Double ratio = (double) image.getWidth() / (double) image.getHeight();
			if (sizeX < 0)
			{
				targetY = sizeY;
				targetX = (int) (Math.floor((double) sizeY * ratio));
			}
			else if (sizeY < 0)
			{
				targetX = sizeX;
				targetY = (int) (Math.floor((double) sizeX / ratio));
			}
		}
		else if (sizeX > 0 && sizeY > 0)
		{
			targetX = sizeX;
			targetY = sizeY;
		}
		intermediate = image.getScaledInstance(targetX, targetY, BufferedImage.SCALE_FAST);
		thumb = new BufferedImage(targetX, targetY, BufferedImage.TYPE_INT_RGB);
		Graphics g = thumb.createGraphics();
		g.drawImage(intermediate, 0, 0, null);
		outputStream = new FileOutputStream(output);
		ImageIO.write(thumb, format, outputStream);
	}
	/**
	 * Makes file with thumbnail image
	 * @param input Input file
	 * @param output Output file
	 * @param size Height and width of output image in pixels
	 * @throws IOException
	 */
	public static void thumbnail(String input, String output, int size) throws IOException
	{
		thumbnail(new File(input), new File(output), size, size);
	}
	/**
	 * Makes file with thumbnail image
	 * @param input Input file
	 * @param output Output file
	 * @throws IOException
	 */
	public static void thumbnail(String input, String output) throws IOException
	{
		thumbnail(new File(input), new File(output), 150, 150);
	}
}