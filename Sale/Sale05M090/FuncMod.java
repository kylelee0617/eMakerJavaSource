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
    System.out.println("chk==>" + getUser() + " , value==>�ק�");

    if (getUser() != null && getUser().toUpperCase().equals("B9999")) {
      messagebox("�ק��v�������\!!!");
      return false;
    }

    getButton("ButtonSSMediaID").doClick();
    getButton("ButtonSetSaleID").doClick();
    //
    System.out.println("�ק�------------------------------------S");
    JTable jtableTable1 = getTable(custTable);
    if (jtableTable1.getRowCount() == 0) {
      message("[�Ȥ���] ���i�ť�");
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

    // ����ˮ�
    JTable jtable6 = getTable(benTable);
    JTable jTable10 = getTable(angetTable);

    // �~���ˮ֥�
    String orderNo = getValue("field3").trim(); //�����D�t�~�@��orderNo�ƻ�ɭԦ�  �γo�ӥd��b
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
      String stringTel2 = ("" + getValueAt(custTable, intRow, "Tel2")).trim();// 2010-4-16 �s�W�q��2
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
      String recordType = "�Ȥ���";
      
      if(StringUtils.isBlank(stringCustomNo)) stringCustomNo = engNo;
      if(StringUtils.isBlank(stringCustomName)) stringCustomName = engName;

      // customNo
      if ("1".equals(stringNationality) && stringCustomNo.length() == 0) {// 20090414
        message("����:" + (intRow + 1) + "-[�νs/�����Ҹ�] ���i�ť�");
        return false;
      }

      // customName
      if (stringCustomName.length() == 0) {
        message("����:" + (intRow + 1) + "-[�q��m�W] ���i�ť�");
        return false;
      }

      if ("".equals(stringCountryName)) {
        messagebox("�� " + (intRow + 1) + " �C��[��O] �������J�C");
        jtableTable1.setRowSelectionInterval(intRow, intRow);
        return false;
      }

      if (!"".equals(stringBirthday) && stringBirthday.replace("/", "").length() != 8) {
        messagebox("�� " + (intRow + 1) + " �C��[�ͤ�/���U��] �������J�C");
        jtableTable1.setRowSelectionInterval(intRow, intRow);
        return false;
      }

      if (!"".equals(stringCellphone) && stringCellphone.length() != 10) {
        messagebox("�� " + (intRow + 1) + " �C��[��ʹq��] �j�p���� 10 �X�C");
        jtableTable1.setRowSelectionInterval(intRow, intRow);
        return false;
      }
      //
      if ("1".equals(stringNationality)) {
        if (stringCustomNo.length() == 0) {
          messagebox("�� " + (intRow + 1) + " �C��[�νs/�����Ҹ�] ���i�ť�!�C");
          jtableTable1.setRowSelectionInterval(intRow, intRow);
          return false;
        }
        if (stringCustomNo.length() != 8 && stringCustomNo.length() != 10) {
          messagebox("�� " + (intRow + 1) + " �C��[�νs/�����Ҹ�] ���׿��~!(����H)�C");
          jtableTable1.setRowSelectionInterval(intRow, intRow);
          return false;
        }
        if (stringCustomNo.length() == 8 && check.isCoId(stringCustomNo) == false) {
          messagebox("�� " + (intRow + 1) + " �C��[�νs/�����Ҹ�] �Τ@�s�����~!�C");
          jtableTable1.setRowSelectionInterval(intRow, intRow);
          return false;
        }
        if (stringCustomNo.length() == 10 && check.isID(stringCustomNo) == false) {
          messagebox("�� " + (intRow + 1) + " �C��[�νs/�����Ҹ�] �����Ҹ����~!�C");
          jtableTable1.setRowSelectionInterval(intRow, intRow);
          return false;
        }
      }
      if ("4".equals(stringNationality)) {
        if (stringCustomNo.length() != 9) {
          messagebox("�� " + (intRow + 1) + " �C��[�νs/�����Ҹ�] ���׿��~!�C");
          jtableTable1.setRowSelectionInterval(intRow, intRow);
          return false;
        }
      }

      stringSql = " SELECT  ZIP FROM  Town b WHERE  Coun   IN  (SELECT  Coun  FROM  City  WHERE  CounName='" + stringCity + "') " + "AND  TownName  =  '" + stringTown + "' ";
      String[][] retTown = dbDoc.queryFromPool(stringSql);
      if (retTown.length == 0) {
        message("�� " + (intRow + 1) + " �C��[����][�m��] ���Y�����T�C");
        jtableTable1.setRowSelectionInterval(intRow, intRow);
        return false;
      }
      if (!stringZIP.equals(retTown[0][0].trim())) {
        if (stringZIP.length() > 3) stringZIP = stringZIP.substring(0, 3);
        if (!stringZIP.equals(retTown[0][0].trim())) {
          messagebox("�� " + (intRow + 1) + " �C��[�l���ϸ�] �����T�C");
          jtableTable1.setRowSelectionInterval(intRow, intRow);
          return false;
        }
      }

      // [���%]
      if (stringPercentage.length() == 0) {
        message("�� " + (intRow + 1) + " �C��[���%] ���i�ť�");
        return false;
      }
      if (Float.parseFloat(stringPercentage.trim()) < 1) {
        message("�� " + (intRow + 1) + " �C��[���%] ���i�p�� 1");
        return false;
      }
      if (!stringStatusCd.equals("C")) floatPercentage = floatPercentage + Float.parseFloat(stringPercentage);
      //
      if (stringAddress.length() == 0) {
        message("�� " + (intRow + 1) + " �C��[�a�}] ���i�ť�");
        return false;
      }
      //
      if (stringTel.length() == 0) {
        message("�� " + (intRow + 1) + " �C��[�q��] ���i�ť�");
        return false;
      }

      // �~���ˮ�
      String indCode = ksUtil.getIndustryCodeByMajorName(majorName);
      String amlText = stringProjectID1 + "," + orderNo + "," + stringTrxDate + "," + funcName + "," + recordType + "," + stringCustomNo + "," + stringCustomName + ","
          + stringBirthday + "," + indCode + "," + stringCountryName + "," + countryName2 + "," + engName + "," + "query18";
      arrAMLs[amlCount] = amlText.split(",");
      amlCount++;
    }

    //
    if (floatPercentage != 100) {
      message("[���%] ������ 100");
      return false;
    }

    // �ˮ�-�����q�H ========================================
    System.out.println("�ˮ�-=====>");
    for (int intNo = 0; intNo < jtable6.getRowCount(); intNo++) {
      String strBCustomNo = ("" + getValueAt(benTable, intNo, "BCustomNo")).trim();
      String strBenName = ("" + getValueAt(benTable, intNo, "BenName")).trim();
      String strCountryName = ("" + getValueAt(benTable, intNo, "CountryName")).trim();
      String birthday = ("" + getValueAt(benTable, intNo, "Birthday")).trim();
      String recordType = "�����q�H���";

      if ("".equals(strBCustomNo)) {
        jtabbedpane1.setSelectedIndex(2);
        jtable6.setRowSelectionInterval(intNo, intNo);
        messagebox("[�����q�H���] �� " + (intNo + 1) + " �C�� [���q�H�m�W] ���i���ťաC");
        return false;
      }
      if ("".equals(strBenName)) {
        jtabbedpane1.setSelectedIndex(2);
        jtable6.setRowSelectionInterval(intNo, intNo);
        messagebox("[�����q�H���] �� " + (intNo + 1) + " �C�� [�����Ҹ�] ���i���ťաC");
        return false;
      }
      if ("".equals(strCountryName)) {
        jtabbedpane1.setSelectedIndex(2);
        jtable6.setRowSelectionInterval(intNo, intNo);
        messagebox("[�����q�H���] �� " + (intNo + 1) + " �C�� [��O] ���i���ťաC");
        return false;
      }

      // �~���ˮ�
      QueryLogBean qBean = ksUtil.getQueryLogByCustNoProjectId(stringProjectID1, strBCustomNo);
      if (qBean == null) {
        messagebox("�d�L[�����q�H���] �� " + (intNo + 1) + " �C���¦W���ơA�Х�����d�ߡC");
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

    // �ˮ�-�N�z�H
    System.out.println("�ˮ�-�N�z�H=====>");
    for (int c = 0; c < jTable10.getRowCount(); c++) {
      String custNo = ("" + getValueAt(angetTable, c, "ACustomNo")).trim();
      String custName = ("" + getValueAt(angetTable, c, "AgentName")).trim();
      String countryName = ("" + getValueAt(angetTable, c, "CountryName")).trim();
      String recordType = "�N�z�H���";

      // �~���ˮ�
      QueryLogBean qBean = ksUtil.getQueryLogByCustNoProjectId(stringProjectID1, custNo);
      if (qBean == null) {
        messagebox("�d�L[�N�z�H���] �� " + (c + 1) + " �C���¦W���ơA�Х�����d�ߡC");
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

    // �~���ˮ� >> ����
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
      message("[��O���] ���i�ť�");
      return false;
    }
    for (int intRow = 0; intRow < jtableTable2.getRowCount(); intRow++) {
      // [�ɼӧO]
      String stringPosition = jtableTable2.getValueAt(intRow, 3).toString();
      if (stringPosition.length() == 0) {
        message("�� " + (intRow + 1) + " �C��[�ɼӧO] ���i�ť�");
        jtableTable2.setRowSelectionInterval(intRow, intRow);
        jtabbedpane1.setSelectedIndex(0);
        return false;
      }
      //
      if (jtableTable2.getValueAt(intRow, 2).toString().length() == 0) {
        message("�� " + (intRow + 1) + " �C��[�Ш�] ���i�ť�");
        jtableTable2.setRowSelectionInterval(intRow, intRow);
        jtabbedpane1.setSelectedIndex(0);
        return false;
      }
      //
      if (jtableTable2.getValueAt(intRow, 4).toString().length() == 0) {
        message("�� " + (intRow + 1) + " �C��[�W��] ���i�ť�");
        jtableTable2.setRowSelectionInterval(intRow, intRow);
        jtabbedpane1.setSelectedIndex(0);
        return false;
      }
      //
      if (jtableTable2.getValueAt(intRow, 5).toString().length() == 0) {
        message("�� " + (intRow + 1) + " �C��[�P��] ���i�ť�");
        jtableTable2.setRowSelectionInterval(intRow, intRow);
        jtabbedpane1.setSelectedIndex(0);
        return false;
      }
      //
      if (jtableTable2.getValueAt(intRow, 6).toString().length() == 0) {
        message("�� " + (intRow + 1) + " �C��[���] ���i�ť�");
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
        messagebox("�P���M�Ϊ�� �� " + (intNo + 1) + " �C�� [�ϥνs��] ���s�b�C");
        return false;
      }
    }
    // ��P���� B3018 2012/09/17 S
    FargloryUtil exeUtil = new FargloryUtil();
    String stringDate = exeUtil.getDateConvert(getValue("field2").trim());
    String[][] retSale05M246 = null;
    if ("".equals(stringProjectID1)) {
      messagebox("[�קO] ���i���ťաC");
      getcLabel("field1").requestFocus();
      return false;
    }
    if (stringDate.length() != 10) {
      messagebox("[���]����榡���~(YYYY/MM/DD)�C");
      getcLabel("field2").requestFocus();
      return false;
    }
    if (!"".equals(stringSaleWay)) {
      // �s�b�ˮ�
      stringSql = "SELECT  Num,  PlanDateS,  PlanDateE " + " FROM  Sale05M246  " + " WHERE  ProjectID1  =  '" + stringProjectID1 + "' " + " AND  StrategyNo  =  '" + stringSaleWay
          + "' " + " AND  PlanDateS  <=  '" + stringDate + "' " + " AND  PlanDateE  >=  '" + stringDate + "' ";
      retSale05M246 = dbSale.queryFromPool(stringSql);
      if (retSale05M246.length == 0) {
        messagebox("[��P����]��ƿ��~�C");
        getcLabel("SaleWay").requestFocus();
        return false;
      }
      // �ƶq�ˮ�
      double doubleNum = exeUtil.doParseDouble(retSale05M246[0][0].trim());
      String stringPlanDateS = retSale05M246[0][1].trim();
      String stringPlanDateE = retSale05M246[0][2].trim();
      if (doubleNum > 0) {
        stringSql = "SELECT  ProjectID1 " + " FROM  Sale05M090 " + " WHERE  ProjectID1  =  '" + stringProjectID1 + "' " + " AND  SaleWay  =  '" + stringSaleWay + "' "
            + " AND  OrderNo  <>  '" + stringOrderNo + "' " + " AND  OrderDate  >=  '" + stringPlanDateS + "' " + " AND  OrderDate  <=  '" + stringPlanDateE + "' ";
        retSale05M246 = dbSale.queryFromPool(stringSql);
        if (exeUtil.doParseDouble("" + (retSale05M246.length + 1)) > doubleNum) {
          String stringStrategyName = exeUtil.getNameUnion("StrategyName", "Sale05M244", " AND  StrategyNo  = '" + stringSaleWay + "' ", new Hashtable(), dbSale);
          messagebox("�ʫ��ҩ��浧�ƶW�L��P����(" + stringStrategyName + ")�ҳ]�w��" + convert.FourToFive("" + doubleNum, 0) + " �����ƶq�C");
          getcLabel("SaleWay").requestFocus();
          return false;
        }
      }
    }
    // ��P���� B3018 2012/09/17 E
    // �C��N�X�ˮ� ���H�q 2010/05/25 S
    String stringSSMediaID = getValue("SSMediaID").trim();
    String stringSSMediaID1 = getValue("SSMediaID1").trim();
    if (!"H601A".equals(stringProjectID1)) { // �ק���:20170815 ���u�s��:B3774
      if ("".equals(stringSSMediaID)) {
        // messagebox("[�C��N�X] ���i���ťաC") ;
        // return false ;
      } else {
        String[][] retMediaSS = dbSale.queryFromPool(" SELECT  SSMediaName  FROM  Media_SS  WHERE  SSMediaID=  '" + stringSSMediaID + "'  AND  Stop  =  'N' ");
        if (retMediaSS.length == 0) {
          messagebox("[�C��N�X] ���s�b��Ʈw���C");
          return false;
        }
      }
    } // �ק���:20170815 ���u�s��:B3774
    // 2015-12-10 B3018 ��X�H�ˮ� S
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
      messagebox("[��X�H���] ���i�L��ơC");
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
        messagebox("[��X�H���] �� " + (intNo + 1) + " �C�� [�P��(���)-���s] ���i���ťաC");
        return false;
      }
      if ("".equals(stringSaleName1)) {
        jTabbedPane1.setSelectedIndex(1);
        jtable9.setRowSelectionInterval(intNo, intNo);
        messagebox("[��X�H���] �� " + (intNo + 1) + " �C�� [�P��(���)-��X�H] ���i���ťաC");
        return false;
      }
      if ("H601A".equals(stringProjectID1)) continue; // �ק���:20170815 ���u�s��:B3774

      if ("".equals(stringCSSaleID2)) {
        jTabbedPane1.setSelectedIndex(1);
        jtable9.setRowSelectionInterval(intNo, intNo);
        messagebox("[��X�H���] �� " + (intNo + 1) + " �C�� [�����H��-���s] ���i���ťաC");
        return false;
      }
      if (!"".equals(stringCSSaleID2) && "".equals(stringCSSaleName2)) {
        jTabbedPane1.setSelectedIndex(1);
        jtable9.setRowSelectionInterval(intNo, intNo);
        messagebox("[��X�H���] �� " + (intNo + 1) + " �C�� [�����H��-��X�H] ���i���ťաC");
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
        messagebox("[�ذe���] �� " + (intNo + 1) + " �C�� [�ƶq] ���i���ťաC");
        return false;
      }
    }
    // 2015-12-10 B3018 ��X�H�ˮ� E
    // �C��N�X�ˮ� 2010/05/25 E

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
      // �~�D�O
      if ("".equals(stringComNo)) {
        jtable12.setRowSelectionInterval(intNo, intNo);
        jtabbedpane1.setSelectedIndex(3);
        messagebox("���q�U���� " + (intNo + 1) + " �椧 [�~�D�O] ���i���ťաC");
        return false;
      }
      stringSqlAnd = " AND  ISNULL(COMPANY_CD,'')  <>  ''  " + " AND  Com_No  =  '" + stringComNo + "' " + " AND Com_No IN (SELECT  distinct H_COM " + " FROM  Sale05M040 "
          + " WHERE  ProjectID1  =  '" + stringProjectID1 + "' " + " UNION " + " SELECT  distinct  L_COM " + " FROM  Sale05M040 " + " WHERE  ProjectID1  =  '" + stringProjectID1
          + "' " + " ) ";
      vectorACom = exeUtil.getQueryDataHashtable("A_Com", new Hashtable(), stringSqlAnd, dbSale);
      if (vectorACom.size() == 0) {
        jtable12.setRowSelectionInterval(intNo, intNo);
        jtabbedpane1.setSelectedIndex(3);
        messagebox("���q�U���� " + (intNo + 1) + " �椧 [�~�D�O] ���s�b��Ʈw���C");
        return false;
      }
      // ���q�U���O
      if ("".equals(stringComLoadDate)) {
        jtable12.setRowSelectionInterval(intNo, intNo);
        jtabbedpane1.setSelectedIndex(3);
        messagebox("���q�U���� " + (intNo + 1) + " �椧 [���q�U���O] ���i���ťաC");
        return false;
      }
      stringComLoadDate = exeUtil.getDateAC(stringComLoadDate, "���q�U���O");
      if (stringComLoadDate.length() != 10) {
        jtable12.setRowSelectionInterval(intNo, intNo);
        jtabbedpane1.setSelectedIndex(3);
        messagebox("���q�U���� " + (intNo + 1) + " �椧 [���q�U���O] ����榡(YYYY/mm/dd)���~�C");
        return false;
      }
      // �U���������B
      if (exeUtil.doParseDouble(stringPrincipalAmt) <= 0) {
        jtable12.setRowSelectionInterval(intNo, intNo);
        jtabbedpane1.setSelectedIndex(3);
        messagebox("���q�U���� " + (intNo + 1) + " �椧 [�U���������B] ���i�� 0�C");
        return false;
      }
      doublePrincipalAmt += exeUtil.doParseDouble(stringPrincipalAmt);

      // �Q����I�覡
      if ("".equals(stringInterestKind)) {
        jtable12.setRowSelectionInterval(intNo, intNo);
        jtabbedpane1.setSelectedIndex(3);
        messagebox("���q�U���� " + (intNo + 1) + " �椧 [�Q����I�覡] ���i���ťաC");
        return false;
      }
    }
    // �ˮ֤��q�U�`�B �n����U���������B �[�`
    doublePrincipalAmt = exeUtil.doParseDouble(convert.FourToFive("" + doublePrincipalAmt, 4));
    doubleComLoadMoney = exeUtil.doParseDouble(convert.FourToFive(stringComLoadMoney, 4));
    if (doublePrincipalAmt != doubleComLoadMoney) {
      jtabbedpane1.setSelectedIndex(3);
      getcLabel("ComLoadMoney").requestFocus();
      messagebox("[���q�U�`�B] ������ ���q�U��椧�U���������B �[�`�C");
      return false;
    }
    if ("B3018".equals(getUser())) {
      messagebox("����");
      return false;
    }
    message("");
    put("TrustAccountNo", value);
    getButton("ButtonTrustAccountNo").doClick();
    setValue("actionText", "�ק�");

    // �ˮ֪k�H-���q�H���Y���T
    if (this.checkHasBen(jtableTable1, jtable6) == false) return false;

    // �ˬd���q�H���
    // 20210610 Kyle update : �אּ�ϥεܴ��t��
    getButton("CheckBensAML18").doClick();
    String amlText = getValue("AMLText").trim();

    System.out.println(">>>CheckBensAML18 amlText:" + amlText);
    if (StringUtils.isNotBlank(getValue("AMLText"))) return false;

   // ��s�����q�H��
    put("UpdBen_RS", "N");
    getButton("updateBen").doClick();
    if ( !StringUtils.equals(get("UpdBen_RS").toString().trim(), "Y") ) return false;
    System.out.println("updateBen=====> Done");
    // ��s���q�H��T end

    System.out.println("�ק�------------------------------------E");
    return true;
  }

  // �ˬd�k�H�P���q�HMATCH
  public boolean checkHasBen(JTable customs, JTable bCustoms) throws Throwable {
    System.out.println(">>>�ˬd�k�H�O�_�����q�H<<<");

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
        // System.out.println(">>>�k�H!!<<<") ;
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
          messagebox("�Ы��w�k�H [" + cName + "]  �����Ĺ����q�H�C");
          return false; // ���q�H�M�椤�䤣�����ID
        }
      }
    }
    return true;
  }

  public String getInformation() {
    return "---------------\u4fee\u6539\u6309\u9215\u7a0b\u5f0f.preProcess()----------------";
  }

}
