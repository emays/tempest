package com.mays.tempest.weather;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

import javax.measure.quantity.Temperature;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mays.tempest.LocationInfo;
import com.mays.tempest.weather.WeatherForecastGridData.ForecastElement;

import systems.uom.common.USCustomary;

public class WeatherTest {

	private static final Logger logger = LoggerFactory.getLogger(WeatherTest.class);

	private static final boolean trace = false;

	private final String cache_dir = "src/test/resources/weather";

	private boolean resources_exception = true;

	public static String PVC_ID = "KPVC";

	private String pointsString;

	private String observationStationsString;

	private String observationsString;

	private String observationStationString;

	private String forecastGridDataString;

	private String forecastString;

	@BeforeEach
	public void before() throws Exception {
		if (pointsString == null) {
			Path path = Paths.get(cache_dir, "weather-points.json");
			if (!Files.exists(path)) {
				if (resources_exception)
					throw new Exception();
				logger.info("setupPoints");
				String res = new Weather().getPointsJsonString(LocationInfo.PROVINCETOWN.getLatitude(),
						LocationInfo.PROVINCETOWN.getLongitude());
				// logger.info(res);
				Files.writeString(path, res);
			}
			pointsString = Files.readString(path);
		}
		if (observationStationsString == null) {
			Path path = Paths.get(cache_dir, "weather-observation-stations.json");
			if (!Files.exists(path)) {
				if (resources_exception)
					throw new Exception();
				logger.info("setupObservationStations");
				WeatherPointsJson points = new Weather().getPointsJson(LocationInfo.PROVINCETOWN.getLatitude(),
						LocationInfo.PROVINCETOWN.getLongitude());
				logger.info(points.getObservationStations());
				String res = new Weather().getPointJsonString(points.getObservationStations());
				// logger.info(res);
				Files.writeString(path, res);
			}
			observationStationsString = Files.readString(path);
		}
		if (observationsString == null) {
			Path path = Paths.get(cache_dir, "weather-observations.json");
			if (!Files.exists(path)) {
				if (resources_exception)
					throw new Exception();
				logger.info("setupObservations");
				String res = new Weather().getObservationsJsonString(PVC_ID);
				// logger.info(res);
				Files.writeString(path, res);
			}
			observationsString = Files.readString(path);
		}
		if (observationStationString == null) {
			Path path = Paths.get(cache_dir, "weather-observation-station.json");
			if (!Files.exists(path)) {
				if (resources_exception)
					throw new Exception();
				logger.info("setupObservationStation");
				String res = new Weather().getObservationStationJsonString(PVC_ID);
				// logger.info(res);
				Files.writeString(path, res);
			}
			observationStationString = Files.readString(path);
		}
		if (forecastGridDataString == null) {
			Path path = Paths.get(cache_dir, "weather-forecast-grid-data.json");
			if (!Files.exists(path)) {
				if (resources_exception)
					throw new Exception();
				logger.info("setupForecastGridData");
				WeatherPointsJson points = new Weather().getPointsJson(LocationInfo.PROVINCETOWN.getLatitude(),
						LocationInfo.PROVINCETOWN.getLongitude());
				if (trace)
					logger.info(points.getForecastGridData());
				String res = new Weather().getPointJsonString(points.getForecastGridData());
				// logger.info(res);
				Files.writeString(path, res);
			}
			forecastGridDataString = Files.readString(path);
		}
		if (forecastString == null) {
			Path path = Paths.get(cache_dir, "weather-forecast.json");
			if (!Files.exists(path)) {
				if (resources_exception)
					throw new Exception();
				logger.info("setupForecast");
				WeatherPointsJson points = new Weather().getPointsJson(LocationInfo.PROVINCETOWN.getLatitude(),
						LocationInfo.PROVINCETOWN.getLongitude());
				if (trace)
					logger.info(points.getForecast());
				String res = new Weather().getPointJsonString(points.getForecast());
				// logger.info(res);
				Files.writeString(path, res);
			}
			forecastString = Files.readString(path);
		}
	}

	@Test
	public void getPoints() throws Exception {
		Weather weather = new Weather();
//		WeatherPointsJson points = weather.getPointsJson(42.02797, -70.0848);
		WeatherPointsJson points = weather.getPointsJsonFromJson(pointsString);
		assertEquals(points.getForecastOffice(), "https://api.weather.gov/offices/BOX");
		assertEquals(points.getGridId(), "BOX");
		assertEquals(points.getGridX(), "106");
		assertEquals(points.getGridY(), "67");
		assertEquals(points.getForecast(), "https://api.weather.gov/gridpoints/BOX/106,67/forecast");
		assertEquals(points.getForecastHourly(), "https://api.weather.gov/gridpoints/BOX/106,67/forecast/hourly");
		assertEquals(points.getForecastGridData(), "https://api.weather.gov/gridpoints/BOX/106,67");
		assertEquals(points.getObservationStations(), "https://api.weather.gov/gridpoints/BOX/106,67/stations");
		assertEquals(points.getForecastZone(), "https://api.weather.gov/zones/forecast/MAZ022");
		assertEquals(points.getCounty(), "https://api.weather.gov/zones/county/MAC001");
		assertEquals(points.getFireWeatherZone(), "https://api.weather.gov/zones/fire/MAZ022");
		assertEquals(points.getTimeZone(), "America/New_York");
		assertEquals(points.getRadarStation(), "KBOX");
	}

	@Test
	public void getObservationStations() throws Exception {
		Weather weather = new Weather();
		List<WeatherObservationStationJson> obs = weather.getObservationStationsJsonFromJson(observationStationsString);
		WeatherObservationStationJson obs1 = obs.get(0);
		if (trace) {
			logger.info("" + obs1.getCoordinates()[0] + " " + obs1.getCoordinates()[1]);
			logger.info("" + obs1.getStationIdentifier());
			logger.info("" + obs1.getName());
			logger.info("" + obs1.getTimeZone());
		}
	}

//	@Test
	public void getNearest() throws Exception {
		Weather weather = new Weather();
		List<WeatherObservationStationDistance> stations = weather.getNearest(LocationInfo.PROVINCETOWN.getLatitude(),
				LocationInfo.PROVINCETOWN.getLongitude(), 50, 5);
		stations.forEach(x -> logger.info(x.toString()));
	}

	@Test
	public void getObservations() throws Exception {
		Weather weather = new Weather();
		List<WeatherObservationJson> obs = weather.getObservationsJsonFromJson(observationsString);
		assertEquals(194, obs.size());
	}

	@Test
	public void getObservation() throws Exception {
		Weather weather = new Weather();
		List<WeatherObservationJson> obs = weather.getObservationsJsonFromJson(observationsString);
		WeatherObservation obs1 = new WeatherObservation(obs.get(0));
		if (trace)
			logger.info("" + obs1.getTime());
		{
			WeatherObservationStation station = new WeatherObservationStation(
					weather.getObservationStationJsonFromJson(observationStationString));
			if (trace) {
				logger.info("" + station.getTimeZone());
				logger.info("" + station.getTimeZone().getDisplayName(TextStyle.SHORT, Locale.getDefault()));
				logger.info("" + obs1.getTime().withZoneSameInstant(station.getTimeZone()));
				logger.info("" + obs1.getTime().withZoneSameInstant(station.getTimeZone()).getOffset());
				logger.info("" + obs1.getTime().withZoneSameInstant(station.getTimeZone())
						.format(DateTimeFormatter.ofPattern("zzz")));
			}
		}
		if (trace) {
			logger.info("" + obs1.getRawMessage());
			logger.info("" + obs1.getTextDescription());
			logger.info("" + obs1.getIcon());
			logger.info("" + obs1.getTemperature());
			logger.info("" + obs1.getTemperature().to(USCustomary.FAHRENHEIT));
			logger.info("" + obs1.getDewpoint().to(USCustomary.FAHRENHEIT));
			logger.info("" + obs1.getWindDirection());
			logger.info("" + obs1.getWindSpeed().to(USCustomary.MILE_PER_HOUR));
			logger.info("" + obs1.getWindGust());
			logger.info("" + obs1.getBarometricPressure());
			logger.info("" + obs1.getSeaLevelPressure());
			logger.info("" + obs1.getVisibility().to(USCustomary.MILE));
			logger.info("" + obs1.getMaxTemperatureLast24Hours());
			logger.info("" + obs1.getMinTemperatureLast24Hours());
			logger.info("" + obs1.getPrecipitationLastHour());
			logger.info("" + obs1.getPrecipitationLast3Hours());
			logger.info("" + obs1.getPrecipitationLast6Hours());
			logger.info("" + obs1.getWindChill());
			logger.info("" + obs1.getHeatIndex());
		}
		obs.forEach(WeatherObservation::new);
	}

	@Test
	public void getObservationStation() throws Exception {
		Weather weather = new Weather();
		WeatherObservationStationJson obs = weather.getObservationStationJsonFromJson(observationStationString);
		WeatherObservationStation obs1 = new WeatherObservationStation(obs);
		if (trace) {
			logger.info("" + obs1.getCoordinate().getLatitude() + " " + obs1.getCoordinate().getLongitude());
			logger.info("" + obs1.getElevation() + " " + obs1.getElevation().to(USCustomary.FOOT) + " "
					+ String.format("%.0f ft", obs1.getElevation().to(USCustomary.FOOT).getValue().doubleValue()));
			logger.info("" + obs1.getStationIdentifier());
			logger.info("" + obs1.getName());
			logger.info("" + obs1.getTimeZone());
		}
//		obs1 = weather.getObservationStation(PVC_ID);
		assertEquals(42.07, obs1.getCoordinate().getLatitude(), 0.005);
		assertEquals(-70.22, obs1.getCoordinate().getLongitude(), 0.005);
		assertEquals(7.0, obs1.getElevation().to(USCustomary.FOOT).getValue().doubleValue(), 0.1);
		assertEquals(PVC_ID, obs1.getStationIdentifier());
		assertEquals("Provincetown, Provincetown Municipal Airport", obs1.getName());
		assertEquals("America/New_York", obs1.getTimeZone().toString());
	}

	@Test
	public void getForecastGridData() throws Exception {
		Weather weather = new Weather();
		WeatherForecastGridDataJson fcgd_json = weather.getForecastGridDataJsonFromJson(forecastGridDataString);
		WeatherForecastGridData fcgd = new WeatherForecastGridData(fcgd_json);
		if (trace) {
			logger.info(fcgd_json.getUpdateTime());
			logger.info(fcgd.getUpdateTime().toString());
			logger.info(fcgd_json.getValidTimes());
			logger.info(fcgd.getValidTimes().toString());
			logger.info(fcgd_json.getElevation().toString());
			logger.info(fcgd.getElevation().toString());
		}
		for (ForecastElement<Temperature> fe : fcgd.getTemperature()) {
			if (trace)
				logger.info(fe.toString());
		}
		List<WeatherForecastGridDataSlice> fcs = new WeatherForecast(null, fcgd).getGridDataSlices();
		if (trace) {
			logger.info("" + fcgd_json.getTemperature());
			logger.info("" + fcgd.getTemperature().get(0));
			logger.info("" + fcs.get(0).getTemperature());
			logger.info("" + fcgd_json.getDewpoint());
			logger.info("" + fcgd.getDewpoint().get(0));
			logger.info("" + fcgd_json.getRelativeHumidity());
			logger.info("" + fcgd.getRelativeHumidity().get(0));
			logger.info("" + fcgd_json.getHeatIndex());
			logger.info("" + fcgd.getHeatIndex().get(0));
			logger.info("" + fcgd_json.getWindChill());
			logger.info("" + fcgd.getWindChill().get(0));
			logger.info("" + fcgd_json.getSkyCover());
			logger.info("" + fcgd.getSkyCover().get(0));
			logger.info("" + fcgd_json.getWindDirection());
			logger.info("" + fcgd.getWindDirection().get(0));
			logger.info("" + fcgd_json.getWindSpeed());
			logger.info("" + fcgd.getWindSpeed().get(0));
			logger.info("" + fcgd_json.getWindGust());
			logger.info("" + fcgd.getWindGust().get(0));
			logger.info("" + fcgd_json.getProbabilityOfPrecipitation());
			logger.info("" + fcgd.getProbabilityOfPrecipitation().get(0));
			logger.info("" + fcgd_json.getQuantitativePrecipitation());
			logger.info("" + fcgd.getQuantitativePrecipitation().get(0));
			logger.info("" + fcs.size());
		}
	}

	@Test
	public void getForecastGeneral() throws Exception {
		Weather weather = new Weather();
		WeatherForecastGeneralJson fc_json = weather.getForecastGeneralJsonFromJson(forecastString);
		WeatherForecastGeneral fc = new WeatherForecastGeneral(fc_json);
		if (trace) {
			logger.info(fc_json.getUpdateTime());
			logger.info(fc.getUpdateTime().toString());
			logger.info(fc_json.getValidTimes());
			logger.info(fc.getValidTimes().toString());
			logger.info(fc_json.getElevation().toString());
			logger.info(fc.getElevation().toString());
		}
		assertEquals(14, fc_json.getPeriods().length);
		assertEquals(14, fc.getPeriods().size());
	}

}
