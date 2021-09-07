package excelParse;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
//        public static void main(String[] args) throws FileNotFoundException {
//            ExcelParse ec = new ExcelParse();
//
//            //因子编码，例如{SO2=["SO2标态干基值","二氧化硫"]}，根据该map对应关系将excel表头转换为对应因子编码，作为json数据的key
//            Map<String, List<String>> factorMap=new HashMap<>();
//            List<String> l=new ArrayList<>();
//            l.add("SO2标态干基值(mg/m3)");
//            l.add("NO标态干基值(mg/m3)");
//            factorMap.put("SO2",l);
//
//            String filename = "./Test/src/main/java/excelParse/(Min_1)2021_09_02 15_36_14.xls";
//            FileInputStream stream=new FileInputStream(filename);
//            try {
//                //函数调用
//                List<String> deviceA = ec.decodeExcel("deviceA", factorMap, stream);
//                for (int i = 0; i < deviceA.size(); i++) {
//                    System.out.println(deviceA.get(i));
//                }
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }

    public static void main(String[] args) throws FileNotFoundException {
        ExcelParse2 ec = new ExcelParse2();

        String filename = "./Test/src/main/java/excelParse/0906.xls";
        FileInputStream stream=new FileInputStream(filename);
        try {
            //函数调用
            List<String> deviceA = ec.decodeReferenceData("deviceA", stream);
            for (int i = 0; i < deviceA.size(); i++) {
                System.out.println(deviceA.get(i));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
