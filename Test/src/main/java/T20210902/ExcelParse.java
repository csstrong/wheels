package T20210902;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class ExcelParse {
    /**
     * 解析分钟数据
     *
     * @param devicedId  设备编号
     * @param factorMap 因子编码，例如{SO2=["SO2标态干基值","二氧化硫"]}，根据该map对应关系将excel表头转换为对应因子编码，作为json数据的key
     * @param stream    excel文件字节流
     * @return json字符串集合，数据json格式要求{"id":"123213","data_time":"2021/09/02 15:32:00",
     *                                          "data_value":{"SO2:"12.22","NO2:"15.22"""}"storage_time":""}
     */
    //Mavne:导入poi和fastjson依赖
    public List<String> decodeExcel(String devicedId, Map<String, List<String>> factorMap, FileInputStream stream) throws IOException {

        List<String> resList = new ArrayList<>();
        //factorMap中List的解析处理，放入factorMap中的表列名
        List<String> list = new ArrayList<>();
        //获取表列名称 遍历map
        for (Map.Entry<String, List<String>> entry : factorMap.entrySet()) {
            list= entry.getValue();
        }
        //对列名进行映射
        Map<String,String> mapFlag=new HashMap<>();
        mapFlag.put("SO2标态干基值(mg/m3)","SO2");
        mapFlag.put("NO标态干基值(mg/m3)","NO");

        //excel表的处理
        HSSFWorkbook workbook = new HSSFWorkbook(stream);
        HSSFSheet sheet = workbook.getSheetAt(0);
        HSSFRow row;
        HSSFCell cell;
        HSSFRow row0=sheet.getRow(0);//第一行，全部列名称
        int rows = sheet.getLastRowNum();//表总行数

        JSONObject resJson = new JSONObject();//组织一行的json
        for (int icount = 1; icount < rows; icount++) {
            row = sheet.getRow(icount);
            int line = row.getPhysicalNumberOfCells();

            resJson = new JSONObject();
            resJson.put("id",devicedId);//1.放入devicedId
            resJson.put("data_time",row.getCell(0).toString());//2.放入data_time

            JSONObject json=new JSONObject();
            for (int j = 1; j < line; j++) {
                cell = row.getCell(j);//单元格具体的值
                int index = cell.getAddress().toString().substring(0, 1).charAt(0)-65;//根据单元格的位置(A2)中的A对应到列位置index
                String cName=row0.getCell(index).toString();//获取列名称
                if(list.contains(cName)){//当前单元格的列方向表头数据在list中时组建json数据
                    //3-1.放入具体数据 key:(根据原始列名映射获取) value:（对单元格进行截断取数据）
                    int len=cell.toString().length();//(Sd) 4个字符
                    if(len>4){
                        //如果有数据并且含有数据单位
                        json.put(mapFlag.get(cName),cell.toString().substring(0,len-4));
                    }else{
                        //空白单元格
                        json.put(mapFlag.get(cName),"0.00");
                    }

                }
            }
            resJson.put("data_value",json);//3-2.放入data_value

            //4.放入storage_time，需格式化
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();
            resJson.put("storage_time",df.format(date));

            //一行json数据转string后放入resList中
            resList.add(JSON.toJSONString(resJson));
        }
        return resList;
    }

    public static void main(String[] args) throws FileNotFoundException {
        ExcelParse ec = new ExcelParse();

        //因子编码，例如{SO2=["SO2标态干基值","二氧化硫"]}，根据该map对应关系将excel表头转换为对应因子编码，作为json数据的key
        Map<String, List<String>> factorMap=new HashMap<>();
        List<String> l=new ArrayList<>();
        l.add("SO2标态干基值(mg/m3)");
        l.add("NO标态干基值(mg/m3)");
        factorMap.put("SO2",l);

        String filename = "C:\\Users\\lenovo\\Documents\\WeChat Files\\wxid_5fbqy8jyrll222\\FileStorage\\File\\2021-09\\(Min_1)2021_09_02 15_36_14.xls";
        FileInputStream stream=new FileInputStream(filename);
        try {
            //函数调用
            List<String> deviceA = ec.decodeExcel("deviceA", factorMap, stream);
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

