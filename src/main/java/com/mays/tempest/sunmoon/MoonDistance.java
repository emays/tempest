package com.mays.tempest.sunmoon;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.DoubleFunction;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mays.util.Util;

public class MoonDistance {

	private static final Logger logger = LoggerFactory.getLogger(MoonDistance.class);

	private static final boolean trace = false;

	private static record PeriodicTerm(int d_co, int m_co, int mp_co, int f_co, int coefficient) {

		private double getValue(double d, double m, double mp, double f, double e, DoubleFunction<Double> func) {
			// multiply the terms whose argument contains M or -M by E,
			// and those containing 2M or -2M by E^2
			double val = func.apply(d_co * Math.toRadians(d) + m_co * Math.toRadians(m) + mp_co * Math.toRadians(mp)
					+ f_co * Math.toRadians(f));
			val = val * coefficient;
			if (Math.abs(m_co) == 1)
				val = val * e;
			if (Math.abs(m_co) == 2)
				val = val * Math.pow(e, 2);
			return val;
		}

		private String coString(int co, String ch) {
			if (co == 0)
				return "    ";
			String num = switch (co) {
			case 1 -> "  ";
			case -1 -> " -";
			default -> String.format("%2d", co);
			};
			return num + ch + " ";
		}

		@Override
		public String toString() {
			return coString(d_co, "D") //
					+ coString(m_co, "M") //
					+ coString(mp_co, "Mp") //
					+ coString(f_co, "F") //
					+ String.format("%,9d", coefficient);
		}

	}

	List<PeriodicTerm> latitude_terms;

	List<PeriodicTerm> longitude_terms;

	List<PeriodicTerm> distance_terms;

	private static MoonDistance instance;

	private MoonDistance() {
	}

	public static MoonDistance getInstance() throws Exception {
		if (instance == null) {
			instance = new MoonDistance();
			instance.init();
		}
		return instance;
	}

	private void logTerms(String tag, List<PeriodicTerm> terms) {
		logger.info("\n" + tag + " " + terms.size() + ":\n"
				+ terms.stream().map(x -> x.toString()).collect(Collectors.joining("\n")));
	}

	private void init() throws Exception {
		latitude_terms = readPeriodicTerms("latitude", false);
		longitude_terms = readPeriodicTerms("longitude-distance", false);
		distance_terms = readPeriodicTerms("longitude-distance", true);
		if (trace) {
			logTerms("Latitude", latitude_terms);
			logTerms("Longtitude", longitude_terms);
			logTerms("Distance", distance_terms);
		}
	}

	private List<PeriodicTerm> readPeriodicTerms(String file, boolean co_offset)
			throws NumberFormatException, IOException {
		ArrayList<PeriodicTerm> terms = new ArrayList<>();
		for (String line : Util.iterable(Util.getResourceLines("/moon-" + file + "-pt.txt").skip(1))) {
			int[] values = new int[5];
			Arrays.fill(values, 0);
			String[] fields = line.split("\\t", -1);
			for (int i = 0; i < 5; i++) {
				String field = fields[(i == 4 && co_offset ? 5 : i)];
				if (!field.isEmpty())
					values[i] = Integer.parseInt(field);
			}
			terms.add(new PeriodicTerm(values[0], values[1], values[2], values[3], values[4]));
		}
		return terms;
	}

	double getT(double jde) {
		// 22.1
		return (jde - 2451_545) / 36_525;
	}

	private double norm360(double v) {
		return v - 360 * (Math.floor(v / 360));
	}

	double getLp(double jde) {
		// Moon's mean longitude 47.1
		double t = getT(jde);
		double lp = 218.316_4477 //
				+ 481_267.881_234_21 * t //
				- 0.001_5786 * Math.pow(t, 2) //
				+ Math.pow(t, 3) / 538_841 //
				- Math.pow(t, 4) / 65_194_000;
		return norm360(lp);
	}

	double getD(double jde) {
		// Moon's mean elongation 47.2
		double t = getT(jde);
		double d = 297.850_1921 //
				+ 445_267.111_4034 * t //
				- 0.001_8819 * Math.pow(t, 2) //
				+ Math.pow(t, 3) / 545_868 //
				- Math.pow(t, 4) / 113_065_000;
		return norm360(d);
	}

	double getM(double jde) {
		// Sun's mean anomaly 47.3
		double t = getT(jde);
		double m = 357.529_1092 //
				+ 35_999.050_2909 * t //
				- 0.000_1536 * Math.pow(t, 2) //
				+ Math.pow(t, 3) / 24_490_000;
		return norm360(m);
	}

	double getMp(double jde) {
		// Moon's mean anomaly 47.4
		double t = getT(jde);
		double mp = 134.963_3964 //
				+ 477_198.867_5055 * t //
				+ 0.008_7414 * Math.pow(t, 2) //
				+ Math.pow(t, 3) / 69_699 //
				- Math.pow(t, 4) / 14_712_000;
		return norm360(mp);
	}

	double getF(double jde) {
		// Moon's argument of latitude (mean distance of the Moon from its ascending
		// node) 47.5
		double t = getT(jde);
		double f = 93.272_0950 //
				+ 483_202.017_5233 * t //
				- 0.003_6539 * Math.pow(t, 2) //
				- Math.pow(t, 3) / 3526_000 //
				+ Math.pow(t, 4) / 863_310_000;
		return norm360(f);
	}

	double getE(double jde) {
		// 47.6
		double t = getT(jde);
		double e = 1 //
				- 0.002_516 * t //
				- 0.000_0074 * Math.pow(t, 2);
		return e;
	}

	double getSumOfTerms(List<PeriodicTerm> terms, double d, double m, double mp, double f, double e,
			DoubleFunction<Double> func) {
		double sum_of_terms = terms.stream().mapToDouble(x -> x.getValue(d, m, mp, f, e, func)).sum();
		return sum_of_terms;
	}

	double getValue(double jde, List<PeriodicTerm> terms, DoubleFunction<Double> func) {
		double d = getD(jde);
		double m = getM(jde);
		double mp = getMp(jde);
		double f = getF(jde);
		double e = getE(jde);
		double sum_of_terms = getSumOfTerms(terms, d, m, mp, f, e, func);
		return sum_of_terms;
	}

	double getSr(double jde) {
		return getValue(jde, distance_terms, Math::cos);
	}

	public double getDistance(double jde) {
		return 385_000.56 + getSr(jde) / 1000;
	}

}
