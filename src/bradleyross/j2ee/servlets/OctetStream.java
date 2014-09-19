package bradleyross.j2ee.servlets;
import java.io.OutputStream;
import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
/**
 * Send a response as an application/octet-stream MIME type.
 * <p>This will be for testing ability of browsers to read binary
 *    data using XmlHTTPRequest objects to read binary data.</p>
 * <p>The first three unsigned integers contain a value of 512.  
 *    The next group of integers increment by one until
 *    they reach the end.</p>
 * <p>The code was modified to have a 512x512 matrix in addition to the first two
 *    integers in order to present something resembling an actual raster image.</p>
 * <p>When JavaScript is used with XMLHttpRequest and the ArrayBuffer mode,
 *    it apparently reads the data in little endian format (least significant first).
 *    The DataOutputStream class writes the data using big endian format (most
 *    significant first).</p>   
 * @author Bradley Ross
 *
 */
@SuppressWarnings("serial")
public class OctetStream extends HttpServlet {
	protected static void  writeShort(OutputStream output, int value) throws IOException {
		output.write(value & 0xff);
		output.write((value >> 8) & 0xff);
	}
	public void service (HttpServletRequest req, HttpServletResponse res) 
			throws IOException, ServletException {
		res.setContentType("application/octet-stream");
		OutputStream output = res.getOutputStream();
		writeShort(output, 0x0200);
		writeShort(output, 0x0200);
		for (int i = 0; i < 512*512; i++) {
			writeShort(output, (0x0200 + i) % 50000);
		}
	}
}
