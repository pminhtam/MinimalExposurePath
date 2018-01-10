package pso_search;

import model.Objective;
import model.Point;

public class Individual {

	protected Objective ob;
	protected Gene[] genes;
	protected double objective; ///

	public Individual(Objective ob, double[] y,double start,double end) { // khoi tao mot ca the
		this.ob = ob;
		this.genes = new Gene[y.length];
		int j=0;
//		this.genes[j] = new Gene(ob.xk[j],start, ob.xk[j + 1], y[j + 1], ob);
//		y[0] = start;
//		y[y.length-1]  = end;
		for (j = 0; j < y.length-1; j++)
			this.genes[j] = new Gene(ob.xk[j], y[j], ob.xk[j + 1], y[j + 1], ob);
		this.genes[j] = new Gene(ob.xk[j], y[j],ob);
		this.objective = getObjective();
	}

	private Individual(Individual ind) {
		this.ob = ind.ob;
		this.objective = ind.objective;
		this.genes = new Gene[ind.genes.length];
		for (int j = 0; j < genes.length; j++)
			this.genes[j] = ind.genes[j];
	}

	public double getObjective() {
		return this.getObjective(0, this.genes.length);
	}

	public double getObjective(int begin, int size) {
		double value = 0;
		for (int i = 0; i < size; i++)
			value += this.genes[i + begin].ip;
		return value;
	}

	public int getSize() {
		return this.genes.length;
	}

	public Point[] Points() {
		return this.genes;
	}

	public Gene getGene(int index) {
		return this.genes[index];
	}

	public double objective() {
		return this.objective;
	}

	
	public void setGene(int index, Gene value) {
		if (index > 0)
			this.genes[index - 1] = new Gene(this.genes[index - 1].x, this.genes[index - 1].y, value.x, value.y, ob);
		if (index < this.genes.length - 1)
			this.genes[index] = new Gene(value.x, value.y, this.genes[index + 1].x, this.genes[index + 1].y, ob);
		else
			this.genes[index] = value;
		// this.objective = getObjective();
	}

	//
	public double tinhHamMucTieuChoCaThe() {
		double value = 0;
		for (int i = 0; i < genes.length; i++)
			value += this.genes[i].ip;
		return value;
	}

	// Lai
	// public void setGene(int index, int size, Individual ind) {
	// for (int i = 0; i < size; i++)
	// this.genes[index + i] = ind.genes[index + i];
	// if (index > 0)
	// this.genes[index - 1] = new Gene(this.genes[index - 1].x,
	// this.genes[index - 1].y, ind.genes[index].x,
	// ind.genes[index].y, ob);
	// if (index + size < this.genes.length)
	// this.genes[index + size - 1] = new Gene(this.genes[index + size - 1].x,
	// this.genes[index + size - 1].y,
	// this.genes[index + size].x, this.genes[index + size].y, ob);
	// this.objective = getObjective();
	// }
}
