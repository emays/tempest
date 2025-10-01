package com.mays.tempest.buoys;

import java.time.ZoneId;

import com.mays.tempest.TimeZoneUtil;
import com.mays.tempest.geo.Coordinate;
import com.mays.tempest.geo.KmlPointDisplay;

public class BuoyStation implements KmlPointDisplay {

	private String id;

	private double lat;

	private double lon;

	private String name;

	public BuoyStation(String id, double lat, double lon, String name) {
		super();
		this.id = id;
		this.lat = lat;
		this.lon = lon;
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public double getLat() {
		return lat;
	}

	public double getLon() {
		return lon;
	}

	public Coordinate getCoordinate() {
		return new Coordinate(lat, lon);
	}
	
	public ZoneId getTimeZone() {
		return TimeZoneUtil.getInstance().getTimeZone(getLat(), getLon());
	}

	public String getName() {
		return name;
	}

	@Override
	public String getKmlName() {
		return getName();
	}

	@Override
	public String getKmlDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Coordinate getKmlCoordinate() {
		return new Coordinate(lat, lon);
	}

	@Override
	public String getKmlUrl() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getKmlAnchorText() {
		// TODO Auto-generated method stub
		return null;
	}

}
