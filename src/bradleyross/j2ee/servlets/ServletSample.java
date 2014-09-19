package bradleyross.j2ee.servlets;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
/**
* Sample servlet that displays a dummy page.
*/
class ServletSample extends HttpServlet
    {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//
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
       res.setContentType ("text/html");
       PrintWriter output = res.getWriter();
       output.println("<html><head>");
       output.println("</head>");
       output.println("<body>");
       output.println("<p>Test message</p>");
       output.println("</body></html>");
       }
    //
    }
