package excelParse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class ExcelParse2 {
    /**
     * 解析PG350参比数据
     * @param deviceId 设备编号
     * @param stream excel文件字节流
     * @return json字符串集合，数据json格式要求{"id":"12321","data_time":"2021/09/02 15:30:00",
     *          "data_value":{"SO2:"12.22",”SO2-Unit“:”ppm“，"NO2:"15.22","NO2-Unit":"ppm"}，"storage_time":""}
     *  注：去除数据种的标注位
     */

    public List<String> decodeReferenceData(String deviceId, FileInputStream stream)throws IOException {
        List<String> resList = new ArrayList<>();

        //excel表的处理
        HSSFWorkbook workbook = new HSSFWorkbook(stream);
        HSSFSheet sheet = workbook.getSheetAt(0);
        HSSFRow row;
        HSSFCell cell;
        HSSFRow row5=sheet.getRow(5);//第五行，覆盖全部列数据
        HSSFRow row2=sheet.getRow(2);//第三行 NO信息行

        int rows = sheet.getLastRowNum();//表总行数
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<String> factorsList=new ArrayList<>();

        //将所有的待查气体放入List中
        int cols = row5.getPhysicalNumberOfCells();//列数
        for(int i=1;i<cols;i++){//以"Conc."为标定点确定要查的气体名称
            if(sheet.getRow(3).getCell(i).toString().equals("Conc.")){
                String fac=sheet.getRow(2).getCell(i-1).toString();
                factorsList.add(fac.trim());//去除前后的空格后添加
            }
        }

        //两层循环
        for(int icount=4;icount<=rows;icount++){
            row=sheet.getRow(icount);//获取当行数据

            JSONObject resJson = new JSONObject();
            resJson.put("id",deviceId);//1.放入devicedId

            Date dateCellValue = row.getCell(0).getDateCellValue();
            resJson.put("data_time", df.format(dateCellValue).toString());

            JSONObject json=new JSONObject();
            for(int j=2;j<cols;j++){

                cell = row.getCell(j);//单元格具体的值
                //这里存在bug 当在列CA4时，会有覆盖。另起函数解析。
                //int index = cell.getAddress().toString().substring(0, 1).charAt(0)-65;
                int index=parse(cell.getAddress().toString());

                String facKey="";
                if( row2.getCell(index-1)!=null){
                    facKey=row2.getCell(index-1).toString().trim();//空指针异常
                }
                //判断有值的才加入
                if(factorsList.contains(facKey)&&
                        !cell.toString().substring(0,3).equals("---")
                ){
                    json.put(facKey,cell.toString());
                    String s=row2.getCell(index).toString().trim();//获取单位名称 [ppm]=>ppm [vol%]=>%
                    String unitVal=s.substring(1,s.length()-1);
                    if(unitVal.substring(unitVal.length()-1).equals("%")){
                        json.put(facKey+"-Unit","%");
                    }else{
                        json.put(facKey+"-Unit",unitVal);
                    }
                }
            }
            resJson.put("data_value",json);

            Date date = new Date();
            resJson.put("storage_time",df.format(date));

            //一行json数据转string后放入resList中
            resList.add(JSON.toJSONString(resJson));
        }
        return resList;
    }
    public static int parse(String s){
        int len=s.length();
        int index=0;
        for (int i = 0; i < len; i++) {
            if(Character.isDigit(s.charAt(i))){
                index=i;
                break;
            }
        }
        String str=s.substring(0,index);
        //System.out.println(str);
        int m=0;
        if(str.length()==1){
            m=str.charAt(0)-65;
        }else{
            m=(str.charAt(0)-64)*26-1+str.charAt(1)-64;
        }
        return m;
    }


}
