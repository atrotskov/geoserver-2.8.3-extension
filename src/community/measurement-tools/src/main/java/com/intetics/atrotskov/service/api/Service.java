package com.intetics.atrotskov.service.api;

import java.util.List;

import com.intetics.atrotskov.model.CloudEntity;

interface Service {
	
	/* Methods for volume data */
	double getVolume(List<CloudEntity> results, double basePlane);
	double getMinValue();
	double getMaxValue();
	double getNumberOfPixels();
	
	/* Methods for linear data*/	
	List<CloudEntity> getCut();
	
	/* Methods for point data*/
	double getHeght();
	
	

}
