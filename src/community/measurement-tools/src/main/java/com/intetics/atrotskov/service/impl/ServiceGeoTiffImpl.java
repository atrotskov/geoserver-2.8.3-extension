package com.intetics.atrotskov.service.impl;

import java.util.List;
import java.util.TreeMap;

import org.geotools.coverage.grid.InvalidGridGeometryException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.operation.TransformException;

import com.intetics.atrotskov.dao.api.PolygonDao;
import com.intetics.atrotskov.model.CloudEntity;
import com.intetics.atrotskov.model.Volume;
import com.intetics.atrotskov.service.api.ServiceMeasTools;
import com.intetics.atrotskov.transformator.api.Transformator;
import com.vividsolutions.jts.geom.Coordinate;;

public class ServiceGeoTiffImpl implements ServiceMeasTools {
	
	private PolygonDao polygonDao;
	private Transformator trans;
	
	public ServiceGeoTiffImpl(PolygonDao polygonDao, Transformator trans) {
		this.polygonDao = polygonDao;
		this.trans = trans;
	}
	
	@Override
	public Volume getVolume(Coordinate[] coords, double basePlane)
			throws InvalidGridGeometryException, TransformException, NoSuchAuthorityCodeException, FactoryException {
		coords = trans.transformAllFrom(coords);
		List<CloudEntity> results = polygonDao.getValuesByCoord(coords);
		Volume volume = new Volume();
		for (CloudEntity cloudEntity : results) {
			if (cloudEntity.getValue() > basePlane) {
				volume.setCut(volume.getCut() + (cloudEntity.getValue() - basePlane));
			} else if (cloudEntity.getValue() < basePlane){
				volume.setFill(volume.getFill() + Math.abs(cloudEntity.getValue() - basePlane));
			}
		}
		return volume;
	}

	@Override
	public double getMin(Coordinate[] coords)
			throws NoSuchAuthorityCodeException, FactoryException, TransformException {
		return getStatistics(coords).firstKey();
	}
	
	@Override
	public double getMax(Coordinate[] coords)
			throws NoSuchAuthorityCodeException, FactoryException, TransformException {
		return getStatistics(coords).lastKey();
	}

	@Override
	public double getNumberOfPixels(List<CloudEntity> results) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	private TreeMap<Double, CloudEntity> getStatistics(Coordinate[] coords)
			throws NoSuchAuthorityCodeException, FactoryException, TransformException{
		coords = trans.transformAllFrom(coords);
		List<CloudEntity> results = polygonDao.getValuesByCoord(coords);
		TreeMap<Double, CloudEntity> sortedCloud = new TreeMap<>();
		for (CloudEntity cloudEntity : results) {
			sortedCloud.put(cloudEntity.getValue(), cloudEntity);
		}
		return sortedCloud;
	}
}
