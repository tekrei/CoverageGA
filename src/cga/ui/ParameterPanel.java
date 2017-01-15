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

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import cga.CoverageFrame;
import cga.data.Parameter;
import cga.data.SensorInfo;
import cga.data.ga.GAParameters;
import cga.utility.GUIUtility;
import cga.utility.SensorXMLReader;

public class ParameterPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private JButton btnCalculateArea;
	private JButton btnLoadFromFile;
	private JButton btnRunGA;
	private JCheckBox chkSaveEliteInfo;
	private final CoverageFrame frame;
	private JTextField txtAreaHeight;
	private JTextField txtAreaWidth;
	private JTextField txtCrossoverProbability;
	private JTextField txtGenerationLimit;

	private JTextArea txtInfo;
	private JTextField txtKCovered;
	private JTextField txtMovementLimit;
	private JTextField txtMutationProbability;

	private JTextField txtPopulationSize;

	private JTextField txtSensorRadius;

	public ParameterPanel(final CoverageFrame _frame) {
		frame = _frame;
		initialize();
	}

	private JButton createBtnCalculateArea(final Rectangle bounds) {
		if (btnCalculateArea == null) {
			btnCalculateArea = new JButton("Area");
			btnCalculateArea.setBounds(bounds);
			btnCalculateArea.setMnemonic('a');
			btnCalculateArea.addActionListener(new ActionListener() {
				public void actionPerformed(final ActionEvent e) {
					frame.calculateArea();
				}
			});
		}
		return btnCalculateArea;
	}

	private JButton createBtnLoadFromFile(final Rectangle bounds) {
		if (btnLoadFromFile == null) {
			btnLoadFromFile = new JButton("Load File");
			btnLoadFromFile.setBounds(bounds);
			btnLoadFromFile.setMnemonic('l');
			btnLoadFromFile.addActionListener(new ActionListener() {
				public void actionPerformed(final ActionEvent e) {
					final File selectedFile = GUIUtility.selectFile("xml");
					if (selectedFile != null) {
						SensorXMLReader.readSensorInfo(selectedFile);

						final Parameter p = SensorInfo.getInstance().parameter;
						txtAreaWidth.setText(Integer.toString(p.getAreaWidth()));
						txtAreaHeight.setText(Integer.toString(p.getAreaHeight()));
						txtSensorRadius.setText(Integer.toString(p.sensorRadius));
						txtKCovered.setText(Integer.toString(p.kCovered));

						frame.load();
					}
				}
			});
		}
		return btnLoadFromFile;
	}

	private JButton createBtnRunGA(final Rectangle bounds) {
		if (btnRunGA == null) {
			btnRunGA = new JButton("Run GA");
			btnRunGA.setBounds(bounds);
			btnRunGA.setMnemonic('r');
			btnRunGA.addActionListener(new ActionListener() {
				public void actionPerformed(final ActionEvent e) {
					final Parameter parameter = new Parameter();
					parameter.setAreaHeight(Integer.parseInt(txtAreaHeight.getText()));
					parameter.setAreaWidth(Integer.parseInt(txtAreaWidth.getText()));
					parameter.sensorRadius = Integer.parseInt(txtSensorRadius.getText());

					parameter.kCovered = Integer.parseInt(txtKCovered.getText());

					parameter.movementLimit = Integer.parseInt(txtMovementLimit.getText());

					SensorInfo.getInstance().parameter = parameter;

					final GAParameters gap = new GAParameters();
					gap.mutationProbability = Float.parseFloat(txtMutationProbability.getText());
					gap.crossoverProbability = Float.parseFloat(txtCrossoverProbability.getText());
					gap.generationLimit = Integer.parseInt(txtGenerationLimit.getText());
					gap.populationSize = Integer.parseInt(txtPopulationSize.getText());

					SensorInfo.getInstance().saveEliteImage = chkSaveEliteInfo.isSelected();

					SensorInfo.getInstance().gaParameter = gap;
					frame.startGAThread();
					btnRunGA.setEnabled(false);
				}
			});
		}
		return btnRunGA;
	}

	private JCheckBox createChkSaveEliteImage(final Rectangle bounds) {
		if (chkSaveEliteInfo == null) {
			chkSaveEliteInfo = new JCheckBox("Save Elite Information");
			chkSaveEliteInfo.setBounds(bounds);
		}
		return chkSaveEliteInfo;

	}

	private JLabel createLabel(final String label, final Rectangle bounds) {
		final JLabel lbl = new JLabel(label);
		lbl.setBounds(bounds);
		return lbl;
	}

	private JTextField createTxtAreaHeight(final Rectangle bounds) {
		if (txtAreaHeight == null) {
			txtAreaHeight = new JTextField("400");
			txtAreaHeight.setBounds(bounds);
		}
		return txtAreaHeight;
	}

	private JTextField createTxtAreaWidth(final Rectangle bounds) {
		if (txtAreaWidth == null) {
			txtAreaWidth = new JTextField("400");
			txtAreaWidth.setBounds(bounds);
		}
		return txtAreaWidth;
	}

	private JTextField createTxtCrossoverProbability(final Rectangle bounds) {
		if (txtCrossoverProbability == null) {
			txtCrossoverProbability = new JTextField("0.7");
			txtCrossoverProbability.setBounds(bounds);
		}
		return txtCrossoverProbability;
	}

	private JTextField createTxtGenerationLimit(final Rectangle bounds) {
		if (txtGenerationLimit == null) {
			txtGenerationLimit = new JTextField("20");
			txtGenerationLimit.setBounds(bounds);
		}
		return txtGenerationLimit;
	}

	private JScrollPane createTxtInfo(final Rectangle bounds) {
		if (txtInfo == null) {
			txtInfo = new JTextArea();
			txtInfo.setBounds(bounds);
		}
		final JScrollPane jsp = new JScrollPane(txtInfo);
		jsp.setBounds(bounds);
		return jsp;
	}

	private JTextField createTxtKCovered(final Rectangle bounds) {
		if (txtKCovered == null) {
			txtKCovered = new JTextField("2");
			txtKCovered.setBounds(bounds);
		}
		return txtKCovered;
	}

	private JTextField createTxtMovementLimit(final Rectangle bounds) {
		if (txtMovementLimit == null) {
			txtMovementLimit = new JTextField("3");
			txtMovementLimit.setBounds(bounds);
		}
		return txtMovementLimit;
	}

	private JTextField createTxtMutationProbability(final Rectangle bounds) {
		if (txtMutationProbability == null) {
			txtMutationProbability = new JTextField("0.2");
			txtMutationProbability.setBounds(bounds);
		}
		return txtMutationProbability;
	}

	private JTextField createTxtPopulationSize(final Rectangle bounds) {
		if (txtPopulationSize == null) {
			txtPopulationSize = new JTextField("20");
			txtPopulationSize.setBounds(bounds);
		}
		return txtPopulationSize;
	}

	private JTextField createTxtSensorRadius(final Rectangle bounds) {
		if (txtSensorRadius == null) {
			txtSensorRadius = new JTextField("50");
			txtSensorRadius.setBounds(bounds);
		}
		return txtSensorRadius;
	}

	public void enableRunButton() {
		btnRunGA.setEnabled(true);
	}

	public void info(final String message) {
		txtInfo.setText(message + "\n" + txtInfo.getText());
	}

	private void initialize() {
		// Bu panelin amaci parametre girisidir
		this.setLayout(null);

		final int height = 20;
		final int lblWidth = 150;
		final int txtWidth = 50;
		int y = 0;

		this.add(createLabel("Sensor Parameters", new Rectangle(0, y, 150, height)));

		y += height;

		this.add(createLabel("Radius:", new Rectangle(0, y, lblWidth, height)));
		this.add(createTxtSensorRadius(new Rectangle(lblWidth, y, txtWidth, height)));

		y += height;

		this.add(createLabel("Height:", new Rectangle(0, y, lblWidth, height)));
		this.add(createTxtAreaHeight(new Rectangle(lblWidth, y, txtWidth, height)));

		y += height;

		this.add(createLabel("Width:", new Rectangle(0, y, lblWidth, height)));
		this.add(createTxtAreaWidth(new Rectangle(lblWidth, y, txtWidth, height)));

		y += height;

		this.add(createLabel("k-covered:", new Rectangle(0, y, lblWidth, height)));
		this.add(createTxtKCovered(new Rectangle(lblWidth, y, txtWidth, height)));

		y += height;

		this.add(createLabel("Movement Limit:", new Rectangle(0, y, lblWidth, height)));
		this.add(createTxtMovementLimit(new Rectangle(lblWidth, y, txtWidth, height)));

		y += height;

		this.add(createLabel("GA Parameters", new Rectangle(0, y, 150, height)));

		y += height;

		this.add(createLabel("Generation Limit:", new Rectangle(0, y, lblWidth, height)));
		this.add(createTxtGenerationLimit(new Rectangle(lblWidth, y, txtWidth, height)));

		y += height;

		this.add(createLabel("Population Size:", new Rectangle(0, y, lblWidth, height)));
		this.add(createTxtPopulationSize(new Rectangle(lblWidth, y, txtWidth, height)));

		y += height;

		this.add(createLabel("Crossover Probability:", new Rectangle(0, y, lblWidth, height)));
		this.add(createTxtCrossoverProbability(new Rectangle(lblWidth, y, txtWidth, height)));

		y += height;

		this.add(createLabel("Mutation Probability:", new Rectangle(0, y, lblWidth, height)));
		this.add(createTxtMutationProbability(new Rectangle(lblWidth, y, txtWidth, height)));

		y += height;

		this.add(createChkSaveEliteImage(new Rectangle(0, y, lblWidth * 2, height)));

		y += height;

		this.add(createBtnLoadFromFile(new Rectangle(0, y, lblWidth + txtWidth, height)));

		y += height;

		this.add(createBtnCalculateArea(new Rectangle(0, y, lblWidth + txtWidth, height)));

		y += height;

		this.add(createBtnRunGA(new Rectangle(0, y, lblWidth + txtWidth, height)));

		y += height;

		this.add(createTxtInfo(new Rectangle(0, y, lblWidth + txtWidth, 100)));

		y += 100;

		this.setSize(200, y);
	}
}
