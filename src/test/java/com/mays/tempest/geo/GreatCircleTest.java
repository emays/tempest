package com.mays.tempest.geo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mays.tempest.MyLocation;
import com.mays.tempest.tides.TideDataAccess;
import com.mays.tempest.tides.TideStation;
import com.mays.tempest.tides.TideStations;

public class GreatCircleTest {

	private static final Logger logger = LoggerFactory.getLogger(GreatCircleTest.class);

	@BeforeAll
	public static void setTest() {
		TideDataAccess.setTest();
	}

	@Test
	public void distance() throws Exception {
		TideStation st = TideStations.getInstance().getStation(MyLocation.TIDE_STATION_ID);
		logger.info(st.toString());
		assertEquals(4.52, GreatCircle.distance(MyLocation.LATITUDE, MyLocation.LONGITUDE, st.getLat(), st.getLng()),
				0.01);
	}

}
