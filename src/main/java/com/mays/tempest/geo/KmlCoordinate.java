package com.mays.tempest.geo;

public class KmlCoordinate extends Coordinate {

	public KmlCoordinate(String coord_str) {
		super();
		String[] coord = coord_str.split(",");
		this.longitude = Double.parseDouble(coord[0]);
		this.latitude = Double.parseDouble(coord[1]);
	}

}