package com.mays.tempest.tides;

import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TideDataAccess {

	private static final Logger logger = LoggerFactory.getLogger(TideDataAccess.class);

	private static Path dir = Paths.get("data", "tide");

	private static boolean file_missing_exception = true;

	public static void setTest() {
		dir = Paths.get("src/test/resources", "tides");
	}

	public static String getStationsJsonString() throws Exception {
		Files.createDirectories(dir);
		Path path = dir.resolve("tide-stations.json");
		Path stamp_path = dir.resolve("tide-stations-stamp.txt");
		if (!Files.exists(path)) {
			logger.info("Init from : " + path.toString());
			if (file_missing_exception)
				throw new FileNotFoundException(path.toString());
			String res = TideStations.getStationsJsonString();
			Files.writeString(path, res);
			Files.writeString(stamp_path, ZonedDateTime.now(ZoneId.of("UTC")).toString());
		}
		ZonedDateTime stamp = ZonedDateTime.parse(Files.readString(stamp_path));
		logger.info("Stamp " + stamp);
		String stations_json = Files.readString(path);
		return stations_json;
	}

	private static Path getPath(String station) {
		return dir.resolve(station);
	}

	private static Path getPath(String station, int year, int month) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMM");
		String file = LocalDate.of(year, month, 1).format(dtf) + ".json";
		Path path = getPath(station).resolve(file);
		return path;
	}

	public static void writeTidesJsonString(String station, int year, int month) throws Exception {
		Path path = getPath(station, year, month);
		Files.createDirectories(path.getParent());
		String res = Tides.getTidesJsonString(station, year, month);
		Files.writeString(path, res);
	}

	private static Path getDatumsPath(String station) {
		Path path = getPath(station).resolve("datums.json");
		return path;
	}

	public static String getTidesJsonString(String station, int year, int month, boolean fme) throws Exception {
		Path path = getPath(station, year, month);
		if (Files.exists(path)) {
			String tides_json = Files.readString(path);
			return tides_json;
		}
		if (fme)
			throw new FileNotFoundException(path.toString());
		return null;
	}

	public static void writeDatumsJsonString(String station) throws Exception {
		Path path = getDatumsPath(station);
		Files.createDirectories(path.getParent());
		String res = Tides.getDatumsJsonString(station);
		Files.writeString(path, res);
	}

	public static String getDatumsJsonString(String station, boolean fme) throws Exception {
		Path path = getDatumsPath(station);
		if (Files.exists(path)) {
			String tides_json = Files.readString(path);
			return tides_json;
		}
		if (fme)
			throw new FileNotFoundException(path.toString());
		return null;
	}

	private static Path getStartEndPath(String station) {
		Path path = getPath(station).resolve("start-end.txt");
		return path;
	}

	public static void writeStartEnd(String station, LocalDate start, LocalDate end) throws Exception {
		List<String> lines = List.of(start.toString(), end.toString());
		Files.write(getStartEndPath(station), lines);
	}

	public record StartEnd(LocalDate start, LocalDate end) {
	}

	public static StartEnd getStartEnd(String station) throws Exception {
		List<String> lines = Files.readAllLines(getStartEndPath(station));
		return new StartEnd(LocalDate.parse(lines.get(0)), LocalDate.parse(lines.get(1)));
	}

}
