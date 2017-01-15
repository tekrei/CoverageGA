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

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class GUIUtility {

	private static JFileChooser jfc;

	private static JFileChooser getFileChooser() {
		if (jfc == null) {
			jfc = new JFileChooser();
		}
		return jfc;
	}

	public static File selectFile(final String extension) {
		final JFileChooser _jfc = getFileChooser();
		_jfc.setFileFilter(new FileNameExtensionFilter("", extension));
		if (_jfc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
			return _jfc.getSelectedFile();
		return null;
	}
}
