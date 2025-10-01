package com.mays.tempest.buoys;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import javax.measure.Quantity;
import javax.measure.quantity.Angle;
import javax.measure.quantity.Length;
import javax.measure.quantity.Pressure;
import javax.measure.quantity.Speed;
import javax.measure.quantity.Temperature;

import com.mays.tempest.weather.WeatherQuantities;

public class BuoyObservation {

	private ZonedDateTime time;

	private Quantity<Angle> windDirection;

	private Quantity<Speed> windSpeed; // m/s - %5Bnmi_i%5D/h = 1.9438445

	private Quantity<Speed> windGust;// m/s

	private Quantity<Length> waveHeight; // m - %5Bft_us%5D = 3.2808333

	private Quantity<Angle> waveDirection;

	private Quantity<Pressure> atmosphericPressure; // hPa - inch Hg = 0.02953

	private Quantity<Temperature> airTemperature; // degC - (C × 9/5) + 32

	private Quantity<Temperature> waterTemperature; // degC

	private Quantity<Temperature> dewPoint; // degC

	private Quantity<Length> visibility; // nm

	// https://ucum.nlm.nih.gov/ucum-service/v1/ucumtransform/from/m/s/to/%5Bnmi_i%5D/h

	// https://ucum.nlm.nih.gov/ucum-service/v1/ucumtransform/from/m/to/%5Bft_us%5D

	public BuoyObservation(int year, int month, int day, int hour, int minute, Integer windDirection, Double windSpeed,
			Double windGust, Double waveHeight, Integer waveDirection, Double atmosphericPressure,
			Double airTemperature, Double waterTemperature, Double dewPoint, Double visibility) {
		time = ZonedDateTime.of(year, month, day, hour, minute, 0, 0, ZoneId.of("UTC").normalized());
		this.windDirection = WeatherQuantities.toAngle(windDirection, "degT");
		if (windSpeed != null)
//			this.windSpeed = windSpeed * 1.9438445;
			this.windSpeed = WeatherQuantities.toSpeed(windSpeed, "m/s");
		if (windGust != null)
			this.windGust = WeatherQuantities.toSpeed(windGust, "m/s");
		if (waveHeight != null)
//			this.waveHeight = waveHeight * 3.2808333;
			this.waveHeight = WeatherQuantities.toLength(waveHeight, "m");
		this.waveDirection = WeatherQuantities.toAngle(waveDirection, "degT");
		if (atmosphericPressure != null)
//			this.atmosphericPressure = atmosphericPressure * 0.02953;
			this.atmosphericPressure = WeatherQuantities.toPressure(atmosphericPressure, "hPa");
		if (airTemperature != null)
//			this.airTemperature = airTemperature * 9 / 5 + 32;
			this.airTemperature = WeatherQuantities.toTemperature(airTemperature, "degC");
		if (waterTemperature != null)
			this.waterTemperature = WeatherQuantities.toTemperature(waterTemperature, "degC");
		if (dewPoint != null)
			this.dewPoint = WeatherQuantities.toTemperature(dewPoint, "degC");
		this.visibility = WeatherQuantities.toLength(visibility, "nm");
	}

	public ZonedDateTime getTime() {
		return time;
	}

	public LocalDateTime getLocalDateTime(BuoyStation station) {
		return getTime().withZoneSameInstant(station.getTimeZone()).toLocalDateTime();
	}

	public Quantity<Angle> getWindDirection() {
		return windDirection;
	}

	public Quantity<Speed> getWindSpeed() {
		return windSpeed;
	}

	public Quantity<Speed> getWindGust() {
		return windGust;
	}

	public Quantity<Length> getWaveHeight() {
		return waveHeight;
	}

	public Quantity<Angle> getWaveDirection() {
		return waveDirection;
	}

	public Quantity<Pressure> getAtmosphericPressure() {
		return atmosphericPressure;
	}

	public Quantity<Temperature> getAirTemperature() {
		return airTemperature;
	}

	public Quantity<Temperature> getWaterTemperature() {
		return waterTemperature;
	}

	public Quantity<Temperature> getDewPoint() {
		return dewPoint;
	}

	public Quantity<Length> getVisibility() {
		return visibility;
	}

//	private String format(String label, Double value, String units) {
//		if (value == null)
//			return "";
//		return label + " " + String.format("%.0f", value) + " " + units;
//	}

//	public String toString() {
//		return getTime() + " " //
//				+ (getWindDirection() == null || getWindSpeed() == null || getWindGust() == null ? ""
//						: getWindDirection() + " @ " + String.format("%.0f", getWindSpeed()) + " G "
//								+ String.format("%.0f", getWindGust()) + " knots ")
//				+ (getWaveDirection() == null || getWaveHeight() == null ? ""
//						: "Wave: " + getWaveDirection() + " " + String.format("%.1f", getWaveHeight()) + " ft ")
//				+ (getAtmosphericPressure() == null ? "" : String.format("%.2f", getAtmosphericPressure()) + " inHg")
//				+ format(" Air:", getAirTemperature(), "degF") //
//				+ format(" Water: ", getWaterTemperature(), "degF") //
//				+ format(" Dewpoint:", getDewPoint(), " degF") //
//				+ format(" Vis:", getVisibility(), " NM");
//	}

}
