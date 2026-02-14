package com.mays.tempest;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalAdjusters;

public class TimeUtil {

	public static boolean equalYearMonth(LocalDate d1, LocalDate d2) {
		return d1.getYear() == d2.getYear() && d1.getMonthValue() == d2.getMonthValue();
	}

	public static String format(TemporalAccessor temporal, String pattern) {
		return DateTimeFormatter.ofPattern(pattern).format(temporal);
	}

	// TODO Should use
	// https://stackoverflow.com/questions/1060479/determine-whether-daylight-savings-time-dst-is-active-in-java-for-a-specified

	// https://www.timeanddate.com/time/change/usa

	// Daylight Saving Time (DST) in most of the United States

	// starts on the 2nd Sunday in March

	public static LocalDate getPossibleDstStart(int year) {
		return LocalDate.of(year, 3, 1).with(TemporalAdjusters.firstInMonth(DayOfWeek.SUNDAY)).plusWeeks(1);
	}

	public static boolean isDstStart(LocalDate date) {
		return date.equals(getPossibleDstStart(date.getYear()));
	}

	public static boolean isDstStart(LocalDate date, ZoneId tz) {
		return isDstStart(date)
				&& tz.getRules().isDaylightSavings(ZonedDateTime.of(date, LocalTime.NOON, tz).toInstant());
	}

	public static LocalDate getDstStart(int year, ZoneId tz) {
		LocalDate d = getPossibleDstStart(year);
		if (isDstStart(d, tz))
			return d;
		return null;
	}

	// ends on the 1st Sunday in November

	public static LocalDate getPossibleDstEnd(int year) {
		return LocalDate.of(year, 11, 1).with(TemporalAdjusters.firstInMonth(DayOfWeek.SUNDAY));
	}

	public static boolean isDstEnd(LocalDate date) {
		return date.equals(getPossibleDstEnd(date.getYear()));
	}

	public static boolean isDstEnd(LocalDate date, ZoneId tz) {
		return isDstEnd(date)
				&& tz.getRules().isDaylightSavings(ZonedDateTime.of(date.minusDays(1), LocalTime.NOON, tz).toInstant());
	}

	public static LocalDate getDstEnd(int year, ZoneId tz) {
		LocalDate d = getPossibleDstEnd(year);
		if (isDstEnd(d, tz))
			return d;
		return null;
	}

	private static String getDstText(String tag, LocalDate dst, ZonedDateTime time, int lower, int upper) {
		if (dst == null)
			return null;
		long days = time.toLocalDate().until(dst, ChronoUnit.DAYS);
		if (days < lower || days > upper)
			return null;
		if (days < -1)
			return tag + "ed " + -days + " days ago";
		if (days > 1)
			return tag + "s in " + days + " days";
		if (days == -1)
			return tag + "ed yesterday";
		if (days == 0)
			return tag + "s today";
		if (days == 1)
			return tag + "s tomorrow";
		throw new IllegalStateException(time + " " + lower + " " + upper);
	}

	public static String getDstStartText(ZonedDateTime time, int lower, int upper) {
		LocalDate dst = TimeUtil.getDstStart(time.getYear(), time.getZone());
		return getDstText("Start", dst, time, lower, upper);
	}

	public static String getDstEndText(ZonedDateTime time, int lower, int upper) {
		LocalDate dst = TimeUtil.getDstEnd(time.getYear(), time.getZone());
		return getDstText("End", dst, time, lower, upper);
	}

}
