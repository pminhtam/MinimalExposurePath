package pso_search;

import model.Objective;
import model.Point;

public class IndividualTheoY {
	Objective ob;
	Gene[] genes;
	double objective; ///
	
	public IndividualTheoY(Objective ob, double[] x, int begin, int finish, int length){
		this.ob = ob;
		this.genes = new Gene[length];
		int j;
		for (j = 0; j < length-1; j++)
			this.genes[j] = new Gene(x[j], ob.yk[begin+j], x[j+1], ob.yk[begin + j + 1], ob);
		this.genes[j] = new Gene(x[j], ob.yk[j+begin]);
		this.objective = getObjective(begin, finish, length);
	}
	
	
	public double getObjective() {
		return this.getObjective(0, this.genes.length);
	}
	public double getObjective(int begin, int size) {
		double value = 0;
		for (int i = begin; i < size; i++)
			value += this.genes[i + begin].ip;
		return value;
	}
	public double getObjective(int begin, int finish, int length) {
		double value = 0;
		for (int i = 0; i < length; i++)
			value += this.genes[i].ip;
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
	public double tinhHamMucTieuChoCaThe() {
		double value = 0;
		for (int i = 0; i < genes.length; i++)
			value += this.genes[i].ip;
		return value;
	}
}
