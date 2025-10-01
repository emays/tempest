package com.mays.tempest.tides;

import java.util.List;

public class Datums {

	// https://tidesandcurrents.noaa.gov/datum_options.html

	// https://tidesandcurrents.noaa.gov/glossary.html

	public enum Datum {
		STND("Station Datum"),

		MHHW("Mean Higher-High Water"),

		MHW("Mean High Water"),

		DTL("Mean Diurnal Tide Level"),

		MTL("Mean Tide Level"),

		MSL("Mean Sea Level"),

		MLW("Mean Low Water"),

		MLLW("Mean Lower-Low Water"),

		GT("Great Diurnal Range"),

		MN("Mean Range of Tide"),

		DHQ("Mean Diurnal High Water Inequality"),

		DLQ("Mean Diurnal Low Water Inequality"),

		HWI("Greenwich High Water Interval (in hours)"),

		LWI("Greenwich Low Water Interval (in hours)");

		private String description;

		public String getDescription() {
			return description;
		}

		Datum(String description) {
			this.description = description;
		}

	}

	private List<DatumJson> datums;

	private Tide highestAstronomicalTide;

	private Tide lowestAstronomicalTide;

	public Datums(List<DatumJson> datums) {
		super();
		this.datums = datums;
	}

	private Tide toTide(String hl, String date, String time, double value) {
		return new Tide(new TideJson(hl,
				date.substring(0, 4) + "-" + date.substring(4, 6) + "-" + date.substring(6) + " " + time,
				Double.toString(value - getRawDatum(Datum.MLLW))));
	}

	public Datums(DatumsJson datums) {
		this.datums = datums.getDatums();
		this.highestAstronomicalTide = toTide("H", datums.getHatDate(), datums.getHatTime(), datums.getHat());
		this.lowestAstronomicalTide = toTide("L", datums.getLatDate(), datums.getLatTime(), datums.getLat());
	}

	private double getRawDatum(String datum) {
		for (DatumJson d : datums) {
			if (d.getName().equals(datum))
				return d.getValue();
		}
		return Double.NaN;
	}

	private double getRawDatum(Datum datum) {
		return getRawDatum(datum.name());
	}

	public double getMeanHighWater() {
		return getRawDatum(Datum.MHW) - getRawDatum(Datum.MLLW);
	}

	public double getMeanLowWater() {
		return getRawDatum(Datum.MLW) - getRawDatum(Datum.MLLW);
	}

	public double getMeanLowerLowWater() {
		return getRawDatum(Datum.MLLW) - getRawDatum(Datum.MLLW);
	}

	public Tide getHighestAstronomicalTide() {
		return highestAstronomicalTide;
	}

	public Tide getLowestAstronomicalTide() {
		return lowestAstronomicalTide;
	}

}
