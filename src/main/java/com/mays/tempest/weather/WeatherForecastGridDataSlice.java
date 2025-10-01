package com.mays.tempest.weather;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import javax.measure.Quantity;
import javax.measure.quantity.Angle;
import javax.measure.quantity.Dimensionless;
import javax.measure.quantity.Length;
import javax.measure.quantity.Speed;
import javax.measure.quantity.Temperature;

public class WeatherForecastGridDataSlice {

	private ZonedDateTime time;

	private Quantity<Temperature> temperature;

	private Quantity<Temperature> dewpoint;

	private Quantity<Dimensionless> relativeHumidity;

	private Quantity<Temperature> heatIndex;

	private Quantity<Temperature> windChill;

	private Quantity<Dimensionless> skyCover;

	private Quantity<Angle> windDirection;

	private Quantity<Speed> windSpeed;

	private Quantity<Speed> windGust;

	private Quantity<Dimensionless> probabilityOfPrecipitation;

	private Quantity<Length> quantitativePrecipitation;

	private Quantity<Length> ceilingHeight;

	private Quantity<Length> visibility;

	public WeatherForecastGridDataSlice(ZonedDateTime time) {
		super();
		this.time = time;
	}

	public ZonedDateTime getTime() {
		return time;
	}

	public LocalDateTime getLocalDateTime(ZoneId timezone) {
		return getTime().withZoneSameInstant(timezone).toLocalDateTime();
	}

	public void setTime(ZonedDateTime time) {
		this.time = time;
	}

	public Quantity<Temperature> getTemperature() {
		return temperature;
	}

	public void setTemperature(Quantity<Temperature> temperature) {
		this.temperature = temperature;
	}

	public Quantity<Temperature> getDewpoint() {
		return dewpoint;
	}

	public void setDewpoint(Quantity<Temperature> dewpoint) {
		this.dewpoint = dewpoint;
	}

	public Quantity<Dimensionless> getRelativeHumidity() {
		return relativeHumidity;
	}

	public void setRelativeHumidity(Quantity<Dimensionless> relativeHumidity) {
		this.relativeHumidity = relativeHumidity;
	}

	public Quantity<Temperature> getHeatIndex() {
		return heatIndex;
	}

	public void setHeatIndex(Quantity<Temperature> heatIndex) {
		this.heatIndex = heatIndex;
	}

	public Quantity<Temperature> getWindChill() {
		return windChill;
	}

	public void setWindChill(Quantity<Temperature> windChill) {
		this.windChill = windChill;
	}

	public Quantity<Dimensionless> getSkyCover() {
		return skyCover;
	}

	public void setSkyCover(Quantity<Dimensionless> skyCover) {
		this.skyCover = skyCover;
	}

	public Quantity<Angle> getWindDirection() {
		return windDirection;
	}

	public void setWindDirection(Quantity<Angle> windDirection) {
		this.windDirection = windDirection;
	}

	public Quantity<Speed> getWindSpeed() {
		return windSpeed;
	}

	public void setWindSpeed(Quantity<Speed> windSpeed) {
		this.windSpeed = windSpeed;
	}

	public Quantity<Speed> getWindGust() {
		return windGust;
	}

	public void setWindGust(Quantity<Speed> windGust) {
		this.windGust = windGust;
	}

	public Quantity<Dimensionless> getProbabilityOfPrecipitation() {
		return probabilityOfPrecipitation;
	}

	public void setProbabilityOfPrecipitation(Quantity<Dimensionless> probabilityOfPrecipitation) {
		this.probabilityOfPrecipitation = probabilityOfPrecipitation;
	}

	public Quantity<Length> getQuantitativePrecipitation() {
		return quantitativePrecipitation;
	}

	public void setQuantitativePrecipitation(Quantity<Length> quantitativePrecipitation) {
		this.quantitativePrecipitation = quantitativePrecipitation;
	}

	public Quantity<Length> getCeilingHeight() {
		return ceilingHeight;
	}

	public void setCeilingHeight(Quantity<Length> ceilingHeight) {
		this.ceilingHeight = ceilingHeight;
	}

	public Quantity<Length> getVisibility() {
		return visibility;
	}

	public void setVisibility(Quantity<Length> visibility) {
		this.visibility = visibility;
	}

}
