package com.mays.tempest.tides;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mays.tempest.LocationInfo;
import com.mays.tempest.geo.Kml;
import com.mays.tempest.geo.KmlPointDisplay;

public class TideStationsTest {

	private static final Logger logger = LoggerFactory.getLogger(TideStationsTest.class);

	private static final boolean trace = false;

	@BeforeAll
	public static void setTest() {
		TideDataAccess.setTest();
	}

	@Test
	public void getStation() throws Exception {
		TideStation st = TideStations.getInstance().getStation(LocationInfo.PROVINCETOWN.getTideStationId());
		assertEquals("Provincetown", st.getName());
		assertEquals("MA", st.getState());
		assertEquals("R", st.getType());
	}

	@Test
	public void getStationSubordinate() throws Exception {
		TideStation st = TideStations.getInstance().getStation(LocationInfo.WELLFLEET.getTideStationId());
		assertEquals("Wellfleet", st.getName());
		assertEquals("MA", st.getState());
		assertEquals("S", st.getType());
		assertEquals(LocationInfo.BOSTON.getTideStationId(), st.getReference());
	}

	@Test
	public void getStates() throws Exception {
		List<String> states = TideStations.getInstance().getStates();
		if (trace)
			states.forEach(x -> logger.info(x));
		assertEquals(23, states.size());
	}

	@Test
	public void getStations() throws Exception {
		assertEquals(2217, TideStations.getInstance().getStations().size());
	}

	@Test
	public void getStationsJson() throws Exception {
		String stationsJson = TideDataAccess.getStationsJsonString();
		List<TideStation> stations = TideStations.getStationsFromJson(stationsJson);
		assertEquals(3258, stations.size());
	}

	@Test
	public void getNearest() throws Exception {
		List<TideStationDistance> nearest = TideStations.getInstance().getNearest(LocationInfo.PROVINCETOWN.getLatitude(),
				LocationInfo.PROVINCETOWN.getLongitude(), 5, 100);
		if (trace)
			nearest.forEach(x -> logger.info(x.toString()));
		assertEquals(1, nearest.size());
		assertEquals(LocationInfo.PROVINCETOWN.getTideStationId(), nearest.getFirst().getStation().getId());
	}

	@Test
	public void getNearestNm() throws Exception {
		List<TideStationDistance> nearest = TideStations.getInstance().getNearest(LocationInfo.PROVINCETOWN.getLatitude(),
				LocationInfo.PROVINCETOWN.getLongitude(), 20, 100);
		if (trace)
			nearest.forEach(x -> logger.info(x.toString()));
		assertEquals(3, nearest.size());
		assertEquals(LocationInfo.PROVINCETOWN.getTideStationId(), nearest.getFirst().getStation().getId());
	}

	@Test
	public void getNearestLimit() throws Exception {
		List<TideStationDistance> nearest = TideStations.getInstance().getNearest(LocationInfo.PROVINCETOWN.getLatitude(),
				LocationInfo.PROVINCETOWN.getLongitude(), Integer.MAX_VALUE, 5);
		if (trace)
			nearest.forEach(x -> logger.info(x.toString()));
		assertEquals(5, nearest.size());
		assertEquals(LocationInfo.PROVINCETOWN.getTideStationId(), nearest.getFirst().getStation().getId());
	}

	@Test
	public void buildKml() throws Exception {
		List<KmlPointDisplay> stations = new ArrayList<>(TideStations.getInstance().getStations());
		new Kml().createPointsKml(Paths.get("target", "tideStations.kml"), stations,
				"https://maps.google.com/mapfiles/kml/paddle/blu-circle.png");
	}

}
