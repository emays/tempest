package com.mays.tempest.weather;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UvForecast {

	private static final Logger logger = LoggerFactory.getLogger(UvForecast.class);

	private static final boolean trace = false;
	
	// https://www.weather.gov/rah/uv

	// https://www.epa.gov/enviro/web-services#uvindex

	// https://enviro.epa.gov/enviro/efservice/getEnvirofactsUVHOURLY/ZIP/{ZIP
	// Code}/{Output Format}

	// https://enviro.epa.gov/enviro/efservice/getEnvirofactsUVHOURLY/ZIP/02652/json

	// https://enviro.epa.gov/enviro/efservice/getEnvirofactsUVHOURLY/CITY/{City
	// Name}/STATE/{State Abbreviation}/{Output Format}

	// https://enviro.epa.gov/enviro/efservice/getEnvirofactsUVHOURLY/CITY/north%20truro/STATE/ma/json
	
	//
	
	// https://www.cpc.ncep.noaa.gov/products/stratosphere/uv_index/uv_global.shtml
	
	// https://ftpprd.ncep.noaa.gov/data/nccf/com/hourly/prod/

	public static String getForecastJsonStringUri(String uri) throws Exception {
		ClientBuilder builder = ClientBuilder.newBuilder();
		Client client = builder.build();
		WebTarget target = client.target(uri);
		Response resp = target.request(MediaType.APPLICATION_JSON).get();
		if (resp.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL) {
			String json = resp.readEntity(String.class);
			if (trace)
				logger.info("\n" + json);
			return json;
		}
		String message = resp.getStatus() + " " + resp.readEntity(String.class);
		logger.error(message);
		throw new Exception(message);
	}

	public static String getForecastJsonString(String zip) throws Exception {
		return getForecastJsonStringUri(
				"https://enviro.epa.gov/enviro/efservice/getEnvirofactsUVHOURLY/ZIP/" + zip + "/json");
	}

	public static String getForecastJsonString(String city, String state) throws Exception {
		return getForecastJsonStringUri("https://enviro.epa.gov/enviro/efservice/getEnvirofactsUVHOURLY/CITY/" + city
				+ "/STATE/" + state + "/json");
	}

}
