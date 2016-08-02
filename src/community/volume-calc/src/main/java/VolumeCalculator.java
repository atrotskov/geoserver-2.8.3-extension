import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.geoserver.catalog.Catalog;
import org.geoserver.config.GeoServerDataDirectory;
import org.geotools.geometry.jts.JTSFactoryFinder;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Polygon;

public class VolumeCalculator {
	
	
	private Catalog catalog;
	private GeoServerDataDirectory geoServerDataDir;

	private static final String TEXT_RESPONSE_FILE_NOT_FOUND = "Looks like server can't find specified file.";

	public VolumeCalculator(Catalog catalog, GeoServerDataDirectory geoServerDataDir) {
		this.catalog = catalog;
		this.geoServerDataDir = geoServerDataDir;
	}

	public void sendVolume(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, URISyntaxException {
		String layerName = request.getParameter("layer");
		String geoJSON = request.getParameter("geoData");
		String dataDir = geoServerDataDir.root().getAbsolutePath();
		String coverageId = catalog.getLayerByName(layerName).getResource().getId();
		String coverageStoreId = catalog.getCoverage(coverageId).getStore().getId();
		String pathFromXml = catalog.getCoverageStore(coverageStoreId).getURL();
		
		Coordinate[] coords = GeoTiffUtils.extractCoordinates(geoJSON);
		GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();
		Polygon polygon = geometryFactory.createPolygon(coords);
		
		File downloadFile = null;
		try {
			downloadFile = getFile(dataDir, pathFromXml);
		} catch (FileNotFoundException e) {
			response.getWriter().write(TEXT_RESPONSE_FILE_NOT_FOUND + e);
		}
			
		
		response.getWriter().write("all work");
	}

		
	private File getFile(String geoServerDataDir, String fileFromXml) throws FileNotFoundException {
		
		// Replace path divider from "\" to "/" if need
		geoServerDataDir = geoServerDataDir.replace("\\", "/");
		fileFromXml = fileFromXml.replace("\\", "/");
		
		// Trim word "file://" or "file:" from getting URL
		if (fileFromXml.startsWith("file://")) {
			fileFromXml = fileFromXml.substring(7);
		} else {
			fileFromXml = fileFromXml.substring(5);
		}
		
		// Set absolute path location
		String fullPath = geoServerDataDir + "/" + fileFromXml;
						
		File file = new File(fullPath);
		if(!file.exists()) {
			file = new File(fileFromXml);
		}
		if (!file.exists()) {
			String e = "1st) tried use this location: " + fileFromXml +
					" 2nd) tried use this location: " + fullPath;
			throw new FileNotFoundException(e);
		}
		return file;
	}
}
