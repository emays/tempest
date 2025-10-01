package com.mays.tempest.tides;

import java.time.ZoneId;

import com.mays.tempest.TimeZoneUtil;
import com.mays.tempest.geo.Coordinate;
import com.mays.tempest.geo.KmlPointDisplay;

public class TideStation implements KmlPointDisplay {

	private String state;

	private String id;

	private String name;

	private double lat;

	private double lng;

	public TideStation(TideStationJson sj) {
		super();
		this.state = sj.getState();
		this.id = sj.getId();
		this.name = sj.getName();
		this.lat = sj.getLat();
		this.lng = sj.getLng();
	}

	public String getState() {
		return state;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public double getLat() {
		return lat;
	}

	public double getLng() {
		return lng;
	}

	public Coordinate getCoordinate() {
		return new Coordinate(lat, lng);
	}

	public ZoneId getTimeZone() {
		return TimeZoneUtil.getInstance().getTimeZone(getLat(), getLng());
	}

	public String toString() {
		return state + " " + id + " " + name + " " + String.format("%.2f", lat) + " " + String.format("%.2f", lng);
	}

	@Override
	public String getKmlName() {
		return getName();
	}

	@Override
	public String getKmlDescription() {
		return getName() + " " + getState() + " (" + getId() + ")";
	}

	@Override
	public Coordinate getKmlCoordinate() {
		return new Coordinate(lat, lng);
	}

	@Override
	public String getKmlUrl() {
		return "http://localhost/tides/tides?id=8446121";
	}

	@Override
	public String getKmlAnchorText() {
		return "Tides";
	}

}
