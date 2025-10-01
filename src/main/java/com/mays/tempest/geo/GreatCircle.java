package com.mays.tempest.geo;

public class GreatCircle {

	// http://edwilliams.org/avform147.htm

	// http://edwilliams.org/gccalc.htm
	
	// https://www.nhc.noaa.gov/gccalc.shtml

	// angle_radians=(pi/180)*angle_degrees
	// angle_degrees=(180/pi)*angle_radians
	// distance_radians=(pi/(180*60))*distance_nm
	// distance_nm=((180*60)/pi)*distance_radians

	// d=acos(sin(lat1)*sin(lat2)+cos(lat1)*cos(lat2)*cos(lon1-lon2))

	// d=2*asin(sqrt((sin((lat1-lat2)/2))^2 +
	// cos(lat1)*cos(lat2)*(sin((lon1-lon2)/2))^2))

	public static double degreesToRadians(double degrees) {
		return (Math.PI / 180.0) * degrees;
	}

	public static double radiansToNm(double radians) {
		return ((180.0 * 60.0) / Math.PI) * radians;
	}

	public static double distance(double lat1, double lon1, double lat2, double lon2) {
		lat1 = degreesToRadians(lat1);
		lon1 = degreesToRadians(lon1);
		lat2 = degreesToRadians(lat2);
		lon2 = degreesToRadians(lon2);
		double d = Math.acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon1 - lon2));
		return radiansToNm(d);
	}

}
