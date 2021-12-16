//20200205 Kyle add
package Sale.Sale05R090;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import com.jacob.com.Dispatch;
import Farglory.util.FargloryUtil;
import Farglory.util.KUtils;
import Farglory.util.MLPUtils;
import jcx.db.talk;
import jcx.jform.bproc;

public class AMLRpt2 extends bproc {
  talk dbSale = getTalk("Sale");
  FargloryUtil exeUtil = new FargloryUtil();
  KUtils kUtils = new KUtils();
  MLPUtils mlpUtils = new MLPUtils();
  Sale05R090_AML_Utils data = new Sale05R090_AML_Utils();

  public String getDefaultValue(String value) throws Throwable {
    String arrTableData090[][] = getTableData("table090");
    if (arrTableData090.length == 0) {
      message("�Х�����d�� !");
      return value;
    } else {
      message("��Ʋզ����еy��...");
      doExcel(arrTableData090);
    }
    return value;
  }

  public void doExcel(String[][] arrTableData090) throws Throwable {
    List mainList = processMainList(arrTableData090);
    System.out.println("mainList size>>>" + mainList.size());

    // �إߪ��
    int startDataRow = 5;
    int endDataRow = mainList.size() + 5;
    Farglory.Excel.FargloryExcel exeFun = new Farglory.Excel.FargloryExcel(startDataRow, endDataRow, endDataRow, 1);

    // �Ysample�ɸ��|
//    String stringPrintExcel = "G:\\kyleTest\\Excel\\Sale05R090_AML_Rpt2.xls";
    String stringPrintExcel = "G:\\��T��\\Excel\\Sale\\Sale05R090\\AMLRpt2.xlt";

    // �إ�Excel����
    Vector retVector = exeFun.getExcelObject(stringPrintExcel);
    Dispatch objectSheet = (Dispatch) retVector.get(1);

    // Start of Body ��ƥ���
    for (int intRow = 0; intRow < mainList.size(); intRow++) {
      String[] thisRow = (String[]) mainList.get(intRow);
      int recordNo = intRow + exeFun.getStartDataRow();

      for (int intCon = 0; intCon < 42; intCon++) {
        exeFun.putDataIntoExcel(intCon, recordNo, thisRow[intCon], objectSheet);
      }
    }
    // End of Body

    exeFun.getReleaseExcelObject(retVector);
    message("��X������!!");

    return;
  }

  /**
   * �զ��D�n�C�� List 53�����o���h�A�i�o�g
   */
  public List processMainList(String[][] arrTableData090) throws Throwable {
    List mainList = new ArrayList();

    // start ���o�d�߫�orderNo���X
    Set setOrderNos = new HashSet();
    Map map090 = new HashMap();
    for (int i = 0; i < arrTableData090.length; i++) {
      String thisOrderNo = arrTableData090[i][0].trim();
      setOrderNos.add(thisOrderNo);
    }

    // orderNos �নsql�y�k
    int setIndex = 0;
    StringBuffer sbQorderNos = new StringBuffer();
    Iterator it = setOrderNos.iterator();
    while (it.hasNext()) {
      if (setIndex > 0) sbQorderNos.append(",");
      sbQorderNos.append("'").append(it.next()).append("'");
      setIndex++;
    }
    // end

    // �X���ɼӧO
    Map map278 = data.getSale05M278(sbQorderNos.toString());
    // System.out.println(">>>map278>>>" + map278);

    // �Ȥ�
    Map map091 = data.getSale05M091(sbQorderNos.toString());
    // System.out.println(">>>map091>>>" + map091);

    // ú���`�p
    Map mapCash = data.getSale05M080(sbQorderNos.toString());
    System.out.println(">>>mapCash>>>" + mapCash);

    // �~�����
    Map mapAML = data.getSale05M070(sbQorderNos.toString());
    System.out.println(">>>mapAML>>>" + mapAML);

    String lastOrderNo = "";
    for (int idx = 0; idx < arrTableData090.length; idx++) {
      String[] newRow = new String[42];

      // �ثe�B�z�q�渹
      String thisOrderNo = arrTableData090[idx][0].trim();
      System.out.println(">>>thisOrderNo>>>" + thisOrderNo);

      // �X���ɼӧO
      Map this278 = (Map) map278.get(thisOrderNo);

      // �ʪ��q��D��
      String[] this090 = arrTableData090[idx];
      // System.out.println(">>>this090>>>" + this090);

      // �ʫέq��Ȥ�
      String[] this091 = (String[]) map091.get(thisOrderNo);
      // System.out.println(">>>this091>>>" + this091);

      // ���ڸ��
      Map thisCash = (Map) mapCash.get(thisOrderNo);
      System.out.println(">>>thisCash>>>" + thisCash);

      // �~��
      Map thisAML = (Map) mapAML.get(thisOrderNo);
      System.out.println(">>>thisAML>>>" + thisAML);

      // come on ~
      newRow[0] = Integer.toString(idx + 1); // NO
      newRow[3] = "".equals(this090[3]) ? "" : this090[3].trim().replaceAll(",", "\n"); // �ɼӧO
      newRow[4] = "".equals(this090[4]) ? "" : this090[4].trim().replaceAll(",", "\n"); // ����
      newRow[9] = this090[19].trim(); // �W��
      newRow[10] = this090[20].trim(); // ���

      // �P���q��Ĥ@��
      if (!thisOrderNo.equals(lastOrderNo)) {
        newRow[1] = thisOrderNo; // �q��s��
        newRow[2] = this090[2].trim(); // �קO
        newRow[5] = this091[1]; // �m�W
        newRow[6] = this090[5].trim(); // �q���
        newRow[7] = this278 == null ? "-" : this278.get("ContractDate").toString(); // ñ����
        newRow[8] = "-"; // �h���
        if ("D".equals(this090[18].trim())) {
          newRow[8] = this090[17].trim();
        }
        newRow[11] = this090[7].trim(); // ��X�H1
        newRow[12] = this090[8].trim(); // ��X�H2
        newRow[13] = this090[9].trim(); // ��X�H3
        newRow[14] = this090[10].trim(); // ��X�H4
        newRow[15] = this090[11].trim(); // ��X�H5
        newRow[16] = this090[12].trim(); // ��X�H6
        newRow[17] = this090[13].trim(); // ��X�H7
        newRow[18] = this090[14].trim(); // ��X�H8
        newRow[19] = this090[15].trim(); // ��X�H9
        newRow[20] = this090[16].trim(); // ��X�H10
        newRow[21] = "0";
        newRow[22] = "0";
        newRow[23] = "0";
        newRow[24] = "-";
        newRow[25] = "0";
        newRow[26] = "0";
        newRow[27] = "0";
        // ��sú��
        if (thisCash != null) {
          newRow[21] = thisCash.get("cashMoney").toString(); // �{��
          newRow[22] = thisCash.get("creditCardMoney").toString(); // �H�Υd���B
          newRow[23] = thisCash.get("bankMoney").toString(); // �Ȧ�״�
          newRow[24] = thisCash.get("bankExportingPlace").toString(); // �Ȧ�ץX�a
          newRow[25] = thisCash.get("billMoney").toString(); // ���ڪ��B
          newRow[26] = Double.toString(Double.parseDouble(thisCash.get("cashMoney").toString()) + Double.parseDouble(thisCash.get("creditCardMoney").toString())
              + Double.parseDouble(thisCash.get("bankMoney").toString()) + Double.parseDouble(thisCash.get("billMoney").toString())); // �p�O (EXCEL SUM)
          newRow[27] = thisCash.get("docCount").toString(); // �֭p�`���ڵ���
        }

        if (thisAML != null) { // �~�����
          newRow[28] = thisAML.get("C021").toString();
          newRow[29] = thisAML.get("C018").toString();
          newRow[30] = thisAML.get("C017").toString();
          newRow[31] = thisAML.get("C009").toString();
          newRow[32] = thisAML.get("C011").toString();
          newRow[33] = thisAML.get("C008").toString();
          newRow[34] = thisAML.get("C004").toString();
          newRow[35] = thisAML.get("C002").toString();
          newRow[36] = thisAML.get("C013").toString();
          newRow[37] = thisAML.get("C010").toString();
          newRow[38] = thisAML.get("C012").toString();
          newRow[39] = thisAML.get("C006").toString();
          newRow[40] = thisAML.get("C015").toString();
          newRow[41] = thisAML.get("C016").toString();
        }

      }
      mainList.add(newRow);
      lastOrderNo = thisOrderNo;
    }

    return mainList;
  }

  public String getInformation() {
    return "---------------button2(�C�L).defaultValue()----------------";
  }
}