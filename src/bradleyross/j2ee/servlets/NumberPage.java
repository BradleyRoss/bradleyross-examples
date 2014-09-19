package bradleyross.j2ee.servlets;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * Returns a JPEG image containing a number.
 * 
 * <p>This will be used for testing.</p>
 * 
 * @author Bradley Ross
 *
 */
@SuppressWarnings("serial")
public class NumberPage extends HttpServlet {
	public void service(HttpServletRequest req, HttpServletResponse res)
	throws IOException, ServletException {
		res.setContentType("image/jpeg");
		BufferedImage image = new BufferedImage(512, 512, BufferedImage.TYPE_3BYTE_BGR);
		Graphics g = image.createGraphics();
		g.setColor(Color.lightGray);
		g.fillRect(0, 0, 512, 512);
		String stringValue = req.getParameter("value");
		if (stringValue == null) {
			stringValue = "Null Value";
		}
		Font font = new Font("serif", Font.BOLD, 36);
		g.setFont(font);
		g.setColor(Color.black);
		g.drawString(stringValue, 20, 150);
		g.dispose();
		OutputStream out = res.getOutputStream();
		if (out == null) {
			throw new ServletException("Value of getOutputStream() is null");
		}
		ImageIO.write(image, "jpeg", out);
	}
}
