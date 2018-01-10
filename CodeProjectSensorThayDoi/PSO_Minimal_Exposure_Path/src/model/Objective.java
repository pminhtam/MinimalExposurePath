package model;

import java.util.ArrayList;
import java.util.Random;

public class Objective {
	// Kich thuoc vung
	public int W, H;
	public ArrayList<Sensor> sensors;
	public ArrayList<Obstace> obstaces; // don't care

	// For init
	private static Random r = new Random();
//	public final double dx = 0.1;
	public final double dx = 0.1;

	public final double dy = 0.1;
	public final double randMax = 1.3;
	public int nbSegmentX;
	public int nbSegmentY;
	public double[] xk;
	public double[] yk;

	public Objective(ArrayList<Sensor> sensors, ArrayList<Obstace> obs, int width, int height) {
		this.obstaces = obs;
		this.sensors = sensors;
		this.W = width;
		this.H = height;
		this.nbSegmentX = (int) (width / dx); // số điểm chia ra trên khu vực
												// theo chiều dài
		this.nbSegmentY = (int) (height / dy); // số điểm chia ra tẻe
		this.xk = new double[nbSegmentX];
		this.yk = new double[nbSegmentY];
		xk[0] = 0;
		yk[0] = 0;
		for (int i = 1; i < xk.length; i++)
			xk[i] = xk[i - 1] + dx; // cập nhật vị trí của x
		for (int i = 1; i < yk.length; i++)
			yk[i] = yk[i - 1] + dy; // cập nhật vị trí của y
	}

	public double Sensor_Point(Sensor s, double x, double y) {
		double dx = x - s.p.x; //
		double dy = y - s.p.y;
		double tvh = dx * Math.cos(s.Viangle) + dy * Math.sin(s.Viangle); 
		double d = Math.sqrt(dx * dx + dy * dy);
		return ((d > s.r) || (tvh < d * Math.cos(s.angle))) ? 0 : 1;
//		return (d > Sensor.r) ? 0 : 1;

//		return 3/Math.pow(d, 3);
//		return 500/d;
	}

	// Hàm này trả về tổng cường độ cảm ứng của tất cả các sensor lên 1 điểm
	public double getIP(double x, double y) {
		double value = 0;
		for (int i = 0; i < sensors.size(); i++)
			value = value + Sensor_Point(sensors.get(i), x, y);
		return value;
	}
	public double getIP(double x1, double y1, double x2, double y2) {
		if (y1 < 0 || y1 > H || y2 < 0 || y2 > H)
			return 10000;
		double delta_x = x2 - x1;
		double delta_y = Math.abs(y2 - y1);
		double value = 0;
		double step = y2 < y1 ? -1 : 1;
		while (delta_y >= 1) {
			value += getIP(x1, y1);
			delta_y--;
			y1 += step;
		}
		value += getIP(x1, y1) * Math.sqrt(delta_x * delta_x + delta_y * delta_y);
		return value;
	}

	public double[] initNormal(double ys0, double min, double max) {		//tao mang ngau nhien
		double[] ys = new double[xk.length];
		ys[0] = ys0;
		for (int i = 1; i < ys.length; i++) {
			do {
				ys[i] = ys[i - 1] + (r.nextDouble() * 2 * randMax - randMax);
			} while (ys[i] < min || ys[i] > max);
		}
		return ys;
	}
	
	public double[] initNormal(double ys0, double min, double max,double xstart,double xend) {		//tao mang ngau nhien
		double[] ys = new double[xk.length];
		ys[0] = ys0;
		for (int i = 1; i < ys.length; i++) {
			do {
				ys[i] = ys[i - 1] + (r.nextDouble() * 2 * randMax - randMax);
			} while (ys[i] < min || ys[i] > max);
		}
		return ys;
	}
	
    public double[] initHeuristic(double ys0,double start,double end) {		//khoi tao ca the
		double[] ys = new double[xk.length];
		ys[0] = ys0;
//        ys[0] = start;
//		ys[0] = 100;
		for (int i = 1; i < xk.length; i++) {
            double min = Double.MAX_VALUE, min2 = Double.MAX_VALUE;
            int index = 0;
            int minp = (int)((ys[i - 1] - randMax) / dy);
            int maxp = (int)((ys[i - 1] + randMax) / dy);
            if (minp < 0)
                minp = 0;
            if (maxp >= yk.length)
                maxp = yk.length - 1;
            for (int j = minp; j < maxp; j++) {
                double dy = Math.abs(yk[j] - ys[i - 1]);
//                double v = getIP(xk[i], yk[j])*Math.abs(dx*dx+dy*dy);
                double v =  getIP(xk[i], yk[j]);
                if (v < min || (v == min && dy < min2)) {
//                if (v < min) {
                    min = v;
                    min2 = dy;
                    index = j;
                }
            
            }
            ys[i] = yk[index];
        }
			return ys;
	}	
	
	
	
	
	
}
