package com.intetics.atrotskov.model;

import org.geotools.coverage.grid.GridCoverage2D;

import com.vividsolutions.jts.geom.Coordinate;

public class CloudEntity {
	private double value;
	private Coordinate worldCoord;
	private GridCoverage2D gridCoordinate;
	
	public double getValue() {
		return value;
	}
	public void setValue(double value) {
		this.value = value;
	}
	public Coordinate getWorldCoord() {
		return worldCoord;
	}
	public void setWorldCoord(Coordinate worldCoord) {
		this.worldCoord = worldCoord;
	}
	public GridCoverage2D getGridCoordinate() {
		return gridCoordinate;
	}
	public void setGridCoordinate(GridCoverage2D gridCoordinate) {
		this.gridCoordinate = gridCoordinate;
	}
}
