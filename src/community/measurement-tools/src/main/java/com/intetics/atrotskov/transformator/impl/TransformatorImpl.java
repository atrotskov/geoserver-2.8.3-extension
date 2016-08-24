package com.intetics.atrotskov.transformator.impl;

import org.geotools.coverage.grid.io.GridCoverage2DReader;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

import com.intetics.atrotskov.connection.api.Connection;
import com.intetics.atrotskov.transformator.api.Transformator;
import com.vividsolutions.jts.geom.Coordinate;

public class TransformatorImpl implements Transformator {
	
	private Connection<GridCoverage2DReader> conn;
	
	public TransformatorImpl(Connection<GridCoverage2DReader> conn) {
		this.conn = conn;
	}

	@Override
	public Coordinate transformFrom(Coordinate coord) throws NoSuchAuthorityCodeException, FactoryException, TransformException {
		CoordinateReferenceSystem sourceCRS = CRS.decode(SOURCE_CRS);
		CoordinateReferenceSystem targetCRS = conn.getCoverage().getCoordinateReferenceSystem();
		MathTransform transform = CRS.findMathTransform(sourceCRS, targetCRS);
		JTS.transform(coord, coord, transform);
		return coord;
	}

	@Override
	public Coordinate[] transformAllFrom(Coordinate[] coords) throws NoSuchAuthorityCodeException, FactoryException, TransformException {
		for (int i = 0; i < coords.length; i++) {
			transformFrom(coords[i]);
		}
		return coords;
	}

}
