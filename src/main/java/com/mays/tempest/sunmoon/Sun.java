package com.mays.tempest.sunmoon;

import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.shredzone.commons.suncalc.SunPosition;
import org.shredzone.commons.suncalc.SunTimes;
import org.shredzone.commons.suncalc.SunTimes.Parameters;
import org.shredzone.commons.suncalc.SunTimes.Twilight;

import com.mays.tempest.geo.Coordinate;

public class Sun {

	private SunTimes sunTimes;

	private SunTimes civilTwilight;

	private SunTimes nauticalTwilight;

	private SunTimes astronomicalTwilight;

	private SunPosition risePosition;

	private SunPosition setPosition;

	private SunPosition noonPosition;

	private double latitude;

	private double longtitude;

	// https://shredzone.org/maven/commons-suncalc/index.html

	// https://github.com/shred/commons-suncalc

	public static Sun get(int year, int month, int day, double lat, double lon, ZoneId tz) {
		Parameters params = SunTimes.compute().on(year, month, day).timezone(tz).at(lat, lon);
		Sun sun = new Sun(params.execute(), params.twilight(Twilight.CIVIL).execute(),
				params.twilight(Twilight.NAUTICAL).execute(), params.twilight(Twilight.ASTRONOMICAL).execute(), lat,
				lon);
		sun.risePosition = sun.on(sun.getRise());
		sun.setPosition = sun.on(sun.getSet());
		sun.noonPosition = sun.on(sun.getNoon());
		return sun;
	}

	public static Sun get(LocalDate date, Coordinate coord, ZoneId tz) {
		return Sun.get(date.getYear(), date.getMonthValue(), date.getDayOfMonth(), coord.getLatitude(),
				coord.getLongitude(), tz);
	}

	public static Sun get(ZonedDateTime time, Coordinate coord) {
		return Sun.get(time.toLocalDate(), coord, time.getZone());
	}

	private Sun(SunTimes sunTimes, SunTimes civilTwilight, SunTimes nauticalTwilight, SunTimes astronomicalTwilight,
			double latitude, double longtitude) {
		super();
		this.sunTimes = sunTimes;
		this.civilTwilight = civilTwilight;
		this.nauticalTwilight = nauticalTwilight;
		this.astronomicalTwilight = astronomicalTwilight;
		this.latitude = latitude;
		this.longtitude = longtitude;
	}

	private SunPosition on(ZonedDateTime time) {
		return SunPosition.compute().on(time).at(latitude, longtitude).execute();
	}

	public ZonedDateTime getRise() {
		return sunTimes.getRise();
	}

	public ZonedDateTime getSet() {
		return sunTimes.getSet();
	}

	public ZonedDateTime getNoon() {
		return sunTimes.getNoon();
	}

	public ZonedDateTime getNadir() {
		return sunTimes.getNadir();
	}

	public boolean isAlwaysUp() {
		return sunTimes.isAlwaysUp();
	}

	public boolean isAlwaysDown() {
		return sunTimes.isAlwaysDown();
	}

	public Duration getDaylength() {
		return Duration.between(getRise(), getSet());
	}

	@Deprecated
	public String getDaylengthFormat() {
		Duration length = getDaylength();
		return getDurationFormat(length);
	}

	public static String getDurationFormat(Duration length) {
		return String.format("%d:%02d", length.toHours(), length.toMinutesPart());
	}

	public static String getDurationFormatHrMin(Duration length) {
		return String.format("%d hr %d min", length.toHours(), length.toMinutesPart());
	}

	public String toString() {
		return "[" //
				+ "rise=" + getRise().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME) + ", " //
				+ "set=" + getSet().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME) + ", " //
				+ "noon=" + getNoon().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME) + ", " //
				+ "daylength=" + getDurationFormat(getDaylength()) //
				+ "]";
	}

	private ZonedDateTime get(SunTimes times, boolean am) {
		if (am)
			return times.getRise();
		return times.getSet();
	}

	public ZonedDateTime getCivilTwilight(boolean am) {
		return get(civilTwilight, am);
	}

	public Duration getCivilTwilightLength() {
		return Duration.between(getCivilTwilight(true), getCivilTwilight(false));
	}

	public ZonedDateTime getNauticalTwilight(boolean am) {
		return get(nauticalTwilight, am);
	}

	public Duration getNauticalTwilightLength() {
		return Duration.between(getNauticalTwilight(true), getNauticalTwilight(false));
	}

	public ZonedDateTime getAstronomicalTwilight(boolean am) {
		return get(astronomicalTwilight, am);
	}

	public Duration getAstronomicalTwilightLength() {
		return Duration.between(getAstronomicalTwilight(true), getAstronomicalTwilight(false));
	}

	public double getRiseAzimuth() {
		return risePosition.getAzimuth();
	}

	public double getSetAzimuth() {
		return setPosition.getAzimuth();
	}

	public double getNoonAltitude() {
		return noonPosition.getAltitude();
	}

	public double getNoonTrueAltitude() {
		return noonPosition.getTrueAltitude();
	}

	public double getDistance() {
		return noonPosition.getDistance();
	}

	public double getAzimuth(ZonedDateTime time) {
		return on(time).getAzimuth();
	}

	public double getAltitude(ZonedDateTime time) {
		return on(time).getAltitude();
	}

}
