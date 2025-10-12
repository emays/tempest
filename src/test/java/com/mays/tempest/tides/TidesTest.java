package com.mays.tempest.tides;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mays.tempest.MyLocation;
import com.mays.tempest.ProvincetownLocation;
import com.mays.tempest.TestDataUtil;
import com.mays.tempest.TimeUtil;
import com.mays.tempest.WellfleetLocation;

public class TidesTest {

	private static final Logger logger = LoggerFactory.getLogger(TidesTest.class);

	private static final boolean trace = false;

	private final String cache_dir = "src/test/resources/tides";

	private boolean resources_exception = true;

	private String datumsJson;

	private String tidesJson;

	@BeforeAll
	public static void setTest() {
		TideDataAccess.setTest();
	}

	@BeforeEach
	public void before() throws Exception {
		setupDatums();
		setupTides();
	}

	public void setupDatums() throws Exception {
		if (datumsJson != null)
			return;
		Path path = Paths.get(cache_dir, "datums.json");
		if (!Files.exists(path)) {
			logger.info("setupDatums");
			if (resources_exception)
				throw new Exception();
			String res = Tides.getDatumsJsonString(MyLocation.TIDE_STATION_ID);
			if (trace)
				logger.info(res);
			Files.writeString(path, res);
		}
		datumsJson = Files.readString(path);
	}

	public void setupTides() throws Exception {
		if (tidesJson != null)
			return;
		Path path = Paths.get(cache_dir, "tides.json");
		if (!Files.exists(path)) {
			logger.info("setupTides");
			if (resources_exception)
				throw new Exception();
			String res = Tides.getTidesJsonString(MyLocation.TIDE_STATION_ID, "20210301", "20210331");
			if (trace)
				logger.info(res);
			Files.writeString(path, res);
		}
		tidesJson = Files.readString(path);
	}

	@Test
	public void getDatums() throws Exception {
		Datums datums = Tides.getDatumsFromJson(datumsJson);
		assertEquals(0, datums.getMeanLowerLowWater(), 0.001);
		assertEquals(9.62, datums.getMeanHighWater(), 0.001);
		assertEquals(0.33, datums.getMeanLowWater(), 0.001);
		Tide hat = datums.getHighestAstronomicalTide();
		assertEquals(12.06, hat.getValue(), 0.01);
		assertEquals("2034-05-19T04:24", hat.getTime().toString());
		Tide lat = datums.getLowestAstronomicalTide();
		assertEquals(-2.09, lat.getValue(), 0.01);
		assertEquals("2016-04-09T11:24", lat.getTime().toString());
	}

	@Test
	public void tideAmHigh() {
		TideJson tj = new TideJson();
		tj.setTime("2021-02-21 06:11");
		tj.setValue("12.597");
		tj.setType("H");
		Tide tide = new Tide(tj);
		LocalDateTime time = tide.getTime();
		assertEquals(2021, time.getYear());
		assertEquals(2, time.getMonthValue());
		assertEquals(21, time.getDayOfMonth());
		assertEquals(6, time.getHour());
		assertEquals(11, time.getMinute());
		assertEquals(12.597, tide.getValue(), 0.001);
		assertEquals(Tide.Type.High, tide.getType());
	}

	@Test
	public void tidePmLow() {
		TideJson tj = new TideJson();
		tj.setTime("2021-02-22 13:33");
		tj.setValue("5.170");
		tj.setType("L");
		Tide tide = new Tide(tj);
		LocalDateTime time = tide.getTime();
		assertEquals(2021, time.getYear());
		assertEquals(2, time.getMonthValue());
		assertEquals(22, time.getDayOfMonth());
		assertEquals(13, time.getHour());
		assertEquals(33, time.getMinute());
		assertEquals(5.170, tide.getValue(), 0.001);
		assertEquals(Tide.Type.Low, tide.getType());
	}

	@Test
	public void getTides() throws Exception {
		List<Tide> tides = Tides.getTidesFromJson(tidesJson);
		if (trace)
			tides.forEach(x -> logger.info(x.toString()));
		assertEquals(120, tides.size());
	}

	@Test
	public void getDailyTides() throws Exception {
		List<Tide> tides = Tides.getTidesFromJson(tidesJson);
		List<DailyTide> dts = Tides.getDailyTides(tides);
		if (trace) {
			for (DailyTide dt : dts) {
				logger.info(dt.toString());
			}
		}
		assertEquals(31, dts.size());
	}

	@Test
	public void getDailyTidesPriorNext() throws Exception {
		TestDataUtil tdu = new TestDataUtil("20210909T173907");
		assertEquals(116, tdu.getTidesForMonth(false).size());
		List<Tide> tides = tdu.getTidesForMonth(true);
		assertEquals(118, tides.size());
		List<DailyTide> dts = Tides.getDailyTides(tides, 2021, 9);
		if (trace) {
			for (DailyTide dt : dts) {
				logger.info(dt.toString());
			}
		}
		assertEquals(30, dts.size());
		assertEquals(tides.get(0), dts.get(0).getPrior());
		assertEquals(tides.get(tides.size() - 1), dts.get(dts.size() - 1).getNext());
	}

	@Test
	public void getMissing() throws Exception {
		assertNull(TideDataAccess.getTidesJsonString(MyLocation.TIDE_STATION_ID, 1990, 1, false));
		String msg = null;
		try {
			Tides.getTides(MyLocation.TIDE_STATION_ID, 1990, 1, true);
		} catch (FileNotFoundException ex) {
			msg = ex.getMessage();
		}
		assertEquals(Paths.get(cache_dir, MyLocation.TIDE_STATION_ID, "199001.json").toString(), msg);
	}

	@Test
	public void getTidesApart() throws Exception {
		for (String station : List.of(ProvincetownLocation.TIDE_STATION_ID, WellfleetLocation.TIDE_STATION_ID)) {
			for (int year = 2020; year <= 2029; year++) {
				for (int month = 1; month <= 12; month++) {
					List<Tide> tides = Tides.getTides(station, year, month, true);
					tides = Tides.getTides(tides, year, month);
					Collection<List<Tide>> days = tides.stream()
							.collect(Collectors.groupingBy(tide -> tide.getTime().toLocalDate())).values();
					days.forEach(day -> {
						assertTrue(day.size() >= 3);
						assertTrue(day.size() <= 4);
						for (Tide.Type ty : Tide.Type.values()) {
							List<Tide> ts = day.stream().filter(tide -> tide.getType() == ty)
									.collect(Collectors.toList());
							assertTrue(ts.size() <= 2);
							if (ts.size() == 2) {
								assertTrue(ts.get(0).getTime().getHour() < 12);
								if (!TimeUtil.isDstEnd(ts.get(1).getTime().toLocalDate()))
									assertTrue(ts.get(1).getTime().getHour() >= 12);
							}
						}
					});
				}
			}
		}
	}

	@Test
	public void getTideDateRange() throws Exception {
		for (String station : List.of(ProvincetownLocation.TIDE_STATION_ID, WellfleetLocation.TIDE_STATION_ID)) {
			for (int year = 2020; year <= 2029; year++) {
				for (int month = 1; month <= 12; month++) {
					LocalDate end_date = LocalDate.of(year, month, 1);
					LocalDate start_date = end_date.minusDays(1);
					if (start_date.getYear() == 2019)
						continue;
					List<Tide> tides = Tides.getTides(station, start_date, end_date, true);
					assertTrue(tides.size() >= 12);
					assertTrue(tides.size() <= 16);
				}
			}
		}
	}

}
