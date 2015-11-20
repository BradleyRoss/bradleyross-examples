package bradleyross.j2ee.servlets;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServlet;
import bradleyross.library.helpers.ExceptionHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@SuppressWarnings("serial")
public class DatabaseExceptions extends HttpServlet {
protected Logger logger = LoggerFactory.getLogger(this.getClass());
protected ExceptionHelper extra = new ExceptionHelper(logger);
public void doGet(HttpServletRequest request, HttpServletResponse response) {
	
}

}
