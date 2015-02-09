package bradleyross.j2ee.beans;
import java.util.Map;
import java.io.Serializable;
// import java.util.Enumeration;
import javax.faces.context.FacesContext;
//import javax.faces.context.ExternalContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
/** 
 * Request scoped sample bean.
 * 
 * <p><a href="http://incepttechnologies.blogspot.com/p/jsf-implicit-objects.html" target="_blank">
 *    JSF implicit objects can be found here.</a></p>
 * <p><a href="http://docs.oracle.com/javaee/7/tutorial/doc/jsf-custom012.htm" target="_blank">
 *    http://docs.oracle.com/javaee/7/tutorial/doc/jsf-custom012.htm</a></p>
 * <p>If the bean returns a collection, the Oracle documentation says that you can get a
 *    specific value by following it by the index in square brackets.  It also indicates that 
 *    something similar can be done for Maps, but this may only apply to specific EL and
 *    JSF implementations.</p>
 * <p>It may be that the implicit beans are handled differently and aren't fully available until
 *    after the managed beans are done with the property management.</p>
 *    
 * @author Bradley Ross
 *
 */
@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class RequestProperties implements Serializable {

	public RequestProperties() {
		
	}
	/**
	 * Implicit object.
	 */
	@ManagedProperty("#{request}")
	protected HttpServletRequest request;
	public HttpServletRequest getRequest() {
		return request;
	}
	public void setRequest (HttpServletRequest value) {
		request = value;
	}
	/**
	 * Implicit object.
	 */
	@ManagedProperty("#{response}")
	protected HttpServletResponse response;
	public HttpServletResponse getResponse() {
		return response;
	}
	public void setResponse(HttpServletResponse value) {
		response = value;
	}
	/**
	 * Implicit Object.
	 * 
	 * <p>This managed property was also specified in
	 *    a managed-property tag in
	 *    WEB-INF/faces-config.xml
	 */
	@ManagedProperty("#{param}")
	protected Map<String,String> param;
	public Map<String,String> getParam() {
		return param;
	}
	/**
	 * Setter for {@link #param} managed bean.
	 * @param value 
	 */
	public void setParam(Map<String,String> value) {
		param=value;
	}
	/**
	 * Reference to implicit object.
	 * 
	 * <p>This property is set by a managed-property
	 *    statement in WEB-INF/faces-config.xml.</p>
	 */
	protected Map<String,String> paramXml;
	public Map<String,String> getParamXml() {
		return paramXml;
	}
	public void setParamXml(Map<String,String> value) {
		paramXml = value;
	}
	@ManagedProperty("#{param}")
	protected Map<String,String> paramAnn;
	public Map<String,String> getParamAnn() {
		return paramAnn;
	}
	public void setParamAnn(Map<String,String> value) {
		paramAnn = value;
	}
	/**
	 * Implicit object.
	 */
	// ManagedProperty("#{cookie}")
	protected Map<String,String> cookie;
	public Map<String,String> getCookie() {
		return cookie;
	}
	public void setCookie(Map<String,String> value) {
		cookie = value;
	}
	/**
	 * Implicit object.
	 */
	@ManagedProperty("#{session}")
	protected HttpSession session;
	public HttpSession getSession() {
		return session;
	}
	public void setSession(HttpSession value) {
		session = value;
	}
	/**
	 * Implicit object.
	 */
	// ManagedProperty("#{facesContext}")
	protected FacesContext facesContext;
	public FacesContext getFacesContext() {
		return facesContext;
	}
	public void setFacesContext(FacesContext value) {
		facesContext = value;
	}
	/**
	 * Managed bean property for the name parameter in the
	 * HTTP call.
	 * <p>Perhaps I can use implicit objects with properties
	 * in the ManageProperty annotation, but not
	 * reference the implicit object directly.</p>
	 */
	@ManagedProperty(value="#{param.name}")
	protected String test1;
	public String getTest1() {
		return test1;
	}
	public void setTest1(String value) { 
		test1 = value; }

}
