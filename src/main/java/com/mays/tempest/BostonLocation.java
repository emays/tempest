package com.mays.tempest;

import java.time.ZoneId;

import com.mays.tempest.geo.Coordinate;

@Deprecated
public class BostonLocation {

	@Deprecated
	public static final Coordinate COORDINATE = new Coordinate(42.353888, -71.050277);

	@Deprecated
	public static final double LATITUDE = COORDINATE.getLatitude();

	@Deprecated
	public static final double LONGITUDE = COORDINATE.getLongitude();

	@Deprecated
	public static final ZoneId TZ = ZoneId.of("America/New_York");

	@Deprecated
	public static final String TIDE_STATION_ID = "8443970"; // Boston

	@Deprecated
	public static final String BUOY_STATION_ID = "44013"; // Boston Approach

	@Deprecated
	public static final String WEATHER_STATION_ID = "KBOS";

}
