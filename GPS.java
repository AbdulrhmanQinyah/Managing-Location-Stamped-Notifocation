
public class GPS {
	public static final double EarthRadiusInM = 6378137.0; // Earth radius in meters

	// Gives the distance between two GPS points in meters.
	public static double dist(double lat1, double lng1, double lat2, double lng2) {
		double dLat = (lat2 - lat1) * Math.PI / 180;
		double dLng = (lng2 - lng1) * Math.PI / 180;

		lat1 *= Math.PI / 180;
		lat2 *= Math.PI / 180;

		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.sin(dLng / 2) * Math.sin(dLng / 2) * Math.cos(lat1) * Math.cos(lat2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		return EarthRadiusInM * c;
	}

	// Converts distance in meters on the surface of Earth to angles (in degrees).
	public static double angle(double dst) {
		return dst / EarthRadiusInM * 180 / Math.PI;
	}

}
