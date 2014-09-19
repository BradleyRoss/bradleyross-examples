package bradleyross.j2ee.tags;
import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.TagSupport;
/**
 * This custom tag can be used to center an object on
 * the web page horizontally.
 * <p>By using optional attributes, it can also
 *    position the element against the left or right
 *    border.</p>
 * @author Bradley Ross
 *
 */
public class CenterTag extends TagSupport 
{
/**
 * This is a default entry used for the support of
 * serialization of the class.
 */
private static final long serialVersionUID = 1L;
/**
 * Value of the attribute <code>align</code> for the
 * custom tag.
 */
protected String align = null;
/**
 * Used by the servlet generated by the application server to
 * set the value of the attribute <code>align</code>
 * for the custom tag.  
 * <p>The name of the attribute is
 *    passed to the application server in the TLD file
 *    so that the application server can use the name
 *    of the attribute to write function calls with
 *    the correct class names.</p>
 *    @see #align
 * @param value Value to be used for attribute align
 */
public void setAlign(String value)
{ align = value; }
/**
 * Returns the value of the attribute <code>align</code> for the
 * custom tag.
 * @see #align
 * @return Value of attribute
 */
public String getAlign()
{ return align; }
/** 
 * Value of the attribute <code>className</code> for the 
 * custom tag.
 * <p>When the tag is translated into a scriptlet, this
 *    attribute becomes the value of the <code>class</code>
 *    attribute for a <code>div</code> element.  However,
 *    class is a reserved word in Java, requiring the use
 *    of a different name for the attribute.</p>
 */
protected String className = null;
/**
 * Used by the application server to set the
 * value of the variable className when it is
 * encountered as an attribute in the custom tags.
 * @see #className
 * @param value Value to be used for attribute className
 */
public void setClassName(String value)
{ className = value; }
/**
 * Returns the value of the attribute <code>className</code>
 * for the custom tag.
 * @see #className
 * @return Value of attribute
 */
public String getClassName()
{ return className; }
/**
 * Value of the attribute <code>style</code> for the
 * custom tag.
 */
protected String style = null;
/**
 * Used by the application server to set the
 * value of the variable <code>style</code> when it is
 * encountered as an attribute in the
 * custom tags.
 * @see #style
 * @param value Value to be used for attribute style
 */
protected void setStyle(String value)
{ style = value; }
/**
 * Returns the value of the attribute <code>style</code>
 * for the custom tag.
 * @see #style
 * @return Value of attribute
 */
protected String getStyle()
{ return style; }
public void setPageContext(PageContext context)
{
	super.setPageContext(context);
}
/**
 * Writes text for the JSP (Java Server Page) when the opening tag
 *    of the custom tag is encountered.
 * <p>The block of text is surrounded by a table since a table's width
 *    will automatically shrink if the sum of the column widths is
 *    less that the available width.  Setting margin-left and
 *    margin-right to auto causes the table to be centered in
 *    this situation.</p>
 */
public int doStartTag() throws JspException
{	
	try
	{
		JspWriter out = pageContext.getOut();
		if (align == null) { align="center"; }
		if (!align.equalsIgnoreCase("left") && !align.equalsIgnoreCase("center") &&
				!align.equalsIgnoreCase("right"))
		{
			out.println("<!-- " + align + " is not a valid option.  Using center -->");
			align = "center";
		}
		if (align.equalsIgnoreCase("left"))
		{ out.print("<table style=\"margin-left:0px;margin-right:auto;clear:both;\">" +
				"<tr><td>"); }
		else if (align.equalsIgnoreCase("center"))
		{ out.print("<table style=\"margin-left:auto;margin-right:auto;clear:both;\">" +
				"<tr><td>"); }
		else if (align.equalsIgnoreCase("right"))
		{ out.print("<table style=\"margin-left:auto;margin-right:0px;clear:both;\">" +
				"<tr><td>"); }
		if (className != null || style != null)
		{
			out.print("<div ");
			if (className != null)
			{ out.print(" class=\"" + className + "\""); }
			if (style != null)
			{ out.print(" style=\"" + style + "\""); }
			out.print(">");
		}
		out.print("<!-- Identifier: " + id + " -->");
		if (align != null)
		{ out.print("<!-- align: " + align + " -->"); }
		if (className != null)
		{ out.print("<!-- class: " + className + " -->"); }
		if (style != null)
		{ out.print("<!-- style: " + style + " -->"); }
	}
	catch (java.io.IOException e)
	{
	}
	return EVAL_BODY_INCLUDE;
}
/**
 * Writes test for the JSP when the closing tag for the custom
 *    tag is encountered.
 */
public int doEndTag() throws JspException
{
	try
	{
		JspWriter out = pageContext.getOut();
		if (align != null || style != null)
		{ out.print("</div>"); }
		out.print("</td></tr></table>");
	}
	catch (java.io.IOException e)
	{
	}
	return EVAL_PAGE;
}
}