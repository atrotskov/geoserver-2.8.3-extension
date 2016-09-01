package com.intetics.atrotskov.dao.api;

import java.util.List;

import org.geotools.coverage.grid.InvalidGridGeometryException;
import org.geotools.geometry.Envelope2D;
import org.opengis.referencing.operation.TransformException;

import com.intetics.atrotskov.model.CloudEntity;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Polygon;

public interface PolygonDao {
	
	List<CloudEntity> getValuesByCoord(Coordinate[] coords) throws InvalidGridGeometryException, TransformException;
	List<CloudEntity> getValuesByPolygon(Polygon polygon) throws InvalidGridGeometryException, TransformException;
	Coordinate[] getPixelArea() throws TransformException;

}
