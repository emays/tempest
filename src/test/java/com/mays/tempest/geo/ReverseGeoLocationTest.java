package com.mays.tempest.geo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mays.tempest.LocationInfo;
import com.mays.tempest.geo.ReverseGeoLocation.Geo;
import com.mays.tempest.geo.ReverseGeoLocation.Location;

public class ReverseGeoLocationTest {

	private static final Logger logger = LoggerFactory.getLogger(ReverseGeoLocationTest.class);

	private final static boolean trace = false;

	@BeforeAll
	public static void setTest() {
		ReverseGeoLocation.setTest();
	}

	@Test
	public void nearest() throws Exception {
		ReverseGeoLocation rgl = ReverseGeoLocation.getInstance();
		Location loc = rgl.getNearest(LocationInfo.COLD_STORAGE.getLatitude(),
				LocationInfo.COLD_STORAGE.getLongitude());
		if (trace)
			logger.info("Nearest: " + loc);
		assertEquals("Truro", loc.getName());
		assertEquals("MA", loc.getState());
		assertEquals(41.99344, loc.getLatitude());
		assertEquals(-70.04975, loc.getLongitude());
		assertEquals(3, loc.getElevation());
		assertEquals("America/New_York", loc.getTimezone());
	}

	@Test
	public void nearestCities() throws Exception {
		ReverseGeoLocation rgl = ReverseGeoLocation.getInstance(Geo.CITIES);
		Location loc = rgl.getNearest(LocationInfo.COLD_STORAGE.getLatitude(),
				LocationInfo.COLD_STORAGE.getLongitude());
		if (trace)
			logger.info("Nearest: " + loc);
		assertEquals("Yarmouth", loc.getName());
		assertEquals("MA", loc.getState());
		assertEquals(41.70567, loc.getLatitude());
		assertEquals(-70.22863, loc.getLongitude());
		assertEquals(10, loc.getElevation());
		assertEquals("America/New_York", loc.getTimezone());
	}

	@Test
	public void extremes() throws Exception {
		ReverseGeoLocation rgl = ReverseGeoLocation.getInstance();
		Location north = null;
		Location south = null;
		Location east = null;
		Location west = null;
		ContiguousUS cu = new ContiguousUS();
		for (Location location : rgl.getLocations()) {
			if (!cu.inBounds(new Coordinate(location.getLatitude(), location.getLongitude())))
				continue;
			if (north == null) {
				north = location;
				south = location;
				east = location;
				west = location;
			}
			if (location.getLatitude() > north.getLatitude())
				north = location;
			if (location.getLatitude() < south.getLatitude())
				south = location;
			if (location.getLongitude() > east.getLongitude())
				east = location;
			if (location.getLongitude() < west.getLongitude())
				west = location;
		}
		if (trace) {
			logger.info("N: " + north.toString());
			logger.info("S: " + south.toString());
			logger.info("E: " + east.toString());
			logger.info("W: " + west.toString());
		}
		assertEquals(ContiguousUS.NORTHERNMOST.getLatitude(), north.getLatitude(), 0.1);
		assertEquals(ContiguousUS.SOUTHERNMOST.getLatitude(), south.getLatitude(), 0.1);
		assertEquals(ContiguousUS.EASTERNMOST.getLongitude(), east.getLongitude(), 0.1);
		assertEquals(ContiguousUS.WESTERNMOST.getLongitude(), west.getLongitude(), 0.15);
	}

}
