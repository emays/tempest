package com.mays.tempest.sunmoon;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.shredzone.commons.suncalc.MoonPosition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MoonApsidesTest {

	private static final Logger logger = LoggerFactory.getLogger(MoonApsidesTest.class);

	// https://en.wikipedia.org/wiki/Apsis

	private static final boolean trace = true;

	@Test
	public void getDistance() {
		final int year = 2024;
		LocalDateTime time = LocalDateTime.of(2024, 1, 1, 0, 0, 0);
		LocalDateTime time_min = null;
		Double distance_min = null;
		LocalDateTime time_max = null;
		Double distance_max = null;
		while (time.getYear() == year) {
			time = time.plusHours(1);
			MoonPosition position = MoonPosition.compute().on(time).at(0, 0).utc().execute();
			if (trace)
				logger.info(time + " " + position.getDistance());
			if (distance_min == null) {
				time_min = time;
				distance_min = position.getDistance();
				time_max = time;
				distance_max = position.getDistance();
				continue;
			}
			if (position.getDistance() < distance_min) {
				time_min = time;
				distance_min = position.getDistance();
			}
			if (position.getDistance() > distance_max) {
				time_max = time;
				distance_max = position.getDistance();
			}
			if (position.getDistance() > distance_min && position.getDistance() < distance_max)
				break;
		}
		logger.info("Min: " + time_min + " " + distance_min);
		logger.info("Max: " + time_max + " " + distance_max);

	}

}
