package com.intetics.atrotskov.controller;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.geotools.coverage.grid.io.GridCoverage2DReader;
import org.geotools.geojson.geom.GeometryJSON;

import com.intetics.atrotskov.connection.api.Connection;
import com.intetics.atrotskov.connection.impl.ConnectionGeoTiffImpl;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Polygon;

public class MeagurmentController {
	
	private Connection<GridCoverage2DReader> conn;
	
	public MeagurmentController(Connection<GridCoverage2DReader> conn){
		this.conn = conn;		
	}
	
	public void getPolygonData(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		long startTime = System.currentTimeMillis();
		
		String layerName = request.getParameter("layer");
		String geoJson = request.getParameter("geoData");
		
		conn.initConnection(layerName);
		
		
		//String basePlane = request.getParameter("basePlane");
		
		
		/*String dataDir = geoServerDataDir.root().getAbsolutePath();
		String coverageId = catalog.getLayerByName(layerName).getResource().getId();
		String coverageStoreId = catalog.getCoverage(coverageId).getStore().getId();
		String pathFromXml = catalog.getCoverageStore(coverageStoreId).getURL();*/
		
		GeometryJSON gjson = new GeometryJSON();

		Reader reader = new StringReader(geoJson);
		double area = gjson.readPolygon(reader).getArea();
		
		String temp1 = "";
		String temp2 = "";
		System.out.println(area);
		
	}
	
	public void getPointData(HttpServletRequest request, HttpServletResponse response)
			throws ServletException {
		
	}
}
