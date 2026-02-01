package com.mays.tempest.sunmoon;

public class PhaseXJson {

//	{
//	      "day": 1, 
//	      "month": 1, 
//	      "phase": "New Moon", 
//	      "time": "13:52", 
//	      "year": 1900
//	}

	private int day;

	private int month;

	private String phase;

	private String time;

	private int year;

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public String getPhase() {
		return phase;
	}

	public void setPhase(String phase) {
		this.phase = phase;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	@Override
	public String toString() {
		return phase + " @ " + month + "/" + day + "/" + year + " " + time;
	}

}
