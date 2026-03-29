package com.mays.tempest.timezone;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TimeZoneEngineTest {

	private final static Logger log = LoggerFactory.getLogger(TimeZoneEngineTest.class);

	private final static boolean trace = false;

	private static TimeZoneEngine engine;

	@BeforeAll
	public static void setTest() throws Exception {
		TimeZoneEngine.setTest();
		engine = TimeZoneEngine.initialize();
	}

	private ZoneId query(double latitude, double longtitude) {
		return engine.query(latitude, longtitude).orElse(null);
	}

	private Set<ZoneId> queryAll(double latitude, double longtitude) {
		return Set.copyOf(engine.queryAll(latitude, longtitude));
	}

	@Test
	public void zoneNow() {
		assertEquals(ZoneId.of("Pacific/Pago_Pago"), query(-14.16, -170.42));
		assertEquals(ZoneId.of("Pacific/Honolulu"), query(21.1825, -157.513));
		assertEquals(ZoneId.of("America/Adak"), query(51.5248, -176.3929));
		assertEquals(ZoneId.of("Pacific/Marquesas"), query(-9.0, -139.3));
		assertEquals(ZoneId.of("America/Anchorage"), query(61.1305, -149.5401));
		assertEquals(ZoneId.of("Pacific/Pitcairn"), query(-25.04, -130.05));
		assertEquals(ZoneId.of("America/Los_Angeles"), query(34.0308, -118.1434));
		assertEquals(ZoneId.of("America/Phoenix"), query(33.2654, -112.0424));
		assertEquals(ZoneId.of("America/Denver"), query(39.4421, -104.5903));
		assertEquals(ZoneId.of("America/Mexico_City"), query(19.24, -99.09));
		assertEquals(ZoneId.of("Pacific/Easter"), query(-27.09, -109.26));
		assertEquals(ZoneId.of("America/Chicago"), query(41.51, -87.39));
		assertEquals(ZoneId.of("America/Lima"), query(-12.03, -77.03));
		assertEquals(ZoneId.of("America/Havana"), query(23.08, -82.22));
		assertEquals(ZoneId.of("America/New_York"), query(40.4251, -74.0023));
		assertEquals(ZoneId.of("America/Caracas"), query(10.3, -66.56));
		assertEquals(ZoneId.of("America/Santiago"), query(-33.27, -70.4));
		assertEquals(ZoneId.of("America/Halifax"), query(44.39, -63.36));
		assertEquals(ZoneId.of("America/St_Johns"), query(47.34, -52.43));
		assertEquals(ZoneId.of("America/Sao_Paulo"), query(-23.32, -46.37));
		assertEquals(ZoneId.of("America/Miquelon"), query(47.03, -56.2));
		assertEquals(ZoneId.of("America/Nuuk"), query(64.11, -51.44));
		assertEquals(ZoneId.of("Atlantic/Azores"), query(37.44, -25.4));
		assertEquals(ZoneId.of("Africa/Abidjan"), query(5.19, -4.02));
		assertEquals(ZoneId.of("Europe/London"), query(51.303, -0.0731));
		assertEquals(ZoneId.of("Europe/Lisbon"), query(38.43, -9.08));
		assertEquals(ZoneId.of("Antarctica/Troll"), query(-72.0041, 2.3206));
		assertEquals(ZoneId.of("Africa/Lagos"), query(6.27, 3.24));
		assertEquals(ZoneId.of("Europe/Dublin"), query(53.2, -6.15));
		assertEquals(ZoneId.of("Africa/Casablanca"), query(33.39, -7.35));
		assertEquals(ZoneId.of("Europe/Paris"), query(48.52, 2.2));
		assertEquals(ZoneId.of("Africa/Johannesburg"), query(-26.15, 28.0));
		assertEquals(ZoneId.of("Europe/Athens"), query(37.58, 23.43));
		assertEquals(ZoneId.of("Africa/Cairo"), query(30.03, 31.15));
		assertEquals(ZoneId.of("Asia/Beirut"), query(33.53, 35.3));
		assertEquals(ZoneId.of("Europe/Chisinau"), query(47.0, 28.5));
		assertEquals(ZoneId.of("Asia/Gaza"), query(31.3, 34.28));
		assertEquals(ZoneId.of("Europe/Moscow"), query(55.4521, 37.3704));
		assertEquals(ZoneId.of("Asia/Tehran"), query(35.4, 51.26));
		assertEquals(ZoneId.of("Asia/Dubai"), query(25.18, 55.18));
		assertEquals(ZoneId.of("Asia/Kabul"), query(34.31, 69.12));
		assertEquals(ZoneId.of("Asia/Karachi"), query(24.52, 67.03));
		assertEquals(ZoneId.of("Asia/Kolkata"), query(22.32, 88.22));
		assertEquals(ZoneId.of("Asia/Kathmandu"), query(27.43, 85.19));
		assertEquals(ZoneId.of("Asia/Dhaka"), query(23.43, 90.25));
		assertEquals(ZoneId.of("Asia/Yangon"), query(16.47, 96.1));
		assertEquals(ZoneId.of("Asia/Jakarta"), query(-6.1, 106.48));
		assertEquals(ZoneId.of("Asia/Manila"), query(14.3512, 120.5804));
		assertEquals(ZoneId.of("Australia/Eucla"), query(-31.43, 128.52));
		assertEquals(ZoneId.of("Asia/Tokyo"), query(35.3916, 139.4441));
		assertEquals(ZoneId.of("Australia/Darwin"), query(-12.28, 130.5));
		assertEquals(ZoneId.of("Australia/Adelaide"), query(-34.55, 138.35));
		assertEquals(ZoneId.of("Australia/Brisbane"), query(-27.28, 153.02));
		assertEquals(ZoneId.of("Australia/Sydney"), query(-33.52, 151.13));
		assertEquals(ZoneId.of("Australia/Lord_Howe"), query(-31.33, 159.05));
		assertEquals(ZoneId.of("Pacific/Auckland"), query(-36.52, 174.46));
		assertEquals(ZoneId.of("Pacific/Chatham"), query(-43.57, -176.33));
		assertEquals(ZoneId.of("Pacific/Tongatapu"), query(-21.08, -175.12));
		assertEquals(ZoneId.of("Pacific/Kiritimati"), query(1.52, -157.2));
	}

	@Test
	public void multiPolygons() {
		assertEquals(ZoneId.of("Europe/London"), query(51.51, -0.03)); // London
		assertEquals(ZoneId.of("Europe/London"), query(54.23, -4.52)); // Isle of Man
		assertEquals(ZoneId.of("Europe/Lisbon"), query(38.74, -9.14)); // Lisbon
		assertEquals(ZoneId.of("Europe/Lisbon"), query(62.01, -6.80)); // Faroe Islands
		assertEquals(ZoneId.of("Europe/Lisbon"), query(28.31, -16.60)); // Tenerife
		assertEquals(ZoneId.of("Africa/Abidjan"), query(5.25, -3.97)); // Abidjan
		assertEquals(ZoneId.of("Africa/Abidjan"), query(64.21, -21.86)); // Reykjavík
	}

	@Test
	public void boundaries() {
		assertEquals(ZoneId.of("America/New_York"), query(40.00, -87.50));
		assertEquals(ZoneId.of("America/New_York"), query(40.00, -87.51));
		assertEquals(ZoneId.of("America/New_York"), query(40.00, -87.52));
		assertEquals(ZoneId.of("America/New_York"), query(40.00, -87.53));
		assertEquals(ZoneId.of("America/New_York"), query(40.00, -87.531));
		assertEquals(ZoneId.of("America/New_York"), query(40.00, -87.532));
		assertEquals(ZoneId.of("America/Chicago"), query(40.00, -87.533));
		assertEquals(ZoneId.of("America/Chicago"), query(40.00, -87.534));
		assertEquals(ZoneId.of("America/Chicago"), query(40.00, -87.54));
		assertEquals(ZoneId.of("America/Chicago"), query(40.00, -87.55));
		assertEquals(ZoneId.of("America/Chicago"), query(40.00, -87.56));
		assertEquals(ZoneId.of("America/Chicago"), query(40.00, -87.57));
		assertEquals(ZoneId.of("America/Chicago"), query(40.00, -87.58));
		assertEquals(ZoneId.of("America/Chicago"), query(40.00, -87.59));
		assertEquals(ZoneId.of("America/Chicago"), query(40.00, -87.60));

	}

	@Test
	public void multipleZones() throws IOException {
		// Sokhumi, 02 GE (43.00697, 40.9893, -1) Europe/Moscow
		assertEquals(Set.of(ZoneId.of("Europe/Moscow"), ZoneId.of("Asia/Dubai")), queryAll(43.00697, 40.9893));
		// Modiin Ilit, 06 IL (31.93221, 35.04416, 280) Asia/Jerusalem
		assertEquals(Set.of(ZoneId.of("Asia/Gaza"), ZoneId.of("Asia/Jerusalem")), queryAll(31.93221, 35.04416));
		// Ariel, WE IL (32.1065, 35.18449, 560) Asia/Jerusalem
		assertEquals(Set.of(ZoneId.of("Asia/Gaza"), ZoneId.of("Asia/Jerusalem")), queryAll(32.1065, 35.18449));
	}

	@Test
	public void availableZones() {
		Set<String> engineZoneIds = engine.getKnownZoneIds().stream().map(ZoneId::getId).collect(Collectors.toSet());
		assertTrue(ZoneId.getAvailableZoneIds().containsAll(engineZoneIds));
	}

	@Test
	public void zoneNowFile() throws Exception {
		int cnt = 0;
		int err_cnt = 0;
		ArrayList<String> lines = new ArrayList<>();
		for (String line : Files.readAllLines(Paths.get("src/test/resources", "zonenow.tab"))) {
			if (line.startsWith("#"))
				continue;
			// XX -1416-17042 Pacific/Pago_Pago Midway; Samoa ("SST")
			// ±DDMM±DDDMM or ±DDMMSS±DDDMMSS
			cnt++;
			String[] fields = line.split("\\t");
			String coord = fields[1];
			int i = Math.max(coord.lastIndexOf("+"), coord.lastIndexOf("-"));
			String lat_str = coord.substring(0, i);
			String lon_str = coord.substring(i);
			assertEquals(coord, lat_str + lon_str);
			double lat = Double.parseDouble(lat_str.substring(0, 3) + "." + lat_str.substring(3));
			double lon = Double.parseDouble(lon_str.substring(0, 4) + "." + lon_str.substring(4));
			ZoneId tz = ZoneId.of(fields[2]);
			ZoneId res = query(lat, lon);
			if (!tz.equals(res)) {
				if (trace)
					log.error("\n" + line + "\n  \t" + lat + " " + lon + "\nExpect:\t" + tz + "\nActual:\t" + res
							+ (engine.queryAll(lat, lon).size() != 1
									? "\nMulti:\t" + engine.queryAll(lat, lon).toString()
									: ""));
				err_cnt++;
			} else {
				lines.add("assertEquals(ZoneId.of(\"" + tz + "\"), query(" + lat + "," + lon + "));");
			}
		}
		log.info("Errors " + err_cnt + " of " + cnt);
		assertEquals(30, err_cnt);
		Files.write(Paths.get("target", "x.txt"), lines);
	}

}
