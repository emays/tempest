package com.mays.tempest.sunmoon;

import java.time.ZonedDateTime;

public interface MoonBase {

	ZonedDateTime getRise();

	ZonedDateTime getSet();

	double getFraction();

	double getPhase();

	String getPhaseName();

	default String getShortPhaseName() {
		String name = getPhaseName();
		name = name.replace("Waxing", "Wax");
		name = name.replace("Waning", "Wan");
		name = name.replace("Gibbous", "Gib");
		name = name.replace("Crescent", "Cres");
		name = name.replace("Quarter", "Qtr");
		return name;
	}

	double getAngle();

	double getDistance();

}