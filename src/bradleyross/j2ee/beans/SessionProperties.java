package bradleyross.j2ee.beans;
// import java.util.Map;
import java.util.Enumeration;
import javax.faces.context.FacesContext;
// import javax.faces.context.ExternalContext;
// import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ManagedBean;
/**
 * This bean covers the session properties for
 * the application.
 * @author Bradley Ross
 *
 */
@ManagedBean
@SessionScoped
public class SessionProperties {
	public SessionProperties() {
		testValue = 0;
	}
	/**
	 * True if user has logged in with a valid username
	 * and password.  False otherwise.
	 */
	protected boolean authorized = false;
	public boolean getAuthorized() {
		return authorized;
	}
	public void setAuthorized(boolean value) {
		authorized = value;
	}
	/**
	 * Account name for user.
	 */
	protected String userName = new String();
	public String getUserName() {
		return userName;
	}
	public void setUserName(String value) {
		userName = value;
	}
	public Enumeration<String> getSessionAttributes() {
		HttpSession session =
				(HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);

		Enumeration<String> attributes = 
				session.getAttributeNames();
		return attributes;
	}
	private int testValue;
	public int getTestValue() {
		testValue++;
		return testValue;
	}
}