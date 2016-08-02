<#setting locale="en_US">
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>Leaflet Map Template</title>
    <link rel="stylesheet" href="${baseUrl}/ows?request=getRes&service=leafletResourcesService&version=1.0.0&path=leaflet/leaflet.css" />
    <link rel="stylesheet" href="${baseUrl}/ows?request=getRes&service=leafletResourcesService&version=1.0.0&path=easy-button/easy-button.css" />
    <link rel="stylesheet" href="${baseUrl}/ows?request=getRes&service=leafletResourcesService&version=1.0.0&path=leaflet-draw/leaflet.draw.css" />
</head>
<body>
    <style>
        body {
            padding: 0;
            margin: 0;
        }
        
        html,
        body,
        #map {
            height: 100%;
            width: 100%;
        }
    </style>

    <div id="map"></div>
	
    <script src="${baseUrl}/ows?request=getRes&service=leafletResourcesService&version=1.0.0&path=leaflet/leaflet.js"></script>
    <script src="${baseUrl}/ows?request=getRes&service=leafletResourcesService&version=1.0.0&path=easy-button/easy-button.js"></script>
    <script src="${baseUrl}/ows?request=getRes&service=leafletResourcesService&version=1.0.0&path=jquery/jquery-2.2.3.js"></script>
    <script src="${baseUrl}/ows?request=getRes&service=leafletResourcesService&version=1.0.0&path=jquery/jquery.fileDownload.js"></script>
    <script src="${baseUrl}/ows?request=getRes&service=leafletResourcesService&version=1.0.0&path=leaflet-draw/leaflet.draw.js"></script>
    <script src="${baseUrl}/ows?request=getRes&service=leafletResourcesService&version=1.0.0&path=leaflet-draw/leaflet.draw-src.js"></script>

    <script>
        // Base layers
		var geoServerWmsLayer = L.tileLayer.wms('${baseUrl}/${servicePath}?', {
		layers: '${layerName}',
		format: 'image/png',
		transparent: true
		});
		
        var layerOSM = new L.tileLayer('http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
            attribution: 'Map data &copy; <a href="https://openstreetmap.org/copyright">OpenStreetMap</a> contributors'
        });

        var layerMapQuest = new L.tileLayer("http://{s}.mqcdn.com/tiles/1.0.0/osm/{z}/{x}/{y}.png", {
            subdomains: ["otile1", "otile2", "otile3", "otile4"],
            attribution: '&copy; Tiles courtesy of <a href="http://www.mapquest.com/" target="_blank">MapQuest</a>. &copy; Map data <a href="http://www.openstreetmap.org/" target="_blank">OpenStreetMap</a> contributors'
        });

        var BingLayer = L.TileLayer.extend({
            getTileUrl: function(tilePoint) {
                this._adjustTilePoint(tilePoint);
                return L.Util.template(this._url, {
                    s: this._getSubdomain(tilePoint),
                    q: this._quadKey(tilePoint.x, tilePoint.y, this._getZoomForUrl())
                });
            },
            _quadKey: function(x, y, z) {
                var quadKey = [];
                for (var i = z; i > 0; i--) {
                    var digit = '0';
                    var mask = 1 << (i - 1);
                    if ((x & mask) != 0) {
                        digit++;
                    }
                    if ((y & mask) != 0) {
                        digit++;
                        digit++;
                    }
                    quadKey.push(digit);
                }
                return quadKey.join('');
            }
        });

        var layerBingAerial = new BingLayer('http://t{s}.tiles.virtualearth.net/tiles/a{q}.jpeg?g=2732', {
            subdomains: ['0', '1', '2', '3', '4'],
            attribution: '&copy; <a href="http://bing.com/maps">Bing Maps</a>'
        });

        var layerMapboxImagery = new L.tileLayer('http://{s}.tiles.mapbox.com/v4/openstreetmap.map-inh7ifmo/{z}/{x}/{y}.png?access_token=pk.eyJ1Ijoib3BlbnN0cmVldG1hcCIsImEiOiJhNVlHd29ZIn0.ti6wATGDWOmCnCYen-Ip7Q', {
            attribution: '&copy; <a href="https://www.mapbox.com/about/maps/">Mapbox</a>'
        });
        
        var southWest = L.latLng(${minY}, ${minX}),
        	northEast = L.latLng(${maxY}, ${maxX}),
        	bounds = L.latLngBounds(southWest, northEast);    
    
        var map = L.map('map', {
            layers: [geoServerWmsLayer, layerOSM],
            /*zoom: 13,
            center: bounds.getCenter()*/
        });
        
        map.fitBounds(bounds);
                
        var baseLayers = {
            "OpenStreetMap": layerOSM,
            "MapQuest <div class='leaflet-control-layers-separator' style='margin-right: -6px;'></div>": layerMapQuest,
            "Bing Aerial": layerBingAerial,
            "Mapbox Imagery": layerMapboxImagery
        };
        
        var overlayLayers = {
			"${layerName}": geoServerWmsLayer
        };

        var overlayLayerControl = L.control.layers(baseLayers, overlayLayers, {   
            collapsed: false
        }).addTo(map);
        
        var helloPopup = L.popup().setContent('Hello World!');

		L.easyButton('<strong>&veeeq;</strong>', function(){
			var URL = '${baseUrl}/ows?request=getSource&service=leafletResourcesService&version=1.0.0&layer=${layerName}';
			$.fileDownload(URL);
		}).addTo(map);
		
		var featureGroup = new L.featureGroup();
        map.addLayer(featureGroup);
		
		
		// add draw polygon control
		var drawControl = new L.Control.Draw({
			position: 'topright',
			draw: {
				polyline: false,
				polygon: {
					allowIntersection: false,
					showArea: true,
					drawError: {
						color: '#b00b00',
						timeout: 1000
					},
					shapeOptions: {
                        color: '#8b0000',
                        clickable: false,
                        fillOpacity: 0.1,
                        weight: 4
					}
				},
				rectangle: false,
				circle: false,
				marker: false
			},
			edit: {
				featureGroup: featureGroup,
				remove: false,
                edit: false
			}
		});
		map.addControl(drawControl);
        
                
        map.on('draw:created', function (e) {
            var layer = e.layer;
            
            featureGroup.clearLayers();
            featureGroup.addLayer(layer);
 
            var polygonData = JSON.stringify(layer.toGeoJSON());
            var sendURL = "${baseUrl}/ows";
            $.get(sendURL,{
            	request: "sendVolume",
            	service: "volumeCalculatorService",
            	version: "1.0.0",
            	layer:   "${layerName}",
            	geoData: polygonData
            }).done(function(data){
            	alert("Response is: " + data)
            });            
            /*alert(polygonData);*/
 
        });
        
                
        L.easyButton('<span class="send-btn">&curren;</span>', function(){
            var data = featureGroup.toGeoJSON()   
            alert(JSON.stringify(data));
        }).addTo(map);
		
    </script>
</body>
</html>