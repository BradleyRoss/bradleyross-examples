package bradleyross.j2ee.tags;
import javax.servlet.jsp.JspException;
// import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
// import javax.servlet.jsp.tagext.TagExtraInfo;
import java.io.IOException;
// import java.util.Enumeration;
/**
 * Demonstration custom body tag.
 */
public class Body extends TagSupport 
{
	/**
	 * Inserted to satisfy Serializable interface.
	 */
	private static final long serialVersionUID = 1L;
	public int doStartTag() throws JspException
	{	
		try
		{
			JspWriter out = pageContext.getOut();
			out.println("<body>");
		}
		catch (IOException e)
		{
			throw new JspException("doStartTag: " + e.getClass().getName() + " " + e.getMessage());
		}
		return EVAL_BODY_INCLUDE;
	}
	public int doEndTag() throws JspException
	{
		try
		{
			JspWriter out = pageContext.getOut();
			out.println("<hr />");
			out.println("<p><a href=\"index.html\">Go to main index page</a></p>");
			out.print("</body>");
		}
		catch (IOException e)
		{
			throw new JspException("doEndTag: " + e.getClass().getName() + " " + e.getMessage());
		}
		return EVAL_PAGE;
	}
}
