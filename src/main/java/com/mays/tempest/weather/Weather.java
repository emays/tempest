package com.mays.tempest.weather;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
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
import com.fasterxml.jackson.databind.node.ObjectNode;

public class Weather {

	private static final Logger logger = LoggerFactory.getLogger(Weather.class);

	private static final boolean trace = false;

	// https://www.weather.gov/documentation/services-web-api

	// https://weather-gov.github.io/api/

	// https://api.weather.gov/points/42.028,-70.0848

	public String getPointsJsonString(double lat, double lon) throws Exception {
		ClientBuilder builder = ClientBuilder.newBuilder();
		Client client = builder.build();
		String uri = "https://api.weather.gov/points/" + String.format("%.4f", lat) + "," + String.format("%.4f", lon);
		if (trace)
			logger.info(uri);
		WebTarget target = client.target(uri);
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

	public WeatherPointsJson getPointsJsonFromJson(String json) throws Exception {
		if (trace)
			logger.info("\n" + json);
		ObjectMapper mapper = new ObjectMapper();
		JsonNode jsonNode = mapper.readTree(json);
		String properties = jsonNode.get("properties").toString();
		if (trace)
			logger.info("\n" + properties);
		WeatherPointsJson ret = mapper.readValue(properties, new TypeReference<WeatherPointsJson>() {
		});
		return ret;
	}

	public WeatherPointsJson getPointsJson(double lat, double lon) throws Exception {
		String json = getPointsJsonString(lat, lon);
		return getPointsJsonFromJson(json);
	}

	public String getPointJsonString(String url) throws Exception {
		return getPointJsonString(url, 5);
	}

	private String getPointJsonString(String url, int attempts) throws Exception {
		ClientBuilder builder = ClientBuilder.newBuilder();
		Client client = builder.build();
		String uri = url;
		if (trace)
			logger.info(uri);
		WebTarget target = client.target(uri);
		Response resp = target.request(MediaType.APPLICATION_JSON).get();
		if (resp.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL) {
			String json = resp.readEntity(String.class);
			if (trace)
				logger.info("\n" + json);
			return json;
		}
		String message = resp.getStatus() + " " + resp.readEntity(String.class);
		logger.error(message);
		if (attempts > 0) {
			Thread.sleep(10 * 1000);
			return getPointJsonString(url, attempts - 1);
		}
		throw new Exception(message);
	}

	// https://api.weather.gov/gridpoints/BOX/106,67/stations

	public List<WeatherObservationStationJson> getObservationStationsJsonFromJson(String json) throws Exception {
		ArrayList<WeatherObservationStationJson> ret = new ArrayList<>();
		if (trace)
			logger.info("\n" + json);
		ObjectMapper mapper = new ObjectMapper();
		JsonNode jsonNode = mapper.readTree(json);
		JsonNode features = jsonNode.get("features");
		for (JsonNode feature : features) {
//			logger.info(feature.toString());
			JsonNode coordinates = feature.findValue("coordinates");
			JsonNode properties = feature.findValue("properties");
			if (coordinates != null) {
				if (trace)
					logger.info(coordinates.toString());
				((ObjectNode) properties).set("coordinates", coordinates);
			}
			if (trace)
				logger.info("\n" + properties);
			WeatherObservationStationJson obs = mapper.readValue(properties.toString(),
					new TypeReference<WeatherObservationStationJson>() {
					});
			ret.add(obs);
		}
		return ret;
	}

	public List<WeatherObservationStation> getObservationStations(String url) throws Exception {
		String json = getPointJsonString(url);
		return getObservationStationsJsonFromJson(json).stream().map(WeatherObservationStation::new)
				.collect(Collectors.toList());
	}

	public List<WeatherObservationStation> getObservationStations(double lat, double lng) throws Exception {
		WeatherPointsJson points = getPointsJson(lat, lng);
		return getObservationStations(points.getObservationStations());
	}

	public List<WeatherObservationStationDistance> getNearest(double lat, double lng, int nm_limit, int num_results)
			throws Exception {
		List<WeatherObservationStation> stations = getObservationStations(lat, lng);
		logger.info("stations: " + stations.size());
		return stations.stream().map(x -> new WeatherObservationStationDistance(lat, lng, x)) //
				.filter(x -> x.getDistance() <= nm_limit) //
				.sorted(Comparator.comparingDouble(x -> x.getDistance())) //
				.limit(num_results).collect(Collectors.toList());
	}

	// https://api.weather.gov/stations/KPVC/observations

	public String getObservationsJsonString(String id) throws Exception {
		ClientBuilder builder = ClientBuilder.newBuilder();
		Client client = builder.build();
		String uri = "https://api.weather.gov/stations/" + id + "/observations";
		if (trace)
			logger.info(uri);
		WebTarget target = client.target(uri);
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

	public List<WeatherObservationJson> getObservationsJsonFromJson(String json) throws Exception {
		ArrayList<WeatherObservationJson> ret = new ArrayList<>();
		if (trace)
			logger.info("\n" + json);
		ObjectMapper mapper = new ObjectMapper();
		JsonNode jsonNode = mapper.readTree(json);
		JsonNode features = jsonNode.get("features");
		for (JsonNode feature : features) {
//			logger.info(feature.toString());
			String properties = feature.get("properties").toString();
			if (trace)
				logger.info("\n" + properties);
			WeatherObservationJson obs = mapper.readValue(properties, new TypeReference<WeatherObservationJson>() {
			});
			ret.add(obs);
		}
		return ret;
	}

	public List<WeatherObservation> getObservations(String id) throws Exception {
		String json = getObservationsJsonString(id);
		return getObservationsFromJson(json);
	}

	public List<WeatherObservation> getObservationsFromJson(String json) throws Exception {
		return getObservationsJsonFromJson(json).stream().map(WeatherObservation::new)
				.sorted(Comparator.comparing(WeatherObservation::getTime).reversed()).collect(Collectors.toList());
	}

	// https://api.weather.gov/stations/KPVC

	public String getObservationStationJsonString(String id) throws Exception {
		ClientBuilder builder = ClientBuilder.newBuilder();
		Client client = builder.build();
		String uri = "https://api.weather.gov/stations/" + id;
		if (trace)
			logger.info(uri);
		WebTarget target = client.target(uri);
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

	public WeatherObservationStationJson getObservationStationJsonFromJson(String json) throws Exception {
		if (trace)
			logger.info("\n" + json);
		ObjectMapper mapper = new ObjectMapper();
		JsonNode jsonNode = mapper.readTree(json);
		JsonNode coordinates = jsonNode.findValue("coordinates");
		JsonNode properties = jsonNode.findValue("properties");
		if (coordinates != null) {
			if (trace)
				logger.info(coordinates.toString());
			((ObjectNode) properties).set("coordinates", coordinates);
		}
		if (trace)
			logger.info("\n" + properties);
		WeatherObservationStationJson obs = mapper.readValue(properties.toString(),
				new TypeReference<WeatherObservationStationJson>() {
				});
		return obs;
	}

	public WeatherObservationStation getObservationStation(String id) throws Exception {
		String json = getObservationStationJsonString(id);
		return new WeatherObservationStation(getObservationStationJsonFromJson(json));
	}

	// https://api.weather.gov/gridpoints/BOX/106,67

	public WeatherForecastGridDataJson getForecastGridDataJsonFromJson(String json) throws Exception {
		if (trace)
			logger.info("\n" + json);
		ObjectMapper mapper = new ObjectMapper();
		JsonNode jsonNode = mapper.readTree(json);
		JsonNode properties = jsonNode.get("properties");
		if (trace)
			logger.info("\n" + properties);
		WeatherForecastGridDataJson ret = mapper.readValue(properties.toString(),
				new TypeReference<WeatherForecastGridDataJson>() {
				});
		return ret;
	}

	public WeatherForecastGridData getForecastGridData(WeatherPointsJson points) throws Exception {
		String url = points.getForecastGridData();
		String json = getPointJsonString(url);
		return new WeatherForecastGridData(getForecastGridDataJsonFromJson(json));
	}

	public WeatherForecastGridData getForecastGridData(double lat, double lng) throws Exception {
		WeatherPointsJson points = getPointsJson(lat, lng);
		return getForecastGridData(points);
	}

	// https://api.weather.gov/gridpoints/BOX/106,67/forecast

	public WeatherForecastGeneralJson getForecastGeneralJsonFromJson(String json) throws Exception {
		if (trace)
			logger.info("\n" + json);
		ObjectMapper mapper = new ObjectMapper();
		JsonNode jsonNode = mapper.readTree(json);
		JsonNode properties = jsonNode.get("properties");
		if (trace)
			logger.info("\n" + properties);
		WeatherForecastGeneralJson ret = mapper.readValue(properties.toString(),
				new TypeReference<WeatherForecastGeneralJson>() {
				});
		return ret;
	}

	public WeatherForecastGeneral getForecastGeneral(WeatherPointsJson points) throws Exception {
		String url = points.getForecast();
		String json = getPointJsonString(url);
		return new WeatherForecastGeneral(getForecastGeneralJsonFromJson(json));
	}

	public WeatherForecastGeneral getForecastGeneral(double lat, double lng) throws Exception {
		WeatherPointsJson points = getPointsJson(lat, lng);
		return getForecastGeneral(points);
	}

	public WeatherForecast getForecastDetailed(double lat, double lng) throws Exception {
		WeatherPointsJson points = getPointsJson(lat, lng);
		WeatherForecastGridData wfgd = getForecastGridData(points);
		return new WeatherForecast(null, wfgd);
	}

	public WeatherForecast getForecast(WeatherPointsJson points) throws Exception {
		WeatherForecastGeneral wfg = getForecastGeneral(points);
		WeatherForecastGridData wfgd = getForecastGridData(points);
		return new WeatherForecast(wfg, wfgd);
	}

	public WeatherForecast getForecast(double lat, double lng) throws Exception {
		WeatherPointsJson points = getPointsJson(lat, lng);
		return getForecast(points);
	}

}
