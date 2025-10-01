package com.mays.tempest.weather;

import com.mays.tempest.geo.GreatCircle;

public class WeatherObservationStationDistance {

	private double lat;

	private double lng;

	private WeatherObservationStation station;

	private double distance;

	public WeatherObservationStationDistance(double lat, double lng, WeatherObservationStation station) {
		super();
		this.lat = lat;
		this.lng = lng;
		this.station = station;
		this.distance = GreatCircle.distance(lat, lng, station.getCoordinate().getLatitude(),
				station.getCoordinate().getLongitude());
	}

	public double getLat() {
		return lat;
	}

	public double getLng() {
		return lng;
	}

	public WeatherObservationStation getStation() {
		return station;
	}

	public double getDistance() {
		return distance;
	}

	public String toString() {
		return String.format("%.2f", distance) + " NM [" + String.format("%.2f", lat) + ", "
				+ String.format("%.2f", lng) + "] " + station;
	}

}
