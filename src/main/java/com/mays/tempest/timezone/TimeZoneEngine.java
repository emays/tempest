package com.mays.tempest.timezone;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.zip.ZipInputStream;

import org.geojson.FeatureCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.esri.core.geometry.Envelope;
import com.fasterxml.jackson.databind.ObjectMapper;

// Based on https://github.com/RomanIakovlev/timeshape

/**
 * Class {@link TimeZoneEngine} is used to lookup the instance of
 * {@link java.time.ZoneId} based on latitude and longitude.
 */
public final class TimeZoneEngine {

	private final static Logger log = LoggerFactory.getLogger(TimeZoneEngine.class);

	private static Path dir = Paths.get("data", "timezone");

	public static void setTest() {
		dir = Paths.get("src/test/resources", "timezone");
	}

	public static final String timezone_boundary_file = "timezones-with-oceans-now.geojson.zip";

	private final TimeZoneIndex index;

	private final static double MIN_LAT = -90;
	private final static double MIN_LON = -180;
	private final static double MAX_LAT = 90;
	private final static double MAX_LON = 180;

	private TimeZoneEngine(TimeZoneIndex index) {
		this.index = index;
	}

	private static void validateCoordinates(double minLat, double minLon, double maxLat, double maxLon) {
		List<String> errors = new ArrayList<>();
		if (minLat < MIN_LAT || minLat > MAX_LAT) {
			errors.add(String.format(Locale.ROOT, "minimum latitude %f is out of range: must be -90 <= latitude <= 90;",
					minLat));
		}
		if (maxLat < MIN_LAT || maxLat > MAX_LAT) {
			errors.add(String.format(Locale.ROOT, "maximum latitude %f is out of range: must be -90 <= latitude <= 90;",
					maxLat));
		}
		if (minLon < MIN_LON || minLon > MAX_LON) {
			errors.add(String.format(Locale.ROOT,
					"minimum longitude %f is out of range: must be -180 <= longitude <= 180;", minLon));
		}
		if (maxLon < MIN_LON || maxLon > MAX_LON) {
			errors.add(String.format(Locale.ROOT,
					"maximum longitude %f is out of range: must be -180 <= longitude <= 180;", maxLon));
		}
		if (minLat > maxLat) {
			errors.add(String.format(Locale.ROOT, "maximum latitude %f is less than minimum latitude %f;", maxLat,
					minLat));
		}
		if (minLon > maxLon) {
			errors.add(String.format(Locale.ROOT, "maximum longitude %f is less than minimum longitude %f;", maxLon,
					minLon));
		}
		if (!errors.isEmpty()) {
			throw new IllegalArgumentException(String.join(" ", errors));
		}
	}

	/**
	 * Queries the {@link TimeZoneEngine} for a {@link java.time.ZoneId} based on
	 * geo coordinates.
	 *
	 * @param latitude  latitude part of query
	 * @param longitude longitude part of query
	 * @return List of all zones at given geo coordinate. Normally it's just one
	 *         zone, but for several places in the world there might be more.
	 */
	public List<ZoneId> queryAll(double latitude, double longitude) {
		return index.query(latitude, longitude);
	}

	/**
	 * Queries the {@link TimeZoneEngine} for a {@link java.time.ZoneId} based on
	 * geo coordinates.
	 *
	 * @param latitude  latitude part of query
	 * @param longitude longitude part of query
	 * @return {@code Optional<ZoneId>#of(ZoneId)} if input corresponds to some
	 *         zone, or {@link Optional#empty()} otherwise.
	 */
	public Optional<ZoneId> query(double latitude, double longitude) {
		final List<ZoneId> result = index.query(latitude, longitude);
		return result.size() > 0 ? Optional.of(result.get(0)) : Optional.empty();
	}

	/**
	 * Returns all the time zones that can be looked up.
	 *
	 * @return all the time zones that can be looked up.
	 */
	public List<ZoneId> getKnownZoneIds() {
		return index.getKnownZoneIds();
	}

	/**
	 * Creates a new instance of {@link TimeZoneEngine} and initializes it. This is
	 * a blocking long running operation. The ZipInputStream resource must be
	 * managed by the caller.
	 *
	 * @return an initialized instance of {@link TimeZoneEngine}
	 * @throws IOException
	 */
	public static TimeZoneEngine initialize(ZipInputStream zipStream) throws IOException {
		return initialize(MIN_LAT, MIN_LON, MAX_LAT, MAX_LON, zipStream);
	}

	/**
	 * Creates a new instance of {@link TimeZoneEngine} and initializes it. This is
	 * a blocking long running operation.
	 *
	 * @return an initialized instance of {@link TimeZoneEngine}
	 * @throws IOException
	 */
	public static TimeZoneEngine initialize() throws IOException {
		return initialize(MIN_LAT, MIN_LON, MAX_LAT, MAX_LON);
	}

	/**
	 * Creates a new instance of {@link TimeZoneEngine} and initializes it from a
	 * given ZipInputStream. This is a blocking long running operation. The
	 * ZipInputStream resource must be managed by the caller.
	 *
	 * @return an initialized instance of {@link TimeZoneEngine}
	 * @throws IOException
	 */
	public static TimeZoneEngine initialize(double minLat, double minLon, double maxLat, double maxLon,
			ZipInputStream zipStream) throws IOException {
		log.info("Initializing with bounding box: {}, {}, {}, {}", minLat, minLon, maxLat, maxLon);
		validateCoordinates(minLat, minLon, maxLat, maxLon);
		FeatureCollection featureCollection;
		zipStream.getNextEntry();
		featureCollection = new ObjectMapper().readValue(zipStream, FeatureCollection.class);
		Envelope boundaries = new Envelope(minLon, minLat, maxLon, maxLat);
		return new TimeZoneEngine(TimeZoneIndex.build(featureCollection, boundaries));
	}

	/**
	 * Creates a new instance of {@link TimeZoneEngine} and initializes it. This is
	 * a blocking long running operation.
	 *
	 * @return an initialized instance of {@link TimeZoneEngine}
	 * @throws IOException
	 */
	public static TimeZoneEngine initialize(double minLat, double minLon, double maxLat, double maxLon)
			throws IOException {
		Path file = dir.resolve(timezone_boundary_file);
		log.info("Initialize with: " + timezone_boundary_file + " @ " + file);
		try (InputStream input_stream = Files.newInputStream(file);
				ZipInputStream zip_stream = new ZipInputStream(input_stream)) {
			return initialize(minLat, minLon, maxLat, maxLon, zip_stream);
		}
	}

}
