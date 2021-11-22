package Doc.Doc3.Doc3R012;

import java.util.Vector;

import com.jacob.com.Dispatch;

import jcx.jform.bproc;
import jcx.util.datetime;

public class ExportExcel extends bproc {
  public String getDefaultValue(String value) throws Throwable {
    Farglory.Excel.FargloryExcel exeExcel = new Farglory.Excel.FargloryExcel();
    Farglory.util.FargloryUtil exeUtil = new Farglory.util.FargloryUtil();
    Doc.Doc2M010 exeFun = new Doc.Doc2M010();
    //
    exeExcel.setVisibleProperty(false); // 控制顯不顯示 Excel
    //
//    Vector retVector = exeExcel.getExcelObject("http://emaker.farglory.com.tw:8080/servlet/baServer3?step=6?filename=C:/emaker/batch/EXCEL/Template/Doc/預算使用狀態.xlt");
    String stringFilePath = "G:/資訊室/Excel/Doc/Doc3/預算使用狀態.xlt";
    Vector retVector = exeExcel.getExcelObject(stringFilePath);
    Dispatch objectSheet1 = (Dispatch) retVector.get(1);
    Dispatch objectClick = null;
    // 表頭
    exeExcel.putDataIntoExcel(0, 0, getValue("ProjectID1").trim() + "預算使用狀態", objectSheet1);
    // 日期
    exeExcel.putDataIntoExcel(3, 1, "日期：" + datetime.getToday("YYYY/mm/dd"), objectSheet1);
    //
    double[] arraySum = { 0, 0, 0, 0, 0 };
    String stringValue = "";
    String[][] retTableData = getTableData("Table1");
    for (int intNoY = 0; intNoY < retTableData.length; intNoY++) {
      for (int intNoX = 0; intNoX < retTableData[0].length; intNoX++) {
        stringValue = retTableData[intNoY][intNoX].trim();
        arraySum[intNoX] += exeUtil.doParseDouble(stringValue);
        if (intNoX == 0) stringValue = stringValue + " " + exeFun.getBudgetName(stringValue);
        if (intNoX == 2 || intNoX == 4) {
          stringValue = "" + (exeUtil.doParseDouble(stringValue) / 100);
          // stringValue = convert.FourToFive(stringValue, 4) ;
        }
        exeExcel.putDataIntoExcel(intNoX, intNoY + 3, stringValue, objectSheet1);
      }
      exeExcel.doLineStyle("A" + (intNoY + 4) + ":E" + (intNoY + 4), objectSheet1);
    }
    // 合計
    int intPos = retTableData.length + 3;
    for (int intNoX = 0; intNoX < arraySum.length; intNoX++) {
      stringValue = "" + arraySum[intNoX];
      if (intNoX == 0) {
        stringValue = "合計";
      } else if (intNoX == 2 || intNoX == 4) {
        stringValue = "" + (exeUtil.doParseDouble(stringValue) / 100);
        // stringValue = convert.FourToFive(stringValue, 4) ;
      }
      System.out.println(intNoX + "------------------" + stringValue);
      exeExcel.putDataIntoExcel(intNoX, intPos, stringValue, objectSheet1);
    }
    exeExcel.doLineStyle("A" + (intPos + 1) + ":E" + (intPos + 1), objectSheet1);
    // 釋放 Excel 物件
    exeExcel.setVisiblePropertyOnFlow(true, retVector); // 控制顯不顯示 Excel
    exeExcel.getReleaseExcelObject(retVector);
    return value;
  }
}
