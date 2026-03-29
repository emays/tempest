package com.mays.tempest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mays.tempest.timezone.TimeZoneEngine;

public class TimeZoneUtilTest {

	private static final Logger logger = LoggerFactory.getLogger(TimeZoneUtilTest.class);

	private final static boolean trace = false;

	@BeforeAll
	public static void setTest() {
		TimeZoneEngine.setTest();
	}

	@Test
	public void zones() {
		assertEquals(92, TimeZoneUtil.getInstance().getZoneIds().size());
	}

	@Test
	public void zonesUS() {
		List<ZoneId> zones = TimeZoneUtil.getContiguousUS().getZoneIds();
		if (trace)
			zones.stream().sorted(Comparator.comparing(ZoneId::toString)).forEach(x -> logger.info("" + x));
		assertEquals(13, zones.size());
	}

	@Test
	public void grid() {
		TreeSet<ZoneId> zones = new TreeSet<>(Comparator.comparing(ZoneId::toString));
		int cnt = 0;
		long start = System.currentTimeMillis();
		final double inc = 0.5;
		for (double lat = TimeZoneUtil.MAX_LATITUDE; lat >= TimeZoneUtil.MIN_LATITUDE; lat = lat - inc) {
			for (double lon = TimeZoneUtil.MAX_LONGITUDE; lon >= TimeZoneUtil.MIN_LONGITUDE; lon = lon - inc) {
				ZoneId tz = TimeZoneUtil.getInstance().getTimeZone(lat, lon);
				assertNotNull(tz, lat + " " + lon);
				zones.add(tz);
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
		if (trace) {
			zones.forEach(x -> logger.info("" + x));
			logger.info("Zones: " + zones.size());
			for (ZoneId tz : TimeZoneUtil.getInstance().getZoneIds()) {
				if (!zones.contains(tz))
					logger.warn("Not used: " + tz);
			}
		}
		assertEquals(13, zones.size());
	}

	@Test
	public void utc() {
		int error_cnt = 0;
		for (double lon = -180; lon <= 180; lon = lon + 0.1) {
			lon = Math.round(lon * 10.0) / 10.0; // double starts to drift
			ZoneId tz = TimeZoneUtil.getInstance().getTimeZone(85, lon);
			ZoneId tzz = TimeZoneUtil.getUTC(lon);
			ZoneId tzn = tz.normalized();
			ZoneId tzzn = tzz.normalized();
			if (!tzn.equals(tzzn)) {
				logger.error(lon + " " + tz + " " + tzz + " " + tz.normalized() + " " + tzz.normalized() + " "
						+ (lon / 15.0));
				error_cnt++;
			}
			assertTrue((((ZoneOffset) tzn).getTotalSeconds()) - (((ZoneOffset) tzzn).getTotalSeconds()) <= 60);
		}
		assertEquals(0, error_cnt);
	}

	@Test
	public void utcZones() {
		TreeSet<ZoneId> zones = new TreeSet<>(Comparator.comparing(ZoneId::toString));
		for (double lon = -180; lon <= 180; lon = lon + 0.1) {
			lon = Math.round(lon * 10.0) / 10.0; // double starts to drift
			ZoneId tz = TimeZoneUtil.getInstance().getTimeZone(85, lon);
			zones.add(tz);
		}
		if (trace)
			zones.forEach(x -> logger.info("" + x));
		assertEquals(25, zones.size());
	}

	@Test
	public void zoneBounds() {
		int cnt = 0;
		ZoneId prior_tz = null;
		for (double lon = -180; lon <= 180; lon = lon + 0.1) {
			lon = Math.round(lon * 10.0) / 10.0; // double starts to drift
			ZoneId tz = TimeZoneUtil.getInstance().getTimeZone(45, lon);
			if (!tz.equals(prior_tz)) {
				if (trace)
					logger.info(tz + " " + lon);
				cnt++;
			}
			prior_tz = tz;
		}
		assertEquals(32, cnt);
	}

}
