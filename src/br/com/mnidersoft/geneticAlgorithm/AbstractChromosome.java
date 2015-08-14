package br.com.mnidersoft.geneticAlgorithm;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractChromosome<T> {
	
	private List<T> gens;
	private int length;
	private int fitness;
	
	public AbstractChromosome() {}
	
	public final int getFitness() {
		return this.fitness;
	}
	
	protected final void updateFitness() {
		if(this.fitness == 0) { 
			this.fitness = this.calcFitness(this.gens);
		}
	}
	
	public void init() {
		this.length = this.getGensLength();
		this.gens = new ArrayList<>(this.length);
		this.populate();
	}
	
	public void init(List<T> gens) {
		this.length = this.getGensLength();
		this.gens = gens;
	}
	
	protected final void populate(boolean haveToclean) {
		if(haveToclean) {
			this.gens.clear();
		}
		
		for(int i = 0; i < this.length; i++) {
			T t = this.generateItem(i);
			if(t != null) {
//				System.out.println("O antigo = " + this.gens.get(i) + " e novo = " + t);
				this.gens.add(i, t);
			}
		}
	}
	
	protected final void populate() {
		this.populate(true);
	}
	
	public List<T> getGens() {
		return this.gens;
	}
	
	public final T getElementAt(int position) {
		return this.gens.get(position);
	}
	
	public final void setElementAt(int index, T element) {
		this.gens.set(index, element);
	}
	
	protected abstract int calcFitness(List<T> gens);
	
	public abstract int getGensLength();
	
	protected abstract T generateItem(int position);
	
	protected abstract T mutateItem(int position);
	
	@Override
	public String toString() {
		String text = "";
		for(int i = 0; i < this.length; i++) {
			text += "i = " + i + " ==> " + this.gens.get(i).toString() + "\n";
		}
		text += "Length = " + this.length + " e fitness = " + this.calcFitness(this.gens);
		
		return text;
	}
}
