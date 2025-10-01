package com.mays.tempest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mays.tempest.buoys.BuoyObservation;
import com.mays.tempest.buoys.Buoys;
import com.mays.tempest.tides.Tide;
import com.mays.tempest.tides.Tides;
import com.mays.tempest.weather.Weather;
import com.mays.tempest.weather.WeatherForecast;
import com.mays.tempest.weather.WeatherForecastGeneral;
import com.mays.tempest.weather.WeatherForecastGeneralJson;
import com.mays.tempest.weather.WeatherForecastGridData;
import com.mays.tempest.weather.WeatherForecastGridDataJson;
import com.mays.tempest.weather.WeatherObservation;
import com.mays.tempest.weather.WeatherPointsJson;

public class TestDataUtil {

	private static final Logger logger = LoggerFactory.getLogger(TestDataUtil.class);

	public static final String DIR = "src/test/resources/data";

	public static final DateTimeFormatter DIR_DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss");

	private ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC"));

	private String time;

	public TestDataUtil(String time) {
		this.time = time;
	}

	public TestDataUtil() {
		this.time = now.withZoneSameInstant(ZoneId.systemDefault()).format(DIR_DATE_TIME_FORMAT);
	}

	public Path getDirectoryPath() {
		return Paths.get(DIR);
	}

	public Path getPath() {
		return getDirectoryPath().resolve(time);
	}

	public static List<String> getDataDirectories() throws Exception {
		return Files.list(Paths.get(DIR)) //
				.map(dir -> dir.getName(dir.getNameCount() - 1).toString()) //
				.filter(dir -> dir.matches("\\d+T\\d+")) //
				.sorted().collect(Collectors.toList());
	}

	private void write(String file, String res) throws IOException {
		Files.writeString(getPath().resolve(file), res);
	}

	private String read(String file) throws Exception {
		return Files.readString(getPath().resolve(file));
	}

	private final String buoy_file = "buoy-observations.txt";

	private final String tides_file = "tides.json";

	private final String obs_file = "weather-observations.json";

	private final String wfgd_file = "weather-forecast-grid-data.json";

	private final String wf_file = "weather-forecast.json";

	private final String ids_file = "ids.txt";

	public void get(String buoy_id, String tide_id, String weather_id, double lat, double lng) throws Exception {
		logger.info(getPath().toString());
		Files.createDirectories(getPath());
		write(buoy_file, new Buoys().getObservationsString(buoy_id));
		{
			DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyyMMdd");
			String tide_start = now.minusDays(30).format(df);
			String tide_end = now.plusDays(30).format(df);
			logger.info("Tides: " + tide_start + " " + tide_end);
			write(tides_file, Tides.getTidesJsonString(tide_id, tide_start, tide_end));
		}
		write(obs_file, new Weather().getObservationsJsonString(weather_id));
		WeatherPointsJson points = new Weather().getPointsJson(lat, lng);
		write(wfgd_file, new Weather().getPointJsonString(points.getForecastGridData()));
		write(wf_file, new Weather().getPointJsonString(points.getForecast()));
		Files.write(getPath().resolve(ids_file),
				List.of(now.toString(), buoy_id, tide_id, weather_id, "" + lat, "" + lng));
	}

	private String readId(int i) throws Exception {
		return Files.readAllLines(getPath().resolve(ids_file)).get(i);
	}

	public ZonedDateTime getTimestamp() throws Exception {
		return ZonedDateTime.parse(readId(0));
	}

	public String getBuoyId() throws Exception {
		return readId(1);
	}

	public String getTideId() throws Exception {
		return readId(2);
	}

	public String getWeatherId() throws Exception {
		return readId(3);
	}

	public double getLatitude() throws Exception {
		return Double.parseDouble(readId(4));
	}

	public double getLongtitude() throws Exception {
		return Double.parseDouble(readId(5));
	}

	public ZoneId getTimeZone() throws Exception {
		return TimeZoneUtil.getInstance().getTimeZone(getLatitude(), getLongtitude());
	}

	public ZonedDateTime getTimeAtZone() throws Exception {
		return getTimestamp().withZoneSameInstant(getTimeZone());
	}

	public List<Tide> getTides() throws Exception {
		String json = read(tides_file);
		return Tides.getTidesFromJson(json);
	}

	public List<Tide> getTidesForMonth(boolean prior_next) throws Exception {
		int year = getTimeAtZone().getYear();
		int month = getTimeAtZone().getMonthValue();
		List<Tide> tides = getTides();
		List<Tide> ret = Tides.getTides(tides, year, month);
		if (prior_next) {
			ret.add(0, tides.get(tides.indexOf(ret.get(0)) - 1));
			ret.add(tides.get(tides.indexOf(ret.get(ret.size() - 1)) + 1));
		}
		return ret;
	}

	public ArrayList<BuoyObservation> getBuoyObservations() throws Exception {
		String json = read(buoy_file);
		return new Buoys().getObservationsFromString(json);
	}

	public List<WeatherObservation> getWeatherObservations() throws Exception {
		String json = read(obs_file);
		return new Weather().getObservationsFromJson(json);
	}

	public WeatherForecastGridData getWeatherForecastGridData() throws Exception {
		String fcgd_str = read(wfgd_file);
		WeatherForecastGridDataJson fcgd_json = new Weather().getForecastGridDataJsonFromJson(fcgd_str);
		return new WeatherForecastGridData(fcgd_json);
	}

	public WeatherForecastGeneral getWeatherForecastGeneral() throws Exception {
		String fc_str = read(wf_file);
		WeatherForecastGeneralJson fc_json = new Weather().getForecastGeneralJsonFromJson(fc_str);
		return new WeatherForecastGeneral(fc_json);
	}

	public WeatherForecast getWeatherForecast() throws Exception {
		return new WeatherForecast(getWeatherForecastGeneral(), getWeatherForecastGridData());
	}

}
