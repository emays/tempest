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

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
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
