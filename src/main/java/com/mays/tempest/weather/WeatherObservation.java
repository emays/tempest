package com.mays.tempest.weather;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

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
import tech.units.indriya.quantity.Quantities;
import tech.units.indriya.unit.Units;

public class WeatherObservation {

	private ZonedDateTime time;

	private String rawMessage;

	private String textDescription;

	private String icon;

	private Quantity<Temperature> temperature;

	private Quantity<Temperature> dewpoint;

	private Quantity<Angle> windDirection;

	private Quantity<Speed> windSpeed;

	private Quantity<Speed> windGust;

	private Quantity<Pressure> barometricPressure;

	private Quantity<Pressure> seaLevelPressure;

	private Quantity<Length> visibility;

	private Quantity<Temperature> maxTemperatureLast24Hours;

	private Quantity<Temperature> minTemperatureLast24Hours;

	private Quantity<Length> precipitationLastHour;

	private Quantity<Length> precipitationLast3Hours;

	private Quantity<Length> precipitationLast6Hours;

	private Quantity<Dimensionless> relativeHumidity;

	private Quantity<Temperature> windChill;

	private Quantity<Temperature> heatIndex;

	public WeatherObservation(WeatherObservationJson obs_json) {
		time = ZonedDateTime.parse(obs_json.getTimestamp());
		rawMessage = obs_json.getRawMessage();
		icon = obs_json.getIcon();
		textDescription = obs_json.getTextDescription();
		temperature = toTemperature(obs_json.getTemperature());
		dewpoint = toTemperature(obs_json.getDewpoint());
		windDirection = toAngle(obs_json.getWindDirection());
		windSpeed = toSpeed(obs_json.getWindSpeed());
		windGust = toSpeed(obs_json.getWindGust());
		barometricPressure = WeatherQuantities.toPressure(obs_json.getBarometricPressure());
		seaLevelPressure = WeatherQuantities.toPressure(obs_json.getSeaLevelPressure());
		visibility = WeatherQuantities.toLength(obs_json.getVisibility());
		maxTemperatureLast24Hours = toTemperature(obs_json.getMaxTemperatureLast24Hours());
		minTemperatureLast24Hours = toTemperature(obs_json.getMinTemperatureLast24Hours());
		precipitationLastHour = WeatherQuantities.toLength(obs_json.getPrecipitationLastHour());
		precipitationLast3Hours = WeatherQuantities.toLength(obs_json.getPrecipitationLast3Hours());
		precipitationLast6Hours = WeatherQuantities.toLength(obs_json.getPrecipitationLast6Hours());
		relativeHumidity = toDimensionless(obs_json.getRelativeHumidity());
		windChill = toTemperature(obs_json.getWindChill());
		heatIndex = toTemperature(obs_json.getHeatIndex());
	}

	private Quantity<Temperature> toTemperature(Value value) {
		if (value.getValue() == null)
			return null;
		Unit<Temperature> unit = null;
		switch (value.getUnitCode()) {
		case "unit:degC":
		case "wmoUnit:degC":
			unit = Units.CELSIUS;
			break;
		default:
			throw new UnsupportedOperationException(value.getUnitCode());
		}
		return Quantities.getQuantity(value.getValue(), unit);
	}

	private Quantity<Angle> toAngle(Value value) {
		if (value.getValue() == null)
			return null;
		Unit<Angle> unit = null;
		switch (value.getUnitCode()) {
		case "unit:degree_(angle)":
		case "wmoUnit:degree_(angle)":
			unit = USCustomary.DEGREE_ANGLE;
			break;
		default:
			throw new UnsupportedOperationException(value.getUnitCode());
		}
		return Quantities.getQuantity(value.getValue(), unit);
	}

	private Quantity<Speed> toSpeed(Value value) {
		if (value.getValue() == null)
			return null;
		Unit<Speed> unit = null;
		switch (value.getUnitCode()) {
		case "unit:km_h-1":
		case "wmoUnit:km_h-1":
			unit = Units.KILOMETRE_PER_HOUR;
			break;
		default:
			throw new UnsupportedOperationException(value.getUnitCode());
		}
		return Quantities.getQuantity(value.getValue(), unit);
	}

	private Quantity<Dimensionless> toDimensionless(Value value) {
		if (value.getValue() == null)
			return null;
		Unit<Dimensionless> unit = null;
		switch (value.getUnitCode()) {
		case "unit:percent":
		case "wmoUnit:percent":
			unit = Units.PERCENT;
			break;
		default:
			throw new UnsupportedOperationException(value.getUnitCode());
		}
		return Quantities.getQuantity(value.getValue(), unit);
	}

	public ZonedDateTime getTime() {
		return time;
	}

	public LocalDateTime getLocalDateTime(WeatherObservationStation station) {
		return getTime().withZoneSameInstant(station.getTimeZone()).toLocalDateTime();
	}

	public String getRawMessage() {
		return rawMessage;
	}

	public String getTextDescription() {
		return textDescription;
	}

	public String getIcon() {
		return icon;
	}

	public Quantity<Temperature> getTemperature() {
		return temperature;
	}

	public Quantity<Temperature> getDewpoint() {
		return dewpoint;
	}

	public Quantity<Angle> getWindDirection() {
		return windDirection;
	}

	public Quantity<Speed> getWindSpeed() {
		return windSpeed;
	}

	public Quantity<Speed> getWindGust() {
		return windGust;
	}

	public Quantity<Pressure> getBarometricPressure() {
		return barometricPressure;
	}

	public Quantity<Pressure> getSeaLevelPressure() {
		return seaLevelPressure;
	}

	public Quantity<Length> getVisibility() {
		return visibility;
	}

	public Quantity<Temperature> getMaxTemperatureLast24Hours() {
		return maxTemperatureLast24Hours;
	}

	public Quantity<Temperature> getMinTemperatureLast24Hours() {
		return minTemperatureLast24Hours;
	}

	public Quantity<Length> getPrecipitationLastHour() {
		return precipitationLastHour;
	}

	public Quantity<Length> getPrecipitationLast3Hours() {
		return precipitationLast3Hours;
	}

	public Quantity<Length> getPrecipitationLast6Hours() {
		return precipitationLast6Hours;
	}

	public Quantity<Dimensionless> getRelativeHumidity() {
		return relativeHumidity;
	}

	public Quantity<Temperature> getWindChill() {
		return windChill;
	}

	public Quantity<Temperature> getHeatIndex() {
		return heatIndex;
	}

}
