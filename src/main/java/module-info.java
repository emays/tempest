module com.mays.tempest {

	requires transitive com.mays.util;

	requires com.fasterxml.jackson.databind;

	requires jakarta.ws.rs;

	requires transitive org.shredzone.commons.suncalc;

	requires transitive java.measure;
	requires si.uom.units;
	requires systems.uom.common;
	requires tech.units.indriya;
	
	requires com.esri.core.geometry;
	requires org.geoson;

	exports com.mays.tempest;
	exports com.mays.tempest.buoys;
	exports com.mays.tempest.geo;
	exports com.mays.tempest.seasons;
	exports com.mays.tempest.sunmoon;
	exports com.mays.tempest.tides;
	exports com.mays.tempest.timezone;
	exports com.mays.tempest.weather;

}
