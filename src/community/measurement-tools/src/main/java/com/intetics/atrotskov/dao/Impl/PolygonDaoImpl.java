package com.intetics.atrotskov.dao.Impl;

import java.util.List;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.io.GridCoverage2DReader;
import com.intetics.atrotskov.connection.api.Connection;
import com.intetics.atrotskov.connection.impl.ConnectionGeoTiffImpl;
import com.intetics.atrotskov.dao.api.PolygonDao;
import com.intetics.atrotskov.model.CloudEntity;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Polygon;

public class PolygonDaoImpl implements PolygonDao {
	
	Connection<GridCoverage2DReader> conn = new ConnectionGeoTiffImpl();
	GridCoverage2DReader coverage = conn.getConnection(layerName);

	@Override
	public List<CloudEntity> getValuesByCoord(Coordinate[] coords) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<CloudEntity> getValuesByPolygon(Polygon polygon) {
		return getValuesByCoord(polygon.getCoordinates());
	}
}
