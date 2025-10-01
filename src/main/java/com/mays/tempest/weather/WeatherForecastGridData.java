package com.mays.tempest.weather;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.measure.Quantity;
import javax.measure.quantity.Angle;
import javax.measure.quantity.Dimensionless;
import javax.measure.quantity.Length;
import javax.measure.quantity.Speed;
import javax.measure.quantity.Temperature;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WeatherForecastGridData {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(WeatherForecastGridData.class);

	public static class ForecastElement<Q extends Quantity<Q>> {

		private Quantity<Q> quantity;

		private WeatherValidTime validTime;

		public ForecastElement(String time, Quantity<Q> quantity) {
			this.quantity = quantity;
			this.validTime = WeatherValidTime.parse(time);
		}

		public ForecastElement(WeatherValidTime validTime, Quantity<Q> quantity) {
			super();
			this.validTime = validTime;
			this.quantity = quantity;
		}

		public Quantity<Q> getQuantity() {
			return quantity;
		}

		public WeatherValidTime getValidTime() {
			return validTime;
		}

		public String toString() {
			return quantity + " " + validTime;
		}

	}

	private ZonedDateTime updateTime;

	private WeatherValidTime validTimes;

	private Quantity<Length> elevation;

	private List<ForecastElement<Temperature>> temperature;

	private List<ForecastElement<Temperature>> dewpoint;

	private List<ForecastElement<Dimensionless>> relativeHumidity;

	private List<ForecastElement<Temperature>> heatIndex;

	private List<ForecastElement<Temperature>> windChill;

	private List<ForecastElement<Dimensionless>> skyCover;

	private List<ForecastElement<Angle>> windDirection;

	private List<ForecastElement<Speed>> windSpeed;

	private List<ForecastElement<Speed>> windGust;

	private List<ForecastElement<Dimensionless>> probabilityOfPrecipitation;

	private List<ForecastElement<Length>> quantitativePrecipitation;

	private List<ForecastElement<Length>> ceilingHeight;

	private List<ForecastElement<Length>> visibility;

	public WeatherForecastGridData(WeatherForecastGridDataJson wfgd_json) {
		updateTime = ZonedDateTime.parse(wfgd_json.getUpdateTime());
		validTimes = WeatherValidTime.parse(wfgd_json.getValidTimes());
		elevation = WeatherQuantities.toLength(wfgd_json.getElevation());
		temperature = toTemperature(wfgd_json.getTemperature());
		dewpoint = toTemperature(wfgd_json.getDewpoint());
		relativeHumidity = toDimensionless(wfgd_json.getRelativeHumidity());
		heatIndex = toTemperature(wfgd_json.getHeatIndex());
		windChill = toTemperature(wfgd_json.getWindChill());
		skyCover = toDimensionless(wfgd_json.getSkyCover());
		windDirection = toAngle(wfgd_json.getWindDirection());
		windSpeed = toSpeed(wfgd_json.getWindSpeed());
		windGust = toSpeed(wfgd_json.getWindGust());
		probabilityOfPrecipitation = toDimensionless(wfgd_json.getProbabilityOfPrecipitation());
		quantitativePrecipitation = toLength(wfgd_json.getQuantitativePrecipitation());
		ceilingHeight = toLength(wfgd_json.getCeilingHeight());
		visibility = toLength(wfgd_json.getVisibility());
		fillAll();
	}

	private List<ForecastElement<Temperature>> toTemperature(WeatherForecastGridDataJson.ForecastElementJson wfgd) {
		return Arrays.stream(wfgd.getValues()).map(x -> new ForecastElement<Temperature>(x.getValidTime(),
				WeatherQuantities.toTemperature(x.getValue(), wfgd.getUom()))).collect(Collectors.toList());
	}

	private List<ForecastElement<Dimensionless>> toDimensionless(WeatherForecastGridDataJson.ForecastElementJson wfgd) {
		return Arrays.stream(wfgd.getValues()).map(x -> new ForecastElement<Dimensionless>(x.getValidTime(),
				WeatherQuantities.toDimensionless(x.getValue(), wfgd.getUom()))).collect(Collectors.toList());
	}

	private List<ForecastElement<Angle>> toAngle(WeatherForecastGridDataJson.ForecastElementJson wfgd) {
		return Arrays.stream(wfgd.getValues()).map(x -> new ForecastElement<Angle>(x.getValidTime(),
				WeatherQuantities.toAngle(x.getValue(), wfgd.getUom()))).collect(Collectors.toList());
	}

	private List<ForecastElement<Speed>> toSpeed(WeatherForecastGridDataJson.ForecastElementJson wfgd) {
		return Arrays.stream(wfgd.getValues()).map(x -> new ForecastElement<Speed>(x.getValidTime(),
				WeatherQuantities.toSpeed(x.getValue(), wfgd.getUom()))).collect(Collectors.toList());
	}

	private List<ForecastElement<Length>> toLength(WeatherForecastGridDataJson.ForecastElementJson wfgd) {
		return Arrays.stream(wfgd.getValues()).map(x -> new ForecastElement<Length>(x.getValidTime(),
				WeatherQuantities.toLength(x.getValue(), wfgd.getUom()))).collect(Collectors.toList());
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

	public List<ForecastElement<Temperature>> getTemperature() {
		return temperature;
	}

	public List<ForecastElement<Temperature>> getDewpoint() {
		return dewpoint;
	}

	public List<ForecastElement<Dimensionless>> getRelativeHumidity() {
		return relativeHumidity;
	}

	public List<ForecastElement<Temperature>> getHeatIndex() {
		return heatIndex;
	}

	public List<ForecastElement<Temperature>> getWindChill() {
		return windChill;
	}

	public List<ForecastElement<Dimensionless>> getSkyCover() {
		return skyCover;
	}

	public List<ForecastElement<Angle>> getWindDirection() {
		return windDirection;
	}

	public List<ForecastElement<Speed>> getWindSpeed() {
		return windSpeed;
	}

	public List<ForecastElement<Speed>> getWindGust() {
		return windGust;
	}

	public List<ForecastElement<Dimensionless>> getProbabilityOfPrecipitation() {
		return probabilityOfPrecipitation;
	}

	public List<ForecastElement<Length>> getQuantitativePrecipitation() {
		return quantitativePrecipitation;
	}

	public List<ForecastElement<Length>> getCeilingHeight() {
		return ceilingHeight;
	}

	public List<ForecastElement<Length>> getVisibility() {
		return visibility;
	}

	private <Q extends Quantity<Q>> void fillAll() {
		fill(temperature);
		fill(dewpoint);
		fill(relativeHumidity);
		fill(heatIndex);
		fill(windChill);
		fill(skyCover);
		fill(windDirection);
		fill(windSpeed);
		fill(windGust);
		fill(probabilityOfPrecipitation);
		fill(quantitativePrecipitation);
		fill(ceilingHeight);
		fill(visibility);
	}

	private <Q extends Quantity<Q>> void fill(List<ForecastElement<Q>> elems) {
//		logger.info("" + getValidTimes());
//		logger.info("" + elems.size());
//		logger.info(elems.size() + " " + elems.get(0).getQuantity() + " " + elems.get(0).getClass());
		List<ForecastElement<Q>> ret = new ArrayList<>();
		for (ZonedDateTime time = getValidTimes().getTime(); time
				.isBefore(getValidTimes().getEndTime()); time = time.plusHours(1)) {
			ForecastElement<Q> max = null;
			for (ForecastElement<Q> el : elems) {
//				logger.info(time + " " + el.getValidTime().getTime());
				if (el.getValidTime().getTime().isBefore(time) || el.getValidTime().getTime().isEqual(time))
					max = el;
			}
//			if (max == null) {
//				logger.warn("No max: " + elems.get(0) + " " + getValidTimes() + " " + getUpdateTime());
//				max = elems.get(0);
//			}
			ForecastElement<Q> elx = new ForecastElement<Q>(new WeatherValidTime(time, Duration.ofHours(1)),
					(max != null ? max.getQuantity() : null));
			ret.add(elx);
		}
		elems.clear();
		elems.addAll(ret);
	}

}
