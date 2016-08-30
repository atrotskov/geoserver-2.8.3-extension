package com.intetics.atrotskov.service.api;

import java.util.List;

import org.geotools.coverage.grid.InvalidGridGeometryException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.operation.TransformException;

import com.intetics.atrotskov.model.CloudEntity;
import com.intetics.atrotskov.model.Volume;
import com.vividsolutions.jts.geom.Coordinate;

public interface ServiceMeasTools {
	
	/* Methods for volume data */
	Volume getVolume(Coordinate[] coords, double basePlane)
			throws InvalidGridGeometryException, TransformException, NoSuchAuthorityCodeException, FactoryException;
	double getNumberOfPixels(List<CloudEntity> results);
	double getMin(Coordinate[] coords)
			throws NoSuchAuthorityCodeException, FactoryException, TransformException;
	double getMax(Coordinate[] coords)
			throws NoSuchAuthorityCodeException, FactoryException, TransformException;
	
}