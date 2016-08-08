package com.intetics.atrotskov.connection.impl;

import org.geoserver.catalog.Catalog;
import org.geoserver.config.GeoServerDataDirectory;
import org.geotools.coverage.grid.io.GridCoverage2DReader;
import org.geotools.gce.geotiff.GeoTiffReader;

import com.intetics.atrotskov.connection.api.Connection;

public class ConnectionGeoTiffImpl implements Connection<GridCoverage2DReader> {
	
	private Catalog catalog;
	private GeoServerDataDirectory geoServerDataDir;
	
	
	private static ConnectionGeoTiffImpl instance;
	
	private static synchronized ConnectionGeoTiffImpl getInstance() {
		if (instance == null) {
			instance = new ConnectionGeoTiffImpl();
		}
		return instance;
	}


	GridCoverage2DReader reader = new GeoTiffReader(getFile(dataDir, pathFromXml));



	@Override
	public GridCoverage2DReader getConnection() {
		get.
		return getInstance().;
	}

}
