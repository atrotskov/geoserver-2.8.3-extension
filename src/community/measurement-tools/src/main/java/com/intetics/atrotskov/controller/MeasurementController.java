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
import com.intetics.atrotskov.service.impl.SkipVertexException;
import com.vividsolutions.jts.geom.Coordinate;

public class MeasurementController {

	private Connection<GridCoverage2DReader> conn;
	private ServiceMeasTools service;

	public MeasurementController(Connection<GridCoverage2DReader> conn, ServiceMeasTools service) {
		this.conn = conn;
		this.service = service;
	}

	public void getPolygonData(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		long startTime = System.currentTimeMillis();

		String layerName = request.getParameter("layer");
		String geoJson = request.getParameter("geoData");
		double basePlane = Double.parseDouble(request.getParameter("basePlane"));

		Volume volume = null;
		MeasurmentToolsResp mtresp = new MeasurmentToolsResp();

		try {
			conn.initConnection(layerName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		GeometryJSON gjson = new GeometryJSON();
		Reader reader = new StringReader(geoJson);

		Coordinate[] c = null;
		try {
			c = gjson.readPolygon(reader).getCoordinates();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			volume = service.getVolume(c, service.getBasePlane(c));
			mtresp.setMaxHeight(service.getMax(c));
			mtresp.setMinHeight(service.getMin(c));
			mtresp.setMessage("Hello message");
			mtresp.setVolume(volume);
			mtresp.setBasePlane(service.getBasePlane(c));
			mtresp.setArea(service.getArea(c));
			mtresp.setPerimetr(service.getPerimetr(c));
			mtresp.setPixelCount(service.getNumberOfPixels(c));
		} catch (SkipVertexException e) {
			mtresp.setMessage(e.getMessage());
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			mtresp.setMessage("Polygon is outside of the layer");
			e.printStackTrace();
		} catch (InvalidGridGeometryException e) {
			mtresp.setMessage("Catch InvalidGridGeometryException exception");
			e.printStackTrace();
		} catch (TransformException e) {
			mtresp.setMessage("Catch TransformException exception");
			e.printStackTrace();
		} catch (FactoryException e) {
			mtresp.setMessage("Catch FactoryException exception");
			e.printStackTrace();
		}

		ObjectMapper mapper = new ObjectMapper();
		mtresp.setResponseTime(System.currentTimeMillis() - startTime);

		try {
			mapper.writeValue(response.getOutputStream(), mtresp);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void getPointData(HttpServletRequest request, HttpServletResponse response) throws ServletException {

	}
}
