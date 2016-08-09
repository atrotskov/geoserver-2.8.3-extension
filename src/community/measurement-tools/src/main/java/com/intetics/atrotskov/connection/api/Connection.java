package com.intetics.atrotskov.connection.api;

import java.io.FileNotFoundException;

import org.geotools.data.DataSourceException;

public interface Connection<T>  {	
	T getConnection(String layerName) throws DataSourceException, FileNotFoundException;
}
