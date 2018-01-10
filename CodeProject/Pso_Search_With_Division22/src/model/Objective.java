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
	public final double dx = 0.1;
	public final double dy = 0.1;
	public final double randMax = 1.6;
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
		return ((d > Sensor.r) || (tvh < d * Math.cos(Sensor.angle))) ? 0 : 1;
//		return 1/d;
//		return 3/Math.pow(d, 3);


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
//		System.out.println("deltax  "+ delta_x + "      delta_y  la  "+ delta_y);

		while (delta_y >= 1) {
			value += getIP(x1, y1);
			delta_y--;
			y1 += step;
		}
//		value += getIP(x1, y1) * Math.sqrt(delta_x * delta_x + delta_y * delta_y);
		value += getIP(x1, y1);
		value = value*0.1;

		return value;
	}

	public double[] initNormal(double ys0, double min, double max) {
		double[] ys = new double[xk.length];
		ys[0] = ys0;
		for (int i = 1; i < ys.length; i++) {
			do {
				ys[i] = ys[i - 1] + (r.nextDouble() * 2 * randMax - randMax);
			} while (ys[i] < min || ys[i] > max);
		}
		return ys;
	}
	
	public double[] initNormal(double ys0, double min, double max, int length) {
		double[] ys = new double[length];
		ys[0] = ys0;
		for (int i = 1; i < length; i++) {
			do {
				ys[i] = ys[i - 1] + (r.nextDouble() * 2 * randMax - randMax);
			} while (ys[i] < min || ys[i] > max);
		}
		return ys;
	}
	
	public double[] initNormal(ArrayList<Double> luuViTriTotNhatDauVao,double min, double max) {
		double[] ys = new double[xk.length];
		
		for(int i=0; i< luuViTriTotNhatDauVao.size(); i++){
			ys[i]=luuViTriTotNhatDauVao.get(i);
		}
		for (int i = luuViTriTotNhatDauVao.size(); i < ys.length; i++) {
			do {
				ys[i] = ys[i - 1] + (r.nextDouble() * 2 * randMax - randMax);
			} while (ys[i] < min || ys[i] > max);
		}
		return ys;
	}
//	public double[] initNormal(double diemCoDinh, double ys0, double min, double max, int length) {
//		double[] ys = new double[length];
//		ys[0] = ys0;
//		for (int i = 1; i < ys.length; i++) {
//			do {
//				ys[i] = ys[i - 1] + (r.nextDouble() * 2 * randMax - randMax);
//			} while (ys[i] < min || ys[i] > max);
//		}
//		return ys;
//	}
    public double[] initHeuristic(double ys0,int begin,int end,double min, double max, int length) {
		double[] ys = new double[length+1];		//tao đoạn gene có chiều dài là length
        ys[0] = ys0;							//gán giá trị bắt đầu
		for (int i = 1; i < length-1; i++) {		//xác định từng gene
            double minip = Double.MAX_VALUE, min2 = Double.MAX_VALUE;
            int index = 0;
            int minp = (int)((ys[i - 1] - randMax) / dy);
            int maxp = (int)((ys[i - 1] + randMax) / dy);
            if (minp < (int)(min/dy))
                minp = (int)(max/dy)+1;
            if (maxp >= (int)(max/dy))
                maxp = (int)(max/dy)-1;
            for (int j = minp; j < maxp; j++) {
                double dy = Math.abs(yk[j] - ys[i - 1]);
//                double v = getIP(xk[begin+i], yk[j])*Math.abs(dx*dx+dy*dy);
                double v = getIP(xk[begin+i], yk[j]);
                if (v < minip || (v == minip && dy < min2)) {
                    minip = v;
                    min2 = dy;
                    index = j;
                }
            }
            ys[i] = yk[index];
        }
			return ys;
	}
    public double[] initHeuristicTheoY(double xs0,int begin,int end,double min, double max, int length) {
		double[] xs = new double[yk.length];
        xs[0] = xs0;
		for (int i = 1; i < length; i++) {
            double minip = Double.MAX_VALUE, min2 = Double.MAX_VALUE;
            int index = 0;
            int minp = (int)((xs[i - 1] - randMax) / dx);
            int maxp = (int)((xs[i - 1] + randMax) / dx);
            if (minp < (int)(min/dx))
                minp = (int)(max/dx)+1;
            if (maxp >= (int)(max/dx))
                maxp = (int)(max/dx)-1;
            for (int j = minp; j < maxp; j++) {
                double dx = Math.abs(xk[j] - xs[i - 1]);
//                double v = getIP(xk[j], yk[begin+i])*Math.abs(dx*dx+dy*dy);
                double v =  getIP(xk[i], yk[j]);
                if (v < minip || (v == minip && dx < min2)) {
                    minip = v;
                    min2 = dx;
                    index = j;
                }
            }
            xs[i] = yk[index];
        }
			return xs;
	}
}
