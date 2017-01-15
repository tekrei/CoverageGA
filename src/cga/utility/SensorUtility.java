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
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class SensorUtility {
	private static int WHITE_PIXEL = Color.WHITE.getRGB();

	public static int calculateArea(final BufferedImage image, String prefix, final boolean saveImage) {
		if (prefix == null)
			prefix = "";
		int toplamRenkliPiksel = 0;
		// Resmi piksel piksel dolasalim
		try {
			if (saveImage)
				ImageIO.write(image, "jpg", new File(prefix + "bas.jpg"));
			for (int j = 0; j < image.getHeight(); j++) {
				for (int i = 0; i < image.getWidth(); i++) {
					if (image.getRGB(i, j) != WHITE_PIXEL) {
						toplamRenkliPiksel++;
						image.setRGB(i, j, WHITE_PIXEL);
					}
				}
			}

		} catch (final Exception e) {
			e.printStackTrace();
		}
		return toplamRenkliPiksel;
	}
}
