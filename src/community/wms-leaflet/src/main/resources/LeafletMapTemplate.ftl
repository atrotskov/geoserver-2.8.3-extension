<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

    <head>
	
	<title>WMS example - Leaflet</title>

	<meta charset="utf-8" />
	<meta name="viewport" content="width=device-width, initial-scale=1.0">

	<link rel="stylesheet" href="http://cdn.leafletjs.com/leaflet/v0.7.7/leaflet.css" />
	<script src="http://cdn.leafletjs.com/leaflet/v0.7.7/leaflet.js"></script>

	<style>
		#map {
			width:1700px;
			height: 900px;
		}
	</style>
	
</head>
<div id='map'></div>

<script type="text/javascript">

	var map = L.map('map', {
		center: [-17, -67],
		zoom: 3
	});

	var wmsLayer = L.tileLayer.wms('${baseUrl}/${servicePath}?', {
		layers: '${layerName}'
	}).addTo(map);

</script>

</html>
