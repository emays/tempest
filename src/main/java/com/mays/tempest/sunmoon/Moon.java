package com.mays.tempest.sunmoon;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.shredzone.commons.suncalc.MoonIllumination;
import org.shredzone.commons.suncalc.MoonPosition;
import org.shredzone.commons.suncalc.MoonTimes;

public class Moon {

	private ZonedDateTime rise;

	private ZonedDateTime set;

	private MoonTimes moonTimes;

	private MoonIllumination moonIllumination;

	private MoonPosition moonPosition;

	private Moon(ZonedDateTime rise, ZonedDateTime set, MoonTimes moonTimes, MoonIllumination moonIllumination,
			MoonPosition moonPosition) {
		super();
		this.rise = rise;
		this.set = set;
		this.moonTimes = moonTimes;
		this.moonIllumination = moonIllumination;
		this.moonPosition = moonPosition;
	}

	// https://shredzone.org/maven/commons-suncalc/index.html

	// https://github.com/shred/commons-suncalc

	public static Moon get(LocalDate date, double lat, double lon, ZoneId tz) {
		return Moon.get(date.getYear(), date.getMonthValue(), date.getDayOfMonth(), lat, lon, tz);
	}

	public static Moon get(int year, int month, int day, double lat, double lon, ZoneId tz) {
		MoonTimes times = MoonTimes.compute().on(year, month, day).timezone(tz).at(lat, lon).execute();
		LocalDate date = LocalDate.of(year, month, day);
		ZonedDateTime rise = times.getRise();
		if (rise != null && !date.equals(rise.toLocalDate()))
			rise = null;
		ZonedDateTime set = times.getSet();
		if (set != null && !date.equals(set.toLocalDate()))
			set = null;
		ZonedDateTime time = Stream.of(rise, set).filter(java.util.Objects::nonNull).findFirst().get();
		MoonIllumination illumination = MoonIllumination.compute().on(time).execute();
		MoonPosition position = MoonPosition.compute().on(time).at(lat, lon).execute();
		return new Moon(rise, set, times, illumination, position);
	}

	public ZonedDateTime getRise() {
		return rise;
	}

	public ZonedDateTime getSet() {
		return set;
	}

	public boolean isAlwaysUp() {
		return moonTimes.isAlwaysUp();
	}

	public boolean isAlwaysDown() {
		return moonTimes.isAlwaysDown();
	}

	public String toString() {
		return "Moon: Rise " + rise + " Set " + set;
	}

	public double getFraction() {
		return moonIllumination.getFraction();
	}

	public double getPhase() {
		return moonIllumination.getPhase();
	}

	public String getPhaseName() {
		String name = moonIllumination.getClosestPhase().toString();
		return Arrays.stream(name.split("_")).map(str -> str.charAt(0) + str.substring(1).toLowerCase())
				.collect(Collectors.joining(" "));
	}

	public double getAngle() {
		return moonIllumination.getAngle();
	}

	public double getAltitude() {
		return moonPosition.getAltitude();
	}

	public double getAzimuth() {
		return moonPosition.getAzimuth();
	}

	public double getDistance() {
		return moonPosition.getDistance();
	}

}
