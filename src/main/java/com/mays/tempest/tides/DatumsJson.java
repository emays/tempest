package com.mays.tempest.tides;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DatumsJson {

	private List<DatumJson> datums;

	private double hat;

	private String hatDate;

	private String hatTime;

	private double lat;

	private String latDate;

	private String latTime;

	public List<DatumJson> getDatums() {
		return datums;
	}

	public void setDatums(List<DatumJson> datums) {
		this.datums = datums;
	}

	public double getHat() {
		return hat;
	}

	@JsonSetter("HAT")
	public void setHat(double hat) {
		this.hat = hat;
	}

	public String getHatDate() {
		return hatDate;
	}

	@JsonSetter("HATdate")
	public void setHatDate(String hatDate) {
		this.hatDate = hatDate;
	}

	public String getHatTime() {
		return hatTime;
	}

	@JsonSetter("HATtime")
	public void setHatTime(String hatTime) {
		this.hatTime = hatTime;
	}

	public double getLat() {
		return lat;
	}

	@JsonSetter("LAT")
	public void setLat(double lat) {
		this.lat = lat;
	}

	public String getLatDate() {
		return latDate;
	}

	@JsonSetter("LATdate")
	public void setLatDate(String latDate) {
		this.latDate = latDate;
	}

	public String getLatTime() {
		return latTime;
	}

	@JsonSetter("LATtime")
	public void setLatTime(String latTime) {
		this.latTime = latTime;
	}

}
