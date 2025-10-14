package com.mays.tempest.tides;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Tide {

	public enum Type {
		High, Low;
	}

	private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

	private TideJson tideJson;

	public Tide(TideJson tideJson) {
		super();
		this.tideJson = tideJson;
	}

	public LocalDateTime getTime() {
		return LocalDateTime.parse(tideJson.getTime(), dtf);
	}

	public double getValue() {
		return Double.parseDouble(tideJson.getValue());
	}

	public Type getType() {
		switch (tideJson.getType()) {
		case "H":
			return Type.High;
		case "L":
			return Type.Low;
		}
		throw new UnsupportedOperationException(tideJson.getType());
	}

	public String toString() {
		return getTime() + " " + String.format("%.1f", getValue()) + " " + getType();
	}

	public TideJson toTideJson() {
		return new TideJson(this.getType().toString().substring(0, 1), this.getTime().format(dtf),
				String.format("%.1f", this.getValue()));
	}

}
