package com.mays.tempest.tides;

import com.mays.tempest.geo.GreatCircle;

public class TideStationDistance {

	private double lat;

	private double lng;

	private TideStation station;

	private double distance;

	public TideStationDistance(double lat, double lng, TideStation station) {
		super();
		this.lat = lat;
		this.lng = lng;
		this.station = station;
		this.distance = GreatCircle.distance(lat, lng, station.getLat(), station.getLng());
	}

	public double getLat() {
		return lat;
	}

	public double getLng() {
		return lng;
	}

	public TideStation getStation() {
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
