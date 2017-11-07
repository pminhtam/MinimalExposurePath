package pso_search;

import model.PbestClass;
import model.Point;
import data.Data;
import pso_search.Individual;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import algorithm.Algothirm;
import pso_search.IndividualTheoY;

public class PSOSearchWithDivision extends Algothirm {
	// So ca the cua quan the
	public static final int SOCATHE = 50;
	public static final int SOLANCHAY = 25;

	// So luong the he
	public static final int SOTHEHE = 15;

	// he so quan tinh w
	public static final double W = 0.2;
	// he so van toc theo maxGene
	public static final double C1 = 1;
	// he so van toc theo doi tuong lon nhat
	public static final double C2 = 1;

	public static final int SOTOADO = 5001;
	// Quan the
	public Individual[] population;
	Individual[] ps;
	double objectiveMin=0.0;
	// Khoi tao quan the
	private Individual[] InitSolution() {

		for (int k = 0; k < SOCATHE; k++) {
			ps[k] = new Individual(ob, ob.initNormal(ob.H * rand.nextDouble(), 0, ob.H));
			System.out.printf("Ca the thu %d duoc khoi tao ", (k + 1)); // **//
			System.out.println("Mep = " + ps[k].getObjective());
		}
		return ps;
	}

	private Individual[] InitSolution(int begin, int finish, int length) {		//Tao toa do cua quan thể

		for (int k = 0; k < SOCATHE; k++) {
			ps[k] = new Individual(ob, ob.initNormal(ob.H * rand.nextDouble(), 0, ob.H, length), begin, finish, length);
//			System.out.printf("Ca the thu %d duoc khoi tao ", (k + 1)); // **//
//			System.out.println("Mep = " + ps[k].getObjective(begin, finish, length));
		}

		return ps;
	}

	private int xacDinhIndexCaTheGbest(PbestClass[] Pbest) {
		// Tim Gbest
		// Tim Gbest trong tat ca cac Pbest
		int xacDinhCaTheGbest = 0;
		for (int a = 1; a < SOCATHE; a++) {
			if (Pbest[xacDinhCaTheGbest].Objective > Pbest[a].Objective) {
				xacDinhCaTheGbest = a;
			}
		}
		return xacDinhCaTheGbest;
	}

	ArrayList<Point> luuViTriTimKiem = new ArrayList<>();
	private void timKiemChoBatDauDiVao(int begin, int finish, int soToaDo) {
		// Tim tu vi tri x0 = 0 den x1200;
		PbestClass GTimKiemChoBatDauDiVaoBest;
		GTimKiemChoBatDauDiVaoBest = new PbestClass(soToaDo);

		ps = new Individual[SOCATHE];

		population = InitSolution(begin, finish, soToaDo);
		PbestClass[] Pbest = new PbestClass[SOCATHE];

		for (int i = 0; i < SOCATHE; i++) {
			Pbest[i] = new PbestClass(soToaDo);
		}
		// Khoi tao Pbest
		for (int i = 0; i < SOCATHE; i++) {
			Pbest[i].point = population[i].Points();
			Pbest[i].Objective = population[i].getObjective(begin, finish, soToaDo);
		}
		GTimKiemChoBatDauDiVaoBest = Pbest[xacDinhIndexCaTheGbest(Pbest)];
		// int xacDinhPbestToiNhat = 0;
		int theHe = 0;
		double y_ti, y_t1i, v_t1i = 0, r1, r2;
		ArrayList<Double> vanToc = new ArrayList<>();

		while (theHe < SOTHEHE && GTimKiemChoBatDauDiVaoBest.Objective > 0) {
			System.out.println("============= Thế hệ thứ " + ++theHe + " ===============");
			System.out.println("Gbest Mep = " + GTimKiemChoBatDauDiVaoBest.Objective);
			for (int k = 0; k < SOCATHE; k++) {
				System.out.println("********** Cá thể thứ " + (k + 1) + " ************");
				r1 = rand.nextDouble();
				r2 = rand.nextDouble();

				for (int j = 0; j < population[0].getSize(); j++) {

					y_ti = population[k].getGene(j).y;

					v_t1i = W * population[k].getGene(j).v + C1 * r1 * (Pbest[k].point[j].y - y_ti)
							+ C2 * r2 * (GTimKiemChoBatDauDiVaoBest.point[j].y - y_ti);
					// }
					vanToc.add(v_t1i);
					y_t1i = y_ti + v_t1i;
					population[k].setGene(j, new Gene(population[k].getGene(j).x, y_t1i));
				}

				for (int m = 0; m < vanToc.size(); m++) {
					population[k].genes[m].v = vanToc.get(m);
				}
				vanToc.clear();
				System.out.println("Mep: " + population[k].tinhHamMucTieuChoCaThe());
				if (Pbest[k].Objective > population[k].getObjective(begin, finish, soToaDo)) {
					Pbest[k].point = population[k].Points();
					Pbest[k].Objective = population[k].getObjective(begin, finish, soToaDo);
				}
				System.out.println("Pbest = " + Pbest[k].Objective);
				System.out.println("---------------------------------------------------");
			}
			GTimKiemChoBatDauDiVaoBest = Pbest[xacDinhIndexCaTheGbest(Pbest)];
		}
		for (int i = 0; i < GTimKiemChoBatDauDiVaoBest.point.length; i++) {
			luuViTriTimKiem.add(GTimKiemChoBatDauDiVaoBest.point[i]);
		}
		objectiveMin +=GTimKiemChoBatDauDiVaoBest.getObjective();

//		System.out.println("Xong tìm kiếm đầu vào");
//		System.out.println("luuViTri.size() = " + luuViTriTimKiem.size());
//		System.out.println("Kich thuoc doan gene la   " + GTimKiemChoBatDauDiVaoBest.point.length);
		
//		System.out.println("Diem dau la x = " + GTimKiemChoBatDauDiVaoBest.point[0].x + "  y = "+ GTimKiemChoBatDauDiVaoBest.point[0].y );
//		System.out.println("Diem cuoi la x = " + GTimKiemChoBatDauDiVaoBest.point[GTimKiemChoBatDauDiVaoBest.point.length-1].x + "  y = "+ GTimKiemChoBatDauDiVaoBest.point[GTimKiemChoBatDauDiVaoBest.point.length-1].y );
		
	} 
	//*****************************************************************//
	//Tìm kiếm theo Y
	IndividualTheoY[] psY;
	public IndividualTheoY[] populationTheoY;
	int[] soLuongGeneTheoY;
	ArrayList<Point> luuViTriTimKiemTheoY = new ArrayList<>();

	private IndividualTheoY[] InitSolutionTheoY(int begin, int finish, int length,Point pointBegin) {

		for (int k = 0; k < SOCATHE; k++) {
			psY[k] = new IndividualTheoY(ob, ob.initNormal(pointBegin.x, 0, ob.H, length), begin, finish, length);

//			System.out.printf("Tim kiem theo YYYYY Ca the thu %d duoc khoi tao ", (k + 1)); // **//
//			System.out.println("Mep = " + psY[k].getObjective(begin, finish, length));
		}

		return psY;
	}

	private int timKiemTheoTrucY(Point beginTheoY,Point endTheoY){
		int beginY = (int) (beginTheoY.y/ob.dy);
		int finishY = (int) (endTheoY.y/ob.dy);
		int soToaDo = Math.abs(beginY-finishY);
		int begin,finish;
		if(beginY<finishY){
			begin = beginY;
			finish = finishY;
		}
		else{
			begin = finishY;
			finish = beginY;
		}
		System.out.println("Tim kiem theo y bat dau la " + begin + "  ket thuc la  " + finish+"   so toa do la "+soToaDo);
		PbestClass GTimKiemChoBatDauDiVaoBest;
		GTimKiemChoBatDauDiVaoBest = new PbestClass(soToaDo);
		psY = new IndividualTheoY[SOCATHE];
		populationTheoY = InitSolutionTheoY(begin, finish, soToaDo,beginTheoY);
		PbestClass[] Pbest = new PbestClass[SOCATHE];
	
		for (int i = 0; i < SOCATHE; i++) {
			Pbest[i] = new PbestClass(soToaDo);
		}
		// Khoi tao Pbest
		for (int i = 0; i < SOCATHE; i++) {
			Pbest[i].point = populationTheoY[i].Points();
			Pbest[i].Objective = populationTheoY[i].getObjective(begin, finish, soToaDo);
		}
		GTimKiemChoBatDauDiVaoBest = Pbest[xacDinhIndexCaTheGbest(Pbest)];
		// int xacDinhPbestToiNhat = 0;
		int theHe = 0;
		double x_ti, x_t1i, v_t1i = 0, r1, r2;
		ArrayList<Double> vanToc = new ArrayList<>();
		while (theHe < SOTHEHE && GTimKiemChoBatDauDiVaoBest.Objective > 0) {
			System.out.println("============= Thế hệ thứ " + ++theHe + " ===============");
			System.out.println("Gbest Mep = " + GTimKiemChoBatDauDiVaoBest.Objective);
			for (int k = 0; k < SOCATHE; k++) {
				System.out.println("********** Cá thể thứ " + (k + 1) + " ************");
				r1 = rand.nextDouble();
				r2 = rand.nextDouble();
	
				for (int j = 0; j < populationTheoY[0].getSize(); j++) {
	
					x_ti = populationTheoY[k].getGene(j).x;
					v_t1i = W * populationTheoY[k].getGene(j).v + C1 * r1 * (Pbest[k].point[j].x - x_ti)
							+ C2 * r2 * (GTimKiemChoBatDauDiVaoBest.point[j].x - x_ti);
					// }
					vanToc.add(v_t1i);
					x_t1i = x_ti + v_t1i;
					populationTheoY[k].setGene(j, new Gene(x_t1i,populationTheoY[k].getGene(j).y));
				}
				for (int m = 0; m < vanToc.size(); m++) {
					populationTheoY[k].genes[m].v = vanToc.get(m);
				}
				vanToc.clear();
				System.out.println("Tim kiem thep Y  Mep: " + populationTheoY[k].tinhHamMucTieuChoCaThe());
				if (Pbest[k].Objective > populationTheoY[k].getObjective(begin, finish, soToaDo)) {
					Pbest[k].point = populationTheoY[k].Points();
					Pbest[k].Objective = populationTheoY[k].getObjective(begin, finish, soToaDo);
				}
				System.out.println("Pbest = " + Pbest[k].Objective);
				System.out.println("---------------------------------------------------");
			}
			GTimKiemChoBatDauDiVaoBest = Pbest[xacDinhIndexCaTheGbest(Pbest)];
		}
		if(beginY<finishY){
			for (int i = 0; i < GTimKiemChoBatDauDiVaoBest.point.length; i++) {
				luuViTriTimKiemTheoY.add(GTimKiemChoBatDauDiVaoBest.point[i]);
			}
		}
		else{
			for (int i = GTimKiemChoBatDauDiVaoBest.point.length-1; i >=0; i--) {
				luuViTriTimKiemTheoY.add(GTimKiemChoBatDauDiVaoBest.point[i]);
			}
		}
		
		
//		System.out.println("Xong tìm kiếm đầu vào  theo YYYYYYYYYYYYYYYYYYY");
//		System.out.println(" tim theo YYYYYYYYYYYYYYYYYYYY luuViTri.size() = " + luuViTriTimKiemTheoY.size());
		objectiveMin +=GTimKiemChoBatDauDiVaoBest.getObjective();
		return GTimKiemChoBatDauDiVaoBest.point.length;
	}
	//*******************************************************************//
	//hết tìm kiếm theo Y//
	// ***//
	@Override
	public ArrayList<Point> RunAlgo() {
		objectiveMin = 0;
		 soLuongGeneTheoY = new int[5];
		 timKiemChoBatDauDiVao(0, 1000, 1000);
		 timKiemChoBatDauDiVao(1000, 2000, 1000);
		 
		 //******************//
		 System.out.println("+++++++++++++++++++++++++++++++++++++++++++++");
		 System.out.println("+++++++++++++++++++++++++++++++++++++++++++++");
		 System.out.println("+++++++++++++++++++++++++++++++++++++++++++++");
		 System.out.println("Bat dau tim kiem theo Y +++++++++++++++++++++");
		 Point beginTheoY = luuViTriTimKiem.get(999);
		 Point endTheoY = luuViTriTimKiem.get(1000);
		 soLuongGeneTheoY[0] = timKiemTheoTrucY(beginTheoY,endTheoY);
		 //*******************//
		 timKiemChoBatDauDiVao(2000, 3000, 1000);
		 //*******************//
		 beginTheoY = luuViTriTimKiem.get(1999);
		 endTheoY = luuViTriTimKiem.get(2000);
		 soLuongGeneTheoY[1] = timKiemTheoTrucY(beginTheoY,endTheoY);
		 //*******************//
		 timKiemChoBatDauDiVao(3000, 4000, 1000);
		//*******************//
		 beginTheoY = luuViTriTimKiem.get(2999);
		 endTheoY = luuViTriTimKiem.get(3000);
		 soLuongGeneTheoY[2] = timKiemTheoTrucY(beginTheoY,endTheoY);
		 //*******************//
		 timKiemChoBatDauDiVao(4000, 5000, 1000);
		//*******************//
		 beginTheoY = luuViTriTimKiem.get(3999);
		 endTheoY = luuViTriTimKiem.get(4000);
		 soLuongGeneTheoY[3] = timKiemTheoTrucY(beginTheoY,endTheoY);
		 //*******************//
		 ketQua = new ArrayList<>();
//		 for(int i = 0; i< luuViTriTimKiem.size(); i++){
//			 ketQua.add(luuViTriTimKiem.get(i));
//		 }
		 int i= 0;
		 int k=0;
		 //Luu ket qua
		 for( ; i< 1000; i++){
			 ketQua.add(luuViTriTimKiem.get(i));
		 }
		 for( int j = 0; j< soLuongGeneTheoY[0]; j++){
			 ketQua.add(luuViTriTimKiemTheoY.get(k));
			 k++;
		 }
		 for( ; i< 2000; i++){
			 ketQua.add(luuViTriTimKiem.get(i));
		 }
		 for( int j = 0; j< soLuongGeneTheoY[1]; j++){
			 ketQua.add(luuViTriTimKiemTheoY.get(k));
			 k++;
		 }
		 for( ; i< 3000; i++){
			 ketQua.add(luuViTriTimKiem.get(i));
		 }
		 for( int j = 0; j< soLuongGeneTheoY[2]; j++){
			 ketQua.add(luuViTriTimKiemTheoY.get(k));
			 k++;
		 }
		 for( ; i< 4000; i++){
			 ketQua.add(luuViTriTimKiem.get(i));
		 }
		 for( int j = 0; j< soLuongGeneTheoY[3]; j++){
			 ketQua.add(luuViTriTimKiemTheoY.get(k));
			 k++;
		 }
		 for(; i< luuViTriTimKiem.size(); i++){
			 ketQua.add(luuViTriTimKiem.get(i));
		 }
		 luuViTriTimKiem.clear();
		 luuViTriTimKiemTheoY.clear();
		 this.minExp = objectiveMin;
		return this.ketQua;
	}

	public static void main(String[] args) {
		PSOSearchWithDivision g = new PSOSearchWithDivision();
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
}
