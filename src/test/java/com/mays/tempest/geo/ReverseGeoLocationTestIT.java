package com.mays.tempest.geo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.TreeSet;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mays.tempest.TimeZoneUtil;
import com.mays.tempest.geo.ReverseGeoLocation.Geo;
import com.mays.tempest.geo.ReverseGeoLocation.Location;
import com.mays.tempest.timezone.TimeZoneEngine;

public class ReverseGeoLocationTestIT {

	private static final Logger logger = LoggerFactory.getLogger(ReverseGeoLocationTestIT.class);

	private final static boolean trace = false;

	@BeforeAll
	public static void setTest() {
		ReverseGeoLocation.setTest();
		TimeZoneEngine.setTest();
	}

	@Test
	public void grid() throws Exception {
		TimeZoneUtil tzu = TimeZoneUtil.getInstance();
		ReverseGeoLocation rgl = ReverseGeoLocation.getInstance(Geo.US);
		TreeSet<ZoneId> zones = new TreeSet<>(Comparator.comparing(ZoneId::toString));
		int cnt = 0;
		HashSet<Location> processed = new HashSet<>();
		long start = System.currentTimeMillis();
		for (double lat = ContiguousUS.NORTHERNMOST.getLatitude(); lat > ContiguousUS.SOUTHERNMOST
				.getLatitude(); lat = lat - 0.5) {
			for (double lon = ContiguousUS.EASTERNMOST.getLongitude(); lon > ContiguousUS.WESTERNMOST
					.getLongitude(); lon = lon - 0.5) {
				Location loc = rgl.getNearest(lat, lon);
				if (!processed.add(loc))
					continue;
				ZoneId tz = tzu.getTimeZone(loc.getLatitude(), loc.getLongitude());
				assertNotNull(tz, loc.getLatitude() + " " + loc.getLongitude());
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
			logger.info("Zones:");
			zones.forEach(x -> logger.info("\t" + x));
		}
		assertEquals(5, zones.size());
	}

	@Test
	public void gridCities() throws Exception {
		TimeZoneUtil tzu = TimeZoneUtil.getInstance();
		ReverseGeoLocation rgl = ReverseGeoLocation.getInstance(Geo.CITIES);
		TreeSet<ZoneId> zones = new TreeSet<>(Comparator.comparing(ZoneId::toString));
		int cnt = 0;
		HashSet<Location> processed = new HashSet<>();
		long start = System.currentTimeMillis();
		for (double lat = 90; lat > -90; lat--) {
			for (double lon = 180; lon > -180; lon--) {
				Location loc = rgl.getNearest(lat, lon);
				if (!processed.add(loc))
					continue;
				ZoneId tz = tzu.getTimeZone(loc.getLatitude(), loc.getLongitude());
				assertNotNull(tz, loc.getLatitude() + " " + loc.getLongitude());
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
			logger.info("Zones:");
			zones.forEach(x -> logger.info("\t" + x));
		}
		assertEquals(56, zones.size());
	}

	@Test
	public void timezone() throws Exception {
		TimeZoneUtil tzu = TimeZoneUtil.getContiguousUS();
		ReverseGeoLocation rgl = ReverseGeoLocation.getInstance();
		HashSet<ZoneId> zones = new HashSet<>(tzu.getZoneIds());
		if (trace)
			zones.forEach(zone -> logger.info("Zone: " + zone));
		int skip_cnt = 0;
		HashSet<ZoneId> skipped_zones = new HashSet<>();
		int error_cnt = 0;
		for (Location location : rgl.getLocations()) {
			ZoneId expect = ZoneId.of(location.getTimezone());
			if (!zones.contains(expect)) {
				skip_cnt++;
				skipped_zones.add(expect);
				continue;
			}
			ZoneId tz = tzu.getTimeZone(location.getLatitude(), location.getLongitude());
			if (!expect.equals(tz)) {
				if (trace)
					logger.error(location + "\n\tExpect:\t" + expect + "\n\tActual:\t" + tz);
				error_cnt++;
			}
		}
		logger.info("Skipped: " + skip_cnt + " of " + rgl.getLocations().size());
		logger.info("Skipped zones: " + skipped_zones.size());
		if (trace)
			skipped_zones.forEach(zone -> logger.info("Skip zone: " + zone));
		assertEquals(13, zones.size());
		assertEquals(2203, skip_cnt);
		assertEquals(24, skipped_zones.size());
		assertEquals(10, error_cnt);
	}

	@Test
	public void timezoneCities() throws Exception {
		TimeZoneUtil tzu = TimeZoneUtil.getInstance();
		ReverseGeoLocation rgl = ReverseGeoLocation.getInstance(Geo.CITIES);
		HashSet<ZoneId> zones = new HashSet<>(tzu.getZoneIds());
		if (trace)
			zones.forEach(zone -> logger.info("Zone: " + zone));
		int skip_cnt = 0;
		HashSet<ZoneId> skipped_zones = new HashSet<>();
		int error_cnt = 0;
		ArrayList<String> multis = new ArrayList<>();
		for (Location location : rgl.getLocations()) {
			ZoneId expect = ZoneId.of(location.getTimezone());
			if (!zones.contains(expect)) {
				skip_cnt++;
				skipped_zones.add(expect);
				continue;
			}
			ZoneId tz = TimeZoneUtil.getInstance().getTimeZone(location.getLatitude(), location.getLongitude());
			if (!expect.equals(tz)) {
				if (trace)
					logger.error(location + "\n\tExpect:\t" + expect + "\n\tActual:\t" + tz);
				error_cnt++;
			}
			ArrayList<ZoneId> tzs = tzu.getTimeZoneAll(location.getLatitude(), location.getLongitude());
			if (tzs.size() > 1)
				multis.add(location + "\t" + tzs);
		}
		Files.write(Paths.get("target", "multi.txt"), multis);
		logger.info("Skipped: " + skip_cnt + " of " + rgl.getLocations().size());
		logger.info("Skipped zones: " + skipped_zones.size());
		if (trace)
			skipped_zones.forEach(zone -> logger.info("Skip zone: " + zone));
		assertEquals(16_689, skip_cnt);
		assertEquals(300, skipped_zones.size());
		assertEquals(3, error_cnt);
		assertEquals(3, multis.size());
	}

}
