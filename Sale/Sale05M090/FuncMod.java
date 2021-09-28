package Sale.Sale05M090;

import jcx.jform.bTransaction;
import java.io.*;
import java.util.*;
import jcx.util.*;
import jcx.html.*;
import jcx.db.*;
import javax.swing.*;

import org.apache.commons.lang.StringUtils;

import Farglory.util.*;

public class FuncMod extends bTransaction {
  KSqlUtils ksUtil;
  KUtils kUtil;
  String custTable = "table1";
  String benTable = "table6";
  String angetTable = "table10";

  public boolean action(String value) throws Throwable {
    System.out.println("chk==>" + getUser() + " , value==>修改");

    if (getUser() != null && getUser().toUpperCase().equals("B9999")) {
      messagebox("修改權限不允許!!!");
      return false;
    }

    getButton("ButtonSSMediaID").doClick();
    getButton("ButtonSetSaleID").doClick();
    //
    System.out.println("修改------------------------------------S");
    JTable jtableTable1 = getTable(custTable);
    if (jtableTable1.getRowCount() == 0) {
      message("[客戶資料] 不可空白");
      return false;
    }

    ksUtil = new KSqlUtils();
    kUtil = new KUtils();
    talk dbSale = getTalk((String) get("put_dbSale"));
    talk dbDoc = getTalk("Doc");
    JTabbedPane jtabbedpane1 = getTabbedPane("tab1");
    float floatPercentage = 0;
    String stringSql = "";
    String stringSaleWay = getValue("SaleWay").trim();
    String stringProjectID1 = getValue("field1").trim();
    String stringOrderNo = getValue("OrderNo").trim();
    String stringTrxDate = getValue("field2").trim();

    // 欄位檢核
    JTable jtable6 = getTable(benTable);
    JTable jTable10 = getTable(angetTable);

    // 洗錢檢核用
    String orderNo = getValue("field3").trim(); //不知道另外一個orderNo甚麼時候有  用這個卡實在
    int relatedCount = jtableTable1.getRowCount() + jtable6.getRowCount() + jTable10.getRowCount();
    String[][] arrAMLs = new String[relatedCount][13];
    String errMsg = "";
    String funcName = getFunctionName().trim();
    int amlCount = 0;

    for (int intRow = 0; intRow < jtableTable1.getRowCount(); intRow++) {
      String stringCustomNo = ("" + getValueAt(custTable, intRow, "CustomNo")).trim();
      String stringCustomName = ("" + getValueAt(custTable, intRow, "CustomName")).trim();
      String stringPercentage = ("" + getValueAt(custTable, intRow, "Percentage")).trim();
      String stringNationality = ("" + getValueAt(custTable, intRow, "Nationality")).trim(); // 20090414
      String stringAddress = ("" + getValueAt(custTable, intRow, "Address")).trim();
      String stringTel = ("" + getValueAt(custTable, intRow, "Tel")).trim();
      String stringTel2 = ("" + getValueAt(custTable, intRow, "Tel2")).trim();// 2010-4-16 新增電話2
      String stringStatusCd = ("" + getValueAt(custTable, intRow, "StatusCd")).trim();
      String stringCity = ("" + getValueAt(custTable, intRow, "City")).trim();
      String stringTown = ("" + getValueAt(custTable, intRow, "Town")).trim();
      String stringZIP = ("" + getValueAt(custTable, intRow, "ZIP")).trim();
      String stringCellphone = ("" + getValueAt(custTable, intRow, "Cellphone")).trim();
      String isBlackList = ("" + getValueAt(custTable, intRow, "IsBlackList")).trim();
      String isControlList = ("" + getValueAt(custTable, intRow, "IsControlList")).trim();
      String isLinked = ("" + getValueAt(custTable, intRow, "IsLinked")).trim();
      String stringBirthday = ("" + getValueAt(custTable, intRow, "Birthday")).trim();
      String majorName = ("" + getValueAt(custTable, intRow, "MajorName")).trim();
      String stringCountryName = ("" + getValueAt(custTable, intRow, "CountryName")).trim();
      String countryName2 = ("" + getValueAt(custTable, intRow, "CountryName2")).trim();
      String engName = ("" + getValueAt(custTable, intRow, "EngName")).trim();
      String engNo = ("" + getValueAt(custTable, intRow, "EngNo")).trim();
      String recordType = "客戶資料";
      
      if(StringUtils.isBlank(stringCustomNo)) stringCustomNo = engNo;
      if(StringUtils.isBlank(stringCustomName)) stringCustomName = engName;

      // customNo
      if ("1".equals(stringNationality) && stringCustomNo.length() == 0) {// 20090414
        message("筆數:" + (intRow + 1) + "-[統編/身分證號] 不可空白");
        return false;
      }

      // customName
      if (stringCustomName.length() == 0) {
        message("筆數:" + (intRow + 1) + "-[訂戶姓名] 不可空白");
        return false;
      }

      if ("".equals(stringCountryName)) {
        messagebox("第 " + (intRow + 1) + " 列之[國別] 必須輪入。");
        jtableTable1.setRowSelectionInterval(intRow, intRow);
        return false;
      }

      if (!"".equals(stringBirthday) && stringBirthday.replace("/", "").length() != 8) {
        messagebox("第 " + (intRow + 1) + " 列之[生日/註冊日] 必須輪入。");
        jtableTable1.setRowSelectionInterval(intRow, intRow);
        return false;
      }

      if (!"".equals(stringCellphone) && stringCellphone.length() != 10) {
        messagebox("第 " + (intRow + 1) + " 列之[行動電話] 大小須為 10 碼。");
        jtableTable1.setRowSelectionInterval(intRow, intRow);
        return false;
      }
      //
      if ("1".equals(stringNationality)) {
        if (stringCustomNo.length() == 0) {
          messagebox("第 " + (intRow + 1) + " 列之[統編/身分證號] 不可空白!。");
          jtableTable1.setRowSelectionInterval(intRow, intRow);
          return false;
        }
        if (stringCustomNo.length() != 8 && stringCustomNo.length() != 10) {
          messagebox("第 " + (intRow + 1) + " 列之[統編/身分證號] 長度錯誤!(本國人)。");
          jtableTable1.setRowSelectionInterval(intRow, intRow);
          return false;
        }
        if (stringCustomNo.length() == 8 && check.isCoId(stringCustomNo) == false) {
          messagebox("第 " + (intRow + 1) + " 列之[統編/身分證號] 統一編號錯誤!。");
          jtableTable1.setRowSelectionInterval(intRow, intRow);
          return false;
        }
        if (stringCustomNo.length() == 10 && check.isID(stringCustomNo) == false) {
          messagebox("第 " + (intRow + 1) + " 列之[統編/身分證號] 身分證號錯誤!。");
          jtableTable1.setRowSelectionInterval(intRow, intRow);
          return false;
        }
      }
      if ("4".equals(stringNationality)) {
        if (stringCustomNo.length() != 9) {
          messagebox("第 " + (intRow + 1) + " 列之[統編/身分證號] 長度錯誤!。");
          jtableTable1.setRowSelectionInterval(intRow, intRow);
          return false;
        }
      }

      stringSql = " SELECT  ZIP FROM  Town b WHERE  Coun   IN  (SELECT  Coun  FROM  City  WHERE  CounName='" + stringCity + "') " + "AND  TownName  =  '" + stringTown + "' ";
      String[][] retTown = dbDoc.queryFromPool(stringSql);
      if (retTown.length == 0) {
        message("第 " + (intRow + 1) + " 列之[縣市][鄉鎮] 關係不正確。");
        jtableTable1.setRowSelectionInterval(intRow, intRow);
        return false;
      }
      if (!stringZIP.equals(retTown[0][0].trim())) {
        if (stringZIP.length() > 3) stringZIP = stringZIP.substring(0, 3);
        if (!stringZIP.equals(retTown[0][0].trim())) {
          messagebox("第 " + (intRow + 1) + " 列之[郵遞區號] 不正確。");
          jtableTable1.setRowSelectionInterval(intRow, intRow);
          return false;
        }
      }

      // [比例%]
      if (stringPercentage.length() == 0) {
        message("第 " + (intRow + 1) + " 列之[比例%] 不可空白");
        return false;
      }
      if (Float.parseFloat(stringPercentage.trim()) < 1) {
        message("第 " + (intRow + 1) + " 列之[比例%] 不可小於 1");
        return false;
      }
      if (!stringStatusCd.equals("C")) floatPercentage = floatPercentage + Float.parseFloat(stringPercentage);
      //
      if (stringAddress.length() == 0) {
        message("第 " + (intRow + 1) + " 列之[地址] 不可空白");
        return false;
      }
      //
      if (stringTel.length() == 0) {
        message("第 " + (intRow + 1) + " 列之[電話] 不可空白");
        return false;
      }

      // 洗錢檢核
      String indCode = ksUtil.getIndustryCodeByMajorName(majorName);
      String amlText = stringProjectID1 + "," + orderNo + "," + stringTrxDate + "," + funcName + "," + recordType + "," + stringCustomNo + "," + stringCustomName + ","
          + stringBirthday + "," + indCode + "," + stringCountryName + "," + countryName2 + "," + engName + "," + "query18";
      arrAMLs[amlCount] = amlText.split(",");
      amlCount++;
    }

    //
    if (floatPercentage != 100) {
      message("[比例%] 必須為 100");
      return false;
    }

    // 檢核-實質受益人 ========================================
    System.out.println("檢核-=====>");
    for (int intNo = 0; intNo < jtable6.getRowCount(); intNo++) {
      String strBCustomNo = ("" + getValueAt(benTable, intNo, "BCustomNo")).trim();
      String strBenName = ("" + getValueAt(benTable, intNo, "BenName")).trim();
      String strCountryName = ("" + getValueAt(benTable, intNo, "CountryName")).trim();
      String birthday = ("" + getValueAt(benTable, intNo, "Birthday")).trim();
      String recordType = "實質受益人資料";

      if ("".equals(strBCustomNo)) {
        jtabbedpane1.setSelectedIndex(2);
        jtable6.setRowSelectionInterval(intNo, intNo);
        messagebox("[實質受益人表格] 第 " + (intNo + 1) + " 列之 [受益人姓名] 不可為空白。");
        return false;
      }
      if ("".equals(strBenName)) {
        jtabbedpane1.setSelectedIndex(2);
        jtable6.setRowSelectionInterval(intNo, intNo);
        messagebox("[實質受益人表格] 第 " + (intNo + 1) + " 列之 [身分證號] 不可為空白。");
        return false;
      }
      if ("".equals(strCountryName)) {
        jtabbedpane1.setSelectedIndex(2);
        jtable6.setRowSelectionInterval(intNo, intNo);
        messagebox("[實質受益人表格] 第 " + (intNo + 1) + " 列之 [國別] 不可為空白。");
        return false;
      }

      // 洗錢檢核
      QueryLogBean qBean = ksUtil.getQueryLogByCustNoProjectId(stringProjectID1, strBCustomNo);
      if (qBean == null) {
        messagebox("查無[實質受益人表格] 第 " + (intNo + 1) + " 列之黑名單資料，請先執行查詢。");
        return false;
      }

      String indCode = qBean.getJobType();
      String countryName2 = ksUtil.getCountryNameByNationCode(qBean.getNtCode2());
      String engName = qBean.getEngName();
      String amlText = stringProjectID1 + "," + orderNo + "," + stringTrxDate + "," + funcName + "," + recordType + "," + strBCustomNo + "," + strBenName + "," + birthday
          + "," + indCode + "," + strCountryName + "," + countryName2 + "," + engName + "," + "query18";
      arrAMLs[amlCount] = amlText.split(",");
      amlCount++;
    }

    // 檢核-代理人
    System.out.println("檢核-代理人=====>");
    for (int c = 0; c < jTable10.getRowCount(); c++) {
      String custNo = ("" + getValueAt(angetTable, c, "ACustomNo")).trim();
      String custName = ("" + getValueAt(angetTable, c, "AgentName")).trim();
      String countryName = ("" + getValueAt(angetTable, c, "CountryName")).trim();
      String recordType = "代理人資料";

      // 洗錢檢核
      QueryLogBean qBean = ksUtil.getQueryLogByCustNoProjectId(stringProjectID1, custNo);
      if (qBean == null) {
        messagebox("查無[代理人表格] 第 " + (c + 1) + " 列之黑名單資料，請先執行查詢。");
        return false;
      }

      String indCode = qBean.getJobType();
      String birthday = qBean.getBirthday();
      String countryName2 = ksUtil.getCountryNameByNationCode(qBean.getNtCode2());
      String engName = qBean.getEngName();
      String amlText = stringProjectID1 + "," + orderNo + "," + stringTrxDate + "," + funcName + "," + recordType + "," + custNo + "," + custName + "," + birthday + ","
          + indCode + "," + countryName + "," + countryName2 + "," + engName + "," + "query18";
      arrAMLs[amlCount] = amlText.split(",");
      amlCount++;
    }

    // 洗錢檢核 >> 執行
    setTableData("TableCheckAML", arrAMLs);

    getButton("ArrCheckAML").doClick();
    errMsg = getValue("AMLText").toString().trim();
    if (StringUtils.isNotBlank(errMsg)) {
      System.out.println(">>>msg:" + errMsg);
      messagebox(errMsg);
      getButton("sendMail").doClick();
      return false;
    }

    //
    JTable jtableTable2 = getTable("table2");
    if (jtableTable2.getRowCount() == 0) {
      message("[戶別資料] 不可空白");
      return false;
    }
    for (int intRow = 0; intRow < jtableTable2.getRowCount(); intRow++) {
      // [棟樓別]
      String stringPosition = jtableTable2.getValueAt(intRow, 3).toString();
      if (stringPosition.length() == 0) {
        message("第 " + (intRow + 1) + " 列之[棟樓別] 不可空白");
        jtableTable2.setRowSelectionInterval(intRow, intRow);
        jtabbedpane1.setSelectedIndex(0);
        return false;
      }
      //
      if (jtableTable2.getValueAt(intRow, 2).toString().length() == 0) {
        message("第 " + (intRow + 1) + " 列之[房車] 不可空白");
        jtableTable2.setRowSelectionInterval(intRow, intRow);
        jtabbedpane1.setSelectedIndex(0);
        return false;
      }
      //
      if (jtableTable2.getValueAt(intRow, 4).toString().length() == 0) {
        message("第 " + (intRow + 1) + " 列之[坪數] 不可空白");
        jtableTable2.setRowSelectionInterval(intRow, intRow);
        jtabbedpane1.setSelectedIndex(0);
        return false;
      }
      //
      if (jtableTable2.getValueAt(intRow, 5).toString().length() == 0) {
        message("第 " + (intRow + 1) + " 列之[牌價] 不可空白");
        jtableTable2.setRowSelectionInterval(intRow, intRow);
        jtabbedpane1.setSelectedIndex(0);
        return false;
      }
      //
      if (jtableTable2.getValueAt(intRow, 6).toString().length() == 0) {
        message("第 " + (intRow + 1) + " 列之[售價] 不可空白");
        jtableTable2.setRowSelectionInterval(intRow, intRow);
        jtabbedpane1.setSelectedIndex(0);
        return false;
      }
      // put("put_OrderNo",getValue("field3").trim());
      setValue("OrderNo", getValue("field3").trim());
    }

    JTable jtableTable7 = getTable("table7");
    String stringOrderNoBonus = "";
    for (int intNo = 0; intNo < jtableTable7.getRowCount(); intNo++) {
      stringOrderNoBonus = ("" + getValueAt("table7", intNo, "OrderNoBonus")).trim();
      //
      stringSql = "SELECT  OrderNo  FROM  Sale05M092  WHERE  ISNULL(StatusCd,'')  = ''  AND  OrderNo  =  '" + stringOrderNoBonus + "' ";
      if (dbSale.queryFromPool(stringSql).length == 0) {
        jtabbedpane1.setSelectedIndex(5);
        jtableTable7.setRowSelectionInterval(intNo, intNo);
        messagebox("銷獎專用表格 第 " + (intNo + 1) + " 列之 [使用編號] 不存在。");
        return false;
      }
    }
    // 行銷策略 B3018 2012/09/17 S
    FargloryUtil exeUtil = new FargloryUtil();
    String stringDate = exeUtil.getDateConvert(getValue("field2").trim());
    String[][] retSale05M246 = null;
    if ("".equals(stringProjectID1)) {
      messagebox("[案別] 不可為空白。");
      getcLabel("field1").requestFocus();
      return false;
    }
    if (stringDate.length() != 10) {
      messagebox("[日期]日期格式錯誤(YYYY/MM/DD)。");
      getcLabel("field2").requestFocus();
      return false;
    }
    if (!"".equals(stringSaleWay)) {
      // 存在檢核
      stringSql = "SELECT  Num,  PlanDateS,  PlanDateE " + " FROM  Sale05M246  " + " WHERE  ProjectID1  =  '" + stringProjectID1 + "' " + " AND  StrategyNo  =  '" + stringSaleWay
          + "' " + " AND  PlanDateS  <=  '" + stringDate + "' " + " AND  PlanDateE  >=  '" + stringDate + "' ";
      retSale05M246 = dbSale.queryFromPool(stringSql);
      if (retSale05M246.length == 0) {
        messagebox("[行銷策略]資料錯誤。");
        getcLabel("SaleWay").requestFocus();
        return false;
      }
      // 數量檢核
      double doubleNum = exeUtil.doParseDouble(retSale05M246[0][0].trim());
      String stringPlanDateS = retSale05M246[0][1].trim();
      String stringPlanDateE = retSale05M246[0][2].trim();
      if (doubleNum > 0) {
        stringSql = "SELECT  ProjectID1 " + " FROM  Sale05M090 " + " WHERE  ProjectID1  =  '" + stringProjectID1 + "' " + " AND  SaleWay  =  '" + stringSaleWay + "' "
            + " AND  OrderNo  <>  '" + stringOrderNo + "' " + " AND  OrderDate  >=  '" + stringPlanDateS + "' " + " AND  OrderDate  <=  '" + stringPlanDateE + "' ";
        retSale05M246 = dbSale.queryFromPool(stringSql);
        if (exeUtil.doParseDouble("" + (retSale05M246.length + 1)) > doubleNum) {
          String stringStrategyName = exeUtil.getNameUnion("StrategyName", "Sale05M244", " AND  StrategyNo  = '" + stringSaleWay + "' ", new Hashtable(), dbSale);
          messagebox("購屋證明單筆數超過行銷策略(" + stringStrategyName + ")所設定的" + convert.FourToFive("" + doubleNum, 0) + " 筆的數量。");
          getcLabel("SaleWay").requestFocus();
          return false;
        }
      }
    }
    // 行銷策略 B3018 2012/09/17 E
    // 媒體代碼檢核 楊信義 2010/05/25 S
    String stringSSMediaID = getValue("SSMediaID").trim();
    String stringSSMediaID1 = getValue("SSMediaID1").trim();
    if (!"H601A".equals(stringProjectID1)) { // 修改日期:20170815 員工編號:B3774
      if ("".equals(stringSSMediaID)) {
        // messagebox("[媒體代碼] 不可為空白。") ;
        // return false ;
      } else {
        String[][] retMediaSS = dbSale.queryFromPool(" SELECT  SSMediaName  FROM  Media_SS  WHERE  SSMediaID=  '" + stringSSMediaID + "'  AND  Stop  =  'N' ");
        if (retMediaSS.length == 0) {
          messagebox("[媒體代碼] 不存在資料庫中。");
          return false;
        }
      }
    } // 修改日期:20170815 員工編號:B3774
    // 2015-12-10 B3018 售出人檢核 S
    JTable jtable9 = getTable("table9");
    JTabbedPane jTabbedPane1 = getTabbedPane("tab1");
    String stringSaleID1 = "";
    String stringZ6SaleID2 = "";
    String stringCSSaleID2 = "";
    String stringSaleName1 = "";
    String stringZ6SaleName2 = "";
    String stringCSSaleName2 = "";
    boolean booleanCheck = "2016/01/01".compareTo(getValue("field2").trim()) < 0;
    if (booleanCheck && jtable9.getRowCount() <= 0) {
      jTabbedPane1.setSelectedIndex(1);
      messagebox("[售出人表格] 不可無資料。");
      return false;
    }
    for (int intNo = 0; intNo < jtable9.getRowCount(); intNo++) {
      stringSaleID1 = ("" + getValueAt("table9", intNo, "SaleID1")).trim();
      stringSaleName1 = ("" + getValueAt("table9", intNo, "SaleName1")).trim();
      stringZ6SaleID2 = ("" + getValueAt("table9", intNo, "Z6SaleID2")).trim();
      stringZ6SaleName2 = ("" + getValueAt("table9", intNo, "Z6SaleName2")).trim();
      stringCSSaleID2 = ("" + getValueAt("table9", intNo, "CSSaleID2")).trim();
      stringCSSaleName2 = ("" + getValueAt("table9", intNo, "CSSaleName2")).trim();
      //
      if (!booleanCheck) continue;
      //
      if ("".equals(stringSaleID1)) {
        jTabbedPane1.setSelectedIndex(1);
        jtable9.setRowSelectionInterval(intNo, intNo);
        messagebox("[售出人表格] 第 " + (intNo + 1) + " 列之 [銷售(實際)-員編] 不可為空白。");
        return false;
      }
      if ("".equals(stringSaleName1)) {
        jTabbedPane1.setSelectedIndex(1);
        jtable9.setRowSelectionInterval(intNo, intNo);
        messagebox("[售出人表格] 第 " + (intNo + 1) + " 列之 [銷售(實際)-售出人] 不可為空白。");
        return false;
      }
      if ("H601A".equals(stringProjectID1)) continue; // 修改日期:20170815 員工編號:B3774

      if ("".equals(stringCSSaleID2)) {
        jTabbedPane1.setSelectedIndex(1);
        jtable9.setRowSelectionInterval(intNo, intNo);
        messagebox("[售出人表格] 第 " + (intNo + 1) + " 列之 [遠雄人壽-員編] 不可為空白。");
        return false;
      }
      if (!"".equals(stringCSSaleID2) && "".equals(stringCSSaleName2)) {
        jTabbedPane1.setSelectedIndex(1);
        jtable9.setRowSelectionInterval(intNo, intNo);
        messagebox("[售出人表格] 第 " + (intNo + 1) + " 列之 [遠雄人壽-售出人] 不可為空白。");
        return false;
      }
    }

    JTable jtable3 = getTable("table3");
    String stringQty = "";
    String stringTotalAmt = "";
    String stringItemNo = "";
    for (int intNo = 0; intNo < jtable3.getRowCount(); intNo++) {
      stringQty = ("" + getValueAt("table3", intNo, "Qty")).trim();
      stringTotalAmt = ("" + getValueAt("table3", intNo, "TotalAmt")).trim();
      stringItemNo = ("" + getValueAt("table3", intNo, "ItemNo")).trim();
      //
      if (!stringItemNo.startsWith("Y")) continue;
      //
      if (exeUtil.doParseDouble(stringTotalAmt) > 0 && exeUtil.doParseDouble(stringQty) == 0) {
        messagebox("[贈送表格] 第 " + (intNo + 1) + " 列之 [數量] 不可為空白。");
        return false;
      }
    }
    // 2015-12-10 B3018 售出人檢核 E
    // 媒體代碼檢核 2010/05/25 E

    JTable jtable12 = getTable("table12");
    String stringComLoadMoney = getValue("ComLoadMoney").trim();
    String stringComNo = "";
    String stringComLoadDate = "";
    String stringPrincipalAmt = "";
    String stringInterestAmt = "";
    String stringInterestKind = "";
    String stringSqlAnd = "";
    double doublePrincipalAmt = 0;
    double doubleComLoadMoney = 0;
    Vector vectorACom = null;
    for (int intNo = 0; intNo < jtable12.getRowCount(); intNo++) {
      stringComNo = ("" + getValueAt("table12", intNo, "Com_No")).trim();
      stringComLoadDate = ("" + getValueAt("table12", intNo, "ComLoadDate")).trim();
      stringPrincipalAmt = ("" + getValueAt("table12", intNo, "PrincipalAmt")).trim();
      stringInterestAmt = ("" + getValueAt("table12", intNo, "InterestAmt")).trim();
      stringInterestKind = ("" + getValueAt("table12", intNo, "InterestKind")).trim();
      // 業主別
      if ("".equals(stringComNo)) {
        jtable12.setRowSelectionInterval(intNo, intNo);
        jtabbedpane1.setSelectedIndex(3);
        messagebox("公司貸表格第 " + (intNo + 1) + " 行之 [業主別] 不可為空白。");
        return false;
      }
      stringSqlAnd = " AND  ISNULL(COMPANY_CD,'')  <>  ''  " + " AND  Com_No  =  '" + stringComNo + "' " + " AND Com_No IN (SELECT  distinct H_COM " + " FROM  Sale05M040 "
          + " WHERE  ProjectID1  =  '" + stringProjectID1 + "' " + " UNION " + " SELECT  distinct  L_COM " + " FROM  Sale05M040 " + " WHERE  ProjectID1  =  '" + stringProjectID1
          + "' " + " ) ";
      vectorACom = exeUtil.getQueryDataHashtable("A_Com", new Hashtable(), stringSqlAnd, dbSale);
      if (vectorACom.size() == 0) {
        jtable12.setRowSelectionInterval(intNo, intNo);
        jtabbedpane1.setSelectedIndex(3);
        messagebox("公司貸表格第 " + (intNo + 1) + " 行之 [業主別] 不存在資料庫中。");
        return false;
      }
      // 公司貸期別
      if ("".equals(stringComLoadDate)) {
        jtable12.setRowSelectionInterval(intNo, intNo);
        jtabbedpane1.setSelectedIndex(3);
        messagebox("公司貸表格第 " + (intNo + 1) + " 行之 [公司貸期別] 不可為空白。");
        return false;
      }
      stringComLoadDate = exeUtil.getDateAC(stringComLoadDate, "公司貸期別");
      if (stringComLoadDate.length() != 10) {
        jtable12.setRowSelectionInterval(intNo, intNo);
        jtabbedpane1.setSelectedIndex(3);
        messagebox("公司貸表格第 " + (intNo + 1) + " 行之 [公司貸期別] 日期格式(YYYY/mm/dd)錯誤。");
        return false;
      }
      // 各期本金金額
      if (exeUtil.doParseDouble(stringPrincipalAmt) <= 0) {
        jtable12.setRowSelectionInterval(intNo, intNo);
        jtabbedpane1.setSelectedIndex(3);
        messagebox("公司貸表格第 " + (intNo + 1) + " 行之 [各期本金金額] 不可為 0。");
        return false;
      }
      doublePrincipalAmt += exeUtil.doParseDouble(stringPrincipalAmt);

      // 利息支付方式
      if ("".equals(stringInterestKind)) {
        jtable12.setRowSelectionInterval(intNo, intNo);
        jtabbedpane1.setSelectedIndex(3);
        messagebox("公司貸表格第 " + (intNo + 1) + " 行之 [利息支付方式] 不可為空白。");
        return false;
      }
    }
    // 檢核公司貸總額 要等於各期本金金額 加總
    doublePrincipalAmt = exeUtil.doParseDouble(convert.FourToFive("" + doublePrincipalAmt, 4));
    doubleComLoadMoney = exeUtil.doParseDouble(convert.FourToFive(stringComLoadMoney, 4));
    if (doublePrincipalAmt != doubleComLoadMoney) {
      jtabbedpane1.setSelectedIndex(3);
      getcLabel("ComLoadMoney").requestFocus();
      messagebox("[公司貸總額] 不等於 公司貸表格之各期本金金額 加總。");
      return false;
    }
    if ("B3018".equals(getUser())) {
      messagebox("測試");
      return false;
    }
    message("");
    put("TrustAccountNo", value);
    getButton("ButtonTrustAccountNo").doClick();
    setValue("actionText", "修改");

    // 檢核法人-受益人關係正確
    if (this.checkHasBen(jtableTable1, jtable6) == false) return false;

    // 檢查受益人欄位
    // 20210610 Kyle update : 改為使用萊斯系統
    getButton("CheckBensAML18").doClick();
    String amlText = getValue("AMLText").trim();

    System.out.println(">>>CheckBensAML18 amlText:" + amlText);
    if (StringUtils.isNotBlank(getValue("AMLText"))) return false;

   // 更新實質受益人表
    put("UpdBen_RS", "N");
    getButton("updateBen").doClick();
    if ( !StringUtils.equals(get("UpdBen_RS").toString().trim(), "Y") ) return false;
    System.out.println("updateBen=====> Done");
    // 更新受益人資訊 end

    System.out.println("修改------------------------------------E");
    return true;
  }

  // 檢查法人與受益人MATCH
  public boolean checkHasBen(JTable customs, JTable bCustoms) throws Throwable {
    System.out.println(">>>檢查法人是否有受益人<<<");

    for (int idx1 = 0; idx1 < customs.getRowCount(); idx1++) {
      String cNo = getValueAt(custTable, idx1, "CustomNo").toString().trim();
      if(StringUtils.isBlank(cNo)) {
        cNo = getValueAt(custTable, idx1, "EngNo").toString().trim();
      }
      
      String cName = getValueAt(custTable, idx1, "CustomName").toString().trim();
      if(StringUtils.isBlank(cName)) {
        cName = getValueAt(custTable, idx1, "EngName").toString().trim();
      }
      
      String cStatus = getValueAt(custTable, idx1, "StatusCd").toString().trim();
      if (kUtil.isCusCompany(cNo) && !"C".equals(cStatus)) {
        // System.out.println(">>>法人!!<<<") ;
        boolean chkHas = false;
        for (int idx2 = 0; idx2 < bCustoms.getRowCount(); idx2++) {
          String bcNo = getValueAt(benTable, idx2, "CustomNo").toString().trim();
          String bcStatus = getValueAt(benTable, idx2, "StatusCd").toString().trim();
          if (bcNo.equals(cNo) && !"C".equals(bcStatus)) {
            chkHas = true;
            break;
          }
        }
        if (chkHas == false) {
          messagebox("請指定法人 [" + cName + "]  之有效實質受益人。");
          return false; // 受益人清單中找不到對應ID
        }
      }
    }
    return true;
  }

  public String getInformation() {
    return "---------------\u4fee\u6539\u6309\u9215\u7a0b\u5f0f.preProcess()----------------";
  }

}
