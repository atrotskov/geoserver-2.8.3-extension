package com.intetics.atrotskov.service.impl;

import java.util.List;

import com.intetics.atrotskov.dao.Impl.PolygonDaoImpl;
import com.intetics.atrotskov.dao.api.PolygonDao;
import com.intetics.atrotskov.model.CloudEntity;
import com.intetics.atrotskov.model.Volume;
import com.intetics.atrotskov.service.api.ServiceMeasTools;;

public class ServiceGeoTiffImpl implements ServiceMeasTools {
	
	PolygonDao polyDao = new PolygonDaoImpl();
	
	
	@Override
	public Volume getVolume(List<CloudEntity> results, double basePlane) {
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
	public double getMinValue(List<CloudEntity> results) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getMaxValue(List<CloudEntity> results) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getNumberOfPixels(List<CloudEntity> results) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<CloudEntity> getCut(List<CloudEntity> results) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double getHeght() {
		// TODO Auto-generated method stub
		return 0;
	}

}
