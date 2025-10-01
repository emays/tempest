package com.mays.tempest.geo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ContiguousUS {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(ContiguousUS.class);

	/*
	 * 
	 * https://www.usgs.gov/faqs/what-constitutes-united-states-what-are-official-
	 * definitions
	 * 
	 * On May 14, 1959, the U.S. Board on Geographic Names issued the following
	 * definitions based partially on the Alaska Omnibus Bill, which defined the
	 * Continental United States as
	 * "the 49 States on the North American Continent and the District of Columbia..."
	 * The Board reaffirmed these definitions on May 13, 1999.
	 * 
	 * United States: The 50 States and the District of Columbia.
	 * 
	 * Continental United States: The 49 States (including Alaska, excluding Hawaii)
	 * located on the continent of North America, and the District of Columbia.
	 * 
	 * Conterminous United States: The 48 States and the District of Columbia; that
	 * is, the United States prior to January 3, 1959 (Alaska Statehood), wholly
	 * filling an unbroken block of territory and excluding Alaska and Hawaii.
	 * Although the official reference applies the term "conterminous," many use the
	 * word "contiguous," which is almost synonymous and better known.
	 * 
	 */

	/*
	 * https://gadm.org/download_country_v3.html
	 * 
	 * United States KMZ: level-0
	 * 
	 * gadm36_USA_0.kmz unzip to gadm36_USA_0.kml
	 * 
	 * Bookmark: http://diva-gis.org/
	 * 
	 * Bookmark: https://www.naturalearthdata.com/
	 * 
	 */

	// https://coastalmap.marine.usgs.gov/mapit/

	// https://www.generic-mapping-tools.org/

	/*
	 * https://en.wikipedia.org/wiki/List_of_extreme_points_of_the_United_States
	 * 
	 * http://www.cohp.org/extremes/extreme_points.html
	 * 
	 * Contiguous 48 United States
	 * 
	 * Northernmost - Northwest Angle, Minnesota (49.38407°N, 95.15274° W)
	 * 
	 * Southernmost - Ballast Key, Florida (24.52° N, 81.965° W) The southernmost
	 * land permanently above the waterline.
	 * 
	 * Easternmost - West Quoddy Head, Maine (44.81335° N, 66.94975° W) The
	 * easternmost land permanently above the waterline.
	 * 
	 * Westernmost - Bodelteh Islands, Washington (48.17761° N, 124.76479° W) The
	 * westernmost land permanently above the waterline.
	 */

	public static final Coordinate NORTHERNMOST = new Coordinate(49.38407, -95.15274);

//	public static final double NORTHERN_BOUND = Math.ceil(NORTHERNMOST.getLatitude() * 10.0) / 10.0;

	public static final Coordinate SOUTHERNMOST = new Coordinate(24.52, -81.965);

//	public static final double SOUTHERN_BOUND = Math.floor(SOUTHERNMOST.getLatitude() * 10.0) / 10.0;

	public static final Coordinate EASTERNMOST = new Coordinate(44.81335, -66.94975);

//	public static final double EASTERN_BOUND = Math.ceil(EASTERNMOST.getLongitude() * 10.0) / 10.0;

	public static final Coordinate WESTERNMOST = new Coordinate(48.17761, -124.76479);

//	public static final double WESTERN_BOUND = Math.floor(WESTERNMOST.getLongitude() * 10.0) / 10.0;

	public boolean inBounds(Coordinate coord) {
		return coord.inBounds(NORTHERNMOST, SOUTHERNMOST, EASTERNMOST, WESTERNMOST);
	}

}
