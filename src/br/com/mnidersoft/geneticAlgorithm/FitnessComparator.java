package br.com.mnidersoft.geneticAlgorithm;

import java.util.Comparator;

public final class FitnessComparator<T> implements Comparator<AbstractChromosome<T>> {

	@Override
	public int compare(AbstractChromosome<T> o1, AbstractChromosome<T> o2) {
		if(o1.getFitness() < o2.getFitness()) {
			return -1;
		}
		else if(o1.getFitness() > o2.getFitness()) {
			return 1;
		}
		return 0;
	}
}
