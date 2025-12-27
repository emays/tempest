package com.mays.tempest.sunmoon;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.shredzone.commons.suncalc.MoonIllumination;
import org.shredzone.commons.suncalc.MoonPhase;
import org.shredzone.commons.suncalc.MoonPhase.Phase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mays.tempest.seasons.SeasonsDataAccess;

public class MoonPhaseTest {

	private static final Logger logger = LoggerFactory.getLogger(MoonPhaseTest.class);

	public static final boolean trace = false;

	@Test
	public void run() throws Exception {
		ArrayList<String> lines = new ArrayList<>();
		lines.add("BEGIN:VCALENDAR");
		lines.add("VERSION:2.0");
		lines.add("PRODID:-//Mays//Tempest 0.1//EN");
		ZonedDateTime date = ZonedDateTime.of(2021, 1, 1, 0, 0, 0, 0, ZoneId.of("America/New_York"));
		double phase = MoonIllumination.compute().on(date).execute().getPhase() + 180.0;
		if (phase >= 360.0)
			phase = phase - 360.0;
		if (trace) {
			logger.info(date.toString());
			logger.info(date.withZoneSameInstant(ZoneId.of("UTC")).toString());
			logger.info("Phase: " + phase);
		}
		Phase next_phase = null;
		for (Phase ph : MoonPhase.Phase.values()) {
			if (phase <= ph.getAngle()
					&& List.of(Phase.NEW_MOON, Phase.FIRST_QUARTER, Phase.FULL_MOON, Phase.LAST_QUARTER).contains(ph)) {
				next_phase = ph;
				break;
			}
		}
		if (trace)
			logger.info("Next phase: " + next_phase);
		MoonPhase moon_phase = MoonPhase.compute().on(date).phase(next_phase).execute();
		while (moon_phase.getTime().getYear() == date.getYear()) {
			if (trace)
				logger.info(moon_phase.getTime() + " " + next_phase + (moon_phase.isSuperMoon() ? " Supermoon" : ""));
			lines.add("BEGIN:VEVENT");
			String str = moon_phase.getTime().withZoneSameInstant(ZoneId.of("UTC"))
					.format(DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'"));
			lines.add("UID:" + str);
			lines.add("SUMMARY:" + next_phase);
			lines.add("DTSTART:" + str);
			lines.add("DTEND:" + str);
			lines.add("END:VEVENT");
			switch (next_phase) {
			case NEW_MOON:
				next_phase = Phase.FIRST_QUARTER;
				break;
			case FIRST_QUARTER:
				next_phase = Phase.FULL_MOON;
				break;
			case FULL_MOON:
				next_phase = Phase.LAST_QUARTER;
				break;
			case LAST_QUARTER:
				next_phase = Phase.NEW_MOON;
				break;
			case WANING_CRESCENT:
			case WANING_GIBBOUS:
			case WAXING_CRESCENT:
			case WAXING_GIBBOUS:
				break;
			}
			date = moon_phase.getTime();
			moon_phase = MoonPhase.compute().on(date).phase(next_phase).execute();
		}
		lines.add("END:VCALENDAR");
		Files.write(Paths.get("target", "moon test.ics"), lines);
	}

	// https://www.fourmilab.ch/earthview/pacalc.html

	private List<String> new_moons = List.of( //
			"Jan 29, 2025 12:37", //
			"Feb 28, 2025 0:47", //
			"Mar 29, 2025 11:00", //
			"Apr 27, 2025 19:33", //
			"May 27, 2025 3:04", //
			"Jun 25, 2025 10:34", //
			"Jul 24, 2025 19:12", //
			"Aug 23, 2025 6:07", //
			"Sep 21, 2025 19:55", //
			"Oct 21, 2025 12:26", //
			"Nov 20, 2025 6:48", //
			"Dec 20, 2025 1:44" //
	);

	private List<String> full_moons = List.of( //
			"Jan 13, 2025 22:28", //
			"Feb 12, 2025 13:54", //
			"Mar 14, 2025 6:56", //
			"Apr 13, 2025 0:24", //
			"May 12, 2025 16:58", //
			"Jun 11, 2025 7:46", //
			"Jul 10, 2025 20:39", //
			"Aug 9, 2025 7:57", //
			"Sep 7, 2025 18:11", //
			"Oct 7, 2025 3:49", //
			"Nov 5, 2025 13:20", //
			"Dec 4, 2025 23:15" //
	);

	// https://aa.usno.navy.mil/data/MoonPhases

	private List<String> new_moons2 = List.of( //
			"Jan 29, 2025 12:36", //
			"Feb 28, 2025 0:45", //
			"Mar 29, 2025 10:58", //
			"Apr 27, 2025 19:31", //
			"May 27, 2025 3:02", //
			"Jun 25, 2025 10:31", //
			"Jul 24, 2025 19:11", //
			"Aug 23, 2025 6:06", //
			"Sep 21, 2025 19:54", //
			"Oct 21, 2025 12:25", //
			"Nov 20, 2025 6:47", //
			"Dec 20, 2025 1:43" //
	);

	private List<String> full_moons2 = List.of( //
			"Jan 13, 2025 22:27", //
			"Feb 12, 2025 13:53", //
			"Mar 14, 2025 6:55", //
			"Apr 13, 2025 0:22", //
			"May 12, 2025 16:56", //
			"Jun 11, 2025 7:44", //
			"Jul 10, 2025 20:37", //
			"Aug 9, 2025 7:55", //
			"Sep 7, 2025 18:09", //
			"Oct 7, 2025 3:47", //
			"Nov 5, 2025 13:19", //
			"Dec 4, 2025 23:14" //
	);

	@Test
	public void phases2025() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("LLL d, yyyy H:mm");
		final int year = 2025;
		for (Phase phase : List.of(Phase.NEW_MOON, Phase.FULL_MOON)) {
			List<String> expects = switch (phase) {
			case FULL_MOON -> full_moons;
			case NEW_MOON -> new_moons;
			default -> throw new IllegalArgumentException("Unexpected value: " + phase);
			};
			List<String> expects2 = switch (phase) {
			case FULL_MOON -> full_moons2;
			case NEW_MOON -> new_moons2;
			default -> throw new IllegalArgumentException("Unexpected value: " + phase);
			};
			ZonedDateTime date = ZonedDateTime.of(year, 1, 1, 0, 0, 0, 0, ZoneId.of("Z"));
			int i = 0;
			while (true) {
				MoonPhase moon_phase = MoonPhase.compute().on(date).phase(phase).execute();
				if (moon_phase.getTime().getYear() != year)
					break;
				if (trace)
					logger.info(phase + " " + moon_phase.getTime());
				{
					LocalDateTime expect = LocalDateTime.parse(expects.get(i), dtf);
//					logger.info("" + expect);
					assertTrue(expect.minusMinutes(4).isBefore(moon_phase.getTime().toLocalDateTime()));
					assertTrue(expect.plusMinutes(4).isAfter(moon_phase.getTime().toLocalDateTime()));
				}
				{
					LocalDateTime expect = LocalDateTime.parse(expects2.get(i), dtf);
//						logger.info("" + expect);
					assertTrue(expect.minusMinutes(4).isBefore(moon_phase.getTime().toLocalDateTime()));
					assertTrue(expect.plusMinutes(6).isAfter(moon_phase.getTime().toLocalDateTime()));
				}
				date = moon_phase.getTime().plusDays(1);
				i++;
			}
		}
	}

	public static class MoonPhasePhase {

		private Phase phase;

		private ZonedDateTime time;

		MoonPhasePhase(Phase phase, ZonedDateTime time) {
			super();
			this.phase = phase;
			this.time = time;
		}

		Phase getPhase() {
			return phase;
		}

		ZonedDateTime getTime() {
			return time;
		}

	}

	public List<MoonPhasePhase> getPhases(final int year) {
		List<MoonPhasePhase> ret = new ArrayList<>();
		List<Phase> phases = List.of(Phase.NEW_MOON, Phase.FIRST_QUARTER, Phase.FULL_MOON, Phase.LAST_QUARTER);
		ZonedDateTime date = ZonedDateTime.of(year - 1, 12, 1, 0, 0, 0, 0, ZoneId.of("Z"));
		int i = 0;
		while (true) {
			Phase phase = phases.get(i);
			MoonPhase moon_phase = MoonPhase.compute().on(date).phase(phase).execute();
			if (moon_phase.getTime().getYear() > year)
				break;
			if (moon_phase.getTime().getYear() == year)
				ret.add(new MoonPhasePhase(phase, moon_phase.getTime()));
			date = moon_phase.getTime().plusDays(1);
			i = (i + 1) % phases.size();
		}
		return ret;
	}

	@Test
	public void phases() throws Exception {
		for (int year = SeasonsDataAccess.START_YEAR; year <= SeasonsDataAccess.END_YEAR; year++) {
			List<PhaseX> ex_phases = PhaseXDataAccess.getPhases(year);
			List<MoonPhasePhase> phases = getPhases(year);
			assertEquals(ex_phases.size(), phases.size());
			int i = 0;
			for (PhaseX ex_phase : ex_phases) {
				MoonPhasePhase phase = phases.get(i);
				assertEquals(ex_phase.getPhase(), phase.getPhase());
				assertTrue(ex_phase.getTime().minusMinutes(8).isBefore(phase.getTime()));
				assertTrue(ex_phase.getTime().plusMinutes(10).isAfter(phase.getTime()));
				i++;
			}
		}
	}

}
