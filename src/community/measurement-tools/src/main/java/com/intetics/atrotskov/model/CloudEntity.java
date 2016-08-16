package com.intetics.atrotskov.model;

import org.geotools.coverage.grid.GridCoordinates2D;

import com.vividsolutions.jts.geom.Coordinate;

public class CloudEntity {
	private double value;
	private Coordinate worldCoord;
	private GridCoordinates2D gridCoordinate;
	
	public CloudEntity(double value, Coordinate worldCoord, GridCoordinates2D gridCoordinate){
		setValue(value);
		setWorldCoord(worldCoord);
		setGridCoordinate(gridCoordinate);		
	}
	
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
	public GridCoordinates2D getGridCoordinate() {
		return gridCoordinate;
	}
	public void setGridCoordinate(GridCoordinates2D gridCoordinate) {
		this.gridCoordinate = gridCoordinate;
	}
}
