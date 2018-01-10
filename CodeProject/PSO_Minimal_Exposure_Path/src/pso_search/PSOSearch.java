package pso_search;

import model.PbestClass;
import model.Point;
import data.Data;
import pso_search.Individual;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import algorithm.Algothirm;

public class PSOSearch extends Algothirm {
	// So ca the cua quan the
	public static final int POPNUM = 100;
	public static final int SOLANCHAY = 20;

	// So luong the he
	public static final int PSOINTER = 200;

	// he so quan tinh w
	public static final double W = 0.3;
	// he so van toc theo maxGene
	public static final double C1 = 0.2;
	// he so van toc theo doi tuong lon nhat
	public static final double C2 = 0.2;

	public static final int SOTOADO = 5001;

	public static final int SOCATHE = POPNUM;
	// Quan the
	public Individual[] population;

	// Khoi tao quan the
	private Individual[] InitSolution() {
		Individual[] ps = new Individual[POPNUM];
		int i = 0;
		int k=0;
		for (k = 0; k < POPNUM; k++) { 
			ps[i++] = new Individual(ob, ob.initNormal(ob.H * rand.nextDouble(), 0, ob.H),250,250);
//		}
//		for (; k < POPNUM; k++) { 

//			ps[i++] = new Individual(ob, ob.initHeuristic(ob.H * rand.nextDouble(),250,250),250,250);

			System.out.printf("Ca the thu %d duoc khoi tao \n ", i); // **//
//			System.out.println("Mep = " + ps[k].getObjective());
		}
//		double[] y0 =new double[ob.xk.length];
//		for(int k=0;k<ob.xk.length;k++){
//			y0[i] = 0.1;
//		}
//		ps[POPNUM-2] = new Individual(ob,y0);
//		System.out.printf("Ca the thu %d duoc khoi tao ",POPNUM-2 ); // **//
//		System.out.println("Mep = " + ps[POPNUM-2].getObjective());
//		double[] yMax =new double[ob.xk.length];
//		for(int k=0;k<ob.xk.length;k++){
//			yMax[i] = 500-0.1;
//		}
//		ps[POPNUM-1] = new Individual(ob,yMax);
//		System.out.printf("Ca the thu %d duoc khoi tao ", POPNUM-2); // **//
//		System.out.println("Mep = " + ps[POPNUM-2].getObjective());
		return ps;
	}

	private void searchPSO(int iter) {
		long start = System.currentTimeMillis();
		System.out.println("Start init tttttmmmmmmmmm");
		System.out.println("Bước 1:  khởi tạo quần thể: ");
		population = InitSolution(); // khoi tao quan the

		PbestClass[] Pbest = new PbestClass[SOCATHE];

		for (int i = 0; i < SOCATHE; i++) {
			Pbest[i] = new PbestClass();
		}

		PbestClass Gbest = new PbestClass();

		// Khoi tao Pbest
		for (int i = 0; i < SOCATHE; i++) {
			Pbest[i].point = population[i].Points();
			Pbest[i].Objective = population[i].getObjective();

		}
		// Tim Gbest
		// Tim Gbest trong tat ca cac Pbest
		int xacDinhCaTheGbest = 0;
		for (int a = 1; a < SOCATHE; a++) {
			if (Pbest[xacDinhCaTheGbest].Objective > Pbest[a].Objective) {
				xacDinhCaTheGbest = a;
			}
		}
		Gbest = Pbest[xacDinhCaTheGbest];
		// ................
		int xacDinhPbestToiNhat = 0;
		int theHe = 0;
		ArrayList<Double> vanToc = new ArrayList<>();
		do {
			System.out.println("============= Thế hệ thứ " + ++theHe + " ===============");
			System.out.println("Gbest Mep = " + Gbest.Objective);
			for (int k = 0; k < SOCATHE; k++) {
//				System.out.println("********** Cá thể thứ " + (k + 1) + " ************");

				double r1 = rand.nextDouble();
				double r2 = rand.nextDouble();

				for (int j = 0; j < population[0].getSize(); j++) {
					//
					double y_ti = population[k].getGene(j).y;

					double v_t1i = W * population[k].getGene(j).v + C1 * r1 * (Pbest[k].point[j].y - y_ti)
							+ C2 * r2 * (Gbest.point[j].y - y_ti);
					double y_t1i = y_ti + v_t1i;
					// Gan vi tri moi y_t1i cho gene va tinh lai ip cho
					// gene[j-1] va ip cua gene[j]
					if(y_t1i >0 && y_t1i<ob.H)
						population[k].setGene(j, new Gene(population[k].getGene(j).x, y_t1i,ob));
					// Gan gia tri van toc cho tung gene. => Cac van toc cua
					// gene tao nen 1 vector van toc
					population[k].genes[j].v = v_t1i;
					// population[k].getGene(j).setV(v_t1i);
//					System.out.println("Van toc v = " + population[k].genes[j].v);
//					System.out.println("Toa do y = " + population[k].genes[j].y);
					vanToc.add(v_t1i);
				}
				for (int j = 0; j < population[0].getSize(); j++) {
					population[k].genes[j].v = vanToc.get(j);
				}
				vanToc.clear();

//				System.out.println("Mep: " + population[k].tinhHamMucTieuChoCaThe());
				if (Pbest[k].Objective > population[k].tinhHamMucTieuChoCaThe()) { //xac dinh Pbest
					Pbest[k].point = population[k].Points();
					Pbest[k].Objective = population[k].tinhHamMucTieuChoCaThe();
				}

//				System.out.println("Ca the thu " + k + " Pbest = " + Pbest[k].Objective);
//				System.out.println("---------------------------------------------------");
			}

			// Xac dinh Pbest toi nhat:

//			for (int b = 0; b < SOCATHE; b++) {
//				if (Pbest[xacDinhPbestToiNhat].Objective < Pbest[b].Objective) {
//					xacDinhPbestToiNhat = b;
//				}
//			}
			// Xac dinh lai ca the Gbest
			xacDinhCaTheGbest = 0;
			for (int a = 1; a < SOCATHE; a++) {
				if (Pbest[xacDinhCaTheGbest].Objective > Pbest[a].Objective && a!=POPNUM-1 && a!= POPNUM-2) {
					xacDinhCaTheGbest = a;
				}
			}
			System.out.println("The he thu "+ theHe + "ca the best la " + Pbest[xacDinhCaTheGbest].Objective);
			Gbest = Pbest[xacDinhCaTheGbest];
		} while (theHe < PSOINTER && Gbest.Objective > 0);

		System.out.println("xxxxxxxxxxxxx XONG GIAI THUAT PSO xxxxxxxxxxxxxxxxxx");

		this.ketqua = Gbest.point;
		// this.ketqua = population[xacDinhPbestToiNhat].Points();
		this.minExp = Gbest.Objective;
		this.timeEslapse = System.currentTimeMillis() - start;
		System.out.println("KQ: MinEXP = " + minExp);
		System.out.println("Time: " + timeEslapse);
	}

	// ***//
	@Override
	public Point[] RunAlgo() {
		searchPSO(PSOINTER);
//		population = InitSolution(); // khoi tao quan the
//		draw(Data.getResultPath("ketqua.png"),population);	
		return ketqua;
	}

	
	public static void main(String[] args) {
		PSOSearch g = new PSOSearch();
		FileOutputStream fos = null;
		PrintStream ps = null;
		String[] datatype = {"datae","datag","datar"};

		try {
			fos = new FileOutputStream("ketquaPSO.txt");
			ps = new PrintStream(fos);
			ps.println("filename                result      DLC       time ");
			//Chay bo 10
			int[] dataNum = Data.dataNum500;
			for (int i = 0; i < dataNum.length; i++) {
				for (int j = 0; j < Data.dataArg.length; j++) {
					for (int k = 0; k < 10; k++) {
						for (int typedata = 0;typedata<3;typedata++){
	
							final int nRun = SOLANCHAY;
							double[] kqO = new double[nRun];
							double kqAv = 0;
							double timeAv = 0;
							double xichMa = 0;
	//						String filename = "datae_" + dataNum[i] + "_" + (k + 1) + ".txt";
							String filename = datatype[typedata]+"/"+ datatype[typedata]+"_" + dataNum[i] + "_" + (k + 1) + ".txt";
							g.readData(filename);
							for (int v = 0; v < nRun; v++) {
								System.out.println("Du lieu " + dataNum[i] + "-" + Data.dataArg[j] + " bo " + (k + 1)
										+ " chay " + v);
//								String outName = "data" + dataNum[i] + "_" + Data.dataArg[j] + "_" + (k + 1)
//										+ "_ketqua_" + v + "_PSO.txt";
								String outName = datatype[typedata]+"/"+ datatype[typedata]+"_" + dataNum[i] + "_" + Data.dataArg[j] + "_" + (k + 1)
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
}
