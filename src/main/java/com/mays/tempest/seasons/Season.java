package com.mays.tempest.seasons;

import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class Season {

	public enum Phenom {

		Perihelion, Aphelion, SpringEquinox, SummerSolstice, FallEquinox, WinterSolstice;

		public String getDisplayName() {
			return this.name().replaceFirst("([a-z])([A-Z])", "$1 $2");
		}

	}

	private ZonedDateTime time;

	private Phenom phenom;

	public ZonedDateTime getTime() {
		return time;
	}

	public Phenom getPhenom() {
		return phenom;
	}

	Season(SeasonJson sj) {
		LocalTime lt = LocalTime.parse(sj.getTime());
		time = ZonedDateTime.of(sj.getYear(), sj.getMonth(), sj.getDay(), lt.getHour(), lt.getMinute(), 0, 0,
				ZoneId.of("Z"));
		switch (sj.getPhenom()) {
		case "Perihelion" -> phenom = Phenom.Perihelion;
		case "Aphelion" -> phenom = Phenom.Aphelion;
		case "Equinox" -> {
			switch (sj.getMonth()) {
			case 3 -> phenom = Phenom.SpringEquinox;
			case 9 -> phenom = Phenom.FallEquinox;
			}
		}
		case "Solstice" -> {
			switch (sj.getMonth()) {
			case 6 -> phenom = Phenom.SummerSolstice;
			case 12 -> phenom = Phenom.WinterSolstice;
			}
		}
		}
		if (phenom == null)
			throw new IllegalArgumentException(sj.getPhenom());
	}

	@Override
	public String toString() {
		return phenom + " @ " + time;
	}

}
