package com.mays.tempest.timezone;

import java.time.ZoneId;
import java.time.zone.ZoneRulesException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.geojson.Feature;
import org.geojson.FeatureCollection;
import org.geojson.GeoJsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Envelope2D;
import com.esri.core.geometry.GeoJsonImportFlags;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.OperatorImportFromGeoJson;
import com.esri.core.geometry.OperatorIntersects;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polygon;
import com.esri.core.geometry.QuadTree;
import com.esri.core.geometry.SpatialReference;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

// Based on https://github.com/RomanIakovlev/timeshape

final class TimeZoneIndex {

	private static final Logger logger = LoggerFactory.getLogger(TimeZoneIndex.class);

	private static record ZoneGeometry(ZoneId zoneId, Geometry geometry) {
	}

	private static final int WGS84_WKID = 4326;

	private static final SpatialReference spatialReference = SpatialReference.create(WGS84_WKID);

	private final QuadTree quadTree = createQuadTree();

	private final ArrayList<ZoneGeometry> zones = new ArrayList<>();

	private final ArrayList<ZoneId> outOfBoundsZones = new ArrayList<>();

	private final ArrayList<String> unknownZones = new ArrayList<>();

	public ArrayList<ZoneId> getOutOfBoundsZones() {
		return outOfBoundsZones;
	}

	public ArrayList<String> getUnknownZones() {
		return unknownZones;
	}

	private TimeZoneIndex() {
	}

	List<ZoneId> getKnownZoneIds() {
		return zones.stream().map(e -> e.zoneId).collect(Collectors.toList());
	}

	List<ZoneId> query(double latitude, double longitude) {
		ArrayList<ZoneId> result = new ArrayList<>();
		Point point = new Point(longitude, latitude);
		OperatorIntersects operator = OperatorIntersects.local();
		QuadTree.QuadTreeIterator iterator = quadTree.getIterator(point, 0);
		for (int i = iterator.next(); i >= 0; i = iterator.next()) {
			int element = quadTree.getElement(i);
			ZoneGeometry entry = zones.get(element);
			if (operator.execute(entry.geometry, point, spatialReference, null)) {
				result.add(entry.zoneId);
			}
		}
		return result;
	}

	private static Polygon toPolygon(GeoJsonObject geoJsonObject) {
		ObjectMapper mapper = new ObjectMapper();
		String str = null;
		try {
			str = mapper.writeValueAsString(geoJsonObject);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
		Geometry geo = OperatorImportFromGeoJson.local()
				.execute(GeoJsonImportFlags.geoJsonImportDefaults, Geometry.Type.Polygon, str, null).getGeometry();
		if (geo instanceof Polygon)
			return (Polygon) geo;
		throw new IllegalArgumentException("Unexpected value: " + geo.getClass());
	}

	private static Polygon getPolygon(Feature feature) {
		GeoJsonObject geoJsonObject = feature.getGeometry();
		Polygon polygon;
		switch (geoJsonObject) {
		case org.geojson.Polygon _ -> {
			polygon = toPolygon(geoJsonObject);
		}
		case org.geojson.MultiPolygon _ -> {
			polygon = toPolygon(geoJsonObject);
		}
		default -> throw new IllegalArgumentException("Unexpected value: " + geoJsonObject);
		}
		return polygon;
	}

	private static QuadTree createQuadTree() {
		// Some of the time zones may contain areas outside the boundaries. So set up
		// the quad tree to be universal. Otherwise the quad tree may not contain some
		// time zones.
		Envelope boundariesEnvelop = new Envelope(-180, -90, 180, 90);
		Envelope2D boundariesEnvelope2D = new Envelope2D();
		boundariesEnvelop.queryEnvelope2D(boundariesEnvelope2D);
		return new QuadTree(boundariesEnvelope2D, 8);
	}

	static TimeZoneIndex build(FeatureCollection featureCollection, Envelope boundaries) {
		TimeZoneIndex tzi = new TimeZoneIndex();
		tzi.builder(featureCollection, boundaries);
		return tzi;
	}

	private ZoneId getZoneId(Feature feature) {
		String tzid = feature.getProperty("tzid");
		try {
			return ZoneId.of(tzid);
		} catch (ZoneRulesException ex) {
			unknownZones.add(tzid);
		}
		return null;
	}

	private void builder(FeatureCollection featureCollection, Envelope boundaries) {
		int index = 0;
		for (Feature feature : featureCollection.getFeatures()) {
			ZoneId zoneId = getZoneId(feature);
			if (zoneId == null)
				continue;
			Polygon polygon = getPolygon(feature);
			if (OperatorIntersects.local().execute(boundaries, polygon, spatialReference, null)) {
				logger.debug("Adding zone {} to index", zoneId);
				Envelope2D env = new Envelope2D();
				polygon.queryEnvelope2D(env);
				quadTree.insert(index, env);
				zones.add(index, new ZoneGeometry(zoneId, polygon));
				index++;
			} else {
				outOfBoundsZones.add(zoneId);
			}
		}
		if (unknownZones.size() != 0) {
			String allUnknownZones = String.join(", ", unknownZones);
			logger.error("Some of the zone ids were not recognized by the Java runtime and will be ignored. "
					+ "The most probable reason for this is outdated Java runtime version. "
					+ "The following zones were not recognized: " + allUnknownZones);
		}
	}

}
