package com.mays.tempest.weather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherPointsJson {

	private String forecastOffice;

	private String gridId;

	private String gridX;

	private String gridY;

	private String forecast;

	private String forecastHourly;

	private String forecastGridData;

	private String observationStations;

	private String forecastZone;

	private String county;

	private String fireWeatherZone;

	private String timeZone;

	private String radarStation;

	public String getForecastOffice() {
		return forecastOffice;
	}

	public void setForecastOffice(String forecastOffice) {
		this.forecastOffice = forecastOffice;
	}

	public String getGridId() {
		return gridId;
	}

	public void setGridId(String gridId) {
		this.gridId = gridId;
	}

	public String getGridX() {
		return gridX;
	}

	public void setGridX(String gridX) {
		this.gridX = gridX;
	}

	public String getGridY() {
		return gridY;
	}

	public void setGridY(String gridY) {
		this.gridY = gridY;
	}

	public String getForecast() {
		return forecast;
	}

	public void setForecast(String forecast) {
		this.forecast = forecast;
	}

	public String getForecastHourly() {
		return forecastHourly;
	}

	public void setForecastHourly(String forecastHourly) {
		this.forecastHourly = forecastHourly;
	}

	public String getForecastGridData() {
		return forecastGridData;
	}

	public void setForecastGridData(String forecastGridData) {
		this.forecastGridData = forecastGridData;
	}

	public String getObservationStations() {
		return observationStations;
	}

	public void setObservationStations(String observationStations) {
		this.observationStations = observationStations;
	}

	public String getForecastZone() {
		return forecastZone;
	}

	public void setForecastZone(String forecastZone) {
		this.forecastZone = forecastZone;
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

	public String getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}

	public String getRadarStation() {
		return radarStation;
	}

	public void setRadarStation(String radarStation) {
		this.radarStation = radarStation;
	}
	
	

}
