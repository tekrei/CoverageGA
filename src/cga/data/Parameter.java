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

public class Parameter {

	private int areaHeight;
	private int areaWidth;
	public int kCovered;
	public int sensorRadius;

	public int totalArea;
	public int movementLimit = 3;

	public int getAreaHeight() {
		return areaHeight;
	}

	public int getAreaWidth() {
		return areaWidth;
	}

	public int getTotalArea() {
		return totalArea;
	}

	public void setAreaHeight(final int _areaHeight) {
		areaHeight = _areaHeight;
		totalArea = areaWidth * areaHeight;
	}

	public void setAreaWidth(final int _areaWidth) {
		areaWidth = _areaWidth;
		totalArea = areaWidth * areaHeight;
	}

	@Override
	public String toString() {
		return "A(" + areaWidth + "," + areaHeight + ")=" + totalArea
				+ " kCovered:" + kCovered + " Radius:" + sensorRadius
				+ " movementLimit:" + movementLimit;
	}
}
