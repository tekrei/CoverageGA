/**
 *  This file is part of CoverageGA
 *  
 *  CoverageGA is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or 
 *  (at your option) any later version.
 *  
 *  CoverageGA is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License
 *  along with CoverageGA.  If not, see <http://www.gnu.org/licenses/>. 
 */
package cga.data;

import java.util.Vector;

import cga.data.ga.GAParameters;

public class SensorInfo {

	private static SensorInfo instance = null;

	public static SensorInfo getInstance() {
		if (instance == null)
			instance = new SensorInfo();
		return instance;
	}

	public static void load(final Parameter _parameter,
			final Vector<Hotspot> _hotspots, final Vector<Sensor> _sensors) {
		getInstance().parameter = _parameter;
		getInstance().setHotSpots(_hotspots);
		getInstance().setSensors(_sensors);
	}

	public boolean saveEliteImage = false;

	public GAParameters gaParameter;
	private Vector<Hotspot> hotspots;
	public Parameter parameter;

	private Vector<Sensor> sensors;

	private SensorInfo() {
		parameter = new Parameter();
		gaParameter = new GAParameters();
	}

	public void addHotspot(final Hotspot hotspot) {
		if (hotspots == null) {
			hotspots = new Vector<Hotspot>();
		}
		hotspots.add(hotspot);
	}

	public void addSensor(final Sensor sensor) {
		if (sensors == null) {
			sensors = new Vector<Sensor>();
		}
		sensors.add(sensor);
	}

	public Vector<Hotspot> getHotSpots() {
		return hotspots;
	}

	public Sensor getSensor(final int i) {
		if (sensors != null)
			return sensors.get(i);
		return null;
	}

	public int getSensorCount() {
		if (sensors != null)
			return sensors.size();
		return 0;
	}

	public Vector<Sensor> getSensors() {
		return sensors;
	}

	public void setHotSpots(final Vector<Hotspot> areaHotSpots) {
		hotspots = areaHotSpots;
	}

	public void setSensors(final Vector<Sensor> sensorler) {
		sensors = sensorler;
	}

	@Override
	public String toString() {
		String str = onlyParameters();
		for (final Sensor sensor : sensors) {
			str += "\n" + sensor;
		}
		for (final Hotspot hs : hotspots) {
			str += "\n" + hs;
		}
		return str;
	}

	public String onlyParameters() {
		String str = parameter.toString();
		str += "\n" + gaParameter;
		return str;
	}

	public void reset() {
		instance = new SensorInfo();
	}
}
