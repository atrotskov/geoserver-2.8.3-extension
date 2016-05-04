import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.geoserver.catalog.Catalog;
import org.geoserver.config.GeoServer;
import org.geoserver.config.GeoServerDataDirectory;
import org.geoserver.platform.ServiceException;
import org.geoserver.wms.GetMapRequest;
import org.geoserver.wms.MapLayerInfo;
import org.geoserver.wms.MapProducerCapabilities;
import org.geoserver.wms.WMSMapContent;
import org.geoserver.wms.map.AbstractMapOutputFormat;
import org.geoserver.wms.map.RawMap;

public class MyRawMapOutputFormat extends AbstractMapOutputFormat {
	
	/** the only MIME type this map producer supports */
    static final String MIME_TYPE = "application/octet-stream";
    
    private GeoServer gs;
    private Catalog catalog;
    private GeoServerDataDirectory dataDir;
    
    private InputStream input;
    
    /** 
     * Default capabilities for RAW format.
     * 
     * <p>
     * <ol>
     *         <li>tiled = unsupported</li>
     *         <li>multipleValues = unsupported</li>
     *         <li>paletteSupported = unsupported</li>
     *         <li>transparency = unsupported</li>
     * </ol>
     */
    private static MapProducerCapabilities CAPABILITIES = new MapProducerCapabilities(false, false, false, false, null);
    
    
    
    public MyRawMapOutputFormat(GeoServer gs, GeoServerDataDirectory dataDir) {    	
        super(MIME_TYPE);
        this.gs = gs;
        this.dataDir = dataDir;
        catalog = gs.getCatalog();
    }
    
	@Override
	public RawMap produceMap(WMSMapContent mapContent) throws ServiceException, IOException {
		
		GetMapRequest request = mapContent.getRequest();
		
		List<MapLayerInfo> layers =  request.getLayers();
		String coverageId = layers.get(0).getLayerInfo().getResource().getId();  // проверить один ли слой
		String coverageStoreId = catalog.getCoverage(coverageId).getStore().getId();
		String rawPath = catalog.getCoverageStore(coverageStoreId).getURL();
		
		String geoServerDataDir = dataDir.root().getAbsolutePath();
		
		File downloadFile = this.getFile(geoServerDataDir, rawPath);
		FileInputStream inputStrem = null;
		
		inputStrem = new FileInputStream(downloadFile);
		
		
		

		
		String headerKey = "Content-Disposition";
		String headerValue = String.format("attachment; filename=\"%s\"", downloadFile.getName());
		
		
		
		RawMap result = new RawMap(mapContent, inputStrem, MIME_TYPE);
		//result.setContentDispositionHeader(mapContent, ""); // TODO Check this method atrotskov
		result.setMimeType(MIME_TYPE);
		result.setResponseHeader(headerKey, headerValue);
		return result;
	}

	@Override
	public MapProducerCapabilities getCapabilities(String format) {
		return CAPABILITIES;
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
				
		String fullPath = geoServerDataDir + "/" + fileFromXml;
						
		File file = new File(fullPath);
		
		if(!file.exists()) {
			file = new File(fileFromXml);
		}
		if (!file.exists()) {
			String e = "<div>1st) tried use this location: " + fileFromXml +
					"</div><div>2nd) tried use this location: " + fullPath + "</div>";
			throw new FileNotFoundException(e);
		}
		
		return file;
	}


}