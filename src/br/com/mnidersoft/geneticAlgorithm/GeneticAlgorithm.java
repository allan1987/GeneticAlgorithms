package br.com.mnidersoft.geneticAlgorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * 
 * @author Allan Ariel (allan.ariel1987@gmail.com)
 *
 * @param <T> With Generics, but I use Integer in my Genetic Algorithms
 */

public class GeneticAlgorithm<T> {

	/**
	 *  If you want to generate random numbers
	 */
	protected Random random;

	/**
	 * Population is represented by List of AbstractChromosome
	 */
	protected List<AbstractChromosome<T>> populationList;

	protected int populationCount, generationsCount;
	
	/**
	 * Class of your Chromosome (extends AbstractChromosome<T>)
	 */
	protected Class<? extends AbstractChromosome<T>> chromosomeClass;

	/**
	 * Constructor
	 * @param populationCount population count. The minimum aceptable value will be 4 and must be pair
	 * @param generationsCount generations count
	 * @param chromosomeClass class of your chromosome
	 */
	public GeneticAlgorithm(int populationCount, int generationsCount, Class<? extends AbstractChromosome<T>> chromosomeClass) {
		this.populationCount = populationCount;
		if(this.populationCount < 4) {
			this.populationCount = 4;
		}
		else if(this.populationCount % 2 == 1) {
			this.populationCount++;
		}

		this.generationsCount = generationsCount;

		this.chromosomeClass = chromosomeClass;

		this.random = new Random();
	}
	
	/**
	 * Starts Genetic Algorithm with first population, crossover, mutation, fitness calculation, elitism and generate population
	 */
	public final void start() {
		this.firstPopulation();

		for(int i = 1; i <= this.generationsCount; i++) {
			System.out.println("geração " + i);
			this.crossover();

			this.mutation();

			this.fitnessCalc();

			this.elitism();
			
			this.generatePopulation();
		}
	}

	/**
	 * Makes the first population, with your chromosome particularity
	 */
	private final void firstPopulation() {
		this.populationList = new ArrayList<>();

		try {
			for(int i = 0; i < this.populationCount; i++) {
				AbstractChromosome<T> chromosome = this.chromosomeClass.newInstance();
				chromosome.init();
				this.populationList.add(chromosome);
//				System.out.println("population = " + i + " e chromosome = \n" + chromosome);
			}
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Generates the population. For each chromosome, call init method
	 */
	private final void generatePopulation() {
		for(int i = 1; i < this.populationCount; i++) {
			this.populationList.get(i).init();
		}
	}

	
	/**
	 * Makes cross between chromosomes by pairs. The cross point is the half of gens length. This method can be overrided
	 */
	protected void crossover() {
		int crossoverPos = Math.round(this.populationList.get(0).getGensLength() / 2);

		try {
			for(int i = 2; i < this.populationList.size() - 1; i = i+2) {
				AbstractChromosome<T> chrA = this.populationList.get(i);
				AbstractChromosome<T> chrB = this.populationList.get(i+1);

				List<T> newChrA = chrA.getGens().subList(0, crossoverPos);
				newChrA.addAll(chrB.getGens().subList(crossoverPos, chrA.getGensLength()));

				List<T> newChrB = chrB.getGens().subList(0, crossoverPos);
				newChrB.addAll(chrA.getGens().subList(crossoverPos, chrB.getGensLength()));

				chrA = this.chromosomeClass.newInstance();
				chrA.init(newChrA);

				chrB = this.chromosomeClass.newInstance();
				chrB.init(newChrB);

				this.populationList.set(i, chrA);
				this.populationList.set(i+1, chrB);
			}
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Mutate chromosomes. Starts at third position because first and second has the best fitness.
	 */
	private final void mutation() {
		for(int i = 2; i < this.populationList.size(); i++) {
			AbstractChromosome<T> chromosome = this.populationList.get(i);

			int pos = 2 * this.random.nextInt(chromosome.getGensLength() / 2);
			chromosome.mutateItem(pos);
		}
	}

	/**
	 * Calculates the fitness of each chromosome
	 */
	private final void fitnessCalc() {
		for(int i = 0; i < this.populationList.size(); i++) {
			this.populationList.get(i).updateFitness();
		}
	}

	/**
	 * Sorts chromosome fitness by asc
	 */
	private final void elitism() {
		Collections.sort(this.populationList, new FitnessComparator<T>());
	}
	
	/**
	 * @return Best chromosome, with less fitness value
	 */
	public final AbstractChromosome<T> getBest() {
		if(this.populationList != null && this.populationList.size() > 0) {
			return this.populationList.get(0);
		}
		return null;
	}

	/**
	 * Print the best chromosome value, with your fitness
	 */
	public final void printBestFitness() {
		AbstractChromosome<T> chromosome = this.populationList.get(0);
		System.out.println("best fitness = " + chromosome.getFitness());
		for(int i = 0; i < chromosome.getGensLength(); i++) {
			System.out.println("i = " + i + ", value = " + chromosome.getElementAt(i));
		}
	}
}
