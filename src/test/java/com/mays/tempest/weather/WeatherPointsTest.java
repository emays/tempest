package com.mays.tempest.weather;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mays.tempest.geo.Coordinate;
import com.mays.tempest.geo.KmlPointDisplay;
import com.mays.util.Util;

public class WeatherPointsTest {

	private static final Logger logger = LoggerFactory.getLogger(WeatherPointsTest.class);

	private static final boolean trace = false;

	public static class WeatherPoint implements KmlPointDisplay {

		private String name;

		private Coordinate coordinate;

		@Override
		public String getKmlName() {
			return name;
		}

		@Override
		public String getKmlDescription() {
			return name + " (" + coordinate + ")";
		}

		@Override
		public Coordinate getKmlCoordinate() {
			return coordinate;
		}

		@Override
		public String getKmlUrl() {
			return "http://localhost/tides/tides?id=8446121";
		}

		@Override
		public String getKmlAnchorText() {
			return "Wx Point";
		}

	}

	@Test
	public void getCenter() throws Exception {
		Path path = Paths.get("src/test/resources/weather", "weather-forecast.json");
		getCenter(path);
	}

	public void getCenter(Path file) throws Exception {
		String json = Files.readString(file);
		ObjectMapper mapper = new ObjectMapper();
		JsonNode jsonNode = mapper.readTree(json);
		JsonNode coords = jsonNode.get("geometry").get("coordinates").get(0);
		if (trace)
			logger.info("\n" + coords);
		double max_lat = Integer.MIN_VALUE;
		double min_lat = Integer.MAX_VALUE;
		double max_lng = Integer.MIN_VALUE;
		double min_lng = Integer.MAX_VALUE;
		for (JsonNode el : Util.iterable(coords.elements())) {
			if (trace)
				logger.info("" + el);
			Coordinate coord = new Coordinate(el.get(1).asDouble(), el.get(0).asDouble());
			if (trace)
				logger.info(coord.toString());
			max_lat = Math.max(max_lat, coord.getLatitude());
			min_lat = Math.min(min_lat, coord.getLatitude());
			max_lng = Math.max(max_lng, coord.getLongitude());
			min_lng = Math.min(min_lng, coord.getLongitude());
		}
		if (trace) {
			logger.info(min_lat + " " + max_lat);
			logger.info(min_lng + " " + max_lng);
			Coordinate center = new Coordinate((min_lat + max_lat) / 2, (min_lng + max_lng) / 2);
			logger.info("Center: " + center);
			logger.info("Offset: " + (max_lat - min_lat) + " " + (max_lng - min_lng));
		}
	}

}
