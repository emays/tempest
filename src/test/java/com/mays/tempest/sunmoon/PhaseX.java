package com.mays.tempest.sunmoon;

import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.shredzone.commons.suncalc.MoonPhase;
import org.shredzone.commons.suncalc.MoonPhase.Phase;

public class PhaseX {

	private ZonedDateTime time;

	private Phase phase;

	ZonedDateTime getTime() {
		return time;
	}

	Phase getPhase() {
		return phase;
	}

	PhaseX(PhaseXJson sj) {
		LocalTime lt = LocalTime.parse(sj.getTime());
		time = ZonedDateTime.of(sj.getYear(), sj.getMonth(), sj.getDay(), lt.getHour(), lt.getMinute(), 0, 0,
				ZoneId.of("Z"));
		phase = MoonPhase.Phase.valueOf(sj.getPhase().replace(" ", "_").toUpperCase());
		if (phase == null)
			throw new IllegalArgumentException(sj.getPhase());
	}

	@Override
	public String toString() {
		return phase + " @ " + time;
	}

}
