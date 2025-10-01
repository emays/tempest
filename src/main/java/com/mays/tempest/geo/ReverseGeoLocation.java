package com.mays.tempest.geo;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReverseGeoLocation {

	private static final Logger logger = LoggerFactory.getLogger(ReverseGeoLocation.class);

	private static Path dir = Paths.get("data", "geonames");

	public static void setTest() {
		dir = Paths.get("src/test/resources", "geonames");
	}

	public static final String US_FILTERED = "US-filtered.txt";

//4945798	North Trurox	North Trurox		42.03344	-70.0953	P	PPL	US		MA	001	70605		0	16	14	America/New_York	2017-05-23

//4947767	Pond Village	Pond Village		42.03066	-70.09391	P	PPL	US		MA	001	70605		0	0	0	America/New_York	2017-05-23

	public static class Location {

		private String name;

		private String state;

		private double latitude;

		private double longitude;

		private int elevation;

		private String timezone;

		public Location(String name, String state, double latitude, double longitude, int elevation, String timezone) {
			super();
			this.name = name;
			this.state = state;
			this.latitude = latitude;
			this.longitude = longitude;
			this.elevation = elevation;
			this.timezone = timezone;
		}

		public String getName() {
			return name;
		}

		public String getState() {
			return state;
		}

		public double getLatitude() {
			return latitude;
		}

		public double getLongitude() {
			return longitude;
		}

		public int getElevation() {
			return elevation;
		}

		public String getTimezone() {
			return timezone;
		}

		public String toString() {
			return name + ", " + state + " (" + latitude + ", " + longitude + ", " + elevation + ") " + timezone;
		}

		private String toLine() {
			return name + "\t" + state + "\t" + latitude + "\t" + longitude + "\t" + elevation + "\t" + timezone;
		}

		private static Location fromLine(String line) {
			String[] fields = line.split("\\t");
			return new Location(fields[0], fields[1], Double.parseDouble(fields[2]), Double.parseDouble(fields[3]),
					Integer.parseInt(fields[4]), fields[5]);
		}

	}

	private static ReverseGeoLocation instance;

	private List<Location> locations;

	public static synchronized ReverseGeoLocation getInstance() throws Exception {
		if (instance == null) {
			instance = new ReverseGeoLocation();
			Path path = dir.resolve(US_FILTERED);
			instance.read(path);
		}
		return instance;
	}

	public List<Location> getLocations() {
		return locations;
	}

	public static int filter(Path file, Path out_file) throws Exception {
		logger.info("Read");
		long cnt = Files.lines(file).count();
		logger.info("Filter");
		ContiguousUS cu = new ContiguousUS();
		List<String> locations = Files.lines(file) //
				.map(line -> line.split("\\t"))
				// Populated place
				.filter(fields -> fields[7].equals("PPL"))
				// Population > 0
				.filter(fields -> Integer.parseInt(fields[14]) > 0)
				.map(fields -> new Location(fields[1], fields[10], Double.parseDouble(fields[4]),
						Double.parseDouble(fields[5]), (fields[15].isEmpty() ? -1 : Integer.parseInt(fields[15])),
						fields[17]))
				.filter(x -> cu.inBounds(new Coordinate(x.getLatitude(), x.getLongitude()))).map(x -> x.toLine())
				.toList();
		logger.info(String.format("Filtered %,d of %,d", locations.size(), cnt));
		Files.write(out_file, locations);
		return locations.size();
	}

	private void read(Path file) throws Exception {
		logger.info("Read");
		locations = Files.lines(file).map(line -> Location.fromLine(line)).toList();
		logger.info(String.format("Read %,d", locations.size()));
	}

	public Location getNearest(double latitude, double longtitude) {
		Location nearest = null;
		double nearest_distance = Double.MAX_VALUE;
		for (Location location : locations) {
			double distance = GreatCircle.distance(latitude, longtitude, location.getLatitude(),
					location.getLongitude());
			if (distance < nearest_distance) {
				nearest = location;
				nearest_distance = distance;
			}
		}
		return nearest;
	}

}
