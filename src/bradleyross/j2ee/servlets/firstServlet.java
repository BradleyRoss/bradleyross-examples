package bradleyross.j2ee.servlets;
import java.util.Enumeration;
/**
* This is a simple example of a Java servlet.
*<p>It identifies information about the node transmitting the
*   the HTTP request.  This could be used in aiding security 
*   efforts by specifying that certain actions can only be
*   carried out if the node transmitting the request is a 
*   link local address, site local address, or loopback 
*   address.  Since these types of addresses can't be sent
*   over the public internet, this would help insure that hackers
*   couldn't carry out the operations remotely.</p>
* <p>This is a very simple example based on the idea that
*    simple examples should be under two pages in length.</p>
* <p>You will notice to references to the javax.servlet and
*    javax.servlet.http packages appear as hyperlinks.  This is 
*    because I included the following options in the javadoc command.</p>
*<p><code>-link "http://java.sun.com/j2se/1.4.2/docs/api"</code> <br>
*<code>-link "http://tomcat.apache.org/tomcat-5.5-doc/servletapi"</code></p> 
*<p>The relationship between the Java servlet to be executed and the
*   HTTP call to the application server is defined by the server.xml file
*   for the application server and the WEB-INF/web.xml file that exists for
*   each web application.  The following is the WEB-INF/web.xml file
*   that was used with this servlet.</p>
* <p><code><pre>
* &lt;web-app&gt;
* &lt;servlet&gt;
* &lt;servlet-name&gt;first&lt;/servlet-name&gt;
* &lt;servlet-class&gt;bradleyross.servlets.firstServlet&lt;/servlet-class&gt;
* &lt;/servlet&gt;
* &lt;servlet-mapping&gt;
* &lt;servlet-name&gt;first&lt;/servlet-name&gt;
* &lt;url-pattern&gt;/firstServlet&lt;/url-pattern&gt;
* &lt;/servlet-mapping&gt;
* &lt;/web-app&gt;
</pre></code></p>
*<p>The meaning of this XML document is as follows.</p>
*<p><ul>
*<li><p>The servlet is named <code>first</code>
*       (<code>servlet-name</code>).  This name doesn't appear
*       in the Java code but is means of describing the configuration to
*       the application server.</p></li>
*<li><p>The Java class for the servlet is 
*       <code>bradleyross.servlets.firstServlet</code> 
*       (<code>servlet-class</code>).  (Although it isn't 
*       indicated from this document, this class
*       file was included in the CLASSPATH for the application server and
*       is therefore accessible to the system.)</p></li>
*<li><p>The URL entered in the browser will contain <code>/test</code>
*       to indicate that this servlet is to be executed
*       (<code>servlet-mapping</code>).  This is known as the
*       servlet path, meaning that it refers to the path
*       within the web application.</p></li>
*</ul></p>
*<p>The web application is contained in the directory
*   <code>webapps/test</code> on the application server, meaning that the
*   XML file is at <code>webapps/test/WEB-INF/web.xml</code>.  Unless
*   there are entries in server.xml, the context path for the
*   web application is <code>/test</code>, meaning that the
*   servlet is accessed as <code>/test/firstServlet</code>.  (The 
*   context path followed by the servlet path.)</p>
* @author Bradley Ross
*/
public class firstServlet extends javax.servlet.http.HttpServlet
   {
   /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private String firstCell(String contents)
      {
      StringBuffer value = new StringBuffer();
      value.append("<tr><td>" + contents + "</td>");
      return new String(value);
      }
   private String lastCell(String contents)
      {
      StringBuffer value = new StringBuffer();
      value.append("<td>" + contents + "</td></tr>");
      return new String(value);
      }
   private String doubleCell(String first, String last)
      {
      StringBuffer value = new StringBuffer();
      value.append("<tr><td>" + first + "</td><td>" +
           last + "</td></tr>");
      return new String(value);
      }
   public void init (javax.servlet.ServletConfig config)
      throws javax.servlet.ServletException
      { super.init(config); }
   public void destroy()
      { super.destroy(); }
//    @SuppressWarnings("unchecked")
   /**
   * @param req Information concerning the HTTP request received
   *        by the server.
   * @param res Information concerning the HTTP response generated
   *        by the server.
   */
   public void service (javax.servlet.http.HttpServletRequest req,
          javax.servlet.http.HttpServletResponse res)
          throws java.io.IOException
      {
      res.setContentType("text/html");
      java.io.PrintWriter out = res.getWriter();
      out.println("<html><head>");
      out.println("<title>Sample Servlet</title>");
      out.println("</head></body>");
      out.println("<h1><center>Information on Requester</center></h1>");
      out.println("<p>The IP address of the node sending the ");
      out.println(" request is " + req.getRemoteAddr() + "</p>");
      java.net.InetAddress address =
         java.net.InetAddress.getByName(req.getRemoteAddr());
      out.println("<table border>");
      out.println(firstCell("isLinkLocalAddress")); 
      out.println(lastCell(Boolean.toString(address.isLinkLocalAddress())));
      out.println(firstCell("isSiteLocalAddress")); 
      out.println(lastCell(Boolean.toString(address.isSiteLocalAddress())));
      out.println(firstCell("isLoopbackAddress")); 
      out.println(lastCell(Boolean.toString(address.isLoopbackAddress())));
      out.println("</table>");
      //
      out.println("<h2>Headers</h2>");
      out.println("<p>Get list of headers for request</p>");
      out.println("<p><ol>");
      Enumeration<String> e =  req.getHeaderNames();
      while( e.hasMoreElements() )
          { 
          String headerName = (String) e.nextElement();
          out.println("<li><p>" +  headerName +
               "</p><p><ul>");
          Enumeration<String> e2 = req.getHeaders(headerName);
          while (e2.hasMoreElements() )
             { out.println("<li><p>" +  e2.nextElement() + "</p></li>"); }
          out.println("</ul></p></li>"); 
          }
      out.println("</ol></p>");
      //
      out.println("<p>URL components</p>");
      out.println("<table border>");
      out.println(doubleCell("getContextPath",
          req.getContextPath()));
      out.println(doubleCell("getMethod",
          req.getMethod()));
      out.println(doubleCell("getPathInfo",
          req.getPathInfo()));
      out.println(doubleCell("getPathTranslated", 
          req.getPathTranslated()));
      out.println(doubleCell("getQueryString", 
          req.getQueryString()));
      out.println(doubleCell("getRequestURI",
          req.getRequestURI()));
      out.println(doubleCell("getRequestURL",
          new String(req.getRequestURL())));
      out.println(doubleCell("getServletPath",
          req.getServletPath()));
      out.println("</table>");
      out.println("<p>Cookies</p>");
      out.println("</body></html>");
      }
   }
