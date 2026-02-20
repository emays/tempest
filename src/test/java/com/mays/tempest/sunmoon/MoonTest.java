package com.mays.tempest.sunmoon;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mays.tempest.LocationInfo;
import com.mays.util.Util;

public class MoonTest {

	private static final Logger logger = LoggerFactory.getLogger(MoonTest.class);

	private static final boolean trace = false;

	@Test
	public void getRiseSet() {
		Util.repeat(365, day -> {
			LocalDate date = LocalDate.of(2021, 1, 1).plusDays(day);
			Moon moon = Moon.get(date, LocationInfo.PROVINCETOWN.getCoordinate(), LocationInfo.PROVINCETOWN.getTimeZone());
			if (trace) {
				logger.info("Rise: " + date + " " + moon.getRise());
				logger.info("Set:  " + date + " " + moon.getSet());
			}
		});

	}

	@Test
	public void getPhase() {
		Util.repeat(365, day -> {
			LocalDate date = LocalDate.of(2021, 1, 1).plusDays(day);
			Moon moon = Moon.get(date, LocationInfo.PROVINCETOWN.getCoordinate(), LocationInfo.PROVINCETOWN.getTimeZone());
			if (trace)
				logger.info(date + " " + moon.getPhaseName());
		});
	}

	// https://www.weather.gov/box/sunmoon?siteid=PVC&month=1&year=2021

	// curl "https://www.weather.gov/source/box/forecasts/sunmoon/PVC_1_2021.xml" >
	// pvc202101.txt

}
