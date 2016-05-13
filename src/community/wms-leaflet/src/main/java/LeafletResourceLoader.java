import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.geoserver.catalog.Catalog;
import org.geoserver.config.GeoServerDataDirectory;

public class LeafletResourceLoader {
	
	private Catalog catalog;
	private GeoServerDataDirectory geoServerDataDir;

	private static final String TEXT_RESPONSE_FILE_NOT_FOUND = "Looks like server can't find specified file.";

	public LeafletResourceLoader(Catalog catalog, GeoServerDataDirectory geoServerDataDir) {
		this.catalog = catalog;
		this.geoServerDataDir = geoServerDataDir;
	}

	public void getRes(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, URISyntaxException {
		ServletContext context = request.getSession().getServletContext();
		String path = request.getParameter("path");
		ClassLoader classLoader = LeafletResourceLoader.class.getClassLoader();
		
		try (	InputStream inputStream = classLoader.getResourceAsStream(path);
				OutputStream outputStream = response.getOutputStream()) {
			String mimeType = context.getMimeType(classLoader.getResource(path).toURI().toString());
			if (mimeType == null) {
				mimeType = "text/plain";
			}
			response.setContentType(mimeType);
			byte[] buffer = new byte[4096];
			int bytesRead = -1;
			while ((bytesRead = inputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, bytesRead);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void getSource(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, URISyntaxException {
		String layerName = request.getParameter("layer");
		String dataDir = geoServerDataDir.root().getAbsolutePath();
		String coverageId = catalog.getLayerByName(layerName).getResource().getId();
		String coverageStoreId = catalog.getCoverage(coverageId).getStore().getId();
		String pathFromXml = catalog.getCoverageStore(coverageStoreId).getURL();
		
		File downloadFile = null;
		try {
			downloadFile = getFile(dataDir, pathFromXml);
		} catch (FileNotFoundException e) {
			response.getWriter().write(TEXT_RESPONSE_FILE_NOT_FOUND + e);
		}
		
		try (	FileInputStream inputStream = new FileInputStream(downloadFile);
				OutputStream outputStream = response.getOutputStream()) {
			response.setContentType("application/octet-stream");
			response.setContentLength((int) downloadFile.length());

			String headerKey = "Content-Disposition";
			String headerValue = String.format("attachment; filename=\"%s\"", downloadFile.getName());
			response.setHeader(headerKey, headerValue);

			byte[] buffer = new byte[4096];
			int bytesRead = -1;

			while ((bytesRead = inputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, bytesRead);
			}
		}catch (IOException e) {
			e.printStackTrace();
		}
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
