package bradleyross.j2ee.tags;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.jsp.tagext.TagExtraInfo;
import java.util.Enumeration;
/**
 * The goal is to produce a tag class that
 * can be used in a variety of circumstances.
 * <p>I was hoping that the tag class could
 *    determine the tag name and act accordingly.  However,
 *    it appears that this can only be used for one tag name.
 *    In this case, it serves as a replacement for the
 *    html tag.</p>
 * @see TagExtraInfo
 * @author Bradley Ross
 *
 */
public class DefaultTag extends TagSupport 
{
	/**
	 * Inserted to satisfy Serializable interface.
	 */
	private static final long serialVersionUID = 1L;

	public void setPageContext(PageContext context)
	{

		super.setPageContext(context);
	}
	protected String source = null;
	public void setSource (String value)
	{source = value; }
	public String getSource()
	{ return source;}
	public int doStartTag() throws JspException
	{	
		try
		{
			JspWriter out = pageContext.getOut();
			out.print("<html><!-- tag for html -->");
			out.print("<!-- Identifier: " + id + " -->");
			out.print("<!-- source is " + source + " -->");
			Enumeration<?> list =   getValues();
			if (list != null)
			{
				for (Enumeration<?> items = list;
				items.hasMoreElements(); )
				{
					String value = (String) items.nextElement();
					out.println("<!-- " + value + " -->");
				}
			}
			else
			{ out.println("<!-- No values -->"); }
		}
		catch (java.io.IOException e)
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
			out.print("</html><!-- end tag for html -->");
		}
		catch (java.io.IOException e)
		{
			throw new JspException("doEndTag: " + e.getClass().getName() + " " + e.getMessage());
		}
		return EVAL_PAGE;
	}
}
