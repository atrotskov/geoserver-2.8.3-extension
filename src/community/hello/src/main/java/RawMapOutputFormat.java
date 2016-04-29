import java.io.IOException;
import java.io.InputStream;

import org.geoserver.platform.ServiceException;
import org.geoserver.wms.MapProducerCapabilities;
import org.geoserver.wms.WMSMapContent;
import org.geoserver.wms.map.AbstractMapOutputFormat;
import org.geoserver.wms.map.RawMap;

public class RawMapOutputFormat extends AbstractMapOutputFormat {
	
	/** the only MIME type this map producer supports */
    static final String MIME_TYPE = "application/octet-stream";
    
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
    
    public RawMapOutputFormat() {
        super(MIME_TYPE);
    }
    
	@Override
	public RawMap produceMap(WMSMapContent mapContent) throws ServiceException, IOException {
		RawMap result = new RawMap(mapContent, input, MIME_TYPE);
		result.setContentDispositionHeader(mapContent, ""); // TODO Check this method atrotskov
		result.setMimeType(MIME_TYPE);
		return result;
	}

	@Override
	public MapProducerCapabilities getCapabilities(String format) {
		return CAPABILITIES;
	}

}