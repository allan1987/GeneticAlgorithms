package br.com.mnidersoft.geneticAlgorithm.example.travelingSalesman;

import java.util.List;

import br.com.mnidersoft.geneticAlgorithm.GeneticAlgorithm;

public class TravelingSalesmanGA extends GeneticAlgorithm<Integer> {

	public TravelingSalesmanGA(int populationCount, int generationsCount) {
		super(populationCount, generationsCount, RouteChromosome.class);
	}

	@Override
	protected void crossover() {
		int crossoverPos = Math.round(this.populationList.get(0).getGensLength() / 2);

		for(int i = 2; i < this.populationList.size() - 1; i = i+2) {
			RouteChromosome chrA = (RouteChromosome)this.populationList.get(i);
			RouteChromosome chrB = (RouteChromosome)this.populationList.get(i+1);

			List<Integer> newChrA = chrA.getGens().subList(0, crossoverPos);
			for(int j = crossoverPos; j < chrA.getGensLength(); j++) {
				if(!newChrA.contains(chrB.getElementAt(j))) {
					newChrA.add(chrB.getElementAt(j));
				}
			}

			chrA = new RouteChromosome();
			chrA.init(this.fillWithRandomGenes(newChrA));

			List<Integer> newChrB = chrB.getGens().subList(0, crossoverPos);
			for(int j = crossoverPos; j < chrB.getGensLength(); j++) {
				if(!newChrB.contains(chrA.getElementAt(j))) {
					newChrB.add(chrA.getElementAt(j));
				}
			}
			chrB = new RouteChromosome();
			chrB.init(this.fillWithRandomGenes(newChrB));

			this.populationList.set(i, chrA);
			this.populationList.set(i+1, chrB);
		}
	}

	private List<Integer> fillWithRandomGenes(List<Integer> list){
		int value;
		for(int i = 0; i < TravelingSalesmanMain.numCities; i++){
			if(list.size() <= i){
				do{
					value = this.random.nextInt(TravelingSalesmanMain.numCities) + 1;
				}while(list.contains(value));
				list.add(value);
			}
		}
		return list;
	}
}
