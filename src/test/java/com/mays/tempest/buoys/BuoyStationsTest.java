package com.mays.tempest.buoys;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mays.tempest.MyLocation;
import com.mays.tempest.geo.Kml;
import com.mays.tempest.geo.KmlPointDisplay;

public class BuoyStationsTest {

	private static final Logger logger = LoggerFactory.getLogger(BuoyStationsTest.class);

	private static final boolean trace = false;

	private static final String PVC_ID = "44018";

	@BeforeAll
	public static void setTest() {
		BuoyDataAccess.setTest();
	}

	@Test
	public void getStations() throws Exception {
		List<BuoyStation> stations = BuoyStations.getInstance().getStations();
		if (trace) {
			logger.info("" + stations.size());
			logger.info("Max lat: " + stations.stream().max(Comparator.comparing(BuoyStation::getLat)).get().getId());
			logger.info("Min lat: " + stations.stream().min(Comparator.comparing(BuoyStation::getLat)).get().getId());
			logger.info("Max lon: " + stations.stream().max(Comparator.comparing(BuoyStation::getLon)).get().getId());
			logger.info("Min lon: " + stations.stream().min(Comparator.comparing(BuoyStation::getLon)).get().getId());
		}
		assertEquals(899, stations.size());
	}

	@Test
	public void getStationsXml() throws Exception {
		String stations_xml = BuoyDataAccess.getStationsXml();
		List<BuoyStation> stations = BuoyStations.getStationsFromXml(stations_xml);
		if (trace) {
			logger.info("" + stations.size());
			logger.info("Max lat: " + stations.stream().max(Comparator.comparing(BuoyStation::getLat)).get().getId());
			logger.info("Min lat: " + stations.stream().min(Comparator.comparing(BuoyStation::getLat)).get().getId());
			logger.info("Max lon: " + stations.stream().max(Comparator.comparing(BuoyStation::getLon)).get().getId());
			logger.info("Min lon: " + stations.stream().min(Comparator.comparing(BuoyStation::getLon)).get().getId());
		}
		assertEquals(1319, stations.size());
	}

	@Test
	public void getNearest() throws Exception {
		List<BuoyStationDistance> nearest = BuoyStations.getInstance().getNearest(MyLocation.LATITUDE,
				MyLocation.LONGITUDE, 15, 1);
		if (trace)
			nearest.forEach(x -> logger.info(x.toString()));
		assertEquals(1, nearest.size());
		assertEquals(PVC_ID, nearest.get(0).getStation().getId());
		assertEquals(PVC_ID,
				BuoyStations.getInstance().getNearest(MyLocation.LATITUDE, MyLocation.LONGITUDE).getStation().getId());
	}

	@Test
	public void buildKml() throws Exception {
		List<KmlPointDisplay> stations = new ArrayList<>(BuoyStations.getInstance().getStations());
		new Kml().createPointsKml(Paths.get("target", "buoyStations.kml"), stations,
				"https://maps.google.com/mapfiles/kml/paddle/ylw-circle.png");
	}

}
