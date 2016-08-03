import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.geoserver.catalog.Catalog;
import org.geoserver.config.GeoServerDataDirectory;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.io.GridCoverage2DReader;
import org.geotools.gce.geotiff.GeoTiffReader;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.opengis.coverage.grid.GridEnvelope;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.TransformException;

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
			throws ServletException, IOException, URISyntaxException, TransformException, FactoryException {
		String layerName = request.getParameter("layer");
		String geoJSON = request.getParameter("geoData");
		String dataDir = geoServerDataDir.root().getAbsolutePath();
		String coverageId = catalog.getLayerByName(layerName).getResource().getId();
		String coverageStoreId = catalog.getCoverage(coverageId).getStore().getId();
		String pathFromXml = catalog.getCoverageStore(coverageStoreId).getURL();
		
		/*Получаем файл, получаем необходимую информацию о GeoTIFF*/		
		GridCoverage2DReader reader = new GeoTiffReader(getFile(dataDir, pathFromXml));
		GridEnvelope dimensions = reader.getOriginalGridRange();
		GridCoverage2D coverage = reader.read(null);
		
		/*Обрабатываем geoJson, получаем координаты, создаем по координатам полигон*/
		Coordinate[] coords = GeoTiffUtils.extractCoordinates(geoJSON);
		coords = GeoTiffUtils.convertCoordinates(coverage, coords);
		Coordinate[] extrimeCorners = GeoTiffUtils.getExtrimeCorners(coords);
		GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();
		Polygon polygon = geometryFactory.createPolygon(coords);
		
		
		
		/*Проверим попадает ли полигон на картинку геоТиффа*/
		if(GeoTiffUtils.isCoordinatesInTheBorder(coords, coverage)) {
			response.getWriter().write("Inside poligon");
		} else {
			response.getWriter().write("Outside polygon");
		}
		
		
		
		
		
		/*Получаем размер пикселя*/
		
		
	
		
		/*Получаем размер пикселя - ОКОНЧАНИЕ*/
		
		
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
