package com.mays.tempest.buoys;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import si.uom.NonSI;
import systems.uom.common.USCustomary;

public class BuoysTest {

	private static final Logger logger = LoggerFactory.getLogger(BuoysTest.class);

	private static final boolean trace = false;

	private final String cache_dir = "src/test/resources/buoys";

	private boolean resources_exception = true;

	private static final String PVC_ID = "44018";

	private String observationsString;

	@BeforeEach
	public void before() throws Exception {
		if (observationsString != null)
			return;
		Path path = Paths.get(cache_dir, "buoy-observations.txt");
		if (!Files.exists(path)) {
			logger.info("setupObservations");
			if (resources_exception)
				throw new Exception();
			String res = new Buoys().getObservationsString(PVC_ID);
			if (trace)
				logger.info(res);
			Files.writeString(path, res);
		}
		observationsString = Files.readString(path);
	}

	@Test
	public void getObservations() throws Exception {
		Buoys bo = new Buoys();
		ArrayList<BuoyObservation> obs = bo.getObservationsFromString(observationsString);
		// 2021 03 09 17 20 290 3.0 4.0 MM MM MM MM 1021.4 5.6 3.9 1.3 MM MM MM
		BuoyObservation obs2 = obs.get(1);
		assertEquals(290, obs2.getWindDirection().getValue().intValue());
		assertEquals(5.8, obs2.getWindSpeed().to(USCustomary.KNOT).getValue().doubleValue(), 0.1);
		assertEquals(7.8, obs2.getWindGust().to(USCustomary.KNOT).getValue().doubleValue(), 0.1);
		assertNull(obs2.getWaveHeight());
		assertNull(obs2.getWaveDirection());
		assertEquals(30.16, obs2.getAtmosphericPressure().to(NonSI.INCH_OF_MERCURY).getValue().doubleValue(), 0.01);
		assertEquals(42, obs2.getAirTemperature().to(USCustomary.FAHRENHEIT).getValue().doubleValue(), 0.5);
		assertEquals(39, obs2.getWaterTemperature().to(USCustomary.FAHRENHEIT).getValue().doubleValue(), 0.5);
		assertEquals(34, obs2.getDewPoint().to(USCustomary.FAHRENHEIT).getValue().doubleValue(), 0.5);
		if (trace)
			logger.info("Obs2: " + obs2);
		// 2021 03 09 16 50 280 3.0 3.0 0.5 3 3.8 255 1021.6 5.3 4.2 1.2 MM MM MM
		BuoyObservation obs5 = obs.get(4);
		assertEquals(1.6, obs5.getWaveHeight().to(USCustomary.FOOT).getValue().doubleValue(), 0.1);
		assertEquals(255, obs5.getWaveDirection().getValue().intValue());
		if (trace)
			logger.info("Obs5: " + obs5);
	}

//	@Test
	public void getStation() throws Exception {
		Buoys bo = new Buoys();
		ArrayList<BuoyObservation> obs = bo.getObservations(PVC_ID);
		obs.forEach(x -> logger.info(x.toString()));
	}

}
