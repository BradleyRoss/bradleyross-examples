package bradleyross.j2ee.servlets;
import javax.servlet.http.HttpServletRequest;
// import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Collections;
import java.util.Vector;
import bradleyross.library.helpers.GenericPrinter;
import bradleyross.library.helpers.StringHelpers;
/**
 * This is a dummy servlet that echoes back the parameters
 * passed in the HTTP transaction.
 * 
 * @author Bradley Ross
 *
 */
@SuppressWarnings("serial")
public class EchoBack extends Servlet 
{
	@Override
	protected void processor(ThisPage thisPage) throws IOException 
	{
		HttpServletRequest request = thisPage.getRequest();
		// HttpServletResponse response = thisPage.getResponse();
		GenericPrinter printer = thisPage.getPrinter();
		printer.println("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">");
		printer.println("<html><head>");
		printer.println("<title>Echo back Parameters</title>");
		printer.println("</head><body>");
		printer.println("<h1>Contents of Form Submittal</h1>");
		printer.println("<p>When a form is submitted that has this servlet listed ");
		printer.print("as the action, this page echoes back the HTTP headers ");
		printer.println("and the parameters contained in the form.</p>");
		printer.println("<p>Method: " + request.getMethod() + "</p>");
		printer.println("<p>Content Type: " + request.getContentType() + "</p>");
		printer.println("<p>Character Encoding: " + request.getCharacterEncoding() + "</p>");
		printer.print("<p><a href=\"#headers\">Headers</a> ");
		printer.println(" <a href=\"#parameters\">Parameters</a></p>");
		printer.println("<h2><a name=\"headers\">Headers</a></h2>");
		Enumeration<?> headerList = request.getHeaderNames();
		printer.println("<table border=\"1\"");
		while (headerList.hasMoreElements())
		{
			int counter2 = 0;
			String name = (String) headerList.nextElement();
			Enumeration<?> headerValues = request.getHeaders(name);
			printer.println("<tr><td>" + name + "</td><td><ul>");
			while (headerValues.hasMoreElements())
			{
				counter2++;
				String value = (String) headerValues.nextElement();
				printer.println("<li>" + StringHelpers.escapeHTML(value) + "</li>");
			}
			if (counter2 == 0)
			{ printer.println("<i>No values</i>"); }
			printer.println("</ul></td></tr>");
		}
		printer.println("</table>");
		printer.println("<h2><a name=\"parameters\">Parameters</a></h2>");
		Enumeration<?> parameterList = request.getParameterNames();
		Vector<String> parameterVector = new Vector<String>();
		while (parameterList.hasMoreElements())
		{
			String name = (String) parameterList.nextElement();
			parameterVector.add(name);
		}
		
		parameterVector.trimToSize();
		if (parameterVector.size() > 0)
		{
			printer.println("<table border=\"1\">");
			printer.println("<tr><th>Parameter name</th><th>Parameter Values</th></tr>");
			Collections.sort(parameterVector);
			for (int i = 0; i < parameterVector.size(); i++)
			{
				String parameterName = parameterVector.elementAt(i);
				printer.print("<tr><td>" + parameterName + "</td>");
				String values[] = request.getParameterValues(parameterName);
				if (values.length == 0)
				{
					printer.print("<td><i>No parameter values</i></td></tr>");
				}
				else
				{
					printer.print("<td>");
					for (int i2 = 0; i2 < values.length; i2++)
					{
						if (i2 > 0)
						{
							printer.print("<br />");
						}
						String thisValue = values[i2];
						if (thisValue != null)
						{
							thisValue = thisValue.trim();
						}
						if (thisValue == null)
						{
							printer.print("<i>Null value</i>");
						}
						else if (thisValue.length() == 0)
						{
							printer.print("<i>Empty String</i>");
						}
						else
						{
							printer.print(thisValue);
						}
					}
					printer.println("</td></tr>");
				}
			}
			printer.print("</table>");
		}
		else
		{
			printer.println("<p>No parameters were used in this transaction</p>");
		}
		printer.print("<h2>Request Attributes</h2>");
		Enumeration<?> attributeList = request.getAttributeNames();
		Vector<String> attributeVector = new Vector<String>();
		while (attributeList.hasMoreElements())
		{
			String name = (String) attributeList.nextElement();
			attributeVector.add(name);
		}
		attributeVector.trimToSize();
		if (attributeVector.size() > 0)
		{
			printer.println("<ul>");
			for (int i = 0; i < attributeVector.size(); i++)
			{
				printer.println(StringHelpers.escapeHTML(attributeVector.elementAt(i)));
			}
			printer.println("</ul>");
		}
		else
		{
			printer.println("<p>No attributes for transaction</p>");
		}
		printer.println("</body></html>");
		thisPage.sendContents();
	}
}
