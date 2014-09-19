package bradleyross.j2ee.servlets;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.Iterator;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageWriter;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.ImageWriteParam;
import javax.imageio.spi.ImageWriterSpi;
@SuppressWarnings("serial")
/**
 * Get information on codecs.
 * 
 * <p>It looks like com.sun.media.imageioimpl.plugins.jpeg2000.ImageWriter and
 *    com.sun.media.imageioimpl.plugins.jpeg2000.ImageReader are pure java
 *    while com.sun.media.imageioimpl.plugins.jpeg.CLibJPEGImageReader,
 *    com.sun.media.imageioimpl.plugins.jpeg.CLibJPEGImageWriter,
 *    com.sun.media.imageioimpl.plugins.jpeg2000.J2KImageWriterCodecLib and
 *    com.sun.media.imageioimpl.plugins.jpeg2000.J2KImageReaderCodecLib use
 *    compiled C code.</p>
 * @author Bradley Ross
 *
 */
public class CodecInfo extends HttpServlet {
    /*
     * This approach was recommended by 
     * http://tomcat.10.n6.nabble.com/Problems-with-ImageIO-td2073212.html
     * See also
     * http://www.java.net/node/702588
     * 
     * Without this line, the ImageIO was unable to find the codecs unless they
     * were placed in the Java system library or the Java system extensions library.
     * Apparently, when ImageIO first initialized itself, it was only able to see
     * the Java system libraries.  It is not clear if it would have been able to see
     * the CLASSPATH at the time that Tomcat was started.
     * 
     * The problem with codecs not being visible occurred with Windows but not
     * with Mac OS X.
     */
    static {
    	ImageIO.scanForPlugins();
    }
	/**
	 * List of ImageWriter objects.
	 * <p>ImagWriterFactory.properties says jpeg2000.J2KImageWriterCodecLib.</p>
	 */
	protected String[] writerCodecs = {
			"com.sun.media.imageioimpl.plugins.jpeg.CLibJPEGImageWriter",
			"com.sun.media.imageioimpl.plugins.jpeg2000.J2KImageWriter",
			"com.sun.media.imageioimpl.plugins.jpeg2000.J2KImageWriterCodecLib"
	};
	/**
	 * List of ImageReader objects.
	 * <p>ImageReaderFactory.properties says jpeg2000.J2KImageReaderCodecLib.</p>
	 * <p>jai_imageio has J2KImageReader and J2KImageReaderCodecLib.</p>
	 */
	protected String[] readerCodecs = {
			"com.sun.media.imageioimpl.plugins.jpeg.CLibJPEGImageReader",
			"com.sun.media.imageioimpl.plugins.jpeg2000.J2KImageReader",
			"com.sun.media.imageioimpl.plugins.jpeg2000.J2KImageReaderCodecLib"
	};
	/**
	 * Show information on an ImageReader codec.
	 * @param reader ImageReader object
	 * @param out destination for printing
	 */
	protected  void printReader(ImageReader reader, PrintWriter out) {
		out.println(reader.getClass().getName());
		ImageReadParam param = reader.getDefaultReadParam();
		out.println("<ol>");
		out.println("<p>" + param.getClass().getName() + "</p>");
		out.println("</ol>");
	}
	/**
	 * Show information on an ImageWriter codec.
	 * @param writer ImageWriter object
	 * @param out destination for printing
	 */
	protected void printWriter(ImageWriter writer, PrintWriter out) {
		String className = writer.getClass().getName();
		String resourceName = "/" + className.replaceAll("\\.", "/") + ".class";
		out.println("<p>" + writer.getClass().getName() + "</p>");
		out.println("<p>" + resourceName + "</p>");
		out.println("<ul>");
		out.println("<li>");
		URL url = writer.getClass().getResource(resourceName);
		if (url==null) {
			out.println("<p>null URL</p>");
		} else {
			out.println("<p>" + url.toString() + "</p>");
		}
		ClassLoader loader = writer.getClass().getClassLoader();
		if (loader == null) {
			out.println("<p>System Class Loader<p>");
			try {
				out.println("<p>" + writer.getClass().getResource(resourceName).toString() + "</p>");
			} catch (Exception e) {
				out.println("<p>" + e.getMessage() + "</p>");
			}
		} else {
			out.println("<p>" + loader.getClass().getName() + "</p>");
			try {
				URL url2 = loader.getResource(resourceName);
				if (url2 == null) {
					out.println("<p>Can't obtain resource URL</p>");
				} else {
				out.println("<p>" + url2.toString() + "</p>");
				}
			} catch (Exception e) {
				out.println("<p>" + e.getMessage() + "</p>");
			}
		}
		out.println("</li>");
		ImageWriteParam param = writer.getDefaultWriteParam();
		out.println("<li>");
		boolean compressed = param.canWriteCompressed();
		out.println("<p>" + param.getClass().getName() + "</p>");
		out.println("<p>Codec can write compressed: " + Boolean.toString(compressed) +"</p>");
		if (compressed) {
			out.println("<p>The types of compression are listed below:</p>");
			String[] types = param.getCompressionTypes();
			out.println("<ol>");
			for (int i2 = 0; i2 < types.length; i2++) {
				out.println("<li><p>" + types[i2] + "</p>");
				out.println("</li>");
			}
			out.println("</ol>");
		}
		out.println("</li>");
		if (JPEGImageWriteParam.class.isAssignableFrom(param.getClass())) {
			/*
			 * The default for compression quality value for JPEG appears to be 0.75.
			 * The standard codec can not handle lossless.
			 */
			out.println("<li>");
			int mode = param.getCompressionMode();
			param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
			out.println("<p>Current quality is " + Float.toString(param.getCompressionQuality()) + "</p>");
			out.print("<p>Estimated compressed bit rate is ");
			float estimate = param.getBitRate(param.getCompressionQuality());
			if (estimate < 0.0f) {
				out.print("<i>estimate not available</i>");
			} else {
				out.print(Float.toString(estimate));
			}
			out.println("</p>");
			String[] descriptions = param.getCompressionQualityDescriptions();
			float[] values = param.getCompressionQualityValues();
			for (int i = 0; i < descriptions.length; i++) {
				out.println("<p>" + descriptions[i] + "  " + Float.toString(values[i]) + 
						"   " + Float.toString(values[i+1]) + "</p>");
			}
			param.setCompressionMode(mode);
			out.println("</li>");
		}
		out.println("<li>");
		ImageWriterSpi spi = writer.getOriginatingProvider();
		if (spi != null) {
			out.println("<p>Spi class is " + spi.getClass().getName() + "</p>");
			out.println("<ol>");
			String[] list = spi.getFileSuffixes();
			if (list != null) {
				out.print("<li>File suffixes are ");
				for (int i = 0; i < list.length; i++) {
					if (i > 0) { out.print(", "); }
					out.print(list[i]);
				}
				out.println("</li>");
			}
			list = spi.getFormatNames();
			if (list != null) {
				out.print("<li>Format names are ");
				for (int i = 0; i < list.length; i++) {
					if (i > 0) { out.print(", "); }
					out.print(list[i]);
				}
				out.println("</li>");
			}
			list = spi.getMIMETypes();
			if (list != null) {
				out.print("<li>MIME types are ");
				for (int i = 0; i < list.length; i++) {
					if (i > 0) { out.print(", "); }
					out.print(list[i]);
				}
			out.println("</li>");
			}
			out.println("</ol>");
			out.println("</li>");
		}
		out.println("</ul>");
	}
	/**
	 * Display information on image reader and writer codecs.
	 * @param out destination for listing
	 */
	protected  void printInfo(PrintWriter out) {
		out.println("<html><head>");
		out.println("<title>Codec Information</title>");
		out.println("</head><body>");
		Iterator<ImageReader> readerList = null;
		out.println("<h1>ImageIO Reader Codecs</h1>");
		String[] mimeList = ImageIO.getReaderMIMETypes();
		for (int i = 0; i < mimeList.length; i++) {
			if (mimeList[i] == null) {
				out.println("<h2>blank name</h2>");
			} else if (mimeList[i].trim().length() == 0) {
				out.println("<h2>blank name</h2>");
			} else {
				out.println("<h2>" + mimeList[i] + "</h2>");
			}
			readerList = ImageIO.getImageReadersByMIMEType(mimeList[i]);
			out.println("<ul>");
			while (readerList.hasNext()) {
				out.println("<li>");
				ImageReader reader = readerList.next();
				printReader(reader, out);
				out.println("</li>");
			}
			out.println("</ul>");
		}
		out.println("<h1>ImageIO Writer Codecs</h1>");
		Iterator<ImageWriter> writerList = null;
		for (int i = 0; i < mimeList.length; i++) {
			if (mimeList[i] == null) {
				out.println("<h2>blank name</h2>");
			} else if (mimeList[i].trim().length() == 0) {
				out.println("<h2>blank name</h2>");
			} else {
				out.println("<h2>" + mimeList[i] + "</h2>");
			}
			writerList = ImageIO.getImageWritersByMIMEType(mimeList[i]);
			out.println("<ul>");
			while (writerList.hasNext()) {
				out.println("<li>");
				ImageWriter writer = writerList.next();
				printWriter(writer, out);
				out.println("</li>");
			}
			out.println("</ul>");
		}
		out.println("<h1>ImageIO</h1>");
		ClassLoader imageLoader = javax.imageio.ImageIO.class.getClassLoader();
		if (imageLoader == null) {
			out.println("<p>System class loader</p>");
		} else {
			out.println("<p>Loader is " + imageLoader.getClass().getName() + "</p>");
		}
		out.println("</body></html>");
	}
	@Override
	public void service(HttpServletRequest req, HttpServletResponse res) 
			throws IOException, ServletException {
		PrintWriter out = res.getWriter();
		printInfo(out);
	}
	/**
	 * Test driver.
	 * @param args
	 */
	public static void main(String[] args) {
		CodecInfo instance = new CodecInfo();
		StringWriter string = new StringWriter();
		PrintWriter out = new PrintWriter(string);
		instance.printInfo(out);
		System.out.println(string.toString());
	}

}
