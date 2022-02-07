package Sale.Report;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import org.apache.commons.lang.StringUtils;
import com.jacob.com.Dispatch;
import Farglory.util.FargloryUtil;
import jcx.jform.bproc;

public class RSTableToExcel extends bproc {
  FargloryUtil exeUtil = new FargloryUtil();
  List mainList = new ArrayList();
  String tableName = "";

  public String getDefaultValue(String value) throws Throwable {

    // 檢核
    if (!isBatchCheckOK()) return value;

    // 準備資料
    this.processData();

    if (mainList.size() == 0) {
      message("無資料 !");
      return value;
    }

    doExcel();

    return value;
  }

  public boolean isBatchCheckOK() throws Throwable {
    // 無需要檢核項目
    return true;
  }

  public void processData() throws Throwable {
    // 準備資料
    JTable tb1 = this.getTable("ResultTable");
    tableName = tb1.getName();
    DefaultTableModel dtm = (DefaultTableModel) tb1.getModel();
    Vector v = new Vector();
    int columnNum = 0;
    while (true) {
      String columnName = dtm.getColumnName(columnNum);
      if (StringUtils.isBlank(columnName)) break;
      v.add(columnName);
      columnNum++;
    }
    String[] tableTitle = (String[]) v.toArray(new String[v.size()]);
    mainList.add(tableTitle);

    String[][] tableRet = this.getTableData("ResultTable");
    for (int i = 0; i < tableRet.length; i++) {
      mainList.add(tableRet[i]);
    }
  }

  public void doExcel() throws Throwable {
    System.out.println("mainList size>>>" + mainList.size());

    // 建立表格 開始列數 結束列數 一頁列數 初始頁數
    Farglory.Excel.FargloryExcel exeFun = new Farglory.Excel.FargloryExcel(1, mainList.size() + 1, mainList.size(), 1);

    // sample檔路徑
    String stringPrintExcel = "G:\\資訊室\\Excel\\public\\PaperExcel.xlt";
    if(StringUtils.contains(this.getFunctionName(), "財務")) {
      stringPrintExcel = "N:\\公用資料區\\財務室\\EXCELTEMP\\PaperExcel.xlt";
    }

    // 建立Excel物件
    Vector retVector = exeFun.getExcelObject(stringPrintExcel);
    Dispatch objectSheet1 = (Dispatch) retVector.get(1);

    // 表頭
    exeFun.putDataIntoExcel(0, 0, tableName, objectSheet1);

    // Start of Body 資料本體
    for (int intRow = 0; intRow < mainList.size(); intRow++) {
      String[] thisRow = (String[]) mainList.get(intRow);
      int recordNo = intRow + exeFun.getStartDataRow();

      for (int intCon = 0; intCon < thisRow.length; intCon++) {
        exeFun.putDataIntoExcel(intCon, recordNo + 1, thisRow[intCon], objectSheet1);
      }
    }
    // End of Body

    exeFun.getReleaseExcelObject(retVector);
    return;
  }

  public String getInformation() {
    return "---------------button2(列印).defaultValue()----------------";
  }
}