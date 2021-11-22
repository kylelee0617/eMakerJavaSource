package Doc.Doc3.Doc3R050;

import java.io.File;
import java.util.Hashtable;
import java.util.Vector;

import com.jacob.com.Dispatch;

import Farglory.util.FargloryUtil;
import jcx.db.talk;
import jcx.jform.bproc;
import jcx.util.convert;
import jcx.util.datetime;

public class ExportExcel extends bproc {
  public String getDefaultValue(String value) throws Throwable {
    FargloryUtil exeUtil = new FargloryUtil();
    String stringDateStart = getValue("DateStart").trim();
    String stringDateEnd = getValue("DateEnd").trim();
    String stringProjectID1 = getValue("ProjectID1").trim();
    //
    if ("".equals(stringDateStart)) {
      message("[請購日期(起)] 不可為空白");
      return value;
    }
    stringDateStart = exeUtil.getDateAC(stringDateStart, "請購日期(起)");
    if (stringDateStart.length() != 10) {
      message(stringDateStart);
      return value;
    }
    setValue("DateStart", stringDateStart);
    if ("".equals(stringDateEnd)) {
      message("[請購日期(訖)] 不可為空白");
      return value;
    }
    stringDateEnd = exeUtil.getDateAC(stringDateEnd, "請購日期(訖)");
    if (stringDateEnd.length() != 10) {
      message(stringDateEnd);
      return value;
    }
    setValue("DateEnd", stringDateEnd);
    //
    Farglory.Excel.FargloryExcel exeExcel = new Farglory.Excel.FargloryExcel(0, 0, 0, 0);
    //
    String stringPath = "g:/資訊室/Excel/Doc/企劃(個案)費用統計表.xlt";
    if (!(new File(stringPath)).exists()) stringPath = "http://emaker.farglory.com.tw:8080/servlet/baServer3?step=6?filename=C:/emaker/batch/EXCEL/Template/Doc/" + stringPath;
    Vector retVector = exeExcel.getExcelObject(stringPath);
    Dispatch objectSheet1 = (Dispatch) retVector.get(1);
    Dispatch objectSheet2 = (Dispatch) retVector.get(2);
    Dispatch objectClick = null;
    //
    String stringDateStartRoc = exeUtil.getDateConvertFullRoc(stringDateStart);
    String stringDateEndRoc = exeUtil.getDateConvertFullRoc(stringDateEnd);
    String stringYear = datetime.getYear(stringDateStartRoc.replaceAll("/", ""));
    // SHEET1
    talk dbDoc = getTalk("" + get("put_Doc"));
    talk dbSale = getTalk("" + get("put_Sale"));
    Hashtable hashtablePurchaseMoney = new Hashtable();
    Hashtable hashtableDealMoney = new Hashtable();
    Hashtable hashtableTargets = new Hashtable();
    String stringProjectID = "";
    String stringTemp = "";
    String stringCostID = "";
    String stringKey = "";
    String stringMoney = "";
    if ("".equals(stringProjectID1)) {
      System.out.println("SHEET1------------------------------S");
      hashtablePurchaseMoney = getPurchaseMoneyDoc3M014(stringDateStartRoc, stringDateEndRoc, hashtablePurchaseMoney, exeUtil, dbDoc); //
      hashtablePurchaseMoney = getBudgetMoneyDoc3M014(stringDateStartRoc, stringDateEndRoc, hashtablePurchaseMoney, exeUtil, dbDoc); //
      hashtablePurchaseMoney = getRequestMoneyDoc2M012(stringDateStartRoc, stringDateEndRoc, hashtablePurchaseMoney, exeUtil, dbDoc);
      hashtablePurchaseMoney = getRequestMoneyDoc6M012(stringDateStartRoc, stringDateEndRoc, hashtablePurchaseMoney, exeUtil, dbDoc);
      hashtablePurchaseMoney = getHashtableRealMoneyHand(stringDateStart, stringDateEnd, hashtablePurchaseMoney, exeUtil, dbSale);
      System.out.println("SHEET1------------------------------E");
      hashtableDealMoney = getDealMoneyForASale(stringDateStart, stringDateEnd, dbSale); // 實際業績
      hashtableTargets = getTargetsForAStarMM(stringDateStart, stringDateEnd, dbSale); // 業績目標
      //
      Dispatch.call(objectSheet1, "Activate");
      // 結算期間：
      exeExcel.putDataIntoExcel(0, 1, "結算期間：" + stringDateStart + "～" + stringDateEnd, objectSheet1);
      for (int intNo = 0; intNo < 60; intNo++) {
        stringTemp = exeExcel.getDataFromExcel2(intNo + 4, 0, objectSheet1);
        if ("".equals(stringTemp)) continue;
        stringProjectID = exeExcel.getDataFromExcel2(intNo + 4, 1, objectSheet1);
        if ("".equals(stringProjectID)) continue;
        // 業績目標
        stringTemp = "" + hashtableTargets.get(stringProjectID);
        stringTemp = convert.FourToFive("" + exeUtil.doParseDouble(stringTemp), 0);
        exeExcel.putDataIntoExcel(intNo + 4, 2, stringTemp, objectSheet1);
        // 實際業績
        stringTemp = "" + hashtableDealMoney.get(stringProjectID);
        stringTemp = convert.FourToFive("" + exeUtil.doParseDouble(stringTemp), 0);
        exeExcel.putDataIntoExcel(intNo + 4, 3, stringTemp, objectSheet1);
        //
        for (int intNoL = 0; intNoL < 55; intNoL++) {
          stringCostID = exeExcel.getDataFromExcel2(0, intNoL + 4, objectSheet1);
          stringKey = stringProjectID + "--" + stringCostID;
          if ("".equals(stringCostID)) continue;
          stringMoney = "" + hashtablePurchaseMoney.get(stringKey);
          if ("null".equals(stringMoney)) stringMoney = "0";
          if ("720".equals(stringCostID)) System.out.println("[" + stringKey + "]----------------------------[" + stringMoney + "]");
          exeExcel.putDataIntoExcel(intNo + 4, intNoL + 4, stringMoney, objectSheet1);
          objectClick = Dispatch.invoke(objectSheet1, "Range", Dispatch.Get, new Object[] { exeExcel.getExcelColumnName("A", (intNo + 4)) + "" + (intNoL + 5) }, new int[1])
              .toDispatch();
          Dispatch.call(objectClick, "Select");
        }
      }
    } else {
      Dispatch.call(objectSheet1, "Delete");
      exeExcel.setSheetEndView(false);
      exeExcel.putDataIntoExcel(0, 0, " 行銷企劃預算動支表(" + stringProjectID1 + ")", objectSheet2);
    }
    // SHEET2
    hashtablePurchaseMoney = new Hashtable();
    System.out.println("SHEET2------------------------------S");
    hashtablePurchaseMoney = getYPurchaseMoneyDoc3M014(stringProjectID1, stringDateStartRoc, stringDateEndRoc, hashtablePurchaseMoney, exeUtil, dbDoc); //
    System.out.println(">>>[hashtablePurchaseMoney1]-----size--------[" + hashtablePurchaseMoney.size() + "]");
    hashtablePurchaseMoney = getYBudgetMoneyDoc3M014(stringProjectID1, stringDateStartRoc, stringDateEndRoc, hashtablePurchaseMoney, exeUtil, dbDoc); //
    System.out.println(">>>[hashtablePurchaseMoney2]-----size--------[" + hashtablePurchaseMoney.size() + "]");
    hashtablePurchaseMoney = getYRequestMoneyDoc2M012(stringProjectID1, stringDateStartRoc, stringDateEndRoc, hashtablePurchaseMoney, exeUtil, dbDoc);
    System.out.println(">>>[hashtablePurchaseMoney3]-----size--------[" + hashtablePurchaseMoney.size() + "]");
    hashtablePurchaseMoney = getYRequestMoneyDoc6M012(stringProjectID1, stringDateStartRoc, stringDateEndRoc, hashtablePurchaseMoney, exeUtil, dbDoc);
    System.out.println(">>>[hashtablePurchaseMoney4]-----size--------[" + hashtablePurchaseMoney.size() + "]");
    hashtablePurchaseMoney = getYHashtableRealMoneyHand(stringProjectID1, stringDateStart, stringDateEnd, hashtablePurchaseMoney, exeUtil, dbSale);
    System.out.println(">>>[hashtablePurchaseMoney5]-----size--------[" + hashtablePurchaseMoney.size() + "]");
    System.out.println("SHEET2------------------------------E");
    hashtableDealMoney = getYDealMoneyForASale(stringProjectID1, stringDateStart, stringDateEnd, dbSale, exeUtil); // 實際業績
    hashtableTargets = getYTargetsForAStarMM(stringProjectID1, stringDateStart, stringDateEnd, dbSale, exeUtil); // 業績目標
    //
    Dispatch.call(objectSheet2, "Activate");
    // 結算期間：
    exeExcel.putDataIntoExcel(0, 1, "結算期間：" + stringDateStart + "～" + stringDateEnd, objectSheet2);
    for (int intNoL = 1; intNoL < 13; intNoL++) {
      stringKey = stringYear + "/" + convert.add0("" + intNoL, "2");
      // 業績目標
      stringTemp = "" + hashtableTargets.get(stringKey);
      stringTemp = convert.FourToFive("" + exeUtil.doParseDouble(stringTemp), 0);
      exeExcel.putDataIntoExcel(intNoL + 4, 2, stringTemp, objectSheet2);
      // 實際業績
      stringTemp = "" + hashtableDealMoney.get(stringKey);
      stringTemp = convert.FourToFive("" + exeUtil.doParseDouble(stringTemp), 0);
      exeExcel.putDataIntoExcel(intNoL + 4, 3, stringTemp, objectSheet2);
    }
    for (int intNo = 0; intNo < 55; intNo++) {
      stringCostID = exeExcel.getDataFromExcel2(0, intNo + 4, objectSheet2);
      if ("".equals(stringCostID)) continue;
      for (int intNoL = 1; intNoL < 13; intNoL++) {
        stringKey = stringYear + "/" + convert.add0("" + intNoL, "2") + "--" + stringCostID;
        stringMoney = "" + hashtablePurchaseMoney.get(stringKey);
        if ("null".equals(stringMoney)) stringMoney = "0";
        System.out.println("[" + stringKey + "]----------------------------[" + stringMoney + "]");
        exeExcel.putDataIntoExcel(intNoL + 4, intNo + 4, stringMoney, objectSheet2);
        objectClick = Dispatch.invoke(objectSheet2, "Range", Dispatch.Get, new Object[] { exeExcel.getExcelColumnName("A", (intNoL + 4)) + "" + (intNo + 5) }, new int[1])
            .toDispatch();
        Dispatch.call(objectClick, "Select");
      }
    }
    // 釋放 Excel 物件
    exeExcel.getReleaseExcelObject(retVector);
    return value;
  }

  public Hashtable getPurchaseMoneyDoc3M014(String stringDateS, String stringDateE, Hashtable hashtableDoc3M011, FargloryUtil exeUtil, talk dbDoc) throws Throwable {
    String stringSql = "";
    String stringKey = "";
    String stringInOut = "";
    String stringDepartNo = "";
    String stringMoney = "";
    String stringCostID = "";
    String stringCostID1 = "";
    String stringProjectID1 = "";
    String stringDate = "096/11/26";
    String[][] retDoc3M011 = null;
    //
    stringSql = " SELECT  InOut,  M14.DepartNo,  ProjectID,  CostID,  CostID1,  SUM(RealMoney-NoUseRealMoney)  FROM  Doc3M014 M14,  Doc3M011 M11 "
        + " WHERE  M14.BarCode  =  M11.BarCode   AND  UNDERGO_WRITE  <>  'X'  AND  ((InOut  =  'O' AND  ProjectID IN ('SH3','H28','E02A','F1','M','ST', " + // 廠辦
        " 'A36A','H37',  'H38','H38B', 'H40A','H46','H51','H52A','H59A','H61A','H62A','H63A','H65A','H68A','H69A','H71A','H72A','H70A', " + // 北市合計
        " 'H32','H36','H39','H42','H43','H45','H47A','H48A','H50A','H53A','H55A','H56A','H58','H66A',  " + // 北縣小計
        " 'H9','TC','XM43' ))  OR " + // 高雄
        " (InOut  =  'O'  AND  M14.DepartNo = '0531'  AND  ProjectID IN  ('H35','H36','H39','H3','M'))  OR "
        + " (InOut  =  'I'  AND  M14.DepartNo IN  ('0333','033622','03365','03335',  '03363')))  AND  NOT  (InOut  =  'I'  AND  M14.DepartNo IN  ('0331')) "
        + " AND  (UNDERGO_WRITE  =  'Y'  OR   UNDERGO_WRITE  =  'C'  OR   (ApplyType='F'   AND  (UNDERGO_WRITE  =  'S'  OR  UNDERGO_WRITE  =  'H')  AND  CDate  >=  '" + stringDate
        + "')) ";
    if (!"".equals(stringDateS)) stringSql += " AND  NeedDate  >=  '" + stringDateS + "' ";
    if (!"".equals(stringDateE)) stringSql += " AND  NeedDate  <=  '" + stringDateE + "' ";
    stringSql += " GROUP BY  InOut,  M14.DepartNo,  ProjectID,  CostID,  CostID1 ";
    //
    retDoc3M011 = dbDoc.queryFromPool(stringSql);
    for (int intNo = 0; intNo < retDoc3M011.length; intNo++) {
      stringInOut = retDoc3M011[intNo][0].trim();
      stringDepartNo = retDoc3M011[intNo][1].trim();
      stringProjectID1 = retDoc3M011[intNo][2].trim();
      stringCostID = retDoc3M011[intNo][3].trim();
      stringCostID1 = retDoc3M011[intNo][4].trim();
      //
      if ("I".equals(stringInOut)) {
        stringKey = stringDepartNo + "--" + stringCostID + stringCostID1;
      } else {
        if ("0531".equals(stringProjectID1)) {
          stringKey = "053" + stringProjectID1 + "--" + stringCostID + stringCostID1;
        } else {
          stringKey = stringProjectID1 + "--" + stringCostID + stringCostID1;
        }
      }
      // if("862".equals(stringCostID+stringCostID1))
      // System.out.println("getPurchaseMoneyDoc3M014["+stringKey+"]----------------------------["+stringMoney+"]")
      // ;
      stringMoney = retDoc3M011[intNo][5].trim();
      stringMoney = "" + (exeUtil.doParseDouble("" + hashtableDoc3M011.get(stringKey)) + exeUtil.doParseDouble(stringMoney));
      // if("862".equals(stringCostID+stringCostID1))
      // System.out.println("getPurchaseMoneyDoc3M014["+stringKey+"]----------------------------["+stringMoney+"]")
      // ;
      //
      hashtableDoc3M011.put(stringKey, convert.FourToFive(stringMoney, 0));
    }
    return hashtableDoc3M011;
  }

  public Hashtable getYPurchaseMoneyDoc3M014(String stringProjectID1Q, String stringDateS, String stringDateE, Hashtable hashtableDoc3M011, FargloryUtil exeUtil, talk dbDoc)
      throws Throwable {
    String stringSql = "";
    String stringKey = "";
    String stringMoney = "";
    String stringCostID = "";
    String stringCostID1 = "";
    String stringProjectID1 = "";
    String stringDate = "096/11/26";
    String[][] retDoc3M011 = null;
    //
    stringSql = " SELECT  SUBSTRING(CONVERT(char(8),NeedDate,111),1,5),  CostID,  CostID1,  SUM(RealMoney-NoUseRealMoney)  FROM  Doc3M014 M14,  Doc3M011 M11 "
        + " WHERE  M14.BarCode  =  M11.BarCode   AND  UNDERGO_WRITE  <>  'X'  AND  ( ";
    if ("b3018".equals(getUser())) {
      stringSql += " (InOut  =  'O' AND  ProjectID IN ('SH3','H28','E02A','F1','M','ST', " + // 廠辦
          " 'A36A','H37',  'H38','H38B', 'H40A','H46','H51','H52A','H59A','H61A','H62A','H63A','H65A','H68A','H69A','H71A','H72A','H70A', " + // 北市合計
          " 'H32','H36','H39','H42','H43','H45','H47A','H48A','H50A','H53A','H55A','H56A','H58','H66A',  " + // 北縣小計
          " 'H9','TC','XM43' ))  OR " + // 高雄
          " (InOut  =  'O'  AND  M14.DepartNo = '0531'  AND  ProjectID IN  ('H35','H36','H39','H3','M'))  OR ";

    } else {
      stringSql += " (InOut  =  'O')  OR ";
    }
    stringSql += " (InOut  =  'I'  AND  M14.DepartNo IN  ('0333','033622','03365','03335',  '03363')))  AND  NOT  (InOut  =  'I'  AND  M14.DepartNo IN  ('0331')) "
        + " AND  (UNDERGO_WRITE  =  'Y'  OR   UNDERGO_WRITE  =  'C'  OR   (ApplyType='F'   AND  (UNDERGO_WRITE  =  'S'  OR  UNDERGO_WRITE  =  'H')  AND  CDate  >=  '" + stringDate
        + "')) ";
    if (!"".equals(stringDateS)) stringSql += " AND  NeedDate  >=  '" + stringDateS + "' ";
    if (!"".equals(stringDateE)) stringSql += " AND  NeedDate  <=  '" + stringDateE + "' ";
    if (!"".equals(stringProjectID1Q)) stringSql += " AND  ProjectID1  =  '" + stringProjectID1Q + "' ";
    stringSql += " GROUP BY  SUBSTRING(CONVERT(char(8),NeedDate,111),1,5),  CostID,  CostID1 ";
    //
    retDoc3M011 = dbDoc.queryFromPool(stringSql);
    for (int intNo = 0; intNo < retDoc3M011.length; intNo++) {
      stringProjectID1 = retDoc3M011[intNo][0].trim();
      stringCostID = retDoc3M011[intNo][1].trim();
      stringCostID1 = retDoc3M011[intNo][2].trim();
      stringKey = stringProjectID1 + "--" + stringCostID + stringCostID1;
      stringMoney = retDoc3M011[intNo][3].trim();
      //
      stringMoney = "" + (exeUtil.doParseDouble("" + hashtableDoc3M011.get(stringKey)) + exeUtil.doParseDouble(stringMoney));
      System.out.println("getYPurchaseMoneyDoc3M014[" + stringKey + "]----------------------------[" + stringMoney + "]");
      //
      hashtableDoc3M011.put(stringKey, convert.FourToFive(stringMoney, 0));
    }
    return hashtableDoc3M011;
  }

  public Hashtable getBudgetMoneyDoc3M014(String stringDateS, String stringDateE, Hashtable hashtableDoc3M011, FargloryUtil exeUtil, talk dbDoc) throws Throwable {
    String stringSql = "";
    String stringKey = "";
    String stringInOut = "";
    String stringDepartNo = "";
    String stringCostID = "";
    String stringCostID1 = "";
    String stringMoney = "";
    String stringProjectID1 = "";
    String stringDate = "096/11/26";
    String[][] retDoc3M011 = null;
    //
    stringSql = " SELECT  InOut,  M14.DepartNo,  ProjectID,  CostID,  CostID1,  SUM(BudgetMoney)  FROM  Doc3M014 M14,  Doc3M011 M11  WHERE  M14.BarCode  =  M11.BarCode "
        + "  AND  UNDERGO_WRITE  <>  'X'  AND  ((InOut  =  'O' AND  ProjectID IN ('SH3','H28','E02A','F1','M','ST', " + // 廠辦
        " 'A36A','H37',  'H38','H38B', 'H40A','H46','H51','H52A','H59A','H61A','H62A','H63A','H65A','H68A','H69A','H71A','H72A','H70A', " + // 北市合計
        " 'H32','H36','H39','H42','H43','H45','H47A','H48A','H50A','H53A','H55A','H56A','H58','H66A',  " + // 北縣小計
        " 'H9','TC','XM43' ))  OR " + // 高雄
        " (InOut  =  'O'  AND  M14.DepartNo = '0531'  AND  ProjectID IN  ('H35','H36','H39','H3','M'))  OR "
        + " (InOut  =  'I'  AND  M14.DepartNo IN  ('0333','033622','03365','03335',  '03363')))  AND  NOT  (InOut  =  'I'  AND  M14.DepartNo IN  ('0331')) "
        + " AND  NOT  (UNDERGO_WRITE  =  'Y'  OR   UNDERGO_WRITE  =  'C'  OR  " + " (ApplyType='F'   AND  (UNDERGO_WRITE  =  'S'  OR  UNDERGO_WRITE  =  'H')  AND  CDate  >=  '"
        + stringDate + "')) ";
    if (!"".equals(stringDateS)) stringSql += " AND  NeedDate  >=  '" + stringDateS + "' ";
    if (!"".equals(stringDateE)) stringSql += " AND  NeedDate  <=  '" + stringDateE + "' ";
    stringSql += " GROUP BY InOut,  M14.DepartNo,  ProjectID, CostID,  CostID1 ";
    //
    retDoc3M011 = dbDoc.queryFromPool(stringSql);
    for (int intNo = 0; intNo < retDoc3M011.length; intNo++) {
      stringInOut = retDoc3M011[intNo][0].trim();
      stringDepartNo = retDoc3M011[intNo][1].trim();
      stringProjectID1 = retDoc3M011[intNo][2].trim();
      stringCostID = retDoc3M011[intNo][3].trim();
      stringCostID1 = retDoc3M011[intNo][4].trim();
      stringMoney = retDoc3M011[intNo][5].trim();
      //
      if ("I".equals(stringInOut)) {
        stringKey = stringDepartNo + "--" + stringCostID + stringCostID1;
      } else {
        if ("0531".equals(stringProjectID1)) {
          stringKey = "053" + stringProjectID1 + "--" + stringCostID + stringCostID1;
        } else {
          stringKey = stringProjectID1 + "--" + stringCostID + stringCostID1;
        }
      }
      // if("862".equals(stringCostID+stringCostID1))
      // System.out.println("getBudgetMoneyDoc3M014["+stringKey+"]----------------------------["+stringMoney+"]")
      // ;
      stringMoney = "" + (exeUtil.doParseDouble("" + hashtableDoc3M011.get(stringKey)) + exeUtil.doParseDouble(stringMoney));
      // if("862".equals(stringCostID+stringCostID1))
      // System.out.println("getBudgetMoneyDoc3M014["+stringKey+"]----------------------------["+stringMoney+"]")
      // ;
      //
      hashtableDoc3M011.put(stringKey, convert.FourToFive(stringMoney, 0));
    }
    return hashtableDoc3M011;
  }

  public Hashtable getYBudgetMoneyDoc3M014(String stringProjectID1Q, String stringDateS, String stringDateE, Hashtable hashtableDoc3M011, FargloryUtil exeUtil, talk dbDoc)
      throws Throwable {
    String stringSql = "";
    String stringKey = "";
    String stringCostID = "";
    String stringCostID1 = "";
    String stringMoney = "";
    String stringProjectID1 = "";
    String stringDate = "096/11/26";
    String[][] retDoc3M011 = null;
    //
    stringSql = " SELECT  SUBSTRING(CONVERT(char(8),NeedDate,111),1,5),  CostID,  CostID1,  SUM(BudgetMoney)  FROM  Doc3M014 M14,  Doc3M011 M11 "
        + " WHERE  M14.BarCode  =  M11.BarCode   AND  UNDERGO_WRITE  <>  'X'   AND  ( ";
    if ("b3018".equals(getUser())) {
      stringSql += "(InOut  =  'O' AND  ProjectID IN ('SH3','H28','E02A','F1','M','ST', " + // 廠辦
          " 'A36A','H37',  'H38','H38B', 'H40A','H46','H51','H52A','H59A','H61A','H62A','H63A','H65A','H68A','H69A','H71A','H72A','H70A', " + // 北市合計
          " 'H32','H36','H39','H42','H43','H45','H47A','H48A','H50A','H53A','H55A','H56A','H58','H66A',  " + // 北縣小計
          " 'H9','TC','XM43' ))  OR " + // 高雄
          " (InOut  =  'O'  AND  M14.DepartNo = '0531'  AND  ProjectID IN  ('H35','H36','H39','H3','M'))  OR ";
    } else {
      stringSql += " (InOut  =  'O')  OR ";
    }
    stringSql += " (InOut  =  'I'  AND  M14.DepartNo IN  ('0333','033622','03365','03335',  '03363')))  AND  NOT  (InOut  =  'I'  AND  M14.DepartNo IN  ('0331')) "
        + " AND  NOT  (UNDERGO_WRITE  =  'Y'  OR   UNDERGO_WRITE  =  'C'  OR  " + " (ApplyType='F'   AND  (UNDERGO_WRITE  =  'S'  OR  UNDERGO_WRITE  =  'H')  AND  CDate  >=  '"
        + stringDate + "')) ";
    if (!"".equals(stringDateS)) stringSql += " AND  NeedDate  >=  '" + stringDateS + "' ";
    if (!"".equals(stringDateE)) stringSql += " AND  NeedDate  <=  '" + stringDateE + "' ";
    if (!"".equals(stringProjectID1Q)) stringSql += " AND  ProjectID1  =  '" + stringProjectID1Q + "' ";
    stringSql += " GROUP BY SUBSTRING(CONVERT(char(8),NeedDate,111),1,5), CostID,  CostID1 ";
    //
    retDoc3M011 = dbDoc.queryFromPool(stringSql);
    for (int intNo = 0; intNo < retDoc3M011.length; intNo++) {
      stringProjectID1 = retDoc3M011[intNo][0].trim();
      stringCostID = retDoc3M011[intNo][1].trim();
      stringCostID1 = retDoc3M011[intNo][2].trim();
      stringKey = stringProjectID1 + "--" + stringCostID + stringCostID1;
      stringMoney = retDoc3M011[intNo][3].trim();
      //
      System.out.println("getYBudgetMoneyDoc3M014[" + stringKey + "]----------------------------[" + stringMoney + "]");
      stringMoney = "" + (exeUtil.doParseDouble("" + hashtableDoc3M011.get(stringKey)) + exeUtil.doParseDouble(stringMoney));
      //
      hashtableDoc3M011.put(stringKey, convert.FourToFive(stringMoney, 0));
    }
    return hashtableDoc3M011;
  }

  public Hashtable getRequestMoneyDoc2M012(String stringDateS, String stringDateE, Hashtable hashtableDoc3M011, FargloryUtil exeUtil, talk dbDoc) throws Throwable {
    String stringSql = "";
    String stringKey = "";
    String stringInOut = "";
    String stringDepartNo = "";
    String stringCostID = "";
    String stringCostID1 = "";
    String stringMoney = "";
    String stringProjectID1 = "";
    String[][] retDoc3M011 = null;
    //
    stringSql = "SELECT  InOut,  M12.DepartNo,  ProjectID,  CostID,  CostID1,  SUM(RealTotalMoney)  FROM  Doc2M012 M12,  Doc2M010  M10 "
        + " WHERE  M12.BarCode  =  M10.BarCode  AND  PurchaseNoExist  =  'N'  AND  UNDERGO_WRITE  <>  'E' "
        + " AND  ((InOut  =  'O' AND  ProjectID IN ('SH3','H28','E02A','F1','M','ST', " + // 廠辦
        " 'A36A','H37',  'H38','H38B', 'H40A','H46','H51','H52A','H59A','H61A','H62A','H63A','H65A','H68A','H69A','H71A','H72A','H70A', " + // 北市合計
        " 'H32','H36','H39','H42','H43','H45','H47A','H48A','H50A','H53A','H55A','H56A','H58','H66A',  " + // 北縣小計
        " 'H9','TC','XM43' ))  OR " + // 高雄
        " (InOut  =  'O'  AND  M12.DepartNo = '0531'  AND  ProjectID IN  ('H35','H36','H39','H3','M'))  OR "
        + " (InOut  =  'I'  AND  M12.DepartNo IN  ('0333','033622','03365','03335',  '03363'))) ";
    if (!"".equals(stringDateS)) stringSql += " AND  CDate  >=  '" + stringDateS + "' ";
    if (!"".equals(stringDateE)) stringSql += " AND  CDate  <=  '" + stringDateE + "' ";
    stringSql += " AND  NOT  (InOut  =  'I'  AND  M12.DepartNo IN  ('0331'))  GROUP BY  InOut,  M12.DepartNo,  ProjectID,  CostID,  CostID1 ";
    //
    retDoc3M011 = dbDoc.queryFromPool(stringSql);
    for (int intNo = 0; intNo < retDoc3M011.length; intNo++) {
      stringInOut = retDoc3M011[intNo][0].trim();
      stringDepartNo = retDoc3M011[intNo][1].trim();
      stringProjectID1 = retDoc3M011[intNo][2].trim();
      stringCostID = retDoc3M011[intNo][3].trim();
      stringCostID1 = retDoc3M011[intNo][4].trim();
      stringMoney = retDoc3M011[intNo][5].trim();
      //
      if ("I".equals(stringInOut)) {
        stringKey = stringDepartNo + "--" + stringCostID + stringCostID1;
      } else {
        if ("0531".equals(stringProjectID1)) {
          stringKey = "053" + stringProjectID1 + "--" + stringCostID + stringCostID1;
        } else {
          stringKey = stringProjectID1 + "--" + stringCostID + stringCostID1;
        }
      }
      stringMoney = "" + (exeUtil.doParseDouble("" + hashtableDoc3M011.get(stringKey)) + exeUtil.doParseDouble(stringMoney));
      //
      hashtableDoc3M011.put(stringKey, convert.FourToFive(stringMoney, 0));
    }
    return hashtableDoc3M011;
  }

  public Hashtable getYRequestMoneyDoc2M012(String stringProjectID1Q, String stringDateS, String stringDateE, Hashtable hashtableDoc3M011, FargloryUtil exeUtil, talk dbDoc)
      throws Throwable {
    String stringSql = "";
    String stringKey = "";
    String stringCostID = "";
    String stringCostID1 = "";
    String stringMoney = "";
    String stringProjectID1 = "";
    String[][] retDoc3M011 = null;
    //
    stringSql = "SELECT  SUBSTRING(CONVERT(char(8),CDate,111),1,5),  CostID,  CostID1,  SUM(RealTotalMoney)  FROM  Doc2M012 M12,  Doc2M010  M10 "
        + " WHERE  M12.BarCode  =  M10.BarCode  AND  PurchaseNoExist  =  'N'  AND  UNDERGO_WRITE  <>  'E'  AND  ( ";
    if ("b3018".equals(getUser())) {
      stringSql += " (InOut  =  'O' AND  ProjectID IN ('SH3','H28','E02A','F1','M','ST', " + // 廠辦
          " 'A36A','H37',  'H38','H38B', 'H40A','H46','H51','H52A','H59A','H61A','H62A','H63A','H65A','H68A','H69A','H71A','H72A','H70A', " + // 北市合計
          " 'H32','H36','H39','H42','H43','H45','H47A','H48A','H50A','H53A','H55A','H56A','H58','H66A',  " + // 北縣小計
          " 'H9','TC','XM43' ))  OR " + // 高雄
          " (InOut  =  'O'  AND  M12.DepartNo = '0531'  AND  ProjectID IN  ('H35','H36','H39','H3','M'))  OR ";
    } else {
      stringSql += " (InOut  =  'O')  OR ";
    }
    stringSql += " (InOut  =  'I'  AND  M12.DepartNo IN  ('0333','033622','03365','03335',  '03363'))) ";
    if (!"".equals(stringDateS)) stringSql += " AND  CDate  >=  '" + stringDateS + "' ";
    if (!"".equals(stringDateE)) stringSql += " AND  CDate  <=  '" + stringDateE + "' ";
    if (!"".equals(stringProjectID1Q)) stringSql += " AND  ProjectID1  =  '" + stringProjectID1Q + "' ";
    stringSql += " AND  NOT  (InOut  =  'I'  AND  M12.DepartNo IN  ('0331'))  GROUP BY  SUBSTRING(CONVERT(char(8),CDate,111),1,5),  CostID,  CostID1 ";
    //
    retDoc3M011 = dbDoc.queryFromPool(stringSql);
    for (int intNo = 0; intNo < retDoc3M011.length; intNo++) {
      stringProjectID1 = retDoc3M011[intNo][0].trim();
      stringCostID = retDoc3M011[intNo][1].trim();
      stringCostID1 = retDoc3M011[intNo][2].trim();
      stringKey = stringProjectID1 + "--" + stringCostID + stringCostID1;
      stringMoney = retDoc3M011[intNo][3].trim();
      //
      System.out.println("getYBudgetMoneyDoc3M014[" + stringKey + "]----------------------------[" + stringMoney + "]");
      stringMoney = "" + (exeUtil.doParseDouble("" + hashtableDoc3M011.get(stringKey)) + exeUtil.doParseDouble(stringMoney));
      //
      hashtableDoc3M011.put(stringKey, convert.FourToFive(stringMoney, 0));
    }
    return hashtableDoc3M011;
  }

  public Hashtable getRequestMoneyDoc6M012(String stringDateS, String stringDateE, Hashtable hashtableDoc3M011, FargloryUtil exeUtil, talk dbDoc) throws Throwable {
    String stringSql = "";
    String stringKey = "";
    String stringInOut = "";
    String stringDepartNo = "";
    String stringCostID = "";
    String stringCostID1 = "";
    String stringMoney = "";
    String stringProjectID1 = "";
    String[][] retDoc3M011 = null;
    //
    stringSql = "SELECT  InOut,  M12.DepartNo,  ProjectID,  CostID,  CostID1,  SUM(RealTotalMoney)  FROM  Doc6M012 M12,  Doc6M010  M10 "
        + " WHERE  M12.BarCode  =  M10.BarCode  AND  PurchaseNoExist  =  'N'  AND  UNDERGO_WRITE  <>  'E' "
        + " AND  ((InOut  =  'O' AND  ProjectID IN ('SH3','H28','E02A','F1','M','ST', " + // 廠辦
        " 'A36A','H37',  'H38','H38B', 'H40A','H46','H51','H52A','H59A','H61A','H62A','H63A','H65A','H68A','H69A','H71A','H72A','H70A', " + // 北市合計
        " 'H32','H36','H39','H42','H43','H45','H47A','H48A','H50A','H53A','H55A','H56A','H58','H66A',  " + // 北縣小計
        " 'H9','TC','XM43' ))  OR " + // 高雄
        " (InOut  =  'O'  AND  M12.DepartNo = '0531'  AND  ProjectID IN  ('H35','H36','H39','H3','M'))  OR "
        + " (InOut  =  'I'  AND  M12.DepartNo IN  ('0333','033622','03365','03335',  '03363'))) ";
    if (!"".equals(stringDateS)) stringSql += " AND  NeedDate  >=  '" + stringDateS + "' ";
    if (!"".equals(stringDateE)) stringSql += " AND  NeedDate  <=  '" + stringDateE + "' ";
    stringSql += " AND  NOT  (InOut  =  'I'  AND  M12.DepartNo IN  ('0331'))  GROUP BY  InOut, M12.DepartNo,  ProjectID,  CostID,  CostID1 ";
    //
    retDoc3M011 = dbDoc.queryFromPool(stringSql);
    for (int intNo = 0; intNo < retDoc3M011.length; intNo++) {
      stringInOut = retDoc3M011[intNo][0].trim();
      stringDepartNo = retDoc3M011[intNo][1].trim();
      stringProjectID1 = retDoc3M011[intNo][2].trim();
      stringCostID = retDoc3M011[intNo][3].trim();
      stringCostID1 = retDoc3M011[intNo][4].trim();
      stringMoney = retDoc3M011[intNo][5].trim();
      //
      if ("I".equals(stringInOut)) {
        stringKey = stringDepartNo + "--" + stringCostID + stringCostID1;
      } else {
        if ("0531".equals(stringProjectID1)) {
          stringKey = "053" + stringProjectID1 + "--" + stringCostID + stringCostID1;
        } else {
          stringKey = stringProjectID1 + "--" + stringCostID + stringCostID1;
        }
      }
      stringMoney = "" + (exeUtil.doParseDouble("" + hashtableDoc3M011.get(stringKey)) + exeUtil.doParseDouble(stringMoney));
      //
      hashtableDoc3M011.put(stringKey, convert.FourToFive(stringMoney, 0));
    }
    return hashtableDoc3M011;
  }

  public Hashtable getYRequestMoneyDoc6M012(String stringProjectID1Q, String stringDateS, String stringDateE, Hashtable hashtableDoc3M011, FargloryUtil exeUtil, talk dbDoc)
      throws Throwable {
    String stringSql = "";
    String stringKey = "";
    String stringCostID = "";
    String stringCostID1 = "";
    String stringMoney = "";
    String stringProjectID1 = "";
    String[][] retDoc3M011 = null;
    //
    // **申請書20210830010 : 錯誤
    stringSql = "SELECT  SUBSTRING(CONVERT(char(8),NeedDate,111),1,5),  CostID,  CostID1,  SUM(RealTotalMoney)  FROM  Doc6M012 M12,  Doc6M010  M10 "
        + " WHERE  M12.BarCode  =  M10.BarCode  AND  PurchaseNoExist  =  'N'  AND  UNDERGO_WRITE  <>  'E'  AND  ( ";
    if ("b3018".equals(getUser())) {
      stringSql += " (InOut  =  'O' AND  ProjectID IN ('SH3','H28','E02A','F1','M','ST', " + // 廠辦
          " 'A36A','H37',  'H38','H38B', 'H40A','H46','H51','H52A','H59A','H61A','H62A','H63A','H65A','H68A','H69A','H71A','H72A','H70A', " + // 北市合計
          " 'H32','H36','H39','H42','H43','H45','H47A','H48A','H50A','H53A','H55A','H56A','H58','H66A',  " + // 北縣小計
          " 'H9','TC','XM43' ))  OR " + // 高雄
          " (InOut  =  'O'  AND  M12.DepartNo = '0531'  AND  ProjectID IN  ('H35','H36','H39','H3','M'))  OR ";
    } else {
      stringSql += " (InOut  =  'O')  OR ";
    }
    stringSql += " (InOut  =  'I'  AND  M12.DepartNo IN  ('0333','033622','03365','03335',  '03363')))  AND  NOT  (InOut  =  'I'  AND  M12.DepartNo IN  ('0331')) ";
    if (!"".equals(stringDateS)) stringSql += " AND  NeedDate  >=  '" + stringDateS + "' ";
    if (!"".equals(stringDateE)) stringSql += " AND  NeedDate  <=  '" + stringDateE + "' ";
    if (!"".equals(stringProjectID1Q)) stringSql += " AND  ProjectID1  =  '" + stringProjectID1Q + "' ";
    stringSql += " GROUP BY  SUBSTRING(CONVERT(char(8),NeedDate,111),1,5),  CostID,  CostID1 ";
    //
    //
    retDoc3M011 = dbDoc.queryFromPool(stringSql);
    for (int intNo = 0; intNo < retDoc3M011.length; intNo++) {
      stringProjectID1 = retDoc3M011[intNo][0].trim();
      stringCostID = retDoc3M011[intNo][1].trim();
      stringCostID1 = retDoc3M011[intNo][2].trim();
      stringKey = stringProjectID1 + "--" + stringCostID + stringCostID1;
      stringMoney = retDoc3M011[intNo][3].trim();
      //
      stringMoney = "" + (exeUtil.doParseDouble("" + hashtableDoc3M011.get(stringKey)) + exeUtil.doParseDouble(stringMoney));
      //
      hashtableDoc3M011.put(stringKey, convert.FourToFive(stringMoney, 0));
    }
    return hashtableDoc3M011;
  }

  // 實際業績
  public Hashtable getDealMoneyForASale(String stringDateACS, String stringDateACE, talk dbSale) throws Throwable {
    String stringSql = "";
    String stringDealMoney = "";
    String[][] retASale = new String[0][0];
    // 0 ProjectID1 1 SUM(DealMoney)*10
    stringSql = " SELECT  ProjectID1,  SUM(DealMoney)  FROM  A_Sale   WHERE  Orderdate  BETWEEN  '" + stringDateACS + "'  AND  '" + stringDateACE + "' "
        + " GROUP BY   ProjectID1 ";
    retASale = dbSale.queryFromPool(stringSql);
    //
    String stringProjectID1 = "";
    String stringMoney = "";
    Hashtable hashtableDealMoneyProjectID = new Hashtable();
    for (int intNo = 0; intNo < retASale.length; intNo++) {
      stringProjectID1 = retASale[intNo][0].trim();
      stringMoney = retASale[intNo][1].trim();
      //
      hashtableDealMoneyProjectID.put(stringProjectID1, convert.FourToFive(stringMoney, 0));
    }
    return hashtableDealMoneyProjectID;
  }

  public Hashtable getYDealMoneyForASale(String stringProjectID1, String stringDateACS, String stringDateACE, talk dbSale, FargloryUtil exeUtil) throws Throwable {
    String stringSql = "";
    String stringDealMoney = "";
    String[][] retASale = new String[0][0];
    // 0 ProjectID1 1 SUM(DealMoney)*10
    stringSql = " SELECT  SUBSTRING(CONVERT(char(10),Orderdate,111),6,2),  SUM(DealMoney)  FROM  A_Sale   WHERE  Orderdate  BETWEEN  '" + stringDateACS + "'  AND  '"
        + stringDateACE + "' ";
    if (!"".equals(stringProjectID1)) stringSql += " AND  ProjectID1  =  '" + stringProjectID1 + "' ";
    stringSql += " GROUP BY   SUBSTRING(CONVERT(char(10),Orderdate,111),6,2) ";
    retASale = dbSale.queryFromPool(stringSql);
    //
    String stringMoney = "";
    Hashtable hashtableDealMoneyProjectID = new Hashtable();
    String stringKey = "";
    String stringYearRoc = datetime.getYear(exeUtil.getDateConvertFullRoc(stringDateACS).replaceAll("/", ""));
    if (!"Y".equals("" + get("Century_START_DATE"))) {
      stringYearRoc = "" + exeUtil.doParseInteger(stringYearRoc);
    }
    for (int intNo = 0; intNo < retASale.length; intNo++) {
      stringProjectID1 = retASale[intNo][0].trim();
      stringMoney = retASale[intNo][1].trim();
      stringKey = stringYearRoc + "/" + stringProjectID1;
      //
      // System.out.println("實際業績["+stringKey+"]-------------------["+stringMoney+"]")
      // ;
      hashtableDealMoneyProjectID.put(stringKey, convert.FourToFive(stringMoney, 0));
    }
    return hashtableDealMoneyProjectID;
  }

  // 表格 A_STarMM
  // 業績目標
  public Hashtable getTargetsForAStarMM(String stringDateACS, String stirngDateACE, talk dbSale) throws Throwable {
    String stringSql = "";
    String[][] retAStarMM = new String[0][0];
    //
    stringDateACS = stringDateACS.replaceAll("/", "");
    stringDateACS = stringDateACS.substring(0, 6) + "01";
    stringDateACS = convert.FormatedDate(stringDateACS, "/");
    stirngDateACE = stirngDateACE.replaceAll("/", "");
    stirngDateACE = stirngDateACE.substring(0, 6) + "01";
    stirngDateACE = datetime.dateAdd(stirngDateACE, "m", 1);
    stirngDateACE = datetime.dateAdd(stirngDateACE, "d", -1);
    stirngDateACE = convert.FormatedDate(stirngDateACE, "/");
    // Targets
    stringSql = " SELECT  ProjectID,  SUM(Targets)  FROM  A_STarMM   WHERE  YearMM  BETWEEN  '" + stringDateACS + "'  AND  '" + stirngDateACE + "' " + " GROUP BY  ProjectID ";
    retAStarMM = dbSale.queryFromPool(stringSql);
    //
    String stringProjectID1 = "";
    String stringMoney = "";
    Hashtable hashtableTargetsProjectID = new Hashtable();
    for (int intNo = 0; intNo < retAStarMM.length; intNo++) {
      stringProjectID1 = retAStarMM[intNo][0].trim();
      stringMoney = retAStarMM[intNo][1].trim();
      //
      hashtableTargetsProjectID.put(stringProjectID1, convert.FourToFive(stringMoney, 0));
    }
    return hashtableTargetsProjectID;
  }

  public Hashtable getYTargetsForAStarMM(String stringProjectID1, String stringDateACS, String stirngDateACE, talk dbSale, FargloryUtil exeUtil) throws Throwable {
    String stringSql = "";
    String[][] retAStarMM = new String[0][0];
    //
    stringDateACS = stringDateACS.replaceAll("/", "");
    stringDateACS = stringDateACS.substring(0, 6) + "01";
    stringDateACS = exeUtil.getDateConvert(stringDateACS);
    stirngDateACE = stirngDateACE.replaceAll("/", "");
    stirngDateACE = stirngDateACE.substring(0, 6) + "01";
    stirngDateACE = datetime.dateAdd(stirngDateACE, "m", 1);
    stirngDateACE = datetime.dateAdd(stirngDateACE, "d", -1);
    stirngDateACE = exeUtil.getDateConvert(stirngDateACE);
    // Targets
    stringSql = " SELECT  SUBSTRING(CONVERT(char(10),YearMM,111),6,2),  SUM(Targets)  FROM  A_STarMM   WHERE  YearMM  BETWEEN  '" + stringDateACS + "'  AND  '" + stirngDateACE
        + "' ";
    if (!"".equals(stringProjectID1)) stringSql += " AND  ProjectID  =  '" + stringProjectID1 + "' ";
    stringSql += " GROUP BY  SUBSTRING(CONVERT(char(10),YearMM,111),6,2) ";
    retAStarMM = dbSale.queryFromPool(stringSql);
    //
    String stringMoney = "";
    Hashtable hashtableTargetsProjectID = new Hashtable();
    String stringYearRoc = datetime.getYear(exeUtil.getDateConvertFullRoc(stringDateACS).replaceAll("/", ""));
    String stringKey = "";
    if (!"Y".equals("" + get("Century_START_DATE"))) {
      stringYearRoc = "" + exeUtil.doParseInteger(stringYearRoc);
    }
    for (int intNo = 0; intNo < retAStarMM.length; intNo++) {
      stringProjectID1 = retAStarMM[intNo][0].trim();
      stringMoney = retAStarMM[intNo][1].trim();
      stringKey = stringYearRoc + "/" + stringProjectID1;
      //
      // System.out.println("業績目標["+stringKey+"]-------------------["+stringMoney+"]")
      // ;
      hashtableTargetsProjectID.put(stringKey, convert.FourToFive(stringMoney, 0));
    }
    return hashtableTargetsProjectID;
  }

  public Hashtable getHashtableRealMoneyHand(String stringDateACS, String stringDateACE, Hashtable hashtableDoc3M011, FargloryUtil exeUtil, talk dbSale) throws Throwable {
    // System.out.println("getHashtableRealMoneyHand-----------------------------S")
    // ;
    String stringSql = "";
    String stringSqlAnd = "";
    //
    String stringCostID = "";
    String stringCostID1 = "";
    String stringKey = "";
    String stringMoney = "";
    String stringProjectID1 = "";
    // 手動(總案)
    stringSql = "SELECT  ProjectID,  CostID,  CostID1,  SUM(RealMoney)*10000  FROM  Z_CoReaMM  WHERE  1=1 "
        + " AND  (ISNULL(BarCode,  '')  =  ''  OR  ISNULL(Transfer,'')  = '公文追蹤'  OR  ISNULL(Transfer,'')  LIKE '%案別分攤%') " +
        // " AND ISNULL(ComNo, '') IN ('', '06') " +
        " AND  ProjectID IN ('SH3','H28','E02A','F1','M','ST', " + // 廠辦
        " 'A36A','H37',  'H38','H38B', 'H40A','H46','H51','H52A','H59A','H61A','H62A','H63A','H65A','H68A','H69A','H71A','H72A','H70A', " + // 北市合計
        " 'H32','H36','H39','H42','H43','H45','H47A','H48A','H50A','H53A','H55A','H56A','H58','H66A',  " + // 北縣小計
        " '053H36', '053H39',  '053H3', '053H42','053M',  " + //
        " 'H9','TC' ) "; // 高雄
    if (!"".equals(stringDateACS)) stringSql += " AND  YYMM  >=  '" + stringDateACS + "' ";
    if (!"".equals(stringDateACE)) stringSql += " AND  YYMM  <=  '" + stringDateACE + "' ";
    stringSql += " GROUP BY ProjectID,  CostID,  CostID1 ";
    String[][] retDoc3M011 = dbSale.queryFromPool(stringSql);
    //
    for (int intNo = 0; intNo < retDoc3M011.length; intNo++) {
      stringProjectID1 = retDoc3M011[intNo][0].trim();
      stringCostID = retDoc3M011[intNo][1].trim();
      stringCostID1 = retDoc3M011[intNo][2].trim();
      if ("".equals(stringCostID1)) {
        stringCostID1 = ("71,73,75,76,81,82,99,".indexOf(stringCostID) != -1) ? "0" : "1";
      }
      stringKey = stringProjectID1 + "--" + stringCostID + stringCostID1;
      stringMoney = retDoc3M011[intNo][3].trim();
      //
      stringMoney = "" + (exeUtil.doParseDouble("" + hashtableDoc3M011.get(stringKey)) + exeUtil.doParseDouble(stringMoney));
      //
      hashtableDoc3M011.put(stringKey, convert.FourToFive(stringMoney, 0));
    }
    // System.out.println("getHashtableRealMoneyHand-----------------------------E")
    // ;
    return hashtableDoc3M011;
  }

  public Hashtable getYHashtableRealMoneyHand(String stringProjectID1Q, String stringDateACS, String stringDateACE, Hashtable hashtableDoc3M011, FargloryUtil exeUtil, talk dbSale)
      throws Throwable {
    String stringSql = "";
    String stringSqlAnd = "";
    //
    String stringCostID = "";
    String stringCostID1 = "";
    String stringKey = "";
    String stringMoney = "";
    String stringProjectID1 = "";
    // 手動(總案)
    stringSql = "SELECT  SUBSTRING(CONVERT(char(10),YYMM,111),6,2),  CostID,  CostID1,  SUM(RealMoney)*10000  FROM  Z_CoReaMM  WHERE  1=1 "
        + " AND  (ISNULL(BarCode,  '')  =  ''  OR  ISNULL(Transfer,'')  = '公文追蹤') ";
    // " AND ISNULL(ComNo, '') IN ('', '06') " ;
    if ("b3018".equals(getUser())) {
      stringSql += " AND  ProjectID IN ('SH3','H28','E02A','F1','M','ST', " + // 廠辦
          " 'A36A','H37',  'H38','H38B','H40A','H46','H51','H52A','H59A','H61A','H62A','H63A','H65A','H68A','H69A','H71A','H72A','H70A', " + // 北市合計
          " 'H32','H36','H39','H42','H43','H45','H47A','H48A','H50A','H53A','H55A','H56A','H58','H66A',  " + // 北縣小計
          " '053H36', '053H39',  '053H3', " + //
          " 'H9','TC' ) "; // 高雄
    }
    if (!"".equals(stringDateACS)) stringSql += " AND  YYMM  >=  '" + stringDateACS + "' ";
    if (!"".equals(stringDateACE)) stringSql += " AND  YYMM  <=  '" + stringDateACE + "' ";
    if (!"".equals(stringProjectID1Q)) stringSql += " AND  ProjectID1  =  '" + stringProjectID1Q + "' ";
    stringSql += " GROUP BY SUBSTRING(CONVERT(char(10),YYMM,111),6,2),  CostID,  CostID1 ";
    String[][] retDoc3M011 = dbSale.queryFromPool(stringSql);
    //
    String stringYearRoc = datetime.getYear(exeUtil.getDateConvertFullRoc(stringDateACS).replaceAll("/", ""));
    for (int intNo = 0; intNo < retDoc3M011.length; intNo++) {
      stringProjectID1 = retDoc3M011[intNo][0].trim();
      stringCostID = retDoc3M011[intNo][1].trim();
      stringCostID1 = retDoc3M011[intNo][2].trim();
      if ("".equals(stringCostID1)) {
        stringCostID1 = ("71,73,75,76,81,82,99,".indexOf(stringCostID) != -1) ? "0" : "1";
      }
      stringKey = stringYearRoc + "/" + stringProjectID1 + "--" + stringCostID + stringCostID1;
      stringMoney = retDoc3M011[intNo][3].trim();
      //
      stringMoney = "" + (exeUtil.doParseDouble("" + hashtableDoc3M011.get(stringKey)) + exeUtil.doParseDouble(stringMoney));
      // System.out.println("["+stringKey+"]--------------------["+stringMoney+"]") ;
      //
      hashtableDoc3M011.put(stringKey, convert.FourToFive(stringMoney, 0));
    }
    return hashtableDoc3M011;
  }
}
