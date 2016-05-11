import java.lang.Math;

public class SphericalMercator {
	public static final double RADIUS = 6378137.0;

	public static double y2lat(double aY) {
		/*return Math.toDegrees(2 * Math.atan(Math.exp(Math.toRadians(aY*Math.PI/180))) - Math.PI / 2);*/
		return 180/Math.PI * (2 * Math.atan(Math.exp(aY * Math.PI / 180)) - Math.PI / 2);
	}

	/*public static double lat2y(double aLat) {
		return Math.log(Math.tan(Math.PI / 4 + Math.toRadians(aLat) / 2)) * RADIUS;
	}*/

	public static double x2lon(double aX) {
		return Math.toDegrees(aX / RADIUS);
	}

	/*public static double lon2x(Angle aLong) {
		return aLong.radians * RADIUS;
	}*/

}
