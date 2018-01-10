package data;

import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Random;

import model.Obstace;
import model.Sensor;

public class GenerData {
	private final int W = 500;
    private final int H = 500;
    private final int r = 80; // ban kinh
    private double angle;// góc
    private Random rand = new Random();

    private boolean checkObs(ArrayList<Obstace> obs, Obstace ob) {
        // Kiem tra va cham bien
        if (ob.x <= 0 || ob.x + ob.width >= W || ob.y <= 0 || ob.y + ob.height >= H)
            return true;
        // Kiem tra va cham cac obs khac
        for(int i = 0; i < obs.size(); i++) {
            Obstace ob1 = obs.get(i);
            if (ob.x + ob.width >= ob1.x && ob.x <= ob1.x + ob1.width && ob.y + ob.height >= ob1.y && ob.y <= ob1.y + ob1.height)
                return true;
        }
        return false;
    }
    
//    Thuật toán kiểm tra va chạm của sensor và chướng ngại vật
    private boolean checkSensor(ArrayList<Obstace> obs, ArrayList<Sensor> sensors, Sensor sensor) {
        // Kiem tra va cham cac obs
        for (int i = 0; i < obs.size(); i++) {
            Obstace ob1 = obs.get(i);

            // Tam thuoc ob1
            if (sensor.p.x >= ob1.x && sensor.p.x <= ob1.x + ob1.width && sensor.p.y >= ob1.y && sensor.p.y <= ob1.y + ob1.height)
                return true;

            // Tim diem A d(sensor.p, ob1) = OA
            double Ax = sensor.p.x;
            double Ay = sensor.p.y;

            if (sensor.p.x < ob1.x)
                Ax = ob1.x;
            else if (sensor.p.x > ob1.x+ob1.width)
                Ax = ob1.x+ob1.width;

            if (sensor.p.y < ob1.y)
                Ay = ob1.y;
            else if (sensor.p.y > ob1.y+ob1.height)
                Ay = ob1.y+ob1.height;

            // Goc OA
            double dx = Ax - sensor.p.x;
            double dy = Ay - sensor.p.y;
            double ag = Math.atan2(dy, dx);

            // Kiem tra dieu kien
            if (dx*dx + dy*dy <= r*r && ag >= sensor.Viangle-angle && ag <= sensor.Viangle+angle)
                return true;
        }
        return false;
    }

//    Khởi tạo các chướng ngại vật của hình chữ nhật
    public ArrayList<Obstace> GenObs(int n) {
        double rangeMin_x = 0, rangeMax_x = W; // range viết tắt của rectange: hình chữ nhật
        double rangeMin_y = 0, rangeMax_y = H;
        double rangeMin_w = 10, rangeMax_w = 50;
        double rangeMin_h = 10, rangeMax_h = 50;
        ArrayList<Obstace> list = new ArrayList<Obstace>();
        for (int i = 0; i < n; i++) {
            Obstace ob;
            do {
                double x = rangeMin_x + (rangeMax_x - rangeMin_x) * rand.nextDouble();
                double y = rangeMin_y + (rangeMax_y - rangeMin_y) * rand.nextDouble();
                double w = rangeMin_w + (rangeMax_w - rangeMin_w) * rand.nextDouble();
                double h = rangeMin_h + (rangeMax_h - rangeMin_h) * rand.nextDouble();
                ob = new Obstace(x, y, w, h);
            } while(checkObs(list, ob));
            list.add(ob);
        }
        return list;
    }

//    Khởi tạo các sensor.
    public ArrayList<Sensor> GenData(int nsen, ArrayList<Obstace> lobs) {
        ArrayList<Sensor> list = new ArrayList<Sensor>();
        double rangeMin_x = 0, rangeMax_x = W;
        double rangeMin_y = 0, rangeMax_y = H;
        for (int i = 0; i < nsen; i++) {
            Sensor s;
            do {
                double x = rangeMin_x + (rangeMax_x - rangeMin_x) * rand.nextDouble();
                double y = rangeMin_y + (rangeMax_y - rangeMin_y) * rand.nextDouble();
                s = new Sensor(x, y, 2 * Math.PI * rand.nextDouble());
            } while (checkSensor(lobs, list, s));
            list.add(s);
        }
        return list;
    }
    
//	 Ghi du lieu vs k la so lan chay
    private void writeData(int nob, int nsen, int arg, int k) {
        angle = arg * Math.PI / 180;
        String fileName = "data" + nsen + "_" + arg + "_" + k + ".txt";
        try {
            FileOutputStream fos = new FileOutputStream(Data.getDataPath(fileName));
            PrintStream ps = new PrintStream(fos);
            ps.println(nob + " " + nsen + " " + r + " " + angle + " " + W + " " + H);
            ArrayList<Obstace> lobs = GenObs(nob);
            ArrayList<Sensor> list = GenData(nsen, lobs);
            for (int i = 0; i < nob; i++) {
                Obstace ob = lobs.get(i);
                ps.println(ob.x + " " + ob.y + " " + ob.width + " " + ob.height);
            }
            for (int i = 0; i < nsen; i++) {
                Sensor s = list.get(i);
                ps.println(s.p.x + " " + s.p.y + " " + s.Viangle); // in ra tâm + hướng trong 360 độ
            }
            ps.close();
            fos.close();
        } catch(Exception e) {
        	e.printStackTrace();
        }
        System.out.println("Done: " + fileName);
    }

    public static void main(String[] args,int[] dataNum) {
        GenerData g = new GenerData();
        System.out.println("begin");
        for (int i = 0; i < dataNum.length; i++) {
            
                for (int j = 0; j < Data.dataArg.length; j++) {
                    for (int k = 0; k < 5; k++)
                        g.writeData(0, dataNum[i], Data.dataArg[j], k);
                }
            
        }
        System.out.println("Done");
    }
}
