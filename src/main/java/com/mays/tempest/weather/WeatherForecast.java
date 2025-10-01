package com.mays.tempest.weather;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import javax.measure.Quantity;
import javax.measure.quantity.Length;

import com.mays.tempest.weather.WeatherForecastGeneral.Period;
import com.mays.tempest.weather.WeatherForecastGridData.ForecastElement;

public class WeatherForecast {

	private WeatherForecastGeneral general;

	private WeatherForecastGridData gridData;

	private List<WeatherForecastGridDataSlice> gridDataSlices;

	public WeatherForecast(WeatherForecastGeneral general, WeatherForecastGridData gridData) {
		super();
		this.general = general;
		this.gridData = gridData;
		gridDataSlices = getForecastGridData(gridData);
	}

	public WeatherForecastGeneral getGeneral() {
		return general;
	}

	public WeatherForecastGridData getGridData() {
		return gridData;
	}

	public List<WeatherForecastGridDataSlice> getGridDataSlices() {
		return gridDataSlices;
	}

	private <Q extends Quantity<Q>> void assignForecastGridData(
			TreeMap<ZonedDateTime, WeatherForecastGridDataSlice> fcs, List<ForecastElement<Q>> gds,
			BiConsumer<WeatherForecastGridDataSlice, Quantity<Q>> setter) {
		for (ForecastElement<Q> gd : gds) {
			fcs.putIfAbsent(gd.getValidTime().getTime(), new WeatherForecastGridDataSlice(gd.getValidTime().getTime()));
			WeatherForecastGridDataSlice fc = fcs.get(gd.getValidTime().getTime());
			setter.accept(fc, gd.getQuantity());
		}
	}

	private List<WeatherForecastGridDataSlice> getForecastGridData(WeatherForecastGridData wfgd) {
		TreeMap<ZonedDateTime, WeatherForecastGridDataSlice> fcs = new TreeMap<>();
		assignForecastGridData(fcs, wfgd.getTemperature(), WeatherForecastGridDataSlice::setTemperature);
		assignForecastGridData(fcs, wfgd.getDewpoint(), WeatherForecastGridDataSlice::setDewpoint);
		assignForecastGridData(fcs, wfgd.getRelativeHumidity(), WeatherForecastGridDataSlice::setRelativeHumidity);
		assignForecastGridData(fcs, wfgd.getHeatIndex(), WeatherForecastGridDataSlice::setHeatIndex);
		assignForecastGridData(fcs, wfgd.getWindChill(), WeatherForecastGridDataSlice::setWindChill);
		assignForecastGridData(fcs, wfgd.getSkyCover(), WeatherForecastGridDataSlice::setSkyCover);
		assignForecastGridData(fcs, wfgd.getWindDirection(), WeatherForecastGridDataSlice::setWindDirection);
		assignForecastGridData(fcs, wfgd.getWindSpeed(), WeatherForecastGridDataSlice::setWindSpeed);
		assignForecastGridData(fcs, wfgd.getWindGust(), WeatherForecastGridDataSlice::setWindGust);
		assignForecastGridData(fcs, wfgd.getProbabilityOfPrecipitation(),
				WeatherForecastGridDataSlice::setProbabilityOfPrecipitation);
		assignForecastGridData(fcs, wfgd.getQuantitativePrecipitation(),
				WeatherForecastGridDataSlice::setQuantitativePrecipitation);
		assignForecastGridData(fcs, wfgd.getCeilingHeight(), WeatherForecastGridDataSlice::setCeilingHeight);
		assignForecastGridData(fcs, wfgd.getVisibility(), WeatherForecastGridDataSlice::setVisibility);
		return new ArrayList<WeatherForecastGridDataSlice>(fcs.values());
	}

	public ArrayList<Period> getPeriods() {
		return general.getPeriods();
	}

	public Quantity<Length> getElevation() {
		return general.getElevation();
	}

	public ZonedDateTime getStartTime() {
		return general.getStartTime();
	}

	public ZonedDateTime getEndTime() {
		return general.getEndTime();
	}
	
	public ZonedDateTime getUpdateTime() {
		return general.getUpdateTime();
	}

	public List<WeatherForecastGridDataSlice> getSlices(Period period) {
		return getGridDataSlices().stream()
				.filter(slice -> (period.getStartTime().isBefore(slice.getTime())
						|| period.getStartTime().isEqual(slice.getTime()))
						&& period.getEndTime().isAfter(slice.getTime()))
				.collect(Collectors.toList());
	}

	public WeatherForecastGridDataSlice getSlice(ZonedDateTime time) {
		return getGridDataSlices().stream()
				.filter(slice -> (time.isAfter(slice.getTime()) || time.isEqual(slice.getTime()))
						&& time.isBefore(slice.getTime().plusHours(1)))
				.findFirst().orElse(null);
	}

	public Period getPeriod(ZonedDateTime time) {
		return general.getPeriod(time);
	}

}
