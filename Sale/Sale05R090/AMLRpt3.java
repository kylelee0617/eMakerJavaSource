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

public class AMLRpt3 extends bproc {
  talk dbSale = getTalk("Sale");
  FargloryUtil exeUtil = new FargloryUtil();
  KUtils kUtils = new KUtils();
  MLPUtils mlpUtils = new MLPUtils();
  Sale05R090_AML_Utils data = new Sale05R090_AML_Utils();

  public String getDefaultValue(String value) throws Throwable {
    String arrTableData090[][] = getTableData("table090");
    if (arrTableData090.length == 0) {
      message("請先執行查詢 !");
      return value;
    } else {
      message("資料組成中請稍後...");
      doExcel(arrTableData090);
    }
    return value;
  }

  public void doExcel(String[][] arrTableData090) throws Throwable {
    List mainList = processMainList(arrTableData090);
    System.out.println("mainList size>>>" + mainList.size());

    // 建立表格
    int startDataRow = 5;
    int endDataRow = mainList.size() + 5;
    Farglory.Excel.FargloryExcel exeFun = new Farglory.Excel.FargloryExcel(startDataRow, endDataRow, endDataRow, 1);

    // 吃sample檔路徑
    String stringPrintExcel = "G:\\資訊室\\Excel\\Sale\\Sale05R090\\AMLRpt3.xlt";

    // 建立Excel物件
    Vector retVector = exeFun.getExcelObject(stringPrintExcel);
    Dispatch objectSheet = (Dispatch) retVector.get(1);

    // Start of Body 資料本體
    for (int intRow = 0; intRow < mainList.size(); intRow++) {
      String[] thisRow = (String[]) mainList.get(intRow);
      int recordNo = intRow + exeFun.getStartDataRow();

      for (int intCon = 0; intCon < 54; intCon++) {
        exeFun.putDataIntoExcel(intCon, recordNo, thisRow[intCon], objectSheet);
      }
    }
    // End of Body

    exeFun.getReleaseExcelObject(retVector);
    message("輸出報表完成!!");

    return;
  }

  /**
   * 組成主要列表 List 53個欄位她媽多，懶得寫
   */
  public List processMainList(String[][] arrTableData090) throws Throwable {
    List mainList = new ArrayList();

    // start 取得查詢後orderNo集合
    Set setOrderNos = new HashSet();
    Map map090 = new HashMap();
    for (int i = 0; i < arrTableData090.length; i++) {
      String thisOrderNo = arrTableData090[i][0].trim();
      setOrderNos.add(thisOrderNo);
    }

    // orderNos 轉成sql語法
    int setIndex = 0;
    StringBuffer sbQorderNos = new StringBuffer();
    Iterator it = setOrderNos.iterator();
    while (it.hasNext()) {
      if (setIndex > 0) sbQorderNos.append(",");
      sbQorderNos.append("'").append(it.next()).append("'");
      setIndex++;
    }
    // end

    // 合約棟樓別
    Map map278 = data.getSale05M278(sbQorderNos.toString());
    // System.out.println(">>>map278>>>" + map278);

    // 客戶
    Map map091 = data.getSale05M091(sbQorderNos.toString());
    // System.out.println(">>>map091>>>" + map091);

    // 洗防欄位
    Map mapAML = data.getSale05M070(sbQorderNos.toString());
    // System.out.println(">>>mapAML>>>" + mapAML);

    // 收款單
    Map mapDoc = data.getSale05M080_Rpt3(sbQorderNos.toString());
    // System.out.println(">>>mapDoc>>>" + mapDoc);

    // 代繳人
    Map mapDeputy = data.getDeputy(sbQorderNos.toString());
    // System.out.println(">>>mapDeputy>>>" + mapDeputy);

    // 代理人
    Map mapAgent = data.getAgent2_Rpt3(sbQorderNos.toString());
    // System.out.println(">>>mapAgent>>>" + mapAgent);

    // 第三人
    Map map3rd = data.getSale05M356_Rpt3(sbQorderNos.toString());
    // System.out.println(">>>map3rd>>>" + map3rd);

    // 洗錢樣態
    Map mapML = data.getSale05M070_Rpt3(sbQorderNos.toString());
    // System.out.println(">>>mapML>>>" + mapML);

    // 收款期別
    Map mapOrder_no = data.getPositionOrder_No(sbQorderNos.toString());
    // System.out.println(">>>mapOrder_no>>>" + mapOrder_no);

    // 匯出地
    Map mapExtPlace = data.getExportingPlaces(sbQorderNos.toString());
    // System.out.println(">>>mapExtPlace>>>" + mapExtPlace);

    String lastOrderNo = "";
    int lv2DocCount = 0; // 收款單計數器
    for (int idx = 0; idx < arrTableData090.length; idx++) {
      String[] newRow = new String[54];

      // 目前處理訂單號
      String thisOrderNo = arrTableData090[idx][0].trim();
      // System.out.println(">>>thisOrderNo>>>" + thisOrderNo);

      // 合約棟樓別
      Map this278 = (Map) map278.get(thisOrderNo);

      // 購物訂單主檔
      String[] this090 = arrTableData090[idx];
      // System.out.println(">>>this090>>>" + this090);

      // 購屋訂單客戶
      String[] this091 = (String[]) map091.get(thisOrderNo);
      // System.out.println(">>>this091>>>" + this091);

      // 收款資料
      List thisListDoc = (List) mapDoc.get(thisOrderNo);
      // System.out.println(">>>thisListDoc>>>" + thisListDoc);

      Map thisAgent = (Map) mapAgent.get(thisOrderNo);
      // System.out.println(">>>thisAgent>>>" + thisAgent);

      Map this3rd = (Map) map3rd.get(thisOrderNo);
      // System.out.println(">>>this3rd>>>" + this3rd);

      // 同筆訂單第一次
      if (!thisOrderNo.equals(lastOrderNo)) {
        lv2DocCount = 0;
      }

      // come on ~
      newRow[0] = Integer.toString(idx + 1); // NO
      newRow[1] = thisOrderNo; // 訂單編號
      newRow[2] = this090[2].trim(); // 案別
      newRow[3] = "".equals(this090[3]) ? "" : this090[3].trim().replaceAll(",", "\n"); // 棟樓別
      newRow[4] = "".equals(this090[4]) ? "" : this090[4].trim().replaceAll(",", "\n");
      newRow[5] = this091[1].trim(); // 客戶姓名
      newRow[6] = this091[2]; // 客戶ID
      newRow[7] = this090[5]; // 付訂日期
      newRow[8] = this278 == null ? "-" : this278.get("ContractDate").toString(); // 簽約日
      newRow[9] = "-"; // 退戶日
      if ("D".equals(this090[18].trim())) {
        newRow[9] = this090[17].trim();
      }
      if (thisAgent != null) {
        newRow[29] = thisAgent.get("CustomName").toString();
        newRow[30] = thisAgent.get("CustomNo").toString();
        newRow[31] = thisAgent.get("Relation").toString();
        newRow[32] = thisAgent.get("IsControlList").toString();
        newRow[33] = thisAgent.get("IsBlackList").toString();
        newRow[34] = thisAgent.get("IsLinked").toString();
        newRow[35] = thisAgent.get("type1").toString();
        newRow[36] = thisAgent.get("type2").toString();
        newRow[37] = thisAgent.get("Reason").toString();
      }
      if (this3rd != null) {
        newRow[38] = this3rd.get("CustomName").toString();
        newRow[39] = this3rd.get("CustomNo").toString();
        newRow[40] = this3rd.get("Relation").toString();
        newRow[41] = this3rd.get("ExportingPlace").toString();
        newRow[42] = this3rd.get("IsControlList").toString();
        newRow[43] = this3rd.get("IsBlackList").toString();
        newRow[44] = this3rd.get("IsLinked").toString();
        newRow[45] = this3rd.get("Reason").toString();
      }

      // 洗錢樣態(先抓抓看)
      String thisML1 = (String) mapML.get(thisOrderNo);
      if (thisML1 != null) newRow[11] = thisML1;

      // 收款單
      if (thisListDoc != null && thisListDoc.size() > lv2DocCount) {
        Map thisDoc = (Map) thisListDoc.get(lv2DocCount);
        String thisDocNo = thisDoc.get("DocNo").toString();
        newRow[10] = thisDoc.get("EDate").toString();
        newRow[12] = thisDocNo;
        newRow[13] = thisDoc.get("EDate").toString();
        newRow[14] = "0".equals(thisDoc.get("ReceiveMoney").toString()) ? "-" : thisDoc.get("ReceiveMoney").toString();
        newRow[16] = "0".equals(thisDoc.get("CashMoney").toString()) ? "-" : thisDoc.get("CashMoney").toString();
        newRow[17] = "0".equals(thisDoc.get("CreditCardMoney").toString()) ? "-" : thisDoc.get("CreditCardMoney").toString();
        newRow[18] = "0".equals(thisDoc.get("CheckMoney").toString()) ? "-" : thisDoc.get("CheckMoney").toString();
        newRow[19] = "0".equals(thisDoc.get("BankMoney").toString()) ? "-" : thisDoc.get("BankMoney").toString();

        // 洗錢樣態
        String thisML = (String) mapML.get(thisOrderNo + thisDocNo);
        if (thisML != null) newRow[11] = thisML;

        // 期別
        Map thisOrder_no = (Map) mapOrder_no.get(thisDocNo);
        if (thisOrder_no != null) newRow[14] = thisOrder_no.get("QQ").toString();

        // 匯出地
        Map thisExtPlace = (Map) mapExtPlace.get(thisDocNo);
        if (thisExtPlace != null) newRow[19] = thisExtPlace.get("ExportingPlace").toString();

        if (mapDeputy.get(thisDoc.get("DocNo").toString()) != null) {
          Map thisDeputy = (Map) mapDeputy.get(thisDoc.get("DocNo").toString());
          newRow[21] = thisDeputy.get("DeputyName").toString();
          newRow[22] = thisDeputy.get("DeputyID").toString();
          newRow[23] = thisDeputy.get("DeputyRelationship").toString();
          newRow[24] = thisDeputy.get("C_STATUS").toString();
          newRow[25] = thisDeputy.get("B_STATUS").toString();
          newRow[26] = thisDeputy.get("R_STATUS").toString();
          newRow[27] = thisDeputy.get("Reason").toString();
          newRow[28] = thisDeputy.get("Money").toString();
        }
      }
      lv2DocCount++;
      mainList.add(newRow);

      // 是不是此訂單最後一筆
      boolean isLast = false;
      String nextOrderNo = "";
      try {
        nextOrderNo = arrTableData090[idx + 1][0].trim();
      } catch (Exception ex) {
        nextOrderNo = "IDX 超過界線";
        // System.out.println("idx out of Index");
      }
      if (!thisOrderNo.equals(nextOrderNo)) isLast = true;

      // 多筆收款，必須放在原mainList Add之後
      // 條件 : 本訂單最後一筆 + 收款列表不為NULL + 收款列表>計數器
      if (isLast && thisListDoc != null && thisListDoc.size() >= lv2DocCount) {
        for (int x = lv2DocCount; x < thisListDoc.size(); x++) {
          String[] newRowLv2 = new String[54];
          // newRowLv2[0] = Integer.toString(idx+1); //NO
          newRowLv2[1] = this090[0].trim(); // 訂單編號
          newRowLv2[2] = this090[2].trim(); // 案別
          newRowLv2[3] = "".equals(this090[3]) ? "" : this090[3].trim().replaceAll(",", "\n"); // 棟樓別
          newRowLv2[4] = "".equals(this090[4]) ? "" : this090[4].trim().replaceAll(",", "\n");
          newRowLv2[5] = this091[1].trim(); // 客戶姓名
          newRowLv2[6] = this091[2]; // 客戶ID
          newRowLv2[7] = this090[5]; // 付訂日期
          newRowLv2[8] = this278 == null ? "-" : this278.get("ContractDate").toString(); // 簽約日
          newRowLv2[9] = "-"; // 退戶日
          if ("D".equals(this090[18].trim())) {
            newRowLv2[9] = this090[17].trim();
          }

          Map thisDoc = (Map) thisListDoc.get(x);
          String thisDocNo = thisDoc.get("DocNo").toString();
          newRowLv2[12] = thisDocNo;
          newRowLv2[13] = thisDoc.get("EDate").toString();
          newRowLv2[14] = "0".equals(thisDoc.get("ReceiveMoney").toString()) ? "-" : thisDoc.get("ReceiveMoney").toString();
          newRowLv2[16] = "0".equals(thisDoc.get("CashMoney").toString()) ? "-" : thisDoc.get("CashMoney").toString();
          newRowLv2[17] = "0".equals(thisDoc.get("CreditCardMoney").toString()) ? "-" : thisDoc.get("CreditCardMoney").toString();
          newRowLv2[18] = "0".equals(thisDoc.get("CheckMoney").toString()) ? "-" : thisDoc.get("CheckMoney").toString();
          newRowLv2[19] = "0".equals(thisDoc.get("BankMoney").toString()) ? "-" : thisDoc.get("BankMoney").toString();

          // 洗錢樣態
          String thisML = (String) mapML.get(thisOrderNo + thisDocNo);
          if (thisML != null) newRowLv2[11] = thisML;

          // 期別
          Map thisOrder_no = (Map) mapOrder_no.get(thisDocNo);
          if (thisOrder_no != null) newRowLv2[14] = thisOrder_no.get("QQ").toString();

          // 匯出地
          Map thisExtPlace = (Map) mapExtPlace.get(thisDocNo);
          if (thisExtPlace != null) newRowLv2[19] = thisExtPlace.get("ExportingPlace").toString();

          if (mapDeputy.get(thisDocNo) != null) {
            Map thisDeputy = (Map) mapDeputy.get(thisDoc.get("DocNo").toString());
            newRowLv2[21] = thisDeputy.get("DeputyName").toString();
            newRowLv2[22] = thisDeputy.get("DeputyID").toString();
            newRowLv2[23] = thisDeputy.get("DeputyRelationship").toString();
            newRowLv2[24] = thisDeputy.get("C_STATUS").toString();
            newRowLv2[25] = thisDeputy.get("B_STATUS").toString();
            newRowLv2[26] = thisDeputy.get("R_STATUS").toString();
            newRowLv2[27] = thisDeputy.get("Reason").toString();
            newRowLv2[28] = thisDeputy.get("Money").toString();
          }
          mainList.add(newRowLv2);
        }
      }
      lastOrderNo = thisOrderNo;
    }

    return mainList;
  }

  public String getInformation() {
    return "---------------button2(列印).defaultValue()----------------";
  }
}