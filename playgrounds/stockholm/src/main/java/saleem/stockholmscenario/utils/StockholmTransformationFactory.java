package saleem.stockholmscenario.utils;

import org.matsim.core.utils.geometry.CoordinateTransformation;
import org.matsim.core.utils.geometry.transformations.AtlantisToWGS84;
import org.matsim.core.utils.geometry.transformations.CH1903LV03PlustoWGS84;
import org.matsim.core.utils.geometry.transformations.CH1903LV03toWGS84;
import org.matsim.core.utils.geometry.transformations.GK4toWGS84;
import org.matsim.core.utils.geometry.transformations.GeotoolsTransformation;
import org.matsim.core.utils.geometry.transformations.IdentityTransformation;
import org.matsim.core.utils.geometry.transformations.TransformationFactory;
import org.matsim.core.utils.geometry.transformations.WGS84toAtlantis;
import org.matsim.core.utils.geometry.transformations.WGS84toCH1903LV03;
import org.matsim.core.utils.geometry.transformations.WGS84toCH1903LV03Plus;

public class StockholmTransformationFactory extends TransformationFactory{
	public final static String WGS84 = "WGS84";
	public final static String ATLANTIS = "Atlantis";
	public final static String CH1903_LV03 = "CH1903_LV03"; // switzerland
	public final static String CH1903_LV03_Plus = "CH1903_LV03_Plus"; // switzerland new
	public final static String GK4 = "GK4"; // berlin/germany, own implementation
	public final static String WGS84_UTM47S = "WGS84_UTM47S"; // indonesia
	public final static String WGS84_UTM48N = "WGS84_UTM48N"; // Singapore
	public final static String WGS84_UTM35S = "WGS84_UTM35S"; // South Africa (Gauteng)
	public final static String WGS84_UTM36S = "WGS84_UTM36S"; // South Africa (eThekwini, Kwazulu-Natal)
	public final static String WGS84_Albers = "WGS84_Albers"; // South Africa (Africa Albers Equal Conic)
	public final static String WGS84_SA_Albers = "WGS84_SA_Albers"; // South Africa (Adapted version of Africa Albers Equal) 
	public final static String WGS84_UTM33N = "WGS84_UTM33N"; // Berlin
	public final static String DHDN_GK4 = "DHDN_GK4"; // berlin/germany, for GeoTools
	public final static String WGS84_UTM29N = "WGS84_UTM29N"; // coimbra/portugal
	public final static String CH1903_LV03_GT = "CH1903_LV03_GT"; //use geotools also for swiss coordinate system
	public final static String CH1903_LV03_Plus_GT = "CH1903_LV03_Plus_GT"; //use geotools also for swiss coordinate system
	public final static String WGS84_SVY21 = "WGS84_SVY21"; //Singapore2
	public final static String NAD83_UTM17N = "NAD83_UTM17N"; //Toronto, Canada
	public static final String WGS84_TM = "WGS84_TM"; //Singapore3
	public final static String WGS84_RT90 = "WGS84toRT90";
	public final static String WGS84_SWEREF99 = "WGS84toSWEREF99";
	public static final String PCS_ITRF2000_TM_UOS = "PCS_ITRF2000_TM_UOS"; // South Korea - but used by University of Seoul - probably a wrong one...

	/**
	 * Returns a coordinate transformation to transform coordinates from one
	 * coordinate system to another one.
	 *
	 * @param fromSystem The source coordinate system.
	 * @param toSystem The destination coordinate system.
	 * @return Coordinate Transformation
	 */
	public static CoordinateTransformation getCoordinateTransformation(final String fromSystem, final String toSystem) {
		if (fromSystem.equals(toSystem)) return new IdentityTransformation();
		if (WGS84.equals(fromSystem)) {
			if (CH1903_LV03.equals(toSystem)) return new WGS84toCH1903LV03();
			if (CH1903_LV03_Plus.equals(toSystem)) return new WGS84toCH1903LV03Plus();
			if (ATLANTIS.equals(toSystem)) return new WGS84toAtlantis();
		}
		if (WGS84.equals(toSystem)) {
			if (CH1903_LV03.equals(fromSystem)) return new CH1903LV03toWGS84();
			if (CH1903_LV03_Plus.equals(fromSystem)) return new CH1903LV03PlustoWGS84();
			if (GK4.equals(fromSystem)) return new GK4toWGS84();
			if (ATLANTIS.equals(fromSystem)) return new AtlantisToWGS84();
		}
		return new StockholmGeotoolTransformation(fromSystem, toSystem);
	}
}
