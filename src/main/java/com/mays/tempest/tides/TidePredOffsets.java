package com.mays.tempest.tides;

public class TidePredOffsets {

	private TidePredOffsetsJson json;

	public TidePredOffsets(TidePredOffsetsJson json) {
		super();
		this.json = json;
	}

	public String getType() {
		return json.getType();
	}

	public double getHeightOffsetHighTide() {
		return json.getHeightOffsetHighTide();
	}

	public double getHeightOffsetLowTide() {
		return json.getHeightOffsetLowTide();
	}

	public int getTimeOffsetHighTide() {
		return json.getTimeOffsetHighTide();
	}

	public int getTimeOffsetLowTide() {
		return json.getTimeOffsetLowTide();
	}

	public String getHeightAdjustedType() {
		return json.getHeightAdjustedType();
	}

}
