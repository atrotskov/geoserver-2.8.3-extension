package com.intetics.atrotskov.dao.api;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Point;

public interface PointDao {
	
	Point getPointByCoord(Coordinate coord);
	
	Point getPointByCoord(double x, double y);

}
