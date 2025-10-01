package com.mays.tempest.tides;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Tide {

	public enum Type {
		High, Low;
	}

	private TideJson tideJson;

//	private LocalDateTime time;

//	private boolean dstAdjust = false;

	public Tide(TideJson tideJson) {
		super();
		this.tideJson = tideJson;
//		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
//		time = LocalDateTime.parse(tideJson.getTime(), dtf);
//		if (TimeUtil.isDstEnd(time.toLocalDate()) && time.getHour() < 2) {
//			time = time.minusHours(1);
//			dstAdjust = true;
//		}
	}

	public LocalDateTime getTime() {
//		return time;
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		return LocalDateTime.parse(tideJson.getTime(), dtf);
	}

//	public boolean isDstAdjust() {
//		return dstAdjust;
//	}

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

}
