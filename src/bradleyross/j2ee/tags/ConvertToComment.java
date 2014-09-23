/**
 * 
 */
package bradleyross.j2ee.tags;

import java.io.IOException;
import java.io.LineNumberReader;
import javax.servlet.jsp.tagext.BodyTagSupport;
// import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.JspException;
/**
 * This is a demonstration action that converts the material between
 * the start and end tags to escaped text and displays it on the output
 * page.
 * 
 * <p>The Tag Library Descriptor defines this tag as TAGDEPENDENT, which 
 *    means that the material in the body of the tag will not be
 *    processed by the JSP preprocessor</p>
 * @author Bradley Ross
 *
 */
@SuppressWarnings("serial")
public class ConvertToComment extends BodyTagSupport {
	protected String className = new String();
	/**
	 * Actions to be carried out when end tag is encountered.
	 * 
	 * <p>The method returns {@link #EVAL_PAGE} to indicate
	 *    that processing of the JSP page will continue.</p>
	 * 
	 * @see LineNumberReader
	 * @see JspWriter
	 */
	@Override
	public int doEndTag() throws JspException {
		try {
			JspWriter out = pageContext.getOut();
			//  BodyContent content = getBodyContent();
			LineNumberReader reader = new LineNumberReader(getBodyContent().getReader());
			out.println("<!-- Start of converted material -->");
			while (true) {
				String line = reader.readLine();
				if (line == null) {break;}
				out.println(line.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;"));
				out.println("<br />");
			}
			out.println("<!-- End of converted comment -->");
			return EVAL_PAGE;
		} catch (IOException e) {
			throw new JspException(e.getClass().getName() + e.getMessage(), e);
		}
	}
	public String getClassName() {
		return className;

	}
	public void setClassName(String value) {
		className = value;
	}

}
