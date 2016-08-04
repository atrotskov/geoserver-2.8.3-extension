import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.geotools.coverage.grid.GridCoordinates2D;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.GridCoverage2DReader;
import org.geotools.geometry.DirectPosition2D;
import org.geotools.geometry.Envelope2D;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.referencing.CRS;
import org.opengis.geometry.DirectPosition;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Polygon;

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
	
	public static double getBasePlaneValue(Coordinate[] coords, GridCoverage2DReader reader) throws IOException {
		GridCoverage2D coverage = reader.read(null);
		//GridGeometry2D geometry = coverage.getGridGeometry();
		int numBands = reader.getGridCoverageCount();
		double[] vals = new double[numBands];
		
		double minValue = 20000.0;
		
		for (Coordinate coordinate : coords) {
			DirectPosition c = new DirectPosition2D(coordinate.x, coordinate.y);
			double resault = coverage.evaluate(c, vals)[0];
			if (resault < minValue) {
				minValue = resault;
			}
		}
		return minValue;
	}
	
	public static Coordinate[] convertCoordinates(GridCoverage2D coverage, Coordinate[] coords)
			throws TransformException, FactoryException {
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
	
	public static Coordinate[] getExtremeCorners(Coordinate[] coords) {
		Coordinate firstCorner = new Coordinate(coords[0]);
		Coordinate secondCorner = new Coordinate(coords[0]);
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
		System.out.println("Extreme corners are:");
		System.out.println("First Corner = " + firstCorner.x + " / " + firstCorner.y);
		System.out.println("Second Corner = " + secondCorner.x + " / " + secondCorner.y);
		return res;	
	}
	
	public static double getPixelDim(GridGeometry2D geometry) throws TransformException {
		Envelope2D pixEnv1 = geometry.gridToWorld(new GridEnvelope2D(0, 0, 1, 1));
		return pixEnv1.height * pixEnv1.width;		
	}
	
	public static void readGeoTiff(Coordinate[] coords, GridCoverage2DReader reader, ResaultResponse resResponse)
			throws IOException, TransformException, FactoryException {
		GridCoverage2D coverage = reader.read(null);
		GridGeometry2D geometry = coverage.getGridGeometry();
		int numBands = reader.getGridCoverageCount();
		double[] vals = new double[numBands];
		
		
		/*Обрабатываем geoJson, получаем координаты, создаем по координатам полигон*/
		
		coords = GeoTiffUtils.convertCoordinates(coverage, coords);
		Coordinate[] extrimeCorners = GeoTiffUtils.getExtremeCorners(coords);
				
		GridCoordinates2D startPosition = geometry.worldToGrid(
				new DirectPosition2D(extrimeCorners[0].x, extrimeCorners[0].y));
		GridCoordinates2D endPosition = geometry.worldToGrid(
				new DirectPosition2D(extrimeCorners[1].x, extrimeCorners[1].y));
		
		GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();
		Polygon polygon = geometryFactory.createPolygon(coords);		
		
		
				
		
		resResponse.setArea(polygon.getArea());
		resResponse.setPerimeter(polygon.getLength());

		double repPoint = getBasePlaneValue(coords, reader);
		double minHeight = 20000.0;
		double maxHeight = -18000.0;
		double sumVolumePos = 0.0;
		double sumVolumeNeg = 0.0;
		double temp;
		int pixelsInThePoly = 0;
		int skipPixels = 0;
		
		for (int j = startPosition.y; j <= endPosition.y; j++) {
			for (int i = startPosition.x; i <= endPosition.x; i++) {
				Envelope2D pixelEnvelop = geometry.gridToWorld(new GridEnvelope2D(i, j, 1, 1));
				
				if (polygon.contains(geometryFactory.createPoint(
						new Coordinate(pixelEnvelop.getCenterX(), pixelEnvelop.getCenterY())))) {
					pixelsInThePoly++;
					double result = coverage.evaluate(new GridCoordinates2D(i, j), vals)[0];
					temp = result - repPoint;
					if (result > maxHeight){
						maxHeight = result;
					}
					if (result < minHeight) {
						minHeight = result;
					}
					
					if (result < -18000.0) {
						skipPixels++;
					} else if (temp > 0) {
						sumVolumePos += temp;
					} else {
						sumVolumeNeg += temp;
					}
				};
				
			}
		}
		resResponse.setPixelsInThePoly(pixelsInThePoly);
		resResponse.setBasePlane(repPoint);
		resResponse.setCutVolume(sumVolumePos * getPixelDim(geometry));
		resResponse.setFillVolume(sumVolumeNeg * getPixelDim(geometry));
		resResponse.setSkipedPixels(skipPixels);
		resResponse.setMaxHeight(maxHeight);
		resResponse.setMinHeight(minHeight);

		System.out.println("Pixel area:" + getPixelDim(geometry) + " m^2");
	}

}
