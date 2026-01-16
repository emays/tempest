package com.mays.tempest.buoys;

import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BuoyDataAccess {

	private static final Logger logger = LoggerFactory.getLogger(BuoyDataAccess.class);

	private static Path dir = Paths.get("data", "buoy");

	private static boolean file_missing_exception = true;

	public static void setTest() {
		dir = Paths.get("src/test/resources", "buoys");
	}

	public static String getStationsXml() throws Exception {
		Files.createDirectories(dir);
		Path path = dir.resolve("buoy-stations.xml");
		Path stamp_path = dir.resolve("buoy-stations-stamp.txt");
		if (!Files.exists(path)) {
			logger.info("Init from : " + path.toString());
			if (file_missing_exception)
				throw new FileNotFoundException(path.toString());
			String res = BuoyStations.getStationsXmlString();
			Files.writeString(path, res);
			Files.writeString(stamp_path, ZonedDateTime.now(ZoneOffset.UTC).toString());
		}
		ZonedDateTime stamp = ZonedDateTime.parse(Files.readString(stamp_path));
		logger.info("Stamp " + stamp);
		String stations_xml = Files.readString(path);
		return stations_xml;
	}

}
