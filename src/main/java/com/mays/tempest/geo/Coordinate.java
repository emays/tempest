package com.mays.tempest.geo;

public class Coordinate {

	protected double latitude;

	protected double longitude;

	public Coordinate(double latitude, double longitude) {
		super();
		this.latitude = latitude;
		this.longitude = longitude;
	}

	protected Coordinate() {
	}

	public static Coordinate of(double latitude, double longitude) {
		return new Coordinate(latitude, longitude);
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public static double toDecimal(int degrees, int minutes) {
		return toDecimal(degrees, minutes, 0);
	}

	public static double toDecimal(int degrees, int minutes, int seconds) {
		if (degrees < -180 || degrees > 180)
			throw new IllegalArgumentException("Degrees: " + degrees);
		double sign = 1;
		if (degrees < 0)
			sign = -1;
		degrees = Math.abs(degrees);
		if (minutes < 0 || minutes > 60)
			throw new IllegalArgumentException("Minutes: " + minutes);
		if (seconds < 0 || seconds > 60)
			throw new IllegalArgumentException("Seconds: " + seconds);
		return sign * (degrees + minutes / 60.0 + seconds / 3600.0);
	}

	public static boolean isValid(double latitude, double longitude) {
		return latitude >= -90 && latitude <= 90 && longitude >= -180 && longitude <= 180;
	}

	public static String isValidString(double latitude, double longitude) {
		return latitude + " >= -90 & <= 90 " + longitude + " >= -180 & <= 180";
	}

	public boolean isValid() {
		return isValid(latitude, longitude);
	}

	public boolean inBounds(double northern, double southern, double eastern, double western) {
		return this.getLatitude() <= northern && //
				this.getLatitude() >= southern && //
				this.getLongitude() <= eastern && //
				this.getLongitude() >= western;
	}

	public String inBoundsString(double northern, double southern, double eastern, double western) {
		return "Bounds for latitude " + latitude + ": > " + southern + " & < " + northern + "\n" //
				+ "Bounds for longitude " + longitude + ": > " + western + " & < " + eastern;
	}

	public boolean inBounds(Coordinate northern, Coordinate southern, Coordinate eastern, Coordinate western) {
		return inBounds(northern.getLatitude(), southern.getLatitude(), eastern.getLongitude(), western.getLongitude());
	}

	public String toString() {
		return String.format("%.5f", latitude) + " " + String.format("%.5f", longitude);
	}

}
