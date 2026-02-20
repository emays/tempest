package com.mays.tempest;

import java.time.ZoneId;

import com.mays.tempest.geo.Coordinate;

public class LocationInfo {

	private Coordinate coordinate;

	private ZoneId timeZone;

	private String tideStationId;

	private String buoyStationId;

	private String weatherStationId;

	public Coordinate getCoordinate() {
		return coordinate;
	}

	public ZoneId getTimeZone() {
		return timeZone;
	}

	public String getTideStationId() {
		return tideStationId;
	}

	public String getBuoyStationId() {
		return buoyStationId;
	}

	public String getWeatherStationId() {
		return weatherStationId;
	}

	public double getLatitude() {
		return coordinate.getLatitude();
	}

	public double getLongitude() {
		return coordinate.getLongitude();
	}

	private LocationInfo(double latitude, double longitude, ZoneId timeZone, String tideStationId, String buoyStationId,
			String weatherStationId) {
		super();
		this.coordinate = new Coordinate(latitude, longitude);
		this.timeZone = timeZone;
		this.tideStationId = tideStationId;
		this.buoyStationId = buoyStationId;
		this.weatherStationId = weatherStationId;
	}

	public static LocationInfo of(double latitude, double longitude, ZoneId timeZone, String tideStationId,
			String buoyStationId, String weatherStationId) {
		return new LocationInfo(latitude, longitude, timeZone, tideStationId, buoyStationId, weatherStationId);
	}

	private static LocationInfo of(double latitude, double longitude, ZoneId timeZone, TideStationId tideStationId,
			BuoyStationId buoyStationId, WeatherStationId weatherStationId) {
		return new LocationInfo(latitude, longitude, timeZone, tideStationId.id, buoyStationId.id, weatherStationId.id);
	}

	public enum TideStationId {
		Boston("8443970"), Provincetown("8446121"), Wellfleet("8446613");

		public String id;

		private TideStationId(String id) {
			this.id = id;
		}
	}

	public enum BuoyStationId {
		// TODO Provincetown is actually Cape Cod
		BostonApproach("44013"), CapeCodBay("44090"), Provincetown("44018");

		public String id;

		private BuoyStationId(String id) {
			this.id = id;
		}
	}

	public enum WeatherStationId {
		Boston("KBOS"), Provincetown("KPVC");

		public String id;

		private WeatherStationId(String id) {
			this.id = id;
		}
	}

	private static final ZoneId tz = ZoneId.of("America/New_York");

	public static final LocationInfo BOSTON = LocationInfo.of(42.353_888, -71.050_277, tz, TideStationId.Boston,
			BuoyStationId.BostonApproach, WeatherStationId.Boston);

	public static final LocationInfo COLD_STORAGE = LocationInfo.of(42.030_785, -70.093_849, tz, TideStationId.Provincetown,
			BuoyStationId.CapeCodBay, WeatherStationId.Provincetown);

	public static final LocationInfo PAMET = LocationInfo.of(41.991_605, -70.071_953, tz, TideStationId.Wellfleet,
			BuoyStationId.CapeCodBay, WeatherStationId.Provincetown);

	public static final LocationInfo PROVINCETOWN = LocationInfo.of(42.049_592, -70.182_158, tz, TideStationId.Provincetown,
			BuoyStationId.CapeCodBay, WeatherStationId.Provincetown);

	public static final LocationInfo WELLFLEET = LocationInfo.of(41.929_669, -70.029_542, tz, TideStationId.Wellfleet,
			BuoyStationId.CapeCodBay, WeatherStationId.Provincetown);

}
