package com.intetics.atrotskov.dao.impl;

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
import com.intetics.atrotskov.connection.impl.ConnectionGeoTiffImpl;
import com.intetics.atrotskov.dao.api.PolygonDao;
import com.intetics.atrotskov.model.CloudEntity;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Polygon;

public class PolygonDaoImpl implements PolygonDao {

	private Connection<GridCoverage2DReader> conn;

	public PolygonDaoImpl(Connection<GridCoverage2DReader> conn) {
		this.conn = conn;
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
					double value = coverage.evaluate(new GridCoordinates2D(i, j), conn.getVals())[0];
					CloudEntity ce = new CloudEntity(value, point, new GridCoordinates2D(i, j));
					listOfResults.add(ce);
				}
				;
			}
		}
		return listOfResults;
	}

	@Override
	public List<CloudEntity> getValuesByPolygon(Polygon polygon)
			throws InvalidGridGeometryException, TransformException {
		return getValuesByCoord(polygon.getCoordinates());
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
		System.out.println("Extreme corners are:");
		System.out.println("First Corner = " + firstCorner.x + " / " + firstCorner.y);
		System.out.println("Second Corner = " + secondCorner.x + " / " + secondCorner.y);
		return res;
	}
}