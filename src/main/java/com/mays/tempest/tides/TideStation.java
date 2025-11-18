package com.mays.tempest.tides;

import java.time.ZoneId;

import com.mays.tempest.TimeZoneUtil;
import com.mays.tempest.geo.Coordinate;
import com.mays.tempest.geo.KmlPointDisplay;

public class TideStation implements KmlPointDisplay {

	// state The state or region code that the station resides within.

	private String state;

	// type R for reference stations, S for subordinate stations.
	
	private String type;

	// reference_id A unique alphanumeric ID referencing a subordinate
	// station's reference station, if applicable
	
	private String reference;

	// id A unique alphanumeric ID specific to this station.

	private String id;

	// name The name of the station

	private String name;

	// lat Latitude in decimal format

	private double lat;

	// lng Longitude in decimal format

	private double lng;

	public TideStation(TideStationJson sj) {
		super();
		this.state = sj.getState();
		this.type = sj.getType();
		this.reference = sj.getReference();
		this.id = sj.getId();
		this.name = sj.getName();
		this.lat = sj.getLat();
		this.lng = sj.getLng();
	}

	public String getState() {
		return state;
	}

	public String getType() {
		return type;
	}

	public String getReference() {
		return reference;
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
