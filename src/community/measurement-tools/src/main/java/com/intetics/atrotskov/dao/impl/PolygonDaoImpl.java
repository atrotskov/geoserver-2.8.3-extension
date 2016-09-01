package com.intetics.atrotskov.dao.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.geotools.coverage.grid.GridCoordinates2D;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.InvalidGridGeometryException;
import org.geotools.coverage.grid.io.GridCoverage2DReader;
import org.geotools.geometry.DirectPosition2D;
import org.geotools.geometry.Envelope2D;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.opengis.referencing.operation.TransformException;

import com.intetics.atrotskov.connection.api.Connection;
import com.intetics.atrotskov.dao.api.CheckerDao;
import com.intetics.atrotskov.dao.api.PolygonDao;
import com.intetics.atrotskov.model.CloudEntity;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Polygon;

public class PolygonDaoImpl implements PolygonDao {

	private Connection<GridCoverage2DReader> conn;
	private CheckerDao checkerDao;

	public PolygonDaoImpl(Connection<GridCoverage2DReader> conn, CheckerDao checkerDao) {
		this.conn = conn;
		this.checkerDao = checkerDao;
	}

	private GridCoverage2D coverage;
	private GridGeometry2D geometry;
	
	private void setFields(){
		this.coverage = conn.getCoverage();
		this.geometry = conn.getGeometry();
	}

	@Override
	public List<CloudEntity> getValuesByCoord(Coordinate[] coords)
			throws InvalidGridGeometryException, TransformException {
		
		long startTime = System.currentTimeMillis();
		
		setFields();
		Coordinate[] extrimeCorners = getExtremeCorners(coords);
		GridCoordinates2D startPosition = geometry
				.worldToGrid(new DirectPosition2D(extrimeCorners[0].x, extrimeCorners[0].y));
		GridCoordinates2D endPosition = geometry
				.worldToGrid(new DirectPosition2D(extrimeCorners[1].x, extrimeCorners[1].y));
		GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();
		Polygon polygon = geometryFactory.createPolygon(coords);

		List<CloudEntity> listOfResults = new ArrayList<>();

		for (int j = startPosition.y; j <= endPosition.y; j++) {
			for (int i = startPosition.x; i <= endPosition.x; i++) {
				Envelope2D pixelEnvelop = geometry.gridToWorld(new GridEnvelope2D(i, j, 1, 1));
				Coordinate point = new Coordinate(pixelEnvelop.getCenterX(), pixelEnvelop.getCenterY());

				if (polygon.contains(geometryFactory.createPoint(point))) {
					double tempValue = coverage.evaluate(new GridCoordinates2D(i, j), conn.getVals())[0];
					// we rounded the value (3 digits after the decimal point)
					double value = new BigDecimal(tempValue).setScale(3, RoundingMode.HALF_UP).doubleValue();
					CloudEntity ce = new CloudEntity(value, point, new GridCoordinates2D(i, j));
					listOfResults.add(ce);
				};
			}
		}
		long duration = System.currentTimeMillis() - startTime;
		System.out.println("Read GeoTiff duration: " + duration);
		return listOfResults;
	}

	@Override
	public List<CloudEntity> getValuesByPolygon(Polygon polygon)
			throws InvalidGridGeometryException, TransformException {
		return getValuesByCoord(polygon.getCoordinates());
	}
	
	@Override
	public Coordinate[] getPixelArea() throws TransformException {
		Envelope2D pe1 = geometry.gridToWorld(new GridEnvelope2D(0, 0, 1, 1));
		Envelope2D pe2 = geometry.gridToWorld(new GridEnvelope2D(1, 0, 1, 1));
		Envelope2D pe3 = geometry.gridToWorld(new GridEnvelope2D(1, 1, 1, 1));
		Envelope2D pe4 = geometry.gridToWorld(new GridEnvelope2D(0, 1, 1, 1));
		
		Coordinate c1 = new Coordinate(pe1.getCenterX(), pe1.getCenterY());
		Coordinate c2 = new Coordinate(pe2.getCenterX(), pe2.getCenterY());
		Coordinate c3 = new Coordinate(pe3.getCenterX(), pe3.getCenterY());
		Coordinate c4 = new Coordinate(pe4.getCenterX(), pe4.getCenterY());
		Coordinate c5 = new Coordinate(pe1.getCenterX(), pe1.getCenterY());
		Coordinate[] coords = {c1, c2, c3, c4, c5};
		return coords;
	}

	private Coordinate[] getExtremeCorners(Coordinate[] coords) {
		Coordinate firstCorner = new Coordinate(coords[0]);
		Coordinate secondCorner = new Coordinate(coords[0]);
		for (Coordinate coordinate : coords) {
			if (coordinate.x < firstCorner.x) {
				firstCorner.x = coordinate.x;
			}
			if (coordinate.y > firstCorner.y) {
				firstCorner.y = coordinate.y;
			}
			if (coordinate.x > secondCorner.x) {
				secondCorner.x = coordinate.x;
			}
			if (coordinate.y < secondCorner.y) {
				secondCorner.y = coordinate.y;
			}
		}
		Coordinate[] res = { firstCorner, secondCorner };
		return res;
	}
}
