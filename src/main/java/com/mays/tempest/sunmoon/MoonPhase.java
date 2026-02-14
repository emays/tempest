package com.mays.tempest.sunmoon;

import java.time.ZonedDateTime;

import org.shredzone.commons.suncalc.MoonPhase.Phase;

public class MoonPhase {

	private org.shredzone.commons.suncalc.MoonPhase moonPhase;

	private Phase phase;

	public ZonedDateTime getTime() {
		return moonPhase.getTime();
	}

	public double getDistance() {
		return moonPhase.getDistance();
	}

	public String toString() {
		return moonPhase.toString();
	}

	@SuppressWarnings("exports")
	public Phase getPhase() {
		return phase;
	}

	private MoonPhase(org.shredzone.commons.suncalc.MoonPhase moonPhase, Phase phase) {
		super();
		this.moonPhase = moonPhase;
		this.phase = phase;
	}

	@SuppressWarnings("exports")
	public static MoonPhase get(ZonedDateTime time, Phase phase) {
		org.shredzone.commons.suncalc.MoonPhase mp = org.shredzone.commons.suncalc.MoonPhase.compute().on(time)
				.phase(phase).execute();
		return new MoonPhase(mp, phase);
	}

}
