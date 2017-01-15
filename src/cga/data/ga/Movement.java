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
package cga.data.ga;

public class Movement {

	private int xInc;
	private int yInc;

	public Movement(final int inc, final int inc2) {
		super();
		xInc = inc;
		yInc = inc2;
	}

	public int getXInc() {
		return xInc;
	}

	public int getYInc() {
		return yInc;
	}

	public void setXInc(final int x) {
		xInc = x;
	}

	public void setYInc(final int y) {
		yInc = y;
	}

	public String toString() {
		return xInc + " " + yInc;
	}
}
