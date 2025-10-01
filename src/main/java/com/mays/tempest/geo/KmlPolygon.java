package com.mays.tempest.geo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class KmlPolygon {

	private List<Coordinate> coordinates;

	public KmlPolygon(String coordinates) {
		this.coordinates = Arrays.stream(coordinates.split(" ")).map(KmlCoordinate::new)
				.collect(Collectors.toCollection(ArrayList::new));
	}

	public List<Coordinate> getCoordinates() {
		return coordinates;
	}

}
