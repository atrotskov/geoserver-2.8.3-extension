import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.geoserver.config.GeoServer;
import org.geoserver.web.demo.MapPreviewPage;
import org.geoserver.web.demo.PreviewLayer;

public class SourceDownloadLinkPage extends MapPreviewPage {

	SourceDownloadLinkPage(GeoServer gs) {
		
		String id = gs.getSettings().getId();
		
		IModel itemModel = new Model();  
        
            PreviewLayer layer = (PreviewLayer) itemModel.getObject();

               // openlayers preview
               Fragment f = new Fragment(id, "sourceDownloadLink", SourceDownloadLinkPage.this);
               final String olUrl = layer.getWmsLink() + "&format=application/openlayers";
               f.add(new ExternalLink("srcLink", olUrl, "Source"));
            
        
	}
}
