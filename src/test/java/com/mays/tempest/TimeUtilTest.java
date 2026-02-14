package com.mays.tempest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TimeUtilTest {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(TimeUtilTest.class);

	private static final ZoneId TZ = ZoneId.of("America/New_York");

	private Stream<LocalDate> genDays() {
		LocalDate j1 = LocalDate.of(2024, 1, 1);
		return IntStream.range(0, j1.lengthOfYear()).mapToObj(d -> j1.plusDays(d));
	}

	private final LocalDate start = LocalDate.of(2024, 3, 10);
	private final LocalDate end = LocalDate.of(2024, 11, 3);

	@Test
	public void isDst() {
//		TimeZoneUtil.getInstance().getZoneIds().stream().map(ZoneId::getId).sorted().forEach(logger::info);
//		ZoneId.getAvailableZoneIds().stream().filter(x -> x.contains("America")).sorted().forEach(logger::info);
//		genDays().map(LocalDate::toString).forEach(logger::info);
		assertEquals(366, genDays().count());
		assertEquals(1, genDays().filter(d -> TimeUtil.isDstStart(d)).count());
		assertEquals(1, genDays().filter(d -> TimeUtil.isDstEnd(d)).count());
		assertEquals(1, genDays().filter(d -> TimeUtil.isDstStart(d, TZ)).count());
		assertEquals(1, genDays().filter(d -> TimeUtil.isDstEnd(d, TZ)).count());
		assertTrue(TimeUtil.isDstStart(start));
		assertTrue(TimeUtil.isDstStart(start, TZ));
		assertTrue(TimeUtil.isDstEnd(end));
		assertTrue(TimeUtil.isDstEnd(end, TZ));
		assertEquals(start, TimeUtil.getDstStart(start.getYear(), TZ));
		assertEquals(end, TimeUtil.getDstEnd(end.getYear(), TZ));
	}

	@Test
	public void noDst() {
		ZoneId tz = ZoneId.of("America/Phoenix");
		assertEquals(0, genDays().filter(d -> TimeUtil.isDstStart(d, tz)).count());
		assertEquals(0, genDays().filter(d -> TimeUtil.isDstEnd(d, tz)).count());
		assertNull(TimeUtil.getDstStart(start.getYear(), tz));
		assertNull(TimeUtil.getDstEnd(end.getYear(), tz));
	}

	@Test
	public void dstStartText() {
		for (int i = -50; i < 10; i++) {
			ZonedDateTime time = ZonedDateTime.of(start.plusDays(i).atStartOfDay(), TZ);
			String text = TimeUtil.getDstStartText(time, -7, 45);
			if (i < -45 || i > 7) {
				assertEquals(null, text, i + " " + text);
				continue;
			}
			if (i < -1)
				assertEquals("Starts in " + -i + " days", text, "" + i);
			if (i == -1)
				assertEquals("Starts tomorrow", text, "" + i);
			if (i == 0)
				assertEquals("Starts today", text, "" + i);
			if (i == 1)
				assertEquals("Started yesterday", text, "" + i);
			if (i > 1)
				assertEquals("Started " + i + " days ago", text, "" + i);

		}
	}

	@Test
	public void dstEndText() {
		for (int i = -50; i < 10; i++) {
			ZonedDateTime time = ZonedDateTime.of(end.plusDays(i).atStartOfDay(), TZ);
			String text = TimeUtil.getDstEndText(time, -7, 45);
			if (i < -45 || i > 7) {
				assertEquals(null, text, i + " " + text);
				continue;
			}
			if (i < -1)
				assertEquals("Ends in " + -i + " days", text, "" + i);
			if (i == -1)
				assertEquals("Ends tomorrow", text, "" + i);
			if (i == 0)
				assertEquals("Ends today", text, "" + i);
			if (i == 1)
				assertEquals("Ended yesterday", text, "" + i);
			if (i > 1)
				assertEquals("Ended " + i + " days ago", text, "" + i);

		}
	}

}
