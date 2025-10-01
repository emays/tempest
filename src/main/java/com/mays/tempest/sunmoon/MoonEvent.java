package com.mays.tempest.sunmoon;

import java.time.ZonedDateTime;

public class MoonEvent {

	public enum Type {
		Rise, Set;
	}

	@SuppressWarnings("unused")
	private Moon moon;

	private ZonedDateTime time;

	private Type type;

	public MoonEvent(Moon moon, ZonedDateTime time, Type type) {
		super();
		this.moon = moon;
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

}
