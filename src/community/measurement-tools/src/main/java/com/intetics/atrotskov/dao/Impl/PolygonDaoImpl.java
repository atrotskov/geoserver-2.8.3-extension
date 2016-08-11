package com.intetics.atrotskov.dao.Impl;

import java.util.List;

import org.geotools.coverage.grid.GridCoordinates2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.InvalidGridGeometryException;
import org.geotools.coverage.grid.io.GridCoverage2DReader;
import org.geotools.geometry.DirectPosition2D;
import org.opengis.referencing.operation.TransformException;

import com.intetics.atrotskov.connection.api.Connection;
import com.intetics.atrotskov.connection.impl.ConnectionGeoTiffImpl;
import com.intetics.atrotskov.dao.api.PolygonDao;
import com.intetics.atrotskov.model.CloudEntity;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Polygon;

public class PolygonDaoImpl implements PolygonDao {
	
	private Connection<GridCoverage2DReader> conn = new ConnectionGeoTiffImpl();
	private GridCoverage2DReader coverage = conn.getConnection();
	private GridGeometry2D geometry = conn.getGeometry();

	@Override
	public List<CloudEntity> getValuesByCoord(Coordinate[] coords) throws InvalidGridGeometryException, TransformException {
		Coordinate[] extrimeCorners = getExtremeCorners(coords);
		GridCoordinates2D startPosition = geometry.worldToGrid(
				new DirectPosition2D(extrimeCorners[0].x, extrimeCorners[0].y));
		GridCoordinates2D endPosition = geometry.worldToGrid(
				new DirectPosition2D(extrimeCorners[1].x, extrimeCorners[1].y));
		
		
		// TODO Auto-generated method stub
		return null;
	}

	

	@Override
	public List<CloudEntity> getValuesByPolygon(Polygon polygon) throws InvalidGridGeometryException, TransformException {
		return getValuesByCoord(polygon.getCoordinates());
	}
	
	private Coordinate[] getExtremeCorners(Coordinate[] coords) {
		// TODO Auto-generated method stub
		return null;
	}
}
