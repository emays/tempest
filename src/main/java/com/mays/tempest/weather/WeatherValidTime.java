package com.mays.tempest.weather;

import java.time.Duration;
import java.time.ZonedDateTime;

public class WeatherValidTime {

	ZonedDateTime time;

	Duration duration;

	public WeatherValidTime(ZonedDateTime time, Duration duration) {
		super();
		this.time = time;
		this.duration = duration;
	}

	public static WeatherValidTime parse(String valid_time) {
		String[] tp = valid_time.split("/");
		return new WeatherValidTime(ZonedDateTime.parse(tp[0]), Duration.parse(tp[1]));
	}

	public ZonedDateTime getTime() {
		return time;
	}

	public Duration getDuration() {
		return duration;
	}

	@Override
	public String toString() {
		return time + "/" + duration;
	}

	public ZonedDateTime getEndTime() {
		return time.plus(duration);
	}

}
