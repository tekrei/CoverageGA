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
 *  
 *  CoverageGA is supplement code for the paper below:
 *  Kasım Sinan Yıldırım, Tahir Emre Kalaycı, Aybars Uğur, 
 *  "Optimizing Coverage in a K-Covered and Connected Sensor Network Using Genetic Algorithms",
 *  The 9th WSEAS International Conference on Evolutionary Computing (EC'08),
 *  Sofia, Bulgaria, 2-4 May 2008
 *  
 *  @author tahir.kalayci@ege.edu.tr, sinan.yildirim@ege.edu.tr
 */
package cga;

/**
 * Source code of the following paper:
 * 
 * K. S. Yildirim, T. E. Kalayci, and A. Ugur. 2008. 
 * Optimizing coverage in a K-covered and connected sensor network using genetic algorithms.
 * In Proceedings of the 9th WSEAS International Conference on Evolutionary Computing (EC'08), 
 * World Scientific and Engineering Academy and Society (WSEAS), Stevens Point, Wisconsin, USA, 21-26.
 * 
 * @see <a href="http://www.wseas.us/e-library/conferences/2008/sofia/EC/ec-2.pdf">http://www.wseas.us/e-library/conferences/2008/sofia/EC/ec-2.pdf</a>
 */
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JSplitPane;

import cga.data.Sensor;
import cga.ga.GANotifier;
import cga.ga.GeneticAlgorithm;
import cga.ui.AreaPanel;
import cga.ui.ParameterPanel;
import cga.utility.SensorUtility;

public class CoverageFrame extends JFrame implements GANotifier {
	private static final long serialVersionUID = 1L;

	public static void main(final String[] args) {
		new CoverageFrame();
	}

	private AreaPanel pnlArea;

	private ParameterPanel pnlParameter;

	public CoverageFrame() {
		initialize();
	}

	public void calculateArea() {
		JOptionPane.showMessageDialog(this,
				"Total Covered Area:" + SensorUtility.calculateArea(pnlArea.getAreaAsImage(), null, false));
	}

	private AreaPanel createPnlArea() {
		if (pnlArea == null) {
			pnlArea = new AreaPanel();
		}
		return pnlArea;
	}

	private ParameterPanel createPnlParameter() {
		if (pnlParameter == null) {
			pnlParameter = new ParameterPanel(this);
		}
		return pnlParameter;
	}

	@Override
	public void finished() {
		pnlParameter.enableRunButton();
	}

	private void initialize() {
		this.setSize(615, 430);

		final JSplitPane jsp = new JSplitPane();

		jsp.setLeftComponent(createPnlParameter());
		jsp.setRightComponent(createPnlArea());
		jsp.setDividerSize(0);
		jsp.setDividerLocation(201);

		this.setContentPane(jsp);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void load() {
		pnlArea.load();
	}

	public void loadSensors(final Vector<Sensor> sensors) {
		pnlArea.loadSensors(sensors);
	}

	@Override
	public void notify(final String message) {
		pnlParameter.info(message);
		pnlArea.repaint();
	}

	private void runGA() {
		new GeneticAlgorithm(this).runGA();
	}

	public void startGAThread() {
		new Thread() {
			@Override
			public void run() {
				runGA();
			}
		}.start();
	}
}
