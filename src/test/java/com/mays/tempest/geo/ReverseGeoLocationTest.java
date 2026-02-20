package com.mays.tempest.geo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.ZoneId;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mays.tempest.LocationInfo;
import com.mays.tempest.TimeZoneUtil;
import com.mays.tempest.geo.ReverseGeoLocation.Location;

public class ReverseGeoLocationTest {

	private static final Logger logger = LoggerFactory.getLogger(ReverseGeoLocationTest.class);

	private final static boolean trace = false;

	private static ReverseGeoLocation rgl;

	@BeforeAll
	public static void setTest() {
		ReverseGeoLocation.setTest();
	}

	@BeforeEach
	public void getInstance() throws Exception {
		if (rgl == null)
			rgl = ReverseGeoLocation.getInstance();
	}

	@Test
	public void nearest() {
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
	public void grid() {
		int cnt = 0;
		long start = System.currentTimeMillis();
		for (double lat = ContiguousUS.NORTHERNMOST.getLatitude(); lat > ContiguousUS.SOUTHERNMOST
				.getLatitude(); lat = lat - 0.5) {
			for (double lon = ContiguousUS.EASTERNMOST.getLongitude(); lon > ContiguousUS.WESTERNMOST
					.getLongitude(); lon = lon - 0.5) {
				// Location loc =
				rgl.getNearest(lat, lon);
				cnt++;
				if (cnt % 1000 == 0)
					if (trace)
						logger.info(lat + " " + lon + " " + cnt);
			}
		}
		long end = System.currentTimeMillis();
		double time = (end - start) / 1000.0;
		logger.info("Processed " + cnt + " in " + String.format("%.1f secs", time) + " @ " + Math.round(cnt / time)
				+ "/sec");
	}

	@Test
	public void timezone() {
		int error_cnt = 0;
		for (Location location : rgl.getLocations()) {
			ZoneId tz = TimeZoneUtil.getInstance().getTimeZone(location.getLatitude(), location.getLongitude());
			try {
				if (!location.getTimezone().equals(tz.getId())) {
					logger.error(tz.getId() + " " + location);
					error_cnt++;
				}
			} catch (Exception ex) {
				logger.error(ex.getMessage());
				logger.error("\t" + location);
			}
		}
		assertEquals(8, error_cnt);
	}

}
