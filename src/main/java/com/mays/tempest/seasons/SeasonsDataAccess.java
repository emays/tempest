package com.mays.tempest.seasons;

import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

public class SeasonsDataAccess {

	private static final Logger logger = LoggerFactory.getLogger(SeasonsDataAccess.class);

	private static Path dir = Paths.get("data", "seasons");

	public static final int START_YEAR = 1900;

	public static final int END_YEAR = 2100;

	public static boolean validYear(int year) {
		return year >= START_YEAR && year <= END_YEAR;
	}

	public static String validYearString(int year) {
		return "Year " + year + ": >= " + START_YEAR + " & <= " + END_YEAR;
	}

	public static void setTest() {
		dir = Paths.get("src/test/resources", "seasons");
	}

	private static Path getPath(int year) {
		Path path = dir.resolve(year + ".json");
		return path;
	}

	public static void writeSeasonsJsonString(int year) throws Exception {
		Path path = getPath(year);
		Files.createDirectories(path.getParent());
		String res = fetchSeasons(year);
		Files.writeString(path, res);
	}

	public static String getSeasonsJsonString(int year) throws Exception {
		Path path = getPath(year);
		if (Files.exists(path)) {
			String tides_json = Files.readString(path);
			return tides_json;
		}
		throw new FileNotFoundException(path.toString());
	}

	// https://aa.usno.navy.mil/data/index

	// https://aa.usno.navy.mil/data/Earth_Seasons

	// https://aa.usno.navy.mil/data/api#seasons

	// https://aa.usno.navy.mil/api/seasons?year=1900

	public static String fetchSeasons(int year) throws Exception {
		ClientBuilder builder = ClientBuilder.newBuilder();
		Client client = builder.build();
		String uri = "https://aa.usno.navy.mil/api/seasons";
		WebTarget target = client.target(uri);
		target = target.queryParam("year", year);
		Response resp = target.request(MediaType.APPLICATION_JSON).get();
		if (resp.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL) {
			String json = resp.readEntity(String.class);
			return json;
		}
		String message = resp.getStatus() + " " + resp.readEntity(String.class);
		logger.error(message);
		throw new Exception(message);
	}

	public static List<SeasonJson> getSeasonsJsonFromJson(String json) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		JsonNode jsonNode = mapper.readTree(json);
		String data = jsonNode.get("data").toString();
		List<SeasonJson> ret = mapper.readValue(data, new TypeReference<List<SeasonJson>>() {
		});
		return ret;
	}

	public static List<Season> getSeasonsFromJson(String json) throws Exception {
		return getSeasonsJsonFromJson(json).stream().map(Season::new).collect(Collectors.toList());
	}

	public static Seasons getSeasons(int year) throws Exception {
		String json = getSeasonsJsonString(year);
		List<Season> seasons = getSeasonsFromJson(json);
		return new Seasons(seasons);
	}

}
