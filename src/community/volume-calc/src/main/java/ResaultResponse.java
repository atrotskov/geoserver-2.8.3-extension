
public class ResaultResponse {
	private double cutVolume = 0.0;
	private double fillVolume = 0.0;
	private double perimeter = 0.0;
	private double area = 0.0;
	private double maxHeight = 0.0;
	private double minHeight = 0.0;
	private double basePlane = 0.0;
	private double envelopeWidth = 0.0;
	private double envelopeHeight = 0.0;
	private int pixelsInThePoly = 0;
	private int skipedPixels = 0;
	private long queryExecTime = 0;
	private String shortResault;
	private String fullResault;
	private String message;
	private String exeptionMessage;
		
	public double getCutVolume() {
		return cutVolume;
	}
	public void setCutVolume(double cutVolume) {
		this.cutVolume = cutVolume;
	}
	public double getFillVolume() {
		return fillVolume;
	}
	public void setFillVolume(double fillVolume) {
		this.fillVolume = fillVolume;
	}
	public double getPerimeter() {
		return perimeter;
	}
	public void setPerimeter(double perimeter) {
		this.perimeter = perimeter;
	}
	public double getArea() {
		return area;
	}
	public void setArea(double area) {
		this.area = area;
	}
	public double getMaxHeight() {
		return maxHeight;
	}
	public void setMaxHeight(double maxHeight) {
		this.maxHeight = maxHeight;
	}
	public double getMinHeight() {
		return minHeight;
	}
	public void setMinHeight(double minHeight) {
		this.minHeight = minHeight;
	}
	public double getBasePlane() {
		return basePlane;
	}
	public void setBasePlane(double basePlane) {
		this.basePlane = basePlane;
	}
	public double getEnvelopeWidth() {
		return envelopeWidth;
	}
	public void setEnvelopeWidth(double envelopeWidth) {
		this.envelopeWidth = envelopeWidth;
	}
	public double getEnvelopeHeight() {
		return envelopeHeight;
	}
	public void setEnvelopeHeight(double envelopeHeight) {
		this.envelopeHeight = envelopeHeight;
	}
	public int getPixelsInThePoly() {
		return pixelsInThePoly;
	}
	public void setPixelsInThePoly(int pixelsInThePoly) {
		this.pixelsInThePoly = pixelsInThePoly;
	}
	public int getSkipedPixels() {
		return skipedPixels;
	}
	public void setSkipedPixels(int skipedPixels) {
		this.skipedPixels = skipedPixels;
	}
	public long getQueryExecTime() {
		return queryExecTime;
	}
	public void setQueryExecTime(long queryExecTime) {
		this.queryExecTime = queryExecTime;
	}
	public String getShortResault() {
		return shortResault;
	}

	public String getFullResault() {
		if (exeptionMessage != null) {
			return "Warning: " + exeptionMessage;
		}
		return 	"Cut: " + String.format("%.1f", cutVolume) + "m&#179;</br>" +
				"Fill: " + String.format("%.1f", Math.abs(fillVolume)) + "m&#179;</br>" +
				"Volume: " + String.format("%.1f", (cutVolume + Math.abs(fillVolume))) + "m&#179;</br>" +
				"Perimeter: " + String.format("%.1f", perimeter) + "m</br>" +
				"Area: " + String.format("%.1f", area) + "m&#178;</br>" +
				"Maximum Height: " + String.format("%.1f", maxHeight) + "m</br>" +
				"Minimum Height: " + String.format("%.1f", minHeight) + "m</br>" +
				"Base Plane: " + String.format("%.1f", basePlane) + "m (lowest point)</br>" +
				//"Full width: " + envelopeWidth + "m</br>" +
				//"Full Height: " + envelopeHeight + "m</br>" +
				"The number of pixels in the polygon: " + pixelsInThePoly + "</br>" +
				"Skiped pixels: " + skipedPixels + "</br>" +
				"Query execution time: " + queryExecTime + "msec</br>"
				//+ "Message=" + message
				;
	}

	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	@Override
	public String toString() {
		return "Resaults:" +
				"Cut: " + cutVolume + "m^3\n" +
				"Fill: " + fillVolume + "m^3\n" +
				"Volume: " + (cutVolume + Math.abs(fillVolume)) + "m^3\n" +
				"Perimeter: " + perimeter + "m\n" +
				"Area: " + area + "m^2\n" +
				"Maximum Height: " + maxHeight + "m\n" +
				"Minimum Height: " + minHeight + "m\n" +
				"Base Plane: " + basePlane + "m (lowest point)\n" +
				"Full width: " + envelopeWidth + "m\n" +
				"Full Height: " + envelopeHeight + "m\n" +
				"The number of pixels in the polygon: " + pixelsInThePoly + "\n" +
				"Skiped pixels: " + skipedPixels + "\n" +
				"Query execution time: " + queryExecTime + "msec\n" +
				"Message=" + message;
	}
	
	
	
}
