package data;

import java.awt.List;
import java.io.File;
import java.util.ArrayList;

public class Data {
	public static boolean xuyGiam  = false;
  public static int[] dataNum500 = new int[] { 30, 40, 50, 60, 70, 80, 90, 100 };  // số lượng sensor
  public static int[] dataNum10 = new int[] { 3,5,10 };  // số lượng sensor
  public static int[] dataNum100 = new int[] { 5,10,15,20,150 };  
//  ArrayList<Integer[]> dataChung = new ArrayList<>(); 
  
 
  
  public static int[] areaSize = new int[] {10,100,500};
  
    public static int[] dataArg = new int[] { 45}; // góc.
    private static String dataPath = "data/DataBin/";
//  private static String dataPath = "data/data10/datae/";
//  private static String dataPath = "data/";

    private static String resultPath = "data/ResultBin/";
//private static String resultPath = "result/";

//    private static String dataxgPath = "data/DataXG/";
//    private static String resultxgPath = "data/ResultXG/";

    public static String getDataPath(String name) {
        String path = dataPath;
        return path + name;
    }

    public static String getResultPath(String name) {
        String path =  resultPath;
        return path + name;
    }
    
    public static boolean isResultExists(String name) {
        String path = resultPath;
        return new File(path + name).exists();
    }
}
