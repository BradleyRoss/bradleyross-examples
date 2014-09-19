package bradleyross.j2ee.servlets;
import java.io.IOException;
import java.io.StringWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletOutputStream;
import bradleyross.j2ee.servlets.Servlet;
import bradleyross.library.helpers.GenericPrinter;
/**
 * Superclass for classes downloading files from the server.
 * 
 * @author Bradley Ross
 *
 */
public class DownloadItem extends Servlet 
{
	/**
	 * Added to satisfy serializable interface.
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * This method retrieves the item to be downloaded.
	 * 
	 * <p>This method will be overridden to produce meaningful classes.</p>
	 * 
	 * @param thisPage Object containing information on this HTTP transaction
	 * @throws IOException
	 */
	protected  void processor(ThisPage thisPage) throws IOException
	{
		StringWriter sw = new StringWriter();
		GenericPrinter writer = new GenericPrinter(sw);
		writer.println("This is a test of bradleyross.library.servlets.DownloadItem.");
		writer.println("This page is produced by the method processor(ThisPage). ");
		writer.println("The processor method would be subclassed to produce a useful page.");
		thisPage.setMimeType("text/plain");
		thisPage.setFileName("test.txt");
		byte[] result = sw.toString().getBytes();
		writer.close();
		thisPage.setContents(result);
	}
	/**
	 * This method is called by the application server to service
	 * the HTTP transaction.
	 * 
	 * @param req HTTP request object
	 * @param res HTTP response object
	 * @throws IOException
	 */
	public void service (HttpServletRequest req,
			HttpServletResponse res)
	throws IOException
	{
		Servlet.ThisPage thisPage = new Servlet.ThisPage(req, res, getConfig());
		starter(thisPage);
		if (thisPage.getTerminateRequest())
		{
			return;
		}
		processor(thisPage);
		if (thisPage.getTerminateRequest())
		{
			return;
		}
		ender(thisPage);
		if (thisPage.getTerminateRequest())
		{
			return;
		}
		if (thisPage.getContents() == null)
		{
			thisPage.addMessage("There is no information to download");
			thisPage.errorMessage();
			return;
		}
		res.setContentType(thisPage.getMimeType());
		String tempFilename = thisPage.getFileName();
		if (tempFilename == null)
		{ ; }
		else if (tempFilename.trim().length() == 0)
		{ ; }
		else if (thisPage.getMode() == ThisPage.DOWNLOAD)
		{
			res.setHeader("Content-Disposition", "attachment;filename=" + tempFilename);
		}
		else
		{
			res.setHeader("Content-Disposition", "filename=" + tempFilename);
		}
		ServletOutputStream output = res.getOutputStream();
		output.write(thisPage.getContents());
	}
}
