package com.mays.tempest;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;

import com.mays.tempest.geo.ContiguousUS;
import com.mays.tempest.geo.Coordinate;

import net.iakovlev.timeshape.TimeZoneEngine;

public class TimeZoneUtil {

	private static TimeZoneUtil instance;

	private TimeZoneEngine timeZoneEngine;

	// NM = one minute (⁠1/60 of a degree) of latitude at the equator
	// territorial waters = 12 NM

	private static final double round_offset = 1; // (12.0 / 60.0) + 0.5;

	public static final int MIN_LATITUDE = (int) Math.round(ContiguousUS.SOUTHERNMOST.getLatitude() - round_offset);

	public static final int MIN_LONGITUDE = (int) Math.round(ContiguousUS.WESTERNMOST.getLongitude() - round_offset);

	public static final int MAX_LATITUDE = (int) Math.round(ContiguousUS.NORTHERNMOST.getLatitude() + round_offset);

	public static final int MAX_LONGITUDE = (int) Math.round(ContiguousUS.EASTERNMOST.getLongitude() + round_offset);

	public static TimeZoneUtil getInstance() {
		if (instance == null) {
			instance = new TimeZoneUtil();
			instance.timeZoneEngine = TimeZoneEngine.initialize(MIN_LATITUDE, MIN_LONGITUDE, MAX_LATITUDE,
					MAX_LONGITUDE, false);
			// timeZoneEngine = TimeZoneEngine.initialize(20, -130, 50, -60, false);
		}
		return instance;
	}

	// Just for testing
	static TimeZoneUtil getGlobal() {
		TimeZoneUtil tzu = new TimeZoneUtil();
		tzu.timeZoneEngine = TimeZoneEngine.initialize();
		return tzu;
	}

	public static boolean inBounds(double latitude, double longitude) {
		return new Coordinate(latitude, longitude).inBounds(MAX_LATITUDE, MIN_LATITUDE, MAX_LONGITUDE, MIN_LONGITUDE);
	}

	public static List<String> inBoundsString(double latitude, double longitude) {
		return List.of("Bounds for latitude " + latitude + ": >= " + MIN_LATITUDE + " & <= " + MAX_LATITUDE, //
				"Bounds for longitude " + longitude + ": >= " + MIN_LONGITUDE + " & <= " + MAX_LONGITUDE);
	}

	public ZoneId getTimeZone(double latitude, double longitude) {
		ZoneId tz = timeZoneEngine.query(latitude, longitude).orElse(null);
		if (tz == null)
			tz = getUTC(longitude);
		return tz.normalized();
	}

	public List<ZoneId> getZoneIds() {
		return timeZoneEngine.getKnownZoneIds();
	}

	// https://en.wikipedia.org/wiki/Tz_database

	// The special area of "Etc" is used for some administrative zones, particularly
	// for "Etc/UTC" which represents Coordinated Universal Time. In order to
	// conform with the POSIX style, those zone names beginning with "Etc/GMT" have
	// their sign reversed from the standard ISO 8601 convention. In the "Etc" area,
	// zones west of GMT have a positive sign and those east have a negative sign in
	// their name (e.g "Etc/GMT-14" is 14 hours ahead of GMT).

	public static ZoneId getUTC(double longitude) {
		return ZoneId.ofOffset("UTC", ZoneOffset.ofHours((int) Math.round(longitude / 15.0)));
	}

}
