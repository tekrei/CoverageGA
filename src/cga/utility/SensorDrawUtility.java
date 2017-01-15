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
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Vector;

import javax.swing.JComponent;

import cga.data.Circle;
import cga.data.Hotspot;
import cga.data.Sensor;
import cga.data.SensorInfo;

public class SensorDrawUtility {
	private static void drawHotspot(final int radius, final Graphics2D g2d, final Hotspot sensor) {
		final Ellipse2D.Double elips = new Ellipse2D.Double(sensor.getX() - radius, sensor.getY() - radius, radius * 2,
				radius * 2);
		g2d.setColor(Color.BLUE);
		g2d.draw(elips);
		drawPoint(g2d, sensor.getPoint());
	}

	public static void drawHotspots(final Graphics2D g2d, final int radius, final Vector<Hotspot> hotspots) {
		for (final Hotspot sensor : hotspots) {
			drawHotspot(radius, g2d, sensor);
		}
	}

	public static void drawPoint(final Graphics2D g, final Point2D.Float point) {
		final int x = (int) point.getX();
		final int y = (int) point.getY();

		g.drawLine(x - 1, y - 1, x + 1, y - 1);
		g.drawLine(x + 1, y - 1, x + 1, y + 1);
		g.drawLine(x + 1, y + 1, x - 1, y + 1);
		g.drawLine(x - 1, y + 1, x - 1, y - 1);
	} // drawPoint

	private static void drawSensor(final int radius, final Graphics2D g2d, final Circle sensor) {
		final Ellipse2D.Double elips = new Ellipse2D.Double(sensor.getX() - radius, sensor.getY() - radius, radius * 2,
				radius * 2);
		g2d.setColor(Color.GRAY);
		g2d.fill(elips);
		g2d.setColor(Color.RED);
		drawPoint(g2d, sensor.getPoint());
		g2d.draw(elips);
	}

	public static void drawSensorNetwork(final Graphics2D g2d, final SensorInfo sensorInfo) {
		try {
			g2d.setPaint(Color.WHITE);
			g2d.fill(new Rectangle2D.Double(0, 0, sensorInfo.parameter.getAreaWidth(),
					sensorInfo.parameter.getAreaHeight()));

			SensorDrawUtility.drawSensors(g2d, sensorInfo, null);

			g2d.setColor(Color.BLUE);
			SensorDrawUtility.drawHotspots(g2d, sensorInfo.parameter.sensorRadius, sensorInfo.getHotSpots());
		} catch (final NullPointerException e) {
			System.out.println("NPE - drawSensorNetwork");
		}
	}

	public static void drawSensors(final Graphics2D g2d, final SensorInfo sensorInfo, final Vector<Sensor> sensors) {
		Vector<Sensor> tempSensors = sensorInfo.getSensors();
		if (sensors != null) {
			tempSensors = sensors;
		}
		for (final Circle sensor : tempSensors) {
			drawSensor(sensorInfo.parameter.sensorRadius, g2d, sensor);
		}
	}

	public static BufferedImage getAreaAsImage(final int w, final int h, final JComponent component) {
		final int type = BufferedImage.TYPE_INT_RGB;
		final BufferedImage image = new BufferedImage(w, h, type);
		final Graphics2D g2 = image.createGraphics();
		component.paint(g2);
		g2.dispose();
		return image;
	}
}
