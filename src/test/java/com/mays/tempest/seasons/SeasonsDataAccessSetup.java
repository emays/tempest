package com.mays.tempest.seasons;

import java.time.Duration;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SeasonsDataAccessSetup {

	private static final Logger logger = LoggerFactory.getLogger(SeasonsDataAccessSetup.class);

	@Test
	public void fetchSeasons() throws Exception {
		for (int year = SeasonsDataAccess.START_YEAR; year <= SeasonsDataAccess.END_YEAR; year++) {
			if (year % 10 == 0)
				logger.info("Year: " + year);
			SeasonsDataAccess.writeSeasonsJsonString(year);
			Thread.sleep(Duration.ofSeconds(10));
		}
	}

}
