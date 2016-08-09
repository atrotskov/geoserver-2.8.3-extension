package com.intetics.atrotskov.connection.impl;

import java.io.File;
import java.io.FileNotFoundException;

import org.geoserver.catalog.Catalog;
import org.geoserver.config.GeoServerDataDirectory;
import org.geotools.coverage.grid.io.GridCoverage2DReader;
import org.geotools.data.DataSourceException;
import org.geotools.gce.geotiff.GeoTiffReader;

import com.intetics.atrotskov.connection.api.Connection;

public class ConnectionGeoTiffImpl implements Connection<GridCoverage2DReader> {
	
	private Catalog catalog;
	private GeoServerDataDirectory geoServerDataDir;
		
	public Catalog getCatalog() {
		return catalog;
	}

	public void setCatalog(Catalog catalog) {
		this.catalog = catalog;
	}

	public GeoServerDataDirectory getGeoServerDataDir() {
		return geoServerDataDir;
	}

	public void setGeoServerDataDir(GeoServerDataDirectory geoServerDataDir) {
		this.geoServerDataDir = geoServerDataDir;
	}
	
	@Override
	public GridCoverage2DReader getConnection(String layerName) throws DataSourceException, FileNotFoundException {
		
		String dataDir = geoServerDataDir.root().getAbsolutePath();
		String coverageId = catalog.getLayerByName(layerName).getResource().getId();
		String coverageStoreId = catalog.getCoverage(coverageId).getStore().getId();
		String pathFromXml = catalog.getCoverageStore(coverageStoreId).getURL();
		
		GridCoverage2DReader reader = new GeoTiffReader(getFile(dataDir, pathFromXml));
		return reader;
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
