import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.CatalogRepository;

public class MyCatalog {
	static CatalogRepository catRepo = new CatalogRepository();
	static Catalog cat = catRepo.getCatalog();

}
