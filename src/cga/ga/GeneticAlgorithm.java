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
package cga.ga;

import java.awt.Graphics2D;
import java.util.Vector;

import cga.data.Sensor;
import cga.data.SensorInfo;
import cga.data.ga.Chromosome;
import cga.data.ga.GAParameters;
import cga.data.ga.Movement;
import cga.utility.GeneticAlgorithmUtility;
import cga.utility.QubbleSortAlgorithm;

public class GeneticAlgorithm {

	public static final java.util.Random RANDOM_NUMBER_GENERATOR = new java.util.Random();

	public static void main(final String[] args) {
		try {
			final GeneticAlgorithm ga = new GeneticAlgorithm();
			ga.runGA();
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	Chromosome best;

	private GeneticAlgorithmUtility gaUtility;

	private GAParameters gpa;
	private GANotifier notifier;
	Chromosome[] population;
	private SensorInfo sensorInfo;

	public GeneticAlgorithm() {
		init(null);
	}

	public GeneticAlgorithm(final GANotifier _notifier) {
		init(null);
		this.setNotifier(_notifier);
	}

	public GeneticAlgorithm(final java.awt.Graphics2D g2d) {
		init(g2d);
	}

	private void calculateAndSort() {
		// Calculate cost of every individual in the population
		for (final Chromosome chr : population) {
			chr.setFitness(calculateFitness(chr));
		}
		// Sort population
		QubbleSortAlgorithm.getInstance().sort(population);
	}

	public int calculateFitness(final Chromosome chr) {
		// First create a sensor array and add every movement
		// for every sensor in this array and send new sensors for area
		// calculation
		final Vector<Sensor> sensors = new Vector<Sensor>();
		for (int i = 0; i < sensorInfo.getSensorCount(); i++) {
			sensors.add(gaUtility.applyMovement(sensorInfo.getSensor(i), chr.getGene(i)));
		}
		return gaUtility.calculateFitness(sensors, sensorInfo);
	}

	private void crossover() {
		final int random = RANDOM_NUMBER_GENERATOR.nextInt(101); // 0-100
		if ((gpa.crossoverProbability * 100) >= random) {
			final Chromosome parent1 = (Chromosome) population[gpa.populationSize - 1].clone();
			final Chromosome parent2 = (Chromosome) population[gpa.populationSize - 2].clone();
			crossover(parent1, parent2);
			population[0] = parent1;
			population[1] = parent2;
		}
	}

	private void crossover(final Chromosome parent1, final Chromosome parent2) {
		final int cutPoint = RANDOM_NUMBER_GENERATOR.nextInt(parent1.getGeneCount());
		for (int i = cutPoint; i < parent1.getGeneCount(); i++) {
			final Movement temp = parent1.getGene(i);
			parent1.setGene(i, parent2.getGene(i));
			parent2.setGene(i, temp);
		}
	}

	private void elitism(final int nesil) {
		final Chromosome tempBest = population[population.length - 1];
		if (best.getFitness() <= tempBest.getFitness()) {
			best = (Chromosome) tempBest.clone();
			gaUtility.applyMovement(sensorInfo.getSensors(), best.getGenes());
			final int eliteArea = gaUtility.calculateArea(sensorInfo.getSensors(), sensorInfo,
					"elite_" + nesil + "_" + best.getFitness(), sensorInfo.saveEliteImage);
			notify("G(" + nesil + ") " + best + " A:" + eliteArea);
		}
	}

	private Chromosome generateRandomChromosome() {
		return new Chromosome(sensorInfo.getSensorCount(), true);
	}

	private void init(final Graphics2D g2d) {
		sensorInfo = SensorInfo.getInstance();
		gaUtility = GeneticAlgorithmUtility.getInstance();
		gaUtility.initImage(g2d);
	}

	private Chromosome[] initializePopulation() {
		// First population are generated using random x and y values
		final Chromosome[] population = new Chromosome[gpa.populationSize];
		for (int i = 0; i < population.length; i++) {
			population[i] = generateRandomChromosome();
		}
		population[0] = new Chromosome(sensorInfo.getSensorCount());
		return population;
	}

	private void mutate() {
		final int random = RANDOM_NUMBER_GENERATOR.nextInt(101); // 0-100
		if ((gpa.mutationProbability * 100) >= random) {
			for (int i = 0; i < gpa.populationSize / 2; i++) {
				mutate(population[i]);
			}
		}
	}

	private void mutate(final Chromosome chr) {
		final int point = RANDOM_NUMBER_GENERATOR.nextInt(chr.getGeneCount());
		chr.setGene(point, new Movement(chr.generateRandomMovement(), chr.generateRandomMovement()));
	}

	private void notify(final String log) {
		if (notifier != null)
			notifier.notify(log);
	}

	public void runGA() {
		try {
			final long baslangic = System.currentTimeMillis();
			gpa = sensorInfo.gaParameter;
			population = initializePopulation();

			best = new Chromosome(sensorInfo.getSensorCount());
			best.setFitness(calculateFitness(best));

			int nesil = 0;

			System.out.println("sensorInfo:" + sensorInfo.onlyParameters());
			while (nesil < gpa.generationLimit) {
				calculateAndSort();
				crossover();
				mutate();
				elitism(nesil);
				nesil++;
			}
			notify("Best " + best + " Total Time:" + (System.currentTimeMillis() - baslangic));
		} catch (final Exception e) {
			e.printStackTrace();
		}
		notifier.finished();
	}

	public void setNotifier(final GANotifier _notifier) {
		notifier = _notifier;
	}
}
