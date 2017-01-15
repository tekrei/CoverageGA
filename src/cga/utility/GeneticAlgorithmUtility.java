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
package cga.utility;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Vector;

import cga.data.Circle;
import cga.data.Hotspot;
import cga.data.Sensor;
import cga.data.SensorInfo;
import cga.data.ga.Movement;

public class GeneticAlgorithmUtility {
	private static GeneticAlgorithmUtility instance = null;

	public static GeneticAlgorithmUtility getInstance() {
		if (instance == null)
			instance = new GeneticAlgorithmUtility();
		return instance;
	}

	private Graphics2D g2d;
	private BufferedImage image;

	private GeneticAlgorithmUtility() {
	}

	public Sensor applyMovement(final Sensor sensor, final Movement mi) {
		return new Sensor(sensor.getId(), sensor.getX() + mi.getXInc(), sensor.getY() + mi.getYInc());
	}

	public void applyMovement(final Vector<Sensor> sensors, final Movement[] movement) {
		for (int i = 0; i < sensors.size(); i++) {
			sensors.set(i, applyMovement(sensors.get(i), movement[i]));
		}
	}

	public int calculateArea(final Vector<Sensor> sensors, final SensorInfo sensorInfo, final String fileNamePrefix,
			final boolean resimKaydet) {
		// background must be white
		g2d.setPaint(Color.WHITE);
		g2d.fill(new Rectangle2D.Double(0, 0, sensorInfo.parameter.getAreaWidth(),
				sensorInfo.parameter.getAreaHeight()));
		// draw sensors
		SensorDrawUtility.drawSensors(g2d, sensorInfo, sensors);
		// draw hotspots
		g2d.setColor(Color.BLUE);
		SensorDrawUtility.drawHotspots(g2d, sensorInfo.parameter.sensorRadius, sensorInfo.getHotSpots());
		// calculate area
		return SensorUtility.calculateArea(image, fileNamePrefix, resimKaydet);
	}

	public int calculateFitness(final Vector<Sensor> sensors, final SensorInfo sensorInfo) {
		final int coverageArea = calculateArea(sensors, sensorInfo, null, false);
		final int connectedComponentCount = countConnectedComponents(sensors);
		final int distanceBetweenConComps = distanceBetweenConnectedComponents(connectedComponentCount, sensors);
		final int connectivityFitness = distanceBetweenConComps * connectedComponentCount;
		// if hotspots are not k-covered
		if (!isKCovered(sensors, sensorInfo))
			return -100000000;
		// if not all sensors are connected
		if (connectedComponentCount > 1)
			return -connectivityFitness;
		return coverageArea;
	}

	protected int distanceBetweenConnectedComponents(final int connectedComponentCount, final Vector<Sensor> sensors) {
		final Vector<Sensor>[] connectedComponents = new Vector[connectedComponentCount];
		for (final Sensor sensor : sensors) {
			if (connectedComponents[sensor.getConnectedComponentNumber() - 1] == null)
				connectedComponents[sensor.getConnectedComponentNumber() - 1] = new Vector<Sensor>();
			connectedComponents[sensor.getConnectedComponentNumber() - 1].add(sensor);
		}
		int totalDistance = 0;
		for (int i = 0; i < connectedComponents.length; i++) {
			for (int j = i + 1; j < connectedComponents.length; j++) {
				totalDistance += distanceBetween(connectedComponents[i], connectedComponents[j]);
			}
		}
		return totalDistance;
	}

	private int countConnectedComponents(final Vector<Sensor> _sensors) {
		createNeighbourhood(_sensors);
		int groupNumber = 0;
		for (final Sensor _sensor : _sensors) {
			if (_sensor.getStatus() == Sensor.WHITE) {
				dfs(_sensor, ++groupNumber);
			}
		}
		return groupNumber;
	}

	private void createNeighbourhood(final Vector<Sensor> sensors) {
		for (final Sensor sensor : sensors) {
			sensor.clearNeighbours();
			sensor.setStatus(Sensor.WHITE);
			for (final Sensor _sensor : sensors) {
				if (sensor.equals(_sensor))
					continue;
				if (sensor.connectedTo(_sensor)) {
					sensor.addNeighbour(_sensor);
				}
			}
		}
	}

	private void dfs(final Sensor _sensor, final int _connectedComponentGroup) {
		if (_sensor.getStatus() == Sensor.WHITE) {
			_sensor.setStatus(Sensor.BLACK);
			_sensor.setConnectedComponentNumber(_connectedComponentGroup);
			if (_sensor.getNeighbours() != null) {
				for (final Sensor neighbour : _sensor.getNeighbours()) {
					dfs(neighbour, _connectedComponentGroup);
				}
			}
		}
	}

	private int distanceBetween(final Vector<Sensor> group1, final Vector<Sensor> group2) {
		int min = Integer.MAX_VALUE;
		for (final Circle group1Sensor : group1) {
			for (final Circle group2Sensor : group2) {
				final int distance = (int) group1Sensor.getPoint().distance(group2Sensor.getPoint());
				if (distance < min) {
					min = distance;
				}
			}
		}
		return min;
	}

	public void initImage(final Graphics2D _g2d) {
		if (_g2d == null) {
			image = new BufferedImage(SensorInfo.getInstance().parameter.getAreaWidth(),
					SensorInfo.getInstance().parameter.getAreaHeight(), BufferedImage.TYPE_INT_RGB);
			g2d = image.createGraphics();
		} else {
			g2d = _g2d;
		}
	}

	protected boolean isConnected(final Vector<Sensor> sensors) {
		// Create graph
		createNeighbourhood(sensors);
		traverseNeighbourhood(sensors);
		for (final Sensor sensor : sensors) {
			if (sensor.getStatus() == Sensor.WHITE)
				return false;
		}
		return true;
	}

	/*
	 * check if there are k-covered sensors over hotspots
	 */
	private boolean isKCovered(final Vector<Sensor> sensors, final SensorInfo sensorInfo) {
		final int kCovered = SensorInfo.getInstance().parameter.kCovered;
		// traverse all hotspots
		for (final Hotspot hotSpot : sensorInfo.getHotSpots()) {
			int covered = 0;
			for (final Sensor sensor : sensors) {
				if ((hotSpot.getX() == sensor.getX()) && (hotSpot.getY() == sensor.getY())) {
					covered++;
				}
			}
			if (covered < kCovered)
				return false;
		}
		return true;
	}

	protected void setImage(final Graphics2D _g2d) {
		g2d = _g2d;
	}

	private void traverseNeighbourhood(final Vector<Sensor> _sensors) {
		final Vector<Sensor> tempSensors = new Vector<Sensor>();
		tempSensors.add(_sensors.get(0));
		tempSensors.get(0).setStatus(Sensor.GRAY);
		while (tempSensors.size() > 0) {
			final Sensor sensor = tempSensors.remove(0);
			if (sensor.getNeighbours() != null) {
				for (final Sensor neighbour : sensor.getNeighbours()) {
					if (neighbour.getStatus() == Sensor.WHITE) {
						neighbour.setStatus(Sensor.GRAY);
						tempSensors.add(neighbour);
					}
				}
			}
			sensor.setStatus(Sensor.BLACK);
		}
	}
}
