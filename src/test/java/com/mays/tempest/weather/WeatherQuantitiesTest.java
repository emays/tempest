package com.mays.tempest.weather;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;

import javax.measure.Quantity;
import javax.measure.quantity.Angle;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mays.tempest.weather.WeatherQuantities.CompassPoint;
import com.mays.tempest.weather.WeatherQuantities.CompassRose;

import systems.uom.common.USCustomary;
import tech.units.indriya.quantity.Quantities;

public class WeatherQuantitiesTest {

	private static final Logger logger = LoggerFactory.getLogger(WeatherQuantitiesTest.class);

	private static final boolean trace = false;

	private Quantity<Angle> toAngle(Number deg) {
		return Quantities.getQuantity(deg, USCustomary.DEGREE_ANGLE);
	}

	@Test
	public void compassRose() {
		assertEquals(4, CompassRose.FOUR.getNumberOfPoints());
		assertEquals(List.of(CompassPoint.N, CompassPoint.E, CompassPoint.S, CompassPoint.W),
				CompassRose.FOUR.getPoints());
		assertEquals(8, CompassRose.EIGHT.getNumberOfPoints());
		assertEquals(List.of(CompassPoint.N, CompassPoint.NE, CompassPoint.E, CompassPoint.SE, CompassPoint.S,
				CompassPoint.SW, CompassPoint.W, CompassPoint.NW), CompassRose.EIGHT.getPoints());
		assertEquals(16, CompassRose.SIXTEEN.getNumberOfPoints());
		assertEquals(Arrays.asList(CompassPoint.values()), CompassRose.SIXTEEN.getPoints());
	}

	@Test
	public void compassPoints() {
		for (CompassPoint pt : CompassPoint.values()) {
			assertEquals(pt, WeatherQuantities.toCompassPoint(pt.getAngle(), CompassRose.SIXTEEN));
		}
	}

	@Test
	public void lessThan0() {
		for (CompassRose cr : CompassRose.values()) {
			for (double i = 0; i >= -360 * 4; i = i - 0.25) {
				assertEquals(WeatherQuantities.toCompassPoint(toAngle(360 - (-i % 360)), cr),
						WeatherQuantities.toCompassPoint(toAngle(i), cr));
			}
		}
	}

	@Test
	public void greaterThan360() {
		for (CompassRose cr : CompassRose.values()) {
			for (double i = 0; i <= 360 * 4; i = i + 0.25) {
				assertEquals(WeatherQuantities.toCompassPoint(toAngle(i % 360), cr),
						WeatherQuantities.toCompassPoint(toAngle(i), cr));
			}
		}
	}

	@Test
	public void compassPointsList() {
		for (double i = 0; i <= 360; i = i + 10) {
			Quantity<Angle> deg = toAngle(i);
			if (trace)
				logger.info(String.format("%6.2f %3s %3s %3s", i,
						WeatherQuantities.toCompassPoint(deg, CompassRose.SIXTEEN),
						WeatherQuantities.toCompassPoint(deg, CompassRose.EIGHT),
						WeatherQuantities.toCompassPoint(deg, CompassRose.FOUR)));
		}
	}

}
