package com.mays.tempest;

import java.time.ZoneId;
import java.util.List;

import com.mays.tempest.geo.ContiguousUS;
import com.mays.tempest.geo.Coordinate;

import net.iakovlev.timeshape.TimeZoneEngine;

public class TimeZoneUtil {

	private static TimeZoneUtil instance;

	private static TimeZoneEngine timeZoneEngine;

	public static final int MIN_LATITUDE = (int) Math.round(ContiguousUS.SOUTHERNMOST.getLatitude() - 1);

	public static final int MIN_LONGITUDE = (int) Math.round(ContiguousUS.WESTERNMOST.getLongitude() - 1);

	public static final int MAX_LATITUDE = (int) Math.round(ContiguousUS.NORTHERNMOST.getLatitude() + 1);

	public static final int MAX_LONGITUDE = (int) Math.round(ContiguousUS.EASTERNMOST.getLongitude() + 1);

	public static TimeZoneUtil getInstance() {
		if (instance == null) {
			instance = new TimeZoneUtil();
			timeZoneEngine = TimeZoneEngine.initialize(MIN_LATITUDE, MIN_LONGITUDE, MAX_LATITUDE, MAX_LONGITUDE, false);
			// timeZoneEngine = TimeZoneEngine.initialize(20, -130, 50, -60, false);
		}
		return instance;
	}

	public static boolean inBounds(double latitude, double longitude) {
		return new Coordinate(latitude, longitude).inBounds(MAX_LATITUDE, MIN_LATITUDE, MAX_LONGITUDE, MIN_LONGITUDE);
	}

	public static List<String> inBoundsString(double latitude, double longitude) {
		return List.of("Bounds for latitude " + latitude + ": >= " + MIN_LATITUDE + " & <= " + MAX_LATITUDE, //
				"Bounds for longitude " + longitude + ": >= " + MIN_LONGITUDE + " & <= " + MAX_LONGITUDE);
	}

	public ZoneId getTimeZone(double latitude, double longitude) {
		return timeZoneEngine.query(latitude, longitude).orElse(null);
	}

	public List<ZoneId> getZoneIds() {
		return timeZoneEngine.getKnownZoneIds();
	}

}
