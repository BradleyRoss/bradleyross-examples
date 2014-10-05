package bradleyross.j2ee.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.apache.log4j.Logger;
// import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
/**
 * Sample filter writing to log.
 * @author Bradley Ross
 *
 */
public class ExampleFilter implements Filter {
	protected Logger logger = null;
    protected FilterConfig filterConfig = null;
	public ExampleFilter() { ; }

	@Override
	public void destroy() { ; }

	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1,
			FilterChain arg2) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) arg0;
		logger.info("Running " + request.getRequestURI());
		// filterConfig.getServletContext().log("Running " +  request.getRequestURI());
		arg2.doFilter(arg0,  arg1);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		filterConfig = arg0;
		logger = Logger.getLogger(this.getClass());

	}

}
