package com.mays.tempest.sunmoon;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mays.tempest.seasons.SeasonsDataAccess;

public class PhaseXDataAccessTest {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(PhaseXDataAccessTest.class);

	@Test
	public void getPhases() throws Exception {
		for (int year = SeasonsDataAccess.START_YEAR; year <= SeasonsDataAccess.END_YEAR; year++) {
			String json = PhaseXDataAccess.getPhasesJsonString(year);
			assertNotNull(json);
			List<PhaseX> phases = PhaseXDataAccess.getPhases(year);
			assertTrue(phases.size() > 48);
			assertTrue(phases.size() < 51);
		}
	}

}
