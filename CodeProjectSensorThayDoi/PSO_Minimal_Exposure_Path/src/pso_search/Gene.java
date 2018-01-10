package pso_search;

import model.Point;
import model.Objective;

public class Gene extends Point {
	public double ip;
	public double v;

	public Gene(double x, double y,Objective ob) {
		super(x, y);
		this.ip = 0;
		this.v = 0;
		this.ip = ob.getIP(x,y);
	}

	public Gene(double x, double y, double x1, double y1, Objective ob) {
		super(x, y);
		this.ip = ob.getIP(x, y, x1, y1);
		this.v = 0;
	}
	public void setV(double v){
		this.v = v;
	}
	
}
