package com.mays.tempest.weather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherForecastGeneralJson {

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class PeriodJson {

		private int number;

		private String name;

		private String startTime;

		private String endTime;

		private boolean daytime;

		private int temperature;

		private String temperatureUnit;

		private String temperatureTrend;

		// TODO
//		"probabilityOfPrecipitation": {
//            "unitCode": "wmoUnit:percent",
//            "value": 80
//        },
//        "dewpoint": {
//            "unitCode": "wmoUnit:degC",
//            "value": 7.2222222222222223
//        },
//        "relativeHumidity": {
//            "unitCode": "wmoUnit:percent",
//            "value": 96
//        },

		private String windSpeed;

		private String windDirection;

		private String icon;

		private String shortForecast;

		private String detailedForecast;

		public int getNumber() {
			return number;
		}

		public void setNumber(int number) {
			this.number = number;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getStartTime() {
			return startTime;
		}

		public void setStartTime(String startTime) {
			this.startTime = startTime;
		}

		public String getEndTime() {
			return endTime;
		}

		public void setEndTime(String endTime) {
			this.endTime = endTime;
		}

		public boolean isDaytime() {
			return daytime;
		}

		@JsonSetter("isDaytime")
		public void setDaytime(boolean daytime) {
			this.daytime = daytime;
		}

		public int getTemperature() {
			return temperature;
		}

		public void setTemperature(int temperature) {
			this.temperature = temperature;
		}

		public String getTemperatureUnit() {
			return temperatureUnit;
		}

		public void setTemperatureUnit(String temperatureUnit) {
			this.temperatureUnit = temperatureUnit;
		}

		public String getTemperatureTrend() {
			return temperatureTrend;
		}

		public void setTemperatureTrend(String temperatureTrend) {
			this.temperatureTrend = temperatureTrend;
		}

		public String getWindSpeed() {
			return windSpeed;
		}

		public void setWindSpeed(String windSpeed) {
			this.windSpeed = windSpeed;
		}

		public String getWindDirection() {
			return windDirection;
		}

		public void setWindDirection(String windDirection) {
			this.windDirection = windDirection;
		}

		public String getIcon() {
			return icon;
		}

		public void setIcon(String icon) {
			this.icon = icon;
		}

		public String getShortForecast() {
			return shortForecast;
		}

		public void setShortForecast(String shortForecast) {
			this.shortForecast = shortForecast;
		}

		public String getDetailedForecast() {
			return detailedForecast;
		}

		public void setDetailedForecast(String detailedForecast) {
			this.detailedForecast = detailedForecast;
		}

	}

	private String updateTime;

	private String validTimes;

	private WeatherObservationJson.Value elevation;

	private PeriodJson[] periods;

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getValidTimes() {
		return validTimes;
	}

	public void setValidTimes(String validTimes) {
		this.validTimes = validTimes;
	}

	public WeatherObservationJson.Value getElevation() {
		return elevation;
	}

	public void setElevation(WeatherObservationJson.Value elevation) {
		this.elevation = elevation;
	}

	public PeriodJson[] getPeriods() {
		return periods;
	}

	public void setPeriods(PeriodJson[] periods) {
		this.periods = periods;
	}

}
