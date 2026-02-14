package com.mays.tempest.sunmoon;

import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.shredzone.commons.suncalc.MoonIllumination;
import org.shredzone.commons.suncalc.MoonPosition;

import com.mays.tempest.geo.Coordinate;

public class MoonRiseSet implements MoonBase {

	private ZonedDateTime rise;

	private ZonedDateTime noon;

	private ZonedDateTime set;

	private MoonPosition risePosition;

	private MoonPosition noonPosition;

	private MoonPosition setPosition;

	private MoonIllumination noonIllumination;

	@Override
	public ZonedDateTime getRise() {
		return rise;
	}

	public ZonedDateTime getNoon() {
		return noon;
	}

	@Override
	public ZonedDateTime getSet() {
		return set;
	}

	private MoonRiseSet(ZonedDateTime rise, ZonedDateTime noon, ZonedDateTime set, MoonPosition risePosition,
			MoonPosition noonPosition, MoonPosition setPosition, MoonIllumination noonIllumination) {
		this.rise = rise;
		this.noon = noon;
		this.set = set;
		this.risePosition = risePosition;
		this.noonPosition = noonPosition;
		this.setPosition = setPosition;
		this.noonIllumination = noonIllumination;
	}

	public Duration getMoonUpLength() {
		return Duration.between(getRise(), getSet());
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

	@Override
	public double getDistance() {
		return noonPosition.getDistance();
	}

	@Override
	public double getFraction() {
		return noonIllumination.getFraction();
	}

	@Override
	public double getPhase() {
		return noonIllumination.getPhase();
	}

	@Override
	public String getPhaseName() {
		return Moon.getPhaseName(noonIllumination);
	}

	@Override
	public double getAngle() {
		return noonIllumination.getAngle();
	}

	public static class MoonRiseSetMonth {

		ArrayList<MoonRiseSet> moons;

		public ArrayList<MoonRiseSet> getMoons() {
			return moons;
		}

		private MoonRiseSetMonth(ArrayList<MoonRiseSet> moons) {
			this.moons = moons;
		}

		public static MoonRiseSetMonth get(int year, int month, double latitude, double longitude, ZoneId timeZone) {
			Builder builder = new Builder(year, month, latitude, longitude, timeZone);
			ArrayList<MoonRiseSet> rs = builder.getRiseSet();
			return new MoonRiseSetMonth(rs);
		}

		public static MoonRiseSetMonth get(LocalDate date, Coordinate coord, ZoneId tz) {
			return MoonRiseSetMonth.get(date.getYear(), date.getMonthValue(), coord.getLatitude(), coord.getLongitude(),
					tz);
		}

		public static MoonRiseSetMonth get(ZonedDateTime time, Coordinate coord) {
			return get(time.toLocalDate(), coord, time.getZone());
		}

		public MoonRiseSet getMoon(LocalDate date) {
			return moons.stream().filter(rs -> rs.getRise().toLocalDate().equals(date)).findFirst().orElse(null);
		}

		public MoonRiseSet getClosestMoon(LocalDate date) {
			MoonRiseSet moon = getMoon(date);
			if (moon != null)
				return moon;
			moon = getMoon(date.minusDays(1));
			if (moon != null)
				return moon;
			moon = getMoon(date.plusDays(1));
			if (moon != null)
				return moon;
			throw new UnsupportedOperationException("" + date);
		}

	}

	private static class Builder {

		private int year;

		private int month;

		private double latitude;

		private double longitude;

		private ZoneId timeZone;

		public Builder(int year, int month, double latitude, double longitude, ZoneId timeZone) {
			this.year = year;
			this.month = month;
			this.latitude = latitude;
			this.longitude = longitude;
			this.timeZone = timeZone;
		}

		private ArrayList<MoonEvent> getEvents(LocalDate date) {
			ArrayList<MoonEvent> events = new ArrayList<>();
			Moon moon = Moon.get(date, latitude, longitude, timeZone);
			if (moon.getRise() != null)
				events.add(MoonEvent.riseEvent(moon));
			if (moon.getSet() != null)
				events.add(MoonEvent.setEvent(moon));
			return events;
		}

		private ArrayList<MoonEvent> getEvents() {
			ArrayList<MoonEvent> events = new ArrayList<>();
			LocalDate start = LocalDate.of(year, month, 1);
			LocalDate end = LocalDate.of(year, month, start.lengthOfMonth());
			for (LocalDate date = start; date.compareTo(end) <= 0; date = date.plusDays(1)) {
				if (date.equals(start))
					events.addAll(getEvents(date.minusDays(1)));
				events.addAll(getEvents(date));
				if (date.equals(end))
					events.addAll(getEvents(date.plusDays(1)));
			}
			Collections.sort(events, Comparator.comparing(MoonEvent::getTime));
			if (events.getFirst().getType() == MoonEvent.Type.Set)
				events.removeFirst();
			if (events.getLast().getType() == MoonEvent.Type.Rise)
				events.removeLast();
			if (events.get(0).getTime().toLocalDate().isBefore(start)
					&& events.get(1).getTime().toLocalDate().isBefore(start)) {
				events.removeFirst();
				events.removeFirst();
			}
			if (events.get(events.size() - 1).getTime().toLocalDate().isAfter(end)
					&& events.get(events.size() - 2).getTime().toLocalDate().isAfter(end)) {
				events.removeLast();
				events.removeLast();
			}
			return events;
		}

		public ArrayList<MoonRiseSet> getRiseSet() {
			ArrayList<MoonRiseSet> rise_set_events = new ArrayList<>();
			ArrayList<MoonEvent> events = getEvents();
			if (events.size() % 2 != 0)
				throw new IllegalStateException(events.size() + " not even");
			for (int i = 0; i < events.size(); i = i + 2) {
				MoonEvent rise = events.get(i);
				MoonEvent set = events.get(i + 1);
				if (rise.getType() != MoonEvent.Type.Rise || set.getType() != MoonEvent.Type.Set)
					throw new IllegalStateException("Expect rise and set: " + rise.getType() + " " + set.getType());
				ZonedDateTime noon = getNoon(rise.getTime(), set.getTime());
				MoonPosition rise_position = MoonPosition.compute().on(rise.getTime()).at(latitude, longitude)
						.execute();
				MoonPosition noon_position = MoonPosition.compute().on(noon).at(latitude, longitude).execute();
				MoonPosition set_position = MoonPosition.compute().on(set.getTime()).at(latitude, longitude).execute();
				MoonIllumination illumination = MoonIllumination.compute().on(noon).execute();
				rise_set_events.add(new MoonRiseSet(rise.getTime(), noon, set.getTime(), rise_position, noon_position,
						set_position, illumination));
			}
			return rise_set_events;
		}

		private long getAzimuth(ZonedDateTime time) {
			MoonPosition position = MoonPosition.compute().on(time).at(latitude, longitude).execute();
			return Math.round(position.getAzimuth());
		}

		private ZonedDateTime getNoon(ZonedDateTime rise, ZonedDateTime set) {
			long dur = rise.until(set, ChronoUnit.MINUTES);
			ZonedDateTime mid = rise.plusMinutes(dur / 2);
			for (int offset = 0; offset < dur / 2; offset++) {
				{
					ZonedDateTime offset_noon = mid.plusMinutes(offset);
					if (getAzimuth(offset_noon) == 180)
						return offset_noon;

				}
				{
					ZonedDateTime offset_noon = mid.minusMinutes(offset);
					if (getAzimuth(offset_noon) == 180)
						return offset_noon;
				}
			}
			return null;
		}

	}
}
