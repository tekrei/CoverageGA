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
package cga.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import cga.data.SensorInfo;
import cga.ga.GANotifier;
import cga.ga.GeneticAlgorithm;
import cga.utility.JPGFileFilter;
import cga.utility.SensorXMLReader;

public class GASTest implements GANotifier {

	public static void main(final String[] args) {
		new GASTest().startTest();
	}

	@Override
	public void finished() {
		System.out.println("-----------------------------------");
	}

	@Override
	public void notify(final String _message) {
		System.out.println(_message);
	}

	private void redirectOutputToFile() {
		try {
			System.setOut(new PrintStream(new FileOutputStream("tests/log.txt")));
		} catch (final FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void runGA(final int kCovered, final int movementLimit) {
		// load sensors from XML file
		SensorXMLReader.readSensorInfo(new File("sensorler2.xml"));
		final SensorInfo sensorInfo = SensorInfo.getInstance();
		sensorInfo.saveEliteImage = true;
		// set GA parameters
		sensorInfo.gaParameter.mutationProbability = 0.01f;
		sensorInfo.gaParameter.crossoverProbability = 0.75f;
		sensorInfo.gaParameter.populationSize = 100;
		sensorInfo.gaParameter.generationLimit = 2000;
		sensorInfo.parameter.kCovered = kCovered;
		sensorInfo.parameter.movementLimit = movementLimit;
		// display information
		System.out.println(sensorInfo.onlyParameters());
		// run GA
		new GeneticAlgorithm(this).runGA();
		// save results
		savePictures("kCovered_" + kCovered + "_movementLimit" + movementLimit);
	}

	private void savePictures(final String folderName) {
		final File folder = new File("tests/" + folderName);
		folder.mkdir();
		final File[] files = new File(".").listFiles(new JPGFileFilter());
		for (final File file : files) {
			file.renameTo(new File("tests/" + folderName + "/" + file.getName()));
		}
		new File("tests/log.txt").renameTo(new File("tests/" + folderName + "/log.txt"));
		redirectOutputToFile();
	}

	private void startTest() {
		redirectOutputToFile();
		// kCovered
		final int[] kCovereds = { 1, 2, 3 };
		// movement limit
		final int[] movementLimits = { 1, 2, 3, 4, 5 };
		// run tests
		for (final int kCovered : kCovereds) {
			for (int movementLimit : movementLimits) {
				runGA(kCovered, movementLimit);
			}
		}
	}

}
