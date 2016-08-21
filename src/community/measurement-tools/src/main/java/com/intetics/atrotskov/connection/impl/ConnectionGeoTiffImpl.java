package com.intetics.atrotskov.connection.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.geoserver.catalog.Catalog;
import org.geoserver.config.GeoServerDataDirectory;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.GridCoverage2DReader;
import org.geotools.data.DataSourceException;
import org.geotools.gce.geotiff.GeoTiffReader;

import com.intetics.atrotskov.connection.api.Connection;

public class ConnectionGeoTiffImpl implements Connection<GridCoverage2DReader> {
	
	private Catalog catalog;
	private GeoServerDataDirectory geoServerDataDir;
	private String layerName;
	private GridCoverage2DReader reader;
	private GridCoverage2D coverage;
	private GridGeometry2D geometry;
	private int numBands;
	private double[] vals;
	
	public ConnectionGeoTiffImpl(Catalog catalog, GeoServerDataDirectory geoServerDataDir) {
		this.catalog = catalog;
		this.geoServerDataDir = geoServerDataDir;
	}
		
	@Override
	public void initConnection(String layerName)
			throws DataSourceException, FileNotFoundException, IOException {
		
		if (this.layerName != null &&
				!(this.layerName.equals(layerName))) {
			this.layerName = layerName;
			String dataDir = geoServerDataDir.root().getAbsolutePath();
			String coverageId = catalog.getLayerByName(layerName).getResource().getId();
			String coverageStoreId = catalog.getCoverage(coverageId).getStore().getId();
			String pathFromXml = catalog.getCoverageStore(coverageStoreId).getURL();
			this.reader = new GeoTiffReader(getFile(dataDir, pathFromXml));
			this.coverage = reader.read(null);
			this.geometry = coverage.getGridGeometry();
			this.numBands = reader.getGridCoverageCount();
			this.vals = new double[numBands];
		}
	}
	
	@Override
	public GridCoverage2DReader getConnection() {
		return this.reader;
	}
	
	@Override
	public GridCoverage2D getCoverage() {
		return this.coverage;
	}

	@Override
	public GridGeometry2D getGeometry() {
		return this.geometry;
	}

	@Override
	public int getNumBands() {
		return this.numBands;
	}

	@Override
	public double[] getVals() {
		return this.vals;
	}
	
	private File getFile(String geoServerDataDir, String fileFromXml) throws FileNotFoundException {

		// Replace path divider from "\" to "/" if need
		geoServerDataDir = geoServerDataDir.replace("\\", "/");
		fileFromXml = fileFromXml.replace("\\", "/");

		// Trim word "file://" or "file:" from getting URL
		if (fileFromXml.startsWith("file://")) {
			fileFromXml = fileFromXml.substring(7);
		} else {
			fileFromXml = fileFromXml.substring(5);
		}

		// Set absolute path location
		String fullPath = geoServerDataDir + "/" + fileFromXml;

		File file = new File(fullPath);
		if (!file.exists()) {
			file = new File(fileFromXml);
		}
		if (!file.exists()) {
			String e = "1st) tried use this location: " + fileFromXml + " 2nd) tried use this location: " + fullPath;
			throw new FileNotFoundException(e);
		}
		return file;
	}

	

	

}
