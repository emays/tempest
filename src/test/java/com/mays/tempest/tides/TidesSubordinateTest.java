package com.mays.tempest.tides;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mays.tempest.BostonLocation;
import com.mays.tempest.TimeUtil;
import com.mays.tempest.WellfleetLocation;
import com.mays.tempest.tides.TideDataAccess.StartEnd;

public class TidesSubordinateTest {

	private static final Logger logger = LoggerFactory.getLogger(TidesSubordinateTest.class);

	@BeforeAll
	public static void setTest() {
		TideDataAccess.setTest();
	}

//	{
//		  "refStationId": "8443970",
//		  "type": "S",
//		  "heightOffsetHighTide": 1.05,
//		  "heightOffsetLowTide": 1.05,
//		  "timeOffsetHighTide": 14,
//		  "timeOffsetLowTide": 30,
//		  "heightAdjustedType": "R",
//		  "self": null
//		}

	@Test
	public void getTides() throws Exception {
		String station = WellfleetLocation.TIDE_STATION_ID;
		StartEnd start_end = TideDataAccess.getStartEnd(station);
		LocalDate start = start_end.start();
		LocalDate end = start_end.end();
		logger.info(station + " " + start + " " + end);
		for (int year = start.getYear(); year <= end.getYear(); year++) {
			for (int month = 1; month <= 12; month++) {
				List<Tide> bos_tides = new ArrayList<>(
						Tides.getTides(BostonLocation.TIDE_STATION_ID, year, month, true, true));
				List<Tide> wf_tides = new ArrayList<>(
						Tides.getTides(WellfleetLocation.TIDE_STATION_ID, year, month, true, true));
				// Boston is earlier than Wellfleet
				if (bos_tides.getFirst().getType() != wf_tides.getFirst().getType()) {
//					logger.info("Drop first: " + bos_tides.getFirst() + " " + wf_tides.getFirst());
					wf_tides.removeFirst();
				}
				assertEquals(bos_tides.getFirst().getType(), wf_tides.getFirst().getType());
				if (bos_tides.size() != wf_tides.size()) {
//					logger.info("Drop last: " + bos_tides.getLast() + " " + wf_tides.getLast());
					bos_tides.removeLast();
				}
				assertEquals(bos_tides.size(), wf_tides.size());
				for (int i = 0; i < bos_tides.size(); i++) {
					Tide bos = bos_tides.get(i);
					Tide wf = wf_tides.get(i);
					assertEquals(bos.getType(), wf.getType());
					if (TimeUtil.isDstStart(bos.getTime().toLocalDate())
							|| TimeUtil.isDstEnd(bos.getTime().toLocalDate())) {
//						logger.info("Tide: " + bos + " " + wf);
						continue;
					}
					if (bos.getType() == Tide.Type.High) {
						assertEquals(14, bos.getTime().until(wf.getTime(), ChronoUnit.MINUTES));
					} else {
						assertEquals(30, bos.getTime().until(wf.getTime(), ChronoUnit.MINUTES));
					}
					assertEquals(bos.getValue() * 1.05, wf.getValue(), 0.01);
				}
			}
		}
	}

}
