package com.intetics.atrotskov.dao.impl;

import com.intetics.atrotskov.dao.api.CheckerDao;
import com.vividsolutions.jts.geom.Coordinate;

public class CheckerDaoImpl implements CheckerDao {

	@Override
	public boolean isSkip(double value) {
		if (value < MIN_HEIGHT || value > MAX_HEIGHT) {
			return true;
		}
		return false;
	}

}
