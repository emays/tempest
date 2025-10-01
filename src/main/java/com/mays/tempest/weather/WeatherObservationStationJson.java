package com.mays.tempest.weather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherObservationStationJson {

	private double[] coordinates;

	private WeatherObservationJson.Value elevation;

	private String stationIdentifier;

	private String name;

	private String timeZone;

	private String forecast;

	private String county;

	private String fireWeatherZone;

	public double[] getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(double[] coordinates) {
		this.coordinates = coordinates;
	}

	public WeatherObservationJson.Value getElevation() {
		return elevation;
	}

	public void setElevation(WeatherObservationJson.Value elevation) {
		this.elevation = elevation;
	}

	public String getStationIdentifier() {
		return stationIdentifier;
	}

	public void setStationIdentifier(String stationIdentifier) {
		this.stationIdentifier = stationIdentifier;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}

	public String getForecast() {
		return forecast;
	}

	public void setForecast(String forecast) {
		this.forecast = forecast;
	}

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public String getFireWeatherZone() {
		return fireWeatherZone;
	}

	public void setFireWeatherZone(String fireWeatherZone) {
		this.fireWeatherZone = fireWeatherZone;
	}

}
