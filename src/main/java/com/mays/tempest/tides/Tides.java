package com.mays.tempest.tides;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
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

public class Tides {

	private static final Logger logger = LoggerFactory.getLogger(Tides.class);

	private static final boolean trace = false;

	// https://tidesandcurrents.noaa.gov/web_services_info.html

	// https://tidesandcurrents.noaa.gov/api-helper/url-generator.html

	// https://api.tidesandcurrents.noaa.gov/api/prod/datagetter?begin_date=20210221&end_date=20210227&station=8446121&product=predictions&datum=STND&time_zone=lst_ldt&interval=hilo&units=english&application=truro&format=xml

	// https://api.tidesandcurrents.noaa.gov/api/prod/datagetter?station=8446121
	// &product=datums&units=english&application=truro&format=xml

	// https://tidesandcurrents.noaa.gov/datum_options.html

	// https://api.tidesandcurrents.noaa.gov/mdapi/prod/

	// https://api.tidesandcurrents.noaa.gov/mdapi/prod/webapi/stations/8446121/datums.xml?type=tidepredictions&units=english

	// https://api.tidesandcurrents.noaa.gov/mdapi/prod/webapi/stations.xml

	// https://api.tidesandcurrents.noaa.gov/mdapi/prod/webapi/stations.xml?type=tidepredictions

	public static String getTidesJsonString(String station, String begin_date, String end_date) throws Exception {
		ClientBuilder builder = ClientBuilder.newBuilder();
		Client client = builder.build();
		String uri = "https://api.tidesandcurrents.noaa.gov/api/prod/datagetter";
		WebTarget target = client.target(uri);
		target = target.queryParam("station", station);
		target = target.queryParam("begin_date", begin_date);
		target = target.queryParam("end_date", end_date);
		target = target.queryParam("product", "predictions");
		target = target.queryParam("datum", "MLLW");
		target = target.queryParam("product", "predictions");
		target = target.queryParam("units", "english");
		target = target.queryParam("time_zone", "lst_ldt");
		target = target.queryParam("format", "json");
		target = target.queryParam("interval", "hilo");
		target = target.queryParam("application", "truro");
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

	public static List<TideJson> getTidesJsonFromJson(String json) throws Exception {
		if (trace)
			logger.info("\n" + json);
		ObjectMapper mapper = new ObjectMapper();
		JsonNode jsonNode = mapper.readTree(json);
		String predictions = jsonNode.get("predictions").toString();
		if (trace)
			logger.info("\n" + predictions);
		List<TideJson> ret = mapper.readValue(predictions, new TypeReference<List<TideJson>>() {
		});
		return ret;
	}

	public static List<TideJson> getTidesJson(String station, String begin_date, String end_date) throws Exception {
		String json = getTidesJsonString(station, begin_date, end_date);
		return getTidesJsonFromJson(json);
	}

	public static List<Tide> getTidesFromJson(String json) throws Exception {
		return getTidesJsonFromJson(json).stream().map(Tide::new).collect(Collectors.toList());
	}

	public static List<Tide> getTides(String station, String begin_date, String end_date) throws Exception {
		String json = getTidesJsonString(station, begin_date, end_date);
		return getTidesFromJson(json);
	}

	public static String getTidesJsonString(String station, int year, int month) throws Exception {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd");
		LocalDate begin_date = LocalDate.of(year, month, 1);
		LocalDate end_date = begin_date.with(TemporalAdjusters.lastDayOfMonth());
		begin_date = begin_date.minusDays(1);
		end_date = end_date.plusDays(1);
		return getTidesJsonString(station, begin_date.format(dtf), end_date.format(dtf));
	}

	public static List<Tide> getTides(String station, int year, int month) throws Exception {
		return getTides(station, year, month, false);
	}

	public static List<Tide> getTides(String station, int year, int month, boolean fme) throws Exception {
		String json = TideDataAccess.getTidesJsonString(station, year, month, fme);
		if (json == null)
			json = getTidesJsonString(station, year, month);
		return getTidesFromJson(json);
	}

	public static List<Tide> getTides(String station, LocalDate start_date, LocalDate end_date) throws Exception {
		return getTides(station, start_date, end_date, false);
	}

	public static List<Tide> getTides(String station, LocalDate start_date, LocalDate end_date, boolean fme)
			throws Exception {
		ArrayList<Tide> tides = new ArrayList<>();
		tides.addAll(getTides(station, start_date.getYear(), start_date.getMonthValue(), fme));
		if (!start_date.withDayOfMonth(1).equals(end_date.withDayOfMonth(1))) {
			List<Tide> next_month_tides = getTides(station, end_date.getYear(), end_date.getMonthValue(), fme).stream()
					.filter(x -> x.getTime().isAfter(tides.getLast().getTime())).toList();
			tides.addAll(next_month_tides);
		}
		tides.removeIf(x -> x.getTime().isBefore(start_date.minusDays(1).atStartOfDay()));
		tides.removeIf(x -> x.getTime().isAfter(end_date.plusDays(2).atStartOfDay().minusSeconds(1)));
		return tides;

	}

	public static String getDatumsJsonString(String station) throws Exception {
		ClientBuilder builder = ClientBuilder.newBuilder();
		Client client = builder.build();
		String uri = "https://api.tidesandcurrents.noaa.gov/mdapi/prod/webapi/stations";
		WebTarget target = client.target(uri);
		target = target.path(station);
		target = target.path("datums.json");
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

	public static DatumsJson getDatumsJsonFromJson(String json) throws Exception {
		if (trace)
			logger.info("\n" + json);
		ObjectMapper mapper = new ObjectMapper();
		JsonNode jsonNode = mapper.readTree(json);
		String datums = jsonNode.toString();
		if (trace)
			logger.info("\n" + datums);
		DatumsJson ret = mapper.readValue(datums, new TypeReference<DatumsJson>() {
		});
		return ret;
	}

	public static Datums getDatumsFromJson(String json) throws Exception {
		return new Datums(getDatumsJsonFromJson(json));
	}

	public static Datums getDatums(String station) throws Exception {
		String json = getDatumsJsonString(station);
		return getDatumsFromJson(json);
	}

	private static List<DailyTide> getDailyTides(List<Tide> tides) {
		Collection<List<Tide>> days = tides.stream()
				.collect(Collectors.groupingBy(tide -> tide.getTime().toLocalDate())).values();
		ArrayList<DailyTide> dts = new ArrayList<>();
		for (List<Tide> day : days) {
			DailyTide dt = null;
			for (Tide tide : day) {
				if (dt == null)
					dt = new DailyTide(tide.getTime().toLocalDate());
				if (tide.getType().equals(Tide.Type.High)) {
					if (tide.getTime().getHour() < 12) {
						if (dt.getHighAm() != null)
							dt.setDstTide(dt.getHighAm());
						dt.setHighAm(tide);
					} else {
						dt.setHighPm(tide);
					}
				} else {
					if (tide.getTime().getHour() < 12) {
						if (dt.getLowAm() != null)
							dt.setDstTide(dt.getLowAm());
						dt.setLowAm(tide);
					} else {
						dt.setLowPm(tide);
					}
				}
			}
			dts.add(dt);
		}
		Collections.sort(dts, Comparator.comparing(DailyTide::getDate));
		return dts;
	}

	public static List<Tide> getTides(List<Tide> tides, int year, int month) {
		return tides.stream().filter(tide -> tide.getTime().getYear() == year) //
				.filter(tide -> tide.getTime().getMonthValue() == month) //
				.collect(Collectors.toList());
	}

	private static void setPriorNextDailyTides(List<DailyTide> daily_tides, List<Tide> tides) {
		for (DailyTide dt : daily_tides) {
			List<Tide> dt_tides = dt.getTides();
			int pi = tides.indexOf(dt_tides.get(0)) - 1;
			int ni = tides.indexOf(dt_tides.get(dt_tides.size() - 1)) + 1;
			dt.setPrior(tides.get(pi));
			dt.setNext(tides.get(ni));
		}
	}

	public static List<DailyTide> getDailyTides(List<Tide> tides, int year, int month) {
		List<DailyTide> ret = getDailyTides(getTides(tides, year, month));
		setPriorNextDailyTides(ret, tides);
		return ret;
	}

	public static List<DailyTide> getDailyTides(String station, int year, int month) throws Exception {
		List<Tide> tides = getTides(station, year, month);
		return getDailyTides(tides);
	}

	public static List<Tide> getTides(List<Tide> tides, LocalDate start_date, LocalDate end_date) {
		return tides.stream() //
				.filter(tide -> !tide.getTime().toLocalDate().isBefore(start_date)) //
				.filter(tide -> !tide.getTime().toLocalDate().isAfter(end_date)) //
				.toList();
	}

	public static List<DailyTide> getDailyTides(List<Tide> tides, LocalDate start_date, LocalDate end_date) {
		List<DailyTide> ret = getDailyTides(getTides(tides, start_date, end_date));
		setPriorNextDailyTides(ret, tides);
		return ret;
	}

}
