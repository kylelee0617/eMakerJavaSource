package Doc.Doc2;

import jcx.jform.bTransaction;
import java.io.*;
import java.util.*;
import java.util.regex.Pattern;
import jcx.util.*;
import jcx.html.*;
import jcx.db.*;
import javax.swing.*;
import javax.swing.table.*;
import com.jacob.activeX.*;
import com.jacob.com.*;
import Farglory.util.FargloryUtil;
import Doc.Doc2M010;

public class Doc2M010Check extends bTransaction {
  // isAssetOK �T���ˮ�
  public boolean action(String value) throws Throwable {
    // 201808check BEGIN
    System.out.println("chk==>" + getUser() + " , action value==>" + value.trim());
    if (value.trim().equals("�s�W") || value.trim().equals("�ק�") || value.trim().equals("�R��")) {
      if (getUser() != null && getUser().toUpperCase().equals("B9999")) {
        messagebox(value.trim() + "�v�������\!!!");
        return false;
      }
    }
    // 201808check FINISH
    Doc2M010 exeFun = new Doc2M010();
    FargloryUtil exeUtil = new Farglory.util.FargloryUtil();
    String stringSubject = getFunctionName();
    String stringSend = "emaker@farglory.com.tw";
    String[] arrayUser = { "B3018@farglory.com.tw" };
    String stringBarCodeE = getValue("BarCode").trim();
    String stringBarCodeOldE = getValue("BarCodeOld").trim();
    String stringMessage = stringSubject + "(" + value.trim() + ")-----" + stringBarCodeE + "-----------" + stringBarCodeOldE + "<br>";
    try {
      getButton("ButtonHalfWidth").doClick(); // ��b���B�z
      getButton("ButtonTable22").doClick(); // �q���N�X�����B�z �w�]�� �� ���B�B�z
      if ("SYS".equals(getUser())) {
        doSyncBarCode();
        return true;
      }
      put("Doc2M010_STATUS", "TEST");
      System.out.println("--------------------isFlowCheckOK   S");
      if (!isFlowCheckOK(value.trim(), exeUtil, exeFun)) {
        put("Doc6M010_ReceiptKind", "null");
        put("Doc2M010_STATUS", "null");
        return false;
      }
      System.out.println("--------------------isBatchCheckOK   S");
      if (stringSubject.indexOf("�H�`") == -1 && !isBatchCheckOK(value.trim(), exeUtil, exeFun)) {
        put("Doc2M010_STATUS", "null");
        //
        exeFun.doDeleteDBDoc("Doc2M044_AutoBarCode", new Hashtable(), " AND  BarCode  =  '" + getValue("BarCode").trim() + "' ", true, exeUtil);
        return false;
      }
      System.out.println("--------------------isBatchCheckOK   E");
      // �y�{
      System.out.println("--------------------isCheckDoc2M048   S");
      if (stringSubject.indexOf("�H�`") == -1 && !isCheckDoc2M048(value.trim(), exeUtil, exeFun)) {
        put("Doc6M010_ReceiptKind", "null");
        put("Doc2M010_STATUS", "null");
        //
        exeFun.doDeleteDBDoc("Doc2M044_AutoBarCode", new Hashtable(), " AND  BarCode  =  '" + getValue("BarCode").trim() + "' ", true, exeUtil);
        return false;
      }
      setFlow(value);
      System.out.println("--------------------isFlowCheckOK   E");
      /*
       * if("B3018".equals(getUser())) { put("Doc6M010_ReceiptKind", "null") ;
       * put("Doc2M010_STATUS", "null") ; return false ; }
       */
      //
      String stringFlow = getFunctionName();
      String stringID = getValue("ID").trim();
      String stringBarCode = getValue("BarCodeOld").trim();
      if ("�R��".equals(value.trim())) {
        exeFun.doDeleteDoc1M040(stringBarCode);
        exeFun.doDeleteDoc1M030(stringBarCode);
        // ��P�t�ΦP�B
        exeFun.doDeleteCiReaMM(stringBarCode);
        exeFun.doDeleteCoReaMM(stringBarCode);
        // �h�O�d���٭����
        doRetainBarCode(exeUtil, exeFun);
        // �q���]��
        doMail(exeUtil, exeFun);
        //
        exeFun.doDeleteDBDoc("Doc2M044_AutoBarCode", new Hashtable(), " AND  BarCode  =  '" + stringBarCode + "' ", true, exeUtil);
      } else {
        // if(stringSubject.indexOf("�H�`")==-1) doCheckDulFactoryNo(exeFun) ;
        // �I�ڱ����s
        String stringComNo = getValue("ComNo").trim();
        String stringPurchaseNo1 = "";
        String stringPurchaseNo2 = "";
        String stringPurchaseNo3 = "";
        String stringPurchaseNo4 = "";
        String stringPayCondition1 = getValue("PayCondition1").trim();
        String stringPayCondition2 = getValue("PayCondition2").trim();
        JTable jtable6 = getTable("Table6");
        //
        System.out.println("doSetPurchaseEnd--------------------------------S");
        doSetPurchaseEnd(exeUtil, exeFun);
        System.out.println("doSetPurchaseEnd--------------------------------E");
        if (stringFlow.indexOf("�ӿ�") != -1 && "�s�W".equals(value.trim())) {
          if (stringFlow.indexOf("--�ӿ�") == -1) {
            stringBarCode = exeFun.getMaxBarCode("Z");
            setValue("BarCode", stringBarCode);
          }
          stringID = exeFun.getMaxIDForDoc2M010();
          setValue("ID", stringID);
        }
        doSyncBarCode();
      }
      put("Doc6M010_ReceiptKind", "null");
      put("Doc2M010_STATUS", "null");
      //
      System.out.println("--------------------------�y�{�O��");
      String stringDeptCd = "";
      String stringUser = getUser().toUpperCase();
      String stringDocNo = getValue("DocNo1").trim() + getValue("DocNo2").trim() + getValue("DocNo3").trim();
      String stringDescript = getValue("Descript").trim();
      String stringToday = datetime.getToday("yymmdd");
      String[][] retFE3D103 = exeFun.getFE3D103(stringUser, "", stringToday);
      //
      if (retFE3D103.length > 0) stringDeptCd = retFE3D103[0][0].trim();
      //
      stringDescript = convert.replace(stringDescript, "'", "''");
      exeFun.doInsertForDoc2M010History(stringID, datetime.getTime("YYYY/mm/dd h:m:s"), stringUser, stringDeptCd,
          getFunctionName() + " " + value + "---" + stringDocNo + "---" + stringBarCodeE + "---" + stringBarCodeOldE, stringDescript, true);
      // �J�`�B�z
      if (stringFlow.indexOf("�J�`") != -1) {
        if ("�s�W".equals(value.trim())) {
          Vector vectorBarCode = (Vector) get("put_Doc2M010_VectorBarCode");
          vectorBarCode.add(stringBarCode);
        } else if ("�R��".equals(value.trim())) {
          Vector vectorBarCode = (Vector) get("put_Doc2M010_VectorBarCode");
          vectorBarCode.remove(stringBarCode);
        }
      }
      //
      doReSetBarCode(exeUtil, exeFun);
      if (!"�R��".equals(value.trim())) getButton("Button11").doClick();
    } catch (Exception e) {
      Vector vectorUse = exeFun.getEmployeeNoDoc3M011("P", "");
      arrayUser = (String[]) vectorUse.toArray(new String[0]);
      exeUtil.doEMail(stringSubject, stringMessage + "(���u�s���G" + getUser() + ")<br>" + e.toString(), stringSend, arrayUser);
      messagebox("�{���o�Ϳ��~");
      //
      exeFun.doDeleteDBDoc("Doc2M044_AutoBarCode", new Hashtable(), " AND  BarCode  =  '" + getValue("BarCode").trim() + "' ", true, exeUtil);
      return false;
    }
    doLimitPayCondition();
    if ("B3849,B3446,".indexOf(getUser().toUpperCase()) == -1) exeUtil.ClipCopy(getValue("BarCode").trim());
    /*
     * if("B3018".equals(getUser().toUpperCase())) { return false ; }
     */
    return true;
  }

  public void doSetPurchaseEnd(FargloryUtil exeUtil, Doc2M010 exeFun) throws Throwable {
    String stringUser = getUser();
    String stringToday = datetime.getToday("YYYY/mm/dd");
    String stringFunctionName = getFunctionName();
    if (stringFunctionName.indexOf("ñ��") != -1) return;
    JTable jtable6 = getTable("Table6");
    String stringComNo = getValue("ComNo").trim();
    String stringPurchaseNo = "";
    String stringPurchaseNo1 = "";
    String stringPurchaseNo2 = "";
    String stringPurchaseNo3 = "";
    String stringFactoryNo = "";
    String stringPurchaseMoney = "";
    String stringBarCode = getValue("BarCodeOld").trim();
    String stringDocNo = getValue("DocNo").trim();
    String stringBarCodePur = "";
    String stringPurchaseNoExist = getValue("PurchaseNoExist").trim();
    if (!"Y".equals(stringPurchaseNoExist)) return;
    boolean booleanShow = false;
    boolean booleanOK = false;
    boolean booleanNumOK = false;
    boolean booleanFlag = true;
    Hashtable hashtableAnd = new Hashtable();
    double doubleContactMoney = 0;
    double doubleUseMoney = 0;
    //
    // if(!stringDocNo.startsWith("153") && !stringDocNo.startsWith("053") &&
    // !stringDocNo.startsWith("133") && !stringDocNo.startsWith("033")) return ;
    //
    for (int intNo = 0; intNo < jtable6.getRowCount(); intNo++) {
      stringPurchaseNo1 = ("" + getValueAt("Table6", intNo, "PurchaseNo1")).trim();
      stringPurchaseNo2 = ("" + getValueAt("Table6", intNo, "PurchaseNo2")).trim();
      stringPurchaseNo3 = ("" + getValueAt("Table6", intNo, "PurchaseNo3")).trim();
      stringFactoryNo = ("" + getValueAt("Table6", intNo, "FactoryNo")).trim();
      stringPurchaseMoney = ("" + getValueAt("Table6", intNo, "PurchaseMoney")).trim();
      stringPurchaseNo = stringPurchaseNo1 + stringPurchaseNo2 + stringPurchaseNo3;
      booleanOK = false;
      //
      hashtableAnd.put("ComNo", stringComNo);
      hashtableAnd.put("DocNo", stringPurchaseNo);
      stringBarCodePur = exeFun.getNameUnionDoc("BarCode", "Doc3M011", "", hashtableAnd, exeUtil);
      // �w�������B�A�����ϥΪ̧P�_
      booleanFlag = isMoneyOK(stringBarCodePur, stringPurchaseNo, stringFactoryNo, stringBarCode, stringPurchaseMoney, exeUtil, exeFun);
      System.out.println(intNo + "���B(" + booleanFlag + ")-----------------------------------------");
      if (booleanFlag) {
        continue;
      }
      // �w�����ƶq �w�]���סA�����ϥΪ̧P�_
      System.out.println(intNo + "isNumOK�ƶq-----------------------------------------S");
      booleanFlag = isNumOK(stringBarCodePur, stringBarCode, stringPurchaseNo, stringFactoryNo, exeUtil, exeFun);
      System.out.println(intNo + "isNumOK�ƶq(" + booleanFlag + ")-----------------------------------------E");
      if (booleanFlag) {
        booleanNumOK = true;
        continue;
      }
      booleanShow = true;
    }
    if (!booleanShow) {
      if (booleanNumOK) setValue("PurchaseEnd", "Y");
      return;
    }
    //
    showDialog("�д�-���פ��ϥ�(Doc2I010)", "", false, true, 30, 120, 590, 290);
  }

  public boolean isNumOK(String stringBarCodePur, String stringBarCode, String stringPurchaseNo, String stringFactoryNo, FargloryUtil exeUtil, Doc2M010 exeFun) throws Throwable {
    String stringActualNum = "";
    String stringRecordNo12 = "";
    String stringUnit = "";
    String stringControlType = "" + get("ONLY_CONTROL_AMT");
    Hashtable hashtableAnd = new Hashtable();
    Hashtable hashtableDoc3M012 = new Hashtable();
    Vector vectorDoc3M012 = null;
    double doubleActualNum = 0;
    double doubleUseNum = 0;
    //
    hashtableAnd.put("BarCode", stringBarCodePur);
    hashtableAnd.put("FactoryNo", stringFactoryNo);
    vectorDoc3M012 = exeFun.getQueryDataHashtableDoc("Doc3M012", hashtableAnd, "", new Vector(), exeUtil);
    for (int intNo = 0; intNo < vectorDoc3M012.size(); intNo++) {
      hashtableDoc3M012 = (Hashtable) vectorDoc3M012.get(intNo);
      if (hashtableDoc3M012 == null) continue;
      stringActualNum = "" + hashtableDoc3M012.get("ActualNum");
      stringUnit = "" + hashtableDoc3M012.get("Unit");
      stringRecordNo12 = "" + hashtableDoc3M012.get("RecordNo");
      // �S���줣�@�ƶq����
      System.out.println("stringControlType(" + stringControlType + ")stringUnit(" + stringUnit + ")-------------------------------------");
      if (("," + stringControlType + ",").indexOf("," + stringUnit + ",") != -1) {
        return false;
      }
      doubleActualNum = exeUtil.doParseDouble(stringActualNum);
      doubleUseNum = getUseNum(stringBarCode, stringPurchaseNo, stringFactoryNo, stringRecordNo12, exeUtil, exeFun);
      System.out.println("doubleActualNum(" + doubleUseNum + ")doubleUseNum(" + doubleActualNum + ")-------------------------------------");
      if (doubleActualNum != doubleUseNum) return false;
    }
    return true;
  }

  public double getUseNum(String stringBarCode, String stringPurchaseNo, String stringFactoryNo, String stringRecordNo12, FargloryUtil exeUtil, Doc2M010 exeFun) throws Throwable {
    JTable jtable9 = getTable("Table9");
    String stringSql = "";
    String stringComNo = getValue("ComNo").trim();
    String[][] retDoc2M017 = null;
    String[][] retDoc6M010 = null;
    double doubleUseNum = 0;
    // �д�
    stringSql = " SELECT  SUM(RequestNum) " + " FROM  Doc2M010 M10,  Doc2M0171 M17 " + " WHERE  M10.BarCode  =  M17.BarCode " + " AND  M10.UNDERGO_WRITE  <>  'E' "
        + " AND  M10.BarCode  <>  '" + stringBarCode + "' " + " AND  M10.ComNo  =  '" + stringComNo + "' " + " AND  M17.PurchaseNo  =  '" + stringPurchaseNo + "' "
        + " AND  M17.FactoryNo  =  '" + stringFactoryNo + "' " + " AND  M17.RecordNo12  =  '" + stringRecordNo12 + "' ";
    retDoc2M017 = exeFun.getTableDataDoc(stringSql);
    doubleUseNum = exeUtil.doParseDouble(retDoc2M017[0][0]);
    // �ɴ�
    stringSql = " SELECT  SUM(RequestNum) " + " FROM  Doc6M010 M10,  Doc6M0171 M17 " + " WHERE  M10.BarCode  =  M17.BarCode " + " AND  M10.UNDERGO_WRITE  <>  'E' "
        + " AND  M10.KindNo  =  '26' " + " AND  M10.BarCode  <>  '" + stringBarCode + "' " + " AND  M10.ComNo  =  '" + stringComNo + "' " + " AND  M17.PurchaseNo  =  '"
        + stringPurchaseNo + "' " + " AND  M17.FactoryNo  =  '" + stringFactoryNo + "' " + " AND  M17.RecordNo12  =  '" + stringRecordNo12 + "' ";
    retDoc6M010 = exeFun.getTableDataDoc(stringSql);
    doubleUseNum += exeUtil.doParseDouble(retDoc6M010[0][0]);
    // ����
    String stringRecordNo12L = "";
    String stringPurchaseNoL = "";
    String stringFactoryNoL = "";
    String stringRequestNumL = "";
    for (int intNo = 0; intNo < jtable9.getRowCount(); intNo++) {
      stringRecordNo12L = ("" + getValueAt("Table9", intNo, "RecordNo12")).trim();
      stringPurchaseNoL = ("" + getValueAt("Table9", intNo, "PurchaseNo")).trim();
      stringFactoryNoL = ("" + getValueAt("Table9", intNo, "FactoryNo")).trim();
      stringRequestNumL = ("" + getValueAt("Table9", intNo, "RequestNum")).trim();
      //
      if (!stringRecordNo12.equals(stringRecordNo12L)) continue;
      if (!stringPurchaseNo.equals(stringPurchaseNoL)) continue;
      if (!stringFactoryNo.equals(stringFactoryNoL)) continue;
      //
      doubleUseNum += exeUtil.doParseDouble(stringRequestNumL);
    }
    return exeUtil.doParseDouble(convert.FourToFive("" + doubleUseNum, 0));
  }

  public boolean isMoneyOK(String stringBarCodePur, String stringPurchaseNo, String stringFactoryNo, String stringBarCode, String stringPurchaseMoney, FargloryUtil exeUtil,
      Doc2M010 exeFun) throws Throwable {
    String stringComNo = getValue("ComNo").trim();
    double doubleContactMoney = getContactMoney(stringBarCodePur, stringFactoryNo, exeUtil, exeFun);// �X�����B
    double doubleUseMoney = getUseMoney(stringBarCode, stringComNo, stringPurchaseNo, stringFactoryNo, exeUtil, exeFun) + exeUtil.doParseDouble(stringPurchaseMoney);
    doubleUseMoney = exeUtil.doParseDouble(convert.FourToFive("" + doubleUseMoney, 0));
    if (doubleContactMoney == doubleUseMoney) {
      return true;
    }
    return false;
  }

  public double getContactMoney(String stringBarCodePur, String stringFactoryNo, FargloryUtil exeUtil, Doc2M010 exeFun) throws Throwable {
    String stringPurchaseMoney = "";
    Hashtable hashtableAnd = new Hashtable();
    Hashtable hashtableDoc3M012 = new Hashtable();
    Vector vectorDoc3M012 = null;
    double doublePurchaseMoney = 0;
    //
    hashtableAnd.put("BarCode", stringBarCodePur);
    hashtableAnd.put("FactoryNo", stringFactoryNo);
    vectorDoc3M012 = exeFun.getQueryDataHashtableDoc("Doc3M012", hashtableAnd, "", new Vector(), exeUtil);
    for (int intNo = 0; intNo < vectorDoc3M012.size(); intNo++) {
      hashtableDoc3M012 = (Hashtable) vectorDoc3M012.get(intNo);
      if (hashtableDoc3M012 == null) continue;
      stringPurchaseMoney = "" + hashtableDoc3M012.get("PurchaseMoney");
      //
      doublePurchaseMoney += exeUtil.doParseDouble(stringPurchaseMoney);
    }
    return exeUtil.doParseDouble(convert.FourToFive("" + doublePurchaseMoney, 0));
  }

  public double getUseMoney(String stringBarCode, String stringComNo, String stringPurchaseNo, String stringFactoryNo, FargloryUtil exeUtil, Doc2M010 exeFun) throws Throwable {
    String stringSql = "";
    String[][] retDoc2M017 = null;
    String[][] retDoc6M010 = null;
    double doubleUseMoney = 0;
    // �д�
    stringSql = " SELECT  SUM(PurchaseMoney) " + " FROM  Doc2M010 M10,  Doc2M017 M17 " + " WHERE  M10.BarCode  =  M17.BarCode " + " AND  M10.UNDERGO_WRITE  <>  'E' "
        + " AND  M10.BarCode  <>  '" + stringBarCode + "' " + " AND  M10.ComNo  =  '" + stringComNo + "' " + " AND  M17.PurchaseNo  =  '" + stringPurchaseNo + "' "
        + " AND  M17.FactoryNo  =  '" + stringFactoryNo + "' ";
    retDoc2M017 = exeFun.getTableDataDoc(stringSql);
    doubleUseMoney = exeUtil.doParseDouble(retDoc2M017[0][0]);
    // �ɴ�
    stringSql = " SELECT  SUM(BorrowMoney) " + " FROM  Doc6M010" + " WHERE  UNDERGO_WRITE  <>  'E' " + " AND  KindNo  =  '26' " + " AND  BarCode  <>  '" + stringBarCode + "' "
        + " AND  ComNo  =  '" + stringComNo + "' " + " AND  PurchaseNo  =  '" + stringPurchaseNo + "' " + " AND  FactoryNo  =  '" + stringFactoryNo + "' ";
    retDoc6M010 = exeFun.getTableDataDoc(stringSql);
    doubleUseMoney += exeUtil.doParseDouble(retDoc6M010[0][0]);
    return exeUtil.doParseDouble(convert.FourToFive("" + doubleUseMoney, 0));
  }

  public void doRetainBarCode(FargloryUtil exeUtil, Doc2M010 exeFun) throws Throwable {
    // �h�O�d�ڳB�z
    String stringBarCode = getValue("BarCodeOld").trim();
    String stringRetainBarCode = "";
    Hashtable hashtableAnd = new Hashtable();
    Hashtable hashtableData = new Hashtable();
    //
    if ("".equals(stringBarCode)) return;
    //
    hashtableAnd.put("BarCode", stringBarCode);
    stringRetainBarCode = exeFun.getNameUnionDoc("RetainBarCode", "Doc2M010", "", hashtableAnd, exeUtil);
    if ("".equals(stringRetainBarCode)) return;
    //
    hashtableAnd.put("BarCode", stringRetainBarCode);
    hashtableData.put("DocClose", "N");
    exeFun.doUpdateDBDoc("Doc2M010", "", hashtableData, hashtableAnd, true, exeUtil);
  }

  // �קO-�ҥ~
  public boolean isProjetIDExcept() throws Throwable {
    String stringDocNo = getValue("DocNo").trim();
    boolean booleanProjetIDExcept = false;
    //
    booleanProjetIDExcept = (",033H90A10311002,033110310005,033110310003,033H99A10310013,033H90A10310002,"
        + "033H90A10310003,033H92A10310002,033H90A10309014,033H96A10310002,033H96A10311003," + "033H92A10311003,033H99A10311018,033110311005,023210312015,033H90A10312004,"
        + "033H90A10312005,").indexOf(stringDocNo) != -1;
    return booleanProjetIDExcept;
  }

  // �ҥ~-���X�s�����s�]�w
  public void doReSetBarCode(FargloryUtil exeUtil, Doc2M010 exeFun) throws Throwable {
    String stringBarCode = getValue("BarCode").trim();
    String stringBarCodeOld = getValue("BarCodeOld").trim();
    Vector vectorDoc2M040 = new Vector();
    Hashtable hashtableAnd = new Hashtable();
    Hashtable hashtableTemp = new Hashtable();
    Hashtable hashtableData = new Hashtable();
    //
    vectorDoc2M040 = exeFun.getQueryDataHashtableDoc("Doc2M040", hashtableAnd, " AND  1=1 ", new Vector(), exeUtil);
    if (vectorDoc2M040.size() == 0) return;
    //
    hashtableTemp = (Hashtable) vectorDoc2M040.get(0);
    if (hashtableTemp == null) return;
    //
    if ("".equals(stringBarCodeOld)) return;
    if (stringBarCodeOld.equals(stringBarCode)) return;
    //
    // ��P-�дڥӽЮѤ��L���ʤ��i�ϥίS�w�дڥN�X���ҥ~
    String stringBarCodeDoc2M010 = "" + hashtableTemp.get("BarCodeDoc2M010");
    if (!"".equals(stringBarCodeDoc2M010) && !"null".equals(stringBarCodeDoc2M010)) {
      if (stringBarCodeDoc2M010.indexOf(stringBarCodeOld) != -1) {
        hashtableAnd.put("BarCodeDoc2M010", stringBarCodeDoc2M010);
        hashtableData.put("BarCodeDoc2M010", stringBarCodeDoc2M010 + "," + stringBarCode);
        exeFun.doUpdateDBDoc("Doc2M040", "", hashtableData, hashtableAnd, true, exeUtil);
      }
    }
    // �дڥӽЮѳ�@���ʳ�ҥ~
    String stringBarCodeDoc5M020 = "" + hashtableTemp.get("BarCodeDoc5M020");
    if (!"".equals(stringBarCodeDoc5M020) && !"null".equals(stringBarCodeDoc5M020)) {
      if (stringBarCodeDoc5M020.indexOf(stringBarCodeOld) != -1) {
        hashtableAnd.put("BarCodeDoc5M020", stringBarCodeDoc5M020);
        hashtableData.put("BarCodeDoc5M020", stringBarCodeDoc5M020 + "," + stringBarCode);
        exeFun.doUpdateDBDoc("Doc2M040", "", hashtableData, hashtableAnd, true, exeUtil);
      }
    }
    // �w��ҥ~
    String stringBarCodeDoc3M011 = "" + hashtableTemp.get("BarCodeDoc3M011");
    if (!"".equals(stringBarCodeDoc3M011) && !"null".equals(stringBarCodeDoc3M011)) {
      if (stringBarCodeDoc3M011.indexOf(stringBarCodeOld) != -1) {
        hashtableAnd.put("BarCodeDoc3M011", stringBarCodeDoc3M011);
        hashtableData.put("BarCodeDoc3M011", stringBarCodeDoc3M011 + "," + stringBarCode);
        exeFun.doUpdateDBDoc("Doc2M040", "", hashtableData, hashtableAnd, true, exeUtil);
      }
    }
  }

  public void doErrorEmail(String stringContentText) throws Throwable {
    String stringSend = "emaker@farglory.com.tw";
    String stringSubject = "����|�P�_���~";
    String[] arrayUser = { "B3018@farglory.com.tw" };
    (new Farglory.util.FargloryUtil()).doEMail(stringSubject, stringContentText, stringSend, arrayUser);
  }

  public boolean isCheckDoc2M048(String stringAction, FargloryUtil exeUtil, Doc2M010 exeFun) throws Throwable {
    String stringCDate = exeUtil.getDateConvert(getValue("CDate").trim());
    String stringFactoryNo = getValue("FactoryNoSpec").trim();
    String[] arrayTable = { "Table1", "Table3" };
    JTable jtableUse = null;
    //
    if ("".equals(stringFactoryNo)) {
      String stringTableName = "";
      for (int intNo = 0; intNo < arrayTable.length; intNo++) {
        stringTableName = arrayTable[intNo].trim();
        jtableUse = getTable(stringTableName);
        if (jtableUse.getRowCount() > 0) {
          stringFactoryNo = "" + getValueAt(stringTableName, 0, "FactoryNo");
          break;
        }
      }
    }
    //
    String stringCostID = "";
    String stringCostID1 = "";
    String stringKey = "";
    String stringRealTotalMoney = "";
    Vector vectorKey = new Vector();
    double doubleMoney = 0;
    double doubleMoneyCF = 0;
    double doubleMoneyThis = 0;
    Hashtable hashtableThisRealTotalMoney = new Hashtable();
    //
    jtableUse = getTable("Table2");
    for (int intNo = 0; intNo < jtableUse.getRowCount(); intNo++) {
      stringCostID = "" + getValueAt("Table2", intNo, "CostID");
      stringCostID1 = "" + getValueAt("Table2", intNo, "CostID1");
      stringRealTotalMoney = "" + getValueAt("Table2", intNo, "RealTotalMoney");
      stringKey = stringCostID + "---" + stringCostID1;
      //
      if (vectorKey.indexOf(stringKey) == -1) vectorKey.add(stringKey);
      //
      doubleMoney = exeUtil.doParseDouble(stringRealTotalMoney) + exeUtil.doParseDouble("" + hashtableThisRealTotalMoney.get(stringKey));
      hashtableThisRealTotalMoney.put(stringKey, "" + doubleMoney);

    }
    String stringEDateTime = getValue("EDateTime").trim();
    String[] arrayTemp = null;
    String[][] retDoc2m048 = null;
    for (int intNo = 0; intNo < vectorKey.size(); intNo++) {
      stringKey = "" + vectorKey.get(intNo);
      arrayTemp = convert.StringToken(stringKey, "---");
      if (arrayTemp.length != 2) continue;
      stringCostID = arrayTemp[0].trim();
      stringCostID1 = arrayTemp[1].trim();
      doubleMoneyThis = exeUtil.doParseDouble("" + hashtableThisRealTotalMoney.get(stringKey));
      // �s�b
      // 0 CostID 1 CostID1 2 LimitDateS 3 LimitDateE 4 FactoryNo 5 RealTotalMoney
      retDoc2m048 = exeFun.getDoc2m048(stringCostID, stringCostID1, stringCDate, stringFactoryNo);
      if (retDoc2m048.length == 0) continue;
      // ���B�ˬd
      if ("�R��".equals(stringAction)) {
        // ���s�b������B
        doubleMoneyCF = exeFun.getUseMoneyAboutDoc2M048(stringCostID, stringCostID1, retDoc2m048[0][2].trim(), retDoc2m048[0][3].trim(), stringFactoryNo, stringEDateTime, ">",
            exeUtil);
        if (doubleMoneyCF > 0) {
          messagebox("���ު��O�ΥN�X(" + stringCostID + stringCostID + ")�w�s�b�����ơA�����\�R���C");
          return false;
        }
      } else {
        doubleMoney = exeUtil.doParseDouble(retDoc2m048[0][5].trim());
        // ���i�j��i�ϥΪ��B
        doubleMoneyCF = doubleMoneyThis
            + exeFun.getUseMoneyAboutDoc2M048(stringCostID, stringCostID1, retDoc2m048[0][2].trim(), retDoc2m048[0][3].trim(), stringFactoryNo, stringEDateTime, "<>", exeUtil);
        if (doubleMoneyCF > doubleMoney) {
          messagebox("���ު��O�ΥN�X(" + stringCostID + stringCostID + ") �w�ϥΪ����B(" + exeUtil.getFormatNum2("" + doubleMoneyCF) + ")�j��i�ϥΪ����B(" + exeUtil.getFormatNum2("" + doubleMoney)
              + ")�A�����\���ʸ�Ʈw�C");
          return false;
        }
      }
    }
    return true;
  }

  public void doLimitPayCondition() throws Throwable {
    String StringFactoryNo = "";
    // �t��
    // �o��
    if (getTable("Table1").getRowCount() > 0) {
      StringFactoryNo = ("" + getValueAt("Table1", 0, "FactoryNo")).trim();
    }
    // ��ú
    if ("".equals(StringFactoryNo) && getTable("Table3").getRowCount() > 0) {
      StringFactoryNo = ("" + getValueAt("Table3", 0, "FactoryNo")).trim();
    }
    // ����
    if ("".equals(StringFactoryNo) && getTable("Table6").getRowCount() > 0) {
      //
      StringFactoryNo = ("" + getValueAt("Table6", 0, "FactoryNo")).trim();
    }
    if ("".equals(StringFactoryNo)) return;
    // �ȭ��w 84703052(�����H��)�B04673318(�����س])
    Vector vectorFactoryNo = new Vector();
    vectorFactoryNo.add("04673318");
    vectorFactoryNo.add("84703052");
    if (vectorFactoryNo.indexOf(StringFactoryNo) == -1) return;
    //
    JTable jtable2 = getTable("Table2");
    int intRowCount = jtable2.getRowCount();
    String stringCostID = "";
    String stringCostID1 = "";
    Vector vectorCostID = new Vector();
    boolean booleanCostID = false;
    //
    vectorCostID.add("831");
    for (int intNo = 0; intNo < intRowCount; intNo++) {
      stringCostID = ("" + getValueAt("Table2", intNo, "CostID")).trim();
      stringCostID1 = ("" + getValueAt("Table2", intNo, "CostID1")).trim();
      //
      if (vectorCostID.indexOf(stringCostID + stringCostID1) != -1) {
        booleanCostID = true;
        break;
      }
    }
    if (!booleanCostID) return;
    //
    String stringPayCondition1 = getValue("PayCondition1").trim();
    String stringPayCondition2 = getValue("PayCondition2").trim();
    // �����س] �Y��
    if ("04673318".equals(StringFactoryNo) && (!"0".equals(stringPayCondition1) || !"999".equals(stringPayCondition2))) {
      setValue("PayCondition1", "0");
      setValue("PayCondition2", "999");
      messagebox("�t�� 04673318(�����س])�B�O�ά� 831(�H�Υd��d����O) �ɡA�I�ڱ��󶷬��Y���C");
      return;
    }
    // �����H�� 60��
    if ("84703052".equals(StringFactoryNo) && (!"60".equals(stringPayCondition1) || !"999".equals(stringPayCondition2))) {
      setValue("PayCondition1", "60");
      setValue("PayCondition2", "999");
      messagebox("�t�� 84703052(�����H��])�B�O�ά� 831(�H�Υd��d����O) �ɡA�I�ڱ��󶷬� 60�ѡC");
    }
  }

  // �q���]��
  public void doMail(FargloryUtil exeUtil, Doc2M010 exeFun) throws Throwable {
    String stringID = getValue("ID").trim();
    String stringDocNo = getValue("DocNo1").trim() + "-" + getValue("DocNo2").trim() + "-" + getValue("DocNo3").trim();
    String stringDescript = getValue("Descript").trim();
    String stringBarCode = getValue("BarCodeOld").trim();
    String stringVoucher = "";
    String[][] retDoc2M080 = exeFun.getDoc2M080(stringID, "�д�", "", "");
    String[][] retDoc2M0801 = new String[0][0];
    //
    if (retDoc2M080.length == 0) {
      retDoc2M0801 = exeFun.getTableDataDoc("SELECT  ID_Def,  UseMoney  FROM  Doc2M0801  WHERE  ID_BarCode  =  " + stringID + " ORDER BY ID_Def  ");
    }
    if (retDoc2M080.length > 0 || retDoc2M0801.length > 0) {
      if (retDoc2M080.length == 0) {
        retDoc2M080 = exeFun.getDoc2M080(retDoc2M0801[0][0], "", "", "");
      }
      if (retDoc2M080.length > 0) {
        stringVoucher = retDoc2M080[0][23].trim();
        stringVoucher = exeUtil.doSubstring(stringVoucher, 0, 12);
      }
      String stringSubject = "[�дڥӽЮ�] �R���q��";
      String stringContent = stringSubject + "<br>" + "���X�s���G[" + stringBarCode + "]<br>" + "����s���G[" + stringDocNo + "]<br>" + "�w���ǲ��G" + stringVoucher + "<br>"
          + "������ [�멳�w��] �� [�~���w��] �Ф�ʧ@�R�P";
      String stringSendView = "�дڨt��";
      String stringSend = "sys";
      String[] arrayUser = { "B3018", "B1721" };
      String[][] retUser = exeFun.getDoc3M011EmployeeNo("", " AND  FunctionType  IN ('P',  'Y') ");
      Vector vectorUser = new Vector();
      for (int intNo = 0; intNo < retUser.length; intNo++) {
        vectorUser.add(retUser[intNo][0].trim() + "@farglory.com.tw");
      }
      arrayUser = (String[]) vectorUser.toArray(new String[0]);
      exeUtil.doEMail(stringSubject, stringContent, stringSend, arrayUser);
    }
  }

  public void doCheckDulFactoryNo(Doc2M010 exeFun) throws Throwable {
    JTable jtable1 = getTable("Table6");
    String stringFactoryNo = "";
    String stringFactoryName = "";
    String stringFactoryNameQ = "";
    String[] arrayTableName = { "Table6", "Table1", "Table3" };
    String[][] retData = null;
    //
    for (int intNo = 0; intNo < arrayTableName.length; intNo++) {
      if (getTable(arrayTableName[intNo]).getRowCount() == 0) continue;
      //
      stringFactoryNo = ("" + getValueAt(arrayTableName[intNo], 0, "FactoryNo")).trim();
      stringFactoryName = exeFun.getFactoryName(stringFactoryNo);
      //
      if (stringFactoryName.length() > 2) {
        stringFactoryNameQ = stringFactoryName.substring(0, 2);
      } else {
        stringFactoryNameQ = stringFactoryName;
      }
      //
      retData = exeFun.getDoc3M015And("", " AND  OBJECT_SHORT_NAME  LIKE  '" + stringFactoryNameQ + "%' ");
      if (retData.length > 1) {
        messagebox("�t�� " + stringFactoryNo + "(" + stringFactoryName + ") ����W�١A��Ʈw�s�b " + (retData.length) + " ���A���ˬd�t�ӬO�_���T�C");
      }
      break;
    }
  }

  public void doSyncBarCode() throws Throwable {
    String stringBarCode = getValue("BarCode").trim();
    String stringBarCodeOld = getValue("BarCodeOld").trim();
    String stringBarCodeT = "";
    JTable jtable = getTable("Table1");
    // 2011-05-10 �S��w�ⱱ�� ���� Table14�Bñ�eTable15
    // 2014-01-15 ����-�קO���u Table17
    // 2014-04-22 �~��-Table21
    // 2015-08-29 �O�ι�ӳq���N�X Table22 Doc2M0124
    for (int intTableNo = 1; intTableNo <= 22; intTableNo++) {
      if (intTableNo == 8) continue;
      if (intTableNo == 11) continue;
      if (intTableNo == 12) continue;
      if (intTableNo == 18) continue;
      if (intTableNo == 19) continue;
      if (intTableNo == 20) continue;
      jtable = getTable("Table" + intTableNo);
      for (int intNo = 0; intNo < jtable.getRowCount(); intNo++) {
        setValueAt("Table" + intTableNo, stringBarCode, intNo, "BarCode");
      }
    }
  }

  public boolean isFlowCheckOK(String value, FargloryUtil exeUtil, Doc2M010 exeFun) throws Throwable {
    String stringUnderGoWrite = getValue("UNDERGO_WRITE").trim();
    String stringFlow = getFunctionName();
    //
    if (!"�s�W".equals(value.trim())) {
      String stringID = getValue("ID").trim();
      String[][] retDoc2M010 = exeFun.getTableDataDoc("SELECT  UNDERGO_WRITE  FROM  Doc2M010  WHERE  ID  =  " + stringID + " ");
      if (retDoc2M010.length == 0) {
        message("��Ƶo�Ϳ��~�A�Ь���T�ǡC");
        return false;
      }
      stringUnderGoWrite = retDoc2M010[0][0].trim();
    }
    //
    if ("E".equals(stringUnderGoWrite)) {
      message("[�@�o���] ���i���ʸ�ơC");
      return false;
    }
    if (stringFlow.indexOf("ñ��") != -1) {
      if (",K,B,Y,".indexOf(stringUnderGoWrite) == -1) {
        message("�~�ޥ�ñ�ֹL�A���i���ʸ�Ʈw�C");
        return false;
      }
      // �����C�L�˱�
      String stringBarCode = getValue("BarCodeOld").trim();
      Hashtable hashtableAnd = new Hashtable();
      Vector vectorDoc2M0161 = new Vector();
      hashtableAnd.put("BarCode", stringBarCode);
      hashtableAnd.put("PRINT_STAUTS", "Y");
      vectorDoc2M0161 = exeFun.getQueryDataHashtableDoc("Doc2M0161", hashtableAnd, "", new Vector(), exeUtil);
      if (vectorDoc2M0161.size() > 0) {
        message("�w�C�L������A�е��P�������A�A�@ �ק�C");
        return false;
      }
    }
    if (stringFlow.indexOf("�~��") != -1 || stringFlow.indexOf("�f��") != -1) {
      if ("Y".equals(stringUnderGoWrite)) {
        message("�wñ�ֹL���i���� [�ק�] �\��C");
        return false;
      }
    }
    if (stringFlow.indexOf("�ӿ�") != -1) {
      if ("Y".equals(stringUnderGoWrite)) {
        message("�wñ�ֹL���i���� [�ק�] [�R��] �\��C");
        return false;
      }
      if (stringFlow.indexOf("--�ӿ�") == -1 && "B".equals(stringUnderGoWrite)) {
        message("�wñ�ֹL���i���� [�ק�] [�R��] �\��C");
        return false;
      }
      if ("�R��".equals(value.trim()) && "X".equals(stringUnderGoWrite)) {
        message("�y�{���A���i���� [�R��] �\��C");
        return false;
      }
      //
      String StringEmployeeNo = getValue("EmployeeNo").trim().toUpperCase();
      String stringUserID = getUser().toUpperCase();
      if (!StringEmployeeNo.equals(stringUserID)) {
        String stringRemark = "";
        String[][] retDoc3M011EmployeeNo = exeFun.getDoc3M011EmployeeNo("07", " AND  EmployeeNo  =  '" + stringUserID + "' ");
        boolean booleanError = false;
        if (retDoc3M011EmployeeNo.length == 0) {
          booleanError = true;
        } else {
          stringRemark = retDoc3M011EmployeeNo[0][2].trim();
          if (stringRemark.indexOf(stringUserID) != -1) {
            booleanError = false;
          } else if (stringRemark.startsWith("ALL")) {
            booleanError = false;
          } else {
            booleanError = true;
          }
        }
        if (booleanError) {
          messagebox("�� " + StringEmployeeNo + " �إߤ���ơA�䥦�H���ಧ�ʳB�z�C");
          return false;
        }
      }
      if (stringFlow.indexOf("--�ӿ�") != -1) {
      } else {
        if (!"".equals(stringUnderGoWrite) && "I,X,".indexOf(stringUnderGoWrite) != -1) {
          message("���i���� [�ק�] [�R��] �\��C");
          return false;
        }
      }
    }
    if (stringFlow.indexOf("�H�`") != -1) {
      if ("Y".equals(stringUnderGoWrite)) {
        messagebox("�wñ�ֹL���i���� [�ק�] [�R��] �\��C");
        return false;
      }
      if (!"I".equals(stringUnderGoWrite)) {
        messagebox("�D [�H�`��ñ��]�A���i���� [�ק�] [�R��] �\��C");
        return false;
      }
    }
    // �w��ǲ�
    String stringBarCodeOld = getValue("BarCodeOld").trim();
    String stringStatus = exeFun.getStatusForDoc2M014(stringBarCodeOld);
    if (",E,Z,".indexOf("," + stringStatus + ",") != -1) {
      String[][] retTable = exeFun.getDoc2M014(stringBarCodeOld);
      String stringVoucherYMD = retTable[0][4].trim();
      //
      stringVoucherYMD = exeUtil.getDateConvertFullRoc(stringVoucherYMD).replaceAll("/", "");
      retTable = exeFun.getFED1012(stringVoucherYMD, retTable[0][5].trim(), retTable[0][7].trim(), retTable[0][8].trim());
      if (retTable.length > 0) {
        messagebox(" �w�}�߶ǲ����i�ק�C");
        return false;
      }
    }
    return true;
  }

  public void setFlow(String value) throws Throwable {
    String stringDocNo1 = getValue("DocNo1").trim();
    String stringPurchaseNoExist = getValue("PurchaseNoExist").trim();
    String stringFlow = getFunctionName();
    boolean booleanFlowI = stringDocNo1.indexOf("033FZ") != -1 && !"Y".equals(stringPurchaseNoExist);
    //
    if (stringFlow.indexOf("ñ��") != -1) {
      setValue("UNDERGO_WRITE", "Y");
    }
    //
    if (stringFlow.indexOf("�~��") != -1 || stringFlow.indexOf("�f��") != -1) {
      if (!booleanFlowI) {
        setValue("UNDERGO_WRITE", "B");
      } else {
        setValue("UNDERGO_WRITE", "I");
      }
    }
    if (stringFlow.indexOf("�ӿ�") != -1) {
      if (stringFlow.indexOf("--�ӿ�") != -1) {
        if (!booleanFlowI) {
          setValue("UNDERGO_WRITE", "B");
        } else {
          setValue("UNDERGO_WRITE", "I");
        }
      } else {
        setValue("UNDERGO_WRITE", "A");
      }
    }
    if (stringFlow.indexOf("�H�`") != -1) {
      setValue("UNDERGO_WRITE", "K");
    }
  }

  public boolean isBatchCheckOK(String value, FargloryUtil exeUtil, Doc2M010 exeFun) throws Throwable {
    setValue("DocNo1", getValue("DepartNo").trim());
    //
    String stringFlow = getFunctionName();
    String stringBarCode = getValue("BarCode").trim();
    String stringBarCodeOld = getValue("BarCodeOld").trim();
    String stringDepartNoSubject = "" + get("EMP_DEPT_CD");
    String stringPurchaseNoExist = getValue("PurchaseNoExist").trim();
    String[][] retDoc1M040 = exeFun.getDoc1M040(stringBarCodeOld);
    //
    System.out.println("�]�ȽT�{-----------------S");
    if (stringFlow.indexOf("ñ��") != -1 && "A".equals(getValue("DocNoType").trim()) && getTableData("Table21").length == 0) {
      int intMaxRow = getTable("Table2").getRowCount();
      String stringCostID = "";
      String stringCostID1 = "";
      String stringRealTotalMoney = "";
      Vector vectorCostID = new Vector();
      boolean booleanCheck = true;
      boolean booleanAllNeg = true;
      //
      vectorCostID.add("002");
      vectorCostID.add("003");
      vectorCostID.add("004");
      vectorCostID.add("100");
      vectorCostID.add("101");
      vectorCostID.add("420");
      vectorCostID.add("231");
      // vectorCostID.add("232") ;
      // vectorCostID.add("233") ;
      vectorCostID.add("261");
      vectorCostID.add("262");
      vectorCostID.add("1591");
      vectorCostID.add("1592");
      vectorCostID.add("1593");
      vectorCostID.add("1594");
      vectorCostID.add("1595");
      vectorCostID.add("1596");
      for (int intRowNo = 0; intRowNo < intMaxRow; intRowNo++) {
        stringCostID = ("" + getValueAt("Table2", intRowNo, "CostID")).trim();
        stringCostID1 = ("" + getValueAt("Table2", intRowNo, "CostID1")).trim();
        stringRealTotalMoney = ("" + getValueAt("Table2", intRowNo, "RealTotalMoney")).trim();
        //
        if (exeUtil.doParseDouble(stringRealTotalMoney) > 0) booleanAllNeg = false;
        if (vectorCostID.indexOf(stringCostID + stringCostID1) != -1) {
          booleanCheck = false;
          break;
        }
        if (",31,32,".indexOf(stringCostID) != -1) {
          booleanCheck = false;
          break;
        }
      }
      if (booleanCheck) {
        JTable jtable1 = getTable("Table1");
        JTable jtable3 = getTable("Table3");
        String stringFactoryNo = "";
        Vector vectorFactoryNo = new Vector();
        for (int intNo = 0; intNo < jtable1.getRowCount(); intNo++) {
          stringFactoryNo = ("" + getValueAt("Table1", intNo, "FactoryNo")).trim();
          if (vectorFactoryNo.indexOf(stringFactoryNo) == -1) vectorFactoryNo.add(stringFactoryNo);
          if (vectorFactoryNo.size() > 1) booleanCheck = false;
        }
        for (int intNo = 0; intNo < jtable3.getRowCount(); intNo++) {
          stringFactoryNo = ("" + getValueAt("Table3", intNo, "FactoryNo")).trim();
          if (vectorFactoryNo.indexOf(stringFactoryNo) == -1) vectorFactoryNo.add(stringFactoryNo);
          System.out.println("�t��-----------------" + vectorFactoryNo.size());
          if (vectorFactoryNo.size() > 1) booleanCheck = false;
        }
      }
      if (!booleanAllNeg && booleanCheck) {
        put("Doc5M020_STATUS", "null");
        showDialog("�]�ȽT�{", "", false, true, 30, 120, 320, 250);
        String stringStatus = ("" + get("Doc5M020_STATUS")).trim();
        if (!"OK".equals(stringStatus)) return false;
      }
    }
    System.out.println("�]�ȽT�{-----------------E");
    // �N�P�X��
    put("Doc7M02691_STATUS", "DB");
    getButton("ButtonTable16").doClick();
    if (!"OK".equals("" + get("Doc7M02691_STATUS"))) {
      return false;
    }
    // ���q�N�X
    String stringComNo = getValue("ComNo").trim();
    if ("".equals(stringComNo)) {
      messagebox("[���q�N�X] ���i���ťաC");
      getcLabel("ComNo").requestFocus();
      return false;
    }
    // 2013-06-19 �� ���q���A(Doc7M056) ���ެO�_�i�ϥ�
    String[][] retDoc7M056 = exeFun.getDoc7M056(stringComNo, "", "", "", "");
    if (retDoc7M056.length == 0) {
      messagebox("���q " + stringComNo + "(" + exeFun.getCompanyName(stringComNo) + ") �����\�ϥΡC\n(�����D�Ь� [�]�ȫ�])");
      getcLabel("ComNo").requestFocus();
      return false;
    }
    String stringUseType = retDoc7M056[0][4].trim();
    String strinComNoType = retDoc7M056[0][2].trim();
    if (!"A".equals(stringUseType)) {
      messagebox("���q " + stringComNo + "(" + exeFun.getCompanyName(stringComNo) + ") �����\�ϥΡC\n(�����D�Ь� [�]�ȫ�])");
      getcLabel("ComNo").requestFocus();
      return false;
    }
    // �̿�I�ڤ�
    String stringLastPayDate = getValue("LastPayDate").trim();
    String stringCDate = getValue("CDate").trim();
    String stringCDateAC = exeUtil.getDateConvert(stringCDate);
    Vector vectorCostIDDoc2M045 = exeFun.getDoc2M045();
    Vector vectorDoc2M0201ForB = null;// ���\�K���Ҹ��
    Vector vectorDoc2M0201ForH = null;// �����\��J�o�����
    Vector vectorDoc2M0201ForJ = null;// �����\��J�ӤH���ڸ��
    JTable jtable2 = getTable("Table2");
    String stringCostIDT = "";
    String stringCostID1T = "";
    boolean booleanLastPayDate = false;
    boolean booleanDoc2M0201ForB = false;
    boolean booleanDoc2M0201ForH = false;
    boolean booleanDoc2M0201ForJ = false;
    Hashtable hashtbleFunctionType = exeFun.getCostIDVDoc2M0201H(stringComNo, "", "", stringCDateAC, "");
    //
    vectorDoc2M0201ForH = (Vector) hashtbleFunctionType.get("H");
    if (vectorDoc2M0201ForH == null) vectorDoc2M0201ForH = new Vector();
    vectorDoc2M0201ForJ = (Vector) hashtbleFunctionType.get("J");
    if (vectorDoc2M0201ForJ == null) vectorDoc2M0201ForJ = new Vector();
    vectorDoc2M0201ForB = (Vector) hashtbleFunctionType.get("B");
    if (vectorDoc2M0201ForB == null) vectorDoc2M0201ForB = new Vector();
    //
    for (int intNo = 0; intNo < jtable2.getRowCount(); intNo++) {
      stringCostIDT = ("" + getValueAt("Table2", intNo, "CostID")).trim();
      stringCostID1T = ("" + getValueAt("Table2", intNo, "CostID1")).trim();
      if (vectorCostIDDoc2M045.indexOf(stringCostIDT + stringCostID1T) != -1) {
        booleanLastPayDate = true;
      }
      if (vectorDoc2M0201ForH.indexOf(stringCostIDT + stringCostID1T) != -1) {
        booleanDoc2M0201ForH = true;
      }
      if (vectorDoc2M0201ForJ.indexOf(stringCostIDT + stringCostID1T) != -1) {
        booleanDoc2M0201ForJ = true;
      }
      if (vectorDoc2M0201ForB.indexOf(stringCostIDT + stringCostID1T) != -1) {
        booleanDoc2M0201ForB = true;
      }
    }
    if (!"Y".equals(stringPurchaseNoExist) && booleanLastPayDate && "".equals(stringLastPayDate)) {
      messagebox("[�̿�I�ڤ�] ���i���ťաC");
      return false;
    }
    if (("Y".equals(stringPurchaseNoExist) || !booleanLastPayDate) && !"".equals(stringLastPayDate)) {
      int ans = JOptionPane.showConfirmDialog(null, "�� [�дڥN�X] �D���� [�̿�I�ڤ�]�A�p��g�ɡA�{���N�ʱ��A�é����e�T�ѡA�q�� [��J�H��][�t�d�H��]�C\n �� [�_] �h�M�� [�̿�I�ڤ�]�C", "�п��?", JOptionPane.YES_NO_OPTION,
          JOptionPane.WARNING_MESSAGE);
      if (ans == JOptionPane.NO_OPTION) {
        stringLastPayDate = "";
        setValue("LastPayDate", stringLastPayDate);
      }
    }
    if (!"".equals(stringLastPayDate)) {
      stringLastPayDate = exeUtil.getDateAC(stringLastPayDate, "�̿�I�ڤ�");
      if (stringLastPayDate.length() != 10) {
        messagebox(stringLastPayDate);
        return false;
      }
      setValue("LastPayDate", stringLastPayDate);
      if (stringFlow.indexOf("ñ��") == -1 && stringLastPayDate.compareTo(exeUtil.getDateConvert(stringCDate)) < 0) {
        int ans = JOptionPane.showConfirmDialog(null, "[�̿�I�ڤ�] �ߩ� [��J���]�A�T�w�Ы� [�O]�A�_�h�N�������y�{�C", "�п��?", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (ans == JOptionPane.NO_OPTION) {
          return false;
        }
      }
    }
    // ����s��
    String stringDocNo1 = getValue("DocNo1").trim();
    String stringDocNo2 = getValue("DocNo2").trim();
    String stringDocNo3 = getValue("DocNo3").trim();
    // ����s��������ˮ�
    String retDateRoc = exeUtil.getDateFullRoc(stringDocNo2 + "01", "12345678");
    if (retDateRoc.length() != 9) {
      messagebox("[����s��2] �榡���~(yymm)�C");
      getcLabel("DocNo2").requestFocus();
      return false;
    }
    retDateRoc = exeUtil.getDateConvertRoc(retDateRoc).replaceAll("/", "");
    stringDocNo2 = datetime.getYear(retDateRoc) + datetime.getMonth(retDateRoc);
    setValue("DocNo2", stringDocNo2);
    // ����N�X����
    String stringDocNo = getValue("DocNo").trim();
    String stringKindNo = getValue("KindNo").trim();
    if ("�s�W".equals(value.trim()) && "0231,".indexOf(stringDepartNoSubject + ",") != -1) {
      // �۰ʵ���
      stringDocNo3 = exeFun.getDocNo3Max(stringComNo, stringKindNo, stringDocNo1, stringDocNo2, stringDocNo1.startsWith("023") ? "B" : "A");
      setValue("DocNo3", stringDocNo3);
    } else {
      if ("".equals(stringDocNo3)) {
        messagebox("[����s��3] ���i���ťաC");
        getcLabel("DocNo3").requestFocus();
        return false;
      }
      //
      if (!exeFun.isExistDocNoCheck(stringDocNo1, stringDocNo2, stringDocNo3, stringKindNo, stringComNo, stringBarCodeOld)) {
        messagebox("[����N�X] ���СI " + stringDocNo1 + "-" + stringDocNo2 + "-" + stringDocNo3 + "�C");
        getcLabel("DocNo3").requestFocus();
        return false;
      }
    }
    // �קO
    boolean booleanProjectIDCS = false;
    String stringProjectID = "";
    String stringProjectID1 = stringDocNo1;
    String stringProjectID2 = "";
    String stringToday = datetime.getToday("YYYY/mm/dd");
    String[][] retDoc7M0265 = exeFun.getDoc7M0265(stringComNo, "", "", "",
        "AND  NOT  ((DateStart  =  '9999/99/99'  OR  DateStart  <=  '" + stringCDateAC + "') AND " + "(DateEnd    =  '9999/99/99'  OR  DateEnd    >=  '" + stringCDateAC + "')) ");
    stringProjectID1 = stringProjectID1.replaceAll("0333", "");
    stringProjectID1 = stringProjectID1.replaceAll("033", "");
    stringProjectID1 = stringProjectID1.replaceAll("0533", "053");
    stringProjectID1 = stringProjectID1.replaceAll("133", "");
    stringProjectID2 = stringProjectID1;
    if (!"".equals(stringProjectID2) && !Character.isDigit(stringProjectID2.charAt(stringProjectID2.length() - 1))) {
      stringProjectID2 = exeUtil.doSubstring(stringProjectID2, 0, stringProjectID2.length() - 1);
    }
    if (getTableData("Table13").length == 0 && stringFlow.indexOf("ñ��") == -1 && !"Y".equals(stringPurchaseNoExist) && !isProjetIDExcept()) {
      for (int intNo = 0; intNo < retDoc7M0265.length; intNo++) {
        stringProjectID = retDoc7M0265[intNo][1].trim();
        //
        if (stringProjectID.equals(stringProjectID1)) {
          messagebox("[�קO](" + stringProjectID + ") �|�����\�ϥΡC\n(�����D�Ь� [�]�ȫ�])");
          return false;
        }
        if (stringProjectID.equals(stringProjectID2)) {
          messagebox("[�קO](" + stringProjectID + ") �|�����\�ϥΡC\n(�����D�Ь� [�]�ȫ�])");
          return false;
        }
      }
    }
    setValue("DocNo", stringDocNo1 + stringDocNo2 + stringDocNo3);
    // �~���w�� E-mail �q��
    // if(!"".equals(stringDocNo1+stringDocNo2+stringDocNo3)) {
    // doYearEndDataExistEmail ("", "AND DocNo =
    // '"+stringDocNo1+stringDocNo2+stringDocNo3+"' ", "", "",
    // getFunctionName()+"("+stringBarCode+")", exeUtil);
    // }
    // �H���X�s���ˮ֤���s���O�_�s�b�A�B�@�P
    String stringDocNoOld = getValue("DocNoOld").trim();
    String[][] retDoc1M030 = exeFun.getDoc1M030(stringBarCodeOld);
    if (retDoc1M030.length > 0 && "5".equals(retDoc1M030[0][6].trim())) {
      messagebox("����l�ܨt�Τ��A�����ʳ�w [�@�o]�A�����\����C");
      return false;
    }
    if (retDoc1M030.length > 0) {
      if (!stringKindNo.equals(retDoc1M030[0][5].trim())) {
        messagebox("[�������O] ���@�P�A�Ь���T�ǳB�z�C");
        return false;
      }
      if (!stringDocNoOld.equals(retDoc1M030[0][2].trim() + retDoc1M030[0][3].trim() + retDoc1M030[0][4].trim())) {
        messagebox("[����N�X] ���@�P�A�Ь���T�ǳB�z�C");
        return false;
      }
      if ("06".equals(retDoc1M030[0][1].trim())) retDoc1M030[0][1] = "Z6";
      if ("20".equals(retDoc1M030[0][1].trim())) retDoc1M030[0][1] = "CS";
      if (!stringComNo.equals(retDoc1M030[0][1].trim())) {
        messagebox("[���q�N�X] ���@�P�A�Ь� [��T������] �B�z�C");
        if (!getUser().startsWith("b")) return false;
      }
    }
    //
    if ("�R��".equals(value)) {
      if (retDoc1M040.length > 1) {
        messagebox(" [����N�X] �w�����o��A�����\�R���A�Ь���T�ǡC");
        // if(!"B3018".equals(getUser()))
        return false;
      }
      if (retDoc1M040.length == 1 && !"1".equals(retDoc1M040[0][6].trim())) {
        messagebox(" [����N�X] �D�Ф�A�����\�R���A�Ь���T�ǡC");
        // if(!"B3018".equals(getUser()))
        return false;
      }
      // �P�_�O�_��������(�дکέɴڨR�P)�s�b�A�s�b�����\�R��
      // �д�
      JTable jtable6 = getTable("Table6");
      String stringEDateTime = getValue("EDateTime").trim();
      String stringPurchaseNo1 = "";
      String stringPurchaseNo2 = "";
      String stringPurchaseNo3 = "";
      String stringPurchaseNo4 = "";
      for (int intNo = 0; intNo < jtable6.getRowCount(); intNo++) {
        stringPurchaseNo1 = ("" + getValueAt("Table6", intNo, "PurchaseNo1")).trim();
        stringPurchaseNo2 = ("" + getValueAt("Table6", intNo, "PurchaseNo2")).trim();
        stringPurchaseNo3 = ("" + getValueAt("Table6", intNo, "PurchaseNo3")).trim();
        stringPurchaseNo4 = ("" + getValueAt("Table6", intNo, "PurchaseNo4")).trim();
        if (exeFun.isExistSignDoc2M010(stringComNo, stringPurchaseNo1, stringPurchaseNo2, stringPurchaseNo3, stringPurchaseNo4, ">", stringEDateTime)) {
          messagebox(" �w�s�b [����дڸ��] �����\�R������ [�дڥӽЮ�]�C\n(�����D�Ь� [�]�ȫ�])");
          // System.out.println("�s�b���(�д�)-----------------"+stringPurchaseNo1+stringPurchaseNo2+stringPurchaseNo3+stringPurchaseNo4)
          // ;
          if (!getUser().startsWith("b")) return false;
        }
        if (exeFun.isExistSignDoc6M010(stringComNo, stringPurchaseNo1, stringPurchaseNo2, stringPurchaseNo3, stringPurchaseNo4, ">", stringEDateTime)) {
          messagebox(" �w�s�b [����дڸ��] �����\�R������ [�дڥӽЮ�]�C\n(�����D�Ь� [�]�ȫ�])");
          // System.out.println("�s�b���(�ɴڨR�P)-----------------"+stringPurchaseNo1+stringPurchaseNo2+stringPurchaseNo3+stringPurchaseNo4)
          // ;
          if (!getUser().startsWith("b")) return false;
        }
      }
      return true;
    }
    // if(!getUser().startsWith("b"))
    if (!"".equals(stringBarCode) && !"".equals(stringBarCodeOld) && !stringBarCode.equals(stringBarCodeOld)) {
      if (stringFlow.indexOf("�ӿ�") != -1) {
        messagebox(" [����N�X] �����\�ܧ���X�s���A�Ь���T�ǡC");
        return false;
      }
      if (retDoc1M040.length > 1) {
        messagebox(" [����N�X] �w�����o��A�����\�ܧ���X�s���A�Ь���T�ǡC");
        return false;
      }
      if (retDoc1M040.length == 1 && !"1".equals(retDoc1M040[0][6].trim())) {
        messagebox(retDoc1M040[0][6].trim() + " [����N�X] �D�Ф�A�����\�ܧ���X�s���A�Ь���T�ǡC");
        return false;
      }
    }
    //
    if (!"".equals(stringBarCode) && !(stringFlow.indexOf("�ӿ�") != -1 && stringFlow.indexOf("--") == -1)
        && exeFun.getBarCodeFirstChar().indexOf(stringBarCode.substring(0, 1)) == -1) {
      messagebox("[���X�s��] �����T�A�Э��s��J�C");
      return false;
    }
    //
    if ("".equals(stringCDate)) {
      messagebox("[���] ���i���ťաC");
      getcLabel("CDate").requestFocus();
      return false;
    }
    String retDate1 = exeUtil.getDateFullRoc(stringCDate, "���");
    if (retDate1.length() != 9) {
      messagebox(retDate1);
      getcLabel("CDate").requestFocus();
      return false;
    }
    stringCDate = retDate1;
    // ���X���i�ť�
    boolean booleanJudge = true;
    if (stringFlow.indexOf("�~��") != -1 || stringFlow.indexOf("--�ӿ�") != -1) {
      if ("�s�W".equals(value)) {
        System.out.println("getAutoBarCode------------------------------S");
        String stringTemp = exeFun.getAutoBarCode(stringComNo, stringDocNo1, exeUtil);
        if (!"".equals(stringTemp)) {
          stringBarCode = stringTemp;
        }
        System.out.println("getAutoBarCode------------------------------E[" + stringBarCode + "]");
      }
      if ("".equals(stringBarCode)) {
        Vector vectorDoc2M044 = exeFun.getQueryDataHashtableDoc("Doc2M044", new Hashtable(), " AND  DEPT_CD  LIKE   '" + exeUtil.doSubstring(stringDocNo1, 0, 3) + "%' ",
            new Vector(), exeUtil);
        String stringTemp = "";
        if (vectorDoc2M044.size() == 0) {
          stringTemp = "[���X�s��] ���i���ťաC";
        } else {
          stringTemp = "�۰ʨ��� �� [���X�s��] �w�ϥΧ��C";
        }
        messagebox(stringTemp);
        getcLabel("BarCode").requestFocus();
        return false;
      } else {
        Vector vectorDoc2M044 = !"�s�W".equals(value) ? new Vector()
            : exeFun.getQueryDataHashtableDoc("Doc2M044", new Hashtable(), " AND  DEPT_CD  LIKE   '" + exeUtil.doSubstring(stringDocNo1, 0, 3) + "%' ", new Vector(), exeUtil);
        if (vectorDoc2M044.size() > 0) {
          Hashtable hashtableData = new Hashtable();
          hashtableData.put("BarCode", stringBarCode);
          hashtableData.put("EDateTime", datetime.getTime("YYYY/mm/dd h:m:s"));
          hashtableData.put("LastEmployeeNo", getUser());
          hashtableData.put("Descript", "��P-�д�");
          exeFun.doInsertDBDoc("Doc2M044_AutoBarCode", hashtableData, true, exeUtil);
          //
          setValue("BarCode", stringBarCode);
        }
      }
      // �P�_ Barcode �G (A ~ Z) 00001 ~ 99999
      String stringStatus = exeFun.getBarCodeKindCheck(stringBarCode, exeUtil);
      if (!"OK".equals(stringStatus)) {
        messagebox(stringStatus + "_");
        getcLabel("BarCode").requestFocus();
        return false;
      }
      // �P�_ Barcode �G (A B C D) 00001 ~ 99999 -- carrey ; Barcode -> H is ������
      /*
       * char charBarCodeFirst = stringBarCode.charAt(0) ; double doubleBarCode =
       * exeUtil.doParseDouble(stringBarCode.substring(1)) ; booleanJudge =
       * stringBarCode.length( ) == 6 && Character.isLetter(charBarCodeFirst) &&
       * (doubleBarCode >= 1 && doubleBarCode <= 99999) ; if(!booleanJudge) {
       * messagebox("[���X�s��] �榡���~�C") ; getcLabel("BarCode").requestFocus( ) ; return
       * false ; }
       */
      // �s�b�ˮ�
      if (!stringBarCodeOld.equals(stringBarCode)) {
        if (!exeFun.isExistBarCodeCheck(stringBarCode)) {
          messagebox("[���X�s��] �w�s�b��Ʈw���A�а��� [�d��] ��A�@�ק�C");
          getcLabel("BarCode").requestFocus();
          return false;
        }
      }
    }
    // �~���w�� E-mail �q��
    if (!"".equals(stringBarCode) && stringFlow.indexOf("�ӿ�") == -1) {
      exeFun.doYearEndDataExistEmail(stringBarCode, "", "", "", getFunctionName() + "(" + stringBarCode + ")", exeUtil);
    }
    //
    // �������O���s�b
    // �����N�X���s�b DepartNo
    String stringDepartNo = getValue("DepartNo").trim();
    if ("".equals(stringDepartNo)) {
      messagebox("[�����N�X] ���i���ťաC");
      getcLabel("DepartNo").requestFocus();
      return false;
    }
    String stringDepartName = exeFun.getDepartName(stringDepartNo);
    if ("".equals(stringDepartName)) {
      messagebox("[�����N�X] ���s�b��Ʈw���A�Ь���T�Ƿs�W���N�X�C");
      getcLabel("DepartNo").requestFocus();
      return false;
    }
    if ("033H39,0333H39,0333H42,0333H42A,033H42,033H42A,033H42B,".indexOf(stringDepartNo + ",") != -1) {
      messagebox("[�����N�X](" + stringDepartNo + ") �����\�ϥ�\n�����D�Ь��~�ޡC");// �}�ɬ�
      getcLabel("DepartNo").requestFocus();
      return false;
    }
    // �ӿ�H��
    String stringOriEmployeeNo = getValue("OriEmployeeNo").trim();
    if ("".equals(stringOriEmployeeNo)) {
      messagebox("[�ӿ�H��] ���i���ťաC");
      getcLabel("OriEmployeeNo").requestFocus();
      return false;
    }
    String stringEmpName = exeFun.getEmpName(stringOriEmployeeNo);
    if ("".equals(stringEmpName)) {
      messagebox("[�ӿ�H��] ���s�b��Ʈw��\n(�����D�Ь� [�H�`��])");
      getcLabel("OriEmployeeNo").requestFocus();
      return false;
    }
    if (",01,12,".indexOf("," + stringComNo + ",") != -1) {
      String stringFunctionType = ("01".equals(stringComNo)) ? "W" : "";
      String stringDoc3M011EmployeeNo = exeFun.getTableDataDoc("SELECT  EmployeeNo  FROM  Doc3M011_EmployeeNo  WHERE  FunctionType = '" + stringFunctionType + "' ")[0][0].trim();
      if (stringDepartNo.startsWith("033") || stringDepartNo.startsWith("053") || stringDepartNo.startsWith("133")) {
        if (!stringDoc3M011EmployeeNo.equals(stringOriEmployeeNo)) {
          stringOriEmployeeNo = stringDoc3M011EmployeeNo;
          setValue("OriEmployeeNo", stringDoc3M011EmployeeNo);
        }
      }
      // �S�O���ޤ��q
      String stringComNoCF = exeFun.getComNoForEmpNo(stringOriEmployeeNo);
      if (stringFlow.indexOf("ñ��") == -1 && !stringComNo.equals(stringComNoCF)) {
        messagebox("[�ӿ�H��] ��O���q�� [" + exeFun.getCompanyName(stringComNoCF) + "] �D [" + exeFun.getCompanyName(stringComNo) + "]�A�����\���ʡC");
        getcLabel("OriEmployeeNo").requestFocus();
        return false;
      }
    }
    // 0 DEPT_CD 1 EMP_NO 2 EMP_NAME
    if ("B3841,".indexOf(getUser()) == -1 && stringFlow.indexOf("ñ��") == -1) {
      String[][] retFE3D05 = exeFun.getFE3D05(stringOriEmployeeNo);
      if (retFE3D05.length == 0) return false;
      //
      Hashtable hashtableData = new Hashtable();
      hashtableData.put("DEPT_CD_USER", retFE3D05[0][0].trim());
      hashtableData.put("DEPT_CD", stringDepartNo);
      hashtableData.put("EmployeeNo", getValue("EmployeeNo").trim());
      String stringErr = exeFun.getDocNoUserCheckErr(hashtableData, exeUtil);
      if (!"".equals(stringErr)) {
        messagebox(stringErr);
        getcLabel("DepartNo").requestFocus();
        return false;
      }
    }
    // ����w�w���פ��
    String stringPreFinDate = getValue("PreFinDate").trim();
    if ("".equals(stringPreFinDate)) {
      messagebox("[����w�w���פ��] ���i���ťաC");
      getcLabel("PreFinDate").requestFocus();
      return false;
    }
    retDateRoc = exeUtil.getDateFullRoc(stringPreFinDate, "����w�w���פ��");
    if (retDateRoc.length() != 9) {
      messagebox(retDateRoc);
      getcLabel("PreFinDate").requestFocus();
      return false;
    }
    if (retDateRoc.compareTo(stringCDate) <= 0) {
      messagebox("[����w�w���פ��][��J���] ������ǿ��~�C");
      getcLabel("DestineExpenseDate").requestFocus();
      return false;
    }
    // ���夺�e
    String stringDescript = getValue("Descript").trim();
    if ("".equals(stringDescript)) {
      messagebox("[���夺�e] ���i���ťաC");
      getcLabel("Descript").requestFocus();
      return false;
    }
    //
    String stringRetainBarCode = getValue("RetainBarCode").trim();
    double doubleRetainBarCode = exeUtil.doParseDouble(getValue("WriteRetainMoney").trim());
    //
    if (!"".equals(stringRetainBarCode)) {
      // �O�d���X�s���s�b�ˮ�
      String stringWriteRetainMoneySUM = exeFun.getWriteRetainMoneySUM(stringRetainBarCode, stringBarCodeOld);
      String stringRetainMoneySUM = exeFun.getRetainMoneySUM(stringRetainBarCode, "");
      // �O�d�ΰh�O�d������קO���ۦP
      String stringDepartNoLast = exeFun.getNameUnionDoc("DocNo1", "Doc2M010", " AND  BarCode   =  '" + stringRetainBarCode + "' ", new Hashtable(), exeUtil);
      String stringProjectID1Last = exeFun.getProjectIDFromDepartNo(stringDepartNoLast, "", stringBarCodeOld, "", exeUtil);
      String stringProjectID1This = exeFun.getProjectIDFromDepartNo(stringDepartNo, "", stringBarCodeOld, "", exeUtil);
      if (!stringProjectID1Last.equals(stringProjectID1This)) {
        messagebox("�O�d���峡��(" + stringDepartNoLast + ") �P �h�O�d���峡��(" + stringDepartNo + ") ���@�P�C");
        return false;
      }
      /*
       * if(isExistRetainCheckForDoc2M010(stringRetainBarCode, stringBarCodeOld)) {
       * messagebox("�w�h�O�d�A�Ь���T�ǡC") ; getcLabel("CDate").requestFocus( ) ; return false
       * ; }
       */
      // �h�O�d���B���i�� 0
      if (doubleRetainBarCode == 0) {
        messagebox("�h�O�d���B���i�� 0�C");
        return false;
      }
      // System.out.println("doubleRetainBarCode("+doubleRetainBarCode+")------------------------")
      // ;
      doubleRetainBarCode = exeUtil.doParseDouble(stringWriteRetainMoneySUM) + doubleRetainBarCode;

      // System.out.println("stringWriteRetainMoneySUM("+stringWriteRetainMoneySUM+")------------------------")
      // ;
      // System.out.println("doubleRetainBarCode("+doubleRetainBarCode+")------------------------")
      // ;

      // System.out.println("stringRetainMoneySUM("+stringRetainMoneySUM+")------------------------")
      // ;
      if (exeUtil.doParseDouble(stringRetainMoneySUM) < doubleRetainBarCode) {
        messagebox("[�h�O�d���B] �j�� [�i�h�O�d���B]�C");
        return false;
      }
      //
      for (int intTableNo = 1; intTableNo < 8; intTableNo++) {
        setTableData("Table" + intTableNo, new String[0][0]);
      }
    }
    // �w��ǲ�
    // �ˮ�
    JTabbedPane jtabbedPane1 = getTabbedPane("Tab1");
    JTable jtable1 = getTable("Table1"); // �o��
    JTable jtable3 = getTable("Table3"); // ��ú
    JTable jtable4 = getTable("Table4"); // ����
    int intTable1Panel = 3; // �T�w����
    int intTable2Panel = 2; // �T�w����
    int intTable3Panel = 4; // �T�w����
    int intTable4Panel = 5; // �T�w����
    String stringCostID = "";
    String stringCostID1 = "";
    String stringInvoiceKind = ""; // �ѶO���_�w�O�_�� [���i����]�A�A�� TRUE �o���u�ର [���i����]�C
    String stringDocNoType = getValue("DocNoType").trim();
    String stringMultiInvoiceKind = getValue("Multi-InvoiceKind").trim();
    // A �ɡA��ܵo�������� A
    // B �ɡA��ܵo�������� B
    // C �ɡA��ܵo�������� C
    // D �ɡA��ܵo�������� D
    // E �ɡA��ܵo�������� �t�|�V�X
    // F �ɡA��ܵo�����t�|���|�V�X
    String stringInvoiceTaxType = exeFun.getInvoiceTaxType(jtable1, 3);
    String stringCostIDExcept = "";
    String stringVersion = "0";
    String stringSqlAndPurchaseNo = "";
    String stringPurchaseNo1 = "";
    String stringPurchaseNo2 = "";
    String stringPurchaseNo3 = "";
    String stringPurchaseNo4 = "";
    String[] retDoc2M040 = exeFun.getDoc2M040();
    double doubleTaxRate = getTaxRate(retDoc2M040, stringInvoiceTaxType, exeFun, exeUtil);// ��o�����h�خ榡�ɡA�@���^�ǫD�s���|�v
    double doubleRealTaxSum = 0;
    double doubleRealMoneySum = getRealTotalMoneyTable2(exeUtil);
    boolean booleanSumMoneyNeg = ("N".equals(stringPurchaseNoExist) && doubleRealMoneySum < 0);
    boolean booleanPocketMoney = false;// true ��ܬ��s�Ϊ��B���n�����S��O�ΥN�X
    Vector vectorCostID = new Vector();
    //
    if ("C".equals(stringDocNoType)) {
      // DocNoType C �h�O�d��
      setTableData("Table1", new String[0][0]); // �o��
      setTableData("Table2", new String[0][0]); // �O��
      setTableData("Table3", new String[0][0]); // �ӤH����
      if (!"".equals(stringBarCode)) setValue("BarCode", stringBarCode);
      if ("".equals(getValue("RetainBarCode").trim())) {
        messagebox("�п�ܰh�O�d��ơC");
        return false;
      }
      return true;
    }
    //
    if (!"A".equals(stringDocNoType)) {
      // DocNoType A �@��
      if (!"B".equals(stringDocNoType)) {
        setTableData("Table6", new String[0][0]); // ���ʳ�
        setTableData("Table8", new String[0][0]); // ���ʸ�T
        setTableData("Table7", new String[0][0]); // �禬
      }
      setTableData("Table4", new String[0][0]); // ����
    }
    // �K���� �v��}�o��
    if ((booleanDoc2M0201ForB && getTableData("Table1").length == 0 && getTableData("Table3").length == 0) || "B".equals(stringDocNoType) || booleanSumMoneyNeg) {
      stringInvoiceTaxType = "F";
      doubleTaxRate = exeUtil.doParseDouble(retDoc2M040[4].trim()) / 100;
      //
      if ("B".equals(stringDocNoType) || booleanSumMoneyNeg) {
        setTableData("Table1", new String[0][0]); // �o��
        setTableData("Table3", new String[0][0]); // �ӤH����
      }
      // �t������ˮ�
      String stringFactoryNo = getValue("FactoryNoSpec").trim();
      String stringFactoryNoByte = "";
      String stringFactoryName = "";
      // �t������ˮ�
      if (getTableData("Table6").length > 0) {
        stringFactoryNo = "" + getValueAt("Table6", 0, "FactoryNo");
        setValue("FactoryNoSpec", stringFactoryNo);
      }
      if (getTableData("Table1").length > 0) {
        stringFactoryNo = "" + getValueAt("Table1", 0, "FactoryNo");
        setValue("FactoryNoSpec", stringFactoryNo);
      }
      if (getTableData("Table3").length > 0) {
        stringFactoryNo = "" + getValueAt("Table3", 0, "FactoryNo");
        setValue("FactoryNoSpec", stringFactoryNo);
      }
      if ("".equals(stringFactoryNo) && getTableData("Table1").length == 0 && getTableData("Table3").length == 0) {
        jtabbedPane1.setSelectedIndex(7);
        messagebox("[�Τ@�s��] ���i���ťաC");
        getcLabel("FactoryNoSpec").requestFocus();
        return false;
      }
      stringFactoryNoByte = code.StrToByte(stringFactoryNo);
      if (!check.isCoId(stringFactoryNo) && !booleanSumMoneyNeg) {
        String[][] retDoc3M015 = exeFun.getDoc3M015(stringFactoryNo);
        if (retDoc3M015.length == 0 || "1".equals(retDoc3M015[0][9].trim())) {
          messagebox("�u���\ [�Τ@�s��] �t�ӡC");
          jtabbedPane1.setSelectedIndex(7);
          return false;
        }
      }

      // 0 LAST_YMD 1 LAST_USER
      String[][] retFED1005 = exeFun.getFED1005(stringFactoryNo);
      /*
       * if(retFED1005.length == 0) { messagebox("��Ʈw���L�� [�Τ@�s��]�C") ;
       * jtabbedPane1.setSelectedIndex(7) ; return false ; }
       */

      // ���v
      String stringStopUseMessage = "";
      Hashtable hashtableCond = new Hashtable();
      hashtableCond.put("OBJECT_CD", stringFactoryNo);
      hashtableCond.put("CHECK_DATE", getValue("CDate").trim());
      hashtableCond.put("PurchaseNo_Exist", getValue("PurchaseNoExist").trim());
      hashtableCond.put("DocNoType", getValue("DocNoType").trim());
      hashtableCond.put("SOURCE", "C");
      hashtableCond.put("FieldName", "[�Τ@�s��] ");
      stringStopUseMessage = exeFun.getStopUseObjectCDMessage(hashtableCond, exeUtil);
      if (!"TRUE".equals(stringStopUseMessage)) {
        messagebox(stringStopUseMessage);
        return false;
      }
      /*
       * if(check.isID(stringFactoryNo) && "".equals(retFED1005[0][9].trim())) {
       * messagebox("�t�Ӹ�Ƥ� [�n�O�a�}]���ťաA���t�Ӥ����\�ϥΡA�и�[�n�O�a�}]��A�A�ϥΡC\n(�����D�Ь� [�]�ȫ�])") ;
       * jtabbedPane1.setSelectedIndex(7) ; return false ; }
       */
      // 20180511 �νs���ۮa�νs���ˬd
      String FGLIFE_NO = "84703052";
      if (!stringFactoryNo.equals(FGLIFE_NO)) {
        if (!exeFun.isFactoryNoOK(stringComNo, stringFactoryNo)) {
          jtabbedPane1.setSelectedIndex(7);
          return false;
        }
      }
      // �Ĥ@���ϥι�H�ήɶ��b�@�Ӥ뤧�� 2016/11/14 ����
      /*
       * if(stringFlow.indexOf("ñ��") != -1) { String stringLastYmd = "" ; stringToday
       * = exeUtil.getDateConvertFullRoc(getToday("yymmdd")).replaceAll("/","") ;
       * boolean booleanFactoryNo = true ; // if(retFED1005.length > 0) {
       * stringLastYmd = exeFun.getFED1005LastYMD(retFED1005[0][0].trim(),
       * exeUtil).replaceAll("/","") ; } // booleanFactoryNo =
       * !"".equals(stringLastYmd) && !"".equals(stringFactoryNo) &&
       * Math.abs(datetime.subDays1(stringToday, convert.replace(stringLastYmd, "/",
       * ""))) < 90 && exeFun.isFirstTimeUseFactoryNo(stringBarCodeOld, stringComNo,
       * stringFactoryNo) ; if(booleanFactoryNo) { messagebox("[�Τ@�s��] " +
       * stringFactoryNo + " �Ĥ@���ϥΡC") ; jtabbedPane1.setSelectedIndex(7) ; } }
       */
      // �s�W �ƫ�ɵo�� �Y��
      if ("B".equals(stringDocNoType) && strinComNoType.equals("E")) {
        messagebox("�S���q �ꤣ���\ �v��}�o���y�{�C");
        return false;
      }
      if ("B".equals(stringDocNoType) && stringFlow.indexOf("�ӿ�") == -1) {
        String stringSql = "";
        Hashtable hashtableAnd = new Hashtable();
        Vector vectorData = new Vector();
        //
        hashtableAnd.put("BarCode", stringBarCode);
        vectorData = exeFun.getQueryDataHashtableDoc("Doc2M060", hashtableAnd, "", new Vector(), exeUtil);
        if (vectorData.size() == 0) {
          Hashtable hashtableData = new Hashtable();
          String stringDeptCdL = "";
          JTable jtableL = getTable("Table2");
          if (jtableL.getRowCount() > 0) {
            String stringInOutL = ("" + getValueAt("Table2", 0, "InOut")).trim();
            String stringDepartNoL = ("" + getValueAt("Table2", 0, "DepartNo")).trim();
            String stringProjectIDL = ("" + getValueAt("Table2", 0, "ProjectID")).trim();
            String stringProjectID1L = ("" + getValueAt("Table2", 0, "ProjectID1")).trim();
            stringDeptCdL = exeFun.getVoucherDepartNo(stringInOutL, stringDepartNoL, stringProjectIDL, stringProjectID1L, exeUtil);
          }
          //
          hashtableData.put("BarCode", stringBarCode);
          hashtableData.put("DataSource", "A");
          hashtableData.put("DataAllow", "N");
          hashtableData.put("FactoryNo", stringFactoryNo);
          hashtableData.put("DEPT_CD", stringDeptCdL);
          hashtableData.put("ComNo", stringComNo);
          stringSql = exeFun.doInsertDBDoc("Doc2M060", hashtableData, false, exeUtil);
        }
        if (!"".equals(stringSql)) {
          addToTransaction(stringSql);
        }
      }
    }
    // ���ʳ楿�T�ˮ�
    JTable jtable8 = getTable("Table8");
    for (int intNo = 0; intNo < jtable8.getRowCount(); intNo++) {
      if (exeUtil.doParseDouble(("" + getValueAt("Table8", intNo, "ContractMoney")).trim()) <= 0) {
        jtabbedPane1.setSelectedIndex(0);
        messagebox("[���ʪ��] ��Ƥ����T�C");
        return false;
      }
    }
    //
    String stringOptometryNo1 = "";
    JTable jtable7 = getTable("Table7");
    boolean booleanSameDeptCd = true;
    boolean booleanSameDeptCdL = false;
    String stringOptometryVersion = "";
    for (int intNo = 0; intNo < jtable7.getRowCount(); intNo++) {
      stringOptometryNo1 = ("" + getValueAt("Table7", intNo, "OptometryNo1")).trim();
      if (stringDepartNo.equals(stringOptometryNo1)) {
        booleanSameDeptCdL = true;
      }
    }
    if (jtable7.getRowCount() > 0 && !booleanSameDeptCdL) {
      stringOptometryVersion = ("" + getValueAt("Table7", 0, "OptometryVersion")).trim();
      if ("A".equals(stringOptometryVersion)) {
        jtabbedPane1.setSelectedIndex(1);
        messagebox("[�禬��-����][�дڳ�-����] ���@�P�C");
        return false;
      }
    }
    //
    String stringUser = getUser().toUpperCase();
    Vector vectorUser = new Vector();
    boolean booleanUse = (vectorUser.indexOf(stringUser) != -1);
    //
    JTable jtable6 = getTable("Table6");
    for (int intNo = 0; intNo < jtable6.getRowCount(); intNo++) {
      stringPurchaseNo1 = ("" + getValueAt("Table6", intNo, "PurchaseNo1")).trim();
      stringPurchaseNo2 = ("" + getValueAt("Table6", intNo, "PurchaseNo2")).trim();
      stringPurchaseNo3 = ("" + getValueAt("Table6", intNo, "PurchaseNo3")).trim();
      stringPurchaseNo4 = ("" + getValueAt("Table6", intNo, "PurchaseNo4")).trim();
      //
      if (!stringDepartNo.equals(stringPurchaseNo1)) {
        booleanSameDeptCd = false;
      }
      //
      if (!"".equals(stringSqlAndPurchaseNo)) stringSqlAndPurchaseNo += ",  ";
      //
      stringSqlAndPurchaseNo += " '" + stringPurchaseNo1 + stringPurchaseNo2 + stringPurchaseNo3 + "' ";
      if (intNo == 0) {
        Vector vectorDocNoDoc3M011 = new Vector();
        //
        if (booleanUse || vectorDocNoDoc3M011.indexOf(stringPurchaseNo1 + stringPurchaseNo2 + stringPurchaseNo3) != -1) {
          stringVersion = "1";
        } else {
          String[][] retDoc3M010 = exeFun.getDoc3M010(stringPurchaseNo1, stringPurchaseNo2, stringPurchaseNo3, stringComNo);
          if (retDoc3M010.length == 0) {
            stringVersion = "1";
          } else {
            stringVersion = "2";
            stringSqlAndPurchaseNo = "";
            break;
          }
        }
      }
    }
    if (!"".equals(stringSqlAndPurchaseNo)) stringSqlAndPurchaseNo = "(" + stringSqlAndPurchaseNo + ")";
    //
    if ("0".equals(stringVersion) && "Y".equals(stringPurchaseNoExist)) {
      jtabbedPane1.setSelectedIndex(0);
      messagebox("[���ʪ��] ��Ƥ����T�C");
      return false;
    }
    // 2011/05/10 �S��w�ⱱ��
    String stringSpecBudget = "," + get("SPEC_BUDGET") + ",";
    String stringDocNo1L = getValue("DocNo1").trim();
    String stringEmployeeNo = getValue("EmployeeNo").trim();
    String stringFunctionType = exeFun.get033FGFunctionType(stringDocNo1, exeUtil);
    if (stringSpecBudget.indexOf("," + stringDepartNo + ",") != -1) {
      // �S��w�ⱱ�� ����H��
      if (stringFlow.indexOf("ñ��") == -1 && stringPurchaseNoExist.startsWith("N") &&
      // "033FG".equals(stringDocNo1L) &&
          exeFun.getTableDataDoc("SELECT  EmployeeNo " + " FROM  Doc3M011_EmployeeNo " + " WHERE  FunctionType  =  '" + stringFunctionType + "' " + " AND  EmployeeNo  =  '"
              + stringEmployeeNo + "' ").length <= 0) {
        messagebox("�D�S��H�������\�ӽ� " + stringDocNo1L + " �O�ΡC\n(�����D�Ь� [��P������])");
        return false;
      }
      if ("N".equals(stringPurchaseNoExist) && getTable("Table14").getRowCount() == 0) {
        getButton("Button033FGInput").doClick();
      }
    }
    System.out.println("isTable2CheckOK-------------------------------S");
    put("Doc2M010_RealTaxMoney", "OK");
    Vector retData = isTable2CheckOK(doubleTaxRate, value, stringInvoiceTaxType, stringVersion, stringSqlAndPurchaseNo, hashtbleFunctionType, booleanSameDeptCd, exeUtil, exeFun);
    put("Doc2M010_RealTaxMoney", "null");
    String stringMessage = ("" + retData.get(0)).trim();
    Hashtable hashtable2Key = null;
    if ("N".equals(stringMessage)) return false;
    hashtable2Key = (Hashtable) retData.get(1);
    doubleRealTaxSum = exeUtil.doParseDouble(("" + retData.get(2)).trim());
    stringInvoiceKind = (String) retData.get(3);
    stringCostIDExcept = (String) retData.get(4);
    stringCostID = ("" + getValueAt("Table2", 0, "CostID")).trim();
    stringCostID1 = ("" + getValueAt("Table2", 0, "CostID1")).trim();
    // �S��N�X�P�_
    vectorCostID.add("31"); // �s�Ϊ��ɡA���|�������A���\�o���L��ơA���\���B���@�P�A���|������
    vectorCostID.add("32");
    if (vectorCostID.indexOf(stringCostID) != -1) booleanPocketMoney = true;
    //
    if (jtable1.getRowCount() == 0 && jtable3.getRowCount() == 0) {
      boolean booleanErrFlag = true;
      if (booleanDoc2M0201ForB) {
        // Doc2M0201 �]�w���\�L���Ҹ��
        booleanErrFlag = false;
      } else if ("A".equals(stringDocNoType)) {
        // �@��
        if (booleanPocketMoney) {
          // �s�Ϊ�
          booleanErrFlag = false;
        } else {
          // ���~
          // �дڪ��B���t�ȡA�B���L���ʳ�ɡA���\�L����
          if (booleanSumMoneyNeg) {
            booleanErrFlag = false;
          }
        }
      } else if ("B".equals(stringDocNoType)) {
        // �v��}�o��
        booleanErrFlag = false;
      } else if ("C".equals(stringDocNoType)) {
        // �h�O�d��
        booleanErrFlag = false;
      } else if ("D".equals(stringDocNoType)) {
        // ����
        booleanErrFlag = false;
      } else {
        // ���~
      }
      if (booleanErrFlag) {
        messagebox("[�o�����]�B [���ڪ��] ���i��̬ҵL��ơC");
        jtabbedPane1.setSelectedIndex(intTable1Panel);
        return false;
      }
    }
    if (booleanDoc2M0201ForH && jtable1.getRowCount() > 0) {
      messagebox("�����\��J [�o�����] ��ơC");
      jtabbedPane1.setSelectedIndex(intTable1Panel);
      return false;
    }
    if (!"P20933".equals(stringBarCode) && booleanDoc2M0201ForJ && jtable3.getRowCount() > 0) {
      messagebox("�����\��J [�ӤH���ڪ��] ��ơC");
      jtabbedPane1.setSelectedIndex(intTable3Panel);
      return false;
    }
    // ���B�P�_
    System.out.println("isCheckMoneyOK-------------------------------S");
    if (!isCheckMoneyOK(booleanPocketMoney, stringCostIDExcept, hashtbleFunctionType, exeUtil, exeFun)) return false;
    System.out.println("isCheckMoneyOK-------------------------------E");
    // �D���
    // �I�ڤ覡
    int intCount = 1;
    String stringPayCondition1 = getValue("PayCondition1").trim();
    String stringPayCondition2 = getValue("PayCondition2").trim();
    if ("".equals(stringPayCondition1) || "999".equals(stringPayCondition1)) {
      messagebox("[�I�ڱ���1] ���i���L�C");
      getcLabel("PayCondition1").requestFocus();
      return false;
    }
    if ("".equals(stringPayCondition2) || "999".equals(stringPayCondition2)) {
      setValue("PayCondition2", "999");
    } else {
      intCount = 2;
    }
    // Table4 ����
    // �s�Ϊ��ɡA���|������
    if (jtable4.getRowCount() > 0 && booleanPocketMoney) {
      messagebox(" �s�Ϊ��ɡA���|�o�ͧ����C");
      jtabbedPane1.setSelectedIndex(intTable2Panel);
      return false;
    }
    if (jtable4.getRowCount() > 0 && jtable1.getRowCount() > 0 && "F".equals(stringInvoiceTaxType)) {
      messagebox("��o���� [�t�|] [���|] �V�X�ɡA�����\�����C");
      jtabbedPane1.setSelectedIndex(intTable4Panel);
      return false;
    }
    System.out.println("isTable4CheckOK-------------------------------S");
    if (!isTable4CheckOK(doubleTaxRate, hashtable2Key, exeUtil, exeFun)) return false;
    System.out.println("isTable4CheckOK-------------------------------E");
    // Table1 �o��
    System.out.println("isTable1CheckOK-------------------------------S");
    if (!isTable1CheckOK(stringMultiInvoiceKind, stringInvoiceKind, doubleTaxRate, doubleRealTaxSum, booleanPocketMoney, retDoc2M040, hashtbleFunctionType, exeUtil, exeFun))
      return false;
    System.out.println("isTable1CheckOK-------------------------------E");
    // Table3 ��ú
    // �I�ڱ���P��ú�@�P�ˮ�
    /*
     * if(jtable3.getRowCount( ) > intCount) { messagebox("[�I�ڱ���] �P�ӤH���ڵ��Ƥ��@�P�C") ;
     * getcLabel("PayCondition2").requestFocus( ) ; return false ; }
     */
    System.out.println("isTable3CheckOK-------------------------------S");
    if (!isTable3CheckOK(hashtbleFunctionType, exeUtil, exeFun)) return false;
    System.out.println("isTable3CheckOK-------------------------------E");
    //
    if ("Y".equals(stringPurchaseNoExist)) {
      Vector vectorPurchaseNo = null;
      Vector vectorApplyTypeD = null;
      // Table6 ����
      retData = isTable6CheckOK(stringVersion, exeUtil, exeFun);
      stringMessage = ("" + retData.get(0)).trim();
      if ("N".equals(stringMessage)) return false;
      vectorPurchaseNo = (Vector) retData.get(1);
      vectorApplyTypeD = (Vector) retData.get(2);
      // 20180123 ���ˬd�禬��� modified by FG-B03812
      /*
       * // Table7 �禬��� if(!isTable7CheckOK(vectorPurchaseNo, vectorApplyTypeD,
       * exeUtil, exeFun)) return false ;
       */
      // 2013-08-28 B3018 �T���ˮ�
      System.out.println("�T���ˮ�isAssetOK-------------------------------S");
      // 20180402 ���ˬd�T��
      /*
       * if(!isAssetOK(value, exeUtil, exeFun)) { return false ; }
       */
      System.out.println("�T���ˮ�isAssetOK-------------------------------E");
    }
    if (!"".equals(stringBarCode)) setValue("BarCode", stringBarCode);
    // KindNoD
    Vector retVector = exeFun.getKindNoDs(getTableData("Table2"), true, exeUtil);
    Vector vectorKindNoDValue = (Vector) retVector.get(0);
    Vector vectorKindNoDView = (Vector) retVector.get(1);
    String stringKindNoD = "" + vectorKindNoDValue.get(0);
    setReference("KindNoD", vectorKindNoDView, vectorKindNoDValue);
    setValue("KindNoD", stringKindNoD);
    // �̤������O KindNoD�A�ץ��w�w���פ��
    String stringKindDay = exeFun.getKindDay(stringKindNoD);
    String stringPreFinDateL = datetime.dateAdd(stringCDate.replaceAll("/", ""), "d", exeUtil.doParseInteger(stringKindDay));
    if (",033FG10603004,".indexOf(stringDocNo) == -1) {
      setValue("PreFinDate", exeUtil.getDateConvertFullRoc(stringPreFinDateL));
    }
    if (stringFlow.indexOf("ñ��") != -1) {
      // �P�_�O�_ñ�ֹL
      String stringID = getValue("ID").trim();
      Hashtable hashtableAnd = new Hashtable();
      //
      hashtableAnd.put("ID_BarCode", stringID);
      hashtableAnd.put("ComNo", stringComNo);
      hashtableAnd.put("Status", "Y");
      Vector vectorDoc2M0801 = exeFun.getQueryDataHashtableDoc("Doc2M0801", hashtableAnd, "", new Vector(), exeUtil);
      //
      if (vectorDoc2M0801.size() == 0) {
        put("Doc2M010_YearEnd", "NO_CHECK");
        getButton("ButtonYearEnd").doClick();
        if ("EXIT".equals("" + get("Doc2M010_YearEnd"))) {
          messagebox("�|���T�{��ơA�����\���ʽдڳ�C");
          return false;
        }
      }
    }
    return true;
  }

  public double getTaxRate(String[] retDoc2M040, String stringInvoiceTaxType, Doc2M010 exeFun, FargloryUtil exeUtil) throws Throwable {
    JTable jtable1 = getTable("Table1"); // �o��
    JTable jtable2 = getTable("Table2"); // �O��
    JTable jtable3 = getTable("Table3"); // ��ú
    return exeFun.getTaxRate(3, jtable1, jtable3, retDoc2M040, stringInvoiceTaxType);
  }

  // ���B�ˬd
  public boolean isCheckMoneyOK(boolean booleanPocketMoney, String stringCostIDExcept, Hashtable hashtbleFunctionType, FargloryUtil exeUtil, Doc2M010 exeFun) throws Throwable {
    JTabbedPane jtabbedPane1 = getTabbedPane("Tab1");
    JTable jtable1 = getTable("Table1"); // �o��
    JTable jtable2 = getTable("Table2"); // �O��
    JTable jtable3 = getTable("Table3"); // ��ú
    JTable jtable4 = getTable("Table4"); // ����
    JTable jtable6 = getTable("Table6"); // ����
    JTable jtable21 = getTable("Table21");
    int intTable1Panel = 3; // �T�w����
    int intTable2Panel = 2; // �T�w����
    int intTable3Panel = 4; // �T�w����
    int intTable4Panel = 5; // �T�w����
    double doubleInvoiceTotalMoneySum = exeUtil.doParseDouble(getValue("InvoiceTotalMoneySum").trim());
    double doubleRealMoneySum = exeUtil.doParseDouble(getValue("RealMoneySum").trim());
    double doubleTotalRealMoney = doubleRealMoneySum;
    double doubleReceiptTotalSum = exeUtil.doParseDouble(getValue("ReceiptSum").trim());
    double doubleReceiptMoneySUM = exeUtil.doParseDouble(getValue("ReceiptMoneySUM").trim());
    double doublePurchaseMoneySum = exeUtil.doParseDouble(getValue("PurchaseMoneySum").trim()); // �������ʪ��B
    boolean booleanPurchaseNoExist = "Y".equals(getValue("PurchaseNoExist").trim()) ? true : false; // true ��ܦ����ʳ�A�_�h���L���ʳ�
    boolean booleanCostIDExcept = "Y".equals(stringCostIDExcept) ? true : false; // true ��ܯS��O�ΥN�X (���O�q�O)
    boolean booleanTable21 = jtable21.getRowCount() > 0;
    String stringBarCode = getValue("BarCode");
    String stringDocNoType = getValue("DocNoType").trim();
    Vector vectorSpecBarCode = new Vector();
    // �ҥ~(�d���f�n�D)
    vectorSpecBarCode.add("G67605");
    // �дڪ��B
    if (doubleRealMoneySum <= 0) {// oce
      if (isNegMoneySumOK(hashtbleFunctionType, exeUtil, exeFun)) {
        return true;
      }
      messagebox(" [�дڦX�p] ���j��s�C");
      jtabbedPane1.setSelectedIndex(intTable2Panel);
      return false;
    }
    // 2011-04-01 �O�Ϊ����B������ 009
    doubleTotalRealMoney = getRealTotalMoneyTable2(exeUtil);
    if ("B".equals(stringDocNoType)) return true;
    // �`���ʪ��B
    if (booleanPurchaseNoExist && jtable6.getRowCount() > 0) {
      if (doublePurchaseMoneySum != doubleTotalRealMoney) {
        messagebox("[�`���ʪ��B] ������ [�дڪ��B]�C");
        if (!"B3018".equals(getUser())) return false;
      }
    }
    // �o��
    if (jtable1.getRowCount() > 0) {
      System.out.println("�o��-------------------------------1");
      // �o�����B
      if (doubleInvoiceTotalMoneySum <= 0 && !booleanPocketMoney && !booleanCostIDExcept) {
        messagebox(" [�o���`���B�X�p] ���j��s�C");
        jtabbedPane1.setSelectedIndex(intTable1Panel);
        return false;
      }
      // ���p�@�G�o���p
      System.out
          .println("�o��doubleTotalRealMoney(" + convert.FourToFive("" + doubleInvoiceTotalMoneySum, 0) + ")doubleTotalRealMoney(" + convert.FourToFive("" + doubleTotalRealMoney, 0)
              + ")booleanPocketMoney(" + booleanPocketMoney + ")booleanCostIDExcept(" + booleanCostIDExcept + ")-------------------------------1");
      if (doubleInvoiceTotalMoneySum < doubleTotalRealMoney && !booleanPocketMoney && !booleanCostIDExcept) {
        if (!"E62232".equals(stringBarCode)) {
          messagebox("[�дڦX�p] ���p�󵥩� [�o���`���B�X�p]�C");
          jtabbedPane1.setSelectedIndex(intTable2Panel);
          return false;
        }
      }
      // ���p�G�G�o�� �j
      if (doubleInvoiceTotalMoneySum > doubleTotalRealMoney) {
        // ������
        // ������涷�����
        if (jtable4.getRowCount() == 0) {
          if (doubleInvoiceTotalMoneySum - doubleTotalRealMoney > 3) {
            // �t�B�j�� 3 ��
            if (vectorSpecBarCode.indexOf(stringBarCode) == -1) {
              messagebox("[�o���`���B] �j�� [�д��`���B] " + convert.FourToFive("" + (doubleInvoiceTotalMoneySum - doubleTotalRealMoney), 0) + " ���A�������L��ơC");
              jtabbedPane1.setSelectedIndex(intTable4Panel);
              return false;
            }
          } else {
            // �t�B�p�󵥩� 3 ��
            int ans = JOptionPane.showConfirmDialog(null,
                "[�o���`���B] �j�� [�д��`���B]  " + convert.FourToFive("" + (doubleInvoiceTotalMoneySum - doubleTotalRealMoney), 0) + " ���A�O�_���\�~�����A���\�Ы� [�O]�C", "�п��?",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (ans == JOptionPane.NO_OPTION) {
              return false;
            }
          }
        }
      }
      // ���p�T�G�۵�
      if (doubleInvoiceTotalMoneySum == doubleTotalRealMoney && jtable4.getRowCount() > 0) {
        messagebox("������椣����ơC");
        jtabbedPane1.setSelectedIndex(intTable4Panel);
        return false;
      }
    }
    // ��ú(�ӤH����)
    if (jtable3.getRowCount() > 0) {
      if (doubleReceiptTotalSum < 0) {
        messagebox(" [�ұo�`���B�X�p] ���j��s�C");
        jtabbedPane1.setSelectedIndex(intTable3Panel);
        return false;
      }
      //
      if (!booleanTable21 && doubleReceiptTotalSum != doubleTotalRealMoney) {
        String stringCostIDCheck = "002,003,004,";
        String stringCostID = "";
        String stringCostID1 = "";
        boolean booleanError = true;
        for (int intNo = 0; intNo < getTable("Table2").getRowCount(); intNo++) {
          stringCostID = ("" + getValueAt("Table2", intNo, "CostID")).trim();
          stringCostID1 = ("" + getValueAt("Table2", intNo, "CostID1")).trim();
          if (stringCostIDCheck.indexOf(stringCostID + stringCostID1) != -1 || booleanTable21) {
            booleanError = false;
            break;
          }
        }
        if (booleanError) {
          messagebox("[�дڦX�p] ������ [�ұo�`���B�X�p]�C");
          jtabbedPane1.setSelectedIndex(intTable2Panel);
          return false;
        }
      }
    }
    return true;
  }

  public boolean isNegMoneySumOK(Hashtable hashtbleFunctionType, FargloryUtil exeUtil, Doc2M010 exeFun) throws Throwable {
    JTable jtable2 = getTable("Table2"); // �O��
    String stringRealTotalMoney = "";
    String stringCostID = "";
    String stringCostID1 = "";
    Vector vectorCostIDExcept1 = null;// ���\�O�ΦX�p���t
    //
    vectorCostIDExcept1 = (Vector) hashtbleFunctionType.get("1");
    if (vectorCostIDExcept1 == null) vectorCostIDExcept1 = new Vector();
    //
    for (int intRowNo = 0; intRowNo < jtable2.getRowCount(); intRowNo++) {
      stringRealTotalMoney = ("" + getValueAt("Table2", intRowNo, "RealTotalMoney")).trim();
      stringCostID = ("" + getValueAt("Table2", intRowNo, "CostID")).trim();
      stringCostID1 = ("" + getValueAt("Table2", intRowNo, "CostID1")).trim();
      // ���Ҭ��t��
      if (exeUtil.doParseDouble(stringRealTotalMoney) > 0) return false;
      // ���O�S�O�дڥN�X
      if (vectorCostIDExcept1.indexOf(stringCostID + stringCostID1) == -1) return false;
    }
    return true;
  }

  // 2011-04-01
  public double getRealTotalMoneyTable2(FargloryUtil exeUtil) throws Throwable {
    JTable jtable2 = getTable("Table2");
    String stringCostID = "";
    String stringCostID1 = "";
    String stringRealTotalMoney = "";
    double doubleRealTotalMoney = 0;
    double doubleRealTotalMoneySum = 0;
    for (int intRowNo = 0; intRowNo < jtable2.getRowCount(); intRowNo++) {
      stringCostID = ("" + getValueAt("Table2", intRowNo, "CostID")).trim();
      stringCostID1 = ("" + getValueAt("Table2", intRowNo, "CostID1")).trim();
      stringRealTotalMoney = ("" + getValueAt("Table2", intRowNo, "RealTotalMoney")).trim();
      doubleRealTotalMoney = exeUtil.doParseDouble(stringRealTotalMoney);
      //
      if ("009".equals(stringCostID + stringCostID1) && exeUtil.doParseDouble(stringRealTotalMoney) < 0) continue;
      //
      doubleRealTotalMoneySum += doubleRealTotalMoney;
    }
    doubleRealTotalMoneySum = exeUtil.doParseDouble(convert.FourToFive("" + doubleRealTotalMoneySum, 0));
    return doubleRealTotalMoneySum;
  }

  // �o�����
  public boolean isTable1CheckOK(String stringMultiInvoiceKind, String stringInvoiceKindB, double doubleTaxRate, double doubleRealTaxSum, boolean booleanPocketMoney,
      String[] retDoc2M040, Hashtable hashtbleFunctionType, FargloryUtil exeUtil, Doc2M010 exeFun) throws Throwable {
    JTable jtable1 = getTable("Table1");
    JTable jtable4 = getTable("Table4");
    JTable jtable7 = getTable("Table7");
    JTabbedPane jtabbedPane1 = getTabbedPane("Tab1");
    int intMaxRow = 0;
    int intTable1Panel = 3;
    int intYear = 0;
    int intMonth = 0;
    int intCompare = 0;
    int[] arrayIntYearMonth = null;
    String stringInvoiceNoAnd = "";
    String stringComNo = getValue("ComNo").trim();
    String stringBarCode = getValue("BarCode").trim();
    String stringBarCodeOld = getValue("BarCodeOld").trim();
    String stringFlow = getFunctionName();
    String stringInvoiceNo = "";
    String stringFactoryNo = "";
    String stringFactoryNoByte = "";
    String stringFactoryNoOld = "";
    String stringFactoryName = "";
    String stringInvoiceDate = "";
    String stringDateRoc = "";
    String stringTodayCF = getToday("yy/mm/dd");
    String stringToday = getToday("yymmdd");
    String stringInvoiceKind = "";
    String stringInvoiceKindFirst = "";
    String stringInvoiceMoney = "";
    String stringCostID = ("" + getValueAt("Table2", 0, "CostID")).trim();
    String stringCostMoneyLimit = "";
    String stringMessageFactor = "";
    String stringInvoiceKindLimitSize = "ABGHKOPQZT";
    String stringOptometryType = jtable7.getRowCount() == 0 ? "" : ("" + getValueAt("Table7", 0, "OptometryType")).trim();
    String[][] retFED1005 = new String[0][0];
    Vector vectorInvoiecNo = new Vector();
    Vector vectorInvoiecNoDB = (Vector) get("Doc2M010_InvoiceNo_Vector");
    Vector vectorCostIDBonus = new Vector();
    Vector vectorFactoryNo = new Vector();
    double doubleInvoiceTax = 0;
    double doubleInvoiceTaxSum = 0;
    double doubleInvoiceMoneyTax = 0;
    double doubleInvoiceTotalMoney = 0;
    double doubleTaxRateUse = 0;
    double doubleDiscountMoney = 0;
    double doubleDiscountMoneySum = 0;
    double doubleDiscountNoTaxMoney = 0;
    double doubleInvoiceTotalMoneySum = exeUtil.doParseDouble(getValue("InvoiceTotalMoneySum").trim());
    double doubleRealMoneySum = exeUtil.doParseDouble(getValue("RealMoneySum").trim());
    boolean booleanPurchaseExist = "Y".equals(getValue("PurchaseNoExist").trim()) ? true : false; // false��ܥi�H�L���ʳ�
    boolean booleanMultiFactoryNo = isTable1AllowMultiFactoryOK(hashtbleFunctionType, exeUtil, exeFun);
    //
    arrayIntYearMonth = exeFun.getInvoiceYearMonth(stringToday, exeUtil);
    intYear = arrayIntYearMonth[0];
    intMonth = arrayIntYearMonth[1];
    //
    vectorCostIDBonus.add("10");
    vectorCostIDBonus.add("13");
    //
    String stringCostIDL = "";
    String stringCostID1L = "";
    Vector vectorInvoiceDateExcept = new Vector();
    Vector vectorDeductKind = new Vector();
    Vector vectorFactoryNoFirm = new Vector();
    boolean booleanDeductKind = false;
    boolean booleanInvoiceDateExcept = false;
    //
    vectorInvoiceDateExcept.add("261");
    vectorInvoiceDateExcept.add("262");
    vectorInvoiceDateExcept.add("231");
    vectorInvoiceDateExcept.add("232");
    vectorInvoiceDateExcept.add("233");
    vectorInvoiceDateExcept.add("492");
    // �O�ά�[�U�C]�ɡADeductKind �w�]�� B
    vectorDeductKind.add("703");
    vectorDeductKind.add("393");
    // 20180515
    boolean is807Type = false;
    for (int intNo = 0; intNo < getTable("Table2").getRowCount(); intNo++) {
      stringCostIDL = ("" + getValueAt("Table2", intNo, "CostID")).trim();
      stringCostID1L = ("" + getValueAt("Table2", intNo, "CostID1")).trim();
      if (vectorInvoiceDateExcept.indexOf(stringCostIDL + stringCostID1L) != -1) {
        booleanInvoiceDateExcept = true;
      }
      if (vectorDeductKind.indexOf(stringCostIDL + stringCostID1L) != -1) {
        booleanDeductKind = true;
      }
      if (booleanInvoiceDateExcept && booleanDeductKind) break;
    }
    for (int intNo = 0; intNo < getTable("Table2").getRowCount(); intNo++) {
      stringCostIDL = ("" + getValueAt("Table2", intNo, "CostID")).trim();
      stringCostID1L = ("" + getValueAt("Table2", intNo, "CostID1")).trim();
      // if((stringCostIDL+stringCostID1L).equals("807")) {
      // if((stringCostIDL+stringCostID1L).indexOf("807") != -1) {
      // is807Type = true;
      // }

      /**
       * 20200312 B04391kyle : �����n�D�אּ�j����80 & 31 �Y�����νs�ˮ�
       */
      if (stringCostIDL.equals("80") || stringCostIDL.equals("31")) {
        is807Type = true;
        break;
      }
    }
    System.out.println("is807CostID==>" + stringCostIDL + stringCostID1L);
    System.out.println(">>>is807Type>>>" + is807Type);

    intMaxRow = jtable1.getRowCount();
    //
    if (intMaxRow == 0) return true;
    //
    for (int intNo = 0; intNo < jtable4.getRowCount(); intNo++) {
      doubleDiscountMoney = exeUtil.doParseDouble(("" + getValueAt("Table4", intNo, "DiscountMoney")).trim());
      doubleDiscountNoTaxMoney = exeUtil.doParseDouble(("" + getValueAt("Table4", intNo, "DiscountNoTaxMoney")).trim());
      doubleRealTaxSum += doubleDiscountMoney - doubleDiscountNoTaxMoney;
      doubleDiscountMoneySum += doubleDiscountMoney;
    }
    // �����ϥΪ��o�����X
    stringInvoiceKindFirst = ("" + getValueAt("Table1", 0, "InvoiceKind")).trim();
    if ("TRUE".equals(stringInvoiceKindB)) {
      boolean booleanD = false;
      for (int intRowNo = 0; intRowNo < intMaxRow; intRowNo++) {
        stringInvoiceKind = ("" + getValueAt("Table1", intRowNo, "InvoiceKind")).trim();
        if ("D".equals(stringInvoiceKind)) {
          booleanD = true;
          break;
        }
      }
      if (!booleanD) {
        messagebox("�o����� [�o���榡] ������ [���o�覩]�C\n(�����D�Ь� [�]�ȫ�])");
        jtabbedPane1.setSelectedIndex(intTable1Panel);
        return false;
      }
    }
    System.out.println("isTable1CheckOK--------------------------------LOOP START");
    String stringPenaltyInvoiceNo = "";
    String stringStopUseMessage = "";
    Hashtable hashtableCond = new Hashtable();
    double doubleInvoiceTaxL = 0;
    for (int intRowNo = 0; intRowNo < intMaxRow; intRowNo++) {
      stringInvoiceNo = ("" + getValueAt("Table1", intRowNo, "InvoiceNo")).trim();
      stringFactoryNo = ("" + getValueAt("Table1", intRowNo, "FactoryNo")).trim();
      stringInvoiceDate = ("" + getValueAt("Table1", intRowNo, "InvoiceDate")).trim();
      stringInvoiceKind = ("" + getValueAt("Table1", intRowNo, "InvoiceKind")).trim();
      doubleInvoiceTax = exeUtil.doParseDouble("" + getValueAt("Table1", intRowNo, "InvoiceTax"));
      doubleInvoiceTotalMoney = exeUtil.doParseDouble("" + getValueAt("Table1", intRowNo, "InvoiceTotalMoney"));
      stringInvoiceMoney = ("" + getValueAt("Table1", intRowNo, "InvoiceMoney")).trim();
      // ����N��
      if ("A".equals(stringOptometryType)) {
        setValueAt("Table1", "A", intRowNo, "DeductKind");
      } else if ("B".equals(stringOptometryType)) {
        setValueAt("Table1", "B", intRowNo, "DeductKind");
      }
      if (!"".equals(stringInvoiceNo)) {
        if (!"".equals(stringInvoiceNoAnd)) stringInvoiceNoAnd += ",";
        stringInvoiceNoAnd += " '" + stringInvoiceNo + "' ";
      }
      //
      String FGLIFE_NO = "84703052";
      if (!stringFactoryNo.equals(FGLIFE_NO)) {
        if (vectorFactoryNoFirm.indexOf(stringFactoryNo) == -1 && !exeFun.isFactoryNoOK(stringComNo, stringFactoryNo)) {
          String stringError = "" + get("ERROR");
          if (!"null".equals(stringError)) {
            messagebox(stringError);
          }
          jtabbedPane1.setSelectedIndex(intTable1Panel);
          jtable1.setRowSelectionInterval(intRowNo, intRowNo);
          return false;
        }
      }
      vectorFactoryNoFirm.add(stringFactoryNo);
      //
      if (stringFlow.indexOf("ñ��") != -1) {
        if (booleanPurchaseExist) {
          if (intRowNo == 0) {
            // �Ĥ@���ϥι�H�ήɶ��b�@�Ӥ뤧�� 2016/11/14 ����
            // 0 LAST_YMD 1 LAST_USER
            /*
             * String stringLastYmd = "" ; retFED1005 = exeFun.getFED1005(stringFactoryNo) ;
             * boolean booleanFactoryNo = true ; // if(retFED1005.length > 0) {
             * stringLastYmd = exeFun.getFED1005LastYMD(retFED1005[0][0].trim(),
             * exeUtil).replaceAll("/","") ; } stringToday =
             * exeUtil.getDateConvertFullRoc(stringToday).replaceAll("/","") ; //
             * booleanFactoryNo = !"".equals(stringLastYmd) && !"".equals(stringFactoryNo)
             * && Math.abs(datetime.subDays1(stringToday, convert.replace(stringLastYmd,
             * "/", ""))) < 90 && exeFun.isFirstTimeUseFactoryNo(stringBarCode, stringComNo,
             * stringFactoryNo) ; if(booleanFactoryNo) { stringMessageFactor += "�o������ " +
             * (intRowNo+1) + " �C���Τ@�s�� " + stringFactoryNo + " �Ĥ@���ϥΡC\n" ; }
             */
          }
        } else {
          /*
           * if(vectorFactoryNo.indexOf(stringFactoryNo) == -1) { // �Ĥ@���ϥι�H�ήɶ��b�@�Ӥ뤧��
           * 2016/11/14 ���� // 0 LAST_YMD 1 LAST_USER String stringLastYmd = "" ;
           * retFED1005 = exeFun.getFED1005(stringFactoryNo) ; boolean booleanFactoryNo =
           * true ; // if(retFED1005.length > 0) { stringLastYmd =
           * exeFun.getFED1005LastYMD(retFED1005[0][0].trim(), exeUtil).replaceAll("/","")
           * ; } stringToday =
           * exeUtil.getDateConvertFullRoc(stringToday).replaceAll("/","") ; //
           * booleanFactoryNo = !"".equals(stringLastYmd) && !"".equals(stringFactoryNo)
           * && Math.abs(datetime.subDays1(stringToday, convert.replace(stringLastYmd,
           * "/", ""))) < 90 && exeFun.isFirstTimeUseFactoryNo(stringBarCode, stringComNo,
           * stringFactoryNo) ; if(booleanFactoryNo) { stringMessageFactor += "�o������ " +
           * (intRowNo+1) + " �C���Τ@�s�� " + stringFactoryNo + " �Ĥ@���ϥΡC\n" ; }
           * vectorFactoryNo.add(stringFactoryNo) ; }
           */
        }
      } else {
        if (booleanDeductKind) {
          setValueAt("Table1", "B", intRowNo, "DeductKind");
        }
      }
      // �o���榡
      if ("".equals(stringInvoiceKind)) {
        messagebox("�o������ " + (intRowNo + 1) + " �C�� [�o���榡] �����ťաC");
        jtabbedPane1.setSelectedIndex(intTable1Panel);
        jtable1.setRowSelectionInterval(intRowNo, intRowNo);
        return false;
      }
      /*
       * if("TRUE".equals(stringInvoiceKindB) && !"D".equals(stringInvoiceKind)) {
       * messagebox("�o������ " + (intRowNo+1) + " �C�� [�o���榡] �u�ର [���o�覩]�C") ;
       * jtabbedPane1.setSelectedIndex(intTable1Panel) ;
       * jtable1.setRowSelectionInterval(intRowNo, intRowNo) ; return false ; }
       */
      if ("N".equals(stringMultiInvoiceKind) && !stringInvoiceKind.equals(stringInvoiceKindFirst)) {
        messagebox("�o������ " + (intRowNo + 1) + " �C�� [�o���榡] ���@�P�C");
        jtabbedPane1.setSelectedIndex(intTable1Panel);
        jtable1.setRowSelectionInterval(intRowNo, intRowNo);
        return false;
      }
      // ���B
      doubleTaxRateUse = ("D".equals(stringInvoiceKind) || "E".equals(stringInvoiceKind)) ? 0 : doubleTaxRate;
      if (doubleInvoiceTotalMoney == 0 && exeUtil.doParseDouble(stringInvoiceMoney) == 0) {
        messagebox("�o������ " + (intRowNo + 1) + " �C�� [�o�����B]���i���s�ΪťաC");
        jtabbedPane1.setSelectedIndex(intTable1Panel);
        jtable1.setRowSelectionInterval(intRowNo, intRowNo);
        return false;
      } else if (doubleInvoiceTotalMoney > 0 && exeUtil.doParseDouble(stringInvoiceMoney) > 0) {
        // �����B�z
      } else if (exeUtil.doParseDouble(stringInvoiceMoney) > 0) {
        if (doubleInvoiceTax == 0) {
          doubleInvoiceTotalMoney = exeUtil.doParseDouble(stringInvoiceMoney) * (1 + doubleTaxRateUse);
        } else {
          doubleInvoiceTotalMoney = exeUtil.doParseDouble(stringInvoiceMoney) + doubleInvoiceTax;
        }
        doubleInvoiceTotalMoney = exeUtil.doParseDouble(convert.FourToFive("" + doubleInvoiceTotalMoney, 0));
        setValueAt("Table1", "" + doubleInvoiceTotalMoney, intRowNo, "InvoiceTotalMoney");
      } else {
        if (doubleInvoiceTax == 0) {
          stringInvoiceMoney = "" + (doubleInvoiceTotalMoney / (1 + doubleTaxRateUse));
        } else {
          stringInvoiceMoney = "" + (doubleInvoiceTotalMoney - doubleInvoiceTax);
        }
        stringInvoiceMoney = convert.FourToFive("" + stringInvoiceMoney, 0);
        setValueAt("Table1", stringInvoiceMoney, intRowNo, "InvoiceMoney");
      }
      if (doubleInvoiceTax == 0) {
        doubleInvoiceTax = doubleInvoiceTotalMoney - exeUtil.doParseDouble(stringInvoiceMoney);
        doubleInvoiceTax = exeUtil.doParseDouble(convert.FourToFive("" + doubleInvoiceTax, 0));
        setValueAt("Table1", "" + doubleInvoiceTax, intRowNo, "InvoiceTax");
      }
      doubleInvoiceTaxSum += doubleInvoiceTax;
      doubleInvoiceMoneyTax = exeUtil.doParseDouble(stringInvoiceMoney) * doubleTaxRateUse;
      // �ˮ�
      // �o���`���B
      /*
       * if(booleanCostID && doubleInvoiceTotalMoney > doubleCostMoneyLimit) {
       * messagebox("�o������ " + (intRowNo+1) + " �C���� [�дڥN�X] �� " + stringCostIDLimit +
       * " �ɡA[�o���`���B] ���i�j�� " + convert.FourToFive(""+doubleCostMoneyLimit, 0) + "�C") ;
       * jtabbedPane1.setSelectedIndex(intTable1Panel) ;
       * jtable1.setRowSelectionInterval(intRowNo, intRowNo) ; return false ; }
       */
      //
      /*
       * if(vectorInvoiceNo.indexOf(stringInvoiceNo.toUpperCase())!=-1) {
       * messagebox("�o������ " + (intRowNo+1) + " �C�� [�o�����X] �����\�ϥΡC") ;
       * jtabbedPane1.setSelectedIndex(intTable1Panel) ;
       * jtable1.setRowSelectionInterval(intRowNo, intRowNo) ; return false ; }
       */
      if (doubleInvoiceTax + exeUtil.doParseDouble(stringInvoiceMoney) != doubleInvoiceTotalMoney) {
        messagebox("�o������ " + (intRowNo + 1) + " �C�� [ �o�����B] + [�o���|�B] ������ [�o���`���B] �C");
        jtabbedPane1.setSelectedIndex(intTable1Panel);
        jtable1.setRowSelectionInterval(intRowNo, intRowNo);
        return false;
      }
      // 20200312 B04391kyle : ���F�]�w�����ݭn�o�����X���A��L(indexOf = -1)���n�����X
      // if(!"D".equals(stringInvoiceKind) && stringInvoiceNo.trim( ).length( ) <= 0)
      // {
      System.out.println(">>stringInvoiceKind>>" + stringInvoiceKind);
      System.out.println(">>stringInvoiceKind index >>" + "F,C,H,D,E".indexOf(stringInvoiceKind));
      if ("F,C,H,D,E".indexOf(stringInvoiceKind) < 0 && stringInvoiceNo.trim().length() <= 0) {
        messagebox("�o������ " + (intRowNo + 1) + " �C�� [�o�����X] ���o���ťաC");
        jtabbedPane1.setSelectedIndex(intTable1Panel);
        jtable1.setRowSelectionInterval(intRowNo, intRowNo);
        return false;
      }
      if (("ABGEILMNKOP".indexOf(stringInvoiceKind) != -1) && stringInvoiceNo.trim().length() != 10) {
        messagebox("�o������ " + (intRowNo + 1) + " �C�� [�o�����X] �j�p���~�C");
        jtabbedPane1.setSelectedIndex(intTable1Panel);
        jtable1.setRowSelectionInterval(intRowNo, intRowNo);
        return false;
      }
      /*
       * if("ABLMN".indexOf(stringInvoiceKind)!=-1) { String stringDateAC =
       * exeUtil.getDateConvert(stringInvoiceDate).replaceAll("/", "") ; String
       * stringACYear = datetime.getYear(stringDateAC) ; String stringMonth =
       * datetime.getMonth(stringDateAC) ;
       * if(!exeFun.isCheckInvoiceDoc2M047OK(stringInvoiceNo.substring(0,2),
       * stringACYear, stringMonth, "A") ) { messagebox("�o������ " + (intRowNo+1) +
       * " �C�� [�o���r�y] [�o�����] ���@�P�C") ; jtabbedPane1.setSelectedIndex(intTable1Panel) ;
       * jtable1.setRowSelectionInterval(intRowNo, intRowNo) ; return false ; } }
       */
      if ("ABLMNKOP".indexOf(stringInvoiceKind) != -1) {
        String stringDateAC = exeUtil.getDateConvert(stringInvoiceDate).replaceAll("/", "");
        String stringACYear = datetime.getYear(stringDateAC);
        String stringMonth = datetime.getMonth(stringDateAC);

        // System.out.println(">>>" + stringInvoiceNo) ;
        // System.out.println(">>>" + stringDateAC) ;
        // System.out.println(">>>" + stringACYear) ;
        // System.out.println(">>>" + stringMonth) ;

        if (!exeFun.isCheckInvoiceDoc2M047OK(stringInvoiceNo.substring(0, 2), stringACYear, stringMonth, "ALL")) {
          messagebox("�o������ " + (intRowNo + 1) + " �C�� [�o���r�y] [�o�����] ���@�P�C\n(�����D�Ь� [�]�ȫ�])");
          jtabbedPane1.setSelectedIndex(intTable1Panel);
          jtable1.setRowSelectionInterval(intRowNo, intRowNo);
          return false;
        }
      }
      if (stringInvoiceKindLimitSize.indexOf(stringInvoiceKind) != -1 && stringInvoiceNo.trim().length() > 10) {
        messagebox("�o������ " + (intRowNo + 1) + " �C�� [�o�����X] ���i�j�� 10�C");
        jtabbedPane1.setSelectedIndex(intTable1Panel);
        jtable1.setRowSelectionInterval(intRowNo, intRowNo);
        return false;
      }
      if (",Z,Q,T,".indexOf(stringInvoiceKind) != -1) {
        // �ƶq-�o�����X
        String[] arrayInvoiceNo = convert.StringToken(stringInvoiceNo, "-");
        if (arrayInvoiceNo.length != 2) {
          messagebox("�o������ " + (intRowNo + 1) + " �C��[�o�����X] �榡���~(�ƥ�-�o�����X)�C");
          jtabbedPane1.setSelectedIndex(intTable1Panel);
          jtable1.setRowSelectionInterval(intRowNo, intRowNo);
          return false;
        }
        if (exeUtil.doParseDouble(arrayInvoiceNo[0]) <= 0) {
          messagebox("�o������ " + (intRowNo + 1) + " �C�� [�o�����X] �ƥخ榡 �u�ର�Ʀr�C");
          jtabbedPane1.setSelectedIndex(intTable1Panel);
          jtable1.setRowSelectionInterval(intRowNo, intRowNo);
          return false;
        }
        if (arrayInvoiceNo[0].length() != 4) {
          messagebox("�o������ " + (intRowNo + 1) + " �C�� [�o�����X] �ƥخ榡 ���� 4 �ӼƦr�C");
          jtabbedPane1.setSelectedIndex(intTable1Panel);
          jtable1.setRowSelectionInterval(intRowNo, intRowNo);
          return false;
        }
        if (exeUtil.doParseDouble(arrayInvoiceNo[0]) == 1) {
          String stringTemp = "Q".equals(stringInvoiceKind) ? "���㸹�X25" : "����";
          messagebox("��~�|���Ҫ��� " + (intRowNo + 1) + " �C��[�o�����X] �ƥج� 1�A�o���榡 �п�� " + stringTemp + "�C");
          jtabbedPane1.setSelectedIndex(intTable1Panel);
          jtable1.setRowSelectionInterval(intRowNo, intRowNo);
          return false;
        }
        if (",Q,T,".indexOf(stringInvoiceKind) != -1 && arrayInvoiceNo[1].length() != 10) {
          messagebox("�o������ " + (intRowNo + 1) + " �C�� [�o�����X] �o�����X�榡 ���� 10 �ӼƦr�C");
          jtabbedPane1.setSelectedIndex(intTable1Panel);
          jtable1.setRowSelectionInterval(intRowNo, intRowNo);
          return false;
        }
        // ���㸹�X�J�` �X�z���ˬd
        doubleInvoiceTaxL = doubleInvoiceTax / exeUtil.doParseDouble(arrayInvoiceNo[0]);
        if (doubleInvoiceTaxL > 500) {
          String stringTempL = "Q".equals(stringInvoiceKind) ? "���㸹�X�J�`25" : "���ڷJ�`";
          messagebox("��~�|���Ҫ��� " + (intRowNo + 1) + " �C " + stringTempL + " �|�B�X�z���ˬd�C\n�o���|�B 500 �� �H�W�ɡA����W�C�ܡA���i�J�`�B�z�C");
          jtabbedPane1.setSelectedIndex(intTable1Panel);
          jtable1.setRowSelectionInterval(intRowNo, intRowNo);
          return false;
        }
      }

      if (!is807Type) {
        // �Τ@�s��
        System.out.println(intRowNo + "--------------------------------�Τ@�s��");
        if (intRowNo == 0) stringFactoryNoOld = stringFactoryNo;

        if ("".equals(stringFactoryNo)) {
          messagebox("�o������ " + (intRowNo + 1) + " �C�� [�Τ@�s��] ���i���ťաC");
          jtabbedPane1.setSelectedIndex(intTable1Panel);
          jtable1.setRowSelectionInterval(intRowNo, intRowNo);
          return false;
        }

        stringFactoryNoByte = code.StrToByte(stringFactoryNo);
        if (!booleanPurchaseExist) {
          if (vectorCostIDBonus.indexOf(stringCostID) != -1 && !"Z0001".equals(stringFactoryNo)) {
            messagebox("�o������ " + (intRowNo) + " �C�� ��O�ά������ɡA[�Τ@�s��] �u�ର Z0001�C");
            jtabbedPane1.setSelectedIndex(intTable1Panel);
            jtable1.setRowSelectionInterval(intRowNo, intRowNo);
            return false;
          }
        }
        if (stringFactoryNo.length() == 8 && !check.isCoId(stringFactoryNo) && stringFactoryNoByte.length() == stringFactoryNo.length()) {
          String[][] retDoc3M015 = exeFun.getDoc3M015(stringFactoryNo);
          if (retDoc3M015.length == 0 || "1".equals(retDoc3M015[0][9].trim())) {
            messagebox("�o������ " + (intRowNo + 1) + " �C�� [�Τ@�s��] �榡���~�C");
            jtabbedPane1.setSelectedIndex(intTable1Panel);
            jtable1.setRowSelectionInterval(intRowNo, intRowNo);
            return false;
          }
        }
        if (stringFactoryNo.length() == 10 && check.isID(stringFactoryNo) && stringFactoryNoByte.length() == stringFactoryNo.length()) {
          String[][] retDoc3M015 = exeFun.getDoc3M015(stringFactoryNo);
          if (retDoc3M015.length == 0 || "1".equals(retDoc3M015[0][9].trim())) {
            messagebox("�o������ " + (intRowNo + 1) + " �C�A[�Τ@�s��] �����\���ӤH�����ҡC");
            jtabbedPane1.setSelectedIndex(intTable1Panel);
            jtable1.setRowSelectionInterval(intRowNo, intRowNo);
            return false;
          }
        }
        if (!stringFactoryNo.equals(stringFactoryNoOld)) {
          boolean booleanErrL = false;
          if (booleanPurchaseExist) {
            booleanErrL = true;
          } else {
            if (!booleanMultiFactoryNo) {
              booleanErrL = true;
            }
          }
          if (booleanErrL) {
            messagebox("�o������ " + (intRowNo + 1) + " �C�� �Ȥ��\�@�a [�Τ@�s��]�C\n(�����D�Ь� [�]�ȫ�])");
            jtabbedPane1.setSelectedIndex(intTable1Panel);
            jtable1.setRowSelectionInterval(intRowNo, intRowNo);
            return false;
          }
        }

        retFED1005 = exeFun.getFED1005(stringFactoryNo);
        if (retFED1005.length == 0) {
          jtabbedPane1.setSelectedIndex(intTable1Panel);
          jtable1.setRowSelectionInterval(intRowNo, intRowNo);
          messagebox("�o������ " + (intRowNo + 1) + " �C�� ��Ʈw���L�� [�Τ@�s��]�C");
          return false;
        }

        // ���v
        hashtableCond.put("OBJECT_CD", stringFactoryNo);
        hashtableCond.put("CHECK_DATE", getValue("CDate").trim());
        hashtableCond.put("PurchaseNo_Exist", getValue("PurchaseNoExist").trim());
        hashtableCond.put("DocNoType", getValue("DocNoType").trim());
        hashtableCond.put("SOURCE", "C");
        hashtableCond.put("FieldName", "�o������ " + (intRowNo + 1) + " �C�� [�Τ@�s��] ");
        stringStopUseMessage = exeFun.getStopUseObjectCDMessage(hashtableCond, exeUtil);
        if (!"TRUE".equals(stringStopUseMessage)) {
          messagebox(stringStopUseMessage);
          return false;
        }

        if (check.isID(stringFactoryNo) && "".equals(retFED1005[0][9].trim())) {
          jtabbedPane1.setSelectedIndex(intTable1Panel);
          jtable1.setRowSelectionInterval(intRowNo, intRowNo);
          messagebox("�o������ " + (intRowNo + 1) + " �C�� �t�Ӹ�Ƥ� [�n�O�a�}]���ťաA���t�Ӥ����\�ϥΡA�и�[�n�O�a�}]��A�A�ϥΡC");
          return false;
        }
      } else { // is 807 �p�B�q��
        if (!("").equals(stringFactoryNo)) {
          talk dbFED1 = getTalk("" + get("put_FED1"));
          String checkFED1047 = "SELECT * FROM FED1047 WHERE OBJECT_CD = '" + stringFactoryNo + "'";
          String[][] resultChkData = dbFED1.queryFromPool(checkFED1047);
          if (resultChkData.length == 0) {
            String nowRocDate = exeUtil.getDateFullRoc(datetime.getToday("YYYY/mm/dd"), "�ˬd���");
            String insertFED1047 = "INSERT INTO FED1047 (OBJECT_CD, LAST_USER, LAST_YMD) VALUES ('" + stringFactoryNo + "', '" + getUser().trim() + "', '"
                + stringToday.substring(0, 5) + "')";
            dbFED1.execFromPool(insertFED1047);
          }
        }
      }

      // /�o�����X
      if (!"".equals(stringInvoiceNo)) {
        if (vectorInvoiecNo.indexOf(stringInvoiceNo) != -1) {
          // ����
          jtabbedPane1.setSelectedIndex(intTable1Panel);
          jtable1.setRowSelectionInterval(intRowNo, intRowNo);
          messagebox("�o������ " + (intRowNo + 1) + " �C�� [�o�����X] ���СC");
          return false;
        }
        vectorInvoiecNo.add(stringInvoiceNo.trim());
        System.out.println(intRowNo + "--------------------------------�o�������ˮ�");
        // ����, ���ڤ��t�|, ���ڷJ�`(�t�|), ���ڷJ�`
        if (",C,X,Y,Z,Q,R,S,T,".indexOf(stringInvoiceKind) == -1) {
          if (!exeFun.isExistInvoiceNoCheck(stringInvoiceNo, stringBarCodeOld)) {
            jtabbedPane1.setSelectedIndex(intTable1Panel);
            jtable1.setRowSelectionInterval(intRowNo, intRowNo);
            messagebox("�o������ " + (intRowNo + 1) + " �C�� [�o�����X] �w�s�b��Ʈw���C");
            return false;
          }
          Hashtable hashtableLimit = new Hashtable();
          String stringDateStart = stringInvoiceDate;
          //
          stringDateStart = exeUtil.getDateConvertRoc(stringDateStart).replaceAll("/", "");
          stringDateStart = datetime.dateAdd(stringDateStart, "y", -2);
          //
          hashtableLimit.put("ComNo", stringComNo);
          hashtableLimit.put("DateStart", stringDateStart);
          hashtableLimit.put("InvoiceNo", stringInvoiceNo);
          if (!exeFun.isExistInvoiceNoCheckFED1012(hashtableLimit, exeUtil)) {
            String stringBarCodeDoc2M0102 = exeFun.getNameUnionDoc("BarCodeDoc2M0102", "Doc2M040", " AND  ISNULL(BarCodeDoc2M0102,'')  <>  '' ", new Hashtable(), exeUtil);
            if ("".equals(stringBarCodeOld) || stringBarCodeDoc2M0102.indexOf(stringBarCodeOld) == -1) {
              jtabbedPane1.setSelectedIndex(intTable1Panel);
              jtable1.setRowSelectionInterval(intRowNo, intRowNo);
              messagebox("�o������ " + (intRowNo + 1) + " �C�� [�o�����X] �w�s�b�]�|��Ʈw���C");
              return false;
            }
          }
        }
      } else {
        // if(!"D".equals(stringInvoiceKind) ) {
        if ("F,C,H,D,E".indexOf(stringInvoiceKind) < 0) {
          jtabbedPane1.setSelectedIndex(intTable1Panel);
          jtable1.setRowSelectionInterval(intRowNo, intRowNo);
          messagebox("�o������ " + (intRowNo + 1) + " �C�� [�o�����X] ���i���ťաC");
          return false;
        }
      }
      // �o�����|���B�ˬd
      if (!"D".equals(stringInvoiceKind) && !"E".equals(stringInvoiceKind)) {
        if (doubleInvoiceTax < (doubleInvoiceMoneyTax - 3) || doubleInvoiceTax > (doubleInvoiceMoneyTax + 3)) {
          messagebox("�o������ " + (intRowNo + 1) + " �C�� [�o���|�B] ���� [�o�����|���B] ���H [�|�v] ���t 3 ���d�򤺡C");
          jtabbedPane1.setSelectedIndex(intTable1Panel);
          jtable1.setRowSelectionInterval(intRowNo, intRowNo);
          setValueAt("Table1", "" + doubleInvoiceMoneyTax, intRowNo, "InvoiceTax");
          return false;
        }
      } else {
        if (doubleInvoiceTax > 0) {
          messagebox("�o������ " + (intRowNo + 1) + " �C�� [�o���榡] �� [���o����] �ɡA[�o���|�B] ���� 0�C");
          setValueAt("Table1", "0", intRowNo, "InvoiceTax");
          return false;
        }
      }
      // �o�����
      if ("".equals(stringInvoiceDate)) {
        messagebox("�o������ " + (intRowNo + 1) + " �C�� [�o�����] ���i���ťաC");
        jtabbedPane1.setSelectedIndex(intTable1Panel);
        jtable1.setRowSelectionInterval(intRowNo, intRowNo);
        return false;
      }
      stringDateRoc = exeUtil.getDateFullRoc(stringInvoiceDate, "�o�����");
      if (stringDateRoc.length() != 9) {
        messagebox("�o������ " + (intRowNo + 1) + " �C�� " + stringDateRoc);
        jtabbedPane1.setSelectedIndex(intTable1Panel);
        jtable1.setRowSelectionInterval(intRowNo, intRowNo);
        return false;
      } else {
        setValueAt("Table1", stringDateRoc, intRowNo, "InvoiceDate");
      }
      arrayIntYearMonth = exeFun.getInvoiceYearMonth(convert.replace(stringDateRoc, "/", ""), exeUtil);
      intCompare = intMonth + (intYear - arrayIntYearMonth[0]) * 12 - arrayIntYearMonth[1];
      System.out.println("intCompare(" + intCompare + ")intMonth(" + intMonth + ")(" + arrayIntYearMonth[1] + ")----------------------------");
      //
      if ("RQST".indexOf(stringInvoiceKind) == -1 && !booleanInvoiceDateExcept && stringTodayCF.compareTo(stringDateRoc) < 0) {
        messagebox("�o������ " + (intRowNo + 1) + " �C�� [�o�����] ����ߩ󤵤�C\n(�����D�Ь� [�]�ȫ�])");
        jtabbedPane1.setSelectedIndex(intTable1Panel);
        jtable1.setRowSelectionInterval(intRowNo, intRowNo);
        if (!getUser().startsWith("a")) return false;
      }
      if (intCompare >= 4) {
        if (!"".equals(stringPenaltyInvoiceNo)) stringPenaltyInvoiceNo += "�B";
        stringPenaltyInvoiceNo += stringInvoiceNo;
        /*
         * jtabbedPane1.setSelectedIndex(intTable1Panel) ;
         * jtable1.setRowSelectionInterval(intRowNo, intRowNo) ; int ans =
         * JOptionPane.showConfirmDialog(null, "[�o�����] �������b�|�Ӥ뤺�A�p���n����A�Ы� [�O]�A�ýЪ��W�@����C",
         * "�п��?", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) ; if(ans ==
         * JOptionPane.NO_OPTION) { return false ; }
         */
      }
    }
    //
    if (!"".equals(stringPenaltyInvoiceNo)) {
      String stringTemp = "";
      String stringPenaltyInvoiceNoL = stringPenaltyInvoiceNo;
      String[] arrayTemp = null;
      for (int intNo = 0; intNo < 30; intNo++) {
        arrayTemp = exeUtil.doCutStringBySize(90, stringPenaltyInvoiceNoL);
        //
        if (!"".equals(stringTemp)) stringTemp += "\n";
        stringTemp += arrayTemp[0];
        //
        stringPenaltyInvoiceNoL = arrayTemp[1];
        if ("".equals(stringPenaltyInvoiceNoL)) {
          break;
        }
      }
      jtabbedPane1.setSelectedIndex(intTable1Panel);
      int ans = JOptionPane.showConfirmDialog(null, "�U�C�o����[�o�����] �������b�|�Ӥ뤺�A�p���n����A�Ы� [�O]�A�ýЪ��W�@����C\n(" + stringTemp + ")", "�п��?", JOptionPane.YES_NO_OPTION,
          JOptionPane.WARNING_MESSAGE);
      if (ans == JOptionPane.NO_OPTION) {
        return false;
      }
    }
    System.out.println("------------------" + doubleRealTaxSum + "------------------------" + doubleInvoiceTaxSum);
    // �t�Z2���H�W�~�������T��^�A�p��2���@�ߵ����~�t
    if (doubleRealTaxSum != doubleInvoiceTaxSum && Math.abs(doubleRealTaxSum - doubleInvoiceTaxSum) >= 2) {
      if (booleanPocketMoney && intMaxRow == 0) {

      } else {
        boolean booleanTemp = (doubleInvoiceTotalMoneySum == doubleRealMoneySum + doubleDiscountMoneySum);
        messagebox("�o����椧�|�B(" + doubleInvoiceTaxSum + ") ������O�Τ��|�B(" + doubleRealTaxSum + ")�C");
        return false;
      }
    }
    // �~���w�� E-mail �q��
    if (!"".equals(stringInvoiceNoAnd)) {
      exeFun.doYearEndDataExistEmail("", "", "AND  InvoiceNo IN (" + stringInvoiceNoAnd + ")", "", getFunctionName() + "(" + stringBarCode + ")", exeUtil);
    }
    //
    if (!"".equals(stringMessageFactor)) {
      messagebox(stringMessageFactor);
    }
    if (!booleanPurchaseExist && "23416579".equals(stringFactoryNo) && stringFlow.indexOf("ñ��") == -1) {
      String stringPayCondition1 = getValue("PayCondition1").trim();
      String stringPayCondition2 = getValue("PayCondition2").trim();
      if (!"60".equals(stringPayCondition1) || !"999".equals(stringPayCondition2)) {
        messagebox("�t�Ӭ� 23416579(�p���ֻ�) �ɡA�I�ڱ��󶷬� 60 �ѡC\n(�����D�Ь� [�]�ȫ�])");
        setValue("PayCondition1", "60");
        setValue("PayCondition2", "999");
      }
    }
    return true;
  }

  public boolean isTable1AllowMultiFactoryOK(Hashtable hashtbleFunctionType, FargloryUtil exeUtil, Doc2M010 exeFun) throws Throwable {
    Vector vectorFunctionT = null; // �ӤH���� ���\�h�t��
    //
    vectorFunctionT = (Vector) hashtbleFunctionType.get("T");
    if (vectorFunctionT == null) vectorFunctionT = new Vector();
    //
    String stringCostID = "";
    String stringCostID1 = "";
    boolean booleanMultiFactoryNo = false;
    for (int intNo = 0; intNo < getTable("Table2").getRowCount(); intNo++) {
      stringCostID = ("" + getValueAt("Table2", intNo, "CostID")).trim();
      stringCostID1 = ("" + getValueAt("Table2", intNo, "CostID1")).trim();
      //
      if ("31,32,".indexOf(stringCostID) != -1) return true;
      if (vectorFunctionT.indexOf(stringCostID + stringCostID1) != -1) return true;
    }
    return false;
  }

  // �O�Ϊ��
  public Vector isTable2CheckOK(double doubleTaxRate, String value, String stringInvoiceTaxType, String stringVersion, String stringSqlAndPurchaseNo,
      Hashtable hashtbleFunctionType, boolean booleanSameDeptCd, FargloryUtil exeUtil, Doc2M010 exeFun) throws Throwable {
    JTable jtable1 = getTable("Table1");
    JTable jtable2 = getTable("Table2");
    JTable jtable9 = getTable("Table9");
    JTable jtable13 = getTable("Table13");
    JTable jtable21 = getTable("Table21");
    JTabbedPane jtabbedPane1 = getTabbedPane("Tab1");
    int intMaxRow = jtable2.getRowCount();
    int intTable2Panel = 2;
    int intInOut = 0;
    String stringBudgetID = "";
    String stringComNo = getValue("ComNo").trim();
    String stringCostID = "";
    String stringCostID1 = "";
    String stringInOut = "";
    String stringDepart = "";
    String stringDepart1 = "";
    String stringDepart2 = "";
    String stringDepartMessage = "";
    String stringProjectID = "";
    String stringProjectID1 = "";
    String stringRealMoney = "";
    String stringRealTotalMoney = "";
    String stringRealMoneyT = "";
    String stringKey = "";
    String stringAcctNo = "";
    String stringInvoiceKind = "FALSE";
    String stringBarCode = getValue("BarCode").trim();
    String stringBarCodeOld = getValue("BarCodeOld").trim();
    String stringCDate = getValue("CDate").trim();
    String stringCDateAC = exeUtil.getDateConvert(stringCDate);
    String stringTemp = "";
    String stringSqlL = "";
    String stringLimit = "%---%";
    String stringDocNoType = getValue("DocNoType").trim();
    String stringFunctionName = getFunctionName();
    String sringProjectIDComput = getProjectIDFromDepartNo(exeUtil, exeFun);
    String stringOriEmployeeNo = getValue("OriEmployeeNo").trim();
    String stringEmployeeNo = getValue("EmployeeNo").trim();
    String stringBarCodeDoc2M010 = exeFun.getNameUnionDoc("BarCodeDoc2M010", "Doc2M040", " AND  1=1 ", new Hashtable(), exeUtil);
    String[][] retDoc2M020 = null;
    String[][] retDoc7M011 = null;
    String[][] retFE3D05 = exeFun.getFE3D05(getValue("EmployeeNo").toUpperCase());
    String[][] retTable2 = getTableData("Table2");
    Hashtable hashtable2Key = new Hashtable();
    Hashtable hashtableAProject = exeFun.getAProject();
    Hashtable hashtableProjectMoneySum = new Hashtable();
    Hashtable hashtableProjectBudgetSum = new Hashtable();
    Hashtable hashtableUsedRealMoney = null;
    boolean booleanPurchaseExist = "Y".equals(getValue("PurchaseNoExist").trim()) ? true : false; // false��ܥi�H�L���ʳ�
    String stringExistDeptCd = booleanPurchaseExist ? "" : "";// getVFEE(stringComNo, stringCDate, exeUtil) ;
    boolean booleanCostIDExcept = false;
    boolean booleanCostID130 = false;
    boolean booleanNoCheck = (retFE3D05.length > 0 && retFE3D05[0][0].indexOf("023") != -1);
    boolean boolean053Start = getValue("DepartNo").startsWith("053");
    boolean booleanTable21 = jtable21.getRowCount() > 0;
    boolean booleanTable9 = jtable9.getRowCount() > 0;
    Vector vectorDoc2M020 = exeFun.getNoPurchaseCostID2ForDoc2M020(stringComNo);
    Vector vectorCostID = null;
    Vector vectorCostID1 = null;
    Vector vectorReturnValue = new Vector();
    Vector vectorPocketMoney = new Vector();
    Vector vectorPurchaseNoExist = new Vector();
    Vector vectorCostIDForInvoice = new Vector();
    Vector vectorProjectID1 = exeFun.getDoc2M051(" AND  DateStart  <=  '" + stringCDateAC + "'  AND  DateEnd  >=  '" + stringCDateAC + "' ");
    Vector vectorProjectID1PurchaseCheck = new Vector();
    Vector vectorBudgetID = new Vector();
    double doubleRealTaxS = 0;
    double doubleRealTaxR = 0;
    double doubleRealTaxSum = 0;
    double doubleRealMoneySumDoc3M014 = 0;
    double doubleRealTotalMoney = 0;
    double doubleTemp = 0;
    // �дڥӽЮѤ���s���� () �ɡA�L���ʳ椧�O�ΥN�X���\ 892
    String stringDocNo = getValue("DocNo1").trim() + getValue("DocNo2").trim() + getValue("DocNo3").trim();
    // if("".equals(stringDocNo)) vectorDoc2M020.add("89-2") ;
    //
    if (intMaxRow == 0) {
      stringTemp = "�п�J [�O��] ��ơC";
      return doReturnTable2Err(intTable2Panel, -1, stringTemp);
    }
    // �L���ʳ椧�O�ΥN�X
    // vectorPurchaseNoExist.add("10") ;
    // vectorPurchaseNoExist.add("13") ;
    // �s�Ϊ��B���n��
    vectorPocketMoney.add("31");
    vectorPocketMoney.add("32");
    // ���o���褧�O�ΥN�X
    vectorCostIDForInvoice.add("280");
    // �w��ҥ~(�Y�Ϲw���z�F�A���i�д�)
    Vector vectorCostIDExceptBudget = null;// �w���z�F���i�д�
    Vector vectorDoc2M0201ForE = null;// �v��}�o��
    Vector vectorCostIDExcept = null;// �O�Ϊ�� +- 5 �ˮ�
    Vector vectorCostIDS = null;// �O�Ϊ�� +- 5 �ˮ�
    Vector vectorCostIDX = null;// ���\�t��
    Vector vectorCostIDZ = null;// ���\�O�ξ��Ҥ��@�P
    Vector vectorCostID3 = null;// �����\�ϥ�
    Vector vectorCostIDTypeW = exeFun.getCostIDVDoc2M0201V(stringComNo, "", "", "W", stringCDateAC,
        " AND  (FunctionName  LIKE  '%�O�ι�ӳq���N�X%'  OR  FunctionName  LIKE  '%�p�B�q���O�� ��� �q���N�X%') ");
    boolean booleanCostIDTypeW = false;
    boolean booleanCostIDZ = false;
    Hashtable hashtableRealMoney = new Hashtable();
    //
    vectorCostIDExceptBudget = (Vector) hashtbleFunctionType.get("D");
    if (vectorCostIDExceptBudget == null) vectorCostIDExceptBudget = new Vector();
    vectorDoc2M0201ForE = (Vector) hashtbleFunctionType.get("E");
    if (vectorDoc2M0201ForE == null) vectorDoc2M0201ForE = new Vector();
    vectorCostIDExcept = (Vector) hashtbleFunctionType.get("C");
    if (vectorCostIDExcept == null) vectorCostIDExcept = new Vector();
    vectorCostIDS = (Vector) hashtbleFunctionType.get("S");
    if (vectorCostIDS == null) vectorCostIDS = new Vector();
    vectorCostIDX = (Vector) hashtbleFunctionType.get("X");
    if (vectorCostIDX == null) vectorCostIDX = new Vector();
    vectorCostIDZ = (Vector) hashtbleFunctionType.get("Z");
    if (vectorCostIDZ == null) vectorCostIDZ = new Vector();
    vectorCostID3 = (Vector) hashtbleFunctionType.get("3");
    if (vectorCostID3 == null) vectorCostID3 = new Vector();
    // if("B3018".equals(getUser().toUpperCase()))vectorCostIDExceptBudget.add("231")
    // ; // ����
    // ���@�w�ⱱ��
    Vector vectorProjectID1NoUseBudget = new Vector();
    vectorProjectID1NoUseBudget.add("0331---XA1---A2---A"); //
    vectorProjectID1NoUseBudget.add("0331---XA1---A2---B"); //
    vectorProjectID1NoUseBudget.add("0331---H42---H42B---A"); //
    vectorProjectID1NoUseBudget.add("0331---H42---H42B---B"); //
    vectorProjectID1NoUseBudget.add("0331---H39---H39---A"); //
    vectorProjectID1NoUseBudget.add("0331---H39---H39---B"); //
    vectorProjectID1NoUseBudget.add("0531---H35---H35---A"); //
    vectorProjectID1NoUseBudget.add("0531---H35---H35---B"); //
    vectorProjectID1NoUseBudget.add("0331---H45---H45A---A"); //
    vectorProjectID1NoUseBudget.add("0331---H45---H45A---B"); //
    vectorProjectID1NoUseBudget.add("0331---H45---H45B---A"); //
    vectorProjectID1NoUseBudget.add("0331---H45---H45B---B"); //
    vectorProjectID1NoUseBudget.add("1331---H9---H9---A"); //
    vectorProjectID1NoUseBudget.add("1331---H9---H9---B"); //
    vectorProjectID1NoUseBudget.add("0331---F1---F1---B");
    // vectorProjectID1NoUseBudget.add("0331---H55---H55A") ; //
    // �w��S�w DocNo
    Vector vectorDocNoUseBudget = new Vector();
    vectorDocNoUseBudget.add("03319706013"); //
    boolean booleanNoUseBudget = vectorDocNoUseBudget.indexOf(stringDocNo) != -1;
    // �h�ҽk(���ʳ�)�B�h�O�ΥN�X�ɡA�{���L�k�P�_
    // ��@���ʳ� OK
    // �h ���ʳ� �дڳ�@���u OK
    // �h ���ʳ� �дڦh ���u ���� ��@�O�Τ��u OK
    // �h �O�Τ��u �����������ϥΧ� �ҽk
    // ���������ϥΧ� ���ʳ欰��@�t�Ӽt�� OK
    // ���ʳ欰�h �t�Ӽt�� �ҽk
    boolean booleanMPurMKey = false;
    Vector vectorMPurKey = new Vector();
    if (!booleanTable9 && getTable("Table6").getRowCount() > 1) {
      booleanMPurMKey = isStatusSlur(exeFun);
    }
    if (!booleanTable9 && booleanMPurMKey) {
      // �@���Ч���
      JTable jtable6 = getTable("Table6");
      String stringPurchaseNo1 = "";
      String stringPurchaseNo2 = "";
      String stringPurchaseNo3 = "";
      String stringPurchaseMoney = "";
      String stringPurchaseMoneyCF = "";
      String[][] retDoc3M011 = null;
      boolean booleanOK = true;
      for (int intNo = 0; intNo < jtable6.getRowCount(); intNo++) {
        stringPurchaseNo1 = ("" + getValueAt("Table6", intNo, "PurchaseNo1")).trim();
        stringPurchaseNo2 = ("" + getValueAt("Table6", intNo, "PurchaseNo2")).trim();
        stringPurchaseNo3 = ("" + getValueAt("Table6", intNo, "PurchaseNo3")).trim();
        stringPurchaseMoney = ("" + getValueAt("Table6", intNo, "PurchaseMoney")).trim();
        //
        retDoc3M011 = exeFun.getTableDataDoc(" SELECT  SUM(M14.RealMoney - M14.NoUseRealMoney)" + " FROM  Doc3M011 M11,  Doc3M014 M14 " + " WHERE  M11.BarCode  =  M14.BarCode "
            + " AND  M11.ComNo   =  '" + stringComNo + "' " + " AND  M11.DocNo1  =  '" + stringPurchaseNo1 + "' " + " AND  M11.DocNo2  =  '" + stringPurchaseNo2 + "' "
            + " AND  M11.DocNo3  =  '" + stringPurchaseNo3 + "' ");
        if (exeUtil.doParseDouble(convert.FourToFive(stringPurchaseMoney, 0)) != exeUtil.doParseDouble(convert.FourToFive(retDoc3M011[0][0].trim(), 0))) {
          booleanOK = false;
          break;
        }
      }
      if (booleanOK) booleanMPurMKey = false;
    }
    // ��
    String stringDepartNoDest = getValue("DepartNoDest").trim();
    boolean booleanDepartNoDest = false;
    boolean booleanDepartNoTemp = false;
    //
    Vector vectorProjectID = new Vector();
    if ("053H36A,053H42,".indexOf(stringDepartNoDest) != -1) {
      vectorProjectID.add("H43-H43A");
      vectorProjectID.add("H43-H43B");
      vectorProjectID.add("H66A-H66A");
    } else if ("053H39".equals(stringDepartNoDest)) {
      vectorProjectID.add("H47A-H47A");
    } else if ("053M51".equals(stringDepartNoDest)) {
      vectorProjectID.add("M-M43");
      vectorProjectID.add("M-M45");
      vectorProjectID.add("M-M40");
      vectorProjectID.add("M-M48");
      vectorProjectID.add("M-M58A");
      vectorProjectID.add("M-M59A");
      vectorProjectID.add("M-M60A");
    } else if ("053H59A".equals(stringDepartNoDest)) {
      vectorProjectID.add("H65A-H65A");
    } else if ("053H63A".equals(stringDepartNoDest)) {
      vectorProjectID.add("H40A-H40A");
      vectorProjectID.add("H59A-H59A");
      vectorProjectID.add("H63A-H63A");
      vectorProjectID.add("H65A-H65A");
      vectorProjectID.add("ST-V");
    } else if ("033CT".equals(stringDepartNoDest)) {
      vectorProjectID.add("H65A-H65A");
    }
    //
    System.out.println("-----------------------------" + doubleTaxRate);
    double doubleTaxRateF = doubleTaxRate;
    boolean booleanCostID831 = false;
    boolean booleanCostID130N = false;
    //
    boolean booleanDepartProjectIDSame = "".equals(sringProjectIDComput);
    // ���~����
    String stringSpecBudget = "" + get("SPEC_BUDGET");
    String[] arraySpecBudget = convert.StringToken(stringSpecBudget, ",");
    Vector vectorDeptCd = new Vector();
    vectorDeptCd.add("03335");
    vectorDeptCd.add("033622");
    vectorDeptCd.add("03363");
    vectorDeptCd.add("0333");
    vectorDeptCd.add("03365");
    vectorDeptCd.add("03396");
    for (int intNo = 0; intNo < arraySpecBudget.length; intNo++)
      vectorDeptCd.add(arraySpecBudget[intNo]); // 2011-04-08 033FG �ץ�
    Vector vectorDeptCd2 = new Vector();
    vectorDeptCd.add("03365");
    //
    double doubleCF = 0;
    Vector vectorCostIDTypeF = null; // �ߨR�ǲ�F
    Vector vectorCostIDTypeG = null; // ��@�дڥN�X�ˮ�
    Vector vectorCostIDKindNum = new Vector();
    Vector vectorCostIDLimit = new Vector();
    boolean booleanCostIDTypeF = false;
    boolean booleanCostIDTypeG = false;
    boolean boolean74 = false;
    boolean boolean033FG = false; // 2011-04-08 033FG �ץ�
    String string033FGType = "0"; // 2011-04-08 033FG �ץ� 0 ���]�w 1 033FG 2 �D033FG 3 �V��
    Hashtable hashtableLimitSmall = new Hashtable();
    doLimitMoneyTable6(hashtableLimitSmall, vectorCostIDLimit, exeUtil, exeFun);
    //
    // String stringCostID002 = "002,003,004,100,101,420," ;
    // boolean booleanCostIDOneCheck = false ;
    //
    String stringProjectID1BTType = "";
    String[][] retDoc7M0265 = null;
    //
    vectorCostIDTypeF = (Vector) hashtbleFunctionType.get("F");
    if (vectorCostIDTypeF == null) vectorCostIDTypeF = new Vector();
    vectorCostIDTypeG = (Vector) hashtbleFunctionType.get("G");
    if (vectorCostIDTypeG == null) vectorCostIDTypeG = new Vector();
    //
    if (!isCoinTypeCheckOK(exeUtil, exeFun)) {
      return doReturnTable2Err(intTable2Panel, -1, "");
    }
    Hashtable hashtableTable2Data = null;
    String stringDocNo1 = getValue("DocNo1").trim();
    String stringSpecBudgetVoucher = "" + get("SPEC_BUDGET_VOUCHER");
    String stringRealMoneyNegType = "";
    for (int intRowNo = 0; intRowNo < intMaxRow; intRowNo++) {
      stringInOut = ("" + getValueAt("Table2", intRowNo, "InOut")).trim();
      stringDepart = ("" + getValueAt("Table2", intRowNo, "DepartNo")).trim();
      stringProjectID = ("" + getValueAt("Table2", intRowNo, "ProjectID")).trim();
      stringProjectID1 = ("" + getValueAt("Table2", intRowNo, "ProjectID1")).trim();
      stringCostID = ("" + getValueAt("Table2", intRowNo, "CostID")).trim();
      stringCostID1 = ("" + getValueAt("Table2", intRowNo, "CostID1")).trim();
      stringRealMoney = ("" + getValueAt("Table2", intRowNo, "RealMoney")).trim();
      stringRealTotalMoney = ("" + getValueAt("Table2", intRowNo, "RealTotalMoney")).trim();
      intInOut = "I".equals(stringInOut) ? 0 : 1;
      retDoc7M011 = null;
      hashtableTable2Data = getTable2DataHashtable(intRowNo);
      // 310 �Ȥ��\�ϥΦb�ɴڤ� 2015-07-24 B1721 ����
      /*
       * if("310".indexOf(stringCostID+stringCostID1) != -1) { stringTemp =
       * "�дڥN�X 310 �����\�ϥΡC\n(�����D�Ь� [�]�ȫ�])" ; return doReturnTable2Err(intTable2Panel,
       * intRowNo, stringTemp) ; }
       */
      // �s���g�k
      if (!isTable2CostIDCheckOK(intRowNo, vectorCostIDS, hashtableTable2Data, exeUtil, exeFun)) return doReturnTable2ErrData();
      //
      if ("74".equals(stringCostID)) boolean74 = true;
      // 2011-04-08 �S��w�ⱱ�� �ץ� S
      if (!"".equals(stringBarCode) && "E99385,E99362,".indexOf(stringBarCode) != -1) {

      } else if (",033CRM,".indexOf("," + getValue("DocNo1") + ",") != -1) {

      } else {
        if (jtable13.getRowCount() == 0 && !booleanTable21) {
          if (stringSpecBudgetVoucher.indexOf(stringDepart) != -1 && getTableData("Table10").length == 0) boolean033FG = true;// ����O�_�n��ܵ���
          if ("I".equals(stringInOut)) {
            if (("," + stringSpecBudget + ",").indexOf(stringDepart) == -1) {
              if ("1".equals(string033FGType)) {
                stringTemp = stringDocNo1 + " �����\�M�䥦�����ήקO�@�P���u�C\n(�����D�Ь� [�]�ȫ�])";
                return doReturnTable2Err(intTable2Panel, intRowNo, stringTemp);
              }
              if (("," + stringSpecBudget + ",").indexOf(stringDocNo1) != -1) {
                stringTemp = stringDocNo1 + "���קO���u�u�ର" + stringDocNo1 + "�C\n(�����D�Ь� [�]�ȫ�])";
                return doReturnTable2Err(intTable2Panel, intRowNo, stringTemp);
              }
              string033FGType = "2";
            } else {
              if (stringDocNo1.equals(stringDepart)) {
                if ("2".equals(string033FGType)) {
                  stringTemp = stringDocNo1 + " �����\�M�䥦�����ήקO�@�P���u�C\n(�����D�Ь� [�]�ȫ�])";
                  return doReturnTable2Err(intTable2Panel, intRowNo, stringTemp);
                }
                if (("," + stringSpecBudget + ",").indexOf(stringDepart) == -1) {
                  stringTemp = stringDocNo1 + " �����\�M�䥦�����ήקO�@�P���u�C\n(�����D�Ь� [�]�ȫ�])";
                  return doReturnTable2Err(intTable2Panel, intRowNo, stringTemp);
                }
                string033FGType = "1";
              } else {
                if ("1".equals(string033FGType)) {
                  stringTemp = stringDocNo1 + " �����\�M�䥦�����ήקO�@�P���u�C\n(�����D�Ь� [�]�ȫ�])";
                  return doReturnTable2Err(intTable2Panel, intRowNo, stringTemp);
                }
                if (("," + stringSpecBudget + ",").indexOf(stringDepart) != -1) {
                  stringTemp = stringDocNo1 + " �����\�M�䥦�����ήקO�@�P���u�C\n(�����D�Ь� [�]�ȫ�])";
                  return doReturnTable2Err(intTable2Panel, intRowNo, stringTemp);
                }
                string033FGType = "2";
              }
            }
          } else {
            if ("1".equals(string033FGType)) {
              stringTemp = stringDocNo1 + " �����\�M�䥦�����ήקO�@�P���u�C\n(�����D�Ь� [�]�ȫ�])";
              return doReturnTable2Err(intTable2Panel, intRowNo, stringTemp);
            }
            if (("," + stringSpecBudget + ",").indexOf(stringDepart) != -1) {
              stringTemp = stringDocNo1 + " �����\�M�䥦�����ήקO�@�P���u�C\n(�����D�Ь� [�]�ȫ�])";
              return doReturnTable2Err(intTable2Panel, intRowNo, stringTemp);
            }
            string033FGType = "2";
          }
          if ("1".equals(string033FGType)) {
            if (!stringDocNo1.equals(stringDepart)) {
              stringTemp = "�קO���u���u�� [" + stringDepart + "] �ɡA����s������ " + stringDepart + "�C\n(�����D�Ь� [�]�ȫ�])";
              return doReturnTable2Err(intTable2Panel, intRowNo, stringTemp);
            }
            if (getTableData("Table6").length > 1) {
              stringTemp = stringDocNo1 + " �ӽЮɡA�дڥӽЮѶȤ��\��@���ʳ�C\n(�����D�Ь� [�]�ȫ�])";
              return doReturnTable2Err(intTable2Panel, intRowNo, stringTemp);
            }
          }
          if ("2".equals(string033FGType)) {
            if (("," + stringSpecBudget + ",").indexOf(stringDocNo1) != -1) {
              stringTemp = "����s������ " + stringDocNo1 + " �ɡA�קO���u�����Ȥ��\ [" + stringDepart + "]�C\n(�����D�Ь� [�]�ȫ�])";
              return doReturnTable2Err(intTable2Panel, intRowNo, stringTemp);
            }
          }
        }
        // 2011-04-08 �S��קO���� �ץ� E
      }
      // �D��P �����\�ϥ�31�B32���
      if (("," + stringSpecBudget + ",").indexOf(stringDocNo1) == -1) {
        if (!stringDepart.startsWith("033") && !stringDepart.startsWith("053") && !stringDepart.startsWith("133")) {
          if (",31,".indexOf(stringCostID) != -1) {
            stringTemp = "�O�Ϊ��� " + (intRowNo + 1) + " �C �D��P���� �����\�ϥ� [�s�Ϊ�]�A�����\���ʸ�Ʈw�C\n(�����D�Ь� [�]�ȫ�])";
            return doReturnTable2Err(intTable2Panel, intRowNo, stringTemp);
          }
          if (",32,".indexOf(stringCostID) != -1) {
            stringTemp = "�O�Ϊ��� " + (intRowNo + 1) + " �C �D��P���� �����\�ϥ� [���Z��]�A�����\���ʸ�Ʈw�C\n(�����D�Ь� [�]�ȫ�])1";
            return doReturnTable2Err(intTable2Panel, intRowNo, stringTemp);
          }
          if (exeUtil.doParseDouble(stringCostID) >= 70) {
            if (stringDocNo.startsWith("015") && "721,".indexOf(stringCostID + stringCostID1) != -1) {
              // �S�Ҥ��\
            } else if (stringDocNo.startsWith("035")) {
              // �S�Ҥ��\
            } else if (",Q32922,".indexOf(stringBarCode) != -1) {
              // �T�ꤹ�\
            } else {
              stringTemp = "�O�Ϊ��� " + (intRowNo + 1) + " �C �D��P���� �����\�ϥ� 70 ���᪺�дڥN�X�A�����\���ʸ�Ʈw�C\n(�����D�Ь� [�]�ȫ�])";
              return doReturnTable2Err(intTable2Panel, intRowNo, stringTemp);
            }
          }
        } else {
          /*
           * 2017-03-16 B2358 �������� if("60,".indexOf(stringCostID)!=-1) {
           * if(!"0333".equals(getValue("DocNo1"))) { stringTemp = "�O�Ϊ��� " +(intRowNo+1)
           * +" �C ��P���� �����\�ϥ� [����]�A�����\���ʸ�Ʈw�C\n(�����D�Ь� [�]�ȫ�])" ; return
           * doReturnTable2Err(intTable2Panel, intRowNo, stringTemp) ; } }
           */
        }
      }
      //
      if (!booleanPurchaseExist && "I".equals(stringInOut) && !"".equals(stringExistDeptCd) && stringExistDeptCd.indexOf(exeUtil.doSubstring(stringDepart, 0, 3)) == -1) {
        stringTemp = "�O�Ϊ��� " + (intRowNo + 1) + " �C ���~����(" + stringDepart.trim() + ")���s�b��Ӥ��q�A�����\���ʸ�Ʈw�C\n(�����D�Ь� [�]�ȫ�])";
        return doReturnTable2Err(intTable2Panel, intRowNo, stringTemp);
      }
      //
      if (stringDocNo.indexOf("033FZ") != -1 && !"0333".equals(stringDepart)) {
        stringTemp = "�O�Ϊ��� " + (intRowNo + 1) + " �C�� ����s�� 033FZ �u��� ���~ 0333 �@���u �C\n(�����D�Ь� [�H�`��])";
        return doReturnTable2Err(intTable2Panel, intRowNo, stringTemp);
      }
      //
      if (!booleanDepartProjectIDSame && sringProjectIDComput.indexOf("," + stringProjectID1 + ",") != -1) {
        if (boolean053Start) {
          if ("0531".equals(stringDepart) && sringProjectIDComput.indexOf("," + stringProjectID1 + ",") != -1) booleanDepartProjectIDSame = true;
        } else {
          if (!"0531".equals(stringDepart) && sringProjectIDComput.indexOf("," + stringProjectID1 + ",") != -1) booleanDepartProjectIDSame = true;
        }
      } else {
        System.out.println(intRowNo + "sringProjectIDComput(" + sringProjectIDComput + ")---------------------");
        System.out.println(intRowNo + "stringProjectID1(" + stringProjectID1 + ")---------------------");
      }
      // if(!booleanDepartProjectIDSame &&
      // stringProjectID1.equals(sringProjectIDComput)) booleanDepartProjectIDSame =
      // true ;
      //
      if ("130".equals(stringCostID + stringCostID1)) booleanCostID130 = true;
      if (!"130".equals(stringCostID + stringCostID1)) booleanCostID130N = true;
      if ("831".equals(stringCostID + stringCostID1)) booleanCostID831 = true;
      if (vectorCostIDTypeF.indexOf(stringCostID + stringCostID1) != -1) booleanCostIDTypeF = true;
      if (vectorCostIDTypeG.indexOf(stringCostID + stringCostID1) != -1) booleanCostIDTypeG = true;
      // if(vectorCostIDTypeH.indexOf(stringCostID+stringCostID1)!=-1)
      // booleanCostIDTypeH = true ;
      if (vectorCostIDTypeW.indexOf(stringCostID + stringCostID1) != -1) booleanCostIDTypeW = true;
      if (stringProjectID.startsWith("053") || stringProjectID1.startsWith("053")) {
        stringTemp = "�O�Ϊ��� " + (intRowNo + 1) + " �C�� [�קO] ���~�C";
        return doReturnTable2Err(intTable2Panel, intRowNo, stringTemp);
      }
      if (vectorCostIDKindNum.indexOf(stringCostID + stringCostID1) == -1) vectorCostIDKindNum.add(stringCostID + stringCostID1);
      //
      if (booleanCostID130 && booleanCostID130N) {

      }
      // ��
      if (!"".equals(stringDepartNoDest) && vectorProjectID.indexOf(stringProjectID + "-" + stringProjectID1) == -1) {
        for (int intNoL = 0; intNoL < vectorProjectID.size(); intNoL++) {
          if (!"".equals(stringTemp)) stringTemp += "�B";
          stringTemp += "[" + convert.StringToken("" + vectorProjectID.get(intNoL), "-")[1].trim() + "]";
        }
        stringTemp = "�O�Ϊ��� " + (intRowNo + 1) + " �C�� �򤶳�쬰[" + stringDepartNoDest + "] �ɡA[�קO] ���� " + stringTemp + "�C";
        return doReturnTable2Err(intTable2Panel, intRowNo, stringTemp);
      }
      // �����~�B���קO��ƮɡA�����\
      if ("I".equals(stringInOut) && (!"".equals(stringProjectID) || !"".equals(stringProjectID1))) {
        stringTemp = "�O�Ϊ��� " + (intRowNo + 1) + " �C�� ��� [���~] �ɡA�קO���i����ơC";
        return doReturnTable2Err(intTable2Panel, intRowNo, stringTemp);
      }
      // �O�Ϊ��B
      if ("B".equals(stringDocNoType)) {
        if (vectorDoc2M0201ForE.indexOf(stringCostID + stringCostID1) != -1) {
          if ("720,601,603,".indexOf(stringCostID + stringCostID1) != -1) {
            // ���|
            doubleTaxRate = doubleTaxRateF;
          } else if ("729".equals(stringCostID + stringCostID1)) {
            // �L�|
            doubleTaxRate = 0;
          } else {
            doubleTaxRate = doubleTaxRateF;
          }
        } else {
          stringTemp = "";
          for (int intNoL = 0; intNoL < vectorDoc2M0201ForE.size(); intNoL++) {
            if (!"".equals(stringTemp)) stringTemp += "�B";
            stringTemp += "" + vectorDoc2M0201ForE.get(intNoL);
          }
          stringTemp = "�O�Ϊ��� " + (intRowNo + 1) + " �C�� [�v��}�o��] �� [�дڥN�X] �u�ର " + stringTemp + "�C\n(�����D�Ь� [�]�ȫ�])";
          return doReturnTable2Err(intTable2Panel, intRowNo, stringTemp);
        }
      }
      if (exeUtil.doParseDouble(stringRealMoney) == 0 && exeUtil.doParseDouble(stringRealTotalMoney) == 0) {
        stringTemp = "�O�Ϊ��� " + (intRowNo + 1) + " �C�� [�дڪ��B] ���i���s�ΪťաC";
        return doReturnTable2Err(intTable2Panel, intRowNo, stringTemp);
      }
      if (exeUtil.doParseDouble(stringRealMoney) != 0 && exeUtil.doParseDouble(stringRealTotalMoney) != 0) { // 2011-04-01 �ק�
        // �����B�z
      } else if (exeUtil.doParseDouble(stringRealTotalMoney) != 0) {
        stringRealMoney = convert.FourToFive("" + (exeUtil.doParseDouble(stringRealTotalMoney) / (1 + doubleTaxRate)), 0);
        setValueAt("Table2", stringRealMoney, intRowNo, "RealMoney");
        retTable2[intRowNo][10] = stringRealMoney;
      } else {
        stringRealTotalMoney = convert.FourToFive("" + (exeUtil.doParseDouble(stringRealMoney) * (1 + doubleTaxRate)), 0);
        setValueAt("Table2", stringRealTotalMoney, intRowNo, "RealTotalMoney");
      }
      // �w���ƾ�z
      // (�L���ʳ�ɡA�u�w��~�~�@�w���ˮ�)
      booleanDepartNoTemp = "O".equals(stringInOut) && (!booleanPurchaseExist || (booleanPurchaseExist && "2".equals(stringVersion) && "�s�W".equals(value))) && !booleanNoUseBudget;
      if (booleanDepartNoTemp) {
        retDoc7M011 = exeFun.getDoc7M011(stringComNo, "", stringCostID, stringCostID1);
        if (retDoc7M011.length > 0 && !"".equals(retDoc7M011[0][0].trim())) {
          booleanDepartNoTemp = vectorProjectID1NoUseBudget
              .indexOf(stringDepart + "---" + stringProjectID + "---" + stringProjectID1 + "---" + retDoc7M011[0][0].trim().substring(0, 1)) == -1;
        }
      }
      if (!booleanPurchaseExist && !booleanDepartNoTemp && vectorDeptCd.indexOf(stringDepart) != -1) {
        retDoc7M011 = exeFun.getDoc7M011(stringComNo, "", stringCostID, stringCostID1);
        // 2011-04-08 �S��קO���� �ק�
        if (jtable13.getRowCount() == 0 && ("," + stringSpecBudget + ",").indexOf(stringDepart) != -1 && !retDoc7M011[0][0].startsWith("B")) {
          stringTemp = stringDepart + " �Ȥ��\�ӽ� ������ [�дڥN�X]�C\n(�����D�Ь� [�]�ȫ�])";
          return doReturnTable2Err(intTable2Panel, -1, stringTemp);
        }
        if (retDoc7M011.length > 0 && retDoc7M011[0][0].startsWith("B")) {
          booleanDepartNoTemp = true;
          stringProjectID1 = stringDepart;
        }
        if ("03365,".indexOf(stringDepart + ",") != -1 && retDoc7M011.length > 0 && "A".equals(retDoc7M011[0][0].trim())) {
          booleanDepartNoTemp = true;
          stringProjectID1 = stringDepart;
        }
      }
      System.out.println(intRowNo + "�w���ƾ�z--------------2");
      if (booleanDepartNoTemp) {
        stringTemp = ("0531".equals(stringDepart.trim())) ? "053" + stringProjectID1 : stringProjectID1;
        // �w��W�L���i����
        if (vectorCostIDExceptBudget.indexOf(stringCostID + stringCostID1) != -1 || booleanNoCheck) {
          doubleTemp = exeUtil.doParseDouble("" + hashtableRealMoney.get(stringTemp)) + exeUtil.doParseDouble(stringRealTotalMoney);
          hashtableRealMoney.put(stringTemp, "" + doubleTemp);
        }
        //
        retDoc7M011 = exeFun.getDoc7M011(stringComNo, "", stringCostID, stringCostID1);
        if (retDoc7M011.length > 0 && !"".equals(retDoc7M011[0][0].trim())) {
          stringBudgetID = retDoc7M011[0][0].trim();
          //
          if (vectorProjectID1PurchaseCheck.indexOf(stringTemp + "%---%" + stringBudgetID.substring(0, 1)) == -1)
            vectorProjectID1PurchaseCheck.add(stringTemp + "%---%" + stringBudgetID.substring(0, 1));
          if (vectorBudgetID.indexOf(stringBudgetID) == -1) vectorBudgetID.add(stringBudgetID);
          //
          doubleTemp = exeUtil.doParseDouble("" + hashtableProjectBudgetSum.get(stringTemp + "-" + stringBudgetID)) + exeUtil.doParseDouble(stringRealTotalMoney);
          hashtableProjectBudgetSum.put(stringTemp + "-" + stringBudgetID, "" + doubleTemp);
          //
          doubleTemp = exeUtil.doParseDouble("" + hashtableProjectMoneySum.get(stringTemp + "%---%" + stringBudgetID.substring(0, 1)))
              + exeUtil.doParseDouble(stringRealTotalMoney);
          hashtableProjectMoneySum.put(stringTemp + "%---%" + stringBudgetID.substring(0, 1), "" + doubleTemp);
        }
      }
      if (vectorDeptCd.indexOf(stringDepart) != -1) {
        stringProjectID1 = "";
      }
      // BT �קO�ˮ�
      retDoc7M0265 = exeFun.getTableDataDoc("SELECT  AreaNum,  DateStart,  DateEnd,  ProjectIDMajor " + " FROM  Doc7M0265 " + " WHERE  ProjectID1  =  '" + stringProjectID1 + "' "
          + " AND  ComNo  =  '" + stringComNo + "' ");
      if (!isProjetIDExcept() && getTableData("Table13").length == 0 && !booleanPurchaseExist && stringFunctionName.indexOf("ñ��") == -1 && retDoc7M0265.length > 0) {
        if (stringCDateAC.compareTo(retDoc7M0265[0][1].trim()) < 0) {
          stringTemp = "�� " + (intRowNo + 1) + " �C [�קO](" + stringProjectID1 + ") �|�����\�ϥΡC\n(�����D�Ь� [�]�ȫ�])";
          return doReturnTable2Err(intTable2Panel, intRowNo, stringTemp);
        }
        if (stringCDateAC.compareTo(retDoc7M0265[0][2].trim()) > 0) {
          if (stringFunctionName.indexOf("�ӿ�") != -1 && "131".equals(stringCostID + stringCostID1)) {
            // �S�ҡA�Ȥ��\��J�ܩӿ�Ӥw
          } else {
            if (stringFunctionName.indexOf("ñ��") != -1) {
              messagebox("�� " + (intRowNo + 1) + " �C [�קO](" + stringProjectID1 + ") �w�����\�ϥΡC\n(���Ľd��" + retDoc7M0265[0][1].trim() + "��" + retDoc7M0265[0][2].trim() + ")");
            } else {
              stringTemp = "�� " + (intRowNo + 1) + " �C [�קO](" + stringProjectID1 + ") �w�����\�ϥΡC\n(�����D�Ь� [�]�ȫ�])";
              return doReturnTable2Err(intTable2Panel, intRowNo, stringTemp);
            }
          }
        }
      }
      if (!booleanPurchaseExist) {
        if (retDoc7M011 == null) retDoc7M011 = exeFun.getDoc7M011(stringComNo, "", stringCostID, stringCostID1);
        /*
         * if("100/11/16".compareTo(stringCDate)>0 && retDoc7M0265.length > 0 &&
         * "BT".equals(retDoc7M0265[0][3].trim())) { if(retDoc7M011.length > 0) {
         * stringBudgetID = retDoc7M011[0][0].trim() ; if("".equals(stringProjectID1) ||
         * !stringBudgetID.startsWith("B") || "".equals(stringBudgetID)) {
         * if("BC31,BC32,".indexOf(stringBudgetID)==-1 &&
         * "Y".equals(stringProjectID1BTType)) { stringTemp =
         * "[BT �קO] ���i�P �D [BT �קO] �@�_�ϥΡC\n(�����D�Ь� [�]�ȫ�])" ; return
         * doReturnTable2Err(intTable2Panel, stringTemp) ; } stringProjectID1BTType =
         * "N" ; // �D BT�קO } else { if(retDoc7M0265.length == 0) {
         * if("BC31,BC32,".indexOf(stringBudgetID)==-1 &&
         * "Y".equals(stringProjectID1BTType)) { stringTemp =
         * "[BT �קO] ���i�P �D [BT �קO] �@�_�ϥΡC\n(�����D�Ь� [�]�ȫ�])" ; return
         * doReturnTable2Err(intTable2Panel, stringTemp) ; } stringProjectID1BTType =
         * "N" ; // �D BT�קO } else { if("BC31,BC32,".indexOf(stringBudgetID)==-1 &&
         * "N".equals(stringProjectID1BTType)) { stringTemp =
         * "[BT �קO] ���i�P �D [BT �קO] �@�_�ϥΡC\n(�����D�Ь� [�]�ȫ�])" ; return
         * doReturnTable2Err(intTable2Panel, stringTemp) ; } // stringProjectID1BTType =
         * "Y" ; } } } else { stringProjectID1BTType = "N" ; } } else {
         * if("Y".equals(stringProjectID1BTType)) { stringTemp =
         * "[BT �קO] ���i�P �D [BT �קO] �@�_�ϥΡC\n(�����D�Ь� [�]�ȫ�])" ; return
         * doReturnTable2Err(intTable2Panel, stringTemp) ; } stringProjectID1BTType =
         * "N" ; }
         */
      }
      //
      if ((vectorPocketMoney.indexOf(stringCostID) != -1 || jtable1.getRowCount() > 0) && exeUtil.doParseDouble(stringRealTotalMoney) < 0) {
        // 009 ���\���B���t�� 2011-04-01
        if (vectorCostIDX.indexOf(stringCostID + stringCostID1) == -1) {
          stringTemp = "�O�Ϊ��� " + (intRowNo + 1) + " �C�� [�дڪ��B] ���i���t�ȡC\n(�����D�Ь� [�]�ȫ�])";
          return doReturnTable2Err(intTable2Panel, intRowNo, stringTemp);
        }
      }
      if (!booleanPurchaseExist) {
        if (vectorCostID3.indexOf(stringCostID + stringCostID1) != -1) {
          stringTemp = "�O�Ϊ��� " + (intRowNo + 1) + " �C [�дڥN�X][�p�дڥN�X] �����\�ϥΡC\n(�����D�Ь� [��P�޲z��])�C";
          return doReturnTable2Err(intTable2Panel, intRowNo, stringTemp);
        }
      }
      // �S��O�ΥN�X
      if (vectorCostIDExcept.indexOf(stringCostID + stringCostID1) != -1) booleanCostIDExcept = true;
      if (vectorCostIDZ.indexOf(stringCostID + stringCostID1) != -1) booleanCostIDZ = true;
      // �|�B�P�__+5
      if ("B".equals(stringDocNoType) && "729".equals(stringCostID + stringCostID1)) {
        if (exeUtil.doParseDouble(stringRealMoney) != exeUtil.doParseDouble(stringRealTotalMoney)) {
          stringTemp = "�O�Ϊ��� " + (intRowNo + 1) + " �C�� [�ƫ�ɵo��] �B [�дڥN�X] 729 �ɡA[�дڪ��B] �P [���|���B] ���@�P�C\n(�����D�Ь� [�]�ȫ�])";
          return doReturnTable2Err(intTable2Panel, intRowNo, stringTemp);
        }
        System.out.println(intRowNo + "-----------------------------C");
      } else {
        doubleRealTaxR = Math.round(exeUtil.doParseDouble(stringRealTotalMoney) - exeUtil.doParseDouble(stringRealMoney));
        doubleRealTaxS = exeUtil.doParseDouble(convert.FourToFive("" + (exeUtil.doParseDouble(stringRealMoney) * doubleTaxRate), 0));
        System.out.println(intRowNo + "-----------------------------D");
        if (doubleRealTaxR < doubleRealTaxS - 5 || doubleRealTaxR > doubleRealTaxS + 5) {
          if (("F".equals(stringInvoiceTaxType) && !"B".equals(stringDocNoType)) || vectorPocketMoney.indexOf(stringCostID) != -1 || booleanCostIDExcept) {
            // �o���h�榡�ɡA�B�t�|�Υ��t�|�V�X��
            // ���s�Ϊ��Τ��Z����
          } else if (stringFunctionName.indexOf("--") != -1) {
            // �H�`�ҥ~
          } else if (stringFunctionName.indexOf("ñ��") != -1 && getValue("DocNo1").indexOf("023") != -1) {
            // �]�� �H�`�ҥ~
          } else {
            if (!"E62232".equals(stringBarCode)) {
              stringTemp = "�O�Ϊ��� " + (intRowNo + 1) + " �C�� [�дڪ��B] �P [���|���B] ���t�B���i�W�L [�дڪ��B] ���H 1+[�|�v] �����t 5�C\n(�����D�Ь� [�]�ȫ�])";
              return doReturnTable2Err(intTable2Panel, intRowNo, stringTemp);
            }
          }
        }
      }
      //
      if ("B".equals(stringDocNoType) && !"729".equals(stringCostID + stringCostID1)) {

      } else {
        if (doubleRealTaxR > 0 && getTable("Table1").getRowCount() == 0) {
          stringTemp = "�O�Ϊ��� " + (intRowNo + 1) + " �C�� �L�o����ƮɡA[�дڪ��B] ���P [���|���B] �@�P�C";
          return doReturnTable2Err(intTable2Panel, intRowNo, stringTemp);
        }
      }
      doubleRealTaxSum += doubleRealTaxR;
      // ����
      stringKey = stringInOut + "-" + stringDepart + "-" + stringProjectID + "-" + stringProjectID1 + "-" + stringCostID + "-" + stringCostID1;
      // System.out.println(intRowNo+"�O��key--------------"+stringKey) ;
      stringRealMoneyT = ("" + hashtable2Key.get(stringKey)).trim();
      if (!"null".equals(stringRealMoneyT) && !booleanCostID130 && !booleanCostIDTypeF) {
        stringTemp = "�O�Ϊ��� " + (intRowNo + 1) + " �C�� [�~�O] + [����] + [�קO] ��ƭ��СC";
        return doReturnTable2Err(intTable2Panel, intRowNo, stringTemp);
      }
      if (vectorMPurKey.indexOf(stringKey) == -1) vectorMPurKey.add(stringKey);
      if (!booleanTable9 && booleanMPurMKey && vectorMPurKey.size() > 1 && !"b4177".equals(getUser())
          && ",0333H73A10110009,0333H73A10110010,".indexOf("," + getValue("DocNo") + ",") == -1) {
        stringTemp = "�h���ʳ�B�h���u��ƮɡA�{���L�k����C";
        return doReturnTable2Err(intTable2Panel, intRowNo, stringTemp);
      }
      hashtable2Key.put(stringKey, stringRealMoney);
      // �дڥN�X���o���ťաC
      if ("".equals(stringCostID.trim())) {
        stringTemp = "�O�Ϊ��� " + (intRowNo + 1) + " �C�� [�дڥN�X] ���o���ťաC";
        return doReturnTable2Err(intTable2Panel, intRowNo, stringTemp);
      }
      if ("".equals(stringCostID1.trim())) {
        stringTemp = "�O�Ϊ��� " + (intRowNo + 1) + " �C�� [�p�дڥN�X] ���o���ťաC";
        return doReturnTable2Err(intTable2Panel, intRowNo, stringTemp);
      }
      System.out.println("getDoc2M020---------------------------------------------------------");
      retDoc2M020 = exeFun.getDoc2M020(stringComNo, stringCostID, stringCostID1);
      if (retDoc2M020.length == 0) {
        if (exeFun.getDoc2M021(stringComNo, stringCostID, stringCostID1).indexOf(getValue("BarCode").trim()) == -1) {
          stringTemp = "�O�Ϊ��� " + (intRowNo + 1) + " �C�� [�p�дڥN�X] ���s�b�� [�O��-�w��N�X-�ɤ�|�p��ع�Ӫ�]�C\n(�����D�Ь� [��P�޲z��])";
          return doReturnTable2Err(intTable2Panel, intRowNo, stringTemp);
        }
      }
      if (vectorPurchaseNoExist.indexOf(stringCostID) != -1 && booleanPurchaseExist) {
        stringTemp = "�O�Ϊ��� " + (intRowNo + 1) + " �C�� [�дڥN�X] ���w�u��L���ʳ�C\n(�����D�Ь� [��P�޲z��])";
        return doReturnTable2Err(intTable2Panel, intRowNo, stringTemp);
      }
      if (!booleanPurchaseExist && !"".equals(stringBarCodeOld) && stringBarCodeDoc2M010.indexOf(stringBarCodeOld) == -1) {
        stringKey = stringCostID + "-" + stringCostID1;
        if ("F46682,J59253,".indexOf(stringBarCodeOld) == -1 && vectorDoc2M020.indexOf(stringKey) == -1) {
          stringTemp = "�O�Ϊ��� " + (intRowNo + 1) + " �C�� [�дڥN�X] �D�i�L���ʳ椧 [�дڥN�X]1�C\n(�����D�Ь� [��P�޲z��])";
          return doReturnTable2Err(intTable2Panel, intRowNo, stringTemp);
        }
      }
      // ���o����P�_
      if (vectorCostIDForInvoice.indexOf(stringCostID + stringCostID1) != -1) stringInvoiceKind = "TRUE";
      // ����
      if (!booleanTable21 && "".equals(stringDepart.trim())) {
        stringTemp = "�O�Ϊ��� " + (intRowNo + 1) + " �C�� [����] ���o���ťաC";
        return doReturnTable2Err(intTable2Panel, intRowNo, stringTemp);
      }
      /*
       * if("I".equals(stringInOut) && stringDepart.length( )!=4 &&
       * !"03363".equals(stringDepart)) { stringTemp = "�O�Ϊ��� " +(intRowNo+1)
       * +" �C [����] �u�ର 4 ��Ʀr�C" ; return doReturnTable2Err(intTable2Panel, intRowNo,
       * stringTemp) ; }
       */
      if (!booleanTable21 && "3011,".indexOf(stringDepart) == -1 && "".equals(exeFun.getDepartName(stringDepart.trim()))) {
        stringTemp = "�O�Ϊ��� " + (intRowNo + 1) + " �C�� [����] ���s�b��Ʈw���C";
        return doReturnTable2Err(intTable2Panel, intRowNo, stringTemp);
      }
      // System.out.println("------------------�O�ΥN�X�P���~�~�@�P�ˮ�") ;
      if (!booleanTable21 && vectorDeptCd2.indexOf(stringDepart) == -1 && vectorDeptCd.indexOf(stringDepart) == -1
          && !exeFun.isInOutToCostID(stringInOut.trim(), stringCostID.trim())) {
        stringTemp = "�O�Ϊ��� " + (intRowNo + 1) + " �C�� [�дڥN�X] �P [��/�~�~] ���@�P�C";
        return doReturnTable2Err(intTable2Panel, intRowNo, stringTemp);
      }
      if (booleanTable21) {
      } else if ("O".equals(stringInOut.trim())) {
        if ("".equals(stringProjectID.trim())) {
          stringTemp = "�O�Ϊ��� " + (intRowNo + 1) + " �C�� [�j�קO] ���o���ťաC";
          return doReturnTable2Err(intTable2Panel, intRowNo, stringTemp);
        }
        if ("".equals(stringProjectID1.trim())) {
          stringTemp = "�O�Ϊ��� " + (intRowNo + 1) + " �C�� [�p�קO] ���o���ťաC";
          return doReturnTable2Err(intTable2Panel, intRowNo, stringTemp);
        }
        // �s�b�ˬd
        System.out.println(intRowNo + "isExistProjectIDCheck-----------------------------S");
        if (!stringDepart.startsWith("053") && !exeFun.isExistProjectIDCheck(stringProjectID, stringProjectID1)) {
          stringTemp = "�O�Ϊ��� " + (intRowNo + 1) + " �C�� [�j�קO] [�p�קO] ���s�b���Ʈw���C\n(�����D�Ь� [��P�޲z��])";
          return doReturnTable2Err(intTable2Panel, intRowNo, stringTemp);
        }
        if (!"0331".equals(stringDepart.trim()) && !"1331".equals(stringDepart.trim()) && !"0531".equals(stringDepart.trim())) {
          stringTemp = "�~�~�ɡA�O�Ϊ��� " + (intRowNo + 1) + " �C�� [����] �u�ର [0331] �� [1331]�C";
          return doReturnTable2Err(intTable2Panel, intRowNo, stringTemp);
        }
        //
        if ("0531".equals(stringDepart.trim()) && vectorProjectID1.indexOf(stringProjectID1) == -1) {
          stringTemp = "";
          for (int intNoL = 0; intNoL < vectorProjectID1.size(); intNoL++) {
            if (!"".equals(stringTemp)) stringTemp += " �B ";
            stringTemp += "[" + vectorProjectID1.get(intNoL) + "]";
          }
          stringTemp = "�~�~�B������ 0531 �ɡA�O�Ϊ��� " + (intRowNo + 1) + " �C�� [�קO] �u�ର " + stringTemp + "�C";
          return doReturnTable2Err(intTable2Panel, intRowNo, stringTemp);
        }
        // �{���� hashtableAProject �o�� �� Depart �� 8 �ɡA�������A�p�����D�A�����ϥΪ�
        stringDepart1 = "" + hashtableAProject.get(stringProjectID);
        stringDepart2 = "" + hashtableAProject.get(stringProjectID1);
        System.out.println(intRowNo + "-----------------------------1111111111");
        if (!booleanCostIDTypeF && !"0531".equals(stringDepart.trim())) {
          if (!"1331".equals(stringDepart.trim())) {
            if (("8".equals(stringDepart2) || "8".equals(stringDepart1))) {
              stringDepartMessage = "�O�Ϊ��� " + (intRowNo + 1) + " �C���קO���� 1331�A�����\���ʸ�Ʈw�C";
              return doReturnTable2Err(intTable2Panel, intRowNo, stringDepartMessage);
            }
          } else {
            if (!("8".equals(stringDepart2) || "8".equals(stringDepart1))) {
              stringDepartMessage = "�O�Ϊ��� " + (intRowNo + 1) + " �C���קO������ 1331�A�����\���ʸ�Ʈw�C";
              return doReturnTable2Err(intTable2Panel, intRowNo, stringDepartMessage);
            }
          }
        }
      } else {
        if ("033MP".equals(stringDepart.trim())) {
          stringDepartMessage = "�O�Ϊ��� " + (intRowNo + 1) + " �C [033MP] �ݩ�~�~�A�����\���ʸ�Ʈw�C";
          return doReturnTable2Err(intTable2Panel, intRowNo, stringDepartMessage);
        }
        if (("," + stringSpecBudget + ",").indexOf(stringDepart.trim()) == -1 && !exeUtil.isDigitNum(stringDepart.trim())) {
          stringDepartMessage = "�O�Ϊ��� " + (intRowNo + 1) + " �C ���~����(" + stringDepart.trim() + ")�榡���~�A�����\���ʸ�Ʈw�C";
          return doReturnTable2Err(intTable2Panel, intRowNo, stringDepartMessage);
        }
      }
      System.out.println(intRowNo + "-----------------------------2222222");
      if (booleanPurchaseExist && !"flife".equalsIgnoreCase(getUser()) && ",0333H73A10110009,0333H73A10110010,".indexOf("," + getValue("DocNo") + ",") == -1) {
        // �����ʳ�
        if ("1".equals(stringVersion)) {
          if (hashtableUsedRealMoney == null) {
            // �O�Τ��u��z
            hashtableUsedRealMoney = getUsedProjectIDMoney2(exeFun);
          }
          if (hashtableUsedRealMoney == null) {
            stringTemp = "��Ƶo�Ϳ��~�A�Ь� [��T������]�C";
            return doReturnTable2Err(intTable2Panel, intRowNo, stringTemp);
          }
          stringKey = stringInOut + stringLimit + // 2 InOut
              stringDepart + stringLimit + // 3 DepartNo
              stringProjectID + stringLimit + // 4 ProjectID
              stringProjectID1 + stringLimit + // 5 ProjectID1
              stringCostID + stringLimit + // 6 CostID
              stringCostID1; // 7 CostID1
          doubleTemp = exeUtil.doParseDouble("" + hashtableUsedRealMoney.get(stringKey));
          System.out.println(
              "�O�Ϊ��� " + (intRowNo + 1) + " �C�A[�O�Ϊ��B�X�p](" + exeUtil.getFormatNum2(stringRealTotalMoney) + ") <==> [���ʥӽЮѤ��i�Ϊ��B�X�p](" + exeUtil.getFormatNum2("" + doubleTemp) + ")�C");
          if (doubleTemp < exeUtil.doParseDouble(stringRealTotalMoney)) {
            stringTemp = "�O�Ϊ��� " + (intRowNo + 1) + " �C�A[�O�Ϊ��B�X�p](" + exeUtil.getFormatNum2(stringRealTotalMoney) + ") �j�� [���ʥӽЮѤ��i�Ϊ��B�X�p](" + exeUtil.getFormatNum2("" + doubleTemp)
                + ")�C";
            if ("B3018".equals(getUser())) stringTemp += "\n[" + stringKey + "]";
            return doReturnTable2Err(intTable2Panel, intRowNo, stringTemp);
          }
          //
          System.out.println("�קO���u�ˮ�------------------------------S");
          if (vectorCostIDLimit.indexOf(stringKey) != -1) {
            // if(!booleanTable9 && vectorCostIDLimit.indexOf(stringKey) != -1) {
            doubleCF = exeUtil.doParseDouble("" + hashtableLimitSmall.get(stringKey));
            System.out.println("stringKey(" + stringKey + "doubleCF(" + doubleCF + ")------------------------------");
            if (doubleCF > exeUtil.doParseDouble(stringRealTotalMoney)) {
              return doReturnTable2Err(intTable2Panel, intRowNo,
                  "�O�Ϊ��� " + (intRowNo + 1) + " �C�A[�O�Ϊ��B�X�p](" + exeUtil.getFormatNum2(stringRealTotalMoney) + ") ���o�p�� ���B(" + exeUtil.getFormatNum2("" + doubleCF) + ")�C");
            }
          }
          System.out.println("�קO���u�ˮ�------------------------------E");
        }
      }
    }
    // �L���ʳ�A�D 130 �ɡA���i���O�d��
    if ("N".equals(getValue("PurchaseNoExist").trim()) && !booleanCostID130 && exeUtil.doParseDouble(getValue("RetainMoney").trim()) > 0) {
      return doReturnTable2Err(intTable2Panel, -1, "�L���ʮɡA�Ȥ��\ �O�ΥN�X 130 (����) �i�@�O�d�ڡC");
    }
    // �L���ʳ�
    if (!"".equals(stringDepartMessage)) {
      int ans = JOptionPane.showConfirmDialog(null, stringDepartMessage, "�п�ܡH", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
      if (ans == JOptionPane.NO_OPTION) {
        return doReturnTable2Err(intTable2Panel, -1, "");
      }
    }
    // 831 + �t�Ӭ� �����س](04673318) +�L���ʳ�
    if (booleanCostID831) {
      String stringPurchaseNoExistL = getValue("PurchaseNoExist").trim();
      String stringPayCondition1L = getValue("PayCondition1").trim();
      String stringPayCondition2L = getValue("PayCondition2").trim();
      String stringFactoryNo = "";
      //
      if (getTable("Table1").getRowCount() > 0) {
        stringFactoryNo = ("" + getValueAt("Table1", 0, "FactoryNo")).trim();
      } else if (getTable("Table3").getRowCount() > 0) {
        stringFactoryNo = ("" + getValueAt("Table3", 0, "FactoryNo")).trim();
      }
      //
      if ("N".equals(stringPurchaseNoExistL) && "04673318".equals(stringFactoryNo) && (!"0".equals(stringPayCondition1L) || !"999".equals(stringPayCondition2L))) {
        int ans = JOptionPane.showConfirmDialog(null, "�O�ΥN�X�� [831]�A�t�Ӭ� [�����س](04673318)]�A�L���ʳ�ɡA�I�ڱ��󶷬��Y���A�O�_�~��y�{�C\n(�O�A�h�ܧ�I�ڱ��󬰧Y���A�~��y�{�A�_�h�A���_�y�{�C)", "�п�ܡH", JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        if (ans == JOptionPane.NO_OPTION) {
          return doReturnTable2Err(intTable2Panel, -1, "");
        }
        setValue("PayCondition1", "0");
        setValue("PayCondition2", "999");
      }
    }
    // �����B�קO���u�@�P�ˮ�
    if (!booleanDepartProjectIDSame) {
      String stringTempL = "[�дڳ�-����] ���s�b�קO���u���C";
      int intIndex = 0;
      if (booleanPurchaseExist) {
        intIndex = 0;
      } else {
        intIndex = intTable2Panel;
      }
      return doReturnTable2Err(intIndex, -1, stringTempL);
    }
    if (booleanCostIDTypeG && !booleanPurchaseExist) {
      // ��@�дڥN�X�ˬd
      if (vectorCostIDKindNum.size() > 1) {
        return doReturnTable2Err(intTable2Panel, -1, "�S��дڥN�X�u���\��J�@���O�ΡC\n(�����D�Ь� [�]�ȫ�])");
      }
    }
    if (booleanCostIDTypeF) {
      if (!isTable13CheckOK(intTable2Panel, false, hashtbleFunctionType, exeUtil, exeFun)) {
        jtabbedPane1.setSelectedIndex(intTable2Panel);
        vectorReturnValue.add("N");
        return vectorReturnValue;
      }
    } else {
      setTableData("Table13", new String[0][0]);
    }
    if (stringFunctionName.indexOf("ñ��") == -1 && boolean74) {
      messagebox("�дڥN�X�� 74 �ɡA[���夺�e] �����w�s�i�D�D�A�_�h [�]�ȫ�] �N�h��B�z�C");
    }
    if (booleanTable9) {
      // Table2 �P Table9 �@�P�ˮ�
      System.out.println("isTable2SameTable17CheckOK----------------------------------");
      if (!isTable2SameTable17CheckOK(exeUtil, exeFun)) return doReturnTable2ErrData();
      System.out.println("isTable17CheckOK----------------------------------");
      // Table17 �� ���ʶ��خקO���u�ˮ�
      if (!isTable17CheckOK(exeUtil, exeFun)) return doReturnTable2ErrData();
    }
    if (booleanCostIDTypeW) {
      // Table2 �P Table22 �@�P�ˮ�
      System.out.println("isTable2SameTable22CheckOK----------------------------------");
      if (!isTable2SameTable22CheckOK(stringCostID, exeUtil, exeFun)) return doReturnTable2ErrData();
    }

    // �S����ʳ椣�@�w��B�z
    boolean booleanFlag = true;
    Vector vectorDocNo = new Vector();
    vectorDocNo.add("03319612008");
    vectorDocNo.add("02329612141"); // ��F H59A �����ɡA�������
    if (vectorDocNo.indexOf(stringDocNo) == -1) {
      // 2011-04-08 �S��קO���� �ץ�
      if (stringSpecBudgetVoucher.indexOf(stringDocNo1) != -1 && stringFunctionName.indexOf("ñ��") != -1 && !isAssetPur()) {
        getButton("ButtonTable10").doClick();
        if (!"OK".equals("" + get("Doc2I010_STATUS"))) {
          return doReturnTable2Err(2, -1, "�п�J [�S��קO���ޤ��קO���u���] ��T�C\n(�����D�Ь� [�]�ȫ�])");
        }
      }
      // �w���ˮ�
      boolean boolean033FGCheck = ("," + stringSpecBudget + ",").indexOf(stringDocNo1) != -1;
      if (!boolean033FGCheck) {
        setTableData("Table14", new String[0][0]);
        setTableData("Table15", new String[0][0]);
      }
      if (!booleanPurchaseExist) {
        String stringFunctionType = "";
        String stringProjectID1F = "";
        String[] arrayTemp = null;
        String[][] retDoc7M020 = null;
        String[][] retDoc7M021 = null;
        double doubleThisRealMoney = 0;
        double doubleThisRealMoneyCF = 0;
        double doubleBudgetMoney = 0;
        if (boolean033FGCheck) {
          // 2011/05/10 �S��קO����
          getButton("Button033FG").doClick();
          String[][] retTableData = getTableData("TableCheck");
          if (retTableData.length == 0) {
            return doReturnTable2Err(intTable2Panel, -1, "��Ƶo�Ϳ��~�A�Ь� [��T������]�C");
          }
          if (retTableData.length == 1 && "OK".equals(retTableData[0][0])) {
            String stringCostIDZL = booleanCostIDZ ? "Y" : "N";
            //
            vectorReturnValue.add("Y");
            vectorReturnValue.add(hashtable2Key);
            vectorReturnValue.add(convert.FourToFive("" + doubleRealTaxSum, 0));
            vectorReturnValue.add(stringInvoiceKind);
            vectorReturnValue.add(stringCostIDZL);
            return vectorReturnValue;
          }
          return doReturnTable2Err(intTable2Panel, -1, "");
        } else {
          // �~��y�{
          if (booleanTable21) {
            // �ˮ�
            System.out.println("isTable21CheckOK-----------------------------------S");
            String stringCheck = isTable21CheckOK(exeUtil, exeFun);
            if (stringCheck.startsWith("ERR")) {
              int intPos = 0;
              String[] arrayTempL = convert.StringToken(stringCheck, "%-%");
              if (arrayTempL.length == 2) {
                if ("1".equals(arrayTempL[1])) intPos = 3;
                if ("3".equals(arrayTempL[1])) intPos = 4;
                if ("2".equals(arrayTempL[1])) intPos = intTable2Panel;
              }
              return doReturnTable2Err(intPos, -1, "");
            }
            System.out.println("isTable21CheckOK-----------------------------------E");
          }
          // �~�׹w���ˮ�
          Vector vectorTableCheck = new Vector();
          for (int intNo = 0; intNo < vectorProjectID1PurchaseCheck.size(); intNo++) {
            stringProjectID1 = ("" + vectorProjectID1PurchaseCheck.get(intNo)).trim();
            if ("".equals(stringProjectID1) || "null".equals(stringProjectID1)) continue;
            arrayTemp = convert.StringToken(stringProjectID1, "%---%");
            if (arrayTemp.length != 2) continue;
            stringProjectID1 = arrayTemp[0].trim();
            stringFunctionType = arrayTemp[1].trim();
            stringProjectID1F = stringProjectID1;
            //
            if (",03396,03335,033622,03363,0333,03365,".indexOf("," + stringProjectID1 + ",") != -1) stringProjectID1F = stringDepart;
            doubleThisRealMoney = exeUtil.doParseDouble("" + hashtableProjectMoneySum.get(stringProjectID1F + "%---%" + stringFunctionType));
            doubleThisRealMoneyCF = exeUtil.doParseDouble("" + hashtableRealMoney.get(stringProjectID1F));
            //
            for (int intNoL = 0; intNoL < vectorBudgetID.size(); intNoL++) {
              stringBudgetID = ("" + vectorBudgetID.get(intNoL)).trim();
              stringKey = stringProjectID1F + "-" + stringBudgetID;
              doubleTemp = exeUtil.doParseDouble("" + hashtableProjectBudgetSum.get(stringKey));
              if (doubleTemp <= 0) continue;
              arrayTemp = new String[1];
              arrayTemp[0] = stringProjectID1 + "%-%" + stringBudgetID + "%-%" + doubleTemp + "%-%" + doubleThisRealMoney + "%-%0%-%0%-%" + doubleThisRealMoneyCF;
              // 2011-04-08 �S��קO���� �ק�
              if (("," + stringSpecBudget + ",").indexOf(stringProjectID1) != -1 && !stringBudgetID.startsWith("B")) {
                return doReturnTable2Err(intTable2Panel, -1, stringProjectID1 + " �Ȥ��\�ӽ� ������ [�дڥN�X]�C\n(�����D�Ь� [�]�ȫ�])");
              }
              //
              vectorTableCheck.add(arrayTemp);
            }
            setTableData("TableCheck", (String[][]) vectorTableCheck.toArray(new String[0][0]));
            getButton("ButtonTableCheck").doClick();
            String[][] retTableData = getTableData("TableCheck");
            if (retTableData.length == 0) {
              return doReturnTable2Err(intTable2Panel, -1, "��Ƶo�Ϳ��~�A�Ь� [��T������]�C");
            }
            if (retTableData.length == 1 && "OK".equals(retTableData[0][0])) {
              String stringCostIDZL = booleanCostIDZ ? "Y" : "N";
              //
              vectorReturnValue.add("Y");
              vectorReturnValue.add(hashtable2Key);
              vectorReturnValue.add(convert.FourToFive("" + doubleRealTaxSum, 0));
              vectorReturnValue.add(stringInvoiceKind);
              vectorReturnValue.add(stringCostIDZL);
              return vectorReturnValue;
            }
            return doReturnTable2Err(intTable2Panel, -1, "");
          }
        }
      }
    }
    //
    String stringCostIDZ = booleanCostIDZ ? "Y" : "N";
    vectorReturnValue.add("Y");
    vectorReturnValue.add(hashtable2Key);
    vectorReturnValue.add(convert.FourToFive("" + doubleRealTaxSum, 0));
    vectorReturnValue.add(stringInvoiceKind);
    vectorReturnValue.add(stringCostIDZ);
    return vectorReturnValue;
  }

  public Hashtable getTable2DataHashtable(int intRowNo) throws Throwable {
    Hashtable hashtableTable2Data = new Hashtable();
    String stringFieldName = "";
    String stringFieldValue = "";
    String[] arrayFieldName = { "InOut", "DepartNo", "ProjectID", "ProjectID1", "CostID", "CostID1", "RealMoney", "RealTotalMoney" };
    for (int intNo = 0; intNo < arrayFieldName.length; intNo++) {
      stringFieldName = arrayFieldName[intNo].trim();
      stringFieldValue = ("" + getValueAt("Table2", intRowNo, stringFieldName)).trim();
      //
      hashtableTable2Data.put(stringFieldName, stringFieldValue);
    }
    return hashtableTable2Data;
  }

  public boolean isTable2CostIDCheckOK(int intRowNo, Vector vectorCostIDS, Hashtable hashtableTable2Data, FargloryUtil exeUtil, Doc2M010 exeFun) throws Throwable {
    String stringEmployeeNo = getValue("EmployeeNo").trim();
    String stringCostID = ("" + hashtableTable2Data.get("CostID")).trim();
    String stringCostID1 = ("" + hashtableTable2Data.get("CostID1")).trim();
    String stringTemp = "";
    //
    if (vectorCostIDS.indexOf(stringCostID + stringCostID1) == -1) return true;
    vectorCostIDS.remove(stringCostID + stringCostID1);
    //
    if (exeFun.getDoc3M011EmployeeNo("09", " AND  EmployeeNo  =  '" + stringEmployeeNo + "' ").length == 0) {
      stringTemp = "�O�Ϊ��� " + (intRowNo + 1) + " �C �L�v���ӽЦ� [�дڥN�X]�A�����\���ʸ�Ʈw�C";
      return doReturnTable2ErrB(intRowNo, stringTemp);
    }
    boolean booleanTable21 = getTable("Table21").getRowCount() > 0;
    if (!booleanTable21) {
      stringTemp = "�O�Ϊ��� " + (intRowNo + 1) + " �C �~���ƿ��~�C";
      return doReturnTable2ErrB(intRowNo, stringTemp);
    }
    return true;
  }

  public boolean isAssetPur() throws Throwable {
    JTable jtable7 = getTable("Table7");
    String stringBarCode = getValue("BarCode").trim();
    if (",P38453,".indexOf("," + stringBarCode + ",") != -1) return false;
    String stringOptometryType = "";
    for (int intNo = 0; intNo < jtable7.getRowCount(); intNo++) {
      stringOptometryType = ("" + getValueAt("Table7", intNo, "OptometryType")).trim();
      if ("B".equals(stringOptometryType)) return true;
    }
    return false;
  }

  public boolean isCoinTypeCheckOK(FargloryUtil exeUtil, Doc2M010 exeFun) throws Throwable {
    JTable jtable2 = getTable("Table2");
    String stringComNo = getValue("ComNo").trim();
    String stringInOut = "";
    String stringProjectID1 = "";
    String stringCoinTypeL = "";
    String stringCoinType = "";
    Vector vectorProjectID1 = new Vector();
    Hashtable hashtableAnd = new Hashtable();
    //
    for (int intNo = 0; intNo < jtable2.getRowCount(); intNo++) {
      stringInOut = ("" + getValueAt("Table2", intNo, "InOut")).trim();
      stringProjectID1 = ("" + getValueAt("Table2", intNo, "ProjectID1")).trim();
      //
      if ("I".equals(stringInOut)) {
        stringCoinTypeL = "NTD";
      } else {
        if (vectorProjectID1.indexOf(stringProjectID1) != -1) continue;
        vectorProjectID1.add(stringProjectID1);
        //
        System.out.println(intNo + "---------------------------------S");
        hashtableAnd.put("ComNo", stringComNo);
        hashtableAnd.put("ProjectID1", stringProjectID1);
        stringCoinTypeL = exeFun.getNameUnionDoc("CoinType", "Doc7M0204", "", hashtableAnd, exeUtil);
        if ("".equals(stringCoinTypeL)) {
          hashtableAnd.put("ComNo", stringComNo);
          hashtableAnd.put("ProjectID1", stringProjectID1);
          stringCoinTypeL = exeFun.getNameUnionDoc("CoinType", "Doc7M020", "", hashtableAnd, exeUtil);
        }
        if ("".equals(stringCoinTypeL)) stringCoinTypeL = "NTD";
      }
      //
      if (!"".equals(stringCoinTypeL) && !"".equals(stringCoinType) && !stringCoinType.equals(stringCoinTypeL)) {
        messagebox("�дڳ� �����\�h���ȡC");
        return false;
      }
      System.out.println(intNo + "---------------------------------E");
      stringCoinType = stringCoinTypeL;
    }
    //
    setValue("CoinType", stringCoinType);
    return true;
  }

  public Vector doReturnTable2Err(int intTable2Panel, int intSelectRow, String stringMessage) throws Throwable {
    System.out.println("doReturnTable2Err---------------------------------S");
    JTable jtable2 = getTable("Table2");
    JTabbedPane jtabbedPane1 = getTabbedPane("Tab1");
    //
    if (intSelectRow != -1) {
      jtable2.setRowSelectionInterval(intSelectRow, intSelectRow);
    }
    if (!"".equals(stringMessage)) {
      messagebox(stringMessage);
    }
    jtabbedPane1.setSelectedIndex(intTable2Panel);
    System.out.println("doReturnTable2Err---------------------------------E");
    return doReturnTable2ErrData();
  }

  public boolean doReturnTable2ErrB(int intSelectRow, String stringMessage) throws Throwable {
    JTable jtable2 = getTable("Table2");
    int intTable2Panel = 2;
    JTabbedPane jtabbedPane1 = getTabbedPane("Tab1");
    //
    if (intSelectRow != -1) {
      jtable2.setRowSelectionInterval(intSelectRow, intSelectRow);
    }
    if (!"".equals(stringMessage)) {
      messagebox(stringMessage);
    }
    jtabbedPane1.setSelectedIndex(intTable2Panel);
    return false;
  }

  public Vector doReturnTable2ErrData() throws Throwable {
    System.out.println("doReturnTable2ErrData-----------------------------S");
    Vector vectorReturnValue = new Vector();
    vectorReturnValue.add("N");
    System.out.println("doReturnTable2ErrData-----------------------------E");
    return vectorReturnValue;
  }

  public void doLimitMoneyTable6(Hashtable hashtableLimitSmall, Vector vectorCostIDLimit, FargloryUtil exeUtil, Doc2M010 exeFun) throws Throwable {
    JTable jtable6 = getTable("Table6");
    String stringPurchaseNo1 = "";
    String stringPurchaseNo2 = "";
    String stringPurchaseNo3 = "";
    String stringFactoryNo = "";
    String stringInOut = "";
    String stringDepart = "";
    String stringProjectID = "";
    String stringProjectID1 = "";
    String stringCostID = "";
    String stringCostID1 = "";
    String stringKey = "";
    String stringLimit = "%---%";
    String stringComNo = getValue("ComNo").trim();
    String[][] retDoc3M014 = null;
    double doublePurchaseMoney = 0;
    double doubleRealMoney = 0;
    double doubleAmt = 0;
    for (int intNo = 0; intNo < jtable6.getRowCount(); intNo++) {
      stringPurchaseNo1 = ("" + getValueAt("Table6", intNo, "PurchaseNo1")).trim();
      stringPurchaseNo2 = ("" + getValueAt("Table6", intNo, "PurchaseNo2")).trim();
      stringPurchaseNo3 = ("" + getValueAt("Table6", intNo, "PurchaseNo3")).trim();
      stringFactoryNo = ("" + getValueAt("Table6", intNo, "FactoryNo")).trim();
      doublePurchaseMoney = exeUtil.doParseDouble(("" + getValueAt("Table6", intNo, "PurchaseMoney")).trim());
      doubleAmt = 0;
      //
      retDoc3M014 = exeFun.getDoc3M014ForPurchaseNoUnion("Doc3M014", stringComNo, stringPurchaseNo1 + stringPurchaseNo2 + stringPurchaseNo3);
      for (int intNoL = 0; intNoL < retDoc3M014.length; intNoL++) {
        doubleRealMoney = exeUtil.doParseDouble(retDoc3M014[intNoL][6].trim());
        doubleAmt += doubleRealMoney;
      }
      doubleAmt = exeUtil.doParseDouble(convert.FourToFive("" + doubleAmt, 0));
      if (retDoc3M014.length == 1 || doubleAmt == doublePurchaseMoney) {
        // �����ܤֶ��ӽжO��
        for (int intNoL = 0; intNoL < retDoc3M014.length; intNoL++) {
          stringInOut = retDoc3M014[intNoL][0].trim();
          stringDepart = retDoc3M014[intNoL][1].trim();
          stringProjectID = retDoc3M014[intNoL][2].trim();
          stringProjectID1 = retDoc3M014[intNoL][3].trim();
          stringCostID = retDoc3M014[intNoL][4].trim();
          stringCostID1 = retDoc3M014[intNoL][5].trim();
          //
          stringKey = stringInOut + stringLimit + // 2 InOut
              stringDepart + stringLimit + // 3 DepartNo
              stringProjectID + stringLimit + // 4 ProjectID
              stringProjectID1 + stringLimit + // 5 ProjectID1
              stringCostID + stringLimit + // 6 CostID
              stringCostID1; // 7 CostID1
          if (vectorCostIDLimit.indexOf(stringKey) == -1) vectorCostIDLimit.add(stringKey);
          //
          if (retDoc3M014.length == 1) {
            doubleRealMoney = doublePurchaseMoney + exeUtil.doParseDouble("" + hashtableLimitSmall.get(stringKey));
          } else {
            doubleRealMoney = exeUtil.doParseDouble(retDoc3M014[intNoL][6].trim()) + exeUtil.doParseDouble("" + hashtableLimitSmall.get(stringKey));
          }
          System.out.println("doLimitMoneyTable6---stringKey(" + stringKey + ")doubleRealMoney(" + doubleRealMoney + ")----------------------------------------");
          hashtableLimitSmall.put(stringKey, convert.FourToFive("" + doubleRealMoney, 0));
        }
      }
    }
  }

  public boolean isTable2SameTable17CheckOK(FargloryUtil exeUtil, Doc2M010 exeFun) throws Throwable {
    // �קO ���B �@�P�ˮ�
    JTable jtable2 = getTable("Table2");
    JTable jtable17 = getTable("Table17");
    String stringInOut = "";
    String stringDepartNo = "";
    String stringProjectID1 = "";
    String stringRealTotalMoney = "";
    String stringCostID = "";
    String stringCostID1 = "";
    String stringKEY = "";
    Vector vectorKEY2 = new Vector();
    Vector vectorKEY17 = new Vector();
    Hashtable hashtableMoney = new Hashtable();
    double doubleMoney2 = 0;
    double doubleMoney17 = 0;
    for (int intNo = 0; intNo < jtable2.getRowCount(); intNo++) {
      stringInOut = ("" + getValueAt("Table2", intNo, "InOut")).trim();
      stringDepartNo = ("" + getValueAt("Table2", intNo, "DepartNo")).trim();
      stringProjectID1 = ("" + getValueAt("Table2", intNo, "ProjectID1")).trim();
      stringRealTotalMoney = ("" + getValueAt("Table2", intNo, "RealTotalMoney")).trim();
      stringCostID = ("" + getValueAt("Table2", intNo, "CostID")).trim();
      stringCostID1 = ("" + getValueAt("Table2", intNo, "CostID1")).trim();
      //
      stringKEY = ("I".equals(stringInOut)) ? stringDepartNo : stringProjectID1;
      stringKEY = stringKEY + "%-%" + stringCostID + "%-%" + stringCostID1;
      //
      if (vectorKEY2.indexOf(stringKEY) == -1) vectorKEY2.add(stringKEY);
      //
      doubleMoney2 = exeUtil.doParseDouble(stringRealTotalMoney) + exeUtil.doParseDouble("" + hashtableMoney.get(stringKEY + "%-%Table2"));
      hashtableMoney.put(stringKEY + "%-%Table2", convert.FourToFive("" + doubleMoney2, 0));
    }
    for (int intNo = 0; intNo < jtable17.getRowCount(); intNo++) {
      System.out.println("===================check table17========================");
      stringInOut = ("" + getValueAt("Table17", intNo, "InOut")).trim();
      stringDepartNo = ("" + getValueAt("Table17", intNo, "DepartNo")).trim();
      stringProjectID1 = ("" + getValueAt("Table17", intNo, "ProjectID1")).trim();
      stringRealTotalMoney = ("" + getValueAt("Table17", intNo, "PurchaseMoney")).trim();
      stringCostID = ("" + getValueAt("Table17", intNo, "CostID")).trim();
      stringCostID1 = ("" + getValueAt("Table17", intNo, "CostID1")).trim();
      //
      stringKEY = ("I".equals(stringInOut)) ? stringDepartNo : stringProjectID1;
      stringKEY = stringKEY + "%-%" + stringCostID + "%-%" + stringCostID1;
      //
      if (vectorKEY2.indexOf(stringKEY) == -1) {
        return doReturnTable2ErrB(-1, "�O�Ϊ�� �P ���ʶ��خקO���u��� �~����Ƥ��@�P�A�Ь���T�ǡC1");
      }
      if (vectorKEY17.indexOf(stringKEY) == -1) vectorKEY17.add(stringKEY);
      //
      doubleMoney17 = exeUtil.doParseDouble(stringRealTotalMoney) + exeUtil.doParseDouble("" + hashtableMoney.get(stringKEY + "%-%Table17"));
      hashtableMoney.put(stringKEY + "%-%Table17", convert.FourToFive("" + doubleMoney17, 0));
    }
    if (vectorKEY2.size() != vectorKEY17.size()) {
      System.out.println("vectorKEY2:" + vectorKEY2.size() + ", vectorKEY17:" + vectorKEY17.size());
      return doReturnTable2ErrB(-1, "�O�Ϊ�� �P ���ʶ��خקO���u��� �~����Ƥ��@�P�A�Ь���T�ǡC2");
    }
    for (int intNo = 0; intNo < vectorKEY2.size(); intNo++) {
      stringKEY = "" + vectorKEY2.get(intNo);
      //
      doubleMoney2 = exeUtil.doParseDouble("" + hashtableMoney.get(stringKEY + "%-%Table2"));
      doubleMoney2 = exeUtil.doParseDouble(convert.FourToFive("" + doubleMoney2, 0));
      doubleMoney17 = exeUtil.doParseDouble("" + hashtableMoney.get(stringKEY + "%-%Table17"));
      doubleMoney17 = exeUtil.doParseDouble(convert.FourToFive("" + doubleMoney17, 0));
      //
      if (doubleMoney2 != doubleMoney17) {
        return doReturnTable2ErrB(-1, "�O�Ϊ�� �P ���ʶ��خקO���u��� ���B��Ƥ��@�P�A�Ь���T�ǡC3");
      }
    }
    return true;
  }

  public boolean isTable17CheckOK(FargloryUtil exeUtil, Doc2M010 exeFun) throws Throwable {
    JTable jtable17 = getTable("Table17");
    String stringTemp = "";
    String stringBarCodePur = "";
    Hashtable hashtableAnd = new Hashtable();
    Hashtable hashtableBarCodePur = new Hashtable();
    Hashtable hashtableTable17Data = new Hashtable();
    double doublePurchaseMoney = 0;
    double doubleRequestMoney = 0;
    double doubleTemp = 0;
    // ���ʶ��ؤ��קO���u ���B�@�P�ˮ�
    for (int intNo = 0; intNo < jtable17.getRowCount(); intNo++) {
      hashtableTable17Data = getTable17DataHashtable(intNo);
      // ���ʪ��B
      stringBarCodePur = getBarCodePur(hashtableTable17Data, exeUtil, exeFun, hashtableBarCodePur);
      hashtableTable17Data.put("BarCode", stringBarCodePur);
      System.out.println(intNo + "stringBarCodePur(" + stringBarCodePur + ")----------------------------------");
      doublePurchaseMoney = getPurchaseMoneyDoc3M0123(hashtableTable17Data, exeUtil, exeFun);
      // �дڪ��B-�д� Doc2M0172
      System.out.println(intNo + "stringBarCodePur(" + stringBarCodePur + ")getRequestMoneyDoc2M0172----------------------------------");
      doubleTemp = getRequestMoneyDoc2M0172(hashtableTable17Data, exeUtil, exeFun);
      doubleRequestMoney = doubleTemp;
      // �дڪ��B-�ɴ�-�s�� Doc6M0172
      // �дڪ��B-�ɴڨR�P-�ª� Doc6M0172
      doubleTemp = getRequestMoneyDoc6M0172(hashtableTable17Data, exeUtil, exeFun);
      doubleRequestMoney += doubleTemp;
      //
      doublePurchaseMoney = exeUtil.doParseDouble(convert.FourToFive("" + doublePurchaseMoney, 0));
      doubleRequestMoney = exeUtil.doParseDouble(convert.FourToFive("" + doubleRequestMoney, 0));
      if (doublePurchaseMoney < doubleRequestMoney) {
        String stringInOut = "" + hashtableTable17Data.get("InOut");
        if ("I".equals(stringInOut)) {
          stringTemp = "����(" + hashtableTable17Data.get("DepartNo") + ")";
        } else {
          stringTemp = "�קO(" + hashtableTable17Data.get("ProjectID1") + ")";
        }
        stringTemp += "�дڥN�X(" + hashtableTable17Data.get("CostID") + hashtableTable17Data.get("CostID1") + ")";
        return doReturnTable2ErrB(-1,
            "���ʶ��ؤ��קO���u��� " + stringTemp + "�w�ϥΪ��B(" + exeUtil.getFormatNum2("" + doubleRequestMoney) + ") �j�� �i�ιw����B(" + exeUtil.getFormatNum2("" + doublePurchaseMoney) + ")�C");
      }
    }
    return true;
  }

  public Hashtable getTable17DataHashtable(int intRowNo) throws Throwable {
    Hashtable hashtableTable17Data = new Hashtable();
    String stringFieldName = "";
    String stringFieldValue = "";
    String[] arrayFieldName = { "InOut", "DepartNo", "ProjectID", "ProjectID1", "CostID", "CostID1", "PurchaseMoney", "PurchaseNo", "RecordNo12" };
    for (int intNo = 0; intNo < arrayFieldName.length; intNo++) {
      stringFieldName = arrayFieldName[intNo].trim();
      stringFieldValue = ("" + getValueAt("Table17", intRowNo, stringFieldName)).trim();
      //
      System.out.println("stringFieldName(" + stringFieldName + ")stringFieldValue(" + stringFieldValue + ")-------------------------------");
      hashtableTable17Data.put(stringFieldName, stringFieldValue);
    }
    return hashtableTable17Data;
  }

  public String getBarCodePur(Hashtable hashtableTable17Data, FargloryUtil exeUtil, Doc2M010 exeFun, Hashtable hashtableBarCodePur) throws Throwable {
    String stringPurchaseNo = ("" + hashtableTable17Data.get("PurchaseNo")).trim();
    String stringBarCodePur = "" + hashtableBarCodePur.get(stringPurchaseNo);
    //
    if (!"null".equals(stringBarCodePur)) return stringBarCodePur;
    //
    String stringComNo = getValue("ComNo").trim();
    String stringKindNo = getValue("KindNo").trim();
    Hashtable hashtableAnd = new Hashtable();
    //
    if ("24".equals(stringKindNo)) stringKindNo = "17";
    if ("23".equals(stringKindNo)) stringKindNo = "15";
    //
    hashtableAnd.put("ComNo", stringComNo);
    hashtableAnd.put("KindNo", stringKindNo);
    hashtableAnd.put("DocNo", stringPurchaseNo);
    stringBarCodePur = exeFun.getNameUnionDoc("BarCode", "Doc3M011", "", hashtableAnd, exeUtil);
    hashtableBarCodePur.put(stringPurchaseNo, stringBarCodePur);
    return stringBarCodePur;
  }

  public double getPurchaseMoneyDoc3M0123(Hashtable hashtableTable17Data, FargloryUtil exeUtil, Doc2M010 exeFun) throws Throwable {
    String stringBarCodePur = "" + hashtableTable17Data.get("BarCode");
    String stringRecordNo = "" + hashtableTable17Data.get("RecordNo12");
    String stringInOut = "" + hashtableTable17Data.get("InOut");
    String stringDepartNo = "" + hashtableTable17Data.get("DepartNo");
    String stringProjectID = "" + hashtableTable17Data.get("ProjectID");
    String stringProjectID1 = "" + hashtableTable17Data.get("ProjectID1");
    String stringCostID = "" + hashtableTable17Data.get("CostID");
    String stringCostID1 = "" + hashtableTable17Data.get("CostID1");
    String stringSql = "";
    String[][] retDoc3M0123 = null;
    //
    stringSql = " SELECT  SUM(M123.PurchaseMoney - M123.NoUseRealMoney)" + " FROM  Doc3M012 M12,  Doc3M0123 M123 " + " WHERE  M12.BarCode    =  M123.BarCode "
        + " AND  M12.RecordNo  =  M123.RecordNo " + " AND  M12.BarCode  =  '" + stringBarCodePur + "' " + " AND  M12.RecordNo  =  " + stringRecordNo + " "
        + " AND  M12.CostID  =  '" + stringCostID + "' " + " AND  M12.CostID1  =  '" + stringCostID1 + "' " + " AND  M123.InOut  =  '" + stringInOut + "' "
        + " AND  M123.DepartNo  =  '" + stringDepartNo + "' " + " AND  M123.ProjectID  =  '" + stringProjectID + "' " + " AND  M123.ProjectID1  =  '" + stringProjectID1 + "' ";
    retDoc3M0123 = exeFun.getTableDataDoc(stringSql);
    return exeUtil.doParseDouble(retDoc3M0123[0][0]);
  }

  public double getRequestMoneyDoc2M0172(Hashtable hashtableTable17Data, FargloryUtil exeUtil, Doc2M010 exeFun) throws Throwable {
    String stringRecordNo = "" + hashtableTable17Data.get("RecordNo12");
    String stringInOut = "" + hashtableTable17Data.get("InOut");
    String stringDepartNo = "" + hashtableTable17Data.get("DepartNo");
    String stringProjectID = "" + hashtableTable17Data.get("ProjectID");
    String stringProjectID1 = "" + hashtableTable17Data.get("ProjectID1");
    String stringCostID = "" + hashtableTable17Data.get("CostID");
    String stringCostID1 = "" + hashtableTable17Data.get("CostID1");
    String stringPurchaseNo = ("" + hashtableTable17Data.get("PurchaseNo")).trim();
    String stringSql = "";
    String stringBarCode = getValue("BarCodeOld").trim();
    String stringComNo = getValue("ComNo").trim();
    String stringKindNo = getValue("KindNo").trim();
    String[][] retDoc2M0172 = null;
    //
    stringSql = " SELECT  SUM(M172.PurchaseMoney)" + " FROM  Doc2M010 M10,  Doc2M0172 M172 " + " WHERE  M10.BarCode    =  M172.BarCode " + " AND  M10.UNDERGO_WRITE  <>  'E' "
        + " AND  M10.BarCode  <>  '" + stringBarCode + "' " + " AND  M10.ComNo  =  '" + stringComNo + "' " + " AND  M10.KindNo  =  '" + stringKindNo + "' "
        + " AND  M172.PurchaseNo  =  '" + stringPurchaseNo + "' " + " AND  M172.RecordNo12  =   " + stringRecordNo + "  " + " AND  M172.InOut  =  '" + stringInOut + "' "
        + " AND  M172.DepartNo  =  '" + stringDepartNo + "' " + " AND  M172.ProjectID  =  '" + stringProjectID + "' " + " AND  M172.ProjectID1  =  '" + stringProjectID1 + "' "
        + " AND  M172.CostID  =  '" + stringCostID + "' " + " AND  M172.CostID1  =  '" + stringCostID1 + "' ";
    retDoc2M0172 = exeFun.getTableDataDoc(stringSql);
    return exeUtil.doParseDouble(retDoc2M0172[0][0]);
  }

  public double getRequestMoneyDoc6M0172(Hashtable hashtableTable17Data, FargloryUtil exeUtil, Doc2M010 exeFun) throws Throwable {
    String stringRecordNo = "" + hashtableTable17Data.get("RecordNo12");
    String stringInOut = "" + hashtableTable17Data.get("InOut");
    String stringDepartNo = "" + hashtableTable17Data.get("DepartNo");
    String stringProjectID = "" + hashtableTable17Data.get("ProjectID");
    String stringProjectID1 = "" + hashtableTable17Data.get("ProjectID1");
    String stringCostID = "" + hashtableTable17Data.get("CostID");
    String stringCostID1 = "" + hashtableTable17Data.get("CostID1");
    String stringPurchaseNo = ("" + hashtableTable17Data.get("PurchaseNo")).trim();
    String stringSql = "";
    String stringBarCode = getValue("BarCode").trim();
    String stringComNo = getValue("ComNo").trim();
    // String stringKindNo = getValue("KindNo").trim() ;
    String[][] retDoc6M0172 = null;
    //
    stringSql = " SELECT  SUM(M172.PurchaseMoney-ISNULL(NoUsePurchaseMoney,  0))" + " FROM  Doc6M010 M10,  Doc6M0172 M172 " + " WHERE  M10.BarCode    =  M172.BarCode "
        + " AND  M10.UNDERGO_WRITE  <>  'E' " + " AND  M10.PurchaseNoExist  =  'Y' " + " AND  M10.BarCode  <>  '" + stringBarCode + "' " + " AND  M10.ComNo  =  '" + stringComNo
        + "' " +
        // " AND M10.KindNo = '" +stringKindNo +"' " +
        " AND  M172.PurchaseNo  =  '" + stringPurchaseNo + "' " + " AND  M172.RecordNo12  =   " + stringRecordNo + "  " + " AND  M172.InOut  =  '" + stringInOut + "' "
        + " AND  M172.DepartNo  =  '" + stringDepartNo + "' " + " AND  M172.ProjectID  =  '" + stringProjectID + "' " + " AND  M172.ProjectID1  =  '" + stringProjectID1 + "' "
        + " AND  M172.CostID  =  '" + stringCostID + "' " + " AND  M172.CostID1  =  '" + stringCostID1 + "' ";
    retDoc6M0172 = exeFun.getTableDataDoc(stringSql);
    return exeUtil.doParseDouble(retDoc6M0172[0][0]);
  }

  // �ߨR�ǲ����
  public boolean isTable13CheckOK(int intTable2Panel, boolean booleanComNoType, Hashtable hashtbleFunctionType, FargloryUtil exeUtil, Doc2M010 exeFun) throws Throwable {
    JTable jtable = getTable("Table13");
    JTabbedPane jtabbedPane1 = getTabbedPane("Tab1");
    String stringBarCode = getValue("BarCode").trim();
    //
    if ("P20933".equals(stringBarCode)) return true;
    //
    if (jtable.getRowCount() == 0) {
      jtabbedPane1.setSelectedIndex(intTable2Panel);
      messagebox("�п�J [�ߨR�ǲ����] ��ơC");
      return false;
    }
    // �O�Ϊ��
    JTable jtable2 = getTable("Table2");
    if (jtable.getRowCount() != jtable2.getRowCount()) {
      jtabbedPane1.setSelectedIndex(intTable2Panel);
      messagebox("[�ߨR�ǲ����] �P [�O�Ϊ��] ��Ƥ��@�P�C");
      return false;
    }
    String stringTemp = "";
    String stringDepartNo = "";
    String stringAcctNo = "";
    String stringAcctNoSum = "";
    String stringInOut = "";
    String stringCDateAC = exeUtil.getDateConvert(getValue("CDate").trim());
    String stringCostID = ("" + getValueAt("Table2", 0, "CostID")).trim();
    String stringCostID1 = ("" + getValueAt("Table2", 0, "CostID1")).trim();
    String stringProjectID1 = "";
    String stringComNo = getValue("ComNo").trim();
    String stringFunctionTypeB = ""; // Y �K���� N �������� X �V�X
    String[][] retDoc2M020 = exeFun.getDoc2M020All(stringComNo, stringCostID, stringCostID1);
    Vector vectorDepartNo = new Vector();
    Vector vectorDoc2M0201ForB = null; // ���\�K����
    Hashtable hasthableAcctNo = new Hashtable();// getCostToAcctNoDoc7M055("", stringSqlAnd) ;
    //
    vectorDoc2M0201ForB = (Vector) hashtbleFunctionType.get("B");
    if (vectorDoc2M0201ForB == null) vectorDoc2M0201ForB = new Vector();
    for (int intNo = 0; intNo < jtable2.getRowCount(); intNo++) {
      stringTemp = ("" + getValueAt("Table2", intNo, "RealTotalMoney")).trim();
      stringDepartNo = ("" + getValueAt("Table2", intNo, "DepartNo")).trim();
      stringInOut = ("" + getValueAt("Table2", intNo, "InOut")).trim();
      stringProjectID1 = ("" + getValueAt("Table2", intNo, "ProjectID1")).trim();
      stringCostID = ("" + getValueAt("Table2", intNo, "CostID")).trim();
      stringCostID1 = ("" + getValueAt("Table2", intNo, "CostID1")).trim();
      //
      retDoc2M020 = exeFun.getDoc2M020All(stringComNo, stringCostID, stringCostID1);
      stringAcctNo = retDoc2M020[0][0].trim();
      if ("0010".equals(stringCostID + stringCostID1)) stringAcctNo = "114206";
      hasthableAcctNo.put(stringCostID + stringCostID1, stringAcctNo);
      //
      if (!"X".equals(stringFunctionTypeB)) {
        if (vectorDoc2M0201ForB.indexOf(stringCostID + stringCostID1) != -1) {
          if (!"Y".equals(stringFunctionTypeB)) {
            stringFunctionTypeB = "".equals(stringFunctionTypeB) ? "Y" : "X";
          }
        } else {
          if (!"N".equals(stringFunctionTypeB)) {
            stringFunctionTypeB = "".equals(stringFunctionTypeB) ? "N" : "X";
          }
        }
      }
      setValueAt("Table13", stringTemp, intNo, "Amt");
      //
      stringDepartNo = exeFun.getVoucherDepartNo(stringInOut, stringDepartNo, stringProjectID1, stringProjectID1, exeUtil);
      vectorDepartNo.add(stringDepartNo + "%-%" + intNo);
    }
    if ("X".equals(stringFunctionTypeB)) {
      messagebox("[�ߨR�y�{] ���ҧP�_ ���~�C");
      return false;
    }
    // �P [�o��] �@�P�ˮ�
    if (!isTable1Table13Same(exeUtil, exeFun)) {
      jtabbedPane1.setSelectedIndex(intTable2Panel + 1);
      return false;
    }
    // �P [�ӤH����] �@�P�ˮ�
    if (!isTable3Table13Same(exeUtil, exeFun)) {
      jtabbedPane1.setSelectedIndex(intTable2Panel + 2);
      return false;
    }
    // �P [�O��] ���B�ˮ�
    if (!isTable2Table13Same(vectorDepartNo, hasthableAcctNo, exeUtil, exeFun)) {
      jtabbedPane1.setSelectedIndex(intTable2Panel);
      return false;
    }
    return true;
  }

  // [�o��][�ߨR] �@�P�ˮ�
  public boolean isTable1Table13Same(FargloryUtil exeUtil, Doc2M010 exeFun) throws Throwable {
    JTable jtable1 = getTable("Table1");
    String stringAmt = "";
    double doubleAmt = 0;
    double doubleAmtCF = 0;
    //
    if (jtable1.getRowCount() == 0) return true;
    //
    for (int intNo = 0; intNo < jtable1.getRowCount(); intNo++) {
      stringAmt = ("" + getValueAt("Table1", intNo, "InvoiceTotalMoney")).trim();
      //
      doubleAmt += exeUtil.doParseDouble(stringAmt);
    }
    JTable jtable13 = getTable("Table13");
    for (int intNo = 0; intNo < jtable13.getRowCount(); intNo++) {
      stringAmt = ("" + getValueAt("Table13", intNo, "Amt")).trim();
      doubleAmtCF += exeUtil.doParseDouble(stringAmt);
    }
    doubleAmt = exeUtil.doParseDouble(convert.FourToFive("" + doubleAmt, 0));
    doubleAmtCF = exeUtil.doParseDouble(convert.FourToFive("" + doubleAmtCF, 0));
    if (doubleAmt != doubleAmtCF) {
      messagebox("[�o��]�X�p���B �j�� [�ߨR�ǲ�]�X�p���B�C");
      return false;
    }
    //
    return true;
  }

  // [�ӤH����][�ߨR] �@�P�ˮ�
  public boolean isTable3Table13Same(FargloryUtil exeUtil, Doc2M010 exeFun) throws Throwable {
    JTable jtable3 = getTable("Table3");
    String stringFactoryNo = "";
    String stringAmt = "";
    Vector vectorFactoryNo3 = new Vector();
    Hashtable hashtableAmt3 = new Hashtable();
    //
    if (jtable3.getRowCount() == 0) return true;
    for (int intNo = 0; intNo < jtable3.getRowCount(); intNo++) {
      stringFactoryNo = ("" + getValueAt("Table3", intNo, "FactoryNo")).trim();
      stringAmt = ("" + getValueAt("Table3", intNo, "ReceiptTotalMoney")).trim();
      stringAmt = convert.FourToFive(stringAmt, 0);
      //
      if (vectorFactoryNo3.indexOf(stringFactoryNo) == -1) vectorFactoryNo3.add(stringFactoryNo);
      //
      stringAmt = "" + (exeUtil.doParseDouble("" + hashtableAmt3.get(stringFactoryNo)) + exeUtil.doParseDouble(stringAmt));
      stringAmt = convert.FourToFive(stringAmt, 0);
      hashtableAmt3.put(stringFactoryNo, stringAmt);
    }
    JTable jtable13 = getTable("Table13");
    Vector vectorFactoryNo13 = new Vector();
    Hashtable hashtableAmt13 = new Hashtable();
    for (int intNo = 0; intNo < jtable13.getRowCount(); intNo++) {
      stringFactoryNo = ("" + getValueAt("Table13", intNo, "FactoryNo")).trim();
      stringAmt = ("" + getValueAt("Table13", intNo, "Amt")).trim();
      stringAmt = convert.FourToFive(stringAmt, 0);
      //
      if (vectorFactoryNo13.indexOf(stringFactoryNo) == -1) vectorFactoryNo13.add(stringFactoryNo);
      //
      stringAmt = "" + (exeUtil.doParseDouble("" + hashtableAmt13.get(stringFactoryNo)) + exeUtil.doParseDouble(stringAmt));
      stringAmt = convert.FourToFive(stringAmt, 0);
      hashtableAmt13.put(stringFactoryNo, stringAmt);
      //
      if (vectorFactoryNo3.indexOf(stringFactoryNo) == -1) {
        messagebox("[�ӤH����][�ߨR�ǲ�]���t�Ӥ��@�P");
        return false;
      }
    }
    if (vectorFactoryNo13.size() != vectorFactoryNo3.size()) {
      messagebox("[�ӤH����][�ߨR�ǲ�]���t�Ӥ��@�P");
      return false;
    }
    //
    String stringAmt13 = "";
    String stringCostID = ("" + getValueAt("Table2", 0, "CostID")).trim();
    String stringCostID1 = ("" + getValueAt("Table2", 0, "CostID1")).trim();
    for (int intNo = 0; intNo < vectorFactoryNo13.size(); intNo++) {
      stringFactoryNo = ("" + vectorFactoryNo13.get(intNo)).trim();
      stringAmt = ("" + hashtableAmt3.get(stringFactoryNo)).trim();
      stringAmt13 = ("" + hashtableAmt13.get(stringFactoryNo)).trim();
      //
      if (!stringAmt.equals(stringAmt13)) {
        if (exeUtil.doParseDouble(stringAmt) > exeUtil.doParseDouble(stringAmt13)) {
          messagebox("�t��" + exeFun.getFactoryName(stringFactoryNo).trim() + "(" + stringFactoryNo + ")��[�ӤH����]���B �j�� [�ߨR�ǲ�]���B�C");
          return false;
        }
      }
    }
    //
    return true;
  }

  // [�ߨR][�O��] �@�P�ˮ�
  public boolean isTable2Table13Same(Vector vectorDepartNo, Hashtable hasthableAcctNo, FargloryUtil exeUtil, Doc2M010 exeFun) throws Throwable {
    boolean booleanFalg = true;
    JTable jtable13 = getTable("Table13");
    String stringAmt = "";
    String stringVoucher = "";
    String stringVoucherYMD = "";
    String stringVoucherFlowNo = "";
    String stringVoucherSeqNo = "";
    String stringSqlAnd = "";
    String stringDepartNo = "";
    for (int intNo = 0; intNo < jtable13.getRowCount(); intNo++) {
      stringVoucherYMD = ("" + getValueAt("Table13", intNo, "VOUCHER_YMD")).trim();
      stringVoucherFlowNo = ("" + getValueAt("Table13", intNo, "VOUCHER_FLOW_NO")).trim();
      stringVoucherSeqNo = ("" + getValueAt("Table13", intNo, "VOUCHER_SEQ_NO")).trim();
      //
      if (!"".equals(stringSqlAnd)) stringSqlAnd += " OR ";
      stringSqlAnd += "(I.VOUCHER_YMD  =  " + stringVoucherYMD + "  AND  " + " I.VOUCHER_FLOW_NO  =  " + stringVoucherFlowNo + "  AND " + " I.VOUCHER_SEQ_NO  =  "
          + stringVoucherSeqNo + " )";
    }
    if ("".equals(stringSqlAnd)) return false;
    stringSqlAnd = " AND (" + stringSqlAnd + ")";
    // [�R�P���B] ���p�󵥩� [�b�C���B �i�R�P���B(�]�|)]
    String stringBarCode = getValue("BarCode").trim();
    String stringTemp = "";
    String stringLimit = "%-%";
    String stringAcctNoSum = "";
    String stringAcctNo = "";
    String stringCostID = "";
    String stringCostID1 = "";
    String[] arrayTemp = null;
    Hashtable hashtableDoc5M0224 = exeFun.getVouherAmtDoc5M0224(stringBarCode, getValue("ComNo").trim(), exeUtil);
    Hashtable hashtableCanWriteAmt = getCanWriteAmtForFED1013(stringSqlAnd, exeFun);
    double doubleMoney = 0;
    double doubleTotalMoney = 0;
    for (int intNo = 0; intNo < jtable13.getRowCount(); intNo++) {
      stringAmt = ("" + getValueAt("Table13", intNo, "Amt")).trim();
      stringVoucherYMD = ("" + getValueAt("Table13", intNo, "VOUCHER_YMD")).trim();
      stringVoucherFlowNo = ("" + getValueAt("Table13", intNo, "VOUCHER_FLOW_NO")).trim();
      stringVoucherSeqNo = ("" + getValueAt("Table13", intNo, "VOUCHER_SEQ_NO")).trim();
      stringCostID = ("" + getValueAt("Table2", intNo, "CostID")).trim();
      stringCostID1 = ("" + getValueAt("Table2", intNo, "CostID1")).trim();
      stringAcctNoSum = "" + hasthableAcctNo.get(stringCostID + stringCostID1);
      stringVoucher = stringVoucherYMD + "-" + convert.add0(stringVoucherFlowNo, "5") + "-" + convert.add0(stringVoucherSeqNo, "4");
      //
      doubleMoney = exeUtil.doParseDouble(stringAmt) + exeUtil.doParseDouble("" + hashtableDoc5M0224.get(stringVoucher));
      doubleMoney = exeUtil.doParseDouble(convert.FourToFive("" + doubleMoney, 0));
      //
      stringDepartNo = convert.StringToken("" + vectorDepartNo.get(intNo), stringLimit)[0].trim();
      stringTemp = "" + hashtableCanWriteAmt.get(stringVoucher);
      arrayTemp = convert.StringToken(stringTemp, stringLimit);
      if (arrayTemp.length != 3) return false;
      //
      if (!stringDepartNo.equals(arrayTemp[1].trim())) {
        messagebox("[�O�Ϊ��] �� " + (intNo + 1) + " �C�� [����](" + stringDepartNo + ") �P �ǲ�(" + arrayTemp[1].trim() + ")���@�P�C");
        return false;
      }
      stringAcctNo = arrayTemp[2].trim();
      if (exeUtil.doParseDouble(stringAmt) > 0 && stringAcctNoSum.indexOf(stringAcctNo) == -1) {
        messagebox("�O�Ϊ��� " + (intNo + 1) + " �C�� [�дڥN�X] �P [�ߨR�|�p���] ���@�P�C");
        return false;
      }
      doubleTotalMoney = exeUtil.doParseDouble(arrayTemp[0]);
      //
      if (Math.abs(doubleTotalMoney) < Math.abs(doubleMoney)) {
        messagebox("[�O�Ϊ��] �� " + (intNo + 1) + " �C�� [�дڪ��B]�X�p(" + exeUtil.getFormatNum2("" + doubleMoney) + ") �j�� [�i�R�P���B](" + exeUtil.getFormatNum2("" + doubleTotalMoney) + ")�C");
        return false;
      }
    }
    //
    return booleanFalg;
  }

  public Hashtable getCanWriteAmtForFED1013(String stringSqlAnd, Doc2M010 exeFun) throws Throwable {
    String stringComNo = getValue("ComNo").trim();
    String stringVoucher = "";
    String stringVoucherYMD = "";
    String stringVoucherFlowNo = "";
    String stringVoucherSeqNo = "";
    String stringAMt = "";
    String stringDepartNo = "";
    String stringAcctNo = "";
    String[][] retFED1013 = exeFun.getCanWriteAmtForFED1013(stringComNo, stringSqlAnd);
    Hashtable hashtableCanWriteAmt = new Hashtable();
    //
    for (int intNo = 0; intNo < retFED1013.length; intNo++) {
      stringVoucherYMD = retFED1013[intNo][0].trim();
      stringVoucherFlowNo = retFED1013[intNo][1].trim();
      stringVoucherSeqNo = retFED1013[intNo][2].trim();
      stringAMt = retFED1013[intNo][3].trim();
      stringDepartNo = retFED1013[intNo][4].trim();
      stringAcctNo = retFED1013[intNo][5].trim();
      stringVoucher = stringVoucherYMD + "-" + convert.add0(stringVoucherFlowNo, "5") + "-" + convert.add0(stringVoucherSeqNo, "4");
      hashtableCanWriteAmt.put(stringVoucher, stringAMt + "%-%" + stringDepartNo + "%-%" + stringAcctNo);
    }
    return hashtableCanWriteAmt;
  }

  public String getProjectIDFromDepartNo(FargloryUtil exeUtil, Doc2M010 exeFun) throws Throwable {
    String stringDepartNo = getValue("DepartNo").trim();
    String stringBarCode = getValue("BarCode").trim();
    String stringComNo = getValue("ComNo").trim();
    String stringPurchaseNoExist = "N"; // getValue("PurchaseNoExist").trim( ) ;
    return exeFun.getProjectIDFromDepartNo(stringDepartNo, stringPurchaseNoExist, stringBarCode, stringComNo, exeUtil);
  }

  // �h�ҽk �ɡA�^true
  // ��@���ʳ� OK
  // �h ���ʳ� �дڳ�@���u OK
  // �h ���ʳ� �дڦh ���u ���� ��@�O�Τ��u OK
  // �h �O�Τ��u �����������ϥΧ� �ҽk
  // ���� �����ϥΧ� ���ʳ欰��@�t�Ӽt�� OK
  // ���ʳ欰�h �t�Ӽt�� �ҽk
  public boolean isStatusSlur(Doc2M010 exeFun) throws Throwable {
    JTable jtable6 = getTable("Table6");
    String[] arrayTemp = null;
    Vector vectorTable6Data = new Vector();
    for (int intNo = 0; intNo < jtable6.getRowCount(); intNo++) {
      arrayTemp = new String[7];
      arrayTemp[0] = ("" + getValueAt("Table6", intNo, "PurchaseNo1")).trim();
      arrayTemp[1] = ("" + getValueAt("Table6", intNo, "PurchaseNo2")).trim();
      arrayTemp[2] = ("" + getValueAt("Table6", intNo, "PurchaseNo3")).trim();
      arrayTemp[3] = ("" + getValueAt("Table6", intNo, "PurchaseNo4")).trim();
      arrayTemp[4] = ("" + getValueAt("Table6", intNo, "PurchaseMoney")).trim();
      arrayTemp[5] = ("" + getValueAt("Table6", intNo, "FactoryNo")).trim();
      arrayTemp[6] = "";
      vectorTable6Data.add(arrayTemp);
    }
    return exeFun.isStatusSlur(true, getValue("ComNo").trim(), (String[][]) vectorTable6Data.toArray(new String[0][0]));
  }

  // �w�дڮקO���B��z
  public Hashtable getUsedProjectIDMoney2(Doc2M010 exeFun) throws Throwable {
    JTable jtable6 = getTable("Table6");
    String stringBarCode = getValue("BarCodeOld").trim();
    String stringComNo = getValue("ComNo").trim();
    String[] arrayTemp = null;
    Vector vectorTable6Data = new Vector();
    for (int intNo = 0; intNo < jtable6.getRowCount(); intNo++) {
      arrayTemp = new String[7];
      arrayTemp[0] = ("" + getValueAt("Table6", intNo, "PurchaseNo1")).trim();
      arrayTemp[1] = ("" + getValueAt("Table6", intNo, "PurchaseNo2")).trim();
      arrayTemp[2] = ("" + getValueAt("Table6", intNo, "PurchaseNo3")).trim();
      arrayTemp[3] = ("" + getValueAt("Table6", intNo, "PurchaseNo4")).trim();
      arrayTemp[4] = ("" + getValueAt("Table6", intNo, "PurchaseMoney")).trim();
      arrayTemp[5] = ("" + getValueAt("Table6", intNo, "FactoryNo")).trim();
      arrayTemp[6] = "";
      vectorTable6Data.add(arrayTemp);
    }
    return exeFun.getUsedProjectIDMoney2(true, stringComNo, stringBarCode, (String[][]) vectorTable6Data.toArray(new String[0][0]));
  }

  public boolean isTable2SameTable22CheckOK(String stringCostID, FargloryUtil exeUtil, Doc2M010 exeFun) throws Throwable {
    JTable jtable22 = getTable("Table22");
    String stringSSMediaID = "";
    String stringRecordNo = "";
    String stringWindowName = "80".equals(stringCostID) ? "�p�B�q���O�� ��� �q���N�X" : "�O�ι�ӳq���N�X";
    Vector vectorRecordNo = new Vector();
    double doubleRealTotalMoney2 = 0;
    double doubleRealTotalMoney22 = 0;
    //
    if (jtable22.getRowCount() <= 0) {
      messagebox(stringWindowName + "��� ���i���ťաC");
      getButton("ButtonTableElse").doClick();
      return false;
    }
    for (int intNo = 0; intNo < jtable22.getRowCount(); intNo++) {
      stringSSMediaID = ("" + getValueAt("Table22", intNo, "SSMediaID")).trim();
      stringRecordNo = ("" + getValueAt("Table22", intNo, "RecordNo")).trim();
      if ("".equals(stringSSMediaID) || "�@".equals(stringSSMediaID)) {
        messagebox(stringWindowName + "��� �� " + (intNo + 1) + " �C�� [�q���N�X]���i���ťաC");
        getButton("ButtonTableElse").doClick();
        return false;
      }
      if (vectorRecordNo.indexOf(stringRecordNo) != -1) continue;
      vectorRecordNo.add(stringRecordNo);
      // �O�Ϊ�� ���B�@�P�ˮ�
      doubleRealTotalMoney2 = getTable2MoneySum(stringRecordNo, exeUtil);
      doubleRealTotalMoney22 = getTable22MoneySum(stringRecordNo, exeUtil);
      // System.out.println("doubleRealTotalMoney2("+convert.FourToFive(""+doubleRealTotalMoney2,
      // 0)+")doubleRealTotalMoney22("+convert.FourToFive(""+doubleRealTotalMoney22,
      // 0)+")-----------------------------------") ;
      if (doubleRealTotalMoney2 != doubleRealTotalMoney22) {
        messagebox(stringWindowName + "��� �� " + (intNo + 1) + " �C���O�Ϊ��������B�X�p(" + exeUtil.getFormatNum2("" + doubleRealTotalMoney2) + ") ���@�P�C(���X�p�G"
            + exeUtil.getFormatNum2("" + doubleRealTotalMoney22) + ")�C");
        getButton("ButtonTableElse").doClick();
        return false;
      }
    }
    return true;
  }

  public double getTable2MoneySum(String stringRecordNoCF, FargloryUtil exeUtil) throws Throwable {
    JTable jtable2 = getTable("Table2");
    String stringRealTotalMoney = ("" + getValueAt("Table2", exeUtil.doParseInteger(stringRecordNoCF) - 1, "RealTotalMoney")).trim();
    return exeUtil.doParseDouble(stringRealTotalMoney);
  }

  public double getTable22MoneySum(String stringRecordNoCF, FargloryUtil exeUtil) throws Throwable {
    JTable jtable22 = getTable("Table22");
    String stringRecordNo = "";
    String stringRealTotalMoney = "";
    double doubleRealTotalMoney = 0;
    for (int intNo = 0; intNo < jtable22.getRowCount(); intNo++) {
      stringRecordNo = ("" + getValueAt("Table22", intNo, "RecordNo")).trim();
      stringRealTotalMoney = ("" + getValueAt("Table22", intNo, "RealTotalMoney")).trim();
      //
      if (!stringRecordNoCF.equals(stringRecordNo)) continue;
      //
      doubleRealTotalMoney += exeUtil.doParseDouble(stringRealTotalMoney);
    }
    return exeUtil.doParseDouble(convert.FourToFive("" + doubleRealTotalMoney, 0));
  }

  // �ӤH��ú���
  public boolean isTable3CheckOK(Hashtable hashtbleFunctionType, FargloryUtil exeUtil, Doc2M010 exeFun) throws Throwable {
    JTable jtable3 = getTable("Table3");
    JTabbedPane jtabbedPane1 = getTabbedPane("Tab1");
    int intMaxRow = jtable3.getRowCount();
    int intTable3Panel = 4;
    String stringFlow = getFunctionName();
    String stringFactoryNo = "";
    String stringFactoryName = "";
    String stringFactoryNoOld = "";
    String stringDateRoc = "";
    String stringReceiptDate = "";
    String stringReceiptKind = "";
    String stringReceiptMoney = "";
    String stringReceiptTaxType = "";
    String stringReceiptTotalMoney = "";
    String stringOriEmployeeNo = getValue("OriEmployeeNo").trim();
    String stringComNo = getValue("ComNo").trim();
    String stringCDateRoc = getValue("CDate").trim();
    String stringCDateAC = exeUtil.getDateConvert(stringCDateRoc);
    String stringBarCode = getValue("BarCode").trim();
    String stringToday = getToday("yymmdd");
    String stringCostID = ("" + getValueAt("Table2", 0, "CostID")).trim();
    String stringMessageFactor = "";
    String stringAcctNo = "";
    String[][] retFED1005 = new String[0][0];
    double doubleReceiptTax = 0;
    double doubleReceiptMoneyTax = 0;
    double doubleTaxRate = 0;
    boolean booleanPurchaseExist = "Y".equals(getValue("PurchaseNoExist").trim()) ? true : false; // false��ܥi�H�L���ʳ� ;
    Vector vectorCostIDBonus = new Vector();
    Vector vectorFactoryNo = new Vector();
    //
    if (intMaxRow == 0) return true;
    //
    // vectorCostIDBonus.add("10") ;
    vectorCostIDBonus.add("13");
    // �I�ڱ���@�P�ˮ�
    Vector vectorPayCondition = new Vector();
    String stringPayCondition = "";
    for (int intNo = 0; intNo < 2; intNo++) {
      stringPayCondition = getValue("PayCondition" + (intNo + 1));
      //
      if ("999".equals(stringPayCondition)) continue;
      //
      vectorPayCondition.add(stringPayCondition);
    }
    boolean booleanCheck = false;
    String stringCostIDL = "";
    String stringCostID1L = "";
    boolean booleanExist = false;
    boolean booleanNoUseZ0001 = false;
    boolean boolean130 = false;
    boolean booleanFunctionK = false;
    Vector vectorFunctionI = null; // �ӤH���� ���\�h�t��
    Vector vectorFunctionK = null; // �ӤH���� ��ܹ����O�γ������
    Vector vectorFunctionL = null; // �ӤH���� �����\��J Z0001
    //
    vectorFunctionI = (Vector) hashtbleFunctionType.get("I");
    if (vectorFunctionI == null) vectorFunctionI = new Vector();
    vectorFunctionK = (Vector) hashtbleFunctionType.get("K");
    if (vectorFunctionK == null) vectorFunctionK = new Vector();
    vectorFunctionL = (Vector) hashtbleFunctionType.get("L");
    if (vectorFunctionL == null) vectorFunctionL = new Vector();
    //
    for (int intNoL = 0; intNoL < getTable("Table2").getRowCount(); intNoL++) {
      stringCostIDL = ("" + getValueAt("Table2", intNoL, "CostID")).trim();
      stringCostID1L = ("" + getValueAt("Table2", intNoL, "CostID1")).trim();
      //
      if (vectorFunctionI.indexOf(stringCostIDL + stringCostID1L) != -1) booleanExist = true;
      if (vectorFunctionK.indexOf(stringCostIDL + stringCostID1L) != -1) booleanFunctionK = true;
      if (vectorFunctionL.indexOf(stringCostIDL + stringCostID1L) != -1) booleanNoUseZ0001 = true;
      // 2013-05-22 �Ȥ��\ ������ �� 2013/09/01 �e�A�ӽ� 32 �O�Τ��\�h�t��
      if ("321,".indexOf(stringCostIDL + stringCostID1L) != -1 && "B4229".equals(stringOriEmployeeNo) && "2013/09/01".compareTo(stringCDateAC) > 0) {
        booleanExist = true;
      }
      if ("130,012,".indexOf(stringCostIDL + stringCostID1L) != -1) {
        boolean130 = true;
      }
    }
    double doubleTemp = 0;
    String stringDepartNo = "";
    String stringSupplementMoney = "";
    Vector vectorDepartNo = new Vector();
    Vector vectorFactoryNoFirm = new Vector();
    Hashtable hashtableAmt = new Hashtable();
    double doubleSupplementMoney = 0;
    double doubleSupplementMoneyCF = 0;
    boolean booleanSupplementMoney0 = false;
    boolean booleanSupplementFlag = true;
    // ��O�Ϊ��Ȥ��u�@�C�ɡA�۰ʹw�] [�����O�γ���]
    String stringInOutL = ("" + getValueAt("Table2", 0, "InOut")).trim();
    String stringDepartNoL = ("" + getValueAt("Table2", 0, "DepartNo")).trim();
    String stringProjectIDL = ("" + getValueAt("Table2", 0, "ProjectID")).trim();
    String stringProjectID1L = ("" + getValueAt("Table2", 0, "ProjectID1")).trim();
    String stringDepartNo12 = exeFun.getVoucherDepartNo(stringInOutL, stringDepartNoL, stringProjectIDL, stringProjectID1L, exeUtil);
    String stringStopUseMessage = "";
    Hashtable hashtableData = new Hashtable();
    Hashtable hashtableCond = new Hashtable();

    if (getTable("Table2").getRowCount() > 1) stringDepartNo12 = "";
    if (!booleanFunctionK) stringDepartNo12 = "";
    //
    for (int intRowNo = 0; intRowNo < intMaxRow; intRowNo++) {
      stringFactoryNo = ("" + getValueAt("Table3", intRowNo, "FactoryNo")).trim();
      stringReceiptDate = ("" + getValueAt("Table3", intRowNo, "ReceiptDate")).trim();
      stringReceiptKind = ("" + getValueAt("Table3", intRowNo, "ReceiptKind")).trim();
      doubleReceiptTax = exeUtil.doParseDouble("" + getValueAt("Table3", intRowNo, "ReceiptTax"));
      stringReceiptMoney = ("" + getValueAt("Table3", intRowNo, "ReceiptMoney")).trim();
      stringReceiptTotalMoney = ("" + getValueAt("Table3", intRowNo, "ReceiptTotalMoney")).trim();
      stringReceiptTaxType = ("" + getValueAt("Table3", intRowNo, "ReceiptTaxType")).trim();
      stringAcctNo = ("" + getValueAt("Table3", intRowNo, "ACCT_NO")).trim();
      stringPayCondition = ("" + getValueAt("Table3", intRowNo, "PayCondition1")).trim();
      stringDepartNo = ("" + getValueAt("Table3", intRowNo, "DepartNo")).trim();
      stringSupplementMoney = ("" + getValueAt("Table3", intRowNo, "SupplementMoney")).trim();
      doubleTaxRate = (!"A".equals(stringReceiptKind)) ? 0 : exeUtil.doParseDouble(stringReceiptTaxType) / 100;
      //
      if (",A,B,C,".indexOf("," + stringReceiptKind + ",") != -1 && ",Z0001,Z8000,G8888,".indexOf("," + stringFactoryNo + ",") == -1 && stringFactoryNo.length() != 8
          && stringFactoryNo.length() != 10) {
        String[][] retDoc3M015L = exeFun.getDoc3M015(stringFactoryNo);
        if (retDoc3M015L.length == 0 || "1".equals(retDoc3M015L[0][9].trim())) {
          messagebox("�ӤH��ú���� " + (intRowNo + 1) + " �C �榡�� ���q����ú���ڡB���q�����ڡB�K�q���ɡA�t�ӶȤ��\�ϥ� �Τ@�s���B�ӤH�����ҡBZ0001���q���u�BZ8000���έ��u�C");
          jtabbedPane1.setSelectedIndex(intTable3Panel);
          jtable3.setRowSelectionInterval(intRowNo, intRowNo);
          return false;
        }
      }
      String FGLIFE_NO = "84703052";
      if (!stringFactoryNo.equals(FGLIFE_NO)) {
        if (vectorFactoryNoFirm.indexOf(stringFactoryNo) == -1 && !exeFun.isFactoryNoOK(stringComNo, stringFactoryNo)) {
          jtabbedPane1.setSelectedIndex(intTable3Panel);
          jtable3.setRowSelectionInterval(intRowNo, intRowNo);
          return false;
        }
      }
      vectorFactoryNoFirm.add(stringFactoryNo);
      //
      if (booleanNoUseZ0001 && "Z0001".equals(stringFactoryNo)) {
        messagebox("�ӤH��ú���� " + (intRowNo + 1) + " �C�� �S��дڥN�X �����\�ϥ� Z0001 �t�ӡC");
        jtabbedPane1.setSelectedIndex(intTable3Panel);
        jtable3.setRowSelectionInterval(intRowNo, intRowNo);
        return false;
      }
      if (stringFlow.indexOf("ñ��") != -1) {
        // �I�ڱ����ˮ�
        if ("".equals(stringPayCondition) || "999".equals(stringPayCondition)) {
          if ("999".equals(getValue("PayCondition2"))) {
            stringPayCondition = getValue("PayCondition1").trim();
            setValueAt("Table3", stringPayCondition, intRowNo, "PayCondition1");
          }
        }
        if (vectorPayCondition.indexOf(stringPayCondition) == -1) {
          String stringTemp = "Y".equals(getValue("PurchaseNoExist")) ? "���ʳ�" : "�e��";
          messagebox("�ӤH��ú���� " + (intRowNo + 1) + " �C�� [�I�ڱ���] �P" + stringTemp + "���@�P�C");
          jtabbedPane1.setSelectedIndex(intTable3Panel);
          jtable3.setRowSelectionInterval(intRowNo, intRowNo);
          return false;
        }
        if (booleanPurchaseExist) {
          if (intRowNo == 0) {
            // �Ĥ@���ϥι�H�ήɶ��b�@�Ӥ뤧�� 2016/11/14 ����
            // 0 LAST_YMD 1 LAST_USER
            /*
             * String stringLastYmd = "" ; retFED1005 = exeFun.getFED1005(stringFactoryNo) ;
             * boolean booleanFactoryNo = true ; // if(retFED1005.length > 0) {
             * stringLastYmd = exeFun.getFED1005LastYMD(retFED1005[0][0].trim(),
             * exeUtil).replaceAll("/","") ; } stringToday =
             * exeUtil.getDateConvertFullRoc(stringToday) ; // booleanFactoryNo =
             * !"".equals(stringLastYmd) && !"".equals(stringFactoryNo) &&
             * Math.abs(datetime.subDays1(stringToday, convert.replace(stringLastYmd, "/",
             * ""))) < 90 && exeFun.isFirstTimeUseFactoryNo(stringBarCode, stringComNo,
             * stringFactoryNo) ; if(booleanFactoryNo) { stringMessageFactor += "�ӤH��ú���� " +
             * (intRowNo+1) + " �C���Τ@�s�� " + stringFactoryNo + " �Ĥ@���ϥΡC\n" ; }
             */
          }
        } else {
          if (vectorFactoryNo.indexOf(stringFactoryNo) == -1) {
            // �Ĥ@���ϥι�H�ήɶ��b�@�Ӥ뤧�� 2016/11/14 ����
            // 0 LAST_YMD 1 LAST_USER
            /*
             * String stringLastYmd = "" ; retFED1005 = exeFun.getFED1005(stringFactoryNo) ;
             * boolean booleanFactoryNo = true ; // if(retFED1005.length > 0) {
             * stringLastYmd = exeFun.getFED1005LastYMD(retFED1005[0][0].trim(),
             * exeUtil).replaceAll("/","") ; } stringToday =
             * exeUtil.getDateConvertFullRoc(stringToday) ; // booleanFactoryNo =
             * !"".equals(stringLastYmd) && !"".equals(stringFactoryNo) &&
             * Math.abs(datetime.subDays1(stringToday, convert.replace(stringLastYmd, "/",
             * ""))) < 90 && exeFun.isFirstTimeUseFactoryNo(stringBarCode, stringComNo,
             * stringFactoryNo) ; if(booleanFactoryNo) { stringMessageFactor += "�ӤH��ú���� " +
             * (intRowNo+1) + " �C���Τ@�s�� " + stringFactoryNo + " �Ĥ@���ϥΡC\n" ; }
             */
            vectorFactoryNo.add(stringFactoryNo);
          }
        }
      }
      // ���B
      if (exeUtil.doParseDouble(stringReceiptTotalMoney) == 0 && exeUtil.doParseDouble(stringReceiptMoney) == 0) {
        messagebox("�ӤH��ú���� " + (intRowNo + 1) + " �C�� [��ú���B]���i���s�ΪťաC");
        jtabbedPane1.setSelectedIndex(intTable3Panel);
        jtable3.setRowSelectionInterval(intRowNo, intRowNo);
        return false;
      } else if (exeUtil.doParseDouble(stringReceiptTotalMoney) > 0 && exeUtil.doParseDouble(stringReceiptMoney) > 0) {
        // �����B�z
      } else if (exeUtil.doParseDouble(stringReceiptMoney) > 0) {
        if (doubleReceiptTax == 0) {
          stringReceiptTotalMoney = "" + (exeUtil.doParseDouble(stringReceiptMoney) / (1 - doubleTaxRate));
        } else {
          stringReceiptTotalMoney = "" + (exeUtil.doParseDouble(stringReceiptMoney) + doubleReceiptTax);
        }
        stringReceiptTotalMoney = convert.FourToFive("" + stringReceiptTotalMoney, 0);
        setValueAt("Table3", stringReceiptTotalMoney, intRowNo, "ReceiptTotalMoney");
      } else {
        if (doubleReceiptTax == 0) {
          stringReceiptMoney = "" + (exeUtil.doParseDouble(stringReceiptTotalMoney) * (1 - doubleTaxRate));
        } else {
          stringReceiptMoney = "" + (exeUtil.doParseDouble(stringReceiptTotalMoney) - doubleReceiptTax);
        }
        stringReceiptMoney = convert.FourToFive("" + stringReceiptMoney, 0);
        setValueAt("Table3", stringReceiptMoney, intRowNo, "ReceiptMoney");
      }
      if (doubleReceiptTax == 0) {
        doubleReceiptTax = exeUtil.doParseDouble(stringReceiptTotalMoney) - exeUtil.doParseDouble(stringReceiptMoney);
        doubleReceiptTax = exeUtil.doParseDouble(convert.FourToFive("" + doubleReceiptTax, 0));
        setValueAt("Table3", "" + doubleReceiptTax, intRowNo, "ReceiptTax");
      }
      // �ұo���B�שұo�b�B+��ú�|�B
      doubleReceiptTax = exeUtil.doParseDouble(convert.FourToFive("" + doubleReceiptTax, 0));
      stringReceiptMoney = convert.FourToFive(stringReceiptMoney, 0);
      stringReceiptTotalMoney = convert.FourToFive(stringReceiptTotalMoney, 0);
      if (exeUtil.doParseDouble(stringReceiptTotalMoney) != (exeUtil.doParseDouble(stringReceiptMoney) + doubleReceiptTax)) {
        messagebox("�ӤH��ú���� " + (intRowNo + 1) + " �C�� �ұo���B ������ �ұo�b�B+��ú�|�B�C");
        jtabbedPane1.setSelectedIndex(intTable3Panel);
        jtable3.setRowSelectionInterval(intRowNo, intRowNo);
        return false;
      }
      doubleReceiptMoneyTax = exeUtil.doParseDouble(stringReceiptTotalMoney) * doubleTaxRate;
      // �ɥR�O�O
      if (!isSupplementMoneyOK(intRowNo, hashtableData, exeUtil, exeFun)) {
        jtabbedPane1.setSelectedIndex(intTable3Panel);
        jtable3.setRowSelectionInterval(intRowNo, intRowNo);
        return false;
      }
      //
      if (booleanPurchaseExist) {
        setValueAt("Table3", "", intRowNo, "DepartNo");
      } else if (!booleanFunctionK) {
        setValueAt("Table3", "", intRowNo, "DepartNo");
      } else {
        if ("".equals(stringDepartNo)) {
          stringDepartNo = stringDepartNo12;
          if (!"".equals(stringDepartNo12)) {
            setValueAt("Table3", stringDepartNo12, intRowNo, "DepartNo");
          } else {
            messagebox("�ӤH��ú���� " + (intRowNo + 1) + " �C�� �S��дڥN�X �������[�����O�γ���]�C");
            jtabbedPane1.setSelectedIndex(intTable3Panel);
            jtable3.setRowSelectionInterval(intRowNo, intRowNo);
            return false;
          }
        }
        if (vectorDepartNo.indexOf(stringDepartNo) == -1) vectorDepartNo.add(stringDepartNo);
        doubleTemp = exeUtil.doParseDouble("" + hashtableAmt.get(stringDepartNo));
        doubleTemp += exeUtil.doParseDouble(stringReceiptTotalMoney);
        //
        hashtableAmt.put(stringDepartNo, "" + doubleTemp);
      }
      // �Τ@�s��
      if (intRowNo == 0) stringFactoryNoOld = stringFactoryNo;
      if ("".equals(stringFactoryNo)) {
        messagebox("�ӤH��ú���� " + (intRowNo + 1) + " �C�� [�Τ@�s��] ���i���ťաC");
        jtabbedPane1.setSelectedIndex(intTable3Panel);
        jtable3.setRowSelectionInterval(intRowNo, intRowNo);
        return false;
      }
      if (!booleanPurchaseExist && vectorCostIDBonus.indexOf(stringCostID) != -1 && !"Z0001".equals(stringFactoryNo)) {
        if (boolean130 && "Z8000".equals(stringFactoryNo)) {

        } else {
          messagebox("�ӤH��ú���� " + (intRowNo + 1) + " �C����O�ά������ɡA[�Τ@�s��] �u�ର Z0001�C");
          jtabbedPane1.setSelectedIndex(intTable3Panel);
          jtable3.setRowSelectionInterval(intRowNo, intRowNo);
          return false;
        }
      }
      if (stringFactoryNo.length() == 8 && !check.isCoId(stringFactoryNo)) {
        String[][] retDoc3M015 = exeFun.getDoc3M015(stringFactoryNo);
        if (retDoc3M015.length == 0 || "1".equals(retDoc3M015[0][9].trim())) {
          messagebox("�ӤH��ú���� " + (intRowNo + 1) + " �C�� [�Τ@�s��] �榡���~�C");
          return false;
        }
      }
      if (stringFactoryNo.length() == 10 && !check.isID(stringFactoryNo)) {
        String[][] retDoc3M015 = exeFun.getDoc3M015(stringFactoryNo);
        if (retDoc3M015.length == 0 || "1".equals(retDoc3M015[0][9].trim())) {
          messagebox("�ӤH��ú���� " + (intRowNo + 1) + " �C�� [�Τ@�s��] �榡���~�C");
          return false;
        }
      }
      if (!stringFactoryNo.equals(stringFactoryNoOld)) {
        if (!booleanCheck) {
          booleanCheck = true;
          boolean booleanErrL = false;
          if (booleanPurchaseExist) {
            booleanErrL = true;
          } else {
            if (!booleanExist) {
              booleanErrL = true;
            }
          }
          if (booleanErrL) {
            messagebox("�ӤH��ú���� " + (intRowNo + 1) + " �C�� �Ȥ��\�@�a [�Τ@�s��]�C");
            jtabbedPane1.setSelectedIndex(intTable3Panel);
            jtable3.setRowSelectionInterval(intRowNo, intRowNo);
            return false;
          }
        }
      }
      /*
       * retFED1005 = exeFun.getFED1005(stringFactoryNo) ; if(retFED1005.length == 0)
       * { messagebox("�ӤH��ú���� " + (intRowNo+1) + " �C�� ��Ʈw���L�� [�Τ@�s��]�C") ;
       * jtabbedPane1.setSelectedIndex(intTable3Panel) ;
       * jtable3.setRowSelectionInterval(intRowNo, intRowNo) ; return false ; }
       */
      // ���v
      hashtableCond.put("OBJECT_CD", stringFactoryNo);
      hashtableCond.put("CHECK_DATE", getValue("CDate").trim());
      hashtableCond.put("PurchaseNo_Exist", getValue("PurchaseNoExist").trim());
      hashtableCond.put("DocNoType", getValue("DocNoType").trim());
      hashtableCond.put("SOURCE", "C");
      hashtableCond.put("FieldName", "�ӤH��ú���� " + (intRowNo + 1) + " �C�� [�Τ@�s��] ");
      stringStopUseMessage = exeFun.getStopUseObjectCDMessage(hashtableCond, exeUtil);
      if (!"TRUE".equals(stringStopUseMessage)) {
        jtabbedPane1.setSelectedIndex(intTable3Panel);
        jtable3.setRowSelectionInterval(intRowNo, intRowNo);
        messagebox(stringStopUseMessage);
        return false;
      }
      /*
       * if(check.isID(stringFactoryNo) && "".equals(retFED1005[0][9].trim())) {
       * messagebox("�ӤH��ú���� " + (intRowNo+1) +
       * " �C�� �t�Ӹ�Ƥ� [�n�O�a�}]���ťաA���t�Ӥ����\�ϥΡA�и�[�n�O�a�}]��A�A�ϥΡC") ;
       * jtabbedPane1.setSelectedIndex(intTable3Panel) ;
       * jtable3.setRowSelectionInterval(intRowNo, intRowNo) ; return false ; }
       */
      // �榡
      // �ұo�b�B
      // ��ú���B
      // �ұo�`�B
      if ("A".equals(stringReceiptKind)) {
        if (",Z0001,Z8000,".indexOf("," + stringFactoryNo + ",") == -1) {
          if (doubleReceiptTax < (doubleReceiptMoneyTax - 10) || doubleReceiptTax > (doubleReceiptMoneyTax + 10)) {
            messagebox("�ӤH��ú���� " + (intRowNo + 1) + " �C�� [��ú�|�B] ���� [�ұo�b�B] ���H [�|�v] ���t 10 ���d�򤺡C");
            jtabbedPane1.setSelectedIndex(intTable3Panel);
            jtable3.setRowSelectionInterval(intRowNo, intRowNo);
            setValueAt("Table3", "" + doubleReceiptMoneyTax, intRowNo, "ReceiptTax");
            return false;
          }
        }
      } else {
        if (doubleReceiptTax > 0) {
          messagebox("�ӤH��ú���� " + (intRowNo + 1) + " �C�� [���ڮ榡] �� [���q����ú����] �ɡA[��ú���B] ���� 0�C");
          jtabbedPane1.setSelectedIndex(intTable3Panel);
          jtable3.setRowSelectionInterval(intRowNo, intRowNo);
          setValueAt("Table3", "0", intRowNo, "ReceiptTax");
          return false;
        }
      }
      // ����
      if ("".equals(stringAcctNo) && stringFlow.indexOf("ñ��") != -1) {
        messagebox("�ӤH��ú���� " + (intRowNo + 1) + " �C�� [����] ���o���ťաC");
        jtabbedPane1.setSelectedIndex(intTable3Panel);
        jtable3.setRowSelectionInterval(intRowNo, intRowNo);
        return false;
      }
    }
    String stringMessage = "" + hashtableData.get("MESSAGE");
    if ("null".equals(stringMessage)) stringMessage = "";
    if (!"".equals(stringMessage)) {
      messagebox(stringMessage);
    }
    if (!"".equals(stringMessageFactor)) {
      messagebox(stringMessageFactor);
    }
    if (!booleanPurchaseExist && booleanFunctionK && !isTable3Table2Same(vectorDepartNo, hashtableAmt, exeUtil, exeFun)) {
      jtabbedPane1.setSelectedIndex(intTable3Panel);
      return false;
    }
    if (!booleanPurchaseExist && "23416579".equals(stringFactoryNo) && stringFlow.indexOf("ñ��") == -1) {
      String stringPayCondition1 = getValue("PayCondition1").trim();
      String stringPayCondition2 = getValue("PayCondition2").trim();
      if (!"60".equals(stringPayCondition1) || !"999".equals(stringPayCondition2)) {
        messagebox("�t�Ӭ� 23416579(�p���ֻ�)�ɡA�I�ڱ��󶷬� 60 �ѡC");
        setValue("PayCondition1", "60");
        setValue("PayCondition2", "999");
      }
    }
    return true;
  }

  public boolean isSupplementMoneyOK(int intRow, Hashtable hashtableData, FargloryUtil exeUtil, Doc2M010 exeFun) throws Throwable {
    String stringField = "";
    String stringValue = "";
    String stringToday = datetime.getToday("YYYY/mm/dd");
    String stringEmployeeNo = getValue("EmployeeNo").trim();
    String[] arrayField = { "ReceiptTotalMoney", "ReceiptMoney", "ReceiptTax", "ReceiptTaxType", "ReceiptKind", "FactoryNo", "ACCT_NO", "SupplementMoney", "PayCondition1" };
    //
    hashtableData.put("EmployeeNo", stringEmployeeNo);
    hashtableData.put("TODAY", stringToday);
    hashtableData.put("SpecUserID", "Y");
    hashtableData.put("TYPE", "CHECK");
    for (int intNo = 0; intNo < arrayField.length; intNo++) {
      stringField = arrayField[intNo];
      stringValue = ("" + getValueAt("Table3", intRow, stringField)).trim();
      //
      hashtableData.put(stringField, stringValue);
    }
    hashtableData.put("SpecCostID", "N");
    //
    stringValue = getTableData("Table21").length > 0 ? "Y" : "N";
    hashtableData.put("Table21Exist", stringValue);
    //
    boolean booleanFlag = exeFun.isSupplementMoneyOK(hashtableData, exeUtil);
    double doubleSupplementMoney = exeUtil.doParseDouble("" + hashtableData.get("SupplementMoney"));
    String stringMessage = "" + hashtableData.get("MESSAGE");
    //
    if ("null".equals(stringMessage)) stringMessage = "";
    if (!booleanFlag && !"".equals(stringMessage)) {
      messagebox("�ӤH��ú���� " + (intRow + 1) + " �C��" + stringMessage);
    }
    //
    setValueAt("Table3", convert.FourToFive("" + doubleSupplementMoney, 0), intRow, "SupplementMoney");
    return booleanFlag;
  }

  public boolean isSpecUserID(FargloryUtil exeUtil, Doc2M010 exeFun) throws Throwable {
    String stringEmployeeNo = getValue("EmployeeNo").trim();
    String stringSqlAnd = " AND  EmployeeNo  =  '" + stringEmployeeNo + "' ";
    return exeFun.getDoc3M011EmployeeNo("12", stringSqlAnd).length > 0;
  }

  public boolean isTable3Table2Same(Vector vectorDepartNo, Hashtable hashtableAmt, FargloryUtil exeUtil, Doc2M010 exeFun) throws Throwable {
    String stringInOut = "";
    String stringVourcherDepartNo = "";
    String stringDepartNo = "";
    String stringProjectID1 = "";
    String stringProjectID = "";
    String stringRealTotalMoney = "";
    double dobuleAmt = 0;
    for (int intNo = 0; intNo < getTable("Table2").getRowCount(); intNo++) {
      stringInOut = ("" + getValueAt("Table2", intNo, "InOut")).trim();
      stringDepartNo = ("" + getValueAt("Table2", intNo, "DepartNo")).trim();
      stringProjectID = ("" + getValueAt("Table2", intNo, "ProjectID")).trim();
      stringProjectID1 = ("" + getValueAt("Table2", intNo, "ProjectID1")).trim();
      stringRealTotalMoney = ("" + getValueAt("Table2", intNo, "RealTotalMoney")).trim();
      //
      stringVourcherDepartNo = exeFun.getVoucherDepartNo(stringInOut, stringDepartNo, stringProjectID, stringProjectID1, exeUtil);
      // �����@�P�ˮ�
      if (vectorDepartNo.indexOf(stringVourcherDepartNo) == -1) {
        messagebox("[�O�Ϊ��]�� " + (intNo + 1) + " �C����� [��ú���] �å�����");
        return false;
      }
      // �������B�@�P�ˮ�
      dobuleAmt = exeUtil.doParseDouble("" + hashtableAmt.get(stringVourcherDepartNo));
      if (dobuleAmt != exeUtil.doParseDouble(stringRealTotalMoney)) {
        messagebox("[�O�Ϊ��]�� " + (intNo + 1) + " �C�����B�� " + exeUtil.getFormatNum2(stringRealTotalMoney) + "���P[��ú���]�X�p�� " + exeUtil.getFormatNum2("" + dobuleAmt) + " ���A���B�ä��@�P�C");
        return false;
      }
    }
    return true;
  }

  // �������B
  public boolean isTable4CheckOK(double doubleTaxRate, Hashtable hashtable2Key, FargloryUtil exeUtil, Doc2M010 exeFun) throws Throwable {
    JTable jtable4 = getTable("Table4");
    JTabbedPane jtabbedPane1 = getTabbedPane("Tab1");
    int intMaxRow = jtable4.getRowCount();
    int intTable4Panel = 5;
    String stringInOut = "";
    String stringDepart = "";
    String stringCostID = "";
    String stringCostID1 = "";
    String stringProjectID = "";
    String stringProjectID1 = "";
    String stringKey = "";
    String stringRealMoneyT = "";
    String stringDiscountMoney = "";
    String stringDiscountNoTaxMoney = "";
    double doubleDiscountMoney = 0;
    double doubleDiscountMoneySum = 0;
    double doubleInvoiceTotalMoneySum = exeUtil.doParseDouble(getValue("InvoiceTotalMoneySum").trim());
    double doubleRealMoneySum = exeUtil.doParseDouble(getValue("RealMoneySum").trim());
    double doubleDiscountNoTaxMoney = 0;
    double doubleRealTaxR = 0;
    double doubleRealTaxS = 0;
    double doubleRealTaxSum = 0;
    Vector vectorKey = new Vector();
    if (intMaxRow == 0) {
      return true;
    }
    for (int intRowNo = 0; intRowNo < intMaxRow; intRowNo++) {
      stringInOut = ("" + getValueAt("Table4", intRowNo, "InOut")).trim();
      stringDepart = ("" + getValueAt("Table4", intRowNo, "DepartNo")).trim();
      stringProjectID = ("" + getValueAt("Table4", intRowNo, "ProjectID")).trim();
      stringProjectID1 = ("" + getValueAt("Table4", intRowNo, "ProjectID1")).trim();
      stringCostID = ("" + getValueAt("Table4", intRowNo, "CostID")).trim();
      stringCostID1 = ("" + getValueAt("Table4", intRowNo, "CostID1")).trim();
      stringDiscountMoney = ("" + getValueAt("Table4", intRowNo, "DiscountMoney")).trim();
      stringDiscountNoTaxMoney = ("" + getValueAt("Table4", intRowNo, "DiscountNoTaxMoney")).trim();
      doubleDiscountMoney = exeUtil.doParseDouble(stringDiscountMoney);
      doubleDiscountNoTaxMoney = exeUtil.doParseDouble(stringDiscountNoTaxMoney);
      //
      if (doubleDiscountMoney == 0 && doubleDiscountNoTaxMoney == 0) {
        messagebox("�������� " + (intRowNo + 1) + " �C�� [�������B] ���i���s�ΪťաC");
        jtabbedPane1.setSelectedIndex(intTable4Panel);
        jtable4.setRowSelectionInterval(intRowNo, intRowNo);
        return false;
      }
      if (doubleDiscountMoney > 0 && doubleDiscountNoTaxMoney > 0) {
        // �����B�z
      } else if (doubleDiscountMoney > 0) {
        stringDiscountNoTaxMoney = convert.FourToFive("" + (doubleDiscountMoney / (1 + doubleTaxRate)), 0);
        doubleDiscountNoTaxMoney = exeUtil.doParseDouble(stringDiscountNoTaxMoney);
        setValueAt("Table4", stringDiscountNoTaxMoney, intRowNo, "DiscountNoTaxMoney");
      } else {
        stringDiscountMoney = convert.FourToFive("" + (doubleDiscountNoTaxMoney * (1 + doubleTaxRate)), 0);
        doubleDiscountMoney = exeUtil.doParseDouble(stringDiscountMoney);
        setValueAt("Table4", stringDiscountMoney, intRowNo, "DiscountMoney");
      }
      // �|�B�P�__+5
      doubleRealTaxR = doubleDiscountMoney - doubleDiscountNoTaxMoney;
      doubleRealTaxS = doubleDiscountNoTaxMoney * doubleTaxRate;
      if (doubleRealTaxR < doubleRealTaxS - 5 || doubleRealTaxR > doubleRealTaxS + 5) {
        messagebox("�������� " + (intRowNo + 1) + " �C�� [�������B] �P [���|���B] ���t�B���i�W�L [�������B] ���H 1+[�|�v] �����t 5�C");
        jtabbedPane1.setSelectedIndex(intTable4Panel);
        jtable4.setRowSelectionInterval(intRowNo, intRowNo);
        return false;
      }
      doubleRealTaxSum += doubleRealTaxR;
      // �����ˬd(�������୫��)
      stringKey = stringInOut + "-" + stringDepart + "-" + stringProjectID + "-" + stringProjectID1 + "-" + stringCostID + "-" + stringCostID1;
      if (vectorKey.indexOf(stringKey) != -1) {
        messagebox("�������� " + (intRowNo + 1) + " �C����ƭ��СC");
        jtabbedPane1.setSelectedIndex(intTable4Panel);
        jtable4.setRowSelectionInterval(intRowNo, intRowNo);
        return false;
      }
      vectorKey.add(stringKey);
      // �s�b�ˮ�(���b��O�Ϊ�椤)
      stringRealMoneyT = ("" + hashtable2Key.get(stringKey)).trim();
      if ("null".equals(stringRealMoneyT)) {
        messagebox("�������� " + (intRowNo + 1) + " �C����Ƥ��s�b�O�Ϊ�椤�C");
        jtabbedPane1.setSelectedIndex(intTable4Panel);
        jtable4.setRowSelectionInterval(intRowNo, intRowNo);
        return false;
      }
      doubleDiscountMoneySum += exeUtil.doParseDouble(convert.FourToFive("" + doubleDiscountMoney, 0));
    }
    // �����|�B�X�p������ �����X�p * �|�v / (1+�|�v)
    double doubleCF = doubleDiscountMoneySum * doubleTaxRate / (1 + doubleTaxRate);
    doubleCF = exeUtil.doParseDouble(convert.FourToFive("" + doubleCF, 0));
    doubleRealTaxSum = exeUtil.doParseDouble(convert.FourToFive("" + doubleRealTaxSum, 0));
    /*
     * if(doubleRealTaxSum<doubleCF-1 || doubleRealTaxSum>doubleCF+1) {
     * messagebox("[�����`�����|�B�X�p] ������ [�������B�X�p] ���H [�|�v] / (1+[�|�v])�C") ;
     * jtabbedPane1.setSelectedIndex(intTable4Panel) ; return false ; }
     */
    // �����`���B������(doubleInvoiceTotalMoneySum - doubleRealMoneySum)�t�B
    if (Double.compare(doubleDiscountMoneySum, doubleInvoiceTotalMoneySum - doubleRealMoneySum) != 0) {
      messagebox("[�����`���B] ������ [�дڪ��B] �P [�o���`���B] ���t�B�C");
      jtabbedPane1.setSelectedIndex(intTable4Panel);
      return false;
    }
    return true;
  }

  // ���ʳ���
  public Vector isTable6CheckOK(String stringVersion, Farglory.util.FargloryUtil exeUtil, Doc2M010 exeFun) throws Throwable {
    JTable jtable6 = getTable("Table6");
    JTable jtable7 = getTable("Table7");
    JTable jtable9 = getTable("Table9");
    JTabbedPane jtabbedPane1 = getTabbedPane("Tab1");
    int intTable6Panel = 0;
    int inRowCount6 = jtable6.getRowCount();
    int intRowCount7 = jtable7.getRowCount();
    String stringPurchaseNoAnd = "";
    String stringPurchaseNo = "";
    String stringPurchaseNo1 = "";
    String stringPurchaseNo2 = "";
    String stringPurchaseNo3 = "";
    String stringPurchaseNo4 = "";
    String stringDepartName = "";
    String retDateRoc = "";
    String stringComNo = getValue("ComNo").trim();
    String stringBarCode = getValue("BarCode").trim();
    String stringBarCodeOld = getValue("BarCodeOld").trim();
    String stringKindNo = getValue("KindNo").trim();
    String stringEmpDeptCd = getValue("DepartNo").trim(); // get("EMP_DEPT_CD") ;
    String stringFactoryNo = "";
    String stringFactoryNoOld = "";
    String stringFlow = getFunctionName();
    String stringUnipurchase = "";
    String stringUnipurchaseOld = "";
    String stringKey = "";
    String stringKeyOld = "";
    String stringCostID = "";
    String stringCostID1 = "";
    String stringPrimaryKey = "";
    String stringExistPurchaseMoney = "";
    String stringPaidUpMoney = "";
    String stringToday = datetime.getToday("yy/mm/dd");
    String stringDate = "096/11/26";
    String stringEDateTime = getValue("EDateTime").trim();
    String stringSqlAnd = " AND  (( CDate  >=  '" + stringDate + "'  AND  ApplyType  =  'F' AND (UNDERGO_WRITE  =  'S'  OR  UNDERGO_WRITE  =  'H'))  OR "
        + " UNDERGO_WRITE  =  'Y')";
    String[][] retDoc3M010 = new String[0][0];
    String[][] retDoc3M011 = new String[0][0];
    String[][] retDoc3M013 = new String[0][0];
    Vector vectorData = new Vector();
    Vector vectorPurchaseNo = new Vector();
    Vector vectorPurchaseNoL = new Vector();
    Vector vectorPrimaryKey = new Vector();
    Vector vectorApplyTypeD = new Vector();
    Vector vectorFactoryNoFirm = new Vector();
    double doublePurchaseMoney = 0;
    double doublePurchaseMoneyDB = 0;
    double doubleExistPurchaseMoney = 0;
    double doubleRetainMoney = exeUtil.doParseDouble(getValue("RetainMoney").trim()); // �h�O�d��(�t�|)
    //
    /*
     * if(inRowCount6 != intRowCount7) { messagebox("���ʪ��P�禬����Ƶ��Ƥ��@�P�C") ;
     * jtabbedPane1.setSelectedIndex(intTable6Panel) ; vectorData.add("N") ; return
     * vectorData ; }
     */
    //
    String stringBarCodeExcept = exeFun.getDoc2M040()[12];
    if (!"".equals(stringBarCodeOld) && stringBarCodeExcept.indexOf(stringBarCodeOld) != -1) {
    } else {
      String stringEDateTimeL = exeUtil.doSubstring(stringEDateTime, 0, 10);
      if (inRowCount6 > 1 && stringEDateTimeL.compareTo("2014/01/03") > 0) {
        messagebox("�дڥӽЮ� �Ȥ��\ ��@���ʳ�C");
        vectorData.add("N");
        return vectorData;
      }
    }
    if (doubleRetainMoney > 0 && inRowCount6 != 1) {
      messagebox("���O�d���ɡA���ʪ��u���\�@����ơC");
      jtabbedPane1.setSelectedIndex(intTable6Panel);
      vectorData.add("N");
      return vectorData;
    }
    setValueAt("Table6", convert.FourToFive("" + doubleRetainMoney, 0), 0, "RetainMoney");
    //
    String strinProjectID1 = "";
    String stringProjectIDCS = "";
    String stringGroupID = "";
    String stringBarCodePur = "";
    String[] arrayProjectID = { "H56", "H85" };
    boolean booleanProjectIDCS = false;
    boolean booleanSpecPurchaseNo = false;
    Hashtable hashtableData = new Hashtable();
    double doublePurchaseMoneySum = 0;
    for (int intNo = 0; intNo < inRowCount6; intNo++) {
      stringPurchaseNo1 = ("" + getValueAt("Table6", intNo, "PurchaseNo1")).trim();
      stringPurchaseNo2 = ("" + getValueAt("Table6", intNo, "PurchaseNo2")).trim();
      stringPurchaseNo3 = ("" + getValueAt("Table6", intNo, "PurchaseNo3")).trim();
      stringPurchaseNo4 = ("" + getValueAt("Table6", intNo, "PurchaseNo4")).trim();
      stringFactoryNo = ("" + getValueAt("Table6", intNo, "FactoryNo")).trim();
      doublePurchaseMoney = exeUtil.doParseDouble(("" + getValueAt("Table6", intNo, "PurchaseMoney")).trim());
      //
      String FGLIFE_NO = "84703052";
      if (!stringFactoryNo.equals(FGLIFE_NO)) {
        if (vectorFactoryNoFirm.indexOf(stringFactoryNo) == -1 && !exeFun.isFactoryNoOK(stringComNo, stringFactoryNo)) {
          jtabbedPane1.setSelectedIndex(intTable6Panel);
          jtable6.setRowSelectionInterval(intNo, intNo);
          vectorData.add("N");
          return vectorData;
        }
      }
      vectorFactoryNoFirm.add(stringFactoryNo);
      //
      if (!"".equals(stringPurchaseNoAnd)) stringPurchaseNoAnd += ", ";
      stringPurchaseNoAnd += " '" + stringPurchaseNo1 + stringPurchaseNo2 + stringPurchaseNo3 + "' ";
      // ���ʳ渹 1
      if ("".equals(stringPurchaseNo1)) {
        messagebox("���ʪ��� " + (intNo + 1) + " �C�� [���ʳ渹1] ���i���ťաC");
        jtabbedPane1.setSelectedIndex(intTable6Panel);
        jtable6.setRowSelectionInterval(intNo, intNo);
        vectorData.add("N");
        return vectorData;
      }
      stringDepartName = exeFun.getDepartName(stringPurchaseNo1);
      if ("".equals(stringDepartName)) {
        messagebox("���ʪ��� " + (intNo + 1) + " �C�� [���ʳ渹1] �ëD��Ʈw���s�b�� [�����N�X]�C");
        jtabbedPane1.setSelectedIndex(intTable6Panel);
        jtable6.setRowSelectionInterval(intNo, intNo);
        vectorData.add("N");
        return vectorData;
      }
      // �H�خקO�ˬd
      // ���ʳ渹 2
      if ("".equals(stringPurchaseNo2)) {
        messagebox("���ʪ��� " + (intNo + 1) + " �C�� [���ʳ渹2] ���i���ťաC");
        jtabbedPane1.setSelectedIndex(intTable6Panel);
        jtable6.setRowSelectionInterval(intNo, intNo);
        vectorData.add("N");
        return vectorData;
      }
      retDateRoc = exeUtil.getDateFullRoc(stringPurchaseNo2 + "01", "12345678");
      if (retDateRoc.length() != 9) {
        messagebox("���ʪ��� " + (intNo + 1) + " �C�� [���ʳ渹2] �榡���~(yymm)�C");
        jtabbedPane1.setSelectedIndex(intTable6Panel);
        jtable6.setRowSelectionInterval(intNo, intNo);
        vectorData.add("N");
        return vectorData;
      }
      retDateRoc = exeUtil.getDateConvertRoc(retDateRoc).replaceAll("/", "");
      stringPurchaseNo2 = datetime.getYear(retDateRoc) + datetime.getMonth(retDateRoc);
      setValueAt("Table6", stringPurchaseNo2, intNo, "PurchaseNo2");
      // ���ʳ渹 3
      if (stringPurchaseNo3.length() != 3) {
        messagebox("���ʪ��� " + (intNo + 1) + " �C�� [���ʳ渹3] �j�p�����T��ơC");
        jtabbedPane1.setSelectedIndex(intTable6Panel);
        jtable6.setRowSelectionInterval(intNo, intNo);
        vectorData.add("N");
        return vectorData;
      }
      if ("2".equals(stringVersion) && stringPurchaseNo4.length() == 0) {
        messagebox("���ʪ��� " + (intNo + 1) + " �C�� [���ʳ渹4] ���i���ťաC");
        jtabbedPane1.setSelectedIndex(intTable6Panel);
        jtable6.setRowSelectionInterval(intNo, intNo);
        vectorData.add("N");
        return vectorData;
      }
      // �s�b�ˮ�
      if ("2".equals(stringVersion)) {
        // 0 ComNo 1 DepartNo 2 FactoryNo 3 PayCondition1 4 PayCondition2
        // 5 PurchaseMoney 6 Unipurchase 7 Descript 8 BarCode 9 CostID
        // 10 CostID1 11 ExistPurchaseMoney
        retDoc3M010 = exeFun.getDoc3M010(stringPurchaseNo1, stringPurchaseNo2, stringPurchaseNo3, stringPurchaseNo4, stringComNo);
        if (retDoc3M010.length == 0) {
          messagebox("���ʪ��� " + (intNo + 1) + " �C�� [���ʳ渹] ���s�b��Ʈw���C");
          jtabbedPane1.setSelectedIndex(intTable6Panel);
          jtable6.setRowSelectionInterval(intNo, intNo);
          vectorData.add("N");
          return vectorData;
        }
        stringFactoryNo = retDoc3M010[0][2].trim();
        stringUnipurchase = retDoc3M010[0][6].trim();
        stringCostID = retDoc3M010[0][9].trim();
        stringCostID1 = retDoc3M010[0][10].trim();
        //
        stringExistPurchaseMoney = retDoc3M010[0][11].trim();
      } else {
        retDoc3M010 = exeFun.getDoc3M010(stringPurchaseNo1, stringPurchaseNo2, stringPurchaseNo3, stringPurchaseNo4, stringComNo);
        if (retDoc3M010.length > 0) {
          messagebox("���ʪ��� " + (intNo + 1) + " �C�� [���ʳ渹] �s�b [���ʳ���@�@�~] ���C");
          jtabbedPane1.setSelectedIndex(intTable6Panel);
          jtable6.setRowSelectionInterval(intNo, intNo);
          vectorData.add("N");
          return vectorData;
        }
        // 0 ComNo 1 DocNo 2 CDate 3 NeedDate 4 ApplyType
        // 5 Analysis 6 DepartNo 7 EDateTime 8 CDate 9 PrintCount
        // 10 CheckAdd 11 CheckAddDescript 12 BarCode 13 ID 14 PayConditionCross
        retDoc3M011 = exeFun.getDoc3M011(stringComNo, stringPurchaseNo1, stringPurchaseNo2, stringPurchaseNo3, stringSqlAnd);
        if (retDoc3M011.length == 0) {
          String stringTemp = "";
          //
          retDoc3M011 = exeFun.getDoc3M011(stringComNo, stringPurchaseNo1, stringPurchaseNo2, stringPurchaseNo3, "");
          if (retDoc3M011.length == 0) {
            stringTemp = "���ʪ��� " + (intNo + 1) + " �C�� [���ʳ渹] ���s�b��Ʈw���C";
          } else {
            String stringUnderGoWrite = retDoc3M011[0][15].trim();
            if (!"".equals(stringUnderGoWrite)) {
              switch (stringUnderGoWrite.charAt(0)) {
              case 'A':
                stringUnderGoWrite = "�ӿ�";
                break;
              case 'M':
                stringUnderGoWrite = "�ӿ�e�e";
                break;
              case 'P':
                stringUnderGoWrite = "��P�M��";
                break;
              case 'B':
                stringUnderGoWrite = "�~��";
                break;
              case 'S':
                stringUnderGoWrite = "��t�D��";
                break;
              case 'C':
                stringUnderGoWrite = "����";
                break;
              case 'Y':
                stringUnderGoWrite = "���ʥD��";
                break;
              case 'X':
                stringUnderGoWrite = "�@�o";
                break;
              case 'R':
                stringUnderGoWrite = "�h��(����)";
                break;
              case 'F':
                stringUnderGoWrite = "�h��(��P�D��)";
                break;
              case 'D':
                stringUnderGoWrite = "���sĳ��";
                break;
              case 'G':
                stringUnderGoWrite = "�ӿ�(�H�`)";
                break;
              case 'H':
                stringUnderGoWrite = "�f��(�H�`)";
                break;
              }
            }
            stringTemp += "�� " + (intNo + 1) + " �C�� [���ʳ渹] �B�� [" + stringUnderGoWrite + "] ���A�A�|���������ʬy�{�C";
          }
          messagebox(stringTemp);
          jtabbedPane1.setSelectedIndex(intTable6Panel);
          jtable6.setRowSelectionInterval(intNo, intNo);
          vectorData.add("N");
          return vectorData;
        } else {
          if (!"".equals(retDoc3M011[0][18].trim())) {
            setValue("LabelKind", retDoc3M011[0][18].trim());
          }
          if ("D".equals(retDoc3M011[0][4].trim())) {
            vectorApplyTypeD.add(stringPurchaseNo1 + stringPurchaseNo2 + stringPurchaseNo3);
          }
        }
        stringUnipurchase = "F".equals(retDoc3M011[0][5].trim()) ? "Y" : "N";
        stringCostID = "";
        stringCostID1 = "";
        // �󶥬q�ˮ�
        stringBarCodePur = retDoc3M011[0][12].trim();
        stringGroupID = getGroupID(stringBarCodePur, stringPurchaseNo1 + stringPurchaseNo2 + stringPurchaseNo3, exeUtil, exeFun);
        booleanSpecPurchaseNo = exeFun.isSpectPurchaseNo(stringComNo, "17", stringPurchaseNo1 + stringPurchaseNo2 + stringPurchaseNo3, " AND  M13.GroupName  LIKE  '%#-#B' ");
        System.out.println("PayConditionCross(" + retDoc3M011[0][14].trim() + ")-----------------------------");
        if ("P93417".equals(stringBarCodePur)) {
          jtabbedPane1.setSelectedIndex(intTable6Panel);
          jtable6.setRowSelectionInterval(intNo, intNo);
          vectorData.add("N");
          messagebox("�S����ʳ椣���\�ϥΡC");
          return vectorData;
        }
        if (!"".equals(stringFactoryNo) && !"Y".equals(retDoc3M011[0][14].trim())) {
          // 0 FactoryNo 1 PurchaseSumMoney 2 PercentRate 3 MonthNum 4 PurchaseMoney
          // 5 PayCondition1 6 PayCondition2 7 Descript 8 NoUseRealMoney
          retDoc3M013 = exeFun.getDoc3M013(retDoc3M011[0][12].trim(), stringFactoryNo);
          if (retDoc3M013.length == 0) {
            messagebox("���ʪ��� " + (intNo + 1) + " �C [���ʳ渹] �� ���s�b�� [��H]�C");
            jtabbedPane1.setSelectedIndex(intTable6Panel);
            jtable6.setRowSelectionInterval(intNo, intNo);
            vectorData.add("N");
            return vectorData;
          }
          // �w�ӽЪ��дڪ��B���]�t����(Doc7M017)
          int intCount = 0;
          double doubleUsePurchaseMoneyL = exeUtil
              .doParseDouble(exeFun.getExistFactoryNoRealMoneyForDoc2M010(stringBarCodeOld, stringComNo, stringEDateTime, stringPurchaseNo1, stringPurchaseNo2, stringPurchaseNo3,
                  stringFactoryNo))
              + exeUtil.doParseDouble(exeFun.getExistFactoryNoRealMoneyBorrowForDoc6M010(stringBarCodeOld, stringComNo, stringEDateTime, stringPurchaseNo1, stringPurchaseNo2,
                  stringPurchaseNo3, stringFactoryNo));
          double doubleUsePurchaseMoneySumL = doubleUsePurchaseMoneyL + doublePurchaseMoney; // �w�ӽЪ����B(�X�p)(�]�t����)
          double doublePurchaseMoneySumL = 0;
          for (int intDoc3M013 = 0; intDoc3M013 < retDoc3M013.length; intDoc3M013++) {
            doublePurchaseMoneySumL += exeUtil.doParseDouble(retDoc3M013[intDoc3M013][4].trim());
            // [�X�����B(���q)�[�`] �j�� [�w�ӽЪ��B(���]�t����)]
            if (doublePurchaseMoneySumL > doubleUsePurchaseMoneyL) {
              intCount++;
              System.out.println(intDoc3M013 + "--------------------------���q" + intCount);
            }
            // [�X�����B(���q)�[�`] �j�󵥩� [�w�ӽЪ����B(�X�p)(�]�t����)]
            if (doublePurchaseMoneySumL >= doubleUsePurchaseMoneySumL) break;
          }
          if (intCount >= 2) {
            messagebox("���ʪ��� " + (intNo + 1) + " �C [���ʳ渹] �� �����\�󶥬q���ʡC");
            jtabbedPane1.setSelectedIndex(intTable6Panel);
            jtable6.setRowSelectionInterval(intNo, intNo);
            vectorData.add("N");
            return vectorData;
          }
        }

      }
      // �ˮ� ���ʡB��H�B�O�� �@�P
      stringKey = stringCostID + "-" + stringCostID1;
      if (!"".equals(stringUnipurchaseOld) && !stringUnipurchaseOld.equals(stringUnipurchase)) {
        messagebox("���ʪ��� " + (intNo + 1) + " �C [���ʳ渹] �� [����] ���@�P�C");
        jtabbedPane1.setSelectedIndex(intTable6Panel);
        jtable6.setRowSelectionInterval(intNo, intNo);
        vectorData.add("N");
        return vectorData;
      }
      if (!"".equals(stringFactoryNoOld) && !stringFactoryNoOld.equals(stringFactoryNo)) {
        messagebox("���ʪ��� " + (intNo + 1) + " �C [���ʳ渹] �� [��H] ���@�P�C");
        jtabbedPane1.setSelectedIndex(intTable6Panel);
        jtable6.setRowSelectionInterval(intNo, intNo);
        vectorData.add("N");
        return vectorData;
      }
      if ("2".equals(stringVersion) && !"".equals(stringKeyOld) && !stringKeyOld.equals(stringKey)) {
        messagebox("���ʪ��� " + (intNo + 1) + " �C [���ʳ渹] �� [�дڥN�X] ���@�P�C");
        jtabbedPane1.setSelectedIndex(intTable6Panel);
        jtable6.setRowSelectionInterval(intNo, intNo);
        vectorData.add("N");
        return vectorData;
      }
      stringUnipurchaseOld = stringUnipurchase;
      stringFactoryNoOld = stringFactoryNo;
      stringKeyOld = stringKey;
      // ���ʳ渹(�����ˮ�)
      stringPurchaseNo = stringPurchaseNo1 + stringPurchaseNo2 + stringPurchaseNo3;
      if (vectorPurchaseNoL.indexOf(stringPurchaseNo) != -1) {
        messagebox("���ʪ��� " + (intNo + 1) + " �C [���ʳ渹] ���ơC");
        jtabbedPane1.setSelectedIndex(intTable6Panel);
        jtable6.setRowSelectionInterval(intNo, intNo);
        vectorData.add("N");
        return vectorData;
      }
      vectorPurchaseNo.add(stringPurchaseNo1 + stringPurchaseNo2 + stringPurchaseNo3);
      vectorPurchaseNoL.add(stringPurchaseNo);
      setValueAt("Table6", stringPurchaseNo1 + stringPurchaseNo2 + stringPurchaseNo3, intNo, "PurchaseNo");
      // ���ʪ��B
      // �D�����w�дڪ��B
      if (doublePurchaseMoney <= 0) {
        messagebox("���ʪ��� " + (intNo + 1) + " �C�� [���ʪ��B] ���j��s�C");
        jtabbedPane1.setSelectedIndex(intTable6Panel);
        jtable6.setRowSelectionInterval(intNo, intNo);
        vectorData.add("N");
        return vectorData;
      }
      if ("2".equals(stringVersion)) {
        doublePurchaseMoneyDB = exeUtil.doParseDouble(retDoc3M010[0][5].trim());
      } else {
        // �t�Ӧs�b�ˮ�
        retDoc3M013 = exeFun.getDoc3M013(stringComNo, stringPurchaseNo1 + stringPurchaseNo2 + stringPurchaseNo3, stringFactoryNo);
        if (retDoc3M013.length == 0) {
          messagebox("���ʪ��� " + (intNo + 1) + " �C [���ʳ渹] �� ���s�b�� [��H]�C");
          jtabbedPane1.setSelectedIndex(intTable6Panel);
          jtable6.setRowSelectionInterval(intNo, intNo);
          vectorData.add("N");
          return vectorData;
        }
        // doublePurchaseMoneyDB = exeUtil.doParseDouble(retDoc3M013[0][1].trim()) ;
        doublePurchaseMoneyDB = getContractMoney(stringBarCodePur, stringFactoryNo, stringGroupID, exeUtil, exeFun);
      }
      /*
       * if("Y".equals(retDoc3M010[0][6].trim( ))) { doubleExistPurchaseMoney = 0 ; }
       * else {
       */
      // System.out.println("getExistRealMoney---------------"+stringBarCodeOld) ;
      if ("2".equals(stringVersion)) {
        stringPaidUpMoney = exeFun.getPaidUpMoney(stringComNo, stringBarCodeOld, stringPurchaseNo1, stringPurchaseNo2, stringPurchaseNo3, stringPurchaseNo4);
        // �w�ӽЪ��B(����ǲ�)
        doubleExistPurchaseMoney = exeUtil
            .doParseDouble(exeFun.getExistRealMoney(stringBarCodeOld, stringComNo, stringPurchaseNo1, stringPurchaseNo2, stringPurchaseNo3, stringPurchaseNo4));
        doubleExistPurchaseMoney += exeUtil.doParseDouble(stringPaidUpMoney);
      } else {
        // stringPaidUpMoney = exeFun.getPaidUpMoneyForFactoryNo(stringComNo,
        // stringBarCodeOld, stringPurchaseNo1, stringPurchaseNo2, stringPurchaseNo3,
        // stringFactoryNo) ;
        // �w�ӽЪ��B(����ǲ�)
        // System.out.println("stringPaidUpMoney---------------"+stringPaidUpMoney) ;
        // doubleExistPurchaseMoney =
        // exeUtil.doParseDouble(exeFun.getExistRealMoneyForFactoryNo(stringBarCodeOld,
        // stringComNo, stringPurchaseNo1, stringPurchaseNo2, stringPurchaseNo3,
        // stringFactoryNo)) ;
        doubleExistPurchaseMoney = getPaidUpMoney(stringPurchaseNo1, stringPurchaseNo2, stringPurchaseNo3, stringFactoryNo, stringBarCodePur, stringGroupID, booleanSpecPurchaseNo,
            exeUtil, exeFun);
      }
      // System.out.println("doubleExistPurchaseMoney---------------"+doubleExistPurchaseMoney)
      // ;

//            }
      System.out.println("doublePurchaseMoney(" + doublePurchaseMoney + ")------------------------------");
      System.out.println("doublePurchaseMoneyDB(" + doublePurchaseMoneyDB + ")------------------------------");
      System.out.println("doubleExistPurchaseMoney(" + doubleExistPurchaseMoney + ")------------------------------");
      System.out.println("stringExistPurchaseMoney(" + stringExistPurchaseMoney + ")------------------------------");
      if (!"Y".equals(stringUnipurchase) && (doublePurchaseMoney > doublePurchaseMoneyDB - doubleExistPurchaseMoney - exeUtil.doParseDouble(stringExistPurchaseMoney))) {
        System.out.println("Money(" + (doublePurchaseMoneyDB - doubleExistPurchaseMoney - exeUtil.doParseDouble(stringExistPurchaseMoney)) + ")------------------------------");
        messagebox("���ʪ��� " + (intNo + 1) + " �C�� [���ʪ��B] �w�j��� [�X�����B]�C");
        jtabbedPane1.setSelectedIndex(intTable6Panel);
        jtable6.setRowSelectionInterval(intNo, intNo);
        vectorData.add("N");
        return vectorData;
      }
      doublePurchaseMoneySum = doublePurchaseMoney + doubleExistPurchaseMoney + exeUtil.doParseDouble(stringExistPurchaseMoney);
      if (!isContractOK(stringBarCodePur, stringFactoryNo, stringPurchaseNo, doublePurchaseMoneyDB, doublePurchaseMoneySum, exeUtil)) {
        jtabbedPane1.setSelectedIndex(intTable6Panel);
        jtable6.setRowSelectionInterval(intNo, intNo);
        vectorData.add("N");
        return vectorData;
      }
      if (jtable9.getRowCount() <= 0) {
        hashtableData.put("ComNo", stringComNo);
        hashtableData.put("KindNo", stringKindNo);
        hashtableData.put("EmpDeptCd", stringEmpDeptCd);
        hashtableData.put("UserID", getValue("EmployeeNo"));
        hashtableData.put("FactoryNo", stringFactoryNo);
        hashtableData.put("PurchaseNo", stringPurchaseNo);
        System.out.println("���ʩ��Ӷ��� �P�_--------------------------");
        if (exeFun.isNewVersion(hashtableData, jtable9)) {
          messagebox("�п�J [���ʩ��Ӷ���] ��ơC");
          jtabbedPane1.setSelectedIndex(intTable6Panel);
          vectorData.add("N");
          return vectorData;
        }
      }
    }
    // �~���w�� E-mail �q��
    if (!"".equals(stringPurchaseNoAnd)) {
      exeFun.doYearEndDataExistEmail("", "", "", "AND  RTRIM(PurchaseNo1)+ RTRIM(PurchaseNo2)+ RTRIM(PurchaseNo3)  IN  (" + stringPurchaseNoAnd + ")",
          getFunctionName() + "(" + stringBarCode + ")", exeUtil);
    }
    vectorData.add("Y");
    vectorData.add(vectorPurchaseNo);
    vectorData.add(vectorApplyTypeD);
    return vectorData;
  }

  public boolean isContractOK(String stringBarCodePur, String stringFactoryNo, String stringPurchaseNo, double doublePurchaseMoneyPur, double doublePurchaseMoneySum,
      FargloryUtil exeUtil) throws Throwable {
    if (!"B3018".equals(getUser())) return true;
    if (getFunctionName().indexOf("ñ��") == -1) return true;
    //
    talk dbConstAsk = getTalk("" + get("put_Const_Ask"));
    Hashtable hashtableAnd = new Hashtable();
    Vector vectorRelContractSendDetailFin = null;
    String stringPrdocode = "";
    // 50 �U�H�U����ĵ��
    if (doublePurchaseMoneyPur <= 500000) return true;
    // ���ϥΦ� 30 %����ĵ��
    if (doublePurchaseMoneySum < doublePurchaseMoneyPur * 0.3) return true;
    // �X���y�{�P�_
    stringPrdocode = exeUtil.getNameUnion("prdocode", "prdt", " AND  social  =  '" + stringFactoryNo + "' ", new Hashtable(), dbConstAsk);
    //
    hashtableAnd.put("cid_barcode", stringBarCodePur);
    hashtableAnd.put("cid_prdocode", stringPrdocode);
    // hashtableAnd.put("permit", "1") ;
    vectorRelContractSendDetailFin = exeUtil.getQueryDataHashtable("rel_contract_send_detail_fin", hashtableAnd, "", dbConstAsk);
    if (vectorRelContractSendDetailFin.size() == 0) {
      messagebox("���ʳ�(" + stringPurchaseNo + ")�t��(" + stringFactoryNo + ") �|�������X���y�{�C");
      return false;
    }
    return true;
  }

  public double getContractMoney(String stringBarCodePur, String stringFactoryNo, String stringGroupID, FargloryUtil exeUtil, Doc2M010 exeFun) throws Throwable {
    JTable jtable9 = getTable("Table9");
    double doublePurchaseMoneyDB = 0;
    String stringContractMoney = "";
    String stringGroupIDL = "";
    String stringGroupName = "";
    String[] arrayGroupName = null;
    Vector vectorDoc3M013 = new Vector();
    Hashtable hashtableAnd = new Hashtable();
    Hashtable hashtableDoc3M013 = new Hashtable();
    //
    hashtableAnd.put("BarCode", stringBarCodePur);
    hashtableAnd.put("FactoryNo", stringFactoryNo);
    vectorDoc3M013 = exeFun.getQueryDataHashtableDoc("Doc3M013", hashtableAnd, "", new Vector(), exeUtil);
    for (int intNo = 0; intNo < vectorDoc3M013.size(); intNo++) {
      hashtableDoc3M013 = (Hashtable) vectorDoc3M013.get(intNo);
      stringContractMoney = "" + hashtableDoc3M013.get("PurchaseSumMoney");
      stringGroupName = ("" + hashtableDoc3M013.get("GroupName")).trim();
      stringGroupIDL = ("" + hashtableDoc3M013.get("GroupID")).trim();
      //
      arrayGroupName = convert.StringToken(stringGroupName, "#-#");
      //
      // 20180607 �ҥ~�B�z
      if (stringBarCodePur.equals("S00022")) {
        if (stringGroupID.equals(stringGroupIDL)) return exeUtil.doParseDouble(stringContractMoney);
      }
      if (arrayGroupName.length == 4 && jtable9.getRowCount() > 0) {
        if (stringGroupID.equals(stringGroupIDL)) return exeUtil.doParseDouble(stringContractMoney);
      } else {
        if (intNo == vectorDoc3M013.size() - 1) return exeUtil.doParseDouble(stringContractMoney);

      }
    }
    return doublePurchaseMoneyDB;
  }

  public String getGroupID(String stringBarCodePur, String stringPurchaseNo, FargloryUtil exeUtil, Doc2M010 exeFun) throws Throwable {
    JTable jtable9 = getTable("Table9");
    String stringBarCode = "";
    String stringComNo = getValue("ComNo").trim();
    String stringGroupID = "";
    String stringPurchaseNoL = "";
    String stringRecordNo12L = "";
    Hashtable hashtableAnd = new Hashtable();
    //
    for (int intNo = 0; intNo < jtable9.getRowCount(); intNo++) {
      stringPurchaseNoL = ("" + getValueAt("Table9", intNo, "PurchaseNo")).trim();
      stringRecordNo12L = ("" + getValueAt("Table9", intNo, "RecordNo12")).trim();
      //
      if (!stringPurchaseNo.equals(stringPurchaseNoL)) continue;
      //
      hashtableAnd.put("BarCode", stringBarCodePur);
      hashtableAnd.put("RecordNo", stringRecordNo12L);
      stringGroupID = exeFun.getNameUnionDoc("GroupID", "Doc3M012", "", hashtableAnd, exeUtil);
      return stringGroupID;
    }
    //
    return "";
  }

  public double getPaidUpMoney(String stringPurchaseNo1, String stringPurchaseNo2, String stringPurchaseNo3, String stringFactoryNo, String stringBarCodePur, String stringGroupID,
      boolean booleanSpecPurchaseNo, FargloryUtil exeUtil, Doc2M010 exeFun) throws Throwable {
    String stringBarCode = getValue("BarCodeOld").trim();
    String stringComNo = getValue("ComNo").trim();
    Hashtable hashtableData = new Hashtable();
    //
    hashtableData.put("ComNo", stringComNo);
    hashtableData.put("PurchaseNo1", stringPurchaseNo1);
    hashtableData.put("PurchaseNo2", stringPurchaseNo2);
    hashtableData.put("PurchaseNo3", stringPurchaseNo3);
    hashtableData.put("FactoryNo", stringFactoryNo);
    hashtableData.put("EDateTime", "");
    hashtableData.put("GroupID", stringGroupID);
    hashtableData.put("BarCode", stringBarCode);
    hashtableData.put("BarCodePur", stringBarCodePur);
    hashtableData.put("UseType", "B"); // A �e�� B �w�ϥ�
    //
    return exeFun.getPaidUpMoney(hashtableData, exeUtil);
  }

  // �禬���
  public boolean isTable7CheckOK(Vector vectorPurchaseNo, Vector vectorApplyTypeD, FargloryUtil exeUtil, Doc2M010 exeFun) throws Throwable {
    JTable jtable7 = getTable("Table7");
    JTabbedPane jtabbedPane1 = getTabbedPane("Tab1");
    int intTable7Panel = 1;
    int intRowCount = jtable7.getRowCount();
    String stringOptometryNo = "";
    String stringOptometryNo1 = "";
    String stringOptometryNo2 = "";
    String stringOptometryNo3 = "";
    String stringPurchaseNo = "";
    String stringPurchaseNo1 = "";
    String stringPurchaseNo2 = "";
    String stringPurchaseNo3 = "";
    String stringOptometryType = "";
    String stringDepartName = "";
    String retDateRoc = "";
    String stringKey = "";
    String stringComNo = getValue("ComNo").trim();
    String stringBarCode = getValue("BarCodeOld").trim();
    String stringFunctionName = getFunctionName();
    Vector vectorKey = new Vector();
    Vector vectorPurchaseNoL = new Vector();
    Vector vectorOptometryNo = new Vector();
    boolean booleanFlag = true;
    Hashtable hashtableCondition = new Hashtable();
    //
    if (intRowCount == 0) {
      messagebox("�ж�g�禬����ơC");
      jtabbedPane1.setSelectedIndex(intTable7Panel);
      return false;
    }
    //
    String stringBarCodeRef = "";
    String stringOptometryVersion = "";
    for (int intNo = 0; intNo < intRowCount; intNo++) {
      stringOptometryNo1 = ("" + getValueAt("Table7", intNo, "OptometryNo1")).trim();
      stringOptometryNo2 = ("" + getValueAt("Table7", intNo, "OptometryNo2")).trim();
      stringOptometryNo3 = ("" + getValueAt("Table7", intNo, "OptometryNo3")).trim();
      stringOptometryNo = stringOptometryNo1 + stringOptometryNo2 + stringOptometryNo3;
      stringPurchaseNo1 = ("" + getValueAt("Table7", intNo, "PurchaseNo1")).trim();
      stringPurchaseNo2 = ("" + getValueAt("Table7", intNo, "PurchaseNo2")).trim();
      stringPurchaseNo3 = ("" + getValueAt("Table7", intNo, "PurchaseNo3")).trim();
      stringBarCodeRef = ("" + getValueAt("Table7", intNo, "BarCodeRef")).trim();
      stringOptometryType = ("" + getValueAt("Table7", intNo, "OptometryType")).trim();
      stringOptometryVersion = ("" + getValueAt("Table7", intNo, "OptometryVersion")).trim();
      stringPurchaseNo = stringPurchaseNo1 + stringPurchaseNo2 + stringPurchaseNo3;
      stringKey = stringOptometryNo1 + "-" + stringOptometryNo2 + "-" + stringOptometryNo3 + "-" + stringPurchaseNo;
      //
      if (vectorOptometryNo.indexOf(stringOptometryNo) == -1) vectorOptometryNo.add(stringOptometryNo);
      //
      booleanFlag = (vectorApplyTypeD.indexOf(stringPurchaseNo) == -1);
      setValueAt("Table7", booleanFlag ? "A" : "B", intNo, "OptometryType");
      //
      if ("".equals(stringBarCodeRef)) stringBarCodeRef = stringBarCode;
      // �禬�渹 1
      if ("".equals(stringOptometryNo1)) {
        messagebox("�禬���� " + (intNo + 1) + " �C�� [�禬�渹1] ���i���ťաC");
        jtabbedPane1.setSelectedIndex(intTable7Panel);
        jtable7.setRowSelectionInterval(intNo, intNo);
        return false;
      }
      stringDepartName = exeFun.getDepartName(stringOptometryNo1);
      if ("".equals(stringDepartName)) {
        messagebox("�禬���� " + (intNo + 1) + " �C�� [�禬�渹1] �ëD��Ʈw���s�b�� [�����N�X]�C");
        jtabbedPane1.setSelectedIndex(intTable7Panel);
        jtable7.setRowSelectionInterval(intNo, intNo);
        return false;
      }
      // �禬�渹 2
      if ("".equals(stringOptometryNo2)) {
        messagebox("�禬���� " + (intNo + 1) + " �C�� [�禬�渹2] ���i���ťաC");
        jtabbedPane1.setSelectedIndex(intTable7Panel);
        jtable7.setRowSelectionInterval(intNo, intNo);
        return false;
      }
      retDateRoc = exeUtil.getDateFullRoc(stringOptometryNo2 + "01", "12345678");
      if (retDateRoc.length() != 9) {
        messagebox("�禬���� " + (intNo + 1) + " �C�� [�禬�渹2] �榡���~(yymm)�C");
        jtabbedPane1.setSelectedIndex(intTable7Panel);
        jtable7.setRowSelectionInterval(intNo, intNo);
        return false;
      }
      retDateRoc = exeUtil.getDateConvertRoc(retDateRoc).replaceAll("/", "");
      stringOptometryNo2 = datetime.getYear(retDateRoc) + datetime.getMonth(retDateRoc);
      setValueAt("Table7", stringOptometryNo2, intNo, "OptometryNo2");
      // �禬�渹 3
      if (stringOptometryNo3.length() != 3) {
        messagebox("�禬���� " + (intNo + 1) + " �C�� [�禬�渹3] �j�p�����T��ơC");
        jtabbedPane1.setSelectedIndex(intTable7Panel);
        jtable7.setRowSelectionInterval(intNo, intNo);
        return false;
      }
      // �禬�渹 �����ˮ�(����)
      if (vectorKey.indexOf(stringKey) != -1) {
        messagebox("�禬���� " + (intNo + 1) + " �C�� �禬��ƭ��ơC");
        jtabbedPane1.setSelectedIndex(intTable7Panel);
        jtable7.setRowSelectionInterval(intNo, intNo);
        return false;
      }
      // System.out.println("---------------------- ��Ʈw�s�b�ˮ�") ;
      if ("B".equals(stringOptometryVersion)) {
        String stringUndergoWrite = "";
        Hashtable hashtableAnd = new Hashtable();
        Vector vectorDoc5M060 = null;
        hashtableAnd.put("DocNo1", stringOptometryNo1);
        hashtableAnd.put("DocNo2", stringOptometryNo2);
        hashtableAnd.put("DocNo3", stringOptometryNo3);
        hashtableAnd.put("ComNo", stringComNo);
        vectorDoc5M060 = exeFun.getQueryDataHashtableDoc("Doc5M060", hashtableAnd, "", new Vector(), exeUtil);
        if (vectorDoc5M060.size() == 0) {
          messagebox("�禬���� " + (intNo + 1) + " �C�� �禬�椣�s�b��Ʈw���A�����\���ʸ�Ʈw�C");
          jtabbedPane1.setSelectedIndex(intTable7Panel);
          jtable7.setRowSelectionInterval(intNo, intNo);
          return false;
        }
        stringUndergoWrite = exeUtil.getVectorFieldValue(vectorDoc5M060, 0, "UNDERGO_WRITE");
        if (stringFunctionName.indexOf("�~��") != -1) {
          String stringUndergoWrite1 = exeUtil.doSubstring(stringUndergoWrite, 0, 1);
          if ("B".equals(stringUndergoWrite1)) {
            String stringID = exeUtil.getVectorFieldValue(vectorDoc5M060, 0, "ID");
            Vector vectorDoc5M064 = exeFun.getQueryDataHashtableDoc("Doc5M064", hashtableAnd,
                " AND  ID  =  " + stringID + " AND  UNDERGO_WRITE  LIKE  'B%'  AND  CheckDate  =  '' ", new Vector(), exeUtil);
            if (vectorDoc5M064.size() > 0) {
              messagebox("�禬���� " + (intNo + 1) + " �C�� �禬�楼���� [�ӿ�-��D��] �禬�y�{�A�����\���ʸ�Ʈw�C" + stringUndergoWrite);
              jtabbedPane1.setSelectedIndex(intTable7Panel);
              jtable7.setRowSelectionInterval(intNo, intNo);
              return false;
            }
          } else if (",C,D,F,G,Y,K,L,H,I,J,".indexOf(stringUndergoWrite1) == -1) {
            messagebox("�禬���� " + (intNo + 1) + " �C�� �禬�楼���� [�ӿ�-��D��] �禬�y�{�A�����\���ʸ�Ʈw�C" + stringUndergoWrite);
            jtabbedPane1.setSelectedIndex(intTable7Panel);
            jtable7.setRowSelectionInterval(intNo, intNo);
            return false;
          }
        }
        if (stringFunctionName.indexOf("ñ��") != -1) {
          // �禬�y�{�ˮ�
          if ("G".equals(stringUndergoWrite)) {
            // �дڥӽЮ� ���ʥX��ΰ]�Ȧ���ΰ]�ȵ��סA�h�����ʥD�ޤwñ�֡C
            String[][] retDoc1M040 = exeFun.getTableDataDoc(
                "SELECT  Barcode " + " FROM  Doc1M040 " + " WHERE  Barcode  =  '" + stringBarCode + "' " + " AND  DepartNo  LIKE  '021%' " + " AND  DocStatus  =  '4' ");
            if (retDoc1M040.length == 0) {
              messagebox("�禬���� " + (intNo + 1) + " �C�� �禬�楼�����禬�y�{�A�����\���ʸ�Ʈw�C1");
              jtabbedPane1.setSelectedIndex(intTable7Panel);
              jtable7.setRowSelectionInterval(intNo, intNo);
              return false;
            }
            retDoc1M040 = exeFun.getTableDataDoc(
                "SELECT  Barcode " + " FROM  Doc1M040 " + " WHERE  Barcode  =  '" + stringBarCode + "' " + " AND  DepartNo  LIKE  '022%' " + " AND  DocStatus  IN(  '2',  '3') ");
            if (retDoc1M040.length == 0) {
              messagebox("�禬���� " + (intNo + 1) + " �C�� �禬�楼�����禬�y�{�A�����\���ʸ�Ʈw�C2");
              jtabbedPane1.setSelectedIndex(intTable7Panel);
              jtable7.setRowSelectionInterval(intNo, intNo);
              return false;
            }
          } else if (!"Y".equals(stringUndergoWrite)) {
            messagebox("�禬���� " + (intNo + 1) + " �C�� �禬�楼�����禬�y�{�A�����\���ʸ�Ʈw�C3");
            jtabbedPane1.setSelectedIndex(intTable7Panel);
            jtable7.setRowSelectionInterval(intNo, intNo);
            return false;
          }
        }
      } else {
        if (exeFun.isExistOptometryNo(stringComNo, stringOptometryNo1, "", stringOptometryNo2, stringOptometryNo3, stringBarCode, stringBarCodeRef, hashtableCondition)) {
          messagebox("�禬���� " + (intNo + 1) + " �C�� [�禬�渹] �w�s�b��Ʈw���C");
          jtabbedPane1.setSelectedIndex(intTable7Panel);
          jtable7.setRowSelectionInterval(intNo, intNo);
          return false;
        }
      }
      //
      vectorKey.add(stringKey);
      setValueAt("Table7", stringOptometryNo1 + stringOptometryNo2 + stringOptometryNo3, intNo, "OptometryNo");
      setValueAt("Table7", stringPurchaseNo1 + stringPurchaseNo2 + stringPurchaseNo3, intNo, "PurchaseNo");
      // ���ʳ渹
      if (vectorPurchaseNo.indexOf(stringPurchaseNo) == -1) {
        messagebox("�禬���� " + (intNo + 1) + " �C�� [���ʳ渹] ���s�b���ʪ�椤�C");
        jtabbedPane1.setSelectedIndex(intTable7Panel);
        jtable7.setRowSelectionInterval(intNo, intNo);
        return false;
      }
      if (vectorPurchaseNoL.indexOf(stringPurchaseNo) == -1) vectorPurchaseNoL.add(stringPurchaseNo);
    }
    if (vectorPurchaseNoL.size() != vectorPurchaseNo.size()) {
      messagebox("[�禬���] �P [���ʪ��] �������@�P�C");
      jtabbedPane1.setSelectedIndex(intTable7Panel);
      return false;
    }
    //
    return true;
  }

  public String isTable21CheckOK(FargloryUtil exeUtil, Doc2M010 exeFun) throws Throwable {
    JTable jtable1 = getTable("Table1");
    JTable jtable2 = getTable("Table2");
    JTable jtable21 = getTable("Table21");
    JTable jtable3 = getTable("Table3");
    String stringCDate = getValue("CDate").trim();
    String stringComNo = getValue("ComNo").trim();
    String stringBarCode = getValue("BarCode").trim();
    String stringBarCodeL = "";
    String stringCDateL = "";
    String stringSystemCd = "";
    String stringSql = "";
    String stringTempKey = "";
    String stringCompanyCd = "";
    String stringVoucherYMD = "";
    String stringVoucherFlowNo = "";
    String stringStatusCd = "";
    String stringTmp = "";
    String stringVoucherType = "";
    String stringReceiptKind = "";
    String stringReceiptTotalMoney = "";
    String stringReceiptMoney = "";
    String stringReceiptTax = "";
    String stringTemp = "";
    String[] arrayReceiptKind = { "A", "C" };
    // String[] arrayReceiptKind = {"A", "C"} ;
    String[] arrayReceiptTotalMoney = { "", "" };
    String[] arrayReceiptTax = { "", "" };
    String[][] retDoc2M0143 = null;
    Hashtable hashtableAnd = new Hashtable();
    Hashtable hashtableTmp = new Hashtable();
    Vector vectorFED1040 = new Vector();
    Vector vectorDoc2M0143 = new Vector();
    double doubleReceiptTotalMoney = 0;
    boolean booleanSYS = "SYS".equals(getUser());
    //
    if (jtable1.getRowCount() > 0) {
      messagebox("�~��y�{ �����\��J [�o�����]�C");
      return "ERR%-%1";
    }
    if (jtable3.getRowCount() == 0) {
      messagebox("�~��y�{ ������J [�ӤH���ڪ��]�C");
      return "ERR%-%3";
    }
    //
    for (int intNo = 0; intNo < jtable21.getRowCount(); intNo++) {
      stringCDateL = ("" + getValueAt("Table21", intNo, "CDate")).trim();
      stringSystemCd = ("" + getValueAt("Table21", intNo, "SYSTEM_CD")).trim();
      stringTempKey = ("" + getValueAt("Table21", intNo, "TEMP_KEY")).trim();
      stringCompanyCd = ("" + getValueAt("Table21", intNo, "COMPANY_CD")).trim();
      stringVoucherYMD = ("" + getValueAt("Table21", intNo, "VOUCHER_YMD")).trim();
      stringVoucherFlowNo = ("" + getValueAt("Table21", intNo, "VOUCHER_FLOW_NO")).trim();
      stringStatusCd = ("" + getValueAt("Table21", intNo, "STATUS_CD")).trim();
      //
      if (!stringCDate.equals(stringCDateL)) {
        setValueAt("Table21", stringCDate, intNo, "CDate");
      }
      if (!stringComNo.equals(stringCompanyCd)) {
        setValueAt("Table21", stringComNo, intNo, "COMPANY_CD");
      }
      // Doc2M0143
      // ����ǲ�
      if (!"".equals(stringVoucherYMD)) {
        messagebox("[�~��-�дڥӽЮ�] �w��ǲ��C");
        if (!booleanSYS) return "ERR";
      }
      if (exeUtil.doParseDouble(stringVoucherFlowNo) > 0) {
        messagebox("[�~��-�дڥӽЮ�] �w��ǲ��C");
        if (!booleanSYS) return "ERR";
      }
      if ("Z".equals(stringStatusCd)) {
        messagebox("[�~��-�дڥӽЮ�] �w��ǲ��C");
        if (!booleanSYS) return "ERR";
      }
      // ���b�O�дڳ椤�ϥ�
      hashtableAnd.put("SYSTEM_CD", stringSystemCd);
      hashtableAnd.put("TEMP_KEY", stringTempKey);
      hashtableAnd.put("COMPANY_CD", stringComNo);
      vectorDoc2M0143 = exeFun.getQueryDataHashtableDoc("Doc2M0143", hashtableAnd, " AND BarCode  <>  '" + stringBarCode + "' ", new Vector(), exeUtil);
      if (vectorDoc2M0143.size() > 0) {
        for (int intNoL = 0; intNoL < vectorDoc2M0143.size(); intNoL++) {
          hashtableTmp = (Hashtable) vectorDoc2M0143.get(intNoL);
          if (hashtableTmp == null) continue;
          stringBarCodeL = "" + hashtableTmp.get("BarCode");
          if ("null".equals(stringBarCodeL)) continue;
          //
          if (stringTmp.indexOf(stringBarCodeL) != -1) continue;
          //
          if (!"".equals(stringTmp)) stringTmp += "�B";
          stringTmp += stringBarCodeL;
        }
        messagebox("[�~��ǲ�] �w�ϥΩ� �дڥӽЮ� " + stringTmp + " ���C");
        return "ERR";
      }
      // FED1040 �w��-�ǲ�
      // �s�b
      hashtableAnd.put("SYSTEM_CD", stringSystemCd);
      hashtableAnd.put("COMPANY_CD", stringComNo);
      hashtableAnd.put("TEMP_KEY", stringTempKey);
      vectorFED1040 = exeFun.getQueryDataHashtableFED1("FED1040", hashtableAnd, "", new Vector(), exeUtil);
      if (vectorFED1040.size() == 0) {
        if (!booleanSYS) messagebox("[�~��ǲ�] ���s�b �]�|��Ʈw���C");
        return "ERR";
      }
      // ����ǲ�
      for (int intNoL = 0; intNoL < vectorFED1040.size(); intNoL++) {
        hashtableTmp = (Hashtable) vectorFED1040.get(intNoL);
        if (hashtableTmp == null) continue;
        stringStatusCd = "" + hashtableTmp.get("STATUS_CD");
        if ("null".equals(stringStatusCd)) stringStatusCd = "";
        stringVoucherYMD = "" + hashtableTmp.get("VOUCHER_YMD");
        if ("null".equals(stringVoucherYMD)) stringVoucherYMD = "";
        stringVoucherFlowNo = "" + hashtableTmp.get("VOUCHER_FLOW_NO");
        if ("null".equals(stringVoucherFlowNo)) stringVoucherFlowNo = "";
        stringVoucherType = stringTempKey.endsWith("1") ? "�w��" : "���";
        if ("Z".equals(stringStatusCd)) {
          messagebox("[" + stringVoucherType + "-�~��ǲ�] �w��ǲ��C");
          if (!booleanSYS) return "ERR";
        }
        if (exeUtil.doParseDouble(stringVoucherYMD) > 0) {
          messagebox("[" + stringVoucherType + "-�~��ǲ�] �w��ǲ��C");
          if (!booleanSYS) return "ERR";
        }
        if (exeUtil.doParseDouble(stringVoucherFlowNo) > 0) {
          messagebox("[" + stringVoucherType + "-�~��ǲ�] �w��ǲ��C");
          if (!booleanSYS) return "ERR";
        }
      }
      String stringRectiptTypeS = "";
      if ("���".equals(stringVoucherType)) {
        arrayReceiptTax[0] = "" + getMoney("228202", "", vectorFED1040, exeUtil);
        if (exeUtil.doParseDouble(arrayReceiptTax[0]) <= 0) {
          arrayReceiptKind[0] = "B";
        }
        //
        stringReceiptTotalMoney = "" + getMoney("2251", "�~�����|", vectorFED1040, exeUtil);
        arrayReceiptTotalMoney[0] = convert.FourToFive(stringReceiptTotalMoney, 0);
        //
        stringTemp = "" + getMoney("2251", "�뭹�O", vectorFED1040, exeUtil);
        arrayReceiptTotalMoney[1] = convert.FourToFive(stringTemp, 0);
        //
        // FED1040 ���-�ǲ� �P �ӤH���ڪ�� ���B�@�P�ˮ�
        if (jtable3.getRowCount() != arrayReceiptKind.length) {
          messagebox("[�ӤH���ڪ��] ���ƿ��~�C");
          return "ERR%-%3";
        }
        for (int intNoL = 0; intNoL < jtable3.getRowCount(); intNoL++) {
          stringReceiptKind = ("" + getValueAt("Table3", intNoL, "ReceiptKind")).trim();
          stringReceiptTotalMoney = ("" + getValueAt("Table3", intNoL, "ReceiptTotalMoney")).trim();
          stringReceiptMoney = ("" + getValueAt("Table3", intNoL, "ReceiptMoney")).trim();
          stringReceiptTax = ("" + getValueAt("Table3", intNoL, "ReceiptTax")).trim();
          System.out.println(intNoL + "stringReceiptKind(" + stringReceiptKind + ")stringReceiptTotalMoney(" + stringReceiptTotalMoney + ")stringReceiptMoney(" + stringReceiptMoney
              + ")------------------------");
          //
          if (exeUtil.doParseDouble(stringReceiptTotalMoney) == 0) stringReceiptTotalMoney = stringReceiptMoney;
          //
          if (!stringReceiptKind.equals(arrayReceiptKind[intNoL])) {
            messagebox("[�ӤH���ڪ��] �� " + (intNoL + 1) + " �椧 [�榡] ���@�P�C");
            return "ERR%-%3";
          }
          if (exeUtil.doParseDouble(stringReceiptTotalMoney) != exeUtil.doParseDouble(arrayReceiptTotalMoney[intNoL])) {
            messagebox("[�ӤH���ڪ��] �� " + (intNoL + 1) + " �椧 [�ұo���B] ���@�P�C");
            return "ERR%-%3";
          }
          if (!"".equals(arrayReceiptTax[intNoL]) && exeUtil.doParseDouble(stringReceiptTax) != exeUtil.doParseDouble(arrayReceiptTax[intNoL])) {
            messagebox("[�ӤH���ڪ��] �� " + (intNoL + 1) + " �椧 [��ú���B] ���@�P�C");
            return "ERR%-%3";
          }

          doubleReceiptTotalMoney = exeUtil.add(doubleReceiptTotalMoney, exeUtil.doParseDouble(stringReceiptTotalMoney));
        }
      }
    }
    // FED1040 ���-�ǲ� �P �O�Ϊ�� ���B�@�P�ˮ�
    if (jtable2.getRowCount() == 0) {
      messagebox("�~��дڥӽЮ� [�O�Ϊ��] �̦h���\ 4 ���C");
      return "ERR%-%2";
    }
    if (jtable2.getRowCount() > 4) {
      messagebox("�~��дڥӽЮ� [�O�Ϊ��] �̦h���\ 4 ���C");
      return "ERR%-%2";
    }
    /*
     * stringTmp = (""+getValueAt("Table2", 0, "RealTotalMoney")).trim() ;
     * if(exeUtil.doParseDouble(stringTmp) != doubleReceiptTotalMoney) {
     * messagebox("[�O�Ϊ��] [���B] ���@�P�C") ; return "ERR%-%2" ; }
     */
    return "OK";
  }

  public double getMoney(String stringAcctNoCF, String stringDescrioptionCF, Vector vectorFED1040, FargloryUtil exeUtil) throws Throwable {
    String stringAcctNo = "";
    String stringAMT = "";
    String stringDescrioption = "";
    boolean booleanFlag = true;
    double doubleAMT = 0;
    Hashtable hashtableTmp = new Hashtable();
    for (int intNo = 0; intNo < vectorFED1040.size(); intNo++) {
      hashtableTmp = (Hashtable) vectorFED1040.get(intNo);
      if (hashtableTmp == null) continue;
      stringAcctNo = "" + hashtableTmp.get("ACCT_NO");
      if ("".equals(stringAcctNo)) stringAcctNo = "";
      stringAMT = "" + hashtableTmp.get("AMT");
      if ("".equals(stringAMT)) stringAMT = "";
      //
      if (!stringAcctNoCF.equals(stringAcctNo)) continue;
      //
      if (!"".equals(stringDescrioptionCF)) {
        booleanFlag = false;
        for (int intNoL = 1; intNoL <= 5; intNoL++) {
          stringDescrioption = "" + hashtableTmp.get("DESCRIPTION_" + intNoL);
          if ("".equals(stringDescrioption)) stringDescrioption = "";
          if (stringDescrioption.equals(stringDescrioptionCF)) {
            booleanFlag = true;
            break;
          }
        }
        if (!booleanFlag) continue;
      }
      //
      doubleAMT += exeUtil.doParseDouble(stringAMT);
    }
    return doubleAMT;
  }

  // 2013-08-28 B3018 �T���ˮ� START
  public boolean isAssetOK(String stringValue, FargloryUtil exeUtil, Doc2M010 exeFun) throws Throwable {
    getButton("ButtonTable9").doClick(); // �����дڳ椧[���ʩ��Ӷ���]�����B�M���|���B �X�p���@�P �O�Ϊ�椧���B�X�p�C(�۰ʳB�z)
    // AS_RECEIPT �P ���ʳ� ����
    // AS_ASSET �P ���ʸ�T ����
    JTable jtable6 = getTable("Table6");
    talk dbAsset = getTalk("" + get("put_Asset"));
    String stringAssetDate = "" + get("ASSET_DATE");
    String stringFlow = getFunctionName();
    String stringToday = exeUtil.getDateConvert(getValue("CDate").trim());
    String stringComNo = getValue("ComNo");
    String stringBarCode = getValue("BarCode");
    String stringBarCodeOld = getValue("BarCodeOld");
    String stringKindNo = getValue("KindNo");
    String stringDocNoType = getValue("DocNoType");
    String stringPurchaseNo = jtable6.getRowCount() == 0 ? "" : ("" + getValueAt("Table6", 0, "PurchaseNo")).trim();
    String stringFactoryNo = jtable6.getRowCount() == 0 ? "" : ("" + getValueAt("Table6", 0, "FactoryNo")).trim();
    String stringFiletrDo = "";
    boolean booleanAssetDate = false;
    Vector vectorDoc2M0401 = exeFun.getQueryDataHashtableDoc("Doc2M0401", new Hashtable(), " AND  UseType  =  'U' ", new Vector(), exeUtil);
    Hashtable hashtabelDoc2M0401 = (Hashtable) vectorDoc2M0401.get(0);
    // �S�� ���@�B�z
    if (",P38453,P20829,B67283,".indexOf("," + stringBarCode + ",") != -1) return true;
    //
    if (hashtabelDoc2M0401 == null) return false;
    stringFiletrDo = "" + hashtabelDoc2M0401.get("Remark");
    if ("".equals(stringFiletrDo)) return false;
    if ("null".equals(stringFiletrDo)) return false;
    //
    String stringPurchaseNo1 = "";
    String stringPurchaseNo2 = "";
    String stringPurchaseNo3 = "";
    String stringSql = "";
    String[][] retDoc2M017 = null;
    String[][] retDoc2M0171 = null;
    String[][] retDoc3M011 = null;
    String[][] retDoc3M012 = null;
    for (int intNo = 0; intNo < jtable6.getRowCount(); intNo++) {
      stringPurchaseNo1 = ("" + getValueAt("Table6", intNo, "PurchaseNo1")).trim();
      stringPurchaseNo2 = ("" + getValueAt("Table6", intNo, "PurchaseNo2")).trim();
      stringPurchaseNo3 = ("" + getValueAt("Table6", intNo, "PurchaseNo3")).trim();
      // �T��
      retDoc3M011 = exeFun.getDoc3M011(stringComNo, stringPurchaseNo1, stringPurchaseNo2, stringPurchaseNo3, " AND  ApplyType  =  'D' ");
      if (retDoc3M011.length == 0) continue;
      // �w�s�b
      stringSql = " SELECT  M17.BarCode " + " FROM  Doc2M0171 M17,  Doc2M010  M10 " + " WHERE  M17.BarCode  =  M10.BarCode " + " AND  M17.PurchaseNo  =  '" + stringPurchaseNo1
          + stringPurchaseNo2 + stringPurchaseNo3 + "' " + " AND  M10.ComNo  =  '" + stringComNo + "' ";
      retDoc2M0171 = exeFun.getTableDataDoc(stringSql);
      if (retDoc2M0171.length > 0) {
        booleanAssetDate = true;
        continue;
      }
      // ���йL��
      stringSql = " SELECT  M17.BarCode " + " FROM  Doc2M017 M17,  Doc2M010  M10 " + " WHERE  M17.BarCode  =  M10.BarCode " + " AND  M10.ComNo  =  '" + stringComNo + "' "
          + " AND  M17.PurchaseNo1  =  '" + stringPurchaseNo1 + "' " + " AND  M17.PurchaseNo2  =  '" + stringPurchaseNo2 + "' " + " AND  M17.PurchaseNo3  =  '" + stringPurchaseNo3
          + "' " + " AND  M10.BarCode  <>  '" + stringBarCodeOld + "' ";
      retDoc2M017 = exeFun.getTableDataDoc(stringSql);
      if (retDoc2M017.length > 0) continue;
      // �L�T��N�X
      stringSql = " SELECT  BarCode " + " FROM  Doc3M012 " + " WHERE  ISNULL(FILTER,'')  =  '' " + " AND  BarCode  =  '" + retDoc3M011[0][12].trim() + "' ";
      retDoc3M012 = exeFun.getTableDataDoc(stringSql);
      if (retDoc3M012.length > 0) {
        messagebox("[���ʳ�(" + stringPurchaseNo1 + "-" + stringPurchaseNo2 + "-" + stringPurchaseNo3 + ")] �� �T��N�X ��T�A�Ь���P�޲z�� �����w ���� 3633�C");
        return false;
      } else {
        booleanAssetDate = true;
      }
    }
    System.out.println("isAssetOK----------------------------------------------1");
    //
    if (!booleanAssetDate) return true;
    if (!exeFun.isApplyTypeDVoucher2("A", stringComNo, stringPurchaseNo, stringKindNo, stringFactoryNo)) return true;
    //
    if (getTableData("Table9").length == 0) {
      messagebox("�п�J ���ʩ��Ӹ�ơC\n(������ ���ʭ��Ҥ����ʪ��B)�A");
      return false;
    }
    // �Ȥ��\��@���ʳ�
    if (jtable6.getRowCount() > 1) {
      messagebox("�T�w�겣 �Ȥ��\��@���ʳ�C");
      return false;
    }
    if (!"A".equals(stringDocNoType)) {
      messagebox("[���ʳ�] ���T�w�겣�ɡA�����\ [�v��}�o��] ��...�S��y�{�C");
      return false;
    }
    if (getTableData("Table3").length > 0) {
      messagebox("[���ʳ�] ���T�w�겣�ɡA���� �u���\ [�o��]�C");
      return false;
    }
    String[][] retAsAsset = getAsAsset(exeUtil, dbAsset, exeFun);
    boolean booleanExistAsset = (retAsAsset.length > 0);
    //
    System.out.println("isAssetOK----------------------------------------------2");
    if (booleanExistAsset) {
      // �s�b�� �T�w�겣��
      if ("�s�W".equals(stringValue)) {
        messagebox("[���ʳ�-�t��] �w�s�b �T��t�ήɡA�����\�s�W �дڥӽЮѡC");
        return false;
      }
      if (stringFlow.indexOf("ñ��") != -1) {
        if (isLastDoc(exeFun)) {
          if (isFrontExistDoc(exeFun)) {
            messagebox("�����дڳ� ���̫�@���A�Х����� �e���дڳ� ñ�֡C");
            return false;
          }
          // �ˮ֩T��t�λP�дڨt�Ϊ��B�@�P�ˮ�(���Ƶ�4)
          if (!isSameAsAsset(stringFiletrDo, retAsAsset, exeFun, exeUtil)) {
            // ���@�P�ɡA�����\�]�ȼf�֡A�Ѱ]�ȫǯ�q���H�`�ǡA�ѤH�`�ǭץ��T��t�Ϊ��B�C
            return false;
          }
          // �ߨR�ˮ�
          System.out.println("isAssetOK----------------------------------------------3");
          if (!isVoucherCheckOK(stringBarCode, exeUtil, exeFun)) {
            return false;
          }
          System.out.println("isAssetOK----------------------------------------------4");
          retAsAsset = exeFun.getAsAssetUnit2("A", stringComNo, stringKindNo, stringPurchaseNo, stringFactoryNo, exeUtil, dbAsset);
        } else {
          retAsAsset = new String[0][0];
        }
      } else {
        retAsAsset = new String[0][0];
      }
      System.out.println("isAssetOK----------------------------------------------5");
    } else {
      // ���s�b�� �T�w�겣 ��
      if (("�s�W".equals(stringValue) || isLastDoc(exeFun)) && isEndDoc(exeFun, exeUtil)) {
        if (stringFlow.indexOf("�~��") != -1 && stringFlow.indexOf("--�ӿ�") != -1) {
          // �~�ޮɡA�̫�@���A�q���~�ޮѭ��дڳ涷�e�ܤH�`��
          put("Doc2I049_MESSAGE", "�����ʳ欰�T�w�겣�A\n�B�w���秹���A\n�бN�ѭ���ưe�ܤH�`�ǡC");
          showDialog("�q��(Doc2I049)", "", false, true, 70, 120, 530, 350);
        }
        // �̫�@���ɡA���s�b�T�ꤤ
        if (stringFlow.indexOf("ñ��") != -1) {
          messagebox("�����дڳ� ���̫�@���A�ФH�`�� �T��t�� �����T����ɰʧ@�C");
          return false;
        }
      }
      retAsAsset = getAsAssetDoc(exeUtil, exeFun);
    }
    String stringAssAccountAsset = "";
    String stringFilter = "";
    String stringComNoAcctNo = "";
    String stringInOut = "";
    String stringTemp = "";
    Vector vectorColumnName = new Vector();
    Vector vectorAsAssetFilter = new Vector();
    Hashtable hashtableTmp = new Hashtable();
    // 0 �C�b Y 1 �T��N�X 2. �X�p���B 3. ���|���B 4 ���~�~
    System.out.println("isAssetOK----------------------------------------------3");
    for (int intNo = 0; intNo < retAsAsset.length; intNo++) {
      stringAssAccountAsset = retAsAsset[intNo][0].trim();
      stringFilter = retAsAsset[intNo][1].trim();
      stringInOut = retAsAsset[intNo][4].trim();
      // [�T��N�X] ���� ���q-�|�p��ئs�b�ˮ֡C
      vectorAsAssetFilter = exeUtil.getQueryDataHashtable("AS_ASSET_FILTER", new Hashtable(), " AND  FILTER  = '" + stringFilter + "' ", vectorColumnName, dbAsset);
      if (vectorAsAssetFilter.size() == 0) {
        messagebox("���ʳ�(" + stringPurchaseNo + ")��[�T��N�X](" + stringFilter + ") ���s�b��Ʈw���C");
        return false;
      }
      hashtableTmp = (Hashtable) vectorAsAssetFilter.get(0);
      if (hashtableTmp == null) {
        messagebox("���ʳ�(" + stringPurchaseNo + ")��[�T��N�X](" + stringFilter + ") ��Ƶo�Ϳ��~�A�Ь���T�ǡC");
        return false;
      }
      if (!stringFilter.equals(stringFiletrDo)) {
        if ("Y".equals(stringAssAccountAsset)) {
          // �|�p��ئs�b�ˮ�
          stringComNoAcctNo = "" + hashtableTmp.get("ANMAL_ACNTNO_SET");
          if ("null".equals(stringComNoAcctNo) || "".equals(stringComNoAcctNo)) {
            messagebox("���ʳ�(" + stringPurchaseNo + ")��[�T��N�X](" + stringFilter + ") ���� [�C�b-�|�p���] ���ťաC");
            return false;
          }
        } else {
          // �������q�������ˮ�
          stringTemp = "SPEC_ACNTNO_SET_" + stringComNo;
          if ("I".equals(stringInOut)) stringTemp = "SPEC_ACNTNO_SET_" + stringComNo + "_IN";
          if (vectorColumnName.indexOf(stringTemp) == -1) {
            messagebox("���ʳ�(" + stringPurchaseNo + ")��[�T��N�X](" + stringFilter + ") ���s�b���� [���q-�|�p���] ���C");
            return false;
          }
          // �|�p��ئs�b�ˮ�
          stringComNoAcctNo = "" + hashtableTmp.get(stringTemp);
          if ("null".equals(stringComNoAcctNo) || "".equals(stringComNoAcctNo)) {
            messagebox("���ʳ�(" + stringPurchaseNo + ")��[�T��N�X](" + stringFilter + ") ���� [���q-�|�p���] ���ťաC");
            return false;
          }
        }
      }
    }
    return true;
  }

  public String[][] getAsAsset(FargloryUtil exeUtil, talk dbAsset, Doc2M010 exeFun) throws Throwable {
    String stringComNo = getValue("ComNo").trim();
    String stringKindNo = getValue("KindNo").trim();
    String stringPurchaseNo = ("" + getValueAt("Table6", 0, "PurchaseNo")).trim();
    String stringFactoryNo = ("" + getValueAt("Table6", 0, "FactoryNo")).trim();
    return exeFun.getAsAssetUnit(stringComNo, stringKindNo, stringPurchaseNo, stringFactoryNo, exeUtil, dbAsset);
  }

  public String[][] getAsAssetDoc(FargloryUtil exeUtil, Doc2M010 exeFun) throws Throwable {
    JTable jtable9 = getTable("Table9");
    String stringRecordNo12 = "";
    String stringPurchaseNo = "";
    String stringSql = "";
    String stringComNo = getValue("ComNo").trim();
    String stringKindNo = getValue("KindNo").trim();
    String stringKindNoPurchase = stringKindNo;
    String stringFilter = "";
    String stringInOut = "";
    String[] arrayTemp = null;
    String[][] retAsAsset = new String[0][0];
    String[][] retDoc3M012 = null;
    Vector vectorKEY = new Vector();
    Vector vectorTemp = new Vector();
    //
    if ("23".equals(stringKindNoPurchase)) stringKindNoPurchase = "15";
    if ("24".equals(stringKindNoPurchase)) stringKindNoPurchase = "17";
    for (int intNo = 0; intNo < jtable9.getRowCount(); intNo++) {
      stringRecordNo12 = ("" + getValueAt("Table9", intNo, "RecordNo12")).trim();
      stringPurchaseNo = ("" + getValueAt("Table9", intNo, "PurchaseNo")).trim();
      // ���o ���ʤ��T��N�X
      stringSql = " SELECT  M12.FILTER " + " FROM  Doc3M012 M12,  Doc3M011 M11 " + " WHERE  M12.BarCode  =  M11.BarCode " + " AND  M11.ComNo  =  '" + stringComNo + "' "
          + " AND  M11.DocNo  =  '" + stringPurchaseNo + "' " + " AND  M11.KindNo  =  '" + stringKindNoPurchase + "' " + " AND  M12.RecordNo  =  " + stringRecordNo12 + " ";
      retDoc3M012 = exeFun.getTableDataDoc(stringSql);
      if (retDoc3M012.length == 0) {
        System.out.println("�ˬd ���ʳ椧�T��N�X �ɡA�o�Ϳ��~�A�Ь���T�ǡC");
        return new String[0][0];
      }
      stringFilter = retDoc3M012[0][0].trim();
      stringInOut = exeFun.getInOutVoucher("A", stringRecordNo12, stringComNo, stringKindNoPurchase, stringPurchaseNo, exeUtil);
      if ("".equals(stringFilter)) continue;
      if (vectorKEY.indexOf(stringPurchaseNo + "---" + stringFilter + "---" + stringInOut) != -1) continue;
      vectorKEY.add(stringPurchaseNo + "---" + stringFilter + "---" + stringInOut);
      // 0 �C�b Y 1 �T��N�X 2. �X�p���B 3. ���|���B 4 ���~�~
      arrayTemp = new String[5];
      arrayTemp[0] = "N";
      arrayTemp[1] = stringFilter;
      arrayTemp[2] = "";
      arrayTemp[3] = "";
      arrayTemp[4] = stringInOut;
      vectorTemp.add(arrayTemp);
    }
    retAsAsset = (String[][]) vectorTemp.toArray(new String[0][0]);
    return retAsAsset;
  }

  public boolean isEndDoc(Doc2M010 exeFun, FargloryUtil exeUtil) throws Throwable {
    JTable jtable9 = getTable("Table9");
    String stringFactoryNo = "";
    String stringPurchaseNo = "";
    String stringComNo = getValue("ComNo").trim();
    String stringKindNo = getValue("KindNo").trim();
    String stringKindNoPurchase = stringKindNo;
    String stringBarCode = getValue("BarCode").trim();
    String stringBarCodePurchase = "";
    String stringSql = "";
    String stringRecordNo = "";
    String stringUnit = "";
    String stringActualNum = "";
    String stringRequestNum = "";
    String stringPurchaseMoney = "";
    String stringRecordNo12 = "";
    String[][] retDoc2M010 = null;
    String[][] retDoc2M017 = null;
    String[][] retDoc3M012 = null;
    String[][] retDoc3M013 = null;
    String[][] retDoc3M014 = null;
    String[][] retDoc6M010 = null;
    String[][] retDoc6M012 = null;
    double doublePurchase = 0;
    double doublePurchaseNum = 0;
    double doubleRequest = 0;
    double doubleRequestNum = 0;
    boolean booleanEND = true;
    // �O�_�w���� ���� �P�_
    if ("23".equals(stringKindNoPurchase)) stringKindNoPurchase = "15";
    if ("24".equals(stringKindNoPurchase)) stringKindNoPurchase = "17";
    if ("15".equals(stringKindNo)) stringKindNo = "23";
    if ("17".equals(stringKindNo)) stringKindNo = "24";
    //
    stringFactoryNo = ("" + getValueAt("Table6", 0, "FactoryNo")).trim();
    stringPurchaseNo = ("" + getValueAt("Table6", 0, "PurchaseNo")).trim();
    // �涵�ˮ�
    stringSql = "SELECT  M12.BarCode,  M12.RecordNo,  M12.Unit,   M12.ActualNum,  M12.PurchaseMoney " + " FROM  Doc3M011 M11,  Doc3M012 M12 "
        + " WHERE  M11.BarCode    =  M12.BarCode " + " AND  M11.ComNo  =  '" + stringComNo + "' " + " AND  M11.DocNo  =  '" + stringPurchaseNo + "' " + " AND  M11.KindNo  =  '"
        + stringKindNoPurchase + "' " + " AND  M12.FactoryNo  =  '" + stringFactoryNo + "' ";
    retDoc3M012 = exeFun.getTableDataDoc(stringSql);
    booleanEND = true;
    for (int intNoL = 0; intNoL < retDoc3M012.length; intNoL++) {
      stringBarCodePurchase = retDoc3M012[intNoL][0].trim();
      stringRecordNo = retDoc3M012[intNoL][1].trim();
      stringUnit = retDoc3M012[intNoL][2].trim();
      stringActualNum = retDoc3M012[intNoL][3].trim();
      doublePurchase = exeUtil.doParseDouble(convert.FourToFive(retDoc3M012[intNoL][4], 0));
      doublePurchaseNum = exeUtil.doParseDouble(convert.FourToFive(stringActualNum, 4));
      // ���B �����ˮ�
      // �дڪ��B-�д�
      stringSql = "SELECT  SUM(M17.PurchaseMoney),  SUM(M17.RequestNum) " + " FROM  Doc2M010 M10,  Doc2M0171 M17 " + " WHERE  M10.BarCode    =  M17.BarCode "
          + " AND  M10.BarCode  <>  '" + stringBarCode + "' " + " AND  M10.ComNo  =  '" + stringComNo + "' " + " AND  M10.KindNo  =  '" + stringKindNo + "' "
          + " AND  M17.PurchaseNo  =  '" + stringPurchaseNo + "' " + " AND  M17.FactoryNo  =  '" + stringFactoryNo + "' " + " AND  M17.RecordNo12  =  " + stringRecordNo + "  ";
      retDoc2M010 = exeFun.getTableDataDoc(stringSql);
      doubleRequest = exeUtil.doParseDouble(convert.FourToFive(retDoc2M010[0][0], 0));
      doubleRequestNum = exeUtil.doParseDouble(convert.FourToFive(retDoc2M010[0][1], 4));
      // �дڪ��B-�ɴڨR�P
      stringSql = "SELECT  SUM(M17.PurchaseMoney),  SUM(M17.RequestNum) " + " FROM  Doc6M010 M10,  Doc6M0171 M17 " + " WHERE  M10.BarCode    =  M17.BarCode "
          + " AND  M10.BarCode  <>  '" + stringBarCode + "' " + " AND  M10.ComNo  =  '" + stringComNo + "' " + " AND  M10.KindNo  =  '" + stringKindNo + "' "
          + " AND  M17.PurchaseNo  =  '" + stringPurchaseNo + "' " + " AND  M17.FactoryNo  =  '" + stringFactoryNo + "' " + " AND  M17.RecordNo12  =  " + stringRecordNo + "  ";
      retDoc6M010 = exeFun.getTableDataDoc(stringSql);
      doubleRequest += exeUtil.doParseDouble(convert.FourToFive(retDoc6M010[0][0], 0));
      doubleRequestNum += exeUtil.doParseDouble(convert.FourToFive(retDoc6M010[0][1], 4));
      // ����
      for (int intL = 0; intL < jtable9.getRowCount(); intL++) {
        stringRecordNo12 = ("" + getValueAt("Table9", intL, "RecordNo12")).trim();
        stringRequestNum = ("" + getValueAt("Table9", intL, "RequestNum")).trim();
        stringPurchaseMoney = ("" + getValueAt("Table9", intL, "PurchaseMoney")).trim();
        //
        if (stringRecordNo.equals(stringRecordNo12)) {
          doubleRequest += exeUtil.doParseDouble(convert.FourToFive(stringPurchaseMoney, 0));
          doubleRequestNum += exeUtil.doParseDouble(convert.FourToFive(stringRequestNum, 4));
        }
      }
      // ���B �����ˮ�
      if (doublePurchase != doubleRequest) {
        if ("��".equals(stringUnit)) {
          booleanEND = false;
          break;
        }
        // �ƶq�ˮ�
        if (doublePurchaseNum != doubleRequestNum) {
          booleanEND = false;
          break;
        }
      }
    }
    if (booleanEND) return true;
    // ���B ���秹�� �ˮ�
    // ���ʪ��B
    stringSql = "SELECT  DISTINCT  M13.BarCode,  M13.FactoryNo " + " FROM  Doc3M011 M11,  Doc3M013 M13 " + " WHERE  M11.BarCode    =  M13.BarCode " + " AND  M11.ComNo  =  '"
        + stringComNo + "' " + " AND  M11.DocNo  =  '" + stringPurchaseNo + "' " + " AND  M11.KindNo  =  '" + stringKindNoPurchase + "' ";
    retDoc3M013 = exeFun.getTableDataDoc(stringSql);
    if (retDoc3M013.length == 1) {
      // ��@�t��
      stringSql = "SELECT  SUM(RealMoney - NoUseRealMoney) " + " FROM  Doc3M014 " + " WHERE  BarCode  =  '" + retDoc3M013[0][0].trim() + "' ";
      retDoc3M014 = exeFun.getTableDataDoc(stringSql);
      doublePurchase = exeUtil.doParseDouble(convert.FourToFive(retDoc3M014[0][0], 0));
    } else {
      // �h�t��
      stringSql = "SELECT  PurchaseSumMoney-NoUseRealMoney " + " FROM  Doc3M013 " + " WHERE  BarCode  =  '" + retDoc3M013[0][0].trim() + "' " + " AND  FactoryNo  =  '"
          + stringFactoryNo + "' " + " ORDER BY  NoUseRealMoney DESC ";
      retDoc3M014 = exeFun.getTableDataDoc(stringSql);
      doublePurchase = exeUtil.doParseDouble(convert.FourToFive(retDoc3M014[0][0], 0));
    }
    // �дڪ��B-�дڳ�
    stringSql = "SELECT  SUM(M17.PurchaseMoney) " + " FROM  Doc2M010 M10,  Doc2M017 M17 " + " WHERE  M10.BarCode    =  M17.BarCode " + " AND  M10.UNDERGO_WRITE  <>  'E' "
        + " AND  M10.ComNo  =  '" + stringComNo + "' " + " AND  M10.KindNo  =  '" + stringKindNo + "' " + " AND  M17.PurchaseNo  =  '" + stringPurchaseNo + "' "
        + " AND  M17.FactoryNo  =  '" + stringFactoryNo + "' ";
    retDoc2M017 = exeFun.getTableDataDoc(stringSql);
    doubleRequest = exeUtil.doParseDouble(convert.FourToFive(retDoc2M017[0][0], 0));
    // �дڪ��B-�ɴڨR�P
    stringSql = "SELECT  SUM(M12.RealTotalMoney) " + " FROM  Doc6M010 M10,  Doc6M012 M12 " + " WHERE  M10.BarCode    =  M12.BarCode " + " AND  M10.UNDERGO_WRITE  <>  'E' "
        + " AND  M10.ComNo  =  '" + stringComNo + "' " + " AND  M10.KindNo  =  '" + stringKindNo + "' " + " AND  M10.PurchaseNo  =  '" + stringPurchaseNo + "' "
        + " AND  M10.FactoryNo  =  '" + stringFactoryNo + "' ";
    retDoc6M012 = exeFun.getTableDataDoc(stringSql);
    doubleRequest += exeUtil.doParseDouble(convert.FourToFive(retDoc6M012[0][0], 0));
    // ����
    for (int intL = 0; intL < jtable9.getRowCount(); intL++) {
      stringPurchaseMoney = ("" + getValueAt("Table9", intL, "PurchaseMoney")).trim();
      //
      doubleRequest += exeUtil.doParseDouble(convert.FourToFive(stringPurchaseMoney, 0));
    }
    // ���B �����ˮ�
    if (doublePurchase == doubleRequest) return true;
    return false;
  }

  public boolean isSameAsAsset(String stringFiletrDo, String[][] retAsAsset, Doc2M010 exeFun, FargloryUtil exeUtil) throws Throwable {
    JTable jtable9 = getTable("Table9");
    String stringPurchaseNo = "";
    String stringFactoryNo = "";
    String stringTemp = "";
    String stringFilter = "";
    String stringPurchaseMoney = "";
    String stringPurchaseNoTaxMoney = "";
    String stringSql = "";
    String stringRecordNo12Sum = "";
    String stringComNo = getValue("ComNo").trim();
    String stringKindNo = getValue("KindNo").trim();
    String stringKindNoPurchase = stringKindNo;
    String stringBarCode = getValue("BarCode").trim();
    String stringPurchaseNoL = "";
    String stringRecordNo12L = "";
    String stringPurchaseMoneyL = "";
    String stringPurchaseNoTaxMoneyL = "";
    String[] arrayTemp = null;
    String[][] retTableData = null;
    String[][] retDoc3M012 = null;
    String[][] retDoc2M0171 = null;
    String[][] retDoc6M0171 = null;
    double doublePurchaseMoney = 0;
    double doublePurchaseMoneyDoc = 0;
    double doublePurchaseMoneySum = 0;
    double doublePurchaseMoneySumDoc = 0;
    double doublePurchaseNoTaxMoney = 0;
    double doublePurchaseNoTaxMoneyDoc = 0;
    double doublePurchaseNoTaxMoneySum = 0;
    double doublePurchaseNoTaxMoneySumDoc = 0;
    Hashtable hashtableRecordNo12 = new Hashtable();
    //
    if ("23".equals(stringKindNoPurchase)) stringKindNoPurchase = "15";
    if ("24".equals(stringKindNoPurchase)) stringKindNoPurchase = "17";
    if ("15".equals(stringKindNo)) stringKindNo = "23";
    if ("17".equals(stringKindNo)) stringKindNo = "24";
    //
    stringPurchaseNo = ("" + getValueAt("Table6", 0, "PurchaseNo")).trim();
    stringFactoryNo = ("" + getValueAt("Table6", 0, "FactoryNo")).trim();
    doublePurchaseNoTaxMoneySum = 0;
    doublePurchaseNoTaxMoneySumDoc = 0;
    // ���ʳ椧�T�궵��
    stringSql = " SELECT  M12.FILTER,  M12.RecordNo " + " FROM  Doc3M011 M11,  Doc3M012 M12 " + " WHERE  M11.BarCode  =  M12.BarCode " + " AND  M11.ComNo  =  '" + stringComNo
        + "' " + " AND  M11.DocNo  =  '" + stringPurchaseNo + "' " + " AND  M11.KindNo  =  '" + stringKindNoPurchase + "' " + " AND  M12.FactoryNo  =  '" + stringFactoryNo + "' ";
    retDoc3M012 = exeFun.getTableDataDoc(stringSql);
    for (int intL = 0; intL < retDoc3M012.length; intL++) {
      stringFilter = retDoc3M012[intL][0].trim();
      stringRecordNo12L = retDoc3M012[intL][1].trim();
      //
      if (stringFiletrDo.equals(stringFilter)) stringFilter = "OTHER";
      //
      stringRecordNo12Sum = "" + hashtableRecordNo12.get(stringFilter);
      if ("null".equals(stringRecordNo12Sum)) stringRecordNo12Sum = "";
      stringRecordNo12Sum += "," + stringRecordNo12L + ",";
      hashtableRecordNo12.put(stringFilter, stringRecordNo12Sum);
    }
    // �дڪ��B- �д�
    stringSql = "SELECT  M17.RecordNo12,  M17.PurchaseMoney,  M17.PurchaseNoTaxMoney " + " FROM  Doc2M0171 M17,  Doc2M010  M10 " + " WHERE  M17.BarCode  =  M10.BarCode "
        + " AND  M10.ComNo  =  '" + stringComNo + "' " + " AND  M17.PurchaseNo  =  '" + stringPurchaseNo + "' " + " AND  M10.KindNo  =  '" + stringKindNo + "' "
        + " AND  M17.BarCode  <>  '" + stringBarCode + "' " + " AND  M17.FactoryNo  =  '" + stringFactoryNo + "' ";
    retDoc2M0171 = exeFun.getTableDataDoc(stringSql);
    stringRecordNo12Sum = "" + hashtableRecordNo12.get("OTHER");
    if ("null".equals(stringRecordNo12Sum)) stringRecordNo12Sum = "";
    System.out.println("�[�u�O ��m(" + stringRecordNo12Sum + ")--------------------");
    for (int intNoL = 0; intNoL < retDoc2M0171.length; intNoL++) {
      stringRecordNo12L = retDoc2M0171[intNoL][0].trim();
      stringPurchaseMoney = retDoc2M0171[intNoL][1].trim();
      stringPurchaseNoTaxMoney = retDoc2M0171[intNoL][2].trim();
      //
      if (stringRecordNo12Sum.indexOf("," + stringRecordNo12L + ",") == -1) continue;
      //
      doublePurchaseMoneySumDoc += exeUtil.doParseDouble(stringPurchaseMoney);
      doublePurchaseNoTaxMoneySumDoc += exeUtil.doParseDouble(stringPurchaseNoTaxMoney);
      System.out.println("�д� �[�u�O doublePurchaseMoneySumDoc(" + doublePurchaseMoneySumDoc + ")------------------------");
    }
    // �дڪ��B- �ɴڨR�P
    stringSql = "SELECT  M17.RecordNo12,  M17.PurchaseMoney,  M17.PurchaseNoTaxMoney " + " FROM  Doc6M0171 M17,  Doc6M010  M10 " + " WHERE  M17.BarCode  =  M10.BarCode "
        + " AND  M10.ComNo  =  '" + stringComNo + "' " + " AND  M10.KindNo  =  '" + stringKindNo + "' " + " AND  M17.PurchaseNo  =  '" + stringPurchaseNo + "' "
        + " AND  M17.FactoryNo  =  '" + stringFactoryNo + "' ";
    retDoc6M0171 = exeFun.getTableDataDoc(stringSql);
    for (int intNoL = 0; intNoL < retDoc6M0171.length; intNoL++) {
      stringRecordNo12L = retDoc6M0171[intNoL][0].trim();
      stringPurchaseMoney = retDoc6M0171[intNoL][1].trim();
      stringPurchaseNoTaxMoney = retDoc6M0171[intNoL][2].trim();
      //
      if (stringRecordNo12Sum.indexOf("," + stringRecordNo12L + ",") == -1) continue;
      //
      doublePurchaseMoneySumDoc += exeUtil.doParseDouble(stringPurchaseMoney);
      doublePurchaseNoTaxMoneySumDoc += exeUtil.doParseDouble(stringPurchaseNoTaxMoney);
      System.out.println("�ɴڨR�P �[�u�O doublePurchaseMoneySumDoc(" + doublePurchaseMoneySumDoc + ")------------------------");
    }
    // ���B��z-�дڳ�-����
    for (int intNoL = 0; intNoL < jtable9.getRowCount(); intNoL++) {
      stringPurchaseNoL = ("" + getValueAt("Table9", intNoL, "PurchaseNo")).trim();
      stringRecordNo12L = ("" + getValueAt("Table9", intNoL, "RecordNo12")).trim();
      stringPurchaseMoneyL = ("" + getValueAt("Table9", intNoL, "PurchaseMoney")).trim();
      stringPurchaseNoTaxMoneyL = ("" + getValueAt("Table9", intNoL, "PurchaseNoTaxMoney")).trim();
      //
      System.out.println(intNoL + "-1�д�-���� �[�u�O stringPurchaseNoL(" + stringPurchaseNoL + ")stringPurchaseNo(" + stringPurchaseNo + ")------------------------");
      if (!stringPurchaseNoL.equals(stringPurchaseNo)) continue;
      System.out.println(intNoL + "-2�д�-���� �[�u�O stringRecordNo12L(" + stringRecordNo12L + ")stringRecordNo12Sum(" + stringRecordNo12Sum + ")("
          + (stringRecordNo12Sum.indexOf("," + stringRecordNo12L + ",") == -1) + ")------------------------");
      if (stringRecordNo12Sum.indexOf("," + stringRecordNo12L + ",") == -1) continue;
      //
      doublePurchaseMoneySumDoc += exeUtil.doParseDouble(stringPurchaseMoneyL);
      doublePurchaseNoTaxMoneySumDoc += exeUtil.doParseDouble(stringPurchaseNoTaxMoneyL);
      System.out.println(intNoL + "-3�д�-���� �[�u�O doublePurchaseMoneySumDoc(" + doublePurchaseMoneySumDoc + ")------------------------");
    }
    // 0 �C�b Y 1 �T��N�X 2. �X�p���B 3. ���|���B
    System.out.println("�T��N�X ���B�ˬd------------------------");
    for (int intNoL = 0; intNoL < retAsAsset.length; intNoL++) {
      stringFilter = retAsAsset[intNoL][1].trim();
      stringPurchaseMoney = retAsAsset[intNoL][2].trim();
      stringPurchaseNoTaxMoney = retAsAsset[intNoL][3].trim();
      doublePurchaseMoney = exeUtil.doParseDouble(stringPurchaseMoney);
      doublePurchaseNoTaxMoney = exeUtil.doParseDouble(stringPurchaseNoTaxMoney);
      doublePurchaseMoneyDoc = 0;
      doublePurchaseNoTaxMoneyDoc = 0;
      // ���B��z-�дڳ�-�дڳ�
      stringRecordNo12Sum = "" + hashtableRecordNo12.get(stringFilter);
      if ("null".equals(stringRecordNo12Sum)) stringRecordNo12Sum = "";
      System.out.println(intNoL + "���B��z-�дڳ�-�дڳ�------------------------");
      System.out.println("�T��N�X(" + stringFilter + ") ��m(" + stringRecordNo12Sum + ")--------------------");
      for (int intL = 0; intL < retDoc2M0171.length; intL++) {
        stringRecordNo12L = retDoc2M0171[intL][0].trim();
        stringPurchaseMoney = retDoc2M0171[intL][1].trim();
        stringPurchaseNoTaxMoney = retDoc2M0171[intL][2].trim();
        //
        if (stringRecordNo12Sum.indexOf("," + stringRecordNo12L + ",") == -1) continue;
        //
        doublePurchaseMoneyDoc += exeUtil.doParseDouble(stringPurchaseMoney);
        doublePurchaseNoTaxMoneyDoc += exeUtil.doParseDouble(stringPurchaseNoTaxMoney);
        //
        System.out.println(intL + "�T��(" + stringFilter + ")-�д�(" + stringRecordNo12L + ") doublePurchaseMoneyDoc(" + doublePurchaseMoneyDoc + ")------------------------1");
      }
      // ���B��z-�дڳ�-�ɴڨR�P
      for (int intL = 0; intL < retDoc6M0171.length; intL++) {
        stringRecordNo12L = retDoc6M0171[intL][0].trim();
        stringPurchaseMoney = retDoc6M0171[intL][1].trim();
        stringPurchaseNoTaxMoney = retDoc6M0171[intL][2].trim();
        //
        if (stringRecordNo12Sum.indexOf("," + stringRecordNo12L + ",") == -1) continue;
        //
        doublePurchaseMoneyDoc += exeUtil.doParseDouble(stringPurchaseMoney);
        doublePurchaseNoTaxMoneyDoc += exeUtil.doParseDouble(stringPurchaseNoTaxMoney);
        //
        System.out.println(intL + "�T��(" + stringFilter + ")-�ɴڨR�P(" + stringRecordNo12L + ") doublePurchaseMoneyDoc(" + doublePurchaseMoneyDoc + ")------------------------1");
      }
      // ���B��z-�дڳ�-����
      for (int intL = 0; intL < jtable9.getRowCount(); intL++) {
        stringPurchaseNoL = ("" + getValueAt("Table9", intL, "PurchaseNo")).trim();
        stringRecordNo12L = ("" + getValueAt("Table9", intL, "RecordNo12")).trim();
        stringPurchaseMoneyL = ("" + getValueAt("Table9", intL, "PurchaseMoney")).trim();
        stringPurchaseNoTaxMoneyL = ("" + getValueAt("Table9", intL, "PurchaseNoTaxMoney")).trim();
        //
        if (!stringPurchaseNoL.equals(stringPurchaseNo)) continue;
        if (stringRecordNo12Sum.indexOf("," + stringRecordNo12L + ",") == -1) continue;
        // �t�|
        doublePurchaseMoneyDoc += exeUtil.doParseDouble(stringPurchaseMoneyL);
        doublePurchaseNoTaxMoneyDoc += exeUtil.doParseDouble(stringPurchaseNoTaxMoneyL);
        //
        System.out.println(intL + "�T��(" + stringFilter + ")-�дڳ�-����(" + stringRecordNo12L + ")  doublePurchaseMoneyDoc(" + doublePurchaseMoneyDoc + ")------------------------1");
      }
      // �ˮ֦U�T��N�X�t�|���B�X�p�A�дڨt�Τ��o�j��T��t�ΡC
      doublePurchaseMoney = exeUtil.doParseDouble(convert.FourToFive("" + doublePurchaseMoney, 0));
      doublePurchaseMoneyDoc = exeUtil.doParseDouble(convert.FourToFive("" + doublePurchaseMoneyDoc, 0));
      if (doublePurchaseMoneyDoc > doublePurchaseMoney) {
        messagebox("[�T��t��](" + exeUtil.getFormatNum2("" + doublePurchaseMoney) + ") [�дڨt��](" + exeUtil.getFormatNum2("" + doublePurchaseMoneyDoc) + ")�� �T��N�X(" + stringFilter
            + ")�����t�|���B ���@�P�A�гq���H�`�ǭץ��T��t�Ϊ��B�C");
        return false;
      }
      // �ˮ֦U�T��N�X���|���B�X�p�A�дڨt�Τ��o�j��T��t�ΡC
      doublePurchaseNoTaxMoney = exeUtil.doParseDouble(convert.FourToFive("" + doublePurchaseNoTaxMoney, 0));
      doublePurchaseNoTaxMoneyDoc = exeUtil.doParseDouble(convert.FourToFive("" + doublePurchaseNoTaxMoneyDoc, 0));
      if (doublePurchaseNoTaxMoneyDoc > doublePurchaseNoTaxMoney) {
        messagebox("[�T��t��](" + exeUtil.getFormatNum2("" + doublePurchaseNoTaxMoney) + ") [�дڨt��](" + exeUtil.getFormatNum2("" + doublePurchaseNoTaxMoneyDoc) + ")�� �T��N�X("
            + stringFilter + ")�������|���B ���@�P�A�гq���H�`�ǭץ��T��t�Ϊ��B�C");
        return false;
      }
      // �X�p���B��z
      doublePurchaseMoneySum += doublePurchaseMoney;
      doublePurchaseMoneySumDoc += doublePurchaseMoneyDoc;
      doublePurchaseNoTaxMoneySum += doublePurchaseNoTaxMoney;
      doublePurchaseNoTaxMoneySumDoc += doublePurchaseNoTaxMoneyDoc;
    }
    // �ˮ֧t�|���B�X�p�A�дڨt�ζ�����T��t�ΡC
    doublePurchaseMoneySum = exeUtil.doParseDouble(convert.FourToFive("" + doublePurchaseMoneySum, 0));
    doublePurchaseMoneySumDoc = exeUtil.doParseDouble(convert.FourToFive("" + doublePurchaseMoneySumDoc, 0));
    if (doublePurchaseMoneySum != doublePurchaseMoneySumDoc) {
      messagebox(
          "[�T��t��](" + exeUtil.getFormatNum2("" + doublePurchaseMoneySum) + ") [�дڨt��](" + exeUtil.getFormatNum2("" + doublePurchaseMoneySumDoc) + ")�� �t�|���B�X�p ���@�P�A�гq���H�`�ǭץ��T��t�Ϊ��B�C");
      return false;
    }
    // �ˮ֥��|���B�X�p�A�дڨt�ζ�����T��t�ΡC
    doublePurchaseNoTaxMoneySum = exeUtil.doParseDouble(convert.FourToFive("" + doublePurchaseNoTaxMoneySum, 0));
    doublePurchaseNoTaxMoneySumDoc = exeUtil.doParseDouble(convert.FourToFive("" + doublePurchaseNoTaxMoneySumDoc, 0));
    if (doublePurchaseNoTaxMoneySum != doublePurchaseNoTaxMoneySumDoc) {
      messagebox("[�T��t��](" + exeUtil.getFormatNum2("" + doublePurchaseNoTaxMoneySum) + ") [�дڨt��](" + exeUtil.getFormatNum2("" + doublePurchaseNoTaxMoneySumDoc)
          + ")�� ���|���B�X�p ���@�P�A�гq���H�`�ǭץ��T��t�Ϊ��B�C");
      return false;
    }
    return true;
  }

  public boolean isFrontExistDoc(Doc2M010 exeFun) throws Throwable {
    String stringBarCode = getValue("BarCode").trim();
    String stringComNo = getValue("ComNo").trim();
    String stringKindNo = getValue("KindNo").trim();
    String stringPurchaseNo = ("" + getValueAt("Table6", 0, "PurchaseNo")).trim();
    String stringFactoryNo = ("" + getValueAt("Table6", 0, "FactoryNo")).trim();
    return exeFun.isFrontExistDocVoucher(true, stringBarCode, stringComNo, stringKindNo, stringPurchaseNo, stringFactoryNo);
  }

  public boolean isLastDoc(Doc2M010 exeFun) throws Throwable {
    String stringBarCode = getValue("BarCode").trim();
    String stringComNo = getValue("ComNo").trim();
    String stringKindNo = getValue("KindNo").trim();
    String stringPurchaseNo = ("" + getValueAt("Table6", 0, "PurchaseNo")).trim();
    String stringFactoryNo = ("" + getValueAt("Table6", 0, "FactoryNo")).trim();
    return exeFun.isLastDocVoucher(true, stringBarCode, stringComNo, stringKindNo, stringPurchaseNo, stringFactoryNo);
  }

  public boolean isVoucherCheckOK(String stringBarCode, FargloryUtil exeUtil, Doc2M010 exeFun) throws Throwable {
    String stringComNo = getValue("ComNo").trim();
    String stringKindNo = getValue("KindNo").trim();
    String stringPurchaseNo = ("" + getValueAt("Table6", 0, "PurchaseNo")).trim();
    String stringFactoryNo = ("" + getValueAt("Table6", 0, "FactoryNo")).trim();
    //
    String stringStatus = exeFun.getVoucherCheckStatus(stringBarCode, stringComNo, stringPurchaseNo, stringKindNo, stringFactoryNo, exeUtil);
    //
    System.out.println("stringFactoryNo(" + stringFactoryNo + ")----------------------------");
    if ("OK".equals(stringStatus)) return true;
    //
    messagebox(stringStatus);
    return false;
  }

  //
  public String getInformation() {
    return "---------------�s�W���s�{��.preProcess()----------------";
  }
}
