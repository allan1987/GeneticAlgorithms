package br.com.mnidersoft.geneticAlgorithm.example.travelingSalesman;

import java.util.Random;

public class TravelingSalesmanMain {
	
	public final static int numCities = 1000, maxDistance = 98, numPopulation = 500, numGenerations = 10;
	public static int cityArray[] = new int[numCities * numCities];
	
	public final static Random random = new Random();
	
	public TravelingSalesmanMain() {}
	
	public static final void main(String args[]) {
		TravelingSalesmanMain main = new TravelingSalesmanMain();
		main.makeCityArray();
		main.printCityArray();
		
		TravelingSalesmanGA ga = new TravelingSalesmanGA(numPopulation, numGenerations);
		ga.start();
		ga.printBestFitness();
	}

	private void makeCityArray(){
		
		for(int i = 0; i < numCities; i++){
			for(int j = 0; j < numCities; j++){
				if(i == j){
					cityArray[i * numCities + j] = 0;
				} else if(i < j){
					cityArray[i * numCities + j] = random.nextInt(maxDistance) + 1;
				} else{
					cityArray[i * numCities + j] = cityArray[j * numCities + i];
				}
			}
		}
	}
	
	private void printCityArray(){
		System.out.println("City Array:");
		
		for(int i = 0; i < numCities; i++){
			for(int j = 0; j < numCities; j++){
				System.out.print(cityArray[i * numCities + j] + ", ");
			}
			System.out.println("");
		}
	}
}
