import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.geoserver.catalog.Catalog;

public class HelloWorld {

  public HelloWorld() {
    // Do nothing
  }

  public void sayHello(HttpServletRequest request, HttpServletResponse response)
  throws ServletException, IOException {
	String param = request.getParameter("myParam");
		
	/*CatalogRepository catRepo = new CatalogRepository();
	Catalog cat = catRepo.getCatalog();*/
	
	Catalog mcat = MyCatalog.cat;
	
	String URL = "fdsfsdfsdfsdfsdf"; /*cat.getCoverageStore(param).getURL();*/
		
    response.getOutputStream().write( URL.getBytes());
  }
}