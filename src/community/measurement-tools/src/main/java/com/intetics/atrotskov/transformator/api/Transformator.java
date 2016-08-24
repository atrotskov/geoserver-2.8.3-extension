package com.intetics.atrotskov.transformator.api;

import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.operation.TransformException;

import com.vividsolutions.jts.geom.Coordinate;

public interface Transformator {
	static final String SOURCE_CRS = "EPSG:4326";
	
	Coordinate transformFrom(Coordinate coord)
			throws NoSuchAuthorityCodeException, FactoryException, TransformException;
	Coordinate[] transformAllFrom(Coordinate[] coords)
			throws NoSuchAuthorityCodeException, FactoryException, TransformException;
}
