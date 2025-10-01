package com.mays.tempest.tides;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class DailyTide {

	private LocalDate date;

	private Tide highAm;

	private Tide highPm;

	private Tide lowAm;

	private Tide lowPm;

	private Tide prior;

	private Tide next;
	
	private Tide dstTide;

	public DailyTide(LocalDate date) {
		super();
		this.date = date;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public Tide getHighAm() {
		return highAm;
	}

	public void setHighAm(Tide highAm) {
		this.highAm = highAm;
	}

	public Tide getHighPm() {
		return highPm;
	}

	public void setHighPm(Tide highPm) {
		this.highPm = highPm;
	}

	public Tide getLowAm() {
		return lowAm;
	}

	public void setLowAm(Tide lowAm) {
		this.lowAm = lowAm;
	}

	public Tide getLowPm() {
		return lowPm;
	}

	public void setLowPm(Tide lowPm) {
		this.lowPm = lowPm;
	}

	public Tide getPrior() {
		return prior;
	}

	public void setPrior(Tide prior) {
		this.prior = prior;
	}

	public Tide getNext() {
		return next;
	}

	public void setNext(Tide next) {
		this.next = next;
	}

	public Tide getDstTide() {
		return dstTide;
	}

	public void setDstTide(Tide dstTide) {
		this.dstTide = dstTide;
	}

	private String toString(Tide tide) {
		if (tide == null)
			return "  -  " + String.format("%10s", "");
		return tide.getTime().toLocalTime() + " " + String.format("%4.1f", tide.getValue()) + " " + tide.getType();
	}

	public String toString() {
		return getDate() + "\t" + //
				toString(getHighAm()) + "\t" + toString(getHighPm()) + "\t" + //
				toString(getLowAm()) + "\t" + toString(getLowPm()) + "\t";
	}

	public List<Tide> getTides() {
		return getTides(false);
	}

	public List<Tide> getTides(boolean include_prior_next) {
		List<Tide> tides = new ArrayList<>(Arrays.asList(highAm, highPm, lowAm, lowPm));
		if (include_prior_next) {
			tides.add(prior);
			tides.add(next);
		}
		return tides.stream().filter(Objects::nonNull).sorted(Comparator.comparing(Tide::getTime))
				.collect(Collectors.toList());
	}

	@Deprecated
	public double getTideRange() {
		return Math.abs(getTides().get(0).getValue() - getTides().get(1).getValue());
	}

	@SuppressWarnings("unused")
	@Deprecated
	private int getMinutesToOrFromLevel(Tide tide, double level) {
		// Length of lunar day
		// https://oceanservice.noaa.gov/education/tutorial_tides/tides05_lunarday.html
		double time_between_high_and_low = ((24.0 * 60.0) + 50.0) / 4;
		double rate = getTideRange() / time_between_high_and_low;
		if (tide.getType() == Tide.Type.Low && level < tide.getValue())
			return -1;
		if (tide.getType() == Tide.Type.High && level > tide.getValue())
			return -1;
		double distance = Math.abs(level - tide.getValue());
		double time = distance / rate;
		if (time > time_between_high_and_low)
			return -1;
		return (int) Math.round(time);
	}

}
