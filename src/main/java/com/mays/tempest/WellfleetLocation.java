package com.mays.tempest;

import java.time.ZoneId;

import com.mays.tempest.geo.Coordinate;

@Deprecated
public class WellfleetLocation {

	@Deprecated
	public static final Coordinate COORDINATE = new Coordinate(41.929669, -70.029542);

	@Deprecated
	public static final double LATITUDE = COORDINATE.getLatitude();

	@Deprecated
	public static final double LONGITUDE = COORDINATE.getLongitude();

	@Deprecated
	public static final ZoneId TZ = ZoneId.of("America/New_York");

	@Deprecated
	public static final String TIDE_STATION_ID = "8446613"; // Wellfleet

	@Deprecated
	public static final String BUOY_STATION_ID = "44090"; // Cape Cod Bay

	@Deprecated
	public static final String WEATHER_STATION_ID = "KPVC";

}
