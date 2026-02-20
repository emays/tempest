package com.mays.tempest;

import java.time.ZoneId;

import com.mays.tempest.geo.Coordinate;

@Deprecated
public class PametLocation {

	public static final Coordinate COORDINATE = new Coordinate(41.991605, -70.071953);

	public static final double LATITUDE = COORDINATE.getLatitude();

	public static final double LONGITUDE = COORDINATE.getLongitude();

	public static final ZoneId TZ = ZoneId.of("America/New_York");

	public static final String TIDE_STATION_ID = "8446613"; // Wellfleet

	public static final String BUOY_STATION_ID = "44090"; // Cape Cod Bay

	public static final String WEATHER_STATION_ID = "KPVC";

}
