package com.mays.tempest.tides;

import java.time.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TideCalc {

	private static final Logger logger = LoggerFactory.getLogger(TideCalc.class);

	private static final boolean trace = false;

	/// https://courses.lumenlearning.com/precalctwo/chapter/graphs-of-the-sine-and-cosine-function/

	// https://study.com/academy/answer/at-high-tide-the-water-level-at-a-particular-boat-dock-is-9-feet-deep-at-low-tide-the-water-is-3-feet-deep-on-a-certain-day-the-low-tide-occurs-at-3-am-and-high-tide-occurs-at-9-am-find-an-equation-for-the-height-of-the-tide-at-time-t-where-t-3-is.html

	// height = amplitude * sin(b * time + c) + centerline

	// amplitude = (high - low) / 2

	// centerline = (high + low) /2

	// high tide: sin(b * 0 + c) = 1

	// sin(c) = 1

	// c = pi / 2

	// low tide: sin(b * time_between + c) = -1

	// sin(b * time_between + pi / 2 ) = -1

	// b * time_between + pi / 2 = 3 pi / 2

	// b = pi / time_between

	// height = amplitude * sin((pi / time_between) * time + pi / 2) + centerline

	// solve for time

	// amplitude * sin((pi / time_between) * time + pi / 2) + centerline = height

	// sin((pi / time_between) * time + pi / 2) = (height - centerline) / amplitude

	// (pi / time_between) * time + pi / 2 =
	// arcsin((height - centerline) / amplitude)

	// (pi / time_between) * time =
	// arcsin((height - centerline) / amplitude) - pi / 2

	// time =
	// (arcsin((height - centerline) / amplitude) - pi / 2) / (pi / time_between)

	public static double getLevelAfterMinutes(Tide fr_tide, Tide to_tide, int minutes) {
		double time_between = Duration.between(fr_tide.getTime(), to_tide.getTime()).toMinutes();
		double amplitude = (fr_tide.getValue() - to_tide.getValue()) / 2;
		double centerline = (fr_tide.getValue() + to_tide.getValue()) / 2;
		return ((amplitude * Math.sin(((Math.PI / time_between) * minutes) + (Math.PI / 2))) + centerline);
	}

	public static int getMinutesToLevel(Tide fr_tide, Tide to_tide, double level) {
		double time_between = Duration.between(fr_tide.getTime(), to_tide.getTime()).toMinutes();
		double amplitude = (fr_tide.getValue() - to_tide.getValue()) / 2;
		double centerline = (fr_tide.getValue() + to_tide.getValue()) / 2;
		if (trace)
			logger.info("> " + (level - centerline));
		double x = (level - centerline) / amplitude;
		if (trace) {
			logger.info("> " + x);
			logger.info("> " + Math.asin(x));
		}
		if (Math.abs(x) > 1)
			x = Math.round(x);
		if (trace) {
			logger.info("> " + x);
			logger.info("> " + Math.asin(x));
		}
		return (int) Math.abs(Math.round((Math.asin(x) - (Math.PI / 2)) / (Math.PI / time_between)));
	}

}
