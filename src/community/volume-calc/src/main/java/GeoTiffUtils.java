import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.batik.svggen.font.table.Coverage;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.geometry.Envelope2D;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

import com.vividsolutions.jts.geom.Coordinate;

public class GeoTiffUtils {
	private static final String REG_EXP = "([0-9]*\\.[0-9]*),([0-9]*\\.[0-9]*)";
	private static final String SOURCE_CRS = "EPSG:4326";
	
	public static Coordinate[] extractCoordinates(String geoJSON) {
		List<Coordinate> listOfCoordinates = new ArrayList<>();		
		Pattern p = Pattern.compile(REG_EXP);
		Matcher m = p.matcher(geoJSON);
		System.out.println("Extracted coodinates:");
		while(m.find()){
			double x = Double.parseDouble(m.group(1));
			double y = Double.parseDouble(m.group(2));
			listOfCoordinates.add(new Coordinate(x,y));
			System.out.println("x = " + x + " / y = " + y);
		}
		Coordinate[] coords  = new Coordinate[listOfCoordinates.size()];
		coords = listOfCoordinates.toArray(coords);
		return coords;
	}
	
	public static CoordinateReferenceSystem getTargetCRS(GridCoverage2D coverage) {
		return coverage.getCoordinateReferenceSystem();
	}
	
	public static Coordinate[] convertCoordinates(GridCoverage2D coverage, Coordinate[] coords) throws TransformException, FactoryException {
		CoordinateReferenceSystem sourceCRS = CRS.decode(SOURCE_CRS);
		CoordinateReferenceSystem targetCRS = getTargetCRS(coverage);
		MathTransform transform = CRS.findMathTransform(sourceCRS, targetCRS);
		System.out.println("Transormation of the coordinates:");
		for (int i = 0; i < coords.length; i++) {
			System.out.print("Was: " + coords[i] + " / ");
			JTS.transform(coords[i], coords[i], transform);
			System.out.println("Become: " + coords[i]);	
		}
		System.out.println("End of transformation");
		return coords;
	}
	
	public static boolean isCoordinatesInTheBorder(Coordinate[] coords, GridCoverage2D coverage) {
		Envelope2D envelope = coverage.getEnvelope2D();
		CoordinateReferenceSystem crs = envelope.getCoordinateReferenceSystem();
		System.out.println("Envelope - " + crs.getName());
		for (Coordinate coordinate : coords) {
			if (!envelope.contains(coordinate.x, coordinate.y)) {
				return false;
			}
		}
		return true;
	}
	
	public static Coordinate[] getExtrimeCorners(Coordinate[] coords) {
		Coordinate firstCorner = coords[0];
		Coordinate secondCorner = coords[0];
		for (Coordinate coordinate : coords) {
			if (coordinate.x < firstCorner.x) {
				firstCorner.x = coordinate.x;
			}
			if (coordinate.y > firstCorner.y) {
				firstCorner.y = coordinate.y;			
			}
			if (coordinate.x > secondCorner.x) {
				secondCorner.x = coordinate.x;
			}
			if (coordinate.y < secondCorner.y) {
				secondCorner.y = coordinate.y;
			}
		}
		Coordinate[] res = {firstCorner, secondCorner};
		System.out.println("Extrime corner is:");
		System.out.println("First Corner = " + firstCorner.x + " / " + firstCorner.y);
		System.out.println("Second Corner = " + secondCorner.x + " / " + secondCorner.y);
		return res;
		
	}
	
	public static double getAbsDimOfPixel () {
		return 0;		
	}

}
