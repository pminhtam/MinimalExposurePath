package data;

import java.io.File;

public class Data {
	public static boolean xuyGiam  = false;
    public static int[] dataNum = new int[] { 30, 40, 50, 60, 70, 80, 90, 100 };  // số lượng sensor
    public static int[] dataArg = new int[] { 45}; // góc.
    private static String dataPath = "data/DataBin/";
    private static String resultPath = "data/ResultBin/";
    private static String dataxgPath = "data/DataXG/";
    private static String resultxgPath = "data/ResultXG/";

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
