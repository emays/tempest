package com.mays.tempest.geo;

public class GreatCircle {

	// http://edwilliams.org/avform147.htm

	// http://edwilliams.org/gccalc.htm

	// https://www.nhc.noaa.gov/gccalc.shtml

	// angle_radians=(pi/180)*angle_degrees

	public static double degreesToRadians(double degrees) {
		return (Math.PI / 180.0) * degrees;
	}

	// angle_degrees=(180/pi)*angle_radians

	public static double radiansToDegrees(double radians) {
		return (180.0 / Math.PI) * radians;
	}

	// distance_radians=(pi/(180*60))*distance_nm

	public static double nmToRadians(double distance) {
		return (Math.PI / (180.0 * 60.0)) * distance;
	}

	// distance_nm=((180*60)/pi)*distance_radians

	public static double radiansToNm(double radians) {
		return ((180.0 * 60.0) / Math.PI) * radians;
	}

	// d=acos(sin(lat1)*sin(lat2)+cos(lat1)*cos(lat2)*cos(lon1-lon2))

	public static double distance(double lat1, double lon1, double lat2, double lon2) {
		lat1 = degreesToRadians(lat1);
		lon1 = degreesToRadians(lon1);
		lat2 = degreesToRadians(lat2);
		lon2 = degreesToRadians(lon2);
		double d = Math.acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon1 - lon2));
		return radiansToNm(d);
	}

	public static double distance(Coordinate coord1, Coordinate coord2) {
		return distance(coord1.getLatitude(), coord1.getLongitude(), coord2.getLatitude(), coord2.getLongitude());
	}

	// lat=asin(sin(lat1)*cos(d)+cos(lat1)*sin(d)*cos(tc))
	// IF (cos(lat)=0)
	// lon=lon1 // endpoint a pole
	// ELSE
	// lon=mod(lon1-asin(sin(tc)*sin(d)/cos(lat))+pi,2*pi)-pi

	// mod(y,x) = y - x*floor(y/x)

	public static Coordinate radialDistance(double lat1, double lon1, double radial, double distance) {
		boolean neg = lon1 < 0;
		lon1 = Math.abs(lon1);
		lat1 = degreesToRadians(lat1);
		lon1 = degreesToRadians(lon1);
		radial = degreesToRadians(radial);
		distance = nmToRadians(distance);
		double lat = Math
				.asin(Math.sin(lat1) * Math.cos(distance) + Math.cos(lat1) * Math.sin(distance) * Math.cos(radial));
		double y = lon1 - Math.asin(Math.sin(radial) * Math.sin(distance) / Math.cos(lat));
		y = y + Math.PI;
		double x = 2 * Math.PI;
		double lon = (y - x * Math.floor(y / x)) - Math.PI;
		if (neg)
			lon = -lon;
		return Coordinate.of(radiansToDegrees(lat), radiansToDegrees(lon));
	}

}
