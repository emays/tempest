package com.mays.tempest.sunmoon;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.JulianFields;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JulianDayTest {

	private static final Logger logger = LoggerFactory.getLogger(JulianDayTest.class);

	private static final boolean trace = false;

	private String toLdt(double jd) {
		return JulianDay.toLocalDateTime(jd).format(DateTimeFormatter.ISO_DATE_TIME);
	}

	// curl "https://aa.usno.navy.mil/api/juliandate?date=1500-3-1&time=12:00:00

	@Test
	public void toLocalDateTime() {
		assertEquals("1970-01-01T00:00:00", toLdt(JulianDay.EPOCH_JULIAN_DAY));
		assertEquals("1988-10-07T20:29:20", toLdt(2447_442.3537)); // Example 50.a
		assertEquals("1582-10-15T00:00:00", toLdt(JulianDay.GREGORIAN_JULIAN_DAY));
		assertEquals("1582-10-04T00:00:00", toLdt(JulianDay.GREGORIAN_JULIAN_DAY - 1));
		assertEquals("1582-10-04T12:00:00", toLdt(JulianDay.GREGORIAN_JULIAN_DAY - 0.5));
		assertEquals("1580-03-01T00:00:00", toLdt(2298_212.5));
		assertEquals("1580-02-29T00:00:00", toLdt(2298_211.5));
		assertEquals("1580-02-28T00:00:00", toLdt(2298_210.5));
		assertEquals("1500-03-01T00:00:00", toLdt(2268_992.5));
		// assertEquals("1500-02-29T00:00:00", toLdt(2268_991.5));
		assertEquals("1500-02-28T00:00:00", toLdt(2268_990.5));
		assertEquals("1400-03-01T00:00:00", toLdt(2232_467.5));
		assertEquals("1200-02-16T00:00:00", toLdt(2159_403.5));
		assertEquals("1200-02-29T00:00:00", toLdt(2159_416.5));
		assertEquals("0333-01-27T12:00:00", toLdt(1842_713.0)); // Example 7.b
	}

	private static boolean hourly = false;

	// Just test locally
//	@Test 
	public void toLocalDateTimeYears() {
		for (int year = 5000; year > -5000; year--) {
			for (int day = 0; day < 365; day++) {
				for (int hour = 0; hour < 24; hour++) {
					if (!hourly && hour > 0)
						continue;
					LocalDateTime date = LocalDateTime.of(year, 1, 1, 0, 0, 0).plusDays(day).plusHours(hour);
					String date_str = date.format(DateTimeFormatter.ISO_DATE_TIME);
					if (date.isBefore(JulianDay.GREGORIAN_DAY) && date.isAfter(JulianDay.GREGORIAN_DAY.minusDays(11))) {
						if (hour == 0)
							logger.info("Skipping " + date_str);
						continue;
					}
					double jd = JulianDay.toJulianDay(date);
					assertEquals(date_str, toLdt(jd), date_str);
				}
			}
		}
	}

	private double toJd(String ldt) {
		return JulianDay.toJulianDay(LocalDateTime.parse(ldt));
	}

	@Test
	public void toJulianDay() {
		assertEquals(JulianDay.EPOCH_JULIAN_DAY, toJd("1970-01-01T00:00:00"));
		assertEquals(2447_442.3537, toJd("1988-10-07T20:29:20"), 0.000_05); // Example 50.a
		{
			// Example 7.a, 1957 October 4.81, the time of launch of Sputnik 1
			// Wikipedia says: 4 October 1957 19:28:34 UTC
			// 1957 October 4.81 is 1957-10-04T19:26:24
			String date = LocalDateTime.of(1957, 10, 4, 0, 0).plusSeconds(Math.round(24 * 60 * 60 * 0.81))
					.format(DateTimeFormatter.ISO_DATE_TIME);
			if (trace)
				logger.info("Sputnik: " + date);
			assertEquals(2436_116.31, toJd(date), 0.005);
		}
		// USNO: A.D. 333 January 27 12:00:00.0 = JD 1842713.000000
		assertEquals(1842_713.0, toJd("0333-01-27T12:00:00"), 0.000_05); // Example 7.b
		// A.D. 1582 October 15 00:00:00.0 2299160.500000
		assertEquals(JulianDay.GREGORIAN_JULIAN_DAY, toJd("1582-10-15T00:00:00"));
		// TODO This is an invalid date: 1582-10-14T00:00:00
		// A.D. 1582 October 4 00:00:00.0 2299159.500000
		assertEquals(JulianDay.GREGORIAN_JULIAN_DAY - 1, toJd("1582-10-04T00:00:00"));
		assertEquals(JulianDay.GREGORIAN_JULIAN_DAY - 2, toJd("1582-10-03T00:00:00"));
		for (int i = 0; i < JulianDay.GREGORIAN_JULIAN_DAY; i++) {
			LocalDateTime date = LocalDateTime.of(1582, 10, 4, 0, 0, 0).minusDays(i);
			if (date.equals(LocalDateTime.of(1500, 2, 28, 0, 0, 0)))
				break;
			assertEquals(JulianDay.GREGORIAN_JULIAN_DAY - 1 - i, JulianDay.toJulianDay(date), date.toString());
		}
		assertEquals(2298_212.5, toJd("1580-03-01T00:00:00"));
		assertEquals(2298_211.5, toJd("1580-02-29T00:00:00"));
		assertEquals(2298_210.5, toJd("1580-02-28T00:00:00"));
	}

	private static record TestCase(int year, String month, double day, double jd) {

		private LocalDate date() {
			int m = LocalDate.parse("2000 " + month + " 1", DateTimeFormatter.ofPattern("yyyy LLL d")).getMonthValue();
			return LocalDate.of(year, m, (int) Math.floor(day));
		}

		private LocalDateTime dateTime() {
			return date().atStartOfDay().plusSeconds(Math.round((day - Math.floor(day)) * 24 * 60 * 60));
		}

	}

	private static final List<TestCase> tcs = List.of( //
			new TestCase(2000, "Jan", 1.5, 2451_545.0), //
			new TestCase(1999, "Jan", 1.0, 2451_179.5), //
			new TestCase(1987, "Jan", 27.0, 2446_822.5), //
			new TestCase(1987, "Jun", 19.5, 2446_966.0), //
			new TestCase(1988, "Jan", 27.0, 2447_187.5), //
			new TestCase(1988, "Jun", 19.5, 2447_332.0), //
			new TestCase(1900, "Jan", 1.0, 2415_020.5), //
			new TestCase(1600, "Jan", 1.0, 2305_447.5), //
			new TestCase(1600, "Dec", 31.0, 2305_812.5), //
			new TestCase(837, "Apr", 10.3, 2026_871.8), //
			new TestCase(-123, "Dec", 31.0, 1676_496.5), //
			new TestCase(-122, "Jan", 1.0, 1676_497.5), //
			new TestCase(-1000, "Jul", 12.5, 1356_001.0), //
			// Not a valid date in java.time
			// new tc(-1000, "Feb", 29.0, 1355_866.5), //
			new TestCase(-1001, "Aug", 17.9, 1355_671.4), //
			new TestCase(-4712, "Jan", 1.5, 0.0) //
	);

	// @Test
	public void parse() {
		for (TestCase tc : tcs) {
			logger.info(tc.date().toString());
			logger.info(tc.dateTime().toString());
		}
	}

	@Test
	public void toJulianDayTC() {
		for (TestCase tc : tcs) {
			if (trace)
				logger.info(tc.dateTime().toString());
			assertEquals(tc.jd(), JulianDay.toJulianDay(tc.dateTime()));
		}
	}

	@Test
	public void toLocalDateTimeTC() {
		for (TestCase tc : tcs) {
			if (trace)
				logger.info(tc.dateTime().toString());
			assertEquals(tc.dateTime(), JulianDay.toLocalDateTime(tc.jd()));
			if (tc.year() >= 1600)
				assertEquals(Math.round(tc.jd()), tc.dateTime().getLong(JulianFields.JULIAN_DAY));
		}
	}

}
