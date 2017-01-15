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

import java.awt.geom.Point2D;
import java.util.Vector;

public class Circle {

	private int id;

	private final Point2D.Float point;

	public Circle(final int _id, final float x, final float y) {
		id = _id;
		point = new Point2D.Float(x, y);
	}

	public float computeArcAngle(final Circle _circle) {
		final float distance = (float) point.distance(_circle.getPoint());
		if (distance == 0)
			return (float) Math.PI;
		return (float) Math.acos(distance
				/ (2 * SensorInfo.getInstance().parameter.sensorRadius));
	}

	public float distance(final Circle circle) {
		return (float) point.distance(circle.getPoint());
	}

	public int getId() {
		return id;
	}

	public Point2D.Float[] getIntersectionPoints(final Circle _circle) {
		final float dx = point.x - _circle.getPoint().x;
		final float dy = point.y - _circle.getPoint().y;
		final float d2 = dx * dx + dy * dy;
		final float d = sqrt(d2);
		final float r = SensorInfo.getInstance().parameter.sensorRadius;

		if (d > r + r || d < Math.abs(r - r))
			return null; // no solution

		final float a = (r - r + d2) / (2 * d);
		final float h = sqrt(r - a * a);
		final float x2 = point.x + a * (_circle.getPoint().x - point.x) / d;
		final float y2 = point.y + a * (_circle.getPoint().y - point.y) / d;

		final float paX = x2 + h * (_circle.getPoint().y - point.y) / d;
		final float paY = y2 - h * (_circle.getPoint().x - point.x) / d;
		final float pbX = x2 - h * (_circle.getPoint().y - point.y) / d;
		final float pbY = y2 + h * (_circle.getPoint().x - point.x) / d;

		return new Point2D.Float[] { new Point2D.Float(paX, paY),
				new Point2D.Float(pbX, pbY) };
	}

	public Point2D.Float getPoint() {
		return point;
	}

	public float getX() {
		return point.x;
	}

	public float getY() {
		return point.y;
	}

	public void intersect(final Circle circle, final Vector<Arc> intersections) {
		float alpha, beta, slope, offset;

		// same point
		if ((this.getX() == circle.getX()) && (this.getY() == circle.getY())) {
			intersections.add(new Arc(0, (float) (2.0 * Math.PI)));
			return;
		}

		final float distance = this.distance(circle);

		final float a = SensorInfo.getInstance().parameter.sensorRadius;
		final float b = distance;
		final float c = SensorInfo.getInstance().parameter.sensorRadius;

		offset = (float) Math.acos((sqr(a) + sqr(b) - sqr(c)) / (2.0 * a * b));

		slope = (float) (Math.atan2(circle.getY() - getY(), circle.getX()
				- getX()) + 2.0 * Math.PI);
		if (slope >= 2.0 * Math.PI)
			slope -= 2.0 * Math.PI;

		alpha = slope - offset;
		if (alpha < 0)
			alpha += 2.0 * Math.PI;

		beta = slope + offset;
		if (beta > 2.0 * Math.PI)
			beta -= 2.0 * Math.PI;
		
		if (alpha > beta) {
			intersections.add(new Arc(0, beta));
			intersections.add(new Arc(alpha, (float) (2.0 * Math.PI)));
		} else {
			intersections.add(new Arc(alpha, beta));
		}
	}

	public void setId(final int _id) {
		id = _id;
	}

	public void setX(final float x) {
		point.x = x;
	}

	public void setY(final float y) {
		point.x = y;
	}

	float sqr(final float x) {
		return x * x;
	}

	long sqr(final long x) {
		return x * x;
	}

	protected float sqrt(final float number) {
		return (float) Math.sqrt(number);
	}

	@Override
	public String toString() {
		return "id:" + id + " x:" + point.x + " y:" + point.y;
	}
}
