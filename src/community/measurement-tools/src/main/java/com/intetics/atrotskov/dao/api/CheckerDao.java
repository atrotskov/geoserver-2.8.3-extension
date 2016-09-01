package com.intetics.atrotskov.dao.api;

import com.vividsolutions.jts.geom.Coordinate;

public interface CheckerDao {
	final double MIN_HEIGHT = -500.0;			// the minimum height above sea level
	final static double MAX_HEIGHT = 7000.0;	// the maximum height above sea level
	boolean isSkip(double value);
}
