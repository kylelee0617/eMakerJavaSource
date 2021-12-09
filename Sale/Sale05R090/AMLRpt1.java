//20200107 Kyle add
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

public class AMLRpt1 extends bproc {
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

    // sample路徑
//    String stringPrintExcel = "G:\\kyleTest\\Excel\\Sale05R090_AML_Rpt.xls";
    String stringPrintExcel = "G:\\資訊室\\Excel\\Sale\\Sale05R090\\AMLRpt1.xlt";

    // 建立Excel物件
    Vector retVector = exeFun.getExcelObject(stringPrintExcel);
    Dispatch objectSheet1 = (Dispatch) retVector.get(1);

    // A1 公司名
    // String stringCompanyNo = getValue("CompanyNo").trim();
    // exeFun.putDataIntoExcel(0, 1, getCompanyName(stringCompanyNo), objectSheet1);

    // Start of Body 資料本體
    for (int intRow = 0; intRow < mainList.size(); intRow++) {
      String[] thisRow = (String[]) mainList.get(intRow);
      int recordNo = intRow + exeFun.getStartDataRow();

      for (int intCon = 0; intCon < 53; intCon++) {
        exeFun.putDataIntoExcel(intCon, recordNo, thisRow[intCon], objectSheet1);
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
    // System.out.println(">>>setOrderNos>>>" + setOrderNos);
    // System.out.println(">>>map090>>>" + map090);

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

    Map map278 = data.getSale05M278(sbQorderNos.toString());
    // System.out.println(">>>map278>>>" + map278);

    Map map091 = data.getSale05M091(sbQorderNos.toString());
    // System.out.println(">>>map091>>>" + map091);

    Map map091b = data.getSale05M091Ben(sbQorderNos.toString());
    // System.out.println(">>>map091b>>>" + map091b);

    // query_log
    Map queryLogAll = mlpUtils.getQueryLog_All();
    System.out.println(">>>queryLogAll>>>" + queryLogAll);

    // 查詢業別
    Map jobType = mlpUtils.get400PDCZPF();
    System.out.println(">>>jobType>>>" + jobType);

    String lastOrderNo = "";
    for (int idx = 0; idx < arrTableData090.length; idx++) {
      String[] newRow = new String[53];

      // 目前處理訂單號
      String thisOrderNo = arrTableData090[idx][0].trim();
      System.out.println(">>>thisOrderNo>>>" + thisOrderNo);

      // 合約棟樓別
      Map this278 = (Map) map278.get(thisOrderNo);

      // 購物訂單主檔
      String[] this090 = arrTableData090[idx];
      // System.out.println(">>>this090>>>" + this090);

      // 購屋訂單客戶
      String[] this091 = (String[]) map091.get(thisOrderNo);
      // System.out.println(">>>this091>>>" + this091);

      // 購屋訂單實質受益人
      String[] this091b = (String[]) map091b.get(thisOrderNo);
      // System.out.println(">>>this091b>>>" + this091b);

      newRow[0] = Integer.toString(idx + 1); // NO
      newRow[3] = "".equals(this090[3]) ? "" : this090[3].trim().replaceAll(",", "\n"); // 棟樓別
      newRow[4] = "".equals(this090[4]) ? "" : this090[4].trim().replaceAll(",", "\n"); // 車位
      newRow[6] = "-"; // 簽約日
      newRow[8] = this090[19].trim(); // 坪數
      newRow[9] = this090[20].trim(); // 售價
      newRow[10] = this090[27].trim(); // 贈送
      newRow[11] = this090[22].trim(); // 佣金
      newRow[12] = this090[23].trim(); // 中原佣金
      newRow[13] = this090[21].trim(); // 利息
      newRow[14] = "-"; // 淨售
      newRow[15] = this090[24].trim(); // 底價
      newRow[16] = "-"; // 超低價

      // 更新合約棟樓
      if (this278 != null) {
        newRow[6] = this278.get("ContractDate").toString(); // 簽約日
        newRow[14] = this278.get("PureMoney").toString(); // 淨售
        newRow[16] = this278.get("BalaMoney").toString(); // 超低價
      }

      // 同筆訂單第一次
      // 20200213 kyle 更新 : 主表全面改成一筆搜尋出來，已經沒有是否第一次的問題了
      if (!thisOrderNo.equals(lastOrderNo)) {
        newRow[1] = thisOrderNo; // 訂單編號
        newRow[2] = this090[2].trim(); // 案別
        newRow[5] = this090[5].trim(); // 訂單日
        newRow[17] = "".equals(this090[7]) ? "-" : this090[7].trim(); // 售出人1
        newRow[18] = "".equals(this090[8]) ? "-" : this090[8].trim(); // 售出人2
        newRow[19] = "".equals(this090[9]) ? "-" : this090[9].trim(); // 售出人3
        newRow[20] = "".equals(this090[10]) ? "-" : this090[10].trim(); // 售出人4
        newRow[21] = "".equals(this090[11]) ? "-" : this090[11].trim(); // 售出人5
        newRow[22] = "".equals(this090[12]) ? "-" : this090[12].trim(); // 售出人6
        newRow[23] = "".equals(this090[13]) ? "-" : this090[13].trim(); // 售出人7
        newRow[24] = "".equals(this090[14]) ? "-" : this090[14].trim(); // 售出人8
        newRow[25] = "".equals(this090[15]) ? "-" : this090[15].trim(); // 售出人9
        newRow[26] = "".equals(this090[16]) ? "-" : this090[16].trim(); // 售出人10

        // 退戶日
        if ("D".equals(this090[18].trim())) {
          newRow[7] = this090[17].trim();
        } else {
          newRow[7] = "-";
        }

        // 客戶基本資料
        newRow[27] = this091[1]; // 姓名
        newRow[28] = "".equals(this091[2]) ? "-" : this091[2].trim(); // ID
        newRow[29] = "".equals(this091[3]) ? "-" : this091[3].trim(); // 國籍
        newRow[30] = "".equals(this091[4]) ? "-" : this091[4].trim(); // 縣市
        newRow[31] = "".equals(this091[5]) ? "-" : this091[5].trim(); // 區
//        newRow[32] = getFormatDate(this091[6].trim()); // 生日
        newRow[32] = this091[6].trim(); // 生日
        newRow[33] = getBG(this091[2]); // 性別
        newRow[34] = "".equals(this091[7]) ? "-" : this091[7].trim(); // 業別
        newRow[35] = "".equals(this091[8]) ? "-" : this091[8].trim(); // 職位
        newRow[36] = "".equals(this091[9]) ? "-" : this091[9].trim(); // 風險等級
        newRow[37] = "".equals(this091[10]) ? "-" : this091[10].trim(); // 控管名單
        newRow[38] = "".equals(this091[11]) ? "-" : this091[11].trim(); // 黑名單
        newRow[39] = "".equals(this091[12]) ? "-" : this091[12].trim(); // 厲害關係人
        newRow[40] = "".equals(this090[25]) ? "-" : this090[25].trim(); // 資金來源
        newRow[41] = "".equals(this090[26]) ? "-" : this090[26].trim(); // 購屋目的

        // 實質受益人
        if (this091b != null) {
          newRow[42] = this091b[1].trim(); // 身分
          newRow[43] = this091b[2].trim(); // 姓名
          newRow[44] = this091b[3].trim(); // ID
          newRow[45] = this091b[4].trim(); // 國籍
          newRow[46] = getFormatDate(this091b[5].trim()); // 生日
          newRow[47] = getBG(this091b[3].trim()); // 性別
          newRow[48] = getJob(this090[2].trim(), this091b, queryLogAll, jobType); // 業別
          newRow[49] = "-"; // 職位(沒有， 不要了)
          newRow[50] = "".equals(this091b[6]) ? "-" : this091b[6].trim(); // 控管名單
          newRow[51] = "".equals(this091b[7]) ? "-" : this091b[7].trim(); // 黑名單
          newRow[52] = "".equals(this091b[8]) ? "-" : this091b[8].trim(); // 利害關係人
        } else {
          for (int zz = 42; zz <= 52; zz++) {
            newRow[zz] = "-";
          }
          // 因為我TM懶得寫10行
        }
      }
      mainList.add(newRow);
      lastOrderNo = thisOrderNo;
    }

    return mainList;
  }

  private String getBG(String id) throws Throwable {
    if (id.length() == 0) return "-";

    String rs = "";
    String[] spID = id.split("\n");
    for (int x = 0; x < spID.length; x++) {
      if (x > 0) {
        rs += "\n";
      }
      String tmpId = spID[x].trim();
      String fir = tmpId.substring(0, 1);
      String sec = tmpId.substring(1, 2);
      String tmpRS = "-";
      if (fir.matches("[A-Z]")) {
        if ("1".equals(sec)) tmpRS = "男";
        if ("2".equals(sec)) tmpRS = "女";
      }
      rs += tmpRS;
    }
    return rs;
  }

  private String getFormatDate(String day) throws Throwable {
    if (day.length() == 0) return "-";

    String rs = "";
    String[] spDay = day.split("\n");
    for (int x = 0; x < spDay.length; x++) {
      if (x > 0) {
        rs += "\n";
      }
      String tmpDay = spDay[x].trim();
      String tmpRS = kUtils.formatACDate(tmpDay);
      rs += tmpRS;
    }
    return rs;
  }

  private String getJob(String projectID, String[] this091b, Map queryLogAll, Map jobType) throws Throwable {
    String tmpNos = this091b[3];
    String tmpNames = this091b[2];
    String[] customNos = tmpNos.split("\n");
    String[] customNames = tmpNames.split("\n");

    String rs = "";
    for (int x = 0; x < customNos.length; x++) {
      if (x > 0) {
        rs += "\n";
      }
      String no = customNos[x];
      String name = customNames[x];
      String tmpRS = "查無業別資料";
      // 更新實質受益人業別
      String jobMapKey = projectID + no + name;
      System.out.println(">>>jobMapKey>>>" + jobMapKey);

      String[] qLog = (String[]) queryLogAll.get(jobMapKey);
      String job_type = "";
      if (qLog != null) job_type = qLog[3].trim();
      System.out.println(">>>job_type>>>" + job_type);
      if (!"".equals(job_type)) {
        if (!"".equals(jobType.get(job_type))) tmpRS = jobType.get(job_type).toString().trim(); // 業別
      }
      rs += tmpRS;
    }
    return rs;
  }

  public String getInformation() {
    return "---------------button2(列印).defaultValue()----------------";
  }
}