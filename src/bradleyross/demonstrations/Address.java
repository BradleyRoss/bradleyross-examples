package bradleyross.demonstrations;
import java.net.InetAddress;
/**
 * See what information I can get about local system.
 * @author Bradley Ross
 *
 */
public class Address extends Object
{

	/**
	 * @param args Not used
	 */
	public static void main(String[] args) {
		try
		{
			InetAddress local = InetAddress.getLocalHost();
			System.out.println("Canonical host name is " + local.getCanonicalHostName());
			System.out.println("Host name is " + local.getHostName());
			System.out.println("OS name is " + System.getProperty("os.name"));
		}
		catch (Exception e)
		{
			System.out.println(e.getClass().getName());
			System.out.println(e.getMessage());
			e.printStackTrace(System.out);
		}

	}

}
