import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.vividsolutions.jts.geom.Coordinate;

public class GeoTiffUtils {
	private static final String REG_EXP = "([0-9]*\\.[0-9]*),([0-9]*\\.[0-9]*)";
	
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
	
	public static double getAbsDimOfPixel () {
		return 0;		
	}

}
