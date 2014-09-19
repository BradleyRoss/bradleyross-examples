package bradleyross.j2ee.servlets;
import java.io.File;
import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.io.FileWriter;
import java.io.StringWriter;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Date;
import java.net.URLConnection;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import bradleyross.library.helpers.FileHelpers;
/**
 * Servlet that will return the contents of a web page that is
 * contained on a different web site (under consideration).
 * 
 * This is a proposed class that has not yet been implemented.
 * 
 * <ul>
 * <li>The name of the file to be retrieved is obtained
 *     using {@link HttpServletRequest#getPathInfo()}.</li>
 * <li>The MIME type is obtained from the file name using
 *     {@link URLConnection#guessContentTypeFromName(String)}</li>
 * <li>The name of the INIT parameter containing the document
 *     root is given by the parameter <code>DocumentRootParameter</code>.
 *     If the parameter is not specified for the servlet, the default 
 *     name is <code>DocumentRoot</code>.</li>
 * <li>If the INIT parameter <code>LogFile</code> has been set, it
 *     is used as the location for a log file for the servlet.</li>
 * </ul>
 * <p>Consideration should be given to increasing security for the
 *    servlet.  The first step would be to stop file names from 
 *    containing two periods in a row.</p>
 * @author Bradley Ross
 *
 */
public class GetWebPage extends HttpServlet
{
	/**
	 * ID number to satisfy SERIALIZABLE interface.
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Keeps track of how many instances of this servlet have
	 * been opened by the application server.
	 *
	 * <p>There is a problem in that Tomcat does not appear to
	 *    treating this as a static value.  The idea was that
	 *    the application server could run multiple GetFile
	 *    objects and would use this parameter to set the
	 *    value of instanceNumber so that log files for the
	 *    different objects would go to different files.  Instead,
	 *    the value appears to be zero each time the init
	 *    method is called.  The init method should only be called
	 *    once for each GetFile object.</p>
	 */
	protected static int runningInstanceNumber = 0;
	/**
	 * Identifies this instance of the servlet.
	 * 
	 * <p>It was intended that this variable would identify the
	 *    GetFile object if the application server was running
	 *    multiple instances of the GetFile class.</p>
	 */
	protected int instanceNumber = 0;
	/**
	 * Keeps track of the different instances of the service method that are running.
	 */
	protected int runningServiceNumber = 0;
	/**
	 * ServletConfig object as passed to the init method.
	 */
	protected ServletConfig config = null;
	/**
	 * ServletContext object containing information
	 * for the entire web application.
	 */
	protected ServletContext context = null;
	/**
	 * Name of parameter containing the location of the document root.
	 */
	protected String fileRootParameter = "DocumentRoot";
	/**
	 * Name of root directory for reading files.
	 */
	protected String fileRoot = null;
	/**
	 * Name of log file.
	 */
	protected String logFile = null;
	/**
	 * If true, place messages in log file.
	 */
	protected boolean logFileActive = false;
	/**
	 * Character string indicating end of line.
	 */
	protected String lineSeparator = null;
	/**
	 * Servlet has a valid configuration when true.
	 */
	protected boolean valid = true;
	/**
	 * Run when object is created for processing calls to servlet.
	 * 
	 * <p>The application server can create
	 *  multiple objects for a single servlet to improve 
	 *  performance.</p>
	 */
	public void init(ServletConfig configIn) throws ServletException
	{ 
		super.init(configIn); 
		runningInstanceNumber++;
		instanceNumber = runningInstanceNumber;
		config = configIn;
		context = config.getServletContext();
		try 
		{
			lineSeparator = System.getProperty("line.separator");
		} 
		catch (Exception e) 
		{
			lineSeparator = "\n";
		}
		String working = (String) null;
		working = config.getInitParameter("DocumentRootParameter");
		if (working != null)
		{ fileRootParameter = working; }
		else
		{ fileRootParameter = "DocumentRoot"; }
		working = (String) null;
		working = config.getInitParameter(fileRootParameter);
		if (working != null)
		{ fileRoot = working; }
		else
		{
			working = null;
			working = context.getInitParameter(fileRootParameter);
			if (working != null)
			{
				fileRoot = working;
			}
		}
		if (fileRoot == null)
		{ valid = false; }
		working = (String) null;
		working = config.getInitParameter("LogFile");
		if (working == null)
		{
			logFileActive = false;
		}
		else
		{
			logFile = config.getInitParameter("LogFile") + Integer.toString(instanceNumber) + ".txt";
			runningServiceNumber = 0;
			logFileActive = true;
			createLogEntry("Instance of servlet has been created");
		}
	}
	/** 
	 * Run when object for processing servlet is no longer needed
	 * and is destroyed.
	 */
	public void destroy()
	{ 
		createLogEntry("Instance has been destroyed");
		super.destroy();
	}
	protected void createLogEntry(String message)
	{
		if (!logFileActive)
		{
			return;
		}
		boolean success = false;
		for (int i = 0; i < 3; i++)
		{
			try 
			{	
				FileWriter output = new FileWriter(logFile, true);
				output.write(new Date().toString() + " ");
				output.write(message);
				output.write(lineSeparator);
				output.close();
				success = true;
			} 
			catch (IOException e) 
			{	
				success = false;
			}
			if (success) { break; }
		}	
	}
	protected void createLogEntry(String message, int serviceNumber)
	{
		if (!logFileActive)
		{
			return;
		}
		boolean success = false;
		for (int i = 0; i < 3; i++)
		{
			try 
			{	
				FileWriter output = new FileWriter(logFile, true);
				output.write(new Date().toString() + " " +
						Integer.toString(serviceNumber) + " " +
						message + lineSeparator);
				output.close();
				success = true;
			} 
			catch (IOException e) 
			{	
				success = false;
			}
			if (success) { break; }
		}	
	}
	protected void createLogEntry(String message, Throwable e, int serviceNumber)
	{
		if (!logFileActive)
		{
			return;
		}
		boolean success = false;
		for (int i = 0; i < 3; i++)
		{
			try 
			{	
				FileWriter output = new FileWriter(logFile, true);
				StringWriter working = new StringWriter();
				PrintWriter writer = new PrintWriter(working);
				e.printStackTrace(writer);
				output.write(new Date().toString() + " " + Integer.toString(serviceNumber) +
						" " + message + lineSeparator + working.toString());
				output.close();
				success = true;
			} 
			catch (IOException e1) 
			{	
				success = false;
			}
			if (success) { break; }
		}	
	}	

	/**
	 * Processes request to a servlet.
	 * 
	 * <p>There can be many instances of the service method running at
	 *    the same time.  This must be taken into account when coding this
	 *    method.</p>
	 * 
	 * <p>It may be necessary to change this file so that the information
	 *    is recorded to a byte array and then repeated as necessary until
	 *    success is achieved.</p>
	 *    
	 * @param req Object containing request information
	 * @param res Object containing response information
	 * @see HttpServletRequest
	 * @see HttpServletResponse
	 * @see IOException
	 */
	@SuppressWarnings("unchecked")
	public void service (HttpServletRequest req,
			HttpServletResponse res) throws IOException
	{
		if (!valid)
		{
			res.sendError(500, "Invalid initialization parameters for servlet");
		}
		runningServiceNumber++;
		if (runningServiceNumber > 9999) {runningServiceNumber = 0; }
		int serviceNumber = runningServiceNumber;
		/**
		 * Object representing the output to the HTTP response when
		 * processing a text file.
		 */
		java.io.PrintWriter output = null;
		/**
		 * Object representing the output to the HTTP response when
		 * processing a binary file.
		 */
		java.io.OutputStream stream = null;
		String fullFileName = null;
		String suffix = null;
		String contextPath = null;
		String servletPath = null;
		String pathInfo = null;
		String queryString = null;
		String mimeType = null;
		try
		{
			contextPath = req.getContextPath();
			pathInfo = req.getPathInfo();
			servletPath = req.getServletPath();
			queryString = req.getQueryString();
			if (pathInfo == null)
			{
				res.sendError(500, "No path info in request");
				return;
			}
			mimeType = URLConnection.guessContentTypeFromName(pathInfo);
			createLogEntry("Running GetFile servlet for " + pathInfo + " with MIME type of " +
					mimeType, serviceNumber);


			if ((System.getProperty("file.separator")).equals("\\"))
			{
				pathInfo = pathInfo.replaceAll("/", "\\\\");
			}
			fullFileName = fileRoot.concat(pathInfo);
			int position = pathInfo.lastIndexOf(".");
			if (position >= 0 && position < (pathInfo.length() - 1))
			{
				suffix = pathInfo.substring(position + 1).toUpperCase();
			}
			if (queryString == null)
			{ ; }
			else if (queryString.toUpperCase().startsWith("QUERY"))
			{
				Enumeration<String> initParameterNames = config.getInitParameterNames();
				Enumeration<String> parameterNames = initParameterNames;
				output = res.getWriter();
				res.setContentType("text/plain");
				output.println("context path: " + contextPath);
				output.println("servlet path: "  + servletPath);
				output.println("path info: " + pathInfo );
				output.println("query string: " + queryString);
				while (parameterNames.hasMoreElements())
				{
					String name = parameterNames.nextElement();
					output.println(name + " : " + config.getInitParameter(name));
				}
				output.println("Suffix: " + suffix);
				output.println("Full file name: " + fullFileName);
				File tester = new File(fullFileName);
				output.println("***");
				output.println("Does file exist: " + Boolean.toString(tester.exists()));
				if (tester.exists())
				{ output.println("File exists"); }
				else
				{ output.println("File does not exist"); }
				output.println("***");
				output.flush();
				return;
			}
			if (!(new File(fullFileName)).exists())
			{
				/*
				 * If file does not exist, retry a second later.
				 */
				boolean success = false;
				for (int i = 0; i < 3; i++)
				{
					try 
					{
						Thread.sleep(1000l * (long) (i + 1));
					} 
					catch (InterruptedException e) 
					{ ; }
					if (new File(fullFileName).exists())
					{
						success = true;
						break;
					}
					else
					{ 
						createLogEntry("File " + fullFileName + " did not exist", serviceNumber);
					}
				}
				if (!success)
				{
					createLogEntry("File " + fullFileName + " does not exist: Aborting", serviceNumber);
					res.sendError(500, "File " + fullFileName + " does not exist");
					return;
				}
			}
			if (suffix == null)
			{
				createLogEntry("No suffix found");
				res.sendError(500, "No suffix found");
				return;
			}
			else if (suffix.equals("TXT"))
			{
				output = res.getWriter();
				res.setContentType("text/plain");
				output.print(FileHelpers.readTextFile(fullFileName));
				return;
			}
			else if (suffix.equalsIgnoreCase("PNG") ||
					suffix.equalsIgnoreCase("JPG") ||
					suffix.equalsIgnoreCase("JPEG") ||
					suffix.equalsIgnoreCase("GIF") )
			{
				stream = res.getOutputStream();
				res.setContentType(mimeType);
				ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
				byte[] createdArray = null;
				boolean success = false;
				for (int trial = 0; trial < 5; trial++)
				{
					try
					{
						File newOutputFile = new File(fullFileName);
						FileHelpers.getBytes(
								newOutputFile,
								byteArray);
						createdArray = byteArray.toByteArray();
						createLogEntry("Length of " + fullFileName + " is " +
								Integer.toString(createdArray.length), serviceNumber);
						res.setContentLength(createdArray.length);
						byteArray.writeTo(stream);
						success = true;
					}
					catch (Exception e)
					{
						success = false;
						byteArray.reset();
						if (trial < 4)
						{
							createLogEntry(new Date().toString() +
									" Warning: error in GetFile.service reading " + fullFileName + " - " +
									e.getClass().getName() + " " + e.getMessage() + " - " +
									"Retrying "  + Integer.toString(trial), e, serviceNumber); 
						}
						else
						{
							createLogEntry(new Date().toString() +
									" Warning: error in GetFile.service reading " + fullFileName + " - " +
									e.getClass().getName() + " " + e.getMessage() + " - " +
									"Aborting", e, serviceNumber); 
							throw new IOException(e.getClass().getName() + " " + e.getMessage());
						}
					}
					if (success) 
					{ 
						createLogEntry("Processing of " + fullFileName + " complete", serviceNumber);
						break; 
					}
				}
				if (!success)
				{
					createLogEntry("Failure in processing " + fullFileName, serviceNumber);
					res.sendError(500, "Unable to read" + fullFileName);
				}
				return;
			}
			else
			{
				output = res.getWriter();
				res.setContentType("text/plain");
				output.println("Suffix " + suffix + 
					" does not have a handler defined");
				createLogEntry ("File " + fullFileName +
					" does not have a handler defined", serviceNumber);
				res.sendError(500, "File " + fullFileName +
					" does not have a handler defined");
			}	
		}
		catch (java.io.IOException e)
		{ 
			createLogEntry("Failure in servlet: " +
					e.getClass().getName() + " " +
					e.getMessage(), e, serviceNumber);
			res.sendError(500, "Internal error in servlet: " + e.getClass().getName() + " " +
					e.getMessage());
		}
	}
	/* End of service method */
	/**
	 * This provides an error message if it is attempted to run this
	 * class as a Java application rather than a Java servlet.
	 * 
	 * @param args Not used at this time
	 */
	public static void main(String[] args) 
	{
		System.out.println("No main program provided.");
	}
}
