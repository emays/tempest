package com.mays.tempest;

import java.time.ZoneId;

import com.mays.tempest.geo.Coordinate;

@Deprecated
public class BostonLocation {

	public static final Coordinate COORDINATE = new Coordinate(42.353888, -71.050277);

	public static final double LATITUDE = COORDINATE.getLatitude();

	public static final double LONGITUDE = COORDINATE.getLongitude();

	public static final ZoneId TZ = ZoneId.of("America/New_York");

	public static final String TIDE_STATION_ID = "8443970"; // Boston

	public static final String BUOY_STATION_ID = "44013"; // Boston Approach

	public static final String WEATHER_STATION_ID = "KBOS";

}
