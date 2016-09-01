package com.intetics.atrotskov.transformator.impl;

import org.geotools.coverage.grid.io.GridCoverage2DReader;
import org.geotools.geometry.Envelope2D;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

import com.intetics.atrotskov.connection.api.Connection;
import com.intetics.atrotskov.transformator.api.Transformator;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Polygon;

public class TransformatorImpl implements Transformator {
	
	private Connection<GridCoverage2DReader> conn;
	
	public TransformatorImpl(Connection<GridCoverage2DReader> conn) {
		this.conn = conn;
	}

	@Override
	public Coordinate transformFrom(Coordinate coord)
			throws NoSuchAuthorityCodeException, FactoryException, TransformException {
		CoordinateReferenceSystem sourceCRS = CRS.decode(SOURCE_CRS);
		CoordinateReferenceSystem targetCRS = conn.getCoverage().getCoordinateReferenceSystem();
		MathTransform transform = CRS.findMathTransform(sourceCRS, targetCRS);
		Coordinate targetCoordinate = JTS.transform(coord, null, transform);
		return targetCoordinate;
	}

	@Override
	public Coordinate[] transformAllFrom(Coordinate[] coords)
			throws NoSuchAuthorityCodeException, FactoryException, TransformException {
		Coordinate[] targetCoordinates = new Coordinate[coords.length];
		for (int i = 0; i < coords.length; i++) {
			targetCoordinates[i] = transformFrom(coords[i]);
		}
		return targetCoordinates;
	}
	
	@Override
	public Geometry transformPolygon(Polygon polygon)
			throws NoSuchAuthorityCodeException, FactoryException,
			MismatchedDimensionException, TransformException {
		CoordinateReferenceSystem targetCrs = CRS.decode("EPSG:3575");
		CoordinateReferenceSystem sourceCrs = conn.getCoverage().getCoordinateReferenceSystem();
		MathTransform transform = CRS.findMathTransform(sourceCrs, targetCrs);
		Geometry geo = JTS.transform(polygon, transform);
		return geo;				
	}
	
	@Override
	public Envelope transformPixelEnvelope(Envelope envelope)
			throws NoSuchAuthorityCodeException, FactoryException,
			MismatchedDimensionException, TransformException {
		CoordinateReferenceSystem targetCrs = CRS.decode("EPSG:3575");
		CoordinateReferenceSystem sourceCrs = conn.getCoverage().getCoordinateReferenceSystem();
		MathTransform transform = CRS.findMathTransform(sourceCrs, targetCrs);
		Envelope env = JTS.transform(envelope, transform);
		return env;				
	}

}
