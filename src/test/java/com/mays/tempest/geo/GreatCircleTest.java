package com.mays.tempest.geo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mays.tempest.LocationInfo;
import com.mays.tempest.tides.TideDataAccess;

public class GreatCircleTest {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(GreatCircleTest.class);

	@BeforeAll
	public static void setTest() {
		TideDataAccess.setTest();
	}

	@Test
	public void stationDistance() throws Exception {
		assertEquals(9.9043,
				GreatCircle.distance(LocationInfo.PROVINCETOWN.getCoordinate(), LocationInfo.WELLFLEET.getCoordinate()),
				0.00005);
	}

	Coordinate LAX = Coordinate.of(Coordinate.toDecimal(33, 56, 33), Coordinate.toDecimal(-118, 24, 29));
	Coordinate JFK = Coordinate.of(Coordinate.toDecimal(40, 38, 24), Coordinate.toDecimal(-73, 46, 43));

	@Test
	public void toDecimal() {
		assertEquals(33.9425, LAX.getLatitude(), 0.00005);
		assertEquals(-118.4081, LAX.getLongitude(), 0.00005);
		assertTrue(LAX.isValid());
	}

	@Test
	public void degreesToRadians() {
		assertEquals(0.592408, GreatCircle.degreesToRadians(LAX.getLatitude()), 0.0000005);
		assertEquals(-2.066610, GreatCircle.degreesToRadians(LAX.getLongitude()), 0.0000005);
		assertEquals(0.709302, GreatCircle.degreesToRadians(JFK.getLatitude()), 0.0000005);
		assertEquals(Math.toRadians(JFK.getLatitude()), GreatCircle.degreesToRadians(JFK.getLatitude()));
		assertEquals(-1.287680, GreatCircle.degreesToRadians(JFK.getLongitude()), 0.0000005);
		assertEquals(Math.toRadians(JFK.getLongitude()), GreatCircle.degreesToRadians(JFK.getLongitude()));
	}

	@Test
	public void distance() throws Exception {
		assertEquals(2144.46, GreatCircle.distance(LAX, JFK), 0.005);
	}

	@Test
	void radialDistance() {
		Coordinate coord = GreatCircle.radialDistance(LAX.getLatitude(), LAX.getLongitude(), 66, 100);
		assertEquals(34.6066, coord.getLatitude(), 0.00005);
		assertEquals(-116.5581, coord.getLongitude(), 0.00005);
	}

}
