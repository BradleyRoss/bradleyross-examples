package bradleyross.j2ee.beans;
import java.io.Serializable;
import java.util.Map;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.context.ExternalContext;
// import bradleyross.j2ee.beans.SessionProperties;
/*
 * http://stackoverflow.com/questions/550448/get-request-and-session-parameters-and-attributes-from-jsf-pages
 */
/**
 * Sample bean for login information.
 * 
 * <p>Uses the annotations {@link ManagedBean} and {@link RequestScoped}.</p>
 * 
 * <p> It appears that I can't use the implicit objects for JSF expression language
 *     (param, request, response, etc.) in the
 *     ManagedBean annotations.</p>
 * 
 * @author Bradley Ross
 * 
 *
 */
@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class LoginBean implements Serializable {
	/**
	 * This constructor reads the HTTP request parameter name and places
	 * it in the user name field of the form.
	 * 
	 * <p>I had previously tried using #{param} in the
	 *    ManagedBean annotation for a property, but it
	 *    didn't seem to work.  Some messages on the web 
	 *    indicated that it worked, but it may be implementation
	 *    dependent.</p>
	 * <p>The constructor will set the userName property to the value of the
	 *    "name" parameter if the parameter is present.</p>
	 * @see FacesContext
	 * @see ExternalContext
	 */
	public LoginBean() {
		String name = 
				FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("name");
		if (name == null || name.length() == 0) {
			userName = new String();
		} else {
			userName = name;
		}
	}
	/**
	 * User name.
	 * <p>The annotation {@link ManagedProperty}
	 * apparently controls actions by the JSF 
	 * handler with regard to variables.</p>
	 */

	private String userName;
	public String getUserName() {
		return userName;
	}
	public void setUserName(String value) {
		if (value == null) return;
		userName = value;
	}
	/**
	 * User login password.
	 */
	private String password = new String();
	public String getPassword() {
		return password;
	}
	public void setPassword(String value) {
		password = value;
	}
	/**
	 * Determines whether the combination of user name and user password represents
	 * a valid user.
	 * 
	 * <p>In this case, the method accepts any login attempt using "password"
	 *    for the password.  In a real application, a more complicated
	 *    algorithm would be used.</p>
	 * @return "login" if properties are valid, otherwise "error"
	 */
	
	public String authorized() {
		if (password.trim().equalsIgnoreCase("password")) {
			Map<String,Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
			SessionProperties tmp = (SessionProperties) sessionMap.get("sessionProperties");
			if (tmp != null ) {
				tmp.setUserName(userName);
				tmp.setAuthorized(true);
			}
			return "login";
		} else {
			return "error";
		}

	}
	/**
	 * Diagnostic routine listing session, application, and
	 * request attributes.
	 * 
	 * @return description 
	 * @see FacesContext
	 * @see ExternalContext
	 */
	public String getList() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext externalContext = facesContext.getExternalContext();
		Map<String,Object> sessionMap = externalContext.getSessionMap();
		Map<String,Object> applicationMap = externalContext.getApplicationMap();
		Map<Object,Object> requestMap = facesContext.getAttributes();
		StringBuilder builder = new StringBuilder();
		builder.append("\r\n\r\nApplication Attributes\r\n\r\n");
		for( Map.Entry<String,Object> entry :  applicationMap.entrySet()) {

			builder.append("(" + entry.getKey() + " : " + entry.getValue().getClass().getName() + " : " +
					entry.getValue().toString() + ")\r\n ");
		}
		builder.append("\r\n\r\nSession Attributes\r\n\r\n");
		for( Map.Entry<String,Object> entry :  sessionMap.entrySet()) {

			builder.append("(" + entry.getKey() + " : " + entry.getValue().getClass().getName() + " : " +
					entry.getValue().toString() + ")\r\n ");
		}
		builder.append("\r\nRequest Attributes\r\n");
		if (requestMap != null && !requestMap.isEmpty()) {
			for( Map.Entry<Object,Object> entry :  requestMap.entrySet()) {
				String string1 = " null value ";
				String string2 = " null value ";
				String string3 = " null value ";
				String string4 = " null value ";
				Object key = entry.getKey();
				if (key != null) {
					string1 = key.getClass().getName();
					string2 = key.toString();
				}
				Object value = entry.getValue();
				if (value != null) {
					string3 = value.getClass().getName();
					string4 = value.toString();
				}
				builder.append("(" + string1 + " : " + 
						string2 + " : " + string3 + " : " +
						string4 + ")\r\n ");
			}	
		}else {
			builder.append("  No request attributes" );
		}
		builder.append("\r\n");
		return builder.toString();
	}

	/**
	 * I believe that the ManagedProperty annotation in
	 * this method will cause the sessionProperties
	 * bean to be created at the same time as the
	 * loginBean.
	 */
    @ManagedProperty("#{sessionProperties}")
	private SessionProperties sessionBean;
	public SessionProperties getSessionBean() {
		return sessionBean;
	}
	public void setSessionBean(SessionProperties value) {
		sessionBean = value;
	}
}
