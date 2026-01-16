package com.mays.tempest.tides;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TideStations {

	private static final Logger logger = LoggerFactory.getLogger(TideStations.class);

	private static final boolean trace = false;

	private static TideStations instance;

	private final List<String> skipStates = List.of("", "AK", "HI", "PR", "VI");

	private List<TideStation> stations;

	private TreeMap<String, List<TideStation>> states = new TreeMap<>();;

	private HashMap<String, TideStation> id_map = new HashMap<>();

	public static synchronized TideStations getInstance() throws Exception {
		if (instance == null) {
			instance = new TideStations();
			instance.init();
		}
		return instance;
	}

	private void init() throws Exception {
		String stationsJson = TideDataAccess.getStationsJsonString();
		stations = getStationsFromJson(stationsJson).stream() //
				.filter(x -> !skipStates.contains(x.getState())) //
				.collect(Collectors.toList());
		for (TideStation station : stations) {
			states.putIfAbsent(station.getState(), new ArrayList<TideStation>());
			states.get(station.getState()).add(station);
			id_map.put(station.getId(), station);
		}
		states.values().forEach(x -> Collections.sort(x, Comparator.comparing(TideStation::getName)));
	}

	public List<TideStation> getStations() {
		return stations;
	}

	public List<String> getStates() {
		return new ArrayList<>(states.keySet());
	}

	public TideStation getStation(String id) {
		return id_map.get(id);
	}

	public List<TideStation> getState(String state) {
		return states.getOrDefault(state, List.of());
	}

	public List<TideStationDistance> getNearest(double lat, double lng, int nm_limit, int num_results) {
		return stations.stream().map(x -> new TideStationDistance(lat, lng, x)) //
				.filter(x -> x.getDistance() <= nm_limit) //
				.sorted(Comparator.comparingDouble(x -> x.getDistance())) //
				.limit(num_results).collect(Collectors.toList());
	}

	public TideStationDistance getNearest(double lat, double lng) {
		return getNearest(lat, lng, Integer.MAX_VALUE, 1).get(0);
	}

	public static String getStationsJsonString() throws Exception {
		ClientBuilder builder = ClientBuilder.newBuilder();
		Client client = builder.build();
		String uri = "https://api.tidesandcurrents.noaa.gov/mdapi/prod/webapi/stations.json";
		WebTarget target = client.target(uri);
		target = target.queryParam("type", "tidepredictions");
		Response resp = target.request(MediaType.APPLICATION_JSON).get();
		if (resp.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL) {
			String json = resp.readEntity(String.class);
			if (trace)
				logger.info("\n" + json);
			return json;
		}
		String message = resp.getStatus() + " " + resp.readEntity(String.class);
		logger.error(message);
		throw new Exception(message);
	}

	public static List<TideStationJson> getStationsJsonFromJson(String json) throws Exception {
		if (trace)
			logger.info("\n" + json);
		ObjectMapper mapper = new ObjectMapper();
		JsonNode jsonNode = mapper.readTree(json);
		String stations = jsonNode.get("stations").toString();
		if (trace)
			logger.info("\n" + stations);
		List<TideStationJson> ret = mapper.readValue(stations, new TypeReference<List<TideStationJson>>() {
		});
		return ret;
	}

	public static List<TideStationJson> getStationsJson() throws Exception {
		String json = getStationsJsonString();
		return getStationsJsonFromJson(json);
	}

	public static List<TideStation> getStationsFromJson(String json) throws Exception {
		return getStationsJsonFromJson(json).stream().map(TideStation::new).collect(Collectors.toList());
	}

//	public List<TideStation> getStations() throws Exception {
//		String json = getStationsJsonString();
//		return getStationsFromJson(json);
//	}

}
