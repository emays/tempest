package com.mays.tempest.weather;

import java.time.ZoneId;

import javax.measure.Quantity;
import javax.measure.quantity.Length;

import com.mays.tempest.geo.Coordinate;

import systems.uom.common.USCustomary;

public class WeatherObservationStation {

	private Coordinate coordinate;

	private Quantity<Length> elevation;

	private String stationIdentifier;

	private String name;

	private ZoneId timeZone;

	private String forecast;

	private String county;

	private String fireWeatherZone;

	public WeatherObservationStation(WeatherObservationStationJson json) {
		this.coordinate = new Coordinate(json.getCoordinates()[1], json.getCoordinates()[0]);
		this.elevation = WeatherQuantities.toLength(json.getElevation());
		this.stationIdentifier = json.getStationIdentifier();
		this.name = json.getName();
		this.timeZone = ZoneId.of(json.getTimeZone());
		this.forecast = json.getForecast();
		this.county = json.getCounty();
		this.fireWeatherZone = json.getFireWeatherZone();
	}

	public Coordinate getCoordinate() {
		return coordinate;
	}

	public Quantity<Length> getElevation() {
		return elevation;
	}

	public String getStationIdentifier() {
		return stationIdentifier;
	}

	public String getName() {
		return name;
	}

	public ZoneId getTimeZone() {
		return timeZone;
	}

	public String getForecast() {
		return forecast;
	}

	public String getCounty() {
		return county;
	}

	public String getFireWeatherZone() {
		return fireWeatherZone;
	}

	public String toString() {
		return getStationIdentifier() + " " + String.format("%.2f", coordinate.getLatitude()) + " "
				+ String.format("%.2f", coordinate.getLongitude()) + " "
				+ String.format("%.0f", elevation.to(USCustomary.FOOT).getValue().doubleValue()) + " " + "ft";
	}

}
