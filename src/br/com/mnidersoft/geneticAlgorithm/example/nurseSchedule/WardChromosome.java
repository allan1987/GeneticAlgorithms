package br.com.mnidersoft.geneticAlgorithm.example.nurseSchedule;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import br.com.mnidersoft.geneticAlgorithm.AbstractChromosome;

public class WardChromosome extends AbstractChromosome<Integer> {

	public int nursieCount, daysInMonth, wardCount, maxJobStatus, holidayCount, minTurnNurse, maxNurseHoursByMonth, maxFollowingWorkDays;
	private int penaltyR1, penaltyR2, penaltyR3, penaltyR4, penaltyR5, penaltyR6, penaltyR7, penaltyR8a, penaltyR8b;
	private boolean hasToWork;
	private Random random;

	private final String MORNING = "morning", AFTERNOON = "afternoon", NIGHT = "night", NIGHT_SHIFT = "nightshift", DAY_SHIFT = "dayshift";

	public final String[] nurses = {
		"Luana Santos", "Cristiany Leite", "Laionara Araújo", "Anne Karine", "Camila Menezes",
		"Vanda Menezes", "Sandra Leite", "Dalva Gonçalves", "Amanda Menezes", "Allan Ariel"
	};

	public final String[] jobStatus = {
		"Manhã: ", "Tarde: ", "Noite: ", "Folga: ", "Férias: "
	};

	public enum StatusEnum {
		MORNING,	//Trabalha no turno matutino (6h)
		AFTERNOON,	//Trabalha no turno vespertino (6h)
		NIGHT,		//Trabalha no turno noturno (6h)
		DAY_SHIFT,	//Plantão diurno (12h)
		NIGHT_SHIFT,//Plantão noturno (12h)
		DAY_OFF,	//Folga nos três turnos
		HOLIDAY	//Está de férias neste dia
	}

	/**
	 * Ward Rules (in Portuguese)
	 * 
	 * Rule 1: penaliza a solução, quando ela não respeita o período de férias de um enfermeiro. A cada dia de férias não respeitado é acrescido o valor da penalidade. 
	 * Rule 2: penaliza a solução, quando ela permite que um determinado período (Manhã, Tarde e Noite) fique sem enfermeiro. A cada período não atendido é acrescido o valor da penalidade (Não utilizada). 
	 * Rule 3: penaliza a solução, quando ela não permite a um enfermeiro que trabalhou a noite, folgar no outro dia. A cada ocorrência é acrescido o valor da penalidade da regra. 
	 * Rule 4: penaliza a solução, quando ela não permite a um enfermeiro que trabalhou em um plantão noturno, folgar 36 horas. A cada ocorrência é acrescido o valor da penalidade da regra. 
	 * Rule 5: penaliza a solução, quando ela não encontra uma quantidade suficiente de enfermeiros por período, esta quantidade é definida pelo usuário. Quando esta regra esta ativa ela desabilita a regra 2. 
	 * Rule 6: penaliza a solução, quando ela não respeita a carga horária máxima de trabalho de um enfermeiro. A penalidade nesta regra é a quantidade de horas que excederam o limite mensal multiplicada pelo valor da penalidade da regra. 
	 * Rule 7: penaliza a solução, quando ela permite a um enfermeiro trabalhar mais de 6 dias seguidos. A cada ocorrência é acrescido o valor da penalidade da regra. 
	 * Rule 8a: penaliza a solução, quando um determinado dia fica sem plantonista noturno. A cada ocorrência é acrescido o valor da penalidade da regra. 
	 * Rule 8b: penaliza a solução, quando um determinado dia fica sem plantonista diurno. A cada ocorrência é acrescido o valor da penalidade da regra. 
	 *
	 */

	public WardChromosome() {
		super();

		this.hasToWork = true;

		this.nursieCount = this.nurses.length;
		this.daysInMonth = 30;
		this.wardCount = 1;
		this.maxJobStatus = 6;
		this.holidayCount = 15;
		this.minTurnNurse = 0;
		this.maxNurseHoursByMonth = 180;
		this.maxFollowingWorkDays = 6;

		this.penaltyR1 	= 10;
		this.penaltyR2 	= 5;
		this.penaltyR3 	= 4;
		this.penaltyR4 	= 4;
		this.penaltyR5 	= 3;
		this.penaltyR6 	= 3;
		this.penaltyR7 	= 3;
		this.penaltyR8a = 5;
		this.penaltyR8b = 1;

		this.random = new Random();
	}

	@Override
	protected int calcFitness(List<Integer> gens) {
		int fitnessValue = 0, currentNurse = 0, holiday = 0, hoursByMonth = 0, followingDays = 0;

		Map<String, Integer> wardMap = new HashMap<>();

		for(int i = 0; i < gens.size(); i = i+2) {

			int value = gens.get(i);

			//Nurse Work
			if(i % 2 == 0) {
				wardMap.clear();

				for(int j = i+1; j < gens.size(); j = j+this.daysInMonth*2) {
					int ward = gens.get(j);
					if(ward > 0) {
						if(value == StatusEnum.MORNING.ordinal()) {
							wardMap.put(ward + "-" + MORNING, wardMap.get(ward + "-" + MORNING) == null ? 1 : wardMap.get(ward + "-" + MORNING) + 1);
						}
						else if(value == StatusEnum.AFTERNOON.ordinal()) {
							wardMap.put(ward + "-" + AFTERNOON, wardMap.get(ward + "-" + AFTERNOON) == null ? 1 : wardMap.get(ward + "-" + AFTERNOON) + 1);
						}
						else if(value == StatusEnum.NIGHT.ordinal()) {
							wardMap.put(ward + "-" + NIGHT, wardMap.get(ward + "-" + NIGHT) == null ? 1 : wardMap.get(ward + "-" + NIGHT) + 1);
						}
						else if(value == StatusEnum.NIGHT_SHIFT.ordinal()) {
							wardMap.put(ward + "-" + NIGHT, wardMap.get(ward + "-" + NIGHT) == null ? 1 : wardMap.get(ward + "-" + NIGHT) + 1);
							wardMap.put(ward + "-" + NIGHT_SHIFT, wardMap.get(ward + "-" + NIGHT_SHIFT) == null ? 1 : wardMap.get(ward + "-" + NIGHT_SHIFT) + 1);
						}
						else if(value == StatusEnum.DAY_SHIFT.ordinal()) {
							wardMap.put(ward + "-" + MORNING, wardMap.get(ward + "-" + MORNING) == null ? 1 : wardMap.get(ward + "-" + MORNING) + 1);
							wardMap.put(ward + "-" + AFTERNOON, wardMap.get(ward + "-" + AFTERNOON) == null ? 1 : wardMap.get(ward + "-" + AFTERNOON) + 1);
							wardMap.put(ward + "-" + DAY_SHIFT, wardMap.get(ward + "-" + DAY_SHIFT) == null ? 1 : wardMap.get(ward + "-" + DAY_SHIFT) + 1);
						}
					}
				}

				for(int wardNumber = 1; wardNumber <= this.wardCount; wardNumber++) {
					if(wardMap.get(wardNumber + "-" + MORNING) == null){
						//TODO Rule 5
						fitnessValue += this.penaltyR5 + 2;
					}
					else if(wardMap.get(wardNumber + "-" + MORNING) < this.minTurnNurse) {
						//TODO Rule 5
						fitnessValue += this.penaltyR5;
					}

					if(wardMap.get(wardNumber + "-" + AFTERNOON) == null){
						//TODO Rule 5
						fitnessValue += this.penaltyR5 + 2;
					}
					else if(wardMap.get(wardNumber + "-" + AFTERNOON) < this.minTurnNurse) {
						//TODO Rule 5
						fitnessValue += this.penaltyR5;
					}

					if(wardMap.get(wardNumber + "-" + NIGHT) == null){
						//TODO Rule 5
						fitnessValue += this.penaltyR5 + 2;
					}
					else if(wardMap.get(wardNumber + "-" + NIGHT) < this.minTurnNurse) {
						//TODO Rule 5
						fitnessValue += this.penaltyR5;
					}

					if(wardMap.get(wardNumber + "-" + NIGHT_SHIFT) == null) {
						//TODO Rule 8a
						fitnessValue += this.penaltyR8a;
					}
					if(wardMap.get(wardNumber + "-" + DAY_SHIFT) == null) {
						//TODO Rule 8b
						fitnessValue += this.penaltyR8b;
					}
				}

				if(value == StatusEnum.MORNING.ordinal() || value == StatusEnum.AFTERNOON.ordinal() || value == StatusEnum.NIGHT.ordinal()) {
					hoursByMonth += 6;
					followingDays++;
				}
				else if(value == StatusEnum.DAY_SHIFT.ordinal() || value == StatusEnum.NIGHT_SHIFT.ordinal()) {
					hoursByMonth += 12;
					followingDays++;
				}
				else {
					if(followingDays > this.maxFollowingWorkDays) {
						//TODO Rule 7
						fitnessValue += this.penaltyR7;
					}
					followingDays = 0;
				}

				if(value == StatusEnum.HOLIDAY.ordinal()) {
					holiday++;
				}
				else if(value == StatusEnum.NIGHT.ordinal() || value == StatusEnum.NIGHT_SHIFT.ordinal()) {
					if(i <= (gens.size() - 4)) {
						int tomorrow = gens.get(i + 2);
						if(tomorrow != StatusEnum.HOLIDAY.ordinal() && tomorrow != StatusEnum.DAY_OFF.ordinal()) {

							//TODO Rule 3
							fitnessValue += this.penaltyR3;
						}
						else if(i <= (gens.size() - 6) && value == StatusEnum.NIGHT_SHIFT.ordinal()) {
							int afterTomorrow = gens.get(i + 4);
							if(afterTomorrow != StatusEnum.HOLIDAY.ordinal() && afterTomorrow != StatusEnum.DAY_OFF.ordinal()
									&& afterTomorrow != StatusEnum.NIGHT.ordinal() && afterTomorrow != StatusEnum.NIGHT_SHIFT.ordinal()) {
								//TODO Rule 4
								fitnessValue += this.penaltyR4;
							}
						}
					}
				}

				//Muda para a próxima enfermeira?
				if((i + 2) / (this.daysInMonth * 2) != currentNurse) {

					//TODO Rule 1
					int vacation = Math.abs(this.holidayCount - holiday) * this.penaltyR1; 
					fitnessValue += vacation;

					if(hoursByMonth > this.maxNurseHoursByMonth) {
						//TODO Rule 6
						fitnessValue += (hoursByMonth - this.maxNurseHoursByMonth) * this.penaltyR6;
					}

					if(followingDays > this.maxFollowingWorkDays) {
						//TODO Rule 7
						fitnessValue += this.penaltyR7;
					}

					holiday = 0;
					hoursByMonth = 0;
					followingDays = 0;
					currentNurse++;
				}
			}
		}

		return fitnessValue;
	}

	@Override
	protected Integer mutateItem(int position) {
		//Odd position (value 0, 2, 4, 6...) ==> Nurse's Work
		int newValue = -1, oldValue = this.getGens().get(position); 
		//Except Holidays and Days Off
		if(oldValue != StatusEnum.DAY_OFF.ordinal() && oldValue != StatusEnum.HOLIDAY.ordinal()) {
			newValue = this.random.nextInt(5);
		}

		return newValue;
	}

	@Override
	public int getGensLength() {
		return 2 * this.nursieCount * this.daysInMonth;
	}

	@Override
	protected Integer generateItem(int position) {
		int value = -1;
		//Pair position (value 1, 3, 5, 7...) ==> Ward number
		if(position % 2 == 1) {
			if(this.hasToWork) {
				value = this.random.nextInt(this.wardCount) + 1;
			}
			else {
				value = -1;
				this.hasToWork = true;
			}
		}
		//Odd position (value 0, 2, 4, 6...) ==> Nurse's Work
		else {
			value = this.random.nextInt(this.maxJobStatus + 1);
			if(value == StatusEnum.DAY_OFF.ordinal() || value == StatusEnum.HOLIDAY.ordinal()) {
				this.hasToWork = false;
			}
		}

		return value;
	}
}
