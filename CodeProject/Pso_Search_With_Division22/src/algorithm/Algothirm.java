package algorithm;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;

import data.Data;
import model.Objective;
import model.Obstace;
import model.Point;
import model.Sensor;
import pso_search.Gene;
import pso_search.Individual;

public abstract class Algothirm {
	public Random rand = new Random();
	public Objective ob;
//	public Point[] ketqua ;
	public ArrayList<Point> ketQua;
	public double minExp;
	public long timeEslapse;
	public BufferedImage bitmap;

	@SuppressWarnings("resource")
	public void readData(String filename) throws Exception {
		ArrayList<Obstace> lobs = new ArrayList<Obstace>();
		ArrayList<Sensor> list = new ArrayList<Sensor>();
		BufferedReader input = new BufferedReader(new FileReader(Data.getDataPath(filename)));
		String s = input.readLine();
		String[] s1 = s.split(" ");
		int nob = Integer.parseInt(s1[0]); // So vat can trong khu vực
		int nse = Integer.parseInt(s1[1]); // Số lượng sensor
		Sensor.r = Double.parseDouble(s1[2]); // bán kính
		Sensor.angle = Double.parseDouble(s1[3]); // Góc
		int W = Integer.parseInt(s1[4]);// chiều dài
		int H = Integer.parseInt(s1[5]);// chiều rộng
		// thêm vật cản vào khu vực
		for (int i = 0; i < nob; i++) {
			String temp = input.readLine();
			String[] t = temp.split(" ");
			lobs.add(new Obstace(Double.parseDouble(t[0]), Double.parseDouble(t[1]), Double.parseDouble(t[2]),
					Double.parseDouble(t[3])));
		}
		// Sinh ra các sensor
		for (int i = 0; i < nse; i++) {
			String temp = input.readLine();
			String[] t = temp.split(" ");
			Point p = new Point(Double.parseDouble(t[0]), Double.parseDouble(t[1]));
			double angle = Double.parseDouble(t[2]);
			list.add(new Sensor(p, angle));
		}
		this.ob = new Objective(list, lobs, W, H);
		this.bitmap = new BufferedImage(W, H + 20, BufferedImage.TYPE_INT_ARGB);// ??????
																				// CỘng
																				// 20
																				// làm
																				// gì?
	}

	@SuppressWarnings("resource")
	public void readOutput(String filename) throws Exception {
		if (this.ob == null)
			return;
		BufferedReader input = new BufferedReader(new FileReader(Data.getResultPath(filename)));
		String[] line = input.readLine().split(" ");
		int lent = Integer.parseInt(line[0]); // lent = phan tu thu 1
		minExp = Double.parseDouble(line[1]); // minExp = phan tu thu 2
		timeEslapse = Long.parseLong(line[2]); // timeEslapse = phan tu thu 3
		ArrayList<Point> ketqua = new ArrayList<>();
		Gene[] ketquagene = new Gene[lent];
		
		for (int i = 0; i < lent; i++) {
			line = input.readLine().split(" ");
//			ketqua[i] = new Point(Double.parseDouble(line[0]), Double.parseDouble(line[1]));
			ketqua.add(new Point(Double.parseDouble(line[0]), Double.parseDouble(line[1])));
		}
		int i=0;
		for(i=0;i<lent-1;i++){
			ketquagene[i] = new Gene(ketqua.get(i).x,ketqua.get(i).y,ketqua.get(i+1).x,ketqua.get(i+1).y,ob);
		}
		ketquagene[i] = new Gene(ketqua.get(i).x,ketqua.get(i).y);
//		draw(null);
		double value = 0;
		for (i = 0; i < lent; i++)
			value += ketquagene[i].ip;
		System.out.println("Gia trị min tinh duoc la: "+value);
		System.out.println("Gia trị min theo ket qua la: "+minExp);
	}

	public abstract ArrayList<Point> RunAlgo();

	public void output(String filename) {			//ghi kết quả ra file
		if (ketQua == null)
			RunAlgo();
		FileOutputStream fos = null;
		PrintStream pw = null;
		try {
			fos = new FileOutputStream(Data.getResultPath(filename));
			pw = new PrintStream(fos);
			pw.println(ketQua.size() + " " + minExp + " " + timeEslapse);
			for (int i = 0; i < ketQua.size(); i++)
				pw.println(ketQua.get(i).x + " " + ketQua.get(i).y);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				pw.close();
				fos.close();
			} catch (Exception e) {
			}
		}
		draw(Data.getResultPath(filename + ".png"));
	}

	public void draw(String filename) {			//vẽ hình kết quả ra file
		Graphics g = bitmap.getGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, ob.W +20, ob.H + 20);
		 g.setColor(Color.WHITE);
		 for (int i = 0; i < this.ob.obstaces.size(); i++) {
		 Obstace ob = this.ob.obstaces.get(i);
		 // Dao nguoc toa do. Toa do ve = p(x, h-y)
		 g.fillRect((int)ob.x, (int)ob.y, (int)ob.width, (int)ob.height);
		 }
		g.setColor(Color.BLUE);
		for (int i = 0; i < ob.sensors.size(); i++) {
			Sensor s = ob.sensors.get(i);
			// Dao nguoc toa do. Toa do ve = p(x, h-y)
			g.fillArc((int) (s.p.x - Sensor.r), (int) (s.p.y - Sensor.r), (int) (2 * Sensor.r), (int) (2 * Sensor.r),
					(int) ((-s.Viangle - Sensor.angle) * 180 / Math.PI), (int) (Sensor.angle * 360 / Math.PI));
		}
		g.setColor(Color.RED);
		Point[] var = new Point[ketQua.size()];
		ketQua.toArray(var);
		for (int j = 0; j < var.length - 1; j++) {
			Point p1 = var[j];
			Point p2 = var[j + 1];
			g.drawLine((int) p1.x, (int) p1.y, (int) p2.x, (int) p2.y);
		}
		g.setColor(Color.BLACK);
		g.drawString("MinE: " + minExp, 0, ob.H + 5);
		if (filename != null) {
			try {
				ImageIO.write(bitmap, "png", new FileOutputStream(filename));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
