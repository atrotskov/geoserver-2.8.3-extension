package com.intetics.atrotskov.connection.api;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.data.DataSourceException;

public interface Connection<T>  {
	void initConnection(String layerName) throws DataSourceException, FileNotFoundException, IOException;
	T getConnection();
	GridCoverage2D getCoverage();
	GridGeometry2D getGeometry();
	int getNumBands();
	double[] getVals();	
}
