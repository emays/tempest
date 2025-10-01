package com.mays.tempest.weather;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.measure.Quantity;
import javax.measure.quantity.Length;
import javax.measure.quantity.Temperature;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherForecastGeneral {

	public static class Period {

		private int number;

		private String name;

		private ZonedDateTime startTime;

		private ZonedDateTime endTime;

		private boolean daytime;

		private Quantity<Temperature> temperature;

		private String temperatureTrend;

		private String windSpeed;

		private String windDirection;

		private String icon;

		private String shortForecast;

		private String detailedForecast;

		public Period(WeatherForecastGeneralJson.PeriodJson wfp_json) {
			super();
			this.number = wfp_json.getNumber();
			this.name = wfp_json.getName();
			this.startTime = ZonedDateTime.parse(wfp_json.getStartTime());
			this.endTime = ZonedDateTime.parse(wfp_json.getEndTime());
			this.daytime = wfp_json.isDaytime();
			this.temperature = WeatherQuantities.toTemperature((double) wfp_json.getTemperature(),
					wfp_json.getTemperatureUnit());
			this.temperatureTrend = wfp_json.getTemperatureTrend();
			this.windSpeed = wfp_json.getWindSpeed();
			this.windDirection = wfp_json.getWindDirection();
			this.icon = wfp_json.getIcon();
			this.shortForecast = wfp_json.getShortForecast();
			this.detailedForecast = wfp_json.getDetailedForecast();
		}

		public int getNumber() {
			return number;
		}

		public String getName() {
			return name;
		}

		public ZonedDateTime getStartTime() {
			return startTime;
		}

		public LocalDateTime getStartLocalDateTime(ZoneId timezone) {
			return getStartTime().withZoneSameInstant(timezone).toLocalDateTime();
		}

		public ZonedDateTime getEndTime() {
			return endTime;
		}

		public LocalDateTime getEndLocalDateTime(ZoneId timezone) {
			return getEndTime().withZoneSameInstant(timezone).toLocalDateTime();
		}

		public boolean isDaytime() {
			return daytime;
		}

		public Quantity<Temperature> getTemperature() {
			return temperature;
		}

		public String getTemperatureTrend() {
			return temperatureTrend;
		}

		public String getWindSpeed() {
			return windSpeed;
		}

		public String getWindDirection() {
			return windDirection;
		}

		public String getIcon() {
			return icon;
		}

		public String getShortForecast() {
			return shortForecast;
		}

		public String getDetailedForecast() {
			return detailedForecast;
		}

	}

	private ZonedDateTime updateTime;

	private WeatherValidTime validTimes;

	private Quantity<Length> elevation;

	private ArrayList<Period> periods;

	public WeatherForecastGeneral(WeatherForecastGeneralJson wf_json) {
		this.updateTime = ZonedDateTime.parse(wf_json.getUpdateTime());
		this.validTimes = WeatherValidTime.parse(wf_json.getValidTimes());
		this.elevation = WeatherQuantities.toLength(wf_json.getElevation());
		periods = Arrays.stream(wf_json.getPeriods()).map(Period::new).collect(Collectors.toCollection(ArrayList::new));
	}

	public ZonedDateTime getUpdateTime() {
		return updateTime;
	}

	public WeatherValidTime getValidTimes() {
		return validTimes;
	}

	public Quantity<Length> getElevation() {
		return elevation;
	}

	public ArrayList<Period> getPeriods() {
		return periods;
	}

	public ZonedDateTime getStartTime() {
		return periods.stream().map(Period::getStartTime).min(Comparator.comparing(Function.identity())).get();
	}

	public ZonedDateTime getEndTime() {
		return periods.stream().map(Period::getEndTime).max(Comparator.comparing(Function.identity())).get();
	}

	public Period getPeriod(ZonedDateTime time) {
		return getPeriods().stream()
				.filter(period -> (time.isAfter(period.getStartTime()) || time.isEqual(period.getStartTime()))
						&& time.isBefore(period.getEndTime()))
				.findFirst().orElse(null);
	}

}
