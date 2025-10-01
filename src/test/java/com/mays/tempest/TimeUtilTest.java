package com.mays.tempest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TimeUtilTest {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(TimeUtilTest.class);

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
		assertEquals(1, genDays().filter(d -> TimeUtil.isDstStart(d, MyLocation.TZ)).count());
		assertEquals(1, genDays().filter(d -> TimeUtil.isDstEnd(d, MyLocation.TZ)).count());
		assertTrue(TimeUtil.isDstStart(start));
		assertTrue(TimeUtil.isDstStart(start, MyLocation.TZ));
		assertTrue(TimeUtil.isDstEnd(end));
		assertTrue(TimeUtil.isDstEnd(end, MyLocation.TZ));
		assertEquals(start, TimeUtil.getDstStart(start.getYear(), MyLocation.TZ));
		assertEquals(end, TimeUtil.getDstEnd(end.getYear(), MyLocation.TZ));
	}

	@Test
	public void noDst() {
		ZoneId tz = ZoneId.of("America/Phoenix");
		assertEquals(0, genDays().filter(d -> TimeUtil.isDstStart(d, tz)).count());
		assertEquals(0, genDays().filter(d -> TimeUtil.isDstEnd(d, tz)).count());
		assertNull(TimeUtil.getDstStart(start.getYear(), tz));
		assertNull(TimeUtil.getDstEnd(end.getYear(), tz));
	}

}
