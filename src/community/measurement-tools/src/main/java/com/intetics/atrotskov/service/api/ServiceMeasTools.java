package com.intetics.atrotskov.service.api;

import java.util.List;

import com.intetics.atrotskov.model.CloudEntity;
import com.intetics.atrotskov.model.Volume;

public interface ServiceMeasTools {
	
	/* Methods for volume data */
	Volume getVolume(List<CloudEntity> results, double basePlane);
	double getMinValue(List<CloudEntity> results);
	double getMaxValue(List<CloudEntity> results);
	double getNumberOfPixels(List<CloudEntity> results);
	
	/* Methods for linear data*/	
	List<CloudEntity> getCut(List<CloudEntity> results);
	
	/* Methods for point data*/
	double getHeght();

}
