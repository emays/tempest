package com.mays.tempest.buoys;

import java.util.ArrayList;
import java.util.stream.Collectors;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Buoys {

	private static final Logger logger = LoggerFactory.getLogger(Buoys.class);

	private static final boolean trace = false;

	// https://www.ndbc.noaa.gov/measdes.shtml

	// https://www.ndbc.noaa.gov/data/realtime2/44018.txt

	// https://www.ndbc.noaa.gov/data/5day2/44018_5day.txt

	// YY MM DD hh mm WDIR WSPD GST WVHT DPD APD MWD PRES ATMP WTMP DEWP VIS PTDY
	// TIDE
	//
	// yr mo dy hr mn degT m/s m/s m sec sec degT hPa degC degC degC nmi hPa ft
	
	// glossary - https://www.ndbc.noaa.gov/waveobs.shtml

	public String getObservationsString(String station) throws Exception {
		ClientBuilder builder = ClientBuilder.newBuilder();
		Client client = builder.build();
		String uri = "https://www.ndbc.noaa.gov/data/realtime2/" + station.toUpperCase() + ".txt";
		WebTarget target = client.target(uri);
		Response resp = target.request(MediaType.TEXT_PLAIN).get();
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

	private Integer mmInteger(String str) {
		if (str.equals("MM"))
			return null;
		return Integer.parseInt(str);
	}

	private Double mmDouble(String str) {
		if (str.equals("MM"))
			return null;
		return Double.parseDouble(str);
	}

	public ArrayList<BuoyObservation> getObservationsFromString(String observations) {
		ArrayList<BuoyObservation> ret = new ArrayList<>();
		for (String line : observations.lines().filter(x -> !x.startsWith("#")).collect(Collectors.toList())) {
			String[] field = line.split("\\s+");
			BuoyObservation obs = new BuoyObservation(Integer.parseInt(field[0]), Integer.parseInt(field[1]),
					Integer.parseInt(field[2]), Integer.parseInt(field[3]), Integer.parseInt(field[4]), //
					mmInteger(field[5]), mmDouble(field[6]), mmDouble(field[7]), //
					mmDouble(field[8]), mmInteger(field[11]), //
					mmDouble(field[12]), mmDouble(field[13]), mmDouble(field[14]), mmDouble(field[15]),
					mmDouble(field[16]));
			ret.add(obs);
		}
		return ret;
	}

	public ArrayList<BuoyObservation> getObservations(String station) throws Exception {
		String obs = getObservationsString(station);
		return getObservationsFromString(obs);
	}

}
