package com.mays.tempest.buoys;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BuoyStations {

	private static final Logger logger = LoggerFactory.getLogger(BuoyStations.class);

	private static final boolean trace = false;

	private static BuoyStations instance;;

	private List<BuoyStation> stations;

	private HashMap<String, BuoyStation> id_map = new HashMap<>();

	public static synchronized BuoyStations getInstance() throws Exception {
		if (instance == null) {
			instance = new BuoyStations();
			instance.init();
		}
		return instance;
	}

	public List<BuoyStation> getStations() {
		return stations;
	}

	private void init() throws Exception {
		String stations_xml = BuoyDataAccess.getStationsXml();
		stations = getStationsFromXml(stations_xml).stream() //
				.filter(x -> x.getCoordinate().inBounds(55, 20, -60, -130)) //
				.collect(Collectors.toList());
		for (BuoyStation station : stations) {
			id_map.put(station.getId(), station);
		}
	}

	public BuoyStation getStation(String id) {
		return id_map.get(id);
	}

	// https://www.ndbc.noaa.gov/data/stations/buoyht.txt

	// https://www.ndbc.noaa.gov/data/stations/station_table.txt

	public static String getStationsXmlString() throws Exception {
		ClientBuilder builder = ClientBuilder.newBuilder();
		Client client = builder.build();
		String uri = "https://www.ndbc.noaa.gov/activestations.xml";
		WebTarget target = client.target(uri);
		Response resp = target.request(MediaType.TEXT_XML).get();
		if (resp.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL) {
			String xml = resp.readEntity(String.class);
			if (trace)
				logger.info("\n" + xml);
			return xml;
		}
		String message = resp.getStatus() + " " + resp.readEntity(String.class);
		logger.error(message);
		throw new Exception(message);
	}

	public static List<BuoyStation> getStationsFromXml(String xml) throws Exception {
		List<BuoyStation> ret = new ArrayList<>();
		Document doc = DocumentHelper.parseText(xml);
		List<Element> elements = doc.getRootElement().elements("station");
		// logger.info("" + elements.size());
		for (Element el : elements) {
			// logger.info(el.asXML());
			String id = el.attributeValue("id");
			String lat = el.attributeValue("lat");
			String lon = el.attributeValue("lon");
			String name = el.attributeValue("name");
			BuoyStation station = new BuoyStation(id, Double.parseDouble(lat), Double.parseDouble(lon), name);
//			if (station.getCoordinate().inBounds(55, 20, -60, -130))
			ret.add(station);
		}
		return ret;
	}

	public List<BuoyStationDistance> getNearest(double lat, double lng, int nm_limit, int num_results) {
		return stations.stream().map(x -> new BuoyStationDistance(lat, lng, x)) //
				.filter(x -> x.getDistance() <= nm_limit) //
				.sorted(Comparator.comparingDouble(x -> x.getDistance())) //
				.limit(num_results).collect(Collectors.toList());
	}

	public BuoyStationDistance getNearest(double lat, double lng) {
		return getNearest(lat, lng, Integer.MAX_VALUE, 1).get(0);
	}

}
