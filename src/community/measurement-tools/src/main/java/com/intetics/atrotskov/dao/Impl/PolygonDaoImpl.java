package com.intetics.atrotskov.dao.Impl;

import java.util.List;

import com.intetics.atrotskov.dao.api.PolygonDao;
import com.intetics.atrotskov.model.CloudEntity;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Polygon;

public class PolygonDaoImpl implements PolygonDao {

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
