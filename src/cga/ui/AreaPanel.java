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
package cga.ui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Vector;

import javax.swing.JPanel;

import cga.data.Sensor;
import cga.data.SensorInfo;
import cga.utility.SensorDrawUtility;

public class AreaPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	SensorInfo sensorInfo;

	public AreaPanel() {
		initialize();
	}

	public BufferedImage getAreaAsImage() {
		try {
			final int w = sensorInfo.parameter.getAreaWidth();
			final int h = sensorInfo.parameter.getAreaHeight();
			return SensorDrawUtility.getAreaAsImage(w, h, this);
		} catch (final Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private void initialize() {
		this.setSize(400, 400);
	}

	public void load() {
		sensorInfo = SensorInfo.getInstance();
		this.repaint();
	}

	public void loadSensors(final Vector<Sensor> sensors) {
		sensorInfo.setSensors(sensors);
		this.repaint();
	}

	@Override
	public void paintComponent(final Graphics g) {
		try {
			SensorDrawUtility.drawSensorNetwork((Graphics2D) g, sensorInfo);
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}
}
