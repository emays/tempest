package com.mays.tempest.weather;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UvForecastTest {

	private static final Logger logger = LoggerFactory.getLogger(UvForecastTest.class);

	private static final boolean trace = false;

	private final String cache_dir = "src/test/resources/weather";

	private boolean resources_exception = true;

	private String zipJson;

	private String cityJson;

	@BeforeEach
	public void before() throws Exception {
		setupZip();
		setupCity();
	}

	public void setupZip() throws Exception {
		if (zipJson != null)
			return;
		Path path = Paths.get(cache_dir, "uv-zip.json");
		if (!Files.exists(path)) {
			logger.info("setupZip");
			if (resources_exception)
				throw new Exception();
			String res = UvForecast.getForecastJsonString("02652");
			if (trace)
				logger.info(res);
			Files.writeString(path, res);
		}
		zipJson = Files.readString(path);
	}

	public void setupCity() throws Exception {
		if (cityJson != null)
			return;
		Path path = Paths.get(cache_dir, "uv-city.json");
		if (!Files.exists(path)) {
			logger.info("setupCity");
			if (resources_exception)
				throw new Exception();
			String res = UvForecast.getForecastJsonString("north truro", "ma");
			if (trace)
				logger.info(res);
			Files.writeString(path, res);
		}
		cityJson = Files.readString(path);
	}

	@Test
	public void getZip() throws Exception {
		if (trace)
			logger.info(zipJson);
	}

	@Test
	public void getCity() throws Exception {
		if (trace)
			logger.info(cityJson);
	}

}
