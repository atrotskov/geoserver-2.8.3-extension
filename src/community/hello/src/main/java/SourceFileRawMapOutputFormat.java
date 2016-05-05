import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.geoserver.catalog.Catalog;
import org.geoserver.config.GeoServerDataDirectory;
import org.geoserver.platform.ServiceException;
import org.geoserver.wms.GetMapRequest;
import org.geoserver.wms.MapLayerInfo;
import org.geoserver.wms.MapProducerCapabilities;
import org.geoserver.wms.WMSMapContent;
import org.geoserver.wms.map.AbstractMapOutputFormat;
import org.geoserver.wms.map.RawMap;

public class SourceFileRawMapOutputFormat extends AbstractMapOutputFormat {
	
	/** the only MIME type this map producer supports.
	 * Actually in this place should be MIME_TYPE = application/octet-stream,
	 * but this hack allows to not edit GeoServerApplication.property were should be placed string
	 * format.wms.application/octet-stream=Source (RAW format)
	 * This hack produce some warnings in the geoServer log, but they can be ignored.*/
    static final String FAKE_MIME_TYPE = "Source (RAW format)";
    static final String REAL_MIME_TYPE = "application/octet-stream";
    static final String HEADER_KEY = "Content-Disposition";
    
    private Catalog catalog;
    private GeoServerDataDirectory dataDir;
    
    /*private static final Set<String> outputFormats = new HashSet<String>(Arrays.asList("Source file (RAW)"));*/
    
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
    
    
    
    public SourceFileRawMapOutputFormat(Catalog catalog, GeoServerDataDirectory dataDir) {    	
        super(FAKE_MIME_TYPE/*, outputFormats*/);
        this.catalog = catalog;
        this.dataDir = dataDir;
    }
    
	@Override
	public RawMap produceMap(WMSMapContent mapContent) throws ServiceException, IOException {
		
		GetMapRequest request = mapContent.getRequest();
		// get file location from coverageStore XML
		List<MapLayerInfo> layers =  request.getLayers();
		String coverageId = layers.get(0).getLayerInfo().getResource().getId();  // проверить один ли слой
		String coverageStoreId = catalog.getCoverage(coverageId).getStore().getId();
		String rawPath = catalog.getCoverageStore(coverageStoreId).getURL();
		
		// get geoServer Data Directory
		String geoServerDataDir = dataDir.root().getAbsolutePath();
		
		File downloadFile = this.getFile(geoServerDataDir, rawPath);
		FileInputStream inputStrem = new FileInputStream(downloadFile);
		String headerKey = HEADER_KEY;
		String headerValue = String.format("attachment; filename=\"%s\"", downloadFile.getName());	
		RawMap result = new RawMap(mapContent, inputStrem, FAKE_MIME_TYPE);
		result.setMimeType(REAL_MIME_TYPE);
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