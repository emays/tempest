package com.mays.tempest.geo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ContiguousUSTest {

	private static final Logger logger = LoggerFactory.getLogger(ContiguousUSTest.class);

	private static final boolean trace = false;

	@Test
	public void testCoord() {
		KmlCoordinate coord = new KmlCoordinate("-70.4,45.2");
		assertEquals(45.2, coord.getLatitude(), 0.1);
		assertEquals(-70.4, coord.getLongitude(), 0.1);
	}

	@Test
	public void testCoordinates() {
		List<Coordinate> coords = new KmlPolygon("-70.4,45.2 -80.1,50.3").getCoordinates();
		assertEquals(45.2, coords.get(0).getLatitude(), 0.1);
		assertEquals(-70.4, coords.get(0).getLongitude(), 0.1);
		assertEquals(50.3, coords.get(1).getLatitude(), 0.1);
		assertEquals(-80.1, coords.get(1).getLongitude(), 0.1);
	}

	@Test
	public void testBounds() {
		ContiguousUS cu = new ContiguousUS();
		if (trace)
			logger.info("" + ContiguousUS.SOUTHERNMOST);
		assertTrue(cu.inBounds(ContiguousUS.NORTHERNMOST));
		assertTrue(cu.inBounds(ContiguousUS.SOUTHERNMOST));
		assertTrue(cu.inBounds(ContiguousUS.EASTERNMOST));
		assertTrue(cu.inBounds(ContiguousUS.WESTERNMOST));
		assertTrue(cu.inBounds(new Coordinate(40, -70)));
	}

	@Test
	public void testBounds2() throws Exception {
		ContiguousUS cu = new ContiguousUS();
		String coords = Files.readString(Paths.get("src/test/resources", "geo", "coords.txt"));
		if (trace)
			logger.info(coords.trim());
		for (Coordinate coord : new KmlPolygon(coords).getCoordinates()) {
			if (trace)
				logger.info(coord.getLatitude() + " " + coord.getLongitude());
			assertTrue(cu.inBounds(coord));
		}
	}

}
