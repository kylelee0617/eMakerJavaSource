package Doc;

import java.util.Hashtable;
import java.util.Vector;

import Farglory.util.FargloryUtil;
import jcx.db.talk;
import jcx.net.smtp;
import jcx.util.convert;
import jcx.util.datetime;

public class Doc7B0204_bk211103 {
  public static void main(String args[]) throws Throwable {
    System.out.println(">>>排程 Doc7B0204 啟動<<<");
    
    FargloryUtil exeUtil = new FargloryUtil();
    talk dbDoc = new talk("mssql", "MISSQL", "doc", "docfglife", "Doc"); // Doc
    talk dbSale = new talk("mssql", "MISSQL", "sale", "salefglife", "Sale"); // SaleEffect，Sale
    talk dbSale1 = new talk("mssql", "MISSQL", "sale", "salefglife", "Sale"); // SaleEffect，Sale
    talk dbSale2 = new talk("mssql", "MISSQL", "sale", "salefglife", "Sale"); // SaleEffect，Sale
    Vector vectorEmpNoP = getDoc3M011EmployeeNo("P", "", exeUtil, dbDoc, new Vector());
    //
    String stringFunctionName = "每日-行銷-案別-公司-總銷金額同步更新";
    String stringTodayAC = datetime.getToday("YYYY/mm/dd");
    long longTime1 = exeUtil.getTimeInMillis();
    String stringStartTime = datetime.getTime("YYYY/mm/dd h:m:s");
    if (isTodayNotDoCheck(stringFunctionName, stringTodayAC, exeUtil, dbDoc)) {
      doMail("請購請款系統－" + stringFunctionName, "不執行", exeUtil);
      dbDoc.close();
      dbSale.close();
      dbSale1.close();
      dbSale2.close();
      return;
    }
    System.out.println(">>>test111");

    try {
      // 單一業主別總銷金額之案別
      String stringProjectID1 = "";
      String stringTotalSaleMoney = "";
      String stringTotalSaleMoneyL = "";
      String stringTotMoney = "";
      String stringComNo = "";
      String stringMailData = "";
      String stringSql = "";
      String stringTime = datetime.getTime("YYYY/mm/dd h:m:s");
      String[] arrayMoney = null;
      Vector vectorProjectID1 = new Vector();
      Vector vectorComNo = new Vector();
      Vector vectorTrueComNo = new Vector();
      Vector vectorTotalSaleMoney = new Vector();
      Vector vectorSql = new Vector();
      Hashtable hashtableTotalSaleMoney = new Hashtable();
      Hashtable hashtableTotalSaleMoneyRatio = new Hashtable();
      Hashtable hashtableComNo = new Hashtable();
      Hashtable hashtableTrueComNo = new Hashtable();
      Hashtable hashtableData = new Hashtable();
      Hashtable hashtableAnd = new Hashtable();
      boolean booleanDB = false;
      
      doDoc7M0204(exeUtil, dbDoc, vectorProjectID1, hashtableTotalSaleMoney, hashtableComNo, hashtableTrueComNo, hashtableTotalSaleMoneyRatio);
      System.out.println(">>>test222:" + hashtableTotalSaleMoney.size());
      
      for (int intNo = 0; intNo < vectorProjectID1.size(); intNo++) {
        stringProjectID1 = ("" + vectorProjectID1.get(intNo)).trim();
        stringTotalSaleMoney = ("" + hashtableTotalSaleMoney.get(stringProjectID1)).trim();
        vectorComNo = (Vector) hashtableComNo.get(stringProjectID1);
        vectorTrueComNo = (Vector) hashtableTrueComNo.get(stringProjectID1);
        vectorTotalSaleMoney = (Vector) hashtableTotalSaleMoneyRatio.get(stringProjectID1);
        stringTotalSaleMoney = convert.FourToFive("" + exeUtil.doParseDouble(stringTotalSaleMoney), 0);

        // 案別H106A與H110A不要更新總銷金額
        /*
         * if ("H106A".equals(stringProjectID1) || "H110A".equals(stringProjectID1))
         * continue;
         */

        // 取得 銷況系統之總銷金額
//        String hCom = exeUtil.getNameUnion("H_Com", "A_Project", " AND  ProjectID  =  '" + stringProjectID1 + "' ", new Hashtable(), dbSale);
//        if ("".equals(hCom)) {
//          continue;
//        }
//        String lCom = exeUtil.getNameUnion("L_Com", "A_Project", " AND  ProjectID  =  '" + stringProjectID1 + "' ", new Hashtable(), dbSale1);
//        if ("".equals(lCom)) {
//          continue;
//        }
//        if (hCom.trim().equalsIgnoreCase("8") && lCom.trim().equalsIgnoreCase("8"))
//          stringTotMoney = exeUtil.getNameUnion("TotMoney", "A_Project", " AND  ProjectID  =  '" + stringProjectID1 + "' ", new Hashtable(), dbSale2);
//        else {
//          if (hCom.trim().equalsIgnoreCase("8"))
//            stringTotMoney = exeUtil.getNameUnion("H_TotMoney", "A_Project", " AND  ProjectID  =  '" + stringProjectID1 + "' ", new Hashtable(), dbSale2);
//          else if (lCom.trim().equalsIgnoreCase("8"))
//            stringTotMoney = exeUtil.getNameUnion("L_TotMoney", "A_Project", " AND  ProjectID  =  '" + stringProjectID1 + "' ", new Hashtable(), dbSale2);
//          else
//            stringTotMoney = "0";
//        }

        stringTotMoney = exeUtil.getNameUnion("TotMoney", "A_Project", " AND  ProjectID  =  '" + stringProjectID1 + "' ", new Hashtable(), dbSale);
        if ("".equals(stringTotMoney)) {
          continue;
        }

        System.out.println("..................................................................ProjectID=" + stringProjectID1 + ",stringTotMoney=" + stringTotMoney + ",stringTotalSaleMoney=" + stringTotalSaleMoney);
        stringTotMoney = "" + (exeUtil.doParseDouble(stringTotMoney) * 10000);
        stringTotMoney = convert.FourToFive(stringTotMoney, 0);
        
        // 金額一致不作處理
        if (stringTotMoney.equals(stringTotalSaleMoney)) continue;
        arrayMoney = exeUtil.getMoneyFromRatio(stringTotMoney, (String[]) vectorTotalSaleMoney.toArray(new String[0]));
        // 金額為 0 不處理
        if (arrayMoney == null) {
          continue;
        }
        System.out.println(">>>test333:" + vectorComNo.size());
        
        vectorSql = new Vector();
        for (int intNoL = 0; intNoL < vectorComNo.size(); intNoL++) {
          stringComNo = ("" + vectorComNo.get(intNoL)).trim();
          stringTotalSaleMoneyL = ("" + vectorTotalSaleMoney.get(intNoL)).trim();
          // 20180419 修改為 CS update TureTotal , 其餘放 0
          /*
           * // 修改 Doc7M0204 之總銷金額 if(vectorTrueComNo.size() == 1) {
           * hashtableData.put("TrueTotalSaleMoney", stringTotMoney) ; }
           */
          if (stringComNo != null && "CS".equals(stringComNo)) {
            hashtableData.put("TrueTotalSaleMoney", stringTotMoney);
          } else {
            hashtableData.put("TrueTotalSaleMoney", "0");
          }
          hashtableData.put("TotalSaleMoney", arrayMoney[intNoL]);
          
          hashtableAnd.put("ComNo", stringComNo);
          hashtableAnd.put("ProjectID1", stringProjectID1);
          hashtableAnd.put("FunctionType", "B");
          stringSql = exeUtil.doUpdateDB("Doc7M0204", " AND  TotalSaleMoney > 0 ", hashtableData, hashtableAnd, booleanDB, dbDoc);
          vectorSql.add(stringSql);
          System.out.println("Update Doc7M0204 SQL ==> " + stringSql);
          stringMailData += "案別(" + stringProjectID1 + ")公司(" + stringComNo + ")總銷金額原(" + stringTotalSaleMoneyL + ")後(" + arrayMoney[intNoL] + ")<br>" + stringSql + "<br>";// mail資料處理
          
          //記錄 Doc7M003
          hashtableData.put("ComNo", stringComNo);
          hashtableData.put("ProjectID1", stringProjectID1);
          hashtableData.put("FunctionType", "");
          hashtableData.put("CostRate", "0");
          hashtableData.put("TotalSaleMoneyOld", stringTotalSaleMoneyL);
          hashtableData.put("TotalSaleMoney", arrayMoney[intNoL]);
          hashtableData.put("BudgetMoneyOld", "0");
          hashtableData.put("BudgetMoney", "0");
          hashtableData.put("Remark", "行銷-案別(" + stringProjectID1 + ")-公司(" + stringComNo + ")-總銷金額設定之總銷金額同步");
          hashtableData.put("EmployeeNo", "SYS");
          hashtableData.put("EDateTime", stringTime);
          stringSql = exeUtil.doInsertDB("Doc7M003", hashtableData, booleanDB, dbDoc);
          vectorSql.add(stringSql);
          System.out.println("Insert Doc7M003 SQL ==> " + stringSql);
          stringMailData += "案別(" + stringProjectID1 + ")公司(" + stringComNo + ")總銷金額原(" + stringTotalSaleMoneyL + ")後(" + arrayMoney[intNoL] + ")LOG<br>" + stringSql
              + "<br><br><br>";// mail資料處理
        }

        // 備份
        stringSql += doBackUpData(stringProjectID1, booleanDB, exeUtil, dbDoc);

        //
        if (vectorSql.size() > 0) dbDoc.execFromPool((String[]) vectorSql.toArray(new String[0]));
      }
      
      
      // MAIL 寄送
      if (!"".equals(stringMailData)) {
        doMail("請購請款系統－" + stringFunctionName, stringMailData, exeUtil);
      } else {
        doMail("請購請款系統－" + stringFunctionName, "執行完畢", exeUtil);
      }
      //
      doTodayDoRecord(stringFunctionName, stringTodayAC, stringStartTime, longTime1, exeUtil, dbDoc);
    } catch (Exception e) {
      doMail("請購請款系統－ERROR--" + stringFunctionName, e.toString(), exeUtil);
      System.out.println(e.toString());
    } finally {
      //
      dbDoc.close();
      dbSale.close();
      dbSale1.close();
      dbSale2.close();
    }
  }

  public static String doBackUpData(String stringProjectID1, boolean booleanDB, FargloryUtil exeUtil, talk dbDoc) throws Throwable {
    String stringTableS = "Doc7M0204";
    String stringTableD = "Doc7M0204_History";
    String stringLastUser = "SYS";
    String stringLastDate = datetime.getTime("YYYY/mm/dd h:m:s");
    String stringSql = "";
    String stringSqlSUM = "";
    Vector vectorTableData = new Vector();
    Hashtable hashtableAnd = new Hashtable();
    Hashtable hashtableData = new Hashtable();
    //
    hashtableAnd.put("ProjectID1", stringProjectID1);
    vectorTableData = exeUtil.getQueryDataHashtable(stringTableS, hashtableAnd, "", new Vector(), dbDoc);
    for (int intNo = 0; intNo < vectorTableData.size(); intNo++) {
      hashtableData = (Hashtable) vectorTableData.get(intNo);
      if (hashtableData == null) continue;
      hashtableData.put("LastUser", stringLastUser);
      hashtableData.put("LastDate", stringLastDate);
      stringSql = exeUtil.doInsertDB(stringTableD, hashtableData, booleanDB, dbDoc);
      stringSqlSUM += stringSql + "<br>";
    }
    //
    return stringSqlSUM;
  }

  // MAIL
  public static void doMail(String stringTitle, String stringContent, FargloryUtil exeUtil) throws Throwable {
    doMail(stringTitle, stringContent, null, exeUtil);
  }

  public static void doMail(String stringTitle, String stringContent, String[] arrayUser, FargloryUtil exeUtil) throws Throwable {
    String stringSend = "realty@fglife.com.tw";
    String stringErrSend = "Kyle_Lee@fglife.com.tw";
    if (arrayUser == null) {
      arrayUser = new String[1];
      arrayUser[0] = stringErrSend;
    } else {
      String stringEmilTrail = "@fglife.com.tw";
      for (int intNo = 0; intNo < arrayUser.length; intNo++) {
        // 取得mail
        talk _dbDoc = new talk("mssql", "MISSQL", "doc", "docfglife", "Doc");
        String tmpSql = "SELECT EMAIL FROM USERS WHERE ID = '" + arrayUser[intNo].trim() + "'";
        System.out.println("Doc7B0204 =================> " + tmpSql);
        String[][] tmpData = _dbDoc.queryFromPool(tmpSql);
        if (tmpData[0][0] != null && tmpData[0][0].trim().length() > 0 && tmpData[0][0].trim().endsWith(stringEmilTrail)) {
          arrayUser[intNo] = tmpData[0][0].trim();
        }
      }
    }

    // exeUtil.doMail(stringTitle, stringContent, stringSend, stringErrSend, null,
    // "", arrayUser) ;
    try {
      if ((arrayUser.length != 0) && (!stringContent.trim().equals(""))) {
        String sendRS = smtp.sendMailbccUTF8("172.16.8.115", "realty@fglife.com.tw", arrayUser, stringTitle, stringContent, null, "", "text/html");
        if (sendRS.trim().equals("")) {
          System.out.println("Send mail complete !!");
        } else {
          System.out.println("Send fail!!");
        }
      }
    } catch (Exception exc1) {
      System.out.println("send fail!!");
    }
  }

  // 表格 Doc7M002
  public static boolean isTodayNotDoCheck(String stringFunctionName, String stringTodayAC, FargloryUtil exeUtil, talk dbDoc) throws Throwable {
    Vector vectorDoc7M002 = new Vector();
    Hashtable hashtableAnd = new Hashtable();
    String stringSqlAnd = "";
    String stringTodayDel = stringTodayAC.replaceAll("/", "");
    stringTodayDel = datetime.dateAdd(stringTodayDel, "d", -10);
    stringTodayDel = exeUtil.getDateConvert(stringTodayDel);
    // 刪除舊資料
    stringSqlAnd = " AND  CDateAC  <  '" + stringTodayDel + "' ";
    hashtableAnd.put("FUNC_NAME", stringFunctionName);
    exeUtil.doDeleteDB("Doc7M002", hashtableAnd, stringSqlAnd, true, dbDoc);
    //
    stringSqlAnd = " AND  (FUNC_NAME  =  'ALL'  OR  FUNC_NAME  =  '" + stringFunctionName + "') ";
    hashtableAnd.put("CDateAC", stringTodayAC);
    vectorDoc7M002 = exeUtil.getQueryDataHashtable("Doc7M002", hashtableAnd, stringSqlAnd, new Vector(), dbDoc);
    if (vectorDoc7M002.size() == 0) return false;
    return true;
  }

  public static void doTodayDoRecord(String stringFunctionName, String stringTodayAC, String stringStartTime, long longTime1, FargloryUtil exeUtil, talk dbDoc) throws Throwable {
    long longTime2 = exeUtil.getTimeInMillis();
    String stringEndTime = datetime.getTime("YYYY/mm/dd h:m:s");
    String stringTimeUse = "" + ((longTime2 - longTime1) / 1000);
    Hashtable hashtableData = new Hashtable();
    hashtableData.put("FUNC_NAME", stringFunctionName);
    hashtableData.put("CDateAC", stringTodayAC);
    hashtableData.put("StartTime", stringStartTime);
    hashtableData.put("EndTime", stringEndTime);
    hashtableData.put("TimeUse", stringTimeUse);
    exeUtil.doInsertDB("Doc7M002", hashtableData, true, dbDoc);
  }

  // 資料庫 Doc
  // 表格 Doc3M011_EmployeeNo
  public static Vector getDoc3M011EmployeeNo(String stringFunctionType, String stringSqlAnd, FargloryUtil exeUtil, talk dbDoc, Vector vectorDoc3M011) throws Throwable {
    String stringEmpNo = "";
    Vector vectorEmpNo = new Vector();
    Hashtable hashtableAnd = new Hashtable();
    Hashtable hashtableDoc3M011 = new Hashtable();
    //
    hashtableAnd.put("FunctionType", stringFunctionType);
    vectorDoc3M011 = exeUtil.getQueryDataHashtable("Doc3M011_EmployeeNo", hashtableAnd, stringSqlAnd, new Vector(), dbDoc);
    for (int intNo = 0; intNo < vectorDoc3M011.size(); intNo++) {
      hashtableDoc3M011 = (Hashtable) vectorDoc3M011.get(intNo);
      if (hashtableDoc3M011 == null) continue;
      stringEmpNo = "" + hashtableDoc3M011.get("EmployeeNo");
      //
      if (vectorEmpNo.indexOf(stringEmpNo) == -1) vectorEmpNo.add(stringEmpNo);
    }
    return vectorEmpNo;
  }

  // 表格 Doc7M0204
  public static void doDoc7M0204(FargloryUtil exeUtil, talk dbDoc, Vector vectorProjectID1, Hashtable hashtableTotalSaleMoney, Hashtable hashtableComNo,
      Hashtable hashtableTrueComNo, Hashtable hashtableTotalSaleMoneyRatio) throws Throwable {
    String stringSqlAnd = " ORDER BY  ProjectID1 ";
    String stringProjectID1 = "";
    String stringComNo = "";
    String stringTotalSaleMoney = "";
    String stringTrueTotalSaleMoney = "";
    String stringTotalSaleControl = "";
    Vector vectorDoc7M0204 = new Vector();
    Vector vectorProjectID1L = new Vector();
    Vector vectorComNo = new Vector();
    Vector vectorTotalSaleMoney = new Vector();
    Hashtable hashtableAnd = new Hashtable();
    Hashtable hashtableDoc7M0204 = new Hashtable();
    Hashtable hashtableTotalSaleMoneyL = new Hashtable();
    Hashtable hashtableTotalSaleControl = new Hashtable();
    double doubleTemp = 0;
    //
    vectorDoc7M0204 = exeUtil.getQueryDataHashtable("Doc7M0204", hashtableAnd, stringSqlAnd, new Vector(), dbDoc);
    for (int intNo = 0; intNo < vectorDoc7M0204.size(); intNo++) {
      hashtableDoc7M0204 = (Hashtable) vectorDoc7M0204.get(intNo);
      if (hashtableDoc7M0204 == null) continue;
      stringProjectID1 = "" + hashtableDoc7M0204.get("ProjectID1");
      stringComNo = "" + hashtableDoc7M0204.get("ComNo");
      stringTotalSaleMoney = "" + hashtableDoc7M0204.get("TotalSaleMoney");
      stringTrueTotalSaleMoney = "" + hashtableDoc7M0204.get("TrueTotalSaleMoney");
      stringTotalSaleControl = "" + hashtableDoc7M0204.get("TotalSaleControl");
      // 案別
      if (vectorProjectID1L.indexOf(stringProjectID1) == -1) vectorProjectID1L.add(stringProjectID1);
      // 公司
      if (exeUtil.doParseDouble(stringTotalSaleMoney) > 0) {
        vectorComNo = (Vector) hashtableComNo.get(stringProjectID1);
        vectorTotalSaleMoney = (Vector) hashtableTotalSaleMoneyRatio.get(stringProjectID1);//
        if (vectorComNo == null) {
          vectorComNo = new Vector();
          vectorTotalSaleMoney = new Vector();
          hashtableComNo.put(stringProjectID1, vectorComNo);
          hashtableTotalSaleMoneyRatio.put(stringProjectID1, vectorTotalSaleMoney);
        }
        if (vectorComNo.indexOf(stringComNo) == -1) {
          vectorComNo.add(stringComNo);
          vectorTotalSaleMoney.add(stringTotalSaleMoney);
        }
      }
      if (exeUtil.doParseDouble(stringTrueTotalSaleMoney) > 0) {
        vectorComNo = (Vector) hashtableTrueComNo.get(stringProjectID1);
        if (vectorComNo == null) {
          vectorComNo = new Vector();
          vectorTotalSaleMoney = new Vector();
          hashtableTrueComNo.put(stringProjectID1, vectorComNo);
        }
        if (vectorComNo.indexOf(stringComNo) == -1) {
          vectorComNo.add(stringComNo);
        }
      }
      // 總銷金額 合計
      doubleTemp = exeUtil.doParseDouble(stringTotalSaleMoney) + exeUtil.doParseDouble("" + hashtableTotalSaleMoneyL.get(stringProjectID1));
      hashtableTotalSaleMoneyL.put(stringProjectID1, convert.FourToFive("" + doubleTemp, 0));
      // 真實總銷 合計
      doubleTemp = exeUtil.doParseDouble(stringTrueTotalSaleMoney) + exeUtil.doParseDouble("" + hashtableTotalSaleMoneyL.get(stringProjectID1 + "%-%TRUE"));
      hashtableTotalSaleMoneyL.put(stringProjectID1 + "%-%TRUE", convert.FourToFive("" + doubleTemp, 0));
      // 維護權限
      hashtableTotalSaleControl.put(stringProjectID1, stringTotalSaleControl);
    }
    boolean booleanOK = true;
    for (int intNo = 0; intNo < vectorProjectID1L.size(); intNo++) {
      stringProjectID1 = "" + vectorProjectID1L.get(intNo);
      stringTotalSaleControl = "" + hashtableTotalSaleControl.get(stringProjectID1);
      booleanOK = false;
      // 已確認
      if (vectorProjectID1.indexOf(stringProjectID1) != -1) booleanOK = true;
      // 單一業主別 OK
      vectorComNo = (Vector) hashtableComNo.get(stringProjectID1);
      if (vectorComNo == null) continue;
      if (vectorComNo.size() == 1) booleanOK = true;
      // 維護權限 B OK( A : 財務, B : 行銷 )
      if ("A".equals(stringTotalSaleControl)) booleanOK = true;
      //
      if (!booleanOK) continue;
      //
      hashtableTotalSaleMoney.put(stringProjectID1, "" + hashtableTotalSaleMoneyL.get(stringProjectID1));
      //
      if (vectorProjectID1.indexOf(stringProjectID1) == -1) vectorProjectID1.add(stringProjectID1);
    }
  }
}
