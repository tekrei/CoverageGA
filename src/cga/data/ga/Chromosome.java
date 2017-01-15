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

import cga.data.SensorInfo;
import cga.ga.GeneticAlgorithm;

public class Chromosome {

	//fitness value of this chromosome
	private int fitness;
	// we are stroing movements as genes of chromosome
	private final Movement[] genes;

	public Chromosome(final int sensorCount) {
		genes = new Movement[sensorCount];
		for (int i = 0; i < genes.length; i++) {
			genes[i] = new Movement(0, 0);
		}
	}

	/**
	 * generate random genes
	 * 
	 * @param sensorCount
	 */
	public Chromosome(final int sensorCount, final boolean random) {
		genes = new Movement[sensorCount];
		if (random) {
			for (int i = 0; i < genes.length; i++) {
				genes[i] = new Movement(generateRandomMovement(),
						generateRandomMovement());
			}
		}
	}

	@Override
	public Object clone() {
		final Chromosome chr = new Chromosome(genes.length, false);
		for (int i = 0; i < genes.length; i++) {
			chr.setGene(i, genes[i]);
		}
		chr.setFitness(fitness);
		return chr;
	}

	public int generateRandomMovement() {
		final int high = SensorInfo.getInstance().parameter.movementLimit;
		final int low = -SensorInfo.getInstance().parameter.movementLimit;
		return GeneticAlgorithm.RANDOM_NUMBER_GENERATOR.nextInt(high - low + 1) + low;
	}

	public int getFitness() {
		return fitness;
	}

	public Movement getGene(final int i) {
		return genes[i];
	}

	public int getGeneCount() {
		return genes.length;
	}

	public Movement[] getGenes() {
		return genes;
	}

	public void setFitness(final int fitness) {
		this.fitness = fitness;
	}

	public void setGene(final int i, final Movement gene) {
		genes[i] = gene;
	}

	@Override
	public String toString() {
		return "F:" + fitness;
	}
}
