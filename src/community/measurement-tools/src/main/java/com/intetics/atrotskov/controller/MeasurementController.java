package com.intetics.atrotskov.controller;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.geotools.coverage.grid.InvalidGridGeometryException;
import org.geotools.coverage.grid.io.GridCoverage2DReader;
import org.geotools.geojson.geom.GeometryJSON;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.operation.TransformException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.intetics.atrotskov.connection.api.Connection;
import com.intetics.atrotskov.model.Volume;
import com.intetics.atrotskov.model.dto.MeasurmentToolsResp;
import com.intetics.atrotskov.service.api.ServiceMeasTools;
import com.vividsolutions.jts.geom.Coordinate;

public class MeasurementController {
	
	private Connection<GridCoverage2DReader> conn;
	private ServiceMeasTools service;
	
	public MeasurementController(Connection<GridCoverage2DReader> conn, ServiceMeasTools service){
		this.conn = conn;
		this.service = service;
	}
	
	public void getPolygonData(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, InvalidGridGeometryException,
			TransformException, NoSuchAuthorityCodeException, FactoryException {
		//long startTime = System.currentTimeMillis();
		
		String layerName = request.getParameter("layer");
		String geoJson = request.getParameter("geoData");
		double basePlane = Double.parseDouble(request.getParameter("basePlane"));
		
		conn.initConnection(layerName);
		
		GeometryJSON gjson = new GeometryJSON();
		Reader reader = new StringReader(geoJson);
		
		Coordinate[] c = gjson.readPolygon(reader).getCoordinates();
		
		Volume volume = service.getVolume(c, service.getBasePlane(c));
		
		MeasurmentToolsResp mtresp = new MeasurmentToolsResp();
		//mtresp.setMaxHeight(service.getMax(c));
		//mtresp.setMinHeight(service.getMin(c));
		//mtresp.setMessage("Hello message");
		mtresp.setVolume(volume);
		mtresp.setBasePlane(service.getBasePlane(c));
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(response.getOutputStream(), mtresp);
		
	}
	
	public void getPointData(HttpServletRequest request, HttpServletResponse response)
			throws ServletException {
		
	}
}
