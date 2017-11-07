package test;

import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import algorithm.Algothirm;
import data.Data;
import model.Point;
import pso_search.IndividualTheoY;

public class TestIndividualTheoY extends Algothirm {
	
	public static final int SOLANCHAY = 5;

	public static void main(String args[]){
		
		TestIndividualTheoY g = new TestIndividualTheoY();
		FileOutputStream fos = null;
		PrintStream ps = null;
		try {
			fos = new FileOutputStream("ketquaPSO.txt");
			ps = new PrintStream(fos);
			ps.println("filename                result      DLC       time ");
			for (int i = 0; i < Data.dataNum.length; i++) {
				for (int j = 0; j < Data.dataArg.length; j++) {
					for (int k = 0; k < 10; k++) {
						final int nRun = SOLANCHAY;
						double[] kqO = new double[nRun];
						double kqAv = 0;
						double timeAv = 0;
						double xichMa = 0;
						String filename = "datae_" + Data.dataNum[i] + "_" + (k + 1) + ".txt";
						g.readData(filename);
						for (int v = 0; v < nRun; v++) {
							System.out.println("Du lieu " + Data.dataNum[i] + "-" + Data.dataArg[j] + " bo " + (k + 1)
									+ " chay " + v);
							String outName = "data" + Data.dataNum[i] + "_" + Data.dataArg[j] + "_" + (k + 1)
									+ "_ketqua_" + v + "_PSO.txt";
							if (Data.isResultExists(outName))
								g.readOutput(outName);
							else {
								g.RunAlgo();
								g.output(outName);
							}
							kqO[v] = g.minExp;
							kqAv += g.minExp;
							timeAv += g.timeEslapse;
						}
						timeAv /= nRun;
						kqAv /= nRun;
						for (int v = 0; v < nRun; v++)
							xichMa += (kqO[v] - kqAv) * (kqO[v] - kqAv);
						xichMa /= nRun;
						ps.println(filename + "      " + kqAv + "      " + xichMa + "      " + timeAv);
					}
					ps.println("-------------------------------------------------");
				}
			}
		} catch (

		Exception e) {
			e.printStackTrace();
		} finally {
			try {
				ps.close();
				fos.close();
			} catch (Exception e) {
			}
		}
		System.out.println("Done");
	}


	@Override
	public ArrayList<Point> RunAlgo() {
		// TODO Auto-generated method stub
		IndividualTheoY ps =new IndividualTheoY(ob, ob.initNormal(ob.H * rand.nextDouble(), 0, ob.H, 1000), 1000, 2000, 1000);
		for (int i=0;i<ps.getSize();i++){
			System.out.println("Toa do la x = "+ps.getGene(i).x+"   y = "+ps.getGene(i).y);
		}
		return null;
	}
}
