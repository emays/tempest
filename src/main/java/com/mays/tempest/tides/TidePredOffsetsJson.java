package com.mays.tempest.tides;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TidePredOffsetsJson {

//	{
//		  "refStationId": "8443970",
//		  "type": "S",
//		  "heightOffsetHighTide": 1.05,
//		  "heightOffsetLowTide": 1.05,
//		  "timeOffsetHighTide": 14,
//		  "timeOffsetLowTide": 30,
//		  "heightAdjustedType": "R",
//		  "self": null
//	}

	private String type;

	private double heightOffsetHighTide;

	private double heightOffsetLowTide;

	private int timeOffsetHighTide;

	private int timeOffsetLowTide;

	private String heightAdjustedType;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public double getHeightOffsetHighTide() {
		return heightOffsetHighTide;
	}

	public void setHeightOffsetHighTide(double heightOffsetHighTide) {
		this.heightOffsetHighTide = heightOffsetHighTide;
	}

	public double getHeightOffsetLowTide() {
		return heightOffsetLowTide;
	}

	public void setHeightOffsetLowTide(double heightOffsetLowTide) {
		this.heightOffsetLowTide = heightOffsetLowTide;
	}

	public int getTimeOffsetHighTide() {
		return timeOffsetHighTide;
	}

	public void setTimeOffsetHighTide(int timeOffsetHighTide) {
		this.timeOffsetHighTide = timeOffsetHighTide;
	}

	public int getTimeOffsetLowTide() {
		return timeOffsetLowTide;
	}

	public void setTimeOffsetLowTide(int timeOffsetLowTide) {
		this.timeOffsetLowTide = timeOffsetLowTide;
	}

	public String getHeightAdjustedType() {
		return heightAdjustedType;
	}

	public void setHeightAdjustedType(String heightAdjustedType) {
		this.heightAdjustedType = heightAdjustedType;
	}

}
