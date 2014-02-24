package bradleyross.library.helpers;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Closeable;
import java.io.Flushable;
/**
 * Allows a single set of code to output results to
 * StringWriter, PrintStream and PrintWriter objects by providing a
 * common wrapper.
 * 
 * <p>PrintStream  and PrintWriter implement Closeable, Flushable, and Appendable.  The 
 *    method names and parameter types used by these methods are the same as
 *    those in the PrintStream and PrintWriter classes.</p>
 * <p>StringWriter is first encapsulated in a PrintWriter to enable it to be used as
 *    an output device.</p>
 * @see PrintStream
 * @see PrintWriter
 * @see StringWriter
 * @author Bradley Ross
 *
 */
public class GenericPrinter implements Appendable, Closeable, Flushable
{
	/**
	 * PrintWriter object.
	 * @see PrintWriter
	 */
	PrintWriter writer = null;
	/**
	 * PrintStream object.
	 * @see PrintStream
	 * 
	 */
	PrintStream stream = null;
	/**
	 * PrintStream, PrintWriter, or StringWriter
	 * object that was used in the constructor.
	 */
	Object underlying = null;
	/**
	 * Create printer for PrintWriter object
	 * @see PrintWriter
	 * @param value PrintWriter object
	 */
	public GenericPrinter(PrintWriter value)
	{
		writer = value;
		underlying = value;
	}
	/**
	 * Create printer for PrintStream object.
	 * @param value PrintStream object
	 */
	public GenericPrinter (PrintStream value)
	{
		stream = value;
		underlying = value;
	}
	/**
	 * Create printer for StringWriter object.
	 * @param value StringWriter object.
	 */
	public GenericPrinter (StringWriter value)
	{
		writer = new PrintWriter(value);
		underlying = value;
	}
	/**
	 * Return the underlying StringWriter, PrintStream or PrintWriter object
	 * for the GenericPrinter object.
	 * @return Underlying object
	 */
	public Object getUnderlyingObject()
	{
		return underlying;		
	}
	/**
	 * Checks if an error has occurred on the output stream.
	 * @see PrintStream#checkError()
	 * @see PrintWriter#checkError()
	 * @return True if an error has occurred
	 */
	public boolean checkError()
	{
		if (writer != null)
		{
			return writer.checkError();
		}
		else if (stream != null)
		{
			return stream.checkError();
		}
		else
		{
			return false;
		}
	}
	/**
	 * Place a byte containing the specified value to the stream.
	 * @see PrintStream#write(int)
	 * @see PrintWriter#write(int)
	 * @param n Value of byte to be written.
	 */
	public void write(int n)
	{
		if (writer != null)
		{
			writer.write(n);
		}
		else if (stream != null)
		{
			stream.write(n);
		}
	}
	/**
	 * Print string and then terminate the line.
	 * @see PrintStream#println(String)
	 * @see PrintWriter#println(String)
	 * @param value String to be printed
	 */
	public void println(String value)
	{
		if (writer != null)
		{
			writer.println(value);
		}
		else if (stream != null)
		{
			stream.println(value);
		}
	}
	/**
	 * Print string.
	 * 
	 * @see PrintStream#print(String)
	 * @see PrintWriter#print(String)
	 * 
	 * @param value String to be printed
	 */
	public void print(String value)
	{
		if (writer != null)
		{
			writer.print(value);
		}
		else if (stream != null)
		{
			stream.print(value);
		}
	}
	/**
	 * Print a character.
	 * @see PrintStream#print(char)
	 * @see PrintWriter#print(char)
	 * @param c Character to be printed
	 */
	public void print(char c)
	{
		if (writer != null)
		{
			writer.print(c);
		}
		else if (stream != null)
		{
			stream.print(c);
		}
	}
	/**
	 * Print a character and terminate the line.
	 * 
	 * @see PrintStream#println(String)
	 * @see PrintWriter#println(String)
	 * @param c Character to be printed
	 */
	public void println(char c)
	{
		print(c);
		println();
	}
	/**
	 * Print an array of  characters.
	 * @see PrintStream#print(char[])
	 * @see PrintWriter#print(char[])
	 * @param c Character to be printed
	 */
	public void print(char c[])
	{
		if (writer != null)
		{
			writer.print(c);
		}
		else if (stream != null)
		{
			stream.print(c);
		}
	}
	/**
	 * Print an array of  characters and then terminate the line.
	 * @see PrintStream#println(char[])
	 * @see PrintWriter#println(char[])
	 * @param c Character to be printed
	 */
	public void println(char c[])
	{
		print(c);
		println();
	}
	/**
	 * Terminate the line.
	 * @see PrintStream#println()
	 * @see PrintWriter#println()
	 */
	public void println()
	{
		if (writer != null)
		{
			writer.println();
		}
		else if (stream != null)
		{
			stream.println();
		}
	}
	/**
	 * Append a character.
	 * 
	 * @see PrintStream#append(char)
	 * @see PrintWriter#append(char)
	 * @return PrintStream or PrintWriter object
	 */
	public Appendable append(char c)
	{
		if (writer != null)
		{
			writer.append(c);
		}
		else if (stream != null)
		{
			stream.append(c);
		}
		else
		{
			return (Appendable) this;
		}
		return (Appendable) this;
	}
	/**
	 * Append a character sequence to the output stream.
	 * @see PrintStream#append(CharSequence)
	 * @see PrintWriter#append(CharSequence)
	 * @param csq The character sequence to be be appended.
	 *
	 */
	public Appendable append(CharSequence csq)
	{
		if (writer != null)
		{
			writer.append(csq);
		}
		else if (stream != null)
		{
			stream.append(csq);
		}
		else
		{
			return (Appendable) this;
		}
		return (Appendable) this;
	}
	/**
	 * Append a subsequence of the specified character sequence to the output stream.
	 * @see PrintStream#append(CharSequence,int,int)
	 * @see PrintWriter#append(CharSequence, int, int)
	 * @param csq The character sequence from which a subsequence will be appended.  If csq is null, 
	 *  then characters will be appended as if csq contained the four characters "null".
	 *  @param start The index of the first character in the subsequence
	 *  @param end The index of the character following the last character in the subsequence
	 */
	public Appendable append(CharSequence csq, int start, int end) throws IndexOutOfBoundsException
	{
		if (writer != null)
		{
			writer.append(csq, start, end);
		}
		else if (stream != null)
		{
			stream.append(csq, start, end);
		}
		return (Appendable) this;
	}
	/**
	 * Close the output stream.
	 * @see PrintStream#close()
	 * @see PrintWriter#close()
	 */
	public void close()
	{
		if (writer != null)
		{
			writer.close();
			
		}
		else if (stream != null)
		{
			stream.close();
		}		
	}
	/**
	 * Flush the output stream.
	 * @see PrintStream#flush()
	 * @see PrintWriter#flush()
	 */
	public void flush()
	{
		if (writer != null)
		{
			writer.flush();
			
		}
		else if (stream != null)
		{
			stream.flush();
		}			
	}
	/**
	 * Print an integer value.
	 * @see Integer#toString(int)
	 * @see PrintStream#print(int)
	 * @see PrintWriter#print(int)
	 * @param value integer value to be printed
	 */
	public void print(int value)
	{
		print(Integer.toString(value));
	}
	/**
	 * Print an integer value and then terminate the line.
	 * @see Integer#toString(int)
	 * @see PrintStream#println(int)
	 * @see PrintWriter#println(int)
	 * @param value integer value to be printed
	 */
	public void println(int value)
	{
		print(Integer.toString(value));
		println();
	}
	/**
	 * Print a long integer value.
	 * @see Long#toString(long)
	 * @see PrintStream#println(long)
	 * @see PrintWriter#println(long)
	 * @param value long integer value to be printed
	 */
	public void print(long value)
	{
		print(Long.toString(value));
	}
	/**
	 * Print a long integer value and then terminate the line.
	 * @see Long#toString(long)
	 * @see PrintStream#println(long)
	 * @see PrintWriter#println(long)
	 * @param value long integer value to be printed
	 */
	public void println(long value)
	{
		print(Long.toString(value));
		println();
	}
	/**
	 * Print a boolean value.
	 * @see Boolean#toString(boolean)
	 * @see PrintStream#print(boolean)
	 * @see PrintWriter#print(boolean)
	 * @param value boolean value to be printed
	 */
	public void print(boolean value)
	{
		print(Boolean.toString(value));
	}
	/**
	 * Print a boolean value and then terminate the line.
	 * @see Boolean#toString(boolean)
	 * @see PrintStream#println(boolean)
	 * @see PrintWriter#println(boolean)
	 * @param value Boolean value to be printed
	 */
	public void println(boolean value)
	{
		print(Boolean.toString(value));
		println();
	}
	/**
	 * Print a double-precision floating-point number.
	 * @see PrintStream#print(double)
	 * @see PrintWriter#print(double)
	 * @param value Double-precision floating-point number to be printed
	 */
	public void print(double value)
	{
		print(Double.toString(value));
	}
	/**
	 * Print a double-precision floating-point number and
	 * then terminate the line.
	 * @see PrintStream#println(double)
	 * @see PrintWriter#println(double)
	 * @param value Double-precision floating-point number to be printed
	 */
	public void println(double value)
	{
		print(Double.toString(value));
		println();
	}
	/**
	 * Print a floating-point number.
	 * 
	 * @see PrintStream#println(float)
	 * @see PrintWriter#println(float)
	 * @param value Floating-point number to be printed
	 */
	public void print(float value)
	{
		print(Float.toString(value));
	}
	/**
	 * Print a floating-point number and
	 * then terminate the line.
	 * @see PrintStream#println(float)
	 * @see PrintWriter#println(float)
	 * @param value Floating-point number to be printed
	 */
	public void println(float value)
	{
		print(Float.toString(value));
		println();
	}
	/**
	 * Test driver.
	 * @param args Not used
	 * 
	 */
	public static void main(String args[])
	{
		StringWriter writer;
		System.out.println("Trying GenericPrinter for PrintStream System.out");
		GenericPrinter instance = new GenericPrinter(System.out);
		instance.println(instance.getUnderlyingObject().getClass().getName());
		instance.println("Test");
		instance.println(true);
		System.out.println("Trying GenericPrinter for StringWriter");
		writer = new StringWriter();
		instance = new GenericPrinter(writer);
		instance.println(instance.getUnderlyingObject().getClass().getName());
		instance.println("Test");
		instance.println(true);
		System.out.println(instance.getUnderlyingObject().toString());
		System.out.println("Using PrintWriter");
		writer = new StringWriter();
		instance = new GenericPrinter(new PrintWriter(writer));
		instance.println(instance.getUnderlyingObject().getClass().getName());
		instance.println("Test");
		instance.println(true);
		System.out.println(writer.toString());
		System.out.println("End of program");
	}
}
