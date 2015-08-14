package br.com.mnidersoft.geneticAlgorithm.example.nurseSchedule;

import br.com.mnidersoft.geneticAlgorithm.GeneticAlgorithm;
import br.com.mnidersoft.geneticAlgorithm.example.nurseSchedule.WardChromosome.StatusEnum;

public class nurseScheduleMain {

	public static void main(String[] args) {
		GeneticAlgorithm<Integer> ga = new GeneticAlgorithm<Integer>(100, 200, WardChromosome.class);
		ga.start();

		WardChromosome wardChromosome = (WardChromosome)ga.getBest();

		printNurseSchedule(wardChromosome);
	}

	private static final void printNurseSchedule(WardChromosome chro) {
		boolean hasSomeone = false;

		for(int i = 0; i < chro.daysInMonth*2; i = i+2) {
			System.out.println("\n==> Dia " + ((i+2)/2) + " <==");
			for(int j = 0; j < chro.jobStatus.length; j++) {
				System.out.println("\n" + chro.jobStatus[j]);
				for(int k = 0; k < chro.nursieCount; k++) {
					int element = chro.getElementAt(i + k * chro.daysInMonth);
					if(j == StatusEnum.MORNING.ordinal()) {
						if(element == StatusEnum.MORNING.ordinal()) {
							System.out.println(chro.nurses[k]);
							hasSomeone = true;
						}
						else if(element == StatusEnum.DAY_SHIFT.ordinal()) {
							System.out.println(chro.nurses[k] + " (plantão)");
							hasSomeone = true;
						}
					}
					else if(j == StatusEnum.AFTERNOON.ordinal()) {
						if(element == StatusEnum.AFTERNOON.ordinal()) {
							System.out.println(chro.nurses[k]);
							hasSomeone = true;
						}
						else if(element == StatusEnum.DAY_SHIFT.ordinal()) {
							System.out.println(chro.nurses[k] + " (plantão)");
							hasSomeone = true;
						}
					}
					else if(j == StatusEnum.NIGHT.ordinal()) {
						if(element == StatusEnum.NIGHT.ordinal()) {
							System.out.println(chro.nurses[k]);
							hasSomeone = true;
						}
						else if(element == StatusEnum.NIGHT_SHIFT.ordinal()) {
							System.out.println(chro.nurses[k] + " (plantão)");
							hasSomeone = true;
						}
					}
					else if(j == 3) { //DAY_OFF
						if(element == StatusEnum.DAY_OFF.ordinal()) {
							System.out.println(chro.nurses[k]);
							hasSomeone = true;
						}
					}
					else if(j == 4) { //HOLIDAY
						if(element == StatusEnum.HOLIDAY.ordinal()) {
							System.out.println(chro.nurses[k]);
							hasSomeone = true;
						}
					}
				}

				if(!hasSomeone) {
					System.out.println("Ninguém");
				}
				hasSomeone = false;
			}
		}
		System.out.println("Fitness = " + chro.getFitness());
	}
}