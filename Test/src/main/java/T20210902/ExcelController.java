package T20210902;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class ExcelController {
    @SuppressWarnings("deprecation")
    public void excel() throws FileNotFoundException, IOException {
        String filename = "C:\\Users\\lenovo\\Documents\\WeChat Files\\wxid_5fbqy8jyrll222\\FileStorage\\File\\2021-09\\3.xls";
        HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(filename));
//按名引用excel工作表
// HSSFSheet sheet = workbook.getSheet("JSP");
//也可以用以下方式来获取excel的工作表，采用工作表的索引值
        HSSFSheet sheet = workbook.getSheetAt(0);
        HSSFRow row;
        HSSFCell cell1;
        int rows = sheet.getLastRowNum();
        for (int icount = 1; icount < rows-5; icount++) {
            row = sheet.getRow(icount);
            int line = row.getPhysicalNumberOfCells();
            for (int j = 0; j < line; j++) {
                cell1 = row.getCell(j);
                //System.out.print(cell1 + "--" + icount + "---" + j);

                System.out.print(cell1 + "--"+cell1.getAddress().toString().substring(0,1)+"**");
            }
            System.out.println();
        }
//打印读取值
// System.out.println(cell.getStringCellValue());
//新建一输出流
        FileOutputStream fout = new FileOutputStream(filename); //PS：filename 是你另存为的路径，不处理直接写入模版文件
//存盘
        workbook.write(fout);
        fout.flush();
//结束关闭
        fout.close();
    }

    public HSSFCell getCell(HSSFRow row, int index) {
// 取得分发日期单元格
        HSSFCell cell = row.getCell(index);
// 如果单元格不存在
        if (cell == null) {
// 创建单元格
            cell = row.createCell(index);
        }
// 返回单元格
        return cell;
    }
    public static void main(String[] args) {
        ExcelController ec = new ExcelController();
        try {
            ec.excel();
        } catch (FileNotFoundException e) {
// TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
// TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
