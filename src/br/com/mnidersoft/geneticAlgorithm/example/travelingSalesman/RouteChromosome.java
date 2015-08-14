package br.com.mnidersoft.geneticAlgorithm.example.travelingSalesman;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import br.com.mnidersoft.geneticAlgorithm.AbstractChromosome;

public class RouteChromosome extends AbstractChromosome<Integer> {

	private Random random;

	public RouteChromosome() {
		super();

		this.random = new Random();

	}
	
	public void reset() {
		List<Integer> list = new ArrayList<>();
		for(int i = 0; i < this.getGensLength(); i++) {
			list.add(i, -1);
		}

		this.init(list);
	}

	@Override
	public void init() {
		this.reset();

		this.populate(false); //don't clean the gens
	}

	@Override
	protected int calcFitness(List<Integer> gens) {
		int start = 0;
		int end = 0;
		int cityArray[] = TravelingSalesmanMain.cityArray;
		int numCities = TravelingSalesmanMain.numCities;
		int fitnessCount = 0;

		for(int i = 0; i < this.getGens().size() - 1; i++){
			start = this.getElementAt(i) - 1;
			//				System.out.println("i = " + i + ", this.getGensLength() = " + this.getGensLength());
			end = this.getElementAt(i + 1) - 1;
			fitnessCount += cityArray[start*numCities + end];
		}
		fitnessCount += cityArray[end*numCities + (this.getElementAt(0)-1)];

		return fitnessCount;
	}

	@Override
	public int getGensLength() {
		return TravelingSalesmanMain.numCities;
	}

	@Override
	protected Integer generateItem(int position) {
		int value = 0;
		do{
			value = this.random.nextInt(TravelingSalesmanMain.numCities);
		}while(this.getElementAt(value) != -1);

		this.setElementAt(value, (position + 1));

		return null;
	}

	protected void generateRandomItens() {
		int value;
		for(int i = 0; i < TravelingSalesmanMain.numCities; i++){
			if(this.getElementAt(i) < 0){
				do{
					value = this.random.nextInt(TravelingSalesmanMain.numCities) + 1;
				}while(this.getGens().contains(value));
				this.setElementAt(i, value);
			}
		}
	}

	@Override
	protected Integer mutateItem(int position) {
		int value = 0;
		do{
			value = this.random.nextInt(TravelingSalesmanMain.numCities);
		}while(value != position);

		int copyValue = this.getElementAt(value);
		this.setElementAt(value, this.getElementAt(position));
		this.setElementAt(position, copyValue);

		return null;
	}
}
