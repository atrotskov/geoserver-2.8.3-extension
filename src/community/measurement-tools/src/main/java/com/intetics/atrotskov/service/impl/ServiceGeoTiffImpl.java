package com.intetics.atrotskov.service.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

import org.geotools.coverage.grid.InvalidGridGeometryException;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.operation.TransformException;

import com.intetics.atrotskov.dao.api.PointDao;
import com.intetics.atrotskov.dao.api.PolygonDao;
import com.intetics.atrotskov.model.CloudEntity;
import com.intetics.atrotskov.model.Volume;
import com.intetics.atrotskov.service.api.ServiceMeasTools;
import com.intetics.atrotskov.transformator.api.Transformator;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Polygon;;

public class ServiceGeoTiffImpl implements ServiceMeasTools {
	
	private TreeMap<Double, Integer> sortedCloud;
	private Coordinate[] coordinates;
	
	private PointDao pointDao;
	private PolygonDao polygonDao;
	private Transformator trans;
	
	public ServiceGeoTiffImpl(PolygonDao polygonDao, Transformator trans, PointDao pointDao) {
		this.polygonDao = polygonDao;
		this.trans = trans;
		this.pointDao = pointDao;
	}
	
	@Override
	public Volume getVolume(Coordinate[] coords, double basePlane)
			throws InvalidGridGeometryException, TransformException, NoSuchAuthorityCodeException, FactoryException {
		
		long startTime1 = System.currentTimeMillis();
		
		Volume volume = new Volume();
		TreeMap<Double, Integer> cloud = getStatistics(coords);
		
		NavigableMap<Double, Integer> tailCloud = cloud.tailMap(basePlane, false);
		NavigableMap<Double, Integer> headCloud = cloud.headMap(basePlane, false);
		
		
		volume.setCut(getVol(tailCloud, basePlane));
		volume.setFill(getVol(headCloud, basePlane));
		
		long duration1 = System.currentTimeMillis() - startTime1;
		System.out.println("Get volume duration: " + duration1);
		
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
	public int getNumberOfPixels(Coordinate[] coords) throws NoSuchAuthorityCodeException, FactoryException, TransformException {
		Collection<Integer> collection = getStatistics(coords).values();
		int sum = 0;
		for (Integer item : collection) {
			sum += item;
		}
		return sum;
	}
	
	@Override
	public double getBasePlane(Coordinate[] coords)
			throws InvalidGridGeometryException, TransformException, NoSuchAuthorityCodeException, FactoryException {
		
		getStatistics(coords);
		
		double basePlane = 7000;					// 7000 is unreal minimal height which must have been override during first iteration
		for (Coordinate coordinate : this.coordinates) {
			double currentValue = pointDao.getValueByCoord(coordinate);
			if (currentValue < basePlane) {
				basePlane = currentValue;
			}
		}
		return basePlane;
	}
	
	@Override
	public double getArea(Coordinate[] coords) throws NoSuchAuthorityCodeException, FactoryException, TransformException {
		getStatistics(coords);
		GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();
		Polygon polygon = geometryFactory.createPolygon(this.coordinates);
		return polygon.getArea();
	}
	
	@Override
	public double getPerimetr(Coordinate[] coords) throws NoSuchAuthorityCodeException, FactoryException, TransformException {
		getStatistics(coords);
		GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();
		Polygon polygon = geometryFactory.createPolygon(this.coordinates);
		return polygon.getLength();
	}
	
	private double getPixelArea() throws TransformException {
		System.out.println("Pixel area = " + polygonDao.getPixelArea());
		return polygonDao.getPixelArea();
		
	}
	
	private double getVol(NavigableMap<Double, Integer> cloud, double basePlane) throws TransformException {
		double heightSum = 0;		
		for (Map.Entry<Double, Integer> entry : cloud.entrySet()) {
			heightSum += (entry.getKey() - basePlane) * entry.getValue();
		}
		return heightSum * getPixelArea();
	}
	
	private boolean isCoordinatesEquals(Coordinate[] coords) {
		if (this.coordinates == null) {
			return false;
		} else if (this.coordinates.length != coords.length) {
			return false;
		} else {
			for (int i = 0; i < coords.length; i++) {
				if (!this.coordinates[i].equals2D(coords[i])) {
					return false;
				}
				
			}
		}
		
		return true;
		
	}
	
	private TreeMap<Double, Integer> getStatistics(Coordinate[] coords)
			throws NoSuchAuthorityCodeException, FactoryException, TransformException {
		
		long startTime2 = System.currentTimeMillis();
		
		coords = trans.transformAllFrom(coords);

		if (!(isCoordinatesEquals(coords))) {
			this.coordinates = coords;
			List<CloudEntity> results = polygonDao.getValuesByCoord(coords);
			TreeMap<Double, Integer> sortedCloud = new TreeMap<>();
			for (CloudEntity cloudEntity : results) {
				if (sortedCloud.containsKey(cloudEntity.getValue())) {
					sortedCloud.replace(cloudEntity.getValue(), (sortedCloud.get(cloudEntity.getValue()) + 1));
				} else {
					sortedCloud.put(cloudEntity.getValue(), 1);
				}
			}
			this.sortedCloud = sortedCloud;
		}
		
		long duration2 = System.currentTimeMillis() - startTime2;
		System.out.println("Get statistic duration: " + duration2);
		
		return this.sortedCloud;

	}
}
