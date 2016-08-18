package com.intetics.atrotskov.model;

public class Volume {
	private double cut = 0;
	private double fill = 0;
	
	public double getCut() {
		return cut;
	}
	
	public void setCut(double cut) {
		this.cut = cut;
	}
	
	public double getFill() {
		return fill;
	}
	
	public void setFill(double fill) {
		this.fill = fill;
	}
	
	public double getTotal() {
		return cut + Math.abs(fill);
	}
	
	@Override
	public String toString() {
		return "Volume [cut=" + cut + ", fill=" + fill +
				", total" + getTotal() + "]";
	}
}
