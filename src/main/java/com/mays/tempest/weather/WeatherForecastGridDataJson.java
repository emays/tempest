package com.mays.tempest.weather;

import java.util.Arrays;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherForecastGridDataJson {

	public static class ForecastElementJson {

		private String uom;

		private Value[] values;

		public String getUom() {
			return uom;
		}

		public void setUom(String uom) {
			this.uom = uom;
		}

		public Value[] getValues() {
			return values;
		}

		public void setValues(Value[] values) {
			this.values = values;
		}

		public String toString() {
			return Arrays.stream(values).map(x -> x.value + " " + uom + " @ " + x.validTime)
					.collect(Collectors.joining(", ", "{", "}"));
		}

	}

	public static class Value {

		private String validTime;

		private Double value;

		public String getValidTime() {
			return validTime;
		}

		public void setValidTime(String validTime) {
			this.validTime = validTime;
		}

		public Double getValue() {
			return value;
		}

		public void setValue(Double value) {
			this.value = value;
		}

	}

	private String updateTime;

	private String validTimes;

	private WeatherObservationJson.Value elevation;

	private ForecastElementJson temperature;

	private ForecastElementJson dewpoint;

	private ForecastElementJson maxTemperature;

	private ForecastElementJson minTemperature;

	private ForecastElementJson relativeHumidity;

	private ForecastElementJson apparentTemperature;

	private ForecastElementJson heatIndex;

	private ForecastElementJson windChill;

	private ForecastElementJson skyCover;

	private ForecastElementJson windDirection;

	private ForecastElementJson windSpeed;

	private ForecastElementJson windGust;

	// TODO
	// weather

	// hazards

	private ForecastElementJson probabilityOfPrecipitation;

	private ForecastElementJson quantitativePrecipitation;

	private ForecastElementJson iceAccumulation;

	private ForecastElementJson snowfallAmount;

	private ForecastElementJson snowLevel;

	private ForecastElementJson ceilingHeight;

	private ForecastElementJson visibility;

	private ForecastElementJson transportWindSpeed;

	private ForecastElementJson transportWindDirection;

	private ForecastElementJson mixingHeight;

	// hainesIndex

	// lightningActivityLevel

	// twentyFootWindSpeed
	// ...

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

	public ForecastElementJson getTemperature() {
		return temperature;
	}

	public void setTemperature(ForecastElementJson temperature) {
		this.temperature = temperature;
	}

	public ForecastElementJson getDewpoint() {
		return dewpoint;
	}

	public void setDewpoint(ForecastElementJson dewpoint) {
		this.dewpoint = dewpoint;
	}

	public ForecastElementJson getMaxTemperature() {
		return maxTemperature;
	}

	public void setMaxTemperature(ForecastElementJson maxTemperature) {
		this.maxTemperature = maxTemperature;
	}

	public ForecastElementJson getMinTemperature() {
		return minTemperature;
	}

	public void setMinTemperature(ForecastElementJson minTemperature) {
		this.minTemperature = minTemperature;
	}

	public ForecastElementJson getRelativeHumidity() {
		return relativeHumidity;
	}

	public void setRelativeHumidity(ForecastElementJson relativeHumidity) {
		this.relativeHumidity = relativeHumidity;
	}

	public ForecastElementJson getApparentTemperature() {
		return apparentTemperature;
	}

	public void setApparentTemperature(ForecastElementJson apparentTemperature) {
		this.apparentTemperature = apparentTemperature;
	}

	public ForecastElementJson getHeatIndex() {
		return heatIndex;
	}

	public void setHeatIndex(ForecastElementJson heatIndex) {
		this.heatIndex = heatIndex;
	}

	public ForecastElementJson getWindChill() {
		return windChill;
	}

	public void setWindChill(ForecastElementJson windChill) {
		this.windChill = windChill;
	}

	public ForecastElementJson getSkyCover() {
		return skyCover;
	}

	public void setSkyCover(ForecastElementJson skyCover) {
		this.skyCover = skyCover;
	}

	public ForecastElementJson getWindDirection() {
		return windDirection;
	}

	public void setWindDirection(ForecastElementJson windDirection) {
		this.windDirection = windDirection;
	}

	public ForecastElementJson getWindSpeed() {
		return windSpeed;
	}

	public void setWindSpeed(ForecastElementJson windSpeed) {
		this.windSpeed = windSpeed;
	}

	public ForecastElementJson getWindGust() {
		return windGust;
	}

	public void setWindGust(ForecastElementJson windGust) {
		this.windGust = windGust;
	}

	public ForecastElementJson getProbabilityOfPrecipitation() {
		return probabilityOfPrecipitation;
	}

	public void setProbabilityOfPrecipitation(ForecastElementJson probabilityOfPrecipitation) {
		this.probabilityOfPrecipitation = probabilityOfPrecipitation;
	}

	public ForecastElementJson getQuantitativePrecipitation() {
		return quantitativePrecipitation;
	}

	public void setQuantitativePrecipitation(ForecastElementJson quantitativePrecipitation) {
		this.quantitativePrecipitation = quantitativePrecipitation;
	}

	public ForecastElementJson getIceAccumulation() {
		return iceAccumulation;
	}

	public void setIceAccumulation(ForecastElementJson iceAccumulation) {
		this.iceAccumulation = iceAccumulation;
	}

	public ForecastElementJson getSnowfallAmount() {
		return snowfallAmount;
	}

	public void setSnowfallAmount(ForecastElementJson snowfallAmount) {
		this.snowfallAmount = snowfallAmount;
	}

	public ForecastElementJson getSnowLevel() {
		return snowLevel;
	}

	public void setSnowLevel(ForecastElementJson snowLevel) {
		this.snowLevel = snowLevel;
	}

	public ForecastElementJson getCeilingHeight() {
		return ceilingHeight;
	}

	public void setCeilingHeight(ForecastElementJson ceilingHeight) {
		this.ceilingHeight = ceilingHeight;
	}

	public ForecastElementJson getVisibility() {
		return visibility;
	}

	public void setVisibility(ForecastElementJson visibility) {
		this.visibility = visibility;
	}

	public ForecastElementJson getTransportWindSpeed() {
		return transportWindSpeed;
	}

	public void setTransportWindSpeed(ForecastElementJson transportWindSpeed) {
		this.transportWindSpeed = transportWindSpeed;
	}

	public ForecastElementJson getTransportWindDirection() {
		return transportWindDirection;
	}

	public void setTransportWindDirection(ForecastElementJson transportWindDirection) {
		this.transportWindDirection = transportWindDirection;
	}

	public ForecastElementJson getMixingHeight() {
		return mixingHeight;
	}

	public void setMixingHeight(ForecastElementJson mixingHeight) {
		this.mixingHeight = mixingHeight;
	}

}
