package com.mays.tempest.sunmoon;

import java.io.FileNotFoundException;
import java.io.IOException;
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

public class PhaseXDataAccess {

	private static final Logger logger = LoggerFactory.getLogger(PhaseXDataAccess.class);

	private static Path getPath(int year) {
		Path dir = Paths.get("src/test/resources", "sunmoon", "phases");
		Path path = dir.resolve(year + ".json");
		return path;
	}

	public static void writePhasesJsonString(int year) throws IOException {
		Path path = getPath(year);
		Files.createDirectories(path.getParent());
		String res = fetchPhases(year);
		Files.writeString(path, res);
	}

	public static String getPhasesJsonString(int year) throws IOException {
		Path path = getPath(year);
		if (Files.exists(path)) {
			String json = Files.readString(path);
			return json;
		}
		throw new FileNotFoundException(path.toString());
	}

	// https://aa.usno.navy.mil/data/api#phase

	// https://aa.usno.navy.mil/api/moon/phases/year?year=1900

	public static String fetchPhases(int year) throws IOException {
		ClientBuilder builder = ClientBuilder.newBuilder();
		Client client = builder.build();
		String uri = "https://aa.usno.navy.mil/api/moon/phases/year";
		WebTarget target = client.target(uri);
		target = target.queryParam("year", year);
		Response resp = target.request(MediaType.APPLICATION_JSON).get();
		if (resp.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL) {
			String json = resp.readEntity(String.class);
			return json;
		}
		String message = resp.getStatus() + " " + resp.readEntity(String.class);
		logger.error(message);
		throw new IOException(message);
	}

	public static List<PhaseXJson> getPhasesJsonFromJson(String json) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		JsonNode jsonNode = mapper.readTree(json);
		String data = jsonNode.get("phasedata").toString();
		List<PhaseXJson> ret = mapper.readValue(data, new TypeReference<List<PhaseXJson>>() {
		});
		return ret;
	}

	public static List<PhaseX> getPhasesFromJson(String json) throws Exception {
		return getPhasesJsonFromJson(json).stream().map(PhaseX::new).collect(Collectors.toList());
	}

	public static List<PhaseX> getPhases(int year) throws Exception {
		String json = getPhasesJsonString(year);
		return getPhasesFromJson(json);
	}

}
