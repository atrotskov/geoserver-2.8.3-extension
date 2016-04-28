import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.geoserver.catalog.Catalog;

public class HelloWorld {
	
	private Catalog catalog;
	
	public HelloWorld() {
		// do nothing
	}

	public HelloWorld(Catalog catalog) {
		this.catalog = catalog;
	}

	public void sayHello(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String param = request.getParameter("myParam");
		
		String url = catalog.getCoverageStore(param).getURL();

		response.getOutputStream().write(url.getBytes());
	}
}