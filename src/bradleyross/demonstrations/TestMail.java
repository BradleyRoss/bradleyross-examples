package bradleyross.demonstrations;
import java.io.PrintWriter;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * Demonstration of sending mail using Java.
 *<p><ul>
 *<li><p>First parameter - domain name for SMTP (RFC 822) server.</p></li>
 *<li><p>Second parameter - Email address to use for destination</p></li>
 *<li><p>Third parameter - Text to go with Email address</p></li>
 *<li><p>Fourth parameter - Email address of sender</p></li>
 *<li><p>Fifth parameter - Text to go with e-mail address of sender</p></li>
 *</ul></p>
 *<p>Example:<br>
 *<code>bradleyross.demonstrations.testMail "smtp.foo.com"
 *  "john.smith@foo.com" "John Smith" "bob.jones@foo.com" "Bob Jones"</code></p>
 *<p>Whether or not you will be able to use the SMTP port in this manner will
 *   depend on how it is configured.</p>
 * @author Bradley Ross
 */
public class TestMail {
	public static void main (String args[])
	{
		if (args.length < 5)
		{
			System.out.println("Parameters for testMail program are");
			System.out.println("1 - Name of SMTP server (e.g. smtp.foo.com)");
			System.out.println("2 - EMail address (e.g. john.smith@foo.com)");
			System.out.println("3 - Text representation of EMail recipient (John Smith)");
			System.out.println("4 - Email of sender");
			System.out.println("5 - Text representation of sender");
			return;
		}
		try
		{
			System.getProperties().put("mail.host", args[0]);
			System.out.println("SMTP host set to " + args[0]);
			// BufferedReader in = new BufferedReader (new InputStreamReader(System.in));
			URL u = new URL("mailto:" + args[1]);
			URLConnection c = u.openConnection();
			System.out.println("Connection opened");
			c.setDoInput(false);
			c.setDoOutput(true);
			c.setReadTimeout(20000);
			c.setConnectTimeout(20000);
			System.out.flush();
			c.connect();
			System.out.println("Connected");
			System.out.flush();
			PrintWriter out = 
					new PrintWriter(new OutputStreamWriter(c.getOutputStream()));
			System.out.println("New PrintWriter created");
			System.out.println("To: address is " + args[1]);
			System.out.println("From: address is " + args[3] + "<" + args[4] + ">");
			System.out.println("Reply-To: address is " + args[3] +
					"<" + args[4] + ">"); 
			System.out.flush();
			out.println("From: \"" + args[3] + "\" <" + args[4] + ">");
			System.out.println("First line sent");
			out.println("Reply-To: \"" + args[3] + "\" <" + args[4] + ">");
			// long currentTime = System.currentTimeMillis();
			Date currentDate = new Date(System.currentTimeMillis());
			SimpleDateFormat df = 
					new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z (zzz)");
			out.println("Date: " + df.format(currentDate));
			System.out.println("Date: value is " + df.format(currentDate));
			out.println("To: " + args[1]);
			out.println("Subject: Demonstration");
			out.println("Content-type: text/html; charset=iso-8859-1");
			out.print("\n");
			out.println("<html><head></head><body>");
			out.println("<title>Test</title>");
			out.println("<p><font color=\"red\">");
			out.println("This is a demonstration of how easy it is to send mail");
			out.println("to an SMTP (RFC 822) server using Java");
			out.println("</font></p></body></html>");
			out.close();
			System.out.println("Closed");
			System.out.flush();
		}
		catch (Exception e)
		{
			System.out.println("Error encountered in bradleyross.demonstrations.testMail");
			System.out.println("Error: " + e.getMessage());
			e.printStackTrace(System.out);
			System.out.flush();
			// System.err.println(e);
		}
	}
}
