package com.mays.tempest.sunmoon;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.shredzone.commons.suncalc.MoonPosition;
import org.shredzone.commons.suncalc.MoonTimes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mays.tempest.LocationInfo;
import com.mays.tempest.sunmoon.MoonRiseSet.MoonRiseSetMonth;

public class MoonRiseSetTest {

	private static final Logger logger = LoggerFactory.getLogger(MoonRiseSetTest.class);

	@Test
	public void riseSet() {
		for (int year = 1956; year < 2056; year++) {
			for (int month = 1; month <= 12; month++) {
				MoonRiseSetMonth mrsb = MoonRiseSetMonth.get(year, month, LocationInfo.PROVINCETOWN.getLatitude(),
						LocationInfo.PROVINCETOWN.getLongitude(), LocationInfo.PROVINCETOWN.getTimeZone());
				ArrayList<MoonRiseSet> events = mrsb.getMoons();
				assertTrue(events.size() >= 27);
				assertTrue(events.size() <= 31);
				for (MoonRiseSet rs : mrsb.getMoons()) {
					assertTrue(rs.getMoonUpLength().toHours() > 7);
					assertTrue(rs.getMoonUpLength().toHours() < 17);
				}
			}
		}
	}

	@Test
	public void noon() {
		for (int year = 1956; year < 2056; year++) {
			for (int month = 1; month <= 12; month++) {
				MoonRiseSetMonth mrsb = MoonRiseSetMonth.get(year, month, LocationInfo.PROVINCETOWN.getLatitude(),
						LocationInfo.PROVINCETOWN.getLongitude(), LocationInfo.PROVINCETOWN.getTimeZone());
				for (MoonRiseSet rs : mrsb.getMoons()) {
					ZonedDateTime rise = rs.getRise();
					ZonedDateTime set = rs.getSet();
					ZonedDateTime mid = rise.plusMinutes(rise.until(set, ChronoUnit.MINUTES) / 2);
					long diff = mid.until(rs.getNoon(), ChronoUnit.MINUTES);
					assertTrue(Math.abs(diff) < 9);
					MoonPosition position = MoonPosition.compute().on(rs.getNoon())
							.at(LocationInfo.PROVINCETOWN.getLatitude(), LocationInfo.PROVINCETOWN.getLongitude()).execute();
					long az = Math.round(position.getAzimuth());
					assertEquals(180, az);
				}
			}
		}
	}

//	@Test
	public void dst() {
		LocalDate start = LocalDate.of(1900, 1, 1);
		LocalDate end = LocalDate.of(2100, 12, 30);
		for (LocalDate date = start; date.compareTo(end) <= 0; date = date.plusDays(1)) {
			MoonTimes times = MoonTimes.compute().oneDay().on(date).timezone(ZoneId.of("America/New_York")).at(42, -70)
					.execute();
			if (times.getRise() != null && !times.getRise().toLocalDate().equals(date))
				logger.error("Rise: " + date + " " + date.getDayOfWeek() + " @ " + times.getRise());
			if (times.getSet() != null && !times.getSet().toLocalDate().equals(date))
				logger.error("Set : " + date + " " + date.getDayOfWeek() + " @ " + times.getSet());
		}
	}

//	@Test
//	public void events() {
//		boolean trace = false;
//		for (int year = 1956; year < 2056; year++) {
//			for (int month = 1; month <= 12; month++) {
//				MoonRiseSet.Builder mrsb = new MoonRiseSet.Builder(year, month, MyLocation.LATITUDE,
//						MyLocation.LONGITUDE, MyLocation.TZ);
//				ArrayList<MoonEvent> events = mrsb.getEvents();
//				assertEquals(MoonEvent.Type.Rise, events.getFirst().getType());
//				assertEquals(MoonEvent.Type.Set, events.getLast().getType());
//				assertEquals(0, events.size() % 2);
//				for (int i = 0; i < events.size() - 1; i++) {
//					long dur = events.get(i).getTime().until(events.get(i + 1).getTime(), ChronoUnit.HOURS);
//					assertTrue(dur > 7);
//					assertTrue(dur < 17);
//					if (trace && (dur < 9 || dur > 15))
//						logger.info(dur + " " + events.get(i) + " " + events.get(i + 1));
//				}
//				assertTrue(events.size() >= 54);
//				assertTrue(events.size() <= 62);
//				int year_ = year;
//				int month_ = month;
//				assertTrue(events.stream()
//						.filter(ev -> ev.getTime().getYear() != year_ && ev.getTime().getMonthValue() != month_)
//						.count() <= 2);
//				assertEquals(year, events.get(1).getTime().getYear());
//				assertEquals(month, events.get(1).getTime().getMonthValue());
//				assertEquals(year, events.get(events.size() - 2).getTime().getYear());
//				assertEquals(month, events.get(events.size() - 2).getTime().getMonthValue());
//				if (trace && events.size() == 62) {
//					logger.info("" + events.get(0));
//					logger.info("" + events.get(1));
//					logger.info("" + events.get(60));
//					logger.info("" + events.get(61));
//					logger.info("");
//				}
//			}
//		}
//	}

}
