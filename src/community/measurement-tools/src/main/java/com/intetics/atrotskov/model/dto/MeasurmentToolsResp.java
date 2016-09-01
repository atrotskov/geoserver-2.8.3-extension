package com.intetics.atrotskov.model.dto;

import com.intetics.atrotskov.model.Volume;

public class MeasurmentToolsResp {
	private String message;
	private Volume volume;
	private double minHeight;
	private double maxHeight;
	private double area;
	private double perimetr;
	private int pixelCount;
	private int pixelSkipped;
	private double basePlane;
	private long responseTime;
	
	public double getArea() {
		return area;
	}
	public void setArea(double area) {
		this.area = area;
	}
	public long getResponseTime() {
		return responseTime;
	}
	public void setResponseTime(long responseTime) {
		this.responseTime = responseTime;
	}
	public double getPerimetr() {
		return perimetr;
	}
	public void setPerimetr(double perimetr) {
		this.perimetr = perimetr;
	}
	
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Volume getVolume() {
		return volume;
	}
	public void setVolume(Volume volume) {
		this.volume = volume;
	}
	public double getMinHeight() {
		return minHeight;
	}
	public void setMinHeight(double minHeight) {
		this.minHeight = minHeight;
	}
	public double getMaxHeight() {
		return maxHeight;
	}
	public void setMaxHeight(double maxHeight) {
		this.maxHeight = maxHeight;
	}
	public int getPixelCount() {
		return pixelCount;
	}
	public void setPixelCount(int pixelCount) {
		this.pixelCount = pixelCount;
	}
	public int getPixelSkipped() {
		return pixelSkipped;
	}
	public void setPixelSkipped(int pixelSkipped) {
		this.pixelSkipped = pixelSkipped;
	}
	public double getBasePlane() {
		return basePlane;
	}
	public void setBasePlane(double basePlane) {
		this.basePlane = basePlane;
	}
	
	
}
