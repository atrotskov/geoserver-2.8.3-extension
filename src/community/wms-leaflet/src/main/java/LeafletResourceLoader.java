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
		InputStream inputStream = null;
		OutputStream outputStream = null;
		try {
			String mimeType = context.getMimeType(classLoader.getResource(path).toURI().toString());
			if (mimeType == null) {
				mimeType = "text/plain";
			}
			response.setContentType(mimeType);
			inputStream = classLoader.getResourceAsStream(path);			
			outputStream = response.getOutputStream();
			byte[] buffer = new byte[4096];
			int bytesRead = -1;
			
			while ((bytesRead = inputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, bytesRead);
			}
		} catch (FileNotFoundException e) {
			response.getWriter().write(TEXT_RESPONSE_FILE_NOT_FOUND + e);
		} catch (IOException e) {
			response.getWriter().write(TEXT_RESPONSE_FILE_NOT_FOUND + e);
		} finally {
			if (inputStream != null) {
				inputStream.close();
			}
			if (outputStream != null) {
				outputStream.close();
			}
		}
	}

	public void getSource(HttpServletRequest request, HttpServletResponse response) {
		String layerName = request.getParameter("layer");
		String dataDir = geoServerDataDir.root().getAbsolutePath();
		String coverageId = catalog.getLayerByName(layerName).getResource().getId();
		String coverageStoreId = catalog.getCoverage(coverageId).getStore().getId();
		String pathFromXml = catalog.getCoverageStore(coverageStoreId).getURL();
		
		FileInputStream inputStrem = null;
		OutputStream outputStream = null;
		try {
			File downloadFile = getFile(dataDir, pathFromXml);
			inputStrem = new FileInputStream(downloadFile);
			response.setContentType("application/octet-stream");
			response.setContentLength((int) downloadFile.length());

			String headerKey = "Content-Disposition";
			String headerValue = String.format("attachment; filename=\"%s\"", downloadFile.getName());
			response.setHeader(headerKey, headerValue);

			outputStream = response.getOutputStream();

			byte[] buffer = new byte[4096];
			int bytesRead = -1;

			while ((bytesRead = inputStrem.read(buffer)) != -1) {
				outputStream.write(buffer, 0, bytesRead);
			}
		} catch (FileNotFoundException e) {
			
		} catch (IOException e) {
			
		} finally {
			if (inputStrem != null) {
				try {
					inputStrem.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	public File getFile(String geoServerDataDir, String fileFromXml) throws FileNotFoundException {
		
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
