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

    // �ˮ�
    if (!isBatchCheckOK()) return value;

    // �ǳƸ��
    this.processData();

    if (mainList.size() == 0) {
      message("�L��� !");
      return value;
    }

    doExcel();

    return value;
  }

  public boolean isBatchCheckOK() throws Throwable {
    // �L�ݭn�ˮֶ���
    return true;
  }

  public void processData() throws Throwable {
    // �ǳƸ��
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

    // �إߪ�� �}�l�C�� �����C�� �@���C�� ��l����
    Farglory.Excel.FargloryExcel exeFun = new Farglory.Excel.FargloryExcel(1, mainList.size() + 1, mainList.size(), 1);

    // sample�ɸ��|
    String stringPrintExcel = "G:\\��T��\\Excel\\public\\PaperExcel.xlt";
    if(StringUtils.contains(this.getFunctionName(), "�]��")) {
      stringPrintExcel = "N:\\���θ�ư�\\�]�ȫ�\\EXCELTEMP\\PaperExcel.xlt";
    }

    // �إ�Excel����
    Vector retVector = exeFun.getExcelObject(stringPrintExcel);
    Dispatch objectSheet1 = (Dispatch) retVector.get(1);

    // ���Y
    exeFun.putDataIntoExcel(0, 0, tableName, objectSheet1);

    // Start of Body ��ƥ���
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
    return "---------------button2(�C�L).defaultValue()----------------";
  }
}