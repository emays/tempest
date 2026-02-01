package com.mays.tempest.seasons;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SeasonsDataAccessTest {

	private static final Logger logger = LoggerFactory.getLogger(SeasonsDataAccessTest.class);

	private static final boolean trace = false;

	@BeforeAll
	public static void setTest() {
		SeasonsDataAccess.setTest();
	}

	@Test
	public void getSeasons() throws Exception {
		int min = Integer.MAX_VALUE;
		int max = Integer.MIN_VALUE;
		for (int year = SeasonsDataAccess.START_YEAR; year <= SeasonsDataAccess.END_YEAR; year++) {
			String json = SeasonsDataAccess.getSeasonsJsonString(year);
			min = Math.min(min, json.length());
			max = Math.max(max, json.length());
			assertEquals(827, json.length());
			List<SeasonJson> sj_l = SeasonsDataAccess.getSeasonsJsonFromJson(json);
			assertEquals(6, sj_l.size());
			List<Season> s_l = SeasonsDataAccess.getSeasonsFromJson(json);
			assertEquals(6, s_l.size());
			if (trace) {
				for (int i = 0; i < 6; i++) {
					logger.info(sj_l.get(i) + " " + s_l.get(i));
				}
			}
			Seasons seasons = SeasonsDataAccess.getSeasons(year);
			Arrays.stream(Season.Phenom.values()).forEach(seasons::getSeason);
		}
		if (trace)
			logger.info("Min: " + min + " Max: " + max);
	}

}
