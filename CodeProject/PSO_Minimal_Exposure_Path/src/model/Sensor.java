package model;

public class Sensor {
	public Point p, pt; // p la 1 điểm tâm, pt trọng tâm của vonoi
	public double Viangle; // hướng của sensor
	public static double r;
	public static double angle;

	public static double gamma = 1;
	public static double beta = 1;
	public static double nuy = 1;

	public Sensor(Point p, double Viangle) {
		this.p = p;
		this.Viangle = Viangle;
		this.pt = new Point(p.x + 2 * r * Math.sin(angle) * Math.cos(Viangle) / 3 / angle,
				p.y + 2 * r * Math.sin(angle) * Math.sin(Viangle) / 3 / angle);
	}

	public Sensor(double x, double y, double Viangle) {
		this.p = new Point(x, y);
		this.Viangle = Viangle;
		this.pt = new Point(x + 2 * r * Math.sin(angle) * Math.cos(Viangle) / 3 / angle,
				y + 2 * r * Math.sin(angle) * Math.sin(Viangle) / 3 / angle);
	}

}
