package com.mays.tempest.tides;

import com.fasterxml.jackson.annotation.JsonSetter;

public class TideJson {

	private String time;

	private String value;

	private String type;

	public TideJson() {
		super();
	}

	public TideJson(String type, String time, String value) {
		super();
		this.type = type;
		this.time = time;
		this.value = value;
	}

	public String getTime() {
		return time;
	}

	@JsonSetter("t")
	public void setTime(String time) {
		this.time = time;
	}

	public String getValue() {
		return value;
	}

	@JsonSetter("v")
	public void setValue(String value) {
		this.value = value;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String toString() {
		return time + " " + value + " " + type;
	}

}
