package com.mays.tempest.sunmoon;

import java.nio.file.Files;
import java.nio.file.Paths;
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

}
