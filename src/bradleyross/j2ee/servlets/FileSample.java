package bradleyross.j2ee.servlets;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
//
/** 
* Sample program to get listing of files in directory (Needs work
* to become more generic).
*/
class FileSample extends HttpServlet
    {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//
    private static void directory (String directory)
       {
       int rowNumber;
       String prefix = " ";
       String testPrefix = " ";
       String fullname;
       String latestName = " ";
       File listing = new File(directory);
	   String[] fileList;
	   System.out.println("Checking for " + directory);
	   if (listing.exists())
	      { 
		  System.out.println("Directory exists");
		  fileList = listing.list();
		  System.out.println(String.valueOf(fileList.length) + " entries"); 
		  }
	   else 
	      { 
		  System.out.println("Directory does not exist"); 
		  return;
		  }
       // Page header
       //
       System.out.println("<html><head>");
       System.out.print("<title>Directory of ");
       System.out.print(directory);
       System.out.println("</title>");
       System.out.println("</head>");
       //
       System.out.println("<body>");
       System.out.print("<p>This is a list of reports in the ");
       System.out.print(directory);
       System.out.println(" directory.  The latest version is shown");
       System.out.println("in the third column.  Click on the version name ");
       System.out.println("to get the latest version.  To get a list of all");
       System.out.println("versions, push the button for the report marked");
       System.out.println("<b>Archive</b>.");
       System.out.println("</p>");
       System.out.println("<table border>");
       //
       // Print report header
       System.out.print("<tr>");
       System.out.print("<th></th>");
       System.out.print("<th>Report name</th>");
       System.out.print("<th>Click for latest version</th>");
       System.out.println("<th>Click for list of archived versions</th>");
       System.out.println("</tr>");
       //
       rowNumber = 1;
       for (int i = 0; i < fileList.length; i++)
          { // start of loop
          fullname = fileList[i];
          if (!fullname.startsWith(".")) // If block A
             {
			 if (fullname.indexOf(".") < 0)
			    {
				System.out.println(fullname + " does not contain period");
				continue;
				}
             testPrefix = fullname.substring(0,fullname.indexOf("."));
             //
             if (prefix.compareTo(testPrefix) != 0) // If block B
                {
                if (!prefix.equals(" ")) // If block C
                   {
                   System.out.println("<!-- *****  *****  ***** -->");
                   System.out.println("<tr>");
                   //
                   // Sequence number
                   System.out.print("<td align=\"right\">");
                   System.out.print(rowNumber);
                   System.out.println("</td>");
                   //
                   // Name of report
                   System.out.print("<td>");
                   System.out.print(latestName.substring(0,latestName.indexOf(".")));
                   System.out.println("</td>");
                   //
                   // Name of report version
                   // The button to select the latest version will go here
                   System.out.print("<td>");
                   System.out.print(latestName.substring(latestName.indexOf(".")+1));
                   System.out.println("</td>");
                   //
                   // Button to get list of archived versions will go here
                   System.out.print("<td>");
                   System.out.print("<b>Archive</b>");
                   System.out.println("</td>");
                   //
                   latestName = " ";
                   rowNumber = rowNumber + 1;
                   //
                   System.out.println("</tr>");
                   //
                   } // end of If block C
                prefix = testPrefix;
                } // End of If block B
             //
             if (fullname.compareTo(latestName) > 0)
                {
                latestName = fullname;
                }
             } // end of If block A
          } // end of loop
       if (!prefix.equals(" "))
          {
                   System.out.println("<!-- *****  *****  ***** -->");
                   System.out.println("<!-- Final pass -->");
                   System.out.println("<tr>");
                   //
                   // Sequence number
                   System.out.print("<td>");
                   System.out.print(rowNumber);
                   System.out.println("</td>");
                   //
                   // Name of report
                   System.out.print("<td>");
                   System.out.print(latestName.substring(0,latestName.indexOf(".")));
                   System.out.println("</td>");
                   //
                   // Name of latest report version
                   // The button to select the latest version will go here
                   System.out.print("<td>");
                   System.out.print(latestName.substring(latestName.indexOf(".")+1));
                   System.out.println("</td>");
                   //
                   // Button to get list of archived versions will go here
                   System.out.print("<td>");
                   System.out.print("<b>Archive</b>");
                   System.out.println("</td>");
                   //
                   //
                   System.out.println("</tr>");
          }
       System.out.println("</table>");
       System.out.println("</body>");
       System.out.println("</html>");
       // Page trailer
       }
    public static void main(String args[])
       {
       // directory("/home/bross/mrs_rpt/dailyrd");
	   String temp = "/Users/bradleyross/repository/dailyrd";
	   if (args.length > 0)
	      { temp = args[0]; }
       directory(temp);
       }
    public void init(ServletConfig config) throws ServletException
       {
       super.init(config);
       }
    public void destroy()
       {
       super.destroy();
       }
    public void service (HttpServletRequest req,
           HttpServletResponse res) throws IOException
       {
       PrintWriter output;
       res.setContentType ("text/html");
       output = res.getWriter();
       output.println("<html><head>");
       output.println("</head>");
       output.println("<body>");
       output.println("<p>Test message</p>");
       output.println("</body></html>");
       }
    //
    }
