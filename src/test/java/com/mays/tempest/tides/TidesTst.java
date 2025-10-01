package com.mays.tempest.tides;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mays.tempest.MyLocation;

public class TidesTst {

	private static final Logger logger = LoggerFactory.getLogger(TidesTst.class);

	private static final boolean trace = false;

	@Test
	public void getTidesJson() throws Exception {
		List<TideJson> tides = Tides.getTidesJson(MyLocation.TIDE_STATION_ID, "20210221", "20210227");
		if (trace) {
			for (TideJson tj : tides) {
				logger.info(tj.toString());
				logger.info(new Tide(tj).toString());
			}
		}
		assertEquals(27, tides.size());
	}

	@Test
	public void getTides() throws Exception {
		List<Tide> tides = Tides.getTides(MyLocation.TIDE_STATION_ID, 2021, 9);
		if (trace) {
			for (Tide tide : tides) {
				logger.info(tide.toString());
			}
		}
		assertEquals(124, tides.size());
	}

}
