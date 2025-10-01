package com.mays.tempest.seasons;

public class SeasonJson {

//	{
//	      "day": 2, 
//	      "month": 1, 
//	      "phenom": "Perihelion", 
//	      "time": "06:25", 
//	      "year": 1900
//	}

	private int day;

	private int month;

	private String phenom;

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

	public String getPhenom() {
		return phenom;
	}

	public void setPhenom(String phenom) {
		this.phenom = phenom;
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
		return phenom + " @ " + month + "/" + day + "/" + year + " " + time;
	}

}
