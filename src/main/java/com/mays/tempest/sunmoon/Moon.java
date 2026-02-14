package com.mays.tempest.sunmoon;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.stream.Collectors;

import org.shredzone.commons.suncalc.MoonIllumination;
import org.shredzone.commons.suncalc.MoonPhase.Phase;
import org.shredzone.commons.suncalc.MoonPosition;
import org.shredzone.commons.suncalc.MoonTimes;

import com.mays.tempest.geo.Coordinate;

public class Moon implements MoonBase {

	private ZonedDateTime rise;

	private ZonedDateTime set;

	private MoonTimes moonTimes;

	private MoonIllumination moonIllumination;

	private MoonPosition moonPosition;

	private Moon(ZonedDateTime rise, ZonedDateTime set, MoonTimes moonTimes, MoonIllumination moonIllumination,
			MoonPosition moonPosition) {
		this.rise = rise;
		this.set = set;
		this.moonTimes = moonTimes;
		this.moonIllumination = moonIllumination;
		this.moonPosition = moonPosition;
	}

	// https://shredzone.org/maven/commons-suncalc/index.html

	// https://github.com/shred/commons-suncalc

	public static Moon get(int year, int month, int day, double lat, double lon, ZoneId tz) {
		MoonTimes times = MoonTimes.compute().on(year, month, day).timezone(tz).at(lat, lon).execute();
		LocalDate date = LocalDate.of(year, month, day);
		ZonedDateTime rise = times.getRise();
		if (rise != null && !date.equals(rise.toLocalDate()))
			rise = null;
		ZonedDateTime set = times.getSet();
		if (set != null && !date.equals(set.toLocalDate()))
			set = null;
		ZonedDateTime time = ZonedDateTime.of(date, LocalTime.NOON, tz);
		MoonIllumination illumination = MoonIllumination.compute().on(time).execute();
		MoonPosition position = MoonPosition.compute().on(time).at(lat, lon).execute();
		return new Moon(rise, set, times, illumination, position);
	}

	public static Moon get(LocalDate date, double lat, double lon, ZoneId tz) {
		return Moon.get(date.getYear(), date.getMonthValue(), date.getDayOfMonth(), lat, lon, tz);
	}

	public static Moon get(LocalDate date, Coordinate coord, ZoneId tz) {
		return Moon.get(date.getYear(), date.getMonthValue(), date.getDayOfMonth(), coord.getLatitude(),
				coord.getLongitude(), tz);
	}

	public static Moon get(ZonedDateTime time, Coordinate coord) {
		return get(time.toLocalDate(), coord, time.getZone());
	}

	@Override
	public ZonedDateTime getRise() {
		return rise;
	}

	@Override
	public ZonedDateTime getSet() {
		return set;
	}

	public boolean isAlwaysUp() {
		return moonTimes.isAlwaysUp();
	}

	public boolean isAlwaysDown() {
		return moonTimes.isAlwaysDown();
	}

	@Override
	public String toString() {
		return "Moon: Rise " + rise + " Set " + set;
	}

	@Override
	public double getFraction() {
		return moonIllumination.getFraction();
	}

	@Override
	public double getPhase() {
		return moonIllumination.getPhase();
	}

	public static String getPhaseName(MoonIllumination moonIllumination) {
		return getPhaseName(moonIllumination.getClosestPhase());
	}

	public static String getPhaseName(Phase phase) {
		String name = phase.toString();
		return Arrays.stream(name.split("_")).map(str -> str.charAt(0) + str.substring(1).toLowerCase())
				.collect(Collectors.joining(" "));
	}

	@Override
	public String getPhaseName() {
		return Moon.getPhaseName(moonIllumination);
	}

	@Override
	public double getAngle() {
		return moonIllumination.getAngle();
	}

	public double getAltitude() {
		return moonPosition.getAltitude();
	}

	public double getAzimuth() {
		return moonPosition.getAzimuth();
	}

	@Override
	public double getDistance() {
		return moonPosition.getDistance();
	}

}
