package com.mays.tempest.weather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherObservationJson {

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Value {

		private Double value;

		private String unitCode;

		public Double getValue() {
			return value;
		}

		public void setValue(Double value) {
			this.value = value;
		}

		public String getUnitCode() {
			return unitCode;
		}

		public void setUnitCode(String unitCode) {
			this.unitCode = unitCode;
		}

		public String toString() {
			return value + " " + unitCode;
		}

	}

	private String timestamp;
	private String rawMessage;
	private String textDescription;
	private String icon;
	// private Value presentWeather; []
	private Value temperature;
	private Value dewpoint;
	private Value windDirection;
	private Value windSpeed;
	private Value windGust;
	private Value barometricPressure;
	private Value seaLevelPressure;
	private Value visibility;
	private Value maxTemperatureLast24Hours;
	private Value minTemperatureLast24Hours;
	private Value precipitationLastHour;
	private Value precipitationLast3Hours;
	private Value precipitationLast6Hours;
	private Value relativeHumidity;
	private Value windChill;
	private Value heatIndex;
	// private Value cloudLayers; []

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getRawMessage() {
		return rawMessage;
	}

	public void setRawMessage(String rawMessage) {
		this.rawMessage = rawMessage;
	}

	public String getTextDescription() {
		return textDescription;
	}

	public void setTextDescription(String textDescription) {
		this.textDescription = textDescription;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public Value getTemperature() {
		return temperature;
	}

	public void setTemperature(Value temperature) {
		this.temperature = temperature;
	}

	public Value getDewpoint() {
		return dewpoint;
	}

	public void setDewpoint(Value dewpoint) {
		this.dewpoint = dewpoint;
	}

	public Value getWindDirection() {
		return windDirection;
	}

	public void setWindDirection(Value windDirection) {
		this.windDirection = windDirection;
	}

	public Value getWindSpeed() {
		return windSpeed;
	}

	public void setWindSpeed(Value windSpeed) {
		this.windSpeed = windSpeed;
	}

	public Value getWindGust() {
		return windGust;
	}

	public void setWindGust(Value windGust) {
		this.windGust = windGust;
	}

	public Value getBarometricPressure() {
		return barometricPressure;
	}

	public void setBarometricPressure(Value barometricPressure) {
		this.barometricPressure = barometricPressure;
	}

	public Value getSeaLevelPressure() {
		return seaLevelPressure;
	}

	public void setSeaLevelPressure(Value seaLevelPressure) {
		this.seaLevelPressure = seaLevelPressure;
	}

	public Value getVisibility() {
		return visibility;
	}

	public void setVisibility(Value visibility) {
		this.visibility = visibility;
	}

	public Value getMaxTemperatureLast24Hours() {
		return maxTemperatureLast24Hours;
	}

	public void setMaxTemperatureLast24Hours(Value maxTemperatureLast24Hours) {
		this.maxTemperatureLast24Hours = maxTemperatureLast24Hours;
	}

	public Value getMinTemperatureLast24Hours() {
		return minTemperatureLast24Hours;
	}

	public void setMinTemperatureLast24Hours(Value minTemperatureLast24Hours) {
		this.minTemperatureLast24Hours = minTemperatureLast24Hours;
	}

	public Value getPrecipitationLastHour() {
		return precipitationLastHour;
	}

	public void setPrecipitationLastHour(Value precipitationLastHour) {
		this.precipitationLastHour = precipitationLastHour;
	}

	public Value getPrecipitationLast3Hours() {
		return precipitationLast3Hours;
	}

	public void setPrecipitationLast3Hours(Value precipitationLast3Hours) {
		this.precipitationLast3Hours = precipitationLast3Hours;
	}

	public Value getPrecipitationLast6Hours() {
		return precipitationLast6Hours;
	}

	public void setPrecipitationLast6Hours(Value precipitationLast6Hours) {
		this.precipitationLast6Hours = precipitationLast6Hours;
	}

	public Value getRelativeHumidity() {
		return relativeHumidity;
	}

	public void setRelativeHumidity(Value relativeHumidity) {
		this.relativeHumidity = relativeHumidity;
	}

	public Value getWindChill() {
		return windChill;
	}

	public void setWindChill(Value windChill) {
		this.windChill = windChill;
	}

	public Value getHeatIndex() {
		return heatIndex;
	}

	public void setHeatIndex(Value heatIndex) {
		this.heatIndex = heatIndex;
	}

}
