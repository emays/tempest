package com.mays.tempest.seasons;

import java.util.List;

public class Seasons {

	private List<Season> seasons;

	public Seasons(List<Season> seasons) {
		this.seasons = seasons;
	}

	public List<Season> getSeasons() {
		return seasons;
	}

	public Season getSeason(Season.Phenom phenom) {
		return seasons.stream().filter(x -> x.getPhenom().equals(phenom)).findFirst().orElseThrow();
	}

}
