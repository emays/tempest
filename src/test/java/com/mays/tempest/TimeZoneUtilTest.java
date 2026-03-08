package com.mays.tempest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Comparator;
import java.util.TreeSet;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TimeZoneUtilTest {

	private static final Logger logger = LoggerFactory.getLogger(TimeZoneUtilTest.class);

	private final static boolean trace = false;

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
		if (trace)
			zones.forEach(x -> logger.info("" + x));
		logger.info("Zones: " + zones.size());
		for (ZoneId tz : TimeZoneUtil.getInstance().getZoneIds()) {
			if (!zones.contains(tz))
				logger.warn("Not used: " + tz);
		}
	}

	@Test
	public void utc() {
		TimeZoneUtil tzu = TimeZoneUtil.getGlobal();
		int error_cnt = 0;
		for (double i = -180; i <= 180; i = i + 0.1) {
			i = Math.round(i * 10.0) / 10.0; // double starts to drift
			ZoneId tz = tzu.getTimeZone(85, i);
			ZoneId tzz = TimeZoneUtil.getUTC(i);
			ZoneId tzn = tz.normalized();
			ZoneId tzzn = tzz.normalized();
			if (!tzn.equals(tzzn)) {
				logger.error(
						i + " " + tz + " " + tzz + " " + tz.normalized() + " " + tzz.normalized() + " " + (i / 15.0));
				error_cnt++;
			}
			assertTrue((((ZoneOffset) tzn).getTotalSeconds()) - (((ZoneOffset) tzzn).getTotalSeconds()) <= 60);
		}
		assertEquals(10, error_cnt);
	}

}
