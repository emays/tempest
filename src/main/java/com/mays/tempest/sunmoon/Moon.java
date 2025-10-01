package com.mays.tempest.sunmoon;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.shredzone.commons.suncalc.MoonIllumination;
import org.shredzone.commons.suncalc.MoonPosition;
import org.shredzone.commons.suncalc.MoonTimes;

public class Moon {

	private MoonTimes moonTimes;

	private MoonIllumination moonIllumination;

	private MoonPosition moonPosition;

	private Moon(MoonTimes moonTimes, MoonIllumination moonIllumination, MoonPosition moonPosition) {
		super();
		this.moonTimes = moonTimes;
		this.moonIllumination = moonIllumination;
		this.moonPosition = moonPosition;
	}

	// https://shredzone.org/maven/commons-suncalc/index.html

	// https://github.com/shred/commons-suncalc

	public static Moon get(int year, int month, int day, Double lat, Double lon, ZoneId tz) {
		MoonTimes times = MoonTimes.compute().oneDay().on(year, month, day).timezone(tz).at(lat, lon).execute();
		ZonedDateTime time = Stream.of(times.getRise(), times.getSet()).filter(java.util.Objects::nonNull).findFirst()
				.get();
		MoonIllumination illumination = MoonIllumination.compute().on(time).execute();
		MoonPosition position = MoonPosition.compute().on(time).at(lat, lon).execute();
		return new Moon(times, illumination, position);
	}

	public ZonedDateTime getRise() {
		return moonTimes.getRise();
	}

	public ZonedDateTime getSet() {
		return moonTimes.getSet();
	}

	public boolean isAlwaysUp() {
		return moonTimes.isAlwaysUp();
	}

	public boolean isAlwaysDown() {
		return moonTimes.isAlwaysDown();
	}

	public String toString() {
		return moonTimes.toString();
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
