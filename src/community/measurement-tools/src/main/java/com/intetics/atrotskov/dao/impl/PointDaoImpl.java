package com.intetics.atrotskov.dao.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.geotools.coverage.grid.GridCoordinates2D;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.InvalidGridGeometryException;
import org.geotools.coverage.grid.io.GridCoverage2DReader;
import org.geotools.geometry.DirectPosition2D;
import org.opengis.referencing.operation.TransformException;

import com.intetics.atrotskov.connection.api.Connection;
import com.intetics.atrotskov.dao.api.PointDao;
import com.vividsolutions.jts.geom.Coordinate;

public class PointDaoImpl implements PointDao {
	private Connection<GridCoverage2DReader> conn;

	public PointDaoImpl(Connection<GridCoverage2DReader> conn) {
		this.conn = conn;
	}

	private GridCoverage2D coverage;
	private GridGeometry2D geometry;
	
	private void setFields(){
		this.coverage = conn.getCoverage();
		this.geometry = conn.getGeometry();
	}

	@Override
	public double getValueByCoord(Coordinate coord) throws InvalidGridGeometryException, TransformException {
		setFields();
		GridCoordinates2D gridCoordinates = geometry
				.worldToGrid(new DirectPosition2D(coord.x, coord.y));
		double tempValue = coverage.evaluate(gridCoordinates, conn.getVals())[0];
		// we rounded the value (3 digits after the decimal point)
		double value = new BigDecimal(tempValue).setScale(3, RoundingMode.HALF_UP).doubleValue();
		return value;
	}

	@Override
	public double getValueByCoord(double x, double y) throws InvalidGridGeometryException, TransformException {
		Coordinate coord = new Coordinate(x, y);
		return getValueByCoord(coord);
	}

}
