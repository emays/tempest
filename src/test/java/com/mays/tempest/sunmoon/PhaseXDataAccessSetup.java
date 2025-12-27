package com.mays.tempest.sunmoon;

import java.time.Duration;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mays.tempest.seasons.SeasonsDataAccess;

public class PhaseXDataAccessSetup {

	private static final Logger logger = LoggerFactory.getLogger(PhaseXDataAccessSetup.class);

	@Test
	public void fetchPhases() throws Exception {
		for (int year = SeasonsDataAccess.START_YEAR; year <= SeasonsDataAccess.END_YEAR; year++) {
			if (year % 10 == 0)
				logger.info("Year: " + year);
			PhaseXDataAccess.writePhasesJsonString(year);
			Thread.sleep(Duration.ofSeconds(10));
		}
	}

}
