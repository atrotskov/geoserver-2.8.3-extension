package com.intetics.atrotskov.dao.api;

import org.geotools.coverage.grid.InvalidGridGeometryException;
import org.opengis.referencing.operation.TransformException;

import com.vividsolutions.jts.geom.Coordinate;

public interface PointDao {
	
	double getValueByCoord(Coordinate coord) throws InvalidGridGeometryException, TransformException;
	
	double getValueByCoord(double x, double y) throws InvalidGridGeometryException, TransformException;

}
