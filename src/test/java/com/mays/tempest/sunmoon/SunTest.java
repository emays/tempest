package com.mays.tempest.sunmoon;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mays.tempest.LocationInfo;
import com.mays.tempest.TimeZoneUtil;
import com.mays.util.Util;

public class SunTest {

	private static final Logger logger = LoggerFactory.getLogger(SunTest.class);

	private static final boolean trace = false;

	@Test
	public void getRiseSet() {
		Util.repeat(365, day -> {
			LocalDate date = LocalDate.of(2021, 1, 1).plusDays(day);
			Sun sun = Sun.get(date, LocationInfo.PROVINCETOWN.getCoordinate(), LocationInfo.PROVINCETOWN.getTimeZone());
			if (trace) {
				logger.info(date.toString());
				logger.info("\tAM twilight: " + String.format("%tT %tT %tT", sun.getAstronomicalTwilight(true),
						sun.getNauticalTwilight(true), sun.getCivilTwilight(true)));
				logger.info("\tRise: " + String.format("%tT %.0f", sun.getRise(), sun.getRiseAzimuth()));
				logger.info("\tNoon: " + String.format("%tT %.0f", sun.getNoon(), sun.getNoonAltitude()));
				logger.info("\tSet:  " + String.format("%tT %.0f", sun.getSet(), sun.getSetAzimuth()));
				logger.info("\tPM twilight: " + String.format("%tT %tT %tT", sun.getCivilTwilight(false),
						sun.getNauticalTwilight(false), sun.getAstronomicalTwilight(false)));
			}
		});
	}

	@Test
	public void getEarlyLate() {
		long max = 0;
		LocalDate max_date = null;
		for (int day = 0; day < 365; day++) {
			LocalDate date = LocalDate.of(2021, 1, 1).plusDays(day);
			Sun sun = Sun.get(date, LocationInfo.PROVINCETOWN.getCoordinate(), LocationInfo.PROVINCETOWN.getTimeZone());
			long len = sun.getAstronomicalTwilight(false).toEpochSecond()
					- sun.getAstronomicalTwilight(true).toEpochSecond();
			if (len > max) {
				max = len;
				max_date = date;
			}
		}
		if (trace)
			logger.info("Max: " + max_date);
	}

	@Test
	public void daylightSavings() throws Exception {
		Sun sun_313 = Sun.get(LocalDate.of(2021, 3, 13), LocationInfo.PROVINCETOWN.getCoordinate(),
				LocationInfo.PROVINCETOWN.getTimeZone());
		Sun sun_314 = Sun.get(LocalDate.of(2021, 3, 14), LocationInfo.PROVINCETOWN.getCoordinate(),
				LocationInfo.PROVINCETOWN.getTimeZone());
//		logger.info(sun_313.toString());
//		logger.info(sun_314.toString());
		assertEquals(5, sun_313.getRise().getHour());
		assertEquals(6, sun_314.getRise().getHour());
//		logger.info(sun_313.getRise().plusDays(1).minusMinutes(1).minusSeconds(41).toString());
		assertEquals(sun_313.getRise().plusHours(24).minusMinutes(1).minusSeconds(41).toEpochSecond(),
				sun_314.getRise().toEpochSecond());
	}

	@Test
	public void spring() throws Exception {
		Sun sun = Sun.get(LocalDate.of(2021, 3, 20), LocationInfo.PROVINCETOWN.getCoordinate(),
				LocationInfo.PROVINCETOWN.getTimeZone());
		if (trace) {
			logger.info(sun.toString());
			logger.info("" + sun.getNoonAltitude());
		}
		assertEquals(48.1, sun.getNoonAltitude(), 0.1);
	}

	@Test
	public void summer() throws Exception {
		Sun sun = Sun.get(LocalDate.of(2021, 6, 20), LocationInfo.PROVINCETOWN.getCoordinate(),
				LocationInfo.PROVINCETOWN.getTimeZone());
		if (trace) {
			logger.info(sun.toString());
			logger.info("" + sun.getNoonAltitude());
		}
		assertEquals(71.4, sun.getNoonAltitude(), 0.1);
	}

	@Test
	public void fall() throws Exception {
		Sun sun = Sun.get(LocalDate.of(2021, 9, 22), LocationInfo.PROVINCETOWN.getCoordinate(),
				LocationInfo.PROVINCETOWN.getTimeZone());
		if (trace) {
			logger.info(sun.toString());
			logger.info("" + sun.getNoonAltitude());
		}
		assertEquals(48.0, sun.getNoonAltitude(), 0.1);
	}

	@Test
	public void winter() throws Exception {
		Sun sun = Sun.get(LocalDate.of(2021, 12, 21), LocationInfo.PROVINCETOWN.getCoordinate(),
				LocationInfo.PROVINCETOWN.getTimeZone());
		if (trace) {
			logger.info(sun.toString());
			logger.info("" + sun.getNoonAltitude());
		}
		assertEquals(24.5, sun.getNoonAltitude(), 0.1);
	}

	@Test
	public void doRiseSetBos() throws Exception {
		doRiseSet("bos");
	}

	@Test
	public void doRiseSetSfo() throws Exception {
		doRiseSet("sfo");
	}

	private void doRiseSet(String loc) throws Exception {
		final int year = 2021;
		List<String> lines = Files
				.readAllLines(Paths.get("src/test/resources", "sunmoon", "sun " + loc + " " + year + ".txt"));
		String location = lines.get(1);
//		logger.info(location);
		String latitude = location.split(" ")[2];
		String longitude = location.split(" ")[4];
		ZoneId tz = TimeZoneUtil.getInstance().getTimeZone(Double.parseDouble(latitude), Double.parseDouble(longitude));
//		logger.info(latitude + " " + longitude);
		List<String> sunrise = lines.subList(7, 38);
//		logger.info(sunrise.get(0));
//		logger.info(sunrise.get(sunrise.size() - 1));
		List<String> sunset = lines.subList(46, 77);
//		logger.info(sunset.get(0));
//		logger.info(sunset.get(sunset.size() - 1));
		int days = 0;
		for (int month = 1; month <= 12; month++) {
			for (int day = 1; day <= 31; day++) {
				if (sunrise.get(day - 1).split("\\t")[month].isEmpty())
					continue;
				LocalTime exp_sunrise = LocalTime.parse(sunrise.get(day - 1).split("\\t")[month]);
				LocalTime exp_sunset = LocalTime.parse(sunset.get(day - 1).split("\\t")[month]);
				LocalDate date = LocalDate.of(year, month, day);
				Sun sun = Sun.get(year, month, day, Double.parseDouble(latitude), Double.parseDouble(longitude), tz);
				if (trace) {
					logger.info(date + "\t" + exp_sunrise + "\t" + exp_sunset);
					logger.info("          \t" + sun.getRise().toLocalTime() + "\t" + sun.getSet().toLocalTime());
				}
				days++;
				assertTrue(Math.abs(exp_sunrise.toSecondOfDay() - sun.getRise().toLocalTime().toSecondOfDay()) <= 60);
			}
		}
		assertEquals(365, days);
	}

}
