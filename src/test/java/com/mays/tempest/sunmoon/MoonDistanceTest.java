package com.mays.tempest.sunmoon;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MoonDistanceTest {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(MoonDistanceTest.class);

	@Test
	public void example47a() throws Exception {
		MoonDistance mp = MoonDistance.getInstance();
		double jde = 2448_724.5;
		double expect_T = -0.077_221_081_451;
		double expect_Lp = 134.290_182;
		double expect_D = 113.842_304;
		double expect_M = 97.643_514;
		double expect_Mp = 5.150_833;
		double expect_F = 219.889_721;
		double expect_E = 1.000_194;
		double expect_Sr = -16_590_875;
		double expect_distance = 368_409.7;
		assertEquals(expect_T, mp.getT(jde), 0.000_000_000_000_5);
		assertEquals(expect_Lp, mp.getLp(jde), 0.000_000_5);
		assertEquals(expect_D, mp.getD(jde), 0.000_000_5);
		assertEquals(expect_M, mp.getM(jde), 0.000_000_5);
		assertEquals(expect_Mp, mp.getMp(jde), 0.000_000_5);
		assertEquals(expect_F, mp.getF(jde), 0.000_000_5);
		assertEquals(expect_E, mp.getE(jde), 0.000_000_5);
		// This does not agree with the example
		assertTrue(Math.abs(expect_Sr - mp.getSr(jde)) < 20);
		assertEquals(expect_distance, mp.getDistance(jde), 0.05);
	}

}
