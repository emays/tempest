package com.mays.tempest.weather;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.measure.MetricPrefix;
import javax.measure.Quantity;
import javax.measure.Unit;
import javax.measure.quantity.Angle;
import javax.measure.quantity.Dimensionless;
import javax.measure.quantity.Length;
import javax.measure.quantity.Pressure;
import javax.measure.quantity.Speed;
import javax.measure.quantity.Temperature;

import com.mays.tempest.weather.WeatherObservationJson.Value;

import systems.uom.common.USCustomary;
import tech.units.indriya.ComparableQuantity;
import tech.units.indriya.quantity.Quantities;
import tech.units.indriya.unit.Units;

public class WeatherQuantities {

	public static Quantity<Angle> toAngle(Number value, String uom) {
		if (value == null)
			return null;
		Unit<Angle> unit = null;
		switch (uom) {
		case "degT":
		case "wmoUnit:degree_(angle)":
			unit = USCustomary.DEGREE_ANGLE;
			break;
		default:
			throw new UnsupportedOperationException(uom);
		}
		return Quantities.getQuantity(value, unit);
	}

	public static enum CompassPoint {

		N, NNE, NE, ENE, E, ESE, SE, SSE, S, SSW, SW, WSW, W, WNW, NW, NNW;

		public double getDegrees() {
			return 22.5 * ordinal();
		}

		public Quantity<Angle> getAngle() {
			return Quantities.getQuantity(getDegrees(), USCustomary.DEGREE_ANGLE);
		}

	}

	public static enum CompassRose {

		FOUR, EIGHT, SIXTEEN;

		public int getNumberOfPoints() {
			return 4 * (int) Math.pow(2, ordinal());
		}

		public List<CompassPoint> getPoints() {
			return Arrays.stream(CompassPoint.values()).filter(pt -> (pt.ordinal() % (16 / getNumberOfPoints())) == 0)
					.collect(Collectors.toList());
		}

	}

	public static CompassPoint toCompassPoint(Quantity<Angle> angle) {
		return toCompassPoint(angle, CompassRose.EIGHT);
	}

	public static CompassPoint toCompassPoint(Quantity<Angle> angle, CompassRose rose) {
		double deg = angle.to(USCustomary.DEGREE_ANGLE).getValue().doubleValue();
		deg = deg % 360;
		if (deg < 0)
			deg = 360.0 + deg;
		int i = (int) (Math.round(deg / (360.0 / rose.getNumberOfPoints())) % rose.getNumberOfPoints());
		i = i * (16 / rose.getNumberOfPoints());
		return CompassPoint.values()[i];
	}

	public static Quantity<Dimensionless> toDimensionless(Double value, String uom) {
		if (value == null)
			return null;
		Unit<Dimensionless> unit = null;
		switch (uom) {
		case "wmoUnit:percent":
			unit = Units.PERCENT;
			break;
		default:
			throw new UnsupportedOperationException(uom);
		}
		return Quantities.getQuantity(value, unit);
	}

	public static Quantity<Length> toLength(Value value) {
		return toLength(value.getValue(), value.getUnitCode());
	}

	public static Quantity<Length> toLength(Double value, String uom) {
		if (value == null)
			return null;
		Unit<Length> unit = null;
		switch (uom) {
		case "m":
		case "unit:m":
		case "wmoUnit:m":
			unit = Units.METRE;
			break;
		case "wmoUnit:mm":
			unit = MetricPrefix.MILLI(Units.METRE);
			break;
		case "nm":
			unit = USCustomary.NAUTICAL_MILE;
			break;
		default:
			throw new UnsupportedOperationException(uom);
		}
		return Quantities.getQuantity(value, unit);
	}

	public static Quantity<Speed> toSpeed(Double value, String uom) {
		if (value == null)
			return null;
		Unit<Speed> unit = null;
		switch (uom) {
		case "wmoUnit:km_h-1":
			unit = Units.KILOMETRE_PER_HOUR;
			break;
		case "m/s":
			unit = Units.METRE_PER_SECOND;
			break;
		default:
			throw new UnsupportedOperationException(uom);
		}
		return Quantities.getQuantity(value, unit);
	}

	public static Quantity<Temperature> toTemperature(Double value, String uom) {
		if (value == null)
			return null;
		Unit<Temperature> unit = null;
		switch (uom) {
		case "C":
		case "degC":
		case "wmoUnit:degC":
			unit = Units.CELSIUS;
			break;
		case "F":
			unit = USCustomary.FAHRENHEIT;
			break;
		default:
			throw new UnsupportedOperationException(uom);
		}
		return Quantities.getQuantity(value, unit);
	}

	public static Quantity<Pressure> toPressure(Value value) {
		return toPressure(value.getValue(), value.getUnitCode());
	}

	public static Quantity<Pressure> toPressure(Double value, String uom) {
		if (value == null)
			return null;
		Unit<Pressure> unit = null;
		switch (uom) {
		case "unit:Pa":
		case "wmoUnit:Pa":
			unit = Units.PASCAL;
			break;
		case "hPa":
			unit = MetricPrefix.HECTO(Units.PASCAL);
			break;
		default:
			throw new UnsupportedOperationException(uom);
		}
		return Quantities.getQuantity(value, unit);
	}

	// https://forecast.weather.gov/glossary.php

	// https://www.weather.gov/bgm/forecast_terms

	// Sky Condition Opaque Cloud Coverage
	// Clear/Sunny 1/8 or less
	// Mostly Clear/Mostly Sunny 1/8 to 3/8
	// Partly Cloudy/Partly Sunny 3/8 to 5/8
	// Mostly Cloudy 5/8 to 7/8
	// Cloudy 7/8 to 8/8

	public enum SkyCondition {

		CLEAR(1.0 / 8.0, "Clear", "Sunny"), //
		MOSTLY_CLEAR(3.0 / 8.0, "Mostly clear", "Mostly sunny"), //
		PARTLY_CLOUDY(5.0 / 8.0, "Partly cloudy", "Partly sunny"), //
		MOSTLY_CLOUDY(7.0 / 8.0, "Mostly cloudy"), //
		CLOUDY(1.0, "Cloudy");

		private ComparableQuantity<Dimensionless> coverage;

		private String term;

		private String dayTerm;

		private SkyCondition(double coverage, String term, String dayTerm) {
			this.coverage = Quantities.getQuantity(coverage * 100, Units.PERCENT);
			this.term = term;
			this.dayTerm = dayTerm;
		}

		private SkyCondition(double coverage, String term) {
			this(coverage, term, null);
		}

		public Quantity<Dimensionless> getCoverage() {
			return coverage;
		}

		public String getTerm() {
			return term;
		}

		public String getDayTerm() {
			return dayTerm;
		}

		private static SkyCondition get(ComparableQuantity<Dimensionless> coverage) {
			for (SkyCondition sc : SkyCondition.values()) {
				if (coverage.isLessThanOrEqualTo(sc.getCoverage()))
					return sc;
			}
			throw new RuntimeException(coverage.toString());
		}

	}

	public static SkyCondition toSkyCondition(Quantity<Dimensionless> coverage) {
		return SkyCondition.get((ComparableQuantity<Dimensionless>) coverage);
	}

}
