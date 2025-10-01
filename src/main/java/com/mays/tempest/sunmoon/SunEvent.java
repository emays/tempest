package com.mays.tempest.sunmoon;

import java.time.ZonedDateTime;

public class SunEvent {

	public enum Type {
		Rise, Set, Noon;
	}

	private Sun sun;

	private ZonedDateTime time;

	private Type type;

	public SunEvent(Sun sun, ZonedDateTime time, Type type) {
		super();
		this.sun = sun;
		this.time = time;
		this.type = type;
	}

	public ZonedDateTime getTime() {
		return time;
	}

	public void setTime(ZonedDateTime time) {
		this.time = time;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	@SuppressWarnings("incomplete-switch")
	public double getAzimuth() {
		switch (type) {
		case Rise:
			return sun.getRiseAzimuth();
		case Set:
			return sun.getSetAzimuth();
		}
		return sun.getAzimuth(time);
	}

	public double getNoonAltitude() {
		return sun.getNoonAltitude();
	}

}
