package com.intetics.atrotskov.dao.api;

import java.util.List;

import com.intetics.atrotskov.model.CloudEntity;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Polygon;

public interface PolygonDao {
	
	List<CloudEntity> getValuesByCoord(Coordinate[] coords);
	List<CloudEntity> getValuesByPolygon(Polygon polygon);

}
