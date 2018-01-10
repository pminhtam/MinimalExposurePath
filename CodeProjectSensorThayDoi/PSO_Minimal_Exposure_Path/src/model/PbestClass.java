package model;

public class PbestClass {
	public static final int SOTOADO = 5001;
	public Point[] point ;
	public double Objective;
	
	public PbestClass() {
		Objective = 0;
		point = new Point[SOTOADO];
	}

	public double getObjective() {
		return Objective;
	}

}
