package com.mays.tempest.buoys;

import com.mays.tempest.geo.GreatCircle;

public class BuoyStationDistance {

	private double lat;

	private double lng;

	private BuoyStation station;

	private double distance;

	public BuoyStationDistance(double lat, double lng, BuoyStation station) {
		super();
		this.lat = lat;
		this.lng = lng;
		this.station = station;
		this.distance = GreatCircle.distance(lat, lng, station.getLat(), station.getLon());
	}

	public double getLat() {
		return lat;
	}

	public double getLng() {
		return lng;
	}

	public BuoyStation getStation() {
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
