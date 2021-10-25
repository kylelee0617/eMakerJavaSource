package Sale.Sale05M094;

import java.text.SimpleDateFormat;
import java.util.Random;

import Farglory.aml.RiskCustomBean;
import Farglory.util.KSqlUtils;
import jcx.db.talk;
import jcx.jform.bproc;
import jcx.util.check;
import jcx.util.convert;
import jcx.util.datetime;

public class Save extends bproc {
  public String getDefaultValue(String value) throws Throwable {
    // 退戶存檔
    KSqlUtils ksUtil = new KSqlUtils();
    talk dbSale = getTalk("Sale");
    talk dbJPSLIB = getTalk("JGENLIB");
    talk dbEIP = getTalk("EIP");
    String[][] tb1_string = getTableData("table1");
    String[][] tb2_string = getTableData("table2");
    String stringSQL = "";
    boolean flag1 = false;
    boolean flag2 = false;
    String stringCustomName = "";
    String[][] retSale05M091 = null;
    StringBuilder sblog = new StringBuilder();
    ksUtil.setSaleLog(this.getFunctionName(), value, this.getUser(), ">>>Start");

    if (getValue("field1").trim().length() == 0) {
      message("案別不可空白");
      return value;
    }
    String stringTrxDate = getValue("field2").trim();
    if (stringTrxDate.length() == 0) {
      message("退名日期不可空白");
      return value;
    }
    // 2011/07/18 員工編號 B3018 Start
    if (stringTrxDate.length() != 10) {
      message("[退名日期] 格式錯誤(YYYY/MM/DD)。");
      return value;
    }
    stringTrxDate = convert.replace(stringTrxDate, "/", "");
    if (!check.isACDay(stringTrxDate)) {
      message("[退名日期] 格式錯誤(YYYY/MM/DD)。");
      return value;
    } else {
      setValue("field2", convert.FormatedDate(stringTrxDate, "/"));
    }
    // 2011/07/18 員工編號 B3018 End
    // Start 修改日：2008/01/09 員工編號：B3774
    if (getValue("BItemCd").trim().length() == 0) {
      message("大分類代碼不可空白");
      return value;
    }
    if (getValue("MItemCd").trim().length() == 0) {
      message("中分類代碼不可空白");
      return value;
    }
    if (getValue("SItemCd").trim().length() == 0) {
      message("小分類代碼不可空白");
      return value;
    }
    if (getValue("DItemCd").trim().length() == 0) {
      message("細分類代碼不可空白");
      return value;
    }
    if (getValue("unsubscribe").trim().length() == 0) {
      message("退訂方式不可空白");
      return value;
    }
    // End
    
    for (int i = 0; i < tb2_string.length; i++) {
      System.out.println("tb2_string" + tb2_string[i][0] + "");
      if (tb2_string[i][0].equals("1")) {
        flag1 = true;
      }
    }
    System.out.println("flag1" + flag1);
    if (flag1 == false) {
      message("請至少勾選一筆退戶");
    } else {
      ksUtil.setSaleLog(this.getFunctionName(), value, this.getUser(), ">>>orderNo:" + getValue("field3").trim());
      stringSQL = "select max(RecordNo) from Sale05M091 " + " WHERE OrderNo = '" + getValue("field3") + "'";
      retSale05M091 = dbSale.queryFromPool(stringSQL);
      int RecordNo = Integer.parseInt(retSale05M091[0][0]) + 1;
      stringSQL = "delete SALE05M094  " + " WHERE ProjectID1 = '" + getValue("field1").trim() + "'" + " AND OrderNo = '" + getValue("field3").trim() + "'" + " AND TrxDate = '"
          + getValue("field2").trim() + "'";
      dbSale.execFromPool(stringSQL);
      stringSQL = "delete SALE05M095  " + " WHERE ProjectID1 = '" + getValue("field1").trim() + "'" + " AND OrderNo = '" + getValue("field3").trim() + "'" + " AND TrxDate = '"
          + getValue("field2").trim() + "'";
      dbSale.execFromPool(stringSQL);

      for (int i = 0; i < tb2_string.length; i++) {
        System.out.println("i2" + tb2_string[i][2]);
        // if (tb2_string[i][0]!="0"){
        if (tb2_string[i][0].equals("1")) {
          stringSQL = " INSERT  " + " INTO Sale05M095" + " ( " + "ProjectID1," + " OrderNo," + " TrxDate," + " RecordNo," + " HouseCar," + " Position " + " ) " + " VALUES " + " ( "
              + "'" + getValue("field1").trim() + "'," + "'" + getValue("field3").trim() + "'," + "'" + getValue("field2").trim() + "'," + "N'" + tb2_string[i][2] + "'," + "N'"
              + tb2_string[i][3] + "'," + "N'" + tb2_string[i][4] + "'" + " ) ";
          dbSale.execFromPool(stringSQL);

          stringSQL = " UPDATE Sale05M092 SET" + "  StatusCd = 'D', " + "  TrxDate = '" + getValue("field2").trim() + "' " + " WHERE OrderNo ='" + getValue("field3").trim() + "' "
              + " and  Position ='" + tb2_string[i][4] + "' ";
          dbSale.execFromPool(stringSQL);

        } else {
          stringSQL = " UPDATE Sale05M092 SET" + "  StatusCd = null, " + "  TrxDate = null " + " WHERE OrderNo ='" + getValue("field3").trim() + "' " + " and  Position ='"
              + tb2_string[i][4] + "' ";
          dbSale.execFromPool(stringSQL);

        }
      }
      String stringToday = datetime.getToday("YYYY/mm/dd"); // 2015-05-13 B3018
      stringSQL = " INSERT INTO Sale05M094 ( " + "ProjectID1," + " OrderNo," + " TrxDate," + " BItemCd," + " MItemCd," + " SItemCd," + " DItemCd," + " LastYMD," + " unsubscribe "
          + " ) " + " VALUES " + " ( " + "'" + getValue("field1").trim() + "'," + "'" + getValue("field3").trim() + "'," + "'" + getValue("field2").trim() + "'," + "'"
          + getValue("BItemCd").trim() + "'," + "'" + getValue("MItemCd").trim() + "'," + "'" + getValue("SItemCd").trim() + "'," + "'" + getValue("DItemCd").trim() + "', " + "'"
          + stringToday + "', " + "'" + getValue("unsubscribe").trim() + "' " + " ) ";
      dbSale.execFromPool(stringSQL);
      // 系統疑似洗錢態樣
      // 洗錢追蹤流水號
      int intRecordNo = 1;
      stringSQL = "SELECT MAX(RecordNo) AS MaxNo FROM Sale05M070 WHERE OrderNo ='" + getValue("field3").trim() + "'";
      String[][] ret070Table = dbSale.queryFromPool(stringSQL);
      if (!"".equals(ret070Table[0][0].trim())) {
        intRecordNo = Integer.parseInt(ret070Table[0][0].trim()) + 1;
      }

      boolean isNonList = true;
      String RocNowDate = stringToday.replace("/", "");
      String tempROCYear = "" + (Integer.parseInt(RocNowDate.substring(0, RocNowDate.length() - 4)) - 1911);
      RocNowDate = tempROCYear + RocNowDate.substring(RocNowDate.length() - 4, RocNowDate.length());
      String strNowTime = new SimpleDateFormat("HHmmss").format(getDate());
      String userNo = getUser().toUpperCase().trim();
      String empNo = "";
      String[][] retEip = null;
      String errMsg = "";
      // actionNo
      String ram = "";
      Random random = new Random();
      for (int i = 0; i < 4; i++) {
        ram += String.valueOf(random.nextInt(10));
      }
      String actionNo = stringToday.replace("/", "") + strNowTime + ram;
      System.out.println("actionNo=====>" + actionNo);

      // 取姓名
      String custName = tb1_string[0][3].trim();
      String custID = tb1_string[0][2].trim();

      System.out.println("custName=====>" + custName);
      System.out.println("custID=====>" + custID);
      stringSQL = "SELECT EMPNO FROM FGEMPMAP where FGEMPNO ='" + userNo + "'";
      retEip = dbEIP.queryFromPool(stringSQL);
      if (retEip.length > 0) {
        empNo = retEip[0][0];
      }

      // 不適用LOG1~5,7~14
      // 1
      stringSQL = "INSERT INTO Sale05M070 ( OrderNo, ProjectID1, RecordNo, ActionNo, Func, RecordType, ActionName, RecordDesc, CustomID, CustomName, EDate, SHB00, SHB06A, SHB06B, SHB06,SHB97,SHB98,SHB99)  VALUES ('"
          + getValue("field3").trim() + "','" + getValue("field1").trim() + "','" + intRecordNo + "','" + actionNo + "','退戶','退戶警示','儲存','不適用','" + custID + "','" + custName
          + "','" + getValue("field2").trim() + "','RY','773','001','同一客戶同一營業日內2筆(含)以上包含現金、匯款、信用卡、支票交易，且每筆皆介於新台幣450,000~499,999元，系統檢核預警。','" + empNo + "','" + RocNowDate + "','"
          + strNowTime + "')";
      dbSale.execFromPool(stringSQL);
      intRecordNo++;
      // 2
      stringSQL = "INSERT INTO Sale05M070 ( OrderNo, ProjectID1, RecordNo, ActionNo, Func, RecordType, ActionName, RecordDesc, CustomID, CustomName, EDate, SHB00, SHB06A, SHB06B, SHB06,SHB97,SHB98,SHB99)  VALUES ('"
          + getValue("field3").trim() + "','" + getValue("field1").trim() + "','" + intRecordNo + "','" + actionNo + "','退戶','退戶警示','儲存','不適用','" + custID + "','" + custName
          + "','" + getValue("field2").trim() + "','RY','773','002','同一客戶3個營業日內，有2日以現金或匯款達450,000~499,999元, 系統檢核提示通報。','" + empNo + "','" + RocNowDate + "','" + strNowTime + "')";
      dbSale.execFromPool(stringSQL);
      intRecordNo++;
      // 3
      stringSQL = "INSERT INTO Sale05M070 ( OrderNo, ProjectID1, RecordNo, ActionNo, Func, RecordType, ActionName, RecordDesc, CustomID, CustomName, EDate, SHB00, SHB06A, SHB06B, SHB06,SHB97,SHB98,SHB99)  VALUES ('"
          + getValue("field3").trim() + "','" + getValue("field1").trim() + "','" + intRecordNo + "','" + actionNo + "','退戶','退戶警示','儲存','不適用','" + custID + "','" + custName
          + "','" + getValue("field2").trim() + "','RY','773','003','同一客戶同一營業日現金繳納累計達50萬元(含)以上，須檢核是否符合疑似洗錢交易表徵。','" + empNo + "','" + RocNowDate + "','" + strNowTime + "')";
      dbSale.execFromPool(stringSQL);
      intRecordNo++;
      // 4
      stringSQL = "INSERT INTO Sale05M070 ( OrderNo, ProjectID1, RecordNo, ActionNo, Func, RecordType, ActionName, RecordDesc, CustomID, CustomName, EDate, SHB00, SHB06A, SHB06B, SHB06,SHB97,SHB98,SHB99)  VALUES ('"
          + getValue("field3").trim() + "','" + getValue("field1").trim() + "','" + intRecordNo + "','" + actionNo + "','退戶','退戶警示','儲存','不適用','" + custID + "','" + custName
          + "','" + getValue("field2").trim() + "','RY','773','004','同一客戶3個營業日內，累計繳交現金超過50萬元, 系統檢核提示通報。','" + empNo + "','" + RocNowDate + "','" + strNowTime + "')";
      dbSale.execFromPool(stringSQL);
      intRecordNo++;
      // 5
      stringSQL = "INSERT INTO Sale05M070 ( OrderNo, ProjectID1, RecordNo, ActionNo, Func, RecordType, ActionName, RecordDesc, CustomID, CustomName, EDate, SHB00, SHB06A, SHB06B, SHB06,SHB97,SHB98,SHB99)  VALUES ('"
          + getValue("field3").trim() + "','" + getValue("field1").trim() + "','" + intRecordNo + "','" + actionNo + "','退戶','退戶警示','儲存','不適用','" + custID + "','" + custName
          + "','" + getValue("field2").trim() + "','RY','773','005','代繳款人與購買人關係為非二等親內血/姻親，系統檢核提示通報。','" + empNo + "','" + RocNowDate + "','" + strNowTime + "')";
      dbSale.execFromPool(stringSQL);
      intRecordNo++;
      // 7
      stringSQL = "INSERT INTO Sale05M070 ( OrderNo, ProjectID1, RecordNo, ActionNo, Func, RecordType, ActionName, RecordDesc, CustomID, CustomName, EDate, SHB00, SHB06A, SHB06B, SHB06,SHB97,SHB98,SHB99)  VALUES ('"
          + getValue("field3").trim() + "','" + getValue("field1").trim() + "','" + intRecordNo + "','" + actionNo + "','退戶','退戶警示','儲存','不適用','" + custID + "','" + custName
          + "','" + getValue("field2").trim() + "','RY','773','007','同一客戶同一營業日現金繳納累計達50萬元(含)以上，須檢核是否符合疑似洗錢交易表徵。','" + empNo + "','" + RocNowDate + "','" + strNowTime + "')";
      dbSale.execFromPool(stringSQL);
      intRecordNo++;
      // 8
      stringSQL = "INSERT INTO Sale05M070 ( OrderNo, ProjectID1, RecordNo, ActionNo, Func, RecordType, ActionName, RecordDesc, CustomID, CustomName, EDate, SHB00, SHB06A, SHB06B, SHB06,SHB97,SHB98,SHB99)  VALUES ('"
          + getValue("field3").trim() + "','" + getValue("field1").trim() + "','" + intRecordNo + "','" + actionNo + "','退戶','退戶警示','儲存','不適用','" + custID + "','" + custName
          + "','" + getValue("field2").trim() + "','RY','773','008','不動產銷售由第三方代理或繳款，系統檢核提示通報。','" + empNo + "','" + RocNowDate + "','" + strNowTime + "')";
      dbSale.execFromPool(stringSQL);
      intRecordNo++;
      // 9
      stringSQL = "INSERT INTO Sale05M070 ( OrderNo, ProjectID1, RecordNo, ActionNo, Func, RecordType, ActionName, RecordDesc, CustomID, CustomName, EDate, SHB00, SHB06A, SHB06B, SHB06,SHB97,SHB98,SHB99)  VALUES ('"
          + getValue("field3").trim() + "','" + getValue("field1").trim() + "','" + intRecordNo + "','" + actionNo + "','退戶','退戶警示','儲存','不適用','" + custID + "','" + custName
          + "','" + getValue("field2").trim() + "','RY','773','009','客戶係來自主管機關所公告防制洗錢與打擊資恐有嚴重缺失之國家或地區，及其他未遵循或未充分遵循之國家或地區，應檢核其合理性。','" + empNo + "','" + RocNowDate + "','"
          + strNowTime + "')";
      dbSale.execFromPool(stringSQL);
      intRecordNo++;
      // 10
      stringSQL = "INSERT INTO Sale05M070 ( OrderNo, ProjectID1, RecordNo, ActionNo, Func, RecordType, ActionName, RecordDesc, CustomID, CustomName, EDate, SHB00, SHB06A, SHB06B, SHB06,SHB97,SHB98,SHB99)  VALUES ('"
          + getValue("field3").trim() + "','" + getValue("field1").trim() + "','" + intRecordNo + "','" + actionNo + "','退戶','退戶警示','儲存','不適用','" + custID + "','" + custName
          + "','" + getValue("field2").trim() + "','RY','773','010','自主管機關所公告防制洗錢與打擊資助恐怖份子有嚴重缺失之國家或地區、及其他未遵循或未充分遵循之國家或地區匯入之交易款項，應檢核其合理性。','" + empNo + "','" + RocNowDate + "','"
          + strNowTime + "')";
      dbSale.execFromPool(stringSQL);
      intRecordNo++;
      // 11
      stringSQL = "INSERT INTO Sale05M070 ( OrderNo, ProjectID1, RecordNo, ActionNo, Func, RecordType, ActionName, RecordDesc, CustomID, CustomName, EDate, SHB00, SHB06A, SHB06B, SHB06,SHB97,SHB98,SHB99)  VALUES ('"
          + getValue("field3").trim() + "','" + getValue("field1").trim() + "','" + intRecordNo + "','" + actionNo + "','退戶','退戶警示','儲存','不適用','" + custID + "','" + custName
          + "','" + getValue("field2").trim() + "','RY','773','011','交易最終受益人或交易人為主管機關公告之恐怖分子或團體；或國際認定或追查之恐怖組織；或交易資金疑似與恐怖組織有關聯者，應依資恐防制法進行相關作業。','" + empNo + "','" + RocNowDate
          + "','" + strNowTime + "')";
      dbSale.execFromPool(stringSQL);
      intRecordNo++;
      // 12
      stringSQL = "INSERT INTO Sale05M070 ( OrderNo, ProjectID1, RecordNo, ActionNo, Func, RecordType, ActionName, RecordDesc, CustomID, CustomName, EDate, SHB00, SHB06A, SHB06B, SHB06,SHB97,SHB98,SHB99)  VALUES ('"
          + getValue("field3").trim() + "','" + getValue("field1").trim() + "','" + intRecordNo + "','" + actionNo + "','退戶','退戶警示','儲存','不適用','" + custID + "','" + custName
          + "','" + getValue("field2").trim() + "','RY','773','012','客戶要求將不動產權利登記予第三人，未能提出任何關聯或拒絕說明之異常狀況。','" + empNo + "','" + RocNowDate + "','" + strNowTime + "')";
      dbSale.execFromPool(stringSQL);
      intRecordNo++;
      // 13
      stringSQL = "INSERT INTO Sale05M070 ( OrderNo, ProjectID1, RecordNo, ActionNo, Func, RecordType, ActionName, RecordDesc, CustomID, CustomName, EDate, SHB00, SHB06A, SHB06B, SHB06,SHB97,SHB98,SHB99)  VALUES ('"
          + getValue("field3").trim() + "','" + getValue("field1").trim() + "','" + intRecordNo + "','" + actionNo + "','退戶','退戶警示','儲存','不適用','" + custID + "','" + custName
          + "','" + getValue("field2").trim() + "','RY','773','013','客戶支付不動產交易之款項，以現鈔支付訂金以外各期價款，且無合理說明資金來源，應檢核是否符合疑似洗錢交易表徵。','" + empNo + "','" + RocNowDate + "','" + strNowTime
          + "')";
      dbSale.execFromPool(stringSQL);
      intRecordNo++;
      // 14
      stringSQL = "INSERT INTO Sale05M070 ( OrderNo, ProjectID1, RecordNo, ActionNo, Func, RecordType, ActionName, RecordDesc, CustomID, CustomName, EDate, SHB00, SHB06A, SHB06B, SHB06,SHB97,SHB98,SHB99)  VALUES ('"
          + getValue("field3").trim() + "','" + getValue("field1").trim() + "','" + intRecordNo + "','" + actionNo + "','退戶','退戶警示','儲存','不適用','" + custID + "','" + custName
          + "','" + getValue("field2").trim() + "','RY','773','014','客戶於簽約前提前付清自備款，且無合理說明資金來源，應檢核是否符合疑似洗錢交易表徵。','" + empNo + "','" + RocNowDate + "','" + strNowTime + "')";
      dbSale.execFromPool(stringSQL);
      intRecordNo++;
      // 退戶預警請排除：03客戶面-03-其他-01個人因素-03.04.07.08.09.10.11
      // strAlertmag = "同一客戶不動產買賣，簽約前退訂取消購買警示。請依洗錢防制作業辦理。";
      if ("03".equals(getValue("DItemCd").trim()) || "04".equals(getValue("DItemCd").trim()) || "07".equals(getValue("DItemCd").trim()) || "08".equals(getValue("DItemCd").trim())
          || "09".equals(getValue("DItemCd").trim()) || "10".equals(getValue("DItemCd").trim()) || "11".equals(getValue("DItemCd").trim())) {
        if ("01".equals(getValue("SItemCd").trim())) {
          if ("03".equals(getValue("MItemCd").trim())) {
            if ("03".equals(getValue("BItemCd").trim())) {
              isNonList = false;
            }
          }
        }
      }

      // 20191004
      // 支票 取消禁背 取消劃線
      if ("支票-取消禁背".equals(getValue("unsubscribe").trim()) || "支票-取消劃線".equals(getValue("unsubscribe").trim())) {
        stringSQL = "SELECT CustomNo, CustomName FROM Sale05M091 WHERE OrderNo = '" + getValue("field3").trim() + "'";
        retSale05M091 = dbSale.queryFromPool(stringSQL);
        if (retSale05M091.length > 0) {
          for (int i = 0; i < retSale05M091.length; i++) {
            String strCustomno = retSale05M091[i][0].trim();
            String strCustomName = retSale05M091[i][1].trim();
            if ("支票-取消禁背".equals(getValue("unsubscribe").trim())) {
              // Sale05M070
              stringSQL = "INSERT INTO Sale05M070 ( OrderNo, ProjectID1, RecordNo, ActionNo, Func, RecordType, ActionName, RecordDesc, CustomID, CustomName, EDate, SHB00, SHB06A, SHB06B, SHB06,SHB97,SHB98,SHB99)  VALUES ('"
                  + getValue("field3").trim() + "','" + getValue("field1").trim() + "','" + intRecordNo + "','" + actionNo + "','退戶','取消禁背','儲存','客戶" + strCustomName
                  + "取消交易退還款項，要求公司給付取消禁止背書轉讓之票據，請依洗錢及資恐防制作業辦理。','" + strCustomno + "','" + strCustomName + "','" + getValue("field2").trim() + "','RY','773','015','客戶"
                  + strCustomName + "取消交易退還款項，要求公司給付取消禁止背書轉讓之票據，請依洗錢及資恐防制作業辦理。','" + empNo + "','" + RocNowDate + "','" + strNowTime + "')";
              dbSale.execFromPool(stringSQL);
              intRecordNo++;
              // AS400
              stringSQL = "INSERT INTO PSHBPF (SHB00, SHB01, SHB03, SHB04, SHB05, SHB06A, SHB06B, SHB06, SHB97,SHB98, SHB99) VALUES ('RY', '" + getValue("field3").trim() + "', '"
                  + RocNowDate + "', '" + retSale05M091[i][0] + "', '" + retSale05M091[i][1] + "', '773', '015', '要求公司開立取消禁止背書轉讓支票作為給付方式，應檢核是否符合疑似洗錢交易表徵。','" + empNo + "','"
                  + RocNowDate + "','" + strNowTime + "')";
              dbJPSLIB.execFromPool(stringSQL);
              if ("".equals(errMsg)) {
                errMsg = "客戶" + strCustomName + "取消交易退還款項，要求公司給付取消禁止背書轉讓之票據，請依洗錢及資恐防制作業辦理。";
              } else {
                errMsg = errMsg + "\n客戶" + strCustomName + "取消交易退還款項，要求公司給付取消禁止背書轉讓之票據，請依洗錢及資恐防制作業辦理。";
              }
            } else {
              // 不符合
              stringSQL = "INSERT INTO Sale05M070 ( OrderNo, ProjectID1, RecordNo, ActionNo, Func, RecordType, ActionName, RecordDesc, CustomID, CustomName, EDate, SHB00, SHB06A, SHB06B, SHB06,SHB97,SHB98,SHB99)  VALUES ('"
                  + getValue("field3").trim() + "','" + getValue("field1").trim() + "','" + intRecordNo + "','" + actionNo + "','退戶','取消禁背','儲存','不符合','" + strCustomno + "','"
                  + strCustomName + "','" + getValue("field2").trim() + "','RY','773','015','客戶" + strCustomName + "取消交易退還款項，要求公司給付取消禁止背書轉讓之票據，請依洗錢及資恐防制作業辦理。','" + empNo + "','"
                  + RocNowDate + "','" + strNowTime + "')";
              dbSale.execFromPool(stringSQL);
              intRecordNo++;
            }
            if ("支票-取消劃線".equals(getValue("unsubscribe").trim())) {
              // Sale05M070
              stringSQL = "INSERT INTO Sale05M070 ( OrderNo, ProjectID1, RecordNo, ActionNo, Func, RecordType, ActionName, RecordDesc, CustomID, CustomName, EDate, SHB00, SHB06A, SHB06B, SHB06,SHB97,SHB98,SHB99)  VALUES ('"
                  + getValue("field3").trim() + "','" + getValue("field1").trim() + "','" + intRecordNo + "','" + actionNo + "', '退戶','取消劃線','儲存','客戶" + strCustomName
                  + "取消交易退還款項，要求公司給付撤銷平行線之票據，請依洗錢及資恐防制作業辦理。','" + strCustomno + "','" + strCustomName + "','" + getValue("field2").trim() + "','RY','773','016','客戶" + strCustomName
                  + "取消交易退還款項，要求公司給付撤銷平行線之票據，請依洗錢及資恐防制作業辦理。','" + empNo + "','" + RocNowDate + "','" + strNowTime + "')";
              dbSale.execFromPool(stringSQL);
              intRecordNo++;
              // AS400
              stringSQL = "INSERT INTO PSHBPF (SHB00, SHB01, SHB03, SHB04, SHB05, SHB06A, SHB06B, SHB06, SHB97,SHB98, SHB99) VALUES ('RY', '" + getValue("field3").trim() + "', '"
                  + RocNowDate + "', '" + retSale05M091[i][0] + "', '" + retSale05M091[i][1] + "', '773', '016', '要求公司開立撤銷平行線(取消劃線)支票作為給付方式，應檢核是否符合疑似洗錢交易表徵。','" + empNo + "','"
                  + RocNowDate + "','" + strNowTime + "')";
              dbJPSLIB.execFromPool(stringSQL);
              if ("".equals(errMsg)) {
                errMsg = "客戶" + strCustomName + "取消交易退還款項，要求公司給付撤銷平行線之票據，請依洗錢及資恐防制作業辦理。";
              } else {
                errMsg = errMsg + "\n客戶" + strCustomName + "取消交易退還款項，要求公司給付撤銷平行線之票據，請依洗錢及資恐防制作業辦理。";
              }
            } else {
              // 不符合
              stringSQL = "INSERT INTO Sale05M070 ( OrderNo, ProjectID1, RecordNo, ActionNo, Func, RecordType, ActionName, RecordDesc, CustomID, CustomName, EDate, SHB00, SHB06A, SHB06B, SHB06,SHB97,SHB98,SHB99)  VALUES ('"
                  + getValue("field3").trim() + "','" + getValue("field1").trim() + "','" + intRecordNo + "','" + actionNo + "', '退戶','取消劃線','儲存','不符合','" + strCustomno + "','"
                  + strCustomName + "','" + getValue("field2").trim() + "','RY','773','016','客戶" + strCustomName + "取消交易退還款項，要求公司給付撤銷平行線之票據，請依洗錢及資恐防制作業辦理。','" + empNo + "','"
                  + RocNowDate + "','" + strNowTime + "')";
              dbSale.execFromPool(stringSQL);
              intRecordNo++;
            }
          }
        }
      }

      RiskCustomBean[] cBeans = ksUtil.getCustom("", getValue("field3").trim(), true);
      if (isNonList) {
        // 4.同一客戶不動產買賣，簽約前退訂取消購買，應檢核其合理性。
        for (int i = 0; i < cBeans.length; i++) {
          RiskCustomBean cBean = cBeans[i];
          String strCustomNo = cBean.getCustomNo();
          String strCustomName = cBean.getCustomName();
          // Sale05M070
          stringSQL = "INSERT INTO Sale05M070 ( OrderNo, ProjectID1, RecordNo,ActionNo, Func, RecordType, ActionName,RecordDesc, CustomID, CustomName, EDate, SHB00, SHB06A, SHB06B, SHB06,SHB97,SHB98,SHB99)  VALUES ('"
              + getValue("field3").trim() + "','" + getValue("field1").trim() + "','" + intRecordNo + "','" + actionNo + "','退戶','退戶警示','儲存','客戶" + strCustomName
              + "簽約前退訂取消交易，請依洗錢及資恐防制作業辦理。','" + strCustomNo + "','" + strCustomName + "','" + getValue("field2").trim() + "','RY','773','006','客戶" + strCustomName
              + "簽約前退訂取消交易，請依洗錢及資恐防制作業辦理。','" + empNo + "','" + RocNowDate + "','" + strNowTime + "')";
          dbSale.execFromPool(stringSQL);
          intRecordNo++;
          // AS400
          stringSQL = "INSERT INTO PSHBPF (SHB00, SHB01, SHB03, SHB04, SHB05, SHB06A, SHB06B, SHB06, SHB97,SHB98, SHB99) VALUES ('RY', '" + getValue("field3").trim() + "', '"
              + RocNowDate + "', '" + strCustomNo + "', '" + strCustomName + "', '773', '006', '同一客戶不動產買賣，簽約前退訂取消購買警示。','" + empNo + "','" + RocNowDate + "','" + strNowTime + "')";
          dbJPSLIB.execFromPool(stringSQL);
          if ("".equals(errMsg)) {
            errMsg = "客戶" + strCustomName + "簽約前退訂取消交易，請依洗錢及資恐防制作業辦理。";
          } else {
            errMsg = errMsg + "\n客戶" + strCustomName + "簽約前退訂取消交易，請依洗錢及資恐防制作業辦理。";
          }
        }
      }

      // TODO: rule23
      String rule23 = getValue("AMLRule231").trim();
      if ("B".equals(rule23)) {
        String amlDesc = ksUtil.getAMLDescOne("023");
        String allCustNames = ksUtil.getCustomNames(getValue("field1").trim(), getValue("field3").trim());
        amlDesc = amlDesc.replaceAll("<customTitle>", "客戶").replace("<customName>", allCustNames);
        errMsg += "\n" + amlDesc;

        for (int i = 0; i < cBeans.length; i++) {
          RiskCustomBean cBean = cBeans[i];
          String strCustomNo = cBean.getCustomNo();
          String strCustomName = cBean.getCustomName();
          // Sale05M070
          stringSQL = "INSERT INTO Sale05M070 ( OrderNo, ProjectID1, RecordNo,ActionNo, Func, RecordType, ActionName,RecordDesc, CustomID, CustomName, EDate, SHB00, SHB06A, SHB06B, SHB06,SHB97,SHB98,SHB99)  VALUES ('"
              + getValue("field3").trim() + "','" + getValue("field1").trim() + "','" + intRecordNo + "','" + actionNo + "','退戶','退戶警示','儲存'," + "'" + amlDesc + "','" + strCustomNo
              + "','" + strCustomName + "','" + getValue("field2").trim() + "','RY','773','023'," + "'" + amlDesc + "','" + empNo + "','" + RocNowDate + "','" + strNowTime + "')";
          dbSale.execFromPool(stringSQL);
          intRecordNo++;
          // AS400
          stringSQL = "INSERT INTO PSHBPF (SHB00, SHB01, SHB03, SHB04, SHB05, SHB06A, SHB06B, SHB06, SHB97,SHB98, SHB99) VALUES ('RY', '" + getValue("field3").trim() + "', '"
              + RocNowDate + "', '" + strCustomNo + "', '" + strCustomName + "', '773', '023', '" + amlDesc + "','" + empNo + "','" + RocNowDate + "','" + strNowTime + "')";
          dbJPSLIB.execFromPool(stringSQL);
          if ("".equals(errMsg)) {
            errMsg = "客戶" + strCustomName + "簽約前退訂取消交易，請依洗錢及資恐防制作業辦理。";
          } else {
            errMsg = errMsg + "\n客戶" + strCustomName + "簽約前退訂取消交易，請依洗錢及資恐防制作業辦理。";
          }
        }

        getButton("SendMailAction2").doClick();
      }

      // 送出errMsg
      if (!"".equals(errMsg)) {
        setValue("errMsgBoxText", errMsg);
        getButton("errMsgBoxBtn").doClick();
      }

      // 開啟按鈕
      getButton("button3").setVisible(true);
      getButton("button4").setVisible(true);

      // 註銷關聯人
      getButton("RenewRelated").doClick();

      message("退戶存檔成功。");

      ksUtil.setSaleLog(this.getFunctionName(), value, this.getUser(), sblog.toString() + " : End");
    }
    return value;
  }

  public String getInformation() {
    return "---------------button4(\u9000\u6236\u524d\u67e5\u8a62).defaultValue()----------------";
  }
}
