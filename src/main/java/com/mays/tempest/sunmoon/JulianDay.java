package com.mays.tempest.sunmoon;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class JulianDay {

	// https://aa.usno.navy.mil/data/JulianDate

//  | ISO date          |  Julian Day Number | Astronomical Julian Day |
//  | 1970-01-01T00:00  |         2,440,588  |         2,440,587.5     |
//  | 1970-01-01T06:00  |         2,440,588  |         2,440,587.75    |
//  | 1970-01-01T12:00  |         2,440,588  |         2,440,588.0     |
//  | 1970-01-01T18:00  |         2,440,588  |         2,440,588.25    |
//  | 1970-01-02T00:00  |         2,440,589  |         2,440,588.5     |
//  | 1970-01-02T06:00  |         2,440,589  |         2,440,588.75    |
//  | 1970-01-02T12:00  |         2,440,589  |         2,440,589.0     |

	public static final double EPOCH_JULIAN_DAY = 2440_587.5;

	private static final int seconds_in_day = 60 * 60 * 24;

	// The day following 1582 October 4 (Julian calendar) is 1582 October 15
	// (Gregorian calendar).

	public static final double GREGORIAN_JULIAN_DAY = 2299_160.5;

	public static final LocalDateTime GREGORIAN_DAY = LocalDateTime.of(1582, 10, 15, 0, 0, 0);

	public static LocalDateTime toLocalDateTime(double jd) {
		if (jd < GREGORIAN_JULIAN_DAY) {
			// This doesn't work for Julian days that are not valid Gregorian dates
			// e.g. 1500-02-29
			// 2268991.5 should produce 1500-02-29T00:00:00 but instead produces 1500-03-01
			//
			// https://aa.usno.navy.mil/faq/leap_years
			double jd0 = jd;
			double jdl = toJulianDay(LocalDateTime.of(1500, 3, 1, 0, 0, 0)) - 1;
			if (jd <= jdl) {
				double y = (jdl - jd) / 365.25;
				jd = jd + Math.floor(y / 100) + 1;
			}
			double jdl2 = toJulianDay(LocalDateTime.of(1200, 3, 1, 0, 0, 0)) - 1;
			if (jd0 <= jdl2) {
				double y2 = (jdl2 - jd0) / 365.25;
				jd = jd - Math.floor(y2 / 400) - 1;
			}
			jd = jd - 10;
		}
		double seconds_from_epoch = (jd - EPOCH_JULIAN_DAY) * (double) seconds_in_day;
		LocalDateTime time = Instant.ofEpochSecond(0).plusSeconds(Math.round(seconds_from_epoch))
				.atOffset(ZoneOffset.UTC).toLocalDateTime();
		return time;
	}

	public static double toJulianDay(LocalDateTime dt) {
		if (dt.isBefore(GREGORIAN_DAY)) {
			int year = dt.getYear();
			int month = dt.getMonthValue();
			double day = dt.getDayOfMonth() + dt.toLocalTime().toSecondOfDay() / (double) seconds_in_day;
			if (month <= 2) {
				year = year - 1;
				month = month + 12;
			}
			double jd = Math.floor(365.25 * (year + 4716)) + Math.floor(30.6001 * (month + 1)) + day + 0 - 1524.5;
			return jd;
		}
		double days_from_epoch = dt.toInstant(ZoneOffset.UTC).getEpochSecond() / (double) seconds_in_day;
		return EPOCH_JULIAN_DAY + days_from_epoch;
	}

}
